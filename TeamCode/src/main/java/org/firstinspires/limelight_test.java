// This algorithm is the algorithm used for testing the limelight 3a. As of 9/12/2026 it is being used to test the connection of the limelight 3a to the control hub.
// this is the docs: https://javadoc.io/doc/org.firstinspires.ftc/Hardware/latest/com/qualcomm/hardware/limelightvision/package-summary.html
// These are the FTC SDK docs: https://javadoc.io/doc/org.firstinspires.ftc

package org.firstinspires.ftc.teamcode;

// Imported code for the limelight 3a as well as other robot functions.
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

@TeleOp
public class limelight_test extends LinearOpMode {

    // This is the creation of the lime_light variable
    private Limelight3A lime_light;

    // This is the creation of the runOpMode function. This is needed for the robot to run the function correctly.
    @Override
    public void runOpMode() {

        CRServo xz_plane_servo  = hardwareMap.get(CRServo.class, "x_servo");
        Servo yz_plane_servo = hardwareMap.get(Servo.class, "top_servo");
        
        // Limelight3A.class gives the function what type of object it is geting. "limelight" is the name on the Driver's Hub i think. If it is it has to be the exact same
        // Second line has it start.
        lime_light = hardwareMap.get(Limelight3A.class, "limelight");
        lime_light.start();

        // This function has the algorithm pause until the limelight has finished starting.
        waitForStart();

        double angleY=0, powerY=0;
        double servoTarget = yz_plane_servo.getPosition();
        // This while look will print a message everytime it repeats.
        // If the limelight 3a is connected, it will print "LIMELIGHT CONNECTED". If the limelight 3a in not connected, it will print "LIMELIGHT NOT CONNECTED".
        while (opModeIsActive()) {

            if (gamepad1.dpadUpWasPressed())
            {
                angleY += 0.1;
                angleY = Math.min(1, angleY);
            }
            if (gamepad1.dpadDownWasPressed())
            {
                angleY -= 0.1;
                angleY = Math.max(0, angleY);
            }


            if (lime_light.isConnected()) {

                // Adds the message showing that the limelight3a is connected
                telemetry.addLine("LIMELIGHT CONNECTED");

                // Creates the object/variable result
                LLResult result = lime_light.getLatestResult();

                // Prints the current pipeline
                telemetry.addLine("Current Pipeline: " + result.getPipelineIndex());

                // Adds a list get contains the apriltags that the limelight3a sees.
                // Adds the message showing number of apriltags seen by limelight3a. The number of them is gained by finding the length of the list
//                List<LLResultTypes.FiducialResult> aprilTags = result.getFiducialResults();
//                telemetry.addLine("Number of April Tags Seen = " + aprilTags.toArray().length);
                double angleX=0, powerX=0;
                if (result.isValid()) {
//                    LLResultTypes.FiducialResult targetTag = aprilTags.get(0);

                    // targetTag.getTargetXDegrees(); -> gets x angle to april tag
                    // targetTag.getTargetYDegrees(); -> gets y angle to april tag

                    // xz_plane_servo.setPosition(); sets rotation of the servo from [0,1]

                    angleX = result.getTx();
                    angleY = result.getTy();
                    //double servoTarget = yz_plane_servo.getPosition()
                    servoTarget += angleY / 720;

                    servoTarget = Math.min(Math.max(servoTarget, 0), 0.75);

                    powerX = -angleX * 0.01;
                    //angleY = angleY + yz_plane_servo.getPosition();

                    if (powerX > .1) {
                        powerX = .1;
                    } else if (powerX < -.1) {
                        powerX = -.1;
                    }

                    yz_plane_servo.setPosition(servoTarget);
                    xz_plane_servo.setPower(powerX);


                }
                else
                {
                    yz_plane_servo.setPosition(0.33);
                    xz_plane_servo.setPower(0);
                }
                // yx_plane_servo.setposition(current_position + angleY)
                telemetry.addLine("Current Y Servo: " + yz_plane_servo.getPosition());
                telemetry.addLine("Current X Angle: " + angleX);
                telemetry.addLine("Current X Power: " + powerX);
                telemetry.addLine("Current Y Angle: " + angleY);
                telemetry.addLine("Current Y Power: " + powerY);
                // CODE TO TEST:
                //telemetry.addLine(lime_light.getTargetXDegrees());
                //telemetry.addLine(lime_light.getTargetYDegrees());
                
                //xz_plane_servo.setPosition(lime_light.getTargetXDegrees());
                //yz_plane_servo.setPosition(lime_light.getTargetYDegrees());
                

            } else {
                telemetry.addLine("LIMELIGHT NOT CONNECTED");
            }

            // This refreshes the screen on the Driver Hub and shows the new message.
            telemetry.update();
        }
    }
}

