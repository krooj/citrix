package com.krooj.docuserv.servlet;

import com.krooj.docuserv.domain.Document;
import com.krooj.docuserv.domain.DocuservDomainException;
import com.krooj.docuserv.service.DocumentService;
import com.krooj.docuserv.service.DocuservServiceException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by admin on 2014-06-24.
 */
public class DocuservWriteListener implements WriteListener {

    private final static Logger LOGGER = LogManager.getLogger(DocuservWriteListener.class.getName());

    private HttpServletResponse response = null;
    private AsyncContext context = null;
    private String documentId;
    private DocumentService documentService;

    public DocuservWriteListener(HttpServletResponse response, AsyncContext context, String documentId, DocumentService documentService) {
        setResponse(response);
        setContext(context);
        setDocumentId(documentId);
        setDocumentService(documentService);
    }

    @Override
    public void onWritePossible() throws IOException {
        try{
            Document document = getDocumentService().retrieveDocumentById(getDocumentId());
            IOUtils.copyLarge(document.getDocumentInputStream(), getResponse().getOutputStream());
            getResponse().getOutputStream().flush();
            getResponse().getOutputStream().close();
        }catch (DocuservDomainException | IOException | DocuservServiceException e){
            LOGGER.error(e);
            getResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.error(throwable);
        getResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public AsyncContext getContext() {
        return context;
    }

    public void setContext(AsyncContext context) {
        this.context = context;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
}
