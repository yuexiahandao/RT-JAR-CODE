/*    */ package sun.net.httpserver;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ class ContextList
/*    */ {
/*    */   static final int MAX_CONTEXTS = 50;
/* 36 */   LinkedList<HttpContextImpl> list = new LinkedList();
/*    */ 
/*    */   public synchronized void add(HttpContextImpl paramHttpContextImpl) {
/* 39 */     assert (paramHttpContextImpl.getPath() != null);
/* 40 */     this.list.add(paramHttpContextImpl);
/*    */   }
/*    */ 
/*    */   public synchronized int size() {
/* 44 */     return this.list.size();
/*    */   }
/*    */ 
/*    */   synchronized HttpContextImpl findContext(String paramString1, String paramString2)
/*    */   {
/* 51 */     return findContext(paramString1, paramString2, false);
/*    */   }
/*    */ 
/*    */   synchronized HttpContextImpl findContext(String paramString1, String paramString2, boolean paramBoolean) {
/* 55 */     paramString1 = paramString1.toLowerCase();
/* 56 */     Object localObject1 = "";
/* 57 */     Object localObject2 = null;
/* 58 */     for (HttpContextImpl localHttpContextImpl : this.list)
/* 59 */       if (localHttpContextImpl.getProtocol().equals(paramString1))
/*    */       {
/* 62 */         String str = localHttpContextImpl.getPath();
/* 63 */         if (((!paramBoolean) || (str.equals(paramString2))) && (
/* 65 */           (paramBoolean) || (paramString2.startsWith(str))))
/*    */         {
/* 68 */           if (str.length() > ((String)localObject1).length()) {
/* 69 */             localObject1 = str;
/* 70 */             localObject2 = localHttpContextImpl;
/*    */           }
/*    */         }
/*    */       }
/* 73 */     return localObject2;
/*    */   }
/*    */ 
/*    */   public synchronized void remove(String paramString1, String paramString2)
/*    */     throws IllegalArgumentException
/*    */   {
/* 79 */     HttpContextImpl localHttpContextImpl = findContext(paramString1, paramString2, true);
/* 80 */     if (localHttpContextImpl == null) {
/* 81 */       throw new IllegalArgumentException("cannot remove element from list");
/*    */     }
/* 83 */     this.list.remove(localHttpContextImpl);
/*    */   }
/*    */ 
/*    */   public synchronized void remove(HttpContextImpl paramHttpContextImpl)
/*    */     throws IllegalArgumentException
/*    */   {
/* 89 */     for (HttpContextImpl localHttpContextImpl : this.list) {
/* 90 */       if (localHttpContextImpl.equals(paramHttpContextImpl)) {
/* 91 */         this.list.remove(localHttpContextImpl);
/* 92 */         return;
/*    */       }
/*    */     }
/* 95 */     throw new IllegalArgumentException("no such context in list");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.ContextList
 * JD-Core Version:    0.6.2
 */