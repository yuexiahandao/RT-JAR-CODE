/*     */ package sun.invoke.anon;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ConstantPoolPatch
/*     */ {
/*     */   final ConstantPoolParser outer;
/*     */   final java.lang.Object[] patchArray;
/*     */   private static final int CONSTANT_MemberRef_MASK = 11;
/*     */   private static final Map<Class<?>, Byte> CONSTANT_VALUE_CLASS_TAG;
/*     */   private static final Class[] CONSTANT_VALUE_CLASS;
/*     */ 
/*     */   ConstantPoolPatch(ConstantPoolParser paramConstantPoolParser)
/*     */   {
/*  53 */     this.outer = paramConstantPoolParser;
/*  54 */     this.patchArray = new java.lang.Object[paramConstantPoolParser.getLength()];
/*     */   }
/*     */ 
/*     */   public ConstantPoolPatch(byte[] paramArrayOfByte)
/*     */     throws InvalidConstantPoolFormatException
/*     */   {
/*  65 */     this(new ConstantPoolParser(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public ConstantPoolPatch(Class<?> paramClass)
/*     */     throws IOException, InvalidConstantPoolFormatException
/*     */   {
/*  76 */     this(new ConstantPoolParser(paramClass));
/*     */   }
/*     */ 
/*     */   public ConstantPoolPatch(ConstantPoolPatch paramConstantPoolPatch)
/*     */   {
/*  87 */     this.outer = paramConstantPoolPatch.outer;
/*  88 */     this.patchArray = ((java.lang.Object[])paramConstantPoolPatch.patchArray.clone());
/*     */   }
/*     */ 
/*     */   public ConstantPoolParser getParser()
/*     */   {
/*  93 */     return this.outer;
/*     */   }
/*     */ 
/*     */   public byte getTag(int paramInt)
/*     */   {
/*  98 */     return this.outer.getTag(paramInt);
/*     */   }
/*     */ 
/*     */   public java.lang.Object getPatch(int paramInt)
/*     */   {
/* 107 */     java.lang.Object localObject = this.patchArray[paramInt];
/* 108 */     if (localObject == null) return null;
/* 109 */     switch (getTag(paramInt)) {
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/* 113 */       if ((localObject instanceof String))
/* 114 */         localObject = stripSemis(2, (String)localObject); break;
/*     */     case 12:
/* 117 */       if ((localObject instanceof String))
/* 118 */         localObject = stripSemis(1, (String)localObject);
/*     */       break;
/*     */     }
/* 121 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 126 */     Arrays.fill(this.patchArray, null);
/*     */   }
/*     */ 
/*     */   public void clear(int paramInt)
/*     */   {
/* 131 */     this.patchArray[paramInt] = null;
/*     */   }
/*     */ 
/*     */   public java.lang.Object[] getPatches()
/*     */   {
/* 136 */     return (java.lang.Object[])this.patchArray.clone();
/*     */   }
/*     */ 
/*     */   public java.lang.Object[] getOriginalCP() throws InvalidConstantPoolFormatException
/*     */   {
/* 141 */     return getOriginalCP(0, this.patchArray.length, -1);
/*     */   }
/*     */ 
/*     */   public void putPatches(final Map<String, String> paramMap, final Map<String, java.lang.Object> paramMap1, final Map<java.lang.Object, java.lang.Object> paramMap2, boolean paramBoolean)
/*     */     throws InvalidConstantPoolFormatException
/*     */   {
/*     */     final HashSet localHashSet1;
/*     */     final HashSet localHashSet2;
/*     */     final HashSet localHashSet3;
/* 158 */     if (paramBoolean) {
/* 159 */       localHashSet1 = paramMap == null ? null : new HashSet();
/* 160 */       localHashSet2 = paramMap1 == null ? null : new HashSet();
/* 161 */       localHashSet3 = paramMap2 == null ? null : new HashSet();
/*     */     } else {
/* 163 */       localHashSet1 = null;
/* 164 */       localHashSet2 = null;
/* 165 */       localHashSet3 = null;
/*     */     }
/*     */ 
/* 168 */     this.outer.parse(new ConstantPoolVisitor()
/*     */     {
/*     */       public void visitUTF8(int paramAnonymousInt, byte paramAnonymousByte, String paramAnonymousString)
/*     */       {
/* 172 */         ConstantPoolPatch.this.putUTF8(paramAnonymousInt, (String)paramMap.get(paramAnonymousString));
/* 173 */         if (localHashSet1 != null) localHashSet1.add(paramAnonymousString);
/*     */       }
/*     */ 
/*     */       public void visitConstantValue(int paramAnonymousInt, byte paramAnonymousByte, java.lang.Object paramAnonymousObject)
/*     */       {
/* 178 */         ConstantPoolPatch.this.putConstantValue(paramAnonymousInt, paramAnonymousByte, paramMap2.get(paramAnonymousObject));
/* 179 */         if (localHashSet3 != null) localHashSet3.add(paramAnonymousObject);
/*     */       }
/*     */ 
/*     */       public void visitConstantString(int paramAnonymousInt1, byte paramAnonymousByte, String paramAnonymousString, int paramAnonymousInt2)
/*     */       {
/* 184 */         if (paramAnonymousByte == 7) {
/* 185 */           ConstantPoolPatch.this.putConstantValue(paramAnonymousInt1, paramAnonymousByte, paramMap1.get(paramAnonymousString));
/* 186 */           if (localHashSet2 != null) localHashSet2.add(paramAnonymousString); 
/*     */         }
/* 188 */         else { assert (paramAnonymousByte == 8);
/* 189 */           visitConstantValue(paramAnonymousInt1, paramAnonymousByte, paramAnonymousString);
/*     */         }
/*     */       }
/*     */     });
/* 193 */     if (localHashSet1 != null) paramMap.keySet().removeAll(localHashSet1);
/* 194 */     if (localHashSet2 != null) paramMap1.keySet().removeAll(localHashSet2);
/* 195 */     if (localHashSet3 != null) paramMap2.keySet().removeAll(localHashSet3);
/*     */   }
/*     */ 
/*     */   java.lang.Object[] getOriginalCP(final int paramInt1, final int paramInt2, final int paramInt3)
/*     */     throws InvalidConstantPoolFormatException
/*     */   {
/* 201 */     final java.lang.Object[] arrayOfObject = new java.lang.Object[paramInt2 - paramInt1];
/* 202 */     this.outer.parse(new ConstantPoolVisitor()
/*     */     {
/*     */       void show(int paramAnonymousInt, byte paramAnonymousByte, java.lang.Object paramAnonymousObject) {
/* 205 */         if ((paramAnonymousInt < paramInt1) || (paramAnonymousInt >= paramInt2)) return;
/* 206 */         if ((1 << paramAnonymousByte & paramInt3) == 0) return;
/* 207 */         arrayOfObject[(paramAnonymousInt - paramInt1)] = paramAnonymousObject;
/*     */       }
/*     */ 
/*     */       public void visitUTF8(int paramAnonymousInt, byte paramAnonymousByte, String paramAnonymousString)
/*     */       {
/* 212 */         show(paramAnonymousInt, paramAnonymousByte, paramAnonymousString);
/*     */       }
/*     */ 
/*     */       public void visitConstantValue(int paramAnonymousInt, byte paramAnonymousByte, java.lang.Object paramAnonymousObject)
/*     */       {
/* 217 */         assert (paramAnonymousByte != 8);
/* 218 */         show(paramAnonymousInt, paramAnonymousByte, paramAnonymousObject);
/*     */       }
/*     */ 
/*     */       public void visitConstantString(int paramAnonymousInt1, byte paramAnonymousByte, String paramAnonymousString, int paramAnonymousInt2)
/*     */       {
/* 224 */         show(paramAnonymousInt1, paramAnonymousByte, paramAnonymousString);
/*     */       }
/*     */ 
/*     */       public void visitMemberRef(int paramAnonymousInt1, byte paramAnonymousByte, String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, int paramAnonymousInt2, int paramAnonymousInt3)
/*     */       {
/* 232 */         show(paramAnonymousInt1, paramAnonymousByte, new String[] { paramAnonymousString1, paramAnonymousString2, paramAnonymousString3 });
/*     */       }
/*     */ 
/*     */       public void visitDescriptor(int paramAnonymousInt1, byte paramAnonymousByte, String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt2, int paramAnonymousInt3)
/*     */       {
/* 239 */         show(paramAnonymousInt1, paramAnonymousByte, new String[] { paramAnonymousString1, paramAnonymousString2 });
/*     */       }
/*     */     });
/* 242 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   void writeHead(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 249 */     this.outer.writePatchedHead(paramOutputStream, this.patchArray);
/*     */   }
/*     */ 
/*     */   void writeTail(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 256 */     this.outer.writeTail(paramOutputStream);
/*     */   }
/*     */ 
/*     */   private void checkConstantTag(byte paramByte, java.lang.Object paramObject) {
/* 260 */     if (paramObject == null) {
/* 261 */       throw new IllegalArgumentException("invalid null constant value");
/*     */     }
/* 263 */     if (classForTag(paramByte) != paramObject.getClass())
/* 264 */       throw new IllegalArgumentException("invalid constant value" + (paramByte == 0 ? "" : new StringBuilder().append(" for tag ").append(ConstantPoolVisitor.tagName(paramByte)).toString()) + " of class " + paramObject.getClass());
/*     */   }
/*     */ 
/*     */   private void checkTag(int paramInt, byte paramByte)
/*     */   {
/* 272 */     byte b = this.outer.tags[paramInt];
/* 273 */     if (b != paramByte)
/* 274 */       throw new IllegalArgumentException("invalid put operation for " + ConstantPoolVisitor.tagName(paramByte) + " at index " + paramInt + " found " + ConstantPoolVisitor.tagName(b));
/*     */   }
/*     */ 
/*     */   private void checkTagMask(int paramInt1, int paramInt2)
/*     */   {
/* 281 */     byte b = this.outer.tags[paramInt1];
/* 282 */     int i = (b & 0x1F) == b ? 1 << b : 0;
/* 283 */     if ((i & paramInt2) == 0)
/* 284 */       throw new IllegalArgumentException("invalid put operation at index " + paramInt1 + " found " + ConstantPoolVisitor.tagName(b));
/*     */   }
/*     */ 
/*     */   private static void checkMemberName(String paramString)
/*     */   {
/* 290 */     if (paramString.indexOf(';') >= 0)
/* 291 */       throw new IllegalArgumentException("memberName " + paramString + " contains a ';'");
/*     */   }
/*     */ 
/*     */   public void putUTF8(int paramInt, String paramString)
/*     */   {
/* 304 */     if (paramString == null) { clear(paramInt); return; }
/* 305 */     checkTag(paramInt, (byte)1);
/* 306 */     this.patchArray[paramInt] = paramString;
/*     */   }
/*     */ 
/*     */   public void putConstantValue(int paramInt, java.lang.Object paramObject)
/*     */   {
/* 330 */     if (paramObject == null) { clear(paramInt); return; }
/* 331 */     byte b = tagForConstant(paramObject.getClass());
/* 332 */     checkConstantTag(b, paramObject);
/* 333 */     checkTag(paramInt, b);
/* 334 */     this.patchArray[paramInt] = paramObject;
/*     */   }
/*     */ 
/*     */   public void putConstantValue(int paramInt, byte paramByte, java.lang.Object paramObject)
/*     */   {
/* 358 */     if (paramObject == null) { clear(paramInt); return; }
/* 359 */     checkTag(paramInt, paramByte);
/* 360 */     if ((paramByte == 7) && ((paramObject instanceof String)))
/* 361 */       checkClassName((String)paramObject);
/* 362 */     else if (paramByte != 8)
/*     */     {
/* 366 */       checkConstantTag(paramByte, paramObject);
/*     */     }
/* 368 */     checkTag(paramInt, paramByte);
/* 369 */     this.patchArray[paramInt] = paramObject;
/*     */   }
/*     */ 
/*     */   public void putDescriptor(int paramInt, String paramString1, String paramString2)
/*     */   {
/* 384 */     checkTag(paramInt, (byte)12);
/* 385 */     checkMemberName(paramString1);
/* 386 */     this.patchArray[paramInt] = addSemis(paramString1, new String[] { paramString2 });
/*     */   }
/*     */ 
/*     */   public void putMemberRef(int paramInt, byte paramByte, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 405 */     checkTagMask(paramByte, 11);
/* 406 */     checkTag(paramInt, paramByte);
/* 407 */     checkClassName(paramString1);
/* 408 */     checkMemberName(paramString2);
/* 409 */     if (paramString3.startsWith("(") == (paramByte == 9))
/* 410 */       throw new IllegalArgumentException("bad signature: " + paramString3);
/* 411 */     this.patchArray[paramInt] = addSemis(paramString1, new String[] { paramString2, paramString3 });
/*     */   }
/*     */ 
/*     */   static Class<?> classForTag(byte paramByte)
/*     */   {
/* 440 */     if ((paramByte & 0xFF) >= CONSTANT_VALUE_CLASS.length)
/* 441 */       return null;
/* 442 */     return CONSTANT_VALUE_CLASS[paramByte];
/*     */   }
/*     */ 
/*     */   static byte tagForConstant(Class<?> paramClass) {
/* 446 */     Byte localByte = (Byte)CONSTANT_VALUE_CLASS_TAG.get(paramClass);
/* 447 */     return localByte == null ? 0 : localByte.byteValue();
/*     */   }
/*     */ 
/*     */   private static void checkClassName(String paramString) {
/* 451 */     if ((paramString.indexOf('/') >= 0) || (paramString.indexOf(';') >= 0))
/* 452 */       throw new IllegalArgumentException("invalid class name " + paramString);
/*     */   }
/*     */ 
/*     */   static String addSemis(String paramString, String[] paramArrayOfString) {
/* 456 */     StringBuilder localStringBuilder = new StringBuilder(paramString.length() * 5);
/* 457 */     localStringBuilder.append(paramString);
/* 458 */     for (String str : paramArrayOfString) {
/* 459 */       localStringBuilder.append(';').append(str);
/*     */     }
/* 461 */     ??? = localStringBuilder.toString();
/* 462 */     assert (stripSemis(paramArrayOfString.length, ???)[0].equals(paramString));
/* 463 */     assert (stripSemis(paramArrayOfString.length, ???)[1].equals(paramArrayOfString[0]));
/* 464 */     assert ((paramArrayOfString.length == 1) || (stripSemis(paramArrayOfString.length, ???)[2].equals(paramArrayOfString[1])));
/*     */ 
/* 466 */     return ???;
/*     */   }
/*     */ 
/*     */   static String[] stripSemis(int paramInt, String paramString) {
/* 470 */     String[] arrayOfString = new String[paramInt + 1];
/* 471 */     int i = 0;
/* 472 */     for (int j = 0; j < paramInt; j++) {
/* 473 */       int k = paramString.indexOf(';', i);
/* 474 */       if (k < 0) k = paramString.length();
/* 475 */       arrayOfString[j] = paramString.substring(i, k);
/* 476 */       i = k;
/*     */     }
/* 478 */     arrayOfString[paramInt] = paramString.substring(i);
/* 479 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 483 */     StringBuilder localStringBuilder = new StringBuilder(getClass().getName());
/* 484 */     localStringBuilder.append("{");
/* 485 */     java.lang.Object[] arrayOfObject = null;
/* 486 */     for (int i = 0; i < this.patchArray.length; i++)
/* 487 */       if (this.patchArray[i] != null) {
/* 488 */         if (arrayOfObject != null)
/* 489 */           localStringBuilder.append(", ");
/*     */         else {
/*     */           try {
/* 492 */             arrayOfObject = getOriginalCP();
/*     */           } catch (InvalidConstantPoolFormatException localInvalidConstantPoolFormatException) {
/* 494 */             arrayOfObject = new java.lang.Object[0];
/*     */           }
/*     */         }
/* 497 */         String str = i < arrayOfObject.length ? arrayOfObject[i] : "?";
/* 498 */         localStringBuilder.append(str).append("=").append(this.patchArray[i]);
/*     */       }
/* 500 */     localStringBuilder.append("}");
/* 501 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 419 */     CONSTANT_VALUE_CLASS_TAG = new IdentityHashMap();
/*     */ 
/* 421 */     CONSTANT_VALUE_CLASS = new Class[16];
/*     */ 
/* 423 */     java.lang.Object[][] arrayOfObject;1 = { { Integer.class, Byte.valueOf(3) }, { Long.class, Byte.valueOf(5) }, { Float.class, Byte.valueOf(4) }, { Double.class, Byte.valueOf(6) }, { String.class, Byte.valueOf(8) }, { Class.class, Byte.valueOf(7) } };
/*     */ 
/* 431 */     for ([Ljava.lang.Object localObject; : arrayOfObject;1) {
/* 432 */       Class localClass = (Class)localObject;[0];
/* 433 */       Byte localByte = (Byte)localObject;[1];
/* 434 */       CONSTANT_VALUE_CLASS_TAG.put(localClass, localByte);
/* 435 */       CONSTANT_VALUE_CLASS[localByte.byteValue()] = localClass;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.anon.ConstantPoolPatch
 * JD-Core Version:    0.6.2
 */