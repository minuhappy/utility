package com.utility.socket;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utility.socket.util.ThreadUtil;


public class MinaSocketClient extends IoHandlerAdapter {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * connection timeout
	 */
	private static final int CONNECTION_TIMEOUT = 30000;
	/**
	 * retry to connect wait time
	 */
	private static final int RETRY_WAIT_TIME = 5000;
	/**
	 * 현재 실행 중인지 여부
	 */
	private boolean isRunning;
	
	/**
	 * 세션
	 */
	private IoSession session;
	
	public void start() {
		ThreadUtil.doAsynch(() -> {
			NioSocketConnector connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(CONNECTION_TIMEOUT);
			connector.getFilterChain().addLast("logger", new LoggingFilter());
			connector.setHandler(this);
			this.isRunning = true;
			
			while (this.isRunning) {
				try {
					String ip = "localhost";
					int port = 4335;
					
					ConnectFuture future = connector.connect(new InetSocketAddress(ip, port));
					future.awaitUninterruptibly();
					session = future.getSession();
					break;
					
				} catch (RuntimeIoException e) {
					this.logger.error("서버 접속에 실패했습니다.", e);
					
					try {
						Thread.sleep(RETRY_WAIT_TIME);
					} catch (InterruptedException e1) {
					}
				}
			}
			
			if (session != null) {
				session.getCloseFuture().awaitUninterruptibly();
			}
			
			connector.dispose();
		});
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public boolean isConnected() {
		return this.session == null ? false : this.session.isConnected();
	}

	public void stop() {
		this.isRunning = false;
		this.session.closeNow();
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		this.session = session;
		this.logger.info("세션 [" + session.getId() + "]이 생성되었습니다.");
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
	 * JSON 문자열을 로봇 서버에 전송
	 *
	 * @param jsonStr
	 */
	public void send(String jsonStr) {
		this.send(jsonStr.getBytes());
	}

	/**
	 * byte[] raw 데이터로 로봇 서버에 전송
	 *
	 * @param rawMsg
	 */
	public void send(byte[] rawMsg) {
		if (this.session != null && this.session.isConnected()) {
			IoBuffer buffer = IoBuffer.allocate(rawMsg.length, false);
			buffer.put(rawMsg);
			buffer.flip();
			this.session.write(buffer);
		}
	}
}
