/*    */ package com.sun.org.apache.xpath.internal.objects;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ 
/*    */ public final class DTMXRTreeFrag
/*    */ {
/*    */   private DTM m_dtm;
/* 34 */   private int m_dtmIdentity = -1;
/*    */   private XPathContext m_xctxt;
/*    */ 
/*    */   public DTMXRTreeFrag(int dtmIdentity, XPathContext xctxt)
/*    */   {
/* 38 */     this.m_xctxt = xctxt;
/* 39 */     this.m_dtmIdentity = dtmIdentity;
/* 40 */     this.m_dtm = xctxt.getDTM(dtmIdentity);
/*    */   }
/*    */ 
/*    */   public final void destruct() {
/* 44 */     this.m_dtm = null;
/* 45 */     this.m_xctxt = null;
/*    */   }
/*    */   final DTM getDTM() {
/* 48 */     return this.m_dtm; } 
/* 49 */   public final int getDTMIdentity() { return this.m_dtmIdentity; } 
/* 50 */   final XPathContext getXPathContext() { return this.m_xctxt; } 
/*    */   public final int hashCode() {
/* 52 */     return this.m_dtmIdentity;
/*    */   }
/* 54 */   public final boolean equals(Object obj) { if ((obj instanceof DTMXRTreeFrag)) {
/* 55 */       return this.m_dtmIdentity == ((DTMXRTreeFrag)obj).getDTMIdentity();
/*    */     }
/* 57 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.DTMXRTreeFrag
 * JD-Core Version:    0.6.2
 */