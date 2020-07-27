package com.techminia.collection.config.security;

import com.techminia.collection.util.ConvertToJson;
import com.techminia.collection.util.JsonResponse;
import com.techminia.collection.util.JsonSetErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        try {
            JsonResponse jsonResponse  = JsonSetErrorResponse.setResponse(1010, "Unauthorized", null);
            jsonResponse.setMessage("Don't have privileges to access this resource");
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            httpServletResponse.getWriter().write(ConvertToJson.setJsonString(jsonResponse));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
