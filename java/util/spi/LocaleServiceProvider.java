package java.util.spi;

import java.util.Locale;

public abstract class LocaleServiceProvider
{
  public abstract Locale[] getAvailableLocales();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.spi.LocaleServiceProvider
 * JD-Core Version:    0.6.2
 */