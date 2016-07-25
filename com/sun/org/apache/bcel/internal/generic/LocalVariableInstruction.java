/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class LocalVariableInstruction extends Instruction
/*     */   implements TypedInstruction, IndexedInstruction
/*     */ {
/*  72 */   protected int n = -1;
/*  73 */   private short c_tag = -1;
/*  74 */   private short canon_tag = -1;
/*     */ 
/*  76 */   private final boolean wide() { return this.n > 255; }
/*     */ 
/*     */ 
/*     */   LocalVariableInstruction(short canon_tag, short c_tag)
/*     */   {
/*  85 */     this.canon_tag = canon_tag;
/*  86 */     this.c_tag = c_tag;
/*     */   }
/*     */ 
/*     */   LocalVariableInstruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected LocalVariableInstruction(short opcode, short c_tag, int n)
/*     */   {
/* 102 */     super(opcode, (short)2);
/*     */ 
/* 104 */     this.c_tag = c_tag;
/* 105 */     this.canon_tag = opcode;
/*     */ 
/* 107 */     setIndex(n);
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/* 115 */     if (wide()) {
/* 116 */       out.writeByte(196);
/*     */     }
/* 118 */     out.writeByte(this.opcode);
/*     */ 
/* 120 */     if (this.length > 1)
/* 121 */       if (wide())
/* 122 */         out.writeShort(this.n);
/*     */       else
/* 124 */         out.writeByte(this.n);
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/* 138 */     if (((this.opcode >= 26) && (this.opcode <= 45)) || ((this.opcode >= 59) && (this.opcode <= 78)))
/*     */     {
/* 142 */       return super.toString(verbose);
/*     */     }
/* 144 */     return super.toString(verbose) + " " + this.n;
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 154 */     if (wide) {
/* 155 */       this.n = bytes.readUnsignedShort();
/* 156 */       this.length = 4;
/* 157 */     } else if (((this.opcode >= 21) && (this.opcode <= 25)) || ((this.opcode >= 54) && (this.opcode <= 58)))
/*     */     {
/* 161 */       this.n = bytes.readUnsignedByte();
/* 162 */       this.length = 2;
/* 163 */     } else if (this.opcode <= 45) {
/* 164 */       this.n = ((this.opcode - 26) % 4);
/* 165 */       this.length = 1;
/*     */     } else {
/* 167 */       this.n = ((this.opcode - 59) % 4);
/* 168 */       this.length = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getIndex()
/*     */   {
/* 175 */     return this.n;
/*     */   }
/*     */ 
/*     */   public void setIndex(int n)
/*     */   {
/* 181 */     if ((n < 0) || (n > 65535)) {
/* 182 */       throw new ClassGenException("Illegal value: " + n);
/*     */     }
/* 184 */     this.n = n;
/*     */ 
/* 186 */     if ((n >= 0) && (n <= 3)) {
/* 187 */       this.opcode = ((short)(this.c_tag + n));
/* 188 */       this.length = 1;
/*     */     } else {
/* 190 */       this.opcode = this.canon_tag;
/*     */ 
/* 192 */       if (wide())
/* 193 */         this.length = 4;
/*     */       else
/* 195 */         this.length = 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public short getCanonicalTag()
/*     */   {
/* 202 */     return this.canon_tag;
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/* 214 */     switch (this.canon_tag) { case 21:
/*     */     case 54:
/* 216 */       return Type.INT;
/*     */     case 22:
/*     */     case 55:
/* 218 */       return Type.LONG;
/*     */     case 24:
/*     */     case 57:
/* 220 */       return Type.DOUBLE;
/*     */     case 23:
/*     */     case 56:
/* 222 */       return Type.FLOAT;
/*     */     case 25:
/*     */     case 58:
/* 224 */       return Type.OBJECT;
/*     */     case 26:
/*     */     case 27:
/*     */     case 28:
/*     */     case 29:
/*     */     case 30:
/*     */     case 31:
/*     */     case 32:
/*     */     case 33:
/*     */     case 34:
/*     */     case 35:
/*     */     case 36:
/*     */     case 37:
/*     */     case 38:
/*     */     case 39:
/*     */     case 40:
/*     */     case 41:
/*     */     case 42:
/*     */     case 43:
/*     */     case 44:
/*     */     case 45:
/*     */     case 46:
/*     */     case 47:
/*     */     case 48:
/*     */     case 49:
/*     */     case 50:
/*     */     case 51:
/*     */     case 52:
/* 226 */     case 53: } throw new ClassGenException("Oops: unknown case in switch" + this.canon_tag);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction
 * JD-Core Version:    0.6.2
 */