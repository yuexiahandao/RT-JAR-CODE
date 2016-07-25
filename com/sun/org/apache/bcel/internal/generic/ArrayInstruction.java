/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public abstract class ArrayInstruction extends Instruction
/*     */   implements ExceptionThrower, TypedInstruction
/*     */ {
/*     */   ArrayInstruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected ArrayInstruction(short opcode)
/*     */   {
/*  78 */     super(opcode, (short)1);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions() {
/*  82 */     return ExceptionConstants.EXCS_ARRAY_EXCEPTION;
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/*  88 */     switch (this.opcode) { case 46:
/*     */     case 79:
/*  90 */       return Type.INT;
/*     */     case 52:
/*     */     case 85:
/*  92 */       return Type.CHAR;
/*     */     case 51:
/*     */     case 84:
/*  94 */       return Type.BYTE;
/*     */     case 53:
/*     */     case 86:
/*  96 */       return Type.SHORT;
/*     */     case 47:
/*     */     case 80:
/*  98 */       return Type.LONG;
/*     */     case 49:
/*     */     case 82:
/* 100 */       return Type.DOUBLE;
/*     */     case 48:
/*     */     case 81:
/* 102 */       return Type.FLOAT;
/*     */     case 50:
/*     */     case 83:
/* 104 */       return Type.OBJECT;
/*     */     case 54:
/*     */     case 55:
/*     */     case 56:
/*     */     case 57:
/*     */     case 58:
/*     */     case 59:
/*     */     case 60:
/*     */     case 61:
/*     */     case 62:
/*     */     case 63:
/*     */     case 64:
/*     */     case 65:
/*     */     case 66:
/*     */     case 67:
/*     */     case 68:
/*     */     case 69:
/*     */     case 70:
/*     */     case 71:
/*     */     case 72:
/*     */     case 73:
/*     */     case 74:
/*     */     case 75:
/*     */     case 76:
/*     */     case 77:
/* 106 */     case 78: } throw new ClassGenException("Oops: unknown case in switch" + this.opcode);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ArrayInstruction
 * JD-Core Version:    0.6.2
 */