/*    */ package com.sun.org.apache.xerces.internal.dom;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.xs.XSImplementationImpl;
/*    */ import java.util.Vector;
/*    */ import org.w3c.dom.DOMImplementation;
/*    */ import org.w3c.dom.DOMImplementationList;
/*    */ 
/*    */ public class DOMXSImplementationSourceImpl extends DOMImplementationSourceImpl
/*    */ {
/*    */   public DOMImplementation getDOMImplementation(String features)
/*    */   {
/* 50 */     DOMImplementation impl = super.getDOMImplementation(features);
/* 51 */     if (impl != null) {
/* 52 */       return impl;
/*    */     }
/*    */ 
/* 55 */     impl = PSVIDOMImplementationImpl.getDOMImplementation();
/* 56 */     if (testImpl(impl, features)) {
/* 57 */       return impl;
/*    */     }
/*    */ 
/* 60 */     impl = XSImplementationImpl.getDOMImplementation();
/* 61 */     if (testImpl(impl, features)) {
/* 62 */       return impl;
/*    */     }
/*    */ 
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   public DOMImplementationList getDOMImplementationList(String features)
/*    */   {
/* 80 */     Vector implementations = new Vector();
/*    */ 
/* 83 */     DOMImplementationList list = super.getDOMImplementationList(features);
/*    */ 
/* 85 */     for (int i = 0; i < list.getLength(); i++) {
/* 86 */       implementations.addElement(list.item(i));
/*    */     }
/*    */ 
/* 89 */     DOMImplementation impl = PSVIDOMImplementationImpl.getDOMImplementation();
/* 90 */     if (testImpl(impl, features)) {
/* 91 */       implementations.addElement(impl);
/*    */     }
/*    */ 
/* 94 */     impl = XSImplementationImpl.getDOMImplementation();
/* 95 */     if (testImpl(impl, features)) {
/* 96 */       implementations.addElement(impl);
/*    */     }
/* 98 */     return new DOMImplementationListImpl(implementations);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMXSImplementationSourceImpl
 * JD-Core Version:    0.6.2
 */