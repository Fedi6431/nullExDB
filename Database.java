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

    public Database(String dbPath) {
        this.dataBasePath = dbPath;
        main.log.appendInfo("Database path setted to " + this.getDataBasePath() + ".");

        loadDatabase(dataBasePath);
    }

    // Database loader
    public void loadDatabase(String name) {
        main.log.appendInfo("Loading database.");

        // Check if the database path is null/blank/empty
        if (this.dataBasePath == null || this.dataBasePath.isEmpty() || name==null || name.isEmpty()) {
            main.log.appendWarn("Database path is null/empty, setting to default.");

            // Set the db path to "jsonDB" (default name)
            setDataBasePath("jsonDB");

            // Load the db with the path assigned
            main.log.appendInfo("Re-loading database.");
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
                    main.log.appendInfo("Initializing scanner.");
                    this.dbReader = new Scanner(jsonDB);
                    main.log.appendInfo("Scanner initialized successfully.");

                    // Start reading the file until EOF (end of file)
                    main.log.appendInfo("Reading database content.");
                    while (dbReader.hasNextLine()) {
                        // Every line is added to the string builder "dbContent"
                        dbContent.append(dbReader.nextLine()).append("\n");
                    }

                    // Close the stream
                    main.log.appendInfo("Closing scanner.");
                    dbReader.close();
                    main.log.appendInfo("Scanner closed successfully.");

                    // Creates the FileWriter object and assign to the variable dbWriter
                    main.log.appendInfo("Initializing FileWriter.");
                    this.dbWriter = new FileWriter(jsonDB);
                    main.log.appendInfo("FileWriter initialized successfully.");

                    // Write every line collected by the String builder
                    main.log.appendInfo("Re-writing database content.");
                    dbWriter.write(dbContent.toString());

                    // Close the stream
                    main.log.appendInfo("Closing FileWriter.");
                    dbWriter.close();
                    main.log.appendInfo("FileWriter closed successfully.");

                } catch (Exception err) {
                    // Print the error in the log
                    main.log.appendErr("An error occurred in the session: " + err.getMessage() + "\n Cause: " + err.getCause());
                }

            } else {
                main.log.appendWarn("Database not found.");
                createDatabase(this.dataBasePath);
            }
        }
        main.log.appendInfo("Loaded database successfully.");
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
                main.log.appendWarn("A database with that name already exist");
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
            // Re initialize scanner
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
            // Re initialize scanner
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
        // Check if the element is already present
        if (getElement(name)!=null) { // TO FIX
            main.log.appendWarn("Element already in database. Skipping method");
        } else {
            try {
                // Creates a StringBuilder object to store the lines of the file
                StringBuilder dbContent = new StringBuilder();
                // Re-initialize scanner
                this.dbReader = new Scanner(jsonDB);
                // Start reading the file until EOF (end of file)
                while (dbReader.hasNextLine()) {
                    String line = dbReader.nextLine();
                    if (line.equals("}")) {
                        break;
                    }
                    // Every line is added to the string builder "dbContent"
                    dbContent.append(line).append("\n");
                }
                // Close the stream
                dbReader.close();
                // Re-initialize the writer
                this.dbWriter = new FileWriter(jsonDB);
                // Write the content
                dbWriter.write(dbContent.toString());
                dbWriter.write("\t\"" + name + "\"");
                // Write the end of the file with "}"
                dbWriter.write("\n}");
                // Close the stream
                dbWriter.close();
                // Catches an error if happens
            } catch (Exception err) {
                // Print the error in the log
                main.log.appendErr("An error occurred in the session: " + err.getMessage() + "\n Cause: " + err.getCause());
            }
        }
    }
}
