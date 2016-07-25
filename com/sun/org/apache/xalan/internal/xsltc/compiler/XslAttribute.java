/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.serializer.ElemDesc;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class XslAttribute extends Instruction
/*     */ {
/*     */   private String _prefix;
/*     */   private AttributeValue _name;
/*  59 */   private AttributeValueTemplate _namespace = null;
/*  60 */   private boolean _ignore = false;
/*  61 */   private boolean _isLiteral = false;
/*     */ 
/*     */   public AttributeValue getName()
/*     */   {
/*  67 */     return this._name;
/*     */   }
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  74 */     indent(indent);
/*  75 */     Util.println("Attribute " + this._name);
/*  76 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/*  83 */     boolean generated = false;
/*  84 */     SymbolTable stable = parser.getSymbolTable();
/*     */ 
/*  86 */     String name = getAttribute("name");
/*  87 */     String namespace = getAttribute("namespace");
/*  88 */     QName qname = parser.getQName(name, false);
/*  89 */     String prefix = qname.getPrefix();
/*     */ 
/*  91 */     if (((prefix != null) && (prefix.equals("xmlns"))) || (name.equals("xmlns"))) {
/*  92 */       reportError(this, parser, "ILLEGAL_ATTR_NAME_ERR", name);
/*  93 */       return;
/*     */     }
/*     */ 
/*  96 */     this._isLiteral = Util.isLiteral(name);
/*  97 */     if ((this._isLiteral) && 
/*  98 */       (!XML11Char.isXML11ValidQName(name))) {
/*  99 */       reportError(this, parser, "ILLEGAL_ATTR_NAME_ERR", name);
/* 100 */       return;
/*     */     }
/*     */ 
/* 105 */     SyntaxTreeNode parent = getParent();
/* 106 */     Vector siblings = parent.getContents();
/* 107 */     for (int i = 0; i < parent.elementCount(); i++) {
/* 108 */       SyntaxTreeNode item = (SyntaxTreeNode)siblings.elementAt(i);
/* 109 */       if (item == this) {
/*     */         break;
/*     */       }
/* 112 */       if ((!(item instanceof XslAttribute)) && 
/* 113 */         (!(item instanceof UseAttributeSets)) && 
/* 114 */         (!(item instanceof LiteralAttribute)) && 
/* 115 */         (!(item instanceof Text)))
/*     */       {
/* 119 */         if ((!(item instanceof If)) && 
/* 120 */           (!(item instanceof Choose)) && 
/* 121 */           (!(item instanceof CopyOf)) && 
/* 122 */           (!(item instanceof VariableBase)))
/*     */         {
/* 125 */           reportWarning(this, parser, "STRAY_ATTRIBUTE_ERR", name);
/*     */         }
/*     */       }
/*     */     }
/* 129 */     if ((namespace != null) && (namespace != "")) {
/* 130 */       this._prefix = lookupPrefix(namespace);
/* 131 */       this._namespace = new AttributeValueTemplate(namespace, parser, this);
/*     */     }
/* 134 */     else if ((prefix != null) && (prefix != "")) {
/* 135 */       this._prefix = prefix;
/* 136 */       namespace = lookupNamespace(prefix);
/* 137 */       if (namespace != null) {
/* 138 */         this._namespace = new AttributeValueTemplate(namespace, parser, this);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 143 */     if (this._namespace != null)
/*     */     {
/* 145 */       if ((this._prefix == null) || (this._prefix == "")) {
/* 146 */         if (prefix != null) {
/* 147 */           this._prefix = prefix;
/*     */         }
/*     */         else {
/* 150 */           this._prefix = stable.generateNamespacePrefix();
/* 151 */           generated = true;
/*     */         }
/*     */       }
/* 154 */       else if ((prefix != null) && (!prefix.equals(this._prefix))) {
/* 155 */         this._prefix = prefix;
/*     */       }
/*     */ 
/* 158 */       name = this._prefix + ":" + qname.getLocalPart();
/*     */ 
/* 165 */       if (((parent instanceof LiteralElement)) && (!generated)) {
/* 166 */         ((LiteralElement)parent).registerNamespace(this._prefix, namespace, stable, false);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 172 */     if ((parent instanceof LiteralElement)) {
/* 173 */       ((LiteralElement)parent).addAttribute(this);
/*     */     }
/*     */ 
/* 176 */     this._name = AttributeValue.create(this, name, parser);
/* 177 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 181 */     if (!this._ignore) {
/* 182 */       this._name.typeCheck(stable);
/* 183 */       if (this._namespace != null) {
/* 184 */         this._namespace.typeCheck(stable);
/*     */       }
/* 186 */       typeCheckContents(stable);
/*     */     }
/* 188 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 195 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 196 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 198 */     if (this._ignore) return;
/* 199 */     this._ignore = true;
/*     */ 
/* 202 */     if (this._namespace != null)
/*     */     {
/* 204 */       il.append(methodGen.loadHandler());
/* 205 */       il.append(new PUSH(cpg, this._prefix));
/* 206 */       this._namespace.translate(classGen, methodGen);
/* 207 */       il.append(methodGen.namespace());
/*     */     }
/*     */ 
/* 210 */     if (!this._isLiteral)
/*     */     {
/* 212 */       LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), null);
/*     */ 
/* 218 */       this._name.translate(classGen, methodGen);
/* 219 */       nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
/* 220 */       il.append(new ALOAD(nameValue.getIndex()));
/*     */ 
/* 223 */       int check = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "checkAttribQName", "(Ljava/lang/String;)V");
/*     */ 
/* 227 */       il.append(new INVOKESTATIC(check));
/*     */ 
/* 230 */       il.append(methodGen.loadHandler());
/* 231 */       il.append(DUP);
/*     */ 
/* 234 */       nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));
/*     */     }
/*     */     else {
/* 237 */       il.append(methodGen.loadHandler());
/* 238 */       il.append(DUP);
/*     */ 
/* 241 */       this._name.translate(classGen, methodGen);
/*     */     }
/*     */ 
/* 246 */     if ((elementCount() == 1) && ((elementAt(0) instanceof Text))) {
/* 247 */       il.append(new PUSH(cpg, ((Text)elementAt(0)).getText()));
/*     */     }
/*     */     else {
/* 250 */       il.append(classGen.loadTranslet());
/* 251 */       il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lcom/sun/org/apache/xalan/internal/xsltc/runtime/StringValueHandler;")));
/*     */ 
/* 254 */       il.append(DUP);
/* 255 */       il.append(methodGen.storeHandler());
/*     */ 
/* 257 */       translateContents(classGen, methodGen);
/*     */ 
/* 259 */       il.append(new INVOKEVIRTUAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;")));
/*     */     }
/*     */ 
/* 264 */     SyntaxTreeNode parent = getParent();
/* 265 */     if (((parent instanceof LiteralElement)) && (((LiteralElement)parent).allAttributesUnique()))
/*     */     {
/* 267 */       int flags = 0;
/* 268 */       ElemDesc elemDesc = ((LiteralElement)parent).getElemDesc();
/*     */ 
/* 271 */       if ((elemDesc != null) && ((this._name instanceof SimpleAttributeValue))) {
/* 272 */         String attrName = ((SimpleAttributeValue)this._name).toString();
/* 273 */         if (elemDesc.isAttrFlagSet(attrName, 4)) {
/* 274 */           flags |= 2;
/*     */         }
/* 276 */         else if (elemDesc.isAttrFlagSet(attrName, 2)) {
/* 277 */           flags |= 4;
/*     */         }
/*     */       }
/* 280 */       il.append(new PUSH(cpg, flags));
/* 281 */       il.append(methodGen.uniqueAttribute());
/*     */     }
/*     */     else
/*     */     {
/* 285 */       il.append(methodGen.attribute());
/*     */     }
/*     */ 
/* 289 */     il.append(methodGen.storeHandler());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.XslAttribute
 * JD-Core Version:    0.6.2
 */