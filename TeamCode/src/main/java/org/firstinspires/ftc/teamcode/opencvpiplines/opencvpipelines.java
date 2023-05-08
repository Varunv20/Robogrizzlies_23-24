package  org.firstinspires.ftc.teamcode.opencvpiplines;



import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class opencvpipelines extends OpenCvPipeline{

        Mat mat = new Mat();
        boolean thing = true;
        double rotate;
        int max = 65;
        int min = 25;
        double forward;
        double[] yellow = {240,180,30};

       // Telemetry telemetry;

        public opencvpipelines() {
                //this.telemetry = telemetry;
        }
        double[] hsv = new double[3];
        public double[] RGBtoHSV(double r, double g, double b) {

                double h, s, v;

                double min, max, delta;

                min = Math.min(Math.min(r, g), b);
                max = Math.max(Math.max(r, g), b);

                // V/* www  .  j  a v a 2 s . co  m*/
                v = max;

                delta = max - min;

                // S
                if (max != 0)
                        s = delta / max;
                else {
                        s = 0;
                        h = -1;
                        return new double[] { h, s, v };
                }

                // H
                if (r == max)
                        h = (g - b) / delta; // between yellow & magenta
                else if (g == max)
                        h = 2 + (b - r) / delta; // between cyan & yellow
                else
                        h = 4 + (r - g) / delta; // between magenta & cyan

                h *= 60; // degrees

                if (h < 0)
                        h += 360;

                h = h * 1.0;
                s = s * 100.0;
                v = (v / 256.0) * 100.0;
                return new double[] { h, s, v };
        }
        public boolean mae(double[] l) {
               hsv =  RGBtoHSV(l[0],l[1], l[2]);
               return ( hsv[0] < max &&  hsv[0] > min );
        }
        @Override
        public Mat processFrame(Mat input) {
                Mat c = input.clone();
                int coord1 = 0;
                int coord2 = 0;
                int current_coord1 = 0;
                int current_coord2 = 0;

                int prev_yellow_streak = 0;
                int high_yellow_streak = 0;
             //   telemetry.addData("jo",c.width());



                for (int j = 0; j < c.width(); j++) {
                        //telemetry.addData("j",j);
                     //   telemetry.addData("color",mae(c.get( c.height()/2, j)));
                        //telemetry.addData("color2",c.get( c.height()/2, j)[0]);
                        //telemetry.addData("color3",c.get( c.height()/2, j)[1]);
                        //telemetry.addData("color4",c.get( c.height()/2, j)[2]);

                        if (mae(c.get( c.height()/2, j))){
                                if (prev_yellow_streak>0){
                                        prev_yellow_streak++;
                                        //telemetry.addData("color2",c.get( c.height()/2, j)[0]);
                                        //telemetry.addData("color3",c.get( c.height()/2, j)[1]);
                                        //telemetry.addData("color4",c.get( c.height()/2, j)[2]);


                                }
                                else{
                                        prev_yellow_streak++;
                                        current_coord1 = j;
                                }


                        }
                        else {

                                if( prev_yellow_streak > high_yellow_streak){
                                        high_yellow_streak = prev_yellow_streak;
                                        coord1 = current_coord1;
                                        coord2 = j;

                                }
                                prev_yellow_streak = 0;
                        }
                }
              //  telemetry.addData("coord1", false);
                //telemetry.addData("coord1", false);
                //telemetry.addData("coord1", coord1);
                //telemetry.addData("coord2", coord2);

                Imgproc.rectangle (
                        c,
                        new Point(coord1, c.height()/2 + 11),
                        new Point(coord2, c.height()/2 - 10),
                        new Scalar(0, 255, 255),
                        1
                );

                return c;
        }
}
