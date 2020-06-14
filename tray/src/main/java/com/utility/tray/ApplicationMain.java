package com.utility.tray;

import com.utility.tray.system.TrayActionListener;

/**
 * @author Minu.Kim
 *
 */
public class ApplicationMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TrayActionListener().initTray();
	}
}
