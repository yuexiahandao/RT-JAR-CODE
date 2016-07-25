/*      */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeException;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.ShortListImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*      */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSFacet;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSMultiValueFacet;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*      */ import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
/*      */ import java.util.AbstractList;
/*      */ import java.util.Locale;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.TypeInfo;
/*      */ 
/*      */ public class XSSimpleTypeDecl
/*      */   implements XSSimpleType, TypeInfo
/*      */ {
/*      */   protected static final short DV_STRING = 1;
/*      */   protected static final short DV_BOOLEAN = 2;
/*      */   protected static final short DV_DECIMAL = 3;
/*      */   protected static final short DV_FLOAT = 4;
/*      */   protected static final short DV_DOUBLE = 5;
/*      */   protected static final short DV_DURATION = 6;
/*      */   protected static final short DV_DATETIME = 7;
/*      */   protected static final short DV_TIME = 8;
/*      */   protected static final short DV_DATE = 9;
/*      */   protected static final short DV_GYEARMONTH = 10;
/*      */   protected static final short DV_GYEAR = 11;
/*      */   protected static final short DV_GMONTHDAY = 12;
/*      */   protected static final short DV_GDAY = 13;
/*      */   protected static final short DV_GMONTH = 14;
/*      */   protected static final short DV_HEXBINARY = 15;
/*      */   protected static final short DV_BASE64BINARY = 16;
/*      */   protected static final short DV_ANYURI = 17;
/*      */   protected static final short DV_QNAME = 18;
/*      */   protected static final short DV_PRECISIONDECIMAL = 19;
/*      */   protected static final short DV_NOTATION = 20;
/*      */   protected static final short DV_ANYSIMPLETYPE = 0;
/*      */   protected static final short DV_ID = 21;
/*      */   protected static final short DV_IDREF = 22;
/*      */   protected static final short DV_ENTITY = 23;
/*      */   protected static final short DV_INTEGER = 24;
/*      */   protected static final short DV_LIST = 25;
/*      */   protected static final short DV_UNION = 26;
/*      */   protected static final short DV_YEARMONTHDURATION = 27;
/*      */   protected static final short DV_DAYTIMEDURATION = 28;
/*      */   protected static final short DV_ANYATOMICTYPE = 29;
/*   99 */   private static final TypeValidator[] gDVs = { new AnySimpleDV(), new StringDV(), new BooleanDV(), new DecimalDV(), new FloatDV(), new DoubleDV(), new DurationDV(), new DateTimeDV(), new TimeDV(), new DateDV(), new YearMonthDV(), new YearDV(), new MonthDayDV(), new DayDV(), new MonthDV(), new HexBinaryDV(), new Base64BinaryDV(), new AnyURIDV(), new QNameDV(), new PrecisionDecimalDV(), new QNameDV(), new IDDV(), new IDREFDV(), new EntityDV(), new IntegerDV(), new ListDV(), new UnionDV(), new YearMonthDurationDV(), new DayTimeDurationDV(), new AnyAtomicDV() };
/*      */   static final short NORMALIZE_NONE = 0;
/*      */   static final short NORMALIZE_TRIM = 1;
/*      */   static final short NORMALIZE_FULL = 2;
/*  135 */   static final short[] fDVNormalizeType = { 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 1, 1, 0 };
/*      */   static final short SPECIAL_PATTERN_NONE = 0;
/*      */   static final short SPECIAL_PATTERN_NMTOKEN = 1;
/*      */   static final short SPECIAL_PATTERN_NAME = 2;
/*      */   static final short SPECIAL_PATTERN_NCNAME = 3;
/*  173 */   static final String[] SPECIAL_PATTERN_STRING = { "NONE", "NMTOKEN", "Name", "NCName" };
/*      */ 
/*  177 */   static final String[] WS_FACET_STRING = { "preserve", "replace", "collapse" };
/*      */   static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
/*      */   static final String ANY_TYPE = "anyType";
/*      */   public static final short YEARMONTHDURATION_DT = 46;
/*      */   public static final short DAYTIMEDURATION_DT = 47;
/*      */   public static final short PRECISIONDECIMAL_DT = 48;
/*      */   public static final short ANYATOMICTYPE_DT = 49;
/*      */   static final int DERIVATION_ANY = 0;
/*      */   static final int DERIVATION_RESTRICTION = 1;
/*      */   static final int DERIVATION_EXTENSION = 2;
/*      */   static final int DERIVATION_UNION = 4;
/*      */   static final int DERIVATION_LIST = 8;
/*  197 */   static final ValidationContext fEmptyContext = new ValidationContext() {
/*      */     public boolean needFacetChecking() {
/*  199 */       return true;
/*      */     }
/*      */     public boolean needExtraChecking() {
/*  202 */       return false;
/*      */     }
/*      */     public boolean needToNormalize() {
/*  205 */       return true;
/*      */     }
/*      */     public boolean useNamespaces() {
/*  208 */       return true;
/*      */     }
/*      */     public boolean isEntityDeclared(String name) {
/*  211 */       return false;
/*      */     }
/*      */     public boolean isEntityUnparsed(String name) {
/*  214 */       return false;
/*      */     }
/*      */     public boolean isIdDeclared(String name) {
/*  217 */       return false;
/*      */     }
/*      */     public void addId(String name) {
/*      */     }
/*      */     public void addIdRef(String name) {
/*      */     }
/*      */     public String getSymbol(String symbol) {
/*  224 */       return symbol.intern();
/*      */     }
/*      */     public String getURI(String prefix) {
/*  227 */       return null;
/*      */     }
/*      */     public Locale getLocale() {
/*  230 */       return Locale.getDefault();
/*      */     }
/*  197 */   };
/*      */ 
/*  237 */   private TypeValidator[] fDVs = gDVs;
/*      */ 
/*  245 */   private boolean fIsImmutable = false;
/*      */   private XSSimpleTypeDecl fItemType;
/*      */   private XSSimpleTypeDecl[] fMemberTypes;
/*      */   private short fBuiltInKind;
/*      */   private String fTypeName;
/*      */   private String fTargetNamespace;
/*  254 */   private short fFinalSet = 0;
/*      */   private XSSimpleTypeDecl fBase;
/*  256 */   private short fVariety = -1;
/*  257 */   private short fValidationDV = -1;
/*      */ 
/*  259 */   private short fFacetsDefined = 0;
/*  260 */   private short fFixedFacet = 0;
/*      */ 
/*  263 */   private short fWhiteSpace = 0;
/*  264 */   private int fLength = -1;
/*  265 */   private int fMinLength = -1;
/*  266 */   private int fMaxLength = -1;
/*  267 */   private int fTotalDigits = -1;
/*  268 */   private int fFractionDigits = -1;
/*      */   private Vector fPattern;
/*      */   private Vector fPatternStr;
/*      */   private Vector fEnumeration;
/*      */   private short[] fEnumerationType;
/*      */   private ShortList[] fEnumerationItemType;
/*      */   private ShortList fEnumerationTypeList;
/*      */   private ObjectList fEnumerationItemTypeList;
/*      */   private StringList fLexicalPattern;
/*      */   private StringList fLexicalEnumeration;
/*      */   private ObjectList fActualEnumeration;
/*      */   private Object fMaxInclusive;
/*      */   private Object fMaxExclusive;
/*      */   private Object fMinExclusive;
/*      */   private Object fMinInclusive;
/*      */   public XSAnnotation lengthAnnotation;
/*      */   public XSAnnotation minLengthAnnotation;
/*      */   public XSAnnotation maxLengthAnnotation;
/*      */   public XSAnnotation whiteSpaceAnnotation;
/*      */   public XSAnnotation totalDigitsAnnotation;
/*      */   public XSAnnotation fractionDigitsAnnotation;
/*      */   public XSObjectListImpl patternAnnotations;
/*      */   public XSObjectList enumerationAnnotations;
/*      */   public XSAnnotation maxInclusiveAnnotation;
/*      */   public XSAnnotation maxExclusiveAnnotation;
/*      */   public XSAnnotation minInclusiveAnnotation;
/*      */   public XSAnnotation minExclusiveAnnotation;
/*      */   private XSObjectListImpl fFacets;
/*      */   private XSObjectListImpl fMultiValueFacets;
/*  305 */   private XSObjectList fAnnotations = null;
/*      */ 
/*  307 */   private short fPatternType = 0;
/*      */   private short fOrdered;
/*      */   private boolean fFinite;
/*      */   private boolean fBounded;
/*      */   private boolean fNumeric;
/*  317 */   private XSNamespaceItem fNamespaceItem = null;
/*      */ 
/* 2830 */   static final XSSimpleTypeDecl fAnySimpleType = new XSSimpleTypeDecl(null, "anySimpleType", (short)0, (short)0, false, true, false, true, (short)1);
/*      */ 
/* 2832 */   static final XSSimpleTypeDecl fAnyAtomicType = new XSSimpleTypeDecl(fAnySimpleType, "anyAtomicType", (short)29, (short)0, false, true, false, true, (short)49);
/*      */ 
/* 2837 */   static final ValidationContext fDummyContext = new ValidationContext() {
/*      */     public boolean needFacetChecking() {
/* 2839 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean needExtraChecking() {
/* 2843 */       return false;
/*      */     }
/*      */     public boolean needToNormalize() {
/* 2846 */       return false;
/*      */     }
/*      */     public boolean useNamespaces() {
/* 2849 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isEntityDeclared(String name) {
/* 2853 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean isEntityUnparsed(String name) {
/* 2857 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean isIdDeclared(String name) {
/* 2861 */       return false;
/*      */     }
/*      */ 
/*      */     public void addId(String name) {
/*      */     }
/*      */ 
/*      */     public void addIdRef(String name) {
/*      */     }
/*      */ 
/*      */     public String getSymbol(String symbol) {
/* 2871 */       return symbol.intern();
/*      */     }
/*      */ 
/*      */     public String getURI(String prefix) {
/* 2875 */       return null;
/*      */     }
/*      */ 
/*      */     public Locale getLocale() {
/* 2879 */       return Locale.getDefault();
/*      */     }
/* 2837 */   };
/*      */ 
/* 2883 */   private boolean fAnonymous = false;
/*      */ 
/*      */   protected static TypeValidator[] getGDVs()
/*      */   {
/*  235 */     return (TypeValidator[])gDVs.clone();
/*      */   }
/*      */ 
/*      */   protected void setDVs(TypeValidator[] dvs) {
/*  239 */     this.fDVs = dvs;
/*      */   }
/*      */ 
/*      */   public XSSimpleTypeDecl()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected XSSimpleTypeDecl(XSSimpleTypeDecl base, String name, short validateDV, short ordered, boolean bounded, boolean finite, boolean numeric, boolean isImmutable, short builtInKind)
/*      */   {
/*  326 */     this.fIsImmutable = isImmutable;
/*  327 */     this.fBase = base;
/*  328 */     this.fTypeName = name;
/*  329 */     this.fTargetNamespace = "http://www.w3.org/2001/XMLSchema";
/*      */ 
/*  331 */     this.fVariety = 1;
/*  332 */     this.fValidationDV = validateDV;
/*  333 */     this.fFacetsDefined = 16;
/*  334 */     if ((validateDV == 0) || (validateDV == 29) || (validateDV == 1))
/*      */     {
/*  337 */       this.fWhiteSpace = 0;
/*      */     }
/*      */     else {
/*  340 */       this.fWhiteSpace = 2;
/*  341 */       this.fFixedFacet = 16;
/*      */     }
/*  343 */     this.fOrdered = ordered;
/*  344 */     this.fBounded = bounded;
/*  345 */     this.fFinite = finite;
/*  346 */     this.fNumeric = numeric;
/*  347 */     this.fAnnotations = null;
/*      */ 
/*  350 */     this.fBuiltInKind = builtInKind;
/*      */   }
/*      */ 
/*      */   protected XSSimpleTypeDecl(XSSimpleTypeDecl base, String name, String uri, short finalSet, boolean isImmutable, XSObjectList annotations, short builtInKind)
/*      */   {
/*  356 */     this(base, name, uri, finalSet, isImmutable, annotations);
/*      */ 
/*  358 */     this.fBuiltInKind = builtInKind;
/*      */   }
/*      */ 
/*      */   protected XSSimpleTypeDecl(XSSimpleTypeDecl base, String name, String uri, short finalSet, boolean isImmutable, XSObjectList annotations)
/*      */   {
/*  364 */     this.fBase = base;
/*  365 */     this.fTypeName = name;
/*  366 */     this.fTargetNamespace = uri;
/*  367 */     this.fFinalSet = finalSet;
/*  368 */     this.fAnnotations = annotations;
/*      */ 
/*  370 */     this.fVariety = this.fBase.fVariety;
/*  371 */     this.fValidationDV = this.fBase.fValidationDV;
/*  372 */     switch (this.fVariety) {
/*      */     case 1:
/*  374 */       break;
/*      */     case 2:
/*  376 */       this.fItemType = this.fBase.fItemType;
/*  377 */       break;
/*      */     case 3:
/*  379 */       this.fMemberTypes = this.fBase.fMemberTypes;
/*      */     }
/*      */ 
/*  385 */     this.fLength = this.fBase.fLength;
/*  386 */     this.fMinLength = this.fBase.fMinLength;
/*  387 */     this.fMaxLength = this.fBase.fMaxLength;
/*  388 */     this.fPattern = this.fBase.fPattern;
/*  389 */     this.fPatternStr = this.fBase.fPatternStr;
/*  390 */     this.fEnumeration = this.fBase.fEnumeration;
/*  391 */     this.fEnumerationType = this.fBase.fEnumerationType;
/*  392 */     this.fEnumerationItemType = this.fBase.fEnumerationItemType;
/*  393 */     this.fWhiteSpace = this.fBase.fWhiteSpace;
/*  394 */     this.fMaxExclusive = this.fBase.fMaxExclusive;
/*  395 */     this.fMaxInclusive = this.fBase.fMaxInclusive;
/*  396 */     this.fMinExclusive = this.fBase.fMinExclusive;
/*  397 */     this.fMinInclusive = this.fBase.fMinInclusive;
/*  398 */     this.fTotalDigits = this.fBase.fTotalDigits;
/*  399 */     this.fFractionDigits = this.fBase.fFractionDigits;
/*  400 */     this.fPatternType = this.fBase.fPatternType;
/*  401 */     this.fFixedFacet = this.fBase.fFixedFacet;
/*  402 */     this.fFacetsDefined = this.fBase.fFacetsDefined;
/*      */ 
/*  405 */     this.lengthAnnotation = this.fBase.lengthAnnotation;
/*  406 */     this.minLengthAnnotation = this.fBase.minLengthAnnotation;
/*  407 */     this.maxLengthAnnotation = this.fBase.maxLengthAnnotation;
/*  408 */     this.patternAnnotations = this.fBase.patternAnnotations;
/*  409 */     this.enumerationAnnotations = this.fBase.enumerationAnnotations;
/*  410 */     this.whiteSpaceAnnotation = this.fBase.whiteSpaceAnnotation;
/*  411 */     this.maxExclusiveAnnotation = this.fBase.maxExclusiveAnnotation;
/*  412 */     this.maxInclusiveAnnotation = this.fBase.maxInclusiveAnnotation;
/*  413 */     this.minExclusiveAnnotation = this.fBase.minExclusiveAnnotation;
/*  414 */     this.minInclusiveAnnotation = this.fBase.minInclusiveAnnotation;
/*  415 */     this.totalDigitsAnnotation = this.fBase.totalDigitsAnnotation;
/*  416 */     this.fractionDigitsAnnotation = this.fBase.fractionDigitsAnnotation;
/*      */ 
/*  419 */     calcFundamentalFacets();
/*  420 */     this.fIsImmutable = isImmutable;
/*      */ 
/*  423 */     this.fBuiltInKind = base.fBuiltInKind;
/*      */   }
/*      */ 
/*      */   protected XSSimpleTypeDecl(String name, String uri, short finalSet, XSSimpleTypeDecl itemType, boolean isImmutable, XSObjectList annotations)
/*      */   {
/*  429 */     this.fBase = fAnySimpleType;
/*  430 */     this.fTypeName = name;
/*  431 */     this.fTargetNamespace = uri;
/*  432 */     this.fFinalSet = finalSet;
/*  433 */     this.fAnnotations = annotations;
/*      */ 
/*  435 */     this.fVariety = 2;
/*  436 */     this.fItemType = itemType;
/*  437 */     this.fValidationDV = 25;
/*  438 */     this.fFacetsDefined = 16;
/*  439 */     this.fFixedFacet = 16;
/*  440 */     this.fWhiteSpace = 2;
/*      */ 
/*  443 */     calcFundamentalFacets();
/*  444 */     this.fIsImmutable = isImmutable;
/*      */ 
/*  447 */     this.fBuiltInKind = 44;
/*      */   }
/*      */ 
/*      */   protected XSSimpleTypeDecl(String name, String uri, short finalSet, XSSimpleTypeDecl[] memberTypes, XSObjectList annotations)
/*      */   {
/*  453 */     this.fBase = fAnySimpleType;
/*  454 */     this.fTypeName = name;
/*  455 */     this.fTargetNamespace = uri;
/*  456 */     this.fFinalSet = finalSet;
/*  457 */     this.fAnnotations = annotations;
/*      */ 
/*  459 */     this.fVariety = 3;
/*  460 */     this.fMemberTypes = memberTypes;
/*  461 */     this.fValidationDV = 26;
/*      */ 
/*  466 */     this.fFacetsDefined = 16;
/*  467 */     this.fWhiteSpace = 2;
/*      */ 
/*  470 */     calcFundamentalFacets();
/*      */ 
/*  473 */     this.fIsImmutable = false;
/*      */ 
/*  476 */     this.fBuiltInKind = 45;
/*      */   }
/*      */ 
/*      */   protected XSSimpleTypeDecl setRestrictionValues(XSSimpleTypeDecl base, String name, String uri, short finalSet, XSObjectList annotations)
/*      */   {
/*  483 */     if (this.fIsImmutable) return null;
/*  484 */     this.fBase = base;
/*  485 */     this.fAnonymous = false;
/*  486 */     this.fTypeName = name;
/*  487 */     this.fTargetNamespace = uri;
/*  488 */     this.fFinalSet = finalSet;
/*  489 */     this.fAnnotations = annotations;
/*      */ 
/*  491 */     this.fVariety = this.fBase.fVariety;
/*  492 */     this.fValidationDV = this.fBase.fValidationDV;
/*  493 */     switch (this.fVariety) {
/*      */     case 1:
/*  495 */       break;
/*      */     case 2:
/*  497 */       this.fItemType = this.fBase.fItemType;
/*  498 */       break;
/*      */     case 3:
/*  500 */       this.fMemberTypes = this.fBase.fMemberTypes;
/*      */     }
/*      */ 
/*  506 */     this.fLength = this.fBase.fLength;
/*  507 */     this.fMinLength = this.fBase.fMinLength;
/*  508 */     this.fMaxLength = this.fBase.fMaxLength;
/*  509 */     this.fPattern = this.fBase.fPattern;
/*  510 */     this.fPatternStr = this.fBase.fPatternStr;
/*  511 */     this.fEnumeration = this.fBase.fEnumeration;
/*  512 */     this.fEnumerationType = this.fBase.fEnumerationType;
/*  513 */     this.fEnumerationItemType = this.fBase.fEnumerationItemType;
/*  514 */     this.fWhiteSpace = this.fBase.fWhiteSpace;
/*  515 */     this.fMaxExclusive = this.fBase.fMaxExclusive;
/*  516 */     this.fMaxInclusive = this.fBase.fMaxInclusive;
/*  517 */     this.fMinExclusive = this.fBase.fMinExclusive;
/*  518 */     this.fMinInclusive = this.fBase.fMinInclusive;
/*  519 */     this.fTotalDigits = this.fBase.fTotalDigits;
/*  520 */     this.fFractionDigits = this.fBase.fFractionDigits;
/*  521 */     this.fPatternType = this.fBase.fPatternType;
/*  522 */     this.fFixedFacet = this.fBase.fFixedFacet;
/*  523 */     this.fFacetsDefined = this.fBase.fFacetsDefined;
/*      */ 
/*  526 */     calcFundamentalFacets();
/*      */ 
/*  529 */     this.fBuiltInKind = base.fBuiltInKind;
/*      */ 
/*  531 */     return this;
/*      */   }
/*      */ 
/*      */   protected XSSimpleTypeDecl setListValues(String name, String uri, short finalSet, XSSimpleTypeDecl itemType, XSObjectList annotations)
/*      */   {
/*  538 */     if (this.fIsImmutable) return null;
/*  539 */     this.fBase = fAnySimpleType;
/*  540 */     this.fAnonymous = false;
/*  541 */     this.fTypeName = name;
/*  542 */     this.fTargetNamespace = uri;
/*  543 */     this.fFinalSet = finalSet;
/*  544 */     this.fAnnotations = annotations;
/*      */ 
/*  546 */     this.fVariety = 2;
/*  547 */     this.fItemType = itemType;
/*  548 */     this.fValidationDV = 25;
/*  549 */     this.fFacetsDefined = 16;
/*  550 */     this.fFixedFacet = 16;
/*  551 */     this.fWhiteSpace = 2;
/*      */ 
/*  554 */     calcFundamentalFacets();
/*      */ 
/*  557 */     this.fBuiltInKind = 44;
/*      */ 
/*  559 */     return this;
/*      */   }
/*      */ 
/*      */   protected XSSimpleTypeDecl setUnionValues(String name, String uri, short finalSet, XSSimpleTypeDecl[] memberTypes, XSObjectList annotations)
/*      */   {
/*  566 */     if (this.fIsImmutable) return null;
/*  567 */     this.fBase = fAnySimpleType;
/*  568 */     this.fAnonymous = false;
/*  569 */     this.fTypeName = name;
/*  570 */     this.fTargetNamespace = uri;
/*  571 */     this.fFinalSet = finalSet;
/*  572 */     this.fAnnotations = annotations;
/*      */ 
/*  574 */     this.fVariety = 3;
/*  575 */     this.fMemberTypes = memberTypes;
/*  576 */     this.fValidationDV = 26;
/*      */ 
/*  581 */     this.fFacetsDefined = 16;
/*  582 */     this.fWhiteSpace = 2;
/*      */ 
/*  585 */     calcFundamentalFacets();
/*      */ 
/*  588 */     this.fBuiltInKind = 45;
/*      */ 
/*  590 */     return this;
/*      */   }
/*      */ 
/*      */   public short getType() {
/*  594 */     return 3;
/*      */   }
/*      */ 
/*      */   public short getTypeCategory() {
/*  598 */     return 16;
/*      */   }
/*      */ 
/*      */   public String getName() {
/*  602 */     return getAnonymous() ? null : this.fTypeName;
/*      */   }
/*      */ 
/*      */   public String getTypeName() {
/*  606 */     return this.fTypeName;
/*      */   }
/*      */ 
/*      */   public String getNamespace() {
/*  610 */     return this.fTargetNamespace;
/*      */   }
/*      */ 
/*      */   public short getFinal() {
/*  614 */     return this.fFinalSet;
/*      */   }
/*      */ 
/*      */   public boolean isFinal(short derivation) {
/*  618 */     return (this.fFinalSet & derivation) != 0;
/*      */   }
/*      */ 
/*      */   public XSTypeDefinition getBaseType() {
/*  622 */     return this.fBase;
/*      */   }
/*      */ 
/*      */   public boolean getAnonymous() {
/*  626 */     return (this.fAnonymous) || (this.fTypeName == null);
/*      */   }
/*      */ 
/*      */   public short getVariety()
/*      */   {
/*  631 */     return this.fValidationDV == 0 ? 0 : this.fVariety;
/*      */   }
/*      */ 
/*      */   public boolean isIDType() {
/*  635 */     switch (this.fVariety) {
/*      */     case 1:
/*  637 */       return this.fValidationDV == 21;
/*      */     case 2:
/*  639 */       return this.fItemType.isIDType();
/*      */     case 3:
/*  641 */       for (int i = 0; i < this.fMemberTypes.length; i++) {
/*  642 */         if (this.fMemberTypes[i].isIDType())
/*  643 */           return true;
/*      */       }
/*      */     }
/*  646 */     return false;
/*      */   }
/*      */ 
/*      */   public short getWhitespace() throws DatatypeException {
/*  650 */     if (this.fVariety == 3) {
/*  651 */       throw new DatatypeException("dt-whitespace", new Object[] { this.fTypeName });
/*      */     }
/*  653 */     return this.fWhiteSpace;
/*      */   }
/*      */ 
/*      */   public short getPrimitiveKind() {
/*  657 */     if ((this.fVariety == 1) && (this.fValidationDV != 0)) {
/*  658 */       if ((this.fValidationDV == 21) || (this.fValidationDV == 22) || (this.fValidationDV == 23)) {
/*  659 */         return 1;
/*      */       }
/*  661 */       if (this.fValidationDV == 24) {
/*  662 */         return 3;
/*      */       }
/*      */ 
/*  668 */       return this.fValidationDV;
/*      */     }
/*      */ 
/*  673 */     return 0;
/*      */   }
/*      */ 
/*      */   public short getBuiltInKind()
/*      */   {
/*  683 */     return this.fBuiltInKind;
/*      */   }
/*      */ 
/*      */   public XSSimpleTypeDefinition getPrimitiveType()
/*      */   {
/*  692 */     if ((this.fVariety == 1) && (this.fValidationDV != 0)) {
/*  693 */       XSSimpleTypeDecl pri = this;
/*      */ 
/*  695 */       while (pri.fBase != fAnySimpleType)
/*  696 */         pri = pri.fBase;
/*  697 */       return pri;
/*      */     }
/*      */ 
/*  701 */     return null;
/*      */   }
/*      */ 
/*      */   public XSSimpleTypeDefinition getItemType()
/*      */   {
/*  711 */     if (this.fVariety == 2) {
/*  712 */       return this.fItemType;
/*      */     }
/*      */ 
/*  716 */     return null;
/*      */   }
/*      */ 
/*      */   public XSObjectList getMemberTypes()
/*      */   {
/*  726 */     if (this.fVariety == 3) {
/*  727 */       return new XSObjectListImpl(this.fMemberTypes, this.fMemberTypes.length);
/*      */     }
/*      */ 
/*  730 */     return XSObjectListImpl.EMPTY_LIST;
/*      */   }
/*      */ 
/*      */   public void applyFacets(XSFacets facets, short presentFacet, short fixedFacet, ValidationContext context)
/*      */     throws InvalidDatatypeFacetException
/*      */   {
/*  739 */     if (context == null) {
/*  740 */       context = fEmptyContext;
/*      */     }
/*  742 */     applyFacets(facets, presentFacet, fixedFacet, (short)0, context);
/*      */   }
/*      */ 
/*      */   void applyFacets1(XSFacets facets, short presentFacet, short fixedFacet)
/*      */   {
/*      */     try
/*      */     {
/*  751 */       applyFacets(facets, presentFacet, fixedFacet, (short)0, fDummyContext);
/*      */     }
/*      */     catch (InvalidDatatypeFacetException e) {
/*  754 */       throw new RuntimeException("internal error");
/*      */     }
/*      */ 
/*  757 */     this.fIsImmutable = true;
/*      */   }
/*      */ 
/*      */   void applyFacets1(XSFacets facets, short presentFacet, short fixedFacet, short patternType)
/*      */   {
/*      */     try
/*      */     {
/*  766 */       applyFacets(facets, presentFacet, fixedFacet, patternType, fDummyContext);
/*      */     }
/*      */     catch (InvalidDatatypeFacetException e) {
/*  769 */       throw new RuntimeException("internal error");
/*      */     }
/*      */ 
/*  772 */     this.fIsImmutable = true;
/*      */   }
/*      */ 
/*      */   void applyFacets(XSFacets facets, short presentFacet, short fixedFacet, short patternType, ValidationContext context)
/*      */     throws InvalidDatatypeFacetException
/*      */   {
/*  782 */     if (this.fIsImmutable) return;
/*  783 */     ValidatedInfo tempInfo = new ValidatedInfo();
/*      */ 
/*  792 */     this.fFacetsDefined = 0;
/*  793 */     this.fFixedFacet = 0;
/*      */ 
/*  795 */     int result = 0;
/*      */ 
/*  798 */     short allowedFacet = this.fDVs[this.fValidationDV].getAllowedFacets();
/*      */ 
/*  801 */     if ((presentFacet & 0x1) != 0) {
/*  802 */       if ((allowedFacet & 0x1) == 0) {
/*  803 */         reportError("cos-applicable-facets", new Object[] { "length", this.fTypeName });
/*      */       } else {
/*  805 */         this.fLength = facets.length;
/*  806 */         this.lengthAnnotation = facets.lengthAnnotation;
/*  807 */         this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x1));
/*  808 */         if ((fixedFacet & 0x1) != 0) {
/*  809 */           this.fFixedFacet = ((short)(this.fFixedFacet | 0x1));
/*      */         }
/*      */       }
/*      */     }
/*  813 */     if ((presentFacet & 0x2) != 0) {
/*  814 */       if ((allowedFacet & 0x2) == 0) {
/*  815 */         reportError("cos-applicable-facets", new Object[] { "minLength", this.fTypeName });
/*      */       } else {
/*  817 */         this.fMinLength = facets.minLength;
/*  818 */         this.minLengthAnnotation = facets.minLengthAnnotation;
/*  819 */         this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x2));
/*  820 */         if ((fixedFacet & 0x2) != 0) {
/*  821 */           this.fFixedFacet = ((short)(this.fFixedFacet | 0x2));
/*      */         }
/*      */       }
/*      */     }
/*  825 */     if ((presentFacet & 0x4) != 0) {
/*  826 */       if ((allowedFacet & 0x4) == 0) {
/*  827 */         reportError("cos-applicable-facets", new Object[] { "maxLength", this.fTypeName });
/*      */       } else {
/*  829 */         this.fMaxLength = facets.maxLength;
/*  830 */         this.maxLengthAnnotation = facets.maxLengthAnnotation;
/*  831 */         this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x4));
/*  832 */         if ((fixedFacet & 0x4) != 0) {
/*  833 */           this.fFixedFacet = ((short)(this.fFixedFacet | 0x4));
/*      */         }
/*      */       }
/*      */     }
/*  837 */     if ((presentFacet & 0x8) != 0) {
/*  838 */       if ((allowedFacet & 0x8) == 0) {
/*  839 */         reportError("cos-applicable-facets", new Object[] { "pattern", this.fTypeName });
/*      */       } else {
/*  841 */         this.patternAnnotations = facets.patternAnnotations;
/*  842 */         RegularExpression regex = null;
/*      */         try {
/*  844 */           regex = new RegularExpression(facets.pattern, "X", context.getLocale());
/*      */         } catch (Exception e) {
/*  846 */           reportError("InvalidRegex", new Object[] { facets.pattern, e.getLocalizedMessage() });
/*      */         }
/*  848 */         if (regex != null) {
/*  849 */           this.fPattern = new Vector();
/*  850 */           this.fPattern.addElement(regex);
/*  851 */           this.fPatternStr = new Vector();
/*  852 */           this.fPatternStr.addElement(facets.pattern);
/*  853 */           this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x8));
/*  854 */           if ((fixedFacet & 0x8) != 0) {
/*  855 */             this.fFixedFacet = ((short)(this.fFixedFacet | 0x8));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  861 */     if ((presentFacet & 0x10) != 0) {
/*  862 */       if ((allowedFacet & 0x10) == 0) {
/*  863 */         reportError("cos-applicable-facets", new Object[] { "whiteSpace", this.fTypeName });
/*      */       } else {
/*  865 */         this.fWhiteSpace = facets.whiteSpace;
/*  866 */         this.whiteSpaceAnnotation = facets.whiteSpaceAnnotation;
/*  867 */         this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x10));
/*  868 */         if ((fixedFacet & 0x10) != 0) {
/*  869 */           this.fFixedFacet = ((short)(this.fFixedFacet | 0x10));
/*      */         }
/*      */       }
/*      */     }
/*  873 */     if ((presentFacet & 0x800) != 0) {
/*  874 */       if ((allowedFacet & 0x800) == 0) {
/*  875 */         reportError("cos-applicable-facets", new Object[] { "enumeration", this.fTypeName });
/*      */       } else {
/*  877 */         this.fEnumeration = new Vector();
/*  878 */         Vector enumVals = facets.enumeration;
/*  879 */         this.fEnumerationType = new short[enumVals.size()];
/*  880 */         this.fEnumerationItemType = new ShortList[enumVals.size()];
/*  881 */         Vector enumNSDecls = facets.enumNSDecls;
/*  882 */         ValidationContextImpl ctx = new ValidationContextImpl(context);
/*  883 */         this.enumerationAnnotations = facets.enumAnnotations;
/*  884 */         for (int i = 0; i < enumVals.size(); i++) {
/*  885 */           if (enumNSDecls != null)
/*  886 */             ctx.setNSContext((NamespaceContext)enumNSDecls.elementAt(i));
/*      */           try {
/*  888 */             ValidatedInfo info = getActualEnumValue((String)enumVals.elementAt(i), ctx, tempInfo);
/*      */ 
/*  890 */             this.fEnumeration.addElement(info.actualValue);
/*  891 */             this.fEnumerationType[i] = info.actualValueType;
/*  892 */             this.fEnumerationItemType[i] = info.itemValueTypes;
/*      */           } catch (InvalidDatatypeValueException ide) {
/*  894 */             reportError("enumeration-valid-restriction", new Object[] { enumVals.elementAt(i), getBaseType().getName() });
/*      */           }
/*      */         }
/*  897 */         this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x800));
/*  898 */         if ((fixedFacet & 0x800) != 0) {
/*  899 */           this.fFixedFacet = ((short)(this.fFixedFacet | 0x800));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  904 */     if ((presentFacet & 0x20) != 0) {
/*  905 */       if ((allowedFacet & 0x20) == 0) {
/*  906 */         reportError("cos-applicable-facets", new Object[] { "maxInclusive", this.fTypeName });
/*      */       } else {
/*  908 */         this.maxInclusiveAnnotation = facets.maxInclusiveAnnotation;
/*      */         try {
/*  910 */           this.fMaxInclusive = this.fBase.getActualValue(facets.maxInclusive, context, tempInfo, true);
/*  911 */           this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x20));
/*  912 */           if ((fixedFacet & 0x20) != 0)
/*  913 */             this.fFixedFacet = ((short)(this.fFixedFacet | 0x20));
/*      */         } catch (InvalidDatatypeValueException ide) {
/*  915 */           reportError(ide.getKey(), ide.getArgs());
/*  916 */           reportError("FacetValueFromBase", new Object[] { this.fTypeName, facets.maxInclusive, "maxInclusive", this.fBase.getName() });
/*      */         }
/*      */ 
/*  921 */         if (((this.fBase.fFacetsDefined & 0x20) != 0) && 
/*  922 */           ((this.fBase.fFixedFacet & 0x20) != 0) && 
/*  923 */           (this.fDVs[this.fValidationDV].compare(this.fMaxInclusive, this.fBase.fMaxInclusive) != 0)) {
/*  924 */           reportError("FixedFacetValue", new Object[] { "maxInclusive", this.fMaxInclusive, this.fBase.fMaxInclusive, this.fTypeName });
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  929 */           this.fBase.validate(context, tempInfo);
/*      */         } catch (InvalidDatatypeValueException ide) {
/*  931 */           reportError(ide.getKey(), ide.getArgs());
/*  932 */           reportError("FacetValueFromBase", new Object[] { this.fTypeName, facets.maxInclusive, "maxInclusive", this.fBase.getName() });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  939 */     boolean needCheckBase = true;
/*  940 */     if ((presentFacet & 0x40) != 0) {
/*  941 */       if ((allowedFacet & 0x40) == 0) {
/*  942 */         reportError("cos-applicable-facets", new Object[] { "maxExclusive", this.fTypeName });
/*      */       } else {
/*  944 */         this.maxExclusiveAnnotation = facets.maxExclusiveAnnotation;
/*      */         try {
/*  946 */           this.fMaxExclusive = this.fBase.getActualValue(facets.maxExclusive, context, tempInfo, true);
/*  947 */           this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x40));
/*  948 */           if ((fixedFacet & 0x40) != 0)
/*  949 */             this.fFixedFacet = ((short)(this.fFixedFacet | 0x40));
/*      */         } catch (InvalidDatatypeValueException ide) {
/*  951 */           reportError(ide.getKey(), ide.getArgs());
/*  952 */           reportError("FacetValueFromBase", new Object[] { this.fTypeName, facets.maxExclusive, "maxExclusive", this.fBase.getName() });
/*      */         }
/*      */ 
/*  957 */         if ((this.fBase.fFacetsDefined & 0x40) != 0) {
/*  958 */           result = this.fDVs[this.fValidationDV].compare(this.fMaxExclusive, this.fBase.fMaxExclusive);
/*  959 */           if (((this.fBase.fFixedFacet & 0x40) != 0) && (result != 0)) {
/*  960 */             reportError("FixedFacetValue", new Object[] { "maxExclusive", facets.maxExclusive, this.fBase.fMaxExclusive, this.fTypeName });
/*      */           }
/*  962 */           if (result == 0) {
/*  963 */             needCheckBase = false;
/*      */           }
/*      */         }
/*      */ 
/*  967 */         if (needCheckBase) {
/*      */           try {
/*  969 */             this.fBase.validate(context, tempInfo);
/*      */           } catch (InvalidDatatypeValueException ide) {
/*  971 */             reportError(ide.getKey(), ide.getArgs());
/*  972 */             reportError("FacetValueFromBase", new Object[] { this.fTypeName, facets.maxExclusive, "maxExclusive", this.fBase.getName() });
/*      */           }
/*      */ 
/*      */         }
/*  978 */         else if (((this.fBase.fFacetsDefined & 0x20) != 0) && 
/*  979 */           (this.fDVs[this.fValidationDV].compare(this.fMaxExclusive, this.fBase.fMaxInclusive) > 0)) {
/*  980 */           reportError("maxExclusive-valid-restriction.2", new Object[] { facets.maxExclusive, this.fBase.fMaxInclusive });
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  986 */     needCheckBase = true;
/*  987 */     if ((presentFacet & 0x80) != 0) {
/*  988 */       if ((allowedFacet & 0x80) == 0) {
/*  989 */         reportError("cos-applicable-facets", new Object[] { "minExclusive", this.fTypeName });
/*      */       } else {
/*  991 */         this.minExclusiveAnnotation = facets.minExclusiveAnnotation;
/*      */         try {
/*  993 */           this.fMinExclusive = this.fBase.getActualValue(facets.minExclusive, context, tempInfo, true);
/*  994 */           this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x80));
/*  995 */           if ((fixedFacet & 0x80) != 0)
/*  996 */             this.fFixedFacet = ((short)(this.fFixedFacet | 0x80));
/*      */         } catch (InvalidDatatypeValueException ide) {
/*  998 */           reportError(ide.getKey(), ide.getArgs());
/*  999 */           reportError("FacetValueFromBase", new Object[] { this.fTypeName, facets.minExclusive, "minExclusive", this.fBase.getName() });
/*      */         }
/*      */ 
/* 1004 */         if ((this.fBase.fFacetsDefined & 0x80) != 0) {
/* 1005 */           result = this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fBase.fMinExclusive);
/* 1006 */           if (((this.fBase.fFixedFacet & 0x80) != 0) && (result != 0)) {
/* 1007 */             reportError("FixedFacetValue", new Object[] { "minExclusive", facets.minExclusive, this.fBase.fMinExclusive, this.fTypeName });
/*      */           }
/* 1009 */           if (result == 0) {
/* 1010 */             needCheckBase = false;
/*      */           }
/*      */         }
/*      */ 
/* 1014 */         if (needCheckBase) {
/*      */           try {
/* 1016 */             this.fBase.validate(context, tempInfo);
/*      */           } catch (InvalidDatatypeValueException ide) {
/* 1018 */             reportError(ide.getKey(), ide.getArgs());
/* 1019 */             reportError("FacetValueFromBase", new Object[] { this.fTypeName, facets.minExclusive, "minExclusive", this.fBase.getName() });
/*      */           }
/*      */ 
/*      */         }
/* 1025 */         else if (((this.fBase.fFacetsDefined & 0x100) != 0) && 
/* 1026 */           (this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fBase.fMinInclusive) < 0)) {
/* 1027 */           reportError("minExclusive-valid-restriction.3", new Object[] { facets.minExclusive, this.fBase.fMinInclusive });
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1033 */     if ((presentFacet & 0x100) != 0) {
/* 1034 */       if ((allowedFacet & 0x100) == 0) {
/* 1035 */         reportError("cos-applicable-facets", new Object[] { "minInclusive", this.fTypeName });
/*      */       } else {
/* 1037 */         this.minInclusiveAnnotation = facets.minInclusiveAnnotation;
/*      */         try {
/* 1039 */           this.fMinInclusive = this.fBase.getActualValue(facets.minInclusive, context, tempInfo, true);
/* 1040 */           this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x100));
/* 1041 */           if ((fixedFacet & 0x100) != 0)
/* 1042 */             this.fFixedFacet = ((short)(this.fFixedFacet | 0x100));
/*      */         } catch (InvalidDatatypeValueException ide) {
/* 1044 */           reportError(ide.getKey(), ide.getArgs());
/* 1045 */           reportError("FacetValueFromBase", new Object[] { this.fTypeName, facets.minInclusive, "minInclusive", this.fBase.getName() });
/*      */         }
/*      */ 
/* 1050 */         if (((this.fBase.fFacetsDefined & 0x100) != 0) && 
/* 1051 */           ((this.fBase.fFixedFacet & 0x100) != 0) && 
/* 1052 */           (this.fDVs[this.fValidationDV].compare(this.fMinInclusive, this.fBase.fMinInclusive) != 0)) {
/* 1053 */           reportError("FixedFacetValue", new Object[] { "minInclusive", facets.minInclusive, this.fBase.fMinInclusive, this.fTypeName });
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1058 */           this.fBase.validate(context, tempInfo);
/*      */         } catch (InvalidDatatypeValueException ide) {
/* 1060 */           reportError(ide.getKey(), ide.getArgs());
/* 1061 */           reportError("FacetValueFromBase", new Object[] { this.fTypeName, facets.minInclusive, "minInclusive", this.fBase.getName() });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1068 */     if ((presentFacet & 0x200) != 0) {
/* 1069 */       if ((allowedFacet & 0x200) == 0) {
/* 1070 */         reportError("cos-applicable-facets", new Object[] { "totalDigits", this.fTypeName });
/*      */       } else {
/* 1072 */         this.totalDigitsAnnotation = facets.totalDigitsAnnotation;
/* 1073 */         this.fTotalDigits = facets.totalDigits;
/* 1074 */         this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x200));
/* 1075 */         if ((fixedFacet & 0x200) != 0) {
/* 1076 */           this.fFixedFacet = ((short)(this.fFixedFacet | 0x200));
/*      */         }
/*      */       }
/*      */     }
/* 1080 */     if ((presentFacet & 0x400) != 0) {
/* 1081 */       if ((allowedFacet & 0x400) == 0) {
/* 1082 */         reportError("cos-applicable-facets", new Object[] { "fractionDigits", this.fTypeName });
/*      */       } else {
/* 1084 */         this.fFractionDigits = facets.fractionDigits;
/* 1085 */         this.fractionDigitsAnnotation = facets.fractionDigitsAnnotation;
/* 1086 */         this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x400));
/* 1087 */         if ((fixedFacet & 0x400) != 0) {
/* 1088 */           this.fFixedFacet = ((short)(this.fFixedFacet | 0x400));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1093 */     if (patternType != 0) {
/* 1094 */       this.fPatternType = patternType;
/*      */     }
/*      */ 
/* 1098 */     if (this.fFacetsDefined != 0)
/*      */     {
/* 1101 */       if (((this.fFacetsDefined & 0x2) != 0) && ((this.fFacetsDefined & 0x4) != 0))
/*      */       {
/* 1103 */         if (this.fMinLength > this.fMaxLength) {
/* 1104 */           reportError("minLength-less-than-equal-to-maxLength", new Object[] { Integer.toString(this.fMinLength), Integer.toString(this.fMaxLength), this.fTypeName });
/*      */         }
/*      */       }
/*      */ 
/* 1108 */       if (((this.fFacetsDefined & 0x40) != 0) && ((this.fFacetsDefined & 0x20) != 0)) {
/* 1109 */         reportError("maxInclusive-maxExclusive", new Object[] { this.fMaxInclusive, this.fMaxExclusive, this.fTypeName });
/*      */       }
/*      */ 
/* 1113 */       if (((this.fFacetsDefined & 0x80) != 0) && ((this.fFacetsDefined & 0x100) != 0)) {
/* 1114 */         reportError("minInclusive-minExclusive", new Object[] { this.fMinInclusive, this.fMinExclusive, this.fTypeName });
/*      */       }
/*      */ 
/* 1118 */       if (((this.fFacetsDefined & 0x20) != 0) && ((this.fFacetsDefined & 0x100) != 0)) {
/* 1119 */         result = this.fDVs[this.fValidationDV].compare(this.fMinInclusive, this.fMaxInclusive);
/* 1120 */         if ((result != -1) && (result != 0)) {
/* 1121 */           reportError("minInclusive-less-than-equal-to-maxInclusive", new Object[] { this.fMinInclusive, this.fMaxInclusive, this.fTypeName });
/*      */         }
/*      */       }
/*      */ 
/* 1125 */       if (((this.fFacetsDefined & 0x40) != 0) && ((this.fFacetsDefined & 0x80) != 0)) {
/* 1126 */         result = this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fMaxExclusive);
/* 1127 */         if ((result != -1) && (result != 0)) {
/* 1128 */           reportError("minExclusive-less-than-equal-to-maxExclusive", new Object[] { this.fMinExclusive, this.fMaxExclusive, this.fTypeName });
/*      */         }
/*      */       }
/*      */ 
/* 1132 */       if (((this.fFacetsDefined & 0x20) != 0) && ((this.fFacetsDefined & 0x80) != 0) && 
/* 1133 */         (this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fMaxInclusive) != -1)) {
/* 1134 */         reportError("minExclusive-less-than-maxInclusive", new Object[] { this.fMinExclusive, this.fMaxInclusive, this.fTypeName });
/*      */       }
/*      */ 
/* 1138 */       if (((this.fFacetsDefined & 0x40) != 0) && ((this.fFacetsDefined & 0x100) != 0) && 
/* 1139 */         (this.fDVs[this.fValidationDV].compare(this.fMinInclusive, this.fMaxExclusive) != -1)) {
/* 1140 */         reportError("minInclusive-less-than-maxExclusive", new Object[] { this.fMinInclusive, this.fMaxExclusive, this.fTypeName });
/*      */       }
/*      */ 
/* 1144 */       if (((this.fFacetsDefined & 0x400) != 0) && ((this.fFacetsDefined & 0x200) != 0))
/*      */       {
/* 1146 */         if (this.fFractionDigits > this.fTotalDigits) {
/* 1147 */           reportError("fractionDigits-totalDigits", new Object[] { Integer.toString(this.fFractionDigits), Integer.toString(this.fTotalDigits), this.fTypeName });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1152 */       if ((this.fFacetsDefined & 0x1) != 0) {
/* 1153 */         if (((this.fBase.fFacetsDefined & 0x2) != 0) && (this.fLength < this.fBase.fMinLength))
/*      */         {
/* 1156 */           reportError("length-minLength-maxLength.1.1", new Object[] { this.fTypeName, Integer.toString(this.fLength), Integer.toString(this.fBase.fMinLength) });
/*      */         }
/* 1158 */         if (((this.fBase.fFacetsDefined & 0x4) != 0) && (this.fLength > this.fBase.fMaxLength))
/*      */         {
/* 1161 */           reportError("length-minLength-maxLength.2.1", new Object[] { this.fTypeName, Integer.toString(this.fLength), Integer.toString(this.fBase.fMaxLength) });
/*      */         }
/* 1163 */         if ((this.fBase.fFacetsDefined & 0x1) != 0)
/*      */         {
/* 1165 */           if (this.fLength != this.fBase.fLength) {
/* 1166 */             reportError("length-valid-restriction", new Object[] { Integer.toString(this.fLength), Integer.toString(this.fBase.fLength), this.fTypeName });
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1171 */       if (((this.fBase.fFacetsDefined & 0x1) != 0) || ((this.fFacetsDefined & 0x1) != 0)) {
/* 1172 */         if ((this.fFacetsDefined & 0x2) != 0) {
/* 1173 */           if (this.fBase.fLength < this.fMinLength)
/*      */           {
/* 1175 */             reportError("length-minLength-maxLength.1.1", new Object[] { this.fTypeName, Integer.toString(this.fBase.fLength), Integer.toString(this.fMinLength) });
/*      */           }
/* 1177 */           if ((this.fBase.fFacetsDefined & 0x2) == 0) {
/* 1178 */             reportError("length-minLength-maxLength.1.2.a", new Object[] { this.fTypeName });
/*      */           }
/* 1180 */           if (this.fMinLength != this.fBase.fMinLength) {
/* 1181 */             reportError("length-minLength-maxLength.1.2.b", new Object[] { this.fTypeName, Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMinLength) });
/*      */           }
/*      */         }
/* 1184 */         if ((this.fFacetsDefined & 0x4) != 0) {
/* 1185 */           if (this.fBase.fLength > this.fMaxLength)
/*      */           {
/* 1187 */             reportError("length-minLength-maxLength.2.1", new Object[] { this.fTypeName, Integer.toString(this.fBase.fLength), Integer.toString(this.fMaxLength) });
/*      */           }
/* 1189 */           if ((this.fBase.fFacetsDefined & 0x4) == 0) {
/* 1190 */             reportError("length-minLength-maxLength.2.2.a", new Object[] { this.fTypeName });
/*      */           }
/* 1192 */           if (this.fMaxLength != this.fBase.fMaxLength) {
/* 1193 */             reportError("length-minLength-maxLength.2.2.b", new Object[] { this.fTypeName, Integer.toString(this.fMaxLength), Integer.toString(this.fBase.fBase.fMaxLength) });
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1199 */       if ((this.fFacetsDefined & 0x2) != 0) {
/* 1200 */         if ((this.fBase.fFacetsDefined & 0x4) != 0) {
/* 1201 */           if (this.fMinLength > this.fBase.fMaxLength) {
/* 1202 */             reportError("minLength-less-than-equal-to-maxLength", new Object[] { Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMaxLength), this.fTypeName });
/*      */           }
/*      */         }
/* 1205 */         else if ((this.fBase.fFacetsDefined & 0x2) != 0) {
/* 1206 */           if (((this.fBase.fFixedFacet & 0x2) != 0) && (this.fMinLength != this.fBase.fMinLength)) {
/* 1207 */             reportError("FixedFacetValue", new Object[] { "minLength", Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMinLength), this.fTypeName });
/*      */           }
/*      */ 
/* 1211 */           if (this.fMinLength < this.fBase.fMinLength) {
/* 1212 */             reportError("minLength-valid-restriction", new Object[] { Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMinLength), this.fTypeName });
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1219 */       if (((this.fFacetsDefined & 0x4) != 0) && ((this.fBase.fFacetsDefined & 0x2) != 0) && 
/* 1220 */         (this.fMaxLength < this.fBase.fMinLength)) {
/* 1221 */         reportError("minLength-less-than-equal-to-maxLength", new Object[] { Integer.toString(this.fBase.fMinLength), Integer.toString(this.fMaxLength) });
/*      */       }
/*      */ 
/* 1226 */       if (((this.fFacetsDefined & 0x4) != 0) && 
/* 1227 */         ((this.fBase.fFacetsDefined & 0x4) != 0)) {
/* 1228 */         if (((this.fBase.fFixedFacet & 0x4) != 0) && (this.fMaxLength != this.fBase.fMaxLength)) {
/* 1229 */           reportError("FixedFacetValue", new Object[] { "maxLength", Integer.toString(this.fMaxLength), Integer.toString(this.fBase.fMaxLength), this.fTypeName });
/*      */         }
/* 1231 */         if (this.fMaxLength > this.fBase.fMaxLength) {
/* 1232 */           reportError("maxLength-valid-restriction", new Object[] { Integer.toString(this.fMaxLength), Integer.toString(this.fBase.fMaxLength), this.fTypeName });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1370 */       if (((this.fFacetsDefined & 0x200) != 0) && 
/* 1371 */         ((this.fBase.fFacetsDefined & 0x200) != 0)) {
/* 1372 */         if (((this.fBase.fFixedFacet & 0x200) != 0) && (this.fTotalDigits != this.fBase.fTotalDigits)) {
/* 1373 */           reportError("FixedFacetValue", new Object[] { "totalDigits", Integer.toString(this.fTotalDigits), Integer.toString(this.fBase.fTotalDigits), this.fTypeName });
/*      */         }
/* 1375 */         if (this.fTotalDigits > this.fBase.fTotalDigits) {
/* 1376 */           reportError("totalDigits-valid-restriction", new Object[] { Integer.toString(this.fTotalDigits), Integer.toString(this.fBase.fTotalDigits), this.fTypeName });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1382 */       if (((this.fFacetsDefined & 0x400) != 0) && 
/* 1383 */         ((this.fBase.fFacetsDefined & 0x200) != 0) && 
/* 1384 */         (this.fFractionDigits > this.fBase.fTotalDigits)) {
/* 1385 */         reportError("fractionDigits-totalDigits", new Object[] { Integer.toString(this.fFractionDigits), Integer.toString(this.fTotalDigits), this.fTypeName });
/*      */       }
/*      */ 
/* 1391 */       if ((this.fFacetsDefined & 0x400) != 0) {
/* 1392 */         if ((this.fBase.fFacetsDefined & 0x400) != 0) {
/* 1393 */           if ((((this.fBase.fFixedFacet & 0x400) != 0) && (this.fFractionDigits != this.fBase.fFractionDigits)) || ((this.fValidationDV == 24) && (this.fFractionDigits != 0)))
/*      */           {
/* 1395 */             reportError("FixedFacetValue", new Object[] { "fractionDigits", Integer.toString(this.fFractionDigits), Integer.toString(this.fBase.fFractionDigits), this.fTypeName });
/*      */           }
/* 1397 */           if (this.fFractionDigits > this.fBase.fFractionDigits) {
/* 1398 */             reportError("fractionDigits-valid-restriction", new Object[] { Integer.toString(this.fFractionDigits), Integer.toString(this.fBase.fFractionDigits), this.fTypeName });
/*      */           }
/*      */         }
/* 1401 */         else if ((this.fValidationDV == 24) && (this.fFractionDigits != 0)) {
/* 1402 */           reportError("FixedFacetValue", new Object[] { "fractionDigits", Integer.toString(this.fFractionDigits), "0", this.fTypeName });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1410 */       if (((this.fFacetsDefined & 0x10) != 0) && ((this.fBase.fFacetsDefined & 0x10) != 0)) {
/* 1411 */         if (((this.fBase.fFixedFacet & 0x10) != 0) && (this.fWhiteSpace != this.fBase.fWhiteSpace)) {
/* 1412 */           reportError("FixedFacetValue", new Object[] { "whiteSpace", whiteSpaceValue(this.fWhiteSpace), whiteSpaceValue(this.fBase.fWhiteSpace), this.fTypeName });
/*      */         }
/*      */ 
/* 1415 */         if ((this.fWhiteSpace == 0) && (this.fBase.fWhiteSpace == 2)) {
/* 1416 */           reportError("whiteSpace-valid-restriction.1", new Object[] { this.fTypeName, "preserve" });
/*      */         }
/* 1418 */         if ((this.fWhiteSpace == 1) && (this.fBase.fWhiteSpace == 2)) {
/* 1419 */           reportError("whiteSpace-valid-restriction.1", new Object[] { this.fTypeName, "replace" });
/*      */         }
/* 1421 */         if ((this.fWhiteSpace == 0) && (this.fBase.fWhiteSpace == 1)) {
/* 1422 */           reportError("whiteSpace-valid-restriction.2", new Object[] { this.fTypeName });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1430 */     if (((this.fFacetsDefined & 0x1) == 0) && ((this.fBase.fFacetsDefined & 0x1) != 0)) {
/* 1431 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x1));
/* 1432 */       this.fLength = this.fBase.fLength;
/* 1433 */       this.lengthAnnotation = this.fBase.lengthAnnotation;
/*      */     }
/*      */ 
/* 1436 */     if (((this.fFacetsDefined & 0x2) == 0) && ((this.fBase.fFacetsDefined & 0x2) != 0)) {
/* 1437 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x2));
/* 1438 */       this.fMinLength = this.fBase.fMinLength;
/* 1439 */       this.minLengthAnnotation = this.fBase.minLengthAnnotation;
/*      */     }
/*      */ 
/* 1442 */     if (((this.fFacetsDefined & 0x4) == 0) && ((this.fBase.fFacetsDefined & 0x4) != 0)) {
/* 1443 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x4));
/* 1444 */       this.fMaxLength = this.fBase.fMaxLength;
/* 1445 */       this.maxLengthAnnotation = this.fBase.maxLengthAnnotation;
/*      */     }
/*      */ 
/* 1448 */     if ((this.fBase.fFacetsDefined & 0x8) != 0) {
/* 1449 */       if ((this.fFacetsDefined & 0x8) == 0) {
/* 1450 */         this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x8));
/* 1451 */         this.fPattern = this.fBase.fPattern;
/* 1452 */         this.fPatternStr = this.fBase.fPatternStr;
/* 1453 */         this.patternAnnotations = this.fBase.patternAnnotations;
/*      */       }
/*      */       else {
/* 1456 */         for (int i = this.fBase.fPattern.size() - 1; i >= 0; i--) {
/* 1457 */           this.fPattern.addElement(this.fBase.fPattern.elementAt(i));
/* 1458 */           this.fPatternStr.addElement(this.fBase.fPatternStr.elementAt(i));
/*      */         }
/* 1460 */         if (this.fBase.patternAnnotations != null) {
/* 1461 */           if (this.patternAnnotations != null) {
/* 1462 */             for (int i = this.fBase.patternAnnotations.getLength() - 1; i >= 0; i--) {
/* 1463 */               this.patternAnnotations.addXSObject(this.fBase.patternAnnotations.item(i));
/*      */             }
/*      */           }
/*      */           else {
/* 1467 */             this.patternAnnotations = this.fBase.patternAnnotations;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1473 */     if (((this.fFacetsDefined & 0x10) == 0) && ((this.fBase.fFacetsDefined & 0x10) != 0)) {
/* 1474 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x10));
/* 1475 */       this.fWhiteSpace = this.fBase.fWhiteSpace;
/* 1476 */       this.whiteSpaceAnnotation = this.fBase.whiteSpaceAnnotation;
/*      */     }
/*      */ 
/* 1479 */     if (((this.fFacetsDefined & 0x800) == 0) && ((this.fBase.fFacetsDefined & 0x800) != 0)) {
/* 1480 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x800));
/* 1481 */       this.fEnumeration = this.fBase.fEnumeration;
/* 1482 */       this.enumerationAnnotations = this.fBase.enumerationAnnotations;
/*      */     }
/*      */ 
/* 1485 */     if (((this.fBase.fFacetsDefined & 0x40) != 0) && ((this.fFacetsDefined & 0x40) == 0) && ((this.fFacetsDefined & 0x20) == 0))
/*      */     {
/* 1487 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x40));
/* 1488 */       this.fMaxExclusive = this.fBase.fMaxExclusive;
/* 1489 */       this.maxExclusiveAnnotation = this.fBase.maxExclusiveAnnotation;
/*      */     }
/*      */ 
/* 1492 */     if (((this.fBase.fFacetsDefined & 0x20) != 0) && ((this.fFacetsDefined & 0x40) == 0) && ((this.fFacetsDefined & 0x20) == 0))
/*      */     {
/* 1494 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x20));
/* 1495 */       this.fMaxInclusive = this.fBase.fMaxInclusive;
/* 1496 */       this.maxInclusiveAnnotation = this.fBase.maxInclusiveAnnotation;
/*      */     }
/*      */ 
/* 1499 */     if (((this.fBase.fFacetsDefined & 0x80) != 0) && ((this.fFacetsDefined & 0x80) == 0) && ((this.fFacetsDefined & 0x100) == 0))
/*      */     {
/* 1501 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x80));
/* 1502 */       this.fMinExclusive = this.fBase.fMinExclusive;
/* 1503 */       this.minExclusiveAnnotation = this.fBase.minExclusiveAnnotation;
/*      */     }
/*      */ 
/* 1506 */     if (((this.fBase.fFacetsDefined & 0x100) != 0) && ((this.fFacetsDefined & 0x80) == 0) && ((this.fFacetsDefined & 0x100) == 0))
/*      */     {
/* 1508 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x100));
/* 1509 */       this.fMinInclusive = this.fBase.fMinInclusive;
/* 1510 */       this.minInclusiveAnnotation = this.fBase.minInclusiveAnnotation;
/*      */     }
/*      */ 
/* 1513 */     if (((this.fBase.fFacetsDefined & 0x200) != 0) && ((this.fFacetsDefined & 0x200) == 0))
/*      */     {
/* 1515 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x200));
/* 1516 */       this.fTotalDigits = this.fBase.fTotalDigits;
/* 1517 */       this.totalDigitsAnnotation = this.fBase.totalDigitsAnnotation;
/*      */     }
/*      */ 
/* 1520 */     if (((this.fBase.fFacetsDefined & 0x400) != 0) && ((this.fFacetsDefined & 0x400) == 0))
/*      */     {
/* 1522 */       this.fFacetsDefined = ((short)(this.fFacetsDefined | 0x400));
/* 1523 */       this.fFractionDigits = this.fBase.fFractionDigits;
/* 1524 */       this.fractionDigitsAnnotation = this.fBase.fractionDigitsAnnotation;
/*      */     }
/*      */ 
/* 1527 */     if ((this.fPatternType == 0) && (this.fBase.fPatternType != 0)) {
/* 1528 */       this.fPatternType = this.fBase.fPatternType;
/*      */     }
/*      */ 
/* 1532 */     this.fFixedFacet = ((short)(this.fFixedFacet | this.fBase.fFixedFacet));
/*      */ 
/* 1535 */     calcFundamentalFacets();
/*      */   }
/*      */ 
/*      */   public Object validate(String content, ValidationContext context, ValidatedInfo validatedInfo)
/*      */     throws InvalidDatatypeValueException
/*      */   {
/* 1544 */     if (context == null) {
/* 1545 */       context = fEmptyContext;
/*      */     }
/* 1547 */     if (validatedInfo == null)
/* 1548 */       validatedInfo = new ValidatedInfo();
/*      */     else {
/* 1550 */       validatedInfo.memberType = null;
/*      */     }
/*      */ 
/* 1553 */     boolean needNormalize = (context == null) || (context.needToNormalize());
/* 1554 */     Object ob = getActualValue(content, context, validatedInfo, needNormalize);
/*      */ 
/* 1556 */     validate(context, validatedInfo);
/*      */ 
/* 1558 */     return ob;
/*      */   }
/*      */ 
/*      */   protected ValidatedInfo getActualEnumValue(String lexical, ValidationContext ctx, ValidatedInfo info)
/*      */     throws InvalidDatatypeValueException
/*      */   {
/* 1564 */     return this.fBase.validateWithInfo(lexical, ctx, info);
/*      */   }
/*      */ 
/*      */   public ValidatedInfo validateWithInfo(String content, ValidationContext context, ValidatedInfo validatedInfo)
/*      */     throws InvalidDatatypeValueException
/*      */   {
/* 1572 */     if (context == null) {
/* 1573 */       context = fEmptyContext;
/*      */     }
/* 1575 */     if (validatedInfo == null)
/* 1576 */       validatedInfo = new ValidatedInfo();
/*      */     else {
/* 1578 */       validatedInfo.memberType = null;
/*      */     }
/*      */ 
/* 1581 */     boolean needNormalize = (context == null) || (context.needToNormalize());
/* 1582 */     getActualValue(content, context, validatedInfo, needNormalize);
/*      */ 
/* 1584 */     validate(context, validatedInfo);
/*      */ 
/* 1586 */     return validatedInfo;
/*      */   }
/*      */ 
/*      */   public Object validate(Object content, ValidationContext context, ValidatedInfo validatedInfo)
/*      */     throws InvalidDatatypeValueException
/*      */   {
/* 1595 */     if (context == null) {
/* 1596 */       context = fEmptyContext;
/*      */     }
/* 1598 */     if (validatedInfo == null)
/* 1599 */       validatedInfo = new ValidatedInfo();
/*      */     else {
/* 1601 */       validatedInfo.memberType = null;
/*      */     }
/*      */ 
/* 1604 */     boolean needNormalize = (context == null) || (context.needToNormalize());
/* 1605 */     Object ob = getActualValue(content, context, validatedInfo, needNormalize);
/*      */ 
/* 1607 */     validate(context, validatedInfo);
/*      */ 
/* 1609 */     return ob;
/*      */   }
/*      */ 
/*      */   public void validate(ValidationContext context, ValidatedInfo validatedInfo)
/*      */     throws InvalidDatatypeValueException
/*      */   {
/* 1622 */     if (context == null) {
/* 1623 */       context = fEmptyContext;
/*      */     }
/*      */ 
/* 1626 */     if ((context.needFacetChecking()) && (this.fFacetsDefined != 0) && (this.fFacetsDefined != 16))
/*      */     {
/* 1628 */       checkFacets(validatedInfo);
/*      */     }
/*      */ 
/* 1632 */     if (context.needExtraChecking())
/* 1633 */       checkExtraRules(context, validatedInfo);
/*      */   }
/*      */ 
/*      */   private void checkFacets(ValidatedInfo validatedInfo)
/*      */     throws InvalidDatatypeValueException
/*      */   {
/* 1640 */     Object ob = validatedInfo.actualValue;
/* 1641 */     String content = validatedInfo.normalizedValue;
/* 1642 */     short type = validatedInfo.actualValueType;
/* 1643 */     ShortList itemType = validatedInfo.itemValueTypes;
/*      */ 
/* 1646 */     if ((this.fValidationDV != 18) && (this.fValidationDV != 20)) {
/* 1647 */       int length = this.fDVs[this.fValidationDV].getDataLength(ob);
/*      */ 
/* 1650 */       if (((this.fFacetsDefined & 0x4) != 0) && 
/* 1651 */         (length > this.fMaxLength)) {
/* 1652 */         throw new InvalidDatatypeValueException("cvc-maxLength-valid", new Object[] { content, Integer.toString(length), Integer.toString(this.fMaxLength), this.fTypeName });
/*      */       }
/*      */ 
/* 1658 */       if (((this.fFacetsDefined & 0x2) != 0) && 
/* 1659 */         (length < this.fMinLength)) {
/* 1660 */         throw new InvalidDatatypeValueException("cvc-minLength-valid", new Object[] { content, Integer.toString(length), Integer.toString(this.fMinLength), this.fTypeName });
/*      */       }
/*      */ 
/* 1666 */       if (((this.fFacetsDefined & 0x1) != 0) && 
/* 1667 */         (length != this.fLength)) {
/* 1668 */         throw new InvalidDatatypeValueException("cvc-length-valid", new Object[] { content, Integer.toString(length), Integer.toString(this.fLength), this.fTypeName });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1675 */     if ((this.fFacetsDefined & 0x800) != 0) {
/* 1676 */       boolean present = false;
/* 1677 */       int enumSize = this.fEnumeration.size();
/* 1678 */       short primitiveType1 = convertToPrimitiveKind(type);
/* 1679 */       for (int i = 0; i < enumSize; i++) {
/* 1680 */         short primitiveType2 = convertToPrimitiveKind(this.fEnumerationType[i]);
/* 1681 */         if (((primitiveType1 == primitiveType2) || ((primitiveType1 == 1) && (primitiveType2 == 2)) || ((primitiveType1 == 2) && (primitiveType2 == 1))) && (this.fEnumeration.elementAt(i).equals(ob)))
/*      */         {
/* 1685 */           if ((primitiveType1 == 44) || (primitiveType1 == 43)) {
/* 1686 */             ShortList enumItemType = this.fEnumerationItemType[i];
/* 1687 */             int typeList1Length = itemType != null ? itemType.getLength() : 0;
/* 1688 */             int typeList2Length = enumItemType != null ? enumItemType.getLength() : 0;
/* 1689 */             if (typeList1Length == typeList2Length)
/*      */             {
/* 1691 */               for (int j = 0; j < typeList1Length; j++) {
/* 1692 */                 short primitiveItem1 = convertToPrimitiveKind(itemType.item(j));
/* 1693 */                 short primitiveItem2 = convertToPrimitiveKind(enumItemType.item(j));
/* 1694 */                 if ((primitiveItem1 != primitiveItem2) && (
/* 1695 */                   (primitiveItem1 != 1) || (primitiveItem2 != 2))) if ((primitiveItem1 != 2) || (primitiveItem2 != 1))
/*      */                   {
/*      */                     break;
/*      */                   }
/*      */ 
/*      */               }
/*      */ 
/* 1702 */               if (j == typeList1Length) {
/* 1703 */                 present = true;
/* 1704 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */           else {
/* 1709 */             present = true;
/* 1710 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1714 */       if (!present) {
/* 1715 */         throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { content, this.fEnumeration.toString() });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1721 */     if ((this.fFacetsDefined & 0x400) != 0) {
/* 1722 */       int scale = this.fDVs[this.fValidationDV].getFractionDigits(ob);
/* 1723 */       if (scale > this.fFractionDigits) {
/* 1724 */         throw new InvalidDatatypeValueException("cvc-fractionDigits-valid", new Object[] { content, Integer.toString(scale), Integer.toString(this.fFractionDigits) });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1730 */     if ((this.fFacetsDefined & 0x200) != 0) {
/* 1731 */       int totalDigits = this.fDVs[this.fValidationDV].getTotalDigits(ob);
/* 1732 */       if (totalDigits > this.fTotalDigits) {
/* 1733 */         throw new InvalidDatatypeValueException("cvc-totalDigits-valid", new Object[] { content, Integer.toString(totalDigits), Integer.toString(this.fTotalDigits) });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1741 */     if ((this.fFacetsDefined & 0x20) != 0) {
/* 1742 */       int compare = this.fDVs[this.fValidationDV].compare(ob, this.fMaxInclusive);
/* 1743 */       if ((compare != -1) && (compare != 0)) {
/* 1744 */         throw new InvalidDatatypeValueException("cvc-maxInclusive-valid", new Object[] { content, this.fMaxInclusive, this.fTypeName });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1750 */     if ((this.fFacetsDefined & 0x40) != 0) {
/* 1751 */       int compare = this.fDVs[this.fValidationDV].compare(ob, this.fMaxExclusive);
/* 1752 */       if (compare != -1) {
/* 1753 */         throw new InvalidDatatypeValueException("cvc-maxExclusive-valid", new Object[] { content, this.fMaxExclusive, this.fTypeName });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1759 */     if ((this.fFacetsDefined & 0x100) != 0) {
/* 1760 */       int compare = this.fDVs[this.fValidationDV].compare(ob, this.fMinInclusive);
/* 1761 */       if ((compare != 1) && (compare != 0)) {
/* 1762 */         throw new InvalidDatatypeValueException("cvc-minInclusive-valid", new Object[] { content, this.fMinInclusive, this.fTypeName });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1768 */     if ((this.fFacetsDefined & 0x80) != 0) {
/* 1769 */       int compare = this.fDVs[this.fValidationDV].compare(ob, this.fMinExclusive);
/* 1770 */       if (compare != 1)
/* 1771 */         throw new InvalidDatatypeValueException("cvc-minExclusive-valid", new Object[] { content, this.fMinExclusive, this.fTypeName });
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkExtraRules(ValidationContext context, ValidatedInfo validatedInfo)
/*      */     throws InvalidDatatypeValueException
/*      */   {
/* 1780 */     Object ob = validatedInfo.actualValue;
/*      */ 
/* 1782 */     if (this.fVariety == 1)
/*      */     {
/* 1784 */       this.fDVs[this.fValidationDV].checkExtraRules(ob, context);
/*      */     }
/* 1786 */     else if (this.fVariety == 2)
/*      */     {
/* 1788 */       ListDV.ListData values = (ListDV.ListData)ob;
/* 1789 */       XSSimpleType memberType = validatedInfo.memberType;
/* 1790 */       int len = values.getLength();
/*      */       try {
/* 1792 */         if (this.fItemType.fVariety == 3) {
/* 1793 */           XSSimpleTypeDecl[] memberTypes = (XSSimpleTypeDecl[])validatedInfo.memberTypes;
/* 1794 */           for (int i = len - 1; i >= 0; i--) {
/* 1795 */             validatedInfo.actualValue = values.item(i);
/* 1796 */             validatedInfo.memberType = memberTypes[i];
/* 1797 */             this.fItemType.checkExtraRules(context, validatedInfo);
/*      */           }
/*      */         } else {
/* 1800 */           for (int i = len - 1; i >= 0; i--) {
/* 1801 */             validatedInfo.actualValue = values.item(i);
/* 1802 */             this.fItemType.checkExtraRules(context, validatedInfo);
/*      */           }
/*      */         }
/*      */       }
/*      */       finally {
/* 1807 */         validatedInfo.actualValue = values;
/* 1808 */         validatedInfo.memberType = memberType;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1813 */       ((XSSimpleTypeDecl)validatedInfo.memberType).checkExtraRules(context, validatedInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object getActualValue(Object content, ValidationContext context, ValidatedInfo validatedInfo, boolean needNormalize)
/*      */     throws InvalidDatatypeValueException
/*      */   {
/*      */     String nvalue;
/*      */     String nvalue;
/* 1825 */     if (needNormalize)
/* 1826 */       nvalue = normalize(content, this.fWhiteSpace);
/*      */     else {
/* 1828 */       nvalue = content.toString();
/*      */     }
/* 1830 */     if ((this.fFacetsDefined & 0x8) != 0) {
/* 1831 */       if ((this.fPattern.size() == 0) && (nvalue.length() > 0)) {
/* 1832 */         throw new InvalidDatatypeValueException("cvc-pattern-valid", new Object[] { content, "(empty string)", this.fTypeName });
/*      */       }
/*      */ 
/* 1838 */       for (int idx = this.fPattern.size() - 1; idx >= 0; idx--) {
/* 1839 */         RegularExpression regex = (RegularExpression)this.fPattern.elementAt(idx);
/* 1840 */         if (!regex.matches(nvalue)) {
/* 1841 */           throw new InvalidDatatypeValueException("cvc-pattern-valid", new Object[] { content, this.fPatternStr.elementAt(idx), this.fTypeName });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1849 */     if (this.fVariety == 1)
/*      */     {
/* 1852 */       if (this.fPatternType != 0)
/*      */       {
/* 1854 */         boolean seenErr = false;
/* 1855 */         if (this.fPatternType == 1)
/*      */         {
/* 1857 */           seenErr = !XMLChar.isValidNmtoken(nvalue);
/*      */         }
/* 1859 */         else if (this.fPatternType == 2)
/*      */         {
/* 1861 */           seenErr = !XMLChar.isValidName(nvalue);
/*      */         }
/* 1863 */         else if (this.fPatternType == 3)
/*      */         {
/* 1865 */           seenErr = !XMLChar.isValidNCName(nvalue);
/*      */         }
/* 1867 */         if (seenErr) {
/* 1868 */           throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { nvalue, SPECIAL_PATTERN_STRING[this.fPatternType] });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1873 */       validatedInfo.normalizedValue = nvalue;
/* 1874 */       Object avalue = this.fDVs[this.fValidationDV].getActualValue(nvalue, context);
/* 1875 */       validatedInfo.actualValue = avalue;
/* 1876 */       validatedInfo.actualValueType = this.fBuiltInKind;
/*      */ 
/* 1878 */       return avalue;
/*      */     }
/* 1880 */     if (this.fVariety == 2)
/*      */     {
/* 1882 */       StringTokenizer parsedList = new StringTokenizer(nvalue, " ");
/* 1883 */       int countOfTokens = parsedList.countTokens();
/* 1884 */       Object[] avalue = new Object[countOfTokens];
/* 1885 */       boolean isUnion = this.fItemType.getVariety() == 3;
/* 1886 */       short[] itemTypes = new short[isUnion ? countOfTokens : 1];
/* 1887 */       if (!isUnion)
/* 1888 */         itemTypes[0] = this.fItemType.fBuiltInKind;
/* 1889 */       XSSimpleTypeDecl[] memberTypes = new XSSimpleTypeDecl[countOfTokens];
/* 1890 */       for (int i = 0; i < countOfTokens; i++)
/*      */       {
/* 1897 */         avalue[i] = this.fItemType.getActualValue(parsedList.nextToken(), context, validatedInfo, false);
/* 1898 */         if ((context.needFacetChecking()) && (this.fItemType.fFacetsDefined != 0) && (this.fItemType.fFacetsDefined != 16))
/*      */         {
/* 1900 */           this.fItemType.checkFacets(validatedInfo);
/*      */         }
/* 1902 */         memberTypes[i] = ((XSSimpleTypeDecl)validatedInfo.memberType);
/* 1903 */         if (isUnion) {
/* 1904 */           itemTypes[i] = memberTypes[i].fBuiltInKind;
/*      */         }
/*      */       }
/* 1907 */       ListDV.ListData v = new ListDV.ListData(avalue);
/* 1908 */       validatedInfo.actualValue = v;
/* 1909 */       validatedInfo.actualValueType = (isUnion ? 43 : 44);
/* 1910 */       validatedInfo.memberType = null;
/* 1911 */       validatedInfo.memberTypes = memberTypes;
/* 1912 */       validatedInfo.itemValueTypes = new ShortListImpl(itemTypes, itemTypes.length);
/* 1913 */       validatedInfo.normalizedValue = nvalue;
/*      */ 
/* 1915 */       return v;
/*      */     }
/*      */ 
/* 1918 */     Object _content = (this.fMemberTypes.length > 1) && (content != null) ? content.toString() : content;
/* 1919 */     for (int i = 0; i < this.fMemberTypes.length; i++)
/*      */     {
/*      */       try
/*      */       {
/* 1927 */         Object aValue = this.fMemberTypes[i].getActualValue(_content, context, validatedInfo, true);
/* 1928 */         if ((context.needFacetChecking()) && (this.fMemberTypes[i].fFacetsDefined != 0) && (this.fMemberTypes[i].fFacetsDefined != 16))
/*      */         {
/* 1930 */           this.fMemberTypes[i].checkFacets(validatedInfo);
/*      */         }
/* 1932 */         validatedInfo.memberType = this.fMemberTypes[i];
/* 1933 */         return aValue;
/*      */       } catch (InvalidDatatypeValueException invalidValue) {
/*      */       }
/*      */     }
/* 1937 */     StringBuffer typesBuffer = new StringBuffer();
/*      */ 
/* 1939 */     for (int i = 0; i < this.fMemberTypes.length; i++) {
/* 1940 */       if (i != 0)
/* 1941 */         typesBuffer.append(" | ");
/* 1942 */       XSSimpleTypeDecl decl = this.fMemberTypes[i];
/* 1943 */       if (decl.fTargetNamespace != null) {
/* 1944 */         typesBuffer.append('{');
/* 1945 */         typesBuffer.append(decl.fTargetNamespace);
/* 1946 */         typesBuffer.append('}');
/*      */       }
/* 1948 */       typesBuffer.append(decl.fTypeName);
/* 1949 */       if (decl.fEnumeration != null) {
/* 1950 */         Vector v = decl.fEnumeration;
/* 1951 */         typesBuffer.append(" : [");
/* 1952 */         for (int j = 0; j < v.size(); j++) {
/* 1953 */           if (j != 0)
/* 1954 */             typesBuffer.append(',');
/* 1955 */           typesBuffer.append(v.elementAt(j));
/*      */         }
/* 1957 */         typesBuffer.append(']');
/*      */       }
/*      */     }
/* 1960 */     throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { content, this.fTypeName, typesBuffer.toString() });
/*      */   }
/*      */ 
/*      */   public boolean isEqual(Object value1, Object value2)
/*      */   {
/* 1967 */     if (value1 == null) {
/* 1968 */       return false;
/*      */     }
/* 1970 */     return value1.equals(value2);
/*      */   }
/*      */ 
/*      */   public boolean isIdentical(Object value1, Object value2)
/*      */   {
/* 1975 */     if (value1 == null) {
/* 1976 */       return false;
/*      */     }
/* 1978 */     return this.fDVs[this.fValidationDV].isIdentical(value1, value2);
/*      */   }
/*      */ 
/*      */   public static String normalize(String content, short ws)
/*      */   {
/* 1983 */     int len = content == null ? 0 : content.length();
/* 1984 */     if ((len == 0) || (ws == 0)) {
/* 1985 */       return content;
/*      */     }
/* 1987 */     StringBuffer sb = new StringBuffer();
/* 1988 */     if (ws == 1)
/*      */     {
/* 1991 */       for (int i = 0; i < len; i++) {
/* 1992 */         char ch = content.charAt(i);
/* 1993 */         if ((ch != '\t') && (ch != '\n') && (ch != '\r'))
/* 1994 */           sb.append(ch);
/*      */         else
/* 1996 */           sb.append(' ');
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2001 */       boolean isLeading = true;
/*      */ 
/* 2003 */       for (int i = 0; i < len; i++) {
/* 2004 */         char ch = content.charAt(i);
/*      */ 
/* 2006 */         if ((ch != '\t') && (ch != '\n') && (ch != '\r') && (ch != ' ')) {
/* 2007 */           sb.append(ch);
/* 2008 */           isLeading = false;
/*      */         }
/*      */         else
/*      */         {
/* 2012 */           for (; i < len - 1; i++) {
/* 2013 */             ch = content.charAt(i + 1);
/* 2014 */             if ((ch != '\t') && (ch != '\n') && (ch != '\r') && (ch != ' ')) {
/*      */               break;
/*      */             }
/*      */           }
/* 2018 */           if ((i < len - 1) && (!isLeading)) {
/* 2019 */             sb.append(' ');
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2024 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   protected String normalize(Object content, short ws)
/*      */   {
/* 2029 */     if (content == null) {
/* 2030 */       return null;
/*      */     }
/*      */ 
/* 2035 */     if ((this.fFacetsDefined & 0x8) == 0) {
/* 2036 */       short norm_type = fDVNormalizeType[this.fValidationDV];
/* 2037 */       if (norm_type == 0) {
/* 2038 */         return content.toString();
/*      */       }
/* 2040 */       if (norm_type == 1) {
/* 2041 */         return XMLChar.trim(content.toString());
/*      */       }
/*      */     }
/*      */ 
/* 2045 */     if (!(content instanceof StringBuffer)) {
/* 2046 */       String strContent = content.toString();
/* 2047 */       return normalize(strContent, ws);
/*      */     }
/*      */ 
/* 2050 */     StringBuffer sb = (StringBuffer)content;
/* 2051 */     int len = sb.length();
/* 2052 */     if (len == 0)
/* 2053 */       return "";
/* 2054 */     if (ws == 0) {
/* 2055 */       return sb.toString();
/*      */     }
/* 2057 */     if (ws == 1)
/*      */     {
/* 2060 */       for (int i = 0; i < len; i++) {
/* 2061 */         char ch = sb.charAt(i);
/* 2062 */         if ((ch == '\t') || (ch == '\n') || (ch == '\r'))
/* 2063 */           sb.setCharAt(i, ' ');
/*      */       }
/*      */     }
/*      */     else {
/* 2067 */       int j = 0;
/* 2068 */       boolean isLeading = true;
/*      */ 
/* 2070 */       for (int i = 0; i < len; i++) {
/* 2071 */         char ch = sb.charAt(i);
/*      */ 
/* 2073 */         if ((ch != '\t') && (ch != '\n') && (ch != '\r') && (ch != ' ')) {
/* 2074 */           sb.setCharAt(j++, ch);
/* 2075 */           isLeading = false;
/*      */         }
/*      */         else
/*      */         {
/* 2079 */           for (; i < len - 1; i++) {
/* 2080 */             ch = sb.charAt(i + 1);
/* 2081 */             if ((ch != '\t') && (ch != '\n') && (ch != '\r') && (ch != ' ')) {
/*      */               break;
/*      */             }
/*      */           }
/* 2085 */           if ((i < len - 1) && (!isLeading))
/* 2086 */             sb.setCharAt(j++, ' ');
/*      */         }
/*      */       }
/* 2089 */       sb.setLength(j);
/*      */     }
/*      */ 
/* 2092 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   void reportError(String key, Object[] args) throws InvalidDatatypeFacetException {
/* 2096 */     throw new InvalidDatatypeFacetException(key, args);
/*      */   }
/*      */ 
/*      */   private String whiteSpaceValue(short ws)
/*      */   {
/* 2101 */     return WS_FACET_STRING[ws];
/*      */   }
/*      */ 
/*      */   public short getOrdered()
/*      */   {
/* 2108 */     return this.fOrdered;
/*      */   }
/*      */ 
/*      */   public boolean getBounded()
/*      */   {
/* 2115 */     return this.fBounded;
/*      */   }
/*      */ 
/*      */   public boolean getFinite()
/*      */   {
/* 2122 */     return this.fFinite;
/*      */   }
/*      */ 
/*      */   public boolean getNumeric()
/*      */   {
/* 2129 */     return this.fNumeric;
/*      */   }
/*      */ 
/*      */   public boolean isDefinedFacet(short facetName)
/*      */   {
/* 2139 */     if ((this.fValidationDV == 0) || (this.fValidationDV == 29))
/*      */     {
/* 2141 */       return false;
/*      */     }
/* 2143 */     if ((this.fFacetsDefined & facetName) != 0) {
/* 2144 */       return true;
/*      */     }
/* 2146 */     if (this.fPatternType != 0) {
/* 2147 */       return facetName == 8;
/*      */     }
/* 2149 */     if (this.fValidationDV == 24) {
/* 2150 */       return (facetName == 8) || (facetName == 1024);
/*      */     }
/* 2152 */     return false;
/*      */   }
/*      */ 
/*      */   public short getDefinedFacets()
/*      */   {
/* 2160 */     if ((this.fValidationDV == 0) || (this.fValidationDV == 29))
/*      */     {
/* 2162 */       return 0;
/*      */     }
/* 2164 */     if (this.fPatternType != 0) {
/* 2165 */       return (short)(this.fFacetsDefined | 0x8);
/*      */     }
/* 2167 */     if (this.fValidationDV == 24) {
/* 2168 */       return (short)(this.fFacetsDefined | 0x8 | 0x400);
/*      */     }
/* 2170 */     return this.fFacetsDefined;
/*      */   }
/*      */ 
/*      */   public boolean isFixedFacet(short facetName)
/*      */   {
/* 2180 */     if ((this.fFixedFacet & facetName) != 0)
/* 2181 */       return true;
/* 2182 */     if (this.fValidationDV == 24)
/* 2183 */       return facetName == 1024;
/* 2184 */     return false;
/*      */   }
/*      */ 
/*      */   public short getFixedFacets()
/*      */   {
/* 2191 */     if (this.fValidationDV == 24)
/* 2192 */       return (short)(this.fFixedFacet | 0x400);
/* 2193 */     return this.fFixedFacet;
/*      */   }
/*      */ 
/*      */   public String getLexicalFacetValue(short facetName)
/*      */   {
/* 2209 */     switch (facetName) {
/*      */     case 1:
/* 2211 */       return this.fLength == -1 ? null : Integer.toString(this.fLength);
/*      */     case 2:
/* 2213 */       return this.fMinLength == -1 ? null : Integer.toString(this.fMinLength);
/*      */     case 4:
/* 2215 */       return this.fMaxLength == -1 ? null : Integer.toString(this.fMaxLength);
/*      */     case 16:
/* 2217 */       if ((this.fValidationDV == 0) || (this.fValidationDV == 29))
/*      */       {
/* 2219 */         return null;
/*      */       }
/* 2221 */       return WS_FACET_STRING[this.fWhiteSpace];
/*      */     case 32:
/* 2223 */       return this.fMaxInclusive == null ? null : this.fMaxInclusive.toString();
/*      */     case 64:
/* 2225 */       return this.fMaxExclusive == null ? null : this.fMaxExclusive.toString();
/*      */     case 128:
/* 2227 */       return this.fMinExclusive == null ? null : this.fMinExclusive.toString();
/*      */     case 256:
/* 2229 */       return this.fMinInclusive == null ? null : this.fMinInclusive.toString();
/*      */     case 512:
/* 2231 */       return this.fTotalDigits == -1 ? null : Integer.toString(this.fTotalDigits);
/*      */     case 1024:
/* 2233 */       if (this.fValidationDV == 24) {
/* 2234 */         return "0";
/*      */       }
/* 2236 */       return this.fFractionDigits == -1 ? null : Integer.toString(this.fFractionDigits);
/*      */     }
/* 2238 */     return null;
/*      */   }
/*      */ 
/*      */   public StringList getLexicalEnumeration()
/*      */   {
/* 2246 */     if (this.fLexicalEnumeration == null) {
/* 2247 */       if (this.fEnumeration == null)
/* 2248 */         return StringListImpl.EMPTY_LIST;
/* 2249 */       int size = this.fEnumeration.size();
/* 2250 */       String[] strs = new String[size];
/* 2251 */       for (int i = 0; i < size; i++)
/* 2252 */         strs[i] = this.fEnumeration.elementAt(i).toString();
/* 2253 */       this.fLexicalEnumeration = new StringListImpl(strs, size);
/*      */     }
/* 2255 */     return this.fLexicalEnumeration; } 
/*      */   public ObjectList getActualEnumeration() { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 1060	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl:fActualEnumeration	Lcom/sun/org/apache/xerces/internal/xs/datatypes/ObjectList;
/*      */     //   4: ifnonnull +15 -> 19
/*      */     //   7: aload_0
/*      */     //   8: new 677	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl$2
/*      */     //   11: dup
/*      */     //   12: aload_0
/*      */     //   13: invokespecial 1160	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl$2:<init>	(Lcom/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl;)V
/*      */     //   16: putfield 1060	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl:fActualEnumeration	Lcom/sun/org/apache/xerces/internal/xs/datatypes/ObjectList;
/*      */     //   19: aload_0
/*      */     //   20: getfield 1060	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl:fActualEnumeration	Lcom/sun/org/apache/xerces/internal/xs/datatypes/ObjectList;
/*      */     //   23: areturn } 
/*      */   public ObjectList getEnumerationItemTypeList() { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 1061	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl:fEnumerationItemTypeList	Lcom/sun/org/apache/xerces/internal/xs/datatypes/ObjectList;
/*      */     //   4: ifnonnull +24 -> 28
/*      */     //   7: aload_0
/*      */     //   8: getfield 1044	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl:fEnumerationItemType	[Lcom/sun/org/apache/xerces/internal/xs/ShortList;
/*      */     //   11: ifnonnull +5 -> 16
/*      */     //   14: aconst_null
/*      */     //   15: areturn
/*      */     //   16: aload_0
/*      */     //   17: new 678	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl$3
/*      */     //   20: dup
/*      */     //   21: aload_0
/*      */     //   22: invokespecial 1161	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl$3:<init>	(Lcom/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl;)V
/*      */     //   25: putfield 1061	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl:fEnumerationItemTypeList	Lcom/sun/org/apache/xerces/internal/xs/datatypes/ObjectList;
/*      */     //   28: aload_0
/*      */     //   29: getfield 1061	com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl:fEnumerationItemTypeList	Lcom/sun/org/apache/xerces/internal/xs/datatypes/ObjectList;
/*      */     //   32: areturn } 
/* 2314 */   public ShortList getEnumerationTypeList() { if (this.fEnumerationTypeList == null) {
/* 2315 */       if (this.fEnumerationType == null) {
/* 2316 */         return ShortListImpl.EMPTY_LIST;
/*      */       }
/* 2318 */       this.fEnumerationTypeList = new ShortListImpl(this.fEnumerationType, this.fEnumerationType.length);
/*      */     }
/* 2320 */     return this.fEnumerationTypeList;
/*      */   }
/*      */ 
/*      */   public StringList getLexicalPattern()
/*      */   {
/* 2328 */     if ((this.fPatternType == 0) && (this.fValidationDV != 24) && (this.fPatternStr == null))
/* 2329 */       return StringListImpl.EMPTY_LIST;
/* 2330 */     if (this.fLexicalPattern == null) {
/* 2331 */       int size = this.fPatternStr == null ? 0 : this.fPatternStr.size();
/*      */       String[] strs;
/* 2333 */       if (this.fPatternType == 1) {
/* 2334 */         String[] strs = new String[size + 1];
/* 2335 */         strs[size] = "\\c+";
/*      */       }
/* 2337 */       else if (this.fPatternType == 2) {
/* 2338 */         String[] strs = new String[size + 1];
/* 2339 */         strs[size] = "\\i\\c*";
/*      */       }
/* 2341 */       else if (this.fPatternType == 3) {
/* 2342 */         String[] strs = new String[size + 2];
/* 2343 */         strs[size] = "\\i\\c*";
/* 2344 */         strs[(size + 1)] = "[\\i-[:]][\\c-[:]]*";
/*      */       }
/* 2346 */       else if (this.fValidationDV == 24) {
/* 2347 */         String[] strs = new String[size + 1];
/* 2348 */         strs[size] = "[\\-+]?[0-9]+";
/*      */       }
/*      */       else {
/* 2351 */         strs = new String[size];
/*      */       }
/* 2353 */       for (int i = 0; i < size; i++)
/* 2354 */         strs[i] = ((String)this.fPatternStr.elementAt(i));
/* 2355 */       this.fLexicalPattern = new StringListImpl(strs, strs.length);
/*      */     }
/* 2357 */     return this.fLexicalPattern;
/*      */   }
/*      */ 
/*      */   public XSObjectList getAnnotations()
/*      */   {
/* 2365 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*      */   }
/*      */ 
/*      */   private void calcFundamentalFacets() {
/* 2369 */     setOrdered();
/* 2370 */     setNumeric();
/* 2371 */     setBounded();
/* 2372 */     setCardinality();
/*      */   }
/*      */ 
/*      */   private void setOrdered()
/*      */   {
/* 2378 */     if (this.fVariety == 1) {
/* 2379 */       this.fOrdered = this.fBase.fOrdered;
/*      */     }
/* 2383 */     else if (this.fVariety == 2) {
/* 2384 */       this.fOrdered = 0;
/*      */     }
/* 2390 */     else if (this.fVariety == 3) {
/* 2391 */       int length = this.fMemberTypes.length;
/*      */ 
/* 2393 */       if (length == 0) {
/* 2394 */         this.fOrdered = 1;
/* 2395 */         return;
/*      */       }
/*      */ 
/* 2398 */       short ancestorId = getPrimitiveDV(this.fMemberTypes[0].fValidationDV);
/* 2399 */       boolean commonAnc = ancestorId != 0;
/* 2400 */       boolean allFalse = this.fMemberTypes[0].fOrdered == 0;
/*      */ 
/* 2403 */       for (int i = 1; (i < this.fMemberTypes.length) && ((commonAnc) || (allFalse)); i++) {
/* 2404 */         if (commonAnc)
/* 2405 */           commonAnc = ancestorId == getPrimitiveDV(this.fMemberTypes[i].fValidationDV);
/* 2406 */         if (allFalse)
/* 2407 */           allFalse = this.fMemberTypes[i].fOrdered == 0;
/*      */       }
/* 2409 */       if (commonAnc)
/*      */       {
/* 2412 */         this.fOrdered = this.fMemberTypes[0].fOrdered;
/* 2413 */       } else if (allFalse)
/* 2414 */         this.fOrdered = 0;
/*      */       else
/* 2416 */         this.fOrdered = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setNumeric()
/*      */   {
/* 2423 */     if (this.fVariety == 1) {
/* 2424 */       this.fNumeric = this.fBase.fNumeric;
/*      */     }
/* 2426 */     else if (this.fVariety == 2) {
/* 2427 */       this.fNumeric = false;
/*      */     }
/* 2429 */     else if (this.fVariety == 3) {
/* 2430 */       XSSimpleType[] memberTypes = this.fMemberTypes;
/* 2431 */       for (int i = 0; i < memberTypes.length; i++) {
/* 2432 */         if (!memberTypes[i].getNumeric()) {
/* 2433 */           this.fNumeric = false;
/* 2434 */           return;
/*      */         }
/*      */       }
/* 2437 */       this.fNumeric = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setBounded()
/*      */   {
/* 2443 */     if (this.fVariety == 1) {
/* 2444 */       if ((((this.fFacetsDefined & 0x100) != 0) || ((this.fFacetsDefined & 0x80) != 0)) && (((this.fFacetsDefined & 0x20) != 0) || ((this.fFacetsDefined & 0x40) != 0)))
/*      */       {
/* 2446 */         this.fBounded = true;
/*      */       }
/*      */       else {
/* 2449 */         this.fBounded = false;
/*      */       }
/*      */     }
/* 2452 */     else if (this.fVariety == 2) {
/* 2453 */       if (((this.fFacetsDefined & 0x1) != 0) || (((this.fFacetsDefined & 0x2) != 0) && ((this.fFacetsDefined & 0x4) != 0)))
/*      */       {
/* 2455 */         this.fBounded = true;
/*      */       }
/*      */       else {
/* 2458 */         this.fBounded = false;
/*      */       }
/*      */ 
/*      */     }
/* 2462 */     else if (this.fVariety == 3)
/*      */     {
/* 2464 */       XSSimpleTypeDecl[] memberTypes = this.fMemberTypes;
/* 2465 */       short ancestorId = 0;
/*      */ 
/* 2467 */       if (memberTypes.length > 0) {
/* 2468 */         ancestorId = getPrimitiveDV(memberTypes[0].fValidationDV);
/*      */       }
/*      */ 
/* 2471 */       for (int i = 0; i < memberTypes.length; i++) {
/* 2472 */         if ((!memberTypes[i].getBounded()) || (ancestorId != getPrimitiveDV(memberTypes[i].fValidationDV))) {
/* 2473 */           this.fBounded = false;
/* 2474 */           return;
/*      */         }
/*      */       }
/* 2477 */       this.fBounded = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean specialCardinalityCheck()
/*      */   {
/* 2483 */     if ((this.fBase.fValidationDV == 9) || (this.fBase.fValidationDV == 10) || (this.fBase.fValidationDV == 11) || (this.fBase.fValidationDV == 12) || (this.fBase.fValidationDV == 13) || (this.fBase.fValidationDV == 14))
/*      */     {
/* 2486 */       return true;
/*      */     }
/* 2488 */     return false;
/*      */   }
/*      */ 
/*      */   private void setCardinality()
/*      */   {
/* 2493 */     if (this.fVariety == 1) {
/* 2494 */       if (this.fBase.fFinite) {
/* 2495 */         this.fFinite = true;
/*      */       }
/* 2498 */       else if (((this.fFacetsDefined & 0x1) != 0) || ((this.fFacetsDefined & 0x4) != 0) || ((this.fFacetsDefined & 0x200) != 0))
/*      */       {
/* 2500 */         this.fFinite = true;
/*      */       }
/* 2502 */       else if ((((this.fFacetsDefined & 0x100) != 0) || ((this.fFacetsDefined & 0x80) != 0)) && (((this.fFacetsDefined & 0x20) != 0) || ((this.fFacetsDefined & 0x40) != 0)))
/*      */       {
/* 2504 */         if (((this.fFacetsDefined & 0x400) != 0) || (specialCardinalityCheck())) {
/* 2505 */           this.fFinite = true;
/*      */         }
/*      */         else {
/* 2508 */           this.fFinite = false;
/*      */         }
/*      */       }
/*      */       else {
/* 2512 */         this.fFinite = false;
/*      */       }
/*      */ 
/*      */     }
/* 2516 */     else if (this.fVariety == 2) {
/* 2517 */       if (((this.fFacetsDefined & 0x1) != 0) || (((this.fFacetsDefined & 0x2) != 0) && ((this.fFacetsDefined & 0x4) != 0)))
/*      */       {
/* 2519 */         this.fFinite = true;
/*      */       }
/*      */       else {
/* 2522 */         this.fFinite = false;
/*      */       }
/*      */ 
/*      */     }
/* 2526 */     else if (this.fVariety == 3) {
/* 2527 */       XSSimpleType[] memberTypes = this.fMemberTypes;
/* 2528 */       for (int i = 0; i < memberTypes.length; i++) {
/* 2529 */         if (!memberTypes[i].getFinite()) {
/* 2530 */           this.fFinite = false;
/* 2531 */           return;
/*      */         }
/*      */       }
/* 2534 */       this.fFinite = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private short getPrimitiveDV(short validationDV)
/*      */   {
/* 2541 */     if ((validationDV == 21) || (validationDV == 22) || (validationDV == 23)) {
/* 2542 */       return 1;
/*      */     }
/* 2544 */     if (validationDV == 24) {
/* 2545 */       return 3;
/*      */     }
/*      */ 
/* 2551 */     return validationDV;
/*      */   }
/*      */ 
/*      */   public boolean derivedFromType(XSTypeDefinition ancestor, short derivation)
/*      */   {
/* 2560 */     if (ancestor == null) {
/* 2561 */       return false;
/*      */     }
/*      */ 
/* 2564 */     while ((ancestor instanceof XSSimpleTypeDelegate)) {
/* 2565 */       ancestor = ((XSSimpleTypeDelegate)ancestor).type;
/*      */     }
/*      */ 
/* 2569 */     if (ancestor.getBaseType() == ancestor) {
/* 2570 */       return true;
/*      */     }
/*      */ 
/* 2573 */     XSTypeDefinition type = this;
/* 2574 */     while ((type != ancestor) && (type != fAnySimpleType))
/*      */     {
/* 2576 */       type = type.getBaseType();
/*      */     }
/* 2578 */     return type == ancestor;
/*      */   }
/*      */ 
/*      */   public boolean derivedFrom(String ancestorNS, String ancestorName, short derivation)
/*      */   {
/* 2585 */     if (ancestorName == null) {
/* 2586 */       return false;
/*      */     }
/* 2588 */     if (("http://www.w3.org/2001/XMLSchema".equals(ancestorNS)) && ("anyType".equals(ancestorName)))
/*      */     {
/* 2590 */       return true;
/*      */     }
/*      */ 
/* 2594 */     XSTypeDefinition type = this;
/*      */ 
/* 2597 */     while (((!ancestorName.equals(type.getName())) || (((ancestorNS != null) || (type.getNamespace() != null)) && ((ancestorNS == null) || (!ancestorNS.equals(type.getNamespace()))))) && (type != fAnySimpleType))
/*      */     {
/* 2599 */       type = type.getBaseType();
/*      */     }
/*      */ 
/* 2602 */     return type != fAnySimpleType;
/*      */   }
/*      */ 
/*      */   public boolean isDOMDerivedFrom(String ancestorNS, String ancestorName, int derivationMethod)
/*      */   {
/* 2622 */     if (ancestorName == null) {
/* 2623 */       return false;
/*      */     }
/*      */ 
/* 2626 */     if ((SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(ancestorNS)) && ("anyType".equals(ancestorName)) && (((derivationMethod & 0x1) != 0) || (derivationMethod == 0)))
/*      */     {
/* 2630 */       return true;
/*      */     }
/*      */ 
/* 2634 */     if (((derivationMethod & 0x1) != 0) && 
/* 2635 */       (isDerivedByRestriction(ancestorNS, ancestorName, this))) {
/* 2636 */       return true;
/*      */     }
/*      */ 
/* 2641 */     if (((derivationMethod & 0x8) != 0) && 
/* 2642 */       (isDerivedByList(ancestorNS, ancestorName, this))) {
/* 2643 */       return true;
/*      */     }
/*      */ 
/* 2648 */     if (((derivationMethod & 0x4) != 0) && 
/* 2649 */       (isDerivedByUnion(ancestorNS, ancestorName, this))) {
/* 2650 */       return true;
/*      */     }
/*      */ 
/* 2655 */     if (((derivationMethod & 0x2) != 0) && ((derivationMethod & 0x1) == 0) && ((derivationMethod & 0x8) == 0) && ((derivationMethod & 0x4) == 0))
/*      */     {
/* 2659 */       return false;
/*      */     }
/*      */ 
/* 2665 */     if (((derivationMethod & 0x2) == 0) && ((derivationMethod & 0x1) == 0) && ((derivationMethod & 0x8) == 0) && ((derivationMethod & 0x4) == 0))
/*      */     {
/* 2669 */       return isDerivedByAny(ancestorNS, ancestorName, this);
/*      */     }
/*      */ 
/* 2672 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isDerivedByAny(String ancestorNS, String ancestorName, XSTypeDefinition type)
/*      */   {
/* 2692 */     boolean derivedFrom = false;
/* 2693 */     XSTypeDefinition oldType = null;
/*      */ 
/* 2695 */     while ((type != null) && (type != oldType))
/*      */     {
/* 2698 */       if ((ancestorName.equals(type.getName())) && (((ancestorNS == null) && (type.getNamespace() == null)) || ((ancestorNS != null) && (ancestorNS.equals(type.getNamespace())))))
/*      */       {
/* 2701 */         derivedFrom = true;
/* 2702 */         break;
/*      */       }
/*      */ 
/* 2706 */       if (isDerivedByRestriction(ancestorNS, ancestorName, type))
/* 2707 */         return true;
/* 2708 */       if (isDerivedByList(ancestorNS, ancestorName, type))
/* 2709 */         return true;
/* 2710 */       if (isDerivedByUnion(ancestorNS, ancestorName, type)) {
/* 2711 */         return true;
/*      */       }
/* 2713 */       oldType = type;
/*      */ 
/* 2715 */       if ((((XSSimpleTypeDecl)type).getVariety() == 0) || (((XSSimpleTypeDecl)type).getVariety() == 1))
/*      */       {
/* 2717 */         type = type.getBaseType();
/* 2718 */       } else if (((XSSimpleTypeDecl)type).getVariety() == 3) {
/* 2719 */         int i = 0; if (i < ((XSSimpleTypeDecl)type).getMemberTypes().getLength()) {
/* 2720 */           return isDerivedByAny(ancestorNS, ancestorName, (XSTypeDefinition)((XSSimpleTypeDecl)type).getMemberTypes().item(i));
/*      */         }
/*      */ 
/*      */       }
/* 2724 */       else if (((XSSimpleTypeDecl)type).getVariety() == 2) {
/* 2725 */         type = ((XSSimpleTypeDecl)type).getItemType();
/*      */       }
/*      */     }
/*      */ 
/* 2729 */     return derivedFrom;
/*      */   }
/*      */ 
/*      */   private boolean isDerivedByRestriction(String ancestorNS, String ancestorName, XSTypeDefinition type)
/*      */   {
/* 2748 */     XSTypeDefinition oldType = null;
/* 2749 */     while ((type != null) && (type != oldType)) {
/* 2750 */       if ((ancestorName.equals(type.getName())) && (((ancestorNS != null) && (ancestorNS.equals(type.getNamespace()))) || ((type.getNamespace() == null) && (ancestorNS == null))))
/*      */       {
/* 2754 */         return true;
/*      */       }
/* 2756 */       oldType = type;
/* 2757 */       type = type.getBaseType();
/*      */     }
/*      */ 
/* 2760 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isDerivedByList(String ancestorNS, String ancestorName, XSTypeDefinition type)
/*      */   {
/* 2778 */     if ((type != null) && (((XSSimpleTypeDefinition)type).getVariety() == 2))
/*      */     {
/* 2781 */       XSTypeDefinition itemType = ((XSSimpleTypeDefinition)type).getItemType();
/*      */ 
/* 2784 */       if (itemType != null)
/*      */       {
/* 2787 */         if (isDerivedByRestriction(ancestorNS, ancestorName, itemType)) {
/* 2788 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 2792 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isDerivedByUnion(String ancestorNS, String ancestorName, XSTypeDefinition type)
/*      */   {
/* 2811 */     if ((type != null) && (((XSSimpleTypeDefinition)type).getVariety() == 3))
/*      */     {
/* 2814 */       XSObjectList memberTypes = ((XSSimpleTypeDefinition)type).getMemberTypes();
/*      */ 
/* 2816 */       for (int i = 0; i < memberTypes.getLength(); i++)
/*      */       {
/* 2818 */         if (memberTypes.item(i) != null)
/*      */         {
/* 2820 */           if (isDerivedByRestriction(ancestorNS, ancestorName, (XSSimpleTypeDefinition)memberTypes.item(i))) {
/* 2821 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2826 */     return false;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/* 2958 */     if (this.fIsImmutable) return;
/* 2959 */     this.fItemType = null;
/* 2960 */     this.fMemberTypes = null;
/*      */ 
/* 2962 */     this.fTypeName = null;
/* 2963 */     this.fTargetNamespace = null;
/* 2964 */     this.fFinalSet = 0;
/* 2965 */     this.fBase = null;
/* 2966 */     this.fVariety = -1;
/* 2967 */     this.fValidationDV = -1;
/*      */ 
/* 2969 */     this.fFacetsDefined = 0;
/* 2970 */     this.fFixedFacet = 0;
/*      */ 
/* 2973 */     this.fWhiteSpace = 0;
/* 2974 */     this.fLength = -1;
/* 2975 */     this.fMinLength = -1;
/* 2976 */     this.fMaxLength = -1;
/* 2977 */     this.fTotalDigits = -1;
/* 2978 */     this.fFractionDigits = -1;
/* 2979 */     this.fPattern = null;
/* 2980 */     this.fPatternStr = null;
/* 2981 */     this.fEnumeration = null;
/* 2982 */     this.fEnumerationType = null;
/* 2983 */     this.fEnumerationItemType = null;
/* 2984 */     this.fLexicalPattern = null;
/* 2985 */     this.fLexicalEnumeration = null;
/* 2986 */     this.fMaxInclusive = null;
/* 2987 */     this.fMaxExclusive = null;
/* 2988 */     this.fMinExclusive = null;
/* 2989 */     this.fMinInclusive = null;
/* 2990 */     this.lengthAnnotation = null;
/* 2991 */     this.minLengthAnnotation = null;
/* 2992 */     this.maxLengthAnnotation = null;
/* 2993 */     this.whiteSpaceAnnotation = null;
/* 2994 */     this.totalDigitsAnnotation = null;
/* 2995 */     this.fractionDigitsAnnotation = null;
/* 2996 */     this.patternAnnotations = null;
/* 2997 */     this.enumerationAnnotations = null;
/* 2998 */     this.maxInclusiveAnnotation = null;
/* 2999 */     this.maxExclusiveAnnotation = null;
/* 3000 */     this.minInclusiveAnnotation = null;
/* 3001 */     this.minExclusiveAnnotation = null;
/*      */ 
/* 3003 */     this.fPatternType = 0;
/* 3004 */     this.fAnnotations = null;
/* 3005 */     this.fFacets = null;
/*      */   }
/*      */ 
/*      */   public XSNamespaceItem getNamespaceItem()
/*      */   {
/* 3014 */     return this.fNamespaceItem;
/*      */   }
/*      */ 
/*      */   public void setNamespaceItem(XSNamespaceItem namespaceItem) {
/* 3018 */     this.fNamespaceItem = namespaceItem;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 3025 */     return this.fTargetNamespace + "," + this.fTypeName;
/*      */   }
/*      */ 
/*      */   public XSObjectList getFacets()
/*      */   {
/* 3035 */     if ((this.fFacets == null) && ((this.fFacetsDefined != 0) || (this.fValidationDV == 24)))
/*      */     {
/* 3038 */       XSFacetImpl[] facets = new XSFacetImpl[10];
/* 3039 */       int count = 0;
/* 3040 */       if (((this.fFacetsDefined & 0x10) != 0) && (this.fValidationDV != 0) && (this.fValidationDV != 29))
/*      */       {
/* 3043 */         facets[count] = new XSFacetImpl(16, WS_FACET_STRING[this.fWhiteSpace], (this.fFixedFacet & 0x10) != 0, this.whiteSpaceAnnotation);
/*      */ 
/* 3049 */         count++;
/*      */       }
/* 3051 */       if (this.fLength != -1) {
/* 3052 */         facets[count] = new XSFacetImpl(1, Integer.toString(this.fLength), (this.fFixedFacet & 0x1) != 0, this.lengthAnnotation);
/*      */ 
/* 3058 */         count++;
/*      */       }
/* 3060 */       if (this.fMinLength != -1) {
/* 3061 */         facets[count] = new XSFacetImpl(2, Integer.toString(this.fMinLength), (this.fFixedFacet & 0x2) != 0, this.minLengthAnnotation);
/*      */ 
/* 3067 */         count++;
/*      */       }
/* 3069 */       if (this.fMaxLength != -1) {
/* 3070 */         facets[count] = new XSFacetImpl(4, Integer.toString(this.fMaxLength), (this.fFixedFacet & 0x4) != 0, this.maxLengthAnnotation);
/*      */ 
/* 3076 */         count++;
/*      */       }
/* 3078 */       if (this.fTotalDigits != -1) {
/* 3079 */         facets[count] = new XSFacetImpl(512, Integer.toString(this.fTotalDigits), (this.fFixedFacet & 0x200) != 0, this.totalDigitsAnnotation);
/*      */ 
/* 3085 */         count++;
/*      */       }
/* 3087 */       if (this.fValidationDV == 24) {
/* 3088 */         facets[count] = new XSFacetImpl(1024, "0", true, this.fractionDigitsAnnotation);
/*      */ 
/* 3094 */         count++;
/*      */       }
/* 3096 */       else if (this.fFractionDigits != -1) {
/* 3097 */         facets[count] = new XSFacetImpl(1024, Integer.toString(this.fFractionDigits), (this.fFixedFacet & 0x400) != 0, this.fractionDigitsAnnotation);
/*      */ 
/* 3103 */         count++;
/*      */       }
/* 3105 */       if (this.fMaxInclusive != null) {
/* 3106 */         facets[count] = new XSFacetImpl(32, this.fMaxInclusive.toString(), (this.fFixedFacet & 0x20) != 0, this.maxInclusiveAnnotation);
/*      */ 
/* 3112 */         count++;
/*      */       }
/* 3114 */       if (this.fMaxExclusive != null) {
/* 3115 */         facets[count] = new XSFacetImpl(64, this.fMaxExclusive.toString(), (this.fFixedFacet & 0x40) != 0, this.maxExclusiveAnnotation);
/*      */ 
/* 3121 */         count++;
/*      */       }
/* 3123 */       if (this.fMinExclusive != null) {
/* 3124 */         facets[count] = new XSFacetImpl(128, this.fMinExclusive.toString(), (this.fFixedFacet & 0x80) != 0, this.minExclusiveAnnotation);
/*      */ 
/* 3130 */         count++;
/*      */       }
/* 3132 */       if (this.fMinInclusive != null) {
/* 3133 */         facets[count] = new XSFacetImpl(256, this.fMinInclusive.toString(), (this.fFixedFacet & 0x100) != 0, this.minInclusiveAnnotation);
/*      */ 
/* 3139 */         count++;
/*      */       }
/* 3141 */       this.fFacets = (count > 0 ? new XSObjectListImpl(facets, count) : XSObjectListImpl.EMPTY_LIST);
/*      */     }
/* 3143 */     return this.fFacets != null ? this.fFacets : XSObjectListImpl.EMPTY_LIST;
/*      */   }
/*      */ 
/*      */   public XSObjectList getMultiValueFacets()
/*      */   {
/* 3151 */     if ((this.fMultiValueFacets == null) && (((this.fFacetsDefined & 0x800) != 0) || ((this.fFacetsDefined & 0x8) != 0) || (this.fPatternType != 0) || (this.fValidationDV == 24)))
/*      */     {
/* 3157 */       XSMVFacetImpl[] facets = new XSMVFacetImpl[2];
/* 3158 */       int count = 0;
/* 3159 */       if (((this.fFacetsDefined & 0x8) != 0) || (this.fPatternType != 0) || (this.fValidationDV == 24))
/*      */       {
/* 3162 */         facets[count] = new XSMVFacetImpl(8, getLexicalPattern(), this.patternAnnotations);
/*      */ 
/* 3167 */         count++;
/*      */       }
/* 3169 */       if (this.fEnumeration != null) {
/* 3170 */         facets[count] = new XSMVFacetImpl(2048, getLexicalEnumeration(), this.enumerationAnnotations);
/*      */ 
/* 3175 */         count++;
/*      */       }
/* 3177 */       this.fMultiValueFacets = new XSObjectListImpl(facets, count);
/*      */     }
/* 3179 */     return this.fMultiValueFacets != null ? this.fMultiValueFacets : XSObjectListImpl.EMPTY_LIST;
/*      */   }
/*      */ 
/*      */   public Object getMinInclusiveValue()
/*      */   {
/* 3184 */     return this.fMinInclusive;
/*      */   }
/*      */ 
/*      */   public Object getMinExclusiveValue() {
/* 3188 */     return this.fMinExclusive;
/*      */   }
/*      */ 
/*      */   public Object getMaxInclusiveValue() {
/* 3192 */     return this.fMaxInclusive;
/*      */   }
/*      */ 
/*      */   public Object getMaxExclusiveValue() {
/* 3196 */     return this.fMaxExclusive;
/*      */   }
/*      */ 
/*      */   public void setAnonymous(boolean anon) {
/* 3200 */     this.fAnonymous = anon;
/*      */   }
/*      */ 
/*      */   public String getTypeNamespace()
/*      */   {
/* 3374 */     return getNamespace();
/*      */   }
/*      */ 
/*      */   public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod) {
/* 3378 */     return isDOMDerivedFrom(typeNamespaceArg, typeNameArg, derivationMethod);
/*      */   }
/*      */ 
/*      */   private short convertToPrimitiveKind(short valueType)
/*      */   {
/* 3383 */     if (valueType <= 20) {
/* 3384 */       return valueType;
/*      */     }
/*      */ 
/* 3387 */     if (valueType <= 29) {
/* 3388 */       return 2;
/*      */     }
/*      */ 
/* 3391 */     if (valueType <= 42) {
/* 3392 */       return 4;
/*      */     }
/*      */ 
/* 3395 */     return valueType;
/*      */   }
/*      */ 
/*      */   private static abstract class AbstractObjectList extends AbstractList
/*      */     implements ObjectList
/*      */   {
/*      */     public Object get(int index)
/*      */     {
/* 3363 */       if ((index >= 0) && (index < getLength())) {
/* 3364 */         return item(index);
/*      */       }
/* 3366 */       throw new IndexOutOfBoundsException("Index: " + index);
/*      */     }
/*      */     public int size() {
/* 3369 */       return getLength();
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class ValidationContextImpl
/*      */     implements ValidationContext
/*      */   {
/*      */     final ValidationContext fExternal;
/*      */     NamespaceContext fNSContext;
/*      */ 
/*      */     ValidationContextImpl(ValidationContext external)
/*      */     {
/* 2894 */       this.fExternal = external;
/*      */     }
/*      */ 
/*      */     void setNSContext(NamespaceContext nsContext)
/*      */     {
/* 2899 */       this.fNSContext = nsContext;
/*      */     }
/*      */ 
/*      */     public boolean needFacetChecking() {
/* 2903 */       return this.fExternal.needFacetChecking();
/*      */     }
/*      */ 
/*      */     public boolean needExtraChecking() {
/* 2907 */       return this.fExternal.needExtraChecking();
/*      */     }
/*      */     public boolean needToNormalize() {
/* 2910 */       return this.fExternal.needToNormalize();
/*      */     }
/*      */ 
/*      */     public boolean useNamespaces() {
/* 2914 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isEntityDeclared(String name) {
/* 2918 */       return this.fExternal.isEntityDeclared(name);
/*      */     }
/*      */ 
/*      */     public boolean isEntityUnparsed(String name) {
/* 2922 */       return this.fExternal.isEntityUnparsed(name);
/*      */     }
/*      */ 
/*      */     public boolean isIdDeclared(String name) {
/* 2926 */       return this.fExternal.isIdDeclared(name);
/*      */     }
/*      */ 
/*      */     public void addId(String name) {
/* 2930 */       this.fExternal.addId(name);
/*      */     }
/*      */ 
/*      */     public void addIdRef(String name) {
/* 2934 */       this.fExternal.addIdRef(name);
/*      */     }
/*      */ 
/*      */     public String getSymbol(String symbol) {
/* 2938 */       return this.fExternal.getSymbol(symbol);
/*      */     }
/*      */ 
/*      */     public String getURI(String prefix) {
/* 2942 */       if (this.fNSContext == null) {
/* 2943 */         return this.fExternal.getURI(prefix);
/*      */       }
/*      */ 
/* 2946 */       return this.fNSContext.getURI(prefix);
/*      */     }
/*      */ 
/*      */     public Locale getLocale()
/*      */     {
/* 2951 */       return this.fExternal.getLocale();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class XSFacetImpl
/*      */     implements XSFacet
/*      */   {
/*      */     final short kind;
/*      */     final String value;
/*      */     final boolean fixed;
/*      */     final XSObjectList annotations;
/*      */ 
/*      */     public XSFacetImpl(short kind, String value, boolean fixed, XSAnnotation annotation)
/*      */     {
/* 3210 */       this.kind = kind;
/* 3211 */       this.value = value;
/* 3212 */       this.fixed = fixed;
/*      */ 
/* 3214 */       if (annotation != null) {
/* 3215 */         this.annotations = new XSObjectListImpl();
/* 3216 */         ((XSObjectListImpl)this.annotations).addXSObject(annotation);
/*      */       }
/*      */       else {
/* 3219 */         this.annotations = XSObjectListImpl.EMPTY_LIST;
/*      */       }
/*      */     }
/*      */ 
/*      */     public XSAnnotation getAnnotation()
/*      */     {
/* 3232 */       return (XSAnnotation)this.annotations.item(0);
/*      */     }
/*      */ 
/*      */     public XSObjectList getAnnotations()
/*      */     {
/* 3244 */       return this.annotations;
/*      */     }
/*      */ 
/*      */     public short getFacetKind()
/*      */     {
/* 3251 */       return this.kind;
/*      */     }
/*      */ 
/*      */     public String getLexicalFacetValue()
/*      */     {
/* 3258 */       return this.value;
/*      */     }
/*      */ 
/*      */     public boolean getFixed()
/*      */     {
/* 3265 */       return this.fixed;
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 3272 */       return null;
/*      */     }
/*      */ 
/*      */     public String getNamespace()
/*      */     {
/* 3279 */       return null;
/*      */     }
/*      */ 
/*      */     public XSNamespaceItem getNamespaceItem()
/*      */     {
/* 3287 */       return null;
/*      */     }
/*      */ 
/*      */     public short getType()
/*      */     {
/* 3294 */       return 13;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class XSMVFacetImpl implements XSMultiValueFacet {
/*      */     final short kind;
/*      */     final XSObjectList annotations;
/*      */     final StringList values;
/*      */ 
/* 3305 */     public XSMVFacetImpl(short kind, StringList values, XSObjectList annotations) { this.kind = kind;
/* 3306 */       this.values = values;
/* 3307 */       this.annotations = (annotations != null ? annotations : XSObjectListImpl.EMPTY_LIST);
/*      */     }
/*      */ 
/*      */     public short getFacetKind()
/*      */     {
/* 3314 */       return this.kind;
/*      */     }
/*      */ 
/*      */     public XSObjectList getAnnotations()
/*      */     {
/* 3321 */       return this.annotations;
/*      */     }
/*      */ 
/*      */     public StringList getLexicalFacetValues()
/*      */     {
/* 3328 */       return this.values;
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 3335 */       return null;
/*      */     }
/*      */ 
/*      */     public String getNamespace()
/*      */     {
/* 3342 */       return null;
/*      */     }
/*      */ 
/*      */     public XSNamespaceItem getNamespaceItem()
/*      */     {
/* 3350 */       return null;
/*      */     }
/*      */ 
/*      */     public short getType()
/*      */     {
/* 3357 */       return 14;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl
 * JD-Core Version:    0.6.2
 */