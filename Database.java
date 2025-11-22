import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Scanner;

// CRUD => Create, Read, Update, Delete
@SuppressWarnings("unused")
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

    // Constructors
    public Database() {}
    public Database(File file) {
        setDatabase(file);
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

    /*
    // Load database methods
    public void loadDatabase(String name) throws IOException {
        File file = new File(name);
        if (file.exists()) {
            loadScanner();
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            unloadScanner();
            loadWriter();
            dbWriter.write(content.toString());
            unloadWriter();
            clearContent();
        } else {
            throw new FileNotFoundException();
        }
    }
    public void loadDatabase() throws IOException {
        if (db.exists()) {
            loadScanner();
            while (dbReader.hasNextLine()) {
                content.append(dbReader.nextLine()).append("\n");
            }
            unloadScanner();
            loadWriter();
            dbWriter.write(content.toString());
            unloadWriter();
            clearContent();
        } else {
            throw new FileNotFoundException();
        }
    }*/


}
