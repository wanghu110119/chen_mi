package com.mht.modules.swust.utils.thread;

public class Business implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
		System.out.println("正在生产");
		Product p = new Product();
		p.make( p);
	}
	}
}
