package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Action;

import databean.FavoriteBean;
import databean.UserBean;
import formbean.LoginForm;
import model.FavoriteListDAO;
import model.Model;
import model.MyDAOException;
import model.UserDAO;


public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public  UserDAO userDAO;
	public FavoriteListDAO favoriteListDAO;
	public void init() throws ServletException {
        Model model = new Model(getServletConfig());
        userDAO = model.getUserDAO();
        favoriteListDAO = model.getItemDAO();
        Action.add(new AddAction(model));
        Action.add(new DeleteAction(model));
        Action.add(new LoginAction(model));
        Action.add(new LogoutAction(model));
        Action.add(new FavoriteList(model));
        Action.add(new ChangePassword(model));
        Action.add(new ListAction(model));
        Action.add(new RegisterAction(model));
        Action.add(new CountAction(model));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   
    	try {
            // Set up user list for nav bar
			//System.out.println("get");
			request.setAttribute("userList",userDAO.readuser());
			HttpSession session = request.getSession();
			UserBean user=(UserBean)session.getAttribute("user");
			try{request.setAttribute("favoriteList", favoriteListDAO.getItems(user.getUserid()));}
			catch(NullPointerException e){e.printStackTrace();}

        } catch (MyDAOException e) {
        	e.printStackTrace();
        }
    	String nextPage = performTheAction(request);
        System.out.println(nextPage);
        sendToNextPage(nextPage,request,response);
    }
    
    /*
     * Extracts the requested action and (depending on whether the user is logged in)
     * perform it (or make the user login).
     * @param request
     * @return the next page (the view)
     */
    private String performTheAction(HttpServletRequest request) {
        HttpSession session     = request.getSession(true);
        String      servletPath = request.getServletPath();
        UserBean        user = (UserBean) session.getAttribute("user");
        String      action = getActionName(servletPath);

        if (action.equals("register.do") || action.equals("login.do")||action.equals("error.jsp")) {
        	// Allow these actions without logging in
			return Action.perform(action,request);
        }
        if(action.equals("list.do")||action.equals("count.do")){return Action.perform(action,request);}
        if (user == null) {
        	
        	// If the user hasn't logged in, direct him to the login page
			return "login.do";
        }
        
        if (action.equals("start")) {
        	// If he's logged in but back at the /start page, send him to manage his pics
			return Action.perform("FavoriteList.do",request);
        }

      	// Let the logged in user run his chosen action
		return Action.perform(action,request);
    }
    
    /*
     * If nextPage is null, send back 404
     * If nextPage ends with ".do", redirect to this page.
     * If nextPage ends with ".jsp", dispatch (forward) to the page (the view)
     *    This is the common case
     */
    private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	if (nextPage == null) {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND,request.getServletPath());
    		return;
    	}
    	
    	if (nextPage.endsWith(".do")) {
			response.sendRedirect(nextPage);
			return;
    	}
    	
    	if (nextPage.endsWith(".jsp")) {
	   		RequestDispatcher d = request.getRequestDispatcher(nextPage);
	   		d.forward(request,response);
	   		return;
    	}
    	
    	if(nextPage.startsWith("http://"))
    		{
    		response.sendRedirect(nextPage);
    		return;
    	}
    	throw new ServletException(Controller.class.getName()+".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
    }

	/*
	 * Returns the path component after the last slash removing any "extension"
	 * if present.
	 */
    private String getActionName(String path) {
    	// We're guaranteed that the path will start with a slash
        int slash = path.lastIndexOf('/');
        return path.substring(slash+1);
    }
}
