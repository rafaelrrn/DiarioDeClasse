package com.diario.de.classe.shared.util;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilitário que gera o comando curl equivalente à requisição HTTP recebida.
 * Usado pelo RequestLoggingInterceptor para facilitar a reprodução de chamadas durante debug.
 */
@Component
public class GeradorDeCurlUtil {

    public String getCurl(String url, Map<String, String> params, MultiValueMap<String, String> headers, HttpMethod httpMethod, String body) {
        StringBuilder curl = new StringBuilder();

        curl.append("curl '").append(url).append("?");

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                curl.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        if (params != null && params.size() > 0) curl.deleteCharAt(curl.length() - 1);
        if ((params == null || params.size() == 0) && curl.toString().contains("?")) curl.deleteCharAt(curl.length() - 1);

        curl.append("'");

        Map<String, String> headersMap = new HashMap<>();
        for (String key : headers.keySet()) {
            if (!key.contains("content-length")) headersMap.put(key, headers.getFirst(key));
        }

        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            curl.append(" -H '").append(entry.getKey()).append(": ").append(entry.getValue()).append("'");
        }

        curl.append(" -X ").append(httpMethod.name());

        if (body != null) curl.append(" --data-raw '").append(body).append("'");

        return curl.toString();
    }
}
