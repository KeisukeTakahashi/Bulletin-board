package takahashi_keisuke.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import takahashi_keisuke.beans.User;
import takahashi_keisuke.beans.UserComment;
import takahashi_keisuke.beans.UserMessage;
import takahashi_keisuke.service.BranchDataService;
import takahashi_keisuke.service.CommentService;
import takahashi_keisuke.service.DepartmentDataService;
import takahashi_keisuke.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();

		List<User> branches = new BranchDataService().getBranchData();
		request.setAttribute("branches", branches);

		List<User> departments = new DepartmentDataService().getDepartmentData();
		request.setAttribute("departments", departments);

		List<UserMessage> categorys = new MessageService().getCategoryData();
		request.setAttribute("categorys", categorys);

		String startDay = request.getParameter("start_day");
		String endDay = request.getParameter("end_day");
		String category = request.getParameter("lists.category");
		int count = 0;

		if(StringUtils.isNotBlank(startDay) != true){
			startDay = "2017/09/25";
			count = count + 1;
		}
		if(StringUtils.isNotBlank(endDay) != true){
			Date d = new Date();
			SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
			endDay = day.format(d);
			count = count + 1;
		}

		if(checkDate(startDay) == true && checkDate(endDay) == true){
			List<UserMessage> messages = new MessageService().getMessage(startDay, endDay, category);
			List<UserComment> comments = new CommentService().getComment();

			if(count != 2){
				request.setAttribute("calendar_start",startDay);
				request.setAttribute("calendar_end",endDay);
			}

			request.setAttribute("category",category);
			request.setAttribute("messages", messages);
			request.setAttribute("comments", comments);

			request.getRequestDispatcher("home.jsp").forward(request, response);

		} else {

			//不正な日付なら正しい日付(初期値)を入れて表示
			startDay = "2017/09/25";

			Date d = new Date();
			SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
			endDay = day.format(d);

			List<UserMessage> messages = new MessageService().getMessage(startDay, endDay, category);
			List<UserComment> comments = new CommentService().getComment();

			List<String> errormessages = new ArrayList<String>();
			errormessages.add("カレンダーのフォーマットが不正です");
			errormessages.add("カテゴリーが入力されていた場合は、現在までの日付でカテゴリーを検索して結果を表示します");
			session.setAttribute("errorMessages", errormessages);

			request.setAttribute("category",category);
			request.setAttribute("messages", messages);
			request.setAttribute("comments", comments);

			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		List<User> branches = new BranchDataService().getBranchData();
		request.setAttribute("branches", branches);

		List<User> departments = new DepartmentDataService().getDepartmentData();
		request.setAttribute("departments", departments);

		List<UserMessage> categorys = new MessageService().getCategoryData();
		request.setAttribute("categorys", categorys);

		String startDay = request.getParameter("start_day");
		String endDay = request.getParameter("end_day");
		String category = request.getParameter("lists.category");

		if(StringUtils.isNotBlank(startDay) != true){
			startDay = "2017/09/25";
		}
		if(StringUtils.isNotBlank(endDay) != true){
			Date d = new Date();
			SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
			endDay = day.format(d);
		}
		List<UserMessage> messages = new MessageService().getMessage(startDay, endDay, category);
		List<UserComment> comments = new CommentService().getComment();

		request.setAttribute("category", category);
		request.setAttribute("messages", messages);
		request.setAttribute("comments", comments);

		request.getRequestDispatcher("home.jsp").forward(request, response);
	}

	public static boolean checkDate(String strDate) {
        if (strDate.length() != 10) {
            return false;
        }
        strDate = strDate.replace('-', '/');
        DateFormat format = DateFormat.getDateInstance();
        format.setLenient(false);
        try {
            format.parse(strDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}