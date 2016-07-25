/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class Instruction
/*     */   implements Cloneable, Serializable
/*     */ {
/*  73 */   protected short length = 1;
/*  74 */   protected short opcode = -1;
/*     */ 
/*  76 */   private static InstructionComparator cmp = InstructionComparator.DEFAULT;
/*     */ 
/*     */   Instruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Instruction(short opcode, short length)
/*     */   {
/*  85 */     this.length = length;
/*  86 */     this.opcode = opcode;
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  94 */     out.writeByte(this.opcode);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 100 */     return com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[this.opcode];
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/* 113 */     if (verbose) {
/* 114 */       return getName() + "[" + this.opcode + "](" + this.length + ")";
/*     */     }
/* 116 */     return getName();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 123 */     return toString(true);
/*     */   }
/*     */ 
/*     */   public String toString(ConstantPool cp)
/*     */   {
/* 130 */     return toString(false);
/*     */   }
/*     */ 
/*     */   public Instruction copy()
/*     */   {
/* 142 */     Instruction i = null;
/*     */ 
/* 145 */     if (InstructionConstants.INSTRUCTIONS[getOpcode()] != null)
/* 146 */       i = this;
/*     */     else {
/*     */       try {
/* 149 */         i = (Instruction)clone();
/*     */       } catch (CloneNotSupportedException e) {
/* 151 */         System.err.println(e);
/*     */       }
/*     */     }
/*     */ 
/* 155 */     return i;
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public static final Instruction readInstruction(ByteSequence bytes)
/*     */     throws IOException
/*     */   {
/* 178 */     boolean wide = false;
/* 179 */     short opcode = (short)bytes.readUnsignedByte();
/* 180 */     Instruction obj = null;
/*     */ 
/* 182 */     if (opcode == 196) {
/* 183 */       wide = true;
/* 184 */       opcode = (short)bytes.readUnsignedByte();
/*     */     }
/*     */ 
/* 187 */     if (InstructionConstants.INSTRUCTIONS[opcode] != null) {
/* 188 */       return InstructionConstants.INSTRUCTIONS[opcode];
/*     */     }
/*     */ 
/*     */     Class clazz;
/*     */     try
/*     */     {
/* 196 */       clazz = Class.forName(className(opcode));
/*     */     }
/*     */     catch (ClassNotFoundException cnfe)
/*     */     {
/* 200 */       throw new ClassGenException("Illegal opcode detected.");
/*     */     }
/*     */     try
/*     */     {
/* 204 */       obj = (Instruction)clazz.newInstance();
/*     */ 
/* 206 */       if ((wide) && (!(obj instanceof LocalVariableInstruction)) && (!(obj instanceof IINC)) && (!(obj instanceof RET)))
/*     */       {
/* 209 */         throw new Exception("Illegal opcode after wide: " + opcode);
/*     */       }
/* 211 */       obj.setOpcode(opcode);
/* 212 */       obj.initFromFile(bytes, wide);
/*     */     } catch (Exception e) {
/* 214 */       throw new ClassGenException(e.toString());
/*     */     }
/* 216 */     return obj;
/*     */   }
/*     */ 
/*     */   private static final String className(short opcode) {
/* 220 */     String name = com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[opcode].toUpperCase();
/*     */     try
/*     */     {
/* 226 */       int len = name.length();
/* 227 */       char ch1 = name.charAt(len - 2); char ch2 = name.charAt(len - 1);
/*     */ 
/* 229 */       if ((ch1 == '_') && (ch2 >= '0') && (ch2 <= '5')) {
/* 230 */         name = name.substring(0, len - 2);
/*     */       }
/* 232 */       if (name.equals("ICONST_M1"))
/* 233 */         name = "ICONST"; 
/*     */     } catch (StringIndexOutOfBoundsException e) { System.err.println(e); }
/*     */ 
/* 236 */     return "com.sun.org.apache.bcel.internal.generic." + name;
/*     */   }
/*     */ 
/*     */   public int consumeStack(ConstantPoolGen cpg)
/*     */   {
/* 247 */     return com.sun.org.apache.bcel.internal.Constants.CONSUME_STACK[this.opcode];
/*     */   }
/*     */ 
/*     */   public int produceStack(ConstantPoolGen cpg)
/*     */   {
/* 258 */     return com.sun.org.apache.bcel.internal.Constants.PRODUCE_STACK[this.opcode];
/*     */   }
/*     */ 
/*     */   public short getOpcode()
/*     */   {
/* 264 */     return this.opcode;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 269 */     return this.length;
/*     */   }
/*     */ 
/*     */   private void setOpcode(short opcode)
/*     */   {
/* 274 */     this.opcode = opcode;
/*     */   }
/*     */ 
/*     */   void dispose()
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract void accept(Visitor paramVisitor);
/*     */ 
/*     */   public static InstructionComparator getComparator()
/*     */   {
/* 295 */     return cmp;
/*     */   }
/*     */ 
/*     */   public static void setComparator(InstructionComparator c) {
/* 299 */     cmp = c;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 305 */     return (that instanceof Instruction) ? cmp.equals(this, (Instruction)that) : false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.Instruction
 * JD-Core Version:    0.6.2
 */