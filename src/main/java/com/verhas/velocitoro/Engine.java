package com.verhas.velocitoro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;

/**
 * The core Velocitoro engine. All the applications, like the Maven plugin,
 * ANT task or the command line version utilize the services provided by this
 * class.
 * <p>
 * Note that for the different parameters the class has default values that
 * may not be the same as that of the different applications. For example
 * the Maven plugin version sets most of the parameters to values different
 * from the engine default to values that fit more the Maven style.
 * 
 * @author Peter Verhas
 */
public class Engine {

    private String sourceDirectoryName = "./";
    private String tagetDirectoryName = "./target";
    private String templateEncoding = "UTF-8";
    private String[] groovyClassPath = new String[]{"groovy"};
    private String scriptDirectory = null;
    private Integer passes = 1;
    private String[] ignoredExtensions = new String[]{"vm", "vmi", "groovy"};
    private String shadowExtension = "vm";
    private Boolean debug = false;
    private PrintStream debugOutputStream = System.out;

    /**
     * Set the debug output stream. The default value is {@code System.out}.
     *
     * @param debugOutputStream
     */
    public void setDebugOutputStream(PrintStream debugOutputStream) {
        this.debugOutputStream = debugOutputStream;
    }

    /**
     * Set the debug flag. The default is false. When the default flag is set
     * debug information is sent to the debug output stream.
     * @param debug
     */
    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    private void debug(String msg) {
        if (debug) {
            debugOutputStream.println(msg);
        }
    }

    /**
     * Set the array of ignored extensions. By default this array contains
     * {@code vm}, {@code groovy} and {@code vmi}. Files with those extensions
     * serve special purpose and are not part of the verbatim or processed
     * content.
     *
     * @param ignoredExtensions and array of String containing the ignorable
     * extensions. The strings should NOT contain the dot, e.g. {@code vm}
     * and NOT {vode .vm}
     */
    public void setIgnoredExtensions(String[] ignoredExtensions) {
        this.ignoredExtensions = ignoredExtensions;
    }

    /**
     * Set the extension that the shadow files should have. The default value is
     * {@code vm}. A shadow file has the same name as the original file and
     * has the extra extension, {@code vm} by default.
     *
     * @param shadowExtension the extension without the dot, e.g. {@code vm}
     * and NOT {@code .vm}
     */
    public void setShadowExtension(String shadowExtension) {
        this.shadowExtension = shadowExtension;
    }

    /**
     * Set the number of passes velocitoro has to perform. The default is 1.
     *
     * @param passes the number of passes velocity should run.
     */
    public void setPasses(Integer passes) {
        this.passes = passes;
    }

    /**
     * Set the script directory where the groovy scripts are.
     * <p>
     * The default value is {@code null} and if it not set then the
     * source directory, where the velicity template files are will be
     * used.
     * @param scriptDirectory
     */
    public void setScriptDirectory(String scriptDirectory) {
        this.scriptDirectory = scriptDirectory;
    }

    /**
     * Set the groovy classpath.
     * <p>
     * Calling this method you can add directories to the standard Java
     * classpath to include in the search when groovy is locating source files
     * for classes written in groovy.
     *
     * @param groovyClassPath array of directtory names. The default value
     * contains a single directory, named {@code groovy}. The maven plugin sets
     * this value to contain a single directory {@code src/main/groovy}.
     * 
     */
    public void setGroovyClassPath(String[] groovyClassPath) {
        this.groovyClassPath = groovyClassPath;
    }

    /**
     * Set the name of the directory where the source files are. The default
     * value is the current working directory. The maven plugin sets this
     * value to {@code src/web} by default.
     *
     * @param sourceDirectoryName the name of the source directory
     */
    public void setSourceDirectoryName(String sourceDirectoryName) {
        this.sourceDirectoryName = sourceDirectoryName;
    }

    /**
     * Set the name of the directory where the generated source file will
     * be created. The default value is the directory {@code target} under the
     * current working directory. The maven plugin sets this value by default to
     * be {@code target/web}.
     *
     * @param tagetDirectoryName
     */
    public void setTagetDirectoryName(String tagetDirectoryName) {
        this.tagetDirectoryName = tagetDirectoryName;
    }

    /**
     * Set the character encoding for the templates. The default value is
     * {@code UTF-8}.
     *
     * @param templateEncoding the standard name of the charset used to encode
     * the source files.
     */
    public void setTemplateEncoding(String templateEncoding) {
        this.templateEncoding = templateEncoding;
    }
    private File targetDir;
    private File sourceDir;

    /**
     * Copy the files listed to the target directory.
     *
     * @param sourceFiles the list of files to copy byte by byte.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void copyFilesVerbatim(File[] sourceFiles)
            throws FileNotFoundException, IOException {
        debug("Copying files verbatim.");
        byte[] buffer = new byte[102400]; // 100KB
        for (File sourceFile : sourceFiles) {
            if (sourceFile.isFile()) {
                String outputFile = targetDir.getAbsolutePath() +
                        sourceFile.getPath().substring(sourceDirectoryName.length());
                FileInputStream fis = new FileInputStream(sourceFile);
                new File(new File(outputFile).getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(outputFile);
                debug("Copying " + sourceFile.getAbsolutePath() +
                        " to " + new File(outputFile).getAbsolutePath());
                int len = 0;
                while (len != -1) {
                    len = fis.read(buffer);
                    if (len != -1) {
                        fos.write(buffer, 0, len);
                    }
                }
                fis.close();
                fos.close();
            }
        }
        debug("All files are copied.");
    }

    private void processFiles(File[] sourceFiles) throws Exception {
        debug("Start processing the files.");
        VelocityEngine ve;
        ve = new VelocityEngine();
        ve.setProperty("file.resource.loader.path", sourceDirectoryName);
        ve.setProperty("directive.foreach.counter.name", "velocityCount");
        ve.setProperty("directive.foreach.iterator.name", "velocityHasNext");
        ve.setProperty("template.encoding", templateEncoding);
        ve.init();
        VelocityContext vc = new VelocityContext();
        vc.put("date", new DateTool());
        GroovyRunner gr = new GroovyRunner();
        gr.setClasspath(groovyClassPath);
        if (scriptDirectory == null) {
            gr.setScriptsDir(sourceDirectoryName);
        } else {
            gr.setScriptsDir(scriptDirectory);
        }
        vc.put("groovy", gr);
        GroovyContextImpl context = gr.getContext();
        context.setPasses(passes);
        debug("Starting " + passes + " passes");
        for (int i = 0; i < passes; i++) {
            debug("Starting pass " + i);
            for (File sourceFile : sourceFiles) {
                String inputFile = sourceFile.getPath().replaceAll("\\." +
                        shadowExtension + "$", "").
                        substring(sourceDirectoryName.length());
                String outputFile = targetDir.getAbsolutePath() +
                        inputFile;
                context.setOutputFileName(outputFile);
                context.setInputFileName(sourceDir.getAbsolutePath() +
                        inputFile);
                context.setSourceDir(sourceDir.getAbsolutePath());
                context.setTargetDir(targetDir.getAbsolutePath());
                context.setPass(i);
                createDirectoryForFile(outputFile);
                debug("Processing " + context.getInputFileName() + " to " +
                        context.getOutputFileName());
                Writer w = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
                Template te = ve.getTemplate(inputFile, "utf-8");
                te.process();
                te.merge(vc, w);
                w.close();
            }
        }
        debug("Processing finished.");
    }

    private void createDirectoryForFile(String file) {
        new File(new File(file).getParent()).mkdirs();
    }

    public void createSite() throws Exception {
        targetDir = new File(tagetDirectoryName);
        targetDir.mkdirs();
        sourceDir = new File(sourceDirectoryName);

        //
        // Copy all files that are not velocity macro files and not
        // velocy macro include files
        //
        FilenameFilterExt filter = new FilenameFilterExt();
        filter.cleanExtensionList().setModeExclude();
        for (String extension : ignoredExtensions) {
            filter.addExtension(extension);
        }
        FileLister fileLister = new FileLister();
        fileLister.setDirectory(new File(sourceDirectoryName)).
                setFilter(filter).setRecurse();
        File[] sourceFiles = fileLister.listFiles();
        copyFilesVerbatim(sourceFiles);
        //
        // Create the HTML files processed through Velocity
        //
        filter = new FilenameFilterExt();
        filter.cleanExtensionList().
                setModeInclude().
                addExtension(shadowExtension);
        fileLister.setDirectory(new File(sourceDirectoryName)).
                setFilter(filter).setRecurse();
        sourceFiles = fileLister.listFiles();
        processFiles(sourceFiles);
    }
}
