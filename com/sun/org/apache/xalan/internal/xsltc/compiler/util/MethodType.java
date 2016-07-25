/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class MethodType extends Type
/*     */ {
/*     */   private final Type _resultType;
/*     */   private final Vector _argsType;
/*     */ 
/*     */   public MethodType(Type resultType)
/*     */   {
/*  37 */     this._argsType = null;
/*  38 */     this._resultType = resultType;
/*     */   }
/*     */ 
/*     */   public MethodType(Type resultType, Type arg1) {
/*  42 */     if (arg1 != Type.Void) {
/*  43 */       this._argsType = new Vector();
/*  44 */       this._argsType.addElement(arg1);
/*     */     }
/*     */     else {
/*  47 */       this._argsType = null;
/*     */     }
/*  49 */     this._resultType = resultType;
/*     */   }
/*     */ 
/*     */   public MethodType(Type resultType, Type arg1, Type arg2) {
/*  53 */     this._argsType = new Vector(2);
/*  54 */     this._argsType.addElement(arg1);
/*  55 */     this._argsType.addElement(arg2);
/*  56 */     this._resultType = resultType;
/*     */   }
/*     */ 
/*     */   public MethodType(Type resultType, Type arg1, Type arg2, Type arg3) {
/*  60 */     this._argsType = new Vector(3);
/*  61 */     this._argsType.addElement(arg1);
/*  62 */     this._argsType.addElement(arg2);
/*  63 */     this._argsType.addElement(arg3);
/*  64 */     this._resultType = resultType;
/*     */   }
/*     */ 
/*     */   public MethodType(Type resultType, Vector argsType) {
/*  68 */     this._resultType = resultType;
/*  69 */     this._argsType = (argsType.size() > 0 ? argsType : null);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  73 */     StringBuffer result = new StringBuffer("method{");
/*  74 */     if (this._argsType != null) {
/*  75 */       int count = this._argsType.size();
/*  76 */       for (int i = 0; i < count; i++) {
/*  77 */         result.append(this._argsType.elementAt(i));
/*  78 */         if (i != count - 1) result.append(','); 
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  82 */       result.append("void");
/*     */     }
/*  84 */     result.append('}');
/*  85 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public String toSignature() {
/*  89 */     return toSignature("");
/*     */   }
/*     */ 
/*     */   public String toSignature(String lastArgSig)
/*     */   {
/*  97 */     StringBuffer buffer = new StringBuffer();
/*  98 */     buffer.append('(');
/*  99 */     if (this._argsType != null) {
/* 100 */       int n = this._argsType.size();
/* 101 */       for (int i = 0; i < n; i++) {
/* 102 */         buffer.append(((Type)this._argsType.elementAt(i)).toSignature());
/*     */       }
/*     */     }
/* 105 */     return lastArgSig + ')' + this._resultType.toSignature();
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.bcel.internal.generic.Type toJCType()
/*     */   {
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean identicalTo(Type other) {
/* 117 */     boolean result = false;
/* 118 */     if ((other instanceof MethodType)) {
/* 119 */       MethodType temp = (MethodType)other;
/* 120 */       if (this._resultType.identicalTo(temp._resultType)) {
/* 121 */         int len = argsCount();
/* 122 */         result = len == temp.argsCount();
/* 123 */         for (int i = 0; (i < len) && (result); i++) {
/* 124 */           Type arg1 = (Type)this._argsType.elementAt(i);
/* 125 */           Type arg2 = (Type)temp._argsType.elementAt(i);
/* 126 */           result = arg1.identicalTo(arg2);
/*     */         }
/*     */       }
/*     */     }
/* 130 */     return result;
/*     */   }
/*     */ 
/*     */   public int distanceTo(Type other) {
/* 134 */     int result = 2147483647;
/* 135 */     if ((other instanceof MethodType)) {
/* 136 */       MethodType mtype = (MethodType)other;
/* 137 */       if (this._argsType != null) {
/* 138 */         int len = this._argsType.size();
/* 139 */         if (len == mtype._argsType.size()) {
/* 140 */           result = 0;
/* 141 */           for (int i = 0; i < len; i++) {
/* 142 */             Type arg1 = (Type)this._argsType.elementAt(i);
/* 143 */             Type arg2 = (Type)mtype._argsType.elementAt(i);
/* 144 */             int temp = arg1.distanceTo(arg2);
/* 145 */             if (temp == 2147483647) {
/* 146 */               result = temp;
/* 147 */               break;
/*     */             }
/*     */ 
/* 150 */             result += arg1.distanceTo(arg2);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/* 155 */       else if (mtype._argsType == null) {
/* 156 */         result = 0;
/*     */       }
/*     */     }
/* 159 */     return result;
/*     */   }
/*     */ 
/*     */   public Type resultType() {
/* 163 */     return this._resultType;
/*     */   }
/*     */ 
/*     */   public Vector argsType() {
/* 167 */     return this._argsType;
/*     */   }
/*     */ 
/*     */   public int argsCount() {
/* 171 */     return this._argsType == null ? 0 : this._argsType.size();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType
 * JD-Core Version:    0.6.2
 */