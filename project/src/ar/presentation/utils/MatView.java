package ar.presentation.utils;

import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class MatView extends JComponent
{
    private final BufferedImage prepared3ChannelImage;
    private final BufferedImage prepared1ChannelImage;
    private boolean is1channel = false;
    private final byte image3ChannelBuffer[];
    private final byte image1ChannelBuffer[];

    public MatView(int width, int height)
    {
        prepared3ChannelImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        image3ChannelBuffer = new byte[width * height * 3]; // 3 - channels count

        prepared1ChannelImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        image1ChannelBuffer = new byte[width * height];

        setPreferredSize(new Dimension(width, height));
    }

    public void setImage(Mat image)
    {
        prepareMatForDrawing(image);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        if (is1channel)
        {
            g.drawImage(prepared1ChannelImage, 0, 0, null);
        }
        else
        {
            g.drawImage(prepared3ChannelImage, 0, 0, null);
        }
    }

    private void prepareMatForDrawing(Mat mat)
    {
        byte[] buffer;
        BufferedImage image;
        if (mat.channels() == 3)
        {
            buffer = image3ChannelBuffer;
            image = prepared3ChannelImage;
            is1channel = false;
        }
        else
        {
            buffer = image1ChannelBuffer;
            image = prepared1ChannelImage;
            is1channel = true;
        }

        mat.get(0, 0, buffer);
        byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
    }
}

