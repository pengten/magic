package org.myagent;

import org.myagent.log.Log;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * agentmain入口类
 * the agent class
 *
 * @author yangwenpeng
 * @version 2021年1月20日16:49:50
 */
public class Agent {

    /**
     * agentmain方法，agent入口方法
     * Agent entry method
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        try {
            Log.init(agentArgs.split(";")[2]);
            Log.getInstants().writeLine("start agent args:" + agentArgs);
            transfer(agentArgs, inst);
            Log.getInstants().writeLine("finish agent!");
            Log.release();
        } catch (Exception e) {
            Log.getInstants().writeError("agent error!", e);
            throw new RuntimeException("agent error", e);
        }
    }


    /**
     * 执行字节码转换
     * Perform bytecode conversion
     * @param agentArgs agent args
     * @param inst {@link Instrumentation}
     * @throws ClassNotFoundException
     * @throws UnmodifiableClassException
     */
    private static void transfer(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException {
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
}
