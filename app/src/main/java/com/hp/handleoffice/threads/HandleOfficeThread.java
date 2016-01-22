package com.hp.handleoffice.threads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.widget.TabHost;
import android.widget.TextView;

import com.hp.handleoffice.AndroidDocxToHtmlTabsActivity;
import com.hp.handleoffice.AndroidFileConversionImageHandler;
import com.hp.handleoffice.R;
import com.hp.handleoffice.docx4j.WordProcessingML.LoadFromZipNG;

import org.docx4j.XmlUtils;
import org.docx4j.convert.out.html.HtmlExporterNonXSLT;
import org.docx4j.model.images.ConversionImageHandler;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.InputStream;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class HandleOfficeThread extends Thread {
    private final static String TAG = HandleOfficeThread.class.getSimpleName();

    private Context mContext;
    private Handler mHandler;

    public HandleOfficeThread(Context context, Handler handler){
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void run(){
        InputStream is = mContext.getResources().openRawResource(R.raw.sample);

        final long startTime = System.currentTimeMillis();
        final long endTime;
        try {
            final LoadFromZipNG loader = new LoadFromZipNG();
            WordprocessingMLPackage wordMLPackage = (WordprocessingMLPackage)loader.get(is);

            String IMAGE_DIR_NAME = "images";

            String baseURL = mContext.getDir(IMAGE_DIR_NAME, Context.MODE_PRIVATE)
                    .toURI().toURL().toString();
            System.out.println(baseURL); // file:/data/data/com.example.HelloAndroid/app_images/

            // Uncomment this to write image files to file system
            ConversionImageHandler conversionImageHandler =
                    new AndroidFileConversionImageHandler(IMAGE_DIR_NAME,
                    // <-- don't use a path separator here
                    baseURL, false, (Activity)mContext);

            // Uncomment to use a base 64 encoded data URI for each image
            //ConversionImageHandler conversionImageHandler = new AndroidDataUriImageHandler();

            HtmlExporterNonXSLT withoutXSLT = new HtmlExporterNonXSLT(wordMLPackage, conversionImageHandler);

            String html = XmlUtils.w3CDomNodeToString(withoutXSLT.export());
            Message message = new Message();
            message.obj = html;
            message.what = AndroidDocxToHtmlTabsActivity.UPDATE_UI;
            mHandler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endTime = System.currentTimeMillis();
        }
        final long duration = endTime - startTime;
        System.err.println("Total time: " + duration + "ms");
    }
}
