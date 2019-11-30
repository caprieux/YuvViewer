import java.io.*;

public class YuvFileReader {
    File yuvFile;
    byte [] yuvData;
    long fileSize = 0;
    public YuvFileReader(String filename) {
        try (InputStream inputStream = new FileInputStream(filename);)
        {
            yuvFile = new File(filename);
            fileSize = yuvFile.length();
            yuvData = new byte[(int) fileSize];
            inputStream.read(yuvData);
            System.out.println("open yuv file : " + filename + ", and read " + Long.toString(fileSize) + " bytes!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public byte [] getData() {
        return this.yuvData;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("usage : YuvFileReader yuv_file_name");
        } else {
            YuvFileReader reader = new YuvFileReader(args[0]);
        }
    }
}
