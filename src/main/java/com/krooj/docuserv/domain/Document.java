package com.krooj.docuserv.domain;

import java.util.regex.Pattern;

/**
 * This domain class represents a resource which is actually hosted
 * somewhere on a physical filesystem and acts as an abstraction of
 * that resource to the client.
 */
public abstract class Document {

    private String id;

    /**
     * Don't allow creation of empty domain object
     */
    protected Document(){

    }

    public Document(String id) throws DocuservDomainException{
        setId(id);
    }



    public String getId() {
        return id;
    }

    protected void setId(String id) throws DocuservDomainException{
        validateNotNullOrEmpty(id, "id may not be empty or null");
        validateId(id);
        this.id = id;
    }


    /**
     * Validates that the passed argument is neither null nor empty (trimmed).
     * This method should be replaced with JSR-303.
     *
     * @param target
     * @param message
     * @throws DocuservDomainException
     */
    protected void validateNotNullOrEmpty(String target, String message) throws DocuservDomainException{
        if(target==null || target.trim().length()==0){
            throw new DocuservDomainException(message);
        }
    }

    /**
     * Perform validation on the ID of a document, where validation implies
     * a naming format and length constraints.
     *
     * @param id
     * @throws DocuservDomainException
     */
    protected void validateId(String id) throws DocuservDomainException{
        //TODO: Inject the pattern.
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$");
        //TODO: Inject the length requirement.
        if(!(pattern.matcher(id).matches() && id.trim().length()<=20)){
            throw new DocuservDomainException("Document id must be alpha-numeric with a '.' extension and no more than 20 characters");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;

        Document document = (Document) o;

        if (!id.equals(document.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
