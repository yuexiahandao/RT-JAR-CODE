package java.util.spi;

import java.util.Locale;

public abstract class TimeZoneNameProvider extends LocaleServiceProvider
{
  public abstract String getDisplayName(String paramString, boolean paramBoolean, int paramInt, Locale paramLocale);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.spi.TimeZoneNameProvider
 * JD-Core Version:    0.6.2
 */