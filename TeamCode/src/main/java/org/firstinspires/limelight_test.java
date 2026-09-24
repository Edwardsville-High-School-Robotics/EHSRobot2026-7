// This algorithm is the algorithm used for testing the limelight 3a. As of 9/12/2026 it is being used to test the connection of the limelight 3a to the control hub.
// this is the docs: https://javadoc.io/doc/org.firstinspires.ftc/Hardware/latest/com/qualcomm/hardware/limelightvision/package-summary.html
// These are the FTC SDK docs: https://javadoc.io/doc/org.firstinspires.ftc
// As of 9/21/2026 this algorithm attempts moves the limelight of a two axis gimbal, facing the april tag.

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

    // This is the creation of the lime_light variable/object
    private Limelight3A lime_light;

    // This is the creation of the runOpMode function. This is needed for the robot to run the function correctly.
    @Override
    public void runOpMode() {

        // Getting the hardware for the code to use.
        CRServo xz_plane_servo  = hardwareMap.get(CRServo.class, "x_servo");
        Servo yz_plane_servo = hardwareMap.get(Servo.class, "top_servo");
        lime_light = hardwareMap.get(Limelight3A.class, "limelight");

        // Starting the limelight.
        lime_light.start();
        waitForStart();

        LLResult ooglyboogly = lime_light.getLatestResult();

        // Creating the variables needed for the limelight tracking.
        double angleY=ooglyboogly.getTy()+1, powerY=0;
        double servoTarget = yz_plane_servo.getPosition();
        double timeSinceUpdate = lime_light.getTimeSinceLastUpdate();
        double xSensitivity = 0;
        double ySensitivity = 0;
        double angleX=0, powerX=0;
        double sensitivity = 1440;

        // This is the while loop the main bit of the algorithm runs in. It repeats faster than the limelight updates.
        while (opModeIsActive()) {

            LLResult result = lime_light.getLatestResult();
            double schmungus_among_us=result.getTy();

            // Y-axis servo angle controller with game_controller
            if (gamepad1.dpadUpWasPressed())
            {
                sensitivity -= 100;
                if(sensitivity <= 0)
                {
                    sensitivity = 1;
                }
            }
            if (gamepad1.dpadDownWasPressed())
            {
                sensitivity += 100;
            }

            // This is the portion of the code that works with lime_light
            if (lime_light.isConnected()) {

                // Adds the message showing that the limelight3a is connected
                telemetry.addLine("LIMELIGHT CONNECTED");

                // Creates the object/variable result
               // LLResult result = lime_light.getLatestResult();

                // Prints the current pipeline
                telemetry.addLine("Current Pipeline: " + result.getPipelineIndex());

//                List<LLResultTypes.FiducialResult> aprilTags = result.getFiducialResults(); <-- This gets the apriltags
//                telemetry.addLine("Number of April Tags Seen = " + aprilTags.toArray().length); <-- This prints the # of april tags

                // This if function is necessary for the case than the limelight is not seeing an april tag.
                //if (result.isValid() && timeSinceUpdate > lime_light.getTimeSinceLastUpdate()) {
                if (result.isValid() && angleY != schmungus_among_us) {
//                    LLResultTypes.FiducialResult targetTag = aprilTags.get(0);

                        // targetTag.getTargetXDegrees(); -> gets x angle to april tag
                        // targetTag.getTargetYDegrees(); -> gets y angle to april tag

                        // xz_plane_servo.setPosition(); sets rotation of the servo from [0,1]

                        angleX = result.getTx();
                        angleY = result.getTy();
                        //double servoTarget = yz_plane_servo.getPosition()
                        servoTarget += angleY / sensitivity;

                        // Clamping for the Y-axis
                        servoTarget = Math.min(Math.max(servoTarget, 0), 0.75);


                        powerX = -angleX * 0.01;
                        //angleY = angleY + yz_plane_servo.getPosition();

                        // Clamping for the X-axis
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

                telemetry.addLine("TimeSinceLasteUpdate: " + lime_light.getTimeSinceLastUpdate());

                // Angle and power data being printed.
                telemetry.addLine("Current Y Servo: " + yz_plane_servo.getPosition());
                telemetry.addLine("Current X Angle: " + angleX);
                telemetry.addLine("Current X Power: " + powerX);
                telemetry.addLine("Current Y Angle: " + angleY);
                telemetry.addLine("Current Y Power: " + powerY);

            } else {
                // This is for when the limelight is not connected.
                telemetry.addLine("LIMELIGHT NOT CONNECTED");
            }

            // This refreshes the screen on the Driver Hub and shows the new message.
            telemetry.update();
        }
    }
}

