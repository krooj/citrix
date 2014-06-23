package com.krooj.docuserv.dm;

/**
 *
 */
public abstract class AbstractDocumentDataMapper implements DocumentDataMapper{

    protected void validateNotNull(Object target, String message) throws DocumentDMException {
        if(target == null){
            throw new DocumentDMException(message);
        }
    }

    /**
     * Validates that the passed argument is neither null nor empty (trimmed).
     * This method should be replaced with JSR-303.
     *
     * @param target
     * @param message
     * @throws com.krooj.docuserv.dm.DocumentDMException
     */
    protected void validateNotNullOrEmpty(String target, String message) throws DocumentDMException {
        if(target==null || target.trim().length()==0){
            throw new DocumentDMException(message);
        }
    }
}
