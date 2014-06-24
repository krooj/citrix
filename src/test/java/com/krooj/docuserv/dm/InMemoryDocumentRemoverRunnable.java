package com.krooj.docuserv.dm;

import com.krooj.docuserv.DocuservUnitTest;

import java.util.concurrent.CountDownLatch;

/**
 * Created by admin on 2014-06-23.
 */
public class InMemoryDocumentRemoverRunnable extends DocuservUnitTest implements Runnable{

    private CountDownLatch startLatch;
    private CountDownLatch notifcationLatch;
    private DocumentDataMapper documentDataMapper;

    public InMemoryDocumentRemoverRunnable(CountDownLatch startLatch, CountDownLatch notifcationLatch, DocumentDataMapper documentDataMapper) {
        this.startLatch = startLatch;
        this.notifcationLatch = notifcationLatch;
        this.documentDataMapper = documentDataMapper;
    }

    @Override
    public void run() {
        try {
            startLatch.await();
            this.documentDataMapper.deleteDocument(DOCUMENT_ID);
        } catch (InterruptedException e) {
            throw new ThreadedRuntimeException(e);
        } catch (DocumentDMException e) {
            throw new ThreadedRuntimeException(e);
        } finally{
            //This is very necessary, since an exception thrown would mean the caller goes into perpetual awaiting if the
            //count is never reduced.
            notifcationLatch.countDown();
        }
    }
}
