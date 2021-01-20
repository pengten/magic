package org.myagent;

import org.apache.commons.io.IOUtils;
import org.myagent.log.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.security.ProtectionDomain;

public class MyTransformer implements ClassFileTransformer {

    private String url;
    private String targetClass;

    public MyTransformer(String[] args) {
        targetClass = args[0];
        url = args[1];
        Log.init(args[2]);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.equals(targetClass.replace(".", "/"))) {
            return null;
        }
        byte[] bytes = null;
        FileInputStream fileInputStream = null;
        try {
            if (url.startsWith("http")) {
                bytes = IOUtils.toByteArray(new URL(url));
            } else {
                fileInputStream = new FileInputStream(new File(url));
                bytes = IOUtils.toByteArray(fileInputStream);
            }
        } catch (IOException e) {
            Log.getInstants().writeError("transform error", e);
            throw new IllegalClassFormatException("transform error");
        } finally {
            try {
                IOUtils.close(fileInputStream);
            } catch (IOException e) {
                Log.getInstants().writeError("filestream close error", e);
            }
        }
        return bytes;
    }
}
