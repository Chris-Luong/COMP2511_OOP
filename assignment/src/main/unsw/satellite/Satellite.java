package unsw.satellite;

// import java.util.ArrayList;
// import java.util.List;

// import unsw.blackout.File;
import unsw.device.Device;
import unsw.utils.Angle;

/**
 * A satellite blueprint
 * @author Christopher Luong
 */
public abstract class Satellite {
    private String id;
    private Angle position;
    private int speed;
    private int range;
    private double height;
    private int fileLimit;
    private int byteLimit;
    private int downloadSpeed;
    private int uploadSpeed;
    // private List<File> fileList = new ArrayList<File>();

    /**
     * Constructor for Satellite
     * @param id
     * @param position
     * @param speed
     * @param range
     * @param height
     * @param fileLimit
     * @param byteLimit
     * @param downloadSpeed
     * @param uploadSpeed
     */
    public Satellite(String id, Angle position, int speed, int range, double height,
                    int fileLimit, int byteLimit, int downloadSpeed,
                    int uploadSpeed) {
        this.id = id;
        this.position = position;
        this.speed = speed;
        this.range = range;
        this.height = height;
        this.fileLimit = fileLimit;
        this.byteLimit = byteLimit;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
    }

    /**
     * Getter for id of satellite
     * @return String
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for position of satellite
     * @return Angle
     */
    public Angle getPosition() {
        return this.position;
    }

    /**
     * Getter for speed of satellite
     * @return int
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * Getter for height of satellite
     * @return double
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Getter for range of satellite
     * @return int
     */
    public int getRange() {
        return this.range;
    }

    /**
     * Getter for type of satellite, implemented by subclasses
     * @return String
     */
    public abstract String getType();

    /**
     * Setter for position of satellite
     * @param position
     */
    public void setPosition(Angle position) {
        this.position = position;
    }

    /**
     * Setter for speed of satellite
     * @param speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Sets the new position of the satellite for simulate()
     */
    public void changePosition() {
        // v = w * r
        double angularVelocity = this.getSpeed() / this.getHeight();
        double displacement = angularVelocity + position.toRadians();
        /**
         * Ensure the 0 <= displacement < 360 i.e. don't count the degrees
         * from extra revolutions. Then convert displacement from a double
         * to an Angle and set this is the new position.
         */
        displacement %= 360;
        setPosition(Angle.fromRadians(displacement));
    }

    /**
     * Checks if the this satellite can send files to given device,
     * implemented by subclasses.
     * @param device
     * @return boolean
     */
    public abstract boolean checkDevice(Device device);
}
