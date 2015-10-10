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
    boolean starting = false;//��Ϸ��ʼ��Ϊtrue������ʱ��Ϊfalse
	 boolean gameover = false;//��Ϊtrue������ʱ��Ϊfalse
	protected boolean win = false;//ӮΪtrue������ʱ��Ϊfalse
	private int lie= 10;//����
	private int hang=10;//����
	private int leinum = 10;//����
	private int blocks_width =65;//��������ռ����,�������ԣ�1080p���ֻ�����Ϊ100,720p���ֻ�����Ϊ65
	private int touchX = 0;//���ص��ĸ��ӵ���ֵ
	private int touchY = 0;//���ص��ĸ��ӵ���ֲ
	private int leiX = -1;//���ص����׵���ֵ
	private int leiY = -1;//���ص����׵���ֵ
	private int sign_num=0;//������Χ�������������
	public int all_sign_num=0;//�Ѳ���������
	boolean qizi=false;//����ȷ���ܷ������

	private leiDefine saoleiArr [][] = null;//��ʼ��ɨ�׸��ӵ���
    TextView left_lei=null;

	public saoleiView(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub

		initSaoleiCanShu();      //��ʼ��ɨ�׵Ĳ���
		//ʹ��setOnTouchListener�����ü���ɨ������Ĵ������Ĳ���
		this.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				
				touchX = (int)((event.getY())/blocks_width);//��ȡ���ص��ĸ��ӵ���ֲ
				touchY= (int)((event.getX())/blocks_width);//��ȡ���ص��ĸ��ӵ���ֵ

				
				//�ж��Ƿ����ɨ������
				if(touchX>=hang||touchY>=lie||touchX<0||touchY<0){
					return true;
				}
				//��ס����ʱ���¼�
				if(event.getAction()==0){
					if(saoleiArr[touchX][touchY].unfolded()==false){
						invalidate();//���»�ͼ
					}
					return true;
				}else if(event.getAction()==1){
                    //�ɿ����Ӻ���¼�
					if(gameover==false)starting = true;//��ʼ��Ϸ
					//�ж��Ƿ���Բ�����
					if(qizi==true&&saoleiArr[touchX][touchY].unfolded()==false&&gameover==false){
						//�ı����ӵ�״̬
						if(saoleiArr[touchX][touchY].sign()==true){
							saoleiArr[touchX][touchY].setsign(false);
							all_sign_num=all_sign_num-1;
							left_lei.setText("ʣ��������"+String.valueOf(leinum-all_sign_num));
							saoleiArr[touchX][touchY].setsign_error(false);
						}else{
							saoleiArr[touchX][touchY].setsign(true);
							all_sign_num=all_sign_num+1;
							left_lei.setText("ʣ��������"+String.valueOf(leinum-all_sign_num));
							if(saoleiArr[touchX][touchY].leiyes()==false)
								saoleiArr[touchX][touchY].setsign_error(true);
						}
						invalidate();
						return true;
					}
					else if(touchX<hang&&touchY<lie&&gameover==false&&starting==true){
							if(saoleiArr[touchX][touchY].leiyes()==true&&saoleiArr[touchX][touchY].getsign()==false){
								//�����˵���������
							    starting = false;
						        gameover = true;
						        //��ȡ���е��׵����꣬������ʾ���е���
						        leiX = touchX;
						        leiY = touchY;
						        invalidate();//���»�ͼ
								return false;
							}
							else //û�е�����
							{
								//���������,��ȡ������������
								int leinums = saoleiArr[touchX][touchY].getleinum();
								if(leinums == 0&&saoleiArr[touchX][touchY].getsign()==false){
									//����Ϊ0���ݹ���ÿ���û���׵�ģ��
									opennolei(touchX, touchY);
								}
								else
									//�����ѿ���
									//�ⲿ����ģ�����ɨ���е����Ѵ򿪵ĸ����У�ͬʱ���������Ҽ���������������δ�����Ĳ��֡�
									//���ڲ���ʵ��˫������������˫�������Ƚ��鷳�����Ը��õ�����ִ�С�
						       {
									if(saoleiArr[touchX][touchY].unfolded()==true)
									{openNeighbor(touchX, touchY);}
								    else{
								    	//����δ����
								    	if(saoleiArr[touchX][touchY].getsign()==false)//�ж�δ�����ĸ����Ƿ��������
							                    saoleiArr[touchX][touchY].setunfold(true);//���õ��еĸ��ӵ�״̬Ϊ�ѿ�����
								}
									}
								//�ж��Ƿ����
								if(leinum == get_fold_block_num()){//�׵���������δ�����ĸ��ӵ�����
									//���ʤ��
									win= true;
									starting=false;
								}
								invalidate();//���»�ͼ
							}
						}
					}
				return true;
			}
			});
		}

      protected void initSaoleiCanShu() //��ʼ��ɨ�׵Ĳ���
      {
        saoleiArr = new leiDefine[hang][lie];
		gameover = false;
		starting = false;
		win = false;
      //Ϊÿ�������½�һ����
		for (int i = 0; i < hang; i++) {
			for (int j = 0; j < lie; j++) {
				saoleiArr[i][j] = new leiDefine();
			}
		}
		//��ȡ�������
		Random r = new Random(System.currentTimeMillis());
		for(int x = 0;x<leinum;x++){
			//�������λ�÷���
			int randX = r.nextInt(hang);//����һ�����ڵ���0С��hang�������
			int randY = r.nextInt(lie);//����һ�����ڵ���0С��lie�������
			if(saoleiArr[randX][randY].leiyes())//�ж����λ���Ƿ�Ź��ס�
			{//�зŹ��ף�����ֵ��1������ѭ��
				x--;
				continue;
			}else{//û�зŹ��ף����ø�λ�õ���״̬Ϊtrue��
				saoleiArr[randX][randY].setlei(true);
				//�Ըø�����Χ�ĸ��ӵ�������1
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

	private void addleinum(int x,int y)//���Ӹ�������
	{
		if(x>=0&&x<=(hang-1)&&y>=0&&y<=(lie-1))//�жϸ�λ���ǲ���ɨ������ĸ���
		{
		int leishu;
		leishu=saoleiArr[x][y].getleinum();//��ȡԭ������������
		leishu=leishu+1;
		saoleiArr[x][y].setleinum(leishu);//����ԭ������������
		}
	}
	//������Χ����Ϊ0�ĸ���
	void opennolei(int x,int y){
		if(saoleiArr[x][y].leiyes()==true){//����,����
			return ;
		}
		if(saoleiArr[x][y].getsign_error()==false)saoleiArr[x][y].setunfold(true);//�ж���û�б�����ӣ�û�С������Կ����С�������
		if(saoleiArr[x][y].getleinum()!=0){//������������Ϊ0�ĸ�
			return ;
		}
		if(x-1>=0&&x-1<=(hang-1)&&y-1>=0&&y-1<=(lie-1)){
			if(saoleiArr[x-1][y-1].unfolded()==false)//�ж��ǲ���ɨ������͸������ޱ�����
					opennolei(x-1, y-1);
					//�������ϽǸ���			
		}
		if(x-1>=0&&x-1<=(hang-1)&&y>=0&&y<=(lie-1)){
			if(saoleiArr[x-1][y].unfolded()==false)//�ж��ǲ���ɨ������͸������ޱ�����
					opennolei(x-1, y);
					//�������Ϸ�����			
		}
		if(x-1>=0&&x-1<=(hang-1)&&y+1>=0&&y+1<=(lie-1)){
			if(saoleiArr[x-1][y+1].unfolded()==false)//�ж��ǲ���ɨ������͸������ޱ�����
					opennolei(x-1, y+1);
					//�������ϽǸ���			
		}
		if(x>=0&&x<=(hang-1)&&y-1>=0&&y-1<=(lie-1)){
			if(saoleiArr[x][y-1].unfolded()==false)//�ж��ǲ���ɨ������͸������ޱ�����
					opennolei(x, y-1);
					//�����󷽸���			
		}
		if(x>=0&&x<=(hang-1)&&y+1>=0&&y+1<=(lie-1)){
			if(saoleiArr[x][y+1].unfolded()==false)//�ж��ǲ���ɨ������͸������ޱ�����
					opennolei(x, y+1);
					//�����ҷ�����			
		}
		if(x+1>=0&&x+1<=(hang-1)&&y-1>=0&&y-1<=(lie-1)){
			if(saoleiArr[x+1][y-1].unfolded()==false)//�ж��ǲ���ɨ������͸������ޱ�����
					opennolei(x+1, y-1);
					//�������½Ǹ���			
		}
		if(x+1>=0&&x+1<=(hang-1)&&y>=0&&y<=(lie-1)){
			if(saoleiArr[x+1][y].unfolded()==false)//�ж��ǲ���ɨ������͸������ޱ�����
					opennolei(x+1, y);
					//�������Ϸ�����			
		}
		if(x+1>=0&&x+1<=(hang-1)&&y+1>=0&&y+1<=(lie-1)){
			if(saoleiArr[x+1][y+1].unfolded()==false)//�ж��ǲ���ɨ������͸������ޱ�����
					opennolei(x+1, y+1);
					//�������½Ǹ���			
		}
		return ;
	}

	private int get_fold_block_num()//��ȡδ�����ĸ������������ж��Ƿ�Ӯ����Ϸ
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
	
	//�����Ѵ򿪵��������׸��ӵ���Χ��������
	private void openNeighbor(int x,int y){
		if( saoleiArr[x][y].getleinum() == get_neighber_sign_num(x,y)){//��Χ����=��Χ������
		   open(x-1,y-1);//����
		   open(x-1,y);//��
		   open(x-1,y+1);//����
		   open(x,y-1);//��
		   open(x,y+1);//��
		   open(x+1,y-1);//����
		   open(x+1,y);//��
		   open(x+1,y+1);//����
		   
		}
		}
//������Χ����
	public void open(int x,int y)
	{
		if(x>=0&&x<=(hang-1)&&y>=0&&y<=(lie-1)){
			if(saoleiArr[x][y].unfolded()==false){//δ�������ӵ����
				if(saoleiArr[x][y].getsign()==false&&saoleiArr[x][y].leiyes()==false)//û�����ӱ�ǣ�û����
				{
					if(saoleiArr[x][y].getleinum()==0)//����Ϊ0 ������ opennolei()
						    opennolei(x,y);						
					else saoleiArr[x][y].setunfold(true);	//���ֲ�Ϊ0 ��ֱ�ӿ�				
				}
				else if(saoleiArr[x][y].getsign()==false&&saoleiArr[x][y].leiyes()==true)//û�����ӱ�ǣ�����
					gameover=true;   //gameover,��ʾ����׵ĸ���
				else if(saoleiArr[x][y].getsign()==true&&saoleiArr[x][y].leiyes()==false)//�����ӱ�ǣ�û����
				   gameover=true;    //gameover����ʾ����׵ĸ���
			

		}
		}
	}
	

	public int get_neighber_sign_num(int x,int y){//��ȡ��Χ������
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
		if(x>=0&&x<=(hang-1)&&y>=0&&y<=(lie-1)){//�ж��ڲ���ɨ��������
			if(saoleiArr[x][y].sign()==true){
				sign_num = sign_num + 1;
			}
		}
	}
	
	
	//���岿��
	//���ú�����Ϊɨ�������ÿ�����ӷ���ͼƬ	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < hang; i++) {
			for (int j = 0; j < lie; j++) {
                  if(gameover == true&&saoleiArr[i][j].leiyes()==true)//�ж��Ƿ���Ϸʧ�ܺ͸�λ���Ƿ�����
		   				{
		   					Bitmap lei = null;
		   					if(i==leiX&&j==leiY){//��ʾ�����׵ĸ���
		   						//��BitmapFactory�е�decodeResource(Resources res, int id)����,
		   						//���ݸ�������ԴId��ָ����Դ�н���������Bitmap����
		   						lei = BitmapFactory.decodeResource(getResources(), R.drawable.zhonglei);
		   					}
		   					else{//��ʾ���׵ĸ���
		   						lei = BitmapFactory.decodeResource(getResources(), R.drawable.lei);
		   					}
		   					//����drawBitmap��������ͼƬ����Ӧ��������
		   					canvas.drawBitmap(lei, j*blocks_width, i*blocks_width,null);
		   					all_sign_num=0;
		   				}
            
					else if(gameover==true&&saoleiArr[i][j].getsign_error()==true)
	                  {   //����챻ը��
						  Bitmap pic = null;
	                	  pic = BitmapFactory.decodeResource(getResources(), R.drawable.sign_error);
		   				  canvas.drawBitmap(pic, j*blocks_width, i*blocks_width,null);
	                  } 
					else if(saoleiArr[i][j].unfolded()==true){
						//�����Ѵ�
						int leinum = saoleiArr[i][j].getleinum();
						Bitmap pic = null;

						//�ж�Ӧ�÷�������Ϊ���ٵķ���ͼƬ
			            if(leinum == 0&&saoleiArr[i][j].leiyes()==false&&saoleiArr[i][j].getsign_error()==false)
					 //����ִ�����׳�ʼ�����������׵ĸ���Ҳ�ᱻͳ�����������Գ����ж������⣬����Ҫ���жϸø����Ƿ�Ϊ���׸���
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
	   					//����drawBitmap��������ͼƬ����Ӧ��������
						canvas.drawBitmap(pic, j*blocks_width, i*blocks_width,null);
					}
                 else 
                	 {if( win == true ){
						//�ɹ�ͨ����Ϸ
						if(saoleiArr[i][j].leiyes()== true){
							//��������ͼƬ�����׵ĸ�����
							Bitmap flag = BitmapFactory.decodeResource(getResources(), R.drawable.qizi);
		   					//����drawBitmap��������ͼƬ����Ӧ��������
							canvas.drawBitmap(flag, j*blocks_width, i*blocks_width,null);
						}
						all_sign_num=0;
					} else {
						Bitmap empty = null;
						if(saoleiArr[i][j].sign()==true){
							//���ӱ��Ϊtrue�ķ�������ͼƬ
							empty = BitmapFactory.decodeResource(getResources(), R.drawable.qizi);
						}else{
							//û�򿪵ĸ��ӷ���δ�򿪵ĸ��ӵ�ͼƬ
							empty = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
						}
	   					//����drawBitmap��������ͼƬ����Ӧ��������
						canvas.drawBitmap(empty, j*blocks_width, i*blocks_width,null);
					}
			}
			}
}
}

//�������ⲿʹ�õĽӿں���
     public void setsign(boolean chaqizi){//�����ܷ������
	        this.qizi = chaqizi;
           }
     public void setstarting(boolean starting){//���ÿ�ʼ
	         this.starting = starting;
           }        
     public void set_Lei(int leinum){//��������
	        this.leinum = leinum;
           }
     public void set_block_width(int block_width)//���ø��ӵĳ���
     {
    	 this.blocks_width = block_width;
     }
     public void set_Area(int hang,int lie){//�����������
	        this.hang=hang;
	        this.lie=lie;
           }
     public int get_hang(){//��ȡ����
    	    return hang;
    	 }
     public int get_lie(){//��ȡ����
    	    return lie;
    	 }
     public int get_leinum(){//��ȡ����
	        return leinum;
         }
     public void set_left_lei(TextView left_lei) {
 		this.left_lei = left_lei;
 		left_lei.setText("ʣ��������"+String.valueOf(leinum-all_sign_num));
 	}
     public void set_all_sign_num(int set)
     {
    	 this.all_sign_num=set;
     }
     //��ȡ��Ϸ״̬
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
