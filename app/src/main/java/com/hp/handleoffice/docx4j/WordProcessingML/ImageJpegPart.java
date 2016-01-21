//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hp.handleoffice.docx4j.WordProcessingML;

import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.parts.ExternalTarget;
import org.docx4j.openpackaging.parts.PartName;

public class ImageJpegPart extends BinaryPartAbstractImage {
    public ImageJpegPart(PartName partName) throws InvalidFormatException {
        super(partName);
        this.init();
    }

    public ImageJpegPart(ExternalTarget externalTarget) {
        super(externalTarget);
        this.init();
    }

    public void init() {
        this.setContentType(new ContentType("image/jpeg"));
        this.setRelationshipType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/image");
    }
}
