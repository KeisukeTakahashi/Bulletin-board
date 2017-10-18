package takahashi_keisuke.dao;

import static takahashi_keisuke.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import takahashi_keisuke.beans.UserComment;
import takahashi_keisuke.exception.SQLRuntimeException;

public class UserCommentDao {

	public List<UserComment> getUserComments(Connection connection, int num) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM users_comments ");
			sql.append("ORDER BY created_at ASC limit " + num);

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<UserComment> ret = toUserCommentList(rs);
			return ret;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserComment> toUserCommentList(ResultSet rs)
			throws SQLException {

		List<UserComment> ret = new ArrayList<UserComment>();
		try {
			while (rs.next()) {
				String login_id = rs.getString("login_id");
				String name = rs.getString("name");
				int id = rs.getInt("id");
				String text = rs.getString("text");
				int branch_id = rs.getInt("branch_id");
				int department_id = rs.getInt("department_id");
				int userId = rs.getInt("user_id");
				int postId = rs.getInt("post_id");
				Timestamp createdDate = rs.getTimestamp("created_at");

				UserComment comment = new UserComment();
				comment.setLoginId(login_id);
				comment.setName(name);
				comment.setId(id);
				comment.setText(text);
				comment.setBranchId(branch_id);
				comment.setDepartmentId(department_id);
				comment.setUserId(userId);
				comment.setPostId(postId);
				comment.setCreatedDate(createdDate);

				ret.add(comment);
			}
			return ret;
		} finally {
			close(rs);
		}
	}

	public void getUserDeleteComments(Connection connection, int num, String delete) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM comments WHERE id = ?");

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, delete);

			ps.executeUpdate();
			return;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

}