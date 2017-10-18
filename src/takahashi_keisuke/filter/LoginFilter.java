package takahashi_keisuke.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import takahashi_keisuke.beans.User;
import takahashi_keisuke.service.LoginService;
import takahashi_keisuke.service.UserService;

@WebFilter("/*")
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest) request).getSession();

		String path = ((HttpServletRequest) request).getServletPath();
		User user = (User) ((HttpServletRequest) request).getSession().getAttribute("loginUser");

		List<String> errormessages = new ArrayList<String>();
		LoginService loginService = new LoginService();

		if (!path.equals("/login") && !path.matches(".*css.*")) {
			if (user == null || user.getIsWorking() == 0) {
				errormessages.add("ログインを行ってください");
				session.setAttribute("errorMessages", errormessages);
				((HttpServletResponse) response).sendRedirect("login");
				return;
			}

			//ユーザー情報を再取得して変化を確認する
			user = new UserService().getUser(user.getId());
			if (user.getIsWorking() == 0) {
				errormessages.add("ログインを行ってください");
				session.setAttribute("errorMessages", errormessages);
				((HttpServletResponse) response).sendRedirect("login");
				return;
			}
			request.setAttribute("loginUser", user);
			user = (User) ((HttpServletRequest) request).getSession().getAttribute("loginUser");

			//ユーザーIDと現在のセッションIDでDBを確認して、返ってきた結果がnullならログアウトさせる
			if(user != null){
				User userSessionId = loginService.getSessionId(user.getId(), session.getId());
				if(userSessionId == null){
					errormessages.add("別のコンピューター、またはブラウザからログインされたため、ログアウトしました");
					session.setAttribute("errorMessages", errormessages);
					((HttpServletResponse) response).sendRedirect("login");
					return;
					}
				}
			}

		if (path.equals("/control") || path.equals("/signup") || path.equals("/settings")) {
			if (user.getDepartmentId() != 1) {
				errormessages.add("該当ページに対する権限が無いため、ホーム画面に遷移しました");
				session.setAttribute("errorMessages", errormessages);
				((HttpServletResponse) response).sendRedirect("./");
				return;
			}
		}

		chain.doFilter(request, response); // サーブレットを実行
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}
}
