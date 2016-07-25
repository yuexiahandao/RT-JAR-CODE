/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import java.util.StringTokenizer;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class UseAttributeSets extends Instruction
/*    */ {
/*    */   private static final String ATTR_SET_NOT_FOUND = "";
/* 50 */   private final Vector _sets = new Vector(2);
/*    */ 
/*    */   public UseAttributeSets(String setNames, Parser parser)
/*    */   {
/* 56 */     setParser(parser);
/* 57 */     addAttributeSets(setNames);
/*    */   }
/*    */ 
/*    */   public void addAttributeSets(String setNames)
/*    */   {
/* 66 */     if ((setNames != null) && (!setNames.equals(""))) {
/* 67 */       StringTokenizer tokens = new StringTokenizer(setNames);
/* 68 */       while (tokens.hasMoreTokens()) {
/* 69 */         QName qname = getParser().getQNameIgnoreDefaultNs(tokens.nextToken());
/*    */ 
/* 71 */         this._sets.add(qname);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable)
/*    */     throws TypeCheckError
/*    */   {
/* 80 */     return Type.Void;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 88 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 89 */     InstructionList il = methodGen.getInstructionList();
/* 90 */     SymbolTable symbolTable = getParser().getSymbolTable();
/*    */ 
/* 93 */     for (int i = 0; i < this._sets.size(); i++)
/*    */     {
/* 95 */       QName name = (QName)this._sets.elementAt(i);
/*    */ 
/* 97 */       AttributeSet attrs = symbolTable.lookupAttributeSet(name);
/*    */ 
/* 99 */       if (attrs != null) {
/* 100 */         String methodName = attrs.getMethodName();
/* 101 */         il.append(classGen.loadTranslet());
/* 102 */         il.append(methodGen.loadDOM());
/* 103 */         il.append(methodGen.loadIterator());
/* 104 */         il.append(methodGen.loadHandler());
/* 105 */         il.append(methodGen.loadCurrentNode());
/* 106 */         int method = cpg.addMethodref(classGen.getClassName(), methodName, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;I)V");
/*    */ 
/* 108 */         il.append(new INVOKESPECIAL(method));
/*    */       }
/*    */       else
/*    */       {
/* 112 */         Parser parser = getParser();
/* 113 */         String atrs = name.toString();
/* 114 */         reportError(this, parser, "ATTRIBSET_UNDEF_ERR", atrs);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.UseAttributeSets
 * JD-Core Version:    0.6.2
 */