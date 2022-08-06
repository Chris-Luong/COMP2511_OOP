package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import unsw.response.models.EntityInfoResponse;
import unsw.satellite.Satellite;
import unsw.satellite.StandardSatellite;
import unsw.satellite.ShrinkingSatellite;
import unsw.satellite.RelaySatellite;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.device.Device;

public class BlackoutController {

    private static final int HANDHELD_DEVICE_RANGE = 50000;
    private static final int LAPTOP_DEVICE_RANGE = 100000;
    private static final int DESKTOP_DEVICE_RANGE = 200000;

    private List<Device> deviceList = new ArrayList<Device>();
    private List<Satellite> satelliteList = new ArrayList<Satellite>();

    /**
     * Adds device to world state
     * @param deviceId
     * @param type
     * @param position
     */
    public void createDevice(String deviceId, String type, Angle position) {
        Device device = null;
        
        switch(type) {
            case "DesktopDevice":
                device = new Device(deviceId, type, DESKTOP_DEVICE_RANGE, position);
                break;
            case "LaptopDevice":
                device = new Device(deviceId, type, LAPTOP_DEVICE_RANGE, position);
                break;
            // case "HandheldDevice":
            //     break;
            default:
                device = new Device(deviceId, type, HANDHELD_DEVICE_RANGE, position);
              // Throw exception? invalid type? or just make handheld the default
          }

        deviceList.add(device);
    }

    /**
     * Removes device from world state
     * @param deviceId
     */
    public void removeDevice(String deviceId) {
        /**
         * An iterator is used to remove the device rather than just looping
         * through the ArrayList because of the reason mentioned on this page:
         * https://stackoverflow.com/questions/16000282/why-do-we-need-to-use-iterator-on-arraylist-in-java
         */
        Iterator<Device> itr = deviceList.iterator();

        while(itr.hasNext()) {
            Device device = itr.next();
            if(device.getDeviceId().equals(deviceId)) {
                itr.remove();
                return;
            }
        }
    }

    /**
     * Adds satellite to world state, similar to how addDevice works
     * @param satelliteId
     * @param type
     * @param height
     * @param position
     */
    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        Satellite sat;

        switch(type) {
            case "StandardSatellite":
                sat = new StandardSatellite(satelliteId, height, position);
                break;
            case "ShrinkingSatellite":
                sat = new ShrinkingSatellite(satelliteId, height, position);
                break;
            // case "RelaySatellite":
            //     break;
            default:
                sat = new RelaySatellite(satelliteId, height, position);
              // Throw exception? invalid type? or just make relay the default
          }

        satelliteList.add(sat);
    }

    /**
     * Removes satellite from world state, similar to how removeDevice works
     * @param satelliteId
     */
    public void removeSatellite(String satelliteId) {
        Iterator<Satellite> itr = satelliteList.iterator();

        while(itr.hasNext()) {
            Satellite sat = itr.next();
            if(sat.getId().equals(satelliteId)) {
                itr.remove();
                return;
            }
        }
    }

    /**
     * Lists Ids that belong to devices
     * @return List<String>
     */
    public List<String> listDeviceIds() {
        List<String> deviceIdList = new ArrayList<String>();

        for (Device device : deviceList) {
            deviceIdList.add(device.getDeviceId());
        }

        return deviceIdList;
    }

    /**
     * Lists Ids that belong to satellites
     * @return List<String>
     */
    public List<String> listSatelliteIds() {
        List<String> satelliteIdList = new ArrayList<String>();

        for(Satellite sat: satelliteList) {
            satelliteIdList.add(sat.getId());
        }

        return satelliteIdList;
    }

    /**
     * Adds a file to a device
     * @param deviceId
     * @param filename
     * @param content
     */
    public void addFileToDevice(String deviceId, String filename, String content) {
        /**
         * Loop through the list of existing devices until the matching one is found,
         * then add the file to the device's list of files.
         */
        for(Device device : deviceList) {
            if(device.getDeviceId().equals(deviceId)) {
                device.setFile(filename, content);
                return;
            }
        }
    }

    /**
     * Gets information on a device/satellite (id, position, height and type of device/satellite)
     * @param id
     * @return EntityInfoResponse
     */
    public EntityInfoResponse getInfo(String id) {
        /**
         * Loop through the list of existing devices/satellites and find the matching one,
         * then return it. Done by using helper methods.
         */
        Device device = retrieveDevice(id);

        if(device != null) {
            return new EntityInfoResponse(id, device.getPosition(),
                        device.getHeight(), device.getType(), device.mapFile());            
        }

        Satellite sat = retrieveSatellite(id);

        if(sat != null) {
            return new EntityInfoResponse(id, sat.getPosition(), sat.getHeight(), sat.getType());
        }
        // Entity not found
        return null;
    }

    /**
     * Simulate the world state for 1 minute.
     * This will include moving satellites around and later on
     * transferring files between satellites and devices.
     */
    public void simulate() {
        for(Satellite sat : satelliteList) {
            if(sat instanceof RelaySatellite) {
                ((RelaySatellite) sat).changeVelocity();
            }
            sat.changePosition();
        }
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for(int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    /**
     * Lists every entity in range of the specified entity that can communicate.
     * @param id
     * @return List<String>
     */
    public List<String> communicableEntitiesInRange(String id) {
        /**
         * Need to see if isVisible and if getDistance is within range
         */
        Satellite sat = retrieveSatellite(id);
        
        if(sat != null) {
            return addEntitiesInRange(sat);
        }
        
        Device device = retrieveDevice(id);
        
        if(device != null) {
            return addEntitiesInRange(device);
        }

        return null;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        Device device = retrieveDevice(fromId);

        try {
            if(device != null && device.getFile(fileName) == null) {
                throw new VirtualFileNotFoundException(fileName);
            }
        }
        catch(VirtualFileNotFoundException e) {
            System.out.println(e.getMessage());
        }


        // Satellite sat = retrieveSatellite(fromId);

        // if(sat != null) {

        // }
    }

    /**
     * ********************************************************************************************
     * *                                                                                          *
     * ********************************** HELPER FUNCTIONS ****************************************
     * *                                                                                          *
     * ********************************************************************************************
     */

    public Satellite retrieveSatellite(String id) {
        for(Satellite sat : satelliteList) {
            if(sat.getId().equals(id)) {
                return sat;
            }
        }
        // Satellite not found
        return null;
    }
    
    public Device retrieveDevice(String deviceId) {
        for(Device device : deviceList) {
            if(device.getDeviceId().equals(deviceId)) {
                return device;
            }
        }
        // Device not found   
        return null;
    }

    /**
     * Finds all compatible satellites in range of given device
     * @param device
     * @return List<String>
     */
    public List<String> addEntitiesInRange(Device device) {
        List<String> satInRangeList = new ArrayList<String>();
        
        for(Satellite sat : satelliteList) {
            if(isInRange(device, sat, true)) {
                satInRangeList.add(sat.getId());
            }
        }

        return satInRangeList;
    }

    /**
     * Finds all satellites and compatible devices in range of given satllelite
     * @param sat
     * @return List<String>
     */
    public List<String> addEntitiesInRange(Satellite sat) {
        List<String> entityInRangeList = new ArrayList<String>();
        // Satellites in range
        for(Satellite otherSat : satelliteList) {
            if(otherSat.equals(sat)) { // Finding the given satellite in our loop
                continue;
            }
            if(isInRange(sat, otherSat)) {
                entityInRangeList.add(otherSat.getId());
            }
        }
        // Devices in range
        for(Device device : deviceList) {
            if(isInRange(device, sat, false)) {
                entityInRangeList.add(device.getDeviceId());
            }
        }

        return entityInRangeList;
    }

    /**
     * Checks if satellite is compatiable with and in range of given device
     * or within range of satellite if checkForDevice is false.
     * @param device
     * @param sat
     * @return boolean
     */
    public boolean isInRange(Device device, Satellite sat, boolean checkForDevice) {
        /**
         * Since devices and satellites have different ranges, we need to know if we are
         * checking if a satellite is within range of a device (checkForDecvice == true) or
         * if we are checking if a device is within range of a satellite (checkForDevice == false).
         */
        if(checkForDevice) {
            return sat.checkDevice(device) && MathsHelper.isVisible(
                sat.getHeight(), sat.getPosition(), device.getPosition()) &&
                (MathsHelper.getDistance(sat.getHeight(), sat.getPosition(), device.getPosition()) <= device.getRange());
        }
        
        return sat.checkDevice(device) && MathsHelper.isVisible(
            sat.getHeight(), sat.getPosition(), device.getPosition()) &&
            (MathsHelper.getDistance(sat.getHeight(), sat.getPosition(), device.getPosition()) <= sat.getRange());
    }

    /**
     * Checks if another satllite is in range of given satellite
     * @param sat
     * @param otherSat
     * @return boolean
     */
    public boolean isInRange(Satellite sat, Satellite otherSat) {
        
        return MathsHelper.isVisible(sat.getHeight(), sat.getPosition(),
                otherSat.getHeight(), otherSat.getPosition()) &&
                (MathsHelper.getDistance(sat.getHeight(), sat.getPosition(), otherSat.getHeight(), otherSat.getPosition()) <= sat.getRange());
    }
}
