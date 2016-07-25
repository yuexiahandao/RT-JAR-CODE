package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSSimpleTypeDefinition extends XSTypeDefinition
{
  public static final short VARIETY_ABSENT = 0;
  public static final short VARIETY_ATOMIC = 1;
  public static final short VARIETY_LIST = 2;
  public static final short VARIETY_UNION = 3;
  public static final short FACET_NONE = 0;
  public static final short FACET_LENGTH = 1;
  public static final short FACET_MINLENGTH = 2;
  public static final short FACET_MAXLENGTH = 4;
  public static final short FACET_PATTERN = 8;
  public static final short FACET_WHITESPACE = 16;
  public static final short FACET_MAXINCLUSIVE = 32;
  public static final short FACET_MAXEXCLUSIVE = 64;
  public static final short FACET_MINEXCLUSIVE = 128;
  public static final short FACET_MININCLUSIVE = 256;
  public static final short FACET_TOTALDIGITS = 512;
  public static final short FACET_FRACTIONDIGITS = 1024;
  public static final short FACET_ENUMERATION = 2048;
  public static final short ORDERED_FALSE = 0;
  public static final short ORDERED_PARTIAL = 1;
  public static final short ORDERED_TOTAL = 2;

  public abstract short getVariety();

  public abstract XSSimpleTypeDefinition getPrimitiveType();

  public abstract short getBuiltInKind();

  public abstract XSSimpleTypeDefinition getItemType();

  public abstract XSObjectList getMemberTypes();

  public abstract short getDefinedFacets();

  public abstract boolean isDefinedFacet(short paramShort);

  public abstract short getFixedFacets();

  public abstract boolean isFixedFacet(short paramShort);

  public abstract String getLexicalFacetValue(short paramShort);

  public abstract StringList getLexicalEnumeration();

  public abstract StringList getLexicalPattern();

  public abstract short getOrdered();

  public abstract boolean getFinite();

  public abstract boolean getBounded();

  public abstract boolean getNumeric();

  public abstract XSObjectList getFacets();

  public abstract XSObjectList getMultiValueFacets();

  public abstract XSObjectList getAnnotations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
 * JD-Core Version:    0.6.2
 */