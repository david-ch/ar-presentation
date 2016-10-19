package ar.presentation.practice;

import ar.presentation.utils.CatARBase;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

import static ar.presentation.utils.MatViewUtil.showMat;
import static ar.presentation.utils.OpenCVUtils.*;
import static org.opencv.imgproc.Imgproc.blur;
import static org.opencv.imgproc.Imgproc.getPerspectiveTransform;
import static org.opencv.imgproc.Imgproc.warpPerspective;

public class CatAR_3 extends CatARBase
{
    Mat grayscaleFrame = new Mat();
    Size blurSize = new Size(3, 3);
    Mat binaryFrame = new Mat();
    List<MatOfPoint2f> contours = new ArrayList<>();
    List<MatOfPoint2f> polygons = new ArrayList<>();
    Size markerBoundsSize = new Size(80, 80);
    MatOfPoint2f markerBounds = createBoundsMat(markerBoundsSize.width, markerBoundsSize.height);
    Mat unwarpedMarker = new Mat();
    
    protected Mat processFrame(Mat frame)
    {
        convertToGray(frame, grayscaleFrame);
        blur(grayscaleFrame, grayscaleFrame, blurSize);
        applyAdaptiveThreshold(grayscaleFrame, binaryFrame);

        contours = searchContours(binaryFrame);
        polygons = approximatePolygons(contours);

        for (MatOfPoint2f polygon : polygons)
        {
            if (polygon.total() == 4 && contourLength(polygon) > 15)
            {
                Mat markerUnwarpTransform = getPerspectiveTransform(polygon, markerBounds);
                warpPerspective(binaryFrame, unwarpedMarker, markerUnwarpTransform, markerBoundsSize);

                releaseResources(markerUnwarpTransform);
            }
        }

        releaseResources(contours);
        releaseResources(polygons);
        return frame;
    }

    public static void main(String[] args)
    {
        new CatAR_3().run();
    }
}
