package com.asiainfo.fcm.listener;

import com.asiainfo.fcm.entity.User;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

@WebListener
public class MyHttpSessionListener implements HttpSessionListener, HttpSessionAttributeListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            ServletContext servletContext = session.getServletContext();
            servletContext.removeAttribute(user.getUserId());
        }
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        String name = httpSessionBindingEvent.getName();
        if ("user".equals(name)) {
            User user = (User) httpSessionBindingEvent.getValue();

            HttpSession session = httpSessionBindingEvent.getSession();
            ServletContext servletContext = session.getServletContext();

            HttpSession userSession = (HttpSession) servletContext.getAttribute(user.getUserId());

            if (userSession != null) {
                servletContext.removeAttribute(user.getUserId());
                userSession.invalidate();
            }

            servletContext.setAttribute(user.getUserId(), session);
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {

    }
}
