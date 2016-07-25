/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class XslElement extends Instruction
/*     */ {
/*     */   private String _prefix;
/*  50 */   private boolean _ignore = false;
/*  51 */   private boolean _isLiteralName = true;
/*     */   private AttributeValueTemplate _name;
/*     */   private AttributeValueTemplate _namespace;
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  59 */     indent(indent);
/*  60 */     Util.println("Element " + this._name);
/*  61 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public boolean declaresDefaultNS()
/*     */   {
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  73 */     SymbolTable stable = parser.getSymbolTable();
/*     */ 
/*  76 */     String name = getAttribute("name");
/*  77 */     if (name == "") {
/*  78 */       ErrorMsg msg = new ErrorMsg("ILLEGAL_ELEM_NAME_ERR", name, this);
/*     */ 
/*  80 */       parser.reportError(4, msg);
/*  81 */       parseChildren(parser);
/*  82 */       this._ignore = true;
/*  83 */       return;
/*     */     }
/*     */ 
/*  87 */     String namespace = getAttribute("namespace");
/*     */ 
/*  90 */     this._isLiteralName = Util.isLiteral(name);
/*  91 */     if (this._isLiteralName) {
/*  92 */       if (!XML11Char.isXML11ValidQName(name)) {
/*  93 */         ErrorMsg msg = new ErrorMsg("ILLEGAL_ELEM_NAME_ERR", name, this);
/*     */ 
/*  95 */         parser.reportError(4, msg);
/*  96 */         parseChildren(parser);
/*  97 */         this._ignore = true;
/*  98 */         return;
/*     */       }
/*     */ 
/* 101 */       QName qname = parser.getQNameSafe(name);
/* 102 */       String prefix = qname.getPrefix();
/* 103 */       String local = qname.getLocalPart();
/*     */ 
/* 105 */       if (prefix == null) {
/* 106 */         prefix = "";
/*     */       }
/*     */ 
/* 109 */       if (!hasAttribute("namespace")) {
/* 110 */         namespace = lookupNamespace(prefix);
/* 111 */         if (namespace == null) {
/* 112 */           ErrorMsg err = new ErrorMsg("NAMESPACE_UNDEF_ERR", prefix, this);
/*     */ 
/* 114 */           parser.reportError(4, err);
/* 115 */           parseChildren(parser);
/* 116 */           this._ignore = true;
/* 117 */           return;
/*     */         }
/* 119 */         this._prefix = prefix;
/* 120 */         this._namespace = new AttributeValueTemplate(namespace, parser, this);
/*     */       }
/*     */       else {
/* 123 */         if (prefix == "") {
/* 124 */           if (Util.isLiteral(namespace)) {
/* 125 */             prefix = lookupPrefix(namespace);
/* 126 */             if (prefix == null) {
/* 127 */               prefix = stable.generateNamespacePrefix();
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 132 */           StringBuffer newName = new StringBuffer(prefix);
/* 133 */           if (prefix != "") {
/* 134 */             newName.append(':');
/*     */           }
/* 136 */           name = local;
/*     */         }
/* 138 */         this._prefix = prefix;
/* 139 */         this._namespace = new AttributeValueTemplate(namespace, parser, this);
/*     */       }
/*     */     }
/*     */     else {
/* 143 */       this._namespace = (namespace == "" ? null : new AttributeValueTemplate(namespace, parser, this));
/*     */     }
/*     */ 
/* 147 */     this._name = new AttributeValueTemplate(name, parser, this);
/*     */ 
/* 149 */     String useSets = getAttribute("use-attribute-sets");
/* 150 */     if (useSets.length() > 0) {
/* 151 */       if (!Util.isValidQNames(useSets)) {
/* 152 */         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", useSets, this);
/* 153 */         parser.reportError(3, err);
/*     */       }
/* 155 */       setFirstElement(new UseAttributeSets(useSets, parser));
/*     */     }
/*     */ 
/* 158 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 165 */     if (!this._ignore) {
/* 166 */       this._name.typeCheck(stable);
/* 167 */       if (this._namespace != null) {
/* 168 */         this._namespace.typeCheck(stable);
/*     */       }
/*     */     }
/* 171 */     typeCheckContents(stable);
/* 172 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translateLiteral(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 181 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 182 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 184 */     if (!this._ignore) {
/* 185 */       il.append(methodGen.loadHandler());
/* 186 */       this._name.translate(classGen, methodGen);
/* 187 */       il.append(DUP2);
/* 188 */       il.append(methodGen.startElement());
/*     */ 
/* 190 */       if (this._namespace != null) {
/* 191 */         il.append(methodGen.loadHandler());
/* 192 */         il.append(new PUSH(cpg, this._prefix));
/* 193 */         this._namespace.translate(classGen, methodGen);
/* 194 */         il.append(methodGen.namespace());
/*     */       }
/*     */     }
/*     */ 
/* 198 */     translateContents(classGen, methodGen);
/*     */ 
/* 200 */     if (!this._ignore)
/* 201 */       il.append(methodGen.endElement());
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 214 */     LocalVariableGen local = null;
/* 215 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 216 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 219 */     if (this._isLiteralName) {
/* 220 */       translateLiteral(classGen, methodGen);
/* 221 */       return;
/*     */     }
/*     */ 
/* 224 */     if (!this._ignore)
/*     */     {
/* 227 */       LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), null);
/*     */ 
/* 233 */       this._name.translate(classGen, methodGen);
/* 234 */       nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
/* 235 */       il.append(new ALOAD(nameValue.getIndex()));
/*     */ 
/* 238 */       int check = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "checkQName", "(Ljava/lang/String;)V");
/*     */ 
/* 242 */       il.append(new INVOKESTATIC(check));
/*     */ 
/* 245 */       il.append(methodGen.loadHandler());
/*     */ 
/* 248 */       nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));
/*     */ 
/* 250 */       if (this._namespace != null) {
/* 251 */         this._namespace.translate(classGen, methodGen);
/*     */       }
/*     */       else {
/* 254 */         il.append(ACONST_NULL);
/*     */       }
/*     */ 
/* 258 */       il.append(methodGen.loadHandler());
/* 259 */       il.append(methodGen.loadDOM());
/* 260 */       il.append(methodGen.loadCurrentNode());
/*     */ 
/* 263 */       il.append(new INVOKESTATIC(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "startXslElement", "(Ljava/lang/String;Ljava/lang/String;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;I)Ljava/lang/String;")));
/*     */     }
/*     */ 
/* 273 */     translateContents(classGen, methodGen);
/*     */ 
/* 275 */     if (!this._ignore)
/* 276 */       il.append(methodGen.endElement());
/*     */   }
/*     */ 
/*     */   public void translateContents(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 286 */     int n = elementCount();
/* 287 */     for (int i = 0; i < n; i++) {
/* 288 */       SyntaxTreeNode item = (SyntaxTreeNode)getContents().elementAt(i);
/*     */ 
/* 290 */       if ((!this._ignore) || (!(item instanceof XslAttribute)))
/* 291 */         item.translate(classGen, methodGen);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.XslElement
 * JD-Core Version:    0.6.2
 */