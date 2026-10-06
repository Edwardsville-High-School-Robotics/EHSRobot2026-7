package org.firstinspires.ftc.teamcode;
import com.pedropathing.util.Timer;
public class PIDController {
    double p, i, d, c;
    double prev;
    double area = 0;
    Timer timer;
    public PIDController(double p, double i, double d)
    {
        this.p = p;
        this.i = i;
        this.d = d;
        this.c = 0;
        this.prev = 0;
        this.timer = new Timer();
    }
    public PIDController(double p, double i, double d, double c)
    {
        this.p = p;
        this.i = i;
        this.d = d;
        this.c = c;
        this.prev = 0;
        this.timer = new Timer();
    }
    public void SetP(double p)
    {
        this.p = p;
    }
    public void SetI(double i)
    {
        this.i = i;
    }
    public void SetD(double d)
    {
        this.d = d;
    }

    public double GetP()
    {
        return p;
    }
    public double GetI()
    {
        return i;
    }
    public double GetD()
    {
        return d;
    }
    public double Calculate(double target, double current)
    {
        double error = target - current;

        double slope = 0;

        if (prev != 0)
        {
            slope = (error - prev) / (timer.getElapsedTime());
            area += 0.5 * (error+prev) * timer.getElapsedTime();
            //area += error * timer.getElapsedTime();
        }

        prev = error;
        timer.resetTimer();

        return p * (error + i * area + slope * d) + c;
    }
}
