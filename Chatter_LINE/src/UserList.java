import java.net.InetSocketAddress;
import java.util.List;

// シングルトン
// このクラス自身のフィールドで己をnewする。
// コンストラクタをprivateにし、ここ以外でnewできないようにする。
public class UserList {

	private  List<User> userList;

	public UserList(){
	}

	/**
	 * ユーザリストにユーザ追加
	 * @param user 追加するユーザ
	 */
	public void addUserList(User user){
		userList.add(user);
	}

	/**
	 * ユーザリストからユーザ削除
	 * @param user 削除するユーザ
	 */
	public void rmUser(String userID){

		for(int i=0;i < userList.size();i++){

			if(userID.equalsIgnoreCase(userList.get(i).getUserID())){
				userList.remove(i);
			}

		}

	}

	/**
	 * ユーザリスト取得
	 * @return ユーザリス
	 */
	public List<User> getUserList(){
		return userList;
	}

	/**
	 * ユーザ数取得
	 * @return ユーザ数
	 */
	public int getUserNum(){
		return userList.size();
	}

	/**
	 * ユーザ取得
	 * @param userID	取得対象ユーザのユーザID
	 * @return	取得対象ユーザ
	 */
	public User getUser(String userID){

		for(int i=0;i < userList.size();i++){

			if(userID.equalsIgnoreCase(userList.get(i).getUserID())){
				return userList.get(i);
			}

		}

		//対象ユーザがリストにない場合はNULLを返す
		return null;
	}
	/**
	 * ユーザ取得
	 * @param userID	取得対象ユーザのユーザID
	 * @return	取得対象ユーザ
	 */
	public InetSocketAddress getInetSocketAddress(String userID){

		for(int i=0;i < userList.size();i++){

			if(userID.equalsIgnoreCase(userList.get(i).getUserID())){
				return userList.get(i).getInetSocketAddress();
			}

		}

		//対象ユーザがリストにない場合はNULLを返す
		return null;
	}

}
