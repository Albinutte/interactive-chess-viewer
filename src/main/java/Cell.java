import org.opencv.core.Mat;


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
}
