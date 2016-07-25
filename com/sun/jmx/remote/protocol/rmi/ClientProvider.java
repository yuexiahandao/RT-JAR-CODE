/*    */ package com.sun.jmx.remote.protocol.rmi;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.util.Map;
/*    */ import javax.management.remote.JMXConnector;
/*    */ import javax.management.remote.JMXConnectorProvider;
/*    */ import javax.management.remote.JMXServiceURL;
/*    */ import javax.management.remote.rmi.RMIConnector;
/*    */ 
/*    */ public class ClientProvider
/*    */   implements JMXConnectorProvider
/*    */ {
/*    */   public JMXConnector newJMXConnector(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap)
/*    */     throws IOException
/*    */   {
/* 42 */     if (!paramJMXServiceURL.getProtocol().equals("rmi")) {
/* 43 */       throw new MalformedURLException("Protocol not rmi: " + paramJMXServiceURL.getProtocol());
/*    */     }
/*    */ 
/* 46 */     return new RMIConnector(paramJMXServiceURL, paramMap);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.protocol.rmi.ClientProvider
 * JD-Core Version:    0.6.2
 */