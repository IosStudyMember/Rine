import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class TCPManager {
	private Socket socket;
	private Boolean active;
	private ChatRoom chatRoom;

	public TCPManager(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
		this.socket = chatRoom.getSocket();
		active = true;
	}

	public boolean isActive() {
		return active;
	}

	public void stop() {
		active = false;
	}

	public void close() {
		try {
			socket.close();
			System.out.println("--connection closed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1. run socketWriter as a thread.
	 * 2. run socketReader as a thread.
	 */
	public void run() {
		try {
			// run socket reader as a thread
			Thread TCPReader = new Thread(new TCPReader(this));
			TCPReader.start();
			// wait the threads end
			TCPReader.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// * 出力ストリーム作成。スレッド化したsocketWriterからコール
	public DataOutputStream getOutputStream() throws Exception {
		return new DataOutputStream(socket.getOutputStream());
	}

	// * 入力ストリーム作成。スレッド化したSocketReaderからコール
	public BufferedReader getInputStream() throws Exception {
		return new BufferedReader(new InputStreamReader(
				new DataInputStream(socket.getInputStream())));
	}

	//通常メッセージ送信 GUIからコールしてもらう。
	public void sendMessage(String line){
		try {
			DataOutputStream out = getOutputStream();
			out.writeBytes("MSG\n" + Chatter.mine + "\n" +line);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveMessageDecide(String line){
		//通常メッセージ
		if(line.substring(0,3).equals("MSG")){
			String userID = line.substring(4,line.indexOf("\n",4));
			String message = line.substring(line.indexOf("\n",4)+1);
			Chatter.showMessage(userID,message);
		}
		//チャットルーム閉店メッセージ受信時
		else if(line.substring(0,3).equals("CLR")){
			//チャットルーム閉店処理
			Chatter.closeChatroom();
			stop();
		}
		//ユーザ退室メッセージ受信時
		else if(line.substring(0,3).equals("EXU")){
			String userName = line.substring(4,line.indexOf("\n",4));
			chatRoom.exitUser(userName);
		}

	}

}
