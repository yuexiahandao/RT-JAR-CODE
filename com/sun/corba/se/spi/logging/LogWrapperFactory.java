package com.sun.corba.se.spi.logging;

import java.util.logging.Logger;

public abstract interface LogWrapperFactory
{
  public abstract LogWrapperBase create(Logger paramLogger);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.logging.LogWrapperFactory
 * JD-Core Version:    0.6.2
 */