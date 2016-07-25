/*    */ package com.sun.org.apache.xerces.internal.impl.dv.dtd;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class XML11DTDDVFactoryImpl extends DTDDVFactoryImpl
/*    */ {
/* 38 */   static Hashtable fXML11BuiltInTypes = new Hashtable();
/*    */ 
/*    */   public DatatypeValidator getBuiltInDV(String name)
/*    */   {
/* 49 */     if (fXML11BuiltInTypes.get(name) != null) {
/* 50 */       return (DatatypeValidator)fXML11BuiltInTypes.get(name);
/*    */     }
/* 52 */     return (DatatypeValidator)fBuiltInTypes.get(name);
/*    */   }
/*    */ 
/*    */   public Hashtable getBuiltInTypes()
/*    */   {
/* 62 */     Hashtable toReturn = (Hashtable)fBuiltInTypes.clone();
/* 63 */     Enumeration xml11Keys = fXML11BuiltInTypes.keys();
/* 64 */     while (xml11Keys.hasMoreElements()) {
/* 65 */       Object key = xml11Keys.nextElement();
/* 66 */       toReturn.put(key, fXML11BuiltInTypes.get(key));
/*    */     }
/* 68 */     return toReturn;
/*    */   }
/*    */ 
/*    */   static {
/* 72 */     fXML11BuiltInTypes.put("XML11ID", new XML11IDDatatypeValidator());
/* 73 */     DatatypeValidator dvTemp = new XML11IDREFDatatypeValidator();
/* 74 */     fXML11BuiltInTypes.put("XML11IDREF", dvTemp);
/* 75 */     fXML11BuiltInTypes.put("XML11IDREFS", new ListDatatypeValidator(dvTemp));
/* 76 */     dvTemp = new XML11NMTOKENDatatypeValidator();
/* 77 */     fXML11BuiltInTypes.put("XML11NMTOKEN", dvTemp);
/* 78 */     fXML11BuiltInTypes.put("XML11NMTOKENS", new ListDatatypeValidator(dvTemp));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl
 * JD-Core Version:    0.6.2
 */