/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class MULTIANEWARRAY extends CPInstruction
/*     */   implements LoadClass, AllocationInstruction, ExceptionThrower
/*     */ {
/*     */   private short dimensions;
/*     */ 
/*     */   MULTIANEWARRAY()
/*     */   {
/*     */   }
/*     */ 
/*     */   public MULTIANEWARRAY(int index, short dimensions)
/*     */   {
/*  81 */     super((short)197, index);
/*     */ 
/*  83 */     if (dimensions < 1) {
/*  84 */       throw new ClassGenException("Invalid dimensions value: " + dimensions);
/*     */     }
/*  86 */     this.dimensions = dimensions;
/*  87 */     this.length = 4;
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  95 */     out.writeByte(this.opcode);
/*  96 */     out.writeShort(this.index);
/*  97 */     out.writeByte(this.dimensions);
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 106 */     super.initFromFile(bytes, wide);
/* 107 */     this.dimensions = ((short)bytes.readByte());
/* 108 */     this.length = 4;
/*     */   }
/*     */ 
/*     */   public final short getDimensions()
/*     */   {
/* 114 */     return this.dimensions;
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/* 120 */     return super.toString(verbose) + " " + this.index + " " + this.dimensions;
/*     */   }
/*     */ 
/*     */   public String toString(ConstantPool cp)
/*     */   {
/* 127 */     return super.toString(cp) + " " + this.dimensions;
/*     */   }
/*     */ 
/*     */   public int consumeStack(ConstantPoolGen cpg)
/*     */   {
/* 135 */     return this.dimensions;
/*     */   }
/*     */   public Class[] getExceptions() {
/* 138 */     Class[] cs = new Class[2 + ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];
/*     */ 
/* 140 */     System.arraycopy(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
/*     */ 
/* 143 */     cs[(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length + 1)] = ExceptionConstants.NEGATIVE_ARRAY_SIZE_EXCEPTION;
/* 144 */     cs[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = ExceptionConstants.ILLEGAL_ACCESS_ERROR;
/*     */ 
/* 146 */     return cs;
/*     */   }
/*     */ 
/*     */   public ObjectType getLoadClassType(ConstantPoolGen cpg) {
/* 150 */     Type t = getType(cpg);
/*     */ 
/* 152 */     if ((t instanceof ArrayType)) {
/* 153 */       t = ((ArrayType)t).getBasicType();
/*     */     }
/*     */ 
/* 156 */     return (t instanceof ObjectType) ? (ObjectType)t : null;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 168 */     v.visitLoadClass(this);
/* 169 */     v.visitAllocationInstruction(this);
/* 170 */     v.visitExceptionThrower(this);
/* 171 */     v.visitTypedInstruction(this);
/* 172 */     v.visitCPInstruction(this);
/* 173 */     v.visitMULTIANEWARRAY(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.MULTIANEWARRAY
 * JD-Core Version:    0.6.2
 */