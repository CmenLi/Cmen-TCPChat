package cmen.tcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fusesource.jansi.AnsiConsole;

import cmen.tcp.ChatLogger.SenderType;
import cmen.tcp.client.TCPClient;
import cmen.tcp.server.TCPServer;

public class TCPMain {
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		AnsiConsole.systemInstall();
		File logFolder = new File(System.getProperty("user.dir"), "logs/");
		if(!logFolder.exists()) {
			logFolder.mkdir();
		}
		try {
			if(args[0].equalsIgnoreCase("@server")) {
				while(true) {
					System.out.print(SenderType.SYSTEM.head() + "�п�J�s����(Port): ");
					String portStr = reader.readLine();
					if(isInteger(portStr)) {
						new TCPServer(Integer.parseInt(portStr)).connect();
						break;
					}else {
						if(portStr.equalsIgnoreCase("default")) {
							new TCPServer().connect();
							break;
						}
						System.err.println(ChatLogger.colorPool.get("cRed") + "[Error]�п�J���T���s����榡");
					}
				}
			}else if(args[0].equalsIgnoreCase("@client")) {
				while(true) {
					System.out.print(SenderType.SYSTEM.head() + "�п�J��誺������}(IP): ");
					String ip = reader.readLine();
					if(ip.equalsIgnoreCase("local")) {
						ip = "127.0.0.1";
					}else if(ip.equalsIgnoreCase("default")) {
						ip = InetAddress.getLocalHost().getHostAddress();
						System.out.println(SenderType.SYSTEM.head() + "�z��e��������}��: " + ip);
					}
					System.out.print(SenderType.SYSTEM.head() + "�п�J�s����(Port): ");
					String portStr = reader.readLine();
					if(isInteger(portStr)) {
						try {
							System.out.println(SenderType.SYSTEM.head() + "���ճs�����A�еy��......");
							new TCPClient(ip, Integer.parseInt(portStr)).connect();
							break;
						} catch (IOException e){
							if(e instanceof UnknownHostException) {
								System.err.println(ChatLogger.colorPool.get("cRed") + "[Error]�䤣���IP��}�A�й��խ��s��J���T��IP");
								continue;
							}
							else if(e instanceof ConnectException) {
								System.err.println(ChatLogger.colorPool.get("cRed") + "[Error]�ӪA�Ⱥݪ�Port�P�z��J�����šA�й��խ��s��J");
								continue;
							}
							e.printStackTrace();
						}
					}else {
						if(portStr.equalsIgnoreCase("default")) {
							System.out.println(SenderType.SYSTEM.head() + "���ճs�����A�еy��......");
							new TCPClient(ip).connect();
							break;
						}
						System.err.println(ChatLogger.colorPool.get("cRed") + "[Error]�п�J���T���s����榡" + ChatLogger.colorPool.get("cReset"));
					}
				}
			}else {
				return;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.exit(1);
		return;
	}
	
	/*�Q�Υ��W��ܦ��P�_�Ӧr��O�_���Ʀr*/
	public static boolean isInteger(String s) {
		Pattern pattern = Pattern.compile("^([1-9]?)(\\d*)$");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}
}
