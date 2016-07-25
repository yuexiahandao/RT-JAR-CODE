/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.CodeException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public final class CodeExceptionGen
/*     */   implements InstructionTargeter, Cloneable, Serializable
/*     */ {
/*     */   private InstructionHandle start_pc;
/*     */   private InstructionHandle end_pc;
/*     */   private InstructionHandle handler_pc;
/*     */   private ObjectType catch_type;
/*     */ 
/*     */   public CodeExceptionGen(InstructionHandle start_pc, InstructionHandle end_pc, InstructionHandle handler_pc, ObjectType catch_type)
/*     */   {
/*  95 */     setStartPC(start_pc);
/*  96 */     setEndPC(end_pc);
/*  97 */     setHandlerPC(handler_pc);
/*  98 */     this.catch_type = catch_type;
/*     */   }
/*     */ 
/*     */   public CodeException getCodeException(ConstantPoolGen cp)
/*     */   {
/* 111 */     return new CodeException(this.start_pc.getPosition(), this.end_pc.getPosition() + this.end_pc.getInstruction().getLength(), this.handler_pc.getPosition(), this.catch_type == null ? 0 : cp.addClass(this.catch_type));
/*     */   }
/*     */ 
/*     */   public final void setStartPC(InstructionHandle start_pc)
/*     */   {
/* 121 */     BranchInstruction.notifyTargetChanging(this.start_pc, this);
/* 122 */     this.start_pc = start_pc;
/* 123 */     BranchInstruction.notifyTargetChanged(this.start_pc, this);
/*     */   }
/*     */ 
/*     */   public final void setEndPC(InstructionHandle end_pc)
/*     */   {
/* 130 */     BranchInstruction.notifyTargetChanging(this.end_pc, this);
/* 131 */     this.end_pc = end_pc;
/* 132 */     BranchInstruction.notifyTargetChanged(this.end_pc, this);
/*     */   }
/*     */ 
/*     */   public final void setHandlerPC(InstructionHandle handler_pc)
/*     */   {
/* 139 */     BranchInstruction.notifyTargetChanging(this.handler_pc, this);
/* 140 */     this.handler_pc = handler_pc;
/* 141 */     BranchInstruction.notifyTargetChanged(this.handler_pc, this);
/*     */   }
/*     */ 
/*     */   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih)
/*     */   {
/* 150 */     boolean targeted = false;
/*     */ 
/* 152 */     if (this.start_pc == old_ih) {
/* 153 */       targeted = true;
/* 154 */       setStartPC(new_ih);
/*     */     }
/*     */ 
/* 157 */     if (this.end_pc == old_ih) {
/* 158 */       targeted = true;
/* 159 */       setEndPC(new_ih);
/*     */     }
/*     */ 
/* 162 */     if (this.handler_pc == old_ih) {
/* 163 */       targeted = true;
/* 164 */       setHandlerPC(new_ih);
/*     */     }
/*     */ 
/* 167 */     if (!targeted)
/* 168 */       throw new ClassGenException("Not targeting " + old_ih + ", but {" + this.start_pc + ", " + this.end_pc + ", " + this.handler_pc + "}");
/*     */   }
/*     */ 
/*     */   public boolean containsTarget(InstructionHandle ih)
/*     */   {
/* 177 */     return (this.start_pc == ih) || (this.end_pc == ih) || (this.handler_pc == ih);
/*     */   }
/*     */ 
/*     */   public void setCatchType(ObjectType catch_type) {
/* 181 */     this.catch_type = catch_type;
/*     */   }
/* 183 */   public ObjectType getCatchType() { return this.catch_type; }
/*     */ 
/*     */   public InstructionHandle getStartPC()
/*     */   {
/* 187 */     return this.start_pc;
/*     */   }
/*     */ 
/*     */   public InstructionHandle getEndPC() {
/* 191 */     return this.end_pc;
/*     */   }
/*     */ 
/*     */   public InstructionHandle getHandlerPC() {
/* 195 */     return this.handler_pc;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 199 */     return "CodeExceptionGen(" + this.start_pc + ", " + this.end_pc + ", " + this.handler_pc + ")";
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try {
/* 205 */       return super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 207 */       System.err.println(e);
/* 208 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.CodeExceptionGen
 * JD-Core Version:    0.6.2
 */