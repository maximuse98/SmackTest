package servlets;

import connections.Connect;
import dao.UserDao;
import connections.ConnectDb;
import models.Msg;
import models.UserNote;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Properties;

public class UserServlet extends HttpServlet {
    private String login;
    private String password;
    private Connect user;
    private ConnectDb connection;

    public UserServlet() throws IOException {
        connection = new ConnectDb();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Msg msg = new Msg(req.getParameter("login"),
                    req.getParameter("msg"),
                    req.getParameter("time"));
            user.send(req.getParameter("receiver"), msg);
        }
        catch (Exception e){
        }
        try {
            String action = req.getParameter("action");
            String time = req.getParameter("time");
            String login = req.getParameter("login");

            new UserDao(connection).add(new UserNote(login,action,time));
        }catch (Exception e) {
            //e.printStackTrace();
        }

        req.setAttribute("connect", connection);
        req.setAttribute("login", login);
        req.getRequestDispatcher("/pages/user.jsp").
                forward(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        login = request.getParameter("login");
        password = request.getParameter("password");
        request.setAttribute("login",login);
        request.setAttribute("password",password);
        request.setAttribute("connect", connection);
        Connect old = user;

        try {
            user =new Connect(login,password);
            user.connect();
        } catch (Exception e){
            try { old.disconnect(); } catch (Exception f){}
            request.getRequestDispatcher("/pages/main.jsp").
                    forward(request, response);
            return;
        }

        request.getRequestDispatcher("/pages/user.jsp").
                forward(request, response);
    }
}
