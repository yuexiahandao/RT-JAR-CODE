/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.LocalVariable;
/*     */ import com.sun.org.apache.bcel.internal.util.Objects;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class LocalVariableGen
/*     */   implements InstructionTargeter, NamedAndTyped, Cloneable, Serializable
/*     */ {
/*     */   private final int index;
/*     */   private String name;
/*     */   private Type type;
/*     */   private InstructionHandle start;
/*     */   private InstructionHandle end;
/*     */ 
/*     */   public LocalVariableGen(int index, String name, Type type, InstructionHandle start, InstructionHandle end)
/*     */   {
/*  96 */     if ((index < 0) || (index > 65535)) {
/*  97 */       throw new ClassGenException("Invalid index index: " + index);
/*     */     }
/*  99 */     this.name = name;
/* 100 */     this.type = type;
/* 101 */     this.index = index;
/* 102 */     setStart(start);
/* 103 */     setEnd(end);
/*     */   }
/*     */ 
/*     */   public LocalVariable getLocalVariable(ConstantPoolGen cp)
/*     */   {
/* 122 */     int start_pc = this.start.getPosition();
/* 123 */     int length = this.end.getPosition() - start_pc;
/*     */ 
/* 125 */     if (length > 0) {
/* 126 */       length += this.end.getInstruction().getLength();
/*     */     }
/* 128 */     int name_index = cp.addUtf8(this.name);
/* 129 */     int signature_index = cp.addUtf8(this.type.getSignature());
/*     */ 
/* 131 */     return new LocalVariable(start_pc, length, name_index, signature_index, this.index, cp.getConstantPool());
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/* 135 */     return this.index;
/*     */   }
/* 137 */   public void setName(String name) { this.name = name; } 
/*     */   public String getName() {
/* 139 */     return this.name;
/*     */   }
/* 141 */   public void setType(Type type) { this.type = type; } 
/*     */   public Type getType() {
/* 143 */     return this.type;
/*     */   }
/* 145 */   public InstructionHandle getStart() { return this.start; } 
/* 146 */   public InstructionHandle getEnd() { return this.end; }
/*     */ 
/*     */ 
/*     */   void notifyTargetChanging()
/*     */   {
/* 159 */     BranchInstruction.notifyTargetChanging(this.start, this);
/* 160 */     if (this.end != this.start)
/*     */     {
/* 164 */       BranchInstruction.notifyTargetChanging(this.end, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   void notifyTargetChanged()
/*     */   {
/* 179 */     BranchInstruction.notifyTargetChanged(this.start, this);
/* 180 */     if (this.end != this.start)
/*     */     {
/* 184 */       BranchInstruction.notifyTargetChanged(this.end, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void setStart(InstructionHandle start)
/*     */   {
/* 193 */     notifyTargetChanging();
/*     */ 
/* 195 */     this.start = start;
/*     */ 
/* 200 */     notifyTargetChanged();
/*     */   }
/*     */ 
/*     */   public final void setEnd(InstructionHandle end)
/*     */   {
/* 208 */     notifyTargetChanging();
/*     */ 
/* 210 */     this.end = end;
/*     */ 
/* 216 */     notifyTargetChanged();
/*     */   }
/*     */ 
/*     */   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih)
/*     */   {
/* 226 */     boolean targeted = false;
/*     */ 
/* 228 */     if (this.start == old_ih) {
/* 229 */       targeted = true;
/* 230 */       setStart(new_ih);
/*     */     }
/*     */ 
/* 233 */     if (this.end == old_ih) {
/* 234 */       targeted = true;
/* 235 */       setEnd(new_ih);
/*     */     }
/*     */ 
/* 238 */     if (!targeted)
/* 239 */       throw new ClassGenException("Not targeting " + old_ih + ", but {" + this.start + ", " + this.end + "}");
/*     */   }
/*     */ 
/*     */   public boolean containsTarget(InstructionHandle ih)
/*     */   {
/* 248 */     return (this.start == ih) || (this.end == ih);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 257 */     if (o == this) {
/* 258 */       return true;
/*     */     }
/* 260 */     if (!(o instanceof LocalVariableGen)) {
/* 261 */       return false;
/*     */     }
/* 263 */     LocalVariableGen l = (LocalVariableGen)o;
/* 264 */     return (l.index == this.index) && (l.start == this.start) && (l.end == this.end);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 269 */     int hash = 7;
/* 270 */     hash = 59 * hash + this.index;
/* 271 */     hash = 59 * hash + Objects.hashCode(this.start);
/* 272 */     hash = 59 * hash + Objects.hashCode(this.end);
/* 273 */     return hash;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 278 */     return "LocalVariableGen(" + this.name + ", " + this.type + ", " + this.start + ", " + this.end + ")";
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try {
/* 284 */       return super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 286 */       System.err.println(e);
/* 287 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LocalVariableGen
 * JD-Core Version:    0.6.2
 */