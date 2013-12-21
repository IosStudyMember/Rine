import java.net.InetSocketAddress;
import java.util.List;

/*
 引数：IP,Port,ユーザー名

 UTF-8の設定箇所
「ウィンドウ」⇒「一般」⇒「ワークスペース」⇒「テキスト・ファイルのエンコード」
「実行」⇒「実行構成」⇒"共通"タブ⇒「エンコード」
eclipse.exeと同じフォルダにあるeclipse.iniに以下を記述
-Dfile.encoding=utf-8
 */


/**
 * Chatter Main
 */
public class Chatter {

	public static User mine;
	public static UserList userList;
	public static int port;


    public static void main(String[] argv) throws Exception{

    	port = Integer.parseInt(argv[2]);
    	mine = new User(argv[0], new InetSocketAddress(argv[1], port),argv[3]);
    	userList = new UserList();
    	UDPManager udpManager = new UDPManager(new InetSocketAddress(argv[0], Integer.parseInt(argv[1])),mine,userList);
    	//UDPManager udpManager = new UDPManager(new InetSocketAddress("192.168.1.255", 5100),argv[0]);

    	udpManager.receiveMessage();

		Thread UDPSender = new Thread(new UDPSender(udpManager));
		UDPSender.start();

		UDPSender.join();

		System.out.println("終了します");

    }

	public static void refreshUserList(List<User> list) {

		System.out.println("===UserList===");
		for(int i=0;i < list.size();i++){
			System.out.println("   [ "+list.get(i).getUserID()+" ]" + " [ " + list.get(i).getNickname() + " ]");
		}
		System.out.println("==============");
	}

	public static void showMessage(String userName,String message){

		System.out.println(userName+"「"+message+"」");


	}

	public static void closeChatroom() {
		// チャットルーム閉店時の画面表示処理を記述。

	}
}
