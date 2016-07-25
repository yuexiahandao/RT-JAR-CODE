/*    */ package com.sun.org.apache.xml.internal.utils;
/*    */ 
/*    */ public class NSInfo
/*    */ {
/*    */   public String m_namespace;
/*    */   public boolean m_hasXMLNSAttrs;
/*    */   public boolean m_hasProcessedNS;
/*    */   public int m_ancestorHasXMLNSAttrs;
/*    */   public static final int ANCESTORXMLNSUNPROCESSED = 0;
/*    */   public static final int ANCESTORHASXMLNS = 1;
/*    */   public static final int ANCESTORNOXMLNS = 2;
/*    */ 
/*    */   public NSInfo(boolean hasProcessedNS, boolean hasXMLNSAttrs)
/*    */   {
/* 46 */     this.m_hasProcessedNS = hasProcessedNS;
/* 47 */     this.m_hasXMLNSAttrs = hasXMLNSAttrs;
/* 48 */     this.m_namespace = null;
/* 49 */     this.m_ancestorHasXMLNSAttrs = 0;
/*    */   }
/*    */ 
/*    */   public NSInfo(boolean hasProcessedNS, boolean hasXMLNSAttrs, int ancestorHasXMLNSAttrs)
/*    */   {
/* 69 */     this.m_hasProcessedNS = hasProcessedNS;
/* 70 */     this.m_hasXMLNSAttrs = hasXMLNSAttrs;
/* 71 */     this.m_ancestorHasXMLNSAttrs = ancestorHasXMLNSAttrs;
/* 72 */     this.m_namespace = null;
/*    */   }
/*    */ 
/*    */   public NSInfo(String namespace, boolean hasXMLNSAttrs)
/*    */   {
/* 86 */     this.m_hasProcessedNS = true;
/* 87 */     this.m_hasXMLNSAttrs = hasXMLNSAttrs;
/* 88 */     this.m_namespace = namespace;
/* 89 */     this.m_ancestorHasXMLNSAttrs = 0;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.NSInfo
 * JD-Core Version:    0.6.2
 */