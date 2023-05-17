package org.firstinspires.ftc.teamcode.opencvpiplines;
/*
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.highgui.HighGui;
*/
public class OpenCV_SIM_Test {
    /*
    public static void main(String[] args) {
        // Load OpenCV native library

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        opencvpipelines opencvpipeline = new opencvpipelines();
        VideoCapture capture = new VideoCapture(0);

        // Check if camera is opened successfully
        if (!capture.isOpened()) {
            System.out.println("Could not open video stream");
            return;
        }

        // Read frames from the video stream and convert them to Mat objects
        Mat frame = new Mat();
        while (capture.read(frame)) {
            frame = opencvpipeline.processFrame(frame);
            HighGui.imshow("Video Stream", frame);
            HighGui.waitKey(33);
        }

        // Release the VideoCapture object and close the window
        capture.release();
        HighGui.destroyAllWindows();
    }
    */
}