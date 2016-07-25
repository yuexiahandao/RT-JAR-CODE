/*    */ package com.sun.xml.internal.bind.v2.model.core;
/*    */ 
/*    */ public enum PropertyKind
/*    */ {
/* 44 */   VALUE(true, false, 2147483647), 
/* 45 */   ATTRIBUTE(false, false, 2147483647), 
/* 46 */   ELEMENT(true, true, 0), 
/* 47 */   REFERENCE(false, true, 1), 
/* 48 */   MAP(false, true, 2);
/*    */ 
/*    */   public final boolean canHaveXmlMimeType;
/*    */   public final boolean isOrdered;
/*    */   public final int propertyIndex;
/*    */ 
/*    */   private PropertyKind(boolean canHaveExpectedContentType, boolean isOrdered, int propertyIndex)
/*    */   {
/* 69 */     this.canHaveXmlMimeType = canHaveExpectedContentType;
/* 70 */     this.isOrdered = isOrdered;
/* 71 */     this.propertyIndex = propertyIndex;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.PropertyKind
 * JD-Core Version:    0.6.2
 */