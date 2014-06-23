package com.krooj.docuserv.service;

import com.krooj.docuserv.DocuservException;

/**
 * The purpose of this class is to propagate other types of exceptions thrown from
 * the service layer to the caller in a digestible and predicable manner.
 */
public class DocuservServiceException extends DocuservException{

    public DocuservServiceException(String message) {
        super(message);
    }

    public DocuservServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
