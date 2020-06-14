package com.utility.serial;

import java.io.InputStream;
import java.io.OutputStream;

import com.utility.serial.util.ThreadUtil;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class RxTxSerialConnection {
	public void connect(String connectionName, String portName, int port) {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (portIdentifier.isCurrentlyOwned()) {
				throw new RuntimeException("Error: Port is currently in use.");
			}

			// 클래스 이름을 식별자로 사용하여 포트 오픈
			CommPort commPort = portIdentifier.open(connectionName, 2000);

			if (commPort instanceof SerialPort) {
				throw new RuntimeException("Error: Only serial ports are handled by this example.");
			}

			// 포트 설정(통신속도 설정. 기본 9600으로 사용)
			SerialPort serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(port, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// Input,OutputStream 버퍼 생성 후 오픈
			InputStream in = serialPort.getInputStream();
			OutputStream out = serialPort.getOutputStream();

			// 읽기.
			ThreadUtil.doAsynch(() -> new RxTxSerialReader(in).run());

			// 쓰기.
			ThreadUtil.doAsynch(() -> new RxTxSerialWriter(out).run());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
