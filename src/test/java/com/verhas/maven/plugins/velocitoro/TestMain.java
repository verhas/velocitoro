package com.verhas.maven.plugins.velocitoro;


import com.verhas.velocitoro.Engine;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author verhas
 */
public class TestMain {

    public TestMain() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void startMain() throws Exception {
        Engine engine = new Engine();
        engine.setSourceDirectoryName("src/web");
        engine.setTagetDirectoryName("target/web");
        engine.setGroovyClassPath(new String[]{"src/main/groovy"});
        engine.createSite();
    }

    @Test
    public void startMaven() throws Exception{
        Plugin plugin = new Plugin();
        plugin.setDebug();
        plugin.execute();
    }
}
