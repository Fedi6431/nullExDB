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
    @SuppressWarnings("All")
    public void loadDatabase(String name) {
        // Check if the database path is null/blank/empty
        if (this.dataBasePath == null || this.dataBasePath.isEmpty() || name==null || name.isEmpty()) {
            setDataBasePath("jsonDB"); // Set the db path to "jsonDB" (default name)
            loadDatabase(this.dataBasePath); // Load the db with the path assigned
            return;
        }
        // Creates a temp File object with the db
        File myJsonDB = new File(name);
        if (myJsonDB.exists()) {
            // Set the db file object from the temp File
            this.jsonDB = myJsonDB;
            // Creates a StringBuilder object to store the lines of the file
            StringBuilder dbContent = new StringBuilder();
            loadScanner(); // Load the scanner
            // Start reading the file until EOF (end of file)
            while (dbReader.hasNextLine()) {
                // Every line is added to the string builder "dbContent"
                dbContent.append(dbReader.nextLine()).append("\n");
            }
            unloadScanner(); // Close the stream
            loadWriter(); // Load the writer
            // Write every line collected by the String builder
            try {
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
        ArrayList<String> elements = new ArrayList<>();
        loadScanner(); // Re initialize scanner
        // Loop until end  of file (EOF)
        while (dbReader.hasNext()) {
            // Read line
            String currentLine;
            currentLine = dbReader.nextLine();
            if (!(currentLine.equals("{") || currentLine.equals("}"))) {
                currentLine = currentLine.replaceAll("[ \\t\"]", "");
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
        ArrayList<String> elements = getElements();
        int indexOfElement = elements.indexOf(elementName);
        if (!(indexOfElement<0)) {
            return elements.get(indexOfElement);
        } else {
            return null;
        }
    }

    // add element method
    public void addElement(String name) {
        // Check if the element is already present
        if (getElement(name)==null) {

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
            try {
                // Write the content
                dbWriter.write(dbContent.toString());
                dbWriter.write("\t\"" + name + "\"");
                // Write the end of the file with "}"
                dbWriter.write("\n}");
            } catch (Exception err) {
                // Print the error in the log
                System.out.println("An error occurred in the session: " + err.getMessage() + "\n Cause: " + err.getCause());
            }
            // Close the stream
            unloadWriter();
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

    // Remove element method
    public void removeElement(String name) {
        StringBuilder dbContent = new StringBuilder();
        loadScanner(); // Re initialize scanner
        // Loop until end  of file (EOF)
        while (dbReader.hasNext()) {
            // Read line
            String currentLine = dbReader.nextLine();

            if (currentLine.contains(name)) {
                String actualName = currentLine
                        .replace("\t","")
                        .replace("\"","")
                        .substring(0,name.length());

                String tempLine = currentLine
                        .replace("\t","")
                        .replace("\"","");
                if (tempLine.equals(actualName)) {
                    continue;
                }
            }
            dbContent.append(currentLine).append("\n");
        }
        // Close the streams
        unloadScanner();
        // Re-initialize the writer
        loadWriter();
        try {
            // Write the content
            dbWriter.write(dbContent.toString());
        } catch (Exception err) {
            // Print the error in the log
            System.out.println("An error occurred in the session: " + err.getMessage() + "\n Cause: " + err.getCause());
        } finally {
            unloadWriter();
        }
    }

    // Get parameters method
    public HashMap<String, String> getAttributes(String elementName) {
        // Creates an HashMap obj
        HashMap<String, String> parameters = new HashMap<>();
        // Re initialize scanner
        loadScanner();
        while (dbReader.hasNext()) {
            // Read line in the file
            String currentLine;
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
        HashMap<String, String> attributes = getAttributes(elementName);
        return attributes.get(key);
    }

    // Add attribute method
    public void addAttribute(String elementName, String key, String parameter) {
        StringBuilder dbContent = new StringBuilder();
        boolean isInElement = getAttribute(elementName,key).equals("1");
        loadScanner(); // Re initialize scanner
        // Loop until end  of file (EOF)
        while (dbReader.hasNext()) {
            // Read line
            String currentLine = dbReader.nextLine();
            if (!(isInElement)) {
                if (currentLine.contains(elementName)) {
                    String tempLine = currentLine
                            .replace("\t","")
                            .replace("\"","")
                            .replace(" ","")
                            .substring(0,elementName.length());
                    if (tempLine.equals(elementName)) {
                        currentLine = currentLine + ":[\"" + key + "\":\"" + parameter + "\"]";
                    }
                }
            }

            dbContent.append(currentLine).append("\n");
        }
        // Close the streams
        unloadScanner();
        // Load writer
        loadWriter();
        try {
            // Write the content
            dbWriter.write(dbContent.toString());
        } catch (Exception err) {
            // Print the error in the log
            System.out.println("An error occurred in the session: " + err.getMessage() + "\n Cause: " + err.getCause());
        } finally {
            unloadWriter();
        }
    }
    // Edit attribute method
    public void editAttribute() {}

    public void removeAttribute() {}

    // Add attribute method
    public void addParameter(String elementName, String key, String parameter) {

    }
    // Edit attribute method
    public void editParameter() {}

    public void removeParameter() {}

}
