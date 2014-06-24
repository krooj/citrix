package com.krooj.docuserv.service;

import com.krooj.docuserv.DocuservUnitTest;
import com.krooj.docuserv.dm.DocumentDMException;
import com.krooj.docuserv.dm.DocumentDataMapper;
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
        documentService = new DocumentServiceImpl(documentDataMapper);

    }

    @Test
    public void testCreateDocument_success() throws Exception{
        //Prepare
        InputStream testInputStream = loadTestDocument();

        //Expect
        documentDataMapper.createDocument(DOCUMENT_ID, testInputStream);


        //Replay
        mocksControl.replay();

        //Execute
        documentService.createDocument(DOCUMENT_ID, testInputStream);

        //Assert

        //Verify
        mocksControl.verify();
    }

    @Test
    public void testCreateDocument_fail_documentIdAlreadyExists() throws Exception {
        //Prepare
        InputStream testInputStream = loadTestDocument();
        DocumentDMException dmException = new DocumentDMException("There is already a value associated with the given documentId: "+DOCUMENT_ID);

        //Expect
        documentDataMapper.createDocument(DOCUMENT_ID, testInputStream);
        EasyMock.expectLastCall().andThrow(dmException);

        //Replay
        mocksControl.replay();

        //Execute
        try{
            documentService.createDocument(DOCUMENT_ID, testInputStream);
        }catch(DocuservServiceException e){
            //Assert
            Assert.assertEquals("DocumentDMException occurred while trying to create Document: "+DOCUMENT_ID, e.getMessage());
            Assert.assertNotNull(e.getCause());
            Assert.assertTrue(e.getCause() instanceof DocumentDMException);
            Assert.assertEquals("There is already a value associated with the given documentId: "+DOCUMENT_ID,e.getCause().getMessage());
        }


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
