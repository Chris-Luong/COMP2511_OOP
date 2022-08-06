package unsw.satellite;

import unsw.device.Device;
import unsw.utils.Angle;

/**
 * A shrinking satellite
 * @author Christopher Luong
 */
public class ShrinkingSatellite extends Satellite{
    /**
     * Stats specific to ShrinkingSatellite
     */
    private static final int SPEED = 1000;
    private static final int MAX_RANGE = 200000;
    private static final int DOWNLOAD_SPEED = 15;
    private static final int UPLOAD_SPEED = 10;
    private static final int FILE_LIMIT = -1; // There is no file storage limit
    private static final int BYTE_LIMIT = 150;

    /**
     * Constructor for ShrinkingSatellite
     * @param id
     * @param height
     * @param position
     */
    public ShrinkingSatellite(String id, double height, Angle position) {
        super(id, position, SPEED, MAX_RANGE, height, FILE_LIMIT, BYTE_LIMIT, DOWNLOAD_SPEED, UPLOAD_SPEED);
    }

    @Override
    public String getType() {
        return "ShrinkingSatellite";
    }

    /**
     * Shrinking satellites can send files to all devices, hence return true
     * @param device
     * @return boolean
     */
    public boolean checkDevice(Device device) {
        return true;
    }
}
