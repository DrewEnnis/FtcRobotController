package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;




/**
 * This file contains an example of a Linear "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode is executed.
 *
 * This particular OpMode illustrates driving a 4-motor Omni-Directional (or Holonomic) robot.
 * This code will work with either a Mecanum-Drive or an X-Drive train.
 * Both of these drives are illustrated at https://gm0.org/en/latest/docs/robot-design/drivetrains/holonomic.html
 * Note that a Mecanum drive must display an X roller-pattern when viewed from above.
 *
 * Also note that it is critical to set the correct rotation direction for each motor.  See details below.
 *
 * Holonomic drives provide the ability for the robot to move in three axes (directions) simultaneously.
 * Each motion axis is controlled by one Joystick axis.
 *
 * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
 * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
 * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
 *
 * This code is written assuming that the right-side motors need to be reversed for the robot to drive forward.
 * When you first test your robot, if it moves backward when you push the left stick forward, then you must flip
 * the direction of all 4 motors (see code below).
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="TeleOp Drive", group="Linear Opmode")
public class driverControlled extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx leftFrontDrive = null;
    private DcMotorEx leftBackDrive = null;
    private DcMotorEx rightFrontDrive = null;
    private DcMotorEx rightBackDrive = null;
    private DcMotorEx lifter = null;
    private Servo claw = null;


    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive = hardwareMap.get(DcMotorEx.class, "3");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "2");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "1");
        leftBackDrive = hardwareMap.get(DcMotorEx.class, "0");
        lifter = hardwareMap.get(DcMotorEx.class, "lifter");
        claw = hardwareMap.get(Servo.class, "claw");
        //clawControl = hardwareMap.get(Servo.class, "clawControl");

        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.


        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();
//Motor test code below:
/*while (opModeIsActive()) {
    leftBackDrive.setPower(0);
    leftFrontDrive.setPower(0);
    rightBackDrive.setPower(0);
    rightFrontDrive.setPower(0);
// run until the end of the match (driver presses STOP)
    if (gamepad1.a) {
        leftBackDrive.setPower(0.5);
        sleep(1500);
        leftBackDrive.setPower(0);
    }
    if (gamepad1.b) {
        leftFrontDrive.setPower(0.5);
        sleep(1500);
        leftFrontDrive.setPower(0);
    }
    if (gamepad1.x) {
        rightBackDrive.setPower(0.5);
        sleep(1500);
        rightBackDrive.setPower(0);
    }
    if (gamepad1.y) {
        rightFrontDrive.setPower(0.5);
        sleep(1500);
        rightFrontDrive.setPower(0);
    }
}*/

        while (opModeIsActive()) {

            String clawState = null;
            // POV Mode uses left joystick to go forward & strafe, and right joystick to .
            double lateral = -gamepad1.right_stick_x;
            double axial = -gamepad1.left_stick_x/2;
            double yaw = gamepad1.right_stick_y;
            double liftUp = gamepad1.right_trigger;
            double lower = gamepad1.left_trigger;


            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower =  axial + lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial - lateral - yaw;
            double lifterPower = liftUp - lower;

            // Send calculated power to wheels
            leftFrontDrive.setPower(0.8 * leftFrontPower);
            rightFrontDrive.setPower(1 * rightFrontPower);
            leftBackDrive.setPower(1 * leftBackPower);
            rightBackDrive.setPower(0.8 * rightBackPower);
            lifter.setPower(lifterPower);

            if (gamepad1.a) {

                claw.setPosition(0.2);
            }

            if (gamepad1.b) {

                claw.setPosition(0.4);
            }

            if (lifterPower < .18 && lifterPower >=
                    0 ) {
                lifter.setPower(.18);
            }

            /*while (gamepad1.right_stick_button) {

                leftFrontDrive.setPower(.5 * leftFrontPower);
                rightFrontDrive.setPower(.5 * rightFrontPower);
                leftBackDrive.setPower(.5 * leftBackPower);
                rightBackDrive.setPower(.5 * rightBackPower);
            }

            while (gamepad1.dpad_up) {
                lifter.setPower(1);
            }

            while (gamepad1.dpad_down) {
                lifter.setPower(-1);
            }*/

           /* double tgtAngle = 0;


            if (gamepad1.y) {
                tgtAngle -= .1;
                clawControl.setPosition(tgtAngle);
            }

            if (gamepad1.a) {
                tgtAngle += .1;
                clawControl.setPosition(tgtAngle);
            }
          *//**//*  if (gamepad1.a) {

                clawControl.setPosition(clawControl.getPosition() + 5);
            }*//**//*

            double clawPosition = clawControl.getPosition();
            if (clawPosition < 90) {

                clawState = "Clamped";
            } else if (clawPosition > 90) {

                clawState = "Released";
            }*/

            // Show the elapsed game time and other factors of the robot
            //telemetry.addData("test", gamepad1.a);
            //*telemetry.addData("Claw value", clawControl.getPosition());
            //telemetry.addData("Claw Position", clawState);//*
            //telemetry.addData("Status", "Run Time: " + runtime.toString());
            //* telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            //telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);//*
            //telemetry.update();
        }
    }}
