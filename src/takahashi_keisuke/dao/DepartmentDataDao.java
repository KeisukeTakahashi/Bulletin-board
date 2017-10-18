package takahashi_keisuke.dao;

import static takahashi_keisuke.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import takahashi_keisuke.beans.User;
import takahashi_keisuke.exception.SQLRuntimeException;

public class DepartmentDataDao {
	public List<User> getDepartmentDatas(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM departments ");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<User> ret = toDepartmentDataList(rs);
			return ret;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<User> toDepartmentDataList(ResultSet rs)
			throws SQLException {

		List<User> ret = new ArrayList<User>();
		try {
			while (rs.next()) {
				int department_id = rs.getInt("id");
				String department_name = rs.getString("name");

				User departmentData = new User();
				departmentData.setDepartmentId(department_id);
				departmentData.setDepartmentName(department_name);

				ret.add(departmentData);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
}
