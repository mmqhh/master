package com.example.saoleisheji;








import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;




public class MainActivity extends Activity {
    saoleiView saoleiView;
    Button  intosaolei;//����ɨ�׽���İ�ť
    Button  controlmusic;//�������ֵİ�ť
    Button  getleinum;//��ȡ�����İ�ť
    Button  paihangbang;//��ʾ���а�İ�ť
    Button   get_wid;

    EditText wid;
    int block_wid;
    int    block_width=65;

    private Intent intent = new Intent("com.example.saoleisheji.Music");
    boolean    state=false;//�������ֵĲ���״̬
    SharedPreferences pre;
    SharedPreferences.Editor editor;
    SharedPreferences save_block_width;  
    SharedPreferences.Editor block_width_edtior;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		save_block_width=getSharedPreferences("block_width",Activity.MODE_PRIVATE);
		block_width_edtior=save_block_width.edit();

		startService(intent);
		intosaolei= (Button)findViewById(R.id.intosaolei);
		controlmusic=(Button)findViewById(R.id.music);
		paihangbang=(Button)findViewById(R.id.paihangbang);
		
		get_wid=(Button)findViewById(R.id.get_block_width);
		wid=(EditText)findViewById(R.id.edit_block_width);
		
        get_wid.setOnClickListener(new wClick());

		paihangbang.setOnClickListener(new pClick());
		controlmusic.setOnClickListener(new sClick());
		intosaolei.setOnClickListener(new intoClick());
	}
	class wClick implements OnClickListener
	{ 
		public void onClick(View v)
		{
			CharSequence edit_wid =wid.getText(); //��ȡ���ӳ���
			if(TextUtils.isEmpty(edit_wid)==false){
			block_width = Integer.parseInt(String.valueOf(edit_wid));//������ΪCharSequence�ĸ��ӳ���ת���int��
			block_width_edtior.putInt("block_width",block_width);//������ӳ���
			block_width_edtior.commit();
			}
			else{
				Builder dialog=new AlertDialog.Builder(MainActivity.this);
				dialog.setMessage("���ӳ������ô���,����������");
				dialog.setNegativeButton("����",new exitClick());
				dialog.create();
				dialog.show();
			}

		}
	}
	  class exitClick implements DialogInterface.OnClickListener //�˳��Ի���
	  {
	      	  public void onClick(DialogInterface dialog,int which)
	  {
	  	dialog.cancel();
	  }
	  }
	class sClick implements OnClickListener //���Ʊ������ֵĲ���
	{ 
		public void onClick(View v)
		{

		//�ı����ֲ���״̬��state=0��ֹͣ�������֣�state=1�򲥷�����
		if(state==false){stopService(intent); state=true;return;}
		if(state==true){startService(intent); state=false;return;}
	     }
		
	}

	class intoClick implements OnClickListener //����ɨ����Ϸ����
	{ 
		public void onClick(View v)
		{//����ɨ����Ϸ�Ľ���
			Intent in=new Intent(MainActivity.this,saoleijiemian.class);
			startActivity(in);
		}
	}
	class pClick implements OnClickListener //�������а�
	{ 
		public void onClick(View v)
		{
			show_pai_hang();
		}
	}
	public void show_pai_hang() //��ʾ���а�
	{
   
		pre=getSharedPreferences("paihang",Activity.MODE_PRIVATE);
		editor=pre.edit();
		//SharedPreferences�ļ�ֵ�б���ֵ�з������������
		   String[] score={"num1","num2","num3","num4","num5","num6","num7","num8","num9","num10"};
		   String[] player={"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
		   
		   String[] show =new String[10];//�����Ҫ��ʾ���ַ�������
		   //��ʱ�����ֵscore��player��ֵ
		   String[] top = new String[10];
		   String[] top_name=new String[10];
		   //��ȡ��ֵscore��player��ÿ����Ӧ��value
		      for (int i = 0; i < top.length; i++) {
					top[i] = String.valueOf(pre.getInt(score[i], 0));
					top_name[i]=pre.getString(player[i], "0");
				}
		      //�������а�ÿһ����Ҫ������ַ���
		      for (int i = 0; i < top.length; i++){ 
		    	  String c=String.valueOf(i+1);
		    	  show[i]=("��"+c+"λ"+" ����:"+top_name[i]+";��ʱ:"+top[i]+"��");
		      }
		      //�������а�ĶԻ���
				AlertDialog.Builder myDialog = new AlertDialog.Builder(
						MainActivity.this);
				myDialog.setTitle("���а�");
				myDialog.setPositiveButton("������а�", new cleanClick());
				myDialog.setNegativeButton("����", null);
				myDialog.setItems(show, null); // hangs tops
				myDialog.show();
				
	}
	class cleanClick implements DialogInterface.OnClickListener //������а�İ�ť�Ľӿ�
	{

	public void onClick(DialogInterface dialog,int which)
	{  
		    //�����Ƿ�������а����ʾ
		    AlertDialog.Builder myDialog = new AlertDialog.Builder(
			MainActivity.this);
	        myDialog.setTitle("�Ƿ�������а�");
	        myDialog.setPositiveButton("��", new confirmClick());
	        myDialog.setNegativeButton("����", null);
	        myDialog.show();

		
	}
	class confirmClick implements DialogInterface.OnClickListener //ȷ��������а��¼
	{

	public void onClick(DialogInterface dialog,int which)
	{    
	     editor.clear();//������а��key-value��
	     editor.commit();//�ύ�޸ķ���
	     //��ʾ�����
	     AlertDialog.Builder myDialog = new AlertDialog.Builder(
	 			MainActivity.this);
	        myDialog.setMessage("�����");
	        myDialog.setNegativeButton("����", null);
	        myDialog.show();
	}
	}
	}
	

}
