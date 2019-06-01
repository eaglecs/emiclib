package basecode.com.domain.model.epub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class SkySetting {
	public int bookCode;
	public String fontName;
	public int fontSize;
	public int lineSpacing;
	public int foreground;
    public int background;
    public int theme;
    public double brightness;
    public int transitionType;	
    public boolean lockRotation;
    public boolean doublePaged;
    public boolean allow3G;
    public boolean globalPagination;
    
    public boolean mediaOverlay;
    public boolean tts;
    public boolean autoStartPlaying;
    public boolean autoLoadNewChapter;
    public boolean highlightTextToVoice;

    public static String storageDirectory=null;

    public static String getStorageDirectory() {
        return storageDirectory;
    }

    public static void setStorageDirectory(String directory, String appName) {
        storageDirectory = directory+"/"+appName;
    }

    public static boolean moveFile(String from, String to) {
        try {
            int bytesum = 0;
            int byteread = 0;
            InputStream inStream = new FileInputStream(from);
            FileOutputStream fs = new FileOutputStream(to);
            byte[] buffer = new byte[1444];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            fs.close();
            File source = new File(from);
            source.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String removeExtention(String filePath) {
        // These first few lines the same as Justin's
        File f = new File(filePath);

        // if it's a directory, don't remove the extention
        if (f.isDirectory()) return filePath;

        String name = f.getName();

        // Now we know it's a file - don't need to do any special hidden
        // checking or contains() checking because of:
        final int lastPeriodPos = name.lastIndexOf('.');
        if (lastPeriodPos <= 0)
        {
            // No period after first character - return name as it was passed in
            return filePath;
        }
        else
        {
            // Remove the last period and everything after it
            File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
            return renamed.getPath();
        }
    }

    public static String getFileExtension(String url) {
        String extension = url.substring(url.lastIndexOf(".")+1);
        return extension;
    }
    public static String getPureName(String url) {
        String fileName = url.substring( url.lastIndexOf('/')+1, url.length() );
        String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
        return fileNameWithoutExtn;
    }

    public static String getFileName(String url) {
        String fileName = url.substring( url.lastIndexOf('/')+1, url.length() );
        return fileName;
    }
}
