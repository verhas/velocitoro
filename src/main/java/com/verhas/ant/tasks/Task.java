package com.verhas.ant.tasks;

import com.verhas.maven.plugins.velocitoro.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.BuildException;

/**
 *
 * @author verhas
 */
public class Task extends org.apache.tools.ant.Task {

    private Plugin plugin = new Plugin();

    public void setDebug(Boolean debug) {
        plugin.setDebug(debug);
    }

    public void setGroovyClassPaths(String[] groovyClassPaths) {
        plugin.setGroovyClassPaths(groovyClassPaths);
    }

    public void setIgnoredExtensions(String[] ignoredExtensions) {
        plugin.setIgnoredExtensions(ignoredExtensions);
    }

    public void setPasses(Integer passes) {
        plugin.setPasses(passes);
    }

    public void setScriptDirectory(String scriptDirectory) {
        plugin.setScriptDirectory(scriptDirectory);
    }

    public void setShadowExtension(String shadowExtension) {
        plugin.setShadowExtension(shadowExtension);
    }

    public void setSourceDirectory(String sourceDirectory) {
        plugin.setSourceDirectory(sourceDirectory);
    }

    public void setTargetDirectory(String targetDirectory) {
        plugin.setTargetDirectory(targetDirectory);
    }

    public void setTemplateEncoding(String templateEncoding) {
        plugin.setTemplateEncoding(templateEncoding);
    }

    @Override
    public void execute() throws BuildException {
        try {
            plugin.execute();
        } catch (MojoExecutionException ex) {
            throw new BuildException(ex);
        }
    }
}
