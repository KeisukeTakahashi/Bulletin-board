package takahashi_keisuke.dao;

import static takahashi_keisuke.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import takahashi_keisuke.beans.Comment;
import takahashi_keisuke.exception.SQLRuntimeException;

public class CommentDao {

	public int insert(Connection connection, Comment comment) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int autoIncKey = -1;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO comments ( ");
			sql.append("text");
			sql.append(", branch_id");
			sql.append(", department_id");
			sql.append(", user_id");
			sql.append(", post_id");
			sql.append(", created_at");
			sql.append(", updated_at");
			sql.append(") VALUES (");
			sql.append("?"); // text
			sql.append(", ?"); // branch_id
			sql.append(", ?"); // department_id
			sql.append(", ?"); // user_id
			sql.append(", ?"); // post_id
			sql.append(", CURRENT_TIMESTAMP"); // created_at
			sql.append(", CURRENT_TIMESTAMP"); // updated_at
			sql.append(")");

			ps = connection.prepareStatement(sql.toString(), java.sql.Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, comment.getText());
			ps.setInt(2, comment.getBranchId());
			ps.setInt(3, comment.getDepartmentId());
			ps.setInt(4, comment.getUserId());
			ps.setInt(5, comment.getPostId());

			ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				autoIncKey = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return autoIncKey;
	}
}