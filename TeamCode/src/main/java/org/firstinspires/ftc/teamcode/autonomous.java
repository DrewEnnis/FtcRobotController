package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.util.Arrays;
import java.nio.ByteBuffer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import android.os.Environment;
import java.lang.Math;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous
public class autonomous extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx leftFrontDrive = null;
    private DcMotorEx leftBackDrive = null;
    private DcMotorEx rightFrontDrive = null;
    private DcMotorEx rightBackDrive = null;
    private DcMotorEx lifter = null;

    double robotSpeed = 175;

    public void back(double dist) {

        double sleepTime = (dist/175) * 1000;
        leftFrontDrive.setPower(1);
        leftBackDrive.setPower(1);
        rightFrontDrive.setPower(-1);
        rightBackDrive.setPower(-1);
        sleep((long)sleepTime);
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
    }

    public void forward(double dist) {

        double sleepTime = (dist/175) * 1000;
        leftFrontDrive.setPower(-1);
        leftBackDrive.setPower(-1);
        rightFrontDrive.setPower(1);
        rightBackDrive.setPower(1);
        sleep((long)sleepTime);
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
    }

    public void strafeLeft(double dist) {

        double sleepTime = (dist/175) * 1000;
        leftFrontDrive.setPower(1);
        leftBackDrive.setPower(-1);
        rightFrontDrive.setPower(1);
        rightBackDrive.setPower(-1);
        sleep((long)sleepTime);
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
    }

    public void strafeRight(double dist) {

        double sleepTime = (dist/175) * 1000;
        leftFrontDrive.setPower(-1);
        leftBackDrive.setPower(1);
        rightFrontDrive.setPower(-1);
        rightBackDrive.setPower(1);
        sleep((long)sleepTime);
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
    }
    OpenCvWebcam webcam;
    TensorFlowPipeline pipeline;

    @Override
    public void runOpMode()
    {

        leftFrontDrive = hardwareMap.get(DcMotorEx.class, "3");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "2");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "1");
        leftBackDrive = hardwareMap.get(DcMotorEx.class, "0");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

                pipeline = new TensorFlowPipeline(hardwareMap.appContext);
                webcam.setPipeline(pipeline);
            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });

        telemetry.addLine("Wait for camera to Initialize. (10 secs)");
        telemetry.update();

        waitForStart();

        final int SIGNAL_DETECTED = pipeline.getSignalDetected();
        telemetry.addData("Signal Detected", SIGNAL_DETECTED);
        telemetry.addLine(Arrays.toString(pipeline.getRawOutput()));
        telemetry.update();

        while (opModeIsActive())
        {
            if (SIGNAL_DETECTED == 1) {
                strafeLeft(50);
                forward(40);
                    leftFrontDrive.setPower(0);
                    rightBackDrive.setPower(0);
                    rightFrontDrive.setPower(0);
                    leftBackDrive.setPower(0);
                    webcam.stopStreaming();
                    sleep(30000);
            } else if (SIGNAL_DETECTED == 2) {
                forward(40);
                    leftFrontDrive.setPower(0);
                    rightBackDrive.setPower(0);
                    rightFrontDrive.setPower(0);
                    leftBackDrive.setPower(0);
                webcam.stopStreaming();
                    sleep(30000);
            } else if (SIGNAL_DETECTED == 3) {
                strafeRight(50);
                forward(40);
                    leftFrontDrive.setPower(0);
                    rightBackDrive.setPower(0);
                    rightFrontDrive.setPower(0);
                    leftBackDrive.setPower(0);
                webcam.stopStreaming();
                    sleep(30000);
            }
        }
    }

    class TensorFlowPipeline extends OpenCvPipeline
    {
        boolean viewportPaused;
        Mat mRgb = new Mat();

        TensorImage tImage = new TensorImage();
        Interpreter interpreter;

        // Probabilities for each output [signal 1, signal 2, signal 3]
        int[] out = {0, 0, 0};

        public TensorFlowPipeline(Context ctx) {
            super();
            try {
                // Read model.tflite from asset folder
                AssetFileDescriptor fileDescriptor = ctx.getAssets().openFd("model.tflite");
                FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
                FileChannel fileChannel = inputStream.getChannel();
                long startOffset = fileDescriptor.getStartOffset();
                long declaredLength = fileDescriptor.getDeclaredLength();
                ByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
                this.interpreter = new Interpreter(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Rect roi = new Rect(48, 0, 272, 180);

        @Override
        public Mat processFrame(Mat input)
        {
            mRgb = new Mat(input, roi);
            Imgproc.resize(mRgb, mRgb, new Size(224, 224), Imgproc.INTER_LINEAR_EXACT);
            Imgproc.cvtColor(mRgb, mRgb, Imgproc.COLOR_RGBA2RGB);
            mRgb.convertTo(mRgb, CvType.CV_32SC3);

            int[] data = new int[(int)mRgb.total() * mRgb.channels()];
            mRgb.get(0, 0, data);

            int[] shape = {mRgb.cols(), mRgb.rows(), mRgb.channels()};
            tImage.load(data, shape);

            byte[][] bout = new byte[1][3];
            interpreter.run(tImage.getBuffer(), bout);


            for (int i = 0; i < 3; i++) {
                out[i] = (int) bout[0][i] & 0xFF;
            }
            return input;
        }

        @Override
        public void onViewportTapped()
        {
            viewportPaused = !viewportPaused;

            if(viewportPaused)
            {
                webcam.pauseViewport();
            }
            else
            {
                webcam.resumeViewport();
            }
        }

        public int getSignalDetected()
        {
            int sdi = 0;
            if (Math.abs(out[0] - out[1]) < 230) {
                return 3;
            }
            for (int i = 1; i < out.length; i++) {
                sdi = out[i] > out[sdi] ? i : sdi;
            }
            return sdi + 1;
        }

        public int[] getRawOutput() {
            return out;
        }

    }
}

