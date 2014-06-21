package com.krooj.docuserv.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This servlet provides a very basic RESTful interface to a document store without
 * consideration for Hypermedia, persistence, redirection, or other niceties.
 *
 * @author Michael Kuredjian
 */
@WebServlet(name = "document-servlet", urlPatterns = {"/storage/documents/*", "/storage/documents"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class DocumentServlet extends HttpServlet {

    private final static Logger LOGGER = LogManager.getLogger(DocumentServlet.class.getName());

    private final static String POST = "POST";
    private final static String PUT = "PUT";

    private ConcurrentMap<String, byte[]> documentMap = new ConcurrentHashMap<>();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            String documentId = extractDocumentId(request.getRequestURI());
            validateDocumentId(documentId.trim());
            LOGGER.info("Got GET request for document: " + documentId + " from host: " + request.getRemoteHost());
            if (!getDocumentMap().containsKey(documentId)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                //Specifically do not set the response type. We have no idea what it is..
                handleFileDownload(documentId, response.getOutputStream());
            }
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
            handleDelete(documentId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DocumentServletException e) {
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
            handleUpload(PUT, documentId, filePart.getInputStream());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DocumentServletException e) {
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
            handleUpload(POST, documentId, filePart.getInputStream());
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (DocumentServletException e) {
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

    private void handleUpload(String method, String documentId, InputStream inputStream) throws DocumentServletException {

        //Simple optimization: validate BEFORE reading from a potentially huge stream.
        if(PUT.equalsIgnoreCase(method) && (!getDocumentMap().containsKey(documentId))){
            throw new DocumentServletException("DocumentId: " + documentId + " cannot be PUT because it does not exist in the documentMap");
        }else if(POST.equals(method) && getDocumentMap().containsKey(documentId)){
            throw new DocumentServletException("DocumentId: " + documentId + " cannot be POST because it already exists in the documentMap");
        }

        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            getDocumentMap().put(documentId, out.toByteArray());


        } catch (IOException e) {
            throw new DocumentServletException("IOException occurred while handling an upload for documentId: " + documentId, e);
        }
    }

    //This could be much more slick if we had servlet 3.1 for async handlers.
    private void handleFileDownload(String documentId, OutputStream outputStream) throws DocumentServletException {

        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(getDocumentMap().get(documentId));
        ) {
            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (IOException e) {
            throw new DocumentServletException("IOException occurred while handling an upload for documentId: " + documentId, e);
        }


    }

    private void handleDelete(String documentId) throws DocumentServletException {

        if (!getDocumentMap().containsKey(documentId)) {
            throw new DocumentServletException("Failed to delete document: " + documentId + " because it was not found in the documentMap");
        }

        getDocumentMap().remove(documentId);

    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= " + contentDisp);
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

    public ConcurrentMap<String, byte[]> getDocumentMap() {
        return documentMap;
    }

    public void setDocumentMap(ConcurrentMap<String, byte[]> documentMap) {
        this.documentMap = documentMap;
    }
}
