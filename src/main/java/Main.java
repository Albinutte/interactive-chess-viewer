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
    public static void main( String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //reading image
        Mat m = Highgui.imread("chessDiagramSamples/5.png",
                Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        showMat(m);

        //finding cells
        CellIdentifier cellIdentifier = new CellIdentifier(m);
        Mat cells = cellIdentifier.find_cells();
        showMat(cells);
    }

    /*
    public static void main( String[] args )
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        PrintWriter writer = null;
        try {
            //opening output file
            writer = new PrintWriter("out.txt", "UTF-8");

            //reading image
            Mat m = Highgui.imread("chessDiagramSamples/5.png",
                    Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            showMat(m);

            CellIdentifier cellIdentifier = new CellIdentifier(m);
            Mat cells = cellIdentifier.find_cells();
            showMat(cells);


            //finding corners
            Mat corners = new Mat();
            Imgproc.cornerHarris(edges, corners, 2, 3, 0.04, Imgproc.BORDER_DEFAULT);
            Core.normalize(corners, corners, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1, new Mat());
            Core.convertScaleAbs(corners, corners);
            showMat(corners);

            //drawing cirsles there
            int thresh = 100;
            for (int i = 0; i < corners.rows(); i++)
                for (int j = 0; j < corners.cols(); j++)
                    if (corners.get(i, j)[0] > thresh)
                        Core.circle(corners, new Point(j, i), 5, new Scalar(0), 2, 8, 0);
            showMat(corners);

        } catch (IOException ex) {
            System.out.println("Didn't open file");
        }
        finally {
            try {
                writer.close();
            } catch (java.lang.NullPointerException ex) {
                System.out.println("Unable to close");
            }
        }
    }
    */

    public static void showMat(Mat img) {
        //getting screen resolution to set optimal img dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int N = (int) (screenSize.getWidth() / 3);
        int rows = img.rows();
        int cols = img.cols();
        Imgproc.resize(img, img, new Size(N, N * rows / cols));
        MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}