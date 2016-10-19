package ar.presentation.practice;

import org.opencv.core.Mat;

import static ar.presentation.utils.OpenCVUtils.rotateImage;
import static org.opencv.highgui.Highgui.imread;

public class CatImage
{
    public static Mat imageUp = imread("resources/cat.jpg");
    public static Mat imageRight;
    public static Mat imageDown;
    public static Mat imageLeft;
}
