/*    */ package sun.applet;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class AppletIOException extends IOException
/*    */ {
/* 37 */   private String key = null;
/* 38 */   private Object msgobj = null;
/*    */ 
/* 57 */   private static AppletMessageHandler amh = new AppletMessageHandler("appletioexception");
/*    */ 
/*    */   public AppletIOException(String paramString)
/*    */   {
/* 41 */     super(paramString);
/* 42 */     this.key = paramString;
/*    */   }
/*    */ 
/*    */   public AppletIOException(String paramString, Object paramObject) {
/* 46 */     this(paramString);
/* 47 */     this.msgobj = paramObject;
/*    */   }
/*    */ 
/*    */   public String getLocalizedMessage() {
/* 51 */     if (this.msgobj != null) {
/* 52 */       return amh.getMessage(this.key, this.msgobj);
/*    */     }
/* 54 */     return amh.getMessage(this.key);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletIOException
 * JD-Core Version:    0.6.2
 */