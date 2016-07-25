/*     */ package com.sun.corba.se.impl.logging;
/*     */ 
/*     */ import com.sun.corba.se.spi.logging.LogWrapperBase;
/*     */ import com.sun.corba.se.spi.logging.LogWrapperFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.omg.CORBA.BAD_INV_ORDER;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.COMM_FAILURE;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.INTERNAL;
/*     */ import org.omg.CORBA.NO_IMPLEMENT;
/*     */ import org.omg.CORBA.OBJECT_NOT_EXIST;
/*     */ import org.omg.CORBA.UNKNOWN;
/*     */ 
/*     */ public class InterceptorsSystemException extends LogWrapperBase
/*     */ {
/*  37 */   private static LogWrapperFactory factory = new LogWrapperFactory()
/*     */   {
/*     */     public LogWrapperBase create(Logger paramAnonymousLogger) {
/*  40 */       return new InterceptorsSystemException(paramAnonymousLogger);
/*     */     }
/*  37 */   };
/*     */   public static final int TYPE_OUT_OF_RANGE = 1398080289;
/*     */   public static final int NAME_NULL = 1398080290;
/*     */   public static final int RIR_INVALID_PRE_INIT = 1398080289;
/*     */   public static final int BAD_STATE1 = 1398080290;
/*     */   public static final int BAD_STATE2 = 1398080291;
/*     */   public static final int IOEXCEPTION_DURING_CANCEL_REQUEST = 1398080289;
/*     */   public static final int EXCEPTION_WAS_NULL = 1398080289;
/*     */   public static final int OBJECT_HAS_NO_DELEGATE = 1398080290;
/*     */   public static final int DELEGATE_NOT_CLIENTSUB = 1398080291;
/*     */   public static final int OBJECT_NOT_OBJECTIMPL = 1398080292;
/*     */   public static final int EXCEPTION_INVALID = 1398080293;
/*     */   public static final int REPLY_STATUS_NOT_INIT = 1398080294;
/*     */   public static final int EXCEPTION_IN_ARGUMENTS = 1398080295;
/*     */   public static final int EXCEPTION_IN_EXCEPTIONS = 1398080296;
/*     */   public static final int EXCEPTION_IN_CONTEXTS = 1398080297;
/*     */   public static final int EXCEPTION_WAS_NULL_2 = 1398080298;
/*     */   public static final int SERVANT_INVALID = 1398080299;
/*     */   public static final int CANT_POP_ONLY_PICURRENT = 1398080300;
/*     */   public static final int CANT_POP_ONLY_CURRENT_2 = 1398080301;
/*     */   public static final int PI_DSI_RESULT_IS_NULL = 1398080302;
/*     */   public static final int PI_DII_RESULT_IS_NULL = 1398080303;
/*     */   public static final int EXCEPTION_UNAVAILABLE = 1398080304;
/*     */   public static final int CLIENT_INFO_STACK_NULL = 1398080305;
/*     */   public static final int SERVER_INFO_STACK_NULL = 1398080306;
/*     */   public static final int MARK_AND_RESET_FAILED = 1398080307;
/*     */   public static final int SLOT_TABLE_INVARIANT = 1398080308;
/*     */   public static final int INTERCEPTOR_LIST_LOCKED = 1398080309;
/*     */   public static final int SORT_SIZE_MISMATCH = 1398080310;
/*     */   public static final int PI_ORB_NOT_POLICY_BASED = 1398080289;
/*     */   public static final int ORBINITINFO_INVALID = 1398080289;
/*     */   public static final int UNKNOWN_REQUEST_INVOKE = 1398080289;
/*     */ 
/*     */   public InterceptorsSystemException(Logger paramLogger)
/*     */   {
/*  34 */     super(paramLogger);
/*     */   }
/*     */ 
/*     */   public static InterceptorsSystemException get(ORB paramORB, String paramString)
/*     */   {
/*  46 */     InterceptorsSystemException localInterceptorsSystemException = (InterceptorsSystemException)paramORB.getLogWrapper(paramString, "INTERCEPTORS", factory);
/*     */ 
/*  49 */     return localInterceptorsSystemException;
/*     */   }
/*     */ 
/*     */   public static InterceptorsSystemException get(String paramString)
/*     */   {
/*  54 */     InterceptorsSystemException localInterceptorsSystemException = (InterceptorsSystemException)ORB.staticGetLogWrapper(paramString, "INTERCEPTORS", factory);
/*     */ 
/*  57 */     return localInterceptorsSystemException;
/*     */   }
/*     */ 
/*     */   public BAD_PARAM typeOutOfRange(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*     */   {
/*  67 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398080289, paramCompletionStatus);
/*  68 */     if (paramThrowable != null) {
/*  69 */       localBAD_PARAM.initCause(paramThrowable);
/*     */     }
/*  71 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  72 */       Object[] arrayOfObject = new Object[1];
/*  73 */       arrayOfObject[0] = paramObject;
/*  74 */       doLog(Level.WARNING, "INTERCEPTORS.typeOutOfRange", arrayOfObject, InterceptorsSystemException.class, localBAD_PARAM);
/*     */     }
/*     */ 
/*  78 */     return localBAD_PARAM;
/*     */   }
/*     */ 
/*     */   public BAD_PARAM typeOutOfRange(CompletionStatus paramCompletionStatus, Object paramObject) {
/*  82 */     return typeOutOfRange(paramCompletionStatus, null, paramObject);
/*     */   }
/*     */ 
/*     */   public BAD_PARAM typeOutOfRange(Throwable paramThrowable, Object paramObject) {
/*  86 */     return typeOutOfRange(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*     */   }
/*     */ 
/*     */   public BAD_PARAM typeOutOfRange(Object paramObject) {
/*  90 */     return typeOutOfRange(CompletionStatus.COMPLETED_NO, null, paramObject);
/*     */   }
/*     */ 
/*     */   public BAD_PARAM nameNull(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/*  96 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398080290, paramCompletionStatus);
/*  97 */     if (paramThrowable != null) {
/*  98 */       localBAD_PARAM.initCause(paramThrowable);
/*     */     }
/* 100 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 101 */       Object[] arrayOfObject = null;
/* 102 */       doLog(Level.WARNING, "INTERCEPTORS.nameNull", arrayOfObject, InterceptorsSystemException.class, localBAD_PARAM);
/*     */     }
/*     */ 
/* 106 */     return localBAD_PARAM;
/*     */   }
/*     */ 
/*     */   public BAD_PARAM nameNull(CompletionStatus paramCompletionStatus) {
/* 110 */     return nameNull(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public BAD_PARAM nameNull(Throwable paramThrowable) {
/* 114 */     return nameNull(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public BAD_PARAM nameNull() {
/* 118 */     return nameNull(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER rirInvalidPreInit(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 128 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398080289, paramCompletionStatus);
/* 129 */     if (paramThrowable != null) {
/* 130 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*     */     }
/* 132 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 133 */       Object[] arrayOfObject = null;
/* 134 */       doLog(Level.WARNING, "INTERCEPTORS.rirInvalidPreInit", arrayOfObject, InterceptorsSystemException.class, localBAD_INV_ORDER);
/*     */     }
/*     */ 
/* 138 */     return localBAD_INV_ORDER;
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER rirInvalidPreInit(CompletionStatus paramCompletionStatus) {
/* 142 */     return rirInvalidPreInit(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER rirInvalidPreInit(Throwable paramThrowable) {
/* 146 */     return rirInvalidPreInit(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER rirInvalidPreInit() {
/* 150 */     return rirInvalidPreInit(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER badState1(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*     */   {
/* 156 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398080290, paramCompletionStatus);
/* 157 */     if (paramThrowable != null) {
/* 158 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*     */     }
/* 160 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 161 */       Object[] arrayOfObject = new Object[2];
/* 162 */       arrayOfObject[0] = paramObject1;
/* 163 */       arrayOfObject[1] = paramObject2;
/* 164 */       doLog(Level.WARNING, "INTERCEPTORS.badState1", arrayOfObject, InterceptorsSystemException.class, localBAD_INV_ORDER);
/*     */     }
/*     */ 
/* 168 */     return localBAD_INV_ORDER;
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER badState1(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 172 */     return badState1(paramCompletionStatus, null, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER badState1(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 176 */     return badState1(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER badState1(Object paramObject1, Object paramObject2) {
/* 180 */     return badState1(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER badState2(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3)
/*     */   {
/* 186 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398080291, paramCompletionStatus);
/* 187 */     if (paramThrowable != null) {
/* 188 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*     */     }
/* 190 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 191 */       Object[] arrayOfObject = new Object[3];
/* 192 */       arrayOfObject[0] = paramObject1;
/* 193 */       arrayOfObject[1] = paramObject2;
/* 194 */       arrayOfObject[2] = paramObject3;
/* 195 */       doLog(Level.WARNING, "INTERCEPTORS.badState2", arrayOfObject, InterceptorsSystemException.class, localBAD_INV_ORDER);
/*     */     }
/*     */ 
/* 199 */     return localBAD_INV_ORDER;
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER badState2(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 203 */     return badState2(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3);
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER badState2(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 207 */     return badState2(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3);
/*     */   }
/*     */ 
/*     */   public BAD_INV_ORDER badState2(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 211 */     return badState2(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3);
/*     */   }
/*     */ 
/*     */   public COMM_FAILURE ioexceptionDuringCancelRequest(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 221 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398080289, paramCompletionStatus);
/* 222 */     if (paramThrowable != null) {
/* 223 */       localCOMM_FAILURE.initCause(paramThrowable);
/*     */     }
/* 225 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 226 */       Object[] arrayOfObject = null;
/* 227 */       doLog(Level.WARNING, "INTERCEPTORS.ioexceptionDuringCancelRequest", arrayOfObject, InterceptorsSystemException.class, localCOMM_FAILURE);
/*     */     }
/*     */ 
/* 231 */     return localCOMM_FAILURE;
/*     */   }
/*     */ 
/*     */   public COMM_FAILURE ioexceptionDuringCancelRequest(CompletionStatus paramCompletionStatus) {
/* 235 */     return ioexceptionDuringCancelRequest(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public COMM_FAILURE ioexceptionDuringCancelRequest(Throwable paramThrowable) {
/* 239 */     return ioexceptionDuringCancelRequest(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public COMM_FAILURE ioexceptionDuringCancelRequest() {
/* 243 */     return ioexceptionDuringCancelRequest(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionWasNull(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 253 */     INTERNAL localINTERNAL = new INTERNAL(1398080289, paramCompletionStatus);
/* 254 */     if (paramThrowable != null) {
/* 255 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 257 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 258 */       Object[] arrayOfObject = null;
/* 259 */       doLog(Level.WARNING, "INTERCEPTORS.exceptionWasNull", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 263 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionWasNull(CompletionStatus paramCompletionStatus) {
/* 267 */     return exceptionWasNull(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionWasNull(Throwable paramThrowable) {
/* 271 */     return exceptionWasNull(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionWasNull() {
/* 275 */     return exceptionWasNull(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL objectHasNoDelegate(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 281 */     INTERNAL localINTERNAL = new INTERNAL(1398080290, paramCompletionStatus);
/* 282 */     if (paramThrowable != null) {
/* 283 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 285 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 286 */       Object[] arrayOfObject = null;
/* 287 */       doLog(Level.WARNING, "INTERCEPTORS.objectHasNoDelegate", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 291 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL objectHasNoDelegate(CompletionStatus paramCompletionStatus) {
/* 295 */     return objectHasNoDelegate(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL objectHasNoDelegate(Throwable paramThrowable) {
/* 299 */     return objectHasNoDelegate(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL objectHasNoDelegate() {
/* 303 */     return objectHasNoDelegate(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL delegateNotClientsub(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 309 */     INTERNAL localINTERNAL = new INTERNAL(1398080291, paramCompletionStatus);
/* 310 */     if (paramThrowable != null) {
/* 311 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 313 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 314 */       Object[] arrayOfObject = null;
/* 315 */       doLog(Level.WARNING, "INTERCEPTORS.delegateNotClientsub", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 319 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL delegateNotClientsub(CompletionStatus paramCompletionStatus) {
/* 323 */     return delegateNotClientsub(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL delegateNotClientsub(Throwable paramThrowable) {
/* 327 */     return delegateNotClientsub(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL delegateNotClientsub() {
/* 331 */     return delegateNotClientsub(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL objectNotObjectimpl(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 337 */     INTERNAL localINTERNAL = new INTERNAL(1398080292, paramCompletionStatus);
/* 338 */     if (paramThrowable != null) {
/* 339 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 341 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 342 */       Object[] arrayOfObject = null;
/* 343 */       doLog(Level.WARNING, "INTERCEPTORS.objectNotObjectimpl", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 347 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL objectNotObjectimpl(CompletionStatus paramCompletionStatus) {
/* 351 */     return objectNotObjectimpl(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL objectNotObjectimpl(Throwable paramThrowable) {
/* 355 */     return objectNotObjectimpl(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL objectNotObjectimpl() {
/* 359 */     return objectNotObjectimpl(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInvalid(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 365 */     INTERNAL localINTERNAL = new INTERNAL(1398080293, paramCompletionStatus);
/* 366 */     if (paramThrowable != null) {
/* 367 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 369 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 370 */       Object[] arrayOfObject = null;
/* 371 */       doLog(Level.WARNING, "INTERCEPTORS.exceptionInvalid", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 375 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInvalid(CompletionStatus paramCompletionStatus) {
/* 379 */     return exceptionInvalid(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInvalid(Throwable paramThrowable) {
/* 383 */     return exceptionInvalid(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInvalid() {
/* 387 */     return exceptionInvalid(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL replyStatusNotInit(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 393 */     INTERNAL localINTERNAL = new INTERNAL(1398080294, paramCompletionStatus);
/* 394 */     if (paramThrowable != null) {
/* 395 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 397 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 398 */       Object[] arrayOfObject = null;
/* 399 */       doLog(Level.WARNING, "INTERCEPTORS.replyStatusNotInit", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 403 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL replyStatusNotInit(CompletionStatus paramCompletionStatus) {
/* 407 */     return replyStatusNotInit(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL replyStatusNotInit(Throwable paramThrowable) {
/* 411 */     return replyStatusNotInit(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL replyStatusNotInit() {
/* 415 */     return replyStatusNotInit(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInArguments(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 421 */     INTERNAL localINTERNAL = new INTERNAL(1398080295, paramCompletionStatus);
/* 422 */     if (paramThrowable != null) {
/* 423 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 425 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 426 */       Object[] arrayOfObject = null;
/* 427 */       doLog(Level.WARNING, "INTERCEPTORS.exceptionInArguments", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 431 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInArguments(CompletionStatus paramCompletionStatus) {
/* 435 */     return exceptionInArguments(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInArguments(Throwable paramThrowable) {
/* 439 */     return exceptionInArguments(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInArguments() {
/* 443 */     return exceptionInArguments(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInExceptions(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 449 */     INTERNAL localINTERNAL = new INTERNAL(1398080296, paramCompletionStatus);
/* 450 */     if (paramThrowable != null) {
/* 451 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 453 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 454 */       Object[] arrayOfObject = null;
/* 455 */       doLog(Level.WARNING, "INTERCEPTORS.exceptionInExceptions", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 459 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInExceptions(CompletionStatus paramCompletionStatus) {
/* 463 */     return exceptionInExceptions(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInExceptions(Throwable paramThrowable) {
/* 467 */     return exceptionInExceptions(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInExceptions() {
/* 471 */     return exceptionInExceptions(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInContexts(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 477 */     INTERNAL localINTERNAL = new INTERNAL(1398080297, paramCompletionStatus);
/* 478 */     if (paramThrowable != null) {
/* 479 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 481 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 482 */       Object[] arrayOfObject = null;
/* 483 */       doLog(Level.WARNING, "INTERCEPTORS.exceptionInContexts", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 487 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInContexts(CompletionStatus paramCompletionStatus) {
/* 491 */     return exceptionInContexts(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInContexts(Throwable paramThrowable) {
/* 495 */     return exceptionInContexts(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionInContexts() {
/* 499 */     return exceptionInContexts(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionWasNull2(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 505 */     INTERNAL localINTERNAL = new INTERNAL(1398080298, paramCompletionStatus);
/* 506 */     if (paramThrowable != null) {
/* 507 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 509 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 510 */       Object[] arrayOfObject = null;
/* 511 */       doLog(Level.WARNING, "INTERCEPTORS.exceptionWasNull2", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 515 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionWasNull2(CompletionStatus paramCompletionStatus) {
/* 519 */     return exceptionWasNull2(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionWasNull2(Throwable paramThrowable) {
/* 523 */     return exceptionWasNull2(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionWasNull2() {
/* 527 */     return exceptionWasNull2(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL servantInvalid(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 533 */     INTERNAL localINTERNAL = new INTERNAL(1398080299, paramCompletionStatus);
/* 534 */     if (paramThrowable != null) {
/* 535 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 537 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 538 */       Object[] arrayOfObject = null;
/* 539 */       doLog(Level.WARNING, "INTERCEPTORS.servantInvalid", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 543 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL servantInvalid(CompletionStatus paramCompletionStatus) {
/* 547 */     return servantInvalid(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL servantInvalid(Throwable paramThrowable) {
/* 551 */     return servantInvalid(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL servantInvalid() {
/* 555 */     return servantInvalid(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL cantPopOnlyPicurrent(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 561 */     INTERNAL localINTERNAL = new INTERNAL(1398080300, paramCompletionStatus);
/* 562 */     if (paramThrowable != null) {
/* 563 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 565 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 566 */       Object[] arrayOfObject = null;
/* 567 */       doLog(Level.WARNING, "INTERCEPTORS.cantPopOnlyPicurrent", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 571 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL cantPopOnlyPicurrent(CompletionStatus paramCompletionStatus) {
/* 575 */     return cantPopOnlyPicurrent(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL cantPopOnlyPicurrent(Throwable paramThrowable) {
/* 579 */     return cantPopOnlyPicurrent(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL cantPopOnlyPicurrent() {
/* 583 */     return cantPopOnlyPicurrent(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL cantPopOnlyCurrent2(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 589 */     INTERNAL localINTERNAL = new INTERNAL(1398080301, paramCompletionStatus);
/* 590 */     if (paramThrowable != null) {
/* 591 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 593 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 594 */       Object[] arrayOfObject = null;
/* 595 */       doLog(Level.WARNING, "INTERCEPTORS.cantPopOnlyCurrent2", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 599 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL cantPopOnlyCurrent2(CompletionStatus paramCompletionStatus) {
/* 603 */     return cantPopOnlyCurrent2(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL cantPopOnlyCurrent2(Throwable paramThrowable) {
/* 607 */     return cantPopOnlyCurrent2(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL cantPopOnlyCurrent2() {
/* 611 */     return cantPopOnlyCurrent2(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL piDsiResultIsNull(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 617 */     INTERNAL localINTERNAL = new INTERNAL(1398080302, paramCompletionStatus);
/* 618 */     if (paramThrowable != null) {
/* 619 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 621 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 622 */       Object[] arrayOfObject = null;
/* 623 */       doLog(Level.WARNING, "INTERCEPTORS.piDsiResultIsNull", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 627 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL piDsiResultIsNull(CompletionStatus paramCompletionStatus) {
/* 631 */     return piDsiResultIsNull(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL piDsiResultIsNull(Throwable paramThrowable) {
/* 635 */     return piDsiResultIsNull(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL piDsiResultIsNull() {
/* 639 */     return piDsiResultIsNull(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL piDiiResultIsNull(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 645 */     INTERNAL localINTERNAL = new INTERNAL(1398080303, paramCompletionStatus);
/* 646 */     if (paramThrowable != null) {
/* 647 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 649 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 650 */       Object[] arrayOfObject = null;
/* 651 */       doLog(Level.WARNING, "INTERCEPTORS.piDiiResultIsNull", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 655 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL piDiiResultIsNull(CompletionStatus paramCompletionStatus) {
/* 659 */     return piDiiResultIsNull(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL piDiiResultIsNull(Throwable paramThrowable) {
/* 663 */     return piDiiResultIsNull(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL piDiiResultIsNull() {
/* 667 */     return piDiiResultIsNull(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionUnavailable(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 673 */     INTERNAL localINTERNAL = new INTERNAL(1398080304, paramCompletionStatus);
/* 674 */     if (paramThrowable != null) {
/* 675 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 677 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 678 */       Object[] arrayOfObject = null;
/* 679 */       doLog(Level.WARNING, "INTERCEPTORS.exceptionUnavailable", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 683 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionUnavailable(CompletionStatus paramCompletionStatus) {
/* 687 */     return exceptionUnavailable(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionUnavailable(Throwable paramThrowable) {
/* 691 */     return exceptionUnavailable(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL exceptionUnavailable() {
/* 695 */     return exceptionUnavailable(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL clientInfoStackNull(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 701 */     INTERNAL localINTERNAL = new INTERNAL(1398080305, paramCompletionStatus);
/* 702 */     if (paramThrowable != null) {
/* 703 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 705 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 706 */       Object[] arrayOfObject = null;
/* 707 */       doLog(Level.WARNING, "INTERCEPTORS.clientInfoStackNull", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 711 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL clientInfoStackNull(CompletionStatus paramCompletionStatus) {
/* 715 */     return clientInfoStackNull(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL clientInfoStackNull(Throwable paramThrowable) {
/* 719 */     return clientInfoStackNull(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL clientInfoStackNull() {
/* 723 */     return clientInfoStackNull(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL serverInfoStackNull(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 729 */     INTERNAL localINTERNAL = new INTERNAL(1398080306, paramCompletionStatus);
/* 730 */     if (paramThrowable != null) {
/* 731 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 733 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 734 */       Object[] arrayOfObject = null;
/* 735 */       doLog(Level.WARNING, "INTERCEPTORS.serverInfoStackNull", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 739 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL serverInfoStackNull(CompletionStatus paramCompletionStatus) {
/* 743 */     return serverInfoStackNull(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL serverInfoStackNull(Throwable paramThrowable) {
/* 747 */     return serverInfoStackNull(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL serverInfoStackNull() {
/* 751 */     return serverInfoStackNull(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL markAndResetFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 757 */     INTERNAL localINTERNAL = new INTERNAL(1398080307, paramCompletionStatus);
/* 758 */     if (paramThrowable != null) {
/* 759 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 761 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 762 */       Object[] arrayOfObject = null;
/* 763 */       doLog(Level.WARNING, "INTERCEPTORS.markAndResetFailed", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 767 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL markAndResetFailed(CompletionStatus paramCompletionStatus) {
/* 771 */     return markAndResetFailed(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL markAndResetFailed(Throwable paramThrowable) {
/* 775 */     return markAndResetFailed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL markAndResetFailed() {
/* 779 */     return markAndResetFailed(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL slotTableInvariant(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*     */   {
/* 785 */     INTERNAL localINTERNAL = new INTERNAL(1398080308, paramCompletionStatus);
/* 786 */     if (paramThrowable != null) {
/* 787 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 789 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 790 */       Object[] arrayOfObject = new Object[2];
/* 791 */       arrayOfObject[0] = paramObject1;
/* 792 */       arrayOfObject[1] = paramObject2;
/* 793 */       doLog(Level.WARNING, "INTERCEPTORS.slotTableInvariant", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 797 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL slotTableInvariant(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 801 */     return slotTableInvariant(paramCompletionStatus, null, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public INTERNAL slotTableInvariant(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 805 */     return slotTableInvariant(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public INTERNAL slotTableInvariant(Object paramObject1, Object paramObject2) {
/* 809 */     return slotTableInvariant(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public INTERNAL interceptorListLocked(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 815 */     INTERNAL localINTERNAL = new INTERNAL(1398080309, paramCompletionStatus);
/* 816 */     if (paramThrowable != null) {
/* 817 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 819 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 820 */       Object[] arrayOfObject = null;
/* 821 */       doLog(Level.WARNING, "INTERCEPTORS.interceptorListLocked", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 825 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL interceptorListLocked(CompletionStatus paramCompletionStatus) {
/* 829 */     return interceptorListLocked(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL interceptorListLocked(Throwable paramThrowable) {
/* 833 */     return interceptorListLocked(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL interceptorListLocked() {
/* 837 */     return interceptorListLocked(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL sortSizeMismatch(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 843 */     INTERNAL localINTERNAL = new INTERNAL(1398080310, paramCompletionStatus);
/* 844 */     if (paramThrowable != null) {
/* 845 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 847 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 848 */       Object[] arrayOfObject = null;
/* 849 */       doLog(Level.WARNING, "INTERCEPTORS.sortSizeMismatch", arrayOfObject, InterceptorsSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 853 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL sortSizeMismatch(CompletionStatus paramCompletionStatus) {
/* 857 */     return sortSizeMismatch(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL sortSizeMismatch(Throwable paramThrowable) {
/* 861 */     return sortSizeMismatch(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL sortSizeMismatch() {
/* 865 */     return sortSizeMismatch(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public NO_IMPLEMENT piOrbNotPolicyBased(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 875 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1398080289, paramCompletionStatus);
/* 876 */     if (paramThrowable != null) {
/* 877 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*     */     }
/* 879 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 880 */       Object[] arrayOfObject = null;
/* 881 */       doLog(Level.WARNING, "INTERCEPTORS.piOrbNotPolicyBased", arrayOfObject, InterceptorsSystemException.class, localNO_IMPLEMENT);
/*     */     }
/*     */ 
/* 885 */     return localNO_IMPLEMENT;
/*     */   }
/*     */ 
/*     */   public NO_IMPLEMENT piOrbNotPolicyBased(CompletionStatus paramCompletionStatus) {
/* 889 */     return piOrbNotPolicyBased(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public NO_IMPLEMENT piOrbNotPolicyBased(Throwable paramThrowable) {
/* 893 */     return piOrbNotPolicyBased(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public NO_IMPLEMENT piOrbNotPolicyBased() {
/* 897 */     return piOrbNotPolicyBased(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public OBJECT_NOT_EXIST orbinitinfoInvalid(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 907 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398080289, paramCompletionStatus);
/* 908 */     if (paramThrowable != null) {
/* 909 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*     */     }
/* 911 */     if (this.logger.isLoggable(Level.FINE)) {
/* 912 */       Object[] arrayOfObject = null;
/* 913 */       doLog(Level.FINE, "INTERCEPTORS.orbinitinfoInvalid", arrayOfObject, InterceptorsSystemException.class, localOBJECT_NOT_EXIST);
/*     */     }
/*     */ 
/* 917 */     return localOBJECT_NOT_EXIST;
/*     */   }
/*     */ 
/*     */   public OBJECT_NOT_EXIST orbinitinfoInvalid(CompletionStatus paramCompletionStatus) {
/* 921 */     return orbinitinfoInvalid(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public OBJECT_NOT_EXIST orbinitinfoInvalid(Throwable paramThrowable) {
/* 925 */     return orbinitinfoInvalid(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public OBJECT_NOT_EXIST orbinitinfoInvalid() {
/* 929 */     return orbinitinfoInvalid(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public UNKNOWN unknownRequestInvoke(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 939 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398080289, paramCompletionStatus);
/* 940 */     if (paramThrowable != null) {
/* 941 */       localUNKNOWN.initCause(paramThrowable);
/*     */     }
/* 943 */     if (this.logger.isLoggable(Level.FINE)) {
/* 944 */       Object[] arrayOfObject = null;
/* 945 */       doLog(Level.FINE, "INTERCEPTORS.unknownRequestInvoke", arrayOfObject, InterceptorsSystemException.class, localUNKNOWN);
/*     */     }
/*     */ 
/* 949 */     return localUNKNOWN;
/*     */   }
/*     */ 
/*     */   public UNKNOWN unknownRequestInvoke(CompletionStatus paramCompletionStatus) {
/* 953 */     return unknownRequestInvoke(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public UNKNOWN unknownRequestInvoke(Throwable paramThrowable) {
/* 957 */     return unknownRequestInvoke(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public UNKNOWN unknownRequestInvoke() {
/* 961 */     return unknownRequestInvoke(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.logging.InterceptorsSystemException
 * JD-Core Version:    0.6.2
 */