package com.krooj.docuserv.domain;

import com.krooj.docuserv.DocuservException;

/**
 * Domain (application) level exception.
 */
public class DocuservDomainException extends DocuservException{

    public DocuservDomainException(String message) {
        super(message);
    }

    public DocuservDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
