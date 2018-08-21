package model.io;

public class XMLLoader {
    private static XMLLoader ourInstance = new XMLLoader();

    public static XMLLoader getInstance() {
        return ourInstance;
    }

    private XMLLoader() {
    }
}
