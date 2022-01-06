package model;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;

import controller.MessageManagement;
import controller.StaticFunctions;

public class Room {

	private HashMap<CommonContent.DIRECTION, Room> neighbor = new HashMap<CommonContent.DIRECTION, Room>();


	public void setRoom(CommonContent.DIRECTION d, Room r) {
		neighbor.put(d, r);
		// assert r.getRoom(d) == this;
	}

	public Room getRoom(CommonContent.DIRECTION d) {
		return neighbor.get(d);
	}

	private String roomDescription;
	private String roomLooking;
	private String roomId;
	private String roomName;
	private HashMap<String, Player> playerList = new HashMap<String, Player>();//每个房间的玩家列表

	public void exist(Player player, CommonContent.DIRECTION direction) {
		playerList.remove(player.getId(),player);
		Collection<Player> c=playerList.values();
		for(Player value:c){
			MessageManagement.showToPlayer(value, player.getName()+"从"+StaticFunctions.getDirection(direction) +"边安详地走了");
		}
		
	}

	public void enter(Player player, CommonContent.DIRECTION direction) {
		Collection<Player> c=playerList.values();
		for(Player value:c){
			MessageManagement.showToPlayer(value, player.getName()+"从"+StaticFunctions.getReverseDirection(direction)+"边过来了,快去打他啊");
		}
		playerList.put(player.getId(), player);
	}
	public void removePlayer(Player player){
	//用户退出后，清除用户在列表中内容，通知房间内其他玩家
		playerList.remove(player.getId(),player);
		Collection<PrintStream> outs = MessageManagement.playerChannels.values();
		for (PrintStream printStream : outs) {
			printStream.println(player.getName()+"离开啦");
			printStream.flush();
		}

	}
	public void addPlayer(Player player){
	//用户连线进入，加入列表，通知房间其他玩家
		Collection<PrintStream> outs = MessageManagement.playerChannels.values();
		for (PrintStream printStream : outs) {
			printStream.println(player.getName()+"上线啦");
			printStream.flush();
		}
		playerList.put(player.getId(), player);
		String roomLooking2 = getRoomLooking();
		MessageManagement.showToPlayer(player, roomLooking2);
	}

	public void setDescription(String roomDescription) {
		this.roomDescription = roomDescription;
	}

	public String getDescription() {
		return roomDescription;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void SetRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomName() {
		return roomName;
	}

	/*public String getRoomLooking() {
		return roomLooking;
	}*/

	public String getRoomLooking() {
		// 房间名
		roomLooking = roomName + "\t";
		// 房间描述
		// 应该由Client负责解析传输过来的字符（设定字体，每行字数）
		int roomDescriptionLength = roomDescription.length();
		if (roomDescriptionLength <= CommonContent.CHARS_PER_LINE)
			roomLooking += roomDescription + "\t";
		else {
			int i;
			for (i = 0; i <= roomDescriptionLength
					- CommonContent.CHARS_PER_LINE; i = i
					+ CommonContent.CHARS_PER_LINE) {
				roomLooking += roomDescription.substring(i, i
						+ CommonContent.CHARS_PER_LINE)
						+ "\t";
			}
			roomLooking += roomDescription.substring(i, roomDescriptionLength)
					+ "\t";
		}

		// 房间出口
		roomLooking += "房间出口为:\n"+getChuKou() + "\t";
		// 房间npc

		// 房间player
		roomLooking += "这个房间里的玩家有:\n"+listRoomPlayers();
		// 房间obj
		return roomLooking;
	}
	private String listRoomPlayers(){
		//列出这个房间中的所有玩家
		StringBuilder sBuilder = new StringBuilder();
		Collection<Player> c=playerList.values();
		for(Player value:c){
			sBuilder.append(value.getName()+" || ");
		}
		return sBuilder.toString();
	}
	private String getChuKou() {
		/*描述每个房间的出口
		 * 
		 * 
		 * */
		String temp = "";
		if(this.roomId == "yangzhou_guangchang" || this.roomId == "yangzhou_beidajie1" || this.roomId == "yangzhou_beidajie2" || this.roomId == "yangzhou_beidajie3" || this.roomId == "yangzhou_nandajie1"|| this.roomId == "yangzhou_nandajie2" ||this.roomId == "yangzhou_nandajie3") {
			temp = "请选择东西南北";
		}
		if(this.roomId == "yangzhou_xidajie1" || this.roomId == "room_qianzhuang" || this.roomId == "room_dangpu" || this.roomId == "room_duchang" || this.roomId == "yangzhou_chaguan" ) {
			temp = "请选择东/南/北";
		}
		if(this.roomId == "yangzhou_kezhan" || this.roomId == "yangzhou_yizhan" || this.roomId == "yangzhou_bingying" || this.roomId == "yangzhou_yanju" || this.roomId == "yangzhou_dongdajie1" ) {
			temp = "请选择西/南/北";
		}
		if(this.roomId == "yangzhou_chmiao" ) {
			temp = "请选择东/南";
		}
		if(this.roomId == "yangzhou_xiaochidian" ) {
			temp = "请选择西/南";
		}
		if(this.roomId == "yangzhou_geyuan" ) {
			temp = "请选择东/北";
		}
		if(this.roomId == "yangzhou_xiaopangu" ) {
			temp = "请选择西/北";
		}
		if(this.roomId == "yangzhou_nanmen" ) {
			temp = "请选择北";
		}
		if(this.roomId == "yangzhou_beimen" ) {
			temp = "请选择南";
		}
		
		return temp;
	}
}

