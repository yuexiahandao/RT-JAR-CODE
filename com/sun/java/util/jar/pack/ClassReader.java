/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ 
/*     */ class ClassReader
/*     */ {
/*     */   int verbose;
/*     */   Package pkg;
/*     */   Package.Class cls;
/*     */   long inPos;
/*     */   DataInputStream in;
/*     */   Map<Attribute.Layout, Attribute> attrDefs;
/*     */   Map attrCommands;
/*  56 */   String unknownAttrCommand = "error";
/*     */ 
/*     */   ClassReader(Package.Class paramClass, InputStream paramInputStream) throws IOException {
/*  59 */     this.pkg = paramClass.getPackage();
/*  60 */     this.cls = paramClass;
/*  61 */     this.verbose = this.pkg.verbose;
/*  62 */     this.in = new DataInputStream(new FilterInputStream(paramInputStream) {
/*     */       public int read(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2) throws IOException {
/*  64 */         int i = super.read(paramAnonymousArrayOfByte, paramAnonymousInt1, paramAnonymousInt2);
/*  65 */         if (i >= 0) ClassReader.this.inPos += i;
/*  66 */         return i;
/*     */       }
/*     */       public int read() throws IOException {
/*  69 */         int i = super.read();
/*  70 */         if (i >= 0) ClassReader.this.inPos += 1L;
/*  71 */         return i;
/*     */       }
/*     */       public long skip(long paramAnonymousLong) throws IOException {
/*  74 */         long l = super.skip(paramAnonymousLong);
/*  75 */         if (l >= 0L) ClassReader.this.inPos += l;
/*  76 */         return l;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void setAttrDefs(Map<Attribute.Layout, Attribute> paramMap) {
/*  82 */     this.attrDefs = paramMap;
/*     */   }
/*     */ 
/*     */   public void setAttrCommands(Map paramMap) {
/*  86 */     this.attrCommands = paramMap;
/*     */   }
/*     */ 
/*     */   private void skip(int paramInt, String paramString) throws IOException {
/*  90 */     Utils.log.warning("skipping " + paramInt + " bytes of " + paramString);
/*  91 */     long l1 = 0L;
/*  92 */     while (l1 < paramInt) {
/*  93 */       long l2 = this.in.skip(paramInt - l1);
/*  94 */       assert (l2 > 0L);
/*  95 */       l1 += l2;
/*     */     }
/*  97 */     assert (l1 == paramInt);
/*     */   }
/*     */ 
/*     */   private int readUnsignedShort() throws IOException {
/* 101 */     return this.in.readUnsignedShort();
/*     */   }
/*     */ 
/*     */   private int readInt() throws IOException {
/* 105 */     return this.in.readInt();
/*     */   }
/*     */ 
/*     */   private ConstantPool.Entry readRef() throws IOException
/*     */   {
/* 110 */     int i = this.in.readUnsignedShort();
/* 111 */     return i == 0 ? null : this.cls.cpMap[i];
/*     */   }
/*     */ 
/*     */   private ConstantPool.Entry readRef(byte paramByte) throws IOException {
/* 115 */     ConstantPool.Entry localEntry = readRef();
/* 116 */     assert (localEntry != null);
/* 117 */     assert (localEntry.tagMatches(paramByte));
/* 118 */     return localEntry;
/*     */   }
/*     */ 
/*     */   private ConstantPool.Entry readRefOrNull(byte paramByte) throws IOException {
/* 122 */     ConstantPool.Entry localEntry = readRef();
/* 123 */     assert ((localEntry == null) || (localEntry.tagMatches(paramByte)));
/* 124 */     return localEntry;
/*     */   }
/*     */ 
/*     */   private ConstantPool.Utf8Entry readUtf8Ref() throws IOException {
/* 128 */     return (ConstantPool.Utf8Entry)readRef((byte)1);
/*     */   }
/*     */ 
/*     */   private ConstantPool.ClassEntry readClassRef() throws IOException {
/* 132 */     return (ConstantPool.ClassEntry)readRef((byte)7);
/*     */   }
/*     */ 
/*     */   private ConstantPool.ClassEntry readClassRefOrNull() throws IOException {
/* 136 */     return (ConstantPool.ClassEntry)readRefOrNull((byte)7);
/*     */   }
/*     */ 
/*     */   private ConstantPool.SignatureEntry readSignatureRef() throws IOException
/*     */   {
/* 141 */     ConstantPool.Entry localEntry = readRef((byte)1);
/* 142 */     return ConstantPool.getSignatureEntry(localEntry.stringValue());
/*     */   }
/*     */ 
/*     */   void read() throws IOException {
/* 146 */     int i = 0;
/*     */     try {
/* 148 */       readMagicNumbers();
/* 149 */       readConstantPool();
/* 150 */       readHeader();
/* 151 */       readMembers(false);
/* 152 */       readMembers(true);
/* 153 */       readAttributes(0, this.cls);
/* 154 */       this.cls.finishReading();
/* 155 */       assert (0 >= this.in.read(new byte[1]));
/* 156 */       i = 1;
/*     */     } finally {
/* 158 */       if ((i == 0) && 
/* 159 */         (this.verbose > 0)) Utils.log.warning("Erroneous data at input offset " + this.inPos + " of " + this.cls.file); 
/*     */     }
/*     */   }
/*     */ 
/*     */   void readMagicNumbers()
/*     */     throws IOException
/*     */   {
/* 165 */     this.cls.magic = this.in.readInt();
/* 166 */     if (this.cls.magic != -889275714) {
/* 167 */       throw new Attribute.FormatException("Bad magic number in class file " + Integer.toHexString(this.cls.magic), 0, "magic-number", "pass");
/*     */     }
/*     */ 
/* 171 */     this.cls.minver = ((short)readUnsignedShort());
/* 172 */     this.cls.majver = ((short)readUnsignedShort());
/*     */ 
/* 174 */     String str = checkVersion(this.cls.majver, this.cls.minver);
/* 175 */     if (str != null)
/* 176 */       throw new Attribute.FormatException("classfile version too " + str + ": " + this.cls.majver + "." + this.cls.minver + " in " + this.cls.file, 0, "version", "pass");
/*     */   }
/*     */ 
/*     */   private String checkVersion(int paramInt1, int paramInt2)
/*     */   {
/* 184 */     if ((paramInt1 < this.pkg.min_class_majver) || ((paramInt1 == this.pkg.min_class_majver) && (paramInt2 < this.pkg.min_class_minver)))
/*     */     {
/* 187 */       return "small";
/*     */     }
/* 189 */     if ((paramInt1 > this.pkg.max_class_majver) || ((paramInt1 == this.pkg.max_class_majver) && (paramInt2 > this.pkg.max_class_minver)))
/*     */     {
/* 192 */       return "large";
/*     */     }
/* 194 */     return null;
/*     */   }
/*     */ 
/*     */   void readConstantPool() throws IOException {
/* 198 */     int i = this.in.readUnsignedShort();
/*     */ 
/* 201 */     int[] arrayOfInt = new int[i * 4];
/* 202 */     int j = 0;
/*     */ 
/* 204 */     ConstantPool.Entry[] arrayOfEntry = new ConstantPool.Entry[i];
/* 205 */     arrayOfEntry[0] = null;
/*     */     int m;
/* 206 */     for (int k = 1; k < i; k++)
/*     */     {
/* 208 */       m = this.in.readByte();
/* 209 */       switch (m) {
/*     */       case 1:
/* 211 */         arrayOfEntry[k] = ConstantPool.getUtf8Entry(this.in.readUTF());
/* 212 */         break;
/*     */       case 3:
/* 215 */         arrayOfEntry[k] = ConstantPool.getLiteralEntry(Integer.valueOf(this.in.readInt()));
/*     */ 
/* 217 */         break;
/*     */       case 4:
/* 220 */         arrayOfEntry[k] = ConstantPool.getLiteralEntry(Float.valueOf(this.in.readFloat()));
/*     */ 
/* 222 */         break;
/*     */       case 5:
/* 225 */         arrayOfEntry[k] = ConstantPool.getLiteralEntry(Long.valueOf(this.in.readLong()));
/* 226 */         arrayOfEntry[(++k)] = null;
/*     */ 
/* 228 */         break;
/*     */       case 6:
/* 231 */         arrayOfEntry[k] = ConstantPool.getLiteralEntry(Double.valueOf(this.in.readDouble()));
/* 232 */         arrayOfEntry[(++k)] = null;
/*     */ 
/* 234 */         break;
/*     */       case 7:
/*     */       case 8:
/* 239 */         arrayOfInt[(j++)] = k;
/* 240 */         arrayOfInt[(j++)] = m;
/* 241 */         arrayOfInt[(j++)] = this.in.readUnsignedShort();
/* 242 */         arrayOfInt[(j++)] = -1;
/* 243 */         break;
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/* 248 */         arrayOfInt[(j++)] = k;
/* 249 */         arrayOfInt[(j++)] = m;
/* 250 */         arrayOfInt[(j++)] = this.in.readUnsignedShort();
/* 251 */         arrayOfInt[(j++)] = this.in.readUnsignedShort();
/* 252 */         break;
/*     */       case 2:
/*     */       default:
/* 254 */         throw new ClassFormatException("Bad constant pool tag " + m + " in File: " + this.cls.file.nameString + " at pos: " + this.inPos);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 261 */     while (j > 0) {
/* 262 */       if (this.verbose > 3)
/* 263 */         Utils.log.fine("CP fixups [" + j / 4 + "]");
/* 264 */       k = j;
/* 265 */       j = 0;
/* 266 */       for (m = 0; m < k; ) {
/* 267 */         int n = arrayOfInt[(m++)];
/* 268 */         int i1 = arrayOfInt[(m++)];
/* 269 */         int i2 = arrayOfInt[(m++)];
/* 270 */         int i3 = arrayOfInt[(m++)];
/* 271 */         if (this.verbose > 3)
/* 272 */           Utils.log.fine("  cp[" + n + "] = " + ConstantPool.tagName(i1) + "{" + i2 + "," + i3 + "}");
/* 273 */         if ((arrayOfEntry[i2] == null) || ((i3 >= 0) && (arrayOfEntry[i3] == null)))
/*     */         {
/* 275 */           arrayOfInt[(j++)] = n;
/* 276 */           arrayOfInt[(j++)] = i1;
/* 277 */           arrayOfInt[(j++)] = i2;
/* 278 */           arrayOfInt[(j++)] = i3;
/*     */         }
/*     */         else {
/* 281 */           switch (i1) {
/*     */           case 7:
/* 283 */             arrayOfEntry[n] = ConstantPool.getClassEntry(arrayOfEntry[i2].stringValue());
/* 284 */             break;
/*     */           case 8:
/* 286 */             arrayOfEntry[n] = ConstantPool.getStringEntry(arrayOfEntry[i2].stringValue());
/* 287 */             break;
/*     */           case 9:
/*     */           case 10:
/*     */           case 11:
/* 291 */             ConstantPool.ClassEntry localClassEntry = (ConstantPool.ClassEntry)arrayOfEntry[i2];
/* 292 */             ConstantPool.DescriptorEntry localDescriptorEntry = (ConstantPool.DescriptorEntry)arrayOfEntry[i3];
/* 293 */             arrayOfEntry[n] = ConstantPool.getMemberEntry((byte)i1, localClassEntry, localDescriptorEntry);
/* 294 */             break;
/*     */           case 12:
/* 296 */             ConstantPool.Utf8Entry localUtf8Entry1 = (ConstantPool.Utf8Entry)arrayOfEntry[i2];
/* 297 */             ConstantPool.Utf8Entry localUtf8Entry2 = (ConstantPool.Utf8Entry)arrayOfEntry[i3];
/* 298 */             arrayOfEntry[n] = ConstantPool.getDescriptorEntry(localUtf8Entry1, localUtf8Entry2);
/* 299 */             break;
/*     */           default:
/* 301 */             if (!$assertionsDisabled) throw new AssertionError(); break; } 
/*     */         }
/*     */       }
/* 304 */       assert (j < k);
/*     */     }
/*     */ 
/* 307 */     this.cls.cpMap = arrayOfEntry;
/*     */   }
/*     */ 
/*     */   void readHeader() throws IOException {
/* 311 */     this.cls.flags = readUnsignedShort();
/* 312 */     this.cls.thisClass = readClassRef();
/* 313 */     this.cls.superClass = readClassRefOrNull();
/* 314 */     int i = readUnsignedShort();
/* 315 */     this.cls.interfaces = new ConstantPool.ClassEntry[i];
/* 316 */     for (int j = 0; j < i; j++)
/* 317 */       this.cls.interfaces[j] = readClassRef();
/*     */   }
/*     */ 
/*     */   void readMembers(boolean paramBoolean) throws IOException
/*     */   {
/* 322 */     int i = readUnsignedShort();
/* 323 */     for (int j = 0; j < i; j++)
/* 324 */       readMember(paramBoolean);
/*     */   }
/*     */ 
/*     */   void readMember(boolean paramBoolean) throws IOException
/*     */   {
/* 329 */     int i = readUnsignedShort();
/* 330 */     ConstantPool.Utf8Entry localUtf8Entry = readUtf8Ref();
/* 331 */     ConstantPool.SignatureEntry localSignatureEntry = readSignatureRef();
/* 332 */     ConstantPool.DescriptorEntry localDescriptorEntry = ConstantPool.getDescriptorEntry(localUtf8Entry, localSignatureEntry);
/*     */     Object localObject;
/* 334 */     if (!paramBoolean)
/*     */     {
/*     */       Package.Class tmp36_33 = this.cls; tmp36_33.getClass(); localObject = new Package.Class.Field(tmp36_33, i, localDescriptorEntry);
/*     */     }
/*     */     else
/*     */     {
/*     */       Package.Class tmp60_57 = this.cls; tmp60_57.getClass(); localObject = new Package.Class.Method(tmp60_57, i, localDescriptorEntry);
/* 338 */     }readAttributes(!paramBoolean ? 1 : 2, (Attribute.Holder)localObject);
/*     */   }
/*     */ 
/*     */   void readAttributes(int paramInt, Attribute.Holder paramHolder) throws IOException {
/* 342 */     int i = readUnsignedShort();
/* 343 */     if (i == 0) return;
/* 344 */     if (this.verbose > 3)
/* 345 */       Utils.log.fine("readAttributes " + paramHolder + " [" + i + "]");
/* 346 */     for (int j = 0; j < i; j++) {
/* 347 */       String str1 = readUtf8Ref().stringValue();
/* 348 */       int k = readInt();
/*     */       Object localObject2;
/*     */       Object localObject3;
/* 350 */       if (this.attrCommands != null) {
/* 351 */         localObject1 = Attribute.keyForLookup(paramInt, str1);
/* 352 */         String str2 = (String)this.attrCommands.get(localObject1);
/* 353 */         if (str2 != null) {
/* 354 */           localObject2 = str2; int n = -1; switch (((String)localObject2).hashCode()) { case 3433489:
/* 354 */             if (((String)localObject2).equals("pass")) n = 0; break;
/*     */           case 96784904:
/* 354 */             if (((String)localObject2).equals("error")) n = 1; break;
/*     */           case 109773592:
/* 354 */             if (((String)localObject2).equals("strip")) n = 2; break; } switch (n) {
/*     */           case 0:
/* 356 */             localObject3 = "passing attribute bitwise in " + paramHolder;
/* 357 */             throw new Attribute.FormatException((String)localObject3, paramInt, str1, str2);
/*     */           case 1:
/* 359 */             String str3 = "attribute not allowed in " + paramHolder;
/* 360 */             throw new Attribute.FormatException(str3, paramInt, str1, str2);
/*     */           case 2:
/* 362 */             skip(k, str1 + " attribute in " + paramHolder);
/* 363 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 368 */       Object localObject1 = Attribute.lookup(Package.attrDefs, paramInt, str1);
/* 369 */       if ((this.verbose > 4) && (localObject1 != null))
/* 370 */         Utils.log.fine("pkg_attribute_lookup " + str1 + " = " + localObject1);
/* 371 */       if (localObject1 == null) {
/* 372 */         localObject1 = Attribute.lookup(this.attrDefs, paramInt, str1);
/* 373 */         if ((this.verbose > 4) && (localObject1 != null))
/* 374 */           Utils.log.fine("this " + str1 + " = " + localObject1);
/*     */       }
/* 376 */       if (localObject1 == null) {
/* 377 */         localObject1 = Attribute.lookup(null, paramInt, str1);
/* 378 */         if ((this.verbose > 4) && (localObject1 != null))
/* 379 */           Utils.log.fine("null_attribute_lookup " + str1 + " = " + localObject1);
/*     */       }
/* 381 */       if ((localObject1 == null) && (k == 0))
/*     */       {
/* 385 */         localObject1 = Attribute.find(paramInt, str1, "");
/*     */       }
/* 387 */       int m = (paramInt == 3) && ((str1.equals("StackMap")) || (str1.equals("StackMapX"))) ? 1 : 0;
/*     */ 
/* 390 */       if (m != 0)
/*     */       {
/* 392 */         localObject2 = (Code)paramHolder;
/*     */ 
/* 394 */         if ((((Code)localObject2).max_stack >= 65536) || (((Code)localObject2).max_locals >= 65536) || (((Code)localObject2).getLength() >= 65536) || (str1.endsWith("X")))
/*     */         {
/* 400 */           localObject1 = null;
/*     */         }
/*     */       }
/* 403 */       if (localObject1 == null) {
/* 404 */         if (m != 0)
/*     */         {
/* 406 */           localObject2 = "unsupported StackMap variant in " + paramHolder;
/* 407 */           throw new Attribute.FormatException((String)localObject2, paramInt, str1, "pass");
/*     */         }
/* 409 */         if ("strip".equals(this.unknownAttrCommand))
/*     */         {
/* 411 */           skip(k, "unknown " + str1 + " attribute in " + paramHolder);
/*     */         }
/*     */         else {
/* 414 */           localObject2 = " is unknown attribute in class " + paramHolder;
/* 415 */           throw new Attribute.FormatException((String)localObject2, paramInt, str1, this.unknownAttrCommand);
/*     */         }
/*     */       }
/*     */       else {
/* 419 */         if ((((Attribute)localObject1).layout() == Package.attrCodeEmpty) || (((Attribute)localObject1).layout() == Package.attrInnerClassesEmpty))
/*     */         {
/* 422 */           long l = this.inPos;
/* 423 */           if ("Code".equals(((Attribute)localObject1).name())) {
/* 424 */             localObject3 = (Package.Class.Method)paramHolder;
/* 425 */             ((Package.Class.Method)localObject3).code = new Code((Package.Class.Method)localObject3);
/*     */             try {
/* 427 */               readCode(((Package.Class.Method)localObject3).code);
/*     */             } catch (Instruction.FormatException localFormatException) {
/* 429 */               String str4 = localFormatException.getMessage() + " in " + paramHolder;
/* 430 */               throw new ClassFormatException(str4, localFormatException);
/*     */             }
/*     */           } else {
/* 433 */             assert (paramHolder == this.cls);
/* 434 */             readInnerClasses(this.cls);
/*     */           }
/* 436 */           assert (k == this.inPos - l);
/*     */         }
/* 438 */         else if (k > 0) {
/* 439 */           byte[] arrayOfByte = new byte[k];
/* 440 */           this.in.readFully(arrayOfByte);
/* 441 */           localObject1 = ((Attribute)localObject1).addContent(arrayOfByte);
/*     */         }
/* 443 */         if ((((Attribute)localObject1).size() == 0) && (!((Attribute)localObject1).layout().isEmpty())) {
/* 444 */           throw new ClassFormatException(str1 + ": attribute length cannot be zero, in " + paramHolder);
/*     */         }
/*     */ 
/* 447 */         paramHolder.addAttribute((Attribute)localObject1);
/* 448 */         if (this.verbose > 2)
/* 449 */           Utils.log.fine("read " + localObject1); 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 454 */   void readCode(Code paramCode) throws IOException { paramCode.max_stack = readUnsignedShort();
/* 455 */     paramCode.max_locals = readUnsignedShort();
/* 456 */     paramCode.bytes = new byte[readInt()];
/* 457 */     this.in.readFully(paramCode.bytes);
/* 458 */     Instruction.opcodeChecker(paramCode.bytes);
/* 459 */     int i = readUnsignedShort();
/* 460 */     paramCode.setHandlerCount(i);
/* 461 */     for (int j = 0; j < i; j++) {
/* 462 */       paramCode.handler_start[j] = readUnsignedShort();
/* 463 */       paramCode.handler_end[j] = readUnsignedShort();
/* 464 */       paramCode.handler_catch[j] = readUnsignedShort();
/* 465 */       paramCode.handler_class[j] = readClassRefOrNull();
/*     */     }
/* 467 */     readAttributes(3, paramCode); }
/*     */ 
/*     */   void readInnerClasses(Package.Class paramClass) throws IOException
/*     */   {
/* 471 */     int i = readUnsignedShort();
/* 472 */     ArrayList localArrayList = new ArrayList(i);
/* 473 */     for (int j = 0; j < i; j++) {
/* 474 */       Package.InnerClass localInnerClass = new Package.InnerClass(readClassRef(), readClassRefOrNull(), (ConstantPool.Utf8Entry)readRefOrNull((byte)1), readUnsignedShort());
/*     */ 
/* 479 */       localArrayList.add(localInnerClass);
/*     */     }
/* 481 */     paramClass.innerClasses = localArrayList;
/*     */   }
/*     */ 
/*     */   static class ClassFormatException extends IOException
/*     */   {
/*     */     public ClassFormatException(String paramString) {
/* 487 */       super();
/*     */     }
/*     */ 
/*     */     public ClassFormatException(String paramString, Throwable paramThrowable) {
/* 491 */       super(paramThrowable);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.ClassReader
 * JD-Core Version:    0.6.2
 */