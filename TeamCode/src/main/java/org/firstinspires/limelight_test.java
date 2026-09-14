// This algorithm is the algorithm used for testing the limelight 3a. As of 9/12/2026 it is being used to test the connection of the limelight 3a to the control hub.
// this is the docs: https://javadoc.io/doc/org.firstinspires.ftc/Hardware/latest/com/qualcomm/hardware/limelightvision/package-summary.html

package org.firstinspires.ftc.teamcode;

// Imported code for the limelight 3a as well as other robot functions.
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

@TeleOp
public class limelight_test extends LinearOpMode {

    // This is the creation of the lime_light variable
    private Limelight3A lime_light;

    // This is the creation of the runOpMode function. This is needed for the robot to run the function correctly.
    @Override
    public void runOpMode() {

        // Limelight3A.class gives the function what type of object it is geting. "limelight" is the name on the Driver's Hub i think. If it is it has to be the exact same
        // Second line has it start.
        lime_light = hardwareMap.get(Limelight3A.class, "limelight");
        lime_light.start();

        // This function has the algorithm pause until the limelight has finished starting.
        waitForStart();

        // This while look will print a message everytime it repeats.
        // If the limelight 3a is connected, it will print "LIMELIGHT CONNECTED". If the limelight 3a in not connected, it will print "LIMELIGHT NOT CONNECTED".
        while (opModeIsActive()) {
            if (lime_light.isConnected()) {
                telemetry.addLine("LIMELIGHT CONNECTED");

                LLResult result = lime_light.getLatestResult();


                telemetry.addLine("Current Pipeline: " + result.getPipelineIndex());

                List<LLResultTypes.FiducialResult> aprilTags = result.getFiducialResults();
                telemetry.addLine("Number of April Tags Seen = " + aprilTags.toArray().length);


            } else {
                telemetry.addLine("LIMELIGHT NOT CONNECTED");
            }

            // This refreshes the screen on the Driver Hub and shows the new message.
            telemetry.update();
        }
    }
}

