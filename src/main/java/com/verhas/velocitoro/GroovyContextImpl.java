/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.verhas.velocitoro;

import java.util.Hashtable;
import java.util.Map;

/**
 * An implementation of the interface {@see GroovyContext}. The implementation
 * provides the getter to local private variables and setters. That is all.
 *
 * @author Peter Verhas
 */
public class GroovyContextImpl implements GroovyContext {

    private String inputFileName;

    @Override
    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String fileName) {
        this.inputFileName = fileName;
    }
    private String outputFileName;

    @Override
    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
    private String targetDir;

    @Override
    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }
    private String sourceDir;

    @Override
    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }
    private Integer pass;

    @Override
    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }
    private Integer passes;

    @Override
    public Integer getPasses() {
        return passes;
    }

    public void setPasses(Integer passes) {
        this.passes = passes;
    }
    private Map<Object, Object> map = new Hashtable<Object, Object>();

    @Override
    public Map<Object, Object> getMap() {
        return map;
    }

    public void setMap(Map<Object, Object> map) {
        this.map = map;
    }
}
