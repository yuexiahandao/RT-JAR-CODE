/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*    */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*    */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.bcel.internal.generic.Type;
/*    */ 
/*    */ public final class AttributeSetMethodGenerator extends MethodGenerator
/*    */ {
/*    */   protected static final int CURRENT_INDEX = 4;
/*    */   private static final int PARAM_START_INDEX = 5;
/* 41 */   private static final String[] argNames = new String[4];
/* 42 */   private static final Type[] argTypes = new Type[4];
/*    */ 
/*    */   public AttributeSetMethodGenerator(String methodName, ClassGenerator classGen)
/*    */   {
/* 57 */     super(2, Type.VOID, argTypes, argNames, methodName, classGen.getClassName(), new InstructionList(), classGen.getConstantPool());
/*    */   }
/*    */ 
/*    */   public int getLocalIndex(String name)
/*    */   {
/* 66 */     if (name.equals("current")) {
/* 67 */       return 4;
/*    */     }
/* 69 */     return super.getLocalIndex(name);
/*    */   }
/*    */ 
/*    */   public Instruction loadParameter(int index) {
/* 73 */     return new ALOAD(index + 5);
/*    */   }
/*    */ 
/*    */   public Instruction storeParameter(int index) {
/* 77 */     return new ASTORE(index + 5);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 46 */     argTypes[0] = Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/* 47 */     argTypes[1] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/* 48 */     argTypes[2] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
/* 49 */     argTypes[3] = Type.INT;
/* 50 */     argNames[0] = "document";
/* 51 */     argNames[1] = "iterator";
/* 52 */     argNames[2] = "handler";
/* 53 */     argNames[3] = "node";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.AttributeSetMethodGenerator
 * JD-Core Version:    0.6.2
 */