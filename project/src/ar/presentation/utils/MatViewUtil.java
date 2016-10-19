package ar.presentation.utils;

import org.opencv.core.Mat;

import javax.swing.*;

public class MatViewUtil extends JFrame
{
    private static MatViewUtil instance;

    private final MatView matView;

    private MatViewUtil()
    {
        matView = new MatView(80, 80);
        add(matView);
        pack();
        setLocationByPlatform(true);
    }

    public static void showMat(Mat mat)
    {
        getInstance().matView.setImage(mat);
    }

    private static MatViewUtil getInstance()
    {
        if (instance == null)
        {
            instance = new MatViewUtil();
            getInstance().setVisible(true);
        }
        return instance;
    }
}
