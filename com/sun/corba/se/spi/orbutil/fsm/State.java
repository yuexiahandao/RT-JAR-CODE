package com.sun.corba.se.spi.orbutil.fsm;

public abstract interface State
{
  public abstract void preAction(FSM paramFSM);

  public abstract void postAction(FSM paramFSM);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.fsm.State
 * JD-Core Version:    0.6.2
 */