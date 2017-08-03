package org.pskoko.tcpping;

import org.pskoko.tcpping.arguments.ArgumentsUtility;
import org.pskoko.tcpping.arguments.ArgumentsUtilityException;
import org.pskoko.tcpping.execution.ExectutionMode;

/**
 * Main class for starting a program
 * Created by pskoko on 7/31/17.
 */
public class TCPPing {

    /**
     * Starting point of execution of progra
     * @param args arguments from command line
     */
    public static void main(String[] args){
        ArgumentsUtility argumentsUtility = new ArgumentsUtility(args);
        ExectutionMode exectutionMode = null;

        try {
            exectutionMode = argumentsUtility.getExecutionMode();
        } catch (ArgumentsUtilityException e) {
            System.out.println(e.getMessage());
            argumentsUtility.printHelp();
            return;
        }

        exectutionMode.execute();
    }
}
