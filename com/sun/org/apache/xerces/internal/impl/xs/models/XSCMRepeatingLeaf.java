/*    */ package com.sun.org.apache.xerces.internal.impl.xs.models;
/*    */ 
/*    */ public final class XSCMRepeatingLeaf extends XSCMLeaf
/*    */ {
/*    */   private final int fMinOccurs;
/*    */   private final int fMaxOccurs;
/*    */ 
/*    */   public XSCMRepeatingLeaf(int type, Object leaf, int minOccurs, int maxOccurs, int id, int position)
/*    */   {
/* 39 */     super(type, leaf, id, position);
/* 40 */     this.fMinOccurs = minOccurs;
/* 41 */     this.fMaxOccurs = maxOccurs;
/*    */   }
/*    */ 
/*    */   final int getMinOccurs() {
/* 45 */     return this.fMinOccurs;
/*    */   }
/*    */ 
/*    */   final int getMaxOccurs() {
/* 49 */     return this.fMaxOccurs;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.models.XSCMRepeatingLeaf
 * JD-Core Version:    0.6.2
 */