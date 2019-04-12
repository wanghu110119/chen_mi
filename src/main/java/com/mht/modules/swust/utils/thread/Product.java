package com.mht.modules.swust.utils.thread;

import java.util.ArrayList;
import java.util.List;

import com.mht.modules.sys.entity.User;

public class Product {
	private String name;
	private int count;
	public static List<Product> list = new ArrayList<Product>();
	static User u = new User();
	public static int j;

	public static int getJ() {
		return j;
	}

	public static void setJ(int j) {
		Product.j = j;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return list.size();
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static List<Product> getList() {
		return list;
	}

	public static void setList(List<Product> list) {
		Product.list = list;
	}

	public synchronized void produce(Product p) {

	}

	// /生产方法
	public void make( Product p) {
		synchronized (u) {
			if (j > 9) {
				try {
					System.out.println(Thread.currentThread().getName()+"等待销售========" + j);
					u.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				j++;
				System.out.println(Thread.currentThread().getName()+"已生产====" + j);
				u.notifyAll();
			}
		}
	}

	// /消费
	public  void sale( Product p) {
		synchronized (u) {
			if (j < 1) {
				try {
					System.out.println(Thread.currentThread().getName()+"等待生产========" + j);
					u.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				j--;
				System.out.println(Thread.currentThread().getName()+"已售出=====" + j);
				u.notifyAll();
			}
		}
	}

	public static void main(String[] args) {
		Business b = new Business(); 
		Costomer c = new Costomer();
		Thread th1 = new Thread(b);
		Thread th3 = new Thread(b);
		Thread th = new Thread(b);
		Thread th2 = new Thread(c);
		Thread th4 = new Thread(c);
		th4.start();
		th1.start();
		th3.start();
		th2.start();
		th.start();
	}

}










