/*     */ package com.sun.xml.internal.ws.org.objectweb.asm;
/*     */ 
/*     */ public class Label
/*     */ {
/*     */   static final int DEBUG = 1;
/*     */   static final int RESOLVED = 2;
/*     */   static final int RESIZED = 4;
/*     */   static final int PUSHED = 8;
/*     */   static final int TARGET = 16;
/*     */   static final int STORE = 32;
/*     */   static final int REACHABLE = 64;
/*     */   static final int JSR = 128;
/*     */   static final int RET = 256;
/*     */   static final int SUBROUTINE = 512;
/*     */   static final int VISITED = 1024;
/*     */   public Object info;
/*     */   int status;
/*     */   int line;
/*     */   int position;
/*     */   private int referenceCount;
/*     */   private int[] srcAndRefPositions;
/*     */   int inputStackTop;
/*     */   int outputStackMax;
/*     */   Frame frame;
/*     */   Label successor;
/*     */   Edge successors;
/*     */   Label next;
/*     */ 
/*     */   public int getOffset()
/*     */   {
/* 291 */     if ((this.status & 0x2) == 0) {
/* 292 */       throw new IllegalStateException("Label offset position has not been resolved yet");
/*     */     }
/* 294 */     return this.position;
/*     */   }
/*     */ 
/*     */   void put(MethodWriter owner, ByteVector out, int source, boolean wideOffset)
/*     */   {
/* 318 */     if ((this.status & 0x2) == 0) {
/* 319 */       if (wideOffset) {
/* 320 */         addReference(-1 - source, out.length);
/* 321 */         out.putInt(-1);
/*     */       } else {
/* 323 */         addReference(source, out.length);
/* 324 */         out.putShort(-1);
/*     */       }
/*     */     }
/* 327 */     else if (wideOffset)
/* 328 */       out.putInt(this.position - source);
/*     */     else
/* 330 */       out.putShort(this.position - source);
/*     */   }
/*     */ 
/*     */   private void addReference(int sourcePosition, int referencePosition)
/*     */   {
/* 351 */     if (this.srcAndRefPositions == null) {
/* 352 */       this.srcAndRefPositions = new int[6];
/*     */     }
/* 354 */     if (this.referenceCount >= this.srcAndRefPositions.length) {
/* 355 */       int[] a = new int[this.srcAndRefPositions.length + 6];
/* 356 */       System.arraycopy(this.srcAndRefPositions, 0, a, 0, this.srcAndRefPositions.length);
/*     */ 
/* 361 */       this.srcAndRefPositions = a;
/*     */     }
/* 363 */     this.srcAndRefPositions[(this.referenceCount++)] = sourcePosition;
/* 364 */     this.srcAndRefPositions[(this.referenceCount++)] = referencePosition;
/*     */   }
/*     */ 
/*     */   boolean resolve(MethodWriter owner, int position, byte[] data)
/*     */   {
/* 391 */     boolean needUpdate = false;
/* 392 */     this.status |= 2;
/* 393 */     this.position = position;
/* 394 */     int i = 0;
/* 395 */     while (i < this.referenceCount) {
/* 396 */       int source = this.srcAndRefPositions[(i++)];
/* 397 */       int reference = this.srcAndRefPositions[(i++)];
/*     */ 
/* 399 */       if (source >= 0) {
/* 400 */         int offset = position - source;
/* 401 */         if ((offset < -32768) || (offset > 32767))
/*     */         {
/* 411 */           int opcode = data[(reference - 1)] & 0xFF;
/* 412 */           if (opcode <= 168)
/*     */           {
/* 414 */             data[(reference - 1)] = ((byte)(opcode + 49));
/*     */           }
/*     */           else {
/* 417 */             data[(reference - 1)] = ((byte)(opcode + 20));
/*     */           }
/* 419 */           needUpdate = true;
/*     */         }
/* 421 */         data[(reference++)] = ((byte)(offset >>> 8));
/* 422 */         data[reference] = ((byte)offset);
/*     */       } else {
/* 424 */         int offset = position + source + 1;
/* 425 */         data[(reference++)] = ((byte)(offset >>> 24));
/* 426 */         data[(reference++)] = ((byte)(offset >>> 16));
/* 427 */         data[(reference++)] = ((byte)(offset >>> 8));
/* 428 */         data[reference] = ((byte)offset);
/*     */       }
/*     */     }
/* 431 */     return needUpdate;
/*     */   }
/*     */ 
/*     */   Label getFirst()
/*     */   {
/* 443 */     return this.frame == null ? this : this.frame.owner;
/*     */   }
/*     */ 
/*     */   boolean inSubroutine(long id)
/*     */   {
/* 457 */     if ((this.status & 0x400) != 0) {
/* 458 */       return (this.srcAndRefPositions[((int)(id >>> 32))] & (int)id) != 0;
/*     */     }
/* 460 */     return false;
/*     */   }
/*     */ 
/*     */   boolean inSameSubroutine(Label block)
/*     */   {
/* 472 */     for (int i = 0; i < this.srcAndRefPositions.length; i++) {
/* 473 */       if ((this.srcAndRefPositions[i] & block.srcAndRefPositions[i]) != 0) {
/* 474 */         return true;
/*     */       }
/*     */     }
/* 477 */     return false;
/*     */   }
/*     */ 
/*     */   void addToSubroutine(long id, int nbSubroutines)
/*     */   {
/* 487 */     if ((this.status & 0x400) == 0) {
/* 488 */       this.status |= 1024;
/* 489 */       this.srcAndRefPositions = new int[(nbSubroutines - 1) / 32 + 1];
/*     */     }
/* 491 */     this.srcAndRefPositions[((int)(id >>> 32))] |= (int)id;
/*     */   }
/*     */ 
/*     */   void visitSubroutine(Label JSR, long id, int nbSubroutines)
/*     */   {
/* 508 */     if (JSR != null) {
/* 509 */       if ((this.status & 0x400) != 0) {
/* 510 */         return;
/*     */       }
/* 512 */       this.status |= 1024;
/*     */ 
/* 514 */       if (((this.status & 0x100) != 0) && 
/* 515 */         (!inSameSubroutine(JSR))) {
/* 516 */         Edge e = new Edge();
/* 517 */         e.info = this.inputStackTop;
/* 518 */         e.successor = JSR.successors.successor;
/* 519 */         e.next = this.successors;
/* 520 */         this.successors = e;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 525 */       if (inSubroutine(id)) {
/* 526 */         return;
/*     */       }
/*     */ 
/* 529 */       addToSubroutine(id, nbSubroutines);
/*     */     }
/*     */ 
/* 532 */     Edge e = this.successors;
/* 533 */     while (e != null)
/*     */     {
/* 537 */       if (((this.status & 0x80) == 0) || (e != this.successors.next)) {
/* 538 */         e.successor.visitSubroutine(JSR, id, nbSubroutines);
/*     */       }
/* 540 */       e = e.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 554 */     return "L" + System.identityHashCode(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.Label
 * JD-Core Version:    0.6.2
 */