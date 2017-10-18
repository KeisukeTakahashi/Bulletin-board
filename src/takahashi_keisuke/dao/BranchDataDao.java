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

public class BranchDataDao {
	public List<User> getBranchDatas(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM branches ");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<User> ret = toBranchDataList(rs);
			return ret;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<User> toBranchDataList(ResultSet rs)
			throws SQLException {

		List<User> ret = new ArrayList<User>();
		try {
			while (rs.next()) {
				int branch_id = rs.getInt("id");
				String branch_name = rs.getString("name");

				User branchData = new User();
				branchData.setBranchId(branch_id);
				branchData.setBranchName(branch_name);

				ret.add(branchData);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
}
