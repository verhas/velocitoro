/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.verhas.velocitoro;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Vector;

/**
 *
 * @author verhas
 */
public class FileLister {

    private File directory;
    private FilenameFilter filter;
    private Boolean recurse = false;

    /**
     * Set the directory in which the files are to be listed.
     *
     * @param directory
     * @return
     */
    public FileLister setDirectory(File directory) {
        this.directory = directory;
        return this;
    }

    /**
     * Set the filter for the listing.
     * @param filter
     * @return
     */
    public FileLister setFilter(FilenameFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Set whether the listing should go into sub directories.
     *
     * @param recurse true to get into subdirs, false to get files only from the
     * root directory.
     *
     * @return
     */
    public FileLister setRecurse(Boolean recurse) {
        this.recurse = recurse;
        return this;
    }

    /**
     * Convenience method for {@code setRecurse(true)}
     * @return
     */
    public FileLister setRecurse() {
        this.recurse = true;
        return this;
    }

    /**
     * Convenience method for {@code setRecurse(false)}
     * @return
     */
    public FileLister setFlat() {
        this.recurse = false;
        return this;
    }

    /**
     * List the files from the directory.
     *
     * @return the array of the files.
     */
    public File[] listFiles() {
        Collection<File> files = listFilesCollection(directory, filter, recurse);

        File[] arr = new File[files.size()];
        return files.toArray(arr);
    }

    private Collection<File> listFilesCollection(File directory,
            FilenameFilter filter, boolean recurse) {
        // List of files / directories
        Vector<File> files = new Vector<File>();

        // Get files / directories in the directory
        File[] entries = directory.listFiles();

        // Go over entries
        for (File entry : entries) {
            if (!entry.getPath().startsWith(".") &&
                    entry.getPath().indexOf("/.") == -1 &&
                    entry.getPath().indexOf("\\.") == -1) {
                // If there is no filter or the filter accepts the
                // file / directory, add it to the list
                if (filter == null || filter.accept(directory, entry.getName())) {
                    files.add(entry);
                }

                // If the file is a directory and the recurse flag
                // is set, recurse into the directory
                if (recurse && entry.isDirectory()) {
                    files.addAll(listFilesCollection(entry, filter, recurse));
                }
            }
        }

        // Return collection of files
        return files;
    }
}
