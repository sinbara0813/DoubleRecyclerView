package com.example.doublerecyclerview;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

class CrashExceptionHandler implements UncaughtExceptionHandler {

    private Context mContext;

    CrashExceptionHandler(Context act) {
        mContext = act;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //thread.getUncaughtExceptionHandler().uncaughtException(thread,ex);
        sendCrashReport(ex);
        Log.e("Rong Crash", ex.getMessage(), ex);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        handleException();
    }

    private void sendCrashReport(Throwable ex) {
        File root = mContext.getExternalCacheDir();
        File file = new File(root, "crash");
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file.listFiles().length >= 10) {
            for (File iFile : file.listFiles()) {
                iFile.delete();
            }
        }
        File outFile = new File(file, System.currentTimeMillis() + ".txt");

        try {
            outFile.createNewFile();
            saveCrashInfo2File(outFile, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleException() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void saveCrashInfo2File(File file, Throwable ex) throws IOException {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(sb.toString().getBytes());
        fos.close();
    }
}