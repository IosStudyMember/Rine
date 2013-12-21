import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

// 機能を持たせすぎなので、一部UDPReceiverに機能を渡す
// receiveMessageDecide
public class UDPManager {

	private Boolean active;
	public InetSocketAddress bloadCastAddress;	//private にして setterとgetterでアクセスするようにすべきか
	public int receiveMessageMax = 1024; //受信メッセージ最大長
	public DatagramPacket sendPacket;
	public DatagramSocket datagramsocket;
	private User mine;
	private UserList userList;

	public UDPManager(InetSocketAddress bloadCastAddress,User mine,UserList userList){
		this.bloadCastAddress = bloadCastAddress;
		this.mine = mine;
		this.userList = userList;
		active = true;
	}

	public void init(){
		byte[] sendBuffer = "".getBytes();
		try {
			userList.addUserList(mine);
			sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, this.bloadCastAddress);
			datagramsocket = new DatagramSocket();
			sendaddUser();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}



	//////////////////////////////////////////////////////////////////////////////////////////////

	// 状態確認
	public boolean isActive() {
		return active;
	}

	// 停止メソッド-GUIから呼び出し
	public void stop() {
		active = false;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 *  ユーザー接続通知メッセージをブロードキャスト
	 *  先頭3文字が"ADU"のメッセージはユーザー参加メッセージとする
	 *  メッセージフォーマット
	 *    [ADU][\n][ユーザID]][\n][ユーザ名]
	 *  …先頭文字が直切にならんようにしたい。
	 *  …区切り文字を改行コードにしているが、これも要再考
	 */
	public void sendaddUser(){
		System.out.println("sendaddUser() start");
		String sendMessage =("ADU\n" + mine.getUserID() + "\n" + mine.getNickname());
		byte[] sendBuffer = sendMessage.getBytes();
		sendPacket.setData(sendBuffer);
		sendPacket.setAddress(bloadCastAddress.getAddress());	// 不要かも…念のためブロードキャストアドレスセットし直し
		try {
			datagramsocket.send(sendPacket);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}


	/**
	 *  ユーザー受入応答メッセージを送信
	 *  先頭3文字が"EXS"のメッセージはユーザー受入応答メッセージとする
	 *  メッセージフォーマット
	 *    [EXS][\n][ユーザID]][\n][ユーザ名]
	 *  …先頭文字が直切にならんようにしたい。
	 *  …区切り文字を改行コードにしているが、これも要再考
	 */
	public void sendaddUser(InetAddress receiveIp){
		System.out.println("sendaddUser("+receiveIp+") start");
		String sendMessage =("EXS\n" + mine.getUserID() + "\n" + mine.getNickname());
		byte[] sendBuffer = sendMessage.getBytes();
		sendPacket.setData(sendBuffer);
		sendPacket.setAddress(receiveIp);
		try {
			datagramsocket.send(sendPacket);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 *  ユーザー切断通知メッセージを送信
	 *  先頭3文字が"RMU"のメッセージはユーザー切断通知メッセージとする
	 *  メッセージフォーマット
	 *    [RMU][\n][ユーザID]]
	 *  …先頭文字が直切にならんようにしたい。
	 *  …区切り文字を改行コードにしているが、これも要再考
	 */
	public void sendrmUser(){
		System.out.println("sendrmUser start");
		String sendMessage =("RMU\n" + mine.getUserID());
		byte[]  sendBuffer = sendMessage.getBytes();
		sendPacket.setData(sendBuffer);
		sendPacket.setAddress(bloadCastAddress.getAddress());	// 不要かも…念のためブロードキャストアドレスセットし直し
		try {
			datagramsocket.send(sendPacket);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 *  ユーザー入室依頼メッセージを送信
	 *  先頭3文字が"JRQ"のメッセージはユーザー入室依頼メッセージとする
	 *  メッセージフォーマット
	 *    [JRQ][\n][ユーザID]
	 *  …先頭文字が直切にならんようにしたい。
	 */
	public void sendJoinReq(List<String> joinUserIDList){
		String sendMessage =("JRQ\n"+mine.getUserID());
		byte[] sendBuffer = sendMessage.getBytes();
		sendPacket.setData(sendBuffer);
		for(int i=0;i<joinUserIDList.size();i++){
			sendPacket.setAddress(userList.getUser(joinUserIDList.get(i)).getInetSocketAddress().getAddress());
			try {
				datagramsocket.send(sendPacket);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
		}
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////

	//受信メッセージ判別メソッド
	// @2013/12/21 機能が大きすぎるので、別クラスにして、UDPReceiverからコール
	public void receiveMessageDecide(DatagramPacket receivePacket){

		System.out.println("receiveMessageDecide() start");

		try {
			// 受信パケットからメッセージを取得
			String line = new String(receivePacket.getData(),0, receivePacket.getLength(),"UTF-8");
			System.out.println("RECEIVE KIND: "+line.substring(0,4));
			// 受信パケットから送信元IPを取得 …InetSocketAddressで戻り値とれないか？
			InetAddress receiveIp = receivePacket.getAddress();
			int receivePort = receivePacket.getPort();
			System.out.println("RECEIVE IP: "+receiveIp);

				//ユーザー接続通知メッセージ受信時
				//ユーザーリストに追加してユーザーリスト表示更新
				//ユーザー受入応答メッセージを送信
				if(line.substring(0,3).equals("ADU")){
					String userID = line.substring(4,line.indexOf("\n",4));
					String nicKName = line.substring(line.indexOf("\n",4)+1);
					userList.addUserList(new User(userID,new InetSocketAddress(receiveIp,receivePort),nicKName));
					List<User> list = userList.getUserList();
					Chatter.refreshUserList(list);
					// 送信元IPが自分自身でなければ、送信元に自分のIP送信 …ifは要らないかも
					if(!receiveIp.equals(InetAddress.getLocalHost())){
						sendaddUser(receiveIp);
					}
				}
				//ユーザー受入応答メッセージ受信時
				//ユーザーリストに追加してユーザーリスト表示更新
				else if(line.substring(0,3).equals("EXS")){
					String userID = line.substring(4,line.indexOf("\n",4));
					String nicKName = line.substring(line.indexOf("\n",4)+1);
					userList.addUserList(new User(userID,new InetSocketAddress(receiveIp,receivePort),nicKName));
					List<User> list = userList.getUserList();
					Chatter.refreshUserList(list);
				}
				//ユーザー切断通知メッセージ受信時
				//ユーザーリストからユーザ削除してユーザーリスト表示更新
				else if(line.substring(0,3).equals("RMU")){
					userList.rmUserList(line.substring(4));
					List<User> list = userList.getUserList();
					Chatter.refreshUserList(list);
				}
				//ユーザー入室依頼メッセージ受信時
				//Chatroomインスタンスを生成して、送信元とTCPSocket接続
				else if(line.substring(0,3).equals("JRQ")){
					String userID = line.substring(4,line.indexOf("\n",4));
					ChatRoom chatRoom = new ChatRoom(userID);
					chatRoom.init();
				}
				else{
					Chatter.showMessage("システムエラー","判別不能電文受信");
				}
			//}
			} catch (Exception e) {
				e.printStackTrace();
		}
	}

	//メッセージ受信開始メソッド
	public void receiveMessage(){

		System.out.println("receiveMessage() start");
		try {

			Thread UDPreceiver = new Thread(new UDPReceiver(this));
			UDPreceiver.start();
			//以下に終了処理
			//sendrmUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
