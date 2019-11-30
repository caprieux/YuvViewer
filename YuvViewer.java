import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import javax.swing.*;

public class YuvViewer extends JFrame {
    private YuvFileReader yuvReader;
    private int yuvWidth;
    private int yuvHeight;
    private BufferedImage yuvImage;
    private DrawCanvas testPanel;

    private class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //paintRTest();
            g.drawImage(yuvImage, 0, 0, null);
            /*
            System.out.println("minus r count : " + Integer.toString(minusR));
            System.out.println("minus g count : " + Integer.toString(minusG));
            System.out.println("minus b count : " + Integer.toString(minusB));
            */
        }
    }

    public YuvViewer(String fileName, int width, int height) {
        //setLayout(new FlowLayout());
        testPanel = new DrawCanvas();
        testPanel.setBackground(new Color(128, 128, 128));
        //add(new JLabel("Wdith"));
        //add(new JLabel("Height"));
        add(testPanel);

        yuvReader = new YuvFileReader(fileName);
        yuvWidth = width;
        yuvHeight = height;
        yuvImage = new BufferedImage(yuvWidth, yuvHeight, TYPE_INT_RGB);
        paintYuvFile();
    }
    private void paintYuvFile() {
        byte [] data = yuvReader.getData();
        int uOffset = yuvWidth * yuvHeight;
        int vOffset = uOffset * 2;
        for (int r = 0;r < yuvWidth;++r) {
            for (int c = 0;c < yuvHeight;++c) {
                int offset = c*yuvWidth + r;
                int luma = toInt(data[offset]);
                int cb = toInt(data[uOffset + offset]);
                int cr = toInt(data[vOffset + offset]);
                int rgb = convertYuvToRgb(luma, cb, cr);
                yuvImage.setRGB(r, c, rgb);
            }
        }
    }
    private void paintRTest() {
        yuvImage = null;
        yuvImage = new BufferedImage(256, 256, TYPE_INT_RGB);
        for (int r = 0;r < 256;++r) {
            for (int c = 0;c < 256;++c) {
                int rgb = convertYuvToRgb(r, c, c);
                yuvImage.setRGB(r, c, rgb);
            }
        }

    }
    private int toInt(byte in) {
        if (in < 0) {
            return (int)in + 256;
        }
        return (int)in;
    }

    private int convertYuvToRgb(int y, int cb, int cr) {
        // full range YUV to RGB
        int r = (int)(((float)y-16)*1.164 + ((float)cr-128)*1.596);
        int g = (int)(((float)y-16)*1.164 - ((float)cr-128)*0.813 - ((float)cb-128)*0.391);
        int b = (int)(((float)y-16)*1.164 + ((float)cb-128)*2.018);
        // YUV to RGB
        //int r = (int)((float)y + 1.371*(cr-128));
        //int g = (int)((float)y - 0.698*(cr-128) - 0.336*(cb-128));
        //int b = (int)((float)y + 1.732*(cb-128));
        if (r < 0) {
            ++minusR;
            r = 0;
        } else if (r > 255) {
            r = 255;
        }
        if (g < 0) {
            ++minusG;
            g = 0;
        } else if (g > 255) {
            g = 255;
        }
        if (b < 0) {
            ++minusB;
            b = 0;
        } else if (b > 255) {
            b = 255;
        }
        return (r << 16) + (g << 8) + b;
    }

    private int minusR = 0;
    private int minusG = 0;
    private int minusB = 0;

    public static void main(String args[]) {
        if (args.length < 3) {
            System.out.println("usage : YuvViewer yuv_file_name width height");
        } else {
            YuvViewer viewer = new YuvViewer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewer.setVisible(true);
            viewer.setSize(300, 300);
            viewer.setTitle("YUV Viewer");
        }
    }
}
