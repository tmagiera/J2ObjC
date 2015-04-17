package External;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import External.UnzipEvent.UnzipEventType;

public class UnZip  implements Runnable {
    
    interface UnzipCallback {
        void UnzipSuccess(String path, String tag);
        void UnzipFail(String message, String tag);
    }
    
    public Thread thread;
    public UnzipCallback unzipCallback;
    public String zipFilePath = "";
    public String unzipToDirectory = "";
    public String unzipTag = "";
    
    
    public UnZip(String filePath, String directoryPath, UnzipCallback callback, String tag) {
        unzipCallback = callback;
        zipFilePath = filePath;
        unzipTag = tag;
        unzipToDirectory = directoryPath;
        thread = new Thread(this,"");
        thread.start();
    }
    
    
    public void run() {
        unzipEm(zipFilePath,unzipToDirectory);
    }
    

    void eventUnzipStart() {
        UnzipEvent ue = new UnzipEvent(UnzipEventType.unzipEventTypeStarted, unzipTag);
        EventBusSingleton.post(ue);
    }
    
    void eventUnzipFinish() {
        UnzipEvent ue = new UnzipEvent(UnzipEventType.unipEventTypeFinished, unzipTag);
        EventBusSingleton.post(ue);
    }
    
    void eventUnzipFail(String message) {
        UnzipEvent ue = new UnzipEvent(UnzipEventType.unzipEventTypeFailed, message);
        EventBusSingleton.post(ue);
    }
    
    
    private static final int  BUFFER_SIZE = 4096;
    
    private void unzipEm(String zipFilePath, String outputFolder) {
        File outputDir = new File(outputFolder);
        File zipFile = new File(zipFilePath);
        extract(zipFile,outputDir);
    }
    
    
    private void extractFile(ZipInputStream in, File outdir, String name) throws IOException
    {
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outdir,name)));
        int count = -1;
        while ((count = in.read(buffer)) != -1)
            out.write(buffer, 0, count);
        out.close();
    }
    
    private void mkdirs(File outdir,String path)
    {
        File d = new File(outdir, path);
        if( !d.exists() )
            d.mkdirs();
    }
    
    private static String dirpart(String name)
    {
        int s = name.lastIndexOf( File.separatorChar );
        return s == -1 ? null : name.substring( 0, s );
    }
    

    public void extract(File zipfile, File outdir)
    {
         eventUnzipStart();
        
        try
        {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
            ZipEntry entry;
            String name, dir;
            while ((entry = zin.getNextEntry()) != null)
            {
                name = entry.getName();
                if( entry.isDirectory() )
                {
                    mkdirs(outdir,name);
                    continue;
                }

                dir = dirpart(name);
                if( dir != null )
                    mkdirs(outdir,dir);
                
                extractFile(zin, outdir, name);
            }
            zin.close();
            unzipCallback.UnzipSuccess(zipFilePath,unzipTag);
            eventUnzipFinish();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
            unzipCallback.UnzipFail(e.getMessage(),unzipTag);
            eventUnzipFail(e.getMessage());
        }
    }

}