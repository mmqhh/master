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
    Button  intosaolei;//进入扫雷界面的按钮
    Button  controlmusic;//控制音乐的按钮
    Button  getleinum;//获取雷数的按钮
    Button  paihangbang;//显示排行榜的按钮
    Button   get_wid;

    EditText wid;
    int block_wid;
    int    block_width=65;

    private Intent intent = new Intent("com.example.saoleisheji.Music");
    boolean    state=false;//控制音乐的播放状态
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
			CharSequence edit_wid =wid.getText(); //获取格子长度
			if(TextUtils.isEmpty(edit_wid)==false){
			block_width = Integer.parseInt(String.valueOf(edit_wid));//将类型为CharSequence的格子长度转变成int型
			block_width_edtior.putInt("block_width",block_width);//存入格子长度
			block_width_edtior.commit();
			}
			else{
				Builder dialog=new AlertDialog.Builder(MainActivity.this);
				dialog.setMessage("格子长度设置错误,请重新输入");
				dialog.setNegativeButton("返回",new exitClick());
				dialog.create();
				dialog.show();
			}

		}
	}
	  class exitClick implements DialogInterface.OnClickListener //退出对话框
	  {
	      	  public void onClick(DialogInterface dialog,int which)
	  {
	  	dialog.cancel();
	  }
	  }
	class sClick implements OnClickListener //控制背景音乐的播放
	{ 
		public void onClick(View v)
		{

		//改变音乐播放状态，state=0则停止播放音乐，state=1则播放音乐
		if(state==false){stopService(intent); state=true;return;}
		if(state==true){startService(intent); state=false;return;}
	     }
		
	}

	class intoClick implements OnClickListener //进入扫雷游戏界面
	{ 
		public void onClick(View v)
		{//进入扫雷游戏的界面
			Intent in=new Intent(MainActivity.this,saoleijiemian.class);
			startActivity(in);
		}
	}
	class pClick implements OnClickListener //进入排行榜
	{ 
		public void onClick(View v)
		{
			show_pai_hang();
		}
	}
	public void show_pai_hang() //显示排行榜
	{
   
		pre=getSharedPreferences("paihang",Activity.MODE_PRIVATE);
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
						MainActivity.this);
				myDialog.setTitle("排行榜");
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
			MainActivity.this);
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
	 			MainActivity.this);
	        myDialog.setMessage("已清除");
	        myDialog.setNegativeButton("返回", null);
	        myDialog.show();
	}
	}
	}
	

}
