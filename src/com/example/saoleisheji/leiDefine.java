package com.example.saoleisheji;
//扫雷格子类 ：用于定义扫雷区域中每一格子的状态
public class leiDefine {
	private boolean leiyes = false; //是否为有雷格子，有false，没有true
	private boolean unfolded = false;//该格子是否已经被开启，有true,没有false
	private boolean sign = false;//是已标记旗子，有true，没有false
	private boolean sign_error=false;//是否标错旗，是true，否false
    private int leinum=0;//该格子的周围的雷的总数

	public boolean leiyes() { //供外部调用确定该格子是否为雷格子
		return leiyes;
	}
	public void setlei(boolean leiyes) { //供外部调用设置是否为雷格子
		this.leiyes =leiyes;
	}
	public boolean unfolded () {//供外部调用确定格子是否已开启
		return unfolded ;
	}
	public void setunfold(boolean unfolded) {//供外部调用设置格子是否已开启
		this.unfolded = unfolded;
	}
	public boolean sign() {//供外部调用确定已标记旗子
		return sign;
	}
	public void setsign_error(boolean sign_error) {//供外部调用设置是否标错旗子
		this.sign_error =sign_error;
	}
	public boolean getsign() {//供外部调用获取格子是否已标记旗子
		return sign;
	}
	public boolean getsign_error() {//供外部调用获取格子是否已标记旗子
		return sign_error;
	}
	
	public void setsign(boolean sign) {//供外部调用设置已标记旗子
		this.sign = sign;    
	}
	public void setleinum(int leinum){//供外部调用设置格子的周围的雷的总数
		this.leinum=leinum;
	}
	public int getleinum(){//供外部调用获取格子的周围的雷的总数
		return leinum;
	}
	}

