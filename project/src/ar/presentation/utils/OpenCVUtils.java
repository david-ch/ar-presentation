package ar.presentation.utils;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.opencv.core.Core.bitwise_not;
import static org.opencv.imgproc.Imgproc.*;

public class OpenCVUtils
{
    private static Mat dumbMat = new Mat();

    public static void releaseResources(List<? extends Mat> mats)
    {
        mats.forEach(Mat::release);
        mats.clear();
    }

    public static void releaseResources(Mat mat)
    {
        mat.release();
    }

    public static void printMatrix(byte[][] mat)
    {
        for (int i = 0; i < mat.length; i++)
        {
            System.out.println(Arrays.toString(mat[i]));
        }
        System.out.println();
    }

    public static byte[][] rotateMatrix(byte[][] mat)
    {
        int size = mat.length;
        byte[][] result = new byte[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                result[j][size - 1 - i] = mat[i][j];
            }
        }
        return result;
    }

    public static void applyAdaptiveThreshold(Mat source, Mat destination)
    {
        adaptiveThreshold(source, destination, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 117, 5);
    }

    public static void drawContoursOnFrame(List<MatOfPoint2f> contours, Mat frame, Scalar color)
    {
        List<MatOfPoint> convertedContours = contours.stream().map(OpenCVUtils::matOfPoint2f_to_matOfPoint).collect(Collectors.toList());
        drawContours(frame, convertedContours, -1, color);
        releaseResources(convertedContours);
    }

    public static Mat rotateImage(Mat image, int angle)
    {
        Point imageCenter = new Point(image.cols() / 2, image.rows() / 2);
        Mat rotationMatrix = getRotationMatrix2D(imageCenter, -angle, 1.0); // angle is negative because getRotationMatrix2D wants negative angle for clockwise rotation
        Mat dst = new Mat(image.height(), image.width(), image.type());
        warpAffine(image, dst, rotationMatrix, image.size());
        return dst;
    }

    public static Mat createZeroMat(Size size)
    {
        return new Mat(size, CvType.CV_8UC3);
    }

    public static boolean compareMatricies(byte[][] first, byte[][] second)
    {
        return Arrays.deepEquals(first, second);
    }

    public static double contourLength(MatOfPoint2f contour)
    {
        return arcLength(contour, true);
    }

    public static void convertToGray(Mat source, Mat destination)
    {
        cvtColor(source, destination, COLOR_BGR2GRAY);
    }

    public static MatOfPoint2f createBoundsMat(double width, double height)
    {
        Point[] points = new Point[4];
        points[0] = new Point(0, 0);
        points[1] = new Point(0, width);
        points[2] = new Point(height, width);
        points[3] = new Point(height, 0);
        return new MatOfPoint2f(points);
    }

    public static List<MatOfPoint2f> searchContours(Mat frame)
    {
        Mat invertedFrame = new Mat();
        bitwise_not(frame, invertedFrame); // inversion is for certain order of the points in contour
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(invertedFrame, contours, dumbMat, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        List<MatOfPoint2f> result = contours.stream().map(OpenCVUtils::matOfPoint_to_matOfPoint2f).collect(Collectors.toList());
        releaseResources(contours);
        releaseResources(invertedFrame);
        return result;
    }

    public static List<MatOfPoint2f> approximatePolygons(List<MatOfPoint2f> contours)
    {
        return contours.stream().map(OpenCVUtils::approximatePolygon).collect(Collectors.toList());
    }

    private static MatOfPoint2f approximatePolygon(MatOfPoint2f contour)
    {
        MatOfPoint2f polygon = new MatOfPoint2f();
        Imgproc.approxPolyDP(contour, polygon, contour.size().height * 0.01, true);
        return polygon;
    }

    private static MatOfPoint2f matOfPoint_to_matOfPoint2f(MatOfPoint matOfPoint)
    {
        return new MatOfPoint2f(matOfPoint.toArray());
    }

    private static MatOfPoint matOfPoint2f_to_matOfPoint(MatOfPoint2f matOfPoint2f)
    {
        return new MatOfPoint(matOfPoint2f.toArray());
    }

    public static Scalar color(int r, int g, int b)
    {
        return new Scalar(b, g, r);
    }

    public static Mat createWhiteMat(Size size)
    {
        Mat whiteMat = createZeroMat(size);
        bitwise_not(whiteMat, whiteMat);
        return whiteMat;
    }

}
