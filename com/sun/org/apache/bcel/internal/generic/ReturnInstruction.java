/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public abstract class ReturnInstruction extends Instruction
/*     */   implements ExceptionThrower, TypedInstruction, StackConsumer
/*     */ {
/*     */   ReturnInstruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected ReturnInstruction(short opcode)
/*     */   {
/*  80 */     super(opcode, (short)1);
/*     */   }
/*     */ 
/*     */   public Type getType() {
/*  84 */     switch (this.opcode) { case 172:
/*  85 */       return Type.INT;
/*     */     case 173:
/*  86 */       return Type.LONG;
/*     */     case 174:
/*  87 */       return Type.FLOAT;
/*     */     case 175:
/*  88 */       return Type.DOUBLE;
/*     */     case 176:
/*  89 */       return Type.OBJECT;
/*     */     case 177:
/*  90 */       return Type.VOID;
/*     */     }
/*     */ 
/*  93 */     throw new ClassGenException("Unknown type " + this.opcode);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions()
/*     */   {
/*  98 */     return new Class[] { ExceptionConstants.ILLEGAL_MONITOR_STATE };
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/* 104 */     return getType();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ReturnInstruction
 * JD-Core Version:    0.6.2
 */