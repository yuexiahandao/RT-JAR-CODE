/*    */ package com.sun.jmx.snmp.daemon;
/*    */ 
/*    */ import com.sun.jmx.defaults.JmxProperties;
/*    */ import com.sun.jmx.snmp.SnmpMessage;
/*    */ import com.sun.jmx.snmp.SnmpPduFactory;
/*    */ import com.sun.jmx.snmp.SnmpPduPacket;
/*    */ import com.sun.jmx.snmp.SnmpPduRequest;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.InetAddress;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ class SnmpResponseHandler
/*    */ {
/* 33 */   SnmpAdaptorServer adaptor = null;
/* 34 */   SnmpQManager snmpq = null;
/*    */ 
/*    */   public SnmpResponseHandler(SnmpAdaptorServer paramSnmpAdaptorServer, SnmpQManager paramSnmpQManager)
/*    */   {
/* 40 */     this.adaptor = paramSnmpAdaptorServer;
/* 41 */     this.snmpq = paramSnmpQManager;
/*    */   }
/*    */ 
/*    */   public synchronized void processDatagram(DatagramPacket paramDatagramPacket)
/*    */   {
/* 49 */     byte[] arrayOfByte = paramDatagramPacket.getData();
/* 50 */     int i = paramDatagramPacket.getLength();
/*    */ 
/* 52 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 53 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpResponseHandler.class.getName(), "action", "processDatagram", "Received from " + paramDatagramPacket.getAddress().toString() + " Length = " + i + "\nDump : \n" + SnmpMessage.dumpHexBuffer(arrayOfByte, 0, i));
/*    */     }
/*    */ 
/*    */     try
/*    */     {
/* 59 */       SnmpMessage localSnmpMessage = new SnmpMessage();
/* 60 */       localSnmpMessage.decodeMessage(arrayOfByte, i);
/* 61 */       localSnmpMessage.address = paramDatagramPacket.getAddress();
/* 62 */       localSnmpMessage.port = paramDatagramPacket.getPort();
/*    */ 
/* 66 */       SnmpPduFactory localSnmpPduFactory = this.adaptor.getPduFactory();
/* 67 */       if (localSnmpPduFactory == null) {
/* 68 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 69 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpResponseHandler.class.getName(), "processDatagram", "Dropping packet. Unable to find the pdu factory of the SNMP adaptor server");
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 74 */         SnmpPduPacket localSnmpPduPacket = (SnmpPduPacket)localSnmpPduFactory.decodeSnmpPdu(localSnmpMessage);
/*    */ 
/* 76 */         if (localSnmpPduPacket == null) {
/* 77 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 78 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpResponseHandler.class.getName(), "processDatagram", "Dropping packet. Pdu factory returned a null value");
/*    */           }
/*    */ 
/*    */         }
/* 82 */         else if ((localSnmpPduPacket instanceof SnmpPduRequest))
/*    */         {
/* 84 */           SnmpPduRequest localSnmpPduRequest = (SnmpPduRequest)localSnmpPduPacket;
/* 85 */           SnmpInformRequest localSnmpInformRequest = this.snmpq.removeRequest(localSnmpPduRequest.requestId);
/* 86 */           if (localSnmpInformRequest != null) {
/* 87 */             localSnmpInformRequest.invokeOnResponse(localSnmpPduRequest);
/*    */           }
/* 89 */           else if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 90 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpResponseHandler.class.getName(), "processDatagram", "Dropping packet. Unable to find corresponding for InformRequestId = " + localSnmpPduRequest.requestId);
/*    */           }
/*    */ 
/*    */         }
/* 96 */         else if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 97 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpResponseHandler.class.getName(), "processDatagram", "Dropping packet. The packet does not contain an inform response");
/*    */         }
/*    */ 
/* 101 */         localSnmpPduPacket = null;
/*    */       }
/*    */     } catch (Exception localException) {
/* 104 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/* 105 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpResponseHandler.class.getName(), "processDatagram", "Exception while processsing", localException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpResponseHandler
 * JD-Core Version:    0.6.2
 */