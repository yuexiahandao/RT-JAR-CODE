/*    */ package com.sun.xml.internal.messaging.saaj.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import javax.xml.soap.MimeHeader;
/*    */ import javax.xml.soap.MimeHeaders;
/*    */ 
/*    */ public class MimeHeadersUtil
/*    */ {
/*    */   public static MimeHeaders copy(MimeHeaders headers)
/*    */   {
/* 39 */     MimeHeaders newHeaders = new MimeHeaders();
/* 40 */     Iterator eachHeader = headers.getAllHeaders();
/* 41 */     while (eachHeader.hasNext()) {
/* 42 */       MimeHeader currentHeader = (MimeHeader)eachHeader.next();
/*    */ 
/* 44 */       newHeaders.addHeader(currentHeader.getName(), currentHeader.getValue());
/*    */     }
/*    */ 
/* 48 */     return newHeaders;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.MimeHeadersUtil
 * JD-Core Version:    0.6.2
 */