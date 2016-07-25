/*      */ package com.sun.jmx.snmp.daemon;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.snmp.InetAddressAcl;
/*      */ import com.sun.jmx.snmp.SnmpDefinitions;
/*      */ import com.sun.jmx.snmp.SnmpMessage;
/*      */ import com.sun.jmx.snmp.SnmpPduBulk;
/*      */ import com.sun.jmx.snmp.SnmpPduFactory;
/*      */ import com.sun.jmx.snmp.SnmpPduPacket;
/*      */ import com.sun.jmx.snmp.SnmpPduRequest;
/*      */ import com.sun.jmx.snmp.SnmpStatusException;
/*      */ import com.sun.jmx.snmp.SnmpTooBigException;
/*      */ import com.sun.jmx.snmp.SnmpVarBind;
/*      */ import com.sun.jmx.snmp.SnmpVarBindList;
/*      */ import com.sun.jmx.snmp.agent.SnmpMibAgent;
/*      */ import com.sun.jmx.snmp.agent.SnmpUserDataFactory;
/*      */ import java.io.InterruptedIOException;
/*      */ import java.net.DatagramPacket;
/*      */ import java.net.DatagramSocket;
/*      */ import java.net.SocketException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.ObjectName;
/*      */ 
/*      */ class SnmpRequestHandler extends ClientHandler
/*      */   implements SnmpDefinitions
/*      */ {
/*   74 */   private transient DatagramSocket socket = null;
/*   75 */   private transient DatagramPacket packet = null;
/*   76 */   private transient Vector mibs = null;
/*      */ 
/*   81 */   private transient Hashtable<SnmpMibAgent, SnmpSubRequestHandler> subs = null;
/*      */   private transient SnmpMibTree root;
/*   88 */   private transient Object ipacl = null;
/*   89 */   private transient SnmpPduFactory pduFactory = null;
/*   90 */   private transient SnmpUserDataFactory userDataFactory = null;
/*   91 */   private transient SnmpAdaptorServer adaptor = null;
/*      */   private static final String InterruptSysCallMsg = "Interrupted system call";
/*      */ 
/*      */   public SnmpRequestHandler(SnmpAdaptorServer paramSnmpAdaptorServer, int paramInt, DatagramSocket paramDatagramSocket, DatagramPacket paramDatagramPacket, SnmpMibTree paramSnmpMibTree, Vector paramVector, Object paramObject, SnmpPduFactory paramSnmpPduFactory, SnmpUserDataFactory paramSnmpUserDataFactory, MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */   {
/*  102 */     super(paramSnmpAdaptorServer, paramInt, paramMBeanServer, paramObjectName);
/*      */ 
/*  107 */     this.adaptor = paramSnmpAdaptorServer;
/*  108 */     this.socket = paramDatagramSocket;
/*  109 */     this.packet = paramDatagramPacket;
/*  110 */     this.root = paramSnmpMibTree;
/*  111 */     this.mibs = ((Vector)paramVector.clone());
/*  112 */     this.subs = new Hashtable(this.mibs.size());
/*  113 */     this.ipacl = paramObject;
/*  114 */     this.pduFactory = paramSnmpPduFactory;
/*  115 */     this.userDataFactory = paramSnmpUserDataFactory;
/*      */   }
/*      */ 
/*      */   public void doRun()
/*      */   {
/*  128 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  129 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "doRun", "Packet received:\n" + SnmpMessage.dumpHexBuffer(this.packet.getData(), 0, this.packet.getLength()));
/*      */     }
/*      */ 
/*  136 */     DatagramPacket localDatagramPacket = makeResponsePacket(this.packet);
/*      */ 
/*  140 */     if ((JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) && (localDatagramPacket != null)) {
/*  141 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "doRun", "Packet to be sent:\n" + SnmpMessage.dumpHexBuffer(localDatagramPacket.getData(), 0, localDatagramPacket.getLength()));
/*      */     }
/*      */ 
/*  148 */     if (localDatagramPacket != null)
/*      */       try {
/*  150 */         this.socket.send(localDatagramPacket);
/*      */       } catch (SocketException localSocketException) {
/*  152 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  153 */           if (localSocketException.getMessage().equals("Interrupted system call")) {
/*  154 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "doRun", "interrupted");
/*      */           }
/*      */           else
/*  157 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "doRun", "I/O exception", localSocketException);
/*      */         }
/*      */       }
/*      */       catch (InterruptedIOException localInterruptedIOException)
/*      */       {
/*  162 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  163 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "doRun", "interrupted");
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*  167 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  168 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "doRun", "failure when sending response", localException);
/*      */       }
/*      */   }
/*      */ 
/*      */   private DatagramPacket makeResponsePacket(DatagramPacket paramDatagramPacket)
/*      */   {
/*  180 */     DatagramPacket localDatagramPacket = null;
/*      */ 
/*  184 */     SnmpMessage localSnmpMessage1 = new SnmpMessage();
/*      */     try {
/*  186 */       localSnmpMessage1.decodeMessage(paramDatagramPacket.getData(), paramDatagramPacket.getLength());
/*  187 */       localSnmpMessage1.address = paramDatagramPacket.getAddress();
/*  188 */       localSnmpMessage1.port = paramDatagramPacket.getPort();
/*      */     }
/*      */     catch (SnmpStatusException localSnmpStatusException) {
/*  191 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  192 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponsePacket", "packet decoding failed", localSnmpStatusException);
/*      */       }
/*      */ 
/*  195 */       localSnmpMessage1 = null;
/*  196 */       ((SnmpAdaptorServer)this.adaptorServer).incSnmpInASNParseErrs(1);
/*      */     }
/*      */ 
/*  201 */     SnmpMessage localSnmpMessage2 = null;
/*  202 */     if (localSnmpMessage1 != null) {
/*  203 */       localSnmpMessage2 = makeResponseMessage(localSnmpMessage1);
/*      */     }
/*      */ 
/*  209 */     if (localSnmpMessage2 != null) {
/*      */       try {
/*  211 */         paramDatagramPacket.setLength(localSnmpMessage2.encodeMessage(paramDatagramPacket.getData()));
/*  212 */         localDatagramPacket = paramDatagramPacket;
/*      */       }
/*      */       catch (SnmpTooBigException localSnmpTooBigException1) {
/*  215 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  216 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponsePacket", "response message is too big");
/*      */         }
/*      */         try
/*      */         {
/*  220 */           localSnmpMessage2 = newTooBigMessage(localSnmpMessage1);
/*  221 */           paramDatagramPacket.setLength(localSnmpMessage2.encodeMessage(paramDatagramPacket.getData()));
/*  222 */           localDatagramPacket = paramDatagramPacket;
/*      */         }
/*      */         catch (SnmpTooBigException localSnmpTooBigException2) {
/*  225 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  226 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponsePacket", "'too big' is 'too big' !!!");
/*      */           }
/*      */ 
/*  229 */           this.adaptor.incSnmpSilentDrops(1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  234 */     return localDatagramPacket;
/*      */   }
/*      */ 
/*      */   private SnmpMessage makeResponseMessage(SnmpMessage paramSnmpMessage)
/*      */   {
/*  242 */     SnmpMessage localSnmpMessage = null;
/*      */ 
/*  246 */     SnmpPduPacket localSnmpPduPacket1 = null;
/*  247 */     Object localObject = null;
/*      */     try {
/*  249 */       localSnmpPduPacket1 = (SnmpPduPacket)this.pduFactory.decodeSnmpPdu(paramSnmpMessage);
/*  250 */       if ((localSnmpPduPacket1 != null) && (this.userDataFactory != null))
/*  251 */         localObject = this.userDataFactory.allocateUserData(localSnmpPduPacket1);
/*      */     }
/*      */     catch (SnmpStatusException localSnmpStatusException1) {
/*  254 */       localSnmpPduPacket1 = null;
/*  255 */       SnmpAdaptorServer localSnmpAdaptorServer = (SnmpAdaptorServer)this.adaptorServer;
/*  256 */       localSnmpAdaptorServer.incSnmpInASNParseErrs(1);
/*  257 */       if (localSnmpStatusException1.getStatus() == 243)
/*  258 */         localSnmpAdaptorServer.incSnmpInBadVersions(1);
/*  259 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  260 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "message decoding failed", localSnmpStatusException1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  267 */     SnmpPduPacket localSnmpPduPacket2 = null;
/*  268 */     if (localSnmpPduPacket1 != null) {
/*  269 */       localSnmpPduPacket2 = makeResponsePdu(localSnmpPduPacket1, localObject);
/*      */       try {
/*  271 */         if (this.userDataFactory != null)
/*  272 */           this.userDataFactory.releaseUserData(localObject, localSnmpPduPacket2);
/*      */       } catch (SnmpStatusException localSnmpStatusException2) {
/*  274 */         localSnmpPduPacket2 = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  280 */     if (localSnmpPduPacket2 != null) {
/*      */       try {
/*  282 */         localSnmpMessage = (SnmpMessage)this.pduFactory.encodeSnmpPdu(localSnmpPduPacket2, this.packet.getData().length);
/*      */       }
/*      */       catch (SnmpStatusException localSnmpStatusException3)
/*      */       {
/*  286 */         localSnmpMessage = null;
/*  287 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  288 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "failure when encoding the response message", localSnmpStatusException3);
/*      */         }
/*      */       }
/*      */       catch (SnmpTooBigException localSnmpTooBigException1)
/*      */       {
/*  293 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  294 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "response message is too big");
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  302 */           if (this.packet.getData().length <= 32)
/*  303 */             throw localSnmpTooBigException1;
/*  304 */           int i = localSnmpTooBigException1.getVarBindCount();
/*  305 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  306 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "fail on element" + i);
/*      */           }
/*      */ 
/*  309 */           int j = 0;
/*      */           while (true)
/*      */             try {
/*  312 */               localSnmpPduPacket2 = reduceResponsePdu(localSnmpPduPacket1, localSnmpPduPacket2, i);
/*  313 */               localSnmpMessage = (SnmpMessage)this.pduFactory.encodeSnmpPdu(localSnmpPduPacket2, this.packet.getData().length - 32);
/*      */             }
/*      */             catch (SnmpTooBigException localSnmpTooBigException4)
/*      */             {
/*  318 */               if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  319 */                 JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "response message is still too big");
/*      */               }
/*      */ 
/*  322 */               j = i;
/*  323 */               i = localSnmpTooBigException4.getVarBindCount();
/*  324 */               if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  325 */                 JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "fail on element" + i);
/*      */               }
/*      */ 
/*  328 */               if (i == j)
/*      */               {
/*  332 */                 throw localSnmpTooBigException4;
/*      */               }
/*      */             }
/*      */         }
/*      */         catch (SnmpStatusException localSnmpStatusException4) {
/*  337 */           localSnmpMessage = null;
/*  338 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  339 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "failure when encoding the response message", localSnmpStatusException4);
/*      */         }
/*      */         catch (SnmpTooBigException localSnmpTooBigException2)
/*      */         {
/*      */           try
/*      */           {
/*  345 */             localSnmpPduPacket2 = newTooBigPdu(localSnmpPduPacket1);
/*  346 */             localSnmpMessage = (SnmpMessage)this.pduFactory.encodeSnmpPdu(localSnmpPduPacket2, this.packet.getData().length);
/*      */           }
/*      */           catch (SnmpTooBigException localSnmpTooBigException3)
/*      */           {
/*  350 */             localSnmpMessage = null;
/*  351 */             if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  352 */               JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "'too big' is 'too big' !!!");
/*      */             }
/*      */ 
/*  355 */             this.adaptor.incSnmpSilentDrops(1);
/*      */           }
/*      */           catch (Exception localException2) {
/*  358 */             if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  359 */               JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "Got unexpected exception", localException2);
/*      */             }
/*      */ 
/*  362 */             localSnmpMessage = null;
/*      */           }
/*      */         }
/*      */         catch (Exception localException1) {
/*  366 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  367 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponseMessage", "Got unexpected exception", localException1);
/*      */           }
/*      */ 
/*  370 */           localSnmpMessage = null;
/*      */         }
/*      */       }
/*      */     }
/*  374 */     return localSnmpMessage;
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket makeResponsePdu(SnmpPduPacket paramSnmpPduPacket, Object paramObject)
/*      */   {
/*  384 */     SnmpAdaptorServer localSnmpAdaptorServer = (SnmpAdaptorServer)this.adaptorServer;
/*  385 */     SnmpPduPacket localSnmpPduPacket = null;
/*      */ 
/*  387 */     localSnmpAdaptorServer.updateRequestCounters(paramSnmpPduPacket.type);
/*  388 */     if (paramSnmpPduPacket.varBindList != null) {
/*  389 */       localSnmpAdaptorServer.updateVarCounters(paramSnmpPduPacket.type, paramSnmpPduPacket.varBindList.length);
/*      */     }
/*      */ 
/*  392 */     if (checkPduType(paramSnmpPduPacket)) {
/*  393 */       localSnmpPduPacket = checkAcl(paramSnmpPduPacket);
/*  394 */       if (localSnmpPduPacket == null) {
/*  395 */         if (this.mibs.size() < 1) {
/*  396 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  397 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "makeResponsePdu", "Request " + paramSnmpPduPacket.requestId + " received but no MIB registered.");
/*      */           }
/*      */ 
/*  401 */           return makeNoMibErrorPdu((SnmpPduRequest)paramSnmpPduPacket, paramObject);
/*      */         }
/*  403 */         switch (paramSnmpPduPacket.type) {
/*      */         case 160:
/*      */         case 161:
/*      */         case 163:
/*  407 */           localSnmpPduPacket = makeGetSetResponsePdu((SnmpPduRequest)paramSnmpPduPacket, paramObject);
/*      */ 
/*  409 */           break;
/*      */         case 165:
/*  412 */           localSnmpPduPacket = makeGetBulkResponsePdu((SnmpPduBulk)paramSnmpPduPacket, paramObject);
/*      */         case 162:
/*      */         case 164:
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  420 */         if (!localSnmpAdaptorServer.getAuthRespEnabled()) {
/*  421 */           localSnmpPduPacket = null;
/*      */         }
/*  423 */         if (localSnmpAdaptorServer.getAuthTrapEnabled()) {
/*      */           try {
/*  425 */             localSnmpAdaptorServer.snmpV1Trap(4, 0, new SnmpVarBindList());
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/*  430 */             if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  431 */               JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "makeResponsePdu", "Failure when sending authentication trap", localException);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  438 */     return localSnmpPduPacket;
/*      */   }
/*      */ 
/*      */   SnmpPduPacket makeErrorVarbindPdu(SnmpPduPacket paramSnmpPduPacket, int paramInt)
/*      */   {
/*  453 */     SnmpVarBind[] arrayOfSnmpVarBind = paramSnmpPduPacket.varBindList;
/*  454 */     int i = arrayOfSnmpVarBind.length;
/*      */     int j;
/*  456 */     switch (paramInt) {
/*      */     case 130:
/*  458 */       for (j = 0; j < i; j++)
/*  459 */         arrayOfSnmpVarBind[j].value = SnmpVarBind.endOfMibView;
/*  460 */       break;
/*      */     case 128:
/*  462 */       for (j = 0; j < i; j++)
/*  463 */         arrayOfSnmpVarBind[j].value = SnmpVarBind.noSuchObject;
/*  464 */       break;
/*      */     case 129:
/*  466 */       for (j = 0; j < i; j++)
/*  467 */         arrayOfSnmpVarBind[j].value = SnmpVarBind.noSuchInstance;
/*  468 */       break;
/*      */     default:
/*  470 */       return newErrorResponsePdu(paramSnmpPduPacket, 5, 1);
/*      */     }
/*  472 */     return newValidResponsePdu(paramSnmpPduPacket, arrayOfSnmpVarBind);
/*      */   }
/*      */ 
/*      */   SnmpPduPacket makeNoMibErrorPdu(SnmpPduRequest paramSnmpPduRequest, Object paramObject)
/*      */   {
/*  493 */     if (paramSnmpPduRequest.version == 0)
/*      */     {
/*  495 */       return newErrorResponsePdu(paramSnmpPduRequest, 2, 1);
/*      */     }
/*  497 */     if (paramSnmpPduRequest.version == 1)
/*      */     {
/*  499 */       switch (paramSnmpPduRequest.type)
/*      */       {
/*      */       case 163:
/*      */       case 253:
/*  503 */         return newErrorResponsePdu(paramSnmpPduRequest, 6, 1);
/*      */       case 160:
/*  507 */         return makeErrorVarbindPdu(paramSnmpPduRequest, 128);
/*      */       case 161:
/*      */       case 165:
/*  513 */         return makeErrorVarbindPdu(paramSnmpPduRequest, 130);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  520 */     return newErrorResponsePdu(paramSnmpPduRequest, 5, 1);
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket makeGetSetResponsePdu(SnmpPduRequest paramSnmpPduRequest, Object paramObject)
/*      */   {
/*  541 */     if (paramSnmpPduRequest.varBindList == null)
/*      */     {
/*  544 */       return newValidResponsePdu(paramSnmpPduRequest, null);
/*      */     }
/*      */ 
/*  549 */     splitRequest(paramSnmpPduRequest);
/*  550 */     int i = this.subs.size();
/*  551 */     if (i == 1) {
/*  552 */       return turboProcessingGetSet(paramSnmpPduRequest, paramObject);
/*      */     }
/*      */ 
/*  558 */     SnmpPduPacket localSnmpPduPacket = executeSubRequest(paramSnmpPduRequest, paramObject);
/*  559 */     if (localSnmpPduPacket != null)
/*      */     {
/*  563 */       return localSnmpPduPacket;
/*      */     }
/*      */ 
/*  567 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  568 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "makeGetSetResponsePdu", "Build the unified response for request " + paramSnmpPduRequest.requestId);
/*      */     }
/*      */ 
/*  572 */     return mergeResponses(paramSnmpPduRequest);
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket executeSubRequest(SnmpPduPacket paramSnmpPduPacket, Object paramObject)
/*      */   {
/*  582 */     int i = 0;
/*  583 */     int j = this.subs.size();
/*      */ 
/*  585 */     int k = 0;
/*      */     SnmpSubRequestHandler localSnmpSubRequestHandler;
/*  588 */     if (paramSnmpPduPacket.type == 163)
/*      */     {
/*  590 */       k = 0;
/*  591 */       for (localEnumeration = this.subs.elements(); localEnumeration.hasMoreElements(); k++)
/*      */       {
/*  595 */         localSnmpSubRequestHandler = (SnmpSubRequestHandler)localEnumeration.nextElement();
/*      */ 
/*  597 */         localSnmpSubRequestHandler.setUserData(paramObject);
/*  598 */         localSnmpSubRequestHandler.type = 253;
/*      */ 
/*  600 */         localSnmpSubRequestHandler.run();
/*      */ 
/*  602 */         localSnmpSubRequestHandler.type = 163;
/*      */ 
/*  604 */         if (localSnmpSubRequestHandler.getErrorStatus() != 0)
/*      */         {
/*  607 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  608 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "executeSubRequest", "an error occurs");
/*      */           }
/*      */ 
/*  612 */           return newErrorResponsePdu(paramSnmpPduPacket, i, localSnmpSubRequestHandler.getErrorIndex() + 1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  620 */     k = 0;
/*  621 */     for (Enumeration localEnumeration = this.subs.elements(); localEnumeration.hasMoreElements(); k++) {
/*  622 */       localSnmpSubRequestHandler = (SnmpSubRequestHandler)localEnumeration.nextElement();
/*      */ 
/*  624 */       localSnmpSubRequestHandler.setUserData(paramObject);
/*      */ 
/*  627 */       localSnmpSubRequestHandler.run();
/*      */ 
/*  629 */       if (localSnmpSubRequestHandler.getErrorStatus() != 0)
/*      */       {
/*  632 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  633 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "executeSubRequest", "an error occurs");
/*      */         }
/*      */ 
/*  637 */         return newErrorResponsePdu(paramSnmpPduPacket, i, localSnmpSubRequestHandler.getErrorIndex() + 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  644 */     return null;
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket turboProcessingGetSet(SnmpPduRequest paramSnmpPduRequest, Object paramObject)
/*      */   {
/*  653 */     int i = 0;
/*  654 */     SnmpSubRequestHandler localSnmpSubRequestHandler = (SnmpSubRequestHandler)this.subs.elements().nextElement();
/*  655 */     localSnmpSubRequestHandler.setUserData(paramObject);
/*      */ 
/*  660 */     if (paramSnmpPduRequest.type == 163) {
/*  661 */       localSnmpSubRequestHandler.type = 253;
/*  662 */       localSnmpSubRequestHandler.run();
/*  663 */       localSnmpSubRequestHandler.type = 163;
/*      */ 
/*  667 */       i = localSnmpSubRequestHandler.getErrorStatus();
/*  668 */       if (i != 0)
/*      */       {
/*  671 */         return newErrorResponsePdu(paramSnmpPduRequest, i, localSnmpSubRequestHandler.getErrorIndex() + 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  679 */     localSnmpSubRequestHandler.run();
/*  680 */     i = localSnmpSubRequestHandler.getErrorStatus();
/*  681 */     if (i != 0)
/*      */     {
/*  684 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  685 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "turboProcessingGetSet", "an error occurs");
/*      */       }
/*      */ 
/*  688 */       int j = localSnmpSubRequestHandler.getErrorIndex() + 1;
/*  689 */       return newErrorResponsePdu(paramSnmpPduRequest, i, j);
/*      */     }
/*      */ 
/*  695 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  696 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "turboProcessingGetSet", "build the unified response for request " + paramSnmpPduRequest.requestId);
/*      */     }
/*      */ 
/*  700 */     return mergeResponses(paramSnmpPduRequest);
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket makeGetBulkResponsePdu(SnmpPduBulk paramSnmpPduBulk, Object paramObject)
/*      */   {
/*  710 */     Object localObject = null;
/*      */ 
/*  713 */     int i = paramSnmpPduBulk.varBindList.length;
/*  714 */     int j = Math.max(Math.min(paramSnmpPduBulk.nonRepeaters, i), 0);
/*  715 */     int k = Math.max(paramSnmpPduBulk.maxRepetitions, 0);
/*  716 */     int m = i - j;
/*      */ 
/*  718 */     if (paramSnmpPduBulk.varBindList == null)
/*      */     {
/*  721 */       return newValidResponsePdu(paramSnmpPduBulk, null);
/*      */     }
/*      */ 
/*  726 */     splitBulkRequest(paramSnmpPduBulk, j, k, m);
/*  727 */     SnmpPduPacket localSnmpPduPacket = executeSubRequest(paramSnmpPduBulk, paramObject);
/*  728 */     if (localSnmpPduPacket != null) {
/*  729 */       return localSnmpPduPacket;
/*      */     }
/*  731 */     localObject = mergeBulkResponses(j + k * m);
/*      */ 
/*  736 */     int i1 = localObject.length;
/*  737 */     while ((i1 > j) && (localObject[(i1 - 1)].value.equals(SnmpVarBind.endOfMibView)))
/*      */     {
/*  739 */       i1--;
/*      */     }
/*      */     int n;
/*  741 */     if (i1 == j)
/*  742 */       n = j + m;
/*      */     else
/*  744 */       n = j + ((i1 - 1 - j) / m + 2) * m;
/*  745 */     if (n < localObject.length) {
/*  746 */       SnmpVarBind[] arrayOfSnmpVarBind = new SnmpVarBind[n];
/*  747 */       for (int i2 = 0; i2 < n; i2++) {
/*  748 */         arrayOfSnmpVarBind[i2] = localObject[i2];
/*      */       }
/*  750 */       localObject = arrayOfSnmpVarBind;
/*      */     }
/*      */ 
/*  755 */     return newValidResponsePdu(paramSnmpPduBulk, (SnmpVarBind[])localObject);
/*      */   }
/*      */ 
/*      */   private boolean checkPduType(SnmpPduPacket paramSnmpPduPacket)
/*      */   {
/*  764 */     boolean bool = true;
/*      */ 
/*  766 */     switch (paramSnmpPduPacket.type)
/*      */     {
/*      */     case 160:
/*      */     case 161:
/*      */     case 163:
/*      */     case 165:
/*  772 */       bool = true;
/*  773 */       break;
/*      */     case 162:
/*      */     case 164:
/*      */     default:
/*  776 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  777 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "checkPduType", "cannot respond to this kind of PDU");
/*      */       }
/*      */ 
/*  780 */       bool = false;
/*      */     }
/*      */ 
/*  784 */     return bool;
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket checkAcl(SnmpPduPacket paramSnmpPduPacket)
/*      */   {
/*  793 */     SnmpPduRequest localSnmpPduRequest = null;
/*  794 */     String str = new String(paramSnmpPduPacket.community);
/*      */ 
/*  799 */     if (this.ipacl != null)
/*      */     {
/*      */       int i;
/*  800 */       if (paramSnmpPduPacket.type == 163) {
/*  801 */         if (!((InetAddressAcl)this.ipacl).checkWritePermission(paramSnmpPduPacket.address, str))
/*      */         {
/*  803 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  804 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "checkAcl", "sender is " + paramSnmpPduPacket.address + " with " + str + ". Sender has no write permission");
/*      */           }
/*      */ 
/*  808 */           i = SnmpSubRequestHandler.mapErrorStatus(16, paramSnmpPduPacket.version, paramSnmpPduPacket.type);
/*      */ 
/*  812 */           localSnmpPduRequest = newErrorResponsePdu(paramSnmpPduPacket, i, 0);
/*      */         }
/*  815 */         else if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  816 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "checkAcl", "sender is " + paramSnmpPduPacket.address + " with " + str + ". Sender has write permission");
/*      */         }
/*      */ 
/*      */       }
/*  823 */       else if (!((InetAddressAcl)this.ipacl).checkReadPermission(paramSnmpPduPacket.address, str)) {
/*  824 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  825 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "checkAcl", "sender is " + paramSnmpPduPacket.address + " with " + str + ". Sender has no read permission");
/*      */         }
/*      */ 
/*  829 */         i = SnmpSubRequestHandler.mapErrorStatus(16, paramSnmpPduPacket.version, paramSnmpPduPacket.type);
/*      */ 
/*  833 */         localSnmpPduRequest = newErrorResponsePdu(paramSnmpPduPacket, i, 0);
/*      */ 
/*  836 */         SnmpAdaptorServer localSnmpAdaptorServer2 = (SnmpAdaptorServer)this.adaptorServer;
/*      */ 
/*  838 */         localSnmpAdaptorServer2.updateErrorCounters(2);
/*      */       }
/*  842 */       else if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  843 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "checkAcl", "sender is " + paramSnmpPduPacket.address + " with " + str + ". Sender has read permission");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  854 */     if (localSnmpPduRequest != null) {
/*  855 */       SnmpAdaptorServer localSnmpAdaptorServer1 = (SnmpAdaptorServer)this.adaptorServer;
/*  856 */       localSnmpAdaptorServer1.incSnmpInBadCommunityUses(1);
/*  857 */       if (!((InetAddressAcl)this.ipacl).checkCommunity(str)) {
/*  858 */         localSnmpAdaptorServer1.incSnmpInBadCommunityNames(1);
/*      */       }
/*      */     }
/*  861 */     return localSnmpPduRequest;
/*      */   }
/*      */ 
/*      */   private SnmpPduRequest newValidResponsePdu(SnmpPduPacket paramSnmpPduPacket, SnmpVarBind[] paramArrayOfSnmpVarBind)
/*      */   {
/*  870 */     SnmpPduRequest localSnmpPduRequest = new SnmpPduRequest();
/*      */ 
/*  872 */     localSnmpPduRequest.address = paramSnmpPduPacket.address;
/*  873 */     localSnmpPduRequest.port = paramSnmpPduPacket.port;
/*  874 */     localSnmpPduRequest.version = paramSnmpPduPacket.version;
/*  875 */     localSnmpPduRequest.community = paramSnmpPduPacket.community;
/*  876 */     localSnmpPduRequest.type = 162;
/*  877 */     localSnmpPduRequest.requestId = paramSnmpPduPacket.requestId;
/*  878 */     localSnmpPduRequest.errorStatus = 0;
/*  879 */     localSnmpPduRequest.errorIndex = 0;
/*  880 */     localSnmpPduRequest.varBindList = paramArrayOfSnmpVarBind;
/*      */ 
/*  882 */     ((SnmpAdaptorServer)this.adaptorServer).updateErrorCounters(localSnmpPduRequest.errorStatus);
/*      */ 
/*  885 */     return localSnmpPduRequest;
/*      */   }
/*      */ 
/*      */   private SnmpPduRequest newErrorResponsePdu(SnmpPduPacket paramSnmpPduPacket, int paramInt1, int paramInt2)
/*      */   {
/*  893 */     SnmpPduRequest localSnmpPduRequest = newValidResponsePdu(paramSnmpPduPacket, null);
/*  894 */     localSnmpPduRequest.errorStatus = paramInt1;
/*  895 */     localSnmpPduRequest.errorIndex = paramInt2;
/*  896 */     localSnmpPduRequest.varBindList = paramSnmpPduPacket.varBindList;
/*      */ 
/*  898 */     ((SnmpAdaptorServer)this.adaptorServer).updateErrorCounters(localSnmpPduRequest.errorStatus);
/*      */ 
/*  901 */     return localSnmpPduRequest;
/*      */   }
/*      */ 
/*      */   private SnmpMessage newTooBigMessage(SnmpMessage paramSnmpMessage) throws SnmpTooBigException
/*      */   {
/*  906 */     SnmpMessage localSnmpMessage = null;
/*  907 */     SnmpPduPacket localSnmpPduPacket1 = null;
/*      */     try
/*      */     {
/*  910 */       localSnmpPduPacket1 = (SnmpPduPacket)this.pduFactory.decodeSnmpPdu(paramSnmpMessage);
/*  911 */       if (localSnmpPduPacket1 != null) {
/*  912 */         SnmpPduPacket localSnmpPduPacket2 = newTooBigPdu(localSnmpPduPacket1);
/*  913 */         localSnmpMessage = (SnmpMessage)this.pduFactory.encodeSnmpPdu(localSnmpPduPacket2, this.packet.getData().length);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SnmpStatusException localSnmpStatusException)
/*      */     {
/*  920 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  921 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "newTooBigMessage", "Internal error", localSnmpStatusException);
/*      */       }
/*      */ 
/*  924 */       throw new InternalError();
/*      */     }
/*      */ 
/*  927 */     return localSnmpMessage;
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket newTooBigPdu(SnmpPduPacket paramSnmpPduPacket) {
/*  931 */     SnmpPduRequest localSnmpPduRequest = newErrorResponsePdu(paramSnmpPduPacket, 1, 0);
/*      */ 
/*  933 */     localSnmpPduRequest.varBindList = null;
/*  934 */     return localSnmpPduRequest;
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket reduceResponsePdu(SnmpPduPacket paramSnmpPduPacket1, SnmpPduPacket paramSnmpPduPacket2, int paramInt)
/*      */     throws SnmpTooBigException
/*      */   {
/*  944 */     if (paramSnmpPduPacket1.type != 165) {
/*  945 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  946 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "reduceResponsePdu", "cannot remove anything");
/*      */       }
/*      */ 
/*  949 */       throw new SnmpTooBigException(paramInt);
/*      */     }
/*      */ 
/*  964 */     int i = paramSnmpPduPacket2.varBindList.length;
/*  965 */     if (paramInt >= 3)
/*  966 */       i = Math.min(paramInt - 1, paramSnmpPduPacket2.varBindList.length);
/*  967 */     else if (paramInt == 1)
/*  968 */       i = 1;
/*      */     else {
/*  970 */       i = paramSnmpPduPacket2.varBindList.length / 2;
/*      */     }
/*  972 */     if (i < 1) {
/*  973 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  974 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "reduceResponsePdu", "cannot remove anything");
/*      */       }
/*      */ 
/*  977 */       throw new SnmpTooBigException(paramInt);
/*      */     }
/*      */ 
/*  980 */     SnmpVarBind[] arrayOfSnmpVarBind = new SnmpVarBind[i];
/*  981 */     for (int j = 0; j < i; j++) {
/*  982 */       arrayOfSnmpVarBind[j] = paramSnmpPduPacket2.varBindList[j];
/*      */     }
/*  984 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  985 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "reduceResponsePdu", paramSnmpPduPacket2.varBindList.length - arrayOfSnmpVarBind.length + " items have been removed");
/*      */     }
/*      */ 
/*  989 */     paramSnmpPduPacket2.varBindList = arrayOfSnmpVarBind;
/*      */ 
/*  992 */     return paramSnmpPduPacket2;
/*      */   }
/*      */ 
/*      */   private void splitRequest(SnmpPduRequest paramSnmpPduRequest)
/*      */   {
/* 1000 */     int i = this.mibs.size();
/* 1001 */     SnmpMibAgent localSnmpMibAgent = (SnmpMibAgent)this.mibs.firstElement();
/* 1002 */     if (i == 1)
/*      */     {
/* 1005 */       this.subs.put(localSnmpMibAgent, new SnmpSubRequestHandler(localSnmpMibAgent, paramSnmpPduRequest, true));
/* 1006 */       return;
/*      */     }
/*      */ 
/* 1012 */     if (paramSnmpPduRequest.type == 161) {
/* 1013 */       for (Enumeration localEnumeration = this.mibs.elements(); localEnumeration.hasMoreElements(); ) {
/* 1014 */         localObject = (SnmpMibAgent)localEnumeration.nextElement();
/* 1015 */         this.subs.put(localObject, new SnmpSubNextRequestHandler(this.adaptor, (SnmpMibAgent)localObject, paramSnmpPduRequest));
/*      */       }
/* 1017 */       return;
/*      */     }
/*      */ 
/* 1020 */     int j = paramSnmpPduRequest.varBindList.length;
/* 1021 */     Object localObject = paramSnmpPduRequest.varBindList;
/*      */ 
/* 1023 */     for (int k = 0; k < j; k++) {
/* 1024 */       localSnmpMibAgent = this.root.getAgentMib(localObject[k].oid);
/* 1025 */       SnmpSubRequestHandler localSnmpSubRequestHandler = (SnmpSubRequestHandler)this.subs.get(localSnmpMibAgent);
/* 1026 */       if (localSnmpSubRequestHandler == null)
/*      */       {
/* 1030 */         localSnmpSubRequestHandler = new SnmpSubRequestHandler(localSnmpMibAgent, paramSnmpPduRequest);
/* 1031 */         this.subs.put(localSnmpMibAgent, localSnmpSubRequestHandler);
/*      */       }
/*      */ 
/* 1036 */       localSnmpSubRequestHandler.updateRequest(localObject[k], k);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void splitBulkRequest(SnmpPduBulk paramSnmpPduBulk, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1050 */     for (Enumeration localEnumeration = this.mibs.elements(); localEnumeration.hasMoreElements(); ) {
/* 1051 */       SnmpMibAgent localSnmpMibAgent = (SnmpMibAgent)localEnumeration.nextElement();
/*      */ 
/* 1053 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1054 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "splitBulkRequest", "Create a sub with : " + localSnmpMibAgent + " " + paramInt1 + " " + paramInt2 + " " + paramInt3);
/*      */       }
/*      */ 
/* 1059 */       this.subs.put(localSnmpMibAgent, new SnmpSubBulkRequestHandler(this.adaptor, localSnmpMibAgent, paramSnmpPduBulk, paramInt1, paramInt2, paramInt3));
/*      */     }
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket mergeResponses(SnmpPduRequest paramSnmpPduRequest)
/*      */   {
/* 1072 */     if (paramSnmpPduRequest.type == 161) {
/* 1073 */       return mergeNextResponses(paramSnmpPduRequest);
/*      */     }
/*      */ 
/* 1076 */     SnmpVarBind[] arrayOfSnmpVarBind = paramSnmpPduRequest.varBindList;
/*      */ 
/* 1081 */     for (Enumeration localEnumeration = this.subs.elements(); localEnumeration.hasMoreElements(); ) {
/* 1082 */       SnmpSubRequestHandler localSnmpSubRequestHandler = (SnmpSubRequestHandler)localEnumeration.nextElement();
/* 1083 */       localSnmpSubRequestHandler.updateResult(arrayOfSnmpVarBind);
/*      */     }
/* 1085 */     return newValidResponsePdu(paramSnmpPduRequest, arrayOfSnmpVarBind);
/*      */   }
/*      */ 
/*      */   private SnmpPduPacket mergeNextResponses(SnmpPduRequest paramSnmpPduRequest) {
/* 1089 */     int i = paramSnmpPduRequest.varBindList.length;
/* 1090 */     SnmpVarBind[] arrayOfSnmpVarBind = new SnmpVarBind[i];
/*      */ 
/* 1095 */     for (Enumeration localEnumeration = this.subs.elements(); localEnumeration.hasMoreElements(); ) {
/* 1096 */       localObject = (SnmpSubRequestHandler)localEnumeration.nextElement();
/* 1097 */       ((SnmpSubRequestHandler)localObject).updateResult(arrayOfSnmpVarBind);
/*      */     }
/*      */     Object localObject;
/* 1100 */     if (paramSnmpPduRequest.version == 1) {
/* 1101 */       return newValidResponsePdu(paramSnmpPduRequest, arrayOfSnmpVarBind);
/*      */     }
/*      */ 
/* 1106 */     for (int j = 0; j < i; j++) {
/* 1107 */       localObject = arrayOfSnmpVarBind[j].value;
/* 1108 */       if (localObject == SnmpVarBind.endOfMibView) {
/* 1109 */         return newErrorResponsePdu(paramSnmpPduRequest, 2, j + 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1115 */     return newValidResponsePdu(paramSnmpPduRequest, arrayOfSnmpVarBind);
/*      */   }
/*      */ 
/*      */   private SnmpVarBind[] mergeBulkResponses(int paramInt)
/*      */   {
/* 1121 */     SnmpVarBind[] arrayOfSnmpVarBind = new SnmpVarBind[paramInt];
/* 1122 */     for (int i = paramInt - 1; i >= 0; i--) {
/* 1123 */       arrayOfSnmpVarBind[i] = new SnmpVarBind();
/* 1124 */       arrayOfSnmpVarBind[i].value = SnmpVarBind.endOfMibView;
/*      */     }
/*      */ 
/* 1130 */     for (Enumeration localEnumeration = this.subs.elements(); localEnumeration.hasMoreElements(); ) {
/* 1131 */       SnmpSubRequestHandler localSnmpSubRequestHandler = (SnmpSubRequestHandler)localEnumeration.nextElement();
/* 1132 */       localSnmpSubRequestHandler.updateResult(arrayOfSnmpVarBind);
/*      */     }
/*      */ 
/* 1135 */     return arrayOfSnmpVarBind;
/*      */   }
/*      */ 
/*      */   protected String makeDebugTag() {
/* 1139 */     return "SnmpRequestHandler[" + this.adaptorServer.getProtocol() + ":" + this.adaptorServer.getPort() + "]";
/*      */   }
/*      */ 
/*      */   Thread createThread(Runnable paramRunnable)
/*      */   {
/* 1144 */     return null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpRequestHandler
 * JD-Core Version:    0.6.2
 */