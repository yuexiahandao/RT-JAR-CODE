/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*     */ 
/*     */ public class FullDVFactory extends BaseDVFactory
/*     */ {
/*     */   static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
/*  42 */   static SymbolHash fFullTypes = new SymbolHash(89);
/*     */ 
/*     */   public XSSimpleType getBuiltInType(String name)
/*     */   {
/*  59 */     return (XSSimpleType)fFullTypes.get(name);
/*     */   }
/*     */ 
/*     */   public SymbolHash getBuiltInTypes()
/*     */   {
/*  69 */     return fFullTypes.makeClone();
/*     */   }
/*     */ 
/*     */   static void createBuiltInTypes(SymbolHash types)
/*     */   {
/*  75 */     BaseDVFactory.createBuiltInTypes(types);
/*     */ 
/*  78 */     String DOUBLE = "double";
/*  79 */     String DURATION = "duration";
/*  80 */     String ENTITY = "ENTITY";
/*  81 */     String ENTITIES = "ENTITIES";
/*  82 */     String FLOAT = "float";
/*  83 */     String HEXBINARY = "hexBinary";
/*  84 */     String ID = "ID";
/*  85 */     String IDREF = "IDREF";
/*  86 */     String IDREFS = "IDREFS";
/*  87 */     String NAME = "Name";
/*  88 */     String NCNAME = "NCName";
/*  89 */     String NMTOKEN = "NMTOKEN";
/*  90 */     String NMTOKENS = "NMTOKENS";
/*  91 */     String LANGUAGE = "language";
/*  92 */     String NORMALIZEDSTRING = "normalizedString";
/*  93 */     String NOTATION = "NOTATION";
/*  94 */     String QNAME = "QName";
/*  95 */     String STRING = "string";
/*  96 */     String TOKEN = "token";
/*     */ 
/*  98 */     XSFacets facets = new XSFacets();
/*     */ 
/* 100 */     XSSimpleTypeDecl anySimpleType = XSSimpleTypeDecl.fAnySimpleType;
/* 101 */     XSSimpleTypeDecl stringDV = (XSSimpleTypeDecl)types.get("string");
/*     */ 
/* 103 */     types.put("float", new XSSimpleTypeDecl(anySimpleType, "float", (short)4, (short)1, true, true, true, true, (short)5));
/* 104 */     types.put("double", new XSSimpleTypeDecl(anySimpleType, "double", (short)5, (short)1, true, true, true, true, (short)6));
/* 105 */     types.put("duration", new XSSimpleTypeDecl(anySimpleType, "duration", (short)6, (short)1, false, false, false, true, (short)7));
/* 106 */     types.put("hexBinary", new XSSimpleTypeDecl(anySimpleType, "hexBinary", (short)15, (short)0, false, false, false, true, (short)16));
/* 107 */     types.put("QName", new XSSimpleTypeDecl(anySimpleType, "QName", (short)18, (short)0, false, false, false, true, (short)19));
/* 108 */     types.put("NOTATION", new XSSimpleTypeDecl(anySimpleType, "NOTATION", (short)20, (short)0, false, false, false, true, (short)20));
/*     */ 
/* 110 */     facets.whiteSpace = 1;
/* 111 */     XSSimpleTypeDecl normalizedDV = new XSSimpleTypeDecl(stringDV, "normalizedString", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)21);
/* 112 */     normalizedDV.applyFacets1(facets, (short)16, (short)0);
/* 113 */     types.put("normalizedString", normalizedDV);
/*     */ 
/* 115 */     facets.whiteSpace = 2;
/* 116 */     XSSimpleTypeDecl tokenDV = new XSSimpleTypeDecl(normalizedDV, "token", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)22);
/* 117 */     tokenDV.applyFacets1(facets, (short)16, (short)0);
/* 118 */     types.put("token", tokenDV);
/*     */ 
/* 120 */     facets.whiteSpace = 2;
/* 121 */     facets.pattern = "([a-zA-Z]{1,8})(-[a-zA-Z0-9]{1,8})*";
/* 122 */     XSSimpleTypeDecl languageDV = new XSSimpleTypeDecl(tokenDV, "language", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)23);
/* 123 */     languageDV.applyFacets1(facets, (short)24, (short)0);
/* 124 */     types.put("language", languageDV);
/*     */ 
/* 126 */     facets.whiteSpace = 2;
/* 127 */     XSSimpleTypeDecl nameDV = new XSSimpleTypeDecl(tokenDV, "Name", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)25);
/* 128 */     nameDV.applyFacets1(facets, (short)16, (short)0, (short)2);
/* 129 */     types.put("Name", nameDV);
/*     */ 
/* 131 */     facets.whiteSpace = 2;
/* 132 */     XSSimpleTypeDecl ncnameDV = new XSSimpleTypeDecl(nameDV, "NCName", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)26);
/* 133 */     ncnameDV.applyFacets1(facets, (short)16, (short)0, (short)3);
/* 134 */     types.put("NCName", ncnameDV);
/*     */ 
/* 136 */     types.put("ID", new XSSimpleTypeDecl(ncnameDV, "ID", (short)21, (short)0, false, false, false, true, (short)27));
/* 137 */     XSSimpleTypeDecl idrefDV = new XSSimpleTypeDecl(ncnameDV, "IDREF", (short)22, (short)0, false, false, false, true, (short)28);
/* 138 */     types.put("IDREF", idrefDV);
/*     */ 
/* 140 */     facets.minLength = 1;
/* 141 */     XSSimpleTypeDecl tempDV = new XSSimpleTypeDecl(null, "http://www.w3.org/2001/XMLSchema", (short)0, idrefDV, true, null);
/* 142 */     XSSimpleTypeDecl idrefsDV = new XSSimpleTypeDecl(tempDV, "IDREFS", "http://www.w3.org/2001/XMLSchema", (short)0, false, null);
/* 143 */     idrefsDV.applyFacets1(facets, (short)2, (short)0);
/* 144 */     types.put("IDREFS", idrefsDV);
/*     */ 
/* 146 */     XSSimpleTypeDecl entityDV = new XSSimpleTypeDecl(ncnameDV, "ENTITY", (short)23, (short)0, false, false, false, true, (short)29);
/* 147 */     types.put("ENTITY", entityDV);
/*     */ 
/* 149 */     facets.minLength = 1;
/* 150 */     tempDV = new XSSimpleTypeDecl(null, "http://www.w3.org/2001/XMLSchema", (short)0, entityDV, true, null);
/* 151 */     XSSimpleTypeDecl entitiesDV = new XSSimpleTypeDecl(tempDV, "ENTITIES", "http://www.w3.org/2001/XMLSchema", (short)0, false, null);
/* 152 */     entitiesDV.applyFacets1(facets, (short)2, (short)0);
/* 153 */     types.put("ENTITIES", entitiesDV);
/*     */ 
/* 156 */     facets.whiteSpace = 2;
/* 157 */     XSSimpleTypeDecl nmtokenDV = new XSSimpleTypeDecl(tokenDV, "NMTOKEN", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)24);
/* 158 */     nmtokenDV.applyFacets1(facets, (short)16, (short)0, (short)1);
/* 159 */     types.put("NMTOKEN", nmtokenDV);
/*     */ 
/* 161 */     facets.minLength = 1;
/* 162 */     tempDV = new XSSimpleTypeDecl(null, "http://www.w3.org/2001/XMLSchema", (short)0, nmtokenDV, true, null);
/* 163 */     XSSimpleTypeDecl nmtokensDV = new XSSimpleTypeDecl(tempDV, "NMTOKENS", "http://www.w3.org/2001/XMLSchema", (short)0, false, null);
/* 164 */     nmtokensDV.applyFacets1(facets, (short)2, (short)0);
/* 165 */     types.put("NMTOKENS", nmtokensDV);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  44 */     createBuiltInTypes(fFullTypes);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.FullDVFactory
 * JD-Core Version:    0.6.2
 */