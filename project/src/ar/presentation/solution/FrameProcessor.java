package ar.presentation.solution;

import org.opencv.core.*;

import java.util.ArrayList;
import java.util.List;

import static ar.presentation.utils.OpenCVUtils.*;
import static org.opencv.core.Core.*;
import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.*;

public class FrameProcessor
{
    Size screenSize;
    Mat grayscaleFrame = new Mat();
    Size blurSize = new Size(3, 3);
    Mat binaryFrame = new Mat();
    List<MatOfPoint2f> contours = new ArrayList<>();
    List<MatOfPoint2f> polygons = new ArrayList<>();
    MatOfPoint2f markerBounds = createBoundsMat(80, 80);
    Size markerBoundsSize = new Size(80, 80);
    Mat unwarpedMarker = new Mat();

    // marker schema. 0 - white cell, 1 - black cell
    byte[][] markerEthalonUp = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };


    byte[][] markerEthalonRight = rotateMatrix(markerEthalonUp);
    byte[][] markerEthalonDown = rotateMatrix(markerEthalonRight);
    byte[][] markerEthalonLeft = rotateMatrix(markerEthalonDown);

    Mat imageUp = imread("resources/cat.jpg");
    Mat imageRight = rotateImage(imageUp, 90);
    Mat imageDown = rotateImage(imageUp, 180);
    Mat imageLeft = rotateImage(imageUp, 270);
    MatOfPoint2f imageBounds = createBoundsMat(imageUp.width(), imageUp.height());

    byte[][] marker = new byte[8][8];

    Mat whiteMat;

    public FrameProcessor(int screenWidth, int screenHeight)
    {
        screenSize = new Size(screenWidth, screenHeight);

        whiteMat = createZeroMat(imageUp.size());
        bitwise_not(whiteMat, whiteMat);
    }

    public Mat processFrame(Mat frame)
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
                warpPerspective(grayscaleFrame, unwarpedMarker, markerUnwarpTransform, markerBoundsSize);
                applyAdaptiveThreshold(unwarpedMarker, unwarpedMarker);

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

                Mat image = null;
                if (compareMatricies(marker, markerEthalonUp))
                {
                    image = imageUp;
                }
                else if (compareMatricies(marker, markerEthalonRight))
                {
                    image = imageRight;
                }
                else if (compareMatricies(marker, markerEthalonDown))
                {
                    image = imageDown;
                }
                else if (compareMatricies(marker, markerEthalonLeft))
                {
                    image = imageLeft;
                }


                if (image != null)
                {
                    Mat imageTransform = getPerspectiveTransform(imageBounds, polygon);


                    Mat blackBackground = createZeroMat(image.size()); // сделать более понятные имена
                    warpPerspective(image, blackBackground, imageTransform, screenSize);

                    Mat frameWithHole = createZeroMat(image.size()); // сделать более понятные имена
                    warpPerspective(whiteMat, frameWithHole, imageTransform, screenSize);

                    bitwise_not(frameWithHole, frameWithHole);

                    bitwise_and(frameWithHole, frame, frameWithHole);
                    bitwise_or(frameWithHole, blackBackground, frame);

                    releaseResources(blackBackground);
                    releaseResources(frameWithHole);
                    releaseResources(imageTransform);
                }

                releaseResources(markerUnwarpTransform);
            }
        }



        releaseResources(contours);
        releaseResources(polygons);
        return frame;
    }
}
