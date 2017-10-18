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

import takahashi_keisuke.beans.Message;
import takahashi_keisuke.beans.User;
import takahashi_keisuke.beans.UserMessage;
import takahashi_keisuke.service.MessageService;

@WebServlet(urlPatterns = { "/post" })
public class NewMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		List<UserMessage> categorys = new MessageService().getCategoryData();
		request.setAttribute("categorys", categorys);

		request.getRequestDispatcher("post.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		List<String> messages = new ArrayList<String>();

		List<UserMessage> categorys = new MessageService().getCategoryData();
		request.setAttribute("categorys", categorys);

		if (isValid(request, messages) == true) {

			User user = (User) session.getAttribute("loginUser");

			Message message = new Message();
			message.setSubject(request.getParameter("subject"));
			message.setCategory(request.getParameter("lists.category"));
			message.setText(request.getParameter("message"));
			message.setUserId(user.getId());
			message.setBranchId(user.getBranchId());
			message.setDepartmentId(user.getDepartmentId());

			new MessageService().register(message);

			response.sendRedirect("./");
		} else {

			request.setAttribute("subject", request.getParameter("subject"));
			request.setAttribute("category", request.getParameter("lists.category"));
			request.setAttribute("message", request.getParameter("message"));
			request.setAttribute("errorMessages", messages);
			request.getRequestDispatcher("post.jsp").forward(request, response);
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String subject = request.getParameter("subject");
		String category = request.getParameter("lists.category");
		String message = request.getParameter("message");

		if(category.indexOf(" ") != -1 || category.indexOf("　") != -1){
			messages.add("空白を含む文字列はカテゴリーとして登録できません");
        }
		if (StringUtils.isNotBlank(subject) != true) {
			messages.add("件名を入力してください");
		}
		if (StringUtils.isNotBlank(category) != true) {
			messages.add("カテゴリーを入力してください");
		}
		if (StringUtils.isNotBlank(message) != true) {
			messages.add("本文を入力してください");
		}
		if (30 < subject.length()) {
			messages.add("件名は30文字以下で入力してください");
		}
		if (10 < category.length()) {
			messages.add("カテゴリーは10文字以下で入力してください");
		}
		if (1000 < message.length()) {
			messages.add("本文は1000文字以下で入力してください");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}