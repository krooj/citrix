package com.krooj.docuserv.service;

import com.krooj.docuserv.dm.DocumentDMException;
import com.krooj.docuserv.dm.DocumentDataMapper;
import com.krooj.docuserv.domain.Document;
import com.krooj.docuserv.domain.DocuservDomainException;

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

    /**
     * Creates an instance of this DocumentService with the specified mapper
     * @param documentDataMapper
     */
    public DocumentServiceImpl(DocumentDataMapper documentDataMapper){
        setDocumentDataMapper(documentDataMapper);
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
            throw new DocuservServiceException("DocumentDMException occurred while trying to create Document: "+documentId, e);
        }
    }

    @Override
    public Document retrieveDocumentById(String documentId) throws DocuservServiceException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        try{
            Document.validateId(documentId);
            return getDocumentDataMapper().retrieveDocumentById(documentId);
        } catch (DocumentDMException e) {
            throw new DocuservServiceException("DocumentDMException occurred while trying to retrieve Document: "+documentId, e);
        } catch (DocuservDomainException e) {
            throw new DocuservServiceException("DocuservDomainException occurred while trying to retrieve Document: "+documentId, e);
        }
    }

    @Override
    public void deleteDocument(String documentId) throws DocuservServiceException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        try{
            Document.validateId(documentId);
            getDocumentDataMapper().deleteDocument(documentId);
        }catch (DocumentDMException e) {
            throw new DocuservServiceException("DocumentDMException occurred while trying to delete Document: "+documentId, e);
        } catch (DocuservDomainException e) {
            throw new DocuservServiceException("DocuservDomainException occurred while trying to delete Document: "+documentId, e);
        }

    }

    @Override
    public void updateDocument(String documentId, InputStream documentInputStream) throws DocuservServiceException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        validateNotNull(documentInputStream, "documentInputStream may not be null");
        try{
            Document.validateId(documentId);
            getDocumentDataMapper().updateDocument(documentId, documentInputStream);
        }catch (DocumentDMException e) {
            throw new DocuservServiceException("DocumentDMException occurred while trying to update Document: "+documentId, e);
        } catch (DocuservDomainException e) {
            throw new DocuservServiceException("DocuservDomainException occurred while trying to update Document: "+documentId, e);
        }
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


}
