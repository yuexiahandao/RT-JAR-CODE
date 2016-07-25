package com.sun.corba.se.spi.orbutil.fsm;

public abstract interface FSM
{
  public abstract State getState();

  public abstract void doIt(Input paramInput);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.fsm.FSM
 * JD-Core Version:    0.6.2
 */