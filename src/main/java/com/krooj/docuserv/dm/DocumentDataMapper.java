package com.krooj.docuserv.dm;

import com.krooj.docuserv.domain.Document;

import java.io.InputStream;

/**
 * Interface for crud operations on a {@link com.krooj.docuserv.domain.Document}
 */
public interface DocumentDataMapper {

    /**
     * This will take an {@link java.io.InputStream} which is opened from the caller and a Document instance.
     *
     * @param document
     * @param documentInputStream
     * @throws com.krooj.docuserv.dm.DocumentDMException
     */
    void createDocument(Document document, InputStream documentInputStream) throws DocumentDMException;

    /**
     * Returns a {@link com.krooj.docuserv.domain.Document} via it's id. A {@link com.krooj.docuserv.dm.DocumentDMException}
     * is thrown if no document can be found.
     *
     * @param documentId
     * @return
     * @throws DocumentDMException
     */
    Document retrieveDocumentById(String documentId) throws DocumentDMException;

    /**
     * Removes the {@link com.krooj.docuserv.domain.Document}, specified by it's id, and throws a
     * {@link com.krooj.docuserv.dm.DocumentDMException} if that document cannot be found.
     * @param documentId
     * @throws DocumentDMException
     */
    void deleteDocument(String documentId) throws DocumentDMException;

    /**
     * Updates the contents of an existing document. If the ID is not found, then an {@link com.krooj.docuserv.dm.DocumentDMException}
     * will be thrown.
     * @param documentId
     * @param documentInputStream
     * @throws DocumentDMException
     */
    void updateDocument(String documentId, InputStream documentInputStream) throws DocumentDMException;

    /**
     * Helper method to determine whether the {@link Document} with the specified ID already exists.
     *
     * @param document
     * @return true if the document exists, false otherwise.
     * @throws DocumentDMException
     */
    boolean validateDocumentExistence(Document document) throws DocumentDMException;

    /**
     * Returns a {@link com.krooj.docuserv.domain.Document} which is acceptable for the target mapper.
     * @param documentId
     * @return
     * @throws DocumentDMException
     */
    public Document newDocumentForMapper(String documentId) throws DocumentDMException;

}
