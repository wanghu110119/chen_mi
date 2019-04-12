package com.mht.modules.swust.utils.thread;

public class Costomer implements Runnable{

	@Override
	public void run() {
		while (true) {
			System.out.println("正在销售");
			// TODO Auto-generated method stub
			Product p = new Product();
			p.sale( p);
		}
		
	}

}
