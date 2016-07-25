package com.sun.corba.se.impl.activation;

import java.io.PrintStream;
import org.omg.CORBA.ORB;

public abstract interface CommandHandler
{
  public static final boolean shortHelp = true;
  public static final boolean longHelp = false;
  public static final boolean parseError = true;
  public static final boolean commandDone = false;

  public abstract String getCommandName();

  public abstract void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean);

  public abstract boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.CommandHandler
 * JD-Core Version:    0.6.2
 */