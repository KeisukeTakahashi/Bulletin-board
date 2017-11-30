package takahashi_keisuke.service;

import static takahashi_keisuke.utils.CloseableUtil.*;
import static takahashi_keisuke.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;

import takahashi_keisuke.beans.Comment;
import takahashi_keisuke.beans.UserComment;
import takahashi_keisuke.dao.CommentDao;
import takahashi_keisuke.dao.UserCommentDao;

public class CommentService {

	public int register(Comment comment) {

		Connection connection = null;
		try {
			connection = getConnection();

			CommentDao commentDao = new CommentDao();
			int autoIncKey = commentDao.insert(connection, comment);

			commit(connection);

			return autoIncKey;
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

	private static final int LIMIT_NUM = 1000;

	public List<UserComment> getComment() {

		Connection connection = null;
		try {
			connection = getConnection();

			UserCommentDao commentDao = new UserCommentDao();
			List<UserComment> ret = commentDao.getUserComments(connection, LIMIT_NUM);

			commit(connection);

			return ret;
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

	public void getDeleteComment(String delete) {

		Connection connection = null;
		try {
			connection = getConnection();

			UserCommentDao commentDao = new UserCommentDao();
			commentDao.getUserDeleteComments(connection, LIMIT_NUM, delete);

			commit(connection);

			return;
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