package com.diario.de.classe.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalWrapFilter implements Filter {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        MultiReadRequest wrapper = new MultiReadRequest((HttpServletRequest) request);
        chain.doFilter(wrapper, response);
    }

    @Override
    public void destroy() {
    }

    class MultiReadRequest extends HttpServletRequestWrapper {

        private String requestBody;

        @SneakyThrows
        public MultiReadRequest(HttpServletRequest request) {
            super(request);
            if (isMultipart(request)) {
                // Return the original InputStream if the request is multipart
                requestBody = null;
            } else {
                // Read the request body and store it in requestBody
                requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if(requestBody !=null ) {
                final ByteArrayInputStream byteArrayInputStream;

                byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes());

                return new ServletInputStream() {
                    @Override
                    public boolean isFinished() {
                        return byteArrayInputStream.available() == 0;
                    }

                    @Override
                    public boolean isReady() {
                        return true;
                    }

                    @Override
                    public void setReadListener(ReadListener readListener) {

                    }

                    @Override
                    public int read() throws IOException {
                        return byteArrayInputStream.read();
                    }
                };
            }
            return null;
        }

        @Override
        @SneakyThrows
        public BufferedReader getReader() {
            if(this.getInputStream() != null)            return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
            return new BufferedReader(new InputStreamReader(new EmptyInputStream()));
        }
        private class EmptyInputStream extends InputStream {
            @Override
            public int read() throws IOException {
                return -1;  // Retorna -1 para indicar o fim do stream
            }
        }

        private boolean isMultipart(HttpServletRequest request) {
            String contentType = request.getContentType();
            return contentType != null && contentType.toLowerCase().startsWith("multipart/");
        }
    }
}
