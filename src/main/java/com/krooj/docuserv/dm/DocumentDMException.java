package com.krooj.docuserv.dm;

import com.krooj.docuserv.DocuservException;

/**
 * Created by admin on 2014-06-22.
 */
public class DocumentDMException extends DocuservException {

    public DocumentDMException(String message) {
        super(message);
    }

    public DocumentDMException(String message, Throwable cause) {
        super(message, cause);
    }
}
