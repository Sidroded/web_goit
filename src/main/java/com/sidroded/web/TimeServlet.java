package com.sidroded.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Current Time</title></head>");
        out.println("<body>");

        String timezoneParam = request.getParameter("timezone");
        ZoneId zoneId;

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            timezoneParam = timezoneParam.replace(" ", "+");
            zoneId = ZoneId.of(timezoneParam);
        } else {
            zoneId = ZoneOffset.UTC;
        }

        ZonedDateTime time = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        out.println("<h1>Current Time</h1>");
        out.println("<p>" + formatter.format(time) + "</p>");

        out.println("</body>");
        out.println("</html>");
    }

    public void destroy() {
    }
}