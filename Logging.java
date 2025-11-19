package Main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class Logging {
    // File path and filename variables
    private String path = "logs/";
    // Set path method
    public void setPath(String dirPath, boolean defaultPath) {
        if (defaultPath) {
            path = "logs/";
        } else {
            if (!dirPath.endsWith("/")) {
                path = dirPath + "/";
            } else {
                path = dirPath;
            }
        }
    }
    // Return path variable method
    public String getPath() {
        return path;
    }

    private String filename = "log-" + getCurrentDate();
    // Set filename method
    public void setFilename(String name, boolean defaultPath) {
        if (defaultPath) {
            filename = "log-" + getCurrentDate();
        } else {
                filename = name;
        }
    }
    // Return filename variable method
    public String getFilename() {
        return filename;
    }

    // Verbose log variable
    private boolean verboseLog = false;
    // Verbose log variable editor method
    public void verbose(boolean bool) {
        verboseLog = bool;
    }
    // Return verbose variable method
    public boolean getVerbose() {
        return verboseLog;
    }

    // Startup pop up variable
    private String startUpPopUp = "";
    // Startup pop up variable editor method
    public void setStartUpPopUp(String string, boolean fancy) {
        if (fancy) {
            int numHashes = string.length() + 4; // +2 for the additional # on both sides
            startUpPopUp = "#".repeat(Math.max(0, numHashes)) +
                    "\n# " + string + " #\n" + // Add the string with # on both sides
                    "#".repeat(Math.max(0, numHashes)); // Convert StringBuilder to String
        } else {
            startUpPopUp = string;
        }
    }
    // Return startup pop up variable method
    public String getStartUpPopUp() {
        return  startUpPopUp;
    }

    // Return current date method
    private String getCurrentDate() {
        SimpleDateFormat ft
                = new SimpleDateFormat("dd-MM-yyyy");
        return ft.format(new Date());
    }

    // Return current time method
    private LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    // Make file method
    public void makeFile() {
        try {
            File dir = new File(path);
            if (dir.mkdir()) {
                if (verboseLog) {
                    appendInfo("Directory created");
                }
            } else {
                if (verboseLog) {
                    appendWarn("Directory already exist");
                }
            }

            File file = new File(path + filename);
            if (file.createNewFile()) {
                if (verboseLog) {
                    appendInfo("File created");
                }
            } else {
                if (verboseLog) {
                    appendWarn("File already exist");
                }
            }
            append(startUpPopUp);
        } catch (IOException e) {
            appendErr(e.getMessage());
        }

    }
    // Append in file method (startup only)
    private void append(String str) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(path + filename, true));
            out.write(str + "\n");
            out.close();
        } catch (IOException e) {
            appendErr(e.getMessage());
        }
    }
    // Append an info in the file method
    public void appendInfo(String str) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(path + filename, true));
            out.write(getCurrentDate() + " " + getCurrentTime() + " | info: "+ str + "\n");
            out.close();
        } catch (IOException e) {
            appendErr(e.getMessage());
        }
    }
    // Append a warning in the file method
    public void appendWarn(String str) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(path + filename, true));
            out.write(getCurrentDate() + " " + getCurrentTime() + " | warn: "+ str + "\n");
            out.close();
        } catch (IOException e) {
            appendErr(e.getMessage());
        }
    }
    // Append an error in the file method
    public void appendErr(String str) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(path + filename, true));
            out.write(getCurrentDate() + " " + getCurrentTime() + " | err: "+ str + "\n");
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
