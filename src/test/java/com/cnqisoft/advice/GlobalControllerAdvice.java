package com.cnqisoft.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice implements ResponseBodyAdvice<Object> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Response handleValidationException(HttpServletRequest request, HandlerMethod handlerMethod, BindException ex) {
        logError(request, handlerMethod, ex);

        StringBuilder strBuilder = new StringBuilder();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            strBuilder.append(fieldError.getField()).append(" ").append(fieldError.getDefaultMessage());
        }

        return Response.error(strBuilder.toString());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response handleException(HttpServletRequest request, HandlerMethod handlerMethod, Exception ex) {
        logError(request, handlerMethod, ex);

        return Response.error(ex);
    }

    private void logError(HttpServletRequest request, HandlerMethod handlerMethod, Exception ex) {
        try {
            String requestUri = request.getRequestURI();
            String parameters = objectMapper.writeValueAsString(request.getParameterMap());
            String files = "";
            if (request instanceof MultipartHttpServletRequest) {
                Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
                if (!fileMap.isEmpty()) {
                    Map<String, String> map = new HashMap<>(fileMap.size());
                    for (Map.Entry<String, MultipartFile> fileEntry : fileMap.entrySet()) {
                        map.put(fileEntry.getKey(), fileEntry.getValue().getOriginalFilename());
                    }
                    files = objectMapper.writeValueAsString(map);
                }
            }
            logger.error("发生异常，请求URI:[{}]，请求处理：[{}]，请求参数：[{}]，请求文件:[{}]", requestUri, handlerMethod, parameters, files, ex);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Response) {
            return body;
        }
        return Response.success(body);
    }
}
