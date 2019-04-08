package FinalProject;

import Odometer.Odometer;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;


public class LightLocalization {
    //Parameters used to know the location (odometry) and navigate
    public static int ROTATION_SPEED = 100;
    private double SENSOR_DIST = 0;
    private Odometer odometer;
    private EV3LargeRegulatedMotor leftMotor, rightMotor;
    public Navigation navigation;
    //Parameters related to the light sensor (input)
    private SensorModes lightSensor = Main.back_sensor;
    private float[] colorData;
    private float prevColor = 0;
    private int numLines = 0;
    private double[] lineAngle = new double[4];
    private static SampleProvider gyro_sp = Main.gyro_Sensor.getMode("Angle");
    private float[] gyroData = new float[gyro_sp.sampleSize()];

    /**
     * This is the constructor for the class 
     * @param odometer   The odometer
     * @param leftMotor  The left motor of the robot
     * @param rightMotor The right motor of the robot
     * @return Not used
     */
    public LightLocalization(Odometer odometer, EV3LargeRegulatedMotor leftMotor,
            EV3LargeRegulatedMotor rightMotor) {
        this.odometer = odometer;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    /**
     * This method is used to bring the cart to the origin 
     * @return Not used
     */
    public void findOrigin() {
        Navigation.turnTo(45);
        leftMotor.setSpeed(ROTATION_SPEED);
        rightMotor.setSpeed(ROTATION_SPEED);
        // use a differential filter to detect lines
        this.colorData = new float[lightSensor.sampleSize()];
        SampleProvider color  = lightSensor.getMode("Red");
        color.fetchSample(colorData, 0);
        float colordiff = prevColor - colorData[0];
        prevColor = colorData[0];
        while (colordiff < 0.07) {//We move forward until we detect a line
            color.fetchSample(colorData, 0);
            colordiff = prevColor - colorData[0];
            //prevColor = colorData[0];
            leftMotor.forward();
            rightMotor.forward();
        }
        //Once a line is detected, we move backward a specific distance
        leftMotor.stop(true);
        rightMotor.stop();
        leftMotor.rotate(convertDistance(Main.WHEEL_RADIUS, -12), true);
        rightMotor.rotate(convertDistance(Main.WHEEL_RADIUS, -12), false);

    }

    /**
     * This method is used to perform the light localization 
     * @return Not used
     */
    public void localize() {
        SampleProvider color  = lightSensor.getMode("Red");
        leftMotor.setSpeed(ROTATION_SPEED);
        rightMotor.setSpeed(ROTATION_SPEED);
        Main.gyro_Sensor.reset();
        float[] colorData = new float[color.sampleSize()]; 
        color.fetchSample(colorData, 0);
        prevColor = colorData[0];
        //Start by getting close to the origin
        findOrigin();
        this.colorData = new float[lightSensor.sampleSize()];
        while (numLines < 4) {//Rotate and detect the 4 lines the sensor comes across
            leftMotor.forward();
            rightMotor.backward();
            color.fetchSample(colorData, 0);
            float colordiff = prevColor - colorData[0];
            if (colordiff >= 0.07) {
                lineAngle[numLines] =gyroFetch();//Store the angle for each line
                numLines++;
                Sound.beep();
            }
        }
        leftMotor.stop(true);
        rightMotor.stop();
        double dX, dY, thetax, thetay;//Variables used to calculate the 0� direction and the origin
        //From the 4 angles stored, calculate how off from the origin and 0� the robot is
        thetay = lineAngle[3] - lineAngle[1];
        thetax = lineAngle[2] - lineAngle[0];
        dX = -1 * SENSOR_DIST * Math.cos(Math.toRadians(thetay / 2));
        dY = -1 * SENSOR_DIST * Math.cos(Math.toRadians(thetax / 2));
        odometer.setXYT(dX, dY, odometer.getXYT()[2]-10);//Set the accurate current position
        Navigation.travelTo(0.0, 0.0);//Navigate to the origin
        leftMotor.setSpeed(ROTATION_SPEED);
        rightMotor.setSpeed(ROTATION_SPEED);
        navigation.turnTo(0);
        //Rotate to be in the 0� direction
        /*if (odometer.getXYT()[2] <= 358 && odometer.getXYT()[2] >= 2.0) {
            leftMotor.rotate(convertAngle(Main.WHEEL_RADIUS, Main.WHEEL_BASE, -odometer.getXYT()[2]), true);
            rightMotor.rotate(-convertAngle(Main.WHEEL_RADIUS, Main.WHEEL_BASE, -odometer.getXYT()[2]), false);
        }*/
        leftMotor.stop(true);
        rightMotor.stop();
        Main.gyro_Sensor.reset();
    }

    public void startCorner() {
        int corner = Main.corner;
        if(corner == 0) {
            odometer.setXYT(Main.TILE_SIZE, Main.TILE_SIZE, 0.0);
            Main.gyro_Sensor.reset();
            Sound.beep();
        }
        else if(corner == 1) {
            Navigation.turnTo(90);
            odometer.setXYT(14*Main.TILE_SIZE, Main.TILE_SIZE, 0.0);
            Main.gyro_Sensor.reset();
            Sound.beep();
        }
        else if(corner == 2) {
            Navigation.turnTo(180);
            odometer.setXYT(14*Main.TILE_SIZE, 8*Main.TILE_SIZE, 0.0);
            Main.gyro_Sensor.reset();
            Sound.beep();
        }
        else if(corner == 3) {
            Navigation.turnTo(-90);
            odometer.setXYT(Main.TILE_SIZE, 8*Main.TILE_SIZE, 0.0);
            Main.gyro_Sensor.reset();
            Sound.twoBeeps();
        }
    }

    private double gyroFetch() {
        Main.gyro_Sensor.fetchSample(gyroData, 0);
        angleCorrection();
        return odometer.getXYT()[2];
    }

    private void angleCorrection() {
      Main.gyro_Sensor.fetchSample(gyroData, 0);        
      if (gyroData[0] >= 0) {
            odometer.setXYT(odometer.getXYT()[0], odometer.getXYT()[1],gyroData[0]);
        }else {
            odometer.setXYT(odometer.getXYT()[0], odometer.getXYT()[1], 360+gyroData[0]);
        }
    }

    /**
     * This method allows the conversion of a distance to the total rotation of each
     * wheel need to cover that distance.
     * 
     * @param radius
     * @param distance
     * @return
     */
    private static int convertDistance(double radius, double distance) {
        return (int) ((180.0 * distance) / (Math.PI * radius));
    }

    private static int convertAngle(double radius, double width, double angle) {
        return convertDistance(radius, Math.PI * width * angle / 360.0);
    }

}