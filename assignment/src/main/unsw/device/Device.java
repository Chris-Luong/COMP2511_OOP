package unsw.device;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import unsw.blackout.File;

import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

/**
 * A device
 * @author Christopher Luong
 */
public class Device {

    private String deviceId;
    private String type; // Handheld | Laptop | Desktop
    private int range;
    private Angle position;
    private List<File> fileList = new ArrayList<File>();

    /**
     * Constructor for device
     * @param deviceId
     * @param type
     * @param range
     * @param position
     */
    public Device(String deviceId, String type, int range, Angle position) {
        this.deviceId = deviceId;
        this.type = type;
        this.range = range;
        this.position = position;
    }

    /**
     * Getter for id of device
     * @return String
     */
    public String getDeviceId() {
        return this.deviceId;
    }

    /**
     * Getter for position of device
     * @return Angle
     */
    public Angle getPosition() {
        return this.position;
    }

    /**
     * Getter for height of device, which
     * is the radius of Jupiter since it
     * is on the surface
     * @return int
     */
    public double getHeight() {
        return MathsHelper.RADIUS_OF_JUPITER;
    }

    /**
     * Getter for type of device
     * @return String
     */
    public String getType() {
        return this.type;
    }

    /**
     * Getter for range of device
     * @return int
     */
    public int getRange() {
        return this.range;
    }

    /**
     * Getter for file, given name of file
     * @param name
     * @return File
     */
    public File getFile(String name) {
        for(File f1 : fileList) {
            if(f1.getName().equals(name)) {
                return f1;
            }
        }

        return null;
    }

    /**
     * Setter for fileList (add file to fileList)
     * @param name
     * @param data
     */
    public void setFile(String name, String data) {
        File f1 = new File(name, data);
        fileList.add(f1);
    }

    /**
     * Make a hashmap of the files currently in device
     * @return Map
     */
    public Map<String, FileInfoResponse> mapFile() {
        Map<String, FileInfoResponse> map = new HashMap<String, FileInfoResponse>();

        for (File f1 : fileList) {
            // Format to pass the test for files {name : name, data, size, hasTransferCompleted == true}
            map.put(f1.getName(), new FileInfoResponse(f1.getName(), f1.getData(), f1.getSize(), true));
        }
        return map;
    }
}
