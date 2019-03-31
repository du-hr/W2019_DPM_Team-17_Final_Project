package ca.mcgill.ecse211.project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;

class ClawMovement {
	private static final EV3MediumRegulatedMotor clawMotor =
			new EV3MediumRegulatedMotor(LocalEV3.get().getPort("B"));
	private static final int angle = 180;

			public ClawMovement() {
				// TODO Auto-generated constructor stub
			}
			public static void holdCan() {
				// TODO Auto-generated method stub
				clawMotor.setSpeed(50);
				clawMotor.rotate(-angle);
				clawMotor.stop(true);
			}
			public static void releaseCan() {
				clawMotor.setSpeed(50);
				clawMotor.rotate(angle);
				clawMotor.stop(true);
			}


}
