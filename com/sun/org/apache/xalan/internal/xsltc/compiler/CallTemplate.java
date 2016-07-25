/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class CallTemplate extends Instruction
/*     */ {
/*     */   private QName _name;
/*  59 */   private Object[] _parameters = null;
/*     */ 
/*  64 */   private Template _calleeTemplate = null;
/*     */ 
/*     */   public void display(int indent) {
/*  67 */     indent(indent);
/*  68 */     System.out.print("CallTemplate");
/*  69 */     Util.println(" name " + this._name);
/*  70 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public boolean hasWithParams() {
/*  74 */     return elementCount() > 0;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  78 */     String name = getAttribute("name");
/*  79 */     if (name.length() > 0) {
/*  80 */       if (!XML11Char.isXML11ValidQName(name)) {
/*  81 */         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
/*  82 */         parser.reportError(3, err);
/*     */       }
/*  84 */       this._name = parser.getQNameIgnoreDefaultNs(name);
/*     */     }
/*     */     else {
/*  87 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
/*     */     }
/*  89 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  96 */     Template template = stable.lookupTemplate(this._name);
/*  97 */     if (template != null) {
/*  98 */       typeCheckContents(stable);
/*     */     }
/*     */     else {
/* 101 */       ErrorMsg err = new ErrorMsg("TEMPLATE_UNDEF_ERR", this._name, this);
/* 102 */       throw new TypeCheckError(err);
/*     */     }
/* 104 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 108 */     Stylesheet stylesheet = classGen.getStylesheet();
/* 109 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 110 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 113 */     if ((stylesheet.hasLocalParams()) || (hasContents())) {
/* 114 */       this._calleeTemplate = getCalleeTemplate();
/*     */ 
/* 117 */       if (this._calleeTemplate != null) {
/* 118 */         buildParameterList();
/*     */       }
/*     */       else
/*     */       {
/* 124 */         int push = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "pushParamFrame", "()V");
/*     */ 
/* 127 */         il.append(classGen.loadTranslet());
/* 128 */         il.append(new INVOKEVIRTUAL(push));
/* 129 */         translateContents(classGen, methodGen);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 134 */     String className = stylesheet.getClassName();
/* 135 */     String methodName = Util.escape(this._name.toString());
/*     */ 
/* 138 */     il.append(classGen.loadTranslet());
/* 139 */     il.append(methodGen.loadDOM());
/* 140 */     il.append(methodGen.loadIterator());
/* 141 */     il.append(methodGen.loadHandler());
/* 142 */     il.append(methodGen.loadCurrentNode());
/*     */ 
/* 145 */     StringBuffer methodSig = new StringBuffer("(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;I");
/*     */ 
/* 149 */     if (this._calleeTemplate != null) {
/* 150 */       Vector calleeParams = this._calleeTemplate.getParameters();
/* 151 */       int numParams = this._parameters.length;
/*     */ 
/* 153 */       for (int i = 0; i < numParams; i++) {
/* 154 */         SyntaxTreeNode node = (SyntaxTreeNode)this._parameters[i];
/* 155 */         methodSig.append("Ljava/lang/Object;");
/*     */ 
/* 158 */         if ((node instanceof Param)) {
/* 159 */           il.append(ACONST_NULL);
/*     */         }
/*     */         else {
/* 162 */           node.translate(classGen, methodGen);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 168 */     methodSig.append(")V");
/* 169 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref(className, methodName, methodSig.toString())));
/*     */ 
/* 175 */     if ((this._calleeTemplate == null) && ((stylesheet.hasLocalParams()) || (hasContents())))
/*     */     {
/* 177 */       int pop = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "popParamFrame", "()V");
/*     */ 
/* 180 */       il.append(classGen.loadTranslet());
/* 181 */       il.append(new INVOKEVIRTUAL(pop));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Template getCalleeTemplate()
/*     */   {
/* 191 */     Template foundTemplate = getXSLTC().getParser().getSymbolTable().lookupTemplate(this._name);
/*     */ 
/* 194 */     return foundTemplate.isSimpleNamedTemplate() ? foundTemplate : null;
/*     */   }
/*     */ 
/*     */   private void buildParameterList()
/*     */   {
/* 206 */     Vector defaultParams = this._calleeTemplate.getParameters();
/* 207 */     int numParams = defaultParams.size();
/* 208 */     this._parameters = new Object[numParams];
/* 209 */     for (int i = 0; i < numParams; i++) {
/* 210 */       this._parameters[i] = defaultParams.elementAt(i);
/*     */     }
/*     */ 
/* 214 */     int count = elementCount();
/* 215 */     for (int i = 0; i < count; i++) {
/* 216 */       Object node = elementAt(i);
/*     */ 
/* 219 */       if ((node instanceof WithParam)) {
/* 220 */         WithParam withParam = (WithParam)node;
/* 221 */         QName name = withParam.getName();
/*     */ 
/* 224 */         for (int k = 0; k < numParams; k++) {
/* 225 */           Object object = this._parameters[k];
/* 226 */           if (((object instanceof Param)) && (((Param)object).getName().equals(name)))
/*     */           {
/* 228 */             withParam.setDoParameterOptimization(true);
/* 229 */             this._parameters[k] = withParam;
/* 230 */             break;
/*     */           }
/* 232 */           if (((object instanceof WithParam)) && (((WithParam)object).getName().equals(name)))
/*     */           {
/* 234 */             withParam.setDoParameterOptimization(true);
/* 235 */             this._parameters[k] = withParam;
/* 236 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.CallTemplate
 * JD-Core Version:    0.6.2
 */