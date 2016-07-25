/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.ServerRequestImpl;
/*     */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo.CodeSetContext;
/*     */ import com.sun.corba.se.impl.encoding.MarshalInputStream;
/*     */ import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
/*     */ import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.pept.encoding.OutputObject;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.oa.NullServant;
/*     */ import com.sun.corba.se.spi.oa.OADestroyed;
/*     */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBVersion;
/*     */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.protocol.CorbaProtocolHandler;
/*     */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*     */ import com.sun.corba.se.spi.protocol.ForwardException;
/*     */ import com.sun.corba.se.spi.protocol.PIHandler;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*     */ import com.sun.corba.se.spi.servicecontext.CodeSetServiceContext;
/*     */ import com.sun.corba.se.spi.servicecontext.ORBVersionServiceContext;
/*     */ import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContext;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*     */ import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.ServerRequest;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*     */ import org.omg.CORBA.UNKNOWN;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.InvokeHandler;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.UnknownException;
/*     */ 
/*     */ public class CorbaServerRequestDispatcherImpl
/*     */   implements CorbaServerRequestDispatcher
/*     */ {
/*     */   protected ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */   private POASystemException poaWrapper;
/*     */ 
/*     */   public CorbaServerRequestDispatcherImpl(ORB paramORB)
/*     */   {
/* 106 */     this.orb = paramORB;
/* 107 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/* 109 */     this.poaWrapper = POASystemException.get(paramORB, "rpc.protocol");
/*     */   }
/*     */ 
/*     */   public IOR locate(ObjectKey paramObjectKey)
/*     */   {
/*     */     try
/*     */     {
/* 139 */       if (this.orb.subcontractDebugFlag) {
/* 140 */         dprint(".locate->");
/*     */       }
/* 142 */       ObjectKeyTemplate localObjectKeyTemplate = paramObjectKey.getTemplate();
/*     */       try
/*     */       {
/* 145 */         checkServerId(paramObjectKey);
/*     */       } catch (ForwardException localForwardException) {
/* 147 */         return localForwardException.getIOR();
/*     */       }
/*     */ 
/* 151 */       findObjectAdapter(localObjectKeyTemplate);
/*     */ 
/* 153 */       return null;
/*     */     } finally {
/* 155 */       if (this.orb.subcontractDebugFlag)
/* 156 */         dprint(".locate<-");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void dispatch(MessageMediator paramMessageMediator)
/*     */   {
/* 162 */     CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)paramMessageMediator;
/*     */     try {
/* 164 */       if (this.orb.subcontractDebugFlag) {
/* 165 */         dprint(".dispatch->: " + opAndId(localCorbaMessageMediator));
/*     */       }
/*     */ 
/* 170 */       consumeServiceContexts(localCorbaMessageMediator);
/*     */ 
/* 175 */       ((MarshalInputStream)localCorbaMessageMediator.getInputObject()).performORBVersionSpecificInit();
/*     */ 
/* 178 */       ObjectKey localObjectKey = localCorbaMessageMediator.getObjectKey();
/*     */       try
/*     */       {
/* 182 */         checkServerId(localObjectKey);
/*     */       } catch (ForwardException localForwardException1) {
/* 184 */         if (this.orb.subcontractDebugFlag) {
/* 185 */           dprint(".dispatch: " + opAndId(localCorbaMessageMediator) + ": bad server id");
/*     */         }
/*     */ 
/* 189 */         localCorbaMessageMediator.getProtocolHandler().createLocationForward(localCorbaMessageMediator, localForwardException1.getIOR(), null);
/*     */         return;
/*     */       }
/*     */ 
/* 194 */       String str = localCorbaMessageMediator.getOperationName();
/* 195 */       ObjectAdapter localObjectAdapter = null;
/*     */       try
/*     */       {
/* 198 */         byte[] arrayOfByte = localObjectKey.getId().getId();
/* 199 */         localObject1 = localObjectKey.getTemplate();
/* 200 */         localObjectAdapter = findObjectAdapter((ObjectKeyTemplate)localObject1);
/*     */ 
/* 202 */         localObject2 = getServantWithPI(localCorbaMessageMediator, localObjectAdapter, arrayOfByte, (ObjectKeyTemplate)localObject1, str);
/*     */ 
/* 205 */         dispatchToServant(localObject2, localCorbaMessageMediator, arrayOfByte, localObjectAdapter);
/*     */       } catch (ForwardException localForwardException2) {
/* 207 */         if (this.orb.subcontractDebugFlag) {
/* 208 */           dprint(".dispatch: " + opAndId(localCorbaMessageMediator) + ": ForwardException caught");
/*     */         }
/*     */ 
/* 214 */         localCorbaMessageMediator.getProtocolHandler().createLocationForward(localCorbaMessageMediator, localForwardException2.getIOR(), null);
/*     */       }
/*     */       catch (OADestroyed localOADestroyed) {
/* 217 */         if (this.orb.subcontractDebugFlag) {
/* 218 */           dprint(".dispatch: " + opAndId(localCorbaMessageMediator) + ": OADestroyed exception caught");
/*     */         }
/*     */ 
/* 230 */         dispatch(localCorbaMessageMediator);
/*     */       } catch (RequestCanceledException localRequestCanceledException) {
/* 232 */         if (this.orb.subcontractDebugFlag) {
/* 233 */           dprint(".dispatch: " + opAndId(localCorbaMessageMediator) + ": RequestCanceledException caught");
/*     */         }
/*     */ 
/* 241 */         throw localRequestCanceledException;
/*     */       } catch (UnknownException localUnknownException) {
/* 243 */         if (this.orb.subcontractDebugFlag) {
/* 244 */           dprint(".dispatch: " + opAndId(localCorbaMessageMediator) + ": UnknownException caught " + localUnknownException);
/*     */         }
/*     */ 
/* 254 */         if ((localUnknownException.originalEx instanceof RequestCanceledException)) {
/* 255 */           throw ((RequestCanceledException)localUnknownException.originalEx);
/*     */         }
/*     */ 
/* 258 */         Object localObject1 = new ServiceContexts(this.orb);
/* 259 */         Object localObject2 = new UEInfoServiceContext(localUnknownException.originalEx);
/*     */ 
/* 262 */         ((ServiceContexts)localObject1).put((ServiceContext)localObject2);
/*     */ 
/* 264 */         UNKNOWN localUNKNOWN = this.wrapper.unknownExceptionInDispatch(CompletionStatus.COMPLETED_MAYBE, localUnknownException);
/*     */ 
/* 266 */         localCorbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(localCorbaMessageMediator, localUNKNOWN, (ServiceContexts)localObject1);
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 270 */         if (this.orb.subcontractDebugFlag) {
/* 271 */           dprint(".dispatch: " + opAndId(localCorbaMessageMediator) + ": other exception " + localThrowable);
/*     */         }
/*     */ 
/* 274 */         localCorbaMessageMediator.getProtocolHandler().handleThrowableDuringServerDispatch(localCorbaMessageMediator, localThrowable, CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 280 */       if (this.orb.subcontractDebugFlag)
/* 281 */         dprint(".dispatch<-: " + opAndId(localCorbaMessageMediator));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void releaseServant(ObjectAdapter paramObjectAdapter)
/*     */   {
/*     */     try
/*     */     {
/* 289 */       if (this.orb.subcontractDebugFlag) {
/* 290 */         dprint(".releaseServant->");
/*     */       }
/*     */ 
/* 293 */       if (paramObjectAdapter == null) {
/* 294 */         if (this.orb.subcontractDebugFlag) {
/* 295 */           dprint(".releaseServant: null object adapter");
/*     */         }
/*     */       }
/*     */       else
/*     */         try
/*     */         {
/* 301 */           paramObjectAdapter.returnServant();
/*     */         } finally {
/* 303 */           paramObjectAdapter.exit();
/* 304 */           this.orb.popInvocationInfo();
/*     */         }
/*     */     } finally {
/* 307 */       if (this.orb.subcontractDebugFlag)
/* 308 */         dprint(".releaseServant<-");
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object getServant(ObjectAdapter paramObjectAdapter, byte[] paramArrayOfByte, String paramString)
/*     */     throws OADestroyed
/*     */   {
/*     */     try
/*     */     {
/* 319 */       if (this.orb.subcontractDebugFlag) {
/* 320 */         dprint(".getServant->");
/*     */       }
/*     */ 
/* 323 */       OAInvocationInfo localOAInvocationInfo = paramObjectAdapter.makeInvocationInfo(paramArrayOfByte);
/* 324 */       localOAInvocationInfo.setOperation(paramString);
/* 325 */       this.orb.pushInvocationInfo(localOAInvocationInfo);
/* 326 */       paramObjectAdapter.getInvocationServant(localOAInvocationInfo);
/* 327 */       return localOAInvocationInfo.getServantContainer();
/*     */     } finally {
/* 329 */       if (this.orb.subcontractDebugFlag)
/* 330 */         dprint(".getServant<-");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object getServantWithPI(CorbaMessageMediator paramCorbaMessageMediator, ObjectAdapter paramObjectAdapter, byte[] paramArrayOfByte, ObjectKeyTemplate paramObjectKeyTemplate, String paramString)
/*     */     throws OADestroyed
/*     */   {
/*     */     try
/*     */     {
/* 341 */       if (this.orb.subcontractDebugFlag) {
/* 342 */         dprint(".getServantWithPI->");
/*     */       }
/*     */ 
/* 348 */       this.orb.getPIHandler().initializeServerPIInfo(paramCorbaMessageMediator, paramObjectAdapter, paramArrayOfByte, paramObjectKeyTemplate);
/*     */ 
/* 350 */       this.orb.getPIHandler().invokeServerPIStartingPoint();
/*     */ 
/* 352 */       paramObjectAdapter.enter();
/*     */ 
/* 357 */       if (paramCorbaMessageMediator != null) {
/* 358 */         paramCorbaMessageMediator.setExecuteReturnServantInResponseConstructor(true);
/*     */       }
/* 360 */       Object localObject1 = getServant(paramObjectAdapter, paramArrayOfByte, paramString);
/*     */ 
/* 367 */       String str = "unknown";
/*     */ 
/* 369 */       if ((localObject1 instanceof NullServant))
/* 370 */         handleNullServant(paramString, (NullServant)localObject1);
/*     */       else {
/* 372 */         str = paramObjectAdapter.getInterfaces(localObject1, paramArrayOfByte)[0];
/*     */       }
/* 374 */       this.orb.getPIHandler().setServerPIInfo(localObject1, str);
/*     */ 
/* 376 */       if (((localObject1 != null) && (!(localObject1 instanceof org.omg.CORBA.DynamicImplementation)) && (!(localObject1 instanceof org.omg.PortableServer.DynamicImplementation))) || (SpecialMethod.getSpecialMethod(paramString) != null))
/*     */       {
/* 380 */         this.orb.getPIHandler().invokeServerPIIntermediatePoint();
/*     */       }
/*     */ 
/* 383 */       return localObject1;
/*     */     } finally {
/* 385 */       if (this.orb.subcontractDebugFlag)
/* 386 */         dprint(".getServantWithPI<-");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void checkServerId(ObjectKey paramObjectKey)
/*     */   {
/*     */     try
/*     */     {
/* 394 */       if (this.orb.subcontractDebugFlag) {
/* 395 */         dprint(".checkServerId->");
/*     */       }
/*     */ 
/* 398 */       ObjectKeyTemplate localObjectKeyTemplate = paramObjectKey.getTemplate();
/* 399 */       int i = localObjectKeyTemplate.getServerId();
/* 400 */       int j = localObjectKeyTemplate.getSubcontractId();
/*     */ 
/* 402 */       if (!this.orb.isLocalServerId(j, i)) {
/* 403 */         if (this.orb.subcontractDebugFlag) {
/* 404 */           dprint(".checkServerId: bad server id");
/*     */         }
/*     */ 
/* 407 */         this.orb.handleBadServerId(paramObjectKey);
/*     */       }
/*     */     } finally {
/* 410 */       if (this.orb.subcontractDebugFlag)
/* 411 */         dprint(".checkServerId<-");
/*     */     }
/*     */   }
/*     */ 
/*     */   private ObjectAdapter findObjectAdapter(ObjectKeyTemplate paramObjectKeyTemplate)
/*     */   {
/*     */     try
/*     */     {
/* 419 */       if (this.orb.subcontractDebugFlag) {
/* 420 */         dprint(".findObjectAdapter->");
/*     */       }
/*     */ 
/* 423 */       RequestDispatcherRegistry localRequestDispatcherRegistry = this.orb.getRequestDispatcherRegistry();
/* 424 */       int i = paramObjectKeyTemplate.getSubcontractId();
/* 425 */       ObjectAdapterFactory localObjectAdapterFactory = localRequestDispatcherRegistry.getObjectAdapterFactory(i);
/* 426 */       if (localObjectAdapterFactory == null)
/*     */       {
/* 431 */         throw this.wrapper.noObjectAdapterFactory();
/*     */       }
/*     */ 
/* 434 */       ObjectAdapterId localObjectAdapterId = paramObjectKeyTemplate.getObjectAdapterId();
/* 435 */       ObjectAdapter localObjectAdapter1 = localObjectAdapterFactory.find(localObjectAdapterId);
/*     */ 
/* 437 */       if (localObjectAdapter1 == null)
/*     */       {
/* 442 */         throw this.wrapper.badAdapterId();
/*     */       }
/*     */ 
/* 445 */       return localObjectAdapter1;
/*     */     } finally {
/* 447 */       if (this.orb.subcontractDebugFlag)
/* 448 */         dprint(".findObjectAdapter<-");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleNullServant(String paramString, NullServant paramNullServant)
/*     */   {
/*     */     try
/*     */     {
/* 462 */       if (this.orb.subcontractDebugFlag) {
/* 463 */         dprint(".handleNullServant->: " + paramString);
/*     */       }
/*     */ 
/* 466 */       SpecialMethod localSpecialMethod = SpecialMethod.getSpecialMethod(paramString);
/*     */ 
/* 469 */       if ((localSpecialMethod == null) || (!localSpecialMethod.isNonExistentMethod()))
/*     */       {
/* 471 */         if (this.orb.subcontractDebugFlag) {
/* 472 */           dprint(".handleNullServant: " + paramString + ": throwing OBJECT_NOT_EXIST");
/*     */         }
/*     */ 
/* 476 */         throw paramNullServant.getException();
/*     */       }
/*     */     } finally {
/* 479 */       if (this.orb.subcontractDebugFlag)
/* 480 */         dprint(".handleNullServant<-: " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void consumeServiceContexts(CorbaMessageMediator paramCorbaMessageMediator)
/*     */   {
/*     */     try
/*     */     {
/* 488 */       if (this.orb.subcontractDebugFlag) {
/* 489 */         dprint(".consumeServiceContexts->: " + opAndId(paramCorbaMessageMediator));
/*     */       }
/*     */ 
/* 493 */       ServiceContexts localServiceContexts = paramCorbaMessageMediator.getRequestServiceContexts();
/*     */ 
/* 496 */       GIOPVersion localGIOPVersion = paramCorbaMessageMediator.getGIOPVersion();
/*     */ 
/* 502 */       boolean bool = processCodeSetContext(paramCorbaMessageMediator, localServiceContexts);
/*     */ 
/* 504 */       if (this.orb.subcontractDebugFlag) {
/* 505 */         dprint(".consumeServiceContexts: " + opAndId(paramCorbaMessageMediator) + ": GIOP version: " + localGIOPVersion);
/*     */ 
/* 507 */         dprint(".consumeServiceContexts: " + opAndId(paramCorbaMessageMediator) + ": as code set context? " + bool);
/*     */       }
/*     */ 
/* 511 */       ServiceContext localServiceContext = localServiceContexts.get(6);
/*     */       Object localObject1;
/* 514 */       if (localServiceContext != null) {
/* 515 */         SendingContextServiceContext localSendingContextServiceContext = (SendingContextServiceContext)localServiceContext;
/*     */ 
/* 517 */         localObject1 = localSendingContextServiceContext.getIOR();
/*     */         try
/*     */         {
/* 520 */           ((CorbaConnection)paramCorbaMessageMediator.getConnection()).setCodeBaseIOR((IOR)localObject1);
/*     */         }
/*     */         catch (ThreadDeath localThreadDeath) {
/* 523 */           throw localThreadDeath;
/*     */         } catch (Throwable localThrowable) {
/* 525 */           throw this.wrapper.badStringifiedIor(localThrowable);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 545 */       int i = 0;
/*     */ 
/* 547 */       if ((localGIOPVersion.equals(GIOPVersion.V1_0)) && (bool)) {
/* 548 */         if (this.orb.subcontractDebugFlag) {
/* 549 */           dprint(".consumeServiceCOntexts: " + opAndId(paramCorbaMessageMediator) + ": Determined to be an old Sun ORB");
/*     */         }
/*     */ 
/* 553 */         this.orb.setORBVersion(ORBVersionFactory.getOLD());
/*     */       }
/*     */       else
/*     */       {
/* 558 */         i = 1;
/*     */       }
/*     */ 
/* 563 */       localServiceContext = localServiceContexts.get(1313165056);
/* 564 */       if (localServiceContext != null) {
/* 565 */         localObject1 = (ORBVersionServiceContext)localServiceContext;
/*     */ 
/* 568 */         ORBVersion localORBVersion = ((ORBVersionServiceContext)localObject1).getVersion();
/* 569 */         this.orb.setORBVersion(localORBVersion);
/*     */ 
/* 571 */         i = 0;
/*     */       }
/*     */ 
/* 574 */       if (i != 0) {
/* 575 */         if (this.orb.subcontractDebugFlag) {
/* 576 */           dprint(".consumeServiceContexts: " + opAndId(paramCorbaMessageMediator) + ": Determined to be a foreign ORB");
/*     */         }
/*     */ 
/* 580 */         this.orb.setORBVersion(ORBVersionFactory.getFOREIGN());
/*     */       }
/*     */     } finally {
/* 583 */       if (this.orb.subcontractDebugFlag)
/* 584 */         dprint(".consumeServiceContexts<-: " + opAndId(paramCorbaMessageMediator));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected CorbaMessageMediator dispatchToServant(Object paramObject, CorbaMessageMediator paramCorbaMessageMediator, byte[] paramArrayOfByte, ObjectAdapter paramObjectAdapter)
/*     */   {
/*     */     try
/*     */     {
/* 595 */       if (this.orb.subcontractDebugFlag) {
/* 596 */         dprint(".dispatchToServant->: " + opAndId(paramCorbaMessageMediator));
/*     */       }
/*     */ 
/* 599 */       CorbaMessageMediator localCorbaMessageMediator = null;
/*     */ 
/* 601 */       String str = paramCorbaMessageMediator.getOperationName();
/*     */ 
/* 603 */       SpecialMethod localSpecialMethod = SpecialMethod.getSpecialMethod(str);
/*     */       Object localObject1;
/* 604 */       if (localSpecialMethod != null) {
/* 605 */         if (this.orb.subcontractDebugFlag) {
/* 606 */           dprint(".dispatchToServant: " + opAndId(paramCorbaMessageMediator) + ": Handling special method");
/*     */         }
/*     */ 
/* 610 */         localCorbaMessageMediator = localSpecialMethod.invoke(paramObject, paramCorbaMessageMediator, paramArrayOfByte, paramObjectAdapter);
/* 611 */         return localCorbaMessageMediator;
/*     */       }
/*     */       Object localObject2;
/* 615 */       if ((paramObject instanceof org.omg.CORBA.DynamicImplementation)) {
/* 616 */         if (this.orb.subcontractDebugFlag) {
/* 617 */           dprint(".dispatchToServant: " + opAndId(paramCorbaMessageMediator) + ": Handling old style DSI type servant");
/*     */         }
/*     */ 
/* 621 */         localObject1 = (org.omg.CORBA.DynamicImplementation)paramObject;
/*     */ 
/* 623 */         localObject2 = new ServerRequestImpl(paramCorbaMessageMediator, this.orb);
/*     */ 
/* 627 */         ((org.omg.CORBA.DynamicImplementation)localObject1).invoke((ServerRequest)localObject2);
/*     */ 
/* 629 */         localCorbaMessageMediator = handleDynamicResult((ServerRequestImpl)localObject2, paramCorbaMessageMediator);
/* 630 */       } else if ((paramObject instanceof org.omg.PortableServer.DynamicImplementation)) {
/* 631 */         if (this.orb.subcontractDebugFlag) {
/* 632 */           dprint(".dispatchToServant: " + opAndId(paramCorbaMessageMediator) + ": Handling POA DSI type servant");
/*     */         }
/*     */ 
/* 636 */         localObject1 = (org.omg.PortableServer.DynamicImplementation)paramObject;
/*     */ 
/* 638 */         localObject2 = new ServerRequestImpl(paramCorbaMessageMediator, this.orb);
/*     */ 
/* 642 */         ((org.omg.PortableServer.DynamicImplementation)localObject1).invoke((ServerRequest)localObject2);
/*     */ 
/* 644 */         localCorbaMessageMediator = handleDynamicResult((ServerRequestImpl)localObject2, paramCorbaMessageMediator);
/*     */       } else {
/* 646 */         if (this.orb.subcontractDebugFlag) {
/* 647 */           dprint(".dispatchToServant: " + opAndId(paramCorbaMessageMediator) + ": Handling invoke handler type servant");
/*     */         }
/*     */ 
/* 651 */         localObject1 = (InvokeHandler)paramObject;
/*     */ 
/* 653 */         localObject2 = ((InvokeHandler)localObject1)._invoke(str, (InputStream)paramCorbaMessageMediator.getInputObject(), paramCorbaMessageMediator);
/*     */ 
/* 658 */         localCorbaMessageMediator = (CorbaMessageMediator)((OutputObject)localObject2).getMessageMediator();
/*     */       }
/*     */ 
/* 662 */       return localCorbaMessageMediator;
/*     */     } finally {
/* 664 */       if (this.orb.subcontractDebugFlag)
/* 665 */         dprint(".dispatchToServant<-: " + opAndId(paramCorbaMessageMediator));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected CorbaMessageMediator handleDynamicResult(ServerRequestImpl paramServerRequestImpl, CorbaMessageMediator paramCorbaMessageMediator)
/*     */   {
/*     */     try
/*     */     {
/* 675 */       if (this.orb.subcontractDebugFlag) {
/* 676 */         dprint(".handleDynamicResult->: " + opAndId(paramCorbaMessageMediator));
/*     */       }
/*     */ 
/* 679 */       CorbaMessageMediator localCorbaMessageMediator = null;
/*     */ 
/* 682 */       Any localAny = paramServerRequestImpl.checkResultCalled();
/*     */       Object localObject1;
/* 684 */       if (localAny == null) {
/* 685 */         if (this.orb.subcontractDebugFlag) {
/* 686 */           dprint(".handleDynamicResult: " + opAndId(paramCorbaMessageMediator) + ": handling normal result");
/*     */         }
/*     */ 
/* 691 */         localCorbaMessageMediator = sendingReply(paramCorbaMessageMediator);
/* 692 */         localObject1 = (OutputStream)localCorbaMessageMediator.getOutputObject();
/* 693 */         paramServerRequestImpl.marshalReplyParams((OutputStream)localObject1);
/*     */       } else {
/* 695 */         if (this.orb.subcontractDebugFlag) {
/* 696 */           dprint(".handleDynamicResult: " + opAndId(paramCorbaMessageMediator) + ": handling error");
/*     */         }
/*     */ 
/* 700 */         localCorbaMessageMediator = sendingReply(paramCorbaMessageMediator, localAny);
/*     */       }
/*     */ 
/* 703 */       return localCorbaMessageMediator;
/*     */     } finally {
/* 705 */       if (this.orb.subcontractDebugFlag)
/* 706 */         dprint(".handleDynamicResult<-: " + opAndId(paramCorbaMessageMediator));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected CorbaMessageMediator sendingReply(CorbaMessageMediator paramCorbaMessageMediator)
/*     */   {
/*     */     try
/*     */     {
/* 714 */       if (this.orb.subcontractDebugFlag) {
/* 715 */         dprint(".sendingReply->: " + opAndId(paramCorbaMessageMediator));
/*     */       }
/*     */ 
/* 718 */       ServiceContexts localServiceContexts = new ServiceContexts(this.orb);
/* 719 */       return paramCorbaMessageMediator.getProtocolHandler().createResponse(paramCorbaMessageMediator, localServiceContexts);
/*     */     } finally {
/* 721 */       if (this.orb.subcontractDebugFlag)
/* 722 */         dprint(".sendingReply<-: " + opAndId(paramCorbaMessageMediator));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected CorbaMessageMediator sendingReply(CorbaMessageMediator paramCorbaMessageMediator, Any paramAny)
/*     */   {
/*     */     try
/*     */     {
/* 734 */       if (this.orb.subcontractDebugFlag) {
/* 735 */         dprint(".sendingReply/Any->: " + opAndId(paramCorbaMessageMediator));
/*     */       }
/*     */ 
/* 738 */       ServiceContexts localServiceContexts = new ServiceContexts(this.orb);
/*     */ 
/* 743 */       String str = null;
/*     */       try {
/* 745 */         str = paramAny.type().id();
/*     */       } catch (BadKind localBadKind) {
/* 747 */         throw this.wrapper.problemWithExceptionTypecode(localBadKind);
/*     */       }
/*     */       Object localObject1;
/*     */       CorbaMessageMediator localCorbaMessageMediator;
/* 750 */       if (ORBUtility.isSystemException(str)) {
/* 751 */         if (this.orb.subcontractDebugFlag) {
/* 752 */           dprint(".sendingReply/Any: " + opAndId(paramCorbaMessageMediator) + ": handling system exception");
/*     */         }
/*     */ 
/* 757 */         localObject1 = paramAny.create_input_stream();
/* 758 */         SystemException localSystemException = ORBUtility.readSystemException((InputStream)localObject1);
/*     */ 
/* 760 */         localCorbaMessageMediator = paramCorbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(paramCorbaMessageMediator, localSystemException, localServiceContexts);
/*     */       }
/*     */       else {
/* 763 */         if (this.orb.subcontractDebugFlag) {
/* 764 */           dprint(".sendingReply/Any: " + opAndId(paramCorbaMessageMediator) + ": handling user exception");
/*     */         }
/*     */ 
/* 768 */         localCorbaMessageMediator = paramCorbaMessageMediator.getProtocolHandler().createUserExceptionResponse(paramCorbaMessageMediator, localServiceContexts);
/*     */ 
/* 770 */         localObject1 = (OutputStream)localCorbaMessageMediator.getOutputObject();
/* 771 */         paramAny.write_value((OutputStream)localObject1);
/*     */       }
/*     */ 
/* 774 */       return localCorbaMessageMediator;
/*     */     } finally {
/* 776 */       if (this.orb.subcontractDebugFlag)
/* 777 */         dprint(".sendingReply/Any<-: " + opAndId(paramCorbaMessageMediator));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean processCodeSetContext(CorbaMessageMediator paramCorbaMessageMediator, ServiceContexts paramServiceContexts)
/*     */   {
/*     */     try
/*     */     {
/* 791 */       if (this.orb.subcontractDebugFlag) {
/* 792 */         dprint(".processCodeSetContext->: " + opAndId(paramCorbaMessageMediator));
/*     */       }
/*     */ 
/* 795 */       ServiceContext localServiceContext = paramServiceContexts.get(1);
/*     */ 
/* 797 */       if (localServiceContext != null)
/*     */       {
/*     */         boolean bool1;
/* 799 */         if (paramCorbaMessageMediator.getConnection() == null) {
/* 800 */           return true;
/*     */         }
/*     */ 
/* 806 */         if (paramCorbaMessageMediator.getGIOPVersion().equals(GIOPVersion.V1_0)) {
/* 807 */           return true;
/*     */         }
/*     */ 
/* 810 */         CodeSetServiceContext localCodeSetServiceContext = (CodeSetServiceContext)localServiceContext;
/* 811 */         CodeSetComponentInfo.CodeSetContext localCodeSetContext = localCodeSetServiceContext.getCodeSetContext();
/*     */ 
/* 826 */         if (((CorbaConnection)paramCorbaMessageMediator.getConnection()).getCodeSetContext() == null)
/*     */         {
/* 831 */           if (this.orb.subcontractDebugFlag) {
/* 832 */             dprint(".processCodeSetContext: " + opAndId(paramCorbaMessageMediator) + ": Setting code sets to: " + localCodeSetContext);
/*     */           }
/*     */ 
/* 836 */           ((CorbaConnection)paramCorbaMessageMediator.getConnection()).setCodeSetContext(localCodeSetContext);
/*     */ 
/* 851 */           if (localCodeSetContext.getCharCodeSet() != OSFCodeSetRegistry.ISO_8859_1.getNumber())
/*     */           {
/* 853 */             ((MarshalInputStream)paramCorbaMessageMediator.getInputObject()).resetCodeSetConverters();
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 866 */       return localServiceContext != null;
/*     */     } finally {
/* 868 */       if (this.orb.subcontractDebugFlag)
/* 869 */         dprint(".processCodeSetContext<-: " + opAndId(paramCorbaMessageMediator));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 876 */     ORBUtility.dprint("CorbaServerRequestDispatcherImpl", paramString);
/*     */   }
/*     */ 
/*     */   protected String opAndId(CorbaMessageMediator paramCorbaMessageMediator)
/*     */   {
/* 881 */     return ORBUtility.operationNameAndRequestId(paramCorbaMessageMediator);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.CorbaServerRequestDispatcherImpl
 * JD-Core Version:    0.6.2
 */