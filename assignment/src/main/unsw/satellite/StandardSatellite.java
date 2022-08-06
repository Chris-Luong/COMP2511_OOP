package unsw.satellite;

import java.util.Arrays;
import java.util.List;

import unsw.device.Device;
import unsw.utils.Angle;

/**
 * A standard satellite
 * @author Christopher Luong
 */
public class StandardSatellite extends Satellite {

    /**
     * Stats specific to StandardSatellite
     */
    private static final int SPEED = 2500;
    private static final int MAX_RANGE = 150000;
    private static final int DOWNLOAD_SPEED = 1;
    private static final int UPLOAD_SPEED = 1;
    private static final int FILE_LIMIT = 3;
    private static final int BYTE_LIMIT = 80;
    private List<String> possibleDevices = Arrays.asList("LaptopDevice", "HandheldDevice");
    
    /**
     * Constructor for StandardSatellite
     * @param id
     * @param height
     * @param position
     */
    public StandardSatellite(String id, double height, Angle position) {
        super(id, position, SPEED, MAX_RANGE, height, FILE_LIMIT, BYTE_LIMIT, DOWNLOAD_SPEED, UPLOAD_SPEED);
    }

    @Override
    public String getType() {
        return "StandardSatellite";
    }

    /**
     * Checks if the standard satellite can send files to this device.
     * True for laptop/handheld device, false for desktop
     * @param device
     * @return boolean
     */
    public boolean checkDevice(Device device) {
        return possibleDevices.contains(device.getType());
    }
}
