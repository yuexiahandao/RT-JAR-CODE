/*    */ package com.sun.xml.internal.bind;
/*    */ 
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public final class AnyTypeAdapter extends XmlAdapter<Object, Object>
/*    */ {
/*    */   public Object unmarshal(Object v)
/*    */   {
/* 44 */     return v;
/*    */   }
/*    */ 
/*    */   public Object marshal(Object v)
/*    */   {
/* 51 */     return v;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.AnyTypeAdapter
 * JD-Core Version:    0.6.2
 */