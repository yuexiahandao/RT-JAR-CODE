package java.util.concurrent;

import java.util.Map;

public abstract interface ConcurrentMap<K, V> extends Map<K, V>
{
  public abstract V putIfAbsent(K paramK, V paramV);

  public abstract boolean remove(Object paramObject1, Object paramObject2);

  public abstract boolean replace(K paramK, V paramV1, V paramV2);

  public abstract V replace(K paramK, V paramV);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ConcurrentMap
 * JD-Core Version:    0.6.2
 */