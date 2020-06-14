package com.utility.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.utility.socket.util.ThreadUtil;

public class MinaSocketServer extends IoHandlerAdapter {
	private static final int PORT = 4335;

	/**
	 * 세션
	 */
	Map<Long, IoSession> SESSION_MAP = new HashMap<Long, IoSession>();

	/**
	 * 실행 중 여부
	 */
	private boolean isRunning;

	public boolean isRunning() {
		return this.isRunning;
	}

	public void start() throws Exception {
		ThreadUtil.doAsynch(() -> {
			IoAcceptor acceptor = new NioSocketAcceptor();

			// acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.setHandler(this);
			acceptor.getSessionConfig().setReadBufferSize(2048);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
			try {
				acceptor.bind(new InetSocketAddress(PORT));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	// public void stop() {
	// this.isRunning = false;
	//
	// if (this.session != null) {
	// this.session.closeNow();
	// }
	// }

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		SESSION_MAP.put(session.getId(), session);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		SESSION_MAP.put(session.getId(), session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		SESSION_MAP.remove(session.getId());
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		SESSION_MAP.put(session.getId(), session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		SESSION_MAP.put(session.getId(), session);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;

			Set<Long> keys = SESSION_MAP.keySet();
			for (Long key : keys) {
				IoSession getSession = SESSION_MAP.get(key);
				getSession.write(buffer);
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		SESSION_MAP.put(session.getId(), session);
	}
}
