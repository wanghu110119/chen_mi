package com.mht.modules.swust.utils.thread;

import java.util.ArrayList;

import com.mht.common.config.Global;

public class Thread2 implements Runnable {

	private static int j;

	private static ArrayList<Integer> list = new ArrayList<>();

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		Thread2.j = j;
	}

	public ArrayList<Integer> getList() {
		return list;
	}

	public void setList(ArrayList<Integer> list) {
		Thread2.list = list;
	}

	public synchronized  void add() {
		j = j + 500;
	}
	public synchronized  void sale(){
		if (j>= 1000) {
			j=j - 1000;
			System.out.println(Thread.currentThread().getName() + "---卖出1000个");
			notifyAll();
		} else{
			try {
				Thread.currentThread().wait();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			synchronized (this) {
				if (j >= 2000) {
					try {
						System.out.println(Thread.currentThread().getName() + "----------等待销售完成");
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
				add();
				System.out.println(Thread.currentThread().getName() + "----------完成500个");
				notifyAll();
				System.out.println(Thread.currentThread().getName() + "----------全部线程激活");
				}
			}
		}
	}

	public static void main(String[] args) {
		Global.threadPool.execute(new Runnable() {
			@Override
			public void run() {
				for (int i = 0;  ; i++) {
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("库存剩余----"+j);
//					if(){
//
//					}
				}
			}
		});
		Thread th1 = new Thread(new Thread1());
		Thread th = new Thread(new Thread2());
		Thread th2 = new Thread(new Thread2());
		th1.start();
		th2.start();
		th.start();
	}
}
