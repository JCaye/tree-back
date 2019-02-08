package com.desafio.tree.exceptions.handler;

import com.desafio.tree.exceptions.DescendantOfSelfException;
import com.desafio.tree.exceptions.NodeNotFoundException;
import com.desafio.tree.exceptions.ParentNotFoundExcpetion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @Autowired
    public RestResponseEntityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler({
            NodeNotFoundException.class
    })
    protected ResponseEntity<Object> handleResouceNotFound(NodeNotFoundException e, WebRequest request) throws JsonProcessingException {
        final ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("code", 404);
        errorNode.put("error", e.getMessage());
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(e, objectMapper.writeValueAsString(errorNode), headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            ParentNotFoundExcpetion.class
    })
    protected ResponseEntity<Object> handleResouceNotFound(ParentNotFoundExcpetion e, WebRequest request) throws JsonProcessingException {
        final ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("code", 404);
        errorNode.put("error", e.getMessage());
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(e, objectMapper.writeValueAsString(errorNode), headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            DescendantOfSelfException.class
    })
    protected ResponseEntity<Object> handleResouceNotFound(DescendantOfSelfException e, WebRequest request) throws JsonProcessingException {
        final ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("code", 400);
        errorNode.put("error", e.getMessage());
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(e, objectMapper.writeValueAsString(errorNode), headers, HttpStatus.BAD_REQUEST, request);
    }


}
