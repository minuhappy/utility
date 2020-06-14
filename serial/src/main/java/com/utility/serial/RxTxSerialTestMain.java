package com.utility.serial;

public class RxTxSerialTestMain {
	public static void main(String[] args) {
		RxTxSerialConnection serial = new RxTxSerialConnection();
		serial.connect("Test Serial", "COM3", 9300);
	}
}
