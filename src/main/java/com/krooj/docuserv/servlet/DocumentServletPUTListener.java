package com.krooj.docuserv.servlet;

import com.krooj.docuserv.service.DocumentService;
import com.krooj.docuserv.service.DocuservServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by michaelk on 2014-06-29.
 */
public class DocumentServletPUTListener implements ReadListener {

    private final static Logger LOGGER = LogManager.getLogger(DocumentServletPUTListener.class.getName());

    private AsyncContext asyncContext;
    private ServletInputStream inputStream;
    private HttpServletResponse response;
    private DocumentService documentService;
    private String documentId;
    private ByteArrayOutputStream byteArrayOutputStream;

    public DocumentServletPUTListener(AsyncContext asyncContext, ServletInputStream inputStream, HttpServletResponse response, DocumentService documentService, String documentId) {
        setAsyncContext(asyncContext);
        setInputStream(inputStream);
        setResponse(response);
        setDocumentService(documentService);
        setDocumentId(documentId);
        setByteArrayOutputStream(new ByteArrayOutputStream());

    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void onDataAvailable() throws IOException {

        byte[] buffer = new byte[1024];
        while (getInputStream().isReady()) {
            int length = getInputStream().read(buffer);
            if(length<0){
                getAsyncContext().complete();
                return;
            }
            getByteArrayOutputStream().write(buffer);
        }
    }

    @Override
    public void onAllDataRead() throws IOException {
        try {
            getDocumentService().updateDocument(getDocumentId(),new ByteArrayInputStream(getByteArrayOutputStream().toByteArray()));
            getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DocuservServiceException e) {
            LOGGER.error(e);
            getResponse().setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }finally {
            getAsyncContext().complete();
        }

    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.error(throwable);
        getAsyncContext().complete();
    }

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    protected void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    public ServletInputStream getInputStream() {
        return inputStream;
    }

    protected void setInputStream(ServletInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    protected void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    protected void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public String getDocumentId() {
        return documentId;
    }

    protected void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    protected ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }
}
