package unsw.test;


import unsw.Exceptions.*;
import org.junit.jupiter.api.Test;

import unsw.archaic_fs.ArchaicFileSystem;
import unsw.archaic_fs.FileWriteOptions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

public class ArchaicFsTest {
    @Test
    public void testCdInvalidDirectory() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        // Try to change directory to an invalid one
        assertThrows(UNSWNoSuchFileException.class, () -> {
            fs.cd("/usr/bin/cool-stuff");
        });
    }

    @Test
    public void testCdValidDirectory() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.cd("/usr/bin/cool-stuff");
        });
    }

    @Test
    public void testCdAroundPaths() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.cd("/usr/bin/cool-stuff");
            assertEquals("/usr/bin/cool-stuff", fs.cwd());
            fs.cd("..");
            assertEquals("/usr/bin", fs.cwd());
            fs.cd("../bin/..");
            assertEquals("/usr", fs.cwd());
        });
    }

    @Test
    public void testCreateFile() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            assertEquals("My Content", fs.readFromFile("test.txt"));
            fs.writeToFile("test.txt", "New Content", EnumSet.of(FileWriteOptions.TRUNCATE));
            assertEquals("New Content", fs.readFromFile("test.txt"));
        });
    }

    // Now write some more!
    // Some ideas to spark inspiration;
    // - File Writing/Reading with various options (appending for example)
    // - Cd'ing .. on the root most directory (shouldn't error should just remain on root directory)
    // - many others...
    @Test
    public void testMkdirInvalid() {
        ArchaicFileSystem fs = new ArchaicFileSystem();
        assertThrows(UNSWFileNotFoundException.class, () -> {
            fs.mkdir("/usr/bin/test-file", false, true);
        });
        assertThrows(UNSWFileNotFoundException.class, () -> {
            fs.mkdir("/usr/bin/test-file", false, false);
        });
        assertThrows(UNSWFileAlreadyExistsException.class, () -> {
            fs.mkdir("/usr/bin/test-file", true, true);
            fs.mkdir("/usr/bin/test-file", true, false);
        });
        assertThrows(UNSWFileAlreadyExistsException.class, () -> {
            fs.mkdir("/usr/bin/test-file", true, true);
            fs.mkdir("/usr/bin/test-file", false, false);
        });
        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/test", true, false);
            fs.mkdir("/usr/bin/test", true, true);
        });
    }

    @Test
    public void testWriteInvalid() {
        ArchaicFileSystem fs = new ArchaicFileSystem();
        assertThrows(UNSWFileNotFoundException.class, () -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.TRUNCATE));
        });
        assertThrows(UNSWFileAlreadyExistsException .class, () -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
        });
        assertThrows(UNSWFileAlreadyExistsException .class, () -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.APPEND));
        });
    }
    
    @Test void createfileInvalid() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertThrows(UNSWFileAlreadyExistsException.class, () -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.APPEND));
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
        });
        
    }

    @Test
    public void testReadInvalid() {
        ArchaicFileSystem fs = new ArchaicFileSystem();
        assertThrows(UNSWFileNotFoundException.class, () -> {
            fs.readFromFile("/usr/bin/test-file");
        });
    }

    @Test
    public void testReadValid() {
        ArchaicFileSystem fs = new ArchaicFileSystem();
        assertDoesNotThrow(() -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            fs.readFromFile("test.txt");
        });
    }
}