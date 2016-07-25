/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class INVOKEINTERFACE extends InvokeInstruction
/*     */ {
/*     */   private int nargs;
/*     */ 
/*     */   INVOKEINTERFACE()
/*     */   {
/*     */   }
/*     */ 
/*     */   public INVOKEINTERFACE(int index, int nargs)
/*     */   {
/*  83 */     super((short)185, index);
/*  84 */     this.length = 5;
/*     */ 
/*  86 */     if (nargs < 1) {
/*  87 */       throw new ClassGenException("Number of arguments must be > 0 " + nargs);
/*     */     }
/*  89 */     this.nargs = nargs;
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  97 */     out.writeByte(this.opcode);
/*  98 */     out.writeShort(this.index);
/*  99 */     out.writeByte(this.nargs);
/* 100 */     out.writeByte(0);
/*     */   }
/*     */ 
/*     */   public int getCount()
/*     */   {
/* 107 */     return this.nargs;
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 115 */     super.initFromFile(bytes, wide);
/*     */ 
/* 117 */     this.length = 5;
/* 118 */     this.nargs = bytes.readUnsignedByte();
/* 119 */     bytes.readByte();
/*     */   }
/*     */ 
/*     */   public String toString(ConstantPool cp)
/*     */   {
/* 126 */     return super.toString(cp) + " " + this.nargs;
/*     */   }
/*     */ 
/*     */   public int consumeStack(ConstantPoolGen cpg) {
/* 130 */     return this.nargs;
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions() {
/* 134 */     Class[] cs = new Class[4 + ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length];
/*     */ 
/* 136 */     System.arraycopy(ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length);
/*     */ 
/* 139 */     cs[(ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length + 3)] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
/* 140 */     cs[(ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length + 2)] = ExceptionConstants.ILLEGAL_ACCESS_ERROR;
/* 141 */     cs[(ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length + 1)] = ExceptionConstants.ABSTRACT_METHOD_ERROR;
/* 142 */     cs[ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length] = ExceptionConstants.UNSATISFIED_LINK_ERROR;
/*     */ 
/* 144 */     return cs;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 156 */     v.visitExceptionThrower(this);
/* 157 */     v.visitTypedInstruction(this);
/* 158 */     v.visitStackConsumer(this);
/* 159 */     v.visitStackProducer(this);
/* 160 */     v.visitLoadClass(this);
/* 161 */     v.visitCPInstruction(this);
/* 162 */     v.visitFieldOrMethod(this);
/* 163 */     v.visitInvokeInstruction(this);
/* 164 */     v.visitINVOKEINTERFACE(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE
 * JD-Core Version:    0.6.2
 */