package com.utility.cassandra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.cql.jdbc.CassandraDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SuppressWarnings("all")
public class CassandraJdbc {
	public void getJdbcTempl() {
		{
			// Use Thrift Port.
			String driverClassName = "org.apache.cassandra.cql.jdbc.CassandraDriver";
			String url = "jdbc:cassandra://10.211.55.6:9160/cycling";
			String userName = "cassandra";
			String password = "cassandra";

			NamedParameterJdbcTemplate jdbcTempl = this.connectByJdbc(driverClassName, url, userName, password);
			List<Map<String, Object>> list = jdbcTempl.queryForList("select * from emp", new HashMap<String, Object>());
		}

		{
			// Use Cassandra Client Port.
			String driverClassName = "com.dbschema.CassandraJdbcDriver";
			String url = "jdbc:cassandra://10.211.55.6:9042/cycling";
			String userName = "cassandra";
			String password = "cassandra";

			NamedParameterJdbcTemplate jdbcTempl = this.connectByJdbc(driverClassName, url, userName, password);
			List<Map<String, Object>> list = jdbcTempl.queryForList("select * from emp", new HashMap<String, Object>());
		}
	}

	public NamedParameterJdbcTemplate connectByJdbc(String driverClassName, String url, String userName, String password) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(userName);
		ds.setPassword(password);
		ds.setMaxActive(30);
		ds.setMinIdle(10);
		ds.setMaxIdle(10);
		ds.setMaxWait(-1);
		ds.setTimeBetweenEvictionRunsMillis(1800000L);

		return new NamedParameterJdbcTemplate(ds);
	}

	public void connectByCassandraDataSource() {
		String host = "10.211.55.6";
		int port = 9160;

		String keyspace = "cycling";
		String user = "cassandra";
		String password = "cassandra";
		String version = null;
		String consistency = null;
		//
		CassandraDataSource dataSource = new CassandraDataSource(host, port, keyspace, user, password, version, consistency);
		NamedParameterJdbcTemplate jdbcTempl = new NamedParameterJdbcTemplate(dataSource);

		List<Map<String, Object>> list = jdbcTempl.queryForList("select * from emp", new HashMap<String, Object>());

		System.out.println("END");
	}
}