package com.krooj.docuserv.dm;

import com.krooj.docuserv.DocuservUnitTest;

import java.io.ByteArrayInputStream;
import java.util.concurrent.CountDownLatch;

/**
 * Created by admin on 2014-06-23.
 */
public class InMemoryDocumentProducerRunnable extends DocuservUnitTest implements Runnable{

    private CountDownLatch startLatch;
    private CountDownLatch notifcationLatch;
    private DocumentDataMapper documentDataMapper;
    private ByteArrayInputStream byteArrayInputStream;
    private Exception thrown;


    public InMemoryDocumentProducerRunnable(CountDownLatch startLatch, CountDownLatch notifcationLatch, DocumentDataMapper documentDataMapper, byte[] bytes) {
        this.startLatch = startLatch;
        this.notifcationLatch = notifcationLatch;
        this.documentDataMapper = documentDataMapper;
        this.byteArrayInputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public void run() {
        try {
            startLatch.await();
            this.documentDataMapper.createDocument(DOCUMENT_ID, byteArrayInputStream);
        } catch (InterruptedException e) {
            setThrown(e);
        } catch (DocumentDMException e) {
            setThrown(e);
        }finally{
            //This is very necessary, since an exception thrown would mean the caller goes into perpetual awaiting if the
            //count is never reduced.
            notifcationLatch.countDown();
        }
    }

    public Exception getThrown() {
        return thrown;
    }

    private void setThrown(Exception thrown) {
        this.thrown = thrown;
    }
}
