/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public abstract class ConversionInstruction extends Instruction
/*     */   implements TypedInstruction, StackProducer, StackConsumer
/*     */ {
/*     */   ConversionInstruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected ConversionInstruction(short opcode)
/*     */   {
/*  78 */     super(opcode, (short)1);
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/*  84 */     switch (this.opcode) { case 136:
/*     */     case 139:
/*     */     case 142:
/*  86 */       return Type.INT;
/*     */     case 134:
/*     */     case 137:
/*     */     case 144:
/*  88 */       return Type.FLOAT;
/*     */     case 133:
/*     */     case 140:
/*     */     case 143:
/*  90 */       return Type.LONG;
/*     */     case 135:
/*     */     case 138:
/*     */     case 141:
/*  92 */       return Type.DOUBLE;
/*     */     case 145:
/*  94 */       return Type.BYTE;
/*     */     case 146:
/*  96 */       return Type.CHAR;
/*     */     case 147:
/*  98 */       return Type.SHORT;
/*     */     }
/*     */ 
/* 101 */     throw new ClassGenException("Unknown type " + this.opcode);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ConversionInstruction
 * JD-Core Version:    0.6.2
 */