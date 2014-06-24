package com.krooj.docuserv.dm;

import com.krooj.docuserv.DocuservUnitTest;

import java.util.concurrent.CountDownLatch;

/**
 * Created by admin on 2014-06-23.
 */
public class InMemoryDocumentUpdaterRunnable extends DocuservUnitTest implements Runnable{

    private CountDownLatch startLatch;
    private CountDownLatch notifcationLatch;
    private DocumentDataMapper documentDataMapper;
    private Exception thrown;

    public InMemoryDocumentUpdaterRunnable(CountDownLatch startLatch, CountDownLatch notifcationLatch, DocumentDataMapper documentDataMapper) {
        this.startLatch = startLatch;
        this.notifcationLatch = notifcationLatch;
        this.documentDataMapper = documentDataMapper;
    }

    @Override
    public void run() {
        try {
            startLatch.await();
            this.documentDataMapper.updateDocument(DOCUMENT_ID,getClass().getResourceAsStream(TEST_DOCUMENT));
        } catch (InterruptedException e) {
            setThrown(e);
        } catch (DocumentDMException e) {
            setThrown(e);
        } finally{
            //This is very necessary, since an exception thrown would mean the caller goes into perpetual awaiting if the
            //count is never reduced.
            notifcationLatch.countDown();
        }
    }

    public Exception getThrown() {
        return thrown;
    }

    public void setThrown(Exception thrown) {
        this.thrown = thrown;
    }
}
