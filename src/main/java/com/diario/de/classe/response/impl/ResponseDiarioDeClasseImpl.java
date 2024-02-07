package com.diario.de.classe.response.impl;

import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.response.entity.ErrorInfo;
import com.diario.de.classe.response.entity.Meta;
import com.diario.de.classe.response.entity.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ResponseDiarioDeClasseImpl implements ResponseDiarioDeClasse {
    final private static Logger LOG = LogManager.getLogger(ResponseDiarioDeClasseImpl.class);

    private static final Integer RECORDS_PER_PAGE_DEF = 10;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public ResponseEntity<Object> error(String userMessage, HttpStatus status) {
        ErrorInfo error = new ErrorInfo(userMessage, status);
        return new ResponseEntity<>(error, status);
    }

    public ResponseEntity<Object> error(String userMessage, List<String> developerMessage, HttpStatus status) {
        ErrorInfo error = getRequestContext(userMessage, developerMessage, status);
        LOG.error(error);

        return new ResponseEntity<>(error, status);
    }

    public ResponseEntity<Object> error(String userMessage, List<String> developerMessage, HttpStatus status, String url) {
        ErrorInfo error = new ErrorInfo(userMessage, httpServletRequest.getMethod(), developerMessage, status, url);
        LOG.error(error);

        return new ResponseEntity<>(error, status);
    }

    public ResponseEntity<Object> success(Object data, long timeStend, HttpStatus status) {
        Response<Object> response = new Response<>();
        Meta meta = new Meta();

        Object dataPaginada = new ArrayList<Object>();

        if (data instanceof List<?>) { //Se Lista

            List<?> list = ((List) data);

            if (httpServletRequest != null && httpServletRequest.getParameter("page") != null) {
                meta.setPage(Integer.parseInt(httpServletRequest.getParameter("page")));
            } else {
                meta.setPage(1);
            }
            if (httpServletRequest != null && httpServletRequest.getParameter("rpp") != null) {
                meta.setRpp(Integer.parseInt(httpServletRequest.getParameter("rpp")));
            } else {
                meta.setRpp(RECORDS_PER_PAGE_DEF);
            }
            meta.setSize(list.size());
            meta.setType(Meta.LIST);

            if(httpServletRequest.getParameter("page")!=null) {
                for (int i=(meta.getRpp()*(meta.getPage()-1)); i<(meta.getPage()*meta.getRpp()); i++) {
                    ((ArrayList<Object>) dataPaginada).add(list.get(i));
                }
                response.setResults(dataPaginada);
            }

        } else { //Se Objeto
            meta.setPage(1);
            meta.setRpp(RECORDS_PER_PAGE_DEF);
            meta.setSize(1);
            meta.setType(Meta.OBJECT);
        }

        if(response.getResults()== null) response.setResults(data);

        meta.setVersion("0.0.1");
        meta.setDuration((System.currentTimeMillis() - timeStend));
        response.setMeta(meta);

        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<Object> success(Object data, HttpStatus status) {
        LOG.info("BODY: " + data);

        return success(data, new Date().getTime(), status);
    }

    private ErrorInfo getRequestContext(String userMessage, List<String> developerMessage, HttpStatus status) {
        ErrorInfo error;
        try { //Request normal
            error = new ErrorInfo(userMessage, httpServletRequest.getMethod(), developerMessage, status, httpServletRequest.getRequestURI());
        } catch (IllegalStateException e) { //Request pela cronjob
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest httpServletRequest = attributes.getRequest();
                error = new ErrorInfo(userMessage, httpServletRequest.getMethod(), developerMessage, status, "");
            } else {
                error = new ErrorInfo(userMessage, "unknown", developerMessage, status, "");
            }
        }
        return error;
    }
}
