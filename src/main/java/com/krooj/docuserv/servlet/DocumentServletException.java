package com.krooj.docuserv.servlet;

import com.krooj.docuserv.DocuservException;

/**
 * Simple exception used to handle error-scenarios in the
 * {@link com.krooj.docuserv.servlet.DocumentServlet}
 */
public class DocumentServletException extends DocuservException{

    public DocumentServletException(String message) {
        super(message);
    }

    public DocumentServletException(String message, Throwable cause) {
        super(message, cause);
    }
}
