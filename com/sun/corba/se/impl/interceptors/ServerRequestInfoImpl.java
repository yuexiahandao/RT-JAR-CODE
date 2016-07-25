/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.InterceptorsSystemException;
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.legacy.connection.Connection;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.protocol.PIHandler;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_INV_ORDER;
/*     */ import org.omg.CORBA.NVList;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.Dynamic.Parameter;
/*     */ import org.omg.IOP.ServiceContext;
/*     */ import org.omg.PortableInterceptor.ForwardRequest;
/*     */ import org.omg.PortableInterceptor.InvalidSlot;
/*     */ import org.omg.PortableInterceptor.ServerRequestInfo;
/*     */ import org.omg.PortableServer.Servant;
/*     */ 
/*     */ public final class ServerRequestInfoImpl extends RequestInfoImpl
/*     */   implements ServerRequestInfo
/*     */ {
/*     */   static final int CALL_RECEIVE_REQUEST_SERVICE_CONTEXT = 0;
/*     */   static final int CALL_RECEIVE_REQUEST = 0;
/*     */   static final int CALL_INTERMEDIATE_NONE = 1;
/*     */   static final int CALL_SEND_REPLY = 0;
/*     */   static final int CALL_SEND_EXCEPTION = 1;
/*     */   static final int CALL_SEND_OTHER = 2;
/*     */   private boolean forwardRequestRaisedInEnding;
/*     */   private CorbaMessageMediator request;
/*     */   private java.lang.Object servant;
/*     */   private byte[] objectId;
/*     */   private ObjectKeyTemplate oktemp;
/*     */   private byte[] adapterId;
/*     */   private String[] adapterName;
/*     */   private ArrayList addReplyServiceContextQueue;
/*     */   private ReplyMessage replyMessage;
/*     */   private String targetMostDerivedInterface;
/*     */   private NVList dsiArguments;
/*     */   private Any dsiResult;
/*     */   private Any dsiException;
/*     */   private boolean isDynamic;
/*     */   private ObjectAdapter objectAdapter;
/*     */   private int serverRequestId;
/*     */   private Parameter[] cachedArguments;
/*     */   private Any cachedSendingException;
/*     */   private HashMap cachedRequestServiceContexts;
/*     */   private HashMap cachedReplyServiceContexts;
/*     */   protected static final int MID_SENDING_EXCEPTION = 14;
/*     */   protected static final int MID_OBJECT_ID = 15;
/*     */   protected static final int MID_ADAPTER_ID = 16;
/*     */   protected static final int MID_TARGET_MOST_DERIVED_INTERFACE = 17;
/*     */   protected static final int MID_GET_SERVER_POLICY = 18;
/*     */   protected static final int MID_SET_SLOT = 19;
/*     */   protected static final int MID_TARGET_IS_A = 20;
/*     */   protected static final int MID_ADD_REPLY_SERVICE_CONTEXT = 21;
/*     */   protected static final int MID_SERVER_ID = 22;
/*     */   protected static final int MID_ORB_ID = 23;
/*     */   protected static final int MID_ADAPTER_NAME = 24;
/* 198 */   private static final boolean[][] validCall = { { true, true, true, true, true }, { true, true, true, true, true }, { false, true, true, false, false }, { false, true, true, true, true }, { false, true, true, true, true }, { false, true, true, false, false }, { false, false, true, false, false }, { true, true, true, true, true }, { true, true, true, true, true }, { false, false, true, true, true }, { false, false, false, false, true }, { true, true, true, true, true }, { true, true, true, true, true }, { false, false, true, true, true }, { false, false, false, true, false }, { false, true, true, true, true }, { false, true, true, true, true }, { false, true, false, false, false }, { true, true, true, true, true }, { true, true, true, true, true }, { false, true, false, false, false }, { true, true, true, true, true }, { false, true, true, true, true }, { false, true, true, true, true }, { false, true, true, true, true } };
/*     */ 
/*     */   void reset()
/*     */   {
/* 140 */     super.reset();
/*     */ 
/* 144 */     this.forwardRequestRaisedInEnding = false;
/*     */ 
/* 146 */     this.request = null;
/* 147 */     this.servant = null;
/* 148 */     this.objectId = null;
/* 149 */     this.oktemp = null;
/*     */ 
/* 151 */     this.adapterId = null;
/* 152 */     this.adapterName = null;
/*     */ 
/* 154 */     this.addReplyServiceContextQueue = null;
/* 155 */     this.replyMessage = null;
/* 156 */     this.targetMostDerivedInterface = null;
/* 157 */     this.dsiArguments = null;
/* 158 */     this.dsiResult = null;
/* 159 */     this.dsiException = null;
/* 160 */     this.isDynamic = false;
/* 161 */     this.objectAdapter = null;
/* 162 */     this.serverRequestId = this.myORB.getPIHandler().allocateServerRequestId();
/*     */ 
/* 165 */     this.cachedArguments = null;
/* 166 */     this.cachedSendingException = null;
/* 167 */     this.cachedRequestServiceContexts = null;
/* 168 */     this.cachedReplyServiceContexts = null;
/*     */ 
/* 170 */     this.startingPointCall = 0;
/* 171 */     this.intermediatePointCall = 0;
/* 172 */     this.endingPointCall = 0;
/*     */   }
/*     */ 
/*     */   ServerRequestInfoImpl(ORB paramORB)
/*     */   {
/* 254 */     super(paramORB);
/* 255 */     this.startingPointCall = 0;
/* 256 */     this.intermediatePointCall = 0;
/* 257 */     this.endingPointCall = 0;
/* 258 */     this.serverRequestId = paramORB.getPIHandler().allocateServerRequestId();
/*     */   }
/*     */ 
/*     */   public Any sending_exception()
/*     */   {
/* 265 */     checkAccess(14);
/*     */ 
/* 267 */     if (this.cachedSendingException == null) {
/* 268 */       Any localAny = null;
/*     */ 
/* 270 */       if (this.dsiException != null)
/* 271 */         localAny = this.dsiException;
/* 272 */       else if (this.exception != null) {
/* 273 */         localAny = exceptionToAny(this.exception);
/*     */       }
/*     */       else
/*     */       {
/* 277 */         throw this.wrapper.exceptionUnavailable();
/*     */       }
/*     */ 
/* 280 */       this.cachedSendingException = localAny;
/*     */     }
/*     */ 
/* 283 */     return this.cachedSendingException;
/*     */   }
/*     */ 
/*     */   public byte[] object_id()
/*     */   {
/* 290 */     checkAccess(15);
/*     */ 
/* 292 */     if (this.objectId == null)
/*     */     {
/* 297 */       throw this.stdWrapper.piOperationNotSupported6();
/*     */     }
/*     */ 
/* 304 */     return this.objectId;
/*     */   }
/*     */ 
/*     */   private void checkForNullTemplate()
/*     */   {
/* 309 */     if (this.oktemp == null)
/*     */     {
/* 314 */       throw this.stdWrapper.piOperationNotSupported7();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String server_id()
/*     */   {
/* 320 */     checkAccess(22);
/* 321 */     checkForNullTemplate();
/*     */ 
/* 327 */     return Integer.toString(this.oktemp.getServerId());
/*     */   }
/*     */ 
/*     */   public String orb_id()
/*     */   {
/* 332 */     checkAccess(23);
/*     */ 
/* 334 */     return this.myORB.getORBData().getORBId();
/*     */   }
/*     */ 
/*     */   public synchronized String[] adapter_name()
/*     */   {
/* 339 */     checkAccess(24);
/*     */ 
/* 341 */     if (this.adapterName == null) {
/* 342 */       checkForNullTemplate();
/*     */ 
/* 344 */       ObjectAdapterId localObjectAdapterId = this.oktemp.getObjectAdapterId();
/* 345 */       this.adapterName = localObjectAdapterId.getAdapterName();
/*     */     }
/*     */ 
/* 348 */     return this.adapterName;
/*     */   }
/*     */ 
/*     */   public synchronized byte[] adapter_id()
/*     */   {
/* 356 */     checkAccess(16);
/*     */ 
/* 358 */     if (this.adapterId == null) {
/* 359 */       checkForNullTemplate();
/* 360 */       this.adapterId = this.oktemp.getAdapterId();
/*     */     }
/*     */ 
/* 363 */     return this.adapterId;
/*     */   }
/*     */ 
/*     */   public String target_most_derived_interface()
/*     */   {
/* 370 */     checkAccess(17);
/* 371 */     return this.targetMostDerivedInterface;
/*     */   }
/*     */ 
/*     */   public Policy get_server_policy(int paramInt)
/*     */   {
/* 382 */     Policy localPolicy = null;
/*     */ 
/* 384 */     if (this.objectAdapter != null) {
/* 385 */       localPolicy = this.objectAdapter.getEffectivePolicy(paramInt);
/*     */     }
/*     */ 
/* 391 */     return localPolicy;
/*     */   }
/*     */ 
/*     */   public void set_slot(int paramInt, Any paramAny)
/*     */     throws InvalidSlot
/*     */   {
/* 404 */     this.slotTable.set_slot(paramInt, paramAny);
/*     */   }
/*     */ 
/*     */   public boolean target_is_a(String paramString)
/*     */   {
/* 412 */     checkAccess(20);
/*     */ 
/* 414 */     boolean bool = false;
/* 415 */     if ((this.servant instanceof Servant))
/* 416 */       bool = ((Servant)this.servant)._is_a(paramString);
/* 417 */     else if (StubAdapter.isStub(this.servant))
/* 418 */       bool = ((org.omg.CORBA.Object)this.servant)._is_a(paramString);
/*     */     else {
/* 420 */       throw this.wrapper.servantInvalid();
/*     */     }
/*     */ 
/* 423 */     return bool;
/*     */   }
/*     */ 
/*     */   public void add_reply_service_context(ServiceContext paramServiceContext, boolean paramBoolean)
/*     */   {
/* 435 */     if (this.currentExecutionPoint == 2) {
/* 436 */       localObject = this.replyMessage.getServiceContexts();
/*     */ 
/* 439 */       if (localObject == null) {
/* 440 */         localObject = new ServiceContexts(this.myORB);
/* 441 */         this.replyMessage.setServiceContexts((ServiceContexts)localObject);
/*     */       }
/*     */ 
/* 444 */       if (this.cachedReplyServiceContexts == null) {
/* 445 */         this.cachedReplyServiceContexts = new HashMap();
/*     */       }
/*     */ 
/* 450 */       addServiceContext(this.cachedReplyServiceContexts, (ServiceContexts)localObject, paramServiceContext, paramBoolean);
/*     */     }
/*     */ 
/* 470 */     java.lang.Object localObject = new AddReplyServiceContextCommand(null);
/*     */ 
/* 472 */     ((AddReplyServiceContextCommand)localObject).service_context = paramServiceContext;
/* 473 */     ((AddReplyServiceContextCommand)localObject).replace = paramBoolean;
/*     */ 
/* 475 */     if (this.addReplyServiceContextQueue == null) {
/* 476 */       this.addReplyServiceContextQueue = new ArrayList();
/*     */     }
/*     */ 
/* 480 */     enqueue((AddReplyServiceContextCommand)localObject);
/*     */   }
/*     */ 
/*     */   public int request_id()
/*     */   {
/* 509 */     return this.serverRequestId;
/*     */   }
/*     */ 
/*     */   public String operation()
/*     */   {
/* 518 */     return this.request.getOperationName();
/*     */   }
/*     */ 
/*     */   public Parameter[] arguments()
/*     */   {
/* 525 */     checkAccess(2);
/*     */ 
/* 527 */     if (this.cachedArguments == null) {
/* 528 */       if (!this.isDynamic) {
/* 529 */         throw this.stdWrapper.piOperationNotSupported1();
/*     */       }
/*     */ 
/* 532 */       if (this.dsiArguments == null) {
/* 533 */         throw this.stdWrapper.piOperationNotSupported8();
/*     */       }
/*     */ 
/* 538 */       this.cachedArguments = nvListToParameterArray(this.dsiArguments);
/*     */     }
/*     */ 
/* 546 */     return this.cachedArguments;
/*     */   }
/*     */ 
/*     */   public TypeCode[] exceptions()
/*     */   {
/* 553 */     checkAccess(3);
/*     */ 
/* 557 */     throw this.stdWrapper.piOperationNotSupported2();
/*     */   }
/*     */ 
/*     */   public String[] contexts()
/*     */   {
/* 564 */     checkAccess(4);
/*     */ 
/* 568 */     throw this.stdWrapper.piOperationNotSupported3();
/*     */   }
/*     */ 
/*     */   public String[] operation_context()
/*     */   {
/* 575 */     checkAccess(5);
/*     */ 
/* 580 */     throw this.stdWrapper.piOperationNotSupported4();
/*     */   }
/*     */ 
/*     */   public Any result()
/*     */   {
/* 587 */     checkAccess(6);
/*     */ 
/* 589 */     if (!this.isDynamic) {
/* 590 */       throw this.stdWrapper.piOperationNotSupported5();
/*     */     }
/*     */ 
/* 593 */     if (this.dsiResult == null) {
/* 594 */       throw this.wrapper.piDsiResultIsNull();
/*     */     }
/*     */ 
/* 601 */     return this.dsiResult;
/*     */   }
/*     */ 
/*     */   public boolean response_expected()
/*     */   {
/* 610 */     return !this.request.isOneWay();
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object forward_reference()
/*     */   {
/* 617 */     checkAccess(10);
/*     */ 
/* 621 */     if (this.replyStatus != 3) {
/* 622 */       throw this.stdWrapper.invalidPiCall1();
/*     */     }
/*     */ 
/* 625 */     return getForwardRequestException().forward;
/*     */   }
/*     */ 
/*     */   public ServiceContext get_request_service_context(int paramInt)
/*     */   {
/* 632 */     checkAccess(12);
/*     */ 
/* 634 */     if (this.cachedRequestServiceContexts == null) {
/* 635 */       this.cachedRequestServiceContexts = new HashMap();
/*     */     }
/*     */ 
/* 638 */     return getServiceContext(this.cachedRequestServiceContexts, this.request.getRequestServiceContexts(), paramInt);
/*     */   }
/*     */ 
/*     */   public ServiceContext get_reply_service_context(int paramInt)
/*     */   {
/* 646 */     checkAccess(13);
/*     */ 
/* 648 */     if (this.cachedReplyServiceContexts == null) {
/* 649 */       this.cachedReplyServiceContexts = new HashMap();
/*     */     }
/*     */ 
/* 652 */     return getServiceContext(this.cachedReplyServiceContexts, this.replyMessage.getServiceContexts(), paramInt);
/*     */   }
/*     */ 
/*     */   private void enqueue(AddReplyServiceContextCommand paramAddReplyServiceContextCommand)
/*     */   {
/* 675 */     int i = this.addReplyServiceContextQueue.size();
/* 676 */     int j = 0;
/*     */ 
/* 678 */     for (int k = 0; k < i; k++) {
/* 679 */       AddReplyServiceContextCommand localAddReplyServiceContextCommand = (AddReplyServiceContextCommand)this.addReplyServiceContextQueue.get(k);
/*     */ 
/* 683 */       if (localAddReplyServiceContextCommand.service_context.context_id == paramAddReplyServiceContextCommand.service_context.context_id)
/*     */       {
/* 686 */         j = 1;
/* 687 */         if (paramAddReplyServiceContextCommand.replace) {
/* 688 */           this.addReplyServiceContextQueue.set(k, paramAddReplyServiceContextCommand); break;
/*     */         }
/* 690 */         throw this.stdWrapper.serviceContextAddFailed(new Integer(localAddReplyServiceContextCommand.service_context.context_id));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 697 */     if (j == 0)
/* 698 */       this.addReplyServiceContextQueue.add(paramAddReplyServiceContextCommand);
/*     */   }
/*     */ 
/*     */   protected void setCurrentExecutionPoint(int paramInt)
/*     */   {
/* 713 */     super.setCurrentExecutionPoint(paramInt);
/*     */ 
/* 718 */     if ((paramInt == 2) && (this.addReplyServiceContextQueue != null))
/*     */     {
/* 721 */       int i = this.addReplyServiceContextQueue.size();
/* 722 */       for (int j = 0; j < i; j++) {
/* 723 */         AddReplyServiceContextCommand localAddReplyServiceContextCommand = (AddReplyServiceContextCommand)this.addReplyServiceContextQueue.get(j);
/*     */         try
/*     */         {
/* 727 */           add_reply_service_context(localAddReplyServiceContextCommand.service_context, localAddReplyServiceContextCommand.replace);
/*     */         }
/*     */         catch (BAD_INV_ORDER localBAD_INV_ORDER)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setInfo(CorbaMessageMediator paramCorbaMessageMediator, ObjectAdapter paramObjectAdapter, byte[] paramArrayOfByte, ObjectKeyTemplate paramObjectKeyTemplate)
/*     */   {
/* 752 */     this.request = paramCorbaMessageMediator;
/* 753 */     this.objectId = paramArrayOfByte;
/* 754 */     this.oktemp = paramObjectKeyTemplate;
/* 755 */     this.objectAdapter = paramObjectAdapter;
/* 756 */     this.connection = ((Connection)paramCorbaMessageMediator.getConnection());
/*     */   }
/*     */ 
/*     */   protected void setDSIArguments(NVList paramNVList)
/*     */   {
/* 764 */     this.dsiArguments = paramNVList;
/*     */   }
/*     */ 
/*     */   protected void setDSIException(Any paramAny)
/*     */   {
/* 771 */     this.dsiException = paramAny;
/*     */ 
/* 774 */     this.cachedSendingException = null;
/*     */   }
/*     */ 
/*     */   protected void setDSIResult(Any paramAny)
/*     */   {
/* 781 */     this.dsiResult = paramAny;
/*     */   }
/*     */ 
/*     */   protected void setException(Exception paramException)
/*     */   {
/* 789 */     super.setException(paramException);
/*     */ 
/* 792 */     this.dsiException = null;
/*     */ 
/* 795 */     this.cachedSendingException = null;
/*     */   }
/*     */ 
/*     */   protected void setInfo(java.lang.Object paramObject, String paramString)
/*     */   {
/* 804 */     this.servant = paramObject;
/* 805 */     this.targetMostDerivedInterface = paramString;
/* 806 */     this.isDynamic = (((paramObject instanceof org.omg.PortableServer.DynamicImplementation)) || ((paramObject instanceof org.omg.CORBA.DynamicImplementation)));
/*     */   }
/*     */ 
/*     */   void setReplyMessage(ReplyMessage paramReplyMessage)
/*     */   {
/* 816 */     this.replyMessage = paramReplyMessage;
/*     */   }
/*     */ 
/*     */   protected void setReplyStatus(short paramShort)
/*     */   {
/* 824 */     super.setReplyStatus(paramShort);
/* 825 */     switch (paramShort) {
/*     */     case 0:
/* 827 */       this.endingPointCall = 0;
/* 828 */       break;
/*     */     case 1:
/*     */     case 2:
/* 831 */       this.endingPointCall = 1;
/* 832 */       break;
/*     */     case 3:
/*     */     case 4:
/* 835 */       this.endingPointCall = 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   void releaseServant()
/*     */   {
/* 845 */     this.servant = null;
/*     */   }
/*     */ 
/*     */   void setForwardRequestRaisedInEnding()
/*     */   {
/* 853 */     this.forwardRequestRaisedInEnding = true;
/*     */   }
/*     */ 
/*     */   boolean isForwardRequestRaisedInEnding()
/*     */   {
/* 861 */     return this.forwardRequestRaisedInEnding;
/*     */   }
/*     */ 
/*     */   boolean isDynamic()
/*     */   {
/* 868 */     return this.isDynamic;
/*     */   }
/*     */ 
/*     */   protected void checkAccess(int paramInt)
/*     */   {
/* 878 */     int i = 0;
/* 879 */     switch (this.currentExecutionPoint) {
/*     */     case 0:
/* 881 */       i = 0;
/* 882 */       break;
/*     */     case 1:
/* 884 */       i = 1;
/* 885 */       break;
/*     */     case 2:
/* 887 */       switch (this.endingPointCall) {
/*     */       case 0:
/* 889 */         i = 2;
/* 890 */         break;
/*     */       case 1:
/* 892 */         i = 3;
/* 893 */         break;
/*     */       case 2:
/* 895 */         i = 4;
/*     */       }
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 902 */     if (validCall[paramInt][i] == 0)
/* 903 */       throw this.stdWrapper.invalidPiCall2();
/*     */   }
/*     */ 
/*     */   private class AddReplyServiceContextCommand
/*     */   {
/*     */     ServiceContext service_context;
/*     */     boolean replace;
/*     */ 
/*     */     private AddReplyServiceContextCommand()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.ServerRequestInfoImpl
 * JD-Core Version:    0.6.2
 */