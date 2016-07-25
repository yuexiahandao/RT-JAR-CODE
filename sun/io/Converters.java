/*     */ package sun.io;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.security.AccessController;
/*     */ import java.util.Properties;
/*     */ import sun.misc.VM;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ @Deprecated
/*     */ public class Converters
/*     */ {
/*  49 */   private static Object lock = Converters.class;
/*     */ 
/*  52 */   private static String converterPackageName = null;
/*  53 */   private static String defaultEncoding = null;
/*     */   public static final int BYTE_TO_CHAR = 0;
/*     */   public static final int CHAR_TO_BYTE = 1;
/*  58 */   private static final String[] converterPrefix = { "ByteToChar", "CharToByte" };
/*     */   private static final int CACHE_SIZE = 3;
/*  67 */   private static final Object DEFAULT_NAME = new Object();
/*     */ 
/*  89 */   private static SoftReference<Object[]>[][] classCache = (SoftReference[][])new SoftReference[][] { new SoftReference[3], new SoftReference[3] };
/*     */ 
/*     */   private static void moveToFront(Object[] paramArrayOfObject, int paramInt)
/*     */   {
/*  96 */     Object localObject = paramArrayOfObject[paramInt];
/*  97 */     for (int i = paramInt; i > 0; i--)
/*  98 */       paramArrayOfObject[i] = paramArrayOfObject[(i - 1)];
/*  99 */     paramArrayOfObject[0] = localObject;
/*     */   }
/*     */ 
/*     */   private static Class<?> cache(int paramInt, Object paramObject) {
/* 103 */     SoftReference[] arrayOfSoftReference = classCache[paramInt];
/* 104 */     for (int i = 0; i < 3; i++) {
/* 105 */       SoftReference localSoftReference = arrayOfSoftReference[i];
/* 106 */       if (localSoftReference != null)
/*     */       {
/* 108 */         Object[] arrayOfObject = (Object[])localSoftReference.get();
/* 109 */         if (arrayOfObject == null) {
/* 110 */           arrayOfSoftReference[i] = null;
/*     */         }
/* 113 */         else if (arrayOfObject[1].equals(paramObject)) {
/* 114 */           moveToFront(arrayOfSoftReference, i);
/* 115 */           return (Class)arrayOfObject[0];
/*     */         }
/*     */       }
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   private static Class<?> cache(int paramInt, Object paramObject, Class<?> paramClass) {
/* 122 */     SoftReference[] arrayOfSoftReference = classCache[paramInt];
/* 123 */     arrayOfSoftReference[2] = new SoftReference(new Object[] { paramClass, paramObject });
/* 124 */     moveToFront(arrayOfSoftReference, 2);
/* 125 */     return paramClass;
/*     */   }
/*     */ 
/*     */   public static boolean isCached(int paramInt, String paramString)
/*     */   {
/* 132 */     synchronized (lock) {
/* 133 */       SoftReference[] arrayOfSoftReference = classCache[paramInt];
/* 134 */       for (int i = 0; i < 3; i++) {
/* 135 */         SoftReference localSoftReference = arrayOfSoftReference[i];
/* 136 */         if (localSoftReference != null)
/*     */         {
/* 138 */           Object[] arrayOfObject = (Object[])localSoftReference.get();
/* 139 */           if (arrayOfObject == null) {
/* 140 */             arrayOfSoftReference[i] = null;
/*     */           }
/* 143 */           else if (arrayOfObject[1].equals(paramString))
/* 144 */             return true; 
/*     */         }
/*     */       }
/* 146 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getConverterPackageName()
/*     */   {
/* 154 */     String str = converterPackageName;
/* 155 */     if (str != null) return str;
/* 156 */     GetPropertyAction localGetPropertyAction = new GetPropertyAction("file.encoding.pkg");
/*     */ 
/* 158 */     str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 159 */     if (str != null)
/*     */     {
/* 161 */       converterPackageName = str;
/*     */     }
/*     */     else {
/* 164 */       str = "sun.io";
/*     */     }
/* 166 */     return str;
/*     */   }
/*     */ 
/*     */   public static String getDefaultEncodingName() {
/* 170 */     synchronized (lock) {
/* 171 */       if (defaultEncoding == null) {
/* 172 */         GetPropertyAction localGetPropertyAction = new GetPropertyAction("file.encoding");
/*     */ 
/* 174 */         defaultEncoding = (String)AccessController.doPrivileged(localGetPropertyAction);
/*     */       }
/*     */     }
/* 177 */     return defaultEncoding;
/*     */   }
/*     */ 
/*     */   public static void resetDefaultEncodingName()
/*     */   {
/* 182 */     if (VM.isBooted()) {
/* 183 */       return;
/*     */     }
/* 185 */     synchronized (lock) {
/* 186 */       defaultEncoding = "ISO-8859-1";
/* 187 */       Properties localProperties = System.getProperties();
/* 188 */       localProperties.setProperty("file.encoding", defaultEncoding);
/* 189 */       System.setProperties(localProperties);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Class<?> getConverterClass(int paramInt, String paramString)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 201 */     String str = null;
/*     */ 
/* 207 */     if (!paramString.equals("ISO8859_1")) {
/* 208 */       if (paramString.equals("8859_1")) {
/* 209 */         str = "ISO8859_1";
/*     */       }
/* 220 */       else if (paramString.equals("ISO8859-1"))
/* 221 */         str = "ISO8859_1";
/* 222 */       else if (paramString.equals("646"))
/* 223 */         str = "ASCII";
/*     */       else {
/* 225 */         str = CharacterEncoding.aliasName(paramString);
/*     */       }
/*     */     }
/* 228 */     if (str == null) {
/* 229 */       str = paramString;
/*     */     }
/*     */     try
/*     */     {
/* 233 */       return Class.forName(getConverterPackageName() + "." + converterPrefix[paramInt] + str);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*     */     }
/* 236 */     throw new UnsupportedEncodingException(str);
/*     */   }
/*     */ 
/*     */   private static Object newConverter(String paramString, Class<?> paramClass)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*     */     try
/*     */     {
/* 249 */       return paramClass.newInstance();
/*     */     } catch (InstantiationException localInstantiationException) {
/* 251 */       throw new UnsupportedEncodingException(paramString); } catch (IllegalAccessException localIllegalAccessException) {
/*     */     }
/* 253 */     throw new UnsupportedEncodingException(paramString);
/*     */   }
/*     */ 
/*     */   static Object newConverter(int paramInt, String paramString)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*     */     Class localClass;
/* 266 */     synchronized (lock) {
/* 267 */       localClass = cache(paramInt, paramString);
/* 268 */       if (localClass == null) {
/* 269 */         localClass = getConverterClass(paramInt, paramString);
/* 270 */         if (!localClass.getName().equals("sun.io.CharToByteUTF8"))
/* 271 */           cache(paramInt, paramString, localClass);
/*     */       }
/*     */     }
/* 274 */     return newConverter(paramString, localClass);
/*     */   }
/*     */ 
/*     */   private static Class<?> getDefaultConverterClass(int paramInt)
/*     */   {
/* 284 */     int i = 0;
/*     */ 
/* 288 */     Class localClass = cache(paramInt, DEFAULT_NAME);
/* 289 */     if (localClass != null) {
/* 290 */       return localClass;
/*     */     }
/*     */ 
/* 293 */     String str = getDefaultEncodingName();
/* 294 */     if (str != null)
/*     */     {
/* 296 */       i = 1;
/*     */     }
/*     */     else
/*     */     {
/* 300 */       str = "ISO8859_1";
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 305 */       localClass = getConverterClass(paramInt, str);
/* 306 */       if (i != 0)
/* 307 */         cache(paramInt, DEFAULT_NAME, localClass);
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException1)
/*     */     {
/*     */       try {
/* 312 */         localClass = getConverterClass(paramInt, "ISO8859_1");
/*     */       } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/* 314 */         throw new InternalError("Cannot find default " + converterPrefix[paramInt] + " converter class");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 319 */     return localClass;
/*     */   }
/*     */ 
/*     */   static Object newDefaultConverter(int paramInt)
/*     */   {
/*     */     Class localClass;
/* 330 */     synchronized (lock) {
/* 331 */       localClass = getDefaultConverterClass(paramInt);
/*     */     }
/*     */     try {
/* 334 */       return newConverter("", localClass); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 336 */     throw new InternalError("Cannot instantiate default converter class " + localClass.getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.Converters
 * JD-Core Version:    0.6.2
 */