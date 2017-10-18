package takahashi_keisuke.service;

import static takahashi_keisuke.utils.CloseableUtil.*;
import static takahashi_keisuke.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;

import takahashi_keisuke.beans.User;
import takahashi_keisuke.dao.BranchDataDao;

public class BranchDataService {

	public List<User> getBranchData() {

		Connection connection = null;
		try {
			connection = getConnection();

			BranchDataDao branchDataDao = new BranchDataDao();
			List<User> ret = branchDataDao.getBranchDatas(connection);

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