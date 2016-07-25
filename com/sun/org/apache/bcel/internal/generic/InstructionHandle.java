/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ public class InstructionHandle
/*     */   implements Serializable
/*     */ {
/*     */   InstructionHandle next;
/*     */   InstructionHandle prev;
/*     */   Instruction instruction;
/*  86 */   protected int i_position = -1;
/*     */   private HashSet targeters;
/*     */   private HashMap attributes;
/* 126 */   private static InstructionHandle ih_list = null;
/*     */ 
/*     */   public final InstructionHandle getNext()
/*     */   {
/*  90 */     return this.next; } 
/*  91 */   public final InstructionHandle getPrev() { return this.prev; } 
/*  92 */   public final Instruction getInstruction() { return this.instruction; }
/*     */ 
/*     */ 
/*     */   public void setInstruction(Instruction i)
/*     */   {
/*  99 */     if (i == null) {
/* 100 */       throw new ClassGenException("Assigning null to handle");
/*     */     }
/* 102 */     if ((getClass() != BranchHandle.class) && ((i instanceof BranchInstruction))) {
/* 103 */       throw new ClassGenException("Assigning branch instruction " + i + " to plain handle");
/*     */     }
/* 105 */     if (this.instruction != null) {
/* 106 */       this.instruction.dispose();
/*     */     }
/* 108 */     this.instruction = i;
/*     */   }
/*     */ 
/*     */   public Instruction swapInstruction(Instruction i)
/*     */   {
/* 117 */     Instruction oldInstruction = this.instruction;
/* 118 */     this.instruction = i;
/* 119 */     return oldInstruction;
/*     */   }
/*     */ 
/*     */   protected InstructionHandle(Instruction i) {
/* 123 */     setInstruction(i);
/*     */   }
/*     */ 
/*     */   static final InstructionHandle getInstructionHandle(Instruction i)
/*     */   {
/* 131 */     if (ih_list == null) {
/* 132 */       return new InstructionHandle(i);
/*     */     }
/* 134 */     InstructionHandle ih = ih_list;
/* 135 */     ih_list = ih.next;
/*     */ 
/* 137 */     ih.setInstruction(i);
/*     */ 
/* 139 */     return ih;
/*     */   }
/*     */ 
/*     */   protected int updatePosition(int offset, int max_offset)
/*     */   {
/* 154 */     this.i_position += offset;
/* 155 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 162 */     return this.i_position;
/*     */   }
/*     */ 
/*     */   void setPosition(int pos)
/*     */   {
/* 167 */     this.i_position = pos;
/*     */   }
/*     */ 
/*     */   protected void addHandle()
/*     */   {
/* 172 */     this.next = ih_list;
/* 173 */     ih_list = this;
/*     */   }
/*     */ 
/*     */   void dispose()
/*     */   {
/* 180 */     this.next = (this.prev = null);
/* 181 */     this.instruction.dispose();
/* 182 */     this.instruction = null;
/* 183 */     this.i_position = -1;
/* 184 */     this.attributes = null;
/* 185 */     removeAllTargeters();
/* 186 */     addHandle();
/*     */   }
/*     */ 
/*     */   public void removeAllTargeters()
/*     */   {
/* 192 */     if (this.targeters != null)
/* 193 */       this.targeters.clear();
/*     */   }
/*     */ 
/*     */   public void removeTargeter(InstructionTargeter t)
/*     */   {
/* 200 */     this.targeters.remove(t);
/*     */   }
/*     */ 
/*     */   public void addTargeter(InstructionTargeter t)
/*     */   {
/* 207 */     if (this.targeters == null) {
/* 208 */       this.targeters = new HashSet();
/*     */     }
/*     */ 
/* 211 */     this.targeters.add(t);
/*     */   }
/*     */ 
/*     */   public boolean hasTargeters() {
/* 215 */     return (this.targeters != null) && (this.targeters.size() > 0);
/*     */   }
/*     */ 
/*     */   public InstructionTargeter[] getTargeters()
/*     */   {
/* 222 */     if (!hasTargeters()) {
/* 223 */       return null;
/*     */     }
/* 225 */     InstructionTargeter[] t = new InstructionTargeter[this.targeters.size()];
/* 226 */     this.targeters.toArray(t);
/* 227 */     return t;
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/* 233 */     return Utility.format(this.i_position, 4, false, ' ') + ": " + this.instruction.toString(verbose);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 239 */     return toString(true);
/*     */   }
/*     */ 
/*     */   public void addAttribute(Object key, Object attr)
/*     */   {
/* 248 */     if (this.attributes == null) {
/* 249 */       this.attributes = new HashMap(3);
/*     */     }
/* 251 */     this.attributes.put(key, attr);
/*     */   }
/*     */ 
/*     */   public void removeAttribute(Object key)
/*     */   {
/* 259 */     if (this.attributes != null)
/* 260 */       this.attributes.remove(key);
/*     */   }
/*     */ 
/*     */   public Object getAttribute(Object key)
/*     */   {
/* 268 */     if (this.attributes != null) {
/* 269 */       return this.attributes.get(key);
/*     */     }
/* 271 */     return null;
/*     */   }
/*     */ 
/*     */   public Collection getAttributes()
/*     */   {
/* 277 */     return this.attributes.values();
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 285 */     this.instruction.accept(v);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.InstructionHandle
 * JD-Core Version:    0.6.2
 */