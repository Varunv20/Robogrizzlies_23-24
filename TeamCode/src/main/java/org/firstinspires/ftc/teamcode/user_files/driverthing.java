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

@TeleOp(name="no", group="Driver OP")
public class driverthing extends LinearOpMode {

    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();
    public Servo grabber;
    double powersetterr = 1;

    public DcMotor fl;
    public DcMotor fr;
    public DcMotor bl;
    public DcMotor br;
    public DcMotor E;
    public ColorSensor color_sensor;







    @Override
    public void runOpMode() {







        fl= hardwareMap.get(DcMotor.class, "FL");
        fr= hardwareMap.get(DcMotor.class, "FR");
        bl= hardwareMap.get(DcMotor.class, "BL");
        br= hardwareMap.get(DcMotor.class, "BR");

        E = hardwareMap.get(DcMotor.class, "E");

        grabber = hardwareMap.get(Servo.class, "grab");

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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






        boolean pressedLas = false;
        while (opModeIsActive()) {

            move();

/*
            if(gamepad1.left_stick_button && !rishi)
            {
                if (powersetterr == 1.0)
                {
                    powersetterr = 0.5;
                }
                else
                {
                    powersetterr = 1.0;
                }

            }
            rishi = gamepad1.left_stick_button;
*/




            if(gamepad1.left_trigger > 0.5){ grabber.setPosition(.35);
            }

            if(gamepad1.left_bumper){
                E.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                while(gamepad1.left_bumper){
                E.setPower(-1);
                }
                E.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                E.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                E.setPower(0);
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
            telemetry.update();
        }

    }










    // void grab(){
    //}
    //void ungrab(){
    //}

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




    }

