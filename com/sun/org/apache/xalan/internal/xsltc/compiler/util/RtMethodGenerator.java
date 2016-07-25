/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*    */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.bcel.internal.generic.Type;
/*    */ 
/*    */ public final class RtMethodGenerator extends MethodGenerator
/*    */ {
/*    */   private static final int HANDLER_INDEX = 2;
/*    */   private final Instruction _astoreHandler;
/*    */   private final Instruction _aloadHandler;
/*    */ 
/*    */   public RtMethodGenerator(int access_flags, Type return_type, Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp)
/*    */   {
/* 48 */     super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
/*    */ 
/* 51 */     this._astoreHandler = new ASTORE(2);
/* 52 */     this._aloadHandler = new ALOAD(2);
/*    */   }
/*    */ 
/*    */   public int getIteratorIndex() {
/* 56 */     return -1;
/*    */   }
/*    */ 
/*    */   public final Instruction storeHandler() {
/* 60 */     return this._astoreHandler;
/*    */   }
/*    */ 
/*    */   public final Instruction loadHandler() {
/* 64 */     return this._aloadHandler;
/*    */   }
/*    */ 
/*    */   public int getLocalIndex(String name) {
/* 68 */     return -1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.RtMethodGenerator
 * JD-Core Version:    0.6.2
 */