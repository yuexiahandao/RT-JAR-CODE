package java.text.spi;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.spi.LocaleServiceProvider;

public abstract class NumberFormatProvider extends LocaleServiceProvider
{
  public abstract NumberFormat getCurrencyInstance(Locale paramLocale);

  public abstract NumberFormat getIntegerInstance(Locale paramLocale);

  public abstract NumberFormat getNumberInstance(Locale paramLocale);

  public abstract NumberFormat getPercentInstance(Locale paramLocale);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.spi.NumberFormatProvider
 * JD-Core Version:    0.6.2
 */