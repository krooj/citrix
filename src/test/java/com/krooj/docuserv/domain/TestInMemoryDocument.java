package com.krooj.docuserv.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.krooj.docuserv.DocuservUnitTest;

/**
 * Unit test cases for the {@link com.krooj.docuserv.domain.InMemoryDocument}
 * {@link com.krooj.docuserv.domain.Document} implementation.
 */
public class TestInMemoryDocument extends DocuservUnitTest{



	@Test
	public void testCreateDocument_success() throws Exception {
		//Prepare
		byte[] documentContents = loadTestDocumentAsByteArray();

		//Expect

		//Replay

		//Execute
		InMemoryDocument document = new InMemoryDocument(DOCUMENT_ID, documentContents);

		//Assert
		Assert.assertNotNull(document);
		Assert.assertEquals(DOCUMENT_ID, document.getId());
		Assert.assertEquals(documentContents, document.getDocumentContents());

		//Verify
	}

	@Test
	public void testCreateDocument_fail_malformedId_trailingSlash() throws Exception {
		//Prepare
		byte[] documentContents = loadTestDocumentAsByteArray();

		//Expect

		//Replay

		//Execute
		try{
			new InMemoryDocument("test.bar/", documentContents);
		}catch(DocuservDomainException e){
			Assert.assertEquals("Document id must be alpha-numeric with a '.' extension and no more than 20 characters", e.getMessage());
			Assert.assertNull(e.getCause());
		}


		//Verify
	}

	@Test
	public void testCreateDocument_fail_malformedId_null() throws Exception {
		//Prepare
		byte[] documentContents = loadTestDocumentAsByteArray();

		//Expect

		//Replay

		//Execute
		try{
			new InMemoryDocument(null, documentContents);
		}catch(DocuservDomainException e){
			Assert.assertEquals("id may not be empty or null", e.getMessage());
			Assert.assertNull(e.getCause());
		}


		//Verify
	}

	@Test
	public void testCreateDocument_fail_malformedId_empty() throws Exception {
		//Prepare
		byte[] documentContents = loadTestDocumentAsByteArray();

		//Expect

		//Replay

		//Execute
		try{
			new InMemoryDocument(" ", documentContents);
		}catch(DocuservDomainException e){
			Assert.assertEquals("id may not be empty or null", e.getMessage());
			Assert.assertNull(e.getCause());
		}


		//Verify
	}

	@Test
	public void testCreateDocument_fail_nullDocumentContents() throws Exception {
		//Prepare


		//Expect

		//Replay

		//Execute
		try{
			new InMemoryDocument(DOCUMENT_ID, null);
		}catch(DocuservDomainException e){
			Assert.assertEquals("documentContents may not be null", e.getMessage());
			Assert.assertNull(e.getCause());
		}


		//Verify
	}

	@Test
	public void testDocumentEquals_success_expectedEquals() throws Exception{

		Document documentA = new InMemoryDocument(DOCUMENT_ID,new byte[]{});
		Document documentB = new InMemoryDocument(DOCUMENT_ID,new byte[]{});

		Assert.assertTrue(documentA.equals(documentB));
		Assert.assertTrue(documentB.equals(documentA));
	}

	@Test
	public void testDocumentEquals_success_expectedNonEquals() throws Exception{

		Document documentA = new InMemoryDocument("bar.txt",new byte[]{});
		Document documentB = new InMemoryDocument(DOCUMENT_ID,new byte[]{});

		Assert.assertFalse(documentA.equals(documentB));
		Assert.assertFalse(documentB.equals(documentA));
	}

	@Test
	public void testDocumentHashcode_collision() throws Exception{
		Document documentA = new InMemoryDocument(DOCUMENT_ID,new byte[]{});
		Document documentB = new InMemoryDocument(DOCUMENT_ID,new byte[]{});
		Set<Document> documents = new LinkedHashSet<>();
		documents.add(documentA);
		Assert.assertTrue(documents.contains(documentB));
	}

	@Test
	public void testDocumentHashcode_noCollision() throws Exception{
		Document documentA = new InMemoryDocument("bar.txt",new byte[]{});
		Document documentB = new InMemoryDocument(DOCUMENT_ID,new byte[]{});
		Set<Document> documents = new LinkedHashSet<>();
		documents.add(documentA);
		Assert.assertFalse(documents.contains(documentB));
	}

	/**
	 * Constructs a testable {@link com.krooj.docuserv.domain.InMemoryDocument}
	 * @return
	 * @throws Exception
	 */
	public static InMemoryDocument createDocument() throws Exception{

		return new InMemoryDocument(DOCUMENT_ID, new byte[]{});
	}
	
}
