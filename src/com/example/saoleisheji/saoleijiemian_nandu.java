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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class saoleijiemian_nandu extends Activity{
	
	ToggleButton toggle;//����״̬���ذ�ť
	saoleiView saoleiView = null;
    TextView jishi=null;
    TextView left_lei=null;

    Button startbtn;//��ʼ��ť
    EditText name;  //��¼��ʤ�������
    //�����Ѷ�
    Button  set_simple;
    Button  set_mid;
    Button  set_hard;
    
    
    SharedPreferences save_block_width;  
    SharedPreferences.Editor block_width_edtior;
    
    int  level=1;
    int  block_width=65;
    int   set_hang=10;
    int   set_lie=10;
    int     leishu=10;
    int    sign_num=0;
    int    shijian=0;//��¼��Ϸʱ��
    boolean starting=false;//��¼��Ϸ�Ƿ�ʼ
    SharedPreferences preferences_simple;
    SharedPreferences.Editor editor_simple;
    SharedPreferences preferences_mid;
    SharedPreferences.Editor editor_mid;
    SharedPreferences preferences_hard;
    SharedPreferences.Editor editor_hard;
    LinearLayout check;
    String playername;//��ʤ�������

    boolean count=true;//����ֹͣ��ʱ

	protected void onCreate(Bundle savedInstanceState) {   
		// TODO Auto-generated method stub   
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.saoleijiemian_nandu);
		//��ȡֻ�ܱ���Ӧ�ó����д��SharedPreferences����
		preferences_simple=getSharedPreferences("paihang_simple",Activity.MODE_PRIVATE);
		editor_simple=preferences_simple.edit();
		preferences_mid=getSharedPreferences("paihang_mid",Activity.MODE_PRIVATE);
		editor_mid=preferences_mid.edit();
		preferences_hard=getSharedPreferences("paihang_hard",Activity.MODE_PRIVATE);
		editor_hard=preferences_hard.edit();
		save_block_width=getSharedPreferences("block_width",Activity.MODE_PRIVATE);
		block_width_edtior=save_block_width.edit();
		
		saoleiView = (saoleiView) findViewById(R.id.saoleiView);
		
		block_width=save_block_width.getInt("block_width", 65);		
	    saoleiView.set_block_width(block_width);
	    
		toggle=(ToggleButton)findViewById(R.id.sign_change);
        startbtn=(Button)findViewById(R.id.start_game);
        
        set_simple=(Button)findViewById(R.id.simple);
        set_mid=(Button)findViewById(R.id.mid);
        set_hard=(Button)findViewById(R.id.hard);
				

        left_lei=(TextView)findViewById(R.id.left_lei_num);
        jishi=(TextView)findViewById(R.id.jishi);
        saoleiView.set_left_lei(left_lei);
		set_simple.setOnClickListener(new simple_Click());
		set_mid.setOnClickListener(new mid_Click());
		set_hard.setOnClickListener(new hard_Click());
		
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
					Builder dialog=new AlertDialog.Builder(saoleijiemian_nandu.this);
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

	class simple_Click implements OnClickListener   //ȷ�������С
	{ 
		public void onClick(View v)
		{
			leishu=10;
			level=1;
			saoleiView.set_Lei(leishu);
		      int  block_wid=save_block_width.getInt("block_width", 65);
	            saoleiView.set_block_width(block_wid);
				saoleiView.initSaoleiCanShu();//��ʼ��ɨ�׸�������
				saoleiView.invalidate();//����ɨ������
				shijian=0;//ʱ������
				saoleiView.set_all_sign_num(0);
				left_lei.setText(String.valueOf("ʣ������:"+leishu));
				jishi.setText("��ʼ��ʱ");
				count=true;//��ʼ��ʱ

		}
	}
	class mid_Click implements OnClickListener
	{ 
		public void onClick(View v)
		{
			leishu=20;
			level=2;
			saoleiView.set_Lei(leishu);
		      int  block_wid=save_block_width.getInt("block_width", 65);
	            saoleiView.set_block_width(block_wid);
				saoleiView.initSaoleiCanShu();//��ʼ��ɨ�׸�������
				saoleiView.invalidate();//����ɨ������
				shijian=0;//ʱ������
				saoleiView.set_all_sign_num(0);
				left_lei.setText(String.valueOf("ʣ������:"+leishu));
				jishi.setText("��ʼ��ʱ");
				count=true;//��ʼ��ʱ

		}
	}
	class hard_Click implements OnClickListener
	{ 
		public void onClick(View v)
		{
			leishu=30;
			level=3;
			saoleiView.set_Lei(leishu);
		      int  block_wid=save_block_width.getInt("block_width", 65);
	            saoleiView.set_block_width(block_wid);
				saoleiView.initSaoleiCanShu();//��ʼ��ɨ�׸�������
				saoleiView.invalidate();//����ɨ������
				shijian=0;//ʱ������
				saoleiView.set_all_sign_num(0);
				left_lei.setText(String.valueOf("ʣ������:"+leishu));
				jishi.setText("��ʼ��ʱ");
				count=true;//��ʼ��ʱ

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
			saoleiView.set_all_sign_num(0);
			left_lei.setText(String.valueOf("ʣ������:"+leishu));
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
		  //���а��SharedPreferences��key-value������
	    switch(level){
	    case 1:{paihang(preferences_simple,editor_simple);break;}
	    case 2:{paihang(preferences_mid,editor_mid);break;}
	    case 3:{paihang(preferences_hard,editor_hard);break;}
	    }
		//��ʾ���а�
	    switch(level){
	    case 1:{show_pai_hang(preferences_simple);break;}
	    case 2:{show_pai_hang(preferences_mid);break;}
	    case 3:{show_pai_hang(preferences_hard);break;}
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
public void paihang(SharedPreferences rank,SharedPreferences.Editor editor)
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
			top[i] = rank.getInt(score[i], 0);//û�иü�ֵ��Ӧ��value������0
			top_name[i]=rank.getString(player[i], "0");//û�иü�ֵ��Ӧ��value�������ַ�����0��
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
public void show_pai_hang(SharedPreferences rank)
{

	//SharedPreferences�ļ�ֵ�б���ֵ�з������������
	   String[] score={"num1","num2","num3","num4","num5","num6","num7","num8","num9","num10"};
	   String[] player={"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
	   String nan_du = null;

	   String[] show =new String[10];//�����Ҫ��ʾ���ַ�������
	   //��ʱ�����ֵscore��player��ֵ
	      String[]    top = new String[10];
	      String[]  top_name=new String[10];
		   //��ȡ��ֵscore��player��ÿ����Ӧ��value��û������0
	      for (int i = 0; i < top.length; i++) {
				top[i] = String.valueOf(rank.getInt(score[i], 0));
				top_name[i]=rank.getString(player[i], "0");
			}
	      for (int i = 0; i < top.length; i++){
		      //�������а�ÿһ����Ҫ������ַ���
	    	  String c=String.valueOf(i+1);
	    	  show[i]="��"+c+"λ"+" ����:"+top_name[i]+";��ʱ:"+top[i]+"��";
	      }
	      switch(level)
	      {
	      case 1:{nan_du="����";break;}
	      case 2:{nan_du="�м�";break;}
	      case 3:{nan_du="�߼�";break;}
	      }
	      //�������а�ĶԻ���
			AlertDialog.Builder myDialog = new AlertDialog.Builder(
					saoleijiemian_nandu.this);
			myDialog.setTitle(nan_du+"���а�");
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
		saoleijiemian_nandu.this);
     myDialog.setTitle("�Ƿ����");
     myDialog.setPositiveButton("��", new confirmClick());
     myDialog.setNegativeButton("����", null);
     myDialog.show();
     switch(level){
     case 1:{     
    	 editor_simple.clear();
    	 editor_simple.commit();
    	 break;}
     case 2:{     
    	 editor_mid.clear();
    	 editor_mid.commit();
    	 break;}
     case 3:{     
    	 editor_hard.clear();
    	 editor_hard.commit();
    	 break;}
     }
	
}
}
class confirmClick implements DialogInterface.OnClickListener
{

public void onClick(DialogInterface dialog,int which) //ȷ��������а��¼
{    
    switch(level){
    case 1:{     
   	 editor_simple.clear();//������а��key-value��
   	 editor_simple.commit();//�ύ�޸ķ���
   	 break;}
    case 2:{     
   	 editor_mid.clear();
   	 editor_mid.commit();
   	 break;}
    case 3:{     
   	 editor_hard.clear();
   	 editor_hard.commit();
   	 break;}
    }

     //��ʾ�����
     AlertDialog.Builder myDialog = new AlertDialog.Builder(
 			saoleijiemian_nandu.this);
        myDialog.setMessage("�����");
        myDialog.setNegativeButton("����", null);
        myDialog.show();
}
}
    public int set_lei_count()
       {return leishu;}
}
