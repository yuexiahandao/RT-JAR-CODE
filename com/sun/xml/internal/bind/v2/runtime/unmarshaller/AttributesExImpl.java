/*    */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import com.sun.xml.internal.bind.util.AttributesImpl;
/*    */ 
/*    */ public final class AttributesExImpl extends AttributesImpl
/*    */   implements AttributesEx
/*    */ {
/*    */   public CharSequence getData(int idx)
/*    */   {
/* 39 */     return getValue(idx);
/*    */   }
/*    */ 
/*    */   public CharSequence getData(String nsUri, String localName) {
/* 43 */     return getValue(nsUri, localName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.AttributesExImpl
 * JD-Core Version:    0.6.2
 */