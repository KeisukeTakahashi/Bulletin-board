package takahashi_keisuke.service;

import static takahashi_keisuke.utils.CloseableUtil.*;
import static takahashi_keisuke.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;

import takahashi_keisuke.beans.User;
import takahashi_keisuke.dao.DepartmentDataDao;

public class DepartmentDataService {
	public List<User> getDepartmentData() {

		Connection connection = null;
		try {
			connection = getConnection();

			DepartmentDataDao departmentDataDao = new DepartmentDataDao();
			List<User> ret = departmentDataDao.getDepartmentDatas(connection);

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
}
