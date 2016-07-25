/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ final class DecimalFormatting extends TopLevelElement
/*     */ {
/*     */   private static final String DFS_CLASS = "java.text.DecimalFormatSymbols";
/*     */   private static final String DFS_SIG = "Ljava/text/DecimalFormatSymbols;";
/*  50 */   private QName _name = null;
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  56 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/*  64 */     String name = getAttribute("name");
/*  65 */     if ((name.length() > 0) && 
/*  66 */       (!XML11Char.isXML11ValidQName(name))) {
/*  67 */       ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
/*  68 */       parser.reportError(3, err);
/*     */     }
/*     */ 
/*  71 */     this._name = parser.getQNameIgnoreDefaultNs(name);
/*  72 */     if (this._name == null) {
/*  73 */       this._name = parser.getQNameIgnoreDefaultNs("");
/*     */     }
/*     */ 
/*  77 */     SymbolTable stable = parser.getSymbolTable();
/*  78 */     if (stable.getDecimalFormatting(this._name) != null) {
/*  79 */       reportWarning(this, parser, "SYMBOLS_REDEF_ERR", this._name.toString());
/*     */     }
/*     */     else
/*     */     {
/*  83 */       stable.addDecimalFormatting(this._name, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*  93 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  94 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  98 */     int init = cpg.addMethodref("java.text.DecimalFormatSymbols", "<init>", "(Ljava/util/Locale;)V");
/*     */ 
/* 102 */     il.append(classGen.loadTranslet());
/* 103 */     il.append(new PUSH(cpg, this._name.toString()));
/*     */ 
/* 109 */     il.append(new NEW(cpg.addClass("java.text.DecimalFormatSymbols")));
/* 110 */     il.append(DUP);
/* 111 */     il.append(new GETSTATIC(cpg.addFieldref("java.util.Locale", "US", "Ljava/util/Locale;")));
/*     */ 
/* 113 */     il.append(new INVOKESPECIAL(init));
/*     */ 
/* 115 */     String tmp = getAttribute("NaN");
/* 116 */     if ((tmp == null) || (tmp.equals(""))) {
/* 117 */       int nan = cpg.addMethodref("java.text.DecimalFormatSymbols", "setNaN", "(Ljava/lang/String;)V");
/*     */ 
/* 119 */       il.append(DUP);
/* 120 */       il.append(new PUSH(cpg, "NaN"));
/* 121 */       il.append(new INVOKEVIRTUAL(nan));
/*     */     }
/*     */ 
/* 124 */     tmp = getAttribute("infinity");
/* 125 */     if ((tmp == null) || (tmp.equals(""))) {
/* 126 */       int inf = cpg.addMethodref("java.text.DecimalFormatSymbols", "setInfinity", "(Ljava/lang/String;)V");
/*     */ 
/* 129 */       il.append(DUP);
/* 130 */       il.append(new PUSH(cpg, "Infinity"));
/* 131 */       il.append(new INVOKEVIRTUAL(inf));
/*     */     }
/*     */ 
/* 134 */     int nAttributes = this._attributes.getLength();
/* 135 */     for (int i = 0; i < nAttributes; i++) {
/* 136 */       String name = this._attributes.getQName(i);
/* 137 */       String value = this._attributes.getValue(i);
/*     */ 
/* 139 */       boolean valid = true;
/* 140 */       int method = 0;
/*     */ 
/* 142 */       if (name.equals("decimal-separator"))
/*     */       {
/* 144 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setDecimalSeparator", "(C)V");
/*     */       }
/* 147 */       else if (name.equals("grouping-separator")) {
/* 148 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setGroupingSeparator", "(C)V");
/*     */       }
/* 151 */       else if (name.equals("minus-sign")) {
/* 152 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setMinusSign", "(C)V");
/*     */       }
/* 155 */       else if (name.equals("percent")) {
/* 156 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setPercent", "(C)V");
/*     */       }
/* 159 */       else if (name.equals("per-mille")) {
/* 160 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setPerMill", "(C)V");
/*     */       }
/* 163 */       else if (name.equals("zero-digit")) {
/* 164 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setZeroDigit", "(C)V");
/*     */       }
/* 167 */       else if (name.equals("digit")) {
/* 168 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setDigit", "(C)V");
/*     */       }
/* 171 */       else if (name.equals("pattern-separator")) {
/* 172 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setPatternSeparator", "(C)V");
/*     */       }
/* 175 */       else if (name.equals("NaN")) {
/* 176 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setNaN", "(Ljava/lang/String;)V");
/*     */ 
/* 178 */         il.append(DUP);
/* 179 */         il.append(new PUSH(cpg, value));
/* 180 */         il.append(new INVOKEVIRTUAL(method));
/* 181 */         valid = false;
/*     */       }
/* 183 */       else if (name.equals("infinity")) {
/* 184 */         method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setInfinity", "(Ljava/lang/String;)V");
/*     */ 
/* 187 */         il.append(DUP);
/* 188 */         il.append(new PUSH(cpg, value));
/* 189 */         il.append(new INVOKEVIRTUAL(method));
/* 190 */         valid = false;
/*     */       }
/*     */       else {
/* 193 */         valid = false;
/*     */       }
/*     */ 
/* 196 */       if (valid) {
/* 197 */         il.append(DUP);
/* 198 */         il.append(new PUSH(cpg, value.charAt(0)));
/* 199 */         il.append(new INVOKEVIRTUAL(method));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 204 */     int put = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "addDecimalFormat", "(Ljava/lang/String;Ljava/text/DecimalFormatSymbols;)V");
/*     */ 
/* 207 */     il.append(new INVOKEVIRTUAL(put));
/*     */   }
/*     */ 
/*     */   public static void translateDefaultDFS(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 219 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 220 */     InstructionList il = methodGen.getInstructionList();
/* 221 */     int init = cpg.addMethodref("java.text.DecimalFormatSymbols", "<init>", "(Ljava/util/Locale;)V");
/*     */ 
/* 226 */     il.append(classGen.loadTranslet());
/* 227 */     il.append(new PUSH(cpg, ""));
/*     */ 
/* 234 */     il.append(new NEW(cpg.addClass("java.text.DecimalFormatSymbols")));
/* 235 */     il.append(DUP);
/* 236 */     il.append(new GETSTATIC(cpg.addFieldref("java.util.Locale", "US", "Ljava/util/Locale;")));
/*     */ 
/* 238 */     il.append(new INVOKESPECIAL(init));
/*     */ 
/* 240 */     int nan = cpg.addMethodref("java.text.DecimalFormatSymbols", "setNaN", "(Ljava/lang/String;)V");
/*     */ 
/* 242 */     il.append(DUP);
/* 243 */     il.append(new PUSH(cpg, "NaN"));
/* 244 */     il.append(new INVOKEVIRTUAL(nan));
/*     */ 
/* 246 */     int inf = cpg.addMethodref("java.text.DecimalFormatSymbols", "setInfinity", "(Ljava/lang/String;)V");
/*     */ 
/* 249 */     il.append(DUP);
/* 250 */     il.append(new PUSH(cpg, "Infinity"));
/* 251 */     il.append(new INVOKEVIRTUAL(inf));
/*     */ 
/* 253 */     int put = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "addDecimalFormat", "(Ljava/lang/String;Ljava/text/DecimalFormatSymbols;)V");
/*     */ 
/* 256 */     il.append(new INVOKEVIRTUAL(put));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.DecimalFormatting
 * JD-Core Version:    0.6.2
 */