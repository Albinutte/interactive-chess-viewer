import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by Albina on 2015-03-29.
 */
public class CellIdentifier {

    private static class Line implements Comparable<Line> {
        private org.opencv.core.Point start;
        private int length;

        public Line (org.opencv.core.Point start, int length) {
            this.length = length;
            this.start = start;
        }

        public org.opencv.core.Point getStart() {return this.start;}

        public int getLength() {return this.length;}


        public int compareTo(Line compareLine) {
            if (this.getStart().x != compareLine.getStart().x)
                return (int) (this.start.x - compareLine.getStart().x);
            else
                return (int) (this.start.y - compareLine.getStart().y);
        }
    }

    private Mat img, edges, bin;

    public CellIdentifier() {

    }

    public CellIdentifier(Mat m) {
        img = m;
        edges = new Mat();
        bin = new Mat();
    }

    public void refresh(Mat m) {
        img = m;
        edges = new Mat();
        bin = new Mat();
    }

    public ArrayList<Cell> find_cells() {
        //System.out.println(img.size());
        //binarizing
        Imgproc.threshold(img, bin, 170, 255, Imgproc.THRESH_BINARY);
        //Main.showMat(bin);

        //finding edges
        Imgproc.Canny(bin, edges, 80, 120);
        //Main.showMat(edges);

        //finding good lines
        ArrayList<Line> horizontal_lines = find_lines(edges);
        Collections.sort(horizontal_lines);
        Mat rotated = rotate_diag(edges);
        ArrayList<Line> vertical_lines = find_lines(rotated);
        Collections.sort(vertical_lines);

       /* for (Line line : horizontal_lines)
            System.out.println(line.getStart() + " " + line.getLength());*/

        //sorting lines
        ArrayList<Integer> xs = cuts(edges.cols(), horizontal_lines);
        ArrayList<Integer> ys = cuts(edges.rows(), vertical_lines);

        /*System.out.println(xs.size() + " " + ys.size());
        for (int y : ys)
            System.out.println(y);*/

        //getting cells
        int len = 8;
        ArrayList<Cell> res = new ArrayList<Cell>();
        boolean colour = true;
        for (int row = 0; row < len; row++) {
            if (row % 2 == 0)
                colour = true;
            for (int col = 0; col < len; col++) {
                Mat cell = img.submat(ys.get(row), ys.get(row + 1), xs.get(col), xs.get(col + 1));
                res.add(new Cell(cell, colour));
                colour = !colour;
            }
        }

        //System.out.println(res.size());
        return res;
    }

    private ArrayList<Line> find_lines(Mat diag) {
        ArrayList<Line>[] horizontal_lines  = (ArrayList<Line>[]) new ArrayList[diag.rows()];
        //scanning for lines
        for (int i = 0; i < diag.rows(); i++) {
            horizontal_lines[i] = new ArrayList<Line>();
            int start = -1;
            for (int j = 0; j < diag.cols(); j++)
                if (start >= 0) {
                    if (diag.get(i, j)[0] == 255.0) {
                        horizontal_lines[i].add(new Line(new org.opencv.core.Point(start, i), j - start));
                        start = -1;
                    }
                }
                else {
                    if (diag.get(i, j)[0] == 0.0) {
                        start = j;
                    }
                }
            if (start >= 0)
                horizontal_lines[i].add(new Line(new org.opencv.core.Point(start, i), diag.cols() - start));
        }

        //getting lines with good length
        int estimated = diag.cols() / 8;
        ArrayList<Line> good_lines = new ArrayList<Line>();

        double EPS = 0.1;
        for (ArrayList<Line> row : horizontal_lines)
            for (Line line : row)
                if (Math.abs(line.getLength() - estimated) < estimated * EPS)
                    good_lines.add(line);

        return good_lines;
    }

    private Mat rotate_diag(Mat diag) {
        Mat res = new Mat(new Size(diag.rows(), diag.cols()), diag.type());

        for (int i = 0; i < diag.rows(); i++)
            for (int j = 0; j < diag.cols(); j++)
                res.put(diag.cols() - j - 1, i, diag.get(i, j));

        return res;
    }

    private ArrayList<Integer> cuts (int img_size, ArrayList<Line> lines) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        ArrayList<Integer> len = new ArrayList<Integer>();

        int x = 0, last = 0, cnt = 0, last_cnt = 0, next = 0;
        for (Line line : lines) {
            int cur_x = (int) line.getStart().x;
            if (Math.abs(cur_x - last) < img_size / 8 / 4) {
                x += cur_x;
                cnt++;
                last = cur_x;
                next += cur_x + line.getLength();
            }
            else {
                res.add(x / (cnt + last_cnt));
                len.add(cnt + last_cnt);
                last_cnt = cnt;
                cnt = 1;
                last = cur_x;
                x = cur_x + next;
                next = 0;
            }
        }
        res.add(x / (cnt + last_cnt));
        len.add(cnt + last_cnt);
        len.add(img_size);
        len.set(0, img_size);
        res.add(next / cnt);

        while (res.size() > 9) {
            int min = img_size + 1, min_i = -1;
            for (int i = 0; i < len.size(); i++)
                if (min > len.get(i)) {
                    min = len.get(i);
                    min_i = i;
                }
            res.remove(min_i);
            len.remove(min_i);
        }

        return res;
    }
/*
    private ArrayList<Integer> cuts (int img_size, ArrayList<Line> lines) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        ArrayList<Integer> len = new ArrayList<Integer>();
        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> next = new ArrayList<Integer>();

        int last = 0, last_cnt = 0;
        for (Line line : lines) {
            int cur_x = (int) line.getStart().x;
            if (Math.abs(cur_x - last) < img_size / 8 / 4) {
                x.add(cur_x);
                last = cur_x;
                next.add(cur_x + line.getLength());
            }
            else {
                res.add(x.get(x.size() / 2));
                len.add(x.size());
                last = cur_x;
                next = 0;
            }
        }
        res.add(x / (cnt + last_cnt));
        len.add(cnt + last_cnt);
        len.add(img_size);
        len.set(0, img_size);
        res.add(next / cnt);

        while (res.size() > 9) {
            int min = img_size + 1, min_i = -1;
            for (int i = 0; i < len.size(); i++)
                if (min > len.get(i)) {
                    min = len.get(i);
                    min_i = i;
                }
            res.remove(min_i);
            len.remove(min_i);
        }

        return res;
    }*/

    private void debug_lines(ArrayList<Line> horizontal_lines, ArrayList<Line> vertical_lines) {
        Mat disp = img.clone();
        for (Line i : horizontal_lines) {
            Core.circle(disp, i.getStart(), 4, new Scalar(0));
            Core.circle(disp, new Point(i.getStart().x + i.getLength(), i.getStart().y), 4, new Scalar(0));
        }
        for (Line i : vertical_lines) {
            int x = (int) (i.getStart().y);
            int y = (int) (i.getStart().x);
            Core.circle(disp, new Point(x, y), 4, new Scalar(0));
            Core.circle(disp, new Point(x, y + i.getLength()), 4, new Scalar(0));
        }
        Main.showMat(disp);
    }
}
