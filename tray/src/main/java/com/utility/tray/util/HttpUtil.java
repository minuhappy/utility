package com.utility.tray.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class HttpUtil {

	/**
	 * Get방식으로 전송
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public static String executeGetMethod(String uri) throws Exception {
		return executeGetMethod(uri, null);
	}

	public static String executeGetMethod(String uri, Map<String, Object> params) throws Exception {
		// URL 주소
		String sUrl = uri;
		if (params != null && !params.isEmpty()) {
			StringBuilder makeParam = new StringBuilder();
			makeParam.append("?");

			Set<String> keys = params.keySet();
			for (String key : keys) {
				makeParam.append("&").append(key).append("=").append(params.get(key));
			}
			sUrl += makeParam.toString().replaceFirst("&", "");
		}

		URL url = new URL(sUrl);
		// URLConnection connection = url.openConnection();
		// connection.setConnectTimeout(1000); // Timeout 설정
		// connection.setReadTimeout(10000); // Read Timeout 설정

		BufferedReader bfReader = new BufferedReader(new InputStreamReader(url.openStream()));

		String value = "";
		StringBuilder response = new StringBuilder();
		while ((value = bfReader.readLine()) != null) {
			response.append(value);
		}
		bfReader.close();
		return response.toString();
	}

	/**
	 * Post방식으로 전송
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public static String executePostMethod(String uri) {
		return executePostMethod(uri, null);
	}

	public static String executePostMethod(String uri, Map<String, Object> params) {
		return executePostMethod(uri, params, "UTF-8");
	}

	public static String executePostMethod(String uri, Map<String, Object> paramMap, String charSet) {
		try {
			String params = "";
			
			if (paramMap != null && !paramMap.isEmpty()) {
				StringBuilder makeParam = new StringBuilder();

				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					Object value = paramMap.get(key);
					if (value == null) {
						continue;
					}

					makeParam.append("&");
					makeParam.append(URLEncoder.encode(key, charSet));
					makeParam.append("=");
					makeParam.append(URLEncoder.encode(value.toString(), charSet));
				}
				params = makeParam.toString().replaceFirst("&", "");
			}

			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-type", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);

			OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
			osw.write(params);
			osw.flush();

			BufferedReader bfReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			String value = "";
			StringBuilder response = new StringBuilder();
			while ((value = bfReader.readLine()) != null) {
				response.append(value);
			}

			osw.close();
			bfReader.close();

			return response.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}