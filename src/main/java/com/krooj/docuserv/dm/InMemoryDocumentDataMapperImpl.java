package com.krooj.docuserv.dm;

import com.krooj.docuserv.domain.Document;
import com.krooj.docuserv.domain.DocuservDomainException;
import com.krooj.docuserv.domain.InMemoryDocument;
import org.apache.commons.io.IOUtils;

import javax.enterprise.inject.Default;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of a document store, backed by a concurrent hash map.
 */
@Default
@Named("InMemoryDocumentDataMapperImpl")
public class InMemoryDocumentDataMapperImpl extends AbstractDocumentDataMapper {

    private ConcurrentHashMap<String, InMemoryDocument> documentMap = new ConcurrentHashMap<>();

    @Override
    public void createDocument(String documentId, InputStream documentInputStream) throws DocumentDMException {
        validateNotNull(documentId, "document may not be empty or null");
        validateNotNull(documentInputStream, "documentInputStream may not be null");
        try {
            InMemoryDocument document = new InMemoryDocument(documentId, IOUtils.toByteArray(documentInputStream));
            InMemoryDocument previousValue = getDocumentMap().putIfAbsent(document.getId(), document);
            if(previousValue!=null){
                throw new DocumentDMException("There is already a value associated with the given documentId: "+documentId);
            }
        } catch (IOException e) {
            throw new DocumentDMException("IOException occurred while trying to create document: " + documentId, e);
        } catch (DocuservDomainException e) {
            throw new DocumentDMException("DocuservDomainException occurred while trying to create document: " + documentId, e);
        }
    }

    @Override
    public Document retrieveDocumentById(String documentId) throws DocumentDMException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        InMemoryDocument retrievedDocument = getDocumentMap().get(documentId);
        if (retrievedDocument==null) {
            throw new DocumentDMException("Document identified by documentId: " + documentId + " does not exist in store");
        }
        return retrievedDocument;
    }

    @Override
    public void deleteDocument(String documentId) throws DocumentDMException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        InMemoryDocument removedDocument = getDocumentMap().remove(documentId);
        if (removedDocument==null) {
            throw new DocumentDMException("Document identified by documentId: " + documentId + " does not exist in store");
        }
    }

    @Override
    public void updateDocument(String documentId, InputStream documentInputStream) throws DocumentDMException {
        validateNotNull(documentId, "documentId may not be empty or null");
        validateNotNull(documentInputStream, "documentInputStream may not be null");
        try {
            InMemoryDocument document = new InMemoryDocument(documentId, IOUtils.toByteArray(documentInputStream));
            InMemoryDocument replacedDocument = getDocumentMap().replace(documentId, document);
            if(replacedDocument==null){
                throw new DocumentDMException("Document identified by documentId: " + documentId + " does not exist in store");
            }

        } catch (IOException e) {
            throw new DocumentDMException("IOException occurred while trying to update document: " + documentId, e);
        } catch (DocuservDomainException e) {
            throw new DocumentDMException("DocuservDomainException occurred while trying to update document: " + documentId, e);
        }
    }

    @Override
    public boolean validateDocumentExistence(Document document) throws DocumentDMException {
        return getDocumentMap().containsKey(document.getId());
    }

    protected ConcurrentHashMap<String, InMemoryDocument> getDocumentMap() {
        return documentMap;
    }

    private void setDocumentMap(ConcurrentHashMap<String, InMemoryDocument> documentMap) {
        this.documentMap = documentMap;
    }
}
