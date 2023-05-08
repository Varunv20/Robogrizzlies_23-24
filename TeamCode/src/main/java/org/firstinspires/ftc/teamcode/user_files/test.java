package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.openftc.easyopencv.OpenCvInternalCamera;

@TeleOp(name="test-cv-jiggle", group="Driver OP")
public class test extends LinearOpMode {

    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();
    public Servo grabber;
    double powersetterr = 1;
    public DcMotor br;
    public DcMotor fr;
    public DcMotor bl;
    public DcMotor fl;


    public DcMotor E;
    public ColorSensor color_sensor;


    static final int STREAM_WIDTH = 640; // modify for your camera
    static final int STREAM_HEIGHT = 480; // modify for your camera
    OpenCvWebcam webcam;
    opencvpipelines pipeline;





    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = null;
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1"); // put your camera's name here
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        pipeline = new opencvpipelines();
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(STREAM_WIDTH, STREAM_HEIGHT, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Camera Failed","");
                telemetry.update();
            }
        });





        br= hardwareMap.get(DcMotor.class, "BR");

        fr= hardwareMap.get(DcMotor.class, "FR");
        bl= hardwareMap.get(DcMotor.class, "BL");
        fl= hardwareMap.get(DcMotor.class, "FL");


        E = hardwareMap.get(DcMotor.class, "E");

        grabber = hardwareMap.get(Servo.class, "grab");
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        E.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        E.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fl.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.FORWARD);
        bl.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.FORWARD);

        // runs the moment robot is initialized
        waitForStart();
        runtime.reset();






        while (opModeIsActive()) {

            move();
            if (gamepad1.left_stick_button){
                if (powersetterr == 0.5){
                    powersetterr = 1.0;
                }
                if (powersetterr == 1.0){
                    powersetterr = 0.5;
                }
            }
/*
            if(gamepad1.dpad_left){
                if (powersetter > 0.5){
                    powersetter = 0.5;
                }
                else{
                    powersetter = 1;
                }
            }*/
            if(gamepad1.dpad_left){
                String debug2 = pipeline.debug1();
                telemetry.addData("debug ", debug2);
                telemetry.addData("yellow", yellow1);
                telemetry.addData("direction", direction);
            }
            if(gamepad1.dpad_up){
                jiggle_v2();
            }
            if(gamepad1.dpad_down){

                go = false;
                turn += 0.5;
            }
            if(gamepad1.dpad_right){

                forward_const += 0.5;
            }

            // for debug only


            if(gamepad1.left_trigger > 0.5){ grabber.setPosition(.35);
            }
            if(gamepad1.right_trigger > 0.5){grabber.setPosition(.550);}

            if(gamepad1.b){extend(0);}
            if(gamepad1.a){extend(1);}
            if(gamepad1.x){extend(2);}
            if(gamepad1.y){extend(  3);}
            telemetry.addData("fl",fl.getPower());
            telemetry.addData("fr",fr.getPower());
            telemetry.addData("bl",bl.getPower());
            telemetry.addData("e",E.getCurrentPosition());
            telemetry.addData("grab", grabber.getPosition());
            telemetry.addData("direction", direction);
            telemetry.addData("l1", l1);
            telemetry.addData("l2", l2);



            telemetry.update();
        }

    }








    void extend(int position) {

        switch (position) {
            case 0:
                if(E.getCurrentPosition()>10) {
                    E.setTargetPosition(0);
                    E.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    E.setPower(0.75);
                }else{
                    E.setPower(0);
                }
                break;
            case 1:
                E.setTargetPosition(1300);
                E.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                E.setPower(0.75);

                break;
            case 2:
                E.setTargetPosition(1994);
                E.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                E.setPower(0.75);

                break;
            case 3:
                E.setTargetPosition(2990);
                E.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                E.setPower(0.75);


                break;
        }
    }


    void move(){
        double horizontal = -gamepad1.left_stick_x*.5;   // this works so dont question it
        double vertical = gamepad1.left_stick_y*.5;
        double turn = -gamepad1.right_stick_x*2/3;
        //  E.setPower(gamepad1.left_stick_y);
        fl.setPower((Range.clip((vertical + horizontal + turn), -1, 1))/**powersetterr*/);
        fr.setPower((Range.clip((vertical - horizontal - turn), -1, 1))/**powersetterr*/);
        bl.setPower((Range.clip((vertical - horizontal + turn), -1, 1))/**powersetterr*/);
        br.setPower((Range.clip((vertical + horizontal - turn), -1, 1))/**powersetterr*/);
    }
    public class opencvpipelines extends OpenCvPipeline{
        Mat mat = new Mat();
        boolean thing = true;
        String called = "not called";
        //ArrayList<ArrayList<ArrayList<Double>>> image;
        double[][][] image = new double[STREAM_HEIGHT][STREAM_WIDTH][3];
        @Override
        public Mat processFrame(Mat input) {

            Mat c = input.clone();
            called = "point 1";
            int pixelsCounter = 0;

            called = "point 2";
            for (int i = 0; i < c.height(); i++) {
                for (int j = 0; j < c.width(); j++) {
                    called = "point 3";
                    pixelsCounter += 1;
//                    tmp.add(c.get(i, j)[0]);
//                    tmp.add(c.get(i, j)[1]);
//                    tmp.add(c.get(i, j)[2]);
                    image[i][j][0] = c.get(i, j)[0];
                    image[i][j][1] = c.get(i, j)[1];
                    image[i][j][2] = c.get(i, j)[2];
                    called = "point 4";
                    //t2.add(tmp);
                    called = "point 5";
                }
                //pixels.add(t2);
                called = "point 6";
            }
            called = "point 7";
            return c;
        }
        public String debug1(){
            String a =  "" + image[2][2][0];
            String b =  "" + image[2][2][1];
            String c =  "" + image[2][2][2];


            return called + " " + a + " " + b + " " + c;
        }
        public double[][][] get_pixels1(){
            return image;
        }
    }


    void move(double X, double Y, double T, double U, double TU, double P){
        // make sure to set motor mode to RUN_TO_POSITION and give it power!

        fl.setTargetPosition(fl.getCurrentPosition() + (int) (U * (Y + X)));//
        fr.setTargetPosition(fl.getCurrentPosition() + (int) (U * (Y - X)));//
        bl.setTargetPosition(fl.getCurrentPosition() + (int) (U * (Y - X)));//
        br.setTargetPosition(fl.getCurrentPosition() + (int) (U * (Y + X)));//

        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        fl.setPower(P);
        fr.setPower(P);
        bl.setPower(P);
        br.setPower(P);

        fl.setTargetPosition(fl.getCurrentPosition() + (int) (TU * T));
        fr.setTargetPosition(fl.getCurrentPosition() + (int) (TU * -T));
        bl.setTargetPosition(fl.getCurrentPosition() + (int) (TU * T));
        br.setTargetPosition(fl.getCurrentPosition() + (int) (TU * -T));

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    public double mse(double[] rgb){
        double[] yellow = {255,180,0};
        double sum = 0;
        for (int i = 0; i <rgb.length; i++) sum += Math.pow(yellow[i] - rgb[i], 2);

        return sum/3;

    }
    public boolean centered = false;
    public boolean forward = false;
    public boolean go = true;
    public float turn = 10;
    public double forward_const = 6;
    public double up_const = 10;
    public  int l1;
    public int l2;
    public void jiggle_v2(){
        int  times_ran = 0;
        l1 = center(pipeline.get_pixels1());
        times_ran += 1;
        if (!centered) {
            if (l1 != 0) {
                move(0, 0, l1 * turn, 0, 12.05, 1);
                sleep(300);
            }

        }



        l2 = f(pipeline.get_pixels1());
        if (!forward) {
            if (l2 != 0) {
                move(0, (STREAM_WIDTH/up_const - l2) * forward_const, 0, 0, 12.05, 1);
                sleep(300);
            }

        }


    }
    public int center(double[][][] array_of_pixels){
        int line_num  = (int)array_of_pixels[0].length/2;
        double[][] line_of_pixels = getColumn(array_of_pixels,line_num);
        int yellow_counter = 0;
        int yellows = 0;
        boolean prev_data = false;
        int highest_num_yellow = 0;
        for (int i = 0; i <line_of_pixels.length; i++){
            double j = mse(line_of_pixels[i]);
            if (j <= 200){
                yellow_counter += 1;
                prev_data = true;
            }
            else if(prev_data && highest_num_yellow <= yellow_counter){
                highest_num_yellow = yellow_counter;
                yellow_counter = 0;
                yellows = i;
            }
        }
        int r = yellows - line_of_pixels.length/2;
        if ((yellows > line_of_pixels.length/2 + 20) ||(yellows <= line_of_pixels.length/2 - 20) ) {

            return (int) yellows - line_of_pixels.length/2 ;
        }

        else{
            centered = false;
            go = false;
        }
        yellow1 = yellows;

        if(centered){
            direction = "Right";
        }
        else{
            direction = "Left";
        }

        return 0;
    }
    public String direction;
    public int yellow1;

    public int f(double[][][] array_of_pixels){
        int line_num  = (int)array_of_pixels[0].length/2;
        double[][] line_of_pixels = getColumn(array_of_pixels,line_num);
        int yellow_counter = 0;
        int yellows = 0;
        boolean prev_data = false;
        int highest_num_yellow = 0;
        for (int i = 0; i <line_of_pixels.length; i++){
            double j = mse(line_of_pixels[i]);
            if (j <= 700){
                yellow_counter += 1;
                prev_data = true;
            }
            else if(prev_data && highest_num_yellow <= yellow_counter){
                highest_num_yellow = yellow_counter;
                yellows = (int) (i - yellow_counter)/2;
            }
        }
        if (yellow_counter >= line_of_pixels.length/10){
            forward = true;
            return 0;

        }
        return yellow_counter;



    }
    public static double[][] getColumn(double[][][] array, int index){
        double[][] column = new double[array[0].length][3]; // Here I assume a rectangular 2D array!
        for(int i=0; i<column.length; i++){
            column[i] = array[i][index];
        }
        return column;
    }

}