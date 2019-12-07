package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TeleOpMode extends LinearOpMode {
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor Elevator;
    private Servo servoTest;
    private Servo servoBackup;
    private DigitalChannel tSensor;
    @Override
    public void runOpMode() {
        motorLeft = hardwareMap.get(DcMotor.class, "motorLeft");
        motorRight = hardwareMap.get(DcMotor.class, "motorRight");
        Elevator = hardwareMap.get(DcMotor.class, "Elevator");
        servoTest = hardwareMap.get(Servo.class, "servoTest");
        servoBackup = hardwareMap.get(Servo.class,"servoBackup");
        tSensor = hardwareMap.get(DigitalChannel.class,"tSensor");
        tSensor.setMode(DigitalChannel.Mode.INPUT);
        Elevator.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        //CHECK IF ELEVATOR NEEDS TO BE REVERSED
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        double tgtPowerE;
        double maxEncoder;
        double speed;
        maxEncoder = 2325; //may need to be changed
        //Calibration code
        double currentEncoder;
        telemetry.addData("Touch Sensor", tSensor.getState());
        telemetry.update();
        Elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elevator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //End Calibration
        telemetry.addData("Calibrate", "Done");
        telemetry.update();
        speed = 2;

        waitForStart();
        //wait for game to start(driver presses play)
        while (opModeIsActive()) {
            tgtPowerE = -gamepad2.left_stick_y/2;
            currentEncoder = Elevator.getCurrentPosition();
            telemetry.addData("Elevator Encoder Value",Elevator.getCurrentPosition());
            telemetry.addData("Max Encode Value",maxEncoder);
            telemetry.addData("TargetPower",tgtPowerE);
            telemetry.update();
            //check if it is going up or down
            //add reset encoder if sensor is pressed
////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            //drive code
            ////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
            double tgtPowerL;
            double tgtPowerR;

            tgtPowerL = this.gamepad1.left_stick_y;
            tgtPowerR = this.gamepad1.left_stick_y;
            if (gamepad1.y) {
                speed = 2;
            }
            if (gamepad1.b) {
                speed = 4;
            }
            if (gamepad1.a) {
                speed = 6;
            }
            tgtPowerR = tgtPowerR / speed;
            tgtPowerL = tgtPowerL / speed;
            telemetry.addData("SPEED",1/speed);
            if (gamepad1.right_stick_x == 0) {
                //tgtPowerL
                //tgtPowerR = this.gamepad1.left_stick_y;
            } else if (gamepad1.right_stick_x > 0) {
                tgtPowerR = 1/speed;
                tgtPowerL = -tgtPowerR;
            } else if (gamepad1.right_stick_x < 0) {
                tgtPowerL = 1/speed;
                tgtPowerR = -tgtPowerL;
            }


            motorLeft.setPower(tgtPowerL);
            motorRight.setPower(tgtPowerR);

////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            //Backup Servo Code
            ////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
            if (gamepad2.dpad_up){
                servoBackup.setPosition(0);
            }
            if (gamepad2.dpad_down){
                servoBackup.setPosition(90);
            }
////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            //elevator claw code
            ////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
            if (gamepad2.a) {
                // move to 0 degrees.
                servoTest.setPosition(1);
            } else if (gamepad2.x || gamepad2.b) {
                // move to 90 degrees.
                servoTest.setPosition(0.5);
            } else if (gamepad2.y) {
                // move to 180 degrees.
                servoTest.setPosition(0);
            }
////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            //elevator code
            ////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
            if(tSensor.getState() == false) {
                Elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                Elevator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            if (tgtPowerE > 0) {
                //going up
                if (currentEncoder < maxEncoder) {
                    //check if its not at max height
                    Elevator.setPower(tgtPowerE);
                    //if not it keeps going up
                } else {
                    Elevator.setPower(0);
                    //do nothing
                }
            }
                else {
                    //going down
                    if (currentEncoder > 0) {
                        //check if it isn't at bottom
                        Elevator.setPower(tgtPowerE);
                        //keep moving
                    } else {
                        Elevator.setPower(0);
                        //do nothing, we are at the bottom
                    }
                }
            }
    }
}