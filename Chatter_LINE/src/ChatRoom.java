import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


/**
 * 
 * @author Administrator
 * ChatRoomクラス
 * GUIからの操作を契機にインスタンス化される。
 * memberUserIDListは、GUI側から貰う想定
 * 
 */
public class ChatRoom {

	private boolean roomOrner;
	private String ornerUserID;
	private UserList userList;
	private List<String> memberUserIDList;
	private ServerSocket svsock = null;
	private Socket socket = null;
	private UDPManager udpManager;

	//サーバ時のコンストラクタ
	ChatRoom(List<String> memberUserIDList,UDPManager udpManager){
		roomOrner = true;
		this.memberUserIDList = memberUserIDList;
		this.udpManager = udpManager;
	}

	//クライアント時のコンストラクタ
	ChatRoom(String ornerUserID){
		roomOrner = false;
		this.ornerUserID = ornerUserID;
	}


	//ソケット取得
	public Socket getSocket(){
		return socket;
	}

	//ユーザ退出処理
	public void exitUser(String userID){
		for(int i=0 ; i<memberUserIDList.size();i++){
			if(memberUserIDList.get(i).equals(userID)){
				memberUserIDList.remove(i);
			}
		}

	}

	void init(){
		//サーバモード
		if(roomOrner){
			//参加メンバーにUDPで入室依頼メッセージを送信
			//メンバー全員とTCP接続する。
			
			try {
				ServerSocket svsock = new ServerSocket(Chatter.port);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			//参加メンバーにUDPで入室依頼メッセージを送信
			udpManager.sendJoinReq(memberUserIDList);
		
		}
		//クライアントモード
		else{

			socket = new Socket();

			try {
				socket.connect(userList.getInetSocketAddress(ornerUserID));
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			// assign the socket to a socket manager
			TCPManager tcpManager = new TCPManager(this);

			// start conversations
			// *実処理実行
			tcpManager.run();
			// close the connection
			// *終了処理
			tcpManager.close();
		}



	}

}
