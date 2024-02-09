package com.diario.de.classe.interceptor;

import com.diario.de.classe.util.GeradorDeCurlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    final private static Logger LOG = LogManager.getLogger(RequestLoggingInterceptor.class);
    final private static String LOG_MESSAGE = "Tempo de execução: %sms.";
    private static final String LOG_REQUEST_MESSAGE = "REQUEST ";
    private static final String LOG_REQUEST_MESSAGE_METHOD = "METHOD: %s - URL: %s";
    private static final String LOG_REQUEST_MESSAGE_HEADERS = "HEADERS: %s";
    private static final String LOG_REQUEST_MESSAGE_BODY = "BODY: %s";
    private static final String LOG_REQUEST_MESSAGE_PARAMS = "PARAMS: %s";
    private static final String LOG_REQUEST_MESSAGE_CURL = "CURL: %s";
    long tempoInicial = 0;
    private String body = "";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        tempoInicial = System.currentTimeMillis();

        Map<String, String> headers = new HashMap<>();
        MultiValueMap<String, String> headersToCurl = new LinkedMultiValueMap<>();
        Map<String, String> params = new HashMap<>();
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

        String requestBody = getBufferedReader(request);

        if (requestBody == null) {
            requestBody = "{}";
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
            headersToCurl.add(headerName, headerValue);
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();
            String formattedParamValues = Arrays.toString(paramValues).replaceAll("\\[|\\]", "");
            params.put(paramName, formattedParamValues);
        }

        GeradorDeCurlUtil geradorDeCurlUtil = new GeradorDeCurlUtil();
        String curlCommand = geradorDeCurlUtil.getCurl(request.getRequestURL().toString(), params, headersToCurl, httpMethod, requestBody);

        LOG.info(LOG_REQUEST_MESSAGE);
        LOG.info(String.format(LOG_REQUEST_MESSAGE_METHOD, request.getMethod(), request.getRequestURL()));
        LOG.info(String.format(LOG_REQUEST_MESSAGE_HEADERS, headers));
        LOG.info(String.format(LOG_REQUEST_MESSAGE_BODY, requestBody));
        LOG.info(String.format(LOG_REQUEST_MESSAGE_PARAMS, params));
        LOG.info(String.format(LOG_REQUEST_MESSAGE_CURL, curlCommand));

        return true;
    }

    private String getBufferedReader(HttpServletRequest request) throws IOException {
        if (!body.equals("")) return body;

        StringBuilder requestBodyBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBodyBuilder.append(line);
        }
        body = requestBodyBuilder.toString().replaceAll("[\\n\\r\\s]+", "");

        return body;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        // Lógica a ser executada após manipular a solicitação

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws IOException {
        long tempoFinal = System.currentTimeMillis();
        long tempoExecucao = tempoFinal - tempoInicial;

        String LOG_RESPONSE_MESSAGE = "RESPONSE - STATUS %s";

        LOG.info(String.format(LOG_RESPONSE_MESSAGE, response.getStatus()));

        String logResponseMessage = String.format(LOG_MESSAGE, tempoExecucao);
        if (response.getStatus() == 200) LOG.info(logResponseMessage);
        else LOG.error(logResponseMessage);
    }
}
