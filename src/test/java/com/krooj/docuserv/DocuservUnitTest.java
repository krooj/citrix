package com.krooj.docuserv;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Top-level abstract class to house all common test constants
 * and other shared attribs
 */
public abstract class DocuservUnitTest {

    public static final String DOCUMENT_ID = "test.bar";
    public static final String DOCUMENT_PATH = "/static/documents";
    public static final String TEST_DOCUMENT = "/testDocument.txt";

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
