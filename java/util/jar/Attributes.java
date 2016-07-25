/*     */ package java.util.jar;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import sun.misc.ASCIICaseInsensitiveComparator;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class Attributes
/*     */   implements Map<Object, Object>, Cloneable
/*     */ {
/*     */   protected Map<Object, Object> map;
/*     */ 
/*     */   public Attributes()
/*     */   {
/*  64 */     this(11);
/*     */   }
/*     */ 
/*     */   public Attributes(int paramInt)
/*     */   {
/*  74 */     this.map = new HashMap(paramInt);
/*     */   }
/*     */ 
/*     */   public Attributes(Attributes paramAttributes)
/*     */   {
/*  84 */     this.map = new HashMap(paramAttributes);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/*  97 */     return this.map.get(paramObject);
/*     */   }
/*     */ 
/*     */   public String getValue(String paramString)
/*     */   {
/* 116 */     return (String)get(new Name(paramString));
/*     */   }
/*     */ 
/*     */   public String getValue(Name paramName)
/*     */   {
/* 133 */     return (String)get(paramName);
/*     */   }
/*     */ 
/*     */   public Object put(Object paramObject1, Object paramObject2)
/*     */   {
/* 148 */     return this.map.put((Name)paramObject1, (String)paramObject2);
/*     */   }
/*     */ 
/*     */   public String putValue(String paramString1, String paramString2)
/*     */   {
/* 168 */     return (String)put(new Name(paramString1), paramString2);
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 179 */     return this.map.remove(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject)
/*     */   {
/* 191 */     return this.map.containsValue(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/* 201 */     return this.map.containsKey(paramObject);
/*     */   }
/*     */ 
/*     */   public void putAll(Map<?, ?> paramMap)
/*     */   {
/* 213 */     if (!Attributes.class.isInstance(paramMap))
/* 214 */       throw new ClassCastException();
/* 215 */     for (Map.Entry localEntry : paramMap.entrySet())
/* 216 */       put(localEntry.getKey(), localEntry.getValue());
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 223 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 230 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 237 */     return this.map.isEmpty();
/*     */   }
/*     */ 
/*     */   public Set<Object> keySet()
/*     */   {
/* 244 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */   public Collection<Object> values()
/*     */   {
/* 251 */     return this.map.values();
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<Object, Object>> entrySet()
/*     */   {
/* 259 */     return this.map.entrySet();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 271 */     return this.map.equals(paramObject);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 278 */     return this.map.hashCode();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 291 */     return new Attributes(this);
/*     */   }
/*     */ 
/*     */   void write(DataOutputStream paramDataOutputStream)
/*     */     throws IOException
/*     */   {
/* 299 */     Iterator localIterator = entrySet().iterator();
/* 300 */     while (localIterator.hasNext()) {
/* 301 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 302 */       StringBuffer localStringBuffer = new StringBuffer(((Name)localEntry.getKey()).toString());
/*     */ 
/* 304 */       localStringBuffer.append(": ");
/*     */ 
/* 306 */       String str = (String)localEntry.getValue();
/* 307 */       if (str != null) {
/* 308 */         byte[] arrayOfByte = str.getBytes("UTF8");
/* 309 */         str = new String(arrayOfByte, 0, 0, arrayOfByte.length);
/*     */       }
/* 311 */       localStringBuffer.append(str);
/*     */ 
/* 313 */       localStringBuffer.append("\r\n");
/* 314 */       Manifest.make72Safe(localStringBuffer);
/* 315 */       paramDataOutputStream.writeBytes(localStringBuffer.toString());
/*     */     }
/* 317 */     paramDataOutputStream.writeBytes("\r\n");
/*     */   }
/*     */ 
/*     */   void writeMain(DataOutputStream paramDataOutputStream)
/*     */     throws IOException
/*     */   {
/* 330 */     String str1 = Name.MANIFEST_VERSION.toString();
/* 331 */     String str2 = getValue(str1);
/* 332 */     if (str2 == null) {
/* 333 */       str1 = Name.SIGNATURE_VERSION.toString();
/* 334 */       str2 = getValue(str1);
/*     */     }
/*     */ 
/* 337 */     if (str2 != null) {
/* 338 */       paramDataOutputStream.writeBytes(str1 + ": " + str2 + "\r\n");
/*     */     }
/*     */ 
/* 343 */     Iterator localIterator = entrySet().iterator();
/* 344 */     while (localIterator.hasNext()) {
/* 345 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 346 */       String str3 = ((Name)localEntry.getKey()).toString();
/* 347 */       if ((str2 != null) && (!str3.equalsIgnoreCase(str1)))
/*     */       {
/* 349 */         StringBuffer localStringBuffer = new StringBuffer(str3);
/* 350 */         localStringBuffer.append(": ");
/*     */ 
/* 352 */         String str4 = (String)localEntry.getValue();
/* 353 */         if (str4 != null) {
/* 354 */           byte[] arrayOfByte = str4.getBytes("UTF8");
/* 355 */           str4 = new String(arrayOfByte, 0, 0, arrayOfByte.length);
/*     */         }
/* 357 */         localStringBuffer.append(str4);
/*     */ 
/* 359 */         localStringBuffer.append("\r\n");
/* 360 */         Manifest.make72Safe(localStringBuffer);
/* 361 */         paramDataOutputStream.writeBytes(localStringBuffer.toString());
/*     */       }
/*     */     }
/* 364 */     paramDataOutputStream.writeBytes("\r\n");
/*     */   }
/*     */ 
/*     */   void read(Manifest.FastInputStream paramFastInputStream, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 372 */     String str1 = null; String str2 = null;
/* 373 */     Object localObject = null;
/*     */     int i;
/* 376 */     while ((i = paramFastInputStream.readLine(paramArrayOfByte)) != -1) {
/* 377 */       int j = 0;
/* 378 */       if (paramArrayOfByte[(--i)] != 10) {
/* 379 */         throw new IOException("line too long");
/*     */       }
/* 381 */       if ((i > 0) && (paramArrayOfByte[(i - 1)] == 13)) {
/* 382 */         i--;
/*     */       }
/* 384 */       if (i == 0) {
/*     */         break;
/*     */       }
/* 387 */       int k = 0;
/* 388 */       if (paramArrayOfByte[0] == 32)
/*     */       {
/* 390 */         if (str1 == null) {
/* 391 */           throw new IOException("misplaced continuation line");
/*     */         }
/* 393 */         j = 1;
/* 394 */         byte[] arrayOfByte = new byte[localObject.length + i - 1];
/* 395 */         System.arraycopy(localObject, 0, arrayOfByte, 0, localObject.length);
/* 396 */         System.arraycopy(paramArrayOfByte, 1, arrayOfByte, localObject.length, i - 1);
/* 397 */         if (paramFastInputStream.peek() == 32) {
/* 398 */           localObject = arrayOfByte;
/*     */         }
/*     */         else {
/* 401 */           str2 = new String(arrayOfByte, 0, arrayOfByte.length, "UTF8");
/* 402 */           localObject = null;
/*     */         }
/*     */       } else { while (paramArrayOfByte[(k++)] != 58) {
/* 405 */           if (k >= i) {
/* 406 */             throw new IOException("invalid header field");
/*     */           }
/*     */         }
/* 409 */         if (paramArrayOfByte[(k++)] != 32) {
/* 410 */           throw new IOException("invalid header field");
/*     */         }
/* 412 */         str1 = new String(paramArrayOfByte, 0, 0, k - 2);
/* 413 */         if (paramFastInputStream.peek() == 32) {
/* 414 */           localObject = new byte[i - k];
/* 415 */           System.arraycopy(paramArrayOfByte, k, localObject, 0, i - k);
/*     */         }
/*     */         else {
/* 418 */           str2 = new String(paramArrayOfByte, k, i - k, "UTF8");
/*     */           try
/*     */           {
/* 421 */             if ((putValue(str1, str2) != null) && (j == 0)) {
/* 422 */               PlatformLogger.getLogger("java.util.jar").warning("Duplicate name in Manifest: " + str1 + ".\n" + "Ensure that the manifest does not " + "have duplicate entries, and\n" + "that blank lines separate " + "individual sections in both your\n" + "manifest and in the META-INF/MANIFEST.MF " + "entry in the jar file.");
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (IllegalArgumentException localIllegalArgumentException)
/*     */           {
/* 433 */             throw new IOException("invalid header field name: " + str1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Name
/*     */   {
/*     */     private String name;
/* 449 */     private int hashCode = -1;
/*     */ 
/* 533 */     public static final Name MANIFEST_VERSION = new Name("Manifest-Version");
/*     */ 
/* 541 */     public static final Name SIGNATURE_VERSION = new Name("Signature-Version");
/*     */ 
/* 547 */     public static final Name CONTENT_TYPE = new Name("Content-Type");
/*     */ 
/* 556 */     public static final Name CLASS_PATH = new Name("Class-Path");
/*     */ 
/* 565 */     public static final Name MAIN_CLASS = new Name("Main-Class");
/*     */ 
/* 573 */     public static final Name SEALED = new Name("Sealed");
/*     */ 
/* 581 */     public static final Name EXTENSION_LIST = new Name("Extension-List");
/*     */ 
/* 589 */     public static final Name EXTENSION_NAME = new Name("Extension-Name");
/*     */ 
/* 597 */     public static final Name EXTENSION_INSTALLATION = new Name("Extension-Installation");
/*     */ 
/* 605 */     public static final Name IMPLEMENTATION_TITLE = new Name("Implementation-Title");
/*     */ 
/* 613 */     public static final Name IMPLEMENTATION_VERSION = new Name("Implementation-Version");
/*     */ 
/* 621 */     public static final Name IMPLEMENTATION_VENDOR = new Name("Implementation-Vendor");
/*     */ 
/* 629 */     public static final Name IMPLEMENTATION_VENDOR_ID = new Name("Implementation-Vendor-Id");
/*     */ 
/* 637 */     public static final Name IMPLEMENTATION_URL = new Name("Implementation-URL");
/*     */ 
/* 645 */     public static final Name SPECIFICATION_TITLE = new Name("Specification-Title");
/*     */ 
/* 653 */     public static final Name SPECIFICATION_VERSION = new Name("Specification-Version");
/*     */ 
/* 661 */     public static final Name SPECIFICATION_VENDOR = new Name("Specification-Vendor");
/*     */ 
/*     */     public Name(String paramString)
/*     */     {
/* 460 */       if (paramString == null) {
/* 461 */         throw new NullPointerException("name");
/*     */       }
/* 463 */       if (!isValid(paramString)) {
/* 464 */         throw new IllegalArgumentException(paramString);
/*     */       }
/* 466 */       this.name = paramString.intern();
/*     */     }
/*     */ 
/*     */     private static boolean isValid(String paramString) {
/* 470 */       int i = paramString.length();
/* 471 */       if ((i > 70) || (i == 0)) {
/* 472 */         return false;
/*     */       }
/* 474 */       for (int j = 0; j < i; j++) {
/* 475 */         if (!isValid(paramString.charAt(j))) {
/* 476 */           return false;
/*     */         }
/*     */       }
/* 479 */       return true;
/*     */     }
/*     */ 
/*     */     private static boolean isValid(char paramChar) {
/* 483 */       return (isAlpha(paramChar)) || (isDigit(paramChar)) || (paramChar == '_') || (paramChar == '-');
/*     */     }
/*     */ 
/*     */     private static boolean isAlpha(char paramChar) {
/* 487 */       return ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'A') && (paramChar <= 'Z'));
/*     */     }
/*     */ 
/*     */     private static boolean isDigit(char paramChar) {
/* 491 */       return (paramChar >= '0') && (paramChar <= '9');
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 501 */       if ((paramObject instanceof Name)) {
/* 502 */         Comparator localComparator = ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER;
/* 503 */         return localComparator.compare(this.name, ((Name)paramObject).name) == 0;
/*     */       }
/* 505 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 513 */       if (this.hashCode == -1) {
/* 514 */         this.hashCode = ASCIICaseInsensitiveComparator.lowerCaseHashCode(this.name);
/*     */       }
/* 516 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 523 */       return this.name;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.jar.Attributes
 * JD-Core Version:    0.6.2
 */