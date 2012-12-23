/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.verhas.velocitoro;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author verhas
 */
class FilenameFilterExt implements FilenameFilter {

    private List<String> extensions = new ArrayList<String>();

    public FilenameFilterExt cleanExtensionList() {
        extensions = new ArrayList<String>();
        return this;
    }

    public FilenameFilterExt addExtension(String extension) {
        extensions.add(extension);
        return this;
    }

    private Boolean modeIsInclude = null;
    public FilenameFilterExt setModeInclude(){
        modeIsInclude = true;
        return this;
    }
    public FilenameFilterExt setModeExclude(){
        modeIsInclude = false;
        return this;
    }

    @Override
    public boolean accept(File dir, String name) {
        assert modeIsInclude != null;
        for (String extension : extensions) {
            if (name.endsWith("." + extension)) {
                return modeIsInclude;
            }
        }
        return ! modeIsInclude;
    }
}
