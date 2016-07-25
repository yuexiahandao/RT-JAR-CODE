package java.text.spi;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.spi.LocaleServiceProvider;

public abstract class DateFormatSymbolsProvider extends LocaleServiceProvider
{
  public abstract DateFormatSymbols getInstance(Locale paramLocale);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.spi.DateFormatSymbolsProvider
 * JD-Core Version:    0.6.2
 */