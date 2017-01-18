package net.itransformers.expect4java_samples.ssh.with_userinfo;

import com.jcraft.jsch.UserInfo;
import net.itransformers.expect4java.Expect4j;
import net.itransformers.expect4java.cliconnection.CLIConnection;
import net.itransformers.expect4java.cliconnection.impl.LoggableCLIConnection;
import net.itransformers.expect4java.cliconnection.impl.SshCLIConnection;
import net.itransformers.expect4java.impl.Expect4jImpl;
import net.itransformers.expect4java.matches.EofMatch;
import net.itransformers.expect4java.matches.GlobMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Vasil Yordanov on 18.1.2017 г..
 */
public class SshExampleWithUserInfo {
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
        connParams.put("address", "vyordanov.tk");
        connParams.put("port", 22);
        connParams.put("timeout", 1000);
        connParams.put("config", config);
        UserInfo ui=new MySimpleUserInfo("pass123");
        connParams.put("userInfo", ui);

        sshConn.connect(connParams);

        Expect4j e4j = new Expect4jImpl(sshConn);
        e4j.send("whoami\n");
        e4j.expect(new GlobMatch("guest"));
        e4j.send("exit\n");
        e4j.expect(new EofMatch());
        e4j.close();
        sshConn.disconnect();

    }

}
