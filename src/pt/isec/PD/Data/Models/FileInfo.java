package pt.isec.PD.Data.Models;

import java.io.File;
import java.io.Serializable;

public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String path;
    private String name;
    private int size;
    private boolean isDirectory;
    private String owner;



    public FileInfo(File file) {
        path = file.getAbsolutePath();
        name = file.getName();
        size = (int) file.length();
        isDirectory = file.isDirectory();
    }


    public String getOwner() {
        return owner;
    }


    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isDirectory() {
        return isDirectory;
    }


    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }


    public int getSize() {
        return size;
    }


    public void setSize(int size) {
        this.size = size;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        if (name.equals("")) {
            if (isDirectory)
                return path;
            else
                return path + " (" + size + ")";
        } else {
            if (isDirectory)
                return name;
            else
                return name + " (" + size + ")";
        }
    }
}