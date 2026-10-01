package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.ImuParameters;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class MechanumWheels {

    double maxDrivePower;

    IMU imu;
    DcMotorEx leftFront, leftBack, rightBack, rightFront;

    boolean isLocal;

    public MechanumWheels(HardwareMap map, double maxDrivePower, IMU.Parameters parameters)
    {
        this.isLocal = true;
        this.maxDrivePower = maxDrivePower;

        imu = map.get(IMU.class, "imu");
        leftFront = map.get(DcMotorEx.class, "leftFront");
        leftBack = map.get(DcMotorEx.class, "leftBack");
        rightBack = map.get(DcMotorEx.class, "rightBack");
        rightFront = map.get(DcMotorEx.class, "rightFront");

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        imu.initialize(parameters);
    }

    /*
     sets the robot speed using x, y, and rot (range from -1 to 1)
     */
    public void SetSpeed(double x, double y, double rot)
    {
        double botHeading = 0;
        if (isLocal)
        {
            botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        }
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rot), 1);
        double frontLeftPower = (rotY + rotX + rot) / denominator;
        double backLeftPower = (rotY - rotX + rot) / denominator;
        double frontRightPower = (rotY - rotX - rot) / denominator;
        double backRightPower = (rotY + rotX - rot) / denominator;

        leftFront.setPower(frontLeftPower * maxDrivePower);
        leftBack.setPower(backLeftPower * maxDrivePower);
        rightFront.setPower(frontRightPower * maxDrivePower);
        rightBack.setPower(backRightPower * maxDrivePower);
    }

    public boolean GetIsLocal()
    {
        return isLocal;
    }

    public void SetIsLocal(boolean isLocal)
    {
        this.isLocal = isLocal;
    }
}
