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

import takahashi_keisuke.beans.User;
import takahashi_keisuke.exception.NoRowsUpdatedRuntimeException;
import takahashi_keisuke.exception.SQLRuntimeException;

public class UserDao {

	public User getUser(Connection connection, String login_id, String password) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE login_id = ? AND password = ?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, login_id);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty() == true) {
				return null;
			} else if (2 <= userList.size()) {
				throw new IllegalStateException("2 <= userList.size()");
			} else {
				return userList.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<User> toUserList(ResultSet rs) throws SQLException {

		List<User> ret = new ArrayList<User>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String login_id = rs.getString("login_id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				int branch_id = rs.getInt("branch_id");
				int department_id = rs.getInt("department_id");
				int is_working = rs.getInt("is_working");
				String session_id = rs.getString("session_id");
				Timestamp createdDate = rs.getTimestamp("created_at");
				Timestamp updatedDate = rs.getTimestamp("updated_at");

				User user = new User();
				user.setId(id);
				user.setLoginId(login_id);
				user.setPassword(password);
				user.setName(name);
				user.setBranchId(branch_id);
				user.setDepartmentId(department_id);
				user.setIsWorking(is_working);
				user.setSessionId(session_id);
				user.setCreatedDate(createdDate);
				user.setUpdatedDate(updatedDate);

				ret.add(user);
			}
			return ret;
		} finally {
			close(rs);
		}
	}

	public void insert(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO users ( ");
			sql.append("login_id");
			sql.append(", password");
			sql.append(", name");
			sql.append(", branch_id");
			sql.append(", department_id");
			sql.append(", created_at");
			sql.append(", updated_at");
			sql.append(") VALUES (");
			sql.append("?"); // login_id
			sql.append(", ?"); // password
			sql.append(", ?"); // name
			sql.append(", ?"); // branch_id
			sql.append(", ?"); // department_id
			sql.append(", CURRENT_TIMESTAMP"); // created_at
			sql.append(", CURRENT_TIMESTAMP"); // updated_at
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, user.getLoginId());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getName());
			ps.setInt(4, user.getBranchId());
			ps.setInt(5, user.getDepartmentId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void update(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE users SET");
			sql.append("  login_id = ?");

			if (StringUtils.isEmpty(user.getPassword()) != true) {
				sql.append(", password = ?");
			}

			sql.append(", name = ?");
			sql.append(", branch_id = ?");
			sql.append(", department_id = ?");
			sql.append(", updated_at = CURRENT_TIMESTAMP");

			sql.append(" WHERE");
			sql.append(" id = ?");

			ps = connection.prepareStatement(sql.toString());

			if (StringUtils.isEmpty(user.getPassword()) != true) {
				ps.setString(1, user.getLoginId());
				ps.setString(2, user.getPassword());
				ps.setString(3, user.getName());
				ps.setInt(4, user.getBranchId());
				ps.setInt(5, user.getDepartmentId());
				ps.setInt(6, user.getId());
			} else {
				ps.setString(1, user.getLoginId());
				ps.setString(2, user.getName());
				ps.setInt(3, user.getBranchId());
				ps.setInt(4, user.getDepartmentId());
				ps.setInt(5, user.getId());
			}

			int count = ps.executeUpdate();
			if (count == 0) {
				throw new NoRowsUpdatedRuntimeException();
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void updateIsWorking(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE users SET");
			sql.append("  is_working = ?");
			sql.append(", updated_at = CURRENT_TIMESTAMP");

			sql.append(" WHERE");
			sql.append(" id = ?");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, user.getIsWorking());
			ps.setInt(2, user.getId());

			int count = ps.executeUpdate();
			if (count == 0) {
				throw new NoRowsUpdatedRuntimeException();
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public User getSessionId(Connection connection, int id, String session_id) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE id = ? AND session_id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setString(2, session_id);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty() == true) {
				return null;
			} else if (2 <= userList.size()) {
				throw new IllegalStateException("2 <= userList.size()");
			} else {
				return userList.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public User getUserInfo(Connection connection, int id, String login_id) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE id = ? AND login_id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setString(2, login_id);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty() == true) {
				return null;
			} else if (2 <= userList.size()) {
				throw new IllegalStateException("2 <= userList.size()");
			} else {
				return userList.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void updateSessionId(Connection connection, String login_id, String session_id) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE users SET");
			sql.append("  session_id = ?");
			sql.append(", updated_at = CURRENT_TIMESTAMP");

			sql.append(" WHERE");
			sql.append(" login_id = ?");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, session_id);
			ps.setString(2, login_id);

			int count = ps.executeUpdate();
			if (count == 0) {
				throw new NoRowsUpdatedRuntimeException();
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public User getUser(Connection connection, int id) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty() == true) {
				return null;
			} else if (2 <= userList.size()) {
				throw new IllegalStateException("2 <= userList.size()");
			} else {
				return userList.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public User getUserLoginId(Connection connection, String userLoginId) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE login_id = ?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, userLoginId);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty() == true) {
				return null;
			} else if (2 <= userList.size()) {
				throw new IllegalStateException("2 <= userList.size()");
			} else {
				return userList.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public List<User> getAllUsers(Connection connection) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users ORDER BY branch_id, department_id";

			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);

			return userList;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public String getBranchName(Connection connection, int user) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM branches WHERE id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, user);

			ResultSet rs = ps.executeQuery();
			String name = null;

			while (rs.next()) {
				name = rs.getString("name");
			}
			return name;

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public String getDepartmentName(Connection connection, int user) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM departments WHERE id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, user);

			ResultSet rs = ps.executeQuery();
			String name = null;

			while (rs.next()) {
				name = rs.getString("name");
			}
			return name;

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}
