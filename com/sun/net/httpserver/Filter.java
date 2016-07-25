/*    */ package com.sun.net.httpserver;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import java.util.ListIterator;
/*    */ 
/*    */ public abstract class Filter
/*    */ {
/*    */   public abstract void doFilter(HttpExchange paramHttpExchange, Chain paramChain)
/*    */     throws IOException;
/*    */ 
/*    */   public abstract String description();
/*    */ 
/*    */   public static class Chain
/*    */   {
/*    */     private ListIterator<Filter> iter;
/*    */     private HttpHandler handler;
/*    */ 
/*    */     public Chain(List<Filter> paramList, HttpHandler paramHttpHandler)
/*    */     {
/* 59 */       this.iter = paramList.listIterator();
/* 60 */       this.handler = paramHttpHandler;
/*    */     }
/*    */ 
/*    */     public void doFilter(HttpExchange paramHttpExchange)
/*    */       throws IOException
/*    */     {
/* 76 */       if (!this.iter.hasNext()) {
/* 77 */         this.handler.handle(paramHttpExchange);
/*    */       } else {
/* 79 */         Filter localFilter = (Filter)this.iter.next();
/* 80 */         localFilter.doFilter(paramHttpExchange, this);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.Filter
 * JD-Core Version:    0.6.2
 */