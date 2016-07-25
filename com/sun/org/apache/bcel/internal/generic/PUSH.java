/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public final class PUSH
/*     */   implements CompoundInstruction, VariableLengthInstruction, InstructionConstants
/*     */ {
/*     */   private Instruction instruction;
/*     */ 
/*     */   public PUSH(ConstantPoolGen cp, int value)
/*     */   {
/*  81 */     if ((value >= -1) && (value <= 5))
/*  82 */       this.instruction = INSTRUCTIONS[(3 + value)];
/*  83 */     else if ((value >= -128) && (value <= 127))
/*  84 */       this.instruction = new BIPUSH((byte)value);
/*  85 */     else if ((value >= -32768) && (value <= 32767))
/*  86 */       this.instruction = new SIPUSH((short)value);
/*     */     else
/*  88 */       this.instruction = new LDC(cp.addInteger(value));
/*     */   }
/*     */ 
/*     */   public PUSH(ConstantPoolGen cp, boolean value)
/*     */   {
/*  96 */     this.instruction = INSTRUCTIONS[(3 + 0)];
/*     */   }
/*     */ 
/*     */   public PUSH(ConstantPoolGen cp, float value)
/*     */   {
/* 104 */     if (value == 0.0D)
/* 105 */       this.instruction = FCONST_0;
/* 106 */     else if (value == 1.0D)
/* 107 */       this.instruction = FCONST_1;
/* 108 */     else if (value == 2.0D)
/* 109 */       this.instruction = FCONST_2;
/*     */     else
/* 111 */       this.instruction = new LDC(cp.addFloat(value));
/*     */   }
/*     */ 
/*     */   public PUSH(ConstantPoolGen cp, long value)
/*     */   {
/* 119 */     if (value == 0L)
/* 120 */       this.instruction = LCONST_0;
/* 121 */     else if (value == 1L)
/* 122 */       this.instruction = LCONST_1;
/*     */     else
/* 124 */       this.instruction = new LDC2_W(cp.addLong(value));
/*     */   }
/*     */ 
/*     */   public PUSH(ConstantPoolGen cp, double value)
/*     */   {
/* 132 */     if (value == 0.0D)
/* 133 */       this.instruction = DCONST_0;
/* 134 */     else if (value == 1.0D)
/* 135 */       this.instruction = DCONST_1;
/*     */     else
/* 137 */       this.instruction = new LDC2_W(cp.addDouble(value));
/*     */   }
/*     */ 
/*     */   public PUSH(ConstantPoolGen cp, String value)
/*     */   {
/* 145 */     if (value == null)
/* 146 */       this.instruction = ACONST_NULL;
/*     */     else
/* 148 */       this.instruction = new LDC(cp.addString(value));
/*     */   }
/*     */ 
/*     */   public PUSH(ConstantPoolGen cp, Number value)
/*     */   {
/* 156 */     if (((value instanceof Integer)) || ((value instanceof Short)) || ((value instanceof Byte)))
/* 157 */       this.instruction = new PUSH(cp, value.intValue()).instruction;
/* 158 */     else if ((value instanceof Double))
/* 159 */       this.instruction = new PUSH(cp, value.doubleValue()).instruction;
/* 160 */     else if ((value instanceof Float))
/* 161 */       this.instruction = new PUSH(cp, value.floatValue()).instruction;
/* 162 */     else if ((value instanceof Long))
/* 163 */       this.instruction = new PUSH(cp, value.longValue()).instruction;
/*     */     else
/* 165 */       throw new ClassGenException("What's this: " + value);
/*     */   }
/*     */ 
/*     */   public PUSH(ConstantPoolGen cp, Character value)
/*     */   {
/* 173 */     this(cp, value.charValue());
/*     */   }
/*     */ 
/*     */   public PUSH(ConstantPoolGen cp, Boolean value)
/*     */   {
/* 181 */     this(cp, value.booleanValue());
/*     */   }
/*     */ 
/*     */   public final InstructionList getInstructionList() {
/* 185 */     return new InstructionList(this.instruction);
/*     */   }
/*     */ 
/*     */   public final Instruction getInstruction() {
/* 189 */     return this.instruction;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 196 */     return this.instruction.toString() + " (PUSH)";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.PUSH
 * JD-Core Version:    0.6.2
 */