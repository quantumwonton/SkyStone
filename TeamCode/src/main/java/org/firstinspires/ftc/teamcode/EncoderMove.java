package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous


public class EncoderMove extends LinearOpMode {
    private DcMotor motorLeft;
    private DcMotor motorRight;
    @Override
    public void runOpMode() {
        motorLeft = hardwareMap.get(DcMotor.class, "motorLeft");
        motorRight = hardwareMap.get(DcMotor.class, "motorRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setPower(.3);
        motorLeft.setPower(.3);
        motorLeft.setTargetPosition(11500);
        motorRight.setTargetPosition(11500);
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(0);
        motorRight.setPower(0);
        sleep(10000);
    }
}