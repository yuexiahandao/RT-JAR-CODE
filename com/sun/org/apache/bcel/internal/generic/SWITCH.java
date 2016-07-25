/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public final class SWITCH
/*     */   implements CompoundInstruction
/*     */ {
/*     */   private int[] match;
/*     */   private InstructionHandle[] targets;
/*     */   private Select instruction;
/*     */   private int match_length;
/*     */ 
/*     */   public SWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target, int max_gap)
/*     */   {
/*  91 */     this.match = ((int[])match.clone());
/*  92 */     this.targets = ((InstructionHandle[])targets.clone());
/*     */ 
/*  94 */     if ((this.match_length = match.length) < 2) {
/*  95 */       this.instruction = new TABLESWITCH(match, targets, target);
/*     */     } else {
/*  97 */       sort(0, this.match_length - 1);
/*     */ 
/*  99 */       if (matchIsOrdered(max_gap)) {
/* 100 */         fillup(max_gap, target);
/*     */ 
/* 102 */         this.instruction = new TABLESWITCH(this.match, this.targets, target);
/*     */       }
/*     */       else {
/* 105 */         this.instruction = new LOOKUPSWITCH(this.match, this.targets, target);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public SWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target) {
/* 111 */     this(match, targets, target, 1);
/*     */   }
/*     */ 
/*     */   private final void fillup(int max_gap, InstructionHandle target) {
/* 115 */     int max_size = this.match_length + this.match_length * max_gap;
/* 116 */     int[] m_vec = new int[max_size];
/* 117 */     InstructionHandle[] t_vec = new InstructionHandle[max_size];
/* 118 */     int count = 1;
/*     */ 
/* 120 */     m_vec[0] = this.match[0];
/* 121 */     t_vec[0] = this.targets[0];
/*     */ 
/* 123 */     for (int i = 1; i < this.match_length; i++) {
/* 124 */       int prev = this.match[(i - 1)];
/* 125 */       int gap = this.match[i] - prev;
/*     */ 
/* 127 */       for (int j = 1; j < gap; j++) {
/* 128 */         m_vec[count] = (prev + j);
/* 129 */         t_vec[count] = target;
/* 130 */         count++;
/*     */       }
/*     */ 
/* 133 */       m_vec[count] = this.match[i];
/* 134 */       t_vec[count] = this.targets[i];
/* 135 */       count++;
/*     */     }
/*     */ 
/* 138 */     this.match = new int[count];
/* 139 */     this.targets = new InstructionHandle[count];
/*     */ 
/* 141 */     System.arraycopy(m_vec, 0, this.match, 0, count);
/* 142 */     System.arraycopy(t_vec, 0, this.targets, 0, count);
/*     */   }
/*     */ 
/*     */   private final void sort(int l, int r)
/*     */   {
/* 149 */     int i = l; int j = r;
/* 150 */     int m = this.match[((l + r) / 2)];
/*     */     do
/*     */     {
/* 154 */       while (this.match[i] < m) i++;
/* 155 */       while (m < this.match[j]) j--;
/*     */ 
/* 157 */       if (i <= j) {
/* 158 */         int h = this.match[i]; this.match[i] = this.match[j]; this.match[j] = h;
/* 159 */         InstructionHandle h2 = this.targets[i]; this.targets[i] = this.targets[j]; this.targets[j] = h2;
/* 160 */         i++; j--;
/*     */       }
/*     */     }
/* 162 */     while (i <= j);
/*     */ 
/* 164 */     if (l < j) sort(l, j);
/* 165 */     if (i < r) sort(i, r);
/*     */   }
/*     */ 
/*     */   private final boolean matchIsOrdered(int max_gap)
/*     */   {
/* 172 */     for (int i = 1; i < this.match_length; i++) {
/* 173 */       if (this.match[i] - this.match[(i - 1)] > max_gap)
/* 174 */         return false;
/*     */     }
/* 176 */     return true;
/*     */   }
/*     */ 
/*     */   public final InstructionList getInstructionList() {
/* 180 */     return new InstructionList(this.instruction);
/*     */   }
/*     */ 
/*     */   public final Instruction getInstruction() {
/* 184 */     return this.instruction;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.SWITCH
 * JD-Core Version:    0.6.2
 */