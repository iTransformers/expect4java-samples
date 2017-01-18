package net.itransformers.expect4java_samples.ssh.simple;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.UserInfo;
import net.itransformers.expect4java.Expect4j;
import net.itransformers.expect4java.ExpectContext;
import net.itransformers.expect4java.cliconnection.CLIConnection;
import net.itransformers.expect4java.cliconnection.CLIStreamLogger;
import net.itransformers.expect4java.cliconnection.impl.LoggableCLIConnection;
import net.itransformers.expect4java.cliconnection.impl.SshCLIConnection;
import net.itransformers.expect4java.impl.Expect4jException;
import net.itransformers.expect4java.impl.Expect4jImpl;
import net.itransformers.expect4java.matches.EofMatch;
import net.itransformers.expect4java.matches.GlobMatch;
import net.itransformers.expect4java.matches.RegExpMatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by cpt2vyo on 18.1.2017 г..
 */
public class SshExample {
    public static void main(String[] args) throws Exception {
        CLIConnection sshConn = new LoggableCLIConnection(
                new SshCLIConnection(),
                msg -> System.out.println(">>> "+msg),
                msg -> System.out.println("<<< "+msg)
        );
        Map<String, Object> connParams = new HashMap<>();
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "keyboard-interactive,password");
        connParams.put("username", "guest");
        connParams.put("password", "pass123");
        connParams.put("address", "vyordanov.tk");
        connParams.put("port", 22);
        connParams.put("timeout", 1000);
        connParams.put("config", config);

        sshConn.connect(connParams);

        Expect4j e4j = new Expect4jImpl(sshConn);
        e4j.send("uname -a\n");
        e4j.expect( new RegExpMatch("Linux ([^ ]*) .*",
                (ExpectContext context) -> System.out.println("### Found: "+context.getMatch(1)))
        );
        e4j.send("exit\n");
        e4j.expect(new EofMatch());
        e4j.close();
        sshConn.disconnect();
    }
}