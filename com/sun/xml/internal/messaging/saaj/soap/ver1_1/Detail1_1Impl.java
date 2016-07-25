/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.DetailEntry;
/*    */ import javax.xml.soap.Name;
/*    */ 
/*    */ public class Detail1_1Impl extends DetailImpl
/*    */ {
/*    */   public Detail1_1Impl(SOAPDocumentImpl ownerDoc, String prefix)
/*    */   {
/* 43 */     super(ownerDoc, NameImpl.createDetail1_1Name(prefix));
/*    */   }
/*    */   public Detail1_1Impl(SOAPDocumentImpl ownerDoc) {
/* 46 */     super(ownerDoc, NameImpl.createDetail1_1Name());
/*    */   }
/*    */   protected DetailEntry createDetailEntry(Name name) {
/* 49 */     return new DetailEntry1_1Impl((SOAPDocumentImpl)getOwnerDocument(), name);
/*    */   }
/*    */ 
/*    */   protected DetailEntry createDetailEntry(QName name)
/*    */   {
/* 54 */     return new DetailEntry1_1Impl((SOAPDocumentImpl)getOwnerDocument(), name);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.Detail1_1Impl
 * JD-Core Version:    0.6.2
 */