package com.krooj.docuserv.servlet;

import com.krooj.docuserv.domain.Document;
import com.krooj.docuserv.domain.DocuservDomainException;
import com.krooj.docuserv.service.DocumentService;
import com.krooj.docuserv.service.DocuservServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by michaelk on 2014-06-28.
 */
public class DocumentServletWriteListener implements WriteListener{

    private AsyncContext asyncContext;
    private ServletOutputStream servletOutputStream;
    private DocumentService documentService;
    private Document document;

    private final static Logger LOGGER = LogManager.getLogger(DocumentServletWriteListener.class.getName());

    public DocumentServletWriteListener(AsyncContext asyncContext, ServletOutputStream servletOutputStream, DocumentService documentService, String documentId) throws DocumentServletException {
        setAsyncContext(asyncContext);
        setServletOutputStream(servletOutputStream);
        setDocumentService(documentService);
        prepareDocument(documentId);
    }

    @Override
    public void onWritePossible() throws IOException {
        //Do this the old-school way.
        //See: https://webtide.com/servlet-3-1-async-io-and-jetty/
        //In practice, this isn't different from the Impl of IOUtils#copy, but
        //it is more testable, since no powermock required.
        try{
            byte[] buffer = new byte[1024];
            InputStream documentInputStream = getDocument().getDocumentInputStream();
            while(getServletOutputStream().isReady()){
                int length = documentInputStream.read(buffer);
                if(length<0){
                    getAsyncContext().complete();
                    return;
                }
                getServletOutputStream().write(buffer);
            }
        }catch(DocuservDomainException e){
            LOGGER.error("DocuservDomainException occurred while trying to write contents of document: "+getDocument().getId(),e);
            getAsyncContext().complete();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.error(throwable);
        getAsyncContext().complete();
    }

    protected AsyncContext getAsyncContext() {
        return asyncContext;
    }

    protected void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    protected ServletOutputStream getServletOutputStream() {
        return servletOutputStream;
    }

    protected void setServletOutputStream(ServletOutputStream servletOutputStream) {
        this.servletOutputStream = servletOutputStream;
    }

    protected DocumentService getDocumentService() {
        return documentService;
    }

    protected void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    protected Document getDocument() {
        return document;
    }

    protected void setDocument(Document document){
        this.document = document;
    }

    /**
     * Retrieve the {@link com.krooj.docuserv.domain.Document} via it's id. This method is called
     * when this write listener is instantiated, in preparation for the coming write.
     * @param documentId
     * @throws DocumentServletException
     */
    protected void prepareDocument(String documentId) throws DocumentServletException{
        try{
            Document document = getDocumentService().retrieveDocumentById(documentId);
            setDocument(document);
        }catch(DocuservServiceException e){
            throw new DocumentServletException("DocuservServiceException occurred while trying to retrieve document: "+documentId, e);
        }
    }
}
