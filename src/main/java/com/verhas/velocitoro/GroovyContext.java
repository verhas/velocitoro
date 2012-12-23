package com.verhas.velocitoro;

import java.util.Map;

/**
 * An interface to access context information.
 *
 * @author Peter Verhas
 */
public interface GroovyContext {

    /**
     * @return the full path name to the template file that is currently
     * processed.
     */
    public String getInputFileName();

    /**
     *
     * @return the full path to the file that is written by velocitoro
     * during the processing of this file.
     */
    public String getOutputFileName();

    /**
     *
     * @return the full path to the target directory where the output files are
     * written.
     */
    public String getTargetDir();

    /**
     *
     * @return the full path to the directory where the source files are.
     */
    public String getSourceDir();

    /**
     *
     * @return the actual pass executing. It starts from zero.
     */
    public Integer getPass();

    /**
     *
     * @return the total number of passes to be executed.
     */
    public Integer getPasses();

    /**
     *
     * @return a generic Map object that can be used to store object between the
     * executions of the different scripts. The content of the Map is guaranteed
     * to remain intact during the execution of the different scripts and the
     * passes. This Map can be used to exchange data between passes and scripts.
     */
    public Map<Object, Object> getMap();
}
