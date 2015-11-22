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
	
	ToggleButton toggle;//旗子状态开关按钮
	saoleiView saoleiView = null;
    TextView jishi=null;
    TextView left_lei=null;

    Button startbtn;//开始按钮
    EditText name;  //记录获胜玩家名字
    //设置难度
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
    int    shijian=0;//记录游戏时间
    boolean starting=false;//记录游戏是否开始
    SharedPreferences preferences_simple;
    SharedPreferences.Editor editor_simple;
    SharedPreferences preferences_mid;
    SharedPreferences.Editor editor_mid;
    SharedPreferences preferences_hard;
    SharedPreferences.Editor editor_hard;
    LinearLayout check;
    String playername;//获胜玩家名字

    boolean count=true;//控制停止计时

	protected void onCreate(Bundle savedInstanceState) {   
		// TODO Auto-generated method stub   
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.saoleijiemian_nandu);
		//获取只能被本应用程序读写的SharedPreferences对象
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
		{//设置是否放旗子
      public void onCheckedChanged(CompoundButton button,
             boolean isChecked) {
                     if(isChecked)saoleiView.setsign(true);
                           else saoleiView.setsign(false);
            }

        });
		
		//计时模块
		final Handler myHandler=new Handler(){
			public void handleMessage(Message msg)
			{ 
				if(msg.what==0x11)
				{
					shijian++;
					jishi.setText("已进行："+String.valueOf(shijian)+"秒");
					if(shijian>65535)
					{
						shijian=0;
						
					}
				}
				else if((msg.what==0x13))//赢了，弹出玩家记录对话框
				{      
					Builder dialog=new AlertDialog.Builder(saoleijiemian_nandu.this);
					check=(LinearLayout)getLayoutInflater().inflate(R.layout.tishishuru,null);
					final EditText user=(EditText)findViewById(R.id.userEdit);
					dialog.setTitle("恭喜你赢了")
					       .setMessage("请输入玩家姓名")
					       .setView(check);
					dialog.setPositiveButton("确定",new checkClick());
					dialog.setNegativeButton("取消",new exitClick());
					dialog.create();
					dialog.show();
				}
			}
		};
		//定义一个计时器，让计时器每隔1s执行指定任务
		new Timer().schedule(new TimerTask()
		{   
			@Override
			public void run()
			{   
				
				if(saoleiView.getstarting()==true)
				myHandler.sendEmptyMessage(0x11);
				if(saoleiView.getwin()==true&&count==true)
				{
					myHandler.sendEmptyMessage(0x13);//赢了发送该消息
					count=false;
				}
			}
		},0,1000);
	

}

	class simple_Click implements OnClickListener   //确定区域大小
	{ 
		public void onClick(View v)
		{
			leishu=10;
			level=1;
			saoleiView.set_Lei(leishu);
		      int  block_wid=save_block_width.getInt("block_width", 65);
	            saoleiView.set_block_width(block_wid);
				saoleiView.initSaoleiCanShu();//初始化扫雷格子数组
				saoleiView.invalidate();//绘制扫雷区域
				shijian=0;//时间清零
				saoleiView.set_all_sign_num(0);
				left_lei.setText(String.valueOf("剩余雷数:"+leishu));
				jishi.setText("开始计时");
				count=true;//开始计时

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
				saoleiView.initSaoleiCanShu();//初始化扫雷格子数组
				saoleiView.invalidate();//绘制扫雷区域
				shijian=0;//时间清零
				saoleiView.set_all_sign_num(0);
				left_lei.setText(String.valueOf("剩余雷数:"+leishu));
				jishi.setText("开始计时");
				count=true;//开始计时

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
				saoleiView.initSaoleiCanShu();//初始化扫雷格子数组
				saoleiView.invalidate();//绘制扫雷区域
				shijian=0;//时间清零
				saoleiView.set_all_sign_num(0);
				left_lei.setText(String.valueOf("剩余雷数:"+leishu));
				jishi.setText("开始计时");
				count=true;//开始计时

		}
	}


	class sClick implements OnClickListener //开始游戏
	{ 
		public void onClick(View v)
		{
      int  block_wid=save_block_width.getInt("block_width", 65);
            saoleiView.set_block_width(block_wid);
			saoleiView.initSaoleiCanShu();//初始化扫雷格子数组
			saoleiView.invalidate();//绘制扫雷区域
			shijian=0;//时间清零
			saoleiView.set_all_sign_num(0);
			left_lei.setText(String.valueOf("剩余雷数:"+leishu));
			jishi.setText("开始计时");
			count=true;//开始计时
		}
	}
    class checkClick implements DialogInterface.OnClickListener
{
	
	public void onClick(DialogInterface dialog,int which)
	{		
		name=(EditText)check.findViewById(R.id.userEdit);
	    playername=name.getText().toString();
		  //排行榜的SharedPreferences的key-value对设置
	    switch(level){
	    case 1:{paihang(preferences_simple,editor_simple);break;}
	    case 2:{paihang(preferences_mid,editor_mid);break;}
	    case 3:{paihang(preferences_hard,editor_hard);break;}
	    }
		//显示排行榜
	    switch(level){
	    case 1:{show_pai_hang(preferences_simple);break;}
	    case 2:{show_pai_hang(preferences_mid);break;}
	    case 3:{show_pai_hang(preferences_hard);break;}
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
public void paihang(SharedPreferences rank,SharedPreferences.Editor editor)
{		//SharedPreferences的键值列表，键值有分数和玩家两种
   String[] score={"num1","num2","num3","num4","num5","num6","num7","num8","num9","num10"};
   String[] player={"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
   //临时保存键值score和player的值
      int[]    top = new int[10];
      String[]  top_name=new String[10];
      
      int istant;//中间变量，用于交换shiijan与top[i]的值
      String change;//中间变量，用于交换playername与top_name[i]的值
		for (int i = 0; i < top.length; i++) {
			   //获取键值score和player中每个对应的value
			top[i] = rank.getInt(score[i], 0);//没有该键值对应的value，返回0
			top_name[i]=rank.getString(player[i], "0");//没有该键值对应的value，返回字符串“0”
		}
		//排序更新排行榜
		
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

	//SharedPreferences的键值列表，键值有分数和玩家两种
	   String[] score={"num1","num2","num3","num4","num5","num6","num7","num8","num9","num10"};
	   String[] player={"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
	   String nan_du = null;

	   String[] show =new String[10];//存放需要显示的字符串内容
	   //临时保存键值score和player的值
	      String[]    top = new String[10];
	      String[]  top_name=new String[10];
		   //获取键值score和player中每个对应的value，没有则置0
	      for (int i = 0; i < top.length; i++) {
				top[i] = String.valueOf(rank.getInt(score[i], 0));
				top_name[i]=rank.getString(player[i], "0");
			}
	      for (int i = 0; i < top.length; i++){
		      //设置排行榜每一行需要输出的字符串
	    	  String c=String.valueOf(i+1);
	    	  show[i]="第"+c+"位"+" 姓名:"+top_name[i]+";用时:"+top[i]+"秒";
	      }
	      switch(level)
	      {
	      case 1:{nan_du="初级";break;}
	      case 2:{nan_du="中级";break;}
	      case 3:{nan_du="高级";break;}
	      }
	      //弹出排行榜的对话框
			AlertDialog.Builder myDialog = new AlertDialog.Builder(
					saoleijiemian_nandu.this);
			myDialog.setTitle(nan_du+"排行榜");
			myDialog.setPositiveButton("清空排行榜", new cleanClick());
			myDialog.setNegativeButton("返回", null);
			myDialog.setItems(show, null); // hangs tops
			myDialog.show();
			
}
class cleanClick implements DialogInterface.OnClickListener
{

public void onClick(DialogInterface dialog,int which)
{		  		    //弹出是否清除排行榜的提示
	AlertDialog.Builder myDialog = new AlertDialog.Builder(
		saoleijiemian_nandu.this);
     myDialog.setTitle("是否清除");
     myDialog.setPositiveButton("是", new confirmClick());
     myDialog.setNegativeButton("返回", null);
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

public void onClick(DialogInterface dialog,int which) //确认清除排行榜记录
{    
    switch(level){
    case 1:{     
   	 editor_simple.clear();//清楚排行榜的key-value对
   	 editor_simple.commit();//提交修改方法
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

     //提示已清除
     AlertDialog.Builder myDialog = new AlertDialog.Builder(
 			saoleijiemian_nandu.this);
        myDialog.setMessage("已清除");
        myDialog.setNegativeButton("返回", null);
        myDialog.show();
}
}
    public int set_lei_count()
       {return leishu;}
}
