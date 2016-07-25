/*    */ package com.sun.xml.internal.stream.dtd.nonvalidating;
/*    */ 
/*    */ public class XMLNotationDecl
/*    */ {
/*    */   public String name;
/*    */   public String publicId;
/*    */   public String systemId;
/*    */   public String baseSystemId;
/*    */ 
/*    */   public void setValues(String name, String publicId, String systemId, String baseSystemId)
/*    */   {
/* 55 */     this.name = name;
/* 56 */     this.publicId = publicId;
/* 57 */     this.systemId = systemId;
/* 58 */     this.baseSystemId = baseSystemId;
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 65 */     this.name = null;
/* 66 */     this.publicId = null;
/* 67 */     this.systemId = null;
/* 68 */     this.baseSystemId = null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.dtd.nonvalidating.XMLNotationDecl
 * JD-Core Version:    0.6.2
 */