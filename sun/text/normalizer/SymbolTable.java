package sun.text.normalizer;

import java.text.ParsePosition;

/** @deprecated */
public abstract interface SymbolTable
{

  /** @deprecated */
  public static final char SYMBOL_REF = '$';

  /** @deprecated */
  public abstract char[] lookup(String paramString);

  /** @deprecated */
  public abstract UnicodeMatcher lookupMatcher(int paramInt);

  /** @deprecated */
  public abstract String parseReference(String paramString, ParsePosition paramParsePosition, int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.SymbolTable
 * JD-Core Version:    0.6.2
 */