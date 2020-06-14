package com.utility.serial;

import java.io.IOException;
import java.io.InputStream;

public class RxTxSerialReader {
	InputStream in;

	public RxTxSerialReader(InputStream in) {
		this.in = in;
	}

	public void run() {
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			while ((len = this.in.read(buffer)) > -1) {
				System.out.print(new String(buffer, 0, len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
