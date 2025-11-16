package Main;

import java.io.*;
import java.util.*;

public class Database{
    private final Main main = new Main();
    private String dataBasePath;
    public void setDataBasePath(String path) {this.dataBasePath = path;}
    public String getDataBasePath() {return dataBasePath;}
    private File jsonDB;
    private FileWriter dbWriter;
    private Scanner dbReader;
    private final Random random = new Random();

    // Constructor
    public Database(String dbPath) {
        this.dataBasePath = dbPath;
        loadDatabase(dataBasePath);
    }

    // Database loader
    public void loadDatabase(String name) {
        // Check if the database path is null/blank/empty
        if (this.dataBasePath == null || this.dataBasePath.isEmpty() || this.dataBasePath.isBlank() || name==null || name.isBlank()) {
            // Set the db path to "jsonDB" (default name)
            setDataBasePath("jsonDB");
            // Load the db with the path assigned
            loadDatabase(this.dataBasePath);
        } else {
            // Creates a temp File object with the db
            File myJsonDB = new File(this.dataBasePath);
            // Check if it exists
            if (myJsonDB.exists()) {
                // Set the db file object from the temp File
                this.jsonDB = myJsonDB;
                try {
                    // Creates a StringBuilder object to store the lines of the file
                    StringBuilder dbContent = new StringBuilder();
                    // Creates the Scanner object and assign to the variable dbReader
                    this.dbReader = new Scanner(jsonDB);
                    // Start reading the file until EOF (end of file)
                    while (dbReader.hasNextLine()) {
                        // Every line is added to the string builder "dbContent"
                        dbContent.append(dbReader.nextLine()).append("\n");
                    }
                    // Close the stream
                    dbReader.close();
                    // Creates the FileWriter object and assign to the variable dbWriter
                    this.dbWriter = new FileWriter(jsonDB);
                    // Write every line collected by the String builder
                    dbWriter.write(dbContent.toString());
                    // Close the stream
                    dbWriter.close();
                    // Catches an error if happens
                } catch (Exception err) {
                    // Print the error in the log
                    main.log.appendErr("An error occurred in the session: " + err.getMessage() + "\n Cause: " + err.getCause());
                }
            } else {
                createDatabase(this.dataBasePath);
            }
        }
    }

    // Create database method
    public void createDatabase(String name) {
        try {
            // Creates a file with the name selected
            File file = new File(name);
            // Check if the file is created successfully
            if (file.createNewFile()) {
                // Print the message
                main.log.appendInfo("Created new json database file");
            } else {
                // If it already exists
                // It print a warn message
                main.log.appendWarn("That json database already exist");
                // It tries to make another one with the original name + the random number
                createDatabase(name + random.nextInt(0,10000));
            }
            // Catches an error if happens
        } catch (Exception err) {
            // Print the error in the log
            main.log.appendErr("An error occurred the session: " + err.getMessage() + "\n Cause: " + err.getCause());
        }
    }

    // Get element method
    public String getElement(String elementName) {
        // Create a string
        String currentLine = "";
        try {
            // Reinitialize the scanner
            this.dbReader = new Scanner(jsonDB);
            // Loop until end  of file (EOF)
            while (dbReader.hasNext()) {
                // Read line
                currentLine = dbReader.nextLine();
                // If the like has the element
                if (currentLine.contains(elementName)) {
                    // It removes the spaces
                    currentLine = currentLine.replace(" ", "");
                    // Take the element name
                    currentLine = currentLine.substring(1, elementName.length()+1);
                    break;
                } else {
                    currentLine = null;
                }
            }
            // Close the streams
            dbReader.close();

            // Catch an error if happens
        } catch (Exception err) {
            // Print the error in the log
            main.log.appendErr("An error occurred the session: " + err.getMessage() + "\n Cause: " + err.getCause());
        }
        return currentLine;
    }

    // Get parameters method
    public HashMap<String, String> getParameters(String elementName) {
        // Creates a string
        String currentLine;
        // Creates an HashMap obj
        HashMap<String, String> parameters = new HashMap<>();
        try {
            // Re-initialize the scanner
            this.dbReader = new Scanner(jsonDB);
            while (dbReader.hasNext()) {
                // Read line in the file
                currentLine = dbReader.nextLine();
                // If contains the element name
                if (currentLine.contains(elementName)) {
                    // Remove all non-important chars
                    currentLine = currentLine
                            .replace(" ", "")
                            .substring((elementName.length()+3))
                            .replace("[", "")
                            .replace("]", "")
                            .replace("\"", "");

                    // Creates an array by splitting the line
                    String[] pairs = currentLine.split(",");
                    for (String pair : pairs) {
                        // Creates an array by splitting each element of the array
                        String[] keyValue = pair.split(":", 2);
                        if (keyValue.length == 2) {
                            String key = keyValue[0];
                            String value = keyValue[1];
                            // Add the parameters to the hashmap
                            parameters.put(key, value);
                        }
                    }
                    break;
                }
            }
            // Close the streams
            dbReader.close();
            // Catch an error if happens
        } catch (Exception err) {
            // Print the error in the log
            main.log.appendErr("An error occurred the session: " + err.getMessage() + "\n Cause: " + err.getCause());
        }
        return parameters;
    }
    public void addElement(String name) {
        if (getElement(name)!=null) {
            String newName = name + random.nextInt(0,10000);
            addElement(newName);
        }

    }
}
