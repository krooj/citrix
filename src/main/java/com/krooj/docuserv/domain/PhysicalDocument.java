package com.krooj.docuserv.domain;

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
}
