/*     */ package com.sun.corba.se.impl.io;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.corba.Bridge;
/*     */ 
/*     */ public class ObjectStreamField
/*     */   implements Comparable
/*     */ {
/*  51 */   private static final Bridge bridge = (Bridge)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Object run()
/*     */     {
/*  55 */       return Bridge.get();
/*     */     }
/*     */   });
/*     */   private String name;
/*     */   private char type;
/*     */   private Field field;
/*     */   private String typeString;
/*     */   private Class clazz;
/*     */   private String signature;
/* 270 */   private long fieldID = -1L;
/*     */ 
/*     */   ObjectStreamField(String paramString, Class paramClass)
/*     */   {
/*  64 */     this.name = paramString;
/*  65 */     this.clazz = paramClass;
/*     */ 
/*  68 */     if (paramClass.isPrimitive()) {
/*  69 */       if (paramClass == Integer.TYPE)
/*  70 */         this.type = 'I';
/*  71 */       else if (paramClass == Byte.TYPE)
/*  72 */         this.type = 'B';
/*  73 */       else if (paramClass == Long.TYPE)
/*  74 */         this.type = 'J';
/*  75 */       else if (paramClass == Float.TYPE)
/*  76 */         this.type = 'F';
/*  77 */       else if (paramClass == Double.TYPE)
/*  78 */         this.type = 'D';
/*  79 */       else if (paramClass == Short.TYPE)
/*  80 */         this.type = 'S';
/*  81 */       else if (paramClass == Character.TYPE)
/*  82 */         this.type = 'C';
/*  83 */       else if (paramClass == Boolean.TYPE)
/*  84 */         this.type = 'Z';
/*     */     }
/*  86 */     else if (paramClass.isArray()) {
/*  87 */       this.type = '[';
/*  88 */       this.typeString = ObjectStreamClass.getSignature(paramClass);
/*     */     } else {
/*  90 */       this.type = 'L';
/*  91 */       this.typeString = ObjectStreamClass.getSignature(paramClass);
/*     */     }
/*     */ 
/*  94 */     if (this.typeString != null)
/*  95 */       this.signature = this.typeString;
/*     */     else
/*  97 */       this.signature = String.valueOf(this.type);
/*     */   }
/*     */ 
/*     */   ObjectStreamField(Field paramField)
/*     */   {
/* 102 */     this(paramField.getName(), paramField.getType());
/* 103 */     setField(paramField);
/*     */   }
/*     */ 
/*     */   ObjectStreamField(String paramString1, char paramChar, Field paramField, String paramString2)
/*     */   {
/* 111 */     this.name = paramString1;
/* 112 */     this.type = paramChar;
/* 113 */     setField(paramField);
/* 114 */     this.typeString = paramString2;
/*     */ 
/* 116 */     if (this.typeString != null)
/* 117 */       this.signature = this.typeString;
/*     */     else
/* 119 */       this.signature = String.valueOf(this.type);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 127 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Class getType()
/*     */   {
/* 134 */     if (this.clazz != null)
/* 135 */       return this.clazz;
/* 136 */     switch (this.type) { case 'B':
/* 137 */       this.clazz = Byte.TYPE;
/* 138 */       break;
/*     */     case 'C':
/* 139 */       this.clazz = Character.TYPE;
/* 140 */       break;
/*     */     case 'S':
/* 141 */       this.clazz = Short.TYPE;
/* 142 */       break;
/*     */     case 'I':
/* 143 */       this.clazz = Integer.TYPE;
/* 144 */       break;
/*     */     case 'J':
/* 145 */       this.clazz = Long.TYPE;
/* 146 */       break;
/*     */     case 'F':
/* 147 */       this.clazz = Float.TYPE;
/* 148 */       break;
/*     */     case 'D':
/* 149 */       this.clazz = Double.TYPE;
/* 150 */       break;
/*     */     case 'Z':
/* 151 */       this.clazz = Boolean.TYPE;
/* 152 */       break;
/*     */     case 'L':
/*     */     case '[':
/* 155 */       this.clazz = Object.class;
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
/* 159 */     case 'Y': } return this.clazz;
/*     */   }
/*     */ 
/*     */   public char getTypeCode() {
/* 163 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getTypeString() {
/* 167 */     return this.typeString;
/*     */   }
/*     */ 
/*     */   Field getField() {
/* 171 */     return this.field;
/*     */   }
/*     */ 
/*     */   void setField(Field paramField) {
/* 175 */     this.field = paramField;
/* 176 */     this.fieldID = bridge.objectFieldOffset(paramField);
/*     */   }
/*     */ 
/*     */   ObjectStreamField()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isPrimitive()
/*     */   {
/* 190 */     return (this.type != '[') && (this.type != 'L');
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 200 */     ObjectStreamField localObjectStreamField = (ObjectStreamField)paramObject;
/* 201 */     int i = this.typeString == null ? 1 : 0;
/* 202 */     int j = localObjectStreamField.typeString == null ? 1 : 0;
/*     */ 
/* 204 */     if (i != j) {
/* 205 */       return i != 0 ? -1 : 1;
/*     */     }
/* 207 */     return this.name.compareTo(localObjectStreamField.name);
/*     */   }
/*     */ 
/*     */   public boolean typeEquals(ObjectStreamField paramObjectStreamField)
/*     */   {
/* 216 */     if ((paramObjectStreamField == null) || (this.type != paramObjectStreamField.type)) {
/* 217 */       return false;
/*     */     }
/*     */ 
/* 220 */     if ((this.typeString == null) && (paramObjectStreamField.typeString == null)) {
/* 221 */       return true;
/*     */     }
/* 223 */     return ObjectStreamClass.compareClassNames(this.typeString, paramObjectStreamField.typeString, '/');
/*     */   }
/*     */ 
/*     */   public String getSignature()
/*     */   {
/* 233 */     return this.signature;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 241 */     if (this.typeString != null) {
/* 242 */       return this.typeString + " " + this.name;
/*     */     }
/* 244 */     return this.type + " " + this.name;
/*     */   }
/*     */ 
/*     */   public Class getClazz() {
/* 248 */     return this.clazz;
/*     */   }
/*     */ 
/*     */   public long getFieldID()
/*     */   {
/* 255 */     return this.fieldID;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.ObjectStreamField
 * JD-Core Version:    0.6.2
 */