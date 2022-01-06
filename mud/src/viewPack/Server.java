package viewPack;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Set;

import controller.*;
import controller.MessageManagement;
import controller.RoomManagement;
import controller.UserInput;
import model.Player;
public class Server {
	
	public static int PORT_NUM = 1888;
	
	
	static class ServerThread extends Thread {
		Socket socket;
		
		
		public ServerThread(Socket socket) {
			
			this.socket = socket;
		}
		@Override
		public void run() {
			
			try {
				
				RoomManagement.cityMap.get("room_guangchang");
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//读取控制命令
				PrintStream out = new PrintStream(socket.getOutputStream());
				String Message;
				Player player = new Player();
				String id;
				String pname;
				String location;

			    out.println("欢迎来到云深不知处:\n");
				out.println("请输入你的ID:\n");
				out.flush();
				
				Message = in.readLine();
				id = Message;
				while(MysqlManagement.search(id)) {//判断id是否重复
					out.println("这个id已经存在了哟\n");
					out.println("请重新输入:");
					id = in.readLine();
				}
				player.setId(id);
				
				out.println("请输入用户名:\n");
				out.flush();
				Message = in.readLine();
				player.setName(Message);
				pname = Message;
				player.setLocation("yangzhou_guangchang");//初始位置
				location = "yangzhou_guangchang";
				
				
				
					MysqlManagement.insertData(id, pname, location);//将玩家信息插入数据库
				
				
				
				
				MessageManagement.addPlayerChannels(player.getId(), out);
				MessageManagement.addPlayerInChannels(player.getId(), in);//聊天
				
				RoomManagement.cityMap.get(player.getLocation()).addPlayer(player);
				
				boolean isQuit = false;
				
				while(!isQuit) {
					if(isQuit) {
						break;
					}
					Message = in.readLine();
					System.out.println(player.getName());
					if("q".equals(Message.trim()) || "p".equals(Message.trim())) {
						if("q".equals(Message.trim())) {
							isQuit = true;
							UserInput.dealInput(player, Message);
							out.println("服务器已断开链接");
							out.flush();
							out.close();
							in.close();
							socket.close();
						}
						if("p".equals(Message.trim())) {
							String receiveId;
							MessageManagement.showToPlayer(player, "请输入私聊对象的ID,目前在线的人的id为:\n");
							Set<String> keySet = MessageManagement.playerChannels.keySet();//获取所有在线玩家的id,放在一个set里面
							Iterator<String> iterator = keySet.iterator(); //获取keySet的迭代器
							while (iterator.hasNext()) {
								String playId = iterator.next();
								MessageManagement.showToPlayer(player, playId);
							}
							String inputId = in.readLine();
							boolean hasId = false;
							for (String string : keySet) {
								if (inputId.equals(string.trim())) {
									hasId = true;
								}
							}
							if (hasId==true) {
								receiveId = inputId;
								MessageManagement.showToPlayer(player, "请输入你想发送的消息:\n");
								Message = in.readLine();
								MessageManagement.singleChar(player.getId(), receiveId, Message);
							}else {
								MessageManagement.showToPlayer(player, "该玩家不在线上");
							}
						}
					}else {
						UserInput.dealInput(player, Message);
					}
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	}
	static public void main(String[] args) throws Exception {
		RoomManagement.creatRooms();
		
		ServerSocket serverSocket = new ServerSocket(PORT_NUM);
		MysqlManagement.link();
		
		
		while(true) {
			Socket socket = serverSocket.accept();
			new ServerThread(socket).start();

		}
	}
}
