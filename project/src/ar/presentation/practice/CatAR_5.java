package ar.presentation.practice;

import ar.presentation.utils.CatARBase;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

import static ar.presentation.utils.OpenCVUtils.*;
import static org.opencv.core.Core.countNonZero;
import static org.opencv.imgproc.Imgproc.*;

public class CatAR_5 extends CatARBase
{
    Mat grayscaleFrame = new Mat();
    Size blurSize = new Size(3, 3);
    Mat binaryFrame = new Mat();
    List<MatOfPoint2f> contours = new ArrayList<>();
    List<MatOfPoint2f> polygons = new ArrayList<>();
    Size markerBoundsSize = new Size(80, 80);
    MatOfPoint2f markerBounds = createBoundsMat(markerBoundsSize.width, markerBoundsSize.height);
    Mat unwarpedMarker = new Mat();
    byte[][] marker = new byte[8][8];

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

                for (int i = 0; i <= 7; i++)
                {
                    for (int j = 0; j <= 7; j++)
                    {
                        Mat cell = unwarpedMarker.submat(i * 10, (i + 1) * 10, j * 10, (j + 1) * 10);
                        if (countNonZero(cell) > 70)
                        {
                            marker[i][j] = 0;
                        }
                        else
                        {
                            marker[i][j] = 1;
                        }
                        releaseResources(cell);
                    }
                }


                if (compareMatricies(marker, MarkerEthalon.markerEthalonUp))
                {
                    System.out.println("up");
                }
                else if (compareMatricies(marker, MarkerEthalon.markerEthalonRight))
                {
                    System.out.println("right");
                }


                releaseResources(markerUnwarpTransform);
            }
        }

        releaseResources(contours);
        releaseResources(polygons);
        return frame;
    }

    public static void main(String[] args)
    {
        new CatAR_5().run();
    }
}
