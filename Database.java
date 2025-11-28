import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Scanner;

// CRUD => Create, Read, Update, Delete
/**
 *  <p><strong>File structure</strong></p>
 *  <code>{
 *      element([propriety=value],[propriety=value])
 *  }
 *  </code>
 *
 *  @Author Fedi6431
 */
@SuppressWarnings("All")
class Database {
    // Database file variable
    private File db;
    public File getDatabase() {return db;}
    public void setDatabase(File file) {
        db = file;
    }

    // Database writer variable & methods
    private FileWriter dbWriter;
    private void loadWriter(File database) throws IOException{
        dbWriter = new FileWriter(database);
    }
    private void unloadWriter() throws IOException {
        dbWriter.close();
    }

    // Database reader variable & methods
    private Scanner dbReader;
    private void loadScanner(File database) throws FileNotFoundException{
        dbReader = new Scanner(database);
    }
    private void unloadScanner() throws FileNotFoundException {
        dbReader.close();
    }

    // String builder
    private final StringBuilder content = new StringBuilder();
    private void clearContent() {
        if (!content.isEmpty()) {
            content.delete(0, content.length());
        } else {
            return;
        }

    }

    // First Line check
    private boolean containsDBFirstLine(File file) throws IOException {
        StringBuilder content = readDatabase(file);
        if (content.toString().contains("{")) {
            return true;
        } else {
            return false;
        }
    }

    // Constructors
    public Database() {}
    public Database(String file) {
        File file1 = new File(file);
        setDatabase(file1);
    }
    public Database(File file) {
        setDatabase(file);
    }

    // Load database methods
    public void loadDatabase() throws IOException {
        if (db.exists()) {
            loadScanner(db);
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            unloadScanner();
            loadWriter(db);
            dbWriter.write(content.toString());
            unloadWriter();
            clearContent();
        } else {
            throw new FileNotFoundException();
        }
    }
    public void loadDatabase(String name) throws IOException {
        File file = new File(name);
        if (file.exists()) {
            loadScanner(file);
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            unloadScanner();
            loadWriter(file);
            dbWriter.write(content.toString());
            unloadWriter();
            clearContent();
        } else {
            throw new FileNotFoundException();
        }
    }
    public void loadDatabase(File file) throws IOException {
        if (file.exists()) {
            loadScanner(file);
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            unloadScanner();
            loadWriter(file);
            dbWriter.write(content.toString());
            unloadWriter();
            clearContent();
        } else {
            throw new FileNotFoundException();
        }
    }

    // Create database method
    public void createDatabase() throws IOException {
        if (db.createNewFile()) {
            setDatabase(db);
        } else {
            throw new FileAlreadyExistsException(db.getName());
        }
    }
    public void createDatabase(String name) throws IOException {
        File file = new File(name);
        if (file.createNewFile()) {
            setDatabase(file);
        } else {
            throw new FileAlreadyExistsException(file.getName());
        }
    }
    public void createDatabase(File file) throws IOException {
        if (file.createNewFile()) {
            setDatabase(file);
        } else {
            throw new FileAlreadyExistsException(file.getName());
        }
    }

    // Read database method
    public StringBuilder readDatabase() throws IOException {
        StringBuilder tempStrBuildr;
        if (db.exists()) {
            loadScanner(db);
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            tempStrBuildr = new StringBuilder(content.toString());
            clearContent();
        } else {
            throw new FileNotFoundException();
        }
        return tempStrBuildr;
    }
    public StringBuilder readDatabase(String name) throws IOException {
        File file = new File(name);
        StringBuilder tempStrBuildr;
        if (file.exists()) {
            loadScanner(db);
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            tempStrBuildr = new StringBuilder(content.toString());
            clearContent();
        } else {
            throw new FileNotFoundException();
        }
        return tempStrBuildr;
    }
    public StringBuilder readDatabase(File file) throws IOException {
        StringBuilder tempStrBuildr;
        if (file.exists()) {
            loadScanner(db);
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            tempStrBuildr = new StringBuilder(content.toString());
            clearContent();
        } else {
            throw new FileNotFoundException();
        }
        return tempStrBuildr;
    }

    // Update database method
    public void updateDatabase(File oldDatabase, File newDatabase) throws IOException{
        loadScanner(oldDatabase);
        if (oldDatabase.exists() && newDatabase.exists()) {
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            unloadScanner();
            loadWriter(newDatabase);
            dbWriter.write(content.toString());
            unloadWriter();
            clearContent();
        } else {
            throw new FileNotFoundException();
        }

    }
    public void updateDatabase(String oldDatabase, String newDatabase) throws IOException {
        File oldDbFile = new File(oldDatabase);
        File newDbFile = new File(newDatabase);
        loadScanner(oldDbFile);
        if (oldDbFile.exists() && newDbFile.exists()) {
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            unloadScanner();
            loadWriter(newDbFile);
            dbWriter.write(content.toString());
            unloadWriter();
            clearContent();
        } else {
            throw new FileNotFoundException();
        }
    }

    // Delete database method
    public boolean deleteDatabase() {
        return db.delete();
    }
    public boolean deleteDatabase(String name) {
        File file = new File(name);
        return file.delete();
    }
    public boolean deleteDatabase(File name) {
        return name.delete();
    }

    // Create element method
    public void addElement(String element) throws IOException {
        if (!containsDBFirstLine(db)) {
            clearContent();
            loadWriter(db);
            dbWriter.append("{");
            unloadWriter();
        }
        StringBuilder content = readDatabase();
        if (content.toString().contains("}")) {
            int eofIndex = content.indexOf("}");
            content.delete(eofIndex-1,eofIndex+1);
        }
        content.append("\t\"").append(element).append("\"").append("\n}");
        loadWriter(db);
        dbWriter.write(content.toString());
        unloadWriter();
    }
    public void addElement(String file, String element) throws IOException {
        File file1 = new File(file);
        if (!containsDBFirstLine(file1)) {
            clearContent();
            loadWriter(file1);
            dbWriter.append("{");
            unloadWriter();
        }
        StringBuilder content = readDatabase();
        if (content.toString().contains("}")) {
            int eofIndex = content.indexOf("}");
            content.delete(eofIndex-1,eofIndex+1);
        }
        content.append("\t\"").append(element).append("\"").append("\n}");
        loadWriter(file1);
        dbWriter.write(content.toString());
        unloadWriter();
    }
    public void addElement(File file, String element) throws IOException {
        if (!containsDBFirstLine(file)) {
            clearContent();
            loadWriter(file);
            dbWriter.append("{");
            unloadWriter();
        }
        StringBuilder content = readDatabase();
        if (content.toString().contains("}")) {
            int eofIndex = content.indexOf("}");
            content.delete(eofIndex-1,eofIndex+1);
        }
        content.append("\t\"").append(element).append("\"").append("\n}");
        loadWriter(file);
        dbWriter.write(content.toString());
        unloadWriter();
    }

    // Read element method
    public String getElement(String element) throws IOException {
        StringBuilder content = readDatabase(db);
        int elementIndex;
        if (content.indexOf(element) == content.lastIndexOf(element)) {
            return element;
        } else { return null; }
    }
    public String getElement(String file,String element) throws IOException {
        File file1 = new File(file);
        StringBuilder content = readDatabase(file1);
        int elementIndex;
        if (content.indexOf(element) == content.lastIndexOf(element)) {
            return element;
        } else { return null; }
    }
    public String getElement(File file, String element) throws IOException {
        StringBuilder content = readDatabase(file);
        int elementIndex;
        if (content.indexOf(element) == content.lastIndexOf(element)) {
            return element;
        } else { return null; }
    }

    // Update (Edit) element method
    public boolean editElement(String oldElement, String newElement) throws IOException {
        StringBuilder content = readDatabase(db);
        int elementIndex;
        if (content.indexOf(oldElement) == content.lastIndexOf(oldElement)) {
            elementIndex = content.indexOf(oldElement);
            content.delete(elementIndex,elementIndex+oldElement.length());
            content.insert(elementIndex, newElement);
            loadWriter(db);
            dbWriter.write(content.toString());
            unloadWriter();
            return true;
        } else {
            return false;
        }
    }
    public boolean editElement(String file, String oldElement, String newElement) throws IOException {
        File file1 = new File(file);
        StringBuilder content = readDatabase(file1);
        int elementIndex;
        if (content.indexOf(oldElement) == content.lastIndexOf(oldElement)) {
            elementIndex = content.indexOf(oldElement);
            content.delete(elementIndex,elementIndex+oldElement.length());
            content.insert(elementIndex, newElement);
        } else { return false;}
        loadWriter(file1);
        dbWriter.write(content.toString());
        unloadWriter();
        return true;
    }
    public boolean editElement(File file, String oldElement, String newElement) throws IOException {
        StringBuilder content = readDatabase(file);
        int elementIndex;
        if (content.indexOf(oldElement) == content.lastIndexOf(oldElement)) {
            elementIndex = content.indexOf(oldElement);
            content.delete(elementIndex,elementIndex+oldElement.length());
            content.insert(elementIndex, newElement);
            loadWriter(file);
            dbWriter.write(content.toString());
            unloadWriter();
            return true;
        } else { return false;}
    }

    // Delete element method
    public boolean deleteElement(String element) throws IOException {
        StringBuilder content = readDatabase(db);
        int elementIndex;
        if (content.indexOf(element) == content.lastIndexOf(element)) {
            elementIndex = content.indexOf(element);
            content.delete(elementIndex-3,elementIndex+element.length()+1);
            loadWriter(db);
            dbWriter.write(content.toString());
            unloadWriter();
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteElement(String file,String element) throws IOException {
        File file1 = new File(file);
        StringBuilder content = readDatabase(file1);
        int elementIndex;
        if (content.indexOf(element) == content.lastIndexOf(element)) {
            elementIndex = content.indexOf(element);
            content.delete(elementIndex-3,elementIndex+element.length()+1);
            loadWriter(file1);
            dbWriter.write(content.toString());
            unloadWriter();
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteElement(File file,String element) throws IOException {
        StringBuilder content = readDatabase(file);
        int elementIndex;
        if (content.indexOf(element) == content.lastIndexOf(element)) {
            elementIndex = content.indexOf(element);
            content.delete(elementIndex-3,elementIndex+element.length()+1);
            loadWriter(file);
            dbWriter.write(content.toString());
            unloadWriter();
            return true;
        } else {
            return false;
        }
    }

    // Create propriety method
    // Read propriety method
    // Update (Edit) propriety method
    // Delete propriety method
}
