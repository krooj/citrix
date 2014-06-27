package com.krooj.docuserv.servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;

import com.krooj.docuserv.service.DocumentService;
import com.krooj.docuserv.service.DocuservServiceException;

/**
 * This {@link javax.servlet.ReadListener} is intended to support asynchronous
 * handling of files being POSTed or PUT to the {@link com.krooj.docuserv.servlet.DocumentServlet}.
 * THis class is only supported in Servlet 3.1 or later.
 *
 * @author Michael Kuredjian
 */
public class DocumentServletReadListener implements ReadListener{

	private AsyncContext asyncContext;
	private ServletInputStream inputStream;
	private HttpServletResponse response;
	private DocumentService documentService;
	private String documentId;

	public DocumentServletReadListener(AsyncContext asyncContext, ServletInputStream inputStream, HttpServletResponse response, DocumentService documentService, String documentId) {
		setAsyncContext(asyncContext);
		setInputStream(inputStream);
		setResponse(response);
		setDocumentService(documentService);
		setDocumentId(documentId);
	}

	/**
	 *
	 * @throws IOException
	 */
	@Override
	public void onDataAvailable() throws IOException {
		try{
			getDocumentService().createDocument(getDocumentId(),getInputStream());
		}catch (DocuservServiceException e){

		}
	}

	@Override
	public void onAllDataRead() throws IOException {

	}

	@Override
	public void onError(Throwable throwable) {
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
}
