package com.krooj.docuserv.service;

import com.krooj.docuserv.DocuservUnitTest;
import com.krooj.docuserv.dm.DocumentDMException;
import com.krooj.docuserv.dm.DocumentDataMapper;
import com.krooj.docuserv.domain.Document;
import com.krooj.docuserv.domain.PhysicalDocument;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Unit tests for the
 */
public class TestDocumentServiceImpl extends DocuservUnitTest{

    private DocumentService documentService;
    private IMocksControl mocksControl;
    private DocumentDataMapper documentDataMapper;


    @Before
    public void setUp(){
        mocksControl = EasyMock.createControl();
        documentDataMapper = mocksControl.createMock(DocumentDataMapper.class);
        documentService = new DocumentServiceImpl(documentDataMapper, DOCUMENT_PATH);

    }

    @Test
    public void testCreateDocument_success() throws Exception{
        //Prepare
        PhysicalDocument document = new PhysicalDocument(DOCUMENT_ID, DOCUMENT_PATH);
        Capture<Document> documentCapture = new Capture<>();
        InputStream testInputStream = loadTestDocument();

        //Expect
        EasyMock.expect(documentDataMapper.newDocumentForMapper(DOCUMENT_ID)).andReturn(document);
        EasyMock.expect(documentDataMapper.validateDocumentExistence(EasyMock.capture(documentCapture))).andReturn(Boolean.FALSE);
        documentDataMapper.createDocument(EasyMock.capture(documentCapture), EasyMock.eq(testInputStream));


        //Replay
        mocksControl.replay();

        //Execute
        documentService.createDocument(DOCUMENT_ID, testInputStream);

        //Assert
        Document createdDocument = documentCapture.getValue();
        Assert.assertNotNull(createdDocument);
        Assert.assertEquals(DOCUMENT_ID, createdDocument.getId());

        //Verify
        mocksControl.verify();
    }

    @Test
    public void testCreateDocument_fail_documentIdAlreadyExists() throws Exception {
        //Prepare
        PhysicalDocument document = new PhysicalDocument(DOCUMENT_ID, DOCUMENT_PATH);
        Capture<Document> documentCapture = new Capture<>();
        InputStream testInputStream = loadTestDocument();

        //Expect
        EasyMock.expect(documentDataMapper.newDocumentForMapper(DOCUMENT_ID)).andReturn(document);
        EasyMock.expect(documentDataMapper.validateDocumentExistence(EasyMock.capture(documentCapture))).andReturn(Boolean.TRUE);


        //Replay
        mocksControl.replay();

        //Execute
        try{
            documentService.createDocument(DOCUMENT_ID, testInputStream);
        }catch(DocuservServiceException e){
            Assert.assertEquals("Document: "+DOCUMENT_ID+" already exists.", e.getMessage());
            Assert.assertNull(e.getCause());
        }

        //Assert
        Document createdDocument = documentCapture.getValue();
        Assert.assertNotNull(createdDocument);
        Assert.assertEquals(DOCUMENT_ID, createdDocument.getId());

        //Verify
        mocksControl.verify();
    }

    @Test
    public void testCreateDocument_fail_DocumentDMException() throws Exception {
        //Prepare
        PhysicalDocument document = new PhysicalDocument(DOCUMENT_ID, DOCUMENT_PATH);
        Capture<Document> documentCapture = new Capture<>();
        InputStream testInputStream = loadTestDocument();
        DocumentDMException documentDMException = new DocumentDMException("Test");

        //Expect
        EasyMock.expect(documentDataMapper.newDocumentForMapper(DOCUMENT_ID)).andReturn(document);
        EasyMock.expect(documentDataMapper.validateDocumentExistence(EasyMock.capture(documentCapture))).andThrow(documentDMException);


        //Replay
        mocksControl.replay();

        //Execute
        try{
            documentService.createDocument(DOCUMENT_ID, testInputStream);
        }catch(DocuservServiceException e){
            Assert.assertEquals("DocumentDMException occurred while trying to create Document: "+DOCUMENT_ID+" with path: "+DOCUMENT_PATH, e.getMessage());
            Assert.assertTrue(e.getCause() instanceof DocumentDMException);
        }

        //Assert
        Document createdDocument = documentCapture.getValue();
        Assert.assertNotNull(createdDocument);
        Assert.assertEquals(DOCUMENT_ID, createdDocument.getId());

        //Verify
        mocksControl.verify();
    }

    @Test
    public void testCreateDocument_fail_nullDocumentId() throws Exception{
        //Prepare
        InputStream testInputStream = loadTestDocument();

        //Expect

        //Replay
        mocksControl.replay();

        //Execute
        try{
            documentService.createDocument(null, testInputStream);
        }catch(DocuservServiceException e){
            Assert.assertEquals("documentId may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }

        //Assert

        //Verify
        mocksControl.verify();
    }

    @Test
    public void testCreateDocument_fail_emptyDocumentId() throws Exception{
        //Prepare
        InputStream testInputStream = loadTestDocument();

        //Expect

        //Replay
        mocksControl.replay();

        //Execute
        try{
            documentService.createDocument(" ", testInputStream);
        }catch(DocuservServiceException e){
            Assert.assertEquals("documentId may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }

        //Assert

        //Verify
        mocksControl.verify();
    }

    @Test
    public void testUpdateDocument_fail_nullDocumentId() throws Exception{
        //Prepare
        InputStream testInputStream = loadTestDocument();

        //Expect

        //Replay
        mocksControl.replay();

        //Execute
        try{
            documentService.updateDocument(null, testInputStream);
        }catch(DocuservServiceException e){
            Assert.assertEquals("documentId may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }

        //Assert

        //Verify
        mocksControl.verify();
    }

    @Test
    public void testUpdateDocument_fail_emptyDocumentId() throws Exception{
        //Prepare
        InputStream testInputStream = loadTestDocument();

        //Expect

        //Replay
        mocksControl.replay();

        //Execute
        try{
            documentService.updateDocument(" ", testInputStream);
        }catch(DocuservServiceException e){
            Assert.assertEquals("documentId may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }

        //Assert

        //Verify
        mocksControl.verify();
    }

    @Test
    public void testRetrieveDocumentById_fail_nullDocumentId() throws Exception {
        //Prepare

        //Expect

        //Replay

        //Execute
        try{
            documentService.retrieveDocumentById(null);
        }catch(DocuservServiceException e){
            Assert.assertEquals("documentId may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }

        //Assert

        //Verify
    }

    @Test
    public void testRetrieveDocumentById_fail_emptyDocumentId() throws Exception {
        //Prepare

        //Expect

        //Replay

        //Execute
        try{
            documentService.retrieveDocumentById(" ");
        }catch(DocuservServiceException e){
            Assert.assertEquals("documentId may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }

        //Assert

        //Verify
    }

    @Test
    public void testDeleteDocument_fail_nullDocumentId() throws Exception {
        //Prepare

        //Expect

        //Replay

        //Execute
        try{
            documentService.deleteDocument(null);
        }catch(DocuservServiceException e){
            Assert.assertEquals("documentId may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }

        //Assert

        //Verify
    }

    @Test
    public void testDeleteDocument_fail_emptyDocumentId() throws Exception {
        //Prepare

        //Expect

        //Replay

        //Execute
        try{
            documentService.deleteDocument(" ");
        }catch(DocuservServiceException e){
            Assert.assertEquals("documentId may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }

        //Assert

        //Verify
    }

    /**
     * Loads the test resource needed from the root of this classes' classloader.
     * The physical file can be found at /src/test/resources/testDocument.txt.
     *
     * @return {@link java.io.InputStream}
     */
    private InputStream loadTestDocument(){
        try(
                InputStream fileInputStream = getClass().getResourceAsStream(TEST_DOCUMENT);
                ){
            return fileInputStream;
        }catch (IOException e){
            e.printStackTrace();
            Assert.fail(e.getMessage());
            return null;
        }
    }

}
