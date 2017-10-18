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

		HttpSession session = request.getSession();

		List<String> messages = new ArrayList<String>();

		if (isValid(request, messages) == true) {

			User user = (User) session.getAttribute("loginUser");

			Comment comment = new Comment();
			comment.setText(request.getParameter("comment"));
			comment.setUserId(user.getId());
			comment.setPostId(Integer.parseInt(request.getParameter("id")));
			comment.setBranchId(user.getBranchId());
			comment.setDepartmentId(user.getDepartmentId());

			new CommentService().register(comment);

			response.sendRedirect("./");
		} else {
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("./");
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String message = request.getParameter("comment");

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