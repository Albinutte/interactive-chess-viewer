import org.opencv.core.*;
import org.opencv.highgui.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by malinovsky239 on 25.03.2015.
 */
public class IdentifyPieces {

    private static List<Mat> piecesImages;
    private static List<Character> piecesTypes;

    public IdentifyPieces() {
        piecesImages = new ArrayList<Mat>();
        piecesTypes = new ArrayList<Character>();
        loadLearningSamples();
        learn();
    }

    private static void loadLearningSamples() {
        int N = 9;
        Mat[] samples;
        samples = new Mat[N];
        for (int i = 0; i < N; i++) {
            String imageName = "chessDiagramSamples/" + Integer.toString(i) + ".png";
            String annotationName = "chessDiagramSamples/" + Integer.toString(i) + ".txt";

            Mat diagramImage = Highgui.imread(imageName,
                    Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            samples[i] = diagramImage.clone();
            for (int x = 0; x < samples[i].rows(); x++)
                for (int y = 0; y < samples[i].cols(); y++)
                    if (samples[i].get(x, y)[0] > 128)
                        samples[i].put(x, y, 255);
                    else
                        samples[i].put(x, y, 0);
            //showMat(samples[i]);
            for (int y = 0; y < 8; y++)
                for (int x = 0; x < 8; x++) {
                    int h = samples[i].rows();
                    int w = samples[i].cols();
                    Rect region = new Rect(h / 8 * y, w / 8 * x, h / 8, w / 8);
                    piecesImages.add(new Mat(samples[i], region));

                }
        }
    }

    private void learn() {

    }
}
