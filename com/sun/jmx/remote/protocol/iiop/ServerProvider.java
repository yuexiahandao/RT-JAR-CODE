/*    */ package com.sun.jmx.remote.protocol.iiop;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.util.Map;
/*    */ import javax.management.MBeanServer;
/*    */ import javax.management.remote.JMXConnectorServer;
/*    */ import javax.management.remote.JMXConnectorServerProvider;
/*    */ import javax.management.remote.JMXServiceURL;
/*    */ import javax.management.remote.rmi.RMIConnectorServer;
/*    */ 
/*    */ public class ServerProvider
/*    */   implements JMXConnectorServerProvider
/*    */ {
/*    */   public JMXConnectorServer newJMXConnectorServer(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap, MBeanServer paramMBeanServer)
/*    */     throws IOException
/*    */   {
/* 44 */     if (!paramJMXServiceURL.getProtocol().equals("iiop")) {
/* 45 */       throw new MalformedURLException("Protocol not iiop: " + paramJMXServiceURL.getProtocol());
/*    */     }
/*    */ 
/* 48 */     return new RMIConnectorServer(paramJMXServiceURL, paramMap, paramMBeanServer);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.protocol.iiop.ServerProvider
 * JD-Core Version:    0.6.2
 */