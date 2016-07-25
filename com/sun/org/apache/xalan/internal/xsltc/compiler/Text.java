/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ 
/*     */ final class Text extends Instruction
/*     */ {
/*     */   private String _text;
/*  43 */   private boolean _escaping = true;
/*  44 */   private boolean _ignore = false;
/*  45 */   private boolean _textElement = false;
/*     */ 
/*     */   public Text()
/*     */   {
/*  51 */     this._textElement = true;
/*     */   }
/*     */ 
/*     */   public Text(String text)
/*     */   {
/*  59 */     this._text = text;
/*     */   }
/*     */ 
/*     */   protected String getText()
/*     */   {
/*  67 */     return this._text;
/*     */   }
/*     */ 
/*     */   protected void setText(String text)
/*     */   {
/*  76 */     if (this._text == null)
/*  77 */       this._text = text;
/*     */     else
/*  79 */       this._text += text;
/*     */   }
/*     */ 
/*     */   public void display(int indent) {
/*  83 */     indent(indent);
/*  84 */     Util.println("Text");
/*  85 */     indent(indent + 4);
/*  86 */     Util.println(this._text);
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  90 */     String str = getAttribute("disable-output-escaping");
/*  91 */     if ((str != null) && (str.equals("yes"))) this._escaping = false;
/*     */ 
/*  93 */     parseChildren(parser);
/*     */ 
/*  95 */     if (this._text == null) {
/*  96 */       if (this._textElement) {
/*  97 */         this._text = "";
/*     */       }
/*     */       else {
/* 100 */         this._ignore = true;
/*     */       }
/*     */     }
/* 103 */     else if (this._textElement) {
/* 104 */       if (this._text.length() == 0) this._ignore = true;
/*     */     }
/* 106 */     else if ((getParent() instanceof LiteralElement)) {
/* 107 */       LiteralElement element = (LiteralElement)getParent();
/* 108 */       String space = element.getAttribute("xml:space");
/* 109 */       if ((space == null) || (!space.equals("preserve")))
/*     */       {
/* 112 */         int textLength = this._text.length();
/* 113 */         for (int i = 0; i < textLength; i++) {
/* 114 */           char c = this._text.charAt(i);
/* 115 */           if (!isWhitespace(c))
/*     */             break;
/*     */         }
/* 118 */         if (i == textLength)
/* 119 */           this._ignore = true;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 124 */       int textLength = this._text.length();
/* 125 */       for (int i = 0; i < textLength; i++)
/*     */       {
/* 127 */         char c = this._text.charAt(i);
/* 128 */         if (!isWhitespace(c))
/*     */           break;
/*     */       }
/* 131 */       if (i == textLength)
/* 132 */         this._ignore = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void ignore() {
/* 137 */     this._ignore = true;
/*     */   }
/*     */ 
/*     */   public boolean isIgnore() {
/* 141 */     return this._ignore;
/*     */   }
/*     */ 
/*     */   public boolean isTextElement() {
/* 145 */     return this._textElement;
/*     */   }
/*     */ 
/*     */   protected boolean contextDependent() {
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isWhitespace(char c)
/*     */   {
/* 154 */     return (c == ' ') || (c == '\t') || (c == '\n') || (c == '\r');
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 158 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 159 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 161 */     if (!this._ignore)
/*     */     {
/* 163 */       int esc = cpg.addInterfaceMethodref("com/sun/org/apache/xml/internal/serializer/SerializationHandler", "setEscaping", "(Z)Z");
/*     */ 
/* 165 */       if (!this._escaping) {
/* 166 */         il.append(methodGen.loadHandler());
/* 167 */         il.append(new PUSH(cpg, false));
/* 168 */         il.append(new INVOKEINTERFACE(esc, 2));
/*     */       }
/*     */ 
/* 171 */       il.append(methodGen.loadHandler());
/*     */ 
/* 175 */       if (!canLoadAsArrayOffsetLength()) {
/* 176 */         int characters = cpg.addInterfaceMethodref("com/sun/org/apache/xml/internal/serializer/SerializationHandler", "characters", "(Ljava/lang/String;)V");
/*     */ 
/* 179 */         il.append(new PUSH(cpg, this._text));
/* 180 */         il.append(new INVOKEINTERFACE(characters, 2));
/*     */       } else {
/* 182 */         int characters = cpg.addInterfaceMethodref("com/sun/org/apache/xml/internal/serializer/SerializationHandler", "characters", "([CII)V");
/*     */ 
/* 185 */         loadAsArrayOffsetLength(classGen, methodGen);
/* 186 */         il.append(new INVOKEINTERFACE(characters, 4));
/*     */       }
/*     */ 
/* 191 */       if (!this._escaping) {
/* 192 */         il.append(methodGen.loadHandler());
/* 193 */         il.append(SWAP);
/* 194 */         il.append(new INVOKEINTERFACE(esc, 2));
/* 195 */         il.append(POP);
/*     */       }
/*     */     }
/* 198 */     translateContents(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public boolean canLoadAsArrayOffsetLength()
/*     */   {
/* 216 */     return this._text.length() <= 21845;
/*     */   }
/*     */ 
/*     */   public void loadAsArrayOffsetLength(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 230 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 231 */     InstructionList il = methodGen.getInstructionList();
/* 232 */     XSLTC xsltc = classGen.getParser().getXSLTC();
/*     */ 
/* 236 */     int offset = xsltc.addCharacterData(this._text);
/* 237 */     int length = this._text.length();
/* 238 */     String charDataFieldName = "_scharData" + (xsltc.getCharacterDataCount() - 1);
/*     */ 
/* 241 */     il.append(new GETSTATIC(cpg.addFieldref(xsltc.getClassName(), charDataFieldName, "[C")));
/*     */ 
/* 244 */     il.append(new PUSH(cpg, offset));
/* 245 */     il.append(new PUSH(cpg, this._text.length()));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Text
 * JD-Core Version:    0.6.2
 */