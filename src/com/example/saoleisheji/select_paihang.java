package com.example.saoleisheji;

import com.example.saoleisheji.MainActivity.exitClick;
import com.example.saoleisheji.MainActivity.nClick;

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

public class select_paihang extends Activity{
	Button  simple_paihang;//����������а����İ�ť
    Button  mid_paihang;//�����м����а�İ�ť
    Button  hard_paihang;//���Ƹ߼����а�İ�ť
    SharedPreferences pre;
    SharedPreferences.Editor editor;

	protected void onCreate(Bundle savedInstanceState){ 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paihang);
		simple_paihang=(Button)findViewById(R.id.simple_paihang);
		mid_paihang=(Button)findViewById(R.id.mid_paihang);
		hard_paihang=(Button)findViewById(R.id.hard_paihang);
		simple_paihang.setOnClickListener(new sClick());
		mid_paihang.setOnClickListener(new mClick());		
		hard_paihang.setOnClickListener(new hClick());
	

	}
	class sClick implements OnClickListener //�������а�
	{ 
		public void onClick(View v)
		{
			show_pai_hang("paihang_simple","����");
		}

		
	}
	class mClick implements OnClickListener //�������а�
	{ 
		public void onClick(View v)
		{
			show_pai_hang("paihang_mid","�м�");
		}

		
	}
	class hClick implements OnClickListener //�������а�
	{ 
		public void onClick(View v)
		{
			show_pai_hang("paihang_hard","�߼�");
		}

		
	}
	public void show_pai_hang(String bang,String kind) {
		// TODO Auto-generated method stub
		pre=getSharedPreferences(bang,Activity.MODE_PRIVATE);
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
						select_paihang.this);
				myDialog.setTitle(kind+"���а�");
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
		    		select_paihang.this);
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
	    		 select_paihang.this);
	        myDialog.setMessage("�����");
	        myDialog.setNegativeButton("����", null);
	        myDialog.show();
	}
	}
	}
}
