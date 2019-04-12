package com.mht.modules.swust.utils.thread;

import java.util.HashMap;
import java.util.Map;

public class Thread1 implements Runnable {

	private static int j;

	public synchronized static void used() {
		j++;
	}

	@Override
	public void run() {
		synchronized (this) {
			Thread2 thread2 = new Thread2();
			for (int i = 0; i < 10; i++) {
			
				thread2.sale();
			}
		}
	}

	public static void starts() {
	}

	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName() + "--finish---" + j);
	}

}
