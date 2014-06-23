package com.krooj.docuserv;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * Top-level abstract class to house all common test constants
 * and other shared attribs
 */
public abstract class DocuservUnitTest {

    protected static final String DOCUMENT_ID = "test.bar";
    protected static final String DOCUMENT_PATH = "/static/documents";
    protected static final String TEST_DOCUMENT = "/testDocument.txt";

	protected byte[] loadTestDocumentAsByteArray() throws Exception{
		try (
			InputStream inputStream = getClass().getResourceAsStream(TEST_DOCUMENT);
		){
			return IOUtils.toByteArray(inputStream);
		}catch(IOException e){
			throw e;
		}
	}

}
