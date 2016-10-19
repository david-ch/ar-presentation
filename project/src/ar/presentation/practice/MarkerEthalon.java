package ar.presentation.practice;

import static ar.presentation.utils.OpenCVUtils.rotateMatrix;

public class MarkerEthalon
{
    public static byte[][] markerEthalonUp = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };

    public static byte[][] markerEthalonRight = rotateMatrix(markerEthalonUp);
    public static byte[][] markerEthalonDown = rotateMatrix(markerEthalonRight);
    public static byte[][] markerEthalonLeft = rotateMatrix(markerEthalonDown);
}
