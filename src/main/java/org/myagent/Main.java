package org.myagent;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String pid, className, url, logfile;
        if (args.length < 3) {
            Scanner scanner = new Scanner(System.in);
            List<VirtualMachineDescriptor> list = VirtualMachine.list();
            System.out.println("args mission! Please entry args:\n");
            list.forEach(e -> System.out.println(e.toString()));
            System.out.print("entry a PID from upper:");
            pid = scanner.nextLine();
            System.out.print("\nclassName:");
            className = scanner.nextLine();
            System.out.print("\nclassPath:");
            url = scanner.nextLine();
            System.out.print("\nlogfile:");
            logfile = scanner.nextLine();
        } else {
            pid = args[0];
            className = args[1];
            url = args[2];
            logfile = args[3];
        }
        VirtualMachine virtualMachine = null;
        try {
            System.out.println("attaching...");
            virtualMachine = VirtualMachine.attach(pid);
            System.out.println("loadAgent...");
            virtualMachine.loadAgent(Main.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/", ""), className + ";" + url + ";" + logfile);
            System.out.println("agent loaded!\ndetaching...");
            virtualMachine.detach();
            System.out.println("detached!");
        } catch (AttachNotSupportedException | IOException | AgentLoadException | AgentInitializationException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
