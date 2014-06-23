package com.krooj.docuserv.dm;

import com.krooj.docuserv.domain.Document;
import com.krooj.docuserv.domain.DocuservDomainException;
import com.krooj.docuserv.domain.InMemoryDocument;

import javax.enterprise.inject.Default;
import javax.inject.Named;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of a document store, backed by a concurrent hash map.
 */
@Default
@Named("InMemoryDocumentDataMapperImpl")
public class InMemoryDocumentDataMapperImpl extends AbstractDocumentDataMapper{

    private ConcurrentHashMap<String, InMemoryDocument> documentMap = new ConcurrentHashMap<>();

    @Override
    public void createDocument(Document document, InputStream documentInputStream) throws DocumentDMException {
        validateNotNull(document, "document May not be null");
        validateNotNull(documentInputStream, "documentInputStream may not be null");
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = documentInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            ((InMemoryDocument)document).setDocumentContents(out.toByteArray());

            getDocumentMap().putIfAbsent(document.getId(), (InMemoryDocument) document);


        } catch (IOException e) {
            throw new DocumentDMException("IOException occurred while trying to create document: "+document.getId(), e);
        } catch(DocuservDomainException e){
            throw new DocumentDMException("DocuservDomainException occurred while trying to create document: "+document.getId(), e);
        }
    }

    @Override
    public Document retrieveDocumentById(String documentId) throws DocumentDMException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        if(!getDocumentMap().containsKey(documentId)){
            throw new DocumentDMException("Document identified by documentId: "+documentId+" does not exist in store");
        }
        return getDocumentMap().get(documentId);
    }

    @Override
    public void deleteDocument(String documentId) throws DocumentDMException {
        validateNotNullOrEmpty(documentId, "documentId may not be empty or null");
        if(!getDocumentMap().containsKey(documentId)){
            throw new DocumentDMException("Document identified by documentId: "+documentId+" does not exist in store");
        }
        getDocumentMap().remove(documentId);
    }

    @Override
    public void updateDocument(String documentId, InputStream documentInputStream) throws DocumentDMException {
        validateNotNull(documentId, "documentId may not be null");
        validateNotNull(documentInputStream, "documentInputStream may not be null");
        if(!getDocumentMap().containsKey(documentId)){
            throw new DocumentDMException("Document identified by documentId: "+documentId+" does not exist in store");
        }
        InMemoryDocument document =  getDocumentMap().get(documentId);
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = documentInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            document.setDocumentContents(out.toByteArray());

            getDocumentMap().remove(document.getId(), document);


        } catch (IOException e) {
            throw new DocumentDMException("IOException occurred while trying to update document: "+document.getId(), e);
        } catch(DocuservDomainException e){
            throw new DocumentDMException("DocuservDomainException occurred while trying to update document: "+document.getId(), e);
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

    @Override
    public Document newDocumentForMapper(String documentId) throws DocumentDMException {
        try{
            return new InMemoryDocument(documentId, new byte[]{});
        }catch(DocuservDomainException e){
            throw new DocumentDMException("DocuservDomainException occurred while trying to create InMemoryDocument: "+documentId, e);
        }
    }
}