import java.io.BufferedReader;


public class TCPReader implements Runnable {

	private TCPManager tcpManager;
	public TCPReader(TCPManager tcpManager) {
		this.tcpManager = tcpManager;
	}

	/**
	 * 1. get the input stream from the socket.
	 * 2. while the socket is active, read strings from socket and display them in stdout.
	 */
	public void run() {
		try {
			// *入力ストリーム生成。入力の場合はBufferedReaderでラップされとる
			BufferedReader br =
				tcpManager.getInputStream();

			while (tcpManager.isActive()) {
				//入力ストリームから文字列読み取って画面表示
				//相手から"bye"が送られてきたら終了
				String line = br.readLine();
				tcpManager.receiveMessageDecide(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
