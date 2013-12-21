import java.io.BufferedReader;
import java.io.InputStreamReader;


//メッセージ入力スレッド テスト用
public class TCPSender implements Runnable {

	TCPManager tcpManager;

	TCPSender(TCPManager tcpManager){
		this.tcpManager = tcpManager;
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		try {
			// *標準入力のための入力ストリーム生成
			// *標準入力から文字列を読み取るにはBufferdReaderをかますらしい
			BufferedReader stdReader =
				new BufferedReader(new InputStreamReader(System.in));

			while (tcpManager.isActive()) {
				tcpManager.sendMessage(stdReader.readLine());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
