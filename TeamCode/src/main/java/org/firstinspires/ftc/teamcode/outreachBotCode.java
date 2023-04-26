package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name="Outreach Bot TeleOp", group="Linear OpMode")
//@Disabled
public class outreachBotCode extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor lifter = null;
    private Servo claw = null;

@Override
public void runOpMode() {
        rightDrive = hardwareMap.get(DcMotor.class, "leftwheel");
        leftDrive = hardwareMap.get(DcMotor.class, "rightwheel");
        lifter = hardwareMap.get(DcMotor.class,"lifter");
        claw = hardwareMap.get(Servo.class, "claw");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double leftPower;
            double rightPower;
            double lifterUp;
            double lifterDown;

            leftPower = -gamepad1.left_stick_y;
            rightPower = gamepad1.right_stick_y;
            lifterUp = gamepad1.right_trigger;
            lifterDown = gamepad1.left_trigger;
        if (gamepad1.a) {
            claw.setPosition(.8);
        }

if (gamepad1.x) {
    claw.setPosition(.4);
}
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            lifter.setPower(lifterUp - lifterDown);

            if (lifterUp < .18 && lifterDown == 0) {
                lifter.setPower(.18);
            }
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
