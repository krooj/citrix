package com.krooj.docuserv.servlet;

import com.krooj.docuserv.DocuservUnitTest;
import com.krooj.docuserv.service.DocumentService;
import org.apache.commons.io.IOUtils;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

/**
 * Created by admin on 2014-06-27.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(IOUtils.class)
@PowerMockIgnore("javax.management.*")
public class TestDocumentServletReadListener extends DocuservUnitTest{

    private ReadListener readListener;
    private IMocksControl mocksControl;
    private AsyncContext asyncContext;
    private ServletInputStream servletInputStream;
    private HttpServletResponse servletResponse;
    private DocumentService documentService;

    @Before
    public void setUp(){
        mocksControl = EasyMock.createControl();
        this.asyncContext = mocksControl.createMock(AsyncContext.class);
        this.servletInputStream = mocksControl.createMock(ServletInputStream.class);
        this.servletResponse = mocksControl.createMock(HttpServletResponse.class);
        this.documentService = mocksControl.createMock(DocumentService.class);
        readListener = new DocumentServletReadListener(asyncContext,servletInputStream,servletResponse,documentService,DOCUMENT_ID);
    }

    @Test
    public void testOnDataAvailable_success() throws Exception {
        //Prepare
        PowerMock.mockStatic(IOUtils.class);
        Capture<byte[]> inputStreamByteBufferCapture = new Capture<>();
        Capture<ByteArrayOutputStream> byteArrayOutputStreamcapture = new Capture<>();

        //Expect
        EasyMock.expect(servletInputStream.isReady()).andReturn(Boolean.TRUE).anyTimes();
        EasyMock.expect(servletInputStream.read(EasyMock.capture(inputStreamByteBufferCapture))).andReturn(-1);
        IOUtils.copy(EasyMock.eq(servletInputStream), EasyMock.capture(byteArrayOutputStreamcapture));
        EasyMock.expectLastCall().andReturn(Integer.valueOf(0)).anyTimes();


        //Replay
        mocksControl.replay();
        PowerMock.replayAll();

        //Execute
        this.readListener.onDataAvailable();

        //Assert

        //verify
        mocksControl.verify();
        PowerMock.verifyAll();
    }


    @Test
    public void testOnAllDataRead_success() throws Exception {
        //Prepare

        //Expect

        //Replay

        //Execute

        //Assert

        //verify
    }

    @Test
    public void testOnError_success() throws Exception {
        //Prepare

        //Expect

        //Replay

        //Execute

        //Assert

        //verify
    }
}
