package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


@Autonomous

public class AutoMode extends LinearOpMode {
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private Servo servoBackup;
    private Servo servoTest;
    BNO055IMU imu;
    Orientation angles;
    static double speed = 0.25;
    static double turn_speed = 0.2;
    static double coff = 0.05;
    static double new_speed;
    int automode =0;

    String [] my_modes = {
        "Left1",
        "Left2",
        "Right1",
        "Right2",
        "Blue Block Right2",
        "Blue Block Left",
        "Red Block Right",
        "Red Block Left1"

    };
    @Override
    public void runOpMode() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        motorLeft = hardwareMap.get(DcMotor.class, "motorLeft");
        servoTest = hardwareMap.get(Servo.class, "servoTest");
        servoBackup = hardwareMap.get(Servo.class,"servoBackup");
        motorRight = hardwareMap.get(DcMotor.class, "motorRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        IMUinit();

        while (!gamepad1.y){
            if(gamepad1.dpad_up) {
                automode = automode - 1;
                if (automode < 0) {
                    automode = my_modes.length - 1;
                }
                telemetry.addData("Current AutoMode:", my_modes[automode]);
                telemetry.update();
            }
            if (gamepad1.dpad_down) {
                automode = automode + 1;
                if (automode > my_modes.length - 1) {
                    automode = 0;
                }
                telemetry.addData("Current AutoMode:", my_modes[automode]);
                telemetry.update();
            }
            sleep(500);
        }
        telemetry.addData("SELECTED AUTO MODE:",my_modes[automode]);
        telemetry.addData("Auto mode Number",automode);
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        switch (automode) {
            case 0:
                //left1
                IMUmove(0, 1, speed);
                IMURturn(-90);
                telemetry.addData("Heading", angles.firstAngle);
                telemetry.update();
                IMUmove(-90, 1.8, speed);
                telemetry.addData("Heading", angles.firstAngle);
                telemetry.update();
                break;
            case 1:
                //left2
                IMUmove(0, 3, speed);
                IMURturn(-90);
                telemetry.addData("Heading", angles.firstAngle);
                telemetry.update();
                IMUmove(-90, 1.8, speed);
                telemetry.addData("Heading", angles.firstAngle);
                telemetry.update();
                break;
            case 2:
                //right1
                IMUmove(0, 1, speed);
                IMULturn(90);
                telemetry.addData("Heading", angles.firstAngle);
                telemetry.update();
                IMUmove(90, 1.7, speed);
                telemetry.addData("Heading", angles.firstAngle);
                telemetry.update();
                break;
            case 3:
                //right2
                IMUmove(0, 2.8, speed);
                IMULturn(90);
                telemetry.addData("Heading", angles.firstAngle);
                telemetry.update();
                IMUmove(90, 1.7, speed);
                telemetry.addData("Heading", angles.firstAngle);
                telemetry.update();
                break;
            case 4:
                //blueblockright2
                IMUmove(0, 3, -speed);
                servoBackup.setPosition(90);
                sleep(1500);
                IMUmove(0,1.5, speed);
                IMURturn(-90);
                IMUmove(-90,5, speed);
                IMULturn(90);
                servoBackup.setPosition(0);
                sleep(1000);
                IMUmove(90, 2.5, speed);
                break;
            case 5:
                //blueblockleft
                IMUmove(0, 2, -speed);
                IMURturn(-90);
                IMUmove(-90, 4, -speed);
                IMULturn(0);
                IMUmove(0, 2, -speed);
                servoBackup.setPosition(90);
                sleep(1500);
                IMUmove(0, 3, speed);
                IMURturn(-90);
                IMUmove(-90, 5, speed);
                IMULturn(90);
                servoBackup.setPosition(0);
                IMUmove(90, 1.8, speed);
                break;
            case 6:
                //redblockright
                IMUmove(0, 2, -speed);
                IMULturn(90);
                IMUmove(90, 4.8, -speed);
                IMURturn(0);
                IMUmove(0, 2, -speed);
                servoBackup.setPosition(90);
                sleep(1500);
                IMUmove(0, 3, speed);
                IMULturn(90);
                IMUmove(90, 5, speed);
                IMURturn(-90);
                servoBackup.setPosition(0);
                IMUmove(-90, 2.5, speed);
                break;
                case 7:
                //redblockleft1
                IMUmove(0, 3, -speed);
                servoBackup.setPosition(90);
                sleep(1500);
                IMUmove(0, 2.8, speed);
                IMULturn(90);
                IMUmove(90, 5, speed);
                IMURturn(-90);
                servoBackup.setPosition(0);
                IMUmove(-90, 2, speed);
                break;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////
            //Libraries
            ////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
    public void IMUmove(double holdAngle, double time, double speed) {
        motorLeft = hardwareMap.get(DcMotor.class,"motorLeft");
        motorRight = hardwareMap.get(DcMotor.class,"motorRight");
        ElapsedTime holdTimer = new ElapsedTime();
        double err;
        holdTimer.reset();
        while (holdTimer.time() < time) {
            err = getError(holdAngle);
            new_speed = speed - err * coff;
            motorLeft.setPower(new_speed);
            motorRight.setPower(speed);
        }
        motorLeft.setPower(0);
        motorRight.setPower(0);
        telemetry.addData("Heading", angles.firstAngle);
        telemetry.update();
    }
    public void IMULturn(int turnAngle) {
        motorLeft = hardwareMap.get(DcMotor.class, "motorLeft");
        motorRight = hardwareMap.get(DcMotor.class, "motorRight");

        while (getError(turnAngle) > 0) {
            motorRight.setPower(turn_speed);
            motorLeft.setPower(-turn_speed);
        }
        motorLeft.setPower(0);
        motorRight.setPower(0);
        telemetry.addData("Heading", angles.firstAngle);
        telemetry.update();
    }
    public void IMURturn(int turnAngle) {
     motorLeft = hardwareMap.get(DcMotor.class, "motorLeft");
     motorRight = hardwareMap.get(DcMotor.class, "motorRight");

        while (getError(turnAngle) < 0) {
            motorRight.setPower(-turn_speed);
            motorLeft.setPower(turn_speed);
        }
        motorLeft.setPower(0);
        motorRight.setPower(0);
        telemetry.addData("Heading", angles.firstAngle);
        telemetry.update();
    }

    public void IMUinit() {
        BNO055IMU imu;
        Orientation angles;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }
    public float MyAngleUpdate() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("Heading", angles.firstAngle);
        telemetry.update();
        return angles.firstAngle;
    }
    public double getError(double targetAngle) {
        double robotError;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        robotError = targetAngle - angles.firstAngle;
        return robotError;
    }
}