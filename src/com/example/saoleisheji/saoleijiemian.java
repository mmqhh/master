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
	ToggleButton toggle;//旗子状态开关按钮
	saoleiView saoleiView = null;
    TextView jishi=null;
    
    Button startbtn;//开始按钮
    EditText name;  //记录获胜玩家名字
    
    Button  set_area;//设置扫雷区域的面积
    Button  set_leinum;//设置扫雷区域雷的总数
    
    EditText lie;//编辑列数
    EditText hang;//编辑行数
    EditText leinum;//编辑雷数
    
    SharedPreferences save_block_width;  
    SharedPreferences.Editor block_width_edtior;

    int  block_width=65;
    int   set_hang=10;
    int   set_lie=10;
    int     leishu=10;
    int    shijian=0;//记录游戏时间
    boolean starting=false;//记录游戏是否开始
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout check;
    String playername;//获胜玩家名字

    boolean count=true;//控制停止计时

	protected void onCreate(Bundle savedInstanceState) {   
		// TODO Auto-generated method stub   
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.saoleijiemian);
		//获取只能被本应用程序读写的SharedPreferences对象
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
					Builder dialog=new AlertDialog.Builder(saoleijiemian.this);
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

	class aClick implements OnClickListener   //确定区域大小
	{ 
		public void onClick(View v)
		{
			CharSequence gethang =hang.getText(),getlie=lie.getText(); //获取行数
			//利用TextUtils.isEmpty()判断输入是否为null或“”,是——返回true，不是——返回false
			if(TextUtils.isEmpty(gethang)==false&TextUtils.isEmpty(getlie)==false)
			{
			set_hang = Integer.parseInt(String.valueOf(gethang));//将类型为CharSequence的行数转变成int型
			set_lie  = Integer.parseInt(String.valueOf(getlie));//将类型为CharSequence的列数转变成int型
			}
			//确定输入数据是否符合规则
			if(TextUtils.isEmpty(gethang)==false&TextUtils.isEmpty(getlie)==false&&set_hang*set_lie>=saoleiView.get_leinum())
				saoleiView.set_Area(set_hang, set_lie);
			else{
				//弹出提示框，提示重新输入
				Builder dialog=new AlertDialog.Builder(saoleijiemian.this);
				dialog.setMessage("区域大小设置错误,请重新输入");
				dialog.setNegativeButton("返回",new exitClick());
				dialog.create();
				dialog.show();
			}
		}
	}
	class lClick implements OnClickListener
	{ 
		public void onClick(View v)
		{
			CharSequence leicount =leinum.getText(); //获取雷数
			if(TextUtils.isEmpty(leicount)==false)
			leishu = Integer.parseInt(String.valueOf(leicount));//将类型为CharSequence的雷数转变成int型
			if(TextUtils.isEmpty(leicount)==false&&saoleiView.get_hang()*saoleiView.get_lie()>=leishu)
				saoleiView.set_Lei(leishu);
			else{
				Builder dialog=new AlertDialog.Builder(saoleijiemian.this);
				dialog.setMessage("雷数设置错误,请重新输入");
				dialog.setNegativeButton("返回",new exitClick());
				dialog.create();
				dialog.show();
			}

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
		paihang();//排行榜的SharedPreferences的key-value对设置
		show_pai_hang();//显示排行榜
		
	}
}
      class exitClick implements DialogInterface.OnClickListener //退出对话框
{
    	  public void onClick(DialogInterface dialog,int which)
{
	dialog.cancel();
}
}
public void paihang()
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
			top[i] = preferences.getInt(score[i], 0);//没有该键值对应的value，返回0
			top_name[i]=preferences.getString(player[i], "0");//没有该键值对应的value，返回字符串“0”
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
public void show_pai_hang()
{

	//SharedPreferences的键值列表，键值有分数和玩家两种
	   String[] score={"num1","num2","num3","num4","num5","num6","num7","num8","num9","num10"};
	   String[] player={"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
	   
	   String[] show =new String[10];//存放需要显示的字符串内容
	   //临时保存键值score和player的值
	      String[]    top = new String[10];
	      String[]  top_name=new String[10];
		   //获取键值score和player中每个对应的value，没有则置0
	      for (int i = 0; i < top.length; i++) {
				top[i] = String.valueOf(preferences.getInt(score[i], 0));
				top_name[i]=preferences.getString(player[i], "0");
			}
	      for (int i = 0; i < top.length; i++){
		      //设置排行榜每一行需要输出的字符串
	    	  String c=String.valueOf(i+1);
	    	  show[i]="第"+c+"位"+" 姓名:"+top_name[i]+";用时:"+top[i]+"秒";
	      }
	      //弹出排行榜的对话框
			AlertDialog.Builder myDialog = new AlertDialog.Builder(
					saoleijiemian.this);
			myDialog.setTitle("排行榜");
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
		saoleijiemian.this);
     myDialog.setTitle("是否清楚");
     myDialog.setPositiveButton("是", new confirmClick());
     myDialog.setNegativeButton("返回", null);
     myDialog.show();
     editor.clear();
     editor.commit();
	
}
}
class confirmClick implements DialogInterface.OnClickListener
{

public void onClick(DialogInterface dialog,int which) //确认清除排行榜记录
{    
     editor.clear();//清楚排行榜的key-value对
     editor.commit();//提交修改方法
     //提示已清除
     AlertDialog.Builder myDialog = new AlertDialog.Builder(
 			saoleijiemian.this);
        myDialog.setMessage("已清除");
        myDialog.setNegativeButton("返回", null);
        myDialog.show();
}
}
}

