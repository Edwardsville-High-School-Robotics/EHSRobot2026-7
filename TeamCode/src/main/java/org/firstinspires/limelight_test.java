// This algorithm is the algorithm used for testing the limelight 3a. As of 9/12/2026 it is being used to test the connection of the limelight 3a to the control hub.

package org.firstinspires.ftc.teamcode;

// Imported code for the limelight 3a as well as other robot functions.
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class limelight_test extends LinearOpMode {

    // This is the creation of the lime_light variable
    private Limelight3A lime_light;

    // This is the creation of the runOpMode function. This is needed for the robot to run the function correctly.
    @Override
    public void runOpMode() {

        //These two lines of code "gets the limelight" and has it start.
        lime_light = hardwareMap.get(Limelight3A.class, "limelight");
        lime_light.start();

        // This function has the algorithm pause until the limelight has finished starting.
        waitForStart();

        // This while look will print a message everytime it repeats.
        // If the limelight 3a is connected, it will print "LIMELIGHT CONNECTED". If the limelight 3a in not connected, it will print "LIMELIGHT NOT CONNECTED".
        while (opModeIsActive()) {
            if (lime_light.isConnected()) {
                telemetry.addLine("LIMELIGHT CONNECTED");
            } else {
                telemetry.addLine("LIMELIGHT NOT CONNECTED");
            }

            // This refreshes the screen on the Driver Hub and shows the new message.
            telemetry.update();
        }
    }
}

