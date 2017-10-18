package takahashi_keisuke.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import takahashi_keisuke.beans.User;
import takahashi_keisuke.exception.NoRowsUpdatedRuntimeException;
import takahashi_keisuke.service.BranchDataService;
import takahashi_keisuke.service.DepartmentDataService;
import takahashi_keisuke.service.UserService;

@WebServlet(urlPatterns = { "/settings" })
@MultipartConfig(maxFileSize = 100000)
public class SettingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		List<User> branches = new BranchDataService().getBranchData();
		request.setAttribute("branches", branches);

		List<User> departments = new DepartmentDataService().getDepartmentData();
		request.setAttribute("departments", departments);

		List<String> messages = new ArrayList<String>();

		String checkId = request.getParameter("id");
		if(checkId == null){
			messages.add("IDが入力されていないため、管理画面に遷移しました");
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("control");
			return;
		}else if(checkId.matches("^[0-9]$") != true){
			messages.add("不正な値が入力されたため、管理画面に遷移しました");
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("control");
			return;
		}

		User editUser = new UserService().getUser(Integer.parseInt(request.getParameter("id")));

		if(editUser == null){
			messages.add("存在しないIDが入力されたため、管理画面に遷移しました");
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("control");
			return;
		}
		request.setAttribute("editUser", editUser);
		request.getRequestDispatcher("settings.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<String> messages = new ArrayList<String>();
		List<User> branches = new BranchDataService().getBranchData();
		List<User> departments = new DepartmentDataService().getDepartmentData();

		HttpSession session = request.getSession();
		User editUser = getEditUser(request);

		if (isValid(request, messages) == true) {

			try {
				new UserService().update(editUser);

			} catch (NoRowsUpdatedRuntimeException e) {
				messages.add("他の人によって更新されているため、最新のデータを表示しました");
				session.setAttribute("errorMessages", messages);
				response.sendRedirect("settings");
			}
			response.sendRedirect("control");

		} else {
			request.setAttribute("branches", branches);
			request.setAttribute("departments", departments);
			session.setAttribute("errorMessages", messages);

			request.setAttribute("editUser", editUser);
			request.getRequestDispatcher("settings.jsp").forward(request, response);
		}
	}

	private User getEditUser(HttpServletRequest request) throws IOException, ServletException {

		User editUser = new User();

		editUser.setId(Integer.parseInt(request.getParameter("id")));
		editUser.setLoginId(request.getParameter("login_id"));
		editUser.setPassword(request.getParameter("password1"));
		editUser.setName(request.getParameter("name"));
		editUser.setBranchId(Integer.parseInt(request.getParameter("branch_id")));
		editUser.setDepartmentId(Integer.parseInt(request.getParameter("department_id")));
		return editUser;
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		int user_id = Integer.parseInt(request.getParameter("id"));
		String login_id = request.getParameter("login_id");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String name = request.getParameter("name");
		int branch_id = Integer.parseInt(request.getParameter("branch_id"));
		int department_id = Integer.parseInt(request.getParameter("department_id"));

		if (login_id.matches("^[0-9a-zA-Z]{6,20}$") == true) {
			User user = new UserService().getUserLoginId(login_id);
			if(user != null){
				if(user.getId() != user_id){
				messages.add("このログインIDはすでに使用されています");
				}
			}
		}
		if (login_id.matches("^[0-9a-zA-Z]{6,20}$") != true) {
			messages.add("ログインIDは半角英数字で6～20文字以内で入力してください");
		}
		if (password1.equals(password2) != true) {
			messages.add("パスワードが一致しません");
		}
		if (StringUtils.isEmpty(request.getParameter("password1")) != true) {
			if (password1.matches("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]{6,20}$") != true) {
				messages.add("パスワードは空白、または半角文字で6～20文字以内で入力してください");
			}
		}
		if (StringUtils.isEmpty(request.getParameter("password2")) != true) {
			if (password2.matches("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]{6,20}$") != true) {
				messages.add("確認用パスワードは空白、または半角文字で6～20文字以内で入力してください");
			}
		}
		if (name.matches("^.{1,10}$") != true) {
			messages.add("名前は1～10文字以内で入力してください");
		}
		if (branch_id != 1 && department_id == 1 || branch_id != 1 && department_id == 2
				|| branch_id == 1 && department_id == 3 || branch_id == 1 && department_id == 4) {
			messages.add("正しい支店と部署・役職の組み合わせを選んで下さい");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}