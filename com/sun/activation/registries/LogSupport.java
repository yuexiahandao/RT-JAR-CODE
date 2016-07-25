package com.sun.activation.registries;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogSupport {
    private static boolean debug = false;

    private static Logger logger = Logger.getLogger("javax.activation");

    private static final Level level = Level.FINE;

    public static void log(String msg) {
        if (debug)
            System.out.println(msg);
        logger.log(level, msg);
    }

    public static void log(String msg, Throwable t) {
        if (debug)
            System.out.println(msg + "; Exception: " + t);
        logger.log(level, msg, t);
    }

    public static boolean isLoggable() {
        return (debug) || (logger.isLoggable(level));
    }

    static {
        try {
            debug = Boolean.getBoolean("javax.activation.debug");
        } catch (Throwable t) {
        }
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.activation.registries.LogSupport
 * JD-Core Version:    0.6.2
 */