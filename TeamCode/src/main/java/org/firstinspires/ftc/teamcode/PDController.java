package org.firstinspires.ftc.teamcode;

import com.pedropathing.util.Timer;

public class PDController {

    double p, d, c;
    double prev;
    Timer timer;


    public PDController(double p, double d)
    {
        this.p = p;
        this.d = d;
        this.c = 0;
        this.prev = 0;
        this.timer = new Timer();
    }

    // if we wanted a constant
    public PDController(double p, double d, double c)
    {
        this.p = p;
        this.d = d;
        this.c = c;
        this.prev = 0;
        this.timer = new Timer();
    }

    public void SetP(double p)
    {
        this.p = p;
    }

    public void SetD(double d)
    {
        this.d = d;
    }

    public double GetP()
    {
        return p;
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
        }

        prev = error;
        timer.resetTimer();

        return (error + slope * d) * p + c;
    }
}
