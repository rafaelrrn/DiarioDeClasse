package com.diario.de.classe.shared.interceptor;

import com.diario.de.classe.shared.util.GeradorDeCurlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Interceptor que registra em log os detalhes de cada requisição e resposta HTTP.
 * Gera o comando curl equivalente para facilitar a reprodução durante debug.
 */
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LogManager.getLogger(RequestLoggingInterceptor.class);
    private long tempoInicial = 0;
    private String body = "";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        tempoInicial = System.currentTimeMillis();

        Map<String, String> headers = new HashMap<>();
        MultiValueMap<String, String> headersToCurl = new LinkedMultiValueMap<>();
        Map<String, String> params = new HashMap<>();
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

        String requestBody = getBufferedReader(request);
        if (requestBody == null) requestBody = "{}";

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
            headersToCurl.add(headerName, headerValue);
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            params.put(entry.getKey(), Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", ""));
        }

        GeradorDeCurlUtil geradorDeCurlUtil = new GeradorDeCurlUtil();
        String curlCommand = geradorDeCurlUtil.getCurl(request.getRequestURL().toString(), params, headersToCurl, httpMethod, requestBody);

        LOG.info("REQUEST");
        LOG.info("METHOD: {} - URL: {}", request.getMethod(), request.getRequestURL());
        LOG.info("HEADERS: {}", headers);
        LOG.info("BODY: {}", requestBody);
        LOG.info("PARAMS: {}", params);
        LOG.info("CURL: {}", curlCommand);

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
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long tempoExecucao = System.currentTimeMillis() - tempoInicial;
        LOG.info("RESPONSE - STATUS {}", response.getStatus());
        if (response.getStatus() == 200) LOG.info("Tempo de execução: {}ms.", tempoExecucao);
        else LOG.error("Tempo de execução: {}ms.", tempoExecucao);
    }
}
