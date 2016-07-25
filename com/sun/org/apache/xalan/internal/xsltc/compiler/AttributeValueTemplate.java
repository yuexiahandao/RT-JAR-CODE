/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class AttributeValueTemplate extends AttributeValue
/*     */ {
/*     */   static final int OUT_EXPR = 0;
/*     */   static final int IN_EXPR = 1;
/*     */   static final int IN_EXPR_SQUOTES = 2;
/*     */   static final int IN_EXPR_DQUOTES = 3;
/*     */   static final String DELIMITER = "￾";
/*     */ 
/*     */   public AttributeValueTemplate(String value, Parser parser, SyntaxTreeNode parent)
/*     */   {
/*  58 */     setParent(parent);
/*  59 */     setParser(parser);
/*     */     try
/*     */     {
/*  62 */       parseAVTemplate(value, parser);
/*     */     }
/*     */     catch (NoSuchElementException e) {
/*  65 */       reportError(parent, parser, "ATTR_VAL_TEMPLATE_ERR", value);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseAVTemplate(String text, Parser parser)
/*     */   {
/*  77 */     StringTokenizer tokenizer = new StringTokenizer(text, "{}\"'", true);
/*     */ 
/*  85 */     String t = null;
/*  86 */     String lookahead = null;
/*  87 */     StringBuffer buffer = new StringBuffer();
/*  88 */     int state = 0;
/*     */ 
/*  90 */     while (tokenizer.hasMoreTokens())
/*     */     {
/*  92 */       if (lookahead != null) {
/*  93 */         t = lookahead;
/*  94 */         lookahead = null;
/*     */       }
/*     */       else {
/*  97 */         t = tokenizer.nextToken();
/*     */       }
/*     */ 
/* 100 */       if (t.length() == 1) {
/* 101 */         switch (t.charAt(0)) {
/*     */         case '{':
/* 103 */           switch (state) {
/*     */           case 0:
/* 105 */             lookahead = tokenizer.nextToken();
/* 106 */             if (lookahead.equals("{")) {
/* 107 */               buffer.append(lookahead);
/* 108 */               lookahead = null;
/*     */             }
/*     */             else {
/* 111 */               buffer.append("￾");
/* 112 */               state = 1;
/*     */             }
/* 114 */             break;
/*     */           case 1:
/*     */           case 2:
/*     */           case 3:
/* 118 */             reportError(getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", text);
/*     */           }
/*     */ 
/* 122 */           break;
/*     */         case '}':
/* 124 */           switch (state) {
/*     */           case 0:
/* 126 */             lookahead = tokenizer.nextToken();
/* 127 */             if (lookahead.equals("}")) {
/* 128 */               buffer.append(lookahead);
/* 129 */               lookahead = null;
/*     */             }
/*     */             else {
/* 132 */               reportError(getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", text);
/*     */             }
/*     */ 
/* 135 */             break;
/*     */           case 1:
/* 137 */             buffer.append("￾");
/* 138 */             state = 0;
/* 139 */             break;
/*     */           case 2:
/*     */           case 3:
/* 142 */             buffer.append(t);
/*     */           }
/*     */ 
/* 145 */           break;
/*     */         case '\'':
/* 147 */           switch (state) {
/*     */           case 1:
/* 149 */             state = 2;
/* 150 */             break;
/*     */           case 2:
/* 152 */             state = 1;
/* 153 */             break;
/*     */           case 0:
/*     */           case 3:
/*     */           }
/*     */ 
/* 158 */           buffer.append(t);
/* 159 */           break;
/*     */         case '"':
/* 161 */           switch (state) {
/*     */           case 1:
/* 163 */             state = 3;
/* 164 */             break;
/*     */           case 3:
/* 166 */             state = 1;
/* 167 */             break;
/*     */           case 0:
/*     */           case 2:
/*     */           }
/*     */ 
/* 172 */           buffer.append(t);
/* 173 */           break;
/*     */         default:
/* 175 */           buffer.append(t);
/* 176 */           break;
/*     */         }
/*     */       }
/*     */       else {
/* 180 */         buffer.append(t);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 185 */     if (state != 0) {
/* 186 */       reportError(getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", text);
/*     */     }
/*     */ 
/* 193 */     tokenizer = new StringTokenizer(buffer.toString(), "￾", true);
/*     */ 
/* 195 */     while (tokenizer.hasMoreTokens()) {
/* 196 */       t = tokenizer.nextToken();
/*     */ 
/* 198 */       if (t.equals("￾")) {
/* 199 */         addElement(parser.parseExpression(this, tokenizer.nextToken()));
/* 200 */         tokenizer.nextToken();
/*     */       }
/*     */       else {
/* 203 */         addElement(new LiteralExpr(t));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 209 */     Vector contents = getContents();
/* 210 */     int n = contents.size();
/* 211 */     for (int i = 0; i < n; i++) {
/* 212 */       Expression exp = (Expression)contents.elementAt(i);
/* 213 */       if (!exp.typeCheck(stable).identicalTo(Type.String)) {
/* 214 */         contents.setElementAt(new CastExpr(exp, Type.String), i);
/*     */       }
/*     */     }
/* 217 */     return this._type = Type.String;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 221 */     StringBuffer buffer = new StringBuffer("AVT:[");
/* 222 */     int count = elementCount();
/* 223 */     for (int i = 0; i < count; i++) {
/* 224 */       buffer.append(elementAt(i).toString());
/* 225 */       if (i < count - 1)
/* 226 */         buffer.append(' ');
/*     */     }
/* 228 */     return ']';
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 232 */     if (elementCount() == 1) {
/* 233 */       Expression exp = (Expression)elementAt(0);
/* 234 */       exp.translate(classGen, methodGen);
/*     */     }
/*     */     else {
/* 237 */       ConstantPoolGen cpg = classGen.getConstantPool();
/* 238 */       InstructionList il = methodGen.getInstructionList();
/* 239 */       int initBuffer = cpg.addMethodref("java.lang.StringBuffer", "<init>", "()V");
/*     */ 
/* 241 */       Instruction append = new INVOKEVIRTUAL(cpg.addMethodref("java.lang.StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
/*     */ 
/* 247 */       int toString = cpg.addMethodref("java.lang.StringBuffer", "toString", "()Ljava/lang/String;");
/*     */ 
/* 250 */       il.append(new NEW(cpg.addClass("java.lang.StringBuffer")));
/* 251 */       il.append(DUP);
/* 252 */       il.append(new INVOKESPECIAL(initBuffer));
/*     */ 
/* 254 */       Enumeration elements = elements();
/* 255 */       while (elements.hasMoreElements()) {
/* 256 */         Expression exp = (Expression)elements.nextElement();
/* 257 */         exp.translate(classGen, methodGen);
/* 258 */         il.append(append);
/*     */       }
/* 260 */       il.append(new INVOKEVIRTUAL(toString));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.AttributeValueTemplate
 * JD-Core Version:    0.6.2
 */