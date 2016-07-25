package com.sun.org.glassfish.external.statistics;

public abstract interface Stats
{
  public abstract Statistic getStatistic(String paramString);

  public abstract String[] getStatisticNames();

  public abstract Statistic[] getStatistics();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.statistics.Stats
 * JD-Core Version:    0.6.2
 */