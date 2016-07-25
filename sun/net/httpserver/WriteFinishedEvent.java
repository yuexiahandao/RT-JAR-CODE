/*    */ package sun.net.httpserver;
/*    */ 
/*    */ class WriteFinishedEvent extends Event
/*    */ {
/*    */   WriteFinishedEvent(ExchangeImpl paramExchangeImpl)
/*    */   {
/* 42 */     super(paramExchangeImpl);
/* 43 */     assert (!paramExchangeImpl.writefinished);
/* 44 */     paramExchangeImpl.writefinished = true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.WriteFinishedEvent
 * JD-Core Version:    0.6.2
 */