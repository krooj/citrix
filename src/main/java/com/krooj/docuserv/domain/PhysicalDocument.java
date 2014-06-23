package com.krooj.docuserv.domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Implementation of {@link Document} which backs a physical file on a file-system.
 */
public class PhysicalDocument extends Document {

    private String physicalLocation;

    public PhysicalDocument(String id, String physicalLocation) throws DocuservDomainException {
        super(id);
        setPhysicalLocation(physicalLocation);
    }

    public String getPhysicalLocation() {
        return physicalLocation;
    }

    protected void setPhysicalLocation(String physicalLocation) throws DocuservDomainException{
        validateNotNullOrEmpty(physicalLocation, "physicalLocation may not be empty or null");
        this.physicalLocation = physicalLocation;
    }

	@Override
	public InputStream getDocumentInputStream() throws DocuservDomainException {
		try{
			return new FileInputStream(getPhysicalLocation());
		}catch(FileNotFoundException e){
			throw new DocuservDomainException("FileNotFoundException occurred while trying to create inputStream for document: "+getId()+" at path: "+getPhysicalLocation(),e);
		}
	}
}
