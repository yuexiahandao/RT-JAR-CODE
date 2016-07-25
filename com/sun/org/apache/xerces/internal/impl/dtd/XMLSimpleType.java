/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
/*     */ 
/*     */ public class XMLSimpleType
/*     */ {
/*     */   public static final short TYPE_CDATA = 0;
/*     */   public static final short TYPE_ENTITY = 1;
/*     */   public static final short TYPE_ENUMERATION = 2;
/*     */   public static final short TYPE_ID = 3;
/*     */   public static final short TYPE_IDREF = 4;
/*     */   public static final short TYPE_NMTOKEN = 5;
/*     */   public static final short TYPE_NOTATION = 6;
/*     */   public static final short TYPE_NAMED = 7;
/*     */   public static final short DEFAULT_TYPE_DEFAULT = 3;
/*     */   public static final short DEFAULT_TYPE_FIXED = 1;
/*     */   public static final short DEFAULT_TYPE_IMPLIED = 0;
/*     */   public static final short DEFAULT_TYPE_REQUIRED = 2;
/*     */   public short type;
/*     */   public String name;
/*     */   public String[] enumeration;
/*     */   public boolean list;
/*     */   public short defaultType;
/*     */   public String defaultValue;
/*     */   public String nonNormalizedDefaultValue;
/*     */   public DatatypeValidator datatypeValidator;
/*     */ 
/*     */   public void setValues(short type, String name, String[] enumeration, boolean list, short defaultType, String defaultValue, String nonNormalizedDefaultValue, DatatypeValidator datatypeValidator)
/*     */   {
/* 159 */     this.type = type;
/* 160 */     this.name = name;
/*     */ 
/* 162 */     if ((enumeration != null) && (enumeration.length > 0)) {
/* 163 */       this.enumeration = new String[enumeration.length];
/* 164 */       System.arraycopy(enumeration, 0, this.enumeration, 0, this.enumeration.length);
/*     */     }
/*     */     else {
/* 167 */       this.enumeration = null;
/*     */     }
/* 169 */     this.list = list;
/* 170 */     this.defaultType = defaultType;
/* 171 */     this.defaultValue = defaultValue;
/* 172 */     this.nonNormalizedDefaultValue = nonNormalizedDefaultValue;
/* 173 */     this.datatypeValidator = datatypeValidator;
/*     */   }
/*     */ 
/*     */   public void setValues(XMLSimpleType simpleType)
/*     */   {
/* 180 */     this.type = simpleType.type;
/* 181 */     this.name = simpleType.name;
/*     */ 
/* 183 */     if ((simpleType.enumeration != null) && (simpleType.enumeration.length > 0)) {
/* 184 */       this.enumeration = new String[simpleType.enumeration.length];
/* 185 */       System.arraycopy(simpleType.enumeration, 0, this.enumeration, 0, this.enumeration.length);
/*     */     }
/*     */     else {
/* 188 */       this.enumeration = null;
/*     */     }
/* 190 */     this.list = simpleType.list;
/* 191 */     this.defaultType = simpleType.defaultType;
/* 192 */     this.defaultValue = simpleType.defaultValue;
/* 193 */     this.nonNormalizedDefaultValue = simpleType.nonNormalizedDefaultValue;
/* 194 */     this.datatypeValidator = simpleType.datatypeValidator;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 202 */     this.type = -1;
/* 203 */     this.name = null;
/* 204 */     this.enumeration = null;
/* 205 */     this.list = false;
/* 206 */     this.defaultType = -1;
/* 207 */     this.defaultValue = null;
/* 208 */     this.nonNormalizedDefaultValue = null;
/* 209 */     this.datatypeValidator = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLSimpleType
 * JD-Core Version:    0.6.2
 */