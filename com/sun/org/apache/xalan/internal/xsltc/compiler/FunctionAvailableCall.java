/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class FunctionAvailableCall extends FunctionCall
/*     */ {
/*     */   private Expression _arg;
/*  47 */   private String _nameOfFunct = null;
/*  48 */   private String _namespaceOfFunct = null;
/*  49 */   private boolean _isFunctionAvailable = false;
/*     */ 
/*     */   public FunctionAvailableCall(QName fname, Vector arguments)
/*     */   {
/*  58 */     super(fname, arguments);
/*  59 */     this._arg = ((Expression)arguments.elementAt(0));
/*  60 */     this._type = null;
/*     */ 
/*  62 */     if ((this._arg instanceof LiteralExpr)) {
/*  63 */       LiteralExpr arg = (LiteralExpr)this._arg;
/*  64 */       this._namespaceOfFunct = arg.getNamespace();
/*  65 */       this._nameOfFunct = arg.getValue();
/*     */ 
/*  67 */       if (!isInternalNamespace())
/*  68 */         this._isFunctionAvailable = hasMethods();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  78 */     if (this._type != null) {
/*  79 */       return this._type;
/*     */     }
/*  81 */     if ((this._arg instanceof LiteralExpr)) {
/*  82 */       return this._type = Type.Boolean;
/*     */     }
/*  84 */     ErrorMsg err = new ErrorMsg("NEED_LITERAL_ERR", "function-available", this);
/*     */ 
/*  86 */     throw new TypeCheckError(err);
/*     */   }
/*     */ 
/*     */   public Object evaluateAtCompileTime()
/*     */   {
/*  95 */     return getResult() ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   private boolean hasMethods()
/*     */   {
/* 103 */     LiteralExpr arg = (LiteralExpr)this._arg;
/*     */ 
/* 106 */     String className = getClassNameFromUri(this._namespaceOfFunct);
/*     */ 
/* 109 */     String methodName = null;
/* 110 */     int colonIndex = this._nameOfFunct.indexOf(":");
/* 111 */     if (colonIndex > 0) {
/* 112 */       String functionName = this._nameOfFunct.substring(colonIndex + 1);
/* 113 */       int lastDotIndex = functionName.lastIndexOf('.');
/* 114 */       if (lastDotIndex > 0) {
/* 115 */         methodName = functionName.substring(lastDotIndex + 1);
/* 116 */         if ((className != null) && (!className.equals("")))
/* 117 */           className = className + "." + functionName.substring(0, lastDotIndex);
/*     */         else
/* 119 */           className = functionName.substring(0, lastDotIndex);
/*     */       }
/*     */       else {
/* 122 */         methodName = functionName;
/*     */       }
/*     */     } else {
/* 125 */       methodName = this._nameOfFunct;
/*     */     }
/* 127 */     if ((className == null) || (methodName == null)) {
/* 128 */       return false;
/*     */     }
/*     */ 
/* 132 */     if (methodName.indexOf('-') > 0)
/* 133 */       methodName = replaceDash(methodName);
/*     */     try
/*     */     {
/* 136 */       Class clazz = ObjectFactory.findProviderClass(className, true);
/*     */ 
/* 138 */       if (clazz == null) {
/* 139 */         return false;
/*     */       }
/*     */ 
/* 142 */       Method[] methods = clazz.getMethods();
/*     */ 
/* 144 */       for (int i = 0; i < methods.length; i++) {
/* 145 */         int mods = methods[i].getModifiers();
/*     */ 
/* 147 */         if ((Modifier.isPublic(mods)) && (Modifier.isStatic(mods)) && (methods[i].getName().equals(methodName)))
/*     */         {
/* 150 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (ClassNotFoundException e) {
/* 155 */       return false;
/*     */     }
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean getResult()
/*     */   {
/* 165 */     if (this._nameOfFunct == null) {
/* 166 */       return false;
/*     */     }
/*     */ 
/* 169 */     if (isInternalNamespace()) {
/* 170 */       Parser parser = getParser();
/* 171 */       this._isFunctionAvailable = parser.functionSupported(Util.getLocalName(this._nameOfFunct));
/*     */     }
/*     */ 
/* 174 */     return this._isFunctionAvailable;
/*     */   }
/*     */ 
/*     */   private boolean isInternalNamespace()
/*     */   {
/* 181 */     return (this._namespaceOfFunct == null) || (this._namespaceOfFunct.equals("")) || (this._namespaceOfFunct.equals("http://xml.apache.org/xalan/xsltc"));
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 192 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 193 */     methodGen.getInstructionList().append(new PUSH(cpg, getResult()));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionAvailableCall
 * JD-Core Version:    0.6.2
 */