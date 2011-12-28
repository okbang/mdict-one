package oog.codechecker;

import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;


/**
 * @author MINH
 *
 */
public class CodeChecker {
    /** the options to the command line. */
    private static final Options OPTS = new Options();
    /** the object config to receive an xml configuration file. */
    private Configuration config = null;
    static {
        OPTS.addOption("c", true, "The check configuration file to use.");
        OPTS.addOption("r", true, "Traverse the directory for source files");
        //OPTS.addOption("o", true, "Sets the output file. Defaults to stdout");
        //OPTS.addOption("p", true, "Loads the properties file");
        //OPTS.addOption("f", true, "Sets the output format. (plain|xml). Defaults to plain");
    }
    /**
     * @param xmlFilePath
     *            path of check-style xml configuration.
     */
    public CodeChecker(String xmlFilePath) {
        String[] commandLine = {"-c", xmlFilePath};
        final CommandLineParser clp = new PosixParser();
        CommandLine line = null;
        try {
            line = clp.parse(OPTS, commandLine);
        } catch (final ParseException e) {
            e.printStackTrace();
            usage();
        }
        assert line != null;

        // setup the properties
        Properties props = null;
        props = System.getProperties();

        // ensure a config file is specified
        if (!line.hasOption("c")) {
            System.out.println("Must specify a config XML file.");
            usage();
        }

        config = loadConfig(line, props);
    }

    /**
    * Return the checking results in the form of a map.
    * @param srcPath which is a path to the source.
    * @return a Map containing the checking results
    */
    public Map<String, List<AuditEvent>> check(String srcPath) {
        // Key of Map: filename (include path)
        Map<String, List<AuditEvent>> resultCheck = new HashMap<String, List<AuditEvent>>();
        final CommandLineParser clp = new PosixParser();
        CommandLine line = null;
        String[] commandLine = {"-r", srcPath};
        try {
            line = clp.parse(OPTS, commandLine);
        } catch (final ParseException e) {
            e.printStackTrace();
            usage();
        }
        assert line != null;
        final CCListener listener = new CCListener(resultCheck);
        final List<File> files = getFilesToProcess(line);
        final Checker c = createChecker(config, listener);
        final int numErrs = c.process(files);
        return listener.getResultCheck();
    }

    /**
     * Creates the Checker object.
     * @param aConfig
     *            the configuration to use
     * @param aNosy
     *            the sticky beak to track what happens
     * @return a nice new fresh Checker
     */
    private static Checker createChecker(Configuration aConfig, AuditListener aNosy) {
        Checker checker = null;
        try {
            checker = new Checker();

            final ClassLoader moduleClassLoader = Checker.class.getClassLoader();
            checker.setModuleClassLoader(moduleClassLoader);
            checker.configure(aConfig);
            checker.addListener(aNosy);
        } catch (final Exception e) {
            System.out.println("Unable to create Checker: " + e.getMessage());
            e.printStackTrace(System.out);
            System.exit(1);
        }
        return checker;
    }

    /**
     * Determines the files to process.
     * @param aLine
     *            the command line options specifying what files to process
     * @return list of files to process
     */
    private static List<File> getFilesToProcess(CommandLine aLine) {
        final List<File> files = Lists.newLinkedList();
        if (aLine.hasOption("r")) {
            final String[] values = aLine.getOptionValues("r");
            for (String element : values) {
                traverse(new File(element), files);
            }
        }

        final String[] remainingArgs = aLine.getArgs();
        for (String element : remainingArgs) {
            files.add(new File(element));
        }

        if (files.isEmpty() && !aLine.hasOption("r")) {
            System.out.println("Must specify files to process");
            usage();
        }
        return files;
    }



    /**
     * Loads the configuration file. Will exit if unable to load.
     * @param aLine
     *            specifies the location of the configuration
     * @param aProps
     *            the properties to resolve with the configuration
     * @return a fresh new configuration
     */
    private static Configuration loadConfig(CommandLine aLine, Properties aProps) {
        try {
            return ConfigurationLoader.loadConfiguration(aLine.getOptionValue("c"), new PropertiesExpander(aProps));
        } catch (final CheckstyleException e) {
            System.out.println("Error loading configuration file");
            e.printStackTrace(System.out);
            System.exit(1);
            return null; // can never get here
        }
    }

    /** Prints the usage information. **/
    private static void usage() {
        final HelpFormatter hf = new HelpFormatter();
        hf.printHelp("java " + CodeChecker.class.getName() + " [options] -c <config.xml> file...", OPTS);
        System.exit(1);
    }

    /**
     * Traverses a specified node looking for files to check. Found files are added to a specified list. Subdirectories
     * are also traversed.
     * @param aNode
     *            the node to process
     * @param aFiles
     *            list to add found files to
     */
    private static void traverse(File aNode, List<File> aFiles) {
        if (aNode.canRead()) {
            if (aNode.isDirectory()) {
                final File[] nodes = aNode.listFiles();
                for (File element : nodes) {
                    traverse(element, aFiles);
                }
            } else if (aNode.isFile()) {
                aFiles.add(aNode);
            }
        }
    }


}
