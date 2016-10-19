package ar.presentation.practice;

import ar.presentation.utils.CatARBase;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import static ar.presentation.utils.OpenCVUtils.applyAdaptiveThreshold;
import static ar.presentation.utils.OpenCVUtils.convertToGray;
import static org.opencv.imgproc.Imgproc.blur;

public class CatAR_1 extends CatARBase
{
    Mat grayscaleFrame = new Mat();
    Size blurSize = new Size(3, 3);
    Mat binaryFrame = new Mat();
    
    protected Mat processFrame(Mat frame)
    {
        convertToGray(frame, grayscaleFrame);
//        blur(grayscaleFrame, grayscaleFrame, blurSize);
//        applyAdaptiveThreshold(grayscaleFrame, binaryFrame);

        return frame;
    }

    public static void main(String[] args)
    {
        new CatAR_1().run();
    }
}
