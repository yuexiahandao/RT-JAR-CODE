/*    */ package com.sun.org.apache.xerces.internal.impl.dv.dtd;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class DTDDVFactoryImpl extends DTDDVFactory
/*    */ {
/* 37 */   static Hashtable fBuiltInTypes = new Hashtable();
/*    */ 
/*    */   public DatatypeValidator getBuiltInDV(String name)
/*    */   {
/* 49 */     return (DatatypeValidator)fBuiltInTypes.get(name);
/*    */   }
/*    */ 
/*    */   public Hashtable getBuiltInTypes()
/*    */   {
/* 58 */     return (Hashtable)fBuiltInTypes.clone();
/*    */   }
/*    */ 
/*    */   static void createBuiltInTypes()
/*    */   {
/* 66 */     fBuiltInTypes.put("string", new StringDatatypeValidator());
/* 67 */     fBuiltInTypes.put("ID", new IDDatatypeValidator());
/* 68 */     DatatypeValidator dvTemp = new IDREFDatatypeValidator();
/* 69 */     fBuiltInTypes.put("IDREF", dvTemp);
/* 70 */     fBuiltInTypes.put("IDREFS", new ListDatatypeValidator(dvTemp));
/* 71 */     dvTemp = new ENTITYDatatypeValidator();
/* 72 */     fBuiltInTypes.put("ENTITY", new ENTITYDatatypeValidator());
/* 73 */     fBuiltInTypes.put("ENTITIES", new ListDatatypeValidator(dvTemp));
/* 74 */     fBuiltInTypes.put("NOTATION", new NOTATIONDatatypeValidator());
/* 75 */     dvTemp = new NMTOKENDatatypeValidator();
/* 76 */     fBuiltInTypes.put("NMTOKEN", dvTemp);
/* 77 */     fBuiltInTypes.put("NMTOKENS", new ListDatatypeValidator(dvTemp));
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 39 */     createBuiltInTypes();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.dtd.DTDDVFactoryImpl
 * JD-Core Version:    0.6.2
 */