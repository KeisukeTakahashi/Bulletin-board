package takahashi_keisuke.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import takahashi_keisuke.beans.User;
import takahashi_keisuke.service.UserService;

@WebServlet(urlPatterns = { "/control" })
public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		List<User> users = new UserService().getAllUsers();

		for(User user : users){
			user.setBranchName(new UserService().getBranchName(user.getBranchId()));
			user.setDepartmentName(new UserService().getDepartmentName(user.getDepartmentId()));
		}

		request.setAttribute("users", users);
		request.getRequestDispatcher("control.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		User user = new User();
		user.setId(Integer.parseInt(request.getParameter("id")));
		user.setIsWorking(Integer.parseInt(request.getParameter("is_working")));
		new UserService().updateIsWorking(user);

		response.sendRedirect("control");
	}
}