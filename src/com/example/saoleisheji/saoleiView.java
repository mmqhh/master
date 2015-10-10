package com.example.saoleisheji;


import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class saoleiView extends View {
    boolean starting = false;//游戏开始后为true，其它时候为false
	 boolean gameover = false;//输为true，其它时候为false
	protected boolean win = false;//赢为true，其他时候为false
	private int lie= 10;//列数
	private int hang=10;//行数
	private int leinum = 10;//雷数
	private int blocks_width =65;//单个块所占长度,经过测试，1080p的手机设置为100,720p的手机设置为65
	private int touchX = 0;//触控到的格子的行值
	private int touchY = 0;//触控到的格子的列植
	private int leiX = -1;//触控到的雷的行值
	private int leiY = -1;//触控到的雷的列值
	private int sign_num=0;//格子周围被插的旗子总数
	public int all_sign_num=0;//已插旗子总数
	boolean qizi=false;//用来确定能否插旗子

	private leiDefine saoleiArr [][] = null;//初始化扫雷格子的类
    TextView left_lei=null;

	public saoleiView(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub

		initSaoleiCanShu();      //初始化扫雷的参数
		//使用setOnTouchListener，设置监听扫雷区域的触摸屏的操作
		this.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				
				touchX = (int)((event.getY())/blocks_width);//获取触控到的格子的行植
				touchY= (int)((event.getX())/blocks_width);//获取触控到的格子的列值

				
				//判断是否点中扫雷区域
				if(touchX>=hang||touchY>=lie||touchX<0||touchY<0){
					return true;
				}
				//按住格子时的事件
				if(event.getAction()==0){
					if(saoleiArr[touchX][touchY].unfolded()==false){
						invalidate();//重新绘图
					}
					return true;
				}else if(event.getAction()==1){
                    //松开格子后的事件
					if(gameover==false)starting = true;//开始游戏
					//判断是否可以插旗子
					if(qizi==true&&saoleiArr[touchX][touchY].unfolded()==false&&gameover==false){
						//改变旗子的状态
						if(saoleiArr[touchX][touchY].sign()==true){
							saoleiArr[touchX][touchY].setsign(false);
							all_sign_num=all_sign_num-1;
							left_lei.setText("剩余雷数："+String.valueOf(leinum-all_sign_num));
							saoleiArr[touchX][touchY].setsign_error(false);
						}else{
							saoleiArr[touchX][touchY].setsign(true);
							all_sign_num=all_sign_num+1;
							left_lei.setText("剩余雷数："+String.valueOf(leinum-all_sign_num));
							if(saoleiArr[touchX][touchY].leiyes()==false)
								saoleiArr[touchX][touchY].setsign_error(true);
						}
						invalidate();
						return true;
					}
					else if(touchX<hang&&touchY<lie&&gameover==false&&starting==true){
							if(saoleiArr[touchX][touchY].leiyes()==true&&saoleiArr[touchX][touchY].getsign()==false){
								//不走运点中雷输了
							    starting = false;
						        gameover = true;
						        //获取点中的雷的坐标，用于显示点中的雷
						        leiX = touchX;
						        leiY = touchY;
						        invalidate();//重新绘图
								return false;
							}
							else //没有点中雷
							{
								//打开这个方块,获取这个方块的雷数
								int leinums = saoleiArr[touchX][touchY].getleinum();
								if(leinums == 0&&saoleiArr[touchX][touchY].getsign()==false){
									//雷数为0，递归调用开启没有雷的模块
									opennolei(touchX, touchY);
								}
								else
									//格子已开启
									//这部分是模拟电脑扫雷中的在已打开的格子中，同时点击鼠标左右键，开启它附近还未开启的部分。
									//由于不会实现双击操作，而且双击操作比较麻烦，所以该用单击来执行。
						       {
									if(saoleiArr[touchX][touchY].unfolded()==true)
									{openNeighbor(touchX, touchY);}
								    else{
								    	//格子未开启
								    	if(saoleiArr[touchX][touchY].getsign()==false)//判断未开启的格子是否插了旗子
							                    saoleiArr[touchX][touchY].setunfold(true);//设置点中的格子的状态为已开启。
								}
									}
								//判断是否完成
								if(leinum == get_fold_block_num()){//雷的数量等于未开启的格子的数量
									//玩家胜利
									win= true;
									starting=false;
								}
								invalidate();//重新绘图
							}
						}
					}
				return true;
			}
			});
		}

      protected void initSaoleiCanShu() //初始化扫雷的参数
      {
        saoleiArr = new leiDefine[hang][lie];
		gameover = false;
		starting = false;
		win = false;
      //为每个格子新建一个类
		for (int i = 0; i < hang; i++) {
			for (int j = 0; j < lie; j++) {
				saoleiArr[i][j] = new leiDefine();
			}
		}
		//获取随机变量
		Random r = new Random(System.currentTimeMillis());
		for(int x = 0;x<leinum;x++){
			//随机分配位置放雷
			int randX = r.nextInt(hang);//返回一个大于等于0小于hang的随机数
			int randY = r.nextInt(lie);//返回一个大于等于0小于lie的随机数
			if(saoleiArr[randX][randY].leiyes())//判断这个位置是否放过雷。
			{//有放过雷，计数值减1，跳出循环
				x--;
				continue;
			}else{//没有放过雷，设置该位置的雷状态为true。
				saoleiArr[randX][randY].setlei(true);
				//对该格子周围的格子的雷数加1
				addleinum(randX-1,randY-1);
				addleinum(randX-1,randY);
				addleinum(randX-1,randY+1);
				addleinum(randX,randY-1);
				addleinum(randX,randY+1);
				addleinum(randX+1,randY-1);
				addleinum(randX+1,randY);
				addleinum(randX+1,randY+1);
			}
		}
      }

	private void addleinum(int x,int y)//增加格子雷数
	{
		if(x>=0&&x<=(hang-1)&&y>=0&&y<=(lie-1))//判断该位置是不是扫雷区域的格子
		{
		int leishu;
		leishu=saoleiArr[x][y].getleinum();//获取原来的雷数设置
		leishu=leishu+1;
		saoleiArr[x][y].setleinum(leishu);//更改原来的雷数设置
		}
	}
	//开启周围雷数为0的格子
	void opennolei(int x,int y){
		if(saoleiArr[x][y].leiyes()==true){//有雷,不开
			return ;
		}
		if(saoleiArr[x][y].getsign_error()==false)saoleiArr[x][y].setunfold(true);//判断有没有标错旗子，没有——可以开，有——不开
		if(saoleiArr[x][y].getleinum()!=0){//开启到雷数不为0的格
			return ;
		}
		if(x-1>=0&&x-1<=(hang-1)&&y-1>=0&&y-1<=(lie-1)){
			if(saoleiArr[x-1][y-1].unfolded()==false)//判断是不是扫雷区域和格子有无被开启
					opennolei(x-1, y-1);
					//开启左上角格子			
		}
		if(x-1>=0&&x-1<=(hang-1)&&y>=0&&y<=(lie-1)){
			if(saoleiArr[x-1][y].unfolded()==false)//判断是不是扫雷区域和格子有无被开启
					opennolei(x-1, y);
					//开启正上方格子			
		}
		if(x-1>=0&&x-1<=(hang-1)&&y+1>=0&&y+1<=(lie-1)){
			if(saoleiArr[x-1][y+1].unfolded()==false)//判断是不是扫雷区域和格子有无被开启
					opennolei(x-1, y+1);
					//开启右上角格子			
		}
		if(x>=0&&x<=(hang-1)&&y-1>=0&&y-1<=(lie-1)){
			if(saoleiArr[x][y-1].unfolded()==false)//判断是不是扫雷区域和格子有无被开启
					opennolei(x, y-1);
					//开启左方格子			
		}
		if(x>=0&&x<=(hang-1)&&y+1>=0&&y+1<=(lie-1)){
			if(saoleiArr[x][y+1].unfolded()==false)//判断是不是扫雷区域和格子有无被开启
					opennolei(x, y+1);
					//开启右方格子			
		}
		if(x+1>=0&&x+1<=(hang-1)&&y-1>=0&&y-1<=(lie-1)){
			if(saoleiArr[x+1][y-1].unfolded()==false)//判断是不是扫雷区域和格子有无被开启
					opennolei(x+1, y-1);
					//开启左下角格子			
		}
		if(x+1>=0&&x+1<=(hang-1)&&y>=0&&y<=(lie-1)){
			if(saoleiArr[x+1][y].unfolded()==false)//判断是不是扫雷区域和格子有无被开启
					opennolei(x+1, y);
					//开启正上方格子			
		}
		if(x+1>=0&&x+1<=(hang-1)&&y+1>=0&&y+1<=(lie-1)){
			if(saoleiArr[x+1][y+1].unfolded()==false)//判断是不是扫雷区域和格子有无被开启
					opennolei(x+1, y+1);
					//开启右下角格子			
		}
		return ;
	}

	private int get_fold_block_num()//获取未开启的格子数，用于判断是否赢得游戏
	{
		int result = 0;
		for (int i = 0; i < hang; i++) {
			for (int j = 0; j <lie; j++) {
				if(saoleiArr[i][j].unfolded()==false){
					result ++;
				}
			}
		}
		return result;
	}
	
	//开启已打开的有数非雷格子的周围其他格子
	private void openNeighbor(int x,int y){
		if( saoleiArr[x][y].getleinum() == get_neighber_sign_num(x,y)){//周围雷数=周围旗子数
		   open(x-1,y-1);//左上
		   open(x-1,y);//上
		   open(x-1,y+1);//右上
		   open(x,y-1);//左
		   open(x,y+1);//右
		   open(x+1,y-1);//左下
		   open(x+1,y);//下
		   open(x+1,y+1);//右下
		   
		}
		}
//开启周围格子
	public void open(int x,int y)
	{
		if(x>=0&&x<=(hang-1)&&y>=0&&y<=(lie-1)){
			if(saoleiArr[x][y].unfolded()==false){//未开启格子的情况
				if(saoleiArr[x][y].getsign()==false&&saoleiArr[x][y].leiyes()==false)//没有旗子标记，没有雷
				{
					if(saoleiArr[x][y].getleinum()==0)//数字为0 ：调用 opennolei()
						    opennolei(x,y);						
					else saoleiArr[x][y].setunfold(true);	//数字不为0 ：直接开				
				}
				else if(saoleiArr[x][y].getsign()==false&&saoleiArr[x][y].leiyes()==true)//没有旗子标记，有雷
					gameover=true;   //gameover,显示标错雷的格子
				else if(saoleiArr[x][y].getsign()==true&&saoleiArr[x][y].leiyes()==false)//有旗子标记，没有雷
				   gameover=true;    //gameover，显示标错雷的格子
			

		}
		}
	}
	

	public int get_neighber_sign_num(int x,int y){//获取周围旗子数
		sign_num = 0;
		count_sign_num(x-1,y-1);
		count_sign_num(x-1,y);
		count_sign_num(x-1,y+1);
		count_sign_num(x,y-1);
		count_sign_num(x,y+1);
		count_sign_num(x+1,y-1);
		count_sign_num(x+1,y);
		count_sign_num(x+1,y+1);
		return sign_num;
	}
	public void count_sign_num(int x,int y)
	{
		if(x>=0&&x<=(hang-1)&&y>=0&&y<=(lie-1)){//判断在不在扫雷区域内
			if(saoleiArr[x][y].sign()==true){
				sign_num = sign_num + 1;
			}
		}
	}
	
	
	//画板部分
	//设置好区域为扫雷区域的每个格子放置图片	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < hang; i++) {
			for (int j = 0; j < lie; j++) {
                  if(gameover == true&&saoleiArr[i][j].leiyes()==true)//判断是否游戏失败和该位置是否有雷
		   				{
		   					Bitmap lei = null;
		   					if(i==leiX&&j==leiY){//显示点中雷的格子
		   						//用BitmapFactory中的decodeResource(Resources res, int id)函数,
		   						//根据给定的资源Id从指定资源中解析、创建Bitmap对象
		   						lei = BitmapFactory.decodeResource(getResources(), R.drawable.zhonglei);
		   					}
		   					else{//显示有雷的格子
		   						lei = BitmapFactory.decodeResource(getResources(), R.drawable.lei);
		   					}
		   					//调用drawBitmap函数放置图片到相应的坐标中
		   					canvas.drawBitmap(lei, j*blocks_width, i*blocks_width,null);
		   					all_sign_num=0;
		   				}
            
					else if(gameover==true&&saoleiArr[i][j].getsign_error()==true)
	                  {   //标错旗被炸了
						  Bitmap pic = null;
	                	  pic = BitmapFactory.decodeResource(getResources(), R.drawable.sign_error);
		   				  canvas.drawBitmap(pic, j*blocks_width, i*blocks_width,null);
	                  } 
					else if(saoleiArr[i][j].unfolded()==true){
						//方块已打开
						int leinum = saoleiArr[i][j].getleinum();
						Bitmap pic = null;

						//判断应该放置数字为多少的方块图片
			            if(leinum == 0&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false)
					 //由于执行完雷初始化函数后，有雷的格子也会被统计雷数，所以除了判断雷数外，还需要再判断该格子是否为有雷格子
						{
							pic= BitmapFactory.decodeResource(getResources(), R.drawable.l0);
						}else if(leinum==1&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false){
							pic = BitmapFactory.decodeResource(getResources(), R.drawable.l1);
						}else if(leinum==2&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false){
							pic = BitmapFactory.decodeResource(getResources(), R.drawable.l2);
						}else if(leinum==3&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false){
							pic = BitmapFactory.decodeResource(getResources(), R.drawable.l3);
						}else if(leinum==4&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false){
							pic = BitmapFactory.decodeResource(getResources(), R.drawable.l4);
						}else if(leinum==5&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false){
							pic = BitmapFactory.decodeResource(getResources(), R.drawable.l5);
						}else if(leinum==6&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false){
							pic = BitmapFactory.decodeResource(getResources(), R.drawable.l6);
						}else if(leinum==7&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false){
							pic = BitmapFactory.decodeResource(getResources(), R.drawable.l7);
						}else if(leinum==8&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false){
							pic = BitmapFactory.decodeResource(getResources(), R.drawable.l8);
						}
	   					//调用drawBitmap函数放置图片到相应的坐标中
						canvas.drawBitmap(pic, j*blocks_width, i*blocks_width,null);
					}
                 else 
                	 {if( win == true ){
						//成功通关游戏
						if(saoleiArr[i][j].leiyes()== true){
							//放置旗子图片到有雷的格子上
							Bitmap flag = BitmapFactory.decodeResource(getResources(), R.drawable.qizi);
		   					//调用drawBitmap函数放置图片到相应的坐标中
							canvas.drawBitmap(flag, j*blocks_width, i*blocks_width,null);
						}
						all_sign_num=0;
					} else {
						Bitmap empty = null;
						if(saoleiArr[i][j].sign()==true){
							//旗子标记为true的放置旗子图片
							empty = BitmapFactory.decodeResource(getResources(), R.drawable.qizi);
						}else{
							//没打开的格子放置未打开的格子的图片
							empty = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
						}
	   					//调用drawBitmap函数放置图片到相应的坐标中
						canvas.drawBitmap(empty, j*blocks_width, i*blocks_width,null);
					}
			}
			}
}
}

//各个供外部使用的接口函数
     public void setsign(boolean chaqizi){//设置能否放旗子
	        this.qizi = chaqizi;
           }
     public void setstarting(boolean starting){//设置开始
	         this.starting = starting;
           }        
     public void set_Lei(int leinum){//设置雷数
	        this.leinum = leinum;
           }
     public void set_block_width(int block_width)//设置格子的长度
     {
    	 this.blocks_width = block_width;
     }
     public void set_Area(int hang,int lie){//设置区域面积
	        this.hang=hang;
	        this.lie=lie;
           }
     public int get_hang(){//获取行数
    	    return hang;
    	 }
     public int get_lie(){//获取列数
    	    return lie;
    	 }
     public int get_leinum(){//获取雷数
	        return leinum;
         }
     public void set_left_lei(TextView left_lei) {
 		this.left_lei = left_lei;
 		left_lei.setText("剩余雷数："+String.valueOf(leinum-all_sign_num));
 	}
     public void set_all_sign_num(int set)
     {
    	 this.all_sign_num=set;
     }
     //获取游戏状态
    public boolean getstarting(){
	        return starting;
	     }
    public boolean getgameover(){
	       return gameover;
	     }
    public boolean getwin(){
	       return win;
	}
}
