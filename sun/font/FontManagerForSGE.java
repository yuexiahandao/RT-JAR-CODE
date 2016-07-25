package sun.font;

import java.awt.Font;
import java.util.Locale;
import java.util.TreeMap;

public abstract interface FontManagerForSGE extends FontManager
{
  public abstract Font[] getCreatedFonts();

  public abstract TreeMap<String, String> getCreatedFontFamilyNames();

  public abstract Font[] getAllInstalledFonts();

  public abstract String[] getInstalledFontFamilyNames(Locale paramLocale);

  public abstract void useAlternateFontforJALocales();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FontManagerForSGE
 * JD-Core Version:    0.6.2
 */