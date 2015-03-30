import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.*;


/**
 * Created by Albina on 2015-03-30.
 */
public class Cell {
    private Mat img, clear_img, contours;
    private boolean colour, piece_colour; //false = black, true = white

    public Cell(Mat img, boolean colour) {
        this.img = img;
        this.clear_img = new Mat();
        this.contours = new Mat();
        this.colour = colour;
    }

    public Mat getImg() {
        return img;
    }

    public void get_piece() {
        clearify_img();
        Main.showMat(clear_img);
    }

    /**
     * Gets piece contours and img
     */
    private void clearify_img() {
        //binarizing
        Mat bin = new Mat();
        Imgproc.threshold(img, bin, 70, 255, Imgproc.THRESH_BINARY);
        Main.showMat(img);
        Main.showMat(bin);


        //piece contour and img detection
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Mat hierarchy = new Mat();
        Mat bin_copy = bin.clone();
        Imgproc.findContours(bin_copy, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        int max_size = -1, max_i = -1;
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint mop = contours.get(i);
            if (mop.rows() > max_size) {
                max_size = mop.rows();
                max_i = i;
            }
        }

        Imgproc.cvtColor(bin_copy, bin_copy, Imgproc.COLOR_GRAY2BGR);
        Imgproc.drawContours(bin_copy, contours, max_i, new Scalar(255, 255, 255), -1);
        Imgproc.cvtColor(bin_copy, bin_copy, Imgproc.COLOR_BGR2GRAY);

        Imgproc.threshold(bin_copy, clear_img, 128, 255, Imgproc.THRESH_BINARY_INV);
        this.contours = contours.get(max_i);
    }
}
