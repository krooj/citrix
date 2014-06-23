package com.krooj.docuserv.domain;

/**
 * Implementation of the {@link Document} which stores the contents of the document in-memory as
 * a byte array.
 */
public class InMemoryDocument extends Document {

    private byte[] documentContents;

    public InMemoryDocument(String id, byte[] documentContents) throws DocuservDomainException{
        super(id);
        setDocumentContents(documentContents);
    }

    public byte[] getDocumentContents() {
        return documentContents;
    }

    public void setDocumentContents(byte[] documentContents) throws DocuservDomainException{
        validateNotNull(documentContents, "documentContents may not be null");
        this.documentContents = documentContents;
    }

    protected void validateNotNull(Object target, String message) throws DocuservDomainException{
        if(target==null){
            throw new DocuservDomainException(message);
        }
    }
}
