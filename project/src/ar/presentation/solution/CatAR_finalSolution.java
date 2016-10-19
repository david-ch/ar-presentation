package ar.presentation.solution;

import ar.presentation.utils.MatView;
import org.opencv.core.Core;

import static org.opencv.highgui.Highgui.CV_CAP_PROP_FRAME_WIDTH;
import static org.opencv.highgui.Highgui.CV_CAP_PROP_FRAME_HEIGHT;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CatAR_finalSolution
{
    public void run()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        final VideoCapture capture = new VideoCapture(0);
        int videoWidth = (int) capture.get(CV_CAP_PROP_FRAME_WIDTH);
        int videoHeight = (int) capture.get(CV_CAP_PROP_FRAME_HEIGHT);

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

        FrameProcessor frameProcessor = new FrameProcessor(videoWidth, videoHeight);
        Mat image = new Mat();
        while (capture.read(image))
        {
            matView.setImage(frameProcessor.processFrame(image));
        }
    }

    public static void main(String[] args)
    {
       new CatAR_finalSolution().run();
    }
}
