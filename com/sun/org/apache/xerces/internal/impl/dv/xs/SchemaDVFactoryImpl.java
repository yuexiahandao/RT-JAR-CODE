/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*    */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*    */ 
/*    */ public class SchemaDVFactoryImpl extends BaseSchemaDVFactory
/*    */ {
/* 39 */   static final SymbolHash fBuiltInTypes = new SymbolHash();
/*    */ 
/*    */   static void createBuiltInTypes()
/*    */   {
/* 47 */     createBuiltInTypes(fBuiltInTypes, XSSimpleTypeDecl.fAnySimpleType);
/*    */   }
/*    */ 
/*    */   public XSSimpleType getBuiltInType(String name)
/*    */   {
/* 64 */     return (XSSimpleType)fBuiltInTypes.get(name);
/*    */   }
/*    */ 
/*    */   public SymbolHash getBuiltInTypes()
/*    */   {
/* 74 */     return fBuiltInTypes.makeClone();
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 42 */     createBuiltInTypes();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.SchemaDVFactoryImpl
 * JD-Core Version:    0.6.2
 */