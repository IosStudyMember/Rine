import java.net.DatagramPacket;
import java.net.DatagramSocket;

// 汎用UDP受信クラスとしてこれはこれで完結させる。
// これを継承したクラスにreceiveMessageDecideを追加して、使う。
public class UDPReceiver implements Runnable {

	private UDPManager udpmanager;

	public UDPReceiver(UDPManager udpmanager){

		this.udpmanager = udpmanager;

	}


	public void run() {

		try {
			// 受信用UDPソケット生成
			DatagramSocket receiveSocket = new DatagramSocket(udpmanager.isa.getPort());

			// 受信用UDPパケット生成
			byte receiveBuffer[] = new byte[udpmanager.receiveMessageMax];
			DatagramPacket receivePacket =
					new DatagramPacket(receiveBuffer, receiveBuffer.length);

			//メッセージ受信ループ
			while (udpmanager.isActive()) {
				// UDPパケットを受信
				receiveSocket.receive(receivePacket);
				// メッセージを表示
				udpmanager.receiveMessageDecide(receivePacket);
			}

			receiveSocket.close();

		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}


	}

}
