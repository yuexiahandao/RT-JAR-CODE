/*     */ package javax.management.openmbean;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.ImmutableDescriptor;
/*     */ 
/*     */ public abstract class OpenType<T>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -9195195325186646468L;
/*  95 */   public static final List<String> ALLOWED_CLASSNAMES_LIST = Collections.unmodifiableList(Arrays.asList(new String[] { "java.lang.Void", "java.lang.Boolean", "java.lang.Character", "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.String", "java.math.BigDecimal", "java.math.BigInteger", "java.util.Date", "javax.management.ObjectName", CompositeData.class.getName(), TabularData.class.getName() }));
/*     */ 
/*     */   @Deprecated
/* 120 */   public static final String[] ALLOWED_CLASSNAMES = (String[])ALLOWED_CLASSNAMES_LIST.toArray(new String[0]);
/*     */   private String className;
/*     */   private String description;
/*     */   private String typeName;
/* 143 */   private transient boolean isArray = false;
/*     */   private transient Descriptor descriptor;
/*     */ 
/*     */   protected OpenType(String paramString1, String paramString2, String paramString3)
/*     */     throws OpenDataException
/*     */   {
/* 180 */     checkClassNameOverride();
/* 181 */     this.typeName = valid("typeName", paramString2);
/* 182 */     this.description = valid("description", paramString3);
/* 183 */     this.className = validClassName(paramString1);
/* 184 */     this.isArray = ((this.className != null) && (this.className.startsWith("[")));
/*     */   }
/*     */ 
/*     */   OpenType(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
/*     */   {
/* 190 */     this.className = valid("className", paramString1);
/* 191 */     this.typeName = valid("typeName", paramString2);
/* 192 */     this.description = valid("description", paramString3);
/* 193 */     this.isArray = paramBoolean;
/*     */   }
/*     */ 
/*     */   private void checkClassNameOverride() throws SecurityException {
/* 197 */     if (getClass().getClassLoader() == null)
/* 198 */       return;
/* 199 */     if (overridesGetClassName(getClass())) {
/* 200 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.extend.open.types");
/*     */ 
/* 202 */       if (AccessController.doPrivileged(localGetPropertyAction) == null)
/* 203 */         throw new SecurityException("Cannot override getClassName() unless -Djmx.extend.open.types");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean overridesGetClassName(Class<?> paramClass)
/*     */   {
/* 210 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Boolean run() {
/*     */         try {
/* 213 */           return Boolean.valueOf(this.val$c.getMethod("getClassName", new Class[0]).getDeclaringClass() != OpenType.class);
/*     */         } catch (Exception localException) {
/*     */         }
/* 216 */         return Boolean.valueOf(true);
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */ 
/*     */   private static String validClassName(String paramString)
/*     */     throws OpenDataException
/*     */   {
/* 223 */     paramString = valid("className", paramString);
/*     */ 
/* 228 */     int i = 0;
/* 229 */     while (paramString.startsWith("[", i)) {
/* 230 */       i++;
/*     */     }
/*     */ 
/* 233 */     int j = 0;
/*     */     String str;
/* 234 */     if (i > 0) {
/* 235 */       if ((paramString.startsWith("L", i)) && (paramString.endsWith(";")))
/*     */       {
/* 238 */         str = paramString.substring(i + 1, paramString.length() - 1);
/* 239 */       } else if (i == paramString.length() - 1)
/*     */       {
/* 241 */         str = paramString.substring(i, paramString.length());
/* 242 */         j = 1;
/*     */       } else {
/* 244 */         throw new OpenDataException("Argument className=\"" + paramString + "\" is not a valid class name");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 249 */       str = paramString;
/*     */     }
/*     */ 
/* 254 */     boolean bool = false;
/* 255 */     if (j != 0)
/* 256 */       bool = ArrayType.isPrimitiveContentType(str);
/*     */     else {
/* 258 */       bool = ALLOWED_CLASSNAMES_LIST.contains(str);
/*     */     }
/* 260 */     if (!bool) {
/* 261 */       throw new OpenDataException("Argument className=\"" + paramString + "\" is not one of the allowed Java class names for open data.");
/*     */     }
/*     */ 
/* 265 */     return paramString;
/*     */   }
/*     */ 
/*     */   private static String valid(String paramString1, String paramString2)
/*     */   {
/* 271 */     if ((paramString2 == null) || ((paramString2 = paramString2.trim()).equals(""))) {
/* 272 */       throw new IllegalArgumentException("Argument " + paramString1 + " cannot be null or empty");
/*     */     }
/* 274 */     return paramString2;
/*     */   }
/*     */ 
/*     */   synchronized Descriptor getDescriptor()
/*     */   {
/* 279 */     if (this.descriptor == null) {
/* 280 */       this.descriptor = new ImmutableDescriptor(new String[] { "openType" }, new Object[] { this });
/*     */     }
/*     */ 
/* 283 */     return this.descriptor;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 307 */     return this.className;
/*     */   }
/*     */ 
/*     */   String safeGetClassName()
/*     */   {
/* 313 */     return this.className;
/*     */   }
/*     */ 
/*     */   public String getTypeName()
/*     */   {
/* 323 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 333 */     return this.description;
/*     */   }
/*     */ 
/*     */   public boolean isArray()
/*     */   {
/* 344 */     return this.isArray;
/*     */   }
/*     */ 
/*     */   public abstract boolean isValue(Object paramObject);
/*     */ 
/*     */   boolean isAssignableFrom(OpenType<?> paramOpenType)
/*     */   {
/* 367 */     return equals(paramOpenType);
/*     */   }
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */ 
/*     */   public abstract int hashCode();
/*     */ 
/*     */   public abstract String toString();
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 396 */     checkClassNameOverride();
/* 397 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */     String str1;
/*     */     String str2;
/*     */     String str3;
/*     */     try
/*     */     {
/* 402 */       str1 = validClassName((String)localGetField.get("className", null));
/*     */ 
/* 404 */       str2 = valid("description", (String)localGetField.get("description", null));
/*     */ 
/* 406 */       str3 = valid("typeName", (String)localGetField.get("typeName", null));
/*     */     }
/*     */     catch (Exception localException) {
/* 409 */       InvalidObjectException localInvalidObjectException = new InvalidObjectException(localException.getMessage());
/* 410 */       localInvalidObjectException.initCause(localException);
/* 411 */       throw localInvalidObjectException;
/*     */     }
/* 413 */     this.className = str1;
/* 414 */     this.description = str2;
/* 415 */     this.typeName = str3;
/* 416 */     this.isArray = this.className.startsWith("[");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.OpenType
 * JD-Core Version:    0.6.2
 */