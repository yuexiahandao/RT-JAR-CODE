/*    */ package sun.security.x509;
/*    */ 
/*    */ public class X509AttributeName
/*    */ {
/*    */   private static final char SEPARATOR = '.';
/* 39 */   private String prefix = null;
/* 40 */   private String suffix = null;
/*    */ 
/*    */   public X509AttributeName(String paramString)
/*    */   {
/* 49 */     int i = paramString.indexOf('.');
/* 50 */     if (i == -1) {
/* 51 */       this.prefix = paramString;
/*    */     } else {
/* 53 */       this.prefix = paramString.substring(0, i);
/* 54 */       this.suffix = paramString.substring(i + 1);
/*    */     }
/*    */   }
/*    */ 
/*    */   public String getPrefix()
/*    */   {
/* 62 */     return this.prefix;
/*    */   }
/*    */ 
/*    */   public String getSuffix()
/*    */   {
/* 69 */     return this.suffix;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.X509AttributeName
 * JD-Core Version:    0.6.2
 */