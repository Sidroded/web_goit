package com.sidroded.web;

import java.io.IOException;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String timezoneParam = httpRequest.getParameter("timezone");

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            if (!isValidTimezone(timezoneParam)) {
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.setContentType("text/plain");
                httpResponse.getWriter().write("Invalid timezone");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isValidTimezone(String timezone) {
        timezone = timezone.replace(" ", "+");
        String pattern = "UTC[+-]([0-9]|1[0-2])";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(timezone);

        return m.matches();
    }
}
