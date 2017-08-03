package org.pskoko.tcpping.arguments;

import org.apache.commons.cli.*;
import org.pskoko.tcpping.arguments.ArgumentsUtilityException;
import org.pskoko.tcpping.execution.Catcher;
import org.pskoko.tcpping.execution.ExectutionMode;
import org.pskoko.tcpping.execution.Pitcher;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class is used for parsing command line arguments and
 * preparing program for execution based on them.
 * Created by pskoko on 7/31/17.
 */
public class ArgumentsUtility {
    private Options options;
    private CommandLine commandLine;
    private String[] args;

    /**
     * Constructor for class
     * @param args arguments received from command line
     */
    public ArgumentsUtility(String[] args){
        options = new Options();
        options.addOption("p", "Program works in pitcher mode");
        options.addOption("c", "Program works in catcher mode");
        options.addOption("port", true, "[Pitcher] TCP socket port to be used as connect\n" +
                                        "[Catcher] TCP socket port to be used for listen");
        options.addOption("bind", true, "[Catcher] TCP socket bind address for listening");
        options.addOption("mps", true, "[Pitcher] Speed of sending expressed in \"messages per second\"\n" +
                                        "        Default 1");
        options.addOption("size", true, "[Pitcher] Length of messages\n" +
                                        "   Minumum: 50 \n"+
                                        "   Maximum: 3000\n" +
                                        "   Default: 300");

        this.args = args;
    }

    /**
     * Prints help to system output
     */
    public void printHelp(){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setArgName("<hostname>");
        helpFormatter.printHelp("ant", "<hostname> is specified for Pitcher mode", options, "TCPPing");
    }

    /**
     * Based on command line arguments returns the specified execution mode
     * @return the specified execution mode based on command line arguments
     * @throws ArgumentsUtilityException
     */
    public ExectutionMode getExecutionMode() throws ArgumentsUtilityException {
        try {
            DefaultParser parser = new DefaultParser();
            commandLine = parser.parse(options, args);

            if(commandLine.hasOption("p")){
                return getPitcher();
            } else if(commandLine.hasOption("c")){
                return getCatcher();
            } else{
                throw new ArgumentsUtilityException("Mode of program not specified");
            }
        } catch (ParseException e) {
            throw new ArgumentsUtilityException();
        }
    }

    private Catcher getCatcher() throws ArgumentsUtilityException {
        int port = -1;
        InetAddress inetAddress = null;
        if(commandLine.hasOption("port")){
            port = parseInteger(commandLine.getOptionValue("port"), "port", 0, 65535);
        } else {
            throw new ArgumentsUtilityException("Argument error: Port not specified");
        }

        if(commandLine.hasOption("bind")){
            try {
                inetAddress = InetAddress.getByName(commandLine.getOptionValue("bind"));
            } catch (UnknownHostException e) {
                throw new ArgumentsUtilityException("Argument error: bad ip address");
            }
        } else {
            throw new ArgumentsUtilityException("Argument error: not specified bind address");
        }

        return new Catcher(port, inetAddress, System.out);
    }

    private Pitcher getPitcher() throws ArgumentsUtilityException {
        int port = -1, size = -1, rate = -1;
        String hostname = null;

        if(commandLine.hasOption("port")){
            port = parseInteger(commandLine.getOptionValue("port"), "port", 0, 65535);
        } else {
            throw new ArgumentsUtilityException("Argument error: Port not specified");
        }

        if(commandLine.hasOption("mps")){
            rate = parseInteger(commandLine.getOptionValue("mps"), "mps", 1, 1000);
        } else{
            rate = 1;
        }

        if(commandLine.hasOption("size")){
            size = parseInteger(commandLine.getOptionValue("size"), "size", 50, 3000);
        } else{
            size = 300;
        }

        if(commandLine.getArgList().size() != 1){
            throw new ArgumentsUtilityException("Argument error: hostname not specified");
        }
        hostname = commandLine.getArgList().get(0);
        return new Pitcher(port, rate, size, hostname, System.out);
    }

    private int parseInteger(String string, String name, int lowerBound, int upperBound) throws ArgumentsUtilityException {
        int value;
        try {
            value = Integer.parseInt(string);
        } catch(NumberFormatException ex){
            throw new ArgumentsUtilityException("Argument error: " + string + " is not and integer");
        }

        if(value < lowerBound || value > upperBound){
            throw new ArgumentsUtilityException("Argument error: " + string + " is out of bounds");

        }
        return value;
    }

}
