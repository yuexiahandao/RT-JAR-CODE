/*    */ package com.sun.xml.internal.ws.transport.http.server;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*    */ import com.sun.xml.internal.ws.transport.http.HttpAdapterList;
/*    */ 
/*    */ public class ServerAdapterList extends HttpAdapterList<ServerAdapter>
/*    */ {
/*    */   protected ServerAdapter createHttpAdapter(String name, String urlPattern, WSEndpoint<?> endpoint)
/*    */   {
/* 34 */     return new ServerAdapter(name, urlPattern, endpoint, this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.ServerAdapterList
 * JD-Core Version:    0.6.2
 */