package com.example.saoleisheji;
//ɨ�׸����� �����ڶ���ɨ��������ÿһ���ӵ�״̬
public class leiDefine {
	private boolean leiyes = false; //�Ƿ�Ϊ���׸��ӣ���false��û��true
	private boolean unfolded = false;//�ø����Ƿ��Ѿ�����������true,û��false
	private boolean sign = false;//���ѱ�����ӣ���true��û��false
	private boolean sign_error=false;//�Ƿ����죬��true����false
    private int leinum=0;//�ø��ӵ���Χ���׵�����

	public boolean leiyes() { //���ⲿ����ȷ���ø����Ƿ�Ϊ�׸���
		return leiyes;
	}
	public void setlei(boolean leiyes) { //���ⲿ���������Ƿ�Ϊ�׸���
		this.leiyes =leiyes;
	}
	public boolean unfolded () {//���ⲿ����ȷ�������Ƿ��ѿ���
		return unfolded ;
	}
	public void setunfold(boolean unfolded) {//���ⲿ�������ø����Ƿ��ѿ���
		this.unfolded = unfolded;
	}
	public boolean sign() {//���ⲿ����ȷ���ѱ������
		return sign;
	}
	public void setsign_error(boolean sign_error) {//���ⲿ���������Ƿ�������
		this.sign_error =sign_error;
	}
	public boolean getsign() {//���ⲿ���û�ȡ�����Ƿ��ѱ������
		return sign;
	}
	public boolean getsign_error() {//���ⲿ���û�ȡ�����Ƿ��ѱ������
		return sign_error;
	}
	
	public void setsign(boolean sign) {//���ⲿ���������ѱ������
		this.sign = sign;    
	}
	public void setleinum(int leinum){//���ⲿ�������ø��ӵ���Χ���׵�����
		this.leinum=leinum;
	}
	public int getleinum(){//���ⲿ���û�ȡ���ӵ���Χ���׵�����
		return leinum;
	}
	}

