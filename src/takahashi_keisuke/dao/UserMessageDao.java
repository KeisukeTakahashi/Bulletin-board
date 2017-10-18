package takahashi_keisuke.dao;

import static takahashi_keisuke.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import takahashi_keisuke.beans.UserMessage;
import takahashi_keisuke.exception.SQLRuntimeException;

public class UserMessageDao {

	public List<UserMessage> getUserMessages(Connection connection, int num, String startDay, String endDay, String category) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM users_posts ");
			sql.append("WHERE created_at >= ? ");
			sql.append("AND created_at <= ? ");
			if(StringUtils.isNotBlank(category) == true){
				String[] categorys = category.replaceAll("　", " ").split(" ",0);
				sql.append("AND category = ? ");

				for(int i = 1; i < categorys.length; i++){
					sql.append("OR created_at >= ? ");
					sql.append("AND created_at <= ? ");
					sql.append("AND category = ? ");
				}
			}
			sql.append("ORDER BY created_at DESC limit " + num);

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, startDay);
			ps.setString(2, endDay + " 23:59:59");
			if(StringUtils.isNotBlank(category) == true){
				String[] categorys = category.replaceAll("　", " ").split(" ",0);
				ps.setString(3, categorys[0]);

				for(int i = 1; i < categorys.length; i++){
					ps.setString(i*3+1, startDay);
					ps.setString(i*3+2, endDay + " 23:59:59");
					ps.setString(i*3+3, categorys[i]);
				}
			}

			ResultSet rs = ps.executeQuery();
			List<UserMessage> ret = toUserMessageList(rs);
			return ret;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserMessage> toUserMessageList(ResultSet rs)
			throws SQLException {

		List<UserMessage> ret = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				String login_id = rs.getString("login_id");
				String name = rs.getString("name");
				int id = rs.getInt("id");
				int userId = rs.getInt("user_id");
				String subject = rs.getString("subject");
				String category = rs.getString("category");
				String text = rs.getString("text");
				int branch_id = rs.getInt("branch_id");
				int department_id = rs.getInt("department_id");
				Timestamp createdDate = rs.getTimestamp("created_at");

				UserMessage message = new UserMessage();
				message.setLoginId(login_id);
				message.setName(name);
				message.setId(id);
				message.setUserId(userId);
				message.setSubject(subject);
				message.setCategory(category);
				message.setText(text);
				message.setBranchId(branch_id);
				message.setDepartmentId(department_id);
				message.setCreatedDate(createdDate);

				ret.add(message);
			}
			return ret;
		} finally {
			close(rs);
		}
	}

	public void getUserDeleteMessages(Connection connection, int num, String delete) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM posts WHERE id = ?");

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

	public List<UserMessage> getCategoryDatas(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT DISTINCT category FROM posts ");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<UserMessage> ret = toCategoryList(rs);
			return ret;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserMessage> toCategoryList(ResultSet rs)
			throws SQLException {

		List<UserMessage> ret = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				String category = rs.getString("category");

				UserMessage message = new UserMessage();
				message.setCategory(category);

				ret.add(message);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
}