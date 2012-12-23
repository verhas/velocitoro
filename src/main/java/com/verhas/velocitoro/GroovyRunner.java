/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.verhas.velocitoro;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

/**
 *
 * @author verhas
 */
public class GroovyRunner {

    private static Logger log = Logger.getLogger(GroovyRunner.class);
    private String mode = "develop";

    public void setMode(String mode) {
        this.mode = mode;
    }
    private String scriptsDir = "";

    public void setScriptsDir(String scriptsDir) {
        this.scriptsDir = scriptsDir;
    }
    private String[] classpath = null;

    public void setClasspath(String[] classpath) {
        this.classpath = classpath;
    }

    private GroovyContextImpl context = new GroovyContextImpl();

    public GroovyContextImpl getContext() {
        return context;
    }
    private CompilerConfiguration getCompilerConfiguration(StringWriter sw) {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc = new CompilerConfiguration();
        cc.setSourceEncoding("utf-8");
        if (mode.equals("develop")) {
            cc.setDebug(true);
            cc.setRecompileGroovySource(true);
            cc.setMinimumRecompilationInterval(1000);
        } else if (mode.equals("deploy")) {
            cc.setDebug(false);
            cc.setRecompileGroovySource(false);
        } else {
            log.debug("Groovy mode is '" + mode + "' invalid, ignored");
        }
        PrintWriter pw = new PrintWriter(sw);
        cc.setOutput(pw);
        return cc;
    }

    private File getScriptFile(String script) {
        if (scriptsDir == null) {
            log.error("groovy.scripts is not configured. " +
                    "I do not know where to load groovy scripts from.");
            return null;
        }
        if (!scriptsDir.endsWith("/")) {
            scriptsDir += "/";
        }
        return new File(scriptsDir + script);
    }

    private GroovyClassLoader getGroovyClassLoader() {
        GroovyClassLoader gcl = new GroovyClassLoader(
                this.getClass().getClassLoader());
        for (String cp : classpath) {
            gcl.addClasspath(cp);
        }
        return gcl;
    }

    public Object invoke(String script, String... params) {
        if (script == null) {
            return null;
        }
        Binding binding = new Binding();
        binding.setVariable("context", (GroovyContext)context);
        StringWriter sw = new StringWriter();
        CompilerConfiguration cc = getCompilerConfiguration(sw);
        GroovyClassLoader classLoader = getGroovyClassLoader();
        File scriptFile = getScriptFile(script);
        GroovyShell shell = new GroovyShell(classLoader, binding, cc);
        Object scriptReturnedObject = null;
        try {
            scriptReturnedObject = shell.run(scriptFile, params);
        } catch (CompilationFailedException ex) {
            log.error("Could not compile the groovy script '" + script + "'", ex);
            log.error("Compiler error message is: " + sw.toString());
            scriptReturnedObject =
                    sw.toString();
        } catch (Throwable th) {
            log.error("Script '" + script + "'resulted error or exception", th);
            scriptReturnedObject =
                    th.toString();
        }
        return scriptReturnedObject;
    }
}
