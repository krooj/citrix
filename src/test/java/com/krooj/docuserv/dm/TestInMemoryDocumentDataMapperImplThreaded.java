package com.krooj.docuserv.dm;

import com.krooj.docuserv.DocuservUnitTest;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

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

}
