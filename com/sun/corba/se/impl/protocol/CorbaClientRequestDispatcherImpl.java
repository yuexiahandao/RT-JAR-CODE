/*      */ package com.sun.corba.se.impl.protocol;
/*      */ 
/*      */ import com.sun.corba.se.impl.encoding.CDRInputObject;
/*      */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
/*      */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo.CodeSetContext;
/*      */ import com.sun.corba.se.impl.encoding.CodeSetConversion;
/*      */ import com.sun.corba.se.impl.encoding.EncapsInputStream;
/*      */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*      */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*      */ import com.sun.corba.se.pept.broker.Broker;
/*      */ import com.sun.corba.se.pept.encoding.InputObject;
/*      */ import com.sun.corba.se.pept.encoding.OutputObject;
/*      */ import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
/*      */ import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
/*      */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*      */ import com.sun.corba.se.pept.transport.Connection;
/*      */ import com.sun.corba.se.pept.transport.ContactInfo;
/*      */ import com.sun.corba.se.pept.transport.OutboundConnectionCache;
/*      */ import com.sun.corba.se.pept.transport.Selector;
/*      */ import com.sun.corba.se.pept.transport.TransportManager;
/*      */ import com.sun.corba.se.spi.ior.IOR;
/*      */ import com.sun.corba.se.spi.ior.iiop.CodeSetsComponent;
/*      */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*      */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*      */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*      */ import com.sun.corba.se.spi.orb.ORB;
/*      */ import com.sun.corba.se.spi.orb.ORBData;
/*      */ import com.sun.corba.se.spi.orb.ORBVersion;
/*      */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*      */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*      */ import com.sun.corba.se.spi.protocol.PIHandler;
/*      */ import com.sun.corba.se.spi.servicecontext.CodeSetServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.MaxStreamFormatVersionServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.ORBVersionServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.ServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*      */ import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.UnknownServiceContext;
/*      */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*      */ import com.sun.corba.se.spi.transport.CorbaContactInfo;
/*      */ import com.sun.corba.se.spi.transport.CorbaContactInfoListIterator;
/*      */ import java.io.IOException;
/*      */ import java.util.Iterator;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import org.omg.CORBA.SystemException;
/*      */ import org.omg.CORBA.portable.ApplicationException;
/*      */ import org.omg.CORBA.portable.RemarshalException;
/*      */ import org.omg.CORBA.portable.UnknownException;
/*      */ import sun.corba.EncapsInputStreamFactory;
/*      */ 
/*      */ public class CorbaClientRequestDispatcherImpl
/*      */   implements ClientRequestDispatcher
/*      */ {
/*  129 */   private ConcurrentMap<ContactInfo, Object> locks = new ConcurrentHashMap();
/*      */ 
/*      */   public OutputObject beginRequest(Object paramObject, String paramString, boolean paramBoolean, ContactInfo paramContactInfo)
/*      */   {
/*  135 */     ORB localORB = null;
/*      */     try {
/*  137 */       CorbaContactInfo localCorbaContactInfo = (CorbaContactInfo)paramContactInfo;
/*  138 */       localORB = (ORB)paramContactInfo.getBroker();
/*      */ 
/*  140 */       if (localORB.subcontractDebugFlag) {
/*  141 */         dprint(".beginRequest->: op/" + paramString);
/*      */       }
/*      */ 
/*  148 */       localORB.getPIHandler().initiateClientPIRequest(false);
/*      */ 
/*  154 */       CorbaConnection localCorbaConnection = null;
/*      */ 
/*  162 */       Object localObject1 = this.locks.get(paramContactInfo);
/*      */ 
/*  164 */       if (localObject1 == null) {
/*  165 */         Object localObject2 = new Object();
/*  166 */         localObject1 = this.locks.putIfAbsent(paramContactInfo, localObject2);
/*  167 */         if (localObject1 == null)
/*  168 */           localObject1 = localObject2;
/*      */       }
/*      */       Object localObject5;
/*  172 */       synchronized (localObject1) {
/*  173 */         if (paramContactInfo.isConnectionBased()) {
/*  174 */           if (paramContactInfo.shouldCacheConnection()) {
/*  175 */             localCorbaConnection = (CorbaConnection)localORB.getTransportManager().getOutboundConnectionCache(paramContactInfo).get(paramContactInfo);
/*      */           }
/*      */ 
/*  179 */           if (localCorbaConnection != null) {
/*  180 */             if (localORB.subcontractDebugFlag)
/*  181 */               dprint(".beginRequest: op/" + paramString + ": Using cached connection: " + localCorbaConnection);
/*      */           }
/*      */           else
/*      */           {
/*      */             try {
/*  186 */               localCorbaConnection = (CorbaConnection)paramContactInfo.createConnection();
/*      */ 
/*  188 */               if (localORB.subcontractDebugFlag)
/*  189 */                 dprint(".beginRequest: op/" + paramString + ": Using created connection: " + localCorbaConnection);
/*      */             }
/*      */             catch (RuntimeException localRuntimeException)
/*      */             {
/*  193 */               if (localORB.subcontractDebugFlag) {
/*  194 */                 dprint(".beginRequest: op/" + paramString + ": failed to create connection: " + localRuntimeException);
/*      */               }
/*      */ 
/*  198 */               boolean bool = getContactInfoListIterator(localORB).reportException(paramContactInfo, localRuntimeException);
/*      */ 
/*  202 */               if (bool) {
/*  203 */                 if (getContactInfoListIterator(localORB).hasNext()) {
/*  204 */                   paramContactInfo = (ContactInfo)getContactInfoListIterator(localORB).next();
/*      */ 
/*  206 */                   unregisterWaiter(localORB);
/*  207 */                   return beginRequest(paramObject, paramString, paramBoolean, paramContactInfo);
/*      */                 }
/*      */ 
/*  210 */                 throw localRuntimeException;
/*      */               }
/*      */ 
/*  213 */               throw localRuntimeException;
/*      */             }
/*      */ 
/*  216 */             if (localCorbaConnection.shouldRegisterReadEvent())
/*      */             {
/*  218 */               localORB.getTransportManager().getSelector(0).registerForEvent(localCorbaConnection.getEventHandler());
/*      */ 
/*  220 */               localCorbaConnection.setState("ESTABLISHED");
/*      */             }
/*      */ 
/*  225 */             if (paramContactInfo.shouldCacheConnection()) {
/*  226 */               localObject3 = localORB.getTransportManager().getOutboundConnectionCache(paramContactInfo);
/*      */ 
/*  229 */               ((OutboundConnectionCache)localObject3).stampTime(localCorbaConnection);
/*  230 */               ((OutboundConnectionCache)localObject3).put(paramContactInfo, localCorbaConnection);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  237 */       ??? = (CorbaMessageMediator)paramContactInfo.createMessageMediator(localORB, paramContactInfo, localCorbaConnection, paramString, paramBoolean);
/*      */ 
/*  240 */       if (localORB.subcontractDebugFlag) {
/*  241 */         dprint(".beginRequest: " + opAndId((CorbaMessageMediator)???) + ": created message mediator: " + ???);
/*      */       }
/*      */ 
/*  255 */       localORB.getInvocationInfo().setMessageMediator((MessageMediator)???);
/*      */ 
/*  257 */       if ((localCorbaConnection != null) && (localCorbaConnection.getCodeSetContext() == null)) {
/*  258 */         performCodeSetNegotiation((CorbaMessageMediator)???);
/*      */       }
/*      */ 
/*  261 */       addServiceContexts((CorbaMessageMediator)???);
/*      */ 
/*  263 */       Object localObject3 = paramContactInfo.createOutputObject((MessageMediator)???);
/*      */ 
/*  265 */       if (localORB.subcontractDebugFlag) {
/*  266 */         dprint(".beginRequest: " + opAndId((CorbaMessageMediator)???) + ": created output object: " + localObject3);
/*      */       }
/*      */ 
/*  274 */       registerWaiter((CorbaMessageMediator)???);
/*      */ 
/*  277 */       synchronized (localObject1) {
/*  278 */         if ((paramContactInfo.isConnectionBased()) && 
/*  279 */           (paramContactInfo.shouldCacheConnection())) {
/*  280 */           localObject5 = localORB.getTransportManager().getOutboundConnectionCache(paramContactInfo);
/*      */ 
/*  283 */           ((OutboundConnectionCache)localObject5).reclaim();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  288 */       localORB.getPIHandler().setClientPIInfo((CorbaMessageMediator)???);
/*      */       try
/*      */       {
/*  293 */         localORB.getPIHandler().invokeClientPIStartingPoint();
/*      */       } catch (RemarshalException localRemarshalException) {
/*  295 */         if (localORB.subcontractDebugFlag) {
/*  296 */           dprint(".beginRequest: " + opAndId((CorbaMessageMediator)???) + ": Remarshal");
/*      */         }
/*      */ 
/*  313 */         if (getContactInfoListIterator(localORB).hasNext()) {
/*  314 */           paramContactInfo = (ContactInfo)getContactInfoListIterator(localORB).next();
/*  315 */           if (localORB.subcontractDebugFlag) {
/*  316 */             dprint("RemarshalException: hasNext true\ncontact info " + paramContactInfo);
/*      */           }
/*      */ 
/*  320 */           localORB.getPIHandler().makeCompletedClientRequest(3, null);
/*      */ 
/*  322 */           unregisterWaiter(localORB);
/*  323 */           localORB.getPIHandler().cleanupClientPIRequest();
/*      */ 
/*  325 */           return beginRequest(paramObject, paramString, paramBoolean, paramContactInfo);
/*      */         }
/*  327 */         if (localORB.subcontractDebugFlag) {
/*  328 */           dprint("RemarshalException: hasNext false");
/*      */         }
/*  330 */         localObject5 = ORBUtilSystemException.get(localORB, "rpc.protocol");
/*      */ 
/*  333 */         throw ((ORBUtilSystemException)localObject5).remarshalWithNowhereToGo();
/*      */       }
/*      */ 
/*  337 */       ((CorbaMessageMediator)???).initializeMessage();
/*  338 */       if (localORB.subcontractDebugFlag) {
/*  339 */         dprint(".beginRequest: " + opAndId((CorbaMessageMediator)???) + ": initialized message");
/*      */       }
/*      */ 
/*  343 */       return localObject3;
/*      */     }
/*      */     finally {
/*  346 */       if (localORB.subcontractDebugFlag)
/*  347 */         dprint(".beginRequest<-: op/" + paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   public InputObject marshalingComplete(Object paramObject, OutputObject paramOutputObject)
/*      */     throws ApplicationException, RemarshalException
/*      */   {
/*  358 */     ORB localORB = null;
/*  359 */     CorbaMessageMediator localCorbaMessageMediator = null;
/*      */     try {
/*  361 */       localCorbaMessageMediator = (CorbaMessageMediator)paramOutputObject.getMessageMediator();
/*      */ 
/*  364 */       localORB = (ORB)localCorbaMessageMediator.getBroker();
/*      */ 
/*  366 */       if (localORB.subcontractDebugFlag) {
/*  367 */         dprint(".marshalingComplete->: " + opAndId(localCorbaMessageMediator));
/*      */       }
/*      */ 
/*  370 */       InputObject localInputObject1 = marshalingComplete1(localORB, localCorbaMessageMediator);
/*      */ 
/*  373 */       return processResponse(localORB, localCorbaMessageMediator, localInputObject1);
/*      */     }
/*      */     finally {
/*  376 */       if (localORB.subcontractDebugFlag)
/*  377 */         dprint(".marshalingComplete<-: " + opAndId(localCorbaMessageMediator));
/*      */     }
/*      */   }
/*      */ 
/*      */   public InputObject marshalingComplete1(ORB paramORB, CorbaMessageMediator paramCorbaMessageMediator)
/*      */     throws ApplicationException, RemarshalException
/*      */   {
/*      */     try
/*      */     {
/*  389 */       paramCorbaMessageMediator.finishSendingRequest();
/*      */ 
/*  391 */       if (paramORB.subcontractDebugFlag) {
/*  392 */         dprint(".marshalingComplete: " + opAndId(paramCorbaMessageMediator) + ": finished sending request");
/*      */       }
/*      */ 
/*  396 */       return paramCorbaMessageMediator.waitForResponse();
/*      */     }
/*      */     catch (RuntimeException localRuntimeException)
/*      */     {
/*  400 */       if (paramORB.subcontractDebugFlag) {
/*  401 */         dprint(".marshalingComplete: " + opAndId(paramCorbaMessageMediator) + ": exception: " + localRuntimeException.toString());
/*      */       }
/*      */ 
/*  405 */       boolean bool = getContactInfoListIterator(paramORB).reportException(paramCorbaMessageMediator.getContactInfo(), localRuntimeException);
/*      */ 
/*  412 */       Exception localException = paramORB.getPIHandler().invokeClientPIEndingPoint(2, localRuntimeException);
/*      */ 
/*  416 */       if (bool) {
/*  417 */         if (localException == localRuntimeException) {
/*  418 */           continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, new RemarshalException());
/*      */         }
/*      */         else
/*  421 */           continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, localException);
/*      */       }
/*      */       else
/*      */       {
/*  425 */         if ((localException instanceof RuntimeException)) {
/*  426 */           throw ((RuntimeException)localException);
/*      */         }
/*  428 */         if ((localException instanceof RemarshalException))
/*      */         {
/*  430 */           throw ((RemarshalException)localException);
/*      */         }
/*      */ 
/*  434 */         throw localRuntimeException;
/*      */       }
/*      */     }
/*  436 */     return null;
/*      */   }
/*      */ 
/*      */   protected InputObject processResponse(ORB paramORB, CorbaMessageMediator paramCorbaMessageMediator, InputObject paramInputObject)
/*      */     throws ApplicationException, RemarshalException
/*      */   {
/*  447 */     ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*      */ 
/*  451 */     if (paramORB.subcontractDebugFlag) {
/*  452 */       dprint(".processResponse: " + opAndId(paramCorbaMessageMediator) + ": response received");
/*      */     }
/*      */ 
/*  458 */     if (paramCorbaMessageMediator.getConnection() != null) {
/*  459 */       ((CorbaConnection)paramCorbaMessageMediator.getConnection()).setPostInitialContexts();
/*      */     }
/*      */ 
/*  468 */     Object localObject1 = null;
/*      */ 
/*  470 */     if (paramCorbaMessageMediator.isOneWay()) {
/*  471 */       getContactInfoListIterator(paramORB).reportSuccess(paramCorbaMessageMediator.getContactInfo());
/*      */ 
/*  474 */       localObject1 = paramORB.getPIHandler().invokeClientPIEndingPoint(0, (Exception)localObject1);
/*      */ 
/*  476 */       continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, (Exception)localObject1);
/*  477 */       return null;
/*      */     }
/*      */ 
/*  480 */     consumeServiceContexts(paramORB, paramCorbaMessageMediator);
/*      */ 
/*  485 */     ((CDRInputObject)paramInputObject).performORBVersionSpecificInit();
/*      */     Object localObject2;
/*      */     Object localObject3;
/*  487 */     if (paramCorbaMessageMediator.isSystemExceptionReply())
/*      */     {
/*  489 */       localObject2 = paramCorbaMessageMediator.getSystemExceptionReply();
/*      */ 
/*  491 */       if (paramORB.subcontractDebugFlag) {
/*  492 */         dprint(".processResponse: " + opAndId(paramCorbaMessageMediator) + ": received system exception: " + localObject2);
/*      */       }
/*      */ 
/*  496 */       boolean bool = getContactInfoListIterator(paramORB).reportException(paramCorbaMessageMediator.getContactInfo(), (RuntimeException)localObject2);
/*      */ 
/*  500 */       if (bool)
/*      */       {
/*  503 */         localObject1 = paramORB.getPIHandler().invokeClientPIEndingPoint(2, (Exception)localObject2);
/*      */ 
/*  508 */         if (localObject2 == localObject1)
/*      */         {
/*  511 */           localObject1 = null;
/*  512 */           continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, new RemarshalException());
/*      */ 
/*  514 */           throw localORBUtilSystemException.statementNotReachable1();
/*      */         }
/*      */ 
/*  517 */         continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, (Exception)localObject1);
/*      */ 
/*  519 */         throw localORBUtilSystemException.statementNotReachable2();
/*      */       }
/*      */ 
/*  525 */       localObject3 = paramCorbaMessageMediator.getReplyServiceContexts();
/*      */ 
/*  527 */       if (localObject3 != null) {
/*  528 */         UEInfoServiceContext localUEInfoServiceContext = (UEInfoServiceContext)((ServiceContexts)localObject3).get(9);
/*      */ 
/*  532 */         if (localUEInfoServiceContext != null) {
/*  533 */           Throwable localThrowable = localUEInfoServiceContext.getUE();
/*  534 */           UnknownException localUnknownException = new UnknownException(localThrowable);
/*      */ 
/*  537 */           localObject1 = paramORB.getPIHandler().invokeClientPIEndingPoint(2, localUnknownException);
/*      */ 
/*  540 */           continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, (Exception)localObject1);
/*  541 */           throw localORBUtilSystemException.statementNotReachable3();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  549 */       localObject1 = paramORB.getPIHandler().invokeClientPIEndingPoint(2, (Exception)localObject2);
/*      */ 
/*  552 */       continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, (Exception)localObject1);
/*      */ 
/*  556 */       throw localORBUtilSystemException.statementNotReachable4();
/*  557 */     }if (paramCorbaMessageMediator.isUserExceptionReply())
/*      */     {
/*  559 */       if (paramORB.subcontractDebugFlag) {
/*  560 */         dprint(".processResponse: " + opAndId(paramCorbaMessageMediator) + ": received user exception");
/*      */       }
/*      */ 
/*  564 */       getContactInfoListIterator(paramORB).reportSuccess(paramCorbaMessageMediator.getContactInfo());
/*      */ 
/*  567 */       localObject2 = peekUserExceptionId(paramInputObject);
/*  568 */       Exception localException = null;
/*      */ 
/*  570 */       if (paramCorbaMessageMediator.isDIIRequest()) {
/*  571 */         localObject1 = paramCorbaMessageMediator.unmarshalDIIUserException((String)localObject2, (org.omg.CORBA_2_3.portable.InputStream)paramInputObject);
/*      */ 
/*  573 */         localException = paramORB.getPIHandler().invokeClientPIEndingPoint(1, (Exception)localObject1);
/*      */ 
/*  575 */         paramCorbaMessageMediator.setDIIException(localException);
/*      */       }
/*      */       else {
/*  578 */         localObject3 = new ApplicationException((String)localObject2, (org.omg.CORBA.portable.InputStream)paramInputObject);
/*      */ 
/*  582 */         localObject1 = localObject3;
/*  583 */         localException = paramORB.getPIHandler().invokeClientPIEndingPoint(1, (Exception)localObject3);
/*      */       }
/*      */ 
/*  587 */       if (localException != localObject1) {
/*  588 */         continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, localException);
/*      */       }
/*      */ 
/*  591 */       if ((localException instanceof ApplicationException)) {
/*  592 */         throw ((ApplicationException)localException);
/*      */       }
/*      */ 
/*  596 */       return paramInputObject;
/*      */     }
/*  598 */     if (paramCorbaMessageMediator.isLocationForwardReply())
/*      */     {
/*  600 */       if (paramORB.subcontractDebugFlag) {
/*  601 */         dprint(".processResponse: " + opAndId(paramCorbaMessageMediator) + ": received location forward");
/*      */       }
/*      */ 
/*  606 */       getContactInfoListIterator(paramORB).reportRedirect((CorbaContactInfo)paramCorbaMessageMediator.getContactInfo(), paramCorbaMessageMediator.getForwardedIOR());
/*      */ 
/*  611 */       localObject2 = paramORB.getPIHandler().invokeClientPIEndingPoint(3, null);
/*      */ 
/*  614 */       if (!(localObject2 instanceof RemarshalException)) {
/*  615 */         localObject1 = localObject2;
/*      */       }
/*      */ 
/*  621 */       if (localObject1 != null) {
/*  622 */         continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, (Exception)localObject1);
/*      */       }
/*  624 */       continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, new RemarshalException());
/*      */ 
/*  626 */       throw localORBUtilSystemException.statementNotReachable5();
/*      */     }
/*  628 */     if (paramCorbaMessageMediator.isDifferentAddrDispositionRequestedReply())
/*      */     {
/*  630 */       if (paramORB.subcontractDebugFlag) {
/*  631 */         dprint(".processResponse: " + opAndId(paramCorbaMessageMediator) + ": received different addressing dispostion request");
/*      */       }
/*      */ 
/*  636 */       getContactInfoListIterator(paramORB).reportAddrDispositionRetry((CorbaContactInfo)paramCorbaMessageMediator.getContactInfo(), paramCorbaMessageMediator.getAddrDispositionReply());
/*      */ 
/*  641 */       localObject2 = paramORB.getPIHandler().invokeClientPIEndingPoint(5, null);
/*      */ 
/*  645 */       if (!(localObject2 instanceof RemarshalException)) {
/*  646 */         localObject1 = localObject2;
/*      */       }
/*      */ 
/*  652 */       if (localObject1 != null) {
/*  653 */         continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, (Exception)localObject1);
/*      */       }
/*  655 */       continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, new RemarshalException());
/*      */ 
/*  657 */       throw localORBUtilSystemException.statementNotReachable6();
/*      */     }
/*      */ 
/*  660 */     if (paramORB.subcontractDebugFlag) {
/*  661 */       dprint(".processResponse: " + opAndId(paramCorbaMessageMediator) + ": received normal response");
/*      */     }
/*      */ 
/*  665 */     getContactInfoListIterator(paramORB).reportSuccess(paramCorbaMessageMediator.getContactInfo());
/*      */ 
/*  668 */     paramCorbaMessageMediator.handleDIIReply((org.omg.CORBA_2_3.portable.InputStream)paramInputObject);
/*      */ 
/*  671 */     localObject1 = paramORB.getPIHandler().invokeClientPIEndingPoint(0, null);
/*      */ 
/*  675 */     continueOrThrowSystemOrRemarshal(paramCorbaMessageMediator, (Exception)localObject1);
/*      */ 
/*  677 */     return paramInputObject;
/*      */   }
/*      */ 
/*      */   protected void continueOrThrowSystemOrRemarshal(CorbaMessageMediator paramCorbaMessageMediator, Exception paramException)
/*      */     throws SystemException, RemarshalException
/*      */   {
/*  694 */     ORB localORB = (ORB)paramCorbaMessageMediator.getBroker();
/*      */ 
/*  696 */     if (paramException != null)
/*      */     {
/*  700 */       if ((paramException instanceof RemarshalException))
/*      */       {
/*  703 */         localORB.getInvocationInfo().setIsRetryInvocation(true);
/*      */ 
/*  709 */         unregisterWaiter(localORB);
/*      */ 
/*  711 */         if (localORB.subcontractDebugFlag) {
/*  712 */           dprint(".continueOrThrowSystemOrRemarshal: " + opAndId(paramCorbaMessageMediator) + ": throwing Remarshal");
/*      */         }
/*      */ 
/*  717 */         throw ((RemarshalException)paramException);
/*      */       }
/*      */ 
/*  721 */       if (localORB.subcontractDebugFlag) {
/*  722 */         dprint(".continueOrThrowSystemOrRemarshal: " + opAndId(paramCorbaMessageMediator) + ": throwing sex:" + paramException);
/*      */       }
/*      */ 
/*  728 */       throw ((SystemException)paramException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected CorbaContactInfoListIterator getContactInfoListIterator(ORB paramORB)
/*      */   {
/*  734 */     return (CorbaContactInfoListIterator)((CorbaInvocationInfo)paramORB.getInvocationInfo()).getContactInfoListIterator();
/*      */   }
/*      */ 
/*      */   protected void registerWaiter(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/*  741 */     if (paramCorbaMessageMediator.getConnection() != null)
/*  742 */       paramCorbaMessageMediator.getConnection().registerWaiter(paramCorbaMessageMediator);
/*      */   }
/*      */ 
/*      */   protected void unregisterWaiter(ORB paramORB)
/*      */   {
/*  748 */     MessageMediator localMessageMediator = paramORB.getInvocationInfo().getMessageMediator();
/*      */ 
/*  750 */     if ((localMessageMediator != null) && (localMessageMediator.getConnection() != null))
/*      */     {
/*  754 */       localMessageMediator.getConnection().unregisterWaiter(localMessageMediator);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addServiceContexts(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/*  760 */     ORB localORB = (ORB)paramCorbaMessageMediator.getBroker();
/*  761 */     CorbaConnection localCorbaConnection = (CorbaConnection)paramCorbaMessageMediator.getConnection();
/*  762 */     GIOPVersion localGIOPVersion = paramCorbaMessageMediator.getGIOPVersion();
/*      */ 
/*  764 */     ServiceContexts localServiceContexts = paramCorbaMessageMediator.getRequestServiceContexts();
/*      */ 
/*  766 */     addCodeSetServiceContext(localCorbaConnection, localServiceContexts, localGIOPVersion);
/*      */ 
/*  772 */     localServiceContexts.put(MaxStreamFormatVersionServiceContext.singleton);
/*      */ 
/*  775 */     ORBVersionServiceContext localORBVersionServiceContext = new ORBVersionServiceContext(ORBVersionFactory.getORBVersion());
/*      */ 
/*  777 */     localServiceContexts.put(localORBVersionServiceContext);
/*      */ 
/*  780 */     if ((localCorbaConnection != null) && (!localCorbaConnection.isPostInitialContexts()))
/*      */     {
/*  785 */       SendingContextServiceContext localSendingContextServiceContext = new SendingContextServiceContext(localORB.getFVDCodeBaseIOR());
/*      */ 
/*  787 */       localServiceContexts.put(localSendingContextServiceContext);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void consumeServiceContexts(ORB paramORB, CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/*  794 */     ServiceContexts localServiceContexts = paramCorbaMessageMediator.getReplyServiceContexts();
/*      */ 
/*  796 */     ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*      */ 
/*  799 */     if (localServiceContexts == null) {
/*  800 */       return;
/*      */     }
/*      */ 
/*  803 */     ServiceContext localServiceContext = localServiceContexts.get(6);
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  805 */     if (localServiceContext != null) {
/*  806 */       localObject1 = (SendingContextServiceContext)localServiceContext;
/*      */ 
/*  808 */       localObject2 = ((SendingContextServiceContext)localObject1).getIOR();
/*      */       try
/*      */       {
/*  812 */         if (paramCorbaMessageMediator.getConnection() != null)
/*  813 */           ((CorbaConnection)paramCorbaMessageMediator.getConnection()).setCodeBaseIOR((IOR)localObject2);
/*      */       }
/*      */       catch (ThreadDeath localThreadDeath) {
/*  816 */         throw localThreadDeath;
/*      */       } catch (Throwable localThrowable) {
/*  818 */         throw localORBUtilSystemException.badStringifiedIor(localThrowable);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  824 */     localServiceContext = localServiceContexts.get(1313165056);
/*      */ 
/*  826 */     if (localServiceContext != null) {
/*  827 */       localObject1 = (ORBVersionServiceContext)localServiceContext;
/*      */ 
/*  830 */       localObject2 = ((ORBVersionServiceContext)localObject1).getVersion();
/*  831 */       paramORB.setORBVersion((ORBVersion)localObject2);
/*      */     }
/*      */ 
/*  834 */     getExceptionDetailMessage(paramCorbaMessageMediator, localORBUtilSystemException);
/*      */   }
/*      */ 
/*      */   protected void getExceptionDetailMessage(CorbaMessageMediator paramCorbaMessageMediator, ORBUtilSystemException paramORBUtilSystemException)
/*      */   {
/*  841 */     ServiceContext localServiceContext = paramCorbaMessageMediator.getReplyServiceContexts().get(14);
/*      */ 
/*  843 */     if (localServiceContext == null) {
/*  844 */       return;
/*      */     }
/*  846 */     if (!(localServiceContext instanceof UnknownServiceContext)) {
/*  847 */       throw paramORBUtilSystemException.badExceptionDetailMessageServiceContextType();
/*      */     }
/*  849 */     byte[] arrayOfByte = ((UnknownServiceContext)localServiceContext).getData();
/*  850 */     EncapsInputStream localEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream((ORB)paramCorbaMessageMediator.getBroker(), arrayOfByte, arrayOfByte.length);
/*      */ 
/*  853 */     localEncapsInputStream.consumeEndian();
/*      */ 
/*  855 */     String str = "----------BEGIN server-side stack trace----------\n" + localEncapsInputStream.read_wstring() + "\n" + "----------END server-side stack trace----------";
/*      */ 
/*  860 */     paramCorbaMessageMediator.setReplyExceptionDetailMessage(str);
/*      */   }
/*      */ 
/*      */   public void endRequest(Broker paramBroker, Object paramObject, InputObject paramInputObject)
/*      */   {
/*  865 */     ORB localORB = (ORB)paramBroker;
/*      */     try
/*      */     {
/*  868 */       if (localORB.subcontractDebugFlag) {
/*  869 */         dprint(".endRequest->");
/*      */       }
/*      */ 
/*  876 */       MessageMediator localMessageMediator = localORB.getInvocationInfo().getMessageMediator();
/*      */ 
/*  878 */       if (localMessageMediator != null)
/*      */       {
/*  880 */         if (localMessageMediator.getConnection() != null)
/*      */         {
/*  882 */           ((CorbaMessageMediator)localMessageMediator).sendCancelRequestIfFinalFragmentNotSent();
/*      */         }
/*      */ 
/*  888 */         InputObject localInputObject = localMessageMediator.getInputObject();
/*  889 */         if (localInputObject != null) {
/*  890 */           localInputObject.close();
/*      */         }
/*      */ 
/*  893 */         OutputObject localOutputObject = localMessageMediator.getOutputObject();
/*  894 */         if (localOutputObject != null) {
/*  895 */           localOutputObject.close();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  909 */       unregisterWaiter(localORB);
/*      */ 
/*  915 */       localORB.getPIHandler().cleanupClientPIRequest();
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  921 */       if (localORB.subcontractDebugFlag)
/*      */       {
/*  923 */         dprint(".endRequest: ignoring IOException - " + localIOException.toString());
/*      */       }
/*      */     } finally {
/*  926 */       if (localORB.subcontractDebugFlag)
/*  927 */         dprint(".endRequest<-");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void performCodeSetNegotiation(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/*  935 */     CorbaConnection localCorbaConnection = (CorbaConnection)paramCorbaMessageMediator.getConnection();
/*      */ 
/*  937 */     IOR localIOR = ((CorbaContactInfo)paramCorbaMessageMediator.getContactInfo()).getEffectiveTargetIOR();
/*      */ 
/*  940 */     GIOPVersion localGIOPVersion = paramCorbaMessageMediator.getGIOPVersion();
/*      */ 
/*  946 */     if ((localCorbaConnection != null) && (localCorbaConnection.getCodeSetContext() == null) && (!localGIOPVersion.equals(GIOPVersion.V1_0)))
/*      */     {
/*  950 */       synchronized (localCorbaConnection)
/*      */       {
/*  954 */         if (localCorbaConnection.getCodeSetContext() != null) {
/*  955 */           return;
/*      */         }
/*      */ 
/*  960 */         IIOPProfileTemplate localIIOPProfileTemplate = (IIOPProfileTemplate)localIOR.getProfile().getTaggedProfileTemplate();
/*      */ 
/*  963 */         Iterator localIterator = localIIOPProfileTemplate.iteratorById(1);
/*  964 */         if (!localIterator.hasNext())
/*      */         {
/*  968 */           return;
/*      */         }
/*      */ 
/*  973 */         CodeSetComponentInfo localCodeSetComponentInfo = ((CodeSetsComponent)localIterator.next()).getCodeSetComponentInfo();
/*      */ 
/*  978 */         CodeSetComponentInfo.CodeSetContext localCodeSetContext = CodeSetConversion.impl().negotiate(localCorbaConnection.getBroker().getORBData().getCodeSetComponentInfo(), localCodeSetComponentInfo);
/*      */ 
/*  983 */         localCorbaConnection.setCodeSetContext(localCodeSetContext);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addCodeSetServiceContext(CorbaConnection paramCorbaConnection, ServiceContexts paramServiceContexts, GIOPVersion paramGIOPVersion)
/*      */   {
/* 1009 */     if ((paramGIOPVersion.equals(GIOPVersion.V1_0)) || (paramCorbaConnection == null)) {
/* 1010 */       return;
/*      */     }
/* 1012 */     CodeSetComponentInfo.CodeSetContext localCodeSetContext = null;
/*      */ 
/* 1014 */     if ((paramCorbaConnection.getBroker().getORBData().alwaysSendCodeSetServiceContext()) || (!paramCorbaConnection.isPostInitialContexts()))
/*      */     {
/* 1018 */       localCodeSetContext = paramCorbaConnection.getCodeSetContext();
/*      */     }
/*      */ 
/* 1025 */     if (localCodeSetContext == null) {
/* 1026 */       return;
/*      */     }
/* 1028 */     CodeSetServiceContext localCodeSetServiceContext = new CodeSetServiceContext(localCodeSetContext);
/* 1029 */     paramServiceContexts.put(localCodeSetServiceContext);
/*      */   }
/*      */ 
/*      */   protected String peekUserExceptionId(InputObject paramInputObject)
/*      */   {
/* 1034 */     CDRInputObject localCDRInputObject = (CDRInputObject)paramInputObject;
/*      */ 
/* 1036 */     localCDRInputObject.mark(2147483647);
/* 1037 */     String str = localCDRInputObject.read_string();
/* 1038 */     localCDRInputObject.reset();
/* 1039 */     return str;
/*      */   }
/*      */ 
/*      */   protected void dprint(String paramString)
/*      */   {
/* 1044 */     ORBUtility.dprint("CorbaClientRequestDispatcherImpl", paramString);
/*      */   }
/*      */ 
/*      */   protected String opAndId(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 1049 */     return ORBUtility.operationNameAndRequestId(paramCorbaMessageMediator);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.CorbaClientRequestDispatcherImpl
 * JD-Core Version:    0.6.2
 */