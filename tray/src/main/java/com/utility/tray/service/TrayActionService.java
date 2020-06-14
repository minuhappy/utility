package com.utility.tray.service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.utility.tray.system.Constants;
import com.utility.tray.util.HttpUtil;

public class TrayActionService implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionName = e.getActionCommand();
		if (actionName == null) {
			this.trayIcon();
			return;
		}

		switch (actionName) {
		case Constants.RELOAD:
			this.exit();
			break;
		case Constants.EXIT:
			this.exit();
			break;
		default:
			return;
		}

	}

	public void trayIcon() {
		System.out.println("Tray Icon Click.");
	}

	public void reload() {
		System.out.println("Agent Reload.");
	}

	public void exit() {
		// Agent 종료
		HttpUtil.executePostMethod("http://localhost:9010/shutdown");
		// Tray 종료
		System.exit(0);
	}
}