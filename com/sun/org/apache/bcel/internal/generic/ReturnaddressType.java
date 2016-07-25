/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.Objects;
/*     */ 
/*     */ public class ReturnaddressType extends Type
/*     */ {
/*  71 */   public static final ReturnaddressType NO_TARGET = new ReturnaddressType();
/*     */   private InstructionHandle returnTarget;
/*     */ 
/*     */   private ReturnaddressType()
/*     */   {
/*  78 */     super((byte)16, "<return address>");
/*     */   }
/*     */ 
/*     */   public ReturnaddressType(InstructionHandle returnTarget)
/*     */   {
/*  85 */     super((byte)16, "<return address targeting " + returnTarget + ">");
/*  86 */     this.returnTarget = returnTarget;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  91 */     return Objects.hashCode(this.returnTarget);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object rat)
/*     */   {
/*  99 */     if (!(rat instanceof ReturnaddressType)) {
/* 100 */       return false;
/*     */     }
/* 102 */     return ((ReturnaddressType)rat).returnTarget.equals(this.returnTarget);
/*     */   }
/*     */ 
/*     */   public InstructionHandle getTarget()
/*     */   {
/* 109 */     return this.returnTarget;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ReturnaddressType
 * JD-Core Version:    0.6.2
 */