package com.sidroded.web;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        JavaxServletWebApplication jswa = JavaxServletWebApplication.buildApplication(this.getServletContext());
        engine = new TemplateEngine();

        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(jswa);
        resolver.setPrefix("WEB-INF/temp/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String timezoneParam = request.getParameter("timezone");
        ZoneId zoneId;
        Cookie[] cookies = request.getCookies();
        String lastTimezoneCookie = getLastTimezoneCookie(cookies);

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            timezoneParam = timezoneParam.replace(" ", "+");
            zoneId = ZoneId.of(timezoneParam);
            setLastTimezoneCookie(timezoneParam, response);
        } else if (lastTimezoneCookie != null) {
            zoneId = ZoneId.of(lastTimezoneCookie);
        } else {
            zoneId = ZoneId.of("UTC+0");
        }

        String currentTime = getCurrentTime(zoneId);

        Context ctx = new Context();
        ctx.setVariable("currentTime", currentTime);
        engine.process("time", ctx, response.getWriter());
    }

    private void setLastTimezoneCookie(String timezone, HttpServletResponse resp) {
        if (timezone != null && !timezone.isEmpty()) {
            Cookie lastTimezone = new Cookie("timezone", timezone);
            resp.addCookie(lastTimezone);
        }
    }

    private String getLastTimezoneCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("timezone".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getCurrentTime(ZoneId zoneId) {
        ZonedDateTime time = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return formatter.format(time);
    }

}