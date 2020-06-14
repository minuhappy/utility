package com.utility.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster.Builder;

public class CassandraMain {
	private Cluster cluster;
	private Session session;

	public static void main(String[] args) {
		CassandraMain client = new CassandraMain();

		String ipAddress = "10.211.55.6";
		int port = 9042;

		client.connect(ipAddress, port);
//		client.close();
		
		
		client.createKeyspace("test_one", "SimpleStrategy", 1);
		
	}

	public void connect(String node, Integer port) {
		Builder b = Cluster.builder().addContactPoint(node);
		if (port != null) {
			b.withPort(port);
		}

		cluster = b.build();
		session = cluster.connect();
	}

	public Session getSession() {
		return this.session;
	}

	public void close() {
		session.close();
		cluster.close();
	}

	public void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE KEYSPACE IF NOT EXISTS ");
		sql.append(keyspaceName).append(" WITH replication = {");
		sql.append("'class':'").append(replicationStrategy);
		sql.append("','replication_factor':").append(replicationFactor);
		sql.append("};");
		
		session.execute(sql.toString());
	}
}
