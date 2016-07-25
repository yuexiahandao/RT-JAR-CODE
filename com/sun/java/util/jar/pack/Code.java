/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ 
/*     */ class Code extends Attribute.Holder
/*     */ {
/*     */   Package.Class.Method m;
/*  59 */   private static final ConstantPool.Entry[] noRefs = ConstantPool.noRefs;
/*     */   int max_stack;
/*     */   int max_locals;
/*  65 */   ConstantPool.Entry[] handler_class = noRefs;
/*  66 */   int[] handler_start = Constants.noInts;
/*  67 */   int[] handler_end = Constants.noInts;
/*  68 */   int[] handler_catch = Constants.noInts;
/*     */   byte[] bytes;
/*     */   Fixups fixups;
/*     */   Object insnMap;
/*     */   static final boolean shrinkMaps = true;
/*     */ 
/*     */   public Code(Package.Class.Method paramMethod)
/*     */   {
/*  42 */     this.m = paramMethod;
/*     */   }
/*     */ 
/*     */   public Package.Class.Method getMethod() {
/*  46 */     return this.m;
/*     */   }
/*     */   public Package.Class thisClass() {
/*  49 */     return this.m.thisClass();
/*     */   }
/*     */   public Package getPackage() {
/*  52 */     return this.m.thisClass().getPackage();
/*     */   }
/*     */ 
/*     */   public ConstantPool.Entry[] getCPMap() {
/*  56 */     return this.m.getCPMap();
/*     */   }
/*     */ 
/*     */   int getLength()
/*     */   {
/*  74 */     return this.bytes.length;
/*     */   }
/*     */   int getMaxStack() {
/*  77 */     return this.max_stack;
/*     */   }
/*     */   void setMaxStack(int paramInt) {
/*  80 */     this.max_stack = paramInt;
/*     */   }
/*     */ 
/*     */   int getMaxNALocals() {
/*  84 */     int i = this.m.getArgumentSize();
/*  85 */     return this.max_locals - i;
/*     */   }
/*     */   void setMaxNALocals(int paramInt) {
/*  88 */     int i = this.m.getArgumentSize();
/*  89 */     this.max_locals = (i + paramInt);
/*     */   }
/*     */ 
/*     */   int getHandlerCount() {
/*  93 */     assert (this.handler_class.length == this.handler_start.length);
/*  94 */     assert (this.handler_class.length == this.handler_end.length);
/*  95 */     assert (this.handler_class.length == this.handler_catch.length);
/*  96 */     return this.handler_class.length;
/*     */   }
/*     */   void setHandlerCount(int paramInt) {
/*  99 */     if (paramInt > 0) {
/* 100 */       this.handler_class = new ConstantPool.Entry[paramInt];
/* 101 */       this.handler_start = new int[paramInt];
/* 102 */       this.handler_end = new int[paramInt];
/* 103 */       this.handler_catch = new int[paramInt];
/*     */     }
/*     */   }
/*     */ 
/*     */   void setBytes(byte[] paramArrayOfByte)
/*     */   {
/* 109 */     this.bytes = paramArrayOfByte;
/* 110 */     if (this.fixups != null)
/* 111 */       this.fixups.setBytes(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   void setInstructionMap(int[] paramArrayOfInt, int paramInt)
/*     */   {
/* 117 */     this.insnMap = allocateInstructionMap(paramArrayOfInt, paramInt);
/*     */   }
/*     */ 
/*     */   void setInstructionMap(int[] paramArrayOfInt) {
/* 121 */     setInstructionMap(paramArrayOfInt, paramArrayOfInt.length);
/*     */   }
/*     */ 
/*     */   int[] getInstructionMap() {
/* 125 */     return expandInstructionMap(getInsnMap());
/*     */   }
/*     */ 
/*     */   void addFixups(Collection paramCollection) {
/* 129 */     if (this.fixups == null) {
/* 130 */       this.fixups = new Fixups(this.bytes);
/*     */     }
/* 132 */     assert (this.fixups.getBytes() == this.bytes);
/* 133 */     this.fixups.addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public void trimToSize() {
/* 137 */     if (this.fixups != null) {
/* 138 */       this.fixups.trimToSize();
/* 139 */       if (this.fixups.size() == 0)
/* 140 */         this.fixups = null;
/*     */     }
/* 142 */     super.trimToSize();
/*     */   }
/*     */ 
/*     */   protected void visitRefs(int paramInt, Collection<ConstantPool.Entry> paramCollection) {
/* 146 */     int i = getPackage().verbose;
/* 147 */     if (i > 2)
/* 148 */       System.out.println("Reference scan " + this);
/* 149 */     paramCollection.addAll(Arrays.asList(this.handler_class));
/* 150 */     if (this.fixups != null) {
/* 151 */       this.fixups.visitRefs(paramCollection);
/*     */     }
/*     */     else {
/* 154 */       ConstantPool.Entry[] arrayOfEntry = getCPMap();
/* 155 */       for (Instruction localInstruction = instructionAt(0); localInstruction != null; localInstruction = localInstruction.next()) {
/* 156 */         if (i > 4)
/* 157 */           System.out.println(localInstruction);
/* 158 */         int j = localInstruction.getCPIndex();
/* 159 */         if (j >= 0) {
/* 160 */           paramCollection.add(arrayOfEntry[j]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 165 */     super.visitRefs(paramInt, paramCollection);
/*     */   }
/*     */ 
/*     */   private Object allocateInstructionMap(int[] paramArrayOfInt, int paramInt)
/*     */   {
/* 180 */     int i = getLength();
/*     */     int j;
/* 181 */     if (i <= 255) {
/* 182 */       localObject = new byte[paramInt + 1];
/* 183 */       for (j = 0; j < paramInt; j++) {
/* 184 */         localObject[j] = ((byte)(paramArrayOfInt[j] + -128));
/*     */       }
/* 186 */       localObject[paramInt] = ((byte)(i + -128));
/* 187 */       return localObject;
/* 188 */     }if (i < 65535) {
/* 189 */       localObject = new short[paramInt + 1];
/* 190 */       for (j = 0; j < paramInt; j++) {
/* 191 */         localObject[j] = ((short)(paramArrayOfInt[j] + -32768));
/*     */       }
/* 193 */       localObject[paramInt] = ((short)(i + -32768));
/* 194 */       return localObject;
/*     */     }
/* 196 */     Object localObject = Arrays.copyOf(paramArrayOfInt, paramInt + 1);
/* 197 */     localObject[paramInt] = i;
/* 198 */     return localObject;
/*     */   }
/*     */ 
/*     */   private int[] expandInstructionMap(Object paramObject)
/*     */   {
/*     */     Object localObject;
/*     */     int[] arrayOfInt;
/*     */     int i;
/* 203 */     if ((paramObject instanceof byte[])) {
/* 204 */       localObject = (byte[])paramObject;
/* 205 */       arrayOfInt = new int[localObject.length - 1];
/* 206 */       for (i = 0; i < arrayOfInt.length; i++)
/* 207 */         localObject[i] -= -128;
/*     */     }
/* 209 */     else if ((paramObject instanceof short[])) {
/* 210 */       localObject = (short[])paramObject;
/* 211 */       arrayOfInt = new int[localObject.length - 1];
/* 212 */       for (i = 0; i < arrayOfInt.length; i++)
/* 213 */         localObject[i] -= -128;
/*     */     }
/*     */     else {
/* 216 */       localObject = (int[])paramObject;
/* 217 */       arrayOfInt = Arrays.copyOfRange((int[])localObject, 0, localObject.length - 1);
/*     */     }
/* 219 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   Object getInsnMap()
/*     */   {
/* 224 */     if (this.insnMap != null) {
/* 225 */       return this.insnMap;
/*     */     }
/* 227 */     int[] arrayOfInt = new int[getLength()];
/* 228 */     int i = 0;
/* 229 */     for (Instruction localInstruction = instructionAt(0); localInstruction != null; localInstruction = localInstruction.next()) {
/* 230 */       arrayOfInt[(i++)] = localInstruction.getPC();
/*     */     }
/*     */ 
/* 233 */     this.insnMap = allocateInstructionMap(arrayOfInt, i);
/*     */ 
/* 235 */     return this.insnMap;
/*     */   }
/*     */ 
/*     */   public int encodeBCI(int paramInt)
/*     */   {
/* 246 */     if ((paramInt <= 0) || (paramInt > getLength())) return paramInt;
/* 247 */     Object localObject1 = getInsnMap();
/*     */     Object localObject2;
/*     */     int j;
/*     */     int i;
/* 249 */     if ((localObject1 instanceof byte[])) {
/* 250 */       localObject2 = (byte[])localObject1;
/* 251 */       j = localObject2.length;
/* 252 */       i = Arrays.binarySearch((byte[])localObject2, (byte)(paramInt + -128));
/* 253 */     } else if ((localObject1 instanceof short[])) {
/* 254 */       localObject2 = (short[])localObject1;
/* 255 */       j = localObject2.length;
/* 256 */       i = Arrays.binarySearch((short[])localObject2, (short)(paramInt + -32768));
/*     */     } else {
/* 258 */       localObject2 = (int[])localObject1;
/* 259 */       j = localObject2.length;
/* 260 */       i = Arrays.binarySearch((int[])localObject2, paramInt);
/*     */     }
/* 262 */     assert (i != -1);
/* 263 */     assert (i != 0);
/* 264 */     assert (i != j);
/* 265 */     assert (i != -j - 1);
/* 266 */     return i >= 0 ? i : j + paramInt - (-i - 1);
/*     */   }
/*     */   public int decodeBCI(int paramInt) {
/* 269 */     if ((paramInt <= 0) || (paramInt > getLength())) return paramInt;
/* 270 */     Object localObject1 = getInsnMap();
/*     */     Object localObject2;
/*     */     int j;
/*     */     int i;
/*     */     int k;
/* 289 */     if ((localObject1 instanceof byte[])) {
/* 290 */       localObject2 = (byte[])localObject1;
/* 291 */       j = localObject2.length;
/* 292 */       if (paramInt < j)
/* 293 */         return localObject2[paramInt] - -128;
/* 294 */       i = Arrays.binarySearch((byte[])localObject2, (byte)(paramInt + -128));
/* 295 */       if (i < 0) i = -i - 1;
/* 296 */       k = paramInt - j + -128;
/*     */ 
/* 298 */       while (localObject2[(i - 1)] - (i - 1) > k) {
/* 297 */         i--;
/*     */       }
/*     */     }
/* 300 */     else if ((localObject1 instanceof short[])) {
/* 301 */       localObject2 = (short[])localObject1;
/* 302 */       j = localObject2.length;
/* 303 */       if (paramInt < j)
/* 304 */         return localObject2[paramInt] - -32768;
/* 305 */       i = Arrays.binarySearch((short[])localObject2, (short)(paramInt + -32768));
/* 306 */       if (i < 0) i = -i - 1;
/* 307 */       k = paramInt - j + -32768;
/*     */ 
/* 309 */       while (localObject2[(i - 1)] - (i - 1) > k)
/* 308 */         i--;
/*     */     }
/*     */     else
/*     */     {
/* 312 */       localObject2 = (int[])localObject1;
/* 313 */       j = localObject2.length;
/* 314 */       if (paramInt < j)
/* 315 */         return localObject2[paramInt];
/* 316 */       i = Arrays.binarySearch((int[])localObject2, paramInt);
/* 317 */       if (i < 0) i = -i - 1;
/* 318 */       k = paramInt - j;
/*     */ 
/* 320 */       while (localObject2[(i - 1)] - (i - 1) > k) {
/* 319 */         i--;
/*     */       }
/*     */     }
/*     */ 
/* 323 */     return paramInt - j + i;
/*     */   }
/*     */ 
/*     */   public void finishRefs(ConstantPool.Index paramIndex) {
/* 327 */     if (this.fixups != null) {
/* 328 */       this.fixups.finishRefs(paramIndex);
/* 329 */       this.fixups = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   Instruction instructionAt(int paramInt)
/*     */   {
/* 335 */     return Instruction.at(this.bytes, paramInt);
/*     */   }
/*     */ 
/*     */   static boolean flagsRequireCode(int paramInt)
/*     */   {
/* 341 */     return (paramInt & 0x500) == 0;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 345 */     return this.m + ".Code";
/*     */   }
/*     */ 
/*     */   public int getInt(int paramInt) {
/* 349 */     return Instruction.getInt(this.bytes, paramInt); } 
/* 350 */   public int getShort(int paramInt) { return Instruction.getShort(this.bytes, paramInt); } 
/* 351 */   public int getByte(int paramInt) { return Instruction.getByte(this.bytes, paramInt); } 
/* 352 */   void setInt(int paramInt1, int paramInt2) { Instruction.setInt(this.bytes, paramInt1, paramInt2); } 
/* 353 */   void setShort(int paramInt1, int paramInt2) { Instruction.setShort(this.bytes, paramInt1, paramInt2); } 
/* 354 */   void setByte(int paramInt1, int paramInt2) { Instruction.setByte(this.bytes, paramInt1, paramInt2); }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.Code
 * JD-Core Version:    0.6.2
 */