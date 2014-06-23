package com.krooj.docuserv.service;

import com.krooj.docuserv.dm.DocumentDMException;
import com.krooj.docuserv.dm.DocumentDataMapper;
import com.krooj.docuserv.domain.Document;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.InputStream;

/**
 * Default named implementation for the {@link com.krooj.docuserv.service.DocumentService}
 *
 */
@Named("documentService")
@Default
public class DocumentServiceImpl implements DocumentService{

    @Inject
    private DocumentDataMapper documentDataMapper;

    private String relativePath;

    /**
     * Creates an instance of this DocumentService with the specified mapper and relative path.
     * @param documentDataMapper
     * @param relativePath
     */
    public DocumentServiceImpl(DocumentDataMapper documentDataMapper, String relativePath){
        setDocumentDataMapper(documentDataMapper);
        setRelativePath(relativePath);
    }

    public DocumentServiceImpl(){

    }

    @Override
    public void createDocument(String documentId, InputStream documentInputStream) throws DocuservServiceException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        validateNotNull(documentInputStream, "documentInputStream may not be null");
        try{
            Document document = getDocumentDataMapper().newDocumentForMapper(documentId);
            //Ensure the document doesn't already exist.
            if(getDocumentDataMapper().validateDocumentExistence(document)){
                throw new DocuservServiceException("Document: "+documentId+" already exists.");
            }
            getDocumentDataMapper().createDocument(document, documentInputStream);
        }catch (DocumentDMException e){
            throw new DocuservServiceException("DocumentDMException occurred while trying to create Document: "+documentId+" with path: "+getRelativePath(), e);
        }
    }

    @Override
    public Document retrieveDocumentById(String documentId) throws DocuservServiceException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        try{
            return getDocumentDataMapper().retrieveDocumentById(documentId);
        } catch (DocumentDMException e) {
            throw new DocuservServiceException("DocumentDMException occurred while trying to retrieve Document: "+documentId+" with path: "+getRelativePath(), e);
        }
    }

    @Override
    public void deleteDocument(String documentId) throws DocuservServiceException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
    }

    @Override
    public void updateDocument(String documentId, InputStream documentInputStream) throws DocuservServiceException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        validateNotNull(documentInputStream, "documentInputStream may not be null");
    }

    protected void validateNotNullOrEmpty(String target, String message) throws DocuservServiceException {
        if(target==null || target.trim().length()==0){
            throw new DocuservServiceException(message);
        }
    }

    protected void validateNotNull(Object target, String message) throws DocuservServiceException{
        if(target == null){
            throw new DocuservServiceException(message);
        }
    }


    public DocumentDataMapper getDocumentDataMapper() {
        return documentDataMapper;
    }

    public void setDocumentDataMapper(DocumentDataMapper documentDataMapper) {
        this.documentDataMapper = documentDataMapper;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
