package com.techminia.collection.config.security;

import com.techminia.collection.util.ConvertToJson;
import com.techminia.collection.util.JsonResponse;
import com.techminia.collection.util.JsonSetErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        try {

            JsonResponse jsonResponse  = JsonSetErrorResponse.setResponse(1010, "Unauthorized", null);
            jsonResponse.setMessage("Full authentication is required to access this resource");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            httpServletResponse.getWriter().write(ConvertToJson.setJsonString(jsonResponse));

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
