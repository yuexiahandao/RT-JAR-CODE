/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class BranchInstruction extends Instruction
/*     */   implements InstructionTargeter
/*     */ {
/*     */   protected int index;
/*     */   protected InstructionHandle target;
/*     */   protected int position;
/*     */ 
/*     */   BranchInstruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected BranchInstruction(short opcode, InstructionHandle target)
/*     */   {
/*  88 */     super(opcode, (short)3);
/*  89 */     setTarget(target);
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  98 */     out.writeByte(this.opcode);
/*     */ 
/* 100 */     this.index = getTargetOffset();
/*     */ 
/* 102 */     if (Math.abs(this.index) >= 32767) {
/* 103 */       throw new ClassGenException("Branch target offset too large for short");
/*     */     }
/* 105 */     out.writeShort(this.index);
/*     */   }
/*     */ 
/*     */   protected int getTargetOffset(InstructionHandle target)
/*     */   {
/* 113 */     if (target == null) {
/* 114 */       throw new ClassGenException("Target of " + super.toString(true) + " is invalid null handle");
/*     */     }
/*     */ 
/* 117 */     int t = target.getPosition();
/*     */ 
/* 119 */     if (t < 0) {
/* 120 */       throw new ClassGenException("Invalid branch target position offset for " + super.toString(true) + ":" + t + ":" + target);
/*     */     }
/*     */ 
/* 123 */     return t - this.position;
/*     */   }
/*     */ 
/*     */   protected int getTargetOffset()
/*     */   {
/* 129 */     return getTargetOffset(this.target);
/*     */   }
/*     */ 
/*     */   protected int updatePosition(int offset, int max_offset)
/*     */   {
/* 142 */     this.position += offset;
/* 143 */     return 0;
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/* 159 */     String s = super.toString(verbose);
/* 160 */     String t = "null";
/*     */ 
/* 162 */     if (verbose) {
/* 163 */       if (this.target != null) {
/* 164 */         if (this.target.getInstruction() == this)
/* 165 */           t = "<points to itself>";
/* 166 */         else if (this.target.getInstruction() == null)
/* 167 */           t = "<null instruction!!!?>";
/*     */         else
/* 169 */           t = this.target.getInstruction().toString(false);
/*     */       }
/*     */     }
/* 172 */     else if (this.target != null) {
/* 173 */       this.index = getTargetOffset();
/* 174 */       t = "" + (this.index + this.position);
/*     */     }
/*     */ 
/* 178 */     return s + " -> " + t;
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 192 */     this.length = 3;
/* 193 */     this.index = bytes.readShort();
/*     */   }
/*     */ 
/*     */   public final int getIndex()
/*     */   {
/* 199 */     return this.index;
/*     */   }
/*     */ 
/*     */   public InstructionHandle getTarget()
/*     */   {
/* 204 */     return this.target;
/*     */   }
/*     */ 
/*     */   public final void setTarget(InstructionHandle target)
/*     */   {
/* 211 */     notifyTargetChanging(this.target, this);
/* 212 */     this.target = target;
/* 213 */     notifyTargetChanged(this.target, this);
/*     */   }
/*     */ 
/*     */   static void notifyTargetChanging(InstructionHandle old_ih, InstructionTargeter t)
/*     */   {
/* 223 */     if (old_ih != null)
/* 224 */       old_ih.removeTargeter(t);
/*     */   }
/*     */ 
/*     */   static void notifyTargetChanged(InstructionHandle new_ih, InstructionTargeter t)
/*     */   {
/* 235 */     if (new_ih != null)
/* 236 */       new_ih.addTargeter(t);
/*     */   }
/*     */ 
/*     */   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih)
/*     */   {
/* 246 */     if (this.target == old_ih)
/* 247 */       setTarget(new_ih);
/*     */     else
/* 249 */       throw new ClassGenException("Not targeting " + old_ih + ", but " + this.target);
/*     */   }
/*     */ 
/*     */   public boolean containsTarget(InstructionHandle ih)
/*     */   {
/* 257 */     return this.target == ih;
/*     */   }
/*     */ 
/*     */   void dispose()
/*     */   {
/* 265 */     setTarget(null);
/* 266 */     this.index = -1;
/* 267 */     this.position = -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.BranchInstruction
 * JD-Core Version:    0.6.2
 */