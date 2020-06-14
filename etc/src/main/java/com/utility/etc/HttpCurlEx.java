package com.utility.etc;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class HttpCurlEx {
	public static void main(String[] args) {
		
	}

	public void copyInputStreamToFile() {
		String userName = "admin";
		String password = "elidom";

		String path = "http://repo.hatiolab.com/nexus/service/local/repositories/jar_deployed/content/com/hatiolab/elings-boot/2.1.0.1/elings-boot-2.1.0.1.jar";

		try {
			String userPass = userName + ":" + password;
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));

			URL url = new URL(path.toString());

			URLConnection connection = url.openConnection();

			connection.setRequestProperty("Authorization", basicAuth);
			connection.setRequestProperty("X-Requested-With", "Curl");

			InputStream inputStream = connection.getInputStream();

			File targetFile = new File("/Users/minu/Downloads/sample.jar");
			Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			IOUtils.closeQuietly(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyURLToFile() {
		String userName = "admin";
		String password = "elidom";

		String path = "http://repo.hatiolab.com/nexus/service/local/repositories/jar_deployed/content/com/hatiolab/elings-boot/2.1.0.1/elings-boot-2.1.0.1.jar";

		try {
			URL url = new URL(path.toString());
			URLConnection connection = url.openConnection();

			String userPass = userName + ":" + password;
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));

			connection.setRequestProperty("Authorization", basicAuth);
			connection.setRequestProperty("X-Requested-With", "Curl");

			File targetFile = new File("/Users/minu/Downloads/sample.jar");

			FileUtils.copyURLToFile(url, targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 정상 수행 안됨. 파일 사이즈가 다름.
	// public void curlInvoke() {
	// String userName = "admin";
	// String password = "elidom";
	//
	// String path = "http://repo.hatiolab.com/nexus/service/local/repositories/jar_deployed/content/com/hatiolab/elings-boot/2.1.0.1/elings-boot-2.1.0.1.jar";
	//
	// try {
	// URL url = new URL(path.toString());
	// URLConnection connection = url.openConnection();
	//
	// String userPass = userName + ":" + password;
	// String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));
	//
	// connection.setRequestProperty("Authorization", basicAuth);
	// connection.setRequestProperty("X-Requested-With", "Curl");
	//
	// InputStream inputStream = connection.getInputStream();
	//
	// byte[] buffer = new byte[inputStream.available()];
	// inputStream.read(buffer);
	//
	// File targetFile = new File("/Users/minu/Downloads/sample.jar");
	// OutputStream outStream = new FileOutputStream(targetFile);
	// outStream.write(buffer);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
