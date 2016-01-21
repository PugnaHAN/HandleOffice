//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hp.handleoffice.docx4j.WordProcessingML;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class StreamGobbler extends Thread {
    InputStream is;
    OutputStream os;

    StreamGobbler(InputStream is, OutputStream redirect) {
        this.is = new BufferedInputStream(is);
        this.os = redirect;
    }

    public void run() {
        try {
            BinaryPartAbstractImage.copy2(this.is, this.os);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }
}
