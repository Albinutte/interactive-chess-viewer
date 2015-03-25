import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.opencv.core.*;
import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main
{
    public static void main( String[] args )
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        PrintWriter writer = null;
        try {
            //opening output file
            writer = new PrintWriter("out.txt", "UTF-8");
            //IdentifyPieces identifier = new IdentifyPieces();

            //reading image
            Mat m = Highgui.imread("chuzhakinsystem2-07-page-001.jpg",
                    Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            System.out.println(m.size());

            //binarizing
            Mat bin = m.clone();
            for (int i = 0; i < bin.rows(); i++)
                for (int j = 0; j < bin.cols(); j++)
                    if (bin.get(i, j)[0] > 128)
                        bin.put(i, j, 255);
                    else
                        bin.put(i, j, 0);
            showMat(bin);


        } catch (IOException ex) {
            System.out.println("Didn't open file");
        }
        finally {
            try {
                writer.close();
            } catch (Exception ex)
            {
                System.out.println("Unable to close");
            }
        }
    }

    private static void showMat(Mat img) {
        //getting screen resolution to set optimal img dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int N = (int) (screenSize.getWidth() / 3);
        int rows = img.rows();
        int cols = img.cols();
        Imgproc.resize(img, img, new Size(N, N * rows / cols));
        MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Mat boxing(Mat img) {
        Mat res = img.clone();
        return res;
    }
}