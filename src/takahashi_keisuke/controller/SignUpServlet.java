package takahashi_keisuke.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import takahashi_keisuke.beans.User;
import takahashi_keisuke.service.BranchDataService;
import takahashi_keisuke.service.DepartmentDataService;
import takahashi_keisuke.service.UserService;

@WebServlet(urlPatterns = { "/signup" })
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		List<User> branches = new BranchDataService().getBranchData();
		request.setAttribute("branches", branches);

		List<User> departments = new DepartmentDataService().getDepartmentData();
		request.setAttribute("departments", departments);

		request.getRequestDispatcher("signup.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		List<String> messages = new ArrayList<String>();
		List<User> branches = new BranchDataService().getBranchData();
		List<User> departments = new DepartmentDataService().getDepartmentData();

		HttpSession session = request.getSession();
		if (isValid(request, messages) == true) {

			User user = new User();
			user.setLoginId(request.getParameter("login_id"));
			user.setPassword(request.getParameter("password1"));
			user.setName(request.getParameter("name"));
			user.setBranchId(Integer.parseInt(request.getParameter("branch_id")));
			user.setDepartmentId(Integer.parseInt(request.getParameter("department_id")));

			new UserService().register(user);

			response.sendRedirect("control");
		} else {
			request.setAttribute("branches", branches);
			request.setAttribute("departments", departments);

			session.setAttribute("errorMessages", messages);
			request.setAttribute("login_id", request.getParameter("login_id"));
			request.setAttribute("name", request.getParameter("name"));
			request.setAttribute("branch_id", request.getParameter("branch_id"));
			request.setAttribute("department_id", request.getParameter("department_id"));
			request.getRequestDispatcher("signup.jsp").forward(request, response);

		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String login_id = request.getParameter("login_id");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String name = request.getParameter("name");
		int branch_id = Integer.parseInt(request.getParameter("branch_id"));
		int department_id = Integer.parseInt(request.getParameter("department_id"));

		if (login_id.matches("^[0-9a-zA-Z]{6,20}$") == true) {
			User user = new UserService().getUserLoginId(login_id);

			if(user != null){
				messages.add("このログインIDはすでに使用されています");
			}
		}
		if (login_id.matches("^[0-9a-zA-Z]{6,20}$") != true) {
			messages.add("ログインIDは半角英数字で6～20文字以内で入力してください");
		}
		if (password1.equals(password2) != true) {
			messages.add("パスワードが一致しません");
		}
		if (password1.matches("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]{6,20}$") != true) {
			messages.add("パスワードは半角文字で6～20文字以内で入力してください");
		}
		if (password2.matches("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]{6,20}$") != true) {
			messages.add("確認用パスワードは半角文字で6～20文字以内で入力してください");
		}
		if (name.matches("^.{1,10}$") != true) {
			messages.add("名前は1～10文字以内で入力してください");
		}
		if (branch_id != 1 && department_id == 1 || branch_id != 1 && department_id == 2 || branch_id == 1 && department_id == 3 || branch_id == 1 && department_id == 4) {
			messages.add("正しい支店と部署・役職の組み合わせを選んで下さい");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}