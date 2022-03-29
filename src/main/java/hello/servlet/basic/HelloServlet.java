package hello.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
        System.out.println("req = " + req);
        System.out.println("resp = " + resp);

        String id = req.getParameter("id");
        String pw = req.getParameter("pw");

        if (id.equals("abcd") && pw.equals("1234")) {
            resp.getWriter().write("<script>alert('login ok');</script>");
        } else {
            resp.getWriter().write("<script>alert('login fail');</script>");
        }
    }
}
