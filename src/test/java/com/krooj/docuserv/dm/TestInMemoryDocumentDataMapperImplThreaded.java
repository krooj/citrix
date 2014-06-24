package com.krooj.docuserv.dm;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.krooj.docuserv.DocuservUnitTest;

/**
 * Created by admin on 2014-06-23.
 */
public class TestInMemoryDocumentDataMapperImplThreaded extends DocuservUnitTest {

	public static Exception threadAException = null;
	public static Exception threadBException = null;

	@Test
	public void testDoubleProducer() throws Exception {
		//Prepare
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch notificationLatch = new CountDownLatch(2);
		DocumentDataMapper documentDataMapper = new InMemoryDocumentDataMapperImpl();

		byte[] threadABytes = IOUtils.toByteArray(getClass().getResourceAsStream(TEST_DOCUMENT));
		byte[] threadBBytes = IOUtils.toByteArray(getClass().getResourceAsStream(TEST_DOCUMENT));

		Runnable producerARunnable = new InMemoryDocumentProducerRunnable(startLatch, notificationLatch, documentDataMapper, threadABytes);
		Runnable producerBRunnable = new InMemoryDocumentProducerRunnable(startLatch, notificationLatch, documentDataMapper, threadBBytes);
		Thread producerA = new Thread(producerARunnable);
		Thread producerB = new Thread(producerBRunnable);

		//Execute
		producerA.start();
		producerB.start();
		startLatch.countDown();
		notificationLatch.await();

		//Assert
		Assert.assertFalse(((InMemoryDocumentProducerRunnable) producerARunnable).getThrown() == null && ((InMemoryDocumentProducerRunnable) producerBRunnable).getThrown() == null);
		Exception thrown = ((InMemoryDocumentProducerRunnable) producerARunnable).getThrown() != null ? ((InMemoryDocumentProducerRunnable) producerARunnable).getThrown() : ((InMemoryDocumentProducerRunnable) producerBRunnable).getThrown();

		Assert.assertTrue(thrown instanceof DocumentDMException);
		Assert.assertEquals("There is already a value associated with the given documentId: " + DOCUMENT_ID, thrown.getMessage());

		Assert.assertNotNull(documentDataMapper.retrieveDocumentById(DOCUMENT_ID));
	}

	@Test
	public void testDoubleRemover() throws Exception {
		//Prepare
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch notificationLatch = new CountDownLatch(2);
		DocumentDataMapper documentDataMapper = new InMemoryDocumentDataMapperImpl();
		documentDataMapper.createDocument(DOCUMENT_ID, getClass().getResourceAsStream(TEST_DOCUMENT));

		Runnable producerARunnable = new InMemoryDocumentRemoverRunnable(startLatch, notificationLatch, documentDataMapper);
		Runnable producerBRunnable = new InMemoryDocumentRemoverRunnable(startLatch, notificationLatch, documentDataMapper);
		Thread producerA = new Thread(producerARunnable);
		Thread producerB = new Thread(producerBRunnable);

		//Execute
		producerA.start();
		producerB.start();
		startLatch.countDown();
		notificationLatch.await();

		//Assert
		Assert.assertFalse(((InMemoryDocumentRemoverRunnable) producerARunnable).getThrown() == null && ((InMemoryDocumentRemoverRunnable) producerBRunnable).getThrown() == null);
		Exception thrown = ((InMemoryDocumentRemoverRunnable) producerARunnable).getThrown() != null ? ((InMemoryDocumentRemoverRunnable) producerARunnable).getThrown() : ((InMemoryDocumentRemoverRunnable) producerBRunnable).getThrown();

		Assert.assertTrue(thrown instanceof DocumentDMException);
		Assert.assertEquals("Document identified by documentId: " + DOCUMENT_ID + " does not exist in store", thrown.getMessage());

		try {
			documentDataMapper.retrieveDocumentById(DOCUMENT_ID);
		} catch (DocumentDMException e) {
			Assert.assertEquals("Document identified by documentId: " + DOCUMENT_ID + " does not exist in store", e.getMessage());
		}
	}


}
