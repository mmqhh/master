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
	Button  simple_paihang;//进入初级排行榜界面的按钮
    Button  mid_paihang;//控制中级排行榜的按钮
    Button  hard_paihang;//控制高级排行榜的按钮
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
	class sClick implements OnClickListener //进入排行榜
	{ 
		public void onClick(View v)
		{
			show_pai_hang("paihang_simple","初级");
		}

		
	}
	class mClick implements OnClickListener //进入排行榜
	{ 
		public void onClick(View v)
		{
			show_pai_hang("paihang_mid","中级");
		}

		
	}
	class hClick implements OnClickListener //进入排行榜
	{ 
		public void onClick(View v)
		{
			show_pai_hang("paihang_hard","高级");
		}

		
	}
	public void show_pai_hang(String bang,String kind) {
		// TODO Auto-generated method stub
		pre=getSharedPreferences(bang,Activity.MODE_PRIVATE);
		editor=pre.edit();
		//SharedPreferences的键值列表，键值有分数和玩家两种
		   String[] score={"num1","num2","num3","num4","num5","num6","num7","num8","num9","num10"};
		   String[] player={"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
		   
		   String[] show =new String[10];//存放需要显示的字符串内容
		   //临时保存键值score和player的值
		   String[] top = new String[10];
		   String[] top_name=new String[10];
		   //获取键值score和player中每个对应的value
		      for (int i = 0; i < top.length; i++) {
					top[i] = String.valueOf(pre.getInt(score[i], 0));
					top_name[i]=pre.getString(player[i], "0");
				}
		      //设置排行榜每一行需要输出的字符串
		      for (int i = 0; i < top.length; i++){ 
		    	  String c=String.valueOf(i+1);
		    	  show[i]=("第"+c+"位"+" 姓名:"+top_name[i]+";用时:"+top[i]+"秒");
		      }
		      //弹出排行榜的对话框
				AlertDialog.Builder myDialog = new AlertDialog.Builder(
						select_paihang.this);
				myDialog.setTitle(kind+"排行榜");
				myDialog.setPositiveButton("清空排行榜", new cleanClick());
				myDialog.setNegativeButton("返回", null);
				myDialog.setItems(show, null); // hangs tops
				myDialog.show();
	}
	class cleanClick implements DialogInterface.OnClickListener //清除排行榜的按钮的接口
	{

	public void onClick(DialogInterface dialog,int which)
	{  
		    //弹出是否清除排行榜的提示
		    AlertDialog.Builder myDialog = new AlertDialog.Builder(
		    		select_paihang.this);
	        myDialog.setTitle("是否清空排行榜");
	        myDialog.setPositiveButton("是", new confirmClick());
	        myDialog.setNegativeButton("返回", null);
	        myDialog.show();

		
	}
	class confirmClick implements DialogInterface.OnClickListener //确认清除排行榜记录
	{

	public void onClick(DialogInterface dialog,int which)
	{    
	     editor.clear();//清楚排行榜的key-value对
	     editor.commit();//提交修改方法
	     //提示已清除
	     AlertDialog.Builder myDialog = new AlertDialog.Builder(
	    		 select_paihang.this);
	        myDialog.setMessage("已清除");
	        myDialog.setNegativeButton("返回", null);
	        myDialog.show();
	}
	}
	}
}
