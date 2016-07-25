/*    */ package java.nio.charset.spi;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public abstract class CharsetProvider
/*    */ {
/*    */   protected CharsetProvider()
/*    */   {
/* 82 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 83 */     if (localSecurityManager != null)
/* 84 */       localSecurityManager.checkPermission(new RuntimePermission("charsetProvider"));
/*    */   }
/*    */ 
/*    */   public abstract Iterator<Charset> charsets();
/*    */ 
/*    */   public abstract Charset charsetForName(String paramString);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.charset.spi.CharsetProvider
 * JD-Core Version:    0.6.2
 */