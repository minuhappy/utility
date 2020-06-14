package com.utility.socket;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DwMinaSocketServer extends IoHandlerAdapter {

	/**
	 * Logger
	 */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 세션
	 */
	private IoSession session;
	
	/**
	 * 실행 중 여부
	 */
	private boolean isRunning;

	public boolean isRunning() {
		return this.isRunning;
	}

	public boolean isConnected() {
		return this.session == null ? false : this.session.isConnected();
	}

	public void start() {
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.setHandler(this);
		acceptor.getSessionConfig().setReadBufferSize(512);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		try {
			int port = 4335;
			acceptor.bind(new InetSocketAddress(port));
		} catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	public void stop() {
		this.isRunning = false;

		if (this.session != null) {
			this.session.closeNow();
		}
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		this.session = session;
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		this.session = session;
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		this.session = session;
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		this.session = session;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		this.session = session;
		this.logger.error(cause.getMessage(), cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		this.session = session;

		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
			byte[] arr = buffer.array();
			String jsonStr = new String(arr);
			this.logger.info("BODY String : " + jsonStr);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		this.session = session;
		this.logger.info(message.toString());
	}

	/**
	 * JSON 문자열을 로봇 클라이언트에 전송
	 */
	public void send(String jsonStr) {
		this.send(jsonStr.getBytes());
	}

	/**
	 * byte[] raw 데이터로 로봇 클라이언트에 전송
	 *
	 * @param rawMsg
	 */
	public void send(byte[] rawMsg) {
		if (rawMsg != null && rawMsg.length > 0 && this.session != null && this.session.isConnected()) {
			IoBuffer buffer = IoBuffer.allocate(rawMsg.length, false);
			buffer.put(rawMsg);
			buffer.flip();
			this.session.write(buffer);
		}
	}
}
