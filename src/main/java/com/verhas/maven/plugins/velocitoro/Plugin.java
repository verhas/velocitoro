package com.verhas.maven.plugins.velocitoro;

import com.verhas.velocitoro.Engine;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Interface Maven to provide plugin functionality and call the Velocitoro engine.
 *
 * @goal velocitoro
 */
public class Plugin extends AbstractMojo {

    /**
     * @parameter expression="${velocitoro.sourceDirectory}"
     */
    private String sourceDirectory;
    /**
     * @parameter expression="${velocitoro.targetDirectory}"
     */
    private String targetDirectory;
    /**
     * @parameter expression="${velocitoro.groovyClassPaths}"
     */
    private String[] groovyClassPaths;
    /**
     * @parameter expression="${velocitoro.templateEncoding}"
     */
    private String templateEncoding;
    /**
     * @parameter expression="${velocitoro.scriptDirectory}"
     */
    private String scriptDirectory;
    /**
     * @parameter expression="${velocitoro.passes}"
     */
    private Integer passes;
    /**
     * @parameter expression="${velocitoro.ignoredExtensions}"
     */
    private String[] ignoredExtensions;
    /**
     * @parameter expression="${velocitoro.shadowExtension}"
     */
    private String shadowExtension;
    /**
     * @parameter expression="${velocitoro.debug}"
     */
    private Boolean debug = false;

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public void setGroovyClassPaths(String[] groovyClassPaths) {
        this.groovyClassPaths = groovyClassPaths;
    }

    public void setIgnoredExtensions(String[] ignoredExtensions) {
        this.ignoredExtensions = ignoredExtensions;
    }

    public void setPasses(Integer passes) {
        this.passes = passes;
    }

    public void setScriptDirectory(String scriptDirectory) {
        this.scriptDirectory = scriptDirectory;
    }

    public void setShadowExtension(String shadowExtension) {
        this.shadowExtension = shadowExtension;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public void setTemplateEncoding(String templateEncoding) {
        this.templateEncoding = templateEncoding;
    }

    public Boolean getDebug() {
        return debug;
    }

    public static String getDumpHR() {
        return dumpHR;
    }

    public static String getDumpPrefix() {
        return dumpPrefix;
    }

    public String[] getGroovyClassPaths() {
        return groovyClassPaths;
    }

    public String[] getIgnoredExtensions() {
        return ignoredExtensions;
    }

    public Integer getPasses() {
        return passes;
    }

    public String getScriptDirectory() {
        return scriptDirectory;
    }

    public String getShadowExtension() {
        return shadowExtension;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public String getTemplateEncoding() {
        return templateEncoding;
    }

    /**
     * Set the debug option to debug.
     */
    protected void setDebug() {
        debug = true;
    }

    /**
     * Set the default values for the maven implementation of Velocitoro.
     */
    private void setDefaults() {
        if (sourceDirectory == null) {
            sourceDirectory = "src/web";
        }
        if (targetDirectory == null) {
            targetDirectory = "target/web";
        }
        if (groovyClassPaths == null || groovyClassPaths.length == 0) {
            groovyClassPaths = new String[]{"src/main/groovy"};
        }
        if (templateEncoding == null) {
            templateEncoding = "UTF-8";
        }
        if (passes == null) {
            passes = 1;
        }
        if (ignoredExtensions == null || ignoredExtensions.length == 0) {
            ignoredExtensions = new String[]{"vm", "vmi", "groovy"};
        }
        if (shadowExtension == null) {
            shadowExtension = "vm";
        }
    }
    private static final String dumpPrefix = "[INFO] ";
    private static final String dumpHR =
            "------------------------------------------------------------------------";

    private void dump(String name, Object value) {
        System.out.println(dumpPrefix + name + "=" + value);
    }

    private void dump(String name, Object[] values) {
        if (values.length != 0) {
            for (Object value : values) {
                dump(name, value);
            }
        } else {
            System.out.println(dumpPrefix + name + " is empty");
        }
    }

    private void dumpDebug() {
        System.out.println(dumpPrefix + dumpHR);
        System.out.println(dumpPrefix + "VELOCITORO DUMP");
        System.out.println(dumpPrefix + dumpHR);
        dump("sourceDirectory", sourceDirectory);
        dump("targetDirectory", targetDirectory);
        dump("scriptDirectory", scriptDirectory);
        dump("groovyClassPaths", groovyClassPaths);
        dump("passes", passes);
        dump("templateEncoding", templateEncoding);
        dump("ignoredExtensions", ignoredExtensions);
        dump("shadowExtension", shadowExtension);
        System.out.println(dumpPrefix + dumpHR);
    }

    /**
     * Set up the engine setting the values that are either the maven plugin
     * default or configured via the command line or via {@code pom.xml}
     * plugin configuration.
     *
     * @param engine the engine to set up.
     */
    private void setupEngine(Engine engine) {
        engine.setSourceDirectoryName(sourceDirectory);
        engine.setTagetDirectoryName(targetDirectory);
        engine.setGroovyClassPath(groovyClassPaths);
        engine.setTemplateEncoding(templateEncoding);
        engine.setPasses(passes);
        engine.setIgnoredExtensions(ignoredExtensions);
        engine.setShadowExtension(shadowExtension);
        if (scriptDirectory != null) {
            engine.setScriptDirectory(scriptDirectory);
        }
        engine.setDebug(debug);
    }

    /**
     * Execute the velocitoro engine.
     *
     * @throws MojoExecutionException
     */
    @Override
    public void execute() throws MojoExecutionException {
        try {
            setDefaults();
            Engine engine = new Engine();
            setupEngine(engine);
            if (debug) {
                dumpDebug();
            }
            engine.createSite();
        } catch (Exception e) {
            getLog().error("Velocitoro could not be executed without problem.", e);
            throw new MojoExecutionException("Velocitor could not be executed.",
                    e);
        }
    }
}

