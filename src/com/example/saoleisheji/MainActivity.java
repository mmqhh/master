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
    Button  nandu;    //进入“扫雷游戏难度部分”

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
		nandu=(Button)findViewById(R.id.intosaolei_nandu);
		nandu.setOnClickListener(new nClick());

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
   class nClick implements OnClickListener
  {
	   public void onClick(View v)
		{//进入扫雷游戏的界面
			Intent in=new Intent(MainActivity.this,saoleijiemian_nandu.class);
			startActivity(in);
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
			Intent in=new Intent(MainActivity.this,select_paihang.class);
			startActivity(in);
		}
	}


	

}
