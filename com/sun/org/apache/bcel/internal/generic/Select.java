/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class Select extends BranchInstruction
/*     */   implements VariableLengthInstruction, StackProducer
/*     */ {
/*     */   protected int[] match;
/*     */   protected int[] indices;
/*     */   protected InstructionHandle[] targets;
/*     */   protected int fixed_length;
/*     */   protected int match_length;
/*  79 */   protected int padding = 0;
/*     */ 
/*     */   Select()
/*     */   {
/*     */   }
/*     */ 
/*     */   Select(short opcode, int[] match, InstructionHandle[] targets, InstructionHandle target)
/*     */   {
/*  97 */     super(opcode, target);
/*     */ 
/*  99 */     this.targets = targets;
/* 100 */     for (int i = 0; i < targets.length; i++) {
/* 101 */       BranchInstruction.notifyTargetChanged(targets[i], this);
/*     */     }
/*     */ 
/* 104 */     this.match = match;
/*     */ 
/* 106 */     if ((this.match_length = match.length) != targets.length) {
/* 107 */       throw new ClassGenException("Match and target array have not the same length");
/*     */     }
/* 109 */     this.indices = new int[this.match_length];
/*     */   }
/*     */ 
/*     */   protected int updatePosition(int offset, int max_offset)
/*     */   {
/* 127 */     this.position += offset;
/*     */ 
/* 129 */     short old_length = this.length;
/*     */ 
/* 133 */     this.padding = ((4 - (this.position + 1) % 4) % 4);
/* 134 */     this.length = ((short)(this.fixed_length + this.padding));
/*     */ 
/* 136 */     return this.length - old_length;
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/* 145 */     out.writeByte(this.opcode);
/*     */ 
/* 147 */     for (int i = 0; i < this.padding; i++) {
/* 148 */       out.writeByte(0);
/*     */     }
/* 150 */     this.index = getTargetOffset();
/* 151 */     out.writeInt(this.index);
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 160 */     this.padding = ((4 - bytes.getIndex() % 4) % 4);
/*     */ 
/* 162 */     for (int i = 0; i < this.padding; i++) {
/* 163 */       bytes.readByte();
/*     */     }
/*     */ 
/* 167 */     this.index = bytes.readInt();
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/* 175 */     StringBuilder buf = new StringBuilder(super.toString(verbose));
/*     */ 
/* 177 */     if (verbose) {
/* 178 */       for (int i = 0; i < this.match_length; i++) {
/* 179 */         String s = "null";
/*     */ 
/* 181 */         if (this.targets[i] != null) {
/* 182 */           s = this.targets[i].getInstruction().toString();
/*     */         }
/* 184 */         buf.append("(").append(this.match[i]).append(", ").append(s).append(" = {").append(this.indices[i]).append("})");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 189 */       buf.append(" ...");
/*     */     }
/* 191 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public final void setTarget(int i, InstructionHandle target)
/*     */   {
/* 198 */     notifyTargetChanging(this.targets[i], this);
/* 199 */     this.targets[i] = target;
/* 200 */     notifyTargetChanged(this.targets[i], this);
/*     */   }
/*     */ 
/*     */   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih)
/*     */   {
/* 209 */     boolean targeted = false;
/*     */ 
/* 211 */     if (this.target == old_ih) {
/* 212 */       targeted = true;
/* 213 */       setTarget(new_ih);
/*     */     }
/*     */ 
/* 216 */     for (int i = 0; i < this.targets.length; i++) {
/* 217 */       if (this.targets[i] == old_ih) {
/* 218 */         targeted = true;
/* 219 */         setTarget(i, new_ih);
/*     */       }
/*     */     }
/*     */ 
/* 223 */     if (!targeted)
/* 224 */       throw new ClassGenException("Not targeting " + old_ih);
/*     */   }
/*     */ 
/*     */   public boolean containsTarget(InstructionHandle ih)
/*     */   {
/* 232 */     if (this.target == ih) {
/* 233 */       return true;
/*     */     }
/* 235 */     for (int i = 0; i < this.targets.length; i++) {
/* 236 */       if (this.targets[i] == ih)
/* 237 */         return true;
/*     */     }
/* 239 */     return false;
/*     */   }
/*     */ 
/*     */   void dispose()
/*     */   {
/* 247 */     super.dispose();
/*     */ 
/* 249 */     for (int i = 0; i < this.targets.length; i++)
/* 250 */       this.targets[i].removeTargeter(this);
/*     */   }
/*     */ 
/*     */   public int[] getMatchs()
/*     */   {
/* 256 */     return this.match;
/*     */   }
/*     */ 
/*     */   public int[] getIndices()
/*     */   {
/* 261 */     return this.indices;
/*     */   }
/*     */ 
/*     */   public InstructionHandle[] getTargets()
/*     */   {
/* 266 */     return this.targets;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.Select
 * JD-Core Version:    0.6.2
 */