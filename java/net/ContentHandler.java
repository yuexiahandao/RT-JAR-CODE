/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class ContentHandler
/*     */ {
/*     */   public abstract Object getContent(URLConnection paramURLConnection)
/*     */     throws IOException;
/*     */ 
/*     */   public Object getContent(URLConnection paramURLConnection, Class[] paramArrayOfClass)
/*     */     throws IOException
/*     */   {
/* 100 */     Object localObject = getContent(paramURLConnection);
/*     */ 
/* 102 */     for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 103 */       if (paramArrayOfClass[i].isInstance(localObject)) {
/* 104 */         return localObject;
/*     */       }
/*     */     }
/* 107 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.ContentHandler
 * JD-Core Version:    0.6.2
 */