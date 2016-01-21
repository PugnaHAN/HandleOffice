//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hp.handleoffice.docx4j;

import com.hp.handleoffice.docx4j.WordProcessingML.ImageBmpPart;
import com.hp.handleoffice.docx4j.WordProcessingML.ImageGifPart;
import com.hp.handleoffice.docx4j.WordProcessingML.ImageJpegPart;
import com.hp.handleoffice.docx4j.WordProcessingML.ImagePngPart;
import com.hp.handleoffice.docx4j.WordProcessingML.ImageTiffPart;

import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.exceptions.PartUnrecognisedException;
import org.docx4j.openpackaging.parts.ActiveXControlXmlPart;
import org.docx4j.openpackaging.parts.CustomXmlDataStoragePart;
import org.docx4j.openpackaging.parts.DrawingML.DiagramDrawingPart;
import org.docx4j.openpackaging.parts.DrawingML.JaxbDmlPart;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.PresentationML.JaxbPmlPart;
import org.docx4j.openpackaging.parts.SpreadsheetML.JaxbSmlPart;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorkbookPart;
import org.docx4j.openpackaging.parts.VMLBinaryPart;
import org.docx4j.openpackaging.parts.VMLPart;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPart;
import org.docx4j.openpackaging.parts.WordprocessingML.EmbeddedPackagePart;
import org.docx4j.openpackaging.parts.WordprocessingML.MetafileWmfPart;
import org.docx4j.openpackaging.parts.WordprocessingML.OleObjectBinaryPart;
import org.docx4j.openpackaging.parts.digitalsignature.XmlSignaturePart;
import org.docx4j.relationships.Relationship;

public class ContentTypeManager extends org.docx4j.openpackaging.contenttype.ContentTypeManager {

    @Override
    public Part newPartForContentType(String contentType, String partName, Relationship rel) throws InvalidFormatException, PartUnrecognisedException {
        if(rel != null && rel.getType().equals("http://schemas.openxmlformats.org/officeDocument/2006/relationships/aFChunk")) {
            AlternativeFormatInputPart epp1 = new AlternativeFormatInputPart(new PartName(partName));
            epp1.setContentType(new ContentType(contentType));
            return epp1;
        } else if(rel != null && rel.getType().equals("http://schemas.openxmlformats.org/officeDocument/2006/relationships/package")) {
            EmbeddedPackagePart epp = new EmbeddedPackagePart(new PartName(partName));
            epp.setContentType(new ContentType(contentType));
            return epp;
        } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml")) {
            return this.CreateMainDocumentPartObject(partName);
        } else if(contentType.equals("application/vnd.ms-word.document.macroEnabled.main+xml")) {
            return this.CreateMainDocumentPartObject(partName);
        } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.template.main+xml")) {
            return this.CreateMainDocumentPartObject(partName);
        } else if(contentType.equals("application/vnd.ms-word.template.macroEnabledTemplate.main+xml")) {
            return this.CreateMainDocumentPartObject(partName);
        } else if(contentType.equals("application/vnd.openxmlformats-package.core-properties+xml")) {
            return this.CreateDocPropsCorePartObject(partName);
        } else if(contentType.equals("application/vnd.openxmlformats-officedocument.custom-properties+xml")) {
            return this.CreateDocPropsCustomPartObject(partName);
        } else if(contentType.equals("application/vnd.openxmlformats-officedocument.extended-properties+xml")) {
            return this.CreateDocPropsExtendedPartObject(partName);
        } else if(contentType.equals("application/xml")) {
            return new CustomXmlDataStoragePart(new PartName(partName));
        } else if(contentType.equals("application/vnd.openxmlformats-officedocument.customXmlProperties+xml")) {
            return this.CreateCustomXmlDataStoragePropertiesPartObject(partName);
        } else if(contentType.equals("application/vnd.openxmlformats-officedocument.obfuscatedFont")) {
            return this.CreateObfuscatedFontPartObject(partName);
        } else if(!contentType.equals("application/vnd.openxmlformats-officedocument.oleObject") && !contentType.equals("application/vnd.ms-office.activeX")) {
            if(contentType.equals("application/vnd.ms-office.activeX+xml")) {
                return new ActiveXControlXmlPart(new PartName(partName));
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.theme+xml")) {
                return this.CreateThemePartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.comments+xml")) {
                return this.CreateCommentsPartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.endnotes+xml")) {
                return this.CreateEndnotesPartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml")) {
                return this.CreateFontTablePartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml")) {
                return this.CreateFooterPartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.footnotes+xml")) {
                return this.CreateFootnotesPartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document.glossary+xml")) {
                return this.CreateGlossaryDocumentPartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml")) {
                return this.CreateHeaderPartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml")) {
                return this.CreateNumberingPartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml")) {
                return this.CreateDocumentSettingsPartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml")) {
                return this.CreateStyleDefinitionsPartObject(partName);
            } else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.webSettings+xml")) {
                return this.CreateWebSettingsPartObject(partName);
            } else if(contentType.equals("image/jpeg")) {
                if(!partName.toLowerCase().endsWith(".jpg") && !partName.toLowerCase().endsWith(".jpeg")) {
                    partName = partName + "." + "jpeg";
                }

                return new ImageJpegPart(new PartName(partName));
            } else if(contentType.equals("image/png")) {
                if(!partName.toLowerCase().endsWith(".png")) {
                    partName = partName + "." + "png";
                }

                return new ImagePngPart(new PartName(partName));
            } else if(contentType.equals("image/gif")) {
                if(!partName.toLowerCase().endsWith(".gif")) {
                    partName = partName + "." + "gif";
                }

                return new ImageGifPart(new PartName(partName));
            } else if(contentType.equals("image/tiff")) {
                if(!partName.toLowerCase().endsWith(".tiff")) {
                    partName = partName + "." + "tiff";
                }

                return new ImageTiffPart(new PartName(partName));
            } else if(contentType.equals("image/bmp")) {
                if(!partName.toLowerCase().endsWith(".bmp")) {
                    partName = partName + "." + "bmp";
                }

                return new ImageBmpPart(new PartName(partName));
            } else if(!contentType.equals("image/x-emf") && !contentType.equals("image/emf")) {
                if(contentType.equals("image/x-wmf")) {
                    // return new MetafileWmfPart(new PartName(partName));
                    return new ImageBmpPart(new PartName(partName));
                } else if(contentType.equals("application/vnd.openxmlformats-officedocument.vmlDrawing")) {
                    return (Part)(partName.endsWith(".xml")?new VMLPart(new PartName(partName)):new VMLBinaryPart(new PartName(partName)));
                } else if(contentType.equals("application/vnd.ms-office.drawingml.diagramDrawing+xml")) {
                    return new DiagramDrawingPart(new PartName(partName));
                } else if(contentType.startsWith("application/vnd.openxmlformats-officedocument.drawing")) {
                    return JaxbDmlPart.newPartForContentType(contentType, partName);
                } else if(contentType.startsWith("application/vnd.openxmlformats-officedocument.presentationml")) {
                    return JaxbPmlPart.newPartForContentType(contentType, partName);
                } else if(!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml") && !contentType.equals("application/vnd.ms-excel.sheet.macroEnabled.main+xml") && !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.template.main+xml") && !contentType.equals("application/vnd.ms-excel.template.macroEnabled.main+xml")) {
                    if(contentType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml")) {
                        return JaxbSmlPart.newPartForContentType(contentType, partName);
                    } else if(contentType.equals("application/vnd.openxmlformats-package.digital-signature-xmlsignature+xml")) {
                        return new XmlSignaturePart(new PartName(partName));
                    } else if(!contentType.equals("application/xml") && !partName.endsWith(".xml")) {
                        log.error("No subclass found for " + partName + "; defaulting to binary");
                        return new BinaryPart(new PartName(partName));
                    } else {
                        log.warn("DefaultPart used for part \'" + partName + "\' of content type \'" + contentType + "\'");
                        return this.CreateDefaultXmlPartObject(partName);
                    }
                } else {
                    return new WorkbookPart(new PartName(partName));
                }
            } else {
                // return new MetafileEmfPart(new PartName(partName));
                return new ImageBmpPart(new PartName(partName));
            }
        } else {
            return new OleObjectBinaryPart(new PartName(partName));
        }
    }
}
