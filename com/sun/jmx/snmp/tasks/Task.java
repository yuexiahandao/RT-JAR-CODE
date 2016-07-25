package com.sun.jmx.snmp.tasks;

public abstract interface Task extends Runnable
{
  public abstract void cancel();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.tasks.Task
 * JD-Core Version:    0.6.2
 */