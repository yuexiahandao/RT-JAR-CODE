/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ 
/*     */ public abstract class Type
/*     */   implements Constants
/*     */ {
/*  38 */   public static final Type Int = new IntType();
/*  39 */   public static final Type Real = new RealType();
/*  40 */   public static final Type Boolean = new BooleanType();
/*  41 */   public static final Type NodeSet = new NodeSetType();
/*  42 */   public static final Type String = new StringType();
/*  43 */   public static final Type ResultTree = new ResultTreeType();
/*  44 */   public static final Type Reference = new ReferenceType();
/*  45 */   public static final Type Void = new VoidType();
/*     */ 
/*  47 */   public static final Type Object = new ObjectType(Object.class);
/*  48 */   public static final Type ObjectString = new ObjectType(String.class);
/*     */ 
/*  50 */   public static final Type Node = new NodeType(-1);
/*  51 */   public static final Type Root = new NodeType(9);
/*  52 */   public static final Type Element = new NodeType(1);
/*  53 */   public static final Type Attribute = new NodeType(2);
/*  54 */   public static final Type Text = new NodeType(3);
/*  55 */   public static final Type Comment = new NodeType(8);
/*  56 */   public static final Type Processing_Instruction = new NodeType(7);
/*     */ 
/*     */   public static Type newObjectType(String javaClassName)
/*     */   {
/*  63 */     if (javaClassName == "java.lang.Object") {
/*  64 */       return Object;
/*     */     }
/*  66 */     if (javaClassName == "java.lang.String") {
/*  67 */       return ObjectString;
/*     */     }
/*     */ 
/*  71 */     AccessControlContext acc = AccessController.getContext();
/*  72 */     acc.checkPermission(new RuntimePermission("getContextClassLoader"));
/*  73 */     return new ObjectType(javaClassName);
/*     */   }
/*     */ 
/*     */   public static Type newObjectType(Class clazz)
/*     */   {
/*  82 */     if (clazz == Object.class) {
/*  83 */       return Object;
/*     */     }
/*  85 */     if (clazz == String.class) {
/*  86 */       return ObjectString;
/*     */     }
/*     */ 
/*  89 */     return new ObjectType(clazz);
/*     */   }
/*     */ 
/*     */   public abstract String toString();
/*     */ 
/*     */   public abstract boolean identicalTo(Type paramType);
/*     */ 
/*     */   public boolean isNumber()
/*     */   {
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean implementedAsMethod()
/*     */   {
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isSimple()
/*     */   {
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract com.sun.org.apache.bcel.internal.generic.Type toJCType();
/*     */ 
/*     */   public int distanceTo(Type type)
/*     */   {
/* 134 */     return type == this ? 0 : 2147483647;
/*     */   }
/*     */ 
/*     */   public abstract String toSignature();
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/* 149 */     ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/* 151 */     classGen.getParser().reportError(2, err);
/*     */   }
/*     */ 
/*     */   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/* 163 */     FlowList fl = null;
/* 164 */     if (type == Boolean) {
/* 165 */       fl = translateToDesynthesized(classGen, methodGen, (BooleanType)type);
/*     */     }
/*     */     else
/*     */     {
/* 169 */       translateTo(classGen, methodGen, type);
/*     */     }
/* 171 */     return fl;
/*     */   }
/*     */ 
/*     */   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 182 */     ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/* 184 */     classGen.getParser().reportError(2, err);
/* 185 */     return null;
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 195 */     ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getClass().toString());
/*     */ 
/* 197 */     classGen.getParser().reportError(2, err);
/*     */   }
/*     */ 
/*     */   public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 207 */     ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", clazz.getClass().toString(), toString());
/*     */ 
/* 209 */     classGen.getParser().reportError(2, err);
/*     */   }
/*     */ 
/*     */   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 217 */     ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), "[" + toString() + "]");
/*     */ 
/* 219 */     classGen.getParser().reportError(2, err);
/*     */   }
/*     */ 
/*     */   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 227 */     ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", "[" + toString() + "]", toString());
/*     */ 
/* 229 */     classGen.getParser().reportError(2, err);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 236 */     return "";
/*     */   }
/*     */ 
/*     */   public Instruction ADD() {
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction SUB() {
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction MUL() {
/* 248 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction DIV() {
/* 252 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction REM() {
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction NEG() {
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction LOAD(int slot) {
/* 264 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction STORE(int slot) {
/* 268 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction POP() {
/* 272 */     return POP;
/*     */   }
/*     */ 
/*     */   public BranchInstruction GT(boolean tozero) {
/* 276 */     return null;
/*     */   }
/*     */ 
/*     */   public BranchInstruction GE(boolean tozero) {
/* 280 */     return null;
/*     */   }
/*     */ 
/*     */   public BranchInstruction LT(boolean tozero) {
/* 284 */     return null;
/*     */   }
/*     */ 
/*     */   public BranchInstruction LE(boolean tozero) {
/* 288 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction CMP(boolean less) {
/* 292 */     return null;
/*     */   }
/*     */ 
/*     */   public Instruction DUP() {
/* 296 */     return DUP;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
 * JD-Core Version:    0.6.2
 */