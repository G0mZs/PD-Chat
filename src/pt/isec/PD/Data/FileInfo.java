package pt.isec.PD.Data;

import java.io.File;
import java.io.Serializable;

public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String path;
    private String name;
    private int size;
    private boolean isDirectory;
    private String owner;

    /**
     * <p>Constructor for FileInfo.</p>
     *
     * @param file a {@link java.io.File} object.
     */
    public FileInfo(File file) {
        path = file.getAbsolutePath();
        name = file.getName();
        size = (int) file.length();
        isDirectory = file.isDirectory();
    }

    /**
     * <p>Getter for the field <code>owner</code>.</p>
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * <p>Setter for the field <code>owner</code>.</p>
     *
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * <p>isDirectory.</p>
     *
     * @return the isDirectory
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * <p>setDirectory.</p>
     *
     * @param isDirectory the isDirectory to set
     */
    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    /**
     * <p>Getter for the field <code>size</code>.</p>
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * <p>Setter for the field <code>size</code>.</p>
     *
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>path</code>.</p>
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * <p>Setter for the field <code>path</code>.</p>
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /** {@inheritDoc} */
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