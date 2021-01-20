package org.myagent;

import org.myagent.log.Log;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * agentmain入口类
 * @author yangwenpeng
 * @version 2021年1月20日16:49:50
 */
public class Agent {

    /**
     * agentmain方法，agent入口方法
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        try {
            Log.init(agentArgs.split(";")[2]);
            Log.getInstants().writeLine("start agent args:" + agentArgs);
            transferOnce(agentArgs, inst);
            Log.getInstants().writeLine("finish agent!");
            Log.release();
        } catch (Exception e) {
            Log.getInstants().writeError("agent error!", e);
            throw new RuntimeException("agent error", e);
        }
    }


    private static void transferOnce(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException {
        String[] args = agentArgs.split(";");
        Log.getInstants().writeLine("transformClass:" + args[0]);
        final MyTransformer myTransformer = new MyTransformer(args);
        try {
            inst.addTransformer(myTransformer, true);
            inst.retransformClasses(Class.forName(args[0]));
        } finally {
            inst.removeTransformer(myTransformer);
        }
    }

    private static void transferTheard(String agentArgs, Instrumentation inst) {
        new Thread(() -> {
            File file = new File(agentArgs);
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line = null;
                while (true) {
                    while ((line = bufferedReader.readLine()) == null) {
                        synchronized (bufferedReader) {
                            bufferedReader.wait(1000);
                        }
                    }
                    String[] args = line.split(";");
                    final MyTransformer myTransformer = new MyTransformer(args);
                    inst.addTransformer(myTransformer, true);
                    inst.retransformClasses(Class.forName(line));
                    inst.removeTransformer(myTransformer);
                }
            } catch (IOException | InterruptedException | ClassNotFoundException | UnmodifiableClassException e) {
                Log.getInstants().writeError("agent error", e);
            }
        }).start();
    }
}
