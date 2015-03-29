import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by Albina on 2015-03-29.
 */
public class CellIdentifier {

    private class Line {
        private Point start;
        private int length;

        public Line (Point start, int length) {
            this.length = length;
            this.start = start;
        }

        public Point getStart() {return this.start;}

        public int getLength() {return this.length;}
    }

    private Mat img;
    private ArrayList<Line>[] lines;

    public CellIdentifier(Mat m)
    {
        this.img = m;
        lines = (ArrayList<Line>[]) new ArrayList[m.rows()];
    }

    public Mat find_cells() {
        //binarizing
        Mat bin = new Mat();
        Imgproc.threshold(img, bin, 170, 255, Imgproc.THRESH_BINARY);

        //finding edges
        Mat edges = new Mat();
        Imgproc.Canny(bin, edges, 80, 120);

        //scanning for lines
        for (int i = 0; i < img.rows(); i++) {
            lines[i] = new ArrayList<Line>();
            int start = -1;
            for (int j = 0; j < img.cols(); j++)
                if (start >= 0)
                    if (img.get(i, j)[0] == 255)
                        lines[i].add(new Line(new Point(i, start), j - start));
                    else
                        start = -1;
                else
                    if (img.get(i, j)[0] == 0)
                        start = j;
            if (start >= 0)
                lines[i].add(new Line(new Point(i, start), img.cols() - start));
        }

        return edges;
    }

                /*
            TODO:
            loop over rows, --check
            get all black parts, --check
            sort by left edge,
            look at the right edge,
            cut,
            get over cells
             */
}
