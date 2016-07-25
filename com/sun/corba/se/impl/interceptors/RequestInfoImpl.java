/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*     */ import com.sun.corba.se.impl.logging.InterceptorsSystemException;
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.util.RepositoryId;
/*     */ import com.sun.corba.se.impl.util.RepositoryIdCache;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.legacy.connection.Connection;
/*     */ import com.sun.corba.se.spi.legacy.interceptor.RequestInfoExt;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.PIHandler;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*     */ import com.sun.corba.se.spi.servicecontext.UnknownServiceContext;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_INV_ORDER;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.LocalObject;
/*     */ import org.omg.CORBA.NVList;
/*     */ import org.omg.CORBA.NamedValue;
/*     */ import org.omg.CORBA.ParameterMode;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.UNKNOWN;
/*     */ import org.omg.CORBA.UserException;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.Dynamic.Parameter;
/*     */ import org.omg.IOP.ServiceContextHelper;
/*     */ import org.omg.PortableInterceptor.ForwardRequest;
/*     */ import org.omg.PortableInterceptor.InvalidSlot;
/*     */ import org.omg.PortableInterceptor.RequestInfo;
/*     */ import sun.corba.JavaCorbaAccess;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ import sun.corba.SharedSecrets;
/*     */ 
/*     */ public abstract class RequestInfoImpl extends LocalObject
/*     */   implements RequestInfo, RequestInfoExt
/*     */ {
/*     */   protected ORB myORB;
/*     */   protected InterceptorsSystemException wrapper;
/*     */   protected OMGSystemException stdWrapper;
/* 118 */   protected int flowStackIndex = 0;
/*     */   protected int startingPointCall;
/*     */   protected int intermediatePointCall;
/*     */   protected int endingPointCall;
/* 138 */   protected short replyStatus = -1;
/*     */   protected static final short UNINITIALIZED = -1;
/*     */   protected int currentExecutionPoint;
/*     */   protected static final int EXECUTION_POINT_STARTING = 0;
/*     */   protected static final int EXECUTION_POINT_INTERMEDIATE = 1;
/*     */   protected static final int EXECUTION_POINT_ENDING = 2;
/*     */   protected boolean alreadyExecuted;
/*     */   protected Connection connection;
/*     */   protected ServiceContexts serviceContexts;
/*     */   protected ForwardRequest forwardRequest;
/*     */   protected IOR forwardRequestIOR;
/*     */   protected SlotTable slotTable;
/*     */   protected Exception exception;
/*     */   protected static final int MID_REQUEST_ID = 0;
/*     */   protected static final int MID_OPERATION = 1;
/*     */   protected static final int MID_ARGUMENTS = 2;
/*     */   protected static final int MID_EXCEPTIONS = 3;
/*     */   protected static final int MID_CONTEXTS = 4;
/*     */   protected static final int MID_OPERATION_CONTEXT = 5;
/*     */   protected static final int MID_RESULT = 6;
/*     */   protected static final int MID_RESPONSE_EXPECTED = 7;
/*     */   protected static final int MID_SYNC_SCOPE = 8;
/*     */   protected static final int MID_REPLY_STATUS = 9;
/*     */   protected static final int MID_FORWARD_REFERENCE = 10;
/*     */   protected static final int MID_GET_SLOT = 11;
/*     */   protected static final int MID_GET_REQUEST_SERVICE_CONTEXT = 12;
/*     */   protected static final int MID_GET_REPLY_SERVICE_CONTEXT = 13;
/*     */   protected static final int MID_RI_LAST = 13;
/*     */ 
/*     */   void reset()
/*     */   {
/* 187 */     this.flowStackIndex = 0;
/* 188 */     this.startingPointCall = 0;
/* 189 */     this.intermediatePointCall = 0;
/* 190 */     this.endingPointCall = 0;
/*     */ 
/* 192 */     setReplyStatus((short)-1);
/* 193 */     this.currentExecutionPoint = 0;
/* 194 */     this.alreadyExecuted = false;
/* 195 */     this.connection = null;
/* 196 */     this.serviceContexts = null;
/* 197 */     this.forwardRequest = null;
/* 198 */     this.forwardRequestIOR = null;
/* 199 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public RequestInfoImpl(ORB paramORB)
/*     */   {
/* 241 */     this.myORB = paramORB;
/* 242 */     this.wrapper = InterceptorsSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/* 244 */     this.stdWrapper = OMGSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/* 248 */     PICurrent localPICurrent = (PICurrent)paramORB.getPIHandler().getPICurrent();
/* 249 */     this.slotTable = localPICurrent.getSlotTable();
/*     */   }
/*     */ 
/*     */   public abstract int request_id();
/*     */ 
/*     */   public abstract String operation();
/*     */ 
/*     */   public abstract Parameter[] arguments();
/*     */ 
/*     */   public abstract TypeCode[] exceptions();
/*     */ 
/*     */   public abstract String[] contexts();
/*     */ 
/*     */   public abstract String[] operation_context();
/*     */ 
/*     */   public abstract Any result();
/*     */ 
/*     */   public abstract boolean response_expected();
/*     */ 
/*     */   public short sync_scope()
/*     */   {
/* 332 */     checkAccess(8);
/* 333 */     return 1;
/*     */   }
/*     */ 
/*     */   public short reply_status()
/*     */   {
/* 348 */     checkAccess(9);
/* 349 */     return this.replyStatus;
/*     */   }
/*     */ 
/*     */   public abstract org.omg.CORBA.Object forward_reference();
/*     */ 
/*     */   public Any get_slot(int paramInt)
/*     */     throws InvalidSlot
/*     */   {
/* 380 */     return this.slotTable.get_slot(paramInt);
/*     */   }
/*     */ 
/*     */   public abstract org.omg.IOP.ServiceContext get_request_service_context(int paramInt);
/*     */ 
/*     */   public abstract org.omg.IOP.ServiceContext get_reply_service_context(int paramInt);
/*     */ 
/*     */   public Connection connection()
/*     */   {
/* 429 */     return this.connection;
/*     */   }
/*     */ 
/*     */   private void insertApplicationException(ApplicationException paramApplicationException, Any paramAny)
/*     */     throws UNKNOWN
/*     */   {
/*     */     try
/*     */     {
/* 450 */       RepositoryId localRepositoryId = RepositoryId.cache.getId(paramApplicationException.getId());
/*     */ 
/* 452 */       String str1 = localRepositoryId.getClassName();
/*     */ 
/* 455 */       String str2 = str1 + "Helper";
/* 456 */       Class localClass = SharedSecrets.getJavaCorbaAccess().loadClass(str2);
/*     */ 
/* 458 */       Class[] arrayOfClass = new Class[1];
/* 459 */       arrayOfClass[0] = org.omg.CORBA.portable.InputStream.class;
/* 460 */       Method localMethod = localClass.getMethod("read", arrayOfClass);
/*     */ 
/* 465 */       org.omg.CORBA.portable.InputStream localInputStream = paramApplicationException.getInputStream();
/* 466 */       localInputStream.mark(0);
/* 467 */       UserException localUserException = null;
/*     */       try {
/* 469 */         java.lang.Object[] arrayOfObject = new java.lang.Object[1];
/* 470 */         arrayOfObject[0] = localInputStream;
/* 471 */         localUserException = (UserException)localMethod.invoke(null, arrayOfObject);
/*     */       }
/*     */       finally
/*     */       {
/*     */         try {
/* 476 */           localInputStream.reset();
/*     */         }
/*     */         catch (IOException localIOException2) {
/* 479 */           throw this.wrapper.markAndResetFailed(localIOException2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 485 */       insertUserException(localUserException, paramAny);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 487 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localClassNotFoundException);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 489 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localNoSuchMethodException);
/*     */     } catch (SecurityException localSecurityException) {
/* 491 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localSecurityException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 493 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localIllegalAccessException);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 495 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localIllegalArgumentException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 497 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void insertUserException(UserException paramUserException, Any paramAny)
/*     */     throws UNKNOWN
/*     */   {
/*     */     try
/*     */     {
/* 513 */       if (paramUserException != null) {
/* 514 */         Class localClass1 = paramUserException.getClass();
/* 515 */         String str1 = localClass1.getName();
/* 516 */         String str2 = str1 + "Helper";
/* 517 */         Class localClass2 = SharedSecrets.getJavaCorbaAccess().loadClass(str2);
/*     */ 
/* 521 */         Class[] arrayOfClass = new Class[2];
/* 522 */         arrayOfClass[0] = Any.class;
/* 523 */         arrayOfClass[1] = localClass1;
/* 524 */         Method localMethod = localClass2.getMethod("insert", arrayOfClass);
/*     */ 
/* 528 */         java.lang.Object[] arrayOfObject = new java.lang.Object[2];
/*     */ 
/* 530 */         arrayOfObject[0] = paramAny;
/* 531 */         arrayOfObject[1] = paramUserException;
/* 532 */         localMethod.invoke(null, arrayOfObject);
/*     */       }
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 535 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localClassNotFoundException);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 537 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localNoSuchMethodException);
/*     */     } catch (SecurityException localSecurityException) {
/* 539 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localSecurityException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 541 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localIllegalAccessException);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 543 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localIllegalArgumentException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 545 */       throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Parameter[] nvListToParameterArray(NVList paramNVList)
/*     */   {
/* 562 */     int i = paramNVList.count();
/* 563 */     Parameter[] arrayOfParameter = new Parameter[i];
/*     */     try {
/* 565 */       for (int j = 0; j < i; j++) {
/* 566 */         Parameter localParameter = new Parameter();
/* 567 */         arrayOfParameter[j] = localParameter;
/* 568 */         NamedValue localNamedValue = paramNVList.item(j);
/* 569 */         arrayOfParameter[j].argument = localNamedValue.value();
/*     */ 
/* 577 */         arrayOfParameter[j].mode = ParameterMode.from_int(localNamedValue.flags() - 1);
/*     */       }
/*     */     } catch (Exception localException) {
/* 580 */       throw this.wrapper.exceptionInArguments(localException);
/*     */     }
/*     */ 
/* 583 */     return arrayOfParameter;
/*     */   }
/*     */ 
/*     */   protected Any exceptionToAny(Exception paramException)
/*     */   {
/* 593 */     Any localAny = this.myORB.create_any();
/*     */ 
/* 595 */     if (paramException == null)
/*     */     {
/* 598 */       throw this.wrapper.exceptionWasNull2();
/* 599 */     }if ((paramException instanceof SystemException)) {
/* 600 */       ORBUtility.insertSystemException((SystemException)paramException, localAny);
/*     */     }
/* 602 */     else if ((paramException instanceof ApplicationException))
/*     */     {
/*     */       try
/*     */       {
/* 608 */         ApplicationException localApplicationException = (ApplicationException)paramException;
/*     */ 
/* 610 */         insertApplicationException(localApplicationException, localAny);
/*     */       }
/*     */       catch (UNKNOWN localUNKNOWN1)
/*     */       {
/* 617 */         ORBUtility.insertSystemException(localUNKNOWN1, localAny);
/*     */       }
/*     */     }
/* 619 */     else if ((paramException instanceof UserException)) {
/*     */       try {
/* 621 */         UserException localUserException = (UserException)paramException;
/* 622 */         insertUserException(localUserException, localAny);
/*     */       } catch (UNKNOWN localUNKNOWN2) {
/* 624 */         ORBUtility.insertSystemException(localUNKNOWN2, localAny);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 629 */     return localAny;
/*     */   }
/*     */ 
/*     */   protected org.omg.IOP.ServiceContext getServiceContext(HashMap paramHashMap, ServiceContexts paramServiceContexts, int paramInt)
/*     */   {
/* 641 */     org.omg.IOP.ServiceContext localServiceContext = null;
/* 642 */     Integer localInteger = new Integer(paramInt);
/*     */ 
/* 645 */     localServiceContext = (org.omg.IOP.ServiceContext)paramHashMap.get(localInteger);
/*     */ 
/* 651 */     if (localServiceContext == null)
/*     */     {
/* 654 */       com.sun.corba.se.spi.servicecontext.ServiceContext localServiceContext1 = paramServiceContexts.get(paramInt);
/*     */ 
/* 656 */       if (localServiceContext1 == null) {
/* 657 */         throw this.stdWrapper.invalidServiceContextId();
/*     */       }
/*     */ 
/* 662 */       EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.myORB);
/*     */ 
/* 665 */       localServiceContext1.write(localEncapsOutputStream, GIOPVersion.V1_2);
/* 666 */       org.omg.CORBA.portable.InputStream localInputStream = localEncapsOutputStream.create_input_stream();
/* 667 */       localServiceContext = ServiceContextHelper.read(localInputStream);
/*     */ 
/* 669 */       paramHashMap.put(localInteger, localServiceContext);
/*     */     }
/*     */ 
/* 676 */     return localServiceContext;
/*     */   }
/*     */ 
/*     */   protected void addServiceContext(HashMap paramHashMap, ServiceContexts paramServiceContexts, org.omg.IOP.ServiceContext paramServiceContext, boolean paramBoolean)
/*     */   {
/* 697 */     int i = 0;
/*     */ 
/* 699 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.myORB);
/*     */ 
/* 701 */     org.omg.CORBA.portable.InputStream localInputStream = null;
/* 702 */     UnknownServiceContext localUnknownServiceContext = null;
/* 703 */     ServiceContextHelper.write(localEncapsOutputStream, paramServiceContext);
/* 704 */     localInputStream = localEncapsOutputStream.create_input_stream();
/*     */ 
/* 707 */     localUnknownServiceContext = new UnknownServiceContext(localInputStream.read_long(), (org.omg.CORBA_2_3.portable.InputStream)localInputStream);
/*     */ 
/* 711 */     i = localUnknownServiceContext.getId();
/*     */ 
/* 713 */     if (paramServiceContexts.get(i) != null) {
/* 714 */       if (paramBoolean)
/* 715 */         paramServiceContexts.delete(i);
/*     */       else
/* 717 */         throw this.stdWrapper.serviceContextAddFailed(new Integer(i));
/*     */     }
/* 719 */     paramServiceContexts.put(localUnknownServiceContext);
/*     */ 
/* 722 */     paramHashMap.put(new Integer(i), paramServiceContext);
/*     */   }
/*     */ 
/*     */   protected void setFlowStackIndex(int paramInt)
/*     */   {
/* 736 */     this.flowStackIndex = paramInt;
/*     */   }
/*     */ 
/*     */   protected int getFlowStackIndex()
/*     */   {
/* 745 */     return this.flowStackIndex;
/*     */   }
/*     */ 
/*     */   protected void setEndingPointCall(int paramInt)
/*     */   {
/* 753 */     this.endingPointCall = paramInt;
/*     */   }
/*     */ 
/*     */   protected int getEndingPointCall()
/*     */   {
/* 761 */     return this.endingPointCall;
/*     */   }
/*     */ 
/*     */   protected void setIntermediatePointCall(int paramInt)
/*     */   {
/* 769 */     this.intermediatePointCall = paramInt;
/*     */   }
/*     */ 
/*     */   protected int getIntermediatePointCall()
/*     */   {
/* 777 */     return this.intermediatePointCall;
/*     */   }
/*     */ 
/*     */   protected void setStartingPointCall(int paramInt)
/*     */   {
/* 785 */     this.startingPointCall = paramInt;
/*     */   }
/*     */ 
/*     */   protected int getStartingPointCall()
/*     */   {
/* 793 */     return this.startingPointCall;
/*     */   }
/*     */ 
/*     */   protected boolean getAlreadyExecuted()
/*     */   {
/* 801 */     return this.alreadyExecuted;
/*     */   }
/*     */ 
/*     */   protected void setAlreadyExecuted(boolean paramBoolean)
/*     */   {
/* 809 */     this.alreadyExecuted = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void setReplyStatus(short paramShort)
/*     */   {
/* 816 */     this.replyStatus = paramShort;
/*     */   }
/*     */ 
/*     */   protected short getReplyStatus()
/*     */   {
/* 824 */     return this.replyStatus;
/*     */   }
/*     */ 
/*     */   protected void setForwardRequest(ForwardRequest paramForwardRequest)
/*     */   {
/* 832 */     this.forwardRequest = paramForwardRequest;
/* 833 */     this.forwardRequestIOR = null;
/*     */   }
/*     */ 
/*     */   protected void setForwardRequest(IOR paramIOR)
/*     */   {
/* 841 */     this.forwardRequestIOR = paramIOR;
/* 842 */     this.forwardRequest = null;
/*     */   }
/*     */ 
/*     */   protected ForwardRequest getForwardRequestException()
/*     */   {
/* 849 */     if ((this.forwardRequest == null) && 
/* 850 */       (this.forwardRequestIOR != null))
/*     */     {
/* 853 */       org.omg.CORBA.Object localObject = iorToObject(this.forwardRequestIOR);
/* 854 */       this.forwardRequest = new ForwardRequest(localObject);
/*     */     }
/*     */ 
/* 858 */     return this.forwardRequest;
/*     */   }
/*     */ 
/*     */   protected IOR getForwardRequestIOR()
/*     */   {
/* 865 */     if ((this.forwardRequestIOR == null) && 
/* 866 */       (this.forwardRequest != null)) {
/* 867 */       this.forwardRequestIOR = ORBUtility.getIOR(this.forwardRequest.forward);
/*     */     }
/*     */ 
/* 872 */     return this.forwardRequestIOR;
/*     */   }
/*     */ 
/*     */   protected void setException(Exception paramException)
/*     */   {
/* 880 */     this.exception = paramException;
/*     */   }
/*     */ 
/*     */   Exception getException()
/*     */   {
/* 888 */     return this.exception;
/*     */   }
/*     */ 
/*     */   protected void setCurrentExecutionPoint(int paramInt)
/*     */   {
/* 897 */     this.currentExecutionPoint = paramInt;
/*     */   }
/*     */ 
/*     */   protected abstract void checkAccess(int paramInt)
/*     */     throws BAD_INV_ORDER;
/*     */ 
/*     */   void setSlotTable(SlotTable paramSlotTable)
/*     */   {
/* 922 */     this.slotTable = paramSlotTable;
/*     */   }
/*     */ 
/*     */   protected org.omg.CORBA.Object iorToObject(IOR paramIOR)
/*     */   {
/* 927 */     return ORBUtility.makeObjectReference(paramIOR);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.RequestInfoImpl
 * JD-Core Version:    0.6.2
 */