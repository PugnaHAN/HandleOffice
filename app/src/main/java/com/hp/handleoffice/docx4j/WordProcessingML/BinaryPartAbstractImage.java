package com.hp.handleoffice.docx4j.WordProcessingML;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.log4j.Logger;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.DefaultImageContext;
import org.apache.xmlgraphics.image.loader.impl.DefaultImageSessionContext;
import org.docx4j.UnitsOfMeasurement;
import org.docx4j.XmlUtils;
import org.docx4j.dml.Graphic;
import org.docx4j.dml.picture.Pic;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.model.structure.PageDimensions;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.Base;
import org.docx4j.openpackaging.contenttype.ContentTypeManager;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.ExternalTarget;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPart;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

import ae.java.awt.Dimension;
import ae.java.awt.geom.Dimension2D;
import ae.javax.xml.bind.JAXBElement;

public abstract class BinaryPartAbstractImage
        extends BinaryPart
{
    protected static Logger log = Logger.getLogger(BinaryPartAbstractImage.class);
    static final String IMAGE_DIR_PREFIX = "/word/media";
    static final String IMAGE_NAME_PREFIX = "image";
    ImageInfo imageInfo;
    Relationship rel;

    public BinaryPartAbstractImage(PartName partName)
            throws InvalidFormatException
    {
        super(partName);

        getOwningRelationshipPart();
    }

    public BinaryPartAbstractImage(ExternalTarget externalTarget)
    {
        super(externalTarget);
    }

    public ImageInfo getImageInfo()
    {
        if (this.imageInfo == null) {}
        return this.imageInfo;
    }

    public void setImageInfo(ImageInfo imageInfo)
    {
        this.imageInfo = imageInfo;
    }

    static int density = 150;

    public static void setDensity(int density)
    {
        density = density;
    }

    // static ImageManager imageManager = new ImageManager(new DefaultImageContext());
    static final String namespaces = " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\"";

    public static BinaryPartAbstractImage createImagePart(WordprocessingMLPackage wordMLPackage, byte[] bytes)
            throws Exception
    {
        return createImagePart(wordMLPackage, wordMLPackage.getMainDocumentPart(), bytes);
    }

    public static BinaryPartAbstractImage createImagePart(WordprocessingMLPackage wordMLPackage, File imageFile)
            throws Exception
    {
        return createImagePart(wordMLPackage, wordMLPackage.getMainDocumentPart(), imageFile);
    }

    @Deprecated
    public static String createImageName(Base sourcePart, String proposedRelId, String ext)
    {
        return PartName.generateUniqueName(sourcePart, proposedRelId, IMAGE_DIR_PREFIX, IMAGE_NAME_PREFIX, ext);
    }

    public static String createImageName(OpcPackage opcPackage, Base sourcePart, String proposedRelId, String ext)
    {
        if ((opcPackage instanceof WordprocessingMLPackage)) {
            return PartName.generateUniqueName(sourcePart, proposedRelId, IMAGE_DIR_PREFIX, IMAGE_NAME_PREFIX, ext);
        }
        if ((opcPackage instanceof PresentationMLPackage)) {
            return PartName.generateUniqueName(sourcePart, proposedRelId, "/ppt/media/", IMAGE_NAME_PREFIX, ext);
        }
        if ((opcPackage instanceof SpreadsheetMLPackage)) {
            return PartName.generateUniqueName(sourcePart, proposedRelId, "/xl/media/", IMAGE_NAME_PREFIX, ext);
        }
        return PartName.generateUniqueName(sourcePart, proposedRelId, IMAGE_DIR_PREFIX, IMAGE_NAME_PREFIX, ext);
    }

    public static BinaryPartAbstractImage createImagePart(OpcPackage opcPackage, Part sourcePart, byte[] bytes)
            throws Exception
    {
        File tmpImageFile = File.createTempFile("img", ".img");

        FileOutputStream fos = new FileOutputStream(tmpImageFile);
        fos.write(bytes);
        fos.close();
        log.debug("created tmp file: " + tmpImageFile.getAbsolutePath());

        ImageInfo info = ensureFormatIsSupported(tmpImageFile, bytes, true);

        ContentTypeManager ctm = opcPackage.getContentTypeManager();
        if (sourcePart.getRelationshipsPart() == null) {
            RelationshipsPart.createRelationshipsPartForPart(sourcePart);
        }
        String proposedRelId = sourcePart.getRelationshipsPart().getNextId();

        String ext = info.getMimeType().substring(info.getMimeType().indexOf("/") + 1);

        BinaryPartAbstractImage imagePart = (BinaryPartAbstractImage)ctm.newPartForContentType(info.getMimeType(), createImageName(opcPackage, sourcePart, proposedRelId, ext), null);

        log.debug("created part " + imagePart.getClass().getName() + " with name " + imagePart.getPartName().toString());

        FileInputStream fis = new FileInputStream(tmpImageFile);
        imagePart.setBinaryData(fis);

        imagePart.rel = sourcePart.addTargetPart(imagePart, proposedRelId);

        imagePart.setImageInfo(info);

        fos = null;
        fis = null;
        System.gc();
        if (tmpImageFile.delete())
        {
            log.debug(".. deleted " + tmpImageFile.getAbsolutePath());
        }
        else
        {
            log.warn("Couldn't delete tmp file " + tmpImageFile.getAbsolutePath());
            tmpImageFile.deleteOnExit();
        }
        return imagePart;
    }

    public static BinaryPartAbstractImage createImagePart(OpcPackage opcPackage, Part sourcePart, File imageFile)
            throws Exception
    {
        byte[] locByte = new byte[1];

        ImageInfo info = ensureFormatIsSupported(imageFile, locByte, false);

        ContentTypeManager ctm = opcPackage.getContentTypeManager();
        if (sourcePart.getRelationshipsPart() == null) {
            RelationshipsPart.createRelationshipsPartForPart(sourcePart);
        }
        String proposedRelId = sourcePart.getRelationshipsPart().getNextId();

        String ext = info.getMimeType().substring(info.getMimeType().indexOf("/") + 1);

        BinaryPartAbstractImage imagePart = (BinaryPartAbstractImage)ctm.newPartForContentType(info.getMimeType(), createImageName(opcPackage, sourcePart, proposedRelId, ext), null);

        log.debug("created part " + imagePart.getClass().getName() + " with name " + imagePart.getPartName().toString());

        FileInputStream fis = new FileInputStream(imageFile);
        imagePart.setBinaryData(fis);

        imagePart.rel = sourcePart.addTargetPart(imagePart, proposedRelId);

        imagePart.setImageInfo(info);

        return imagePart;
    }

    private static ImageInfo ensureFormatIsSupported(File imageFile, byte[] bytes, boolean isLoad)
            throws Docx4JException, MalformedURLException
    {
        return ensureFormatIsSupported(imageFile.toURI().toURL(), imageFile, bytes, isLoad);
    }

    private static ImageInfo ensureFormatIsSupported(URL url, File imageFile, byte[] bytes, boolean isLoad)
            throws Docx4JException
    {
        ImageInfo info = null;
        boolean imagePreloaderFound = true;
        try
        {
            try
            {
                info = getImageInfo(url);

                displayImageInfo(info);
            }
            catch (ImageException e)
            {
                imagePreloaderFound = false;
                log.warn(e.getMessage());
            }
            if ((imagePreloaderFound) && ((info.getMimeType().equals("image/tiff")) || (info.getMimeType().equals("image/emf")) || (info.getMimeType().equals("image/x-wmf")) || (info.getMimeType().equals("image/png")) || (info.getMimeType().equals("image/jpeg")) || (info.getMimeType().equals("image/gif")) || (info.getMimeType().equals("image/bmp"))))
            {
                log.debug(".. supported natively by Word");
            }
            else if ((imageFile != null) && (bytes != null))
            {
                log.debug(".. attempting to convert to PNG");
                FileOutputStream fos = null;
                if (!isLoad)
                {
                    File tmpImageFile = File.createTempFile("img", ".img");
                    fos = new FileOutputStream(tmpImageFile);

                    FileInputStream bais = new FileInputStream(imageFile);

                    convertToPNG(bais, fos, density);

                    imageFile = tmpImageFile;
                }
                else
                {
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    fos = new FileOutputStream(imageFile);
                    convertToPNG(bais, fos, density);
                }
                fos.close();

                // imageManager.getCache().clearCache();
                info = getImageInfo(new URL(imageFile.getAbsolutePath()));

                displayImageInfo(info);
            }
            else
            {
                throw new Docx4JException("Unsupported linked image type.");
            }
        }
        catch (Exception e)
        {
            throw new Docx4JException("Error checking image format", e);
        }
        return info;
    }

    public static BinaryPartAbstractImage createLinkedImagePart(WordprocessingMLPackage wordMLPackage, URL fileurl)
            throws Exception
    {
        return createLinkedImagePart(wordMLPackage, wordMLPackage.getMainDocumentPart(), fileurl);
    }

    public static BinaryPartAbstractImage createLinkedImagePart(OpcPackage opcPackage, Part sourcePart, URL url)
            throws Exception
    {
        log.debug("Incoming url for linked image: " + url.toString());

        ImageInfo info = ensureFormatIsSupported(url, null, null, false);

        ContentTypeManager ctm = opcPackage.getContentTypeManager();
        String proposedRelId = sourcePart.getRelationshipsPart().getNextId();

        String ext = info.getMimeType().substring(info.getMimeType().indexOf("/") + 1);

        BinaryPartAbstractImage imagePart = (BinaryPartAbstractImage)ctm.newPartForContentType(info.getMimeType(), createImageName(opcPackage, sourcePart, proposedRelId, ext), null);

        log.debug("created part " + imagePart.getClass().getName() + " with name " + imagePart.getPartName().toString());

        imagePart.rel = sourcePart.addTargetPart(imagePart);
        imagePart.rel.setTargetMode("External");

        opcPackage.getExternalResources().put(imagePart.getExternalTarget(), imagePart);

        imagePart.rel.setTarget(url.toString());

        imagePart.setImageInfo(info);

        return imagePart;
    }

    @Deprecated
    public Inline createImageInline(String filenameHint, String altText, int id1, int id2)
            throws Exception
    {
        return createImageInline(filenameHint, altText, id1, id2, false);
    }

    public Inline createImageInline(String filenameHint, String altText, int id1, int id2, boolean link)
            throws Exception
    {
        WordprocessingMLPackage wmlPackage = (WordprocessingMLPackage)getPackage();

        List<SectionWrapper> sections = wmlPackage.getDocumentModel().getSections();
        PageDimensions page = ((SectionWrapper)sections.get(sections.size() - 1)).getPageDimensions();

        CxCy cxcy = CxCy.scale(this.imageInfo, page);

        return createImageInline(filenameHint, altText, id1, id2, cxcy.getCx(), cxcy.getCy(), link);
    }

    @Deprecated
    public Inline createImageInline(String filenameHint, String altText, int id1, int id2, long cx)
            throws Exception
    {
        return createImageInline(filenameHint, altText, id1, id2, cx, false);
    }

    public Inline createImageInline(String filenameHint, String altText, int id1, int id2, long cx, boolean link)
            throws Exception
    {
        ImageSize size = this.imageInfo.getSize();

        Dimension2D dPt = size.getDimensionPt();
        double imageWidthTwips = dPt.getWidth() * 20.0D;
        log.debug("imageWidthTwips: " + imageWidthTwips);

        log.debug("Scaling image height to retain aspect ratio");
        long cy = UnitsOfMeasurement.twipToEMU(dPt.getHeight() * 20.0D * cx / imageWidthTwips);

        cx = UnitsOfMeasurement.twipToEMU(cx);

        log.debug("cx=" + cx + "; cy=" + cy);

        return createImageInline(filenameHint, altText, id1, id2, cx, cy, link);
    }

    public Inline createImageInline(String filenameHint, String altText, int id1, int id2, long cx, long cy, boolean link)
            throws Exception
    {
        if (filenameHint == null) {
            filenameHint = "";
        }
        if (altText == null) {
            altText = "";
        }
        String type;

        if (link) {
            type = "r:link";
        } else {
            type = "r:embed";
        }
        String ml = "<wp:inline distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\"><wp:extent cx=\"${cx}\" cy=\"${cy}\"/><wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/><wp:docPr id=\"${id1}\" name=\"${filenameHint}\" descr=\"${altText}\"/><wp:cNvGraphicFramePr><a:graphicFrameLocks xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" noChangeAspect=\"1\"/></wp:cNvGraphicFramePr><a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"><a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\"><pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\"><pic:nvPicPr><pic:cNvPr id=\"${id2}\" name=\"${filenameHint}\"/><pic:cNvPicPr/></pic:nvPicPr><pic:blipFill><a:blip " + type + "=\"${rEmbedId}\"/><a:stretch><a:fillRect/></a:stretch></pic:blipFill>" + "<pic:spPr><a:xfrm><a:off x=\"0\" y=\"0\"/><a:ext cx=\"${cx}\" cy=\"${cy}\"/></a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom></pic:spPr></pic:pic></a:graphicData></a:graphic>" + "</wp:inline>";

        HashMap<String, String> mappings = new HashMap();

        mappings.put("cx", Long.toString(cx));
        mappings.put("cy", Long.toString(cy));
        mappings.put("filenameHint", filenameHint);
        mappings.put("altText", altText);
        mappings.put("rEmbedId", this.rel.getId());
        mappings.put("id1", Integer.toString(id1));
        mappings.put("id2", Integer.toString(id2));

        Object o = XmlUtils.unmarshallFromTemplate(ml, mappings);
        Inline inline = (Inline)((JAXBElement)o).getValue();

        return inline;
    }

    public static ImageInfo getImageInfo(URL url)
            throws Exception
    {
        // ImageSessionContext sessionContext = new DefaultImageSessionContext(imageManager.getImageContext(), null);

        String path = url.toString();
        String type = "@image/" + path.toLowerCase().substring(path.lastIndexOf('.') + 1);

        ImageInfo info = new ImageInfo(path, type);
        Bitmap bm = BitmapFactory.decodeFile(path);
        ImageSize size = new ImageSize(bm.getWidth(), bm.getHeight(), bm.getDensity());
        info.setSize(size);

        return info;
    }

    public static void main(String[] args)
            throws Exception
    {
        String uri = System.getProperty("user.dir") + "/sample-docs/metafile-samples/freehand_picture_saveas.wmf";
        System.out.println(uri);

        ImageInfo ii = getImageInfo(new URL(uri));

        displayImageInfo(ii);
    }

    public static void displayImageInfo(ImageInfo info)
    {
        ImageSize size = info.getSize();

        Dimension2D dPt = size.getDimensionPt();
        Dimension dPx = size.getDimensionPx();

        log.debug(info.getOriginalURI() + " " + info.getMimeType() + " " + Math.round(dPx.getWidth()) + "x" + Math.round(dPx.getHeight()));

        log.debug("Resolution:" + Math.round(size.getDpiHorizontal()) + "x" + Math.round(size.getDpiVertical()));
        log.debug("Print size: " + Math.round(dPt.getWidth() / 72.0D) + "\" x" + Math.round(dPt.getHeight() / 72.0D) + "\"");
    }

    public static byte[] getImage(WordprocessingMLPackage wmlPkg, Graphic graphic)
    {
        if ((wmlPkg == null) || (wmlPkg.getMainDocumentPart() == null) || (wmlPkg.getMainDocumentPart().getRelationshipsPart() == null)) {
            return null;
        }
        Pic pic = graphic.getGraphicData().getPic();
        String rId = pic.getBlipFill().getBlip().getEmbed();
        if (rId.equals("")) {
            rId = pic.getBlipFill().getBlip().getLink();
        }
        log.debug("Image rel id: " + rId);
        Relationship rel = wmlPkg.getMainDocumentPart().getRelationshipsPart().getRelationshipByID(rId);
        if (rel != null)
        {
            Part part = wmlPkg.getMainDocumentPart().getRelationshipsPart().getPart(rel);
            if (part == null)
            {
                log.error("Couldn't get Part!");
            }
            else
            {
                if ((part instanceof BinaryPart))
                {
                    log.debug("getting bytes...");
                    BinaryPart binaryPart = (BinaryPart)part;

                    ByteBuffer bb = binaryPart.getBuffer();
                    bb.clear();
                    byte[] bytes = new byte[bb.capacity()];
                    bb.get(bytes, 0, bytes.length);

                    return bytes;
                }
                log.error("Part was a " + part.getClass().getName());
            }
        }
        else
        {
            log.error("Couldn't find rel " + rId);
        }
        return null;
    }

    public static class CxCy
    {
        long cx;
        long cy;
        boolean scaled;

        public long getCx()
        {
            return this.cx;
        }

        public long getCy()
        {
            return this.cy;
        }

        public boolean isScaled()
        {
            return this.scaled;
        }

        CxCy(long cx, long cy, boolean scaled)
        {
            this.cx = cx;
            this.cy = cy;
            this.scaled = scaled;
        }

        public static CxCy scale(ImageInfo imageInfo, PageDimensions page)
        {
            double writableWidthTwips = page.getWritableWidthTwips();
            BinaryPartAbstractImage.log.debug("writableWidthTwips: " + writableWidthTwips);

            ImageSize size = imageInfo.getSize();

            Dimension2D dPt = size.getDimensionPt();
            double imageWidthTwips = dPt.getWidth() * 20.0D;
            BinaryPartAbstractImage.log.debug("imageWidthTwips: " + imageWidthTwips);

            boolean scaled = false;
            long cy;
            long cx;
            if (imageWidthTwips > writableWidthTwips)
            {
                BinaryPartAbstractImage.log.debug("Scaling image to fit page width");
                scaled = true;

                cx = UnitsOfMeasurement.twipToEMU(writableWidthTwips);
                cy = UnitsOfMeasurement.twipToEMU(dPt.getHeight() * 20.0D * writableWidthTwips / imageWidthTwips);
            }
            else
            {
                BinaryPartAbstractImage.log.debug("Scaling image - not necessary");

                cx = UnitsOfMeasurement.twipToEMU(imageWidthTwips);
                cy = UnitsOfMeasurement.twipToEMU(dPt.getHeight() * 20.0D);
            }
            BinaryPartAbstractImage.log.debug("cx=" + cx + "; cy=" + cy);

            return new CxCy(cx, cy, scaled);
        }
    }

    public static void convertToPNG(InputStream is, OutputStream os, int density)
            throws IOException, InterruptedException {
        log.info("Start ImageMagick...");
        Process p = Runtime.getRuntime().exec("imconvert -density " + density + " -units PixelsPerInch - png:-");

        StreamGobbler inGobbler = new StreamGobbler(p.getInputStream(), os);
        StreamGobbler errGobbler = new StreamGobbler(p.getErrorStream(), System.err);

        inGobbler.start();
        errGobbler.start();
        try
        {
            copy2(is, new BufferedOutputStream(p.getOutputStream()));
            p.getOutputStream().close();
            log.debug("Image copied...");
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();

            copy2(p.getErrorStream(), System.err);
        }
        if (p.waitFor() != 0) {
            log.error("Error");
        }
        log.debug("End Process...");
    }

    public static void copy2(InputStream is, OutputStream os)
            throws IOException
    {
        byte[] buffer = new byte['?'];
        for (;;)
        {
            int bytesRead = is.read(buffer);
            if (bytesRead == -1) {
                break;
            }
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
    }
}
