/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.InterceptorsSystemException;
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.protocol.CorbaInvocationInfo;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.pept.transport.ContactInfo;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.legacy.connection.Connection;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.protocol.RetryType;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfo;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoListIterator;
/*     */ import java.util.HashMap;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_INV_ORDER;
/*     */ import org.omg.CORBA.Context;
/*     */ import org.omg.CORBA.ContextList;
/*     */ import org.omg.CORBA.ExceptionList;
/*     */ import org.omg.CORBA.NVList;
/*     */ import org.omg.CORBA.NamedValue;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.CORBA.Request;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.Dynamic.Parameter;
/*     */ import org.omg.IOP.ServiceContext;
/*     */ import org.omg.IOP.TaggedComponent;
/*     */ import org.omg.IOP.TaggedProfile;
/*     */ import org.omg.PortableInterceptor.ClientRequestInfo;
/*     */ 
/*     */ public final class ClientRequestInfoImpl extends RequestInfoImpl
/*     */   implements ClientRequestInfo
/*     */ {
/*     */   static final int CALL_SEND_REQUEST = 0;
/*     */   static final int CALL_SEND_POLL = 1;
/*     */   static final int CALL_RECEIVE_REPLY = 0;
/*     */   static final int CALL_RECEIVE_EXCEPTION = 1;
/*     */   static final int CALL_RECEIVE_OTHER = 2;
/*     */   private RetryType retryRequest;
/* 120 */   private int entryCount = 0;
/*     */   private Request request;
/*     */   private boolean diiInitiate;
/*     */   private CorbaMessageMediator messageMediator;
/*     */   private org.omg.CORBA.Object cachedTargetObject;
/*     */   private org.omg.CORBA.Object cachedEffectiveTargetObject;
/*     */   private Parameter[] cachedArguments;
/*     */   private TypeCode[] cachedExceptions;
/*     */   private String[] cachedContexts;
/*     */   private String[] cachedOperationContext;
/*     */   private String cachedReceivedExceptionId;
/*     */   private Any cachedResult;
/*     */   private Any cachedReceivedException;
/*     */   private TaggedProfile cachedEffectiveProfile;
/*     */   private HashMap cachedRequestServiceContexts;
/*     */   private HashMap cachedReplyServiceContexts;
/*     */   private HashMap cachedEffectiveComponents;
/*     */   protected boolean piCurrentPushed;
/*     */   protected static final int MID_TARGET = 14;
/*     */   protected static final int MID_EFFECTIVE_TARGET = 15;
/*     */   protected static final int MID_EFFECTIVE_PROFILE = 16;
/*     */   protected static final int MID_RECEIVED_EXCEPTION = 17;
/*     */   protected static final int MID_RECEIVED_EXCEPTION_ID = 18;
/*     */   protected static final int MID_GET_EFFECTIVE_COMPONENT = 19;
/*     */   protected static final int MID_GET_EFFECTIVE_COMPONENTS = 20;
/*     */   protected static final int MID_GET_REQUEST_POLICY = 21;
/*     */   protected static final int MID_ADD_REQUEST_SERVICE_CONTEXT = 22;
/* 220 */   private static final boolean[][] validCall = { { true, true, true, true, true }, { true, true, true, true, true }, { true, false, true, false, false }, { true, false, true, true, true }, { true, false, true, true, true }, { true, false, true, true, true }, { false, false, true, false, false }, { true, true, true, true, true }, { true, false, true, true, true }, { false, false, true, true, true }, { false, false, false, false, true }, { true, true, true, true, true }, { true, false, true, true, true }, { false, false, true, true, true }, { true, true, true, true, true }, { true, true, true, true, true }, { true, true, true, true, true }, { false, false, false, true, false }, { false, false, false, true, false }, { true, false, true, true, true }, { true, false, true, true, true }, { true, false, true, true, true }, { true, false, false, false, false } };
/*     */ 
/*     */   void reset()
/*     */   {
/* 163 */     super.reset();
/*     */ 
/* 168 */     this.retryRequest = RetryType.NONE;
/*     */ 
/* 173 */     this.request = null;
/* 174 */     this.diiInitiate = false;
/* 175 */     this.messageMediator = null;
/*     */ 
/* 178 */     this.cachedTargetObject = null;
/* 179 */     this.cachedEffectiveTargetObject = null;
/* 180 */     this.cachedArguments = null;
/* 181 */     this.cachedExceptions = null;
/* 182 */     this.cachedContexts = null;
/* 183 */     this.cachedOperationContext = null;
/* 184 */     this.cachedReceivedExceptionId = null;
/* 185 */     this.cachedResult = null;
/* 186 */     this.cachedReceivedException = null;
/* 187 */     this.cachedEffectiveProfile = null;
/* 188 */     this.cachedRequestServiceContexts = null;
/* 189 */     this.cachedReplyServiceContexts = null;
/* 190 */     this.cachedEffectiveComponents = null;
/*     */ 
/* 192 */     this.piCurrentPushed = false;
/*     */ 
/* 194 */     this.startingPointCall = 0;
/* 195 */     this.endingPointCall = 0;
/*     */   }
/*     */ 
/*     */   protected ClientRequestInfoImpl(ORB paramORB)
/*     */   {
/* 274 */     super(paramORB);
/* 275 */     this.startingPointCall = 0;
/* 276 */     this.endingPointCall = 0;
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object target()
/*     */   {
/* 285 */     if (this.cachedTargetObject == null) {
/* 286 */       CorbaContactInfo localCorbaContactInfo = (CorbaContactInfo)this.messageMediator.getContactInfo();
/*     */ 
/* 288 */       this.cachedTargetObject = iorToObject(localCorbaContactInfo.getTargetIOR());
/*     */     }
/*     */ 
/* 291 */     return this.cachedTargetObject;
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object effective_target()
/*     */   {
/* 308 */     if (this.cachedEffectiveTargetObject == null) {
/* 309 */       CorbaContactInfo localCorbaContactInfo = (CorbaContactInfo)this.messageMediator.getContactInfo();
/*     */ 
/* 312 */       this.cachedEffectiveTargetObject = iorToObject(localCorbaContactInfo.getEffectiveTargetIOR());
/*     */     }
/*     */ 
/* 315 */     return this.cachedEffectiveTargetObject;
/*     */   }
/*     */ 
/*     */   public TaggedProfile effective_profile()
/*     */   {
/* 328 */     if (this.cachedEffectiveProfile == null) {
/* 329 */       CorbaContactInfo localCorbaContactInfo = (CorbaContactInfo)this.messageMediator.getContactInfo();
/*     */ 
/* 331 */       this.cachedEffectiveProfile = localCorbaContactInfo.getEffectiveProfile().getIOPProfile();
/*     */     }
/*     */ 
/* 339 */     return this.cachedEffectiveProfile;
/*     */   }
/*     */ 
/*     */   public Any received_exception()
/*     */   {
/* 346 */     checkAccess(17);
/*     */ 
/* 348 */     if (this.cachedReceivedException == null) {
/* 349 */       this.cachedReceivedException = exceptionToAny(this.exception);
/*     */     }
/*     */ 
/* 356 */     return this.cachedReceivedException;
/*     */   }
/*     */ 
/*     */   public String received_exception_id()
/*     */   {
/* 363 */     checkAccess(18);
/*     */ 
/* 365 */     if (this.cachedReceivedExceptionId == null) {
/* 366 */       String str1 = null;
/*     */ 
/* 368 */       if (this.exception == null)
/*     */       {
/* 372 */         throw this.wrapper.exceptionWasNull();
/* 373 */       }if ((this.exception instanceof SystemException)) {
/* 374 */         String str2 = this.exception.getClass().getName();
/* 375 */         str1 = ORBUtility.repositoryIdOf(str2);
/* 376 */       } else if ((this.exception instanceof ApplicationException)) {
/* 377 */         str1 = ((ApplicationException)this.exception).getId();
/*     */       }
/*     */ 
/* 383 */       this.cachedReceivedExceptionId = str1;
/*     */     }
/*     */ 
/* 386 */     return this.cachedReceivedExceptionId;
/*     */   }
/*     */ 
/*     */   public TaggedComponent get_effective_component(int paramInt)
/*     */   {
/* 396 */     checkAccess(19);
/*     */ 
/* 398 */     return get_effective_components(paramInt)[0];
/*     */   }
/*     */ 
/*     */   public TaggedComponent[] get_effective_components(int paramInt)
/*     */   {
/* 406 */     checkAccess(20);
/* 407 */     Integer localInteger = new Integer(paramInt);
/* 408 */     TaggedComponent[] arrayOfTaggedComponent = null;
/* 409 */     int i = 0;
/*     */ 
/* 411 */     if (this.cachedEffectiveComponents == null) {
/* 412 */       this.cachedEffectiveComponents = new HashMap();
/* 413 */       i = 1;
/*     */     }
/*     */     else
/*     */     {
/* 417 */       arrayOfTaggedComponent = (TaggedComponent[])this.cachedEffectiveComponents.get(localInteger);
/*     */     }
/*     */ 
/* 422 */     if ((arrayOfTaggedComponent == null) && ((i != 0) || (!this.cachedEffectiveComponents.containsKey(localInteger))))
/*     */     {
/* 427 */       CorbaContactInfo localCorbaContactInfo = (CorbaContactInfo)this.messageMediator.getContactInfo();
/*     */ 
/* 429 */       IIOPProfileTemplate localIIOPProfileTemplate = (IIOPProfileTemplate)localCorbaContactInfo.getEffectiveProfile().getTaggedProfileTemplate();
/*     */ 
/* 432 */       arrayOfTaggedComponent = localIIOPProfileTemplate.getIOPComponents(this.myORB, paramInt);
/* 433 */       this.cachedEffectiveComponents.put(localInteger, arrayOfTaggedComponent);
/*     */     }
/*     */ 
/* 438 */     if ((arrayOfTaggedComponent == null) || (arrayOfTaggedComponent.length == 0)) {
/* 439 */       throw this.stdWrapper.invalidComponentId(localInteger);
/*     */     }
/*     */ 
/* 447 */     return arrayOfTaggedComponent;
/*     */   }
/*     */ 
/*     */   public Policy get_request_policy(int paramInt)
/*     */   {
/* 454 */     checkAccess(21);
/*     */ 
/* 456 */     throw this.wrapper.piOrbNotPolicyBased();
/*     */   }
/*     */ 
/*     */   public void add_request_service_context(ServiceContext paramServiceContext, boolean paramBoolean)
/*     */   {
/* 468 */     checkAccess(22);
/*     */ 
/* 470 */     if (this.cachedRequestServiceContexts == null) {
/* 471 */       this.cachedRequestServiceContexts = new HashMap();
/*     */     }
/*     */ 
/* 474 */     addServiceContext(this.cachedRequestServiceContexts, this.messageMediator.getRequestServiceContexts(), paramServiceContext, paramBoolean);
/*     */   }
/*     */ 
/*     */   public int request_id()
/*     */   {
/* 504 */     return this.messageMediator.getRequestId();
/*     */   }
/*     */ 
/*     */   public String operation()
/*     */   {
/* 513 */     return this.messageMediator.getOperationName();
/*     */   }
/*     */ 
/*     */   public Parameter[] arguments()
/*     */   {
/* 520 */     checkAccess(2);
/*     */ 
/* 522 */     if (this.cachedArguments == null) {
/* 523 */       if (this.request == null) {
/* 524 */         throw this.stdWrapper.piOperationNotSupported1();
/*     */       }
/*     */ 
/* 529 */       this.cachedArguments = nvListToParameterArray(this.request.arguments());
/*     */     }
/*     */ 
/* 537 */     return this.cachedArguments;
/*     */   }
/*     */ 
/*     */   public TypeCode[] exceptions()
/*     */   {
/* 544 */     checkAccess(3);
/*     */ 
/* 546 */     if (this.cachedExceptions == null) {
/* 547 */       if (this.request == null) {
/* 548 */         throw this.stdWrapper.piOperationNotSupported2();
/*     */       }
/*     */ 
/* 553 */       ExceptionList localExceptionList = this.request.exceptions();
/* 554 */       int i = localExceptionList.count();
/* 555 */       TypeCode[] arrayOfTypeCode = new TypeCode[i];
/*     */       try {
/* 557 */         for (int j = 0; j < i; j++)
/* 558 */           arrayOfTypeCode[j] = localExceptionList.item(j);
/*     */       }
/*     */       catch (Exception localException) {
/* 561 */         throw this.wrapper.exceptionInExceptions(localException);
/*     */       }
/*     */ 
/* 564 */       this.cachedExceptions = arrayOfTypeCode;
/*     */     }
/*     */ 
/* 572 */     return this.cachedExceptions;
/*     */   }
/*     */ 
/*     */   public String[] contexts()
/*     */   {
/* 579 */     checkAccess(4);
/*     */ 
/* 581 */     if (this.cachedContexts == null) {
/* 582 */       if (this.request == null) {
/* 583 */         throw this.stdWrapper.piOperationNotSupported3();
/*     */       }
/*     */ 
/* 588 */       ContextList localContextList = this.request.contexts();
/* 589 */       int i = localContextList.count();
/* 590 */       String[] arrayOfString = new String[i];
/*     */       try {
/* 592 */         for (int j = 0; j < i; j++)
/* 593 */           arrayOfString[j] = localContextList.item(j);
/*     */       }
/*     */       catch (Exception localException) {
/* 596 */         throw this.wrapper.exceptionInContexts(localException);
/*     */       }
/*     */ 
/* 599 */       this.cachedContexts = arrayOfString;
/*     */     }
/*     */ 
/* 606 */     return this.cachedContexts;
/*     */   }
/*     */ 
/*     */   public String[] operation_context()
/*     */   {
/* 613 */     checkAccess(5);
/*     */ 
/* 615 */     if (this.cachedOperationContext == null) {
/* 616 */       if (this.request == null) {
/* 617 */         throw this.stdWrapper.piOperationNotSupported4();
/*     */       }
/*     */ 
/* 622 */       Context localContext = this.request.ctx();
/*     */ 
/* 633 */       NVList localNVList = localContext.get_values("", 15, "*");
/* 634 */       String[] arrayOfString = new String[localNVList.count() * 2];
/* 635 */       if ((localNVList != null) && (localNVList.count() != 0))
/*     */       {
/* 638 */         int i = 0;
/* 639 */         for (int j = 0; j < localNVList.count(); j++) {
/*     */           NamedValue localNamedValue;
/*     */           try {
/* 642 */             localNamedValue = localNVList.item(j);
/*     */           }
/*     */           catch (Exception localException) {
/* 645 */             return (String[])null;
/*     */           }
/* 647 */           arrayOfString[i] = localNamedValue.name();
/* 648 */           i++;
/* 649 */           arrayOfString[i] = localNamedValue.value().extract_string();
/* 650 */           i++;
/*     */         }
/*     */       }
/*     */ 
/* 654 */       this.cachedOperationContext = arrayOfString;
/*     */     }
/*     */ 
/* 661 */     return this.cachedOperationContext;
/*     */   }
/*     */ 
/*     */   public Any result()
/*     */   {
/* 668 */     checkAccess(6);
/*     */ 
/* 670 */     if (this.cachedResult == null) {
/* 671 */       if (this.request == null) {
/* 672 */         throw this.stdWrapper.piOperationNotSupported5();
/*     */       }
/*     */ 
/* 675 */       NamedValue localNamedValue = this.request.result();
/*     */ 
/* 677 */       if (localNamedValue == null) {
/* 678 */         throw this.wrapper.piDiiResultIsNull();
/*     */       }
/*     */ 
/* 681 */       this.cachedResult = localNamedValue.value();
/*     */     }
/*     */ 
/* 688 */     return this.cachedResult;
/*     */   }
/*     */ 
/*     */   public boolean response_expected()
/*     */   {
/* 697 */     return !this.messageMediator.isOneWay();
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object forward_reference()
/*     */   {
/* 704 */     checkAccess(10);
/*     */ 
/* 708 */     if (this.replyStatus != 3) {
/* 709 */       throw this.stdWrapper.invalidPiCall1();
/*     */     }
/*     */ 
/* 715 */     IOR localIOR = getLocatedIOR();
/* 716 */     return iorToObject(localIOR);
/*     */   }
/*     */ 
/*     */   private IOR getLocatedIOR()
/*     */   {
/* 722 */     CorbaContactInfoList localCorbaContactInfoList = (CorbaContactInfoList)this.messageMediator.getContactInfo().getContactInfoList();
/*     */ 
/* 724 */     IOR localIOR = localCorbaContactInfoList.getEffectiveTargetIOR();
/* 725 */     return localIOR;
/*     */   }
/*     */ 
/*     */   protected void setLocatedIOR(IOR paramIOR)
/*     */   {
/* 730 */     ORB localORB = (ORB)this.messageMediator.getBroker();
/*     */ 
/* 732 */     CorbaContactInfoListIterator localCorbaContactInfoListIterator = (CorbaContactInfoListIterator)((CorbaInvocationInfo)localORB.getInvocationInfo()).getContactInfoListIterator();
/*     */ 
/* 738 */     localCorbaContactInfoListIterator.reportRedirect((CorbaContactInfo)this.messageMediator.getContactInfo(), paramIOR);
/*     */   }
/*     */ 
/*     */   public ServiceContext get_request_service_context(int paramInt)
/*     */   {
/* 747 */     checkAccess(12);
/*     */ 
/* 749 */     if (this.cachedRequestServiceContexts == null) {
/* 750 */       this.cachedRequestServiceContexts = new HashMap();
/*     */     }
/*     */ 
/* 753 */     return getServiceContext(this.cachedRequestServiceContexts, this.messageMediator.getRequestServiceContexts(), paramInt);
/*     */   }
/*     */ 
/*     */   public ServiceContext get_reply_service_context(int paramInt)
/*     */   {
/* 763 */     checkAccess(13);
/*     */ 
/* 765 */     if (this.cachedReplyServiceContexts == null) {
/* 766 */       this.cachedReplyServiceContexts = new HashMap();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 785 */       ServiceContexts localServiceContexts = this.messageMediator.getReplyServiceContexts();
/*     */ 
/* 787 */       if (localServiceContexts == null) {
/* 788 */         throw new NullPointerException();
/*     */       }
/* 790 */       return getServiceContext(this.cachedReplyServiceContexts, localServiceContexts, paramInt);
/*     */     }
/*     */     catch (NullPointerException localNullPointerException)
/*     */     {
/* 797 */       throw this.stdWrapper.invalidServiceContextId(localNullPointerException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Connection connection()
/*     */   {
/* 808 */     return (Connection)this.messageMediator.getConnection();
/*     */   }
/*     */ 
/*     */   protected void setInfo(MessageMediator paramMessageMediator)
/*     */   {
/* 821 */     this.messageMediator = ((CorbaMessageMediator)paramMessageMediator);
/*     */ 
/* 823 */     this.messageMediator.setDIIInfo(this.request);
/*     */   }
/*     */ 
/*     */   void setRetryRequest(RetryType paramRetryType)
/*     */   {
/* 830 */     this.retryRequest = paramRetryType;
/*     */   }
/*     */ 
/*     */   RetryType getRetryRequest()
/*     */   {
/* 838 */     return this.retryRequest;
/*     */   }
/*     */ 
/*     */   void incrementEntryCount()
/*     */   {
/* 845 */     this.entryCount += 1;
/*     */   }
/*     */ 
/*     */   void decrementEntryCount()
/*     */   {
/* 852 */     this.entryCount -= 1;
/*     */   }
/*     */ 
/*     */   int getEntryCount()
/*     */   {
/* 859 */     return this.entryCount;
/*     */   }
/*     */ 
/*     */   protected void setReplyStatus(short paramShort)
/*     */   {
/* 867 */     super.setReplyStatus(paramShort);
/* 868 */     switch (paramShort) {
/*     */     case 0:
/* 870 */       this.endingPointCall = 0;
/* 871 */       break;
/*     */     case 1:
/*     */     case 2:
/* 874 */       this.endingPointCall = 1;
/* 875 */       break;
/*     */     case 3:
/*     */     case 4:
/* 878 */       this.endingPointCall = 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setDIIRequest(Request paramRequest)
/*     */   {
/* 887 */     this.request = paramRequest;
/*     */   }
/*     */ 
/*     */   protected void setDIIInitiate(boolean paramBoolean)
/*     */   {
/* 896 */     this.diiInitiate = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected boolean isDIIInitiate()
/*     */   {
/* 903 */     return this.diiInitiate;
/*     */   }
/*     */ 
/*     */   protected void setPICurrentPushed(boolean paramBoolean)
/*     */   {
/* 913 */     this.piCurrentPushed = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected boolean isPICurrentPushed() {
/* 917 */     return this.piCurrentPushed;
/*     */   }
/*     */ 
/*     */   protected void setException(Exception paramException)
/*     */   {
/* 924 */     super.setException(paramException);
/*     */ 
/* 927 */     this.cachedReceivedException = null;
/* 928 */     this.cachedReceivedExceptionId = null;
/*     */   }
/*     */ 
/*     */   protected boolean getIsOneWay() {
/* 932 */     return !response_expected();
/*     */   }
/*     */ 
/*     */   protected void checkAccess(int paramInt)
/*     */     throws BAD_INV_ORDER
/*     */   {
/* 943 */     int i = 0;
/* 944 */     switch (this.currentExecutionPoint) {
/*     */     case 0:
/* 946 */       switch (this.startingPointCall) {
/*     */       case 0:
/* 948 */         i = 0;
/* 949 */         break;
/*     */       case 1:
/* 951 */         i = 1;
/*     */       }
/*     */ 
/* 954 */       break;
/*     */     case 2:
/* 956 */       switch (this.endingPointCall) {
/*     */       case 0:
/* 958 */         i = 2;
/* 959 */         break;
/*     */       case 1:
/* 961 */         i = 3;
/* 962 */         break;
/*     */       case 2:
/* 964 */         i = 4;
/*     */       }
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 971 */     if (validCall[paramInt][i] == 0)
/* 972 */       throw this.stdWrapper.invalidPiCall2();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.ClientRequestInfoImpl
 * JD-Core Version:    0.6.2
 */