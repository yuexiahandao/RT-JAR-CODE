/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ 
/*     */ public abstract class BaseSchemaDVFactory extends SchemaDVFactory
/*     */ {
/*     */   static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
/*  47 */   protected XSDeclarationPool fDeclPool = null;
/*     */ 
/*     */   protected static void createBuiltInTypes(SymbolHash builtInTypes, XSSimpleTypeDecl baseAtomicType)
/*     */   {
/*  52 */     String ANYSIMPLETYPE = "anySimpleType";
/*  53 */     String ANYURI = "anyURI";
/*  54 */     String BASE64BINARY = "base64Binary";
/*  55 */     String BOOLEAN = "boolean";
/*  56 */     String BYTE = "byte";
/*  57 */     String DATE = "date";
/*  58 */     String DATETIME = "dateTime";
/*  59 */     String DAY = "gDay";
/*  60 */     String DECIMAL = "decimal";
/*  61 */     String DOUBLE = "double";
/*  62 */     String DURATION = "duration";
/*  63 */     String ENTITY = "ENTITY";
/*  64 */     String ENTITIES = "ENTITIES";
/*  65 */     String FLOAT = "float";
/*  66 */     String HEXBINARY = "hexBinary";
/*  67 */     String ID = "ID";
/*  68 */     String IDREF = "IDREF";
/*  69 */     String IDREFS = "IDREFS";
/*  70 */     String INT = "int";
/*  71 */     String INTEGER = "integer";
/*  72 */     String LONG = "long";
/*  73 */     String NAME = "Name";
/*  74 */     String NEGATIVEINTEGER = "negativeInteger";
/*  75 */     String MONTH = "gMonth";
/*  76 */     String MONTHDAY = "gMonthDay";
/*  77 */     String NCNAME = "NCName";
/*  78 */     String NMTOKEN = "NMTOKEN";
/*  79 */     String NMTOKENS = "NMTOKENS";
/*  80 */     String LANGUAGE = "language";
/*  81 */     String NONNEGATIVEINTEGER = "nonNegativeInteger";
/*  82 */     String NONPOSITIVEINTEGER = "nonPositiveInteger";
/*  83 */     String NORMALIZEDSTRING = "normalizedString";
/*  84 */     String NOTATION = "NOTATION";
/*  85 */     String POSITIVEINTEGER = "positiveInteger";
/*  86 */     String QNAME = "QName";
/*  87 */     String SHORT = "short";
/*  88 */     String STRING = "string";
/*  89 */     String TIME = "time";
/*  90 */     String TOKEN = "token";
/*  91 */     String UNSIGNEDBYTE = "unsignedByte";
/*  92 */     String UNSIGNEDINT = "unsignedInt";
/*  93 */     String UNSIGNEDLONG = "unsignedLong";
/*  94 */     String UNSIGNEDSHORT = "unsignedShort";
/*  95 */     String YEAR = "gYear";
/*  96 */     String YEARMONTH = "gYearMonth";
/*     */ 
/*  98 */     XSFacets facets = new XSFacets();
/*     */ 
/* 100 */     builtInTypes.put("anySimpleType", XSSimpleTypeDecl.fAnySimpleType);
/*     */ 
/* 102 */     XSSimpleTypeDecl stringDV = new XSSimpleTypeDecl(baseAtomicType, "string", (short)1, (short)0, false, false, false, true, (short)2);
/* 103 */     builtInTypes.put("string", stringDV);
/* 104 */     builtInTypes.put("boolean", new XSSimpleTypeDecl(baseAtomicType, "boolean", (short)2, (short)0, false, true, false, true, (short)3));
/* 105 */     XSSimpleTypeDecl decimalDV = new XSSimpleTypeDecl(baseAtomicType, "decimal", (short)3, (short)2, false, false, true, true, (short)4);
/* 106 */     builtInTypes.put("decimal", decimalDV);
/*     */ 
/* 108 */     builtInTypes.put("anyURI", new XSSimpleTypeDecl(baseAtomicType, "anyURI", (short)17, (short)0, false, false, false, true, (short)18));
/* 109 */     builtInTypes.put("base64Binary", new XSSimpleTypeDecl(baseAtomicType, "base64Binary", (short)16, (short)0, false, false, false, true, (short)17));
/*     */ 
/* 111 */     XSSimpleTypeDecl durationDV = new XSSimpleTypeDecl(baseAtomicType, "duration", (short)6, (short)1, false, false, false, true, (short)7);
/* 112 */     builtInTypes.put("duration", durationDV);
/*     */ 
/* 114 */     builtInTypes.put("dateTime", new XSSimpleTypeDecl(baseAtomicType, "dateTime", (short)7, (short)1, false, false, false, true, (short)8));
/* 115 */     builtInTypes.put("time", new XSSimpleTypeDecl(baseAtomicType, "time", (short)8, (short)1, false, false, false, true, (short)9));
/* 116 */     builtInTypes.put("date", new XSSimpleTypeDecl(baseAtomicType, "date", (short)9, (short)1, false, false, false, true, (short)10));
/* 117 */     builtInTypes.put("gYearMonth", new XSSimpleTypeDecl(baseAtomicType, "gYearMonth", (short)10, (short)1, false, false, false, true, (short)11));
/* 118 */     builtInTypes.put("gYear", new XSSimpleTypeDecl(baseAtomicType, "gYear", (short)11, (short)1, false, false, false, true, (short)12));
/* 119 */     builtInTypes.put("gMonthDay", new XSSimpleTypeDecl(baseAtomicType, "gMonthDay", (short)12, (short)1, false, false, false, true, (short)13));
/* 120 */     builtInTypes.put("gDay", new XSSimpleTypeDecl(baseAtomicType, "gDay", (short)13, (short)1, false, false, false, true, (short)14));
/* 121 */     builtInTypes.put("gMonth", new XSSimpleTypeDecl(baseAtomicType, "gMonth", (short)14, (short)1, false, false, false, true, (short)15));
/*     */ 
/* 123 */     XSSimpleTypeDecl integerDV = new XSSimpleTypeDecl(decimalDV, "integer", (short)24, (short)2, false, false, true, true, (short)30);
/* 124 */     builtInTypes.put("integer", integerDV);
/*     */ 
/* 126 */     facets.maxInclusive = "0";
/* 127 */     XSSimpleTypeDecl nonPositiveDV = new XSSimpleTypeDecl(integerDV, "nonPositiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)31);
/* 128 */     nonPositiveDV.applyFacets1(facets, (short)32, (short)0);
/* 129 */     builtInTypes.put("nonPositiveInteger", nonPositiveDV);
/*     */ 
/* 131 */     facets.maxInclusive = "-1";
/* 132 */     XSSimpleTypeDecl negativeDV = new XSSimpleTypeDecl(nonPositiveDV, "negativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)32);
/* 133 */     negativeDV.applyFacets1(facets, (short)32, (short)0);
/* 134 */     builtInTypes.put("negativeInteger", negativeDV);
/*     */ 
/* 136 */     facets.maxInclusive = "9223372036854775807";
/* 137 */     facets.minInclusive = "-9223372036854775808";
/* 138 */     XSSimpleTypeDecl longDV = new XSSimpleTypeDecl(integerDV, "long", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)33);
/* 139 */     longDV.applyFacets1(facets, (short)288, (short)0);
/* 140 */     builtInTypes.put("long", longDV);
/*     */ 
/* 142 */     facets.maxInclusive = "2147483647";
/* 143 */     facets.minInclusive = "-2147483648";
/* 144 */     XSSimpleTypeDecl intDV = new XSSimpleTypeDecl(longDV, "int", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)34);
/* 145 */     intDV.applyFacets1(facets, (short)288, (short)0);
/* 146 */     builtInTypes.put("int", intDV);
/*     */ 
/* 148 */     facets.maxInclusive = "32767";
/* 149 */     facets.minInclusive = "-32768";
/* 150 */     XSSimpleTypeDecl shortDV = new XSSimpleTypeDecl(intDV, "short", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)35);
/* 151 */     shortDV.applyFacets1(facets, (short)288, (short)0);
/* 152 */     builtInTypes.put("short", shortDV);
/*     */ 
/* 154 */     facets.maxInclusive = "127";
/* 155 */     facets.minInclusive = "-128";
/* 156 */     XSSimpleTypeDecl byteDV = new XSSimpleTypeDecl(shortDV, "byte", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)36);
/* 157 */     byteDV.applyFacets1(facets, (short)288, (short)0);
/* 158 */     builtInTypes.put("byte", byteDV);
/*     */ 
/* 160 */     facets.minInclusive = "0";
/* 161 */     XSSimpleTypeDecl nonNegativeDV = new XSSimpleTypeDecl(integerDV, "nonNegativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)37);
/* 162 */     nonNegativeDV.applyFacets1(facets, (short)256, (short)0);
/* 163 */     builtInTypes.put("nonNegativeInteger", nonNegativeDV);
/*     */ 
/* 165 */     facets.maxInclusive = "18446744073709551615";
/* 166 */     XSSimpleTypeDecl unsignedLongDV = new XSSimpleTypeDecl(nonNegativeDV, "unsignedLong", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)38);
/* 167 */     unsignedLongDV.applyFacets1(facets, (short)32, (short)0);
/* 168 */     builtInTypes.put("unsignedLong", unsignedLongDV);
/*     */ 
/* 170 */     facets.maxInclusive = "4294967295";
/* 171 */     XSSimpleTypeDecl unsignedIntDV = new XSSimpleTypeDecl(unsignedLongDV, "unsignedInt", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)39);
/* 172 */     unsignedIntDV.applyFacets1(facets, (short)32, (short)0);
/* 173 */     builtInTypes.put("unsignedInt", unsignedIntDV);
/*     */ 
/* 175 */     facets.maxInclusive = "65535";
/* 176 */     XSSimpleTypeDecl unsignedShortDV = new XSSimpleTypeDecl(unsignedIntDV, "unsignedShort", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)40);
/* 177 */     unsignedShortDV.applyFacets1(facets, (short)32, (short)0);
/* 178 */     builtInTypes.put("unsignedShort", unsignedShortDV);
/*     */ 
/* 180 */     facets.maxInclusive = "255";
/* 181 */     XSSimpleTypeDecl unsignedByteDV = new XSSimpleTypeDecl(unsignedShortDV, "unsignedByte", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)41);
/* 182 */     unsignedByteDV.applyFacets1(facets, (short)32, (short)0);
/* 183 */     builtInTypes.put("unsignedByte", unsignedByteDV);
/*     */ 
/* 185 */     facets.minInclusive = "1";
/* 186 */     XSSimpleTypeDecl positiveIntegerDV = new XSSimpleTypeDecl(nonNegativeDV, "positiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)42);
/* 187 */     positiveIntegerDV.applyFacets1(facets, (short)256, (short)0);
/* 188 */     builtInTypes.put("positiveInteger", positiveIntegerDV);
/*     */ 
/* 190 */     builtInTypes.put("float", new XSSimpleTypeDecl(baseAtomicType, "float", (short)4, (short)1, true, true, true, true, (short)5));
/* 191 */     builtInTypes.put("double", new XSSimpleTypeDecl(baseAtomicType, "double", (short)5, (short)1, true, true, true, true, (short)6));
/* 192 */     builtInTypes.put("hexBinary", new XSSimpleTypeDecl(baseAtomicType, "hexBinary", (short)15, (short)0, false, false, false, true, (short)16));
/* 193 */     builtInTypes.put("NOTATION", new XSSimpleTypeDecl(baseAtomicType, "NOTATION", (short)20, (short)0, false, false, false, true, (short)20));
/*     */ 
/* 195 */     facets.whiteSpace = 1;
/* 196 */     XSSimpleTypeDecl normalizedDV = new XSSimpleTypeDecl(stringDV, "normalizedString", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)21);
/* 197 */     normalizedDV.applyFacets1(facets, (short)16, (short)0);
/* 198 */     builtInTypes.put("normalizedString", normalizedDV);
/*     */ 
/* 200 */     facets.whiteSpace = 2;
/* 201 */     XSSimpleTypeDecl tokenDV = new XSSimpleTypeDecl(normalizedDV, "token", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)22);
/* 202 */     tokenDV.applyFacets1(facets, (short)16, (short)0);
/* 203 */     builtInTypes.put("token", tokenDV);
/*     */ 
/* 205 */     facets.whiteSpace = 2;
/* 206 */     facets.pattern = "([a-zA-Z]{1,8})(-[a-zA-Z0-9]{1,8})*";
/* 207 */     XSSimpleTypeDecl languageDV = new XSSimpleTypeDecl(tokenDV, "language", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)23);
/* 208 */     languageDV.applyFacets1(facets, (short)24, (short)0);
/* 209 */     builtInTypes.put("language", languageDV);
/*     */ 
/* 211 */     facets.whiteSpace = 2;
/* 212 */     XSSimpleTypeDecl nameDV = new XSSimpleTypeDecl(tokenDV, "Name", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)25);
/* 213 */     nameDV.applyFacets1(facets, (short)16, (short)0, (short)2);
/* 214 */     builtInTypes.put("Name", nameDV);
/*     */ 
/* 216 */     facets.whiteSpace = 2;
/* 217 */     XSSimpleTypeDecl ncnameDV = new XSSimpleTypeDecl(nameDV, "NCName", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)26);
/* 218 */     ncnameDV.applyFacets1(facets, (short)16, (short)0, (short)3);
/* 219 */     builtInTypes.put("NCName", ncnameDV);
/*     */ 
/* 221 */     builtInTypes.put("QName", new XSSimpleTypeDecl(baseAtomicType, "QName", (short)18, (short)0, false, false, false, true, (short)19));
/*     */ 
/* 223 */     builtInTypes.put("ID", new XSSimpleTypeDecl(ncnameDV, "ID", (short)21, (short)0, false, false, false, true, (short)27));
/* 224 */     XSSimpleTypeDecl idrefDV = new XSSimpleTypeDecl(ncnameDV, "IDREF", (short)22, (short)0, false, false, false, true, (short)28);
/* 225 */     builtInTypes.put("IDREF", idrefDV);
/*     */ 
/* 227 */     facets.minLength = 1;
/* 228 */     XSSimpleTypeDecl tempDV = new XSSimpleTypeDecl(null, "http://www.w3.org/2001/XMLSchema", (short)0, idrefDV, true, null);
/* 229 */     XSSimpleTypeDecl idrefsDV = new XSSimpleTypeDecl(tempDV, "IDREFS", "http://www.w3.org/2001/XMLSchema", (short)0, false, null);
/* 230 */     idrefsDV.applyFacets1(facets, (short)2, (short)0);
/* 231 */     builtInTypes.put("IDREFS", idrefsDV);
/*     */ 
/* 233 */     XSSimpleTypeDecl entityDV = new XSSimpleTypeDecl(ncnameDV, "ENTITY", (short)23, (short)0, false, false, false, true, (short)29);
/* 234 */     builtInTypes.put("ENTITY", entityDV);
/*     */ 
/* 236 */     facets.minLength = 1;
/* 237 */     tempDV = new XSSimpleTypeDecl(null, "http://www.w3.org/2001/XMLSchema", (short)0, entityDV, true, null);
/* 238 */     XSSimpleTypeDecl entitiesDV = new XSSimpleTypeDecl(tempDV, "ENTITIES", "http://www.w3.org/2001/XMLSchema", (short)0, false, null);
/* 239 */     entitiesDV.applyFacets1(facets, (short)2, (short)0);
/* 240 */     builtInTypes.put("ENTITIES", entitiesDV);
/*     */ 
/* 242 */     facets.whiteSpace = 2;
/* 243 */     XSSimpleTypeDecl nmtokenDV = new XSSimpleTypeDecl(tokenDV, "NMTOKEN", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)24);
/* 244 */     nmtokenDV.applyFacets1(facets, (short)16, (short)0, (short)1);
/* 245 */     builtInTypes.put("NMTOKEN", nmtokenDV);
/*     */ 
/* 247 */     facets.minLength = 1;
/* 248 */     tempDV = new XSSimpleTypeDecl(null, "http://www.w3.org/2001/XMLSchema", (short)0, nmtokenDV, true, null);
/* 249 */     XSSimpleTypeDecl nmtokensDV = new XSSimpleTypeDecl(tempDV, "NMTOKENS", "http://www.w3.org/2001/XMLSchema", (short)0, false, null);
/* 250 */     nmtokensDV.applyFacets1(facets, (short)2, (short)0);
/* 251 */     builtInTypes.put("NMTOKENS", nmtokensDV);
/*     */   }
/*     */ 
/*     */   public XSSimpleType createTypeRestriction(String name, String targetNamespace, short finalSet, XSSimpleType base, XSObjectList annotations)
/*     */   {
/* 268 */     if (this.fDeclPool != null) {
/* 269 */       XSSimpleTypeDecl st = this.fDeclPool.getSimpleTypeDecl();
/* 270 */       return st.setRestrictionValues((XSSimpleTypeDecl)base, name, targetNamespace, finalSet, annotations);
/*     */     }
/* 272 */     return new XSSimpleTypeDecl((XSSimpleTypeDecl)base, name, targetNamespace, finalSet, false, annotations);
/*     */   }
/*     */ 
/*     */   public XSSimpleType createTypeList(String name, String targetNamespace, short finalSet, XSSimpleType itemType, XSObjectList annotations)
/*     */   {
/* 289 */     if (this.fDeclPool != null) {
/* 290 */       XSSimpleTypeDecl st = this.fDeclPool.getSimpleTypeDecl();
/* 291 */       return st.setListValues(name, targetNamespace, finalSet, (XSSimpleTypeDecl)itemType, annotations);
/*     */     }
/* 293 */     return new XSSimpleTypeDecl(name, targetNamespace, finalSet, (XSSimpleTypeDecl)itemType, false, annotations);
/*     */   }
/*     */ 
/*     */   public XSSimpleType createTypeUnion(String name, String targetNamespace, short finalSet, XSSimpleType[] memberTypes, XSObjectList annotations)
/*     */   {
/* 310 */     int typeNum = memberTypes.length;
/* 311 */     XSSimpleTypeDecl[] mtypes = new XSSimpleTypeDecl[typeNum];
/* 312 */     System.arraycopy(memberTypes, 0, mtypes, 0, typeNum);
/*     */ 
/* 314 */     if (this.fDeclPool != null) {
/* 315 */       XSSimpleTypeDecl st = this.fDeclPool.getSimpleTypeDecl();
/* 316 */       return st.setUnionValues(name, targetNamespace, finalSet, mtypes, annotations);
/*     */     }
/* 318 */     return new XSSimpleTypeDecl(name, targetNamespace, finalSet, mtypes, annotations);
/*     */   }
/*     */ 
/*     */   public void setDeclPool(XSDeclarationPool declPool) {
/* 322 */     this.fDeclPool = declPool;
/*     */   }
/*     */ 
/*     */   public XSSimpleTypeDecl newXSSimpleTypeDecl()
/*     */   {
/* 327 */     return new XSSimpleTypeDecl();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.BaseSchemaDVFactory
 * JD-Core Version:    0.6.2
 */