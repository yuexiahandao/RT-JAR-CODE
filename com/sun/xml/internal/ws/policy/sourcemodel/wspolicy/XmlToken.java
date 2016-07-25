/*    */ package com.sun.xml.internal.ws.policy.sourcemodel.wspolicy;
/*    */ 
/*    */ public enum XmlToken
/*    */ {
/* 33 */   Policy("Policy", true), 
/* 34 */   ExactlyOne("ExactlyOne", true), 
/* 35 */   All("All", true), 
/* 36 */   PolicyReference("PolicyReference", true), 
/* 37 */   UsingPolicy("UsingPolicy", true), 
/* 38 */   Name("Name", false), 
/* 39 */   Optional("Optional", false), 
/* 40 */   Ignorable("Ignorable", false), 
/* 41 */   PolicyUris("PolicyURIs", false), 
/* 42 */   Uri("URI", false), 
/* 43 */   Digest("Digest", false), 
/* 44 */   DigestAlgorithm("DigestAlgorithm", false), 
/*    */ 
/* 46 */   UNKNOWN("", true);
/*    */ 
/*    */   private String tokenName;
/*    */   private boolean element;
/*    */ 
/*    */   public static XmlToken resolveToken(String name)
/*    */   {
/* 59 */     for (XmlToken token : values()) {
/* 60 */       if (token.toString().equals(name)) {
/* 61 */         return token;
/*    */       }
/*    */     }
/*    */ 
/* 65 */     return UNKNOWN;
/*    */   }
/*    */ 
/*    */   private XmlToken(String name, boolean element)
/*    */   {
/* 72 */     this.tokenName = name;
/* 73 */     this.element = element;
/*    */   }
/*    */ 
/*    */   public boolean isElement() {
/* 77 */     return this.element;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 82 */     return this.tokenName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken
 * JD-Core Version:    0.6.2
 */