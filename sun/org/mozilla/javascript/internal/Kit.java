/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Kit
/*     */ {
/*  57 */   private static Method Throwable_initCause = null;
/*     */ 
/*     */   public static Class<?> classOrNull(String paramString)
/*     */   {
/*     */     try
/*     */     {
/*  74 */       return Class.forName(paramString);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*     */     } catch (SecurityException localSecurityException) {
/*     */     }
/*     */     catch (LinkageError localLinkageError) {
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */   public static Class<?> classOrNull(ClassLoader paramClassLoader, String paramString)
/*     */   {
/*     */     try
/*     */     {
/*  92 */       return paramClassLoader.loadClass(paramString);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*     */     } catch (SecurityException localSecurityException) {
/*     */     }
/*     */     catch (LinkageError localLinkageError) {
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   static Object newInstanceOrNull(Class<?> paramClass)
/*     */   {
/*     */     try {
/* 106 */       return paramClass.newInstance();
/*     */     } catch (SecurityException localSecurityException) {
/*     */     } catch (LinkageError localLinkageError) {
/*     */     } catch (InstantiationException localInstantiationException) {
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   static boolean testIfCanLoadRhinoClasses(ClassLoader paramClassLoader)
/*     */   {
/* 120 */     Class localClass1 = ScriptRuntime.ContextFactoryClass;
/* 121 */     Class localClass2 = classOrNull(paramClassLoader, localClass1.getName());
/* 122 */     if (localClass2 != localClass1)
/*     */     {
/* 127 */       return false;
/*     */     }
/* 129 */     return true;
/*     */   }
/*     */ 
/*     */   public static RuntimeException initCause(RuntimeException paramRuntimeException, Throwable paramThrowable)
/*     */   {
/* 140 */     if (Throwable_initCause != null) {
/* 141 */       Object[] arrayOfObject = { paramThrowable };
/*     */       try {
/* 143 */         Throwable_initCause.invoke(paramRuntimeException, arrayOfObject);
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/*     */     }
/* 148 */     return paramRuntimeException;
/*     */   }
/*     */ 
/*     */   public static int xDigitToInt(int paramInt1, int paramInt2)
/*     */   {
/* 160 */     if (paramInt1 <= 57) {
/* 161 */       paramInt1 -= 48;
/* 162 */       if (0 <= paramInt1) break label55; 
/*     */     }
/* 163 */     else if (paramInt1 <= 70) {
/* 164 */       if (65 <= paramInt1) {
/* 165 */         paramInt1 -= 55;
/* 166 */         break label55;
/*     */       }
/* 168 */     } else if ((paramInt1 <= 102) && 
/* 169 */       (97 <= paramInt1)) {
/* 170 */       paramInt1 -= 87;
/* 171 */       break label55;
/*     */     }
/*     */ 
/* 174 */     return -1;
/*     */ 
/* 176 */     label55: return paramInt2 << 4 | paramInt1;
/*     */   }
/*     */ 
/*     */   public static Object addListener(Object paramObject1, Object paramObject2)
/*     */   {
/* 229 */     if (paramObject2 == null) throw new IllegalArgumentException();
/* 230 */     if ((paramObject2 instanceof Object[])) throw new IllegalArgumentException();
/*     */ 
/* 232 */     if (paramObject1 == null) {
/* 233 */       paramObject1 = paramObject2;
/* 234 */     } else if (!(paramObject1 instanceof Object[])) {
/* 235 */       paramObject1 = new Object[] { paramObject1, paramObject2 };
/*     */     } else {
/* 237 */       Object[] arrayOfObject1 = (Object[])paramObject1;
/* 238 */       int i = arrayOfObject1.length;
/*     */ 
/* 240 */       if (i < 2) throw new IllegalArgumentException();
/* 241 */       Object[] arrayOfObject2 = new Object[i + 1];
/* 242 */       System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, i);
/* 243 */       arrayOfObject2[i] = paramObject2;
/* 244 */       paramObject1 = arrayOfObject2;
/*     */     }
/*     */ 
/* 247 */     return paramObject1;
/*     */   }
/*     */ 
/*     */   public static Object removeListener(Object paramObject1, Object paramObject2)
/*     */   {
/* 268 */     if (paramObject2 == null) throw new IllegalArgumentException();
/* 269 */     if ((paramObject2 instanceof Object[])) throw new IllegalArgumentException();
/*     */ 
/* 271 */     if (paramObject1 == paramObject2) {
/* 272 */       paramObject1 = null;
/* 273 */     } else if ((paramObject1 instanceof Object[])) {
/* 274 */       Object[] arrayOfObject1 = (Object[])paramObject1;
/* 275 */       int i = arrayOfObject1.length;
/*     */ 
/* 277 */       if (i < 2) throw new IllegalArgumentException();
/* 278 */       if (i == 2) {
/* 279 */         if (arrayOfObject1[1] == paramObject2)
/* 280 */           paramObject1 = arrayOfObject1[0];
/* 281 */         else if (arrayOfObject1[0] == paramObject2)
/* 282 */           paramObject1 = arrayOfObject1[1];
/*     */       }
/*     */       else {
/* 285 */         int j = i;
/*     */         do {
/* 287 */           j--;
/* 288 */           if (arrayOfObject1[j] == paramObject2) {
/* 289 */             Object[] arrayOfObject2 = new Object[i - 1];
/* 290 */             System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, j);
/* 291 */             System.arraycopy(arrayOfObject1, j + 1, arrayOfObject2, j, i - (j + 1));
/* 292 */             paramObject1 = arrayOfObject2;
/* 293 */             break;
/*     */           }
/*     */         }
/* 295 */         while (j != 0);
/*     */       }
/*     */     }
/*     */ 
/* 299 */     return paramObject1;
/*     */   }
/*     */ 
/*     */   public static Object getListener(Object paramObject, int paramInt)
/*     */   {
/* 316 */     if (paramInt == 0) {
/* 317 */       if (paramObject == null)
/* 318 */         return null;
/* 319 */       if (!(paramObject instanceof Object[]))
/* 320 */         return paramObject;
/* 321 */       arrayOfObject = (Object[])paramObject;
/*     */ 
/* 323 */       if (arrayOfObject.length < 2) throw new IllegalArgumentException();
/* 324 */       return arrayOfObject[0];
/* 325 */     }if (paramInt == 1) {
/* 326 */       if (!(paramObject instanceof Object[])) {
/* 327 */         if (paramObject == null) throw new IllegalArgumentException();
/* 328 */         return null;
/*     */       }
/* 330 */       arrayOfObject = (Object[])paramObject;
/*     */ 
/* 332 */       return arrayOfObject[1];
/*     */     }
/*     */ 
/* 335 */     Object[] arrayOfObject = (Object[])paramObject;
/* 336 */     int i = arrayOfObject.length;
/* 337 */     if (i < 2) throw new IllegalArgumentException();
/* 338 */     if (paramInt == i)
/* 339 */       return null;
/* 340 */     return arrayOfObject[paramInt];
/*     */   }
/*     */ 
/*     */   static Object initHash(Map<Object, Object> paramMap, Object paramObject1, Object paramObject2)
/*     */   {
/* 346 */     synchronized (paramMap) {
/* 347 */       Object localObject1 = paramMap.get(paramObject1);
/* 348 */       if (localObject1 == null)
/* 349 */         paramMap.put(paramObject1, paramObject2);
/*     */       else {
/* 351 */         paramObject2 = localObject1;
/*     */       }
/*     */     }
/* 354 */     return paramObject2;
/*     */   }
/*     */ 
/*     */   public static Object makeHashKeyFromPair(Object paramObject1, Object paramObject2)
/*     */   {
/* 390 */     if (paramObject1 == null) throw new IllegalArgumentException();
/* 391 */     if (paramObject2 == null) throw new IllegalArgumentException();
/* 392 */     return new ComplexKey(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public static String readReader(Reader paramReader)
/*     */     throws IOException
/*     */   {
/* 398 */     Object localObject = new char[512];
/* 399 */     int i = 0;
/*     */     while (true) {
/* 401 */       int j = paramReader.read((char[])localObject, i, localObject.length - i);
/* 402 */       if (j < 0) break;
/* 403 */       i += j;
/* 404 */       if (i == localObject.length) {
/* 405 */         char[] arrayOfChar = new char[localObject.length * 2];
/* 406 */         System.arraycopy(localObject, 0, arrayOfChar, 0, i);
/* 407 */         localObject = arrayOfChar;
/*     */       }
/*     */     }
/* 410 */     return new String((char[])localObject, 0, i);
/*     */   }
/*     */ 
/*     */   public static byte[] readStream(InputStream paramInputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 416 */     if (paramInt <= 0) {
/* 417 */       throw new IllegalArgumentException("Bad initialBufferCapacity: " + paramInt);
/*     */     }
/*     */ 
/* 420 */     Object localObject = new byte[paramInt];
/* 421 */     int i = 0;
/*     */     while (true) {
/* 423 */       int j = paramInputStream.read((byte[])localObject, i, localObject.length - i);
/* 424 */       if (j < 0) break;
/* 425 */       i += j;
/* 426 */       if (i == localObject.length) {
/* 427 */         byte[] arrayOfByte2 = new byte[localObject.length * 2];
/* 428 */         System.arraycopy(localObject, 0, arrayOfByte2, 0, i);
/* 429 */         localObject = arrayOfByte2;
/*     */       }
/*     */     }
/* 432 */     if (i != localObject.length) {
/* 433 */       byte[] arrayOfByte1 = new byte[i];
/* 434 */       System.arraycopy(localObject, 0, arrayOfByte1, 0, i);
/* 435 */       localObject = arrayOfByte1;
/*     */     }
/* 437 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static RuntimeException codeBug()
/*     */     throws RuntimeException
/*     */   {
/* 449 */     IllegalStateException localIllegalStateException = new IllegalStateException("FAILED ASSERTION");
/*     */ 
/* 451 */     localIllegalStateException.printStackTrace(System.err);
/* 452 */     throw localIllegalStateException;
/*     */   }
/*     */ 
/*     */   public static RuntimeException codeBug(String paramString)
/*     */     throws RuntimeException
/*     */   {
/* 464 */     paramString = "FAILED ASSERTION: " + paramString;
/* 465 */     IllegalStateException localIllegalStateException = new IllegalStateException(paramString);
/*     */ 
/* 467 */     localIllegalStateException.printStackTrace(System.err);
/* 468 */     throw localIllegalStateException;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  62 */       Class localClass = classOrNull("java.lang.Throwable");
/*  63 */       Class[] arrayOfClass = { localClass };
/*  64 */       Throwable_initCause = localClass.getMethod("initCause", arrayOfClass);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class ComplexKey
/*     */   {
/*     */     private Object key1;
/*     */     private Object key2;
/*     */     private int hash;
/*     */ 
/*     */     ComplexKey(Object paramObject1, Object paramObject2)
/*     */     {
/* 365 */       this.key1 = paramObject1;
/* 366 */       this.key2 = paramObject2;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 372 */       if (!(paramObject instanceof ComplexKey))
/* 373 */         return false;
/* 374 */       ComplexKey localComplexKey = (ComplexKey)paramObject;
/* 375 */       return (this.key1.equals(localComplexKey.key1)) && (this.key2.equals(localComplexKey.key2));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 381 */       if (this.hash == 0) {
/* 382 */         this.hash = (this.key1.hashCode() ^ this.key2.hashCode());
/*     */       }
/* 384 */       return this.hash;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Kit
 * JD-Core Version:    0.6.2
 */