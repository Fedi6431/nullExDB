package Main;

public class Main {
    public Logging log = new Logging();
    private static final Main main = new Main();

    public static void main(String[] args) {
        Database database = new Database("jsonDB");
        main.log.makeFile();
        System.out.println("DATABASE INFORMATION:\nPATH=" + database.getDataBasePath() + "\nELEMENT: " + database.getElement("element") + "\nPARAMETERS:"+database.getAttributes("element"));
        database.addElement("ww");
        System.out.println(database.getElements());
        database.editElement("ww", "wwww");
    }
}
