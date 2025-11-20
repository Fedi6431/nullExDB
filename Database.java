package Main;

import java.io.*;
import java.util.*;

public class Database{
    private String dataBasePath;
    public void setDataBasePath(String path) {this.dataBasePath = path;}
    public String getDataBasePath() {return dataBasePath;}
    private File jsonDB;
    private FileWriter dbWriter;
    private Scanner dbReader;
    private final Random random = new Random();


    private void loadScanner()  {
        try {
            this.dbReader = new Scanner(jsonDB);
        }   catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    private void unloadScanner() {
        dbReader.close();
    }

    private void loadWriter() {
        try {
            this.dbWriter = new FileWriter(jsonDB);
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    private void unloadWriter() {
        try {
            dbWriter.close();
        } catch (IOException err) {
            throw new RuntimeException(err);
        }

    }

    // Constructor
    public Database(String dbPath) {
        this.dataBasePath = dbPath;
        loadDatabase(dataBasePath);
    }

    // Database loader
    public void loadDatabase(String name) {
        // Check if the database path is null/blank/empty
        if (this.dataBasePath == null || this.dataBasePath.isEmpty() || name==null || name.isEmpty()) {
            // Set the db path to "jsonDB" (default name)
            setDataBasePath("jsonDB");
            // Load the db with the path assigned
            loadDatabase(this.dataBasePath);
        } else {
            // Creates a temp File object with the db
            File myJsonDB = new File(name);
            // Check if it exists
            if (myJsonDB.exists()) {
                // Set the db file object from the temp File
                this.jsonDB = myJsonDB;
                try {
                    // Creates a StringBuilder object to store the lines of the file
                    StringBuilder dbContent = new StringBuilder();
                    // Load the scanner
                    loadScanner();
                    // Start reading the file until EOF (end of file)
                    while (dbReader.hasNextLine()) {
                        // Every line is added to the string builder "dbContent"
                        dbContent.append(dbReader.nextLine()).append("\n");
                    }
                    // Close the stream
                    unloadScanner();
                    // Creates the FileWriter object and assign to the variable dbWriter
                    loadWriter();
                    // Write every line collected by the String builder
                    dbWriter.write(dbContent.toString());
                    // Close the stream
                    unloadWriter();
                } catch (Exception err) {
                    // Print the error in the log
                    System.out.println("An error occurred in the session: " + err.getMessage() + "\n Cause: " + err.getCause());
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
            if (!file.createNewFile()) {
                // If it already exists
                // It tries to make another one with the original name + the random number
                createDatabase(name + random.nextInt(0,10000));
            }
        } catch (Exception err) {
            // Print the error in the log
            System.out.println("An error occurred the session: " + err.getMessage() + "\n Cause: " + err.getCause());
        }
    }

    // Get elements method
    public ArrayList<String> getElements() {
        // Create a string
        String currentLine;
        ArrayList<String> elements = new ArrayList<>();
        // Re initialize scanner
        loadScanner();
        // Loop until end  of file (EOF)
        while (dbReader.hasNext()) {
            // Read line
            currentLine = dbReader.nextLine();
            if (!(currentLine.equals("{") || currentLine.equals("}"))) {
                currentLine = currentLine.replace(" ", "")
                        .replace("\t","")
                        .replace("\"","");
                String[] tempArr = currentLine.split(":");
                elements.add(tempArr[0]);
            }
        }
        // Close the streams
        unloadScanner();
        return elements;
    }

    // Get element method
    public String getElement(String elementName) {
        // Create a string
        String currentLine = "";
        // Re initialize scanner
        loadScanner();
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
        unloadScanner();
        return currentLine;
    }

    // add element method
    public void addElement(String name) {
        // Check if the element is already present
        if (getElement(name)!=null) { // TO FIX
            System.out.println();

        } else {
            try {
                // Creates a StringBuilder object to store the lines of the file
                StringBuilder dbContent = new StringBuilder();
                // Re-initialize scanner
                loadScanner();
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
                unloadScanner();
                // Re-initialize the writer
                loadWriter();
                // Write the content
                dbWriter.write(dbContent.toString());
                dbWriter.write("\t\"" + name + "\"");
                // Write the end of the file with "}"
                dbWriter.write("\n}");
                // Close the stream
                unloadWriter();
            } catch (Exception err) {
                // Print the error in the log
                System.out.println("An error occurred in the session: " + err.getMessage() + "\n Cause: " + err.getCause());
            }
        }
    }

    // Edit element method
    public void editElement(String oldElement, String newElement) {
        try {
            // Creates a StringBuilder object to store the lines of the file
            StringBuilder dbContent = new StringBuilder();
            // Re-initialize scanner
            loadScanner();
            // Start reading the file until EOF (end of file)
            while (dbReader.hasNextLine()) {
                String line = dbReader.nextLine();
                // Every line is added to the string builder "dbContent"
                dbContent.append(line).append("\n");
            }
            // Close the stream
            unloadScanner();
            // Calculation to calculate element position
            int oldElementIndexPosition = dbContent.indexOf(oldElement);
            int elementLength = getElement(oldElement).length();
            // Re place the old element with the new one
            dbContent.replace(oldElementIndexPosition, (oldElementIndexPosition+elementLength), newElement);
            // Re-initialize writer
            loadWriter();
            dbWriter.write(dbContent.toString());
            // Unload the writer
            unloadWriter();
        } catch (Exception err) {
            // Print the error in the log
            System.out.println("An error occurred in the session: " + err.getMessage() + "\n Cause: " + err.getCause());
        }
    }

    // Get parameters method
    public HashMap<String, String> getAttributes(String elementName) {
        // Creates a string
        String currentLine;
        // Creates an HashMap obj
        HashMap<String, String> parameters = new HashMap<>();
        // Re initialize scanner
        loadScanner();
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
        unloadScanner();
        return parameters;
    }

    // Get value method
    public String getAttribute(String elementName, String key) {
        return null;
    }

    // Add attribute method
    public void addAttribute() {}

    // Edit attribute method
    public void editAttribute() {}

}
