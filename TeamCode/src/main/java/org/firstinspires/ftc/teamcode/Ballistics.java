public class Ballistics
{
    static final double STARTING_FIRING_SPEED = 3.0;

    static double GetFiringAngle(double targetX, double targetY, double firingSpeed)
    {
        double discriminant = Math.pow(firingSpeed,4)-19.6 *Math.pow(firingSpeed, 2)*targetY-96.04*Math.pow(targetY);
        double answer = (Math.pow(firingSpeed, 2) - Math.sqrt(discriminant))/(9.8*targetX);
        return Math.atan(answer);
    }

    // firing speed, firing angle
    static FireInfo GetFireInfo(double targetX, double targetY, double minimumRange)
    {
        FireInfo fireInfo = new FireInfo();
        fireInfo.angle = 0;
        fireInfo.speed = 0;

        // code that does stuff
        double CurrentFiringSpeed = STARTING_FIRING_SPEED;
        double angle = GetFiringAngle(targetX, targetY, STARTING_FIRING_SPEED);
        if(minimumRange > angle) {
            while(minimumRange > angle) {
                CurrentFiringSpeed /= 2; 
                angle = GetFiringAngle(targetX, targetY, CurrentFiringSpeed);

            }


        }
        fireInfo.speed = CurrentFiringSpeed;
        fireInfo.angle = angle;

        return fireInfo;
    }

    public static void main(){
        


    }

}