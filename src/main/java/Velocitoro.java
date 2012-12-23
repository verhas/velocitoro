
import com.verhas.velocitoro.Engine;
import org.apache.log4j.Logger;
import org.apache.maven.plugin.MojoExecutionException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * Class containing the {@code public static void main} to start velocitoro
 * from the command line other than as a Maven plugin or ANT task.
 * 
 * @author Peter Verhas
 */
public class Velocitoro {

    private static Logger log = Logger.getLogger(Velocitoro.class);

    /**
     * Start Velocitoro with the default settings.
     * @param args ignored.
     * @throws MojoExecutionException
     */
    public static void main(String[] args) throws MojoExecutionException {
        try {
            new Engine().createSite();
        } catch (Exception e) {
            log.error("Velocitor could not be executed without problem.", e);
            System.err.print(e);
        }
    }
}
