package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp (name="fun", group="Linear OpMode")
//@Disabled
public class fun extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    private DcMotor spinnyThing = null;

    @Override
    public void runOpMode() {
        rightDrive = hardwareMap.get(DcMotor.class, "leftwheel");
        leftDrive = hardwareMap.get(DcMotor.class, "rightwheel");
        spinnyThing = hardwareMap.get(DcMotor.class,"spinner");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double leftPower;
            double rightPower;
            double spinnerPower;

            leftPower = -gamepad1.left_stick_y;
            rightPower = gamepad1.right_stick_y;
            spinnerPower = gamepad1.right_trigger;



            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            spinnyThing.setPower(spinnerPower);
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
