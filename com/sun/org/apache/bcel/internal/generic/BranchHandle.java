/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public final class BranchHandle extends InstructionHandle
/*     */ {
/*     */   private BranchInstruction bi;
/*  82 */   private static BranchHandle bh_list = null;
/*     */ 
/*     */   private BranchHandle(BranchInstruction i)
/*     */   {
/*  76 */     super(i);
/*  77 */     this.bi = i;
/*     */   }
/*     */ 
/*     */   static final BranchHandle getBranchHandle(BranchInstruction i)
/*     */   {
/*  85 */     if (bh_list == null) {
/*  86 */       return new BranchHandle(i);
/*     */     }
/*  88 */     BranchHandle bh = bh_list;
/*  89 */     bh_list = (BranchHandle)bh.next;
/*     */ 
/*  91 */     bh.setInstruction(i);
/*     */ 
/*  93 */     return bh;
/*     */   }
/*     */ 
/*     */   protected void addHandle()
/*     */   {
/* 100 */     this.next = bh_list;
/* 101 */     bh_list = this;
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 108 */     return this.bi.position;
/*     */   }
/*     */   void setPosition(int pos) {
/* 111 */     this.i_position = (this.bi.position = pos);
/*     */   }
/*     */ 
/*     */   protected int updatePosition(int offset, int max_offset) {
/* 115 */     int x = this.bi.updatePosition(offset, max_offset);
/* 116 */     this.i_position = this.bi.position;
/* 117 */     return x;
/*     */   }
/*     */ 
/*     */   public void setTarget(InstructionHandle ih)
/*     */   {
/* 124 */     this.bi.setTarget(ih);
/*     */   }
/*     */ 
/*     */   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih)
/*     */   {
/* 131 */     this.bi.updateTarget(old_ih, new_ih);
/*     */   }
/*     */ 
/*     */   public InstructionHandle getTarget()
/*     */   {
/* 138 */     return this.bi.getTarget();
/*     */   }
/*     */ 
/*     */   public void setInstruction(Instruction i)
/*     */   {
/* 145 */     super.setInstruction(i);
/*     */ 
/* 147 */     if (!(i instanceof BranchInstruction)) {
/* 148 */       throw new ClassGenException("Assigning " + i + " to branch handle which is not a branch instruction");
/*     */     }
/*     */ 
/* 151 */     this.bi = ((BranchInstruction)i);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.BranchHandle
 * JD-Core Version:    0.6.2
 */