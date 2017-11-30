package takahashi_keisuke.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import takahashi_keisuke.service.MessageService;

@WebServlet(urlPatterns = { "/DeleteMessage" })
public class DeleteMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		new MessageService().getDeleteMessage(request.getParameter("requestId"));

		response.sendRedirect("./");
	}
}