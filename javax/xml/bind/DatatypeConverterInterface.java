package javax.xml.bind;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

public abstract interface DatatypeConverterInterface
{
  public abstract String parseString(String paramString);

  public abstract BigInteger parseInteger(String paramString);

  public abstract int parseInt(String paramString);

  public abstract long parseLong(String paramString);

  public abstract short parseShort(String paramString);

  public abstract BigDecimal parseDecimal(String paramString);

  public abstract float parseFloat(String paramString);

  public abstract double parseDouble(String paramString);

  public abstract boolean parseBoolean(String paramString);

  public abstract byte parseByte(String paramString);

  public abstract QName parseQName(String paramString, NamespaceContext paramNamespaceContext);

  public abstract Calendar parseDateTime(String paramString);

  public abstract byte[] parseBase64Binary(String paramString);

  public abstract byte[] parseHexBinary(String paramString);

  public abstract long parseUnsignedInt(String paramString);

  public abstract int parseUnsignedShort(String paramString);

  public abstract Calendar parseTime(String paramString);

  public abstract Calendar parseDate(String paramString);

  public abstract String parseAnySimpleType(String paramString);

  public abstract String printString(String paramString);

  public abstract String printInteger(BigInteger paramBigInteger);

  public abstract String printInt(int paramInt);

  public abstract String printLong(long paramLong);

  public abstract String printShort(short paramShort);

  public abstract String printDecimal(BigDecimal paramBigDecimal);

  public abstract String printFloat(float paramFloat);

  public abstract String printDouble(double paramDouble);

  public abstract String printBoolean(boolean paramBoolean);

  public abstract String printByte(byte paramByte);

  public abstract String printQName(QName paramQName, NamespaceContext paramNamespaceContext);

  public abstract String printDateTime(Calendar paramCalendar);

  public abstract String printBase64Binary(byte[] paramArrayOfByte);

  public abstract String printHexBinary(byte[] paramArrayOfByte);

  public abstract String printUnsignedInt(long paramLong);

  public abstract String printUnsignedShort(int paramInt);

  public abstract String printTime(Calendar paramCalendar);

  public abstract String printDate(Calendar paramCalendar);

  public abstract String printAnySimpleType(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.DatatypeConverterInterface
 * JD-Core Version:    0.6.2
 */