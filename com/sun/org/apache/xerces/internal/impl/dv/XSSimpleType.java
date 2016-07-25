package com.sun.org.apache.xerces.internal.impl.dv;

import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;

public abstract interface XSSimpleType extends XSSimpleTypeDefinition
{
  public static final short WS_PRESERVE = 0;
  public static final short WS_REPLACE = 1;
  public static final short WS_COLLAPSE = 2;
  public static final short PRIMITIVE_STRING = 1;
  public static final short PRIMITIVE_BOOLEAN = 2;
  public static final short PRIMITIVE_DECIMAL = 3;
  public static final short PRIMITIVE_FLOAT = 4;
  public static final short PRIMITIVE_DOUBLE = 5;
  public static final short PRIMITIVE_DURATION = 6;
  public static final short PRIMITIVE_DATETIME = 7;
  public static final short PRIMITIVE_TIME = 8;
  public static final short PRIMITIVE_DATE = 9;
  public static final short PRIMITIVE_GYEARMONTH = 10;
  public static final short PRIMITIVE_GYEAR = 11;
  public static final short PRIMITIVE_GMONTHDAY = 12;
  public static final short PRIMITIVE_GDAY = 13;
  public static final short PRIMITIVE_GMONTH = 14;
  public static final short PRIMITIVE_HEXBINARY = 15;
  public static final short PRIMITIVE_BASE64BINARY = 16;
  public static final short PRIMITIVE_ANYURI = 17;
  public static final short PRIMITIVE_QNAME = 18;
  public static final short PRIMITIVE_PRECISIONDECIMAL = 19;
  public static final short PRIMITIVE_NOTATION = 20;

  public abstract short getPrimitiveKind();

  public abstract Object validate(String paramString, ValidationContext paramValidationContext, ValidatedInfo paramValidatedInfo)
    throws InvalidDatatypeValueException;

  public abstract Object validate(Object paramObject, ValidationContext paramValidationContext, ValidatedInfo paramValidatedInfo)
    throws InvalidDatatypeValueException;

  public abstract void validate(ValidationContext paramValidationContext, ValidatedInfo paramValidatedInfo)
    throws InvalidDatatypeValueException;

  public abstract void applyFacets(XSFacets paramXSFacets, short paramShort1, short paramShort2, ValidationContext paramValidationContext)
    throws InvalidDatatypeFacetException;

  public abstract boolean isEqual(Object paramObject1, Object paramObject2);

  public abstract boolean isIDType();

  public abstract short getWhitespace()
    throws DatatypeException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
 * JD-Core Version:    0.6.2
 */