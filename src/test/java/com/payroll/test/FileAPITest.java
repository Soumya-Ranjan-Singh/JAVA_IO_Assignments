package com.payroll.test;

import com.employee.data.JavaFileWatchService;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;

import static org.junit.Assert.assertTrue;

public class FileAPITest {
    private static final String pathDir = "E:\\BridgeLabz";
    //pathDir = System.getProperty("user.home"); --> It only works when used to return the system property
    // denoted by the specified key passed as its argument.
    private static final String folderName = "FilesAPI_Works";

    @Test
    public void givenPathWhenCheckedThenConfirm() throws IOException {
        // Check File Path Exists
        Path homePath = Paths.get(pathDir);
        assertTrue(Files.exists(homePath));

        // Delete File and Check File Not Exist
        Path playPath = Paths.get(pathDir + "/"+folderName);
        if (Files.exists(playPath))
        {
            FileUtils.deleteDirectory(playPath.toFile());
        }
        assertTrue(Files.notExists(playPath));

        // Create Directory
        Files.createDirectory(playPath);
        assertTrue(Files.exists(playPath));

        // Create File
        IntStream.range(1, 10).forEach(count -> {
            Path tempFile = Paths.get(playPath + "/temp"+count);
            Assert.assertTrue(Files.notExists(tempFile));
            try {
                Files.createFile(tempFile);
            }
            catch (IOException ignored) {
            }
            assertTrue(Files.exists(tempFile));
        });

        // List Files, Directories as well as Files with Extension
        //noinspection resource
        Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
        Files.newDirectoryStream(playPath).forEach(System.out::println);
        Files.newDirectoryStream(playPath, path -> path.toFile().isFile() &
                path.toString().startsWith("temp")).forEach(System.out::println);
    }

    @Test
    public void givenADirectoryWhenWatchedListsAllTheActivities() throws IOException {
        Path dir = Paths.get(pathDir+ "/"+folderName);
        //noinspection resource
        Files. list(dir).filter(Files::isRegularFile).forEach(System.out::println);
        JavaFileWatchService watchService = new JavaFileWatchService(dir);
        watchService.processEvents();
    }
}