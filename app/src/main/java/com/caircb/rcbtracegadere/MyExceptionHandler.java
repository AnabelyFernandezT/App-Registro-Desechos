package com.caircb.rcbtracegadere;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final static String TAG = MyExceptionHandler.class.getSimpleName();
    private final static String ERROR_FILE ="RcbGadere_log.error";

    private final Context context;
    private final Thread.UncaughtExceptionHandler rootHandler;

    public MyExceptionHandler(Context context) {
        this.context = context;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        rootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            Log.d(TAG, "called for " + e.getClass());
            StackTraceElement[] arr = e.getStackTrace();
            String Raghav =t.toString();
            String report = e.toString()+"\n\n";
            report += "--------- Stack trace ---------\n\n"+Raghav;
            for (int i=0; i<arr.length; i++)
            {
                report += "    "+arr[i].toString()+"\n";
            }
            report += "-------------------------------\n\n";

            // If the exception was thrown in a background thread inside
            // AsyncTask, then the actual exception can be found with getCause
            report += "--------- Cause ---------\n\n";
            Throwable cause = e.getCause();
            if(cause != null) {
                report += cause.toString() + "\n\n";
                arr = cause.getStackTrace();
                for (int i=0; i<arr.length; i++)
                {
                    report += "    "+arr[i].toString()+"\n";
                }
            }
            report += "-------------------------------\n\n";

            // assume we would write each error in one file ...
            File f = new File(context.getFilesDir(), ERROR_FILE);
            // log this exception ...
            FileWriter fileWriter;
            BufferedWriter bufferedWriter;

            fileWriter = new FileWriter(f, true); // true to append
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(report);
            bufferedWriter.close();
        }catch (Exception ex) {
            Log.e(TAG, "Exception Logger failed!", e);
            //rootHandler.uncaughtException(t,e);
        }finally {
            rootHandler.uncaughtException(t,e);
        }

    }

    public static final String readExceptions(Context context) {
        FileInputStream stream = null;
        String resp = "";
        try {
            File f = new File(context.getFilesDir(), ERROR_FILE);
            if (f.exists()) {
                stream = new FileInputStream(f);
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                resp = Charset.defaultCharset().decode(bb).toString();
                stream.close();
            }
        }catch(IOException e){
            Log.e(TAG, "readExceptions failed!", e);
        }
        return resp;
    }
}
