/*     */ package com.sun.xml.internal.stream.dtd.nonvalidating;
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
/*     */ 
/*     */   public void setValues(short type, String name, String[] enumeration, boolean list, short defaultType, String defaultValue, String nonNormalizedDefaultValue)
/*     */   {
/* 123 */     this.type = type;
/* 124 */     this.name = name;
/*     */ 
/* 126 */     if ((enumeration != null) && (enumeration.length > 0)) {
/* 127 */       this.enumeration = new String[enumeration.length];
/* 128 */       System.arraycopy(enumeration, 0, this.enumeration, 0, this.enumeration.length);
/*     */     }
/*     */     else {
/* 131 */       this.enumeration = null;
/*     */     }
/* 133 */     this.list = list;
/* 134 */     this.defaultType = defaultType;
/* 135 */     this.defaultValue = defaultValue;
/* 136 */     this.nonNormalizedDefaultValue = nonNormalizedDefaultValue;
/*     */   }
/*     */ 
/*     */   public void setValues(XMLSimpleType simpleType)
/*     */   {
/* 143 */     this.type = simpleType.type;
/* 144 */     this.name = simpleType.name;
/*     */ 
/* 146 */     if ((simpleType.enumeration != null) && (simpleType.enumeration.length > 0)) {
/* 147 */       this.enumeration = new String[simpleType.enumeration.length];
/* 148 */       System.arraycopy(simpleType.enumeration, 0, this.enumeration, 0, this.enumeration.length);
/*     */     }
/*     */     else {
/* 151 */       this.enumeration = null;
/*     */     }
/* 153 */     this.list = simpleType.list;
/* 154 */     this.defaultType = simpleType.defaultType;
/* 155 */     this.defaultValue = simpleType.defaultValue;
/* 156 */     this.nonNormalizedDefaultValue = simpleType.nonNormalizedDefaultValue;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 164 */     this.type = -1;
/* 165 */     this.name = null;
/* 166 */     this.enumeration = null;
/* 167 */     this.list = false;
/* 168 */     this.defaultType = -1;
/* 169 */     this.defaultValue = null;
/* 170 */     this.nonNormalizedDefaultValue = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.dtd.nonvalidating.XMLSimpleType
 * JD-Core Version:    0.6.2
 */