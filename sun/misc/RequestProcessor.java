/*    */ package sun.misc;
/*    */ 
/*    */ public class RequestProcessor
/*    */   implements Runnable
/*    */ {
/*    */   private static Queue requestQueue;
/*    */   private static Thread dispatcher;
/*    */ 
/*    */   public static void postRequest(Request paramRequest)
/*    */   {
/* 47 */     lazyInitialize();
/* 48 */     requestQueue.enqueue(paramRequest);
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 55 */     lazyInitialize();
/*    */     while (true)
/*    */       try {
/* 58 */         Object localObject = requestQueue.dequeue();
/* 59 */         if ((localObject instanceof Request)) {
/* 60 */           Request localRequest = (Request)localObject;
/*    */           try {
/* 62 */             localRequest.execute();
/*    */           }
/*    */           catch (Throwable localThrowable)
/*    */           {
/*    */           }
/*    */         }
/*    */       }
/*    */       catch (InterruptedException localInterruptedException)
/*    */       {
/*    */       }
/*    */   }
/*    */ 
/*    */   public static synchronized void startProcessing()
/*    */   {
/* 82 */     if (dispatcher == null) {
/* 83 */       dispatcher = new Thread(new RequestProcessor(), "Request Processor");
/* 84 */       dispatcher.setPriority(7);
/* 85 */       dispatcher.start();
/*    */     }
/*    */   }
/*    */ 
/*    */   private static synchronized void lazyInitialize()
/*    */   {
/* 94 */     if (requestQueue == null)
/* 95 */       requestQueue = new Queue();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.RequestProcessor
 * JD-Core Version:    0.6.2
 */