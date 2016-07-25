/*    */ package com.sun.xml.internal.bind.v2.runtime.property;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*    */ 
/*    */ public final class UnmarshallerChain
/*    */ {
/* 48 */   private int offset = 0;
/*    */   public final JAXBContextImpl context;
/*    */ 
/*    */   public UnmarshallerChain(JAXBContextImpl context)
/*    */   {
/* 53 */     this.context = context;
/*    */   }
/*    */ 
/*    */   public int allocateOffset()
/*    */   {
/* 60 */     return this.offset++;
/*    */   }
/*    */ 
/*    */   public int getScopeSize()
/*    */   {
/* 67 */     return this.offset;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.UnmarshallerChain
 * JD-Core Version:    0.6.2
 */