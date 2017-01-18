package net.itransformers.expect4java_samples.helloworld;

import net.itransformers.expect4java.cliconnection.CLIConnection;
import net.itransformers.expect4java.cliconnection.impl.*;
import net.itransformers.expect4java.impl.Expect4jImpl;
import net.itransformers.expect4java.matches.GlobMatch;
import java.util.HashMap;

/**
 * Created by Vasil Yordanov on 18.1.2017 г..
 */
public class HelloWorldExpect4Java {
    public static void main(String[] args) throws Exception {
        CLIConnection cliConnection = new LoggableCLIConnection(
                new EchoCLIConnection(),
                message -> System.out.println(">>> " + message),
                message -> System.out.println("<<< " + message)
        );
        cliConnection.connect(new HashMap<>());

        Expect4jImpl e4j = new Expect4jImpl(cliConnection);
        e4j.send("Hello World\n");
        e4j.expect(new GlobMatch("Hello World"));
    }
}
