package takahashi_keisuke.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import takahashi_keisuke.beans.Comment;
import takahashi_keisuke.beans.User;
import takahashi_keisuke.service.CommentService;

@WebServlet(urlPatterns = { "/newComment" })
public class NewCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		System.out.println(request.getParameter("requestId"));
		System.out.println(request.getParameter("requestJs"));

		HttpSession session = request.getSession();

		List<String> messages = new ArrayList<String>();

		if (isValid(request, messages) == true) {

			User user = (User) session.getAttribute("loginUser");

			Comment comment = new Comment();
			comment.setText(request.getParameter("requestJs"));
			comment.setUserId(user.getId());
			comment.setPostId(Integer.parseInt(request.getParameter("requestId")));
			comment.setBranchId(user.getBranchId());
			comment.setDepartmentId(user.getDepartmentId());

			int autoIncKey = new CommentService().register(comment);
			System.out.println(autoIncKey);

			String responseJson = "{\"responseMessage\" : \"" + autoIncKey + "\"}";
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(responseJson);

			//response.sendRedirect("./");
		} else {
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("./");
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String message = request.getParameter("requestJs");

		if (StringUtils.isNotBlank(message) != true) {
			messages.add("コメントを入力してください");
		}
		if (500 < message.length()) {
			messages.add("コメントは500文字以下で入力してください");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}