/*    */ package sun.applet;
/*    */ 
/*    */ public class AppletSecurityException extends SecurityException
/*    */ {
/* 35 */   private String key = null;
/* 36 */   private Object[] msgobj = null;
/*    */ 
/* 63 */   private static AppletMessageHandler amh = new AppletMessageHandler("appletsecurityexception");
/*    */ 
/*    */   public AppletSecurityException(String paramString)
/*    */   {
/* 39 */     super(paramString);
/* 40 */     this.key = paramString;
/*    */   }
/*    */ 
/*    */   public AppletSecurityException(String paramString1, String paramString2) {
/* 44 */     this(paramString1);
/* 45 */     this.msgobj = new Object[1];
/* 46 */     this.msgobj[0] = paramString2;
/*    */   }
/*    */ 
/*    */   public AppletSecurityException(String paramString1, String paramString2, String paramString3) {
/* 50 */     this(paramString1);
/* 51 */     this.msgobj = new Object[2];
/* 52 */     this.msgobj[0] = paramString2;
/* 53 */     this.msgobj[1] = paramString3;
/*    */   }
/*    */ 
/*    */   public String getLocalizedMessage() {
/* 57 */     if (this.msgobj != null) {
/* 58 */       return amh.getMessage(this.key, this.msgobj);
/*    */     }
/* 60 */     return amh.getMessage(this.key);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletSecurityException
 * JD-Core Version:    0.6.2
 */