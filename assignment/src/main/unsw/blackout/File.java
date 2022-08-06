package unsw.blackout;

/**
 * A file
 * @author Christopher Luong
 */
public class File {
    private String name;
    private String data;
    private int size;

    /**
     * Constructor for File
     * @param name
     * @param data
     */
    public File(String name, String data) {
        this.name = name;
        this.data = data;
        this.size = data.length();
    }

    /**
     * Getter for name of file
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for contents of file
     * @return String
     */
    public String getData() {
        return this.data;
    }

    /**
     * Getter for file size
     * @return int
     */
    public int getSize() {
        return this.size;
    }
}
