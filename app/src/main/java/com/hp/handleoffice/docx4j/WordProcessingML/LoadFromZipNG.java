//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hp.handleoffice.docx4j.WordProcessingML;

import com.hp.handleoffice.docx4j.ContentTypeManager;

import ae.javax.xml.bind.JAXBElement;
import ae.javax.xml.bind.UnmarshalException;
import ae.javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;
import org.docx4j.XmlUtils;
import org.docx4j.bibliography.CTSources;
import org.docx4j.jaxb.Context;
import org.docx4j.model.datastorage.CustomXmlDataStorage;
import org.docx4j.openpackaging.Base;
import org.docx4j.openpackaging.URIHelper;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.exceptions.PartUnrecognisedException;
import org.docx4j.openpackaging.io.ExternalResourceUtils;
import org.docx4j.openpackaging.io.Load;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.docx4j.openpackaging.parts.CustomXmlDataStoragePart;
import org.docx4j.openpackaging.parts.CustomXmlDataStoragePropertiesPart;
import org.docx4j.openpackaging.parts.DefaultXmlPart;
import org.docx4j.openpackaging.parts.DocPropsCorePart;
import org.docx4j.openpackaging.parts.DocPropsCustomPart;
import org.docx4j.openpackaging.parts.DocPropsExtendedPart;
import org.docx4j.openpackaging.parts.JaxbXmlPart;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.ThemePart;
import org.docx4j.openpackaging.parts.XmlPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BibliographyPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPart;
import org.docx4j.openpackaging.parts.digitalsignature.XmlSignaturePart;
import org.docx4j.openpackaging.parts.opendope.ComponentsPart;
import org.docx4j.openpackaging.parts.opendope.ConditionsPart;
import org.docx4j.openpackaging.parts.opendope.QuestionsPart;
import org.docx4j.openpackaging.parts.opendope.XPathsPart;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.opendope.components.Components;
import org.opendope.conditions.Conditions;
import org.opendope.questions.Questionnaire;
import org.opendope.xpaths.Xpaths;

public class LoadFromZipNG extends Load {
    private static Logger log = Logger.getLogger(LoadFromZipNG.class);

    public static void main(String[] args) throws Exception {
        String filepath = System.getProperty("user.dir") + "/sample-docs/FontEmbedded.docx";
        log.info("Path: " + filepath);
        LoadFromZipNG loader = new LoadFromZipNG();
        loader.get(filepath);
    }

    public LoadFromZipNG() {
    }

    public OpcPackage get(String filepath) throws Docx4JException {
        return this.get(new File(filepath));
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws Exception {
        BufferedInputStream bufIn = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);

        for(int c = bufIn.read(); c != -1; c = bufIn.read()) {
            bos.write(c);
        }

        bos.flush();
        baos.flush();
        bos.close();
        return baos.toByteArray();
    }

    public OpcPackage get(File f) throws Docx4JException {
        log.info("Filepath = " + f.getPath());
        ZipFile zf = null;

        try {
            if(!f.exists()) {
                log.info("Couldn\'t find " + f.getPath());
            }

            zf = new ZipFile(f);
        } catch (IOException var10) {
            var10.printStackTrace();
            throw new Docx4JException("Couldn\'t get ZipFile", var10);
        }

        HashMap partByteArrays = new HashMap();
        Enumeration entries = zf.entries();

        while(entries.hasMoreElements()) {
            ZipEntry exc = (ZipEntry)entries.nextElement();
            log.info("\n\n" + exc.getName() + "\n");
            Object in = null;

            try {
                byte[] e = getBytesFromInputStream(zf.getInputStream(exc));
                partByteArrays.put(exc.getName(), new LoadFromZipNG.ByteArray(e));
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        }

        try {
            zf.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return this.process(partByteArrays);
    }

    public OpcPackage get(InputStream is) throws Docx4JException {
        HashMap partByteArrays = new HashMap();

        try {
            ZipInputStream e = new ZipInputStream(is);
            ZipEntry entry = null;

            while((entry = e.getNextEntry()) != null) {
                byte[] bytes = getBytesFromInputStream(e);
                partByteArrays.put(entry.getName(), new LoadFromZipNG.ByteArray(bytes));
            }

            e.close();
            return this.process(partByteArrays);
        } catch (Exception var6) {
            log.error(var6.getMessage());
            throw new Docx4JException("Error processing zip file (is it a zip file?)", var6);
        }
    }

    private OpcPackage process(HashMap<String, LoadFromZipNG.ByteArray> partByteArrays) throws Docx4JException {
        ContentTypeManager ctm = new ContentTypeManager();

        try {
            InputStream p = getInputStreamFromZippedPart(partByteArrays, "[Content_Types].xml");
            ctm.parseContentTypesFile(p);
        } catch (IOException var6) {
            throw new Docx4JException("Couldn\'t get [Content_Types].xml from ZipFile", var6);
        }

        OpcPackage p1 = ctm.createPackage();
        String partName = "_rels/.rels";
        RelationshipsPart rp = this.getRelationshipsPartFromZip(p1, partByteArrays, partName);
        p1.setRelationships(rp);
        log.debug("Object created for: " + partName);
        this.addPartsFromRelationships(partByteArrays, p1, rp, ctm);
        registerCustomXmlDataStorageParts(p1);
        return p1;
    }

    private RelationshipsPart getRelationshipsPartFromZip(Base p, HashMap<String, LoadFromZipNG.ByteArray> partByteArrays, String partName) throws Docx4JException {
        RelationshipsPart rp = null;
        InputStream is = null;

        try {
            is = getInputStreamFromZippedPart(partByteArrays, partName);
            rp = new RelationshipsPart(new PartName("/" + partName));
            rp.setSourceP(p);
            rp.unmarshal(is);
        } catch (Exception var14) {
            var14.printStackTrace();
            throw new Docx4JException("Error getting document from Zipped Part:" + partName, var14);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
            }

        }

        return rp;
    }

    private static InputStream getInputStreamFromZippedPart(HashMap<String, LoadFromZipNG.ByteArray> partByteArrays, String partName) throws IOException {
        LoadFromZipNG.ByteArray bytes = (LoadFromZipNG.ByteArray)partByteArrays.get(partName);
        if(bytes == null) {
            throw new IOException("part \'" + partName + "\' not found");
        } else {
            return bytes.getInputStream();
        }
    }

    private void addPartsFromRelationships(HashMap<String, LoadFromZipNG.ByteArray> partByteArrays, Base source, RelationshipsPart rp, ContentTypeManager ctm) throws Docx4JException {
        OpcPackage pkg = source.getPackage();
        Iterator i$ = rp.getRelationships().getRelationship().iterator();

        while(i$.hasNext()) {
            Relationship r = (Relationship)i$.next();
            log.debug("\n For Relationship Id=" + r.getId() + " Source is " + rp.getSourceP().getPartName() + ", Target is " + r.getTarget() + ", type: " + r.getType());

            try {
                this.getPart(partByteArrays, pkg, rp, r, ctm);
            } catch (Exception var9) {
                throw new Docx4JException("Failed to add parts from relationships", var9);
            }
        }

    }

    private void getPart(HashMap<String, LoadFromZipNG.ByteArray> partByteArrays, OpcPackage pkg, RelationshipsPart rp, Relationship r, ContentTypeManager ctm) throws Docx4JException, InvalidFormatException, URISyntaxException {
        Base source = null;
        String resolvedPartUri = null;
        if(r.getType().equals("http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink")) {
            log.info("Encountered (but not loading) hyperlink " + r.getTarget());
        } else if(r.getTargetMode() != null && r.getTargetMode().equals("External")) {
            if(this.loadExternalTargets && r.getType().equals("http://schemas.openxmlformats.org/officeDocument/2006/relationships/image")) {
                log.info("Loading external resource " + r.getTarget() + " of type " + r.getType());
                BinaryPart relationshipType1 = ExternalResourceUtils.getExternalResource(r.getTarget());
                pkg.getExternalResources().put(relationshipType1.getExternalTarget(), relationshipType1);
            } else {
                log.info("Encountered (but not loading) external resource " + r.getTarget() + " of type " + r.getType());
            }

        } else {
            source = rp.getSourceP();
            resolvedPartUri = URIHelper.resolvePartUri(rp.getSourceURI(), new URI(r.getTarget())).toString();
            resolvedPartUri = resolvedPartUri.substring(1);
            if(pkg.handled.get(resolvedPartUri) == null) {
                String relationshipType = r.getType();
                Part part = getRawPart(partByteArrays, ctm, resolvedPartUri, r);
                if(part instanceof BinaryPart || part instanceof DefaultXmlPart) {
                    part.setRelationshipType(relationshipType);
                }

                rp.loadPart(part, r);
                pkg.handled.put(resolvedPartUri, resolvedPartUri);
                if(source.setPartShortcut(part, relationshipType)) {
                    log.debug("Convenience method established from " + source.getPartName() + " to " + part.getPartName());
                }

                RelationshipsPart rrp = this.getRelationshipsPart(partByteArrays, part);
                if(rrp != null) {
                    this.addPartsFromRelationships(partByteArrays, part, rrp, ctm);
                    String relPart = PartName.getRelationshipsPartName(part.getPartName().getName().substring(1));
                }

            }
        }
    }

    public RelationshipsPart getRelationshipsPart(HashMap<String, LoadFromZipNG.ByteArray> partByteArrays, Part part) throws Docx4JException, InvalidFormatException {
        RelationshipsPart rrp = null;
        String relPart = PartName.getRelationshipsPartName(part.getPartName().getName().substring(1));
        if(partByteArrays.get(relPart) != null) {
            log.debug("Found relationships " + relPart);
            rrp = this.getRelationshipsPartFromZip(part, partByteArrays, relPart);
            part.setRelationships(rrp);
            return rrp;
        } else {
            log.debug("No relationships " + relPart);
            return null;
        }
    }

    public static Part getRawPart(HashMap<String, LoadFromZipNG.ByteArray> partByteArrays, ContentTypeManager ctm, String resolvedPartUri, Relationship rel) throws Docx4JException {
        Object part = null;
        InputStream is = null;

        try {
            try {
                log.debug("resolved uri: " + resolvedPartUri);
                is = getInputStreamFromZippedPart(partByteArrays, resolvedPartUri);
                part = ctm.getPart("/" + resolvedPartUri, rel);
                if(part instanceof ThemePart) {
                    ((JaxbXmlPart)part).setJAXBContext(Context.jcThemePart);
                    ((JaxbXmlPart)part).unmarshal(is);
                } else if(part instanceof DocPropsCorePart) {
                    ((JaxbXmlPart)part).setJAXBContext(Context.jcDocPropsCore);
                    ((JaxbXmlPart)part).unmarshal(is);
                } else if(part instanceof DocPropsCustomPart) {
                    ((JaxbXmlPart)part).setJAXBContext(Context.jcDocPropsCustom);
                    ((JaxbXmlPart)part).unmarshal(is);
                } else if(part instanceof DocPropsExtendedPart) {
                    ((JaxbXmlPart)part).setJAXBContext(Context.jcDocPropsExtended);
                    ((JaxbXmlPart)part).unmarshal(is);
                } else if(part instanceof CustomXmlDataStoragePropertiesPart) {
                    ((JaxbXmlPart)part).setJAXBContext(Context.jcCustomXmlProperties);
                    ((JaxbXmlPart)part).unmarshal(is);
                } else if(part instanceof XmlSignaturePart) {
                    ((JaxbXmlPart)part).setJAXBContext(Context.jcXmlDSig);
                    ((JaxbXmlPart)part).unmarshal(is);
                } else if(part instanceof JaxbXmlPart) {
                    ((JaxbXmlPart)part).unmarshal(is);
                } else if(part instanceof BinaryPart) {
                    log.debug("Detected BinaryPart " + part.getClass().getName());
                    ((BinaryPart)part).setBinaryData(is);
                } else if(part instanceof CustomXmlDataStoragePart) {
                    try {
                        Unmarshaller ex = Context.jc.createUnmarshaller();
                        Object data2 = ex.unmarshal(is);
                        log.debug(data2.getClass().getName());
                        PartName name = ((Part)part).getPartName();
                        if(data2 instanceof Conditions) {
                            part = new ConditionsPart(name);
                            ((ConditionsPart)part).setJaxbElement((Conditions)data2);
                        } else if(data2 instanceof Xpaths) {
                            part = new XPathsPart(name);
                            ((XPathsPart)part).setJaxbElement((Xpaths)data2);
                        } else if(data2 instanceof Questionnaire) {
                            part = new QuestionsPart(name);
                            ((QuestionsPart)part).setJaxbElement((Questionnaire)data2);
                        } else if(data2 instanceof Components) {
                            part = new ComponentsPart(name);
                            ((ComponentsPart)part).setJaxbElement((Components)data2);
                        } else if(data2 instanceof JAXBElement && XmlUtils.unwrap(data2) instanceof CTSources) {
                            part = new BibliographyPart(name);
                            ((BibliographyPart)part).setJaxbElement((JAXBElement)data2);
                        } else {
                            log.warn("No known part after all for CustomXmlPart " + data2.getClass().getName());
                            CustomXmlDataStorage data1 = getCustomXmlDataStorageClass().factory();
                            is.reset();
                            data1.setDocument(is);
                            ((CustomXmlDataStoragePart)part).setData(data1);
                        }
                    } catch (UnmarshalException var19) {
                        CustomXmlDataStorage data = getCustomXmlDataStorageClass().factory();
                        is.reset();
                        data.setDocument(is);
                        ((CustomXmlDataStoragePart)part).setData(data);
                    }
                } else if(part instanceof XmlPart) {
                    ((XmlPart)part).setDocument(is);
                } else {
                    log.error("No suitable part found for: " + resolvedPartUri);
                    part = null;
                }
            } catch (PartUnrecognisedException var20) {
                log.error("PartUnrecognisedException shouldn\'t happen anymore!", var20);
                part = getBinaryPart(partByteArrays, ctm, resolvedPartUri);
                log.warn("Using BinaryPart for " + resolvedPartUri);
                ((BinaryPart)part).setBinaryData(is);
            }
        } catch (Exception var21) {
            var21.printStackTrace();
            throw new Docx4JException("Failed to getPart", var21);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException var18) {
                    var18.printStackTrace();
                }
            }

        }

        if(part == null) {
            throw new Docx4JException("cannot find part " + resolvedPartUri + " from rel " + rel.getId() + "=" + rel.getTarget());
        } else {
            return (Part)part;
        }
    }

    public static Part getBinaryPart(HashMap<String, LoadFromZipNG.ByteArray> partByteArrays, ContentTypeManager ctm, String resolvedPartUri) throws Docx4JException {
        BinaryPart part = null;
        InputStream in = null;

        try {
            in = ((LoadFromZipNG.ByteArray)partByteArrays.get(resolvedPartUri)).getInputStream();
            part = new BinaryPart(new PartName("/" + resolvedPartUri));
            part.setContentType(new ContentType(ctm.getContentType(new PartName("/" + resolvedPartUri))));
            ((BinaryPart)part).setBinaryData(in);
            log.info("Stored as BinaryData");
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
            }

        }

        return part;
    }

    public static class ByteArray implements Serializable {
        private static final long serialVersionUID = -784146312250361899L;
        private byte[] bytes;
        private String mimetype;

        public byte[] getBytes() {
            return this.bytes;
        }

        public String getMimetype() {
            return this.mimetype;
        }

        public ByteArray(byte[] bytes) {
            this.bytes = bytes;
        }

        public ByteArray(ByteBuffer bb, String mimetype) {
            bb.clear();
            this.bytes = new byte[bb.capacity()];
            bb.get(this.bytes, 0, this.bytes.length);
            this.mimetype = mimetype;
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(this.bytes);
        }

        public int getLength() {
            return this.bytes.length;
        }
    }
}
