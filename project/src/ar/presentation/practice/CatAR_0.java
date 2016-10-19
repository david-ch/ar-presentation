package ar.presentation.practice;

import ar.presentation.utils.CatARBase;
import org.opencv.core.Mat;

public class CatAR_0 extends CatARBase
{

    protected Mat processFrame(Mat frame)
    {
        return frame;
    }

    public static void main(String[] args)
    {
        new CatAR_0().run();
    }
}
