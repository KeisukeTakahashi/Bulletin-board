package takahashi_keisuke.service;

import static takahashi_keisuke.utils.CloseableUtil.*;
import static takahashi_keisuke.utils.DBUtil.*;

import java.sql.Connection;

import takahashi_keisuke.beans.User;
import takahashi_keisuke.dao.UserDao;
import takahashi_keisuke.utils.CipherUtil;

public class LoginService {

	public User login(String login_id, String password) {

		Connection connection = null;
		try {
			connection = getConnection();

			UserDao userDao = new UserDao();
			String encPassword = CipherUtil.encrypt(password);
			User user = userDao.getUser(connection, login_id, encPassword);

			commit(connection);

			return user;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	public User getSessionId(int id, String session_id) {

		Connection connection = null;
		try {
			connection = getConnection();

			UserDao userDao = new UserDao();
			User userSessionId = userDao.getSessionId(connection, id, session_id);

			commit(connection);

			return userSessionId;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

//	public User getUserInfo(int id, String login_id) {
//
//		Connection connection = null;
//		try {
//			connection = getConnection();
//
//			UserDao userDao = new UserDao();
//			User userSessionId = userDao.getUserInfo(connection, id, login_id);
//
//			commit(connection);
//
//			return userSessionId;
//		} catch (RuntimeException e) {
//			rollback(connection);
//			throw e;
//		} catch (Error e) {
//			rollback(connection);
//			throw e;
//		} finally {
//			close(connection);
//		}
//	}

	public void updateSessionId(String login_id, String session_id) {

		Connection connection = null;
		try {
			connection = getConnection();

			UserDao userDao = new UserDao();
			userDao.updateSessionId(connection, login_id, session_id);

			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

}