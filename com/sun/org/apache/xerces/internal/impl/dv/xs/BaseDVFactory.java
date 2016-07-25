/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ 
/*     */ public class BaseDVFactory extends SchemaDVFactory
/*     */ {
/*     */   static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
/*  45 */   static SymbolHash fBaseTypes = new SymbolHash(53);
/*     */ 
/*     */   public XSSimpleType getBuiltInType(String name)
/*     */   {
/*  62 */     return (XSSimpleType)fBaseTypes.get(name);
/*     */   }
/*     */ 
/*     */   public SymbolHash getBuiltInTypes()
/*     */   {
/*  72 */     return fBaseTypes.makeClone();
/*     */   }
/*     */ 
/*     */   public XSSimpleType createTypeRestriction(String name, String targetNamespace, short finalSet, XSSimpleType base, XSObjectList annotations)
/*     */   {
/*  88 */     return new XSSimpleTypeDecl((XSSimpleTypeDecl)base, name, targetNamespace, finalSet, false, annotations);
/*     */   }
/*     */ 
/*     */   public XSSimpleType createTypeList(String name, String targetNamespace, short finalSet, XSSimpleType itemType, XSObjectList annotations)
/*     */   {
/* 105 */     return new XSSimpleTypeDecl(name, targetNamespace, finalSet, (XSSimpleTypeDecl)itemType, false, annotations);
/*     */   }
/*     */ 
/*     */   public XSSimpleType createTypeUnion(String name, String targetNamespace, short finalSet, XSSimpleType[] memberTypes, XSObjectList annotations)
/*     */   {
/* 122 */     int typeNum = memberTypes.length;
/* 123 */     XSSimpleTypeDecl[] mtypes = new XSSimpleTypeDecl[typeNum];
/* 124 */     System.arraycopy(memberTypes, 0, mtypes, 0, typeNum);
/*     */ 
/* 126 */     return new XSSimpleTypeDecl(name, targetNamespace, finalSet, mtypes, annotations);
/*     */   }
/*     */ 
/*     */   static void createBuiltInTypes(SymbolHash types)
/*     */   {
/* 132 */     String ANYSIMPLETYPE = "anySimpleType";
/* 133 */     String ANYURI = "anyURI";
/* 134 */     String BASE64BINARY = "base64Binary";
/* 135 */     String BOOLEAN = "boolean";
/* 136 */     String BYTE = "byte";
/* 137 */     String DATE = "date";
/* 138 */     String DATETIME = "dateTime";
/* 139 */     String DAY = "gDay";
/* 140 */     String DECIMAL = "decimal";
/* 141 */     String INT = "int";
/* 142 */     String INTEGER = "integer";
/* 143 */     String LONG = "long";
/* 144 */     String NEGATIVEINTEGER = "negativeInteger";
/* 145 */     String MONTH = "gMonth";
/* 146 */     String MONTHDAY = "gMonthDay";
/* 147 */     String NONNEGATIVEINTEGER = "nonNegativeInteger";
/* 148 */     String NONPOSITIVEINTEGER = "nonPositiveInteger";
/* 149 */     String POSITIVEINTEGER = "positiveInteger";
/* 150 */     String SHORT = "short";
/* 151 */     String STRING = "string";
/* 152 */     String TIME = "time";
/* 153 */     String UNSIGNEDBYTE = "unsignedByte";
/* 154 */     String UNSIGNEDINT = "unsignedInt";
/* 155 */     String UNSIGNEDLONG = "unsignedLong";
/* 156 */     String UNSIGNEDSHORT = "unsignedShort";
/* 157 */     String YEAR = "gYear";
/* 158 */     String YEARMONTH = "gYearMonth";
/*     */ 
/* 160 */     XSFacets facets = new XSFacets();
/*     */ 
/* 162 */     XSSimpleTypeDecl anySimpleType = XSSimpleTypeDecl.fAnySimpleType;
/* 163 */     types.put("anySimpleType", anySimpleType);
/* 164 */     XSSimpleTypeDecl stringDV = new XSSimpleTypeDecl(anySimpleType, "string", (short)1, (short)0, false, false, false, true, (short)2);
/* 165 */     types.put("string", stringDV);
/* 166 */     types.put("boolean", new XSSimpleTypeDecl(anySimpleType, "boolean", (short)2, (short)0, false, true, false, true, (short)3));
/* 167 */     XSSimpleTypeDecl decimalDV = new XSSimpleTypeDecl(anySimpleType, "decimal", (short)3, (short)2, false, false, true, true, (short)4);
/* 168 */     types.put("decimal", decimalDV);
/*     */ 
/* 170 */     types.put("anyURI", new XSSimpleTypeDecl(anySimpleType, "anyURI", (short)17, (short)0, false, false, false, true, (short)18));
/* 171 */     types.put("base64Binary", new XSSimpleTypeDecl(anySimpleType, "base64Binary", (short)16, (short)0, false, false, false, true, (short)17));
/* 172 */     types.put("dateTime", new XSSimpleTypeDecl(anySimpleType, "dateTime", (short)7, (short)1, false, false, false, true, (short)8));
/* 173 */     types.put("time", new XSSimpleTypeDecl(anySimpleType, "time", (short)8, (short)1, false, false, false, true, (short)9));
/* 174 */     types.put("date", new XSSimpleTypeDecl(anySimpleType, "date", (short)9, (short)1, false, false, false, true, (short)10));
/* 175 */     types.put("gYearMonth", new XSSimpleTypeDecl(anySimpleType, "gYearMonth", (short)10, (short)1, false, false, false, true, (short)11));
/* 176 */     types.put("gYear", new XSSimpleTypeDecl(anySimpleType, "gYear", (short)11, (short)1, false, false, false, true, (short)12));
/* 177 */     types.put("gMonthDay", new XSSimpleTypeDecl(anySimpleType, "gMonthDay", (short)12, (short)1, false, false, false, true, (short)13));
/* 178 */     types.put("gDay", new XSSimpleTypeDecl(anySimpleType, "gDay", (short)13, (short)1, false, false, false, true, (short)14));
/* 179 */     types.put("gMonth", new XSSimpleTypeDecl(anySimpleType, "gMonth", (short)14, (short)1, false, false, false, true, (short)15));
/*     */ 
/* 181 */     XSSimpleTypeDecl integerDV = new XSSimpleTypeDecl(decimalDV, "integer", (short)24, (short)2, false, false, true, true, (short)30);
/* 182 */     types.put("integer", integerDV);
/*     */ 
/* 184 */     facets.maxInclusive = "0";
/* 185 */     XSSimpleTypeDecl nonPositiveDV = new XSSimpleTypeDecl(integerDV, "nonPositiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)31);
/* 186 */     nonPositiveDV.applyFacets1(facets, (short)32, (short)0);
/* 187 */     types.put("nonPositiveInteger", nonPositiveDV);
/*     */ 
/* 189 */     facets.maxInclusive = "-1";
/* 190 */     XSSimpleTypeDecl negativeDV = new XSSimpleTypeDecl(nonPositiveDV, "negativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)32);
/* 191 */     negativeDV.applyFacets1(facets, (short)32, (short)0);
/* 192 */     types.put("negativeInteger", negativeDV);
/*     */ 
/* 194 */     facets.maxInclusive = "9223372036854775807";
/* 195 */     facets.minInclusive = "-9223372036854775808";
/* 196 */     XSSimpleTypeDecl longDV = new XSSimpleTypeDecl(integerDV, "long", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)33);
/* 197 */     longDV.applyFacets1(facets, (short)288, (short)0);
/* 198 */     types.put("long", longDV);
/*     */ 
/* 201 */     facets.maxInclusive = "2147483647";
/* 202 */     facets.minInclusive = "-2147483648";
/* 203 */     XSSimpleTypeDecl intDV = new XSSimpleTypeDecl(longDV, "int", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)34);
/* 204 */     intDV.applyFacets1(facets, (short)288, (short)0);
/* 205 */     types.put("int", intDV);
/*     */ 
/* 207 */     facets.maxInclusive = "32767";
/* 208 */     facets.minInclusive = "-32768";
/* 209 */     XSSimpleTypeDecl shortDV = new XSSimpleTypeDecl(intDV, "short", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)35);
/* 210 */     shortDV.applyFacets1(facets, (short)288, (short)0);
/* 211 */     types.put("short", shortDV);
/*     */ 
/* 213 */     facets.maxInclusive = "127";
/* 214 */     facets.minInclusive = "-128";
/* 215 */     XSSimpleTypeDecl byteDV = new XSSimpleTypeDecl(shortDV, "byte", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)36);
/* 216 */     byteDV.applyFacets1(facets, (short)288, (short)0);
/* 217 */     types.put("byte", byteDV);
/*     */ 
/* 219 */     facets.minInclusive = "0";
/* 220 */     XSSimpleTypeDecl nonNegativeDV = new XSSimpleTypeDecl(integerDV, "nonNegativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)37);
/* 221 */     nonNegativeDV.applyFacets1(facets, (short)256, (short)0);
/* 222 */     types.put("nonNegativeInteger", nonNegativeDV);
/*     */ 
/* 224 */     facets.maxInclusive = "18446744073709551615";
/* 225 */     XSSimpleTypeDecl unsignedLongDV = new XSSimpleTypeDecl(nonNegativeDV, "unsignedLong", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)38);
/* 226 */     unsignedLongDV.applyFacets1(facets, (short)32, (short)0);
/* 227 */     types.put("unsignedLong", unsignedLongDV);
/*     */ 
/* 229 */     facets.maxInclusive = "4294967295";
/* 230 */     XSSimpleTypeDecl unsignedIntDV = new XSSimpleTypeDecl(unsignedLongDV, "unsignedInt", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)39);
/* 231 */     unsignedIntDV.applyFacets1(facets, (short)32, (short)0);
/* 232 */     types.put("unsignedInt", unsignedIntDV);
/*     */ 
/* 234 */     facets.maxInclusive = "65535";
/* 235 */     XSSimpleTypeDecl unsignedShortDV = new XSSimpleTypeDecl(unsignedIntDV, "unsignedShort", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)40);
/* 236 */     unsignedShortDV.applyFacets1(facets, (short)32, (short)0);
/* 237 */     types.put("unsignedShort", unsignedShortDV);
/*     */ 
/* 239 */     facets.maxInclusive = "255";
/* 240 */     XSSimpleTypeDecl unsignedByteDV = new XSSimpleTypeDecl(unsignedShortDV, "unsignedByte", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)41);
/* 241 */     unsignedByteDV.applyFacets1(facets, (short)32, (short)0);
/* 242 */     types.put("unsignedByte", unsignedByteDV);
/*     */ 
/* 244 */     facets.minInclusive = "1";
/* 245 */     XSSimpleTypeDecl positiveIntegerDV = new XSSimpleTypeDecl(nonNegativeDV, "positiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)42);
/* 246 */     positiveIntegerDV.applyFacets1(facets, (short)256, (short)0);
/* 247 */     types.put("positiveInteger", positiveIntegerDV);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  47 */     createBuiltInTypes(fBaseTypes);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.BaseDVFactory
 * JD-Core Version:    0.6.2
 */