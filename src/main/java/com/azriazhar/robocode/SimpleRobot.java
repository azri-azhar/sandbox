package com.azriazhar.robocode;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

/**
 * A simple crude implementation of Robocode robot. Robocode is a programming
 * tank game (http://robocode.sourceforge.net/).
 *
 * @author Mohamad Azri Azhar
 */
public class SimpleRobot extends Robot {

	/**
	 * Move ahead, turn the gun.
	 */
	@Override
	public void run() {
		while (true) {
			ahead(100);
			turnGunRight(360);

			turnRight(90);
			ahead(100);
			turnLeft(90);
		}
	}

	/**
	 * Lock target, fire.
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		turnGunRight(getHeading() - getRadarHeading() + e.getBearing());
		fire(Rules.MIN_BULLET_POWER);
	}

	/**
	 * Turn 90 degrees away from the bullet, and go ahead.
	 */
	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		turnRight(e.getBearing() + 90);
		ahead(100);
	}

	/**
	 * Turn away from the wall, move ahead.
	 */
	@Override
	public void onHitWall(HitWallEvent e) {
		turnRight(-e.getBearing());
		ahead(100);
	}

	/**
	 * Lock target, fire at min, and back-off.
	 */
	@Override
	public void onHitRobot(HitRobotEvent e) {
		turnGunRight(getHeading() - getRadarHeading() + e.getBearing());
		fire(Rules.MAX_BULLET_POWER);
		back(100);
	}
}
