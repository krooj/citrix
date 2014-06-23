package com.krooj.docuserv.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.krooj.docuserv.domain.Document;
import com.krooj.docuserv.domain.DocuservDomainException;
import com.krooj.docuserv.service.DocumentService;
import com.krooj.docuserv.service.DocuservServiceException;

/**
 * This servlet provides a very basic RESTful interface to a document store without
 * consideration for Hypermedia, persistence, redirection, or other niceties.
 *
 * @author Michael Kuredjian
 */
@WebServlet(name = "document-servlet", urlPatterns = {"/storage/documents/*", "/storage/documents"})
@MultipartConfig
public class DocumentServlet extends HttpServlet {

		@Inject
		private DocumentService documentService;

    private final static Logger LOGGER = LogManager.getLogger(DocumentServlet.class.getName());


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            String documentId = extractDocumentId(request.getRequestURI());
            validateDocumentId(documentId.trim());
            LOGGER.info("Got GET request for document: " + documentId + " from host: " + request.getRemoteHost());

						//Specifically do not set the response type. We have no idea what it is..
						handleFileDownload(documentId, response.getOutputStream());

        } catch (DocumentServletException e) {
            LOGGER.error(e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            String documentId = extractDocumentId(request.getRequestURI());
            validateDocumentId(documentId.trim());
            LOGGER.info("Got DELETE request for document: " + documentId + " from host: " + request.getRemoteHost());

						getDocumentService().deleteDocument(documentId);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DocuservServiceException | DocumentServletException e) {
            LOGGER.error(e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


        try {
            Part filePart = request.getPart("document");
            String documentId = getFileName(filePart);
            validateDocumentId(documentId.trim());
            LOGGER.info("Got PUT request for document: " + documentId + " from host: " + request.getRemoteHost());

						getDocumentService().updateDocument(documentId, filePart.getInputStream());

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DocuservServiceException | DocumentServletException e) {
            LOGGER.error(e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * This method will support file upload functionality.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create path components to save the file
        try {
            Part filePart = request.getPart("document");
            //This method is taken from: http://www.journaldev.com/2122/servlet-3-file-upload-using-multipartconfig-annotation-and-part-interface
            //If I were using servlet 3.1, it would be filePart.getSubmittedFileName...
            String documentId = getFileName(filePart);
            validateDocumentId(documentId);
            LOGGER.info("Got POST request for document: " + documentId + " from host: " + request.getRemoteHost());

						getDocumentService().createDocument(documentId, filePart.getInputStream());

            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (DocuservServiceException | DocumentServletException e) {
            LOGGER.error(e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String extractDocumentId(String requestUrl) throws DocumentServletException {
        if (requestUrl == null || requestUrl.length() == 0 || requestUrl.endsWith("/")) {
            throw new DocumentServletException("Malformed document request");
        }
        return requestUrl.substring(requestUrl.lastIndexOf("/") + 1, requestUrl.length());
    }


    //This could be much more slick if we had servlet 3.1 for async handlers.
    private void handleFileDownload(String documentId, OutputStream outputStream) throws DocumentServletException {

        try{
					Document document = getDocumentService().retrieveDocumentById(documentId);
					IOUtils.copyLarge(document.getDocumentInputStream(), outputStream);
        } catch (IOException e) {
            throw new DocumentServletException("IOException occurred while handling an upload for documentId: " + documentId, e);
        } catch (DocuservDomainException e){
						throw new DocumentServletException("DocuservDomainException occurred while writing document input stream to response output stream for document: "+documentId, e);
				} catch (DocuservServiceException e){
						throw new DocumentServletException("DocuservServiceException occurred while writing document input stream to response output stream for document: "+documentId, e);
				}


    }


    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        LOGGER.info("content-disposition header= " + contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    private void validateDocumentId(String documentId) throws DocumentServletException{
        //NB - the spec calls for ALPHANUMERIC with less than or equal to 20 charlen.
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
        Matcher matcher = pattern.matcher(documentId);
        if((!matcher.matches()) || (documentId.length()>20)){
            throw new DocumentServletException("DocumentId is not alphanumeric or is greater tha 20 characters: "+documentId);
        }
    }

	public DocumentService getDocumentService() {
		return documentService;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
}
