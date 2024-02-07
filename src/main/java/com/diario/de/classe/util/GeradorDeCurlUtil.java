package com.diario.de.classe.util;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Component
public class GeradorDeCurlUtil {
    public String getCurl(String url, Map<String, String> params, MultiValueMap<String, String> headers, HttpMethod httpMethod, String body) {
        StringBuilder curl = new StringBuilder();

        curl.append("curl '").append(url).append("?");

        if(params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                curl.append(key).append("=").append(value).append("&");
            }
        }

        if (params.size() > 0) curl.deleteCharAt(curl.length() - 1); //Remove a última ? se tiver param
        if (params.size() == 0 && curl.toString().contains("?")) curl.deleteCharAt(curl.length() - 1); //Remove a ? se não tiver param

        curl.append("'");

        Map<String, String> headersMap = new HashMap<>();
        for (String key : headers.keySet()) {
            if(!key.contains("content-length")) headersMap.put(key, headers.getFirst(key));
        }

        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            curl.append(" -H '").append(key).append(": ").append(value).append("'");
        }

        curl.append(" -X ").append(httpMethod.name());

        if (body != null) curl.append(" --data-raw '").append(body).append("'");

        return curl.toString();
    }
}