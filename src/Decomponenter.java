import org.opencv.core.Mat;

/**
 * Created by Albina on 2015-03-24.
 */
public class Decomponenter {
    private Mat m = null;
    private boolean initiated = false;
    private int[][] classes = null;
    private int rows = 0;
    private int cols = 0;

    public Decomponenter() {}

    public Decomponenter(Mat m) {
        this.init(m);
    }

    public void init(Mat m) {
        this.m = m;
        rows = m.rows();
        cols = m.cols();
        classes = new int[rows][cols];
        initiated = true;
    }

    public void refresh() {
        if (isInitiated())
            classes = new int[rows][cols];
    }

    public boolean isInitiated() {
        return initiated;
    }

    private void dfs(int row, int col, int from_class) {
        if (row + 1 < rows)
            if (m.get(row + 1, col)[0] == 0) {
                classes
            }
    }
}
