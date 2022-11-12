import com.sshtools.client.SshClient;
import com.sshtools.common.permissions.UnauthorizedException;
import com.sshtools.common.publickey.InvalidPassphraseException;
import com.sshtools.common.ssh.SshException;
import net.datafaker.Faker;
import me.tongfei.progressbar.*;
import com.mysql.jdbc.Driver;


import com.jcraft.jsch.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker(new Locale("es-MX"));
        JSch jsch = new JSch();
        Connection con = null;

        int count = 0;
        List<Long> studentAccounts = new ArrayList<>();
        long randomStudentAccount;
        int studentNumber = 1000000;
        boolean isValid = false;
        int assigned_port;
        int local_port = 3306;
        String remote_host = "209.126.82.71";
        int remote_port = 3306;



        /* try *//*(SshClient ssh = new SshClient("209.126.82.71", 22, "red_g", new File("C:/Users/red_g/.ssh/id_ed25519")))*//* {
         *//*jsch.addIdentity("C:/Users/red_g/.ssh/id_ed25519");*//*
            Session session = jsch.getSession("red_g", "209.126.82.71", 22);
            session.setPassword("P4P4lla00@98's");
            java.util.Properties config = new java.util.Properties();
            config.put("ServerAliveInterval", "30");
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);

            session.connect();

            System.out.println("SSH Connected");

            *//*ssh.getForwardingPolicy().allowForwarding();
            ssh.getForwardingPolicy().allowGatewayForwarding();

            if(ssh.isAuthenticated()) {
                System.out.println("Authenticated");
            } else {
                System.out.println("Authentication failed");
            }

            ssh.startRemoteForwarding("209.126.82.71", 3306, "127.0.0.1", 3306);*//*
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }*/

        try {
            con = DriverManager.getConnection("jdbc:mysql://209.126.82.71:3306/school", "red_g", "Z.!Vz*XcEksJx6");
            System.out.println("DB Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try  {
            con.prepareStatement("TRUNCATE TABLE student").executeUpdate();
            System.out.println("Table data erased");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ProgressBar pb = new ProgressBar("Data Insertion", studentNumber)) {
            for (int i = 0; i < studentNumber; i++){
                do {
                    randomStudentAccount = faker.number().randomNumber(7, true);
                    if (!studentAccounts.contains(randomStudentAccount)) {
                        studentAccounts.add(randomStudentAccount);
                        isValid = true;
                    }
                } while (!isValid);

                con.prepareStatement("INSERT INTO student (enrollment, name, email, grade) VALUES (" + randomStudentAccount + ", \"" + faker.name().fullName() + "\", \"" + faker.internet().emailAddress() + "\", " + faker.number().randomDouble(2, 0, 10) + ")").executeUpdate();
                pb.step();
            }
        } catch (SQLException e) {
            count++;
            e.printStackTrace();
        }
        System.out.println("Proceso terminado con: " + count + " errores");
    }
}