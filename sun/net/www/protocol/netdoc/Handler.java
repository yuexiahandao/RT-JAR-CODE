/*    */ package sun.net.www.protocol.netdoc;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.net.URLStreamHandler;
/*    */ import java.security.AccessController;
/*    */ import sun.security.action.GetBooleanAction;
/*    */ import sun.security.action.GetPropertyAction;
/*    */ 
/*    */ public class Handler extends URLStreamHandler
/*    */ {
/*    */   static URL base;
/*    */ 
/*    */   public synchronized URLConnection openConnection(URL paramURL)
/*    */     throws IOException
/*    */   {
/* 54 */     URLConnection localURLConnection = null;
/*    */ 
/* 57 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new GetBooleanAction("newdoc.localonly"));
/*    */ 
/* 59 */     boolean bool = localBoolean.booleanValue();
/*    */ 
/* 61 */     String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("doc.url"));
/*    */ 
/* 64 */     String str2 = paramURL.getFile();
/*    */     URL localURL;
/* 65 */     if (!bool) {
/*    */       try {
/* 67 */         if (base == null) {
/* 68 */           base = new URL(str1);
/*    */         }
/* 70 */         localURL = new URL(base, str2);
/*    */       } catch (MalformedURLException localMalformedURLException1) {
/* 72 */         localURL = null;
/*    */       }
/* 74 */       if (localURL != null) {
/* 75 */         localURLConnection = localURL.openConnection();
/*    */       }
/*    */     }
/*    */ 
/* 79 */     if (localURLConnection == null) {
/*    */       try {
/* 81 */         localURL = new URL("file", "~", str2);
/*    */ 
/* 83 */         localURLConnection = localURL.openConnection();
/* 84 */         InputStream localInputStream = localURLConnection.getInputStream();
/*    */       } catch (MalformedURLException localMalformedURLException2) {
/* 86 */         localURLConnection = null;
/*    */       } catch (IOException localIOException) {
/* 88 */         localURLConnection = null;
/*    */       }
/*    */     }
/*    */ 
/* 92 */     if (localURLConnection == null) {
/* 93 */       throw new IOException("Can't find file for URL: " + paramURL.toExternalForm());
/*    */     }
/*    */ 
/* 96 */     return localURLConnection;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.netdoc.Handler
 * JD-Core Version:    0.6.2
 */