package com.sun.org.glassfish.external.statistics;

public abstract interface BoundaryStatistic extends Statistic
{
  public abstract long getUpperBound();

  public abstract long getLowerBound();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.statistics.BoundaryStatistic
 * JD-Core Version:    0.6.2
 */