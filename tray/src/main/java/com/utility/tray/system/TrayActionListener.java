package com.utility.tray.system;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.utility.tray.service.TrayActionService;

public class TrayActionListener {
	public void initTray() {
		// Icon 및 Icon Double Click Action 등록
		Image trayIcon = Toolkit.getDefaultToolkit().getImage("image/trayIcon.png");
		TrayIconHandler.registerTrayIcon(trayIcon, "Hatio Agent", new TrayActionService());

		// Menu Item 등록
		TrayIconHandler.addItem(Constants.RELOAD, new TrayActionService());
		TrayIconHandler.addItem(Constants.EXIT, new TrayActionService());

		// Agent 실행
		this.startUpAgent();
		// TrayIconHandler.displayMessage("Hatio Agent", "Connected Hatio
		// Agent.", MessageType.INFO);
	}

	/**
	 * Start up Agent
	 */
	public void startUpAgent() {
		try {
			StringBuilder command = new StringBuilder("java -jar ");
			command.append(System.getProperty("user.dir"));
			command.append("\\lib\\elidom-agent.jar");

			Process process = Runtime.getRuntime().exec(command.toString());
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
