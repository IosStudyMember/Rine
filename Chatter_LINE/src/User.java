import java.net.InetSocketAddress;

/**
 *
 * @author endo
 *
 */
public class User {

	private String userID;	//ユーザーID
	private InetSocketAddress inetSocketAddress;	//IP
	private String nickName;	//ユーザ名

	/**
	 * @ejb.create-methodam
	 * @param	ユーザID
	 * @param inetSocketAddress	IP
	 * @param Nickname	ユーザ名
	 */
	User(String userID,InetSocketAddress inetSocketAddress,String nickName){

		this.userID = userID;
		this.inetSocketAddress = inetSocketAddress;
		this.nickName = nickName;
	}

	/**
	 * ユーザID getter
	 * @return ユーザID
	 */
	public String getUserID(){
		return userID;
	}

	/**
	 * IP getter
	 * @return IPD
	 */
	public InetSocketAddress getInetSocketAddress(){
		return inetSocketAddress;
	}

	/**
	 * IP getter
	 * @return IPD
	 */
	public String getNickname(){
		return nickName;
	}

}
