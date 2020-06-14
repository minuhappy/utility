package com.utility.etc.util;

public class ThreadUtil {

	/**
	 * 동기 방식의 스레드 실행.
	 * 
	 * @param runnable
	 */
	public static void doSynch(Runnable runnable) {
		new SimpleThread(runnable).start();
	}

	/**
	 * 비동기 방식의 스레드 실행.
	 * 
	 * @param runnable
	 */
	public static void doAsynch(Runnable runnable) {
		new AsynchThread(runnable).start();
	}
}

/**
 * 동기 방식의 스레드 클래스.
 * 
 * @author minu
 */
class SimpleThread {
	Runnable runnable;

	public SimpleThread(Runnable runnable) {
		this.runnable = runnable;
	}

	public void start() {
		runnable.run();
	}
}

/**
 * 비동기 방식의 스레드 클레스.
 * 
 * @author minu
 */
class AsynchThread implements Runnable {
	Runnable runnable;

	public AsynchThread(Runnable runnable) {
		this.runnable = runnable;
	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		runnable.run();
	}
}