/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/*
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp
public class IntakeTest extends LinearOpMode {
    final double MAX_DRIVE_POWER = 1.0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status ", "Initialized");
        telemetry.update();


        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "artifact_feeder");
        Limelight3A lime_light = hardwareMap.get(Limelight3A.class, "limelight");

        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

        MechanumWheels mechWheels = new MechanumWheels(hardwareMap, MAX_DRIVE_POWER, parameters);

        PDController pdController = new PDController(15.0, 1.0);
        lime_light.start();
        waitForStart();

        boolean isIntaking = false;
        boolean isTracking = false;

        double intakeMotorSpeed = 0.2;

        double timeSinceUpdate = 0;


        while (opModeIsActive()) {
            double prevTimeSinceUpdate = timeSinceUpdate;
            timeSinceUpdate =  lime_light.getTimeSinceLastUpdate();

            double y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = -gamepad1.left_stick_x;
            double rx = -gamepad1.right_stick_x;


            if (gamepad1.aWasPressed()) {
                isIntaking = !isIntaking;
            }

            if (gamepad1.dpadUpWasPressed())
            {
                intakeMotorSpeed += 0.1;
            }
            if (gamepad1.dpadDownWasPressed())
            {
                intakeMotorSpeed -= 0.1;
            }

            if (gamepad1.dpadLeftWasPressed())
            {
                double d = pdController.GetD()-0.1;
                pdController.SetD(d);
            }

            if (gamepad1.dpadRightWasPressed())
            {
                double d = pdController.GetD()+0.1;
                pdController.SetD(d);
            }

            if (gamepad1.leftBumperWasPressed())
            {
                double p = pdController.GetP()-0.1;
                pdController.SetP(p);
            }


            if (gamepad1.rightBumperWasPressed())
            {
                double p = pdController.GetP()+0.1;
                pdController.SetP(p);
            }

            intakeMotorSpeed = Range.clip(intakeMotorSpeed, -1, 1);

            if (isIntaking)
            {
                intakeMotor.setPower(intakeMotorSpeed);
            }
            else
            {
                intakeMotor.setPower(0.0);
            }

            if (gamepad1.leftStickButtonWasPressed()) {
                mechWheels.SetIsLocal(!mechWheels.GetIsLocal()); // toggle is local
            }

            if (gamepad1.bWasPressed())
            {
                isTracking = !isTracking;
            }

            if (!lime_light.isConnected())
            {
                telemetry.addData("Limelight", "LL IS NOT CONNECTED");
            }
            else if (isTracking)
            {
                LLResult result = lime_light.getLatestResult();
                if (result.isValid() && prevTimeSinceUpdate > timeSinceUpdate)
                {
                    double angleX = result.getTx();
                    // target angle 0 (directly forward)
                    // pdController.Calculate gives degrees/second the robot should be turning
                    rx = pdController.Calculate(0, angleX) / 360; // divide by 360 to convert to motor power
                }
            }



            mechWheels.SetSpeed(x, y, rx);
            telemetry.addData("P", "P: " + pdController.GetP());
            telemetry.addData("D", "D: " + pdController.GetD());
            telemetry.addData("rx", "rx: " + rx);
            telemetry.update();
        }
    }
}
