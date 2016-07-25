/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.AttributeSetMethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class AttributeSet extends TopLevelElement
/*     */ {
/*     */   private static final String AttributeSetPrefix = "$as$";
/*     */   private QName _name;
/*     */   private UseAttributeSets _useSets;
/*     */   private AttributeSet _mergeSet;
/*     */   private String _method;
/*  56 */   private boolean _ignore = false;
/*     */ 
/*     */   public QName getName()
/*     */   {
/*  62 */     return this._name;
/*     */   }
/*     */ 
/*     */   public String getMethodName()
/*     */   {
/*  70 */     return this._method;
/*     */   }
/*     */ 
/*     */   public void ignore()
/*     */   {
/*  80 */     this._ignore = true;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/*  90 */     String name = getAttribute("name");
/*     */ 
/*  92 */     if (!XML11Char.isXML11ValidQName(name)) {
/*  93 */       ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
/*  94 */       parser.reportError(3, err);
/*     */     }
/*  96 */     this._name = parser.getQNameIgnoreDefaultNs(name);
/*  97 */     if ((this._name == null) || (this._name.equals(""))) {
/*  98 */       ErrorMsg msg = new ErrorMsg("UNNAMED_ATTRIBSET_ERR", this);
/*  99 */       parser.reportError(3, msg);
/*     */     }
/*     */ 
/* 103 */     String useSets = getAttribute("use-attribute-sets");
/* 104 */     if (useSets.length() > 0) {
/* 105 */       if (!Util.isValidQNames(useSets)) {
/* 106 */         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", useSets, this);
/* 107 */         parser.reportError(3, err);
/*     */       }
/* 109 */       this._useSets = new UseAttributeSets(useSets, parser);
/*     */     }
/*     */ 
/* 114 */     Vector contents = getContents();
/* 115 */     int count = contents.size();
/* 116 */     for (int i = 0; i < count; i++) {
/* 117 */       SyntaxTreeNode child = (SyntaxTreeNode)contents.elementAt(i);
/* 118 */       if ((child instanceof XslAttribute)) {
/* 119 */         parser.getSymbolTable().setCurrentNode(child);
/* 120 */         child.parseContents(parser);
/*     */       }
/* 122 */       else if (!(child instanceof Text))
/*     */       {
/* 126 */         ErrorMsg msg = new ErrorMsg("ILLEGAL_CHILD_ERR", this);
/* 127 */         parser.reportError(3, msg);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 132 */     parser.getSymbolTable().setCurrentNode(this);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 140 */     if (this._ignore) return Type.Void;
/*     */ 
/* 143 */     this._mergeSet = stable.addAttributeSet(this);
/*     */ 
/* 145 */     this._method = ("$as$" + getXSLTC().nextAttributeSetSerial());
/*     */ 
/* 147 */     if (this._useSets != null) this._useSets.typeCheck(stable);
/* 148 */     typeCheckContents(stable);
/* 149 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 157 */     if (this._ignore) return;
/*     */ 
/* 160 */     methodGen = new AttributeSetMethodGenerator(this._method, classGen);
/*     */ 
/* 164 */     if (this._mergeSet != null) {
/* 165 */       ConstantPoolGen cpg = classGen.getConstantPool();
/* 166 */       InstructionList il = methodGen.getInstructionList();
/* 167 */       String methodName = this._mergeSet.getMethodName();
/*     */ 
/* 169 */       il.append(classGen.loadTranslet());
/* 170 */       il.append(methodGen.loadDOM());
/* 171 */       il.append(methodGen.loadIterator());
/* 172 */       il.append(methodGen.loadHandler());
/* 173 */       il.append(methodGen.loadCurrentNode());
/* 174 */       int method = cpg.addMethodref(classGen.getClassName(), methodName, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;I)V");
/*     */ 
/* 176 */       il.append(new INVOKESPECIAL(method));
/*     */     }
/*     */ 
/* 181 */     if (this._useSets != null) this._useSets.translate(classGen, methodGen);
/*     */ 
/* 184 */     Enumeration attributes = elements();
/* 185 */     while (attributes.hasMoreElements()) {
/* 186 */       SyntaxTreeNode element = (SyntaxTreeNode)attributes.nextElement();
/* 187 */       if ((element instanceof XslAttribute)) {
/* 188 */         XslAttribute attribute = (XslAttribute)element;
/* 189 */         attribute.translate(classGen, methodGen);
/*     */       }
/*     */     }
/* 192 */     InstructionList il = methodGen.getInstructionList();
/* 193 */     il.append(RETURN);
/*     */ 
/* 195 */     classGen.addMethod(methodGen);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 199 */     StringBuffer buf = new StringBuffer("attribute-set: ");
/*     */ 
/* 201 */     Enumeration attributes = elements();
/* 202 */     while (attributes.hasMoreElements()) {
/* 203 */       XslAttribute attribute = (XslAttribute)attributes.nextElement();
/*     */ 
/* 205 */       buf.append(attribute);
/*     */     }
/* 207 */     return buf.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.AttributeSet
 * JD-Core Version:    0.6.2
 */