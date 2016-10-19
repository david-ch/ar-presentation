package ar.presentation.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.opencv.highgui.Highgui.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.highgui.Highgui.CV_CAP_PROP_FRAME_WIDTH;

public abstract class CatARBase
{
    protected Size screenSize;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void run()
    {
        final VideoCapture capture = new VideoCapture(0);
        int videoWidth = (int) capture.get(CV_CAP_PROP_FRAME_WIDTH);
        int videoHeight = (int) capture.get(CV_CAP_PROP_FRAME_HEIGHT);

        screenSize = new Size(videoWidth, videoHeight);

        JFrame frame = new JFrame();
        MatView matView = new MatView(videoWidth, videoHeight);
        frame.add(matView);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                capture.release();
            }
        });

        Mat image = new Mat();
        while (capture.read(image))
        {
            matView.setImage(processFrame(image));
        }
    }

    abstract protected Mat processFrame(Mat frame);
}
