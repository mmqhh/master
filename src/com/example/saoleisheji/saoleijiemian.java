package com.example.saoleisheji;



import java.util.Timer;
import java.util.TimerTask;





import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class saoleijiemian extends Activity {
	ToggleButton toggle;//����״̬���ذ�ť
	saoleiView saoleiView = null;
    TextView jishi=null;
    
    Button startbtn;//��ʼ��ť
    EditText name;  //��¼��ʤ�������
    
    Button  set_area;//����ɨ����������
    Button  set_leinum;//����ɨ�������׵�����
    
    EditText lie;//�༭����
    EditText hang;//�༭����
    EditText leinum;//�༭����
    
    SharedPreferences save_block_width;  
    SharedPreferences.Editor block_width_edtior;

    int  block_width=65;
    int   set_hang=10;
    int   set_lie=10;
    int     leishu=10;
    int    shijian=0;//��¼��Ϸʱ��
    boolean starting=false;//��¼��Ϸ�Ƿ�ʼ
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout check;
    String playername;//��ʤ�������

    boolean count=true;//����ֹͣ��ʱ

	protected void onCreate(Bundle savedInstanceState) {   
		// TODO Auto-generated method stub   
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.saoleijiemian);
		//��ȡֻ�ܱ���Ӧ�ó����д��SharedPreferences����
		preferences=getSharedPreferences("paihang",Activity.MODE_PRIVATE);
		editor=preferences.edit();
		save_block_width=getSharedPreferences("block_width",Activity.MODE_PRIVATE);
		block_width_edtior=save_block_width.edit();
		saoleiView = (saoleiView) findViewById(R.id.saoleiView);
		
		block_width=save_block_width.getInt("block_width", 65);		
	    saoleiView.set_block_width(block_width);
	    
		toggle=(ToggleButton)findViewById(R.id.sign_change);
        startbtn=(Button)findViewById(R.id.start_game);
        
		set_area=(Button)findViewById(R.id.set_button);
		lie=(EditText)findViewById(R.id.edit_lie);
		hang=(EditText)findViewById(R.id.edit_hang);
		
		set_leinum=(Button)findViewById(R.id.get_edit_count_button);
		leinum=(EditText)findViewById(R.id.edit_count);
		

		
        jishi=(TextView)findViewById(R.id.jishi);
        
		set_area.setOnClickListener(new aClick());
		set_leinum.setOnClickListener(new lClick());
        startbtn.setOnClickListener(new sClick());
        

		toggle.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{//�����Ƿ������
      public void onCheckedChanged(CompoundButton button,
             boolean isChecked) {
                     if(isChecked)saoleiView.setsign(true);
                           else saoleiView.setsign(false);
            }

        });
		
		//��ʱģ��
		final Handler myHandler=new Handler(){
			public void handleMessage(Message msg)
			{ 
				if(msg.what==0x11)
				{
					shijian++;
					jishi.setText("�ѽ��У�"+String.valueOf(shijian)+"��");
					if(shijian>65535)
					{
						shijian=0;
					}
				}
				else if((msg.what==0x13))//Ӯ�ˣ�������Ҽ�¼�Ի���
				{      
					Builder dialog=new AlertDialog.Builder(saoleijiemian.this);
					check=(LinearLayout)getLayoutInflater().inflate(R.layout.tishishuru,null);
					final EditText user=(EditText)findViewById(R.id.userEdit);
					dialog.setTitle("��ϲ��Ӯ��")
					       .setMessage("�������������")
					       .setView(check);
					dialog.setPositiveButton("ȷ��",new checkClick());
					dialog.setNegativeButton("ȡ��",new exitClick());
					dialog.create();
					dialog.show();
				}
			}
		};
		//����һ����ʱ�����ü�ʱ��ÿ��1sִ��ָ������
		new Timer().schedule(new TimerTask()
		{   
			@Override
			public void run()
			{   
				
				if(saoleiView.getstarting()==true)
				myHandler.sendEmptyMessage(0x11);
				if(saoleiView.getwin()==true&&count==true)
				{
					myHandler.sendEmptyMessage(0x13);//Ӯ�˷��͸���Ϣ
					count=false;
				}
			}
		},0,1000);
	

}

	class aClick implements OnClickListener   //ȷ�������С
	{ 
		public void onClick(View v)
		{
			CharSequence gethang =hang.getText(),getlie=lie.getText(); //��ȡ����
			//����TextUtils.isEmpty()�ж������Ƿ�Ϊnull�򡰡�,�ǡ�������true�����ǡ�������false
			if(TextUtils.isEmpty(gethang)==false&TextUtils.isEmpty(getlie)==false)
			{
			set_hang = Integer.parseInt(String.valueOf(gethang));//������ΪCharSequence������ת���int��
			set_lie  = Integer.parseInt(String.valueOf(getlie));//������ΪCharSequence������ת���int��
			}
			//ȷ�����������Ƿ���Ϲ���
			if(TextUtils.isEmpty(gethang)==false&TextUtils.isEmpty(getlie)==false&&set_hang*set_lie>=saoleiView.get_leinum())
				saoleiView.set_Area(set_hang, set_lie);
			else{
				//������ʾ����ʾ��������
				Builder dialog=new AlertDialog.Builder(saoleijiemian.this);
				dialog.setMessage("�����С���ô���,����������");
				dialog.setNegativeButton("����",new exitClick());
				dialog.create();
				dialog.show();
			}
		}
	}
	class lClick implements OnClickListener
	{ 
		public void onClick(View v)
		{
			CharSequence leicount =leinum.getText(); //��ȡ����
			if(TextUtils.isEmpty(leicount)==false)
			leishu = Integer.parseInt(String.valueOf(leicount));//������ΪCharSequence������ת���int��
			if(TextUtils.isEmpty(leicount)==false&&saoleiView.get_hang()*saoleiView.get_lie()>=leishu)
				saoleiView.set_Lei(leishu);
			else{
				Builder dialog=new AlertDialog.Builder(saoleijiemian.this);
				dialog.setMessage("�������ô���,����������");
				dialog.setNegativeButton("����",new exitClick());
				dialog.create();
				dialog.show();
			}

		}
	}


	class sClick implements OnClickListener //��ʼ��Ϸ
	{ 
		public void onClick(View v)
		{
      int  block_wid=save_block_width.getInt("block_width", 65);
            saoleiView.set_block_width(block_wid);
			saoleiView.initSaoleiCanShu();//��ʼ��ɨ�׸�������
			saoleiView.invalidate();//����ɨ������
			shijian=0;//ʱ������
			jishi.setText("��ʼ��ʱ");
			count=true;//��ʼ��ʱ
		}
	}
    class checkClick implements DialogInterface.OnClickListener
{
	
	public void onClick(DialogInterface dialog,int which)
	{		
		name=(EditText)check.findViewById(R.id.userEdit);
	    playername=name.getText().toString();
		paihang();//���а��SharedPreferences��key-value������
		show_pai_hang();//��ʾ���а�
		
	}
}
      class exitClick implements DialogInterface.OnClickListener //�˳��Ի���
{
    	  public void onClick(DialogInterface dialog,int which)
{
	dialog.cancel();
}
}
public void paihang()
{		//SharedPreferences�ļ�ֵ�б���ֵ�з������������
   String[] score={"num1","num2","num3","num4","num5","num6","num7","num8","num9","num10"};
   String[] player={"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
   //��ʱ�����ֵscore��player��ֵ
      int[]    top = new int[10];
      String[]  top_name=new String[10];
      
      int istant;//�м���������ڽ���shiijan��top[i]��ֵ
      String change;//�м���������ڽ���playername��top_name[i]��ֵ
		for (int i = 0; i < top.length; i++) {
			   //��ȡ��ֵscore��player��ÿ����Ӧ��value
			top[i] = preferences.getInt(score[i], 0);//û�иü�ֵ��Ӧ��value������0
			top_name[i]=preferences.getString(player[i], "0");//û�иü�ֵ��Ӧ��value�������ַ�����0��
		}
		//����������а�
		
		for (int i = 0; i < top.length; i++) {
			if (top[i] == 0 || top[i] > shijian) {
				istant = top[i];
				top[i] =  shijian;
				 shijian = istant;
				 change=top_name[i];
				 top_name[i]=playername;
				 playername=change;
				if (top[i]!= 0) {
					editor.putString(player[i],top_name[i]);
					editor.putInt(score[i], top[i]);
				}
			}
		}
		editor.commit();
}
public void show_pai_hang()
{

	//SharedPreferences�ļ�ֵ�б���ֵ�з������������
	   String[] score={"num1","num2","num3","num4","num5","num6","num7","num8","num9","num10"};
	   String[] player={"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
	   
	   String[] show =new String[10];//�����Ҫ��ʾ���ַ�������
	   //��ʱ�����ֵscore��player��ֵ
	      String[]    top = new String[10];
	      String[]  top_name=new String[10];
		   //��ȡ��ֵscore��player��ÿ����Ӧ��value��û������0
	      for (int i = 0; i < top.length; i++) {
				top[i] = String.valueOf(preferences.getInt(score[i], 0));
				top_name[i]=preferences.getString(player[i], "0");
			}
	      for (int i = 0; i < top.length; i++){
		      //�������а�ÿһ����Ҫ������ַ���
	    	  String c=String.valueOf(i+1);
	    	  show[i]="��"+c+"λ"+" ����:"+top_name[i]+";��ʱ:"+top[i]+"��";
	      }
	      //�������а�ĶԻ���
			AlertDialog.Builder myDialog = new AlertDialog.Builder(
					saoleijiemian.this);
			myDialog.setTitle("���а�");
			myDialog.setPositiveButton("������а�", new cleanClick());
			myDialog.setNegativeButton("����", null);
			myDialog.setItems(show, null); // hangs tops
			myDialog.show();
			
}
class cleanClick implements DialogInterface.OnClickListener
{

public void onClick(DialogInterface dialog,int which)
{		  		    //�����Ƿ�������а����ʾ
	AlertDialog.Builder myDialog = new AlertDialog.Builder(
		saoleijiemian.this);
     myDialog.setTitle("�Ƿ����");
     myDialog.setPositiveButton("��", new confirmClick());
     myDialog.setNegativeButton("����", null);
     myDialog.show();
     editor.clear();
     editor.commit();
	
}
}
class confirmClick implements DialogInterface.OnClickListener
{

public void onClick(DialogInterface dialog,int which) //ȷ��������а��¼
{    
     editor.clear();//������а��key-value��
     editor.commit();//�ύ�޸ķ���
     //��ʾ�����
     AlertDialog.Builder myDialog = new AlertDialog.Builder(
 			saoleijiemian.this);
        myDialog.setMessage("�����");
        myDialog.setNegativeButton("����", null);
        myDialog.show();
}
}
}

