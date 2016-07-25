package com.sun.org.glassfish.external.statistics;

public abstract interface RangeStatistic extends Statistic
{
  public abstract long getHighWaterMark();

  public abstract long getLowWaterMark();

  public abstract long getCurrent();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.statistics.RangeStatistic
 * JD-Core Version:    0.6.2
 */