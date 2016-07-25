/*     */ package com.sun.corba.se.impl.orbutil;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ 
/*     */ class ObjectStreamField
/*     */   implements Comparable
/*     */ {
/*     */   private String name;
/*     */   private char type;
/*     */   private Field field;
/*     */   private String typeString;
/*     */   private Class clazz;
/*     */   private String signature;
/* 268 */   private long fieldID = -1L;
/*     */ 
/*     */   ObjectStreamField(String paramString, Class paramClass)
/*     */   {
/*  55 */     this.name = paramString;
/*  56 */     this.clazz = paramClass;
/*     */ 
/*  59 */     if (paramClass.isPrimitive()) {
/*  60 */       if (paramClass == Integer.TYPE)
/*  61 */         this.type = 'I';
/*  62 */       else if (paramClass == Byte.TYPE)
/*  63 */         this.type = 'B';
/*  64 */       else if (paramClass == Long.TYPE)
/*  65 */         this.type = 'J';
/*  66 */       else if (paramClass == Float.TYPE)
/*  67 */         this.type = 'F';
/*  68 */       else if (paramClass == Double.TYPE)
/*  69 */         this.type = 'D';
/*  70 */       else if (paramClass == Short.TYPE)
/*  71 */         this.type = 'S';
/*  72 */       else if (paramClass == Character.TYPE)
/*  73 */         this.type = 'C';
/*  74 */       else if (paramClass == Boolean.TYPE)
/*  75 */         this.type = 'Z';
/*     */     }
/*  77 */     else if (paramClass.isArray()) {
/*  78 */       this.type = '[';
/*  79 */       this.typeString = ObjectStreamClass_1_3_1.getSignature(paramClass);
/*     */     } else {
/*  81 */       this.type = 'L';
/*  82 */       this.typeString = ObjectStreamClass_1_3_1.getSignature(paramClass);
/*     */     }
/*     */ 
/*  85 */     if (this.typeString != null)
/*  86 */       this.signature = this.typeString;
/*     */     else
/*  88 */       this.signature = String.valueOf(this.type);
/*     */   }
/*     */ 
/*     */   ObjectStreamField(Field paramField)
/*     */   {
/*  93 */     this(paramField.getName(), paramField.getType());
/*  94 */     this.field = paramField;
/*     */   }
/*     */ 
/*     */   ObjectStreamField(String paramString1, char paramChar, Field paramField, String paramString2)
/*     */   {
/* 102 */     this.name = paramString1;
/* 103 */     this.type = paramChar;
/* 104 */     this.field = paramField;
/* 105 */     this.typeString = paramString2;
/*     */ 
/* 107 */     if (this.typeString != null)
/* 108 */       this.signature = this.typeString;
/*     */     else
/* 110 */       this.signature = String.valueOf(this.type);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 118 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Class getType()
/*     */   {
/* 125 */     if (this.clazz != null)
/* 126 */       return this.clazz;
/* 127 */     switch (this.type) { case 'B':
/* 128 */       this.clazz = Byte.TYPE;
/* 129 */       break;
/*     */     case 'C':
/* 130 */       this.clazz = Character.TYPE;
/* 131 */       break;
/*     */     case 'S':
/* 132 */       this.clazz = Short.TYPE;
/* 133 */       break;
/*     */     case 'I':
/* 134 */       this.clazz = Integer.TYPE;
/* 135 */       break;
/*     */     case 'J':
/* 136 */       this.clazz = Long.TYPE;
/* 137 */       break;
/*     */     case 'F':
/* 138 */       this.clazz = Float.TYPE;
/* 139 */       break;
/*     */     case 'D':
/* 140 */       this.clazz = Double.TYPE;
/* 141 */       break;
/*     */     case 'Z':
/* 142 */       this.clazz = Boolean.TYPE;
/* 143 */       break;
/*     */     case 'L':
/*     */     case '[':
/* 146 */       this.clazz = Object.class;
/*     */     case 'E':
/*     */     case 'G':
/*     */     case 'H':
/*     */     case 'K':
/*     */     case 'M':
/*     */     case 'N':
/*     */     case 'O':
/*     */     case 'P':
/*     */     case 'Q':
/*     */     case 'R':
/*     */     case 'T':
/*     */     case 'U':
/*     */     case 'V':
/*     */     case 'W':
/*     */     case 'X':
/* 150 */     case 'Y': } return this.clazz;
/*     */   }
/*     */ 
/*     */   public char getTypeCode() {
/* 154 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getTypeString() {
/* 158 */     return this.typeString;
/*     */   }
/*     */ 
/*     */   Field getField() {
/* 162 */     return this.field;
/*     */   }
/*     */ 
/*     */   void setField(Field paramField) {
/* 166 */     this.field = paramField;
/* 167 */     this.fieldID = -1L;
/*     */   }
/*     */ 
/*     */   ObjectStreamField()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isPrimitive()
/*     */   {
/* 181 */     return (this.type != '[') && (this.type != 'L');
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 191 */     ObjectStreamField localObjectStreamField = (ObjectStreamField)paramObject;
/* 192 */     int i = this.typeString == null ? 1 : 0;
/* 193 */     int j = localObjectStreamField.typeString == null ? 1 : 0;
/*     */ 
/* 195 */     if (i != j) {
/* 196 */       return i != 0 ? -1 : 1;
/*     */     }
/* 198 */     return this.name.compareTo(localObjectStreamField.name);
/*     */   }
/*     */ 
/*     */   public boolean typeEquals(ObjectStreamField paramObjectStreamField)
/*     */   {
/* 207 */     if ((paramObjectStreamField == null) || (this.type != paramObjectStreamField.type)) {
/* 208 */       return false;
/*     */     }
/*     */ 
/* 211 */     if ((this.typeString == null) && (paramObjectStreamField.typeString == null)) {
/* 212 */       return true;
/*     */     }
/* 214 */     return ObjectStreamClass_1_3_1.compareClassNames(this.typeString, paramObjectStreamField.typeString, '/');
/*     */   }
/*     */ 
/*     */   public String getSignature()
/*     */   {
/* 224 */     return this.signature;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 232 */     if (this.typeString != null) {
/* 233 */       return this.typeString + " " + this.name;
/*     */     }
/* 235 */     return this.type + " " + this.name;
/*     */   }
/*     */ 
/*     */   public Class getClazz() {
/* 239 */     return this.clazz;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.ObjectStreamField
 * JD-Core Version:    0.6.2
 */