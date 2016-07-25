/*      */ package com.sun.corba.se.impl.logging;
/*      */ 
/*      */ import com.sun.corba.se.spi.logging.LogWrapperBase;
/*      */ import com.sun.corba.se.spi.logging.LogWrapperFactory;
/*      */ import com.sun.corba.se.spi.orb.ORB;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import org.omg.CORBA.BAD_INV_ORDER;
/*      */ import org.omg.CORBA.BAD_OPERATION;
/*      */ import org.omg.CORBA.BAD_PARAM;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.INITIALIZE;
/*      */ import org.omg.CORBA.INTERNAL;
/*      */ import org.omg.CORBA.NO_IMPLEMENT;
/*      */ import org.omg.CORBA.OBJECT_NOT_EXIST;
/*      */ import org.omg.CORBA.OBJ_ADAPTER;
/*      */ import org.omg.CORBA.TRANSIENT;
/*      */ import org.omg.CORBA.UNKNOWN;
/*      */ 
/*      */ public class POASystemException extends LogWrapperBase
/*      */ {
/*   40 */   private static LogWrapperFactory factory = new LogWrapperFactory()
/*      */   {
/*      */     public LogWrapperBase create(Logger paramAnonymousLogger) {
/*   43 */       return new POASystemException(paramAnonymousLogger);
/*      */     }
/*   40 */   };
/*      */   public static final int SERVANT_MANAGER_ALREADY_SET = 1398080489;
/*      */   public static final int DESTROY_DEADLOCK = 1398080490;
/*      */   public static final int SERVANT_ORB = 1398080489;
/*      */   public static final int BAD_SERVANT = 1398080490;
/*      */   public static final int ILLEGAL_FORWARD_REQUEST = 1398080491;
/*      */   public static final int BAD_TRANSACTION_CONTEXT = 1398080489;
/*      */   public static final int BAD_REPOSITORY_ID = 1398080490;
/*      */   public static final int INVOKESETUP = 1398080489;
/*      */   public static final int BAD_LOCALREPLYSTATUS = 1398080490;
/*      */   public static final int PERSISTENT_SERVERPORT_ERROR = 1398080491;
/*      */   public static final int SERVANT_DISPATCH = 1398080492;
/*      */   public static final int WRONG_CLIENTSC = 1398080493;
/*      */   public static final int CANT_CLONE_TEMPLATE = 1398080494;
/*      */   public static final int POACURRENT_UNBALANCED_STACK = 1398080495;
/*      */   public static final int POACURRENT_NULL_FIELD = 1398080496;
/*      */   public static final int POA_INTERNAL_GET_SERVANT_ERROR = 1398080497;
/*      */   public static final int MAKE_FACTORY_NOT_POA = 1398080498;
/*      */   public static final int DUPLICATE_ORB_VERSION_SC = 1398080499;
/*      */   public static final int PREINVOKE_CLONE_ERROR = 1398080500;
/*      */   public static final int PREINVOKE_POA_DESTROYED = 1398080501;
/*      */   public static final int PMF_CREATE_RETAIN = 1398080502;
/*      */   public static final int PMF_CREATE_NON_RETAIN = 1398080503;
/*      */   public static final int POLICY_MEDIATOR_BAD_POLICY_IN_FACTORY = 1398080504;
/*      */   public static final int SERVANT_TO_ID_OAA = 1398080505;
/*      */   public static final int SERVANT_TO_ID_SAA = 1398080506;
/*      */   public static final int SERVANT_TO_ID_WP = 1398080507;
/*      */   public static final int CANT_RESOLVE_ROOT_POA = 1398080508;
/*      */   public static final int SERVANT_MUST_BE_LOCAL = 1398080509;
/*      */   public static final int NO_PROFILES_IN_IOR = 1398080510;
/*      */   public static final int AOM_ENTRY_DEC_ZERO = 1398080511;
/*      */   public static final int ADD_POA_INACTIVE = 1398080512;
/*      */   public static final int ILLEGAL_POA_STATE_TRANS = 1398080513;
/*      */   public static final int UNEXPECTED_EXCEPTION = 1398080514;
/*      */   public static final int SINGLE_THREAD_NOT_SUPPORTED = 1398080489;
/*      */   public static final int METHOD_NOT_IMPLEMENTED = 1398080490;
/*      */   public static final int POA_LOOKUP_ERROR = 1398080489;
/*      */   public static final int POA_INACTIVE = 1398080490;
/*      */   public static final int POA_NO_SERVANT_MANAGER = 1398080491;
/*      */   public static final int POA_NO_DEFAULT_SERVANT = 1398080492;
/*      */   public static final int POA_SERVANT_NOT_UNIQUE = 1398080493;
/*      */   public static final int POA_WRONG_POLICY = 1398080494;
/*      */   public static final int FINDPOA_ERROR = 1398080495;
/*      */   public static final int POA_SERVANT_ACTIVATOR_LOOKUP_FAILED = 1398080497;
/*      */   public static final int POA_BAD_SERVANT_MANAGER = 1398080498;
/*      */   public static final int POA_SERVANT_LOCATOR_LOOKUP_FAILED = 1398080499;
/*      */   public static final int POA_UNKNOWN_POLICY = 1398080500;
/*      */   public static final int POA_NOT_FOUND = 1398080501;
/*      */   public static final int SERVANT_LOOKUP = 1398080502;
/*      */   public static final int LOCAL_SERVANT_LOOKUP = 1398080503;
/*      */   public static final int SERVANT_MANAGER_BAD_TYPE = 1398080504;
/*      */   public static final int DEFAULT_POA_NOT_POAIMPL = 1398080505;
/*      */   public static final int WRONG_POLICIES_FOR_THIS_OBJECT = 1398080506;
/*      */   public static final int THIS_OBJECT_SERVANT_NOT_ACTIVE = 1398080507;
/*      */   public static final int THIS_OBJECT_WRONG_POLICY = 1398080508;
/*      */   public static final int NO_CONTEXT = 1398080509;
/*      */   public static final int INCARNATE_RETURNED_NULL = 1398080510;
/*      */   public static final int JTS_INIT_ERROR = 1398080489;
/*      */   public static final int PERSISTENT_SERVERID_NOT_SET = 1398080490;
/*      */   public static final int PERSISTENT_SERVERPORT_NOT_SET = 1398080491;
/*      */   public static final int ORBD_ERROR = 1398080492;
/*      */   public static final int BOOTSTRAP_ERROR = 1398080493;
/*      */   public static final int POA_DISCARDING = 1398080489;
/*      */   public static final int OTSHOOKEXCEPTION = 1398080489;
/*      */   public static final int UNKNOWN_SERVER_EXCEPTION = 1398080490;
/*      */   public static final int UNKNOWN_SERVERAPP_EXCEPTION = 1398080491;
/*      */   public static final int UNKNOWN_LOCALINVOCATION_ERROR = 1398080492;
/*      */   public static final int ADAPTER_ACTIVATOR_NONEXISTENT = 1398080489;
/*      */   public static final int ADAPTER_ACTIVATOR_FAILED = 1398080490;
/*      */   public static final int BAD_SKELETON = 1398080491;
/*      */   public static final int NULL_SERVANT = 1398080492;
/*      */   public static final int ADAPTER_DESTROYED = 1398080493;
/*      */ 
/*      */   public POASystemException(Logger paramLogger)
/*      */   {
/*   37 */     super(paramLogger);
/*      */   }
/*      */ 
/*      */   public static POASystemException get(ORB paramORB, String paramString)
/*      */   {
/*   49 */     POASystemException localPOASystemException = (POASystemException)paramORB.getLogWrapper(paramString, "POA", factory);
/*      */ 
/*   52 */     return localPOASystemException;
/*      */   }
/*      */ 
/*      */   public static POASystemException get(String paramString)
/*      */   {
/*   57 */     POASystemException localPOASystemException = (POASystemException)ORB.staticGetLogWrapper(paramString, "POA", factory);
/*      */ 
/*   60 */     return localPOASystemException;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER servantManagerAlreadySet(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*   70 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398080489, paramCompletionStatus);
/*   71 */     if (paramThrowable != null) {
/*   72 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*   74 */     if (this.logger.isLoggable(Level.WARNING)) {
/*   75 */       Object[] arrayOfObject = null;
/*   76 */       doLog(Level.WARNING, "POA.servantManagerAlreadySet", arrayOfObject, POASystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*   80 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER servantManagerAlreadySet(CompletionStatus paramCompletionStatus) {
/*   84 */     return servantManagerAlreadySet(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER servantManagerAlreadySet(Throwable paramThrowable) {
/*   88 */     return servantManagerAlreadySet(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER servantManagerAlreadySet() {
/*   92 */     return servantManagerAlreadySet(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER destroyDeadlock(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*   98 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398080490, paramCompletionStatus);
/*   99 */     if (paramThrowable != null) {
/*  100 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  102 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  103 */       Object[] arrayOfObject = null;
/*  104 */       doLog(Level.WARNING, "POA.destroyDeadlock", arrayOfObject, POASystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  108 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER destroyDeadlock(CompletionStatus paramCompletionStatus) {
/*  112 */     return destroyDeadlock(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER destroyDeadlock(Throwable paramThrowable) {
/*  116 */     return destroyDeadlock(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER destroyDeadlock() {
/*  120 */     return destroyDeadlock(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION servantOrb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  130 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398080489, paramCompletionStatus);
/*  131 */     if (paramThrowable != null) {
/*  132 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  134 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  135 */       Object[] arrayOfObject = null;
/*  136 */       doLog(Level.WARNING, "POA.servantOrb", arrayOfObject, POASystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  140 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION servantOrb(CompletionStatus paramCompletionStatus) {
/*  144 */     return servantOrb(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION servantOrb(Throwable paramThrowable) {
/*  148 */     return servantOrb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION servantOrb() {
/*  152 */     return servantOrb(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badServant(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  158 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398080490, paramCompletionStatus);
/*  159 */     if (paramThrowable != null) {
/*  160 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  162 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  163 */       Object[] arrayOfObject = null;
/*  164 */       doLog(Level.WARNING, "POA.badServant", arrayOfObject, POASystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  168 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badServant(CompletionStatus paramCompletionStatus) {
/*  172 */     return badServant(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badServant(Throwable paramThrowable) {
/*  176 */     return badServant(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badServant() {
/*  180 */     return badServant(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION illegalForwardRequest(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  186 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398080491, paramCompletionStatus);
/*  187 */     if (paramThrowable != null) {
/*  188 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  190 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  191 */       Object[] arrayOfObject = null;
/*  192 */       doLog(Level.WARNING, "POA.illegalForwardRequest", arrayOfObject, POASystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  196 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION illegalForwardRequest(CompletionStatus paramCompletionStatus) {
/*  200 */     return illegalForwardRequest(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION illegalForwardRequest(Throwable paramThrowable) {
/*  204 */     return illegalForwardRequest(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION illegalForwardRequest() {
/*  208 */     return illegalForwardRequest(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badTransactionContext(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  218 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398080489, paramCompletionStatus);
/*  219 */     if (paramThrowable != null) {
/*  220 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/*  222 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  223 */       Object[] arrayOfObject = null;
/*  224 */       doLog(Level.WARNING, "POA.badTransactionContext", arrayOfObject, POASystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/*  228 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badTransactionContext(CompletionStatus paramCompletionStatus) {
/*  232 */     return badTransactionContext(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badTransactionContext(Throwable paramThrowable) {
/*  236 */     return badTransactionContext(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badTransactionContext() {
/*  240 */     return badTransactionContext(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badRepositoryId(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  246 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398080490, paramCompletionStatus);
/*  247 */     if (paramThrowable != null) {
/*  248 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/*  250 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  251 */       Object[] arrayOfObject = null;
/*  252 */       doLog(Level.WARNING, "POA.badRepositoryId", arrayOfObject, POASystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/*  256 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badRepositoryId(CompletionStatus paramCompletionStatus) {
/*  260 */     return badRepositoryId(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badRepositoryId(Throwable paramThrowable) {
/*  264 */     return badRepositoryId(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badRepositoryId() {
/*  268 */     return badRepositoryId(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invokesetup(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  278 */     INTERNAL localINTERNAL = new INTERNAL(1398080489, paramCompletionStatus);
/*  279 */     if (paramThrowable != null) {
/*  280 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  282 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  283 */       Object[] arrayOfObject = null;
/*  284 */       doLog(Level.WARNING, "POA.invokesetup", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  288 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invokesetup(CompletionStatus paramCompletionStatus) {
/*  292 */     return invokesetup(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invokesetup(Throwable paramThrowable) {
/*  296 */     return invokesetup(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL invokesetup() {
/*  300 */     return invokesetup(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badLocalreplystatus(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  306 */     INTERNAL localINTERNAL = new INTERNAL(1398080490, paramCompletionStatus);
/*  307 */     if (paramThrowable != null) {
/*  308 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  310 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  311 */       Object[] arrayOfObject = null;
/*  312 */       doLog(Level.WARNING, "POA.badLocalreplystatus", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  316 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badLocalreplystatus(CompletionStatus paramCompletionStatus) {
/*  320 */     return badLocalreplystatus(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badLocalreplystatus(Throwable paramThrowable) {
/*  324 */     return badLocalreplystatus(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badLocalreplystatus() {
/*  328 */     return badLocalreplystatus(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL persistentServerportError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  334 */     INTERNAL localINTERNAL = new INTERNAL(1398080491, paramCompletionStatus);
/*  335 */     if (paramThrowable != null) {
/*  336 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  338 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  339 */       Object[] arrayOfObject = null;
/*  340 */       doLog(Level.WARNING, "POA.persistentServerportError", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  344 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL persistentServerportError(CompletionStatus paramCompletionStatus) {
/*  348 */     return persistentServerportError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL persistentServerportError(Throwable paramThrowable) {
/*  352 */     return persistentServerportError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL persistentServerportError() {
/*  356 */     return persistentServerportError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantDispatch(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  362 */     INTERNAL localINTERNAL = new INTERNAL(1398080492, paramCompletionStatus);
/*  363 */     if (paramThrowable != null) {
/*  364 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  366 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  367 */       Object[] arrayOfObject = null;
/*  368 */       doLog(Level.WARNING, "POA.servantDispatch", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  372 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL servantDispatch(CompletionStatus paramCompletionStatus) {
/*  376 */     return servantDispatch(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantDispatch(Throwable paramThrowable) {
/*  380 */     return servantDispatch(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantDispatch() {
/*  384 */     return servantDispatch(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL wrongClientsc(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  390 */     INTERNAL localINTERNAL = new INTERNAL(1398080493, paramCompletionStatus);
/*  391 */     if (paramThrowable != null) {
/*  392 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  394 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  395 */       Object[] arrayOfObject = null;
/*  396 */       doLog(Level.WARNING, "POA.wrongClientsc", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  400 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL wrongClientsc(CompletionStatus paramCompletionStatus) {
/*  404 */     return wrongClientsc(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL wrongClientsc(Throwable paramThrowable) {
/*  408 */     return wrongClientsc(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL wrongClientsc() {
/*  412 */     return wrongClientsc(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL cantCloneTemplate(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  418 */     INTERNAL localINTERNAL = new INTERNAL(1398080494, paramCompletionStatus);
/*  419 */     if (paramThrowable != null) {
/*  420 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  422 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  423 */       Object[] arrayOfObject = null;
/*  424 */       doLog(Level.WARNING, "POA.cantCloneTemplate", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  428 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL cantCloneTemplate(CompletionStatus paramCompletionStatus) {
/*  432 */     return cantCloneTemplate(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL cantCloneTemplate(Throwable paramThrowable) {
/*  436 */     return cantCloneTemplate(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL cantCloneTemplate() {
/*  440 */     return cantCloneTemplate(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL poacurrentUnbalancedStack(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  446 */     INTERNAL localINTERNAL = new INTERNAL(1398080495, paramCompletionStatus);
/*  447 */     if (paramThrowable != null) {
/*  448 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  450 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  451 */       Object[] arrayOfObject = null;
/*  452 */       doLog(Level.WARNING, "POA.poacurrentUnbalancedStack", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  456 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL poacurrentUnbalancedStack(CompletionStatus paramCompletionStatus) {
/*  460 */     return poacurrentUnbalancedStack(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL poacurrentUnbalancedStack(Throwable paramThrowable) {
/*  464 */     return poacurrentUnbalancedStack(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL poacurrentUnbalancedStack() {
/*  468 */     return poacurrentUnbalancedStack(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL poacurrentNullField(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  474 */     INTERNAL localINTERNAL = new INTERNAL(1398080496, paramCompletionStatus);
/*  475 */     if (paramThrowable != null) {
/*  476 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  478 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  479 */       Object[] arrayOfObject = null;
/*  480 */       doLog(Level.WARNING, "POA.poacurrentNullField", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  484 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL poacurrentNullField(CompletionStatus paramCompletionStatus) {
/*  488 */     return poacurrentNullField(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL poacurrentNullField(Throwable paramThrowable) {
/*  492 */     return poacurrentNullField(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL poacurrentNullField() {
/*  496 */     return poacurrentNullField(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL poaInternalGetServantError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  502 */     INTERNAL localINTERNAL = new INTERNAL(1398080497, paramCompletionStatus);
/*  503 */     if (paramThrowable != null) {
/*  504 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  506 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  507 */       Object[] arrayOfObject = null;
/*  508 */       doLog(Level.WARNING, "POA.poaInternalGetServantError", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  512 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL poaInternalGetServantError(CompletionStatus paramCompletionStatus) {
/*  516 */     return poaInternalGetServantError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL poaInternalGetServantError(Throwable paramThrowable) {
/*  520 */     return poaInternalGetServantError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL poaInternalGetServantError() {
/*  524 */     return poaInternalGetServantError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL makeFactoryNotPoa(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/*  530 */     INTERNAL localINTERNAL = new INTERNAL(1398080498, paramCompletionStatus);
/*  531 */     if (paramThrowable != null) {
/*  532 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  534 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  535 */       Object[] arrayOfObject = new Object[1];
/*  536 */       arrayOfObject[0] = paramObject;
/*  537 */       doLog(Level.WARNING, "POA.makeFactoryNotPoa", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  541 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL makeFactoryNotPoa(CompletionStatus paramCompletionStatus, Object paramObject) {
/*  545 */     return makeFactoryNotPoa(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL makeFactoryNotPoa(Throwable paramThrowable, Object paramObject) {
/*  549 */     return makeFactoryNotPoa(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL makeFactoryNotPoa(Object paramObject) {
/*  553 */     return makeFactoryNotPoa(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateOrbVersionSc(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  559 */     INTERNAL localINTERNAL = new INTERNAL(1398080499, paramCompletionStatus);
/*  560 */     if (paramThrowable != null) {
/*  561 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  563 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  564 */       Object[] arrayOfObject = null;
/*  565 */       doLog(Level.WARNING, "POA.duplicateOrbVersionSc", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  569 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateOrbVersionSc(CompletionStatus paramCompletionStatus) {
/*  573 */     return duplicateOrbVersionSc(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateOrbVersionSc(Throwable paramThrowable) {
/*  577 */     return duplicateOrbVersionSc(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateOrbVersionSc() {
/*  581 */     return duplicateOrbVersionSc(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL preinvokeCloneError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  587 */     INTERNAL localINTERNAL = new INTERNAL(1398080500, paramCompletionStatus);
/*  588 */     if (paramThrowable != null) {
/*  589 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  591 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  592 */       Object[] arrayOfObject = null;
/*  593 */       doLog(Level.WARNING, "POA.preinvokeCloneError", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  597 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL preinvokeCloneError(CompletionStatus paramCompletionStatus) {
/*  601 */     return preinvokeCloneError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL preinvokeCloneError(Throwable paramThrowable) {
/*  605 */     return preinvokeCloneError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL preinvokeCloneError() {
/*  609 */     return preinvokeCloneError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL preinvokePoaDestroyed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  615 */     INTERNAL localINTERNAL = new INTERNAL(1398080501, paramCompletionStatus);
/*  616 */     if (paramThrowable != null) {
/*  617 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  619 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  620 */       Object[] arrayOfObject = null;
/*  621 */       doLog(Level.WARNING, "POA.preinvokePoaDestroyed", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  625 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL preinvokePoaDestroyed(CompletionStatus paramCompletionStatus) {
/*  629 */     return preinvokePoaDestroyed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL preinvokePoaDestroyed(Throwable paramThrowable) {
/*  633 */     return preinvokePoaDestroyed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL preinvokePoaDestroyed() {
/*  637 */     return preinvokePoaDestroyed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL pmfCreateRetain(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  643 */     INTERNAL localINTERNAL = new INTERNAL(1398080502, paramCompletionStatus);
/*  644 */     if (paramThrowable != null) {
/*  645 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  647 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  648 */       Object[] arrayOfObject = null;
/*  649 */       doLog(Level.WARNING, "POA.pmfCreateRetain", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  653 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL pmfCreateRetain(CompletionStatus paramCompletionStatus) {
/*  657 */     return pmfCreateRetain(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL pmfCreateRetain(Throwable paramThrowable) {
/*  661 */     return pmfCreateRetain(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL pmfCreateRetain() {
/*  665 */     return pmfCreateRetain(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL pmfCreateNonRetain(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  671 */     INTERNAL localINTERNAL = new INTERNAL(1398080503, paramCompletionStatus);
/*  672 */     if (paramThrowable != null) {
/*  673 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  675 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  676 */       Object[] arrayOfObject = null;
/*  677 */       doLog(Level.WARNING, "POA.pmfCreateNonRetain", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  681 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL pmfCreateNonRetain(CompletionStatus paramCompletionStatus) {
/*  685 */     return pmfCreateNonRetain(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL pmfCreateNonRetain(Throwable paramThrowable) {
/*  689 */     return pmfCreateNonRetain(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL pmfCreateNonRetain() {
/*  693 */     return pmfCreateNonRetain(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL policyMediatorBadPolicyInFactory(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  699 */     INTERNAL localINTERNAL = new INTERNAL(1398080504, paramCompletionStatus);
/*  700 */     if (paramThrowable != null) {
/*  701 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  703 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  704 */       Object[] arrayOfObject = null;
/*  705 */       doLog(Level.WARNING, "POA.policyMediatorBadPolicyInFactory", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  709 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL policyMediatorBadPolicyInFactory(CompletionStatus paramCompletionStatus) {
/*  713 */     return policyMediatorBadPolicyInFactory(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL policyMediatorBadPolicyInFactory(Throwable paramThrowable) {
/*  717 */     return policyMediatorBadPolicyInFactory(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL policyMediatorBadPolicyInFactory() {
/*  721 */     return policyMediatorBadPolicyInFactory(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdOaa(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  727 */     INTERNAL localINTERNAL = new INTERNAL(1398080505, paramCompletionStatus);
/*  728 */     if (paramThrowable != null) {
/*  729 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  731 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  732 */       Object[] arrayOfObject = null;
/*  733 */       doLog(Level.WARNING, "POA.servantToIdOaa", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  737 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdOaa(CompletionStatus paramCompletionStatus) {
/*  741 */     return servantToIdOaa(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdOaa(Throwable paramThrowable) {
/*  745 */     return servantToIdOaa(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdOaa() {
/*  749 */     return servantToIdOaa(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdSaa(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  755 */     INTERNAL localINTERNAL = new INTERNAL(1398080506, paramCompletionStatus);
/*  756 */     if (paramThrowable != null) {
/*  757 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  759 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  760 */       Object[] arrayOfObject = null;
/*  761 */       doLog(Level.WARNING, "POA.servantToIdSaa", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  765 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdSaa(CompletionStatus paramCompletionStatus) {
/*  769 */     return servantToIdSaa(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdSaa(Throwable paramThrowable) {
/*  773 */     return servantToIdSaa(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdSaa() {
/*  777 */     return servantToIdSaa(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdWp(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  783 */     INTERNAL localINTERNAL = new INTERNAL(1398080507, paramCompletionStatus);
/*  784 */     if (paramThrowable != null) {
/*  785 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  787 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  788 */       Object[] arrayOfObject = null;
/*  789 */       doLog(Level.WARNING, "POA.servantToIdWp", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  793 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdWp(CompletionStatus paramCompletionStatus) {
/*  797 */     return servantToIdWp(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdWp(Throwable paramThrowable) {
/*  801 */     return servantToIdWp(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantToIdWp() {
/*  805 */     return servantToIdWp(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL cantResolveRootPoa(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  811 */     INTERNAL localINTERNAL = new INTERNAL(1398080508, paramCompletionStatus);
/*  812 */     if (paramThrowable != null) {
/*  813 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  815 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  816 */       Object[] arrayOfObject = null;
/*  817 */       doLog(Level.WARNING, "POA.cantResolveRootPoa", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  821 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL cantResolveRootPoa(CompletionStatus paramCompletionStatus) {
/*  825 */     return cantResolveRootPoa(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL cantResolveRootPoa(Throwable paramThrowable) {
/*  829 */     return cantResolveRootPoa(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL cantResolveRootPoa() {
/*  833 */     return cantResolveRootPoa(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantMustBeLocal(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  839 */     INTERNAL localINTERNAL = new INTERNAL(1398080509, paramCompletionStatus);
/*  840 */     if (paramThrowable != null) {
/*  841 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  843 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  844 */       Object[] arrayOfObject = null;
/*  845 */       doLog(Level.WARNING, "POA.servantMustBeLocal", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  849 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL servantMustBeLocal(CompletionStatus paramCompletionStatus) {
/*  853 */     return servantMustBeLocal(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantMustBeLocal(Throwable paramThrowable) {
/*  857 */     return servantMustBeLocal(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL servantMustBeLocal() {
/*  861 */     return servantMustBeLocal(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noProfilesInIor(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  867 */     INTERNAL localINTERNAL = new INTERNAL(1398080510, paramCompletionStatus);
/*  868 */     if (paramThrowable != null) {
/*  869 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  871 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  872 */       Object[] arrayOfObject = null;
/*  873 */       doLog(Level.WARNING, "POA.noProfilesInIor", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  877 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL noProfilesInIor(CompletionStatus paramCompletionStatus) {
/*  881 */     return noProfilesInIor(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noProfilesInIor(Throwable paramThrowable) {
/*  885 */     return noProfilesInIor(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL noProfilesInIor() {
/*  889 */     return noProfilesInIor(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL aomEntryDecZero(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  895 */     INTERNAL localINTERNAL = new INTERNAL(1398080511, paramCompletionStatus);
/*  896 */     if (paramThrowable != null) {
/*  897 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  899 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  900 */       Object[] arrayOfObject = null;
/*  901 */       doLog(Level.WARNING, "POA.aomEntryDecZero", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  905 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL aomEntryDecZero(CompletionStatus paramCompletionStatus) {
/*  909 */     return aomEntryDecZero(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL aomEntryDecZero(Throwable paramThrowable) {
/*  913 */     return aomEntryDecZero(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL aomEntryDecZero() {
/*  917 */     return aomEntryDecZero(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL addPoaInactive(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  923 */     INTERNAL localINTERNAL = new INTERNAL(1398080512, paramCompletionStatus);
/*  924 */     if (paramThrowable != null) {
/*  925 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  927 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  928 */       Object[] arrayOfObject = null;
/*  929 */       doLog(Level.WARNING, "POA.addPoaInactive", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  933 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL addPoaInactive(CompletionStatus paramCompletionStatus) {
/*  937 */     return addPoaInactive(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL addPoaInactive(Throwable paramThrowable) {
/*  941 */     return addPoaInactive(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL addPoaInactive() {
/*  945 */     return addPoaInactive(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalPoaStateTrans(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  951 */     INTERNAL localINTERNAL = new INTERNAL(1398080513, paramCompletionStatus);
/*  952 */     if (paramThrowable != null) {
/*  953 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  955 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  956 */       Object[] arrayOfObject = null;
/*  957 */       doLog(Level.WARNING, "POA.illegalPoaStateTrans", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  961 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalPoaStateTrans(CompletionStatus paramCompletionStatus) {
/*  965 */     return illegalPoaStateTrans(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalPoaStateTrans(Throwable paramThrowable) {
/*  969 */     return illegalPoaStateTrans(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalPoaStateTrans() {
/*  973 */     return illegalPoaStateTrans(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedException(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/*  979 */     INTERNAL localINTERNAL = new INTERNAL(1398080514, paramCompletionStatus);
/*  980 */     if (paramThrowable != null) {
/*  981 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/*  983 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  984 */       Object[] arrayOfObject = new Object[1];
/*  985 */       arrayOfObject[0] = paramObject;
/*  986 */       doLog(Level.WARNING, "POA.unexpectedException", arrayOfObject, POASystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/*  990 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedException(CompletionStatus paramCompletionStatus, Object paramObject) {
/*  994 */     return unexpectedException(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedException(Throwable paramThrowable, Object paramObject) {
/*  998 */     return unexpectedException(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedException(Object paramObject) {
/* 1002 */     return unexpectedException(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT singleThreadNotSupported(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1012 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1398080489, paramCompletionStatus);
/* 1013 */     if (paramThrowable != null) {
/* 1014 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 1016 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1017 */       Object[] arrayOfObject = null;
/* 1018 */       doLog(Level.WARNING, "POA.singleThreadNotSupported", arrayOfObject, POASystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 1022 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT singleThreadNotSupported(CompletionStatus paramCompletionStatus) {
/* 1026 */     return singleThreadNotSupported(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT singleThreadNotSupported(Throwable paramThrowable) {
/* 1030 */     return singleThreadNotSupported(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT singleThreadNotSupported() {
/* 1034 */     return singleThreadNotSupported(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT methodNotImplemented(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1040 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1398080490, paramCompletionStatus);
/* 1041 */     if (paramThrowable != null) {
/* 1042 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 1044 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1045 */       Object[] arrayOfObject = null;
/* 1046 */       doLog(Level.WARNING, "POA.methodNotImplemented", arrayOfObject, POASystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 1050 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT methodNotImplemented(CompletionStatus paramCompletionStatus) {
/* 1054 */     return methodNotImplemented(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT methodNotImplemented(Throwable paramThrowable) {
/* 1058 */     return methodNotImplemented(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT methodNotImplemented() {
/* 1062 */     return methodNotImplemented(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaLookupError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1072 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080489, paramCompletionStatus);
/* 1073 */     if (paramThrowable != null) {
/* 1074 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1076 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1077 */       Object[] arrayOfObject = null;
/* 1078 */       doLog(Level.WARNING, "POA.poaLookupError", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1082 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaLookupError(CompletionStatus paramCompletionStatus) {
/* 1086 */     return poaLookupError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaLookupError(Throwable paramThrowable) {
/* 1090 */     return poaLookupError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaLookupError() {
/* 1094 */     return poaLookupError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaInactive(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1100 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080490, paramCompletionStatus);
/* 1101 */     if (paramThrowable != null) {
/* 1102 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1104 */     if (this.logger.isLoggable(Level.FINE)) {
/* 1105 */       Object[] arrayOfObject = null;
/* 1106 */       doLog(Level.FINE, "POA.poaInactive", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1110 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaInactive(CompletionStatus paramCompletionStatus) {
/* 1114 */     return poaInactive(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaInactive(Throwable paramThrowable) {
/* 1118 */     return poaInactive(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaInactive() {
/* 1122 */     return poaInactive(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNoServantManager(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1128 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080491, paramCompletionStatus);
/* 1129 */     if (paramThrowable != null) {
/* 1130 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1132 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1133 */       Object[] arrayOfObject = null;
/* 1134 */       doLog(Level.WARNING, "POA.poaNoServantManager", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1138 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNoServantManager(CompletionStatus paramCompletionStatus) {
/* 1142 */     return poaNoServantManager(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNoServantManager(Throwable paramThrowable) {
/* 1146 */     return poaNoServantManager(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNoServantManager() {
/* 1150 */     return poaNoServantManager(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNoDefaultServant(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1156 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080492, paramCompletionStatus);
/* 1157 */     if (paramThrowable != null) {
/* 1158 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1160 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1161 */       Object[] arrayOfObject = null;
/* 1162 */       doLog(Level.WARNING, "POA.poaNoDefaultServant", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1166 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNoDefaultServant(CompletionStatus paramCompletionStatus) {
/* 1170 */     return poaNoDefaultServant(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNoDefaultServant(Throwable paramThrowable) {
/* 1174 */     return poaNoDefaultServant(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNoDefaultServant() {
/* 1178 */     return poaNoDefaultServant(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantNotUnique(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1184 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080493, paramCompletionStatus);
/* 1185 */     if (paramThrowable != null) {
/* 1186 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1188 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1189 */       Object[] arrayOfObject = null;
/* 1190 */       doLog(Level.WARNING, "POA.poaServantNotUnique", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1194 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantNotUnique(CompletionStatus paramCompletionStatus) {
/* 1198 */     return poaServantNotUnique(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantNotUnique(Throwable paramThrowable) {
/* 1202 */     return poaServantNotUnique(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantNotUnique() {
/* 1206 */     return poaServantNotUnique(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaWrongPolicy(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1212 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080494, paramCompletionStatus);
/* 1213 */     if (paramThrowable != null) {
/* 1214 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1216 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1217 */       Object[] arrayOfObject = null;
/* 1218 */       doLog(Level.WARNING, "POA.poaWrongPolicy", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1222 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaWrongPolicy(CompletionStatus paramCompletionStatus) {
/* 1226 */     return poaWrongPolicy(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaWrongPolicy(Throwable paramThrowable) {
/* 1230 */     return poaWrongPolicy(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaWrongPolicy() {
/* 1234 */     return poaWrongPolicy(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER findpoaError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1240 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080495, paramCompletionStatus);
/* 1241 */     if (paramThrowable != null) {
/* 1242 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1244 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1245 */       Object[] arrayOfObject = null;
/* 1246 */       doLog(Level.WARNING, "POA.findpoaError", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1250 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER findpoaError(CompletionStatus paramCompletionStatus) {
/* 1254 */     return findpoaError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER findpoaError(Throwable paramThrowable) {
/* 1258 */     return findpoaError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER findpoaError() {
/* 1262 */     return findpoaError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantActivatorLookupFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1268 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080497, paramCompletionStatus);
/* 1269 */     if (paramThrowable != null) {
/* 1270 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1272 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1273 */       Object[] arrayOfObject = null;
/* 1274 */       doLog(Level.WARNING, "POA.poaServantActivatorLookupFailed", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1278 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantActivatorLookupFailed(CompletionStatus paramCompletionStatus) {
/* 1282 */     return poaServantActivatorLookupFailed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantActivatorLookupFailed(Throwable paramThrowable) {
/* 1286 */     return poaServantActivatorLookupFailed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantActivatorLookupFailed() {
/* 1290 */     return poaServantActivatorLookupFailed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaBadServantManager(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1296 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080498, paramCompletionStatus);
/* 1297 */     if (paramThrowable != null) {
/* 1298 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1300 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1301 */       Object[] arrayOfObject = null;
/* 1302 */       doLog(Level.WARNING, "POA.poaBadServantManager", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1306 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaBadServantManager(CompletionStatus paramCompletionStatus) {
/* 1310 */     return poaBadServantManager(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaBadServantManager(Throwable paramThrowable) {
/* 1314 */     return poaBadServantManager(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaBadServantManager() {
/* 1318 */     return poaBadServantManager(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantLocatorLookupFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1324 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080499, paramCompletionStatus);
/* 1325 */     if (paramThrowable != null) {
/* 1326 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1328 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1329 */       Object[] arrayOfObject = null;
/* 1330 */       doLog(Level.WARNING, "POA.poaServantLocatorLookupFailed", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1334 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantLocatorLookupFailed(CompletionStatus paramCompletionStatus) {
/* 1338 */     return poaServantLocatorLookupFailed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantLocatorLookupFailed(Throwable paramThrowable) {
/* 1342 */     return poaServantLocatorLookupFailed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaServantLocatorLookupFailed() {
/* 1346 */     return poaServantLocatorLookupFailed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaUnknownPolicy(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1352 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080500, paramCompletionStatus);
/* 1353 */     if (paramThrowable != null) {
/* 1354 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1356 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1357 */       Object[] arrayOfObject = null;
/* 1358 */       doLog(Level.WARNING, "POA.poaUnknownPolicy", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1362 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaUnknownPolicy(CompletionStatus paramCompletionStatus) {
/* 1366 */     return poaUnknownPolicy(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaUnknownPolicy(Throwable paramThrowable) {
/* 1370 */     return poaUnknownPolicy(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaUnknownPolicy() {
/* 1374 */     return poaUnknownPolicy(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNotFound(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1380 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080501, paramCompletionStatus);
/* 1381 */     if (paramThrowable != null) {
/* 1382 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1384 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1385 */       Object[] arrayOfObject = null;
/* 1386 */       doLog(Level.WARNING, "POA.poaNotFound", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1390 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNotFound(CompletionStatus paramCompletionStatus) {
/* 1394 */     return poaNotFound(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNotFound(Throwable paramThrowable) {
/* 1398 */     return poaNotFound(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER poaNotFound() {
/* 1402 */     return poaNotFound(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER servantLookup(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1408 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080502, paramCompletionStatus);
/* 1409 */     if (paramThrowable != null) {
/* 1410 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1412 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1413 */       Object[] arrayOfObject = null;
/* 1414 */       doLog(Level.WARNING, "POA.servantLookup", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1418 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER servantLookup(CompletionStatus paramCompletionStatus) {
/* 1422 */     return servantLookup(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER servantLookup(Throwable paramThrowable) {
/* 1426 */     return servantLookup(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER servantLookup() {
/* 1430 */     return servantLookup(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER localServantLookup(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1436 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080503, paramCompletionStatus);
/* 1437 */     if (paramThrowable != null) {
/* 1438 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1440 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1441 */       Object[] arrayOfObject = null;
/* 1442 */       doLog(Level.WARNING, "POA.localServantLookup", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1446 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER localServantLookup(CompletionStatus paramCompletionStatus) {
/* 1450 */     return localServantLookup(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER localServantLookup(Throwable paramThrowable) {
/* 1454 */     return localServantLookup(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER localServantLookup() {
/* 1458 */     return localServantLookup(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER servantManagerBadType(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1464 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080504, paramCompletionStatus);
/* 1465 */     if (paramThrowable != null) {
/* 1466 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1468 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1469 */       Object[] arrayOfObject = null;
/* 1470 */       doLog(Level.WARNING, "POA.servantManagerBadType", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1474 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER servantManagerBadType(CompletionStatus paramCompletionStatus) {
/* 1478 */     return servantManagerBadType(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER servantManagerBadType(Throwable paramThrowable) {
/* 1482 */     return servantManagerBadType(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER servantManagerBadType() {
/* 1486 */     return servantManagerBadType(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER defaultPoaNotPoaimpl(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1492 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080505, paramCompletionStatus);
/* 1493 */     if (paramThrowable != null) {
/* 1494 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1496 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1497 */       Object[] arrayOfObject = null;
/* 1498 */       doLog(Level.WARNING, "POA.defaultPoaNotPoaimpl", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1502 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER defaultPoaNotPoaimpl(CompletionStatus paramCompletionStatus) {
/* 1506 */     return defaultPoaNotPoaimpl(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER defaultPoaNotPoaimpl(Throwable paramThrowable) {
/* 1510 */     return defaultPoaNotPoaimpl(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER defaultPoaNotPoaimpl() {
/* 1514 */     return defaultPoaNotPoaimpl(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER wrongPoliciesForThisObject(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1520 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080506, paramCompletionStatus);
/* 1521 */     if (paramThrowable != null) {
/* 1522 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1524 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1525 */       Object[] arrayOfObject = null;
/* 1526 */       doLog(Level.WARNING, "POA.wrongPoliciesForThisObject", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1530 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER wrongPoliciesForThisObject(CompletionStatus paramCompletionStatus) {
/* 1534 */     return wrongPoliciesForThisObject(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER wrongPoliciesForThisObject(Throwable paramThrowable) {
/* 1538 */     return wrongPoliciesForThisObject(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER wrongPoliciesForThisObject() {
/* 1542 */     return wrongPoliciesForThisObject(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER thisObjectServantNotActive(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1548 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080507, paramCompletionStatus);
/* 1549 */     if (paramThrowable != null) {
/* 1550 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1552 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1553 */       Object[] arrayOfObject = null;
/* 1554 */       doLog(Level.WARNING, "POA.thisObjectServantNotActive", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1558 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER thisObjectServantNotActive(CompletionStatus paramCompletionStatus) {
/* 1562 */     return thisObjectServantNotActive(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER thisObjectServantNotActive(Throwable paramThrowable) {
/* 1566 */     return thisObjectServantNotActive(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER thisObjectServantNotActive() {
/* 1570 */     return thisObjectServantNotActive(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER thisObjectWrongPolicy(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1576 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080508, paramCompletionStatus);
/* 1577 */     if (paramThrowable != null) {
/* 1578 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1580 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1581 */       Object[] arrayOfObject = null;
/* 1582 */       doLog(Level.WARNING, "POA.thisObjectWrongPolicy", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1586 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER thisObjectWrongPolicy(CompletionStatus paramCompletionStatus) {
/* 1590 */     return thisObjectWrongPolicy(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER thisObjectWrongPolicy(Throwable paramThrowable) {
/* 1594 */     return thisObjectWrongPolicy(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER thisObjectWrongPolicy() {
/* 1598 */     return thisObjectWrongPolicy(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noContext(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1604 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080509, paramCompletionStatus);
/* 1605 */     if (paramThrowable != null) {
/* 1606 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1608 */     if (this.logger.isLoggable(Level.FINE)) {
/* 1609 */       Object[] arrayOfObject = null;
/* 1610 */       doLog(Level.FINE, "POA.noContext", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1614 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noContext(CompletionStatus paramCompletionStatus) {
/* 1618 */     return noContext(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noContext(Throwable paramThrowable) {
/* 1622 */     return noContext(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noContext() {
/* 1626 */     return noContext(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER incarnateReturnedNull(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1632 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398080510, paramCompletionStatus);
/* 1633 */     if (paramThrowable != null) {
/* 1634 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 1636 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1637 */       Object[] arrayOfObject = null;
/* 1638 */       doLog(Level.WARNING, "POA.incarnateReturnedNull", arrayOfObject, POASystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 1642 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER incarnateReturnedNull(CompletionStatus paramCompletionStatus) {
/* 1646 */     return incarnateReturnedNull(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER incarnateReturnedNull(Throwable paramThrowable) {
/* 1650 */     return incarnateReturnedNull(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER incarnateReturnedNull() {
/* 1654 */     return incarnateReturnedNull(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE jtsInitError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1664 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398080489, paramCompletionStatus);
/* 1665 */     if (paramThrowable != null) {
/* 1666 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 1668 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1669 */       Object[] arrayOfObject = null;
/* 1670 */       doLog(Level.WARNING, "POA.jtsInitError", arrayOfObject, POASystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 1674 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE jtsInitError(CompletionStatus paramCompletionStatus) {
/* 1678 */     return jtsInitError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE jtsInitError(Throwable paramThrowable) {
/* 1682 */     return jtsInitError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE jtsInitError() {
/* 1686 */     return jtsInitError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServeridNotSet(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1692 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398080490, paramCompletionStatus);
/* 1693 */     if (paramThrowable != null) {
/* 1694 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 1696 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1697 */       Object[] arrayOfObject = null;
/* 1698 */       doLog(Level.WARNING, "POA.persistentServeridNotSet", arrayOfObject, POASystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 1702 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServeridNotSet(CompletionStatus paramCompletionStatus) {
/* 1706 */     return persistentServeridNotSet(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServeridNotSet(Throwable paramThrowable) {
/* 1710 */     return persistentServeridNotSet(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServeridNotSet() {
/* 1714 */     return persistentServeridNotSet(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServerportNotSet(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1720 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398080491, paramCompletionStatus);
/* 1721 */     if (paramThrowable != null) {
/* 1722 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 1724 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1725 */       Object[] arrayOfObject = null;
/* 1726 */       doLog(Level.WARNING, "POA.persistentServerportNotSet", arrayOfObject, POASystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 1730 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServerportNotSet(CompletionStatus paramCompletionStatus) {
/* 1734 */     return persistentServerportNotSet(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServerportNotSet(Throwable paramThrowable) {
/* 1738 */     return persistentServerportNotSet(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServerportNotSet() {
/* 1742 */     return persistentServerportNotSet(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE orbdError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1748 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398080492, paramCompletionStatus);
/* 1749 */     if (paramThrowable != null) {
/* 1750 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 1752 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1753 */       Object[] arrayOfObject = null;
/* 1754 */       doLog(Level.WARNING, "POA.orbdError", arrayOfObject, POASystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 1758 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE orbdError(CompletionStatus paramCompletionStatus) {
/* 1762 */     return orbdError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE orbdError(Throwable paramThrowable) {
/* 1766 */     return orbdError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE orbdError() {
/* 1770 */     return orbdError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE bootstrapError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1776 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398080493, paramCompletionStatus);
/* 1777 */     if (paramThrowable != null) {
/* 1778 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 1780 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1781 */       Object[] arrayOfObject = null;
/* 1782 */       doLog(Level.WARNING, "POA.bootstrapError", arrayOfObject, POASystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 1786 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE bootstrapError(CompletionStatus paramCompletionStatus) {
/* 1790 */     return bootstrapError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE bootstrapError(Throwable paramThrowable) {
/* 1794 */     return bootstrapError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE bootstrapError() {
/* 1798 */     return bootstrapError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaDiscarding(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1808 */     TRANSIENT localTRANSIENT = new TRANSIENT(1398080489, paramCompletionStatus);
/* 1809 */     if (paramThrowable != null) {
/* 1810 */       localTRANSIENT.initCause(paramThrowable);
/*      */     }
/* 1812 */     if (this.logger.isLoggable(Level.FINE)) {
/* 1813 */       Object[] arrayOfObject = null;
/* 1814 */       doLog(Level.FINE, "POA.poaDiscarding", arrayOfObject, POASystemException.class, localTRANSIENT);
/*      */     }
/*      */ 
/* 1818 */     return localTRANSIENT;
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaDiscarding(CompletionStatus paramCompletionStatus) {
/* 1822 */     return poaDiscarding(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaDiscarding(Throwable paramThrowable) {
/* 1826 */     return poaDiscarding(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaDiscarding() {
/* 1830 */     return poaDiscarding(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN otshookexception(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1840 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398080489, paramCompletionStatus);
/* 1841 */     if (paramThrowable != null) {
/* 1842 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 1844 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1845 */       Object[] arrayOfObject = null;
/* 1846 */       doLog(Level.WARNING, "POA.otshookexception", arrayOfObject, POASystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 1850 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN otshookexception(CompletionStatus paramCompletionStatus) {
/* 1854 */     return otshookexception(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN otshookexception(Throwable paramThrowable) {
/* 1858 */     return otshookexception(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN otshookexception() {
/* 1862 */     return otshookexception(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1868 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398080490, paramCompletionStatus);
/* 1869 */     if (paramThrowable != null) {
/* 1870 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 1872 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1873 */       Object[] arrayOfObject = null;
/* 1874 */       doLog(Level.WARNING, "POA.unknownServerException", arrayOfObject, POASystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 1878 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerException(CompletionStatus paramCompletionStatus) {
/* 1882 */     return unknownServerException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerException(Throwable paramThrowable) {
/* 1886 */     return unknownServerException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerException() {
/* 1890 */     return unknownServerException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerappException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1896 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398080491, paramCompletionStatus);
/* 1897 */     if (paramThrowable != null) {
/* 1898 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 1900 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1901 */       Object[] arrayOfObject = null;
/* 1902 */       doLog(Level.WARNING, "POA.unknownServerappException", arrayOfObject, POASystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 1906 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerappException(CompletionStatus paramCompletionStatus) {
/* 1910 */     return unknownServerappException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerappException(Throwable paramThrowable) {
/* 1914 */     return unknownServerappException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerappException() {
/* 1918 */     return unknownServerappException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownLocalinvocationError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1924 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398080492, paramCompletionStatus);
/* 1925 */     if (paramThrowable != null) {
/* 1926 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 1928 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1929 */       Object[] arrayOfObject = null;
/* 1930 */       doLog(Level.WARNING, "POA.unknownLocalinvocationError", arrayOfObject, POASystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 1934 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownLocalinvocationError(CompletionStatus paramCompletionStatus) {
/* 1938 */     return unknownLocalinvocationError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownLocalinvocationError(Throwable paramThrowable) {
/* 1942 */     return unknownLocalinvocationError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownLocalinvocationError() {
/* 1946 */     return unknownLocalinvocationError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterActivatorNonexistent(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1956 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398080489, paramCompletionStatus);
/* 1957 */     if (paramThrowable != null) {
/* 1958 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 1960 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1961 */       Object[] arrayOfObject = null;
/* 1962 */       doLog(Level.WARNING, "POA.adapterActivatorNonexistent", arrayOfObject, POASystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 1966 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterActivatorNonexistent(CompletionStatus paramCompletionStatus) {
/* 1970 */     return adapterActivatorNonexistent(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterActivatorNonexistent(Throwable paramThrowable) {
/* 1974 */     return adapterActivatorNonexistent(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterActivatorNonexistent() {
/* 1978 */     return adapterActivatorNonexistent(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterActivatorFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1984 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398080490, paramCompletionStatus);
/* 1985 */     if (paramThrowable != null) {
/* 1986 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 1988 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1989 */       Object[] arrayOfObject = null;
/* 1990 */       doLog(Level.WARNING, "POA.adapterActivatorFailed", arrayOfObject, POASystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 1994 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterActivatorFailed(CompletionStatus paramCompletionStatus) {
/* 1998 */     return adapterActivatorFailed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterActivatorFailed(Throwable paramThrowable) {
/* 2002 */     return adapterActivatorFailed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterActivatorFailed() {
/* 2006 */     return adapterActivatorFailed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badSkeleton(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2012 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398080491, paramCompletionStatus);
/* 2013 */     if (paramThrowable != null) {
/* 2014 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 2016 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2017 */       Object[] arrayOfObject = null;
/* 2018 */       doLog(Level.WARNING, "POA.badSkeleton", arrayOfObject, POASystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 2022 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badSkeleton(CompletionStatus paramCompletionStatus) {
/* 2026 */     return badSkeleton(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badSkeleton(Throwable paramThrowable) {
/* 2030 */     return badSkeleton(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badSkeleton() {
/* 2034 */     return badSkeleton(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST nullServant(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2040 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398080492, paramCompletionStatus);
/* 2041 */     if (paramThrowable != null) {
/* 2042 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 2044 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2045 */       Object[] arrayOfObject = null;
/* 2046 */       doLog(Level.FINE, "POA.nullServant", arrayOfObject, POASystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 2050 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST nullServant(CompletionStatus paramCompletionStatus) {
/* 2054 */     return nullServant(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST nullServant(Throwable paramThrowable) {
/* 2058 */     return nullServant(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST nullServant() {
/* 2062 */     return nullServant(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterDestroyed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2068 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398080493, paramCompletionStatus);
/* 2069 */     if (paramThrowable != null) {
/* 2070 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 2072 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2073 */       Object[] arrayOfObject = null;
/* 2074 */       doLog(Level.WARNING, "POA.adapterDestroyed", arrayOfObject, POASystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 2078 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterDestroyed(CompletionStatus paramCompletionStatus) {
/* 2082 */     return adapterDestroyed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterDestroyed(Throwable paramThrowable) {
/* 2086 */     return adapterDestroyed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST adapterDestroyed() {
/* 2090 */     return adapterDestroyed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.logging.POASystemException
 * JD-Core Version:    0.6.2
 */