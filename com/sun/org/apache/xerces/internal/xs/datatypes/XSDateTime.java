package com.sun.org.apache.xerces.internal.xs.datatypes;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

public abstract interface XSDateTime
{
  public abstract int getYears();

  public abstract int getMonths();

  public abstract int getDays();

  public abstract int getHours();

  public abstract int getMinutes();

  public abstract double getSeconds();

  public abstract boolean hasTimeZone();

  public abstract int getTimeZoneHours();

  public abstract int getTimeZoneMinutes();

  public abstract String getLexicalValue();

  public abstract XSDateTime normalize();

  public abstract boolean isNormalized();

  public abstract XMLGregorianCalendar getXMLGregorianCalendar();

  public abstract Duration getDuration();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
 * JD-Core Version:    0.6.2
 */