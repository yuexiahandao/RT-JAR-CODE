/*     */ package java.io;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class ObjectStreamField
/*     */   implements Comparable<Object>
/*     */ {
/*     */   private final String name;
/*     */   private final String signature;
/*     */   private final Class<?> type;
/*     */   private final boolean unshared;
/*     */   private final Field field;
/*  57 */   private int offset = 0;
/*     */ 
/*     */   public ObjectStreamField(String paramString, Class<?> paramClass)
/*     */   {
/*  67 */     this(paramString, paramClass, false);
/*     */   }
/*     */ 
/*     */   public ObjectStreamField(String paramString, Class<?> paramClass, boolean paramBoolean)
/*     */   {
/*  88 */     if (paramString == null) {
/*  89 */       throw new NullPointerException();
/*     */     }
/*  91 */     this.name = paramString;
/*  92 */     this.type = paramClass;
/*  93 */     this.unshared = paramBoolean;
/*  94 */     this.signature = getClassSignature(paramClass).intern();
/*  95 */     this.field = null;
/*     */   }
/*     */ 
/*     */   ObjectStreamField(String paramString1, String paramString2, boolean paramBoolean)
/*     */   {
/* 103 */     if (paramString1 == null) {
/* 104 */       throw new NullPointerException();
/*     */     }
/* 106 */     this.name = paramString1;
/* 107 */     this.signature = paramString2.intern();
/* 108 */     this.unshared = paramBoolean;
/* 109 */     this.field = null;
/*     */ 
/* 111 */     switch (paramString2.charAt(0)) { case 'Z':
/* 112 */       this.type = Boolean.TYPE; break;
/*     */     case 'B':
/* 113 */       this.type = Byte.TYPE; break;
/*     */     case 'C':
/* 114 */       this.type = Character.TYPE; break;
/*     */     case 'S':
/* 115 */       this.type = Short.TYPE; break;
/*     */     case 'I':
/* 116 */       this.type = Integer.TYPE; break;
/*     */     case 'J':
/* 117 */       this.type = Long.TYPE; break;
/*     */     case 'F':
/* 118 */       this.type = Float.TYPE; break;
/*     */     case 'D':
/* 119 */       this.type = Double.TYPE; break;
/*     */     case 'L':
/*     */     case '[':
/* 121 */       this.type = Object.class; break;
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
/*     */     case 'Y':
/*     */     default:
/* 122 */       throw new IllegalArgumentException("illegal signature");
/*     */     }
/*     */   }
/*     */ 
/*     */   ObjectStreamField(Field paramField, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 135 */     this.field = paramField;
/* 136 */     this.unshared = paramBoolean1;
/* 137 */     this.name = paramField.getName();
/* 138 */     Class localClass = paramField.getType();
/* 139 */     this.type = ((paramBoolean2) || (localClass.isPrimitive()) ? localClass : Object.class);
/* 140 */     this.signature = getClassSignature(localClass).intern();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 150 */     return this.name;
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public Class<?> getType()
/*     */   {
/* 165 */     if (System.getSecurityManager() != null) {
/* 166 */       Class localClass = Reflection.getCallerClass();
/* 167 */       if (ReflectUtil.needsPackageAccessCheck(localClass.getClassLoader(), this.type.getClassLoader())) {
/* 168 */         ReflectUtil.checkPackageAccess(this.type);
/*     */       }
/*     */     }
/* 171 */     return this.type;
/*     */   }
/*     */ 
/*     */   public char getTypeCode()
/*     */   {
/* 193 */     return this.signature.charAt(0);
/*     */   }
/*     */ 
/*     */   public String getTypeString()
/*     */   {
/* 203 */     return isPrimitive() ? null : this.signature;
/*     */   }
/*     */ 
/*     */   public int getOffset()
/*     */   {
/* 214 */     return this.offset;
/*     */   }
/*     */ 
/*     */   protected void setOffset(int paramInt)
/*     */   {
/* 225 */     this.offset = paramInt;
/*     */   }
/*     */ 
/*     */   public boolean isPrimitive()
/*     */   {
/* 235 */     int i = this.signature.charAt(0);
/* 236 */     return (i != 76) && (i != 91);
/*     */   }
/*     */ 
/*     */   public boolean isUnshared()
/*     */   {
/* 246 */     return this.unshared;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 257 */     ObjectStreamField localObjectStreamField = (ObjectStreamField)paramObject;
/* 258 */     boolean bool = isPrimitive();
/* 259 */     if (bool != localObjectStreamField.isPrimitive()) {
/* 260 */       return bool ? -1 : 1;
/*     */     }
/* 262 */     return this.name.compareTo(localObjectStreamField.name);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 269 */     return this.signature + ' ' + this.name;
/*     */   }
/*     */ 
/*     */   Field getField()
/*     */   {
/* 277 */     return this.field;
/*     */   }
/*     */ 
/*     */   String getSignature()
/*     */   {
/* 285 */     return this.signature;
/*     */   }
/*     */ 
/*     */   private static String getClassSignature(Class<?> paramClass)
/*     */   {
/* 292 */     StringBuilder localStringBuilder = new StringBuilder();
/* 293 */     while (paramClass.isArray()) {
/* 294 */       localStringBuilder.append('[');
/* 295 */       paramClass = paramClass.getComponentType();
/*     */     }
/* 297 */     if (paramClass.isPrimitive()) {
/* 298 */       if (paramClass == Integer.TYPE)
/* 299 */         localStringBuilder.append('I');
/* 300 */       else if (paramClass == Byte.TYPE)
/* 301 */         localStringBuilder.append('B');
/* 302 */       else if (paramClass == Long.TYPE)
/* 303 */         localStringBuilder.append('J');
/* 304 */       else if (paramClass == Float.TYPE)
/* 305 */         localStringBuilder.append('F');
/* 306 */       else if (paramClass == Double.TYPE)
/* 307 */         localStringBuilder.append('D');
/* 308 */       else if (paramClass == Short.TYPE)
/* 309 */         localStringBuilder.append('S');
/* 310 */       else if (paramClass == Character.TYPE)
/* 311 */         localStringBuilder.append('C');
/* 312 */       else if (paramClass == Boolean.TYPE)
/* 313 */         localStringBuilder.append('Z');
/* 314 */       else if (paramClass == Void.TYPE)
/* 315 */         localStringBuilder.append('V');
/*     */       else
/* 317 */         throw new InternalError();
/*     */     }
/*     */     else {
/* 320 */       localStringBuilder.append('L' + paramClass.getName().replace('.', '/') + ';');
/*     */     }
/* 322 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.ObjectStreamField
 * JD-Core Version:    0.6.2
 */