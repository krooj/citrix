package com.krooj.docuserv;

/**
 * Top level abstract class for exceptions which originate from
 * this server.
 */
public abstract class DocuservException extends Exception{

    public DocuservException(String message) {
        super(message);
    }

    public DocuservException(String message, Throwable cause) {
        super(message, cause);
    }
}
