package com.utility.socket;

public class MinaSocketClientMain {
	public static void main(String[] args) throws Exception {
		MinaSocketClient client = new MinaSocketClient();
		client.start();
		
		
		for(int i = 0; i < 10000000; i++) {
			Thread.sleep(10000);
			client.send("ABC");
		}
	}
}
