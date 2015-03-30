import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.*;


/**
 * Created by Albina on 2015-03-30.
 */
public class Cell {
    private Mat img;
    private boolean colour; //false = black, true = white

    public Cell(Mat img, boolean colour) {
        this.img = img;
        this.colour = colour;
    }

    public Mat getImg() {
        return img;
    }

    public void get_piece() {
        //binarizing
        Mat bin = new Mat();
        Imgproc.threshold(img, bin, 70, 255, Imgproc.THRESH_BINARY);
        Main.showMat(img);
        Main.showMat(bin);


        //deleting noise
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Mat hierarchy = new Mat();
        Mat bin_copy = bin.clone();
        Imgproc.findContours(bin_copy, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println(contours.size());

        for (MatOfPoint mop : contours)
            System.out.println(mop);
    }

    /**
     * deletes noise
     */
    private void clear_img() {
    }
}
