/*    */ package com.sun.org.apache.xerces.internal.dom;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import org.w3c.dom.DOMImplementation;
/*    */ import org.w3c.dom.DOMImplementationList;
/*    */ 
/*    */ public class DOMImplementationListImpl
/*    */   implements DOMImplementationList
/*    */ {
/*    */   private Vector fImplementations;
/*    */ 
/*    */   public DOMImplementationListImpl()
/*    */   {
/* 44 */     this.fImplementations = new Vector();
/*    */   }
/*    */ 
/*    */   public DOMImplementationListImpl(Vector params)
/*    */   {
/* 51 */     this.fImplementations = params;
/*    */   }
/*    */ 
/*    */   public DOMImplementation item(int index)
/*    */   {
/*    */     try
/*    */     {
/* 61 */       return (DOMImplementation)this.fImplementations.elementAt(index); } catch (ArrayIndexOutOfBoundsException e) {
/*    */     }
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   public int getLength()
/*    */   {
/* 73 */     return this.fImplementations.size();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMImplementationListImpl
 * JD-Core Version:    0.6.2
 */