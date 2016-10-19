package ar.presentation.practice;

import ar.presentation.utils.CatARBase;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

import static ar.presentation.utils.OpenCVUtils.*;
import static org.opencv.imgproc.Imgproc.blur;

public class CatAR_2 extends CatARBase
{
    Mat grayscaleFrame = new Mat();
    Size blurSize = new Size(3, 3);
    Mat binaryFrame = new Mat();
    List<MatOfPoint2f> contours = new ArrayList<>();
    List<MatOfPoint2f> polygons = new ArrayList<>();
    
    protected Mat processFrame(Mat frame)
    {
        convertToGray(frame, grayscaleFrame);
        blur(grayscaleFrame, grayscaleFrame, blurSize);
        applyAdaptiveThreshold(grayscaleFrame, binaryFrame);

        contours = searchContours(binaryFrame);
        polygons = approximatePolygons(contours);

        releaseResources(contours);
        releaseResources(polygons);
        return frame;
    }

    public static void main(String[] args)
    {
        new CatAR_2().run();
    }
}
