/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.serializer.ElemDesc;
/*     */ 
/*     */ final class LiteralAttribute extends Instruction
/*     */ {
/*     */   private final String _name;
/*     */   private final AttributeValue _value;
/*     */ 
/*     */   public LiteralAttribute(String name, String value, Parser parser, SyntaxTreeNode parent)
/*     */   {
/*  57 */     this._name = name;
/*  58 */     setParent(parent);
/*  59 */     this._value = AttributeValue.create(this, value, parser);
/*     */   }
/*     */ 
/*     */   public void display(int indent) {
/*  63 */     indent(indent);
/*  64 */     Util.println("LiteralAttribute name=" + this._name + " value=" + this._value);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  68 */     this._value.typeCheck(stable);
/*  69 */     typeCheckContents(stable);
/*  70 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   protected boolean contextDependent() {
/*  74 */     return this._value.contextDependent();
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  78 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  79 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  82 */     il.append(methodGen.loadHandler());
/*     */ 
/*  84 */     il.append(new PUSH(cpg, this._name));
/*     */ 
/*  86 */     this._value.translate(classGen, methodGen);
/*     */ 
/*  90 */     SyntaxTreeNode parent = getParent();
/*  91 */     if (((parent instanceof LiteralElement)) && (((LiteralElement)parent).allAttributesUnique()))
/*     */     {
/*  94 */       int flags = 0;
/*  95 */       boolean isHTMLAttrEmpty = false;
/*  96 */       ElemDesc elemDesc = ((LiteralElement)parent).getElemDesc();
/*     */ 
/*  99 */       if (elemDesc != null) {
/* 100 */         if (elemDesc.isAttrFlagSet(this._name, 4)) {
/* 101 */           flags |= 2;
/* 102 */           isHTMLAttrEmpty = true;
/*     */         }
/* 104 */         else if (elemDesc.isAttrFlagSet(this._name, 2)) {
/* 105 */           flags |= 4;
/*     */         }
/*     */       }
/*     */ 
/* 109 */       if ((this._value instanceof SimpleAttributeValue)) {
/* 110 */         String attrValue = ((SimpleAttributeValue)this._value).toString();
/*     */ 
/* 112 */         if ((!hasBadChars(attrValue)) && (!isHTMLAttrEmpty)) {
/* 113 */           flags |= 1;
/*     */         }
/*     */       }
/*     */ 
/* 117 */       il.append(new PUSH(cpg, flags));
/* 118 */       il.append(methodGen.uniqueAttribute());
/*     */     }
/*     */     else
/*     */     {
/* 122 */       il.append(methodGen.attribute());
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean hasBadChars(String value)
/*     */   {
/* 135 */     char[] chars = value.toCharArray();
/* 136 */     int size = chars.length;
/* 137 */     for (int i = 0; i < size; i++) {
/* 138 */       char ch = chars[i];
/* 139 */       if ((ch < ' ') || ('~' < ch) || (ch == '<') || (ch == '>') || (ch == '&') || (ch == '"'))
/* 140 */         return true;
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 149 */     return this._name;
/*     */   }
/*     */ 
/*     */   public AttributeValue getValue()
/*     */   {
/* 156 */     return this._value;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.LiteralAttribute
 * JD-Core Version:    0.6.2
 */