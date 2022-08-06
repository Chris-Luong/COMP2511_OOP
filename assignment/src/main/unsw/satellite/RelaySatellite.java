package unsw.satellite;

import unsw.device.Device;
import unsw.utils.Angle;

/**
 * A relay satellite
 * @author Christopher Luong
 */
public class RelaySatellite extends Satellite{
    /**
     * Stats specific to RelaySatellite
     */
    private static final int SPEED = 1500;
    private static final int MAX_RANGE = 300000;
    private static final int DOWNLOAD_SPEED = -1;
    private static final int UPLOAD_SPEED = -1;
    private static final int FILE_LIMIT = -1;
    private static final int BYTE_LIMIT = -1;

    private static final Angle MIN = Angle.fromDegrees(140);
    private static final Angle MAX = Angle.fromDegrees(190);
    // The satellite can move in either direction at this 'threshold' angle (155 deg clockwise/anti-clockwise)
    private static final Angle MIDPOINT = Angle.fromDegrees(345);

    /**
     * Constructor for RelaySatellite
     * @param id
     * @param height
     * @param position
     */
    public RelaySatellite(String id, double height, Angle position) {
        super(id, position, SPEED, MAX_RANGE, height, FILE_LIMIT, BYTE_LIMIT, DOWNLOAD_SPEED, UPLOAD_SPEED);
    }

    @Override
    public String getType() {
        return "RelaySatellite";
    }

    /**
     * Relay satellites can send files to all devices, hence return true
     * @param device
     * @return boolean
     */
    public boolean checkDevice(Device device) {
        return true;
    }

    /**
     * Sets the new velocity of the satellite for simulate(),
     * ensuring it stays within the degree range [140, 190]
     */
    public void changeVelocity() {
        // Satellite is in region above 190 and lower than 345 degrees
        if (getPosition().compareTo(MAX) == 1 &&  getPosition().compareTo(MIDPOINT) == -1) {
            if (getSpeed() > 0) {
                // Change direction to negative if satellite is moving in positive direction
                setSpeed(-getSpeed());
            }
        }/**
        * Satellite is in region lower than 140 and above 345.
        * If it is equal to 345, satellite moves in positive direction
        */ 
        else if (getPosition().compareTo(MIN) == -1 || getPosition().compareTo(MIDPOINT) == 0
                || getPosition().compareTo(MIDPOINT) == 1) {
            if (getSpeed() < 0) {
                setSpeed(-getSpeed());
        }
    } 
}
}
