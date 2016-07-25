/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.RequestImpl;
/*     */ import com.sun.corba.se.impl.logging.InterceptorsSystemException;
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.orbutil.closure.ClosureFactory;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.protocol.ForwardException;
/*     */ import com.sun.corba.se.spi.protocol.PIHandler;
/*     */ import com.sun.corba.se.spi.protocol.RetryType;
/*     */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Stack;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.NVList;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.CORBA.PolicyError;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.UserException;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ import org.omg.IOP.CodecFactory;
/*     */ import org.omg.PortableInterceptor.Current;
/*     */ import org.omg.PortableInterceptor.Interceptor;
/*     */ import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
/*     */ import org.omg.PortableInterceptor.ORBInitializer;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceTemplate;
/*     */ import org.omg.PortableInterceptor.PolicyFactory;
/*     */ 
/*     */ public class PIHandlerImpl
/*     */   implements PIHandler
/*     */ {
/*  91 */   boolean printPushPopEnabled = false;
/*  92 */   int pushLevel = 0;
/*     */   private ORB orb;
/*     */   InterceptorsSystemException wrapper;
/*     */   ORBUtilSystemException orbutilWrapper;
/*     */   OMGSystemException omgWrapper;
/* 121 */   private int serverRequestIdCounter = 0;
/*     */ 
/* 124 */   CodecFactory codecFactory = null;
/*     */ 
/* 128 */   String[] arguments = null;
/*     */   private InterceptorList interceptorList;
/*     */   private boolean hasIORInterceptors;
/*     */   private boolean hasClientInterceptors;
/*     */   private boolean hasServerInterceptors;
/*     */   private InterceptorInvoker interceptorInvoker;
/*     */   private PICurrent current;
/*     */   private HashMap policyFactoryTable;
/* 154 */   private static final short[] REPLY_MESSAGE_TO_PI_REPLY_STATUS = { 0, 2, 1, 3, 3, 4 };
/*     */ 
/* 165 */   private ThreadLocal threadLocalClientRequestInfoStack = new ThreadLocal()
/*     */   {
/*     */     protected Object initialValue() {
/* 168 */       return new PIHandlerImpl.RequestInfoStack(PIHandlerImpl.this, null);
/*     */     }
/* 165 */   };
/*     */ 
/* 173 */   private ThreadLocal threadLocalServerRequestInfoStack = new ThreadLocal()
/*     */   {
/*     */     protected Object initialValue() {
/* 176 */       return new PIHandlerImpl.RequestInfoStack(PIHandlerImpl.this, null);
/*     */     }
/* 173 */   };
/*     */ 
/*     */   private void printPush()
/*     */   {
/*  95 */     if (!this.printPushPopEnabled) return;
/*  96 */     printSpaces(this.pushLevel);
/*  97 */     this.pushLevel += 1;
/*  98 */     System.out.println("PUSH");
/*     */   }
/*     */ 
/*     */   private void printPop() {
/* 102 */     if (!this.printPushPopEnabled) return;
/* 103 */     this.pushLevel -= 1;
/* 104 */     printSpaces(this.pushLevel);
/* 105 */     System.out.println("POP");
/*     */   }
/*     */ 
/*     */   private void printSpaces(int paramInt) {
/* 109 */     for (int i = 0; i < paramInt; i++)
/* 110 */       System.out.print(" ");
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 181 */     this.orb = null;
/* 182 */     this.wrapper = null;
/* 183 */     this.orbutilWrapper = null;
/* 184 */     this.omgWrapper = null;
/* 185 */     this.codecFactory = null;
/* 186 */     this.arguments = null;
/* 187 */     this.interceptorList = null;
/* 188 */     this.interceptorInvoker = null;
/* 189 */     this.current = null;
/* 190 */     this.policyFactoryTable = null;
/* 191 */     this.threadLocalClientRequestInfoStack = null;
/* 192 */     this.threadLocalServerRequestInfoStack = null;
/*     */   }
/*     */ 
/*     */   public PIHandlerImpl(ORB paramORB, String[] paramArrayOfString)
/*     */   {
/* 209 */     this.orb = paramORB;
/* 210 */     this.wrapper = InterceptorsSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/* 212 */     this.orbutilWrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/* 214 */     this.omgWrapper = OMGSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/* 216 */     this.arguments = paramArrayOfString;
/*     */ 
/* 219 */     this.codecFactory = new CodecFactoryImpl(paramORB);
/*     */ 
/* 222 */     this.interceptorList = new InterceptorList(this.wrapper);
/*     */ 
/* 225 */     this.current = new PICurrent(paramORB);
/*     */ 
/* 228 */     this.interceptorInvoker = new InterceptorInvoker(paramORB, this.interceptorList, this.current);
/*     */ 
/* 232 */     paramORB.getLocalResolver().register("PICurrent", ClosureFactory.makeConstant(this.current));
/*     */ 
/* 234 */     paramORB.getLocalResolver().register("CodecFactory", ClosureFactory.makeConstant(this.codecFactory));
/*     */   }
/*     */ 
/*     */   public void initialize()
/*     */   {
/* 240 */     if (this.orb.getORBData().getORBInitializers() != null)
/*     */     {
/* 242 */       ORBInitInfoImpl localORBInitInfoImpl = createORBInitInfo();
/*     */ 
/* 246 */       this.current.setORBInitializing(true);
/*     */ 
/* 249 */       preInitORBInitializers(localORBInitInfoImpl);
/*     */ 
/* 252 */       postInitORBInitializers(localORBInitInfoImpl);
/*     */ 
/* 255 */       this.interceptorList.sortInterceptors();
/*     */ 
/* 259 */       this.current.setORBInitializing(false);
/*     */ 
/* 262 */       localORBInitInfoImpl.setStage(2);
/*     */ 
/* 266 */       this.hasIORInterceptors = this.interceptorList.hasInterceptorsOfType(2);
/*     */ 
/* 275 */       this.hasClientInterceptors = true;
/* 276 */       this.hasServerInterceptors = this.interceptorList.hasInterceptorsOfType(1);
/*     */ 
/* 282 */       this.interceptorInvoker.setEnabled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroyInterceptors()
/*     */   {
/* 296 */     this.interceptorList.destroyAll();
/*     */   }
/*     */ 
/*     */   public void objectAdapterCreated(ObjectAdapter paramObjectAdapter)
/*     */   {
/* 301 */     if (!this.hasIORInterceptors) {
/* 302 */       return;
/*     */     }
/* 304 */     this.interceptorInvoker.objectAdapterCreated(paramObjectAdapter);
/*     */   }
/*     */ 
/*     */   public void adapterManagerStateChanged(int paramInt, short paramShort)
/*     */   {
/* 310 */     if (!this.hasIORInterceptors) {
/* 311 */       return;
/*     */     }
/* 313 */     this.interceptorInvoker.adapterManagerStateChanged(paramInt, paramShort);
/*     */   }
/*     */ 
/*     */   public void adapterStateChanged(ObjectReferenceTemplate[] paramArrayOfObjectReferenceTemplate, short paramShort)
/*     */   {
/* 319 */     if (!this.hasIORInterceptors) {
/* 320 */       return;
/*     */     }
/* 322 */     this.interceptorInvoker.adapterStateChanged(paramArrayOfObjectReferenceTemplate, paramShort);
/*     */   }
/*     */ 
/*     */   public void disableInterceptorsThisThread()
/*     */   {
/* 331 */     if (!this.hasClientInterceptors) return;
/*     */ 
/* 333 */     RequestInfoStack localRequestInfoStack = (RequestInfoStack)this.threadLocalClientRequestInfoStack.get();
/*     */ 
/* 335 */     localRequestInfoStack.disableCount += 1;
/*     */   }
/*     */ 
/*     */   public void enableInterceptorsThisThread() {
/* 339 */     if (!this.hasClientInterceptors) return;
/*     */ 
/* 341 */     RequestInfoStack localRequestInfoStack = (RequestInfoStack)this.threadLocalClientRequestInfoStack.get();
/*     */ 
/* 343 */     localRequestInfoStack.disableCount -= 1;
/*     */   }
/*     */ 
/*     */   public void invokeClientPIStartingPoint()
/*     */     throws RemarshalException
/*     */   {
/* 349 */     if (!this.hasClientInterceptors) return;
/* 350 */     if (!isClientPIEnabledForThisThread()) return;
/*     */ 
/* 354 */     ClientRequestInfoImpl localClientRequestInfoImpl = peekClientRequestInfoImplStack();
/* 355 */     this.interceptorInvoker.invokeClientInterceptorStartingPoint(localClientRequestInfoImpl);
/*     */ 
/* 359 */     short s = localClientRequestInfoImpl.getReplyStatus();
/* 360 */     if ((s == 1) || (s == 3))
/*     */     {
/* 366 */       Exception localException = invokeClientPIEndingPoint(convertPIReplyStatusToReplyMessage(s), localClientRequestInfoImpl.getException());
/*     */ 
/* 369 */       if ((localException != null) || 
/* 372 */         ((localException instanceof SystemException)))
/* 373 */         throw ((SystemException)localException);
/* 374 */       if ((localException instanceof RemarshalException))
/* 375 */         throw ((RemarshalException)localException);
/* 376 */       if (((localException instanceof UserException)) || ((localException instanceof ApplicationException)))
/*     */       {
/* 382 */         throw this.wrapper.exceptionInvalid();
/*     */       }
/*     */     }
/* 385 */     else if (s != -1) {
/* 386 */       throw this.wrapper.replyStatusNotInit();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Exception makeCompletedClientRequest(int paramInt, Exception paramException)
/*     */   {
/* 396 */     return handleClientPIEndingPoint(paramInt, paramException, false);
/*     */   }
/*     */ 
/*     */   public Exception invokeClientPIEndingPoint(int paramInt, Exception paramException)
/*     */   {
/* 403 */     return handleClientPIEndingPoint(paramInt, paramException, true);
/*     */   }
/*     */ 
/*     */   public Exception handleClientPIEndingPoint(int paramInt, Exception paramException, boolean paramBoolean)
/*     */   {
/* 408 */     if (!this.hasClientInterceptors) return paramException;
/* 409 */     if (!isClientPIEnabledForThisThread()) return paramException;
/*     */ 
/* 414 */     short s = REPLY_MESSAGE_TO_PI_REPLY_STATUS[paramInt];
/*     */ 
/* 418 */     ClientRequestInfoImpl localClientRequestInfoImpl = peekClientRequestInfoImplStack();
/* 419 */     localClientRequestInfoImpl.setReplyStatus(s);
/* 420 */     localClientRequestInfoImpl.setException(paramException);
/*     */ 
/* 422 */     if (paramBoolean)
/*     */     {
/* 424 */       this.interceptorInvoker.invokeClientInterceptorEndingPoint(localClientRequestInfoImpl);
/* 425 */       s = localClientRequestInfoImpl.getReplyStatus();
/*     */     }
/*     */ 
/* 429 */     if ((s == 3) || (s == 4))
/*     */     {
/* 433 */       localClientRequestInfoImpl.reset();
/*     */ 
/* 436 */       if (paramBoolean)
/* 437 */         localClientRequestInfoImpl.setRetryRequest(RetryType.AFTER_RESPONSE);
/*     */       else {
/* 439 */         localClientRequestInfoImpl.setRetryRequest(RetryType.BEFORE_RESPONSE);
/*     */       }
/*     */ 
/* 443 */       paramException = new RemarshalException();
/* 444 */     } else if ((s == 1) || (s == 2))
/*     */     {
/* 446 */       paramException = localClientRequestInfoImpl.getException();
/*     */     }
/*     */ 
/* 449 */     return paramException;
/*     */   }
/*     */ 
/*     */   public void initiateClientPIRequest(boolean paramBoolean) {
/* 453 */     if (!this.hasClientInterceptors) return;
/* 454 */     if (!isClientPIEnabledForThisThread()) return;
/*     */ 
/* 458 */     RequestInfoStack localRequestInfoStack = (RequestInfoStack)this.threadLocalClientRequestInfoStack.get();
/*     */ 
/* 460 */     ClientRequestInfoImpl localClientRequestInfoImpl = null;
/*     */ 
/* 462 */     if (!localRequestInfoStack.empty()) {
/* 463 */       localClientRequestInfoImpl = (ClientRequestInfoImpl)localRequestInfoStack.peek();
/*     */     }
/*     */ 
/* 466 */     if ((!paramBoolean) && (localClientRequestInfoImpl != null) && (localClientRequestInfoImpl.isDIIInitiate()))
/*     */     {
/* 469 */       localClientRequestInfoImpl.setDIIInitiate(false);
/*     */     }
/*     */     else
/*     */     {
/* 475 */       if ((localClientRequestInfoImpl == null) || (!localClientRequestInfoImpl.getRetryRequest().isRetry())) {
/* 476 */         localClientRequestInfoImpl = new ClientRequestInfoImpl(this.orb);
/* 477 */         localRequestInfoStack.push(localClientRequestInfoImpl);
/* 478 */         printPush();
/*     */       }
/*     */ 
/* 485 */       localClientRequestInfoImpl.setRetryRequest(RetryType.NONE);
/* 486 */       localClientRequestInfoImpl.incrementEntryCount();
/*     */ 
/* 492 */       localClientRequestInfoImpl.setReplyStatus((short)-1);
/*     */ 
/* 495 */       if (paramBoolean)
/* 496 */         localClientRequestInfoImpl.setDIIInitiate(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cleanupClientPIRequest()
/*     */   {
/* 502 */     if (!this.hasClientInterceptors) return;
/* 503 */     if (!isClientPIEnabledForThisThread()) return;
/*     */ 
/* 505 */     ClientRequestInfoImpl localClientRequestInfoImpl = peekClientRequestInfoImplStack();
/* 506 */     RetryType localRetryType = localClientRequestInfoImpl.getRetryRequest();
/*     */ 
/* 509 */     if (!localRetryType.equals(RetryType.BEFORE_RESPONSE))
/*     */     {
/* 519 */       int i = localClientRequestInfoImpl.getReplyStatus();
/* 520 */       if (i == -1) {
/* 521 */         invokeClientPIEndingPoint(2, this.wrapper.unknownRequestInvoke(CompletionStatus.COMPLETED_MAYBE));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 528 */     localClientRequestInfoImpl.decrementEntryCount();
/*     */ 
/* 531 */     if ((localClientRequestInfoImpl.getEntryCount() == 0) && (!localClientRequestInfoImpl.getRetryRequest().isRetry()))
/*     */     {
/* 534 */       RequestInfoStack localRequestInfoStack = (RequestInfoStack)this.threadLocalClientRequestInfoStack.get();
/*     */ 
/* 536 */       localRequestInfoStack.pop();
/* 537 */       printPop();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setClientPIInfo(CorbaMessageMediator paramCorbaMessageMediator)
/*     */   {
/* 543 */     if (!this.hasClientInterceptors) return;
/* 544 */     if (!isClientPIEnabledForThisThread()) return;
/*     */ 
/* 546 */     peekClientRequestInfoImplStack().setInfo(paramCorbaMessageMediator);
/*     */   }
/*     */ 
/*     */   public void setClientPIInfo(RequestImpl paramRequestImpl) {
/* 550 */     if (!this.hasClientInterceptors) return;
/* 551 */     if (!isClientPIEnabledForThisThread()) return;
/*     */ 
/* 553 */     peekClientRequestInfoImplStack().setDIIRequest(paramRequestImpl);
/*     */   }
/*     */ 
/*     */   public void invokeServerPIStartingPoint()
/*     */   {
/* 563 */     if (!this.hasServerInterceptors) return;
/*     */ 
/* 565 */     ServerRequestInfoImpl localServerRequestInfoImpl = peekServerRequestInfoImplStack();
/* 566 */     this.interceptorInvoker.invokeServerInterceptorStartingPoint(localServerRequestInfoImpl);
/*     */ 
/* 569 */     serverPIHandleExceptions(localServerRequestInfoImpl);
/*     */   }
/*     */ 
/*     */   public void invokeServerPIIntermediatePoint()
/*     */   {
/* 574 */     if (!this.hasServerInterceptors) return;
/*     */ 
/* 576 */     ServerRequestInfoImpl localServerRequestInfoImpl = peekServerRequestInfoImplStack();
/* 577 */     this.interceptorInvoker.invokeServerInterceptorIntermediatePoint(localServerRequestInfoImpl);
/*     */ 
/* 581 */     localServerRequestInfoImpl.releaseServant();
/*     */ 
/* 584 */     serverPIHandleExceptions(localServerRequestInfoImpl);
/*     */   }
/*     */ 
/*     */   public void invokeServerPIEndingPoint(ReplyMessage paramReplyMessage)
/*     */   {
/* 589 */     if (!this.hasServerInterceptors) return;
/* 590 */     ServerRequestInfoImpl localServerRequestInfoImpl = peekServerRequestInfoImplStack();
/*     */ 
/* 593 */     localServerRequestInfoImpl.setReplyMessage(paramReplyMessage);
/*     */ 
/* 597 */     localServerRequestInfoImpl.setCurrentExecutionPoint(2);
/*     */ 
/* 602 */     if (!localServerRequestInfoImpl.getAlreadyExecuted()) {
/* 603 */       int i = paramReplyMessage.getReplyStatus();
/*     */ 
/* 609 */       short s = REPLY_MESSAGE_TO_PI_REPLY_STATUS[i];
/*     */ 
/* 613 */       if ((s == 3) || (s == 4))
/*     */       {
/* 616 */         localServerRequestInfoImpl.setForwardRequest(paramReplyMessage.getIOR());
/*     */       }
/*     */ 
/* 624 */       Exception localException1 = localServerRequestInfoImpl.getException();
/*     */ 
/* 629 */       if ((!localServerRequestInfoImpl.isDynamic()) && (s == 2))
/*     */       {
/* 632 */         localServerRequestInfoImpl.setException(this.omgWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE));
/*     */       }
/*     */ 
/* 637 */       localServerRequestInfoImpl.setReplyStatus(s);
/* 638 */       this.interceptorInvoker.invokeServerInterceptorEndingPoint(localServerRequestInfoImpl);
/* 639 */       int j = localServerRequestInfoImpl.getReplyStatus();
/* 640 */       Exception localException2 = localServerRequestInfoImpl.getException();
/*     */ 
/* 645 */       if ((j == 1) && (localException2 != localException1))
/*     */       {
/* 648 */         throw ((SystemException)localException2);
/*     */       }
/*     */ 
/* 652 */       if (j == 3) {
/* 653 */         if (s != 3)
/*     */         {
/* 655 */           IOR localIOR = localServerRequestInfoImpl.getForwardRequestIOR();
/* 656 */           throw new ForwardException(this.orb, localIOR);
/*     */         }
/* 658 */         if (localServerRequestInfoImpl.isForwardRequestRaisedInEnding())
/*     */         {
/* 660 */           paramReplyMessage.setIOR(localServerRequestInfoImpl.getForwardRequestIOR());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setServerPIInfo(Exception paramException) {
/* 667 */     if (!this.hasServerInterceptors) return;
/*     */ 
/* 669 */     ServerRequestInfoImpl localServerRequestInfoImpl = peekServerRequestInfoImplStack();
/* 670 */     localServerRequestInfoImpl.setException(paramException);
/*     */   }
/*     */ 
/*     */   public void setServerPIInfo(NVList paramNVList)
/*     */   {
/* 675 */     if (!this.hasServerInterceptors) return;
/*     */ 
/* 677 */     ServerRequestInfoImpl localServerRequestInfoImpl = peekServerRequestInfoImplStack();
/* 678 */     localServerRequestInfoImpl.setDSIArguments(paramNVList);
/*     */   }
/*     */ 
/*     */   public void setServerPIExceptionInfo(Any paramAny)
/*     */   {
/* 683 */     if (!this.hasServerInterceptors) return;
/*     */ 
/* 685 */     ServerRequestInfoImpl localServerRequestInfoImpl = peekServerRequestInfoImplStack();
/* 686 */     localServerRequestInfoImpl.setDSIException(paramAny);
/*     */   }
/*     */ 
/*     */   public void setServerPIInfo(Any paramAny)
/*     */   {
/* 691 */     if (!this.hasServerInterceptors) return;
/*     */ 
/* 693 */     ServerRequestInfoImpl localServerRequestInfoImpl = peekServerRequestInfoImplStack();
/* 694 */     localServerRequestInfoImpl.setDSIResult(paramAny);
/*     */   }
/*     */ 
/*     */   public void initializeServerPIInfo(CorbaMessageMediator paramCorbaMessageMediator, ObjectAdapter paramObjectAdapter, byte[] paramArrayOfByte, ObjectKeyTemplate paramObjectKeyTemplate)
/*     */   {
/* 700 */     if (!this.hasServerInterceptors) return;
/*     */ 
/* 702 */     RequestInfoStack localRequestInfoStack = (RequestInfoStack)this.threadLocalServerRequestInfoStack.get();
/*     */ 
/* 704 */     ServerRequestInfoImpl localServerRequestInfoImpl = new ServerRequestInfoImpl(this.orb);
/* 705 */     localRequestInfoStack.push(localServerRequestInfoImpl);
/* 706 */     printPush();
/*     */ 
/* 710 */     paramCorbaMessageMediator.setExecutePIInResponseConstructor(true);
/*     */ 
/* 712 */     localServerRequestInfoImpl.setInfo(paramCorbaMessageMediator, paramObjectAdapter, paramArrayOfByte, paramObjectKeyTemplate);
/*     */   }
/*     */ 
/*     */   public void setServerPIInfo(Object paramObject, String paramString)
/*     */   {
/* 718 */     if (!this.hasServerInterceptors) return;
/*     */ 
/* 720 */     ServerRequestInfoImpl localServerRequestInfoImpl = peekServerRequestInfoImplStack();
/* 721 */     localServerRequestInfoImpl.setInfo(paramObject, paramString);
/*     */   }
/*     */ 
/*     */   public void cleanupServerPIRequest() {
/* 725 */     if (!this.hasServerInterceptors) return;
/*     */ 
/* 727 */     RequestInfoStack localRequestInfoStack = (RequestInfoStack)this.threadLocalServerRequestInfoStack.get();
/*     */ 
/* 729 */     localRequestInfoStack.pop();
/* 730 */     printPop();
/*     */   }
/*     */ 
/*     */   private void serverPIHandleExceptions(ServerRequestInfoImpl paramServerRequestInfoImpl)
/*     */   {
/* 747 */     int i = paramServerRequestInfoImpl.getEndingPointCall();
/* 748 */     if (i == 1)
/*     */     {
/* 750 */       throw ((SystemException)paramServerRequestInfoImpl.getException());
/*     */     }
/* 752 */     if ((i == 2) && (paramServerRequestInfoImpl.getForwardRequestException() != null))
/*     */     {
/* 757 */       IOR localIOR = paramServerRequestInfoImpl.getForwardRequestIOR();
/* 758 */       throw new ForwardException(this.orb, localIOR);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int convertPIReplyStatusToReplyMessage(short paramShort)
/*     */   {
/* 770 */     int i = 0;
/* 771 */     for (int j = 0; j < REPLY_MESSAGE_TO_PI_REPLY_STATUS.length; j++) {
/* 772 */       if (REPLY_MESSAGE_TO_PI_REPLY_STATUS[j] == paramShort) {
/* 773 */         i = j;
/* 774 */         break;
/*     */       }
/*     */     }
/* 777 */     return i;
/*     */   }
/*     */ 
/*     */   private ClientRequestInfoImpl peekClientRequestInfoImplStack()
/*     */   {
/* 786 */     RequestInfoStack localRequestInfoStack = (RequestInfoStack)this.threadLocalClientRequestInfoStack.get();
/*     */ 
/* 788 */     ClientRequestInfoImpl localClientRequestInfoImpl = null;
/* 789 */     if (!localRequestInfoStack.empty())
/* 790 */       localClientRequestInfoImpl = (ClientRequestInfoImpl)localRequestInfoStack.peek();
/*     */     else {
/* 792 */       throw this.wrapper.clientInfoStackNull();
/*     */     }
/*     */ 
/* 795 */     return localClientRequestInfoImpl;
/*     */   }
/*     */ 
/*     */   private ServerRequestInfoImpl peekServerRequestInfoImplStack()
/*     */   {
/* 803 */     RequestInfoStack localRequestInfoStack = (RequestInfoStack)this.threadLocalServerRequestInfoStack.get();
/*     */ 
/* 805 */     ServerRequestInfoImpl localServerRequestInfoImpl = null;
/*     */ 
/* 807 */     if (!localRequestInfoStack.empty())
/* 808 */       localServerRequestInfoImpl = (ServerRequestInfoImpl)localRequestInfoStack.peek();
/*     */     else {
/* 810 */       throw this.wrapper.serverInfoStackNull();
/*     */     }
/*     */ 
/* 813 */     return localServerRequestInfoImpl;
/*     */   }
/*     */ 
/*     */   private boolean isClientPIEnabledForThisThread()
/*     */   {
/* 821 */     RequestInfoStack localRequestInfoStack = (RequestInfoStack)this.threadLocalClientRequestInfoStack.get();
/*     */ 
/* 823 */     return localRequestInfoStack.disableCount == 0;
/*     */   }
/*     */ 
/*     */   private void preInitORBInitializers(ORBInitInfoImpl paramORBInitInfoImpl)
/*     */   {
/* 832 */     paramORBInitInfoImpl.setStage(0);
/*     */ 
/* 836 */     for (int i = 0; i < this.orb.getORBData().getORBInitializers().length; 
/* 837 */       i++) {
/* 838 */       ORBInitializer localORBInitializer = this.orb.getORBData().getORBInitializers()[i];
/* 839 */       if (localORBInitializer != null)
/*     */         try {
/* 841 */           localORBInitializer.pre_init(paramORBInitInfoImpl);
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void postInitORBInitializers(ORBInitInfoImpl paramORBInitInfoImpl)
/*     */   {
/* 857 */     paramORBInitInfoImpl.setStage(1);
/*     */ 
/* 861 */     for (int i = 0; i < this.orb.getORBData().getORBInitializers().length; 
/* 862 */       i++) {
/* 863 */       ORBInitializer localORBInitializer = this.orb.getORBData().getORBInitializers()[i];
/* 864 */       if (localORBInitializer != null)
/*     */         try {
/* 866 */           localORBInitializer.post_init(paramORBInitInfoImpl);
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private ORBInitInfoImpl createORBInitInfo()
/*     */   {
/* 881 */     ORBInitInfoImpl localORBInitInfoImpl = null;
/*     */ 
/* 888 */     String str = this.orb.getORBData().getORBId();
/*     */ 
/* 890 */     localORBInitInfoImpl = new ORBInitInfoImpl(this.orb, this.arguments, str, this.codecFactory);
/*     */ 
/* 892 */     return localORBInitInfoImpl;
/*     */   }
/*     */ 
/*     */   public void register_interceptor(Interceptor paramInterceptor, int paramInt)
/*     */     throws DuplicateName
/*     */   {
/* 912 */     if ((paramInt >= 3) || (paramInt < 0)) {
/* 913 */       throw this.wrapper.typeOutOfRange(new Integer(paramInt));
/*     */     }
/*     */ 
/* 916 */     String str = paramInterceptor.name();
/*     */ 
/* 918 */     if (str == null) {
/* 919 */       throw this.wrapper.nameNull();
/*     */     }
/*     */ 
/* 923 */     this.interceptorList.register_interceptor(paramInterceptor, paramInt);
/*     */   }
/*     */ 
/*     */   public Current getPICurrent() {
/* 927 */     return this.current;
/*     */   }
/*     */ 
/*     */   private void nullParam()
/*     */     throws BAD_PARAM
/*     */   {
/* 937 */     throw this.orbutilWrapper.nullParam();
/*     */   }
/*     */ 
/*     */   public Policy create_policy(int paramInt, Any paramAny)
/*     */     throws PolicyError
/*     */   {
/* 951 */     if (paramAny == null) {
/* 952 */       nullParam();
/*     */     }
/* 954 */     if (this.policyFactoryTable == null) {
/* 955 */       throw new PolicyError("There is no PolicyFactory Registered for type " + paramInt, (short)0);
/*     */     }
/*     */ 
/* 959 */     PolicyFactory localPolicyFactory = (PolicyFactory)this.policyFactoryTable.get(new Integer(paramInt));
/*     */ 
/* 961 */     if (localPolicyFactory == null) {
/* 962 */       throw new PolicyError(" Could Not Find PolicyFactory for the Type " + paramInt, (short)0);
/*     */     }
/*     */ 
/* 966 */     Policy localPolicy = localPolicyFactory.create_policy(paramInt, paramAny);
/* 967 */     return localPolicy;
/*     */   }
/*     */ 
/*     */   public void registerPolicyFactory(int paramInt, PolicyFactory paramPolicyFactory)
/*     */   {
/* 975 */     if (this.policyFactoryTable == null) {
/* 976 */       this.policyFactoryTable = new HashMap();
/*     */     }
/* 978 */     Integer localInteger = new Integer(paramInt);
/* 979 */     Object localObject = this.policyFactoryTable.get(localInteger);
/* 980 */     if (localObject == null) {
/* 981 */       this.policyFactoryTable.put(localInteger, paramPolicyFactory);
/*     */     }
/*     */     else
/* 984 */       throw this.omgWrapper.policyFactoryRegFailed(new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   public synchronized int allocateServerRequestId()
/*     */   {
/* 990 */     return this.serverRequestIdCounter++;
/*     */   }
/*     */ 
/*     */   private final class RequestInfoStack extends Stack
/*     */   {
/* 205 */     public int disableCount = 0;
/*     */ 
/*     */     private RequestInfoStack()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.PIHandlerImpl
 * JD-Core Version:    0.6.2
 */