package sun.reflect.annotation;

import java.io.Serializable;

public abstract class ExceptionProxy
  implements Serializable
{
  protected abstract RuntimeException generateException();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.annotation.ExceptionProxy
 * JD-Core Version:    0.6.2
 */