/*    */ package com.sun.xml.internal.ws.encoding;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.istack.internal.Nullable;
/*    */ 
/*    */ public final class ContentTypeImpl
/*    */   implements com.sun.xml.internal.ws.api.pipe.ContentType
/*    */ {
/*    */ 
/*    */   @NotNull
/*    */   private final String contentType;
/*    */ 
/*    */   @NotNull
/*    */   private final String soapAction;
/*    */ 
/*    */   @Nullable
/*    */   private final String accept;
/*    */ 
/*    */   @Nullable
/*    */   private final String charset;
/*    */ 
/*    */   public ContentTypeImpl(String contentType)
/*    */   {
/* 41 */     this(contentType, null, null);
/*    */   }
/*    */ 
/*    */   public ContentTypeImpl(String contentType, @Nullable String soapAction) {
/* 45 */     this(contentType, soapAction, null);
/*    */   }
/*    */ 
/*    */   public ContentTypeImpl(String contentType, @Nullable String soapAction, @Nullable String accept) {
/* 49 */     this.contentType = contentType;
/* 50 */     this.accept = accept;
/* 51 */     this.soapAction = getQuotedSOAPAction(soapAction);
/* 52 */     String tmpCharset = null;
/*    */     try {
/* 54 */       tmpCharset = new ContentType(contentType).getParameter("charset");
/*    */     }
/*    */     catch (Exception e) {
/*    */     }
/* 58 */     this.charset = tmpCharset;
/*    */   }
/*    */ 
/*    */   @Nullable
/*    */   public String getCharSet()
/*    */   {
/* 67 */     return this.charset;
/*    */   }
/*    */ 
/*    */   private String getQuotedSOAPAction(String soapAction)
/*    */   {
/* 72 */     if ((soapAction == null) || (soapAction.length() == 0))
/* 73 */       return "\"\"";
/* 74 */     if ((soapAction.charAt(0) != '"') && (soapAction.charAt(soapAction.length() - 1) != '"'))
/*    */     {
/* 76 */       return "\"" + soapAction + "\"";
/*    */     }
/* 78 */     return soapAction;
/*    */   }
/*    */ 
/*    */   public String getContentType()
/*    */   {
/* 83 */     return this.contentType;
/*    */   }
/*    */ 
/*    */   public String getSOAPActionHeader() {
/* 87 */     return this.soapAction;
/*    */   }
/*    */ 
/*    */   public String getAcceptHeader() {
/* 91 */     return this.accept;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.ContentTypeImpl
 * JD-Core Version:    0.6.2
 */