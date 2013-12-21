//GUIとくっつけるときにクラスごと削除
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;


public class UDPSender implements Runnable {


	private UDPManager udpmanager;


	public UDPSender(UDPManager udpmanager){
		this.udpmanager = udpmanager;
	}

	public void run() {

		try {

			//標準入力用のストリーム生成
			BufferedReader stdReader = new BufferedReader(new InputStreamReader(System.in));

			// メッセージ入力ループ
			while(udpmanager.isActive()){
				String line = stdReader.readLine();
				if(line.equals("end")){
					udpmanager.sendrmUser();
					udpmanager.stop();
				}
				else{
					this.udpmanager.sendMessage(line);
				}
			}

		} catch (SocketException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

}
