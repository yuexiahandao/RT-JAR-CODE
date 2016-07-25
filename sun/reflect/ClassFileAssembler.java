/*     */ package sun.reflect;
/*     */ 
/*     */ class ClassFileAssembler
/*     */   implements ClassFileConstants
/*     */ {
/*     */   private ByteVector vec;
/*  30 */   private short cpIdx = 0;
/*     */ 
/* 164 */   private int stack = 0;
/* 165 */   private int maxStack = 0;
/* 166 */   private int maxLocals = 0;
/*     */ 
/*     */   public ClassFileAssembler()
/*     */   {
/*  33 */     this(ByteVectorFactory.create());
/*     */   }
/*     */ 
/*     */   public ClassFileAssembler(ByteVector paramByteVector) {
/*  37 */     this.vec = paramByteVector;
/*     */   }
/*     */ 
/*     */   public ByteVector getData() {
/*  41 */     return this.vec;
/*     */   }
/*     */ 
/*     */   public short getLength()
/*     */   {
/*  46 */     return (short)this.vec.getLength();
/*     */   }
/*     */ 
/*     */   public void emitMagicAndVersion() {
/*  50 */     emitInt(-889275714);
/*  51 */     emitShort((short)0);
/*  52 */     emitShort((short)49);
/*     */   }
/*     */ 
/*     */   public void emitInt(int paramInt) {
/*  56 */     emitByte((byte)(paramInt >> 24));
/*  57 */     emitByte((byte)(paramInt >> 16 & 0xFF));
/*  58 */     emitByte((byte)(paramInt >> 8 & 0xFF));
/*  59 */     emitByte((byte)(paramInt & 0xFF));
/*     */   }
/*     */ 
/*     */   public void emitShort(short paramShort) {
/*  63 */     emitByte((byte)(paramShort >> 8 & 0xFF));
/*  64 */     emitByte((byte)(paramShort & 0xFF));
/*     */   }
/*     */ 
/*     */   void emitShort(short paramShort1, short paramShort2)
/*     */   {
/*  69 */     this.vec.put(paramShort1, (byte)(paramShort2 >> 8 & 0xFF));
/*  70 */     this.vec.put(paramShort1 + 1, (byte)(paramShort2 & 0xFF));
/*     */   }
/*     */ 
/*     */   public void emitByte(byte paramByte) {
/*  74 */     this.vec.add(paramByte);
/*     */   }
/*     */ 
/*     */   public void append(ClassFileAssembler paramClassFileAssembler) {
/*  78 */     append(paramClassFileAssembler.vec);
/*     */   }
/*     */ 
/*     */   public void append(ByteVector paramByteVector) {
/*  82 */     for (int i = 0; i < paramByteVector.getLength(); i++)
/*  83 */       emitByte(paramByteVector.get(i));
/*     */   }
/*     */ 
/*     */   public short cpi()
/*     */   {
/*  94 */     if (this.cpIdx == 0) {
/*  95 */       throw new RuntimeException("Illegal use of ClassFileAssembler");
/*     */     }
/*  97 */     return this.cpIdx;
/*     */   }
/*     */ 
/*     */   public void emitConstantPoolUTF8(String paramString)
/*     */   {
/* 103 */     byte[] arrayOfByte = UTF8.encode(paramString);
/* 104 */     emitByte((byte)1);
/* 105 */     emitShort((short)arrayOfByte.length);
/* 106 */     for (int i = 0; i < arrayOfByte.length; i++) {
/* 107 */       emitByte(arrayOfByte[i]);
/*     */     }
/* 109 */     this.cpIdx = ((short)(this.cpIdx + 1));
/*     */   }
/*     */ 
/*     */   public void emitConstantPoolClass(short paramShort) {
/* 113 */     emitByte((byte)7);
/* 114 */     emitShort(paramShort);
/* 115 */     this.cpIdx = ((short)(this.cpIdx + 1));
/*     */   }
/*     */ 
/*     */   public void emitConstantPoolNameAndType(short paramShort1, short paramShort2) {
/* 119 */     emitByte((byte)12);
/* 120 */     emitShort(paramShort1);
/* 121 */     emitShort(paramShort2);
/* 122 */     this.cpIdx = ((short)(this.cpIdx + 1));
/*     */   }
/*     */ 
/*     */   public void emitConstantPoolFieldref(short paramShort1, short paramShort2)
/*     */   {
/* 128 */     emitByte((byte)9);
/* 129 */     emitShort(paramShort1);
/* 130 */     emitShort(paramShort2);
/* 131 */     this.cpIdx = ((short)(this.cpIdx + 1));
/*     */   }
/*     */ 
/*     */   public void emitConstantPoolMethodref(short paramShort1, short paramShort2)
/*     */   {
/* 137 */     emitByte((byte)10);
/* 138 */     emitShort(paramShort1);
/* 139 */     emitShort(paramShort2);
/* 140 */     this.cpIdx = ((short)(this.cpIdx + 1));
/*     */   }
/*     */ 
/*     */   public void emitConstantPoolInterfaceMethodref(short paramShort1, short paramShort2)
/*     */   {
/* 146 */     emitByte((byte)11);
/* 147 */     emitShort(paramShort1);
/* 148 */     emitShort(paramShort2);
/* 149 */     this.cpIdx = ((short)(this.cpIdx + 1));
/*     */   }
/*     */ 
/*     */   public void emitConstantPoolString(short paramShort) {
/* 153 */     emitByte((byte)8);
/* 154 */     emitShort(paramShort);
/* 155 */     this.cpIdx = ((short)(this.cpIdx + 1));
/*     */   }
/*     */ 
/*     */   private void incStack()
/*     */   {
/* 169 */     setStack(this.stack + 1);
/*     */   }
/*     */ 
/*     */   private void decStack() {
/* 173 */     this.stack -= 1;
/*     */   }
/*     */ 
/*     */   public short getMaxStack() {
/* 177 */     return (short)this.maxStack;
/*     */   }
/*     */ 
/*     */   public short getMaxLocals() {
/* 181 */     return (short)this.maxLocals;
/*     */   }
/*     */ 
/*     */   public void setMaxLocals(int paramInt)
/*     */   {
/* 188 */     this.maxLocals = paramInt;
/*     */   }
/*     */ 
/*     */   public int getStack()
/*     */   {
/* 193 */     return this.stack;
/*     */   }
/*     */ 
/*     */   public void setStack(int paramInt)
/*     */   {
/* 198 */     this.stack = paramInt;
/* 199 */     if (this.stack > this.maxStack)
/* 200 */       this.maxStack = this.stack;
/*     */   }
/*     */ 
/*     */   public void opc_aconst_null()
/*     */   {
/* 209 */     emitByte((byte)1);
/* 210 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_sipush(short paramShort) {
/* 214 */     emitByte((byte)17);
/* 215 */     emitShort(paramShort);
/* 216 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_ldc(byte paramByte) {
/* 220 */     emitByte((byte)18);
/* 221 */     emitByte(paramByte);
/* 222 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_iload_0()
/*     */   {
/* 230 */     emitByte((byte)26);
/* 231 */     if (this.maxLocals < 1) this.maxLocals = 1;
/* 232 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_iload_1() {
/* 236 */     emitByte((byte)27);
/* 237 */     if (this.maxLocals < 2) this.maxLocals = 2;
/* 238 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_iload_2() {
/* 242 */     emitByte((byte)28);
/* 243 */     if (this.maxLocals < 3) this.maxLocals = 3;
/* 244 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_iload_3() {
/* 248 */     emitByte((byte)29);
/* 249 */     if (this.maxLocals < 4) this.maxLocals = 4;
/* 250 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_lload_0() {
/* 254 */     emitByte((byte)30);
/* 255 */     if (this.maxLocals < 2) this.maxLocals = 2;
/* 256 */     incStack();
/* 257 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_lload_1() {
/* 261 */     emitByte((byte)31);
/* 262 */     if (this.maxLocals < 3) this.maxLocals = 3;
/* 263 */     incStack();
/* 264 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_lload_2() {
/* 268 */     emitByte((byte)32);
/* 269 */     if (this.maxLocals < 4) this.maxLocals = 4;
/* 270 */     incStack();
/* 271 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_lload_3() {
/* 275 */     emitByte((byte)33);
/* 276 */     if (this.maxLocals < 5) this.maxLocals = 5;
/* 277 */     incStack();
/* 278 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_fload_0() {
/* 282 */     emitByte((byte)34);
/* 283 */     if (this.maxLocals < 1) this.maxLocals = 1;
/* 284 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_fload_1() {
/* 288 */     emitByte((byte)35);
/* 289 */     if (this.maxLocals < 2) this.maxLocals = 2;
/* 290 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_fload_2() {
/* 294 */     emitByte((byte)36);
/* 295 */     if (this.maxLocals < 3) this.maxLocals = 3;
/* 296 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_fload_3() {
/* 300 */     emitByte((byte)37);
/* 301 */     if (this.maxLocals < 4) this.maxLocals = 4;
/* 302 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_dload_0() {
/* 306 */     emitByte((byte)38);
/* 307 */     if (this.maxLocals < 2) this.maxLocals = 2;
/* 308 */     incStack();
/* 309 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_dload_1() {
/* 313 */     emitByte((byte)39);
/* 314 */     if (this.maxLocals < 3) this.maxLocals = 3;
/* 315 */     incStack();
/* 316 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_dload_2() {
/* 320 */     emitByte((byte)40);
/* 321 */     if (this.maxLocals < 4) this.maxLocals = 4;
/* 322 */     incStack();
/* 323 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_dload_3() {
/* 327 */     emitByte((byte)41);
/* 328 */     if (this.maxLocals < 5) this.maxLocals = 5;
/* 329 */     incStack();
/* 330 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_aload_0() {
/* 334 */     emitByte((byte)42);
/* 335 */     if (this.maxLocals < 1) this.maxLocals = 1;
/* 336 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_aload_1() {
/* 340 */     emitByte((byte)43);
/* 341 */     if (this.maxLocals < 2) this.maxLocals = 2;
/* 342 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_aload_2() {
/* 346 */     emitByte((byte)44);
/* 347 */     if (this.maxLocals < 3) this.maxLocals = 3;
/* 348 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_aload_3() {
/* 352 */     emitByte((byte)45);
/* 353 */     if (this.maxLocals < 4) this.maxLocals = 4;
/* 354 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_aaload() {
/* 358 */     emitByte((byte)50);
/* 359 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_astore_0() {
/* 363 */     emitByte((byte)75);
/* 364 */     if (this.maxLocals < 1) this.maxLocals = 1;
/* 365 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_astore_1() {
/* 369 */     emitByte((byte)76);
/* 370 */     if (this.maxLocals < 2) this.maxLocals = 2;
/* 371 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_astore_2() {
/* 375 */     emitByte((byte)77);
/* 376 */     if (this.maxLocals < 3) this.maxLocals = 3;
/* 377 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_astore_3() {
/* 381 */     emitByte((byte)78);
/* 382 */     if (this.maxLocals < 4) this.maxLocals = 4;
/* 383 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_pop()
/*     */   {
/* 391 */     emitByte((byte)87);
/* 392 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_dup() {
/* 396 */     emitByte((byte)89);
/* 397 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_dup_x1() {
/* 401 */     emitByte((byte)90);
/* 402 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_swap() {
/* 406 */     emitByte((byte)95);
/*     */   }
/*     */ 
/*     */   public void opc_i2l()
/*     */   {
/* 414 */     emitByte((byte)-123);
/*     */   }
/*     */ 
/*     */   public void opc_i2f() {
/* 418 */     emitByte((byte)-122);
/*     */   }
/*     */ 
/*     */   public void opc_i2d() {
/* 422 */     emitByte((byte)-121);
/*     */   }
/*     */ 
/*     */   public void opc_l2f() {
/* 426 */     emitByte((byte)-119);
/*     */   }
/*     */ 
/*     */   public void opc_l2d() {
/* 430 */     emitByte((byte)-118);
/*     */   }
/*     */ 
/*     */   public void opc_f2d() {
/* 434 */     emitByte((byte)-115);
/*     */   }
/*     */ 
/*     */   public void opc_ifeq(short paramShort)
/*     */   {
/* 442 */     emitByte((byte)-103);
/* 443 */     emitShort(paramShort);
/* 444 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_ifeq(Label paramLabel)
/*     */   {
/* 450 */     short s = getLength();
/* 451 */     emitByte((byte)-103);
/* 452 */     paramLabel.add(this, s, getLength(), getStack() - 1);
/* 453 */     emitShort((short)-1);
/*     */   }
/*     */ 
/*     */   public void opc_if_icmpeq(short paramShort) {
/* 457 */     emitByte((byte)-97);
/* 458 */     emitShort(paramShort);
/* 459 */     setStack(getStack() - 2);
/*     */   }
/*     */ 
/*     */   public void opc_if_icmpeq(Label paramLabel)
/*     */   {
/* 465 */     short s = getLength();
/* 466 */     emitByte((byte)-97);
/* 467 */     paramLabel.add(this, s, getLength(), getStack() - 2);
/* 468 */     emitShort((short)-1);
/*     */   }
/*     */ 
/*     */   public void opc_goto(short paramShort) {
/* 472 */     emitByte((byte)-89);
/* 473 */     emitShort(paramShort);
/*     */   }
/*     */ 
/*     */   public void opc_goto(Label paramLabel)
/*     */   {
/* 479 */     short s = getLength();
/* 480 */     emitByte((byte)-89);
/* 481 */     paramLabel.add(this, s, getLength(), getStack());
/* 482 */     emitShort((short)-1);
/*     */   }
/*     */ 
/*     */   public void opc_ifnull(short paramShort) {
/* 486 */     emitByte((byte)-58);
/* 487 */     emitShort(paramShort);
/* 488 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_ifnull(Label paramLabel)
/*     */   {
/* 494 */     short s = getLength();
/* 495 */     emitByte((byte)-58);
/* 496 */     paramLabel.add(this, s, getLength(), getStack() - 1);
/* 497 */     emitShort((short)-1);
/* 498 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_ifnonnull(short paramShort) {
/* 502 */     emitByte((byte)-57);
/* 503 */     emitShort(paramShort);
/* 504 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_ifnonnull(Label paramLabel)
/*     */   {
/* 510 */     short s = getLength();
/* 511 */     emitByte((byte)-57);
/* 512 */     paramLabel.add(this, s, getLength(), getStack() - 1);
/* 513 */     emitShort((short)-1);
/* 514 */     decStack();
/*     */   }
/*     */ 
/*     */   public void opc_ireturn()
/*     */   {
/* 522 */     emitByte((byte)-84);
/* 523 */     setStack(0);
/*     */   }
/*     */ 
/*     */   public void opc_lreturn() {
/* 527 */     emitByte((byte)-83);
/* 528 */     setStack(0);
/*     */   }
/*     */ 
/*     */   public void opc_freturn() {
/* 532 */     emitByte((byte)-82);
/* 533 */     setStack(0);
/*     */   }
/*     */ 
/*     */   public void opc_dreturn() {
/* 537 */     emitByte((byte)-81);
/* 538 */     setStack(0);
/*     */   }
/*     */ 
/*     */   public void opc_areturn() {
/* 542 */     emitByte((byte)-80);
/* 543 */     setStack(0);
/*     */   }
/*     */ 
/*     */   public void opc_return() {
/* 547 */     emitByte((byte)-79);
/* 548 */     setStack(0);
/*     */   }
/*     */ 
/*     */   public void opc_getstatic(short paramShort, int paramInt)
/*     */   {
/* 556 */     emitByte((byte)-78);
/* 557 */     emitShort(paramShort);
/* 558 */     setStack(getStack() + paramInt);
/*     */   }
/*     */ 
/*     */   public void opc_putstatic(short paramShort, int paramInt) {
/* 562 */     emitByte((byte)-77);
/* 563 */     emitShort(paramShort);
/* 564 */     setStack(getStack() - paramInt);
/*     */   }
/*     */ 
/*     */   public void opc_getfield(short paramShort, int paramInt) {
/* 568 */     emitByte((byte)-76);
/* 569 */     emitShort(paramShort);
/* 570 */     setStack(getStack() + paramInt - 1);
/*     */   }
/*     */ 
/*     */   public void opc_putfield(short paramShort, int paramInt) {
/* 574 */     emitByte((byte)-75);
/* 575 */     emitShort(paramShort);
/* 576 */     setStack(getStack() - paramInt - 1);
/*     */   }
/*     */ 
/*     */   public void opc_invokevirtual(short paramShort, int paramInt1, int paramInt2)
/*     */   {
/* 589 */     emitByte((byte)-74);
/* 590 */     emitShort(paramShort);
/* 591 */     setStack(getStack() - paramInt1 - 1 + paramInt2);
/*     */   }
/*     */ 
/*     */   public void opc_invokespecial(short paramShort, int paramInt1, int paramInt2)
/*     */   {
/* 600 */     emitByte((byte)-73);
/* 601 */     emitShort(paramShort);
/* 602 */     setStack(getStack() - paramInt1 - 1 + paramInt2);
/*     */   }
/*     */ 
/*     */   public void opc_invokestatic(short paramShort, int paramInt1, int paramInt2)
/*     */   {
/* 611 */     emitByte((byte)-72);
/* 612 */     emitShort(paramShort);
/* 613 */     setStack(getStack() - paramInt1 + paramInt2);
/*     */   }
/*     */ 
/*     */   public void opc_invokeinterface(short paramShort, int paramInt1, byte paramByte, int paramInt2)
/*     */   {
/* 623 */     emitByte((byte)-71);
/* 624 */     emitShort(paramShort);
/* 625 */     emitByte(paramByte);
/* 626 */     emitByte((byte)0);
/* 627 */     setStack(getStack() - paramInt1 - 1 + paramInt2);
/*     */   }
/*     */ 
/*     */   public void opc_arraylength()
/*     */   {
/* 635 */     emitByte((byte)-66);
/*     */   }
/*     */ 
/*     */   public void opc_new(short paramShort)
/*     */   {
/* 643 */     emitByte((byte)-69);
/* 644 */     emitShort(paramShort);
/* 645 */     incStack();
/*     */   }
/*     */ 
/*     */   public void opc_athrow()
/*     */   {
/* 653 */     emitByte((byte)-65);
/* 654 */     setStack(1);
/*     */   }
/*     */ 
/*     */   public void opc_checkcast(short paramShort)
/*     */   {
/* 663 */     emitByte((byte)-64);
/* 664 */     emitShort(paramShort);
/*     */   }
/*     */ 
/*     */   public void opc_instanceof(short paramShort) {
/* 668 */     emitByte((byte)-63);
/* 669 */     emitShort(paramShort);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.ClassFileAssembler
 * JD-Core Version:    0.6.2
 */