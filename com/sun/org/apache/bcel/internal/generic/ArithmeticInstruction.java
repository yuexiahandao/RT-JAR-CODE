/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public abstract class ArithmeticInstruction extends Instruction
/*     */   implements TypedInstruction, StackProducer, StackConsumer
/*     */ {
/*     */   ArithmeticInstruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected ArithmeticInstruction(short opcode)
/*     */   {
/*  78 */     super(opcode, (short)1);
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/*  84 */     switch (this.opcode) { case 99:
/*     */     case 103:
/*     */     case 107:
/*     */     case 111:
/*     */     case 115:
/*     */     case 119:
/*  87 */       return Type.DOUBLE;
/*     */     case 98:
/*     */     case 102:
/*     */     case 106:
/*     */     case 110:
/*     */     case 114:
/*     */     case 118:
/*  91 */       return Type.FLOAT;
/*     */     case 96:
/*     */     case 100:
/*     */     case 104:
/*     */     case 108:
/*     */     case 112:
/*     */     case 116:
/*     */     case 120:
/*     */     case 122:
/*     */     case 124:
/*     */     case 126:
/*     */     case 128:
/*     */     case 130:
/*  97 */       return Type.INT;
/*     */     case 97:
/*     */     case 101:
/*     */     case 105:
/*     */     case 109:
/*     */     case 113:
/*     */     case 117:
/*     */     case 121:
/*     */     case 123:
/*     */     case 125:
/*     */     case 127:
/*     */     case 129:
/*     */     case 131:
/* 103 */       return Type.LONG;
/*     */     }
/*     */ 
/* 106 */     throw new ClassGenException("Unknown type " + this.opcode);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ArithmeticInstruction
 * JD-Core Version:    0.6.2
 */