package hello.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


@WebServlet(name="requestParamServlet",urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //전체 파라미터 조회.
        //전체 파라미터를 불러와서 iterator 로 반복하여 출력한다.
        request.getParameterNames().asIterator().forEachRemaining(paramName -> System.out.println("paramName = " + request.getParameter(paramName)));
        //단일 파라미터 조회
        String username = request.getParameter("username");
        String age = request.getParameter("age");
        System.out.println("username = " + username);
        System.out.println("age = " + age);


        response.getWriter().write("OK");
    }
}
