package com.sun.org.glassfish.external.probe.provider;

public abstract interface StatsProviderManagerDelegate
{
  public abstract void register(StatsProviderInfo paramStatsProviderInfo);

  public abstract void unregister(Object paramObject);

  public abstract boolean hasListeners(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.probe.provider.StatsProviderManagerDelegate
 * JD-Core Version:    0.6.2
 */