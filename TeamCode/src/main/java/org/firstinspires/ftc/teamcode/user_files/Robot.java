
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Robot {

    // a class to import in order to have basic robot
    // comes with 4g of ram and 8g of storage, crazy!

    // motors
    public DcMotor FL;
    public DcMotor FR;
    public DcMotor BL;
    public DcMotor BR;

    // autonomous strafe constants (trial & error so that units are meters in autonomous)
    public double AYmult;
    public double AXmult;
    public double ATmult;

    // driverop constants
    public double DXmult = .5;
    public double DYmult = .5;
    public double DTmult = .5;

    // for extender
    public DcMotor extender;
    public double extenderCmMultiple;
    public double extendSpeed;
    // for arm
    public DcMotor shoulder;    // unused currently, may be implemented in future years
    public DcMotor elbow;
    public DcMotor wrist;

    // for claw or other extension
    public Servo claw;          // used lol
    public double clawOpenPos;
    public double clawClosePos;

    public boolean usesArm = false;
    public boolean usesExt = true;
    double powersetterr = 1.0;
    public Gamepad driverGamepad;

    public Robot(Gamepad g, HardwareMap h, String configuration) {
        driverGamepad = g;

        // initializes motors
        initMotor(h);

        switch (configuration) {
            case "basic mecanum":
                // figure this out next year
                break;
            case "boGilda":
                //TODO trial&error this
                FL.setDirection(DcMotor.Direction.REVERSE);
                FR.setDirection(DcMotor.Direction.FORWARD);
                BL.setDirection(DcMotor.Direction.REVERSE);
                BR.setDirection(DcMotor.Direction.FORWARD);
                break;
        }
    }

    void initMotor(HardwareMap hardwareMap) {
        FL = hardwareMap.get(DcMotor.class, "FL");
        FR = hardwareMap.get(DcMotor.class, "FR");
        BL = hardwareMap.get(DcMotor.class, "BL");
        BR = hardwareMap.get(DcMotor.class, "BR");

        if (usesArm) {
            //figure out with arm
        }
        if (usesExt) {
            extender = hardwareMap.get(DcMotor.class, "EXT");
        }
    }

    //driver controlled time
    public void driverMove() {
        //units defined for readability
        double horizontal = driverGamepad.left_stick_x * DXmult;   // this works so dont question it
        double vertical = driverGamepad.left_stick_y * DYmult;
        double turn = driverGamepad.right_stick_x * DTmult;
        double extenderPosCM = extender.getCurrentPosition() * extenderCmMultiple + driverGamepad.right_stick_y * extenderCmMultiple * extendSpeed;
        //the god code. we do not change this. too much work to trial&error
        FL.setPower(Range.clip((vertical + horizontal - turn), -1, 1)*powersetterr);
        FR.setPower(Range.clip((vertical - horizontal + turn), -1, 1)*powersetterr);
        BL.setPower(Range.clip((vertical - horizontal - turn), -1, 1)*powersetterr);
        BR.setPower(Range.clip((vertical + horizontal + turn), -1, 1)*powersetterr);

        extender.setTargetPosition((int) (extenderPosCM / extenderPosCM));
    }

    public void runAutonomous1() {


        //                                         //
        //         INSERT AUTONOMOUS HERE          //
        //(in terms of the functions defined below)//
        //                                         //


    }

    public void runAutonomous2() {


        //                                         //
        //         INSERT AUTONOMOUS HERE          //
        //(in terms of the functions defined below)//
        //                                         //


    }

    public void autoMove(int revx, int revy) {
        FL.setTargetPosition((int) (FL.getCurrentPosition() + Range.clip((revx * AXmult + revy * AYmult), -1, 1)));
        FR.setTargetPosition((int) (FR.getCurrentPosition() + Range.clip((revx * AXmult - revy * AYmult), -1, 1)));
        BL.setTargetPosition((int) (BL.getCurrentPosition() + Range.clip((revx * AXmult - revy * AYmult), -1, 1)));
        BR.setTargetPosition((int) (BR.getCurrentPosition() + Range.clip((revx * AXmult + revy * AYmult), -1, 1)));
    }

    public void autoTurn(double deg) {
        FL.setTargetPosition(FL.getCurrentPosition() + Range.clip((int) (-deg * ATmult), -1, 1));
        FR.setTargetPosition(FR.getCurrentPosition() + Range.clip((int) (deg * ATmult), -1, 1));
        BL.setTargetPosition(BL.getCurrentPosition() + Range.clip((int) (-deg * ATmult), -1, 1));
        BR.setTargetPosition(BR.getCurrentPosition() + Range.clip((int) (deg * ATmult), -1, 1));
    }

    public void extender(double CM) {
        extender.setTargetPosition((int) (CM * extenderCmMultiple));
        extender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extender.setPower(1);
    }

    public void openClaw() {
        claw.setPosition(clawOpenPos);
    }

    public void closeClaw() {
        claw.setPosition(clawClosePos);
    }

    public void motorModeReset() {
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void motorModeEncoder() {
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void motorModePosition() {
        FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}