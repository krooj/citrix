package com.krooj.docuserv.service;

import com.krooj.docuserv.domain.Document;

import java.io.InputStream;

/**
 * This interface defines the actions that can be performed against a
 * {@link com.krooj.docuserv.domain.Document}.
 */
public interface DocumentService {

    /**
     * This will take an {@link java.io.InputStream} which is opened from the caller, and
     * the documentId to create a new {@link Document}. If the documentId is already present,
     * then an exception of type {@link com.krooj.docuserv.service.DocuservServiceException} will
     * be thrown.
     *
     * @param documentId
     * @param documentInputStream
     * @throws DocuservServiceException
     */
    void createDocument(String documentId, InputStream documentInputStream) throws DocuservServiceException;

    /**
     * Returns a {@link com.krooj.docuserv.domain.Document} via it's id. A {@link com.krooj.docuserv.service.DocuservServiceException}
     * is thrown if no document can be found.
     *
     * @param documentId
     * @return
     * @throws DocuservServiceException
     */
    Document retrieveDocumentById(String documentId) throws DocuservServiceException;

    /**
     * Removes the {@link com.krooj.docuserv.domain.Document}, specified by it's id, and throws a
     * {@link com.krooj.docuserv.service.DocuservServiceException} if that document cannot be found.
     * @param documentId
     * @throws DocuservServiceException
     */
    void deleteDocument(String documentId) throws DocuservServiceException;

    /**
     * Updates the contents of an existing document. If the ID is not found, then an {@link com.krooj.docuserv.service.DocuservServiceException}
     * will be thrown.
     * @param documentId
     * @param documentInputStream
     * @throws DocuservServiceException
     */
    void updateDocument(String documentId, InputStream documentInputStream) throws DocuservServiceException;

}
