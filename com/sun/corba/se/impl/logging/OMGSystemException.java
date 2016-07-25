/*      */ package com.sun.corba.se.impl.logging;
/*      */ 
/*      */ import com.sun.corba.se.spi.logging.LogWrapperBase;
/*      */ import com.sun.corba.se.spi.logging.LogWrapperFactory;
/*      */ import com.sun.corba.se.spi.orb.ORB;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import org.omg.CORBA.BAD_CONTEXT;
/*      */ import org.omg.CORBA.BAD_INV_ORDER;
/*      */ import org.omg.CORBA.BAD_OPERATION;
/*      */ import org.omg.CORBA.BAD_PARAM;
/*      */ import org.omg.CORBA.BAD_TYPECODE;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.DATA_CONVERSION;
/*      */ import org.omg.CORBA.IMP_LIMIT;
/*      */ import org.omg.CORBA.INITIALIZE;
/*      */ import org.omg.CORBA.INTERNAL;
/*      */ import org.omg.CORBA.INTF_REPOS;
/*      */ import org.omg.CORBA.INV_OBJREF;
/*      */ import org.omg.CORBA.INV_POLICY;
/*      */ import org.omg.CORBA.MARSHAL;
/*      */ import org.omg.CORBA.NO_IMPLEMENT;
/*      */ import org.omg.CORBA.NO_RESOURCES;
/*      */ import org.omg.CORBA.OBJECT_NOT_EXIST;
/*      */ import org.omg.CORBA.OBJ_ADAPTER;
/*      */ import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
/*      */ import org.omg.CORBA.TRANSIENT;
/*      */ import org.omg.CORBA.UNKNOWN;
/*      */ 
/*      */ public class OMGSystemException extends LogWrapperBase
/*      */ {
/*   50 */   private static LogWrapperFactory factory = new LogWrapperFactory()
/*      */   {
/*      */     public LogWrapperBase create(Logger paramAnonymousLogger) {
/*   53 */       return new OMGSystemException(paramAnonymousLogger);
/*      */     }
/*   50 */   };
/*      */   public static final int IDL_CONTEXT_NOT_FOUND = 1330446337;
/*      */   public static final int NO_MATCHING_IDL_CONTEXT = 1330446338;
/*      */   public static final int DEP_PREVENT_DESTRUCTION = 1330446337;
/*      */   public static final int DESTROY_INDESTRUCTIBLE = 1330446338;
/*      */   public static final int SHUTDOWN_WAIT_FOR_COMPLETION_DEADLOCK = 1330446339;
/*      */   public static final int BAD_OPERATION_AFTER_SHUTDOWN = 1330446340;
/*      */   public static final int BAD_INVOKE = 1330446341;
/*      */   public static final int BAD_SET_SERVANT_MANAGER = 1330446342;
/*      */   public static final int BAD_ARGUMENTS_CALL = 1330446343;
/*      */   public static final int BAD_CTX_CALL = 1330446344;
/*      */   public static final int BAD_RESULT_CALL = 1330446345;
/*      */   public static final int BAD_SEND = 1330446346;
/*      */   public static final int BAD_POLL_BEFORE = 1330446347;
/*      */   public static final int BAD_POLL_AFTER = 1330446348;
/*      */   public static final int BAD_POLL_SYNC = 1330446349;
/*      */   public static final int INVALID_PI_CALL1 = 1330446350;
/*      */   public static final int INVALID_PI_CALL2 = 1330446350;
/*      */   public static final int INVALID_PI_CALL3 = 1330446350;
/*      */   public static final int INVALID_PI_CALL4 = 1330446350;
/*      */   public static final int SERVICE_CONTEXT_ADD_FAILED = 1330446351;
/*      */   public static final int POLICY_FACTORY_REG_FAILED = 1330446352;
/*      */   public static final int CREATE_POA_DESTROY = 1330446353;
/*      */   public static final int PRIORITY_REASSIGN = 1330446354;
/*      */   public static final int XA_START_OUTSIZE = 1330446355;
/*      */   public static final int XA_START_PROTO = 1330446356;
/*      */   public static final int BAD_SERVANT_MANAGER_TYPE = 1330446337;
/*      */   public static final int OPERATION_UNKNOWN_TO_TARGET = 1330446338;
/*      */   public static final int UNABLE_REGISTER_VALUE_FACTORY = 1330446337;
/*      */   public static final int RID_ALREADY_DEFINED = 1330446338;
/*      */   public static final int NAME_USED_IFR = 1330446339;
/*      */   public static final int TARGET_NOT_CONTAINER = 1330446340;
/*      */   public static final int NAME_CLASH = 1330446341;
/*      */   public static final int NOT_SERIALIZABLE = 1330446342;
/*      */   public static final int SO_BAD_SCHEME_NAME = 1330446343;
/*      */   public static final int SO_BAD_ADDRESS = 1330446344;
/*      */   public static final int SO_BAD_SCHEMA_SPECIFIC = 1330446345;
/*      */   public static final int SO_NON_SPECIFIC = 1330446346;
/*      */   public static final int IR_DERIVE_ABS_INT_BASE = 1330446347;
/*      */   public static final int IR_VALUE_SUPPORT = 1330446348;
/*      */   public static final int INCOMPLETE_TYPECODE = 1330446349;
/*      */   public static final int INVALID_OBJECT_ID = 1330446350;
/*      */   public static final int TYPECODE_BAD_NAME = 1330446351;
/*      */   public static final int TYPECODE_BAD_REPID = 1330446352;
/*      */   public static final int TYPECODE_INV_MEMBER = 1330446353;
/*      */   public static final int TC_UNION_DUP_LABEL = 1330446354;
/*      */   public static final int TC_UNION_INCOMPATIBLE = 1330446355;
/*      */   public static final int TC_UNION_BAD_DISC = 1330446356;
/*      */   public static final int SET_EXCEPTION_BAD_ANY = 1330446357;
/*      */   public static final int SET_EXCEPTION_UNLISTED = 1330446358;
/*      */   public static final int NO_CLIENT_WCHAR_CODESET_CTX = 1330446359;
/*      */   public static final int ILLEGAL_SERVICE_CONTEXT = 1330446360;
/*      */   public static final int ENUM_OUT_OF_RANGE = 1330446361;
/*      */   public static final int INVALID_SERVICE_CONTEXT_ID = 1330446362;
/*      */   public static final int RIR_WITH_NULL_OBJECT = 1330446363;
/*      */   public static final int INVALID_COMPONENT_ID = 1330446364;
/*      */   public static final int INVALID_PROFILE_ID = 1330446365;
/*      */   public static final int POLICY_TYPE_DUPLICATE = 1330446366;
/*      */   public static final int BAD_ONEWAY_DEFINITION = 1330446367;
/*      */   public static final int DII_FOR_IMPLICIT_OPERATION = 1330446368;
/*      */   public static final int XA_CALL_INVAL = 1330446369;
/*      */   public static final int UNION_BAD_DISCRIMINATOR = 1330446370;
/*      */   public static final int CTX_ILLEGAL_PROPERTY_NAME = 1330446371;
/*      */   public static final int CTX_ILLEGAL_SEARCH_STRING = 1330446372;
/*      */   public static final int CTX_ILLEGAL_NAME = 1330446373;
/*      */   public static final int CTX_NON_EMPTY = 1330446374;
/*      */   public static final int INVALID_STREAM_FORMAT_VERSION = 1330446375;
/*      */   public static final int NOT_A_VALUEOUTPUTSTREAM = 1330446376;
/*      */   public static final int NOT_A_VALUEINPUTSTREAM = 1330446377;
/*      */   public static final int MARSHALL_INCOMPLETE_TYPECODE = 1330446337;
/*      */   public static final int BAD_MEMBER_TYPECODE = 1330446338;
/*      */   public static final int ILLEGAL_PARAMETER = 1330446339;
/*      */   public static final int CHAR_NOT_IN_CODESET = 1330446337;
/*      */   public static final int PRIORITY_MAP_FAILRE = 1330446338;
/*      */   public static final int NO_USABLE_PROFILE = 1330446337;
/*      */   public static final int PRIORITY_RANGE_RESTRICT = 1330446337;
/*      */   public static final int NO_SERVER_WCHAR_CODESET_CMP = 1330446337;
/*      */   public static final int CODESET_COMPONENT_REQUIRED = 1330446338;
/*      */   public static final int IOR_POLICY_RECONCILE_ERROR = 1330446337;
/*      */   public static final int POLICY_UNKNOWN = 1330446338;
/*      */   public static final int NO_POLICY_FACTORY = 1330446339;
/*      */   public static final int XA_RMERR = 1330446337;
/*      */   public static final int XA_RMFAIL = 1330446338;
/*      */   public static final int NO_IR = 1330446337;
/*      */   public static final int NO_INTERFACE_IN_IR = 1330446338;
/*      */   public static final int UNABLE_LOCATE_VALUE_FACTORY = 1330446337;
/*      */   public static final int SET_RESULT_BEFORE_CTX = 1330446338;
/*      */   public static final int BAD_NVLIST = 1330446339;
/*      */   public static final int NOT_AN_OBJECT_IMPL = 1330446340;
/*      */   public static final int WCHAR_BAD_GIOP_VERSION_SENT = 1330446341;
/*      */   public static final int WCHAR_BAD_GIOP_VERSION_RETURNED = 1330446342;
/*      */   public static final int UNSUPPORTED_FORMAT_VERSION = 1330446343;
/*      */   public static final int RMIIIOP_OPTIONAL_DATA_INCOMPATIBLE1 = 1330446344;
/*      */   public static final int RMIIIOP_OPTIONAL_DATA_INCOMPATIBLE2 = 1330446344;
/*      */   public static final int RMIIIOP_OPTIONAL_DATA_INCOMPATIBLE3 = 1330446344;
/*      */   public static final int MISSING_LOCAL_VALUE_IMPL = 1330446337;
/*      */   public static final int INCOMPATIBLE_VALUE_IMPL = 1330446338;
/*      */   public static final int NO_USABLE_PROFILE_2 = 1330446339;
/*      */   public static final int DII_LOCAL_OBJECT = 1330446340;
/*      */   public static final int BIO_RESET = 1330446341;
/*      */   public static final int BIO_META_NOT_AVAILABLE = 1330446342;
/*      */   public static final int BIO_GENOMIC_NO_ITERATOR = 1330446343;
/*      */   public static final int PI_OPERATION_NOT_SUPPORTED1 = 1330446337;
/*      */   public static final int PI_OPERATION_NOT_SUPPORTED2 = 1330446337;
/*      */   public static final int PI_OPERATION_NOT_SUPPORTED3 = 1330446337;
/*      */   public static final int PI_OPERATION_NOT_SUPPORTED4 = 1330446337;
/*      */   public static final int PI_OPERATION_NOT_SUPPORTED5 = 1330446337;
/*      */   public static final int PI_OPERATION_NOT_SUPPORTED6 = 1330446337;
/*      */   public static final int PI_OPERATION_NOT_SUPPORTED7 = 1330446337;
/*      */   public static final int PI_OPERATION_NOT_SUPPORTED8 = 1330446337;
/*      */   public static final int NO_CONNECTION_PRIORITY = 1330446338;
/*      */   public static final int XA_RB = 1330446337;
/*      */   public static final int XA_NOTA = 1330446338;
/*      */   public static final int XA_END_TRUE_ROLLBACK_DEFERRED = 1330446339;
/*      */   public static final int POA_REQUEST_DISCARD = 1330446337;
/*      */   public static final int NO_USABLE_PROFILE_3 = 1330446338;
/*      */   public static final int REQUEST_CANCELLED = 1330446339;
/*      */   public static final int POA_DESTROYED = 1330446340;
/*      */   public static final int UNREGISTERED_VALUE_AS_OBJREF = 1330446337;
/*      */   public static final int NO_OBJECT_ADAPTOR = 1330446338;
/*      */   public static final int BIO_NOT_AVAILABLE = 1330446339;
/*      */   public static final int OBJECT_ADAPTER_INACTIVE = 1330446340;
/*      */   public static final int ADAPTER_ACTIVATOR_EXCEPTION = 1330446337;
/*      */   public static final int BAD_SERVANT_TYPE = 1330446338;
/*      */   public static final int NO_DEFAULT_SERVANT = 1330446339;
/*      */   public static final int NO_SERVANT_MANAGER = 1330446340;
/*      */   public static final int BAD_POLICY_INCARNATE = 1330446341;
/*      */   public static final int PI_EXC_COMP_ESTABLISHED = 1330446342;
/*      */   public static final int NULL_SERVANT_RETURNED = 1330446343;
/*      */   public static final int UNKNOWN_USER_EXCEPTION = 1330446337;
/*      */   public static final int UNSUPPORTED_SYSTEM_EXCEPTION = 1330446338;
/*      */   public static final int PI_UNKNOWN_USER_EXCEPTION = 1330446339;
/*      */ 
/*      */   public OMGSystemException(Logger paramLogger)
/*      */   {
/*   47 */     super(paramLogger);
/*      */   }
/*      */ 
/*      */   public static OMGSystemException get(ORB paramORB, String paramString)
/*      */   {
/*   59 */     OMGSystemException localOMGSystemException = (OMGSystemException)paramORB.getLogWrapper(paramString, "OMG", factory);
/*      */ 
/*   62 */     return localOMGSystemException;
/*      */   }
/*      */ 
/*      */   public static OMGSystemException get(String paramString)
/*      */   {
/*   67 */     OMGSystemException localOMGSystemException = (OMGSystemException)ORB.staticGetLogWrapper(paramString, "OMG", factory);
/*      */ 
/*   70 */     return localOMGSystemException;
/*      */   }
/*      */ 
/*      */   public BAD_CONTEXT idlContextNotFound(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*   80 */     BAD_CONTEXT localBAD_CONTEXT = new BAD_CONTEXT(1330446337, paramCompletionStatus);
/*   81 */     if (paramThrowable != null) {
/*   82 */       localBAD_CONTEXT.initCause(paramThrowable);
/*      */     }
/*   84 */     if (this.logger.isLoggable(Level.WARNING)) {
/*   85 */       Object[] arrayOfObject = null;
/*   86 */       doLog(Level.WARNING, "OMG.idlContextNotFound", arrayOfObject, OMGSystemException.class, localBAD_CONTEXT);
/*      */     }
/*      */ 
/*   90 */     return localBAD_CONTEXT;
/*      */   }
/*      */ 
/*      */   public BAD_CONTEXT idlContextNotFound(CompletionStatus paramCompletionStatus) {
/*   94 */     return idlContextNotFound(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_CONTEXT idlContextNotFound(Throwable paramThrowable) {
/*   98 */     return idlContextNotFound(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_CONTEXT idlContextNotFound() {
/*  102 */     return idlContextNotFound(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_CONTEXT noMatchingIdlContext(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  108 */     BAD_CONTEXT localBAD_CONTEXT = new BAD_CONTEXT(1330446338, paramCompletionStatus);
/*  109 */     if (paramThrowable != null) {
/*  110 */       localBAD_CONTEXT.initCause(paramThrowable);
/*      */     }
/*  112 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  113 */       Object[] arrayOfObject = null;
/*  114 */       doLog(Level.WARNING, "OMG.noMatchingIdlContext", arrayOfObject, OMGSystemException.class, localBAD_CONTEXT);
/*      */     }
/*      */ 
/*  118 */     return localBAD_CONTEXT;
/*      */   }
/*      */ 
/*      */   public BAD_CONTEXT noMatchingIdlContext(CompletionStatus paramCompletionStatus) {
/*  122 */     return noMatchingIdlContext(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_CONTEXT noMatchingIdlContext(Throwable paramThrowable) {
/*  126 */     return noMatchingIdlContext(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_CONTEXT noMatchingIdlContext() {
/*  130 */     return noMatchingIdlContext(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER depPreventDestruction(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  140 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446337, paramCompletionStatus);
/*  141 */     if (paramThrowable != null) {
/*  142 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  144 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  145 */       Object[] arrayOfObject = null;
/*  146 */       doLog(Level.WARNING, "OMG.depPreventDestruction", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  150 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER depPreventDestruction(CompletionStatus paramCompletionStatus) {
/*  154 */     return depPreventDestruction(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER depPreventDestruction(Throwable paramThrowable) {
/*  158 */     return depPreventDestruction(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER depPreventDestruction() {
/*  162 */     return depPreventDestruction(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER destroyIndestructible(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  168 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446338, paramCompletionStatus);
/*  169 */     if (paramThrowable != null) {
/*  170 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  172 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  173 */       Object[] arrayOfObject = null;
/*  174 */       doLog(Level.WARNING, "OMG.destroyIndestructible", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  178 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER destroyIndestructible(CompletionStatus paramCompletionStatus) {
/*  182 */     return destroyIndestructible(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER destroyIndestructible(Throwable paramThrowable) {
/*  186 */     return destroyIndestructible(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER destroyIndestructible() {
/*  190 */     return destroyIndestructible(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER shutdownWaitForCompletionDeadlock(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  196 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446339, paramCompletionStatus);
/*  197 */     if (paramThrowable != null) {
/*  198 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  200 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  201 */       Object[] arrayOfObject = null;
/*  202 */       doLog(Level.WARNING, "OMG.shutdownWaitForCompletionDeadlock", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  206 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER shutdownWaitForCompletionDeadlock(CompletionStatus paramCompletionStatus) {
/*  210 */     return shutdownWaitForCompletionDeadlock(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER shutdownWaitForCompletionDeadlock(Throwable paramThrowable) {
/*  214 */     return shutdownWaitForCompletionDeadlock(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER shutdownWaitForCompletionDeadlock() {
/*  218 */     return shutdownWaitForCompletionDeadlock(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badOperationAfterShutdown(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  224 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446340, paramCompletionStatus);
/*  225 */     if (paramThrowable != null) {
/*  226 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  228 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  229 */       Object[] arrayOfObject = null;
/*  230 */       doLog(Level.WARNING, "OMG.badOperationAfterShutdown", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  234 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badOperationAfterShutdown(CompletionStatus paramCompletionStatus) {
/*  238 */     return badOperationAfterShutdown(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badOperationAfterShutdown(Throwable paramThrowable) {
/*  242 */     return badOperationAfterShutdown(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badOperationAfterShutdown() {
/*  246 */     return badOperationAfterShutdown(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badInvoke(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  252 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446341, paramCompletionStatus);
/*  253 */     if (paramThrowable != null) {
/*  254 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  256 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  257 */       Object[] arrayOfObject = null;
/*  258 */       doLog(Level.WARNING, "OMG.badInvoke", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  262 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badInvoke(CompletionStatus paramCompletionStatus) {
/*  266 */     return badInvoke(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badInvoke(Throwable paramThrowable) {
/*  270 */     return badInvoke(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badInvoke() {
/*  274 */     return badInvoke(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badSetServantManager(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  280 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446342, paramCompletionStatus);
/*  281 */     if (paramThrowable != null) {
/*  282 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  284 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  285 */       Object[] arrayOfObject = null;
/*  286 */       doLog(Level.WARNING, "OMG.badSetServantManager", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  290 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badSetServantManager(CompletionStatus paramCompletionStatus) {
/*  294 */     return badSetServantManager(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badSetServantManager(Throwable paramThrowable) {
/*  298 */     return badSetServantManager(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badSetServantManager() {
/*  302 */     return badSetServantManager(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badArgumentsCall(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  308 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446343, paramCompletionStatus);
/*  309 */     if (paramThrowable != null) {
/*  310 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  312 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  313 */       Object[] arrayOfObject = null;
/*  314 */       doLog(Level.WARNING, "OMG.badArgumentsCall", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  318 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badArgumentsCall(CompletionStatus paramCompletionStatus) {
/*  322 */     return badArgumentsCall(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badArgumentsCall(Throwable paramThrowable) {
/*  326 */     return badArgumentsCall(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badArgumentsCall() {
/*  330 */     return badArgumentsCall(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badCtxCall(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  336 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446344, paramCompletionStatus);
/*  337 */     if (paramThrowable != null) {
/*  338 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  340 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  341 */       Object[] arrayOfObject = null;
/*  342 */       doLog(Level.WARNING, "OMG.badCtxCall", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  346 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badCtxCall(CompletionStatus paramCompletionStatus) {
/*  350 */     return badCtxCall(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badCtxCall(Throwable paramThrowable) {
/*  354 */     return badCtxCall(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badCtxCall() {
/*  358 */     return badCtxCall(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badResultCall(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  364 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446345, paramCompletionStatus);
/*  365 */     if (paramThrowable != null) {
/*  366 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  368 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  369 */       Object[] arrayOfObject = null;
/*  370 */       doLog(Level.WARNING, "OMG.badResultCall", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  374 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badResultCall(CompletionStatus paramCompletionStatus) {
/*  378 */     return badResultCall(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badResultCall(Throwable paramThrowable) {
/*  382 */     return badResultCall(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badResultCall() {
/*  386 */     return badResultCall(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badSend(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  392 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446346, paramCompletionStatus);
/*  393 */     if (paramThrowable != null) {
/*  394 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  396 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  397 */       Object[] arrayOfObject = null;
/*  398 */       doLog(Level.WARNING, "OMG.badSend", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  402 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badSend(CompletionStatus paramCompletionStatus) {
/*  406 */     return badSend(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badSend(Throwable paramThrowable) {
/*  410 */     return badSend(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badSend() {
/*  414 */     return badSend(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollBefore(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  420 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446347, paramCompletionStatus);
/*  421 */     if (paramThrowable != null) {
/*  422 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  424 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  425 */       Object[] arrayOfObject = null;
/*  426 */       doLog(Level.WARNING, "OMG.badPollBefore", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  430 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollBefore(CompletionStatus paramCompletionStatus) {
/*  434 */     return badPollBefore(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollBefore(Throwable paramThrowable) {
/*  438 */     return badPollBefore(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollBefore() {
/*  442 */     return badPollBefore(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollAfter(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  448 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446348, paramCompletionStatus);
/*  449 */     if (paramThrowable != null) {
/*  450 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  452 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  453 */       Object[] arrayOfObject = null;
/*  454 */       doLog(Level.WARNING, "OMG.badPollAfter", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  458 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollAfter(CompletionStatus paramCompletionStatus) {
/*  462 */     return badPollAfter(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollAfter(Throwable paramThrowable) {
/*  466 */     return badPollAfter(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollAfter() {
/*  470 */     return badPollAfter(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollSync(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  476 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446349, paramCompletionStatus);
/*  477 */     if (paramThrowable != null) {
/*  478 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  480 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  481 */       Object[] arrayOfObject = null;
/*  482 */       doLog(Level.WARNING, "OMG.badPollSync", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  486 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollSync(CompletionStatus paramCompletionStatus) {
/*  490 */     return badPollSync(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollSync(Throwable paramThrowable) {
/*  494 */     return badPollSync(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER badPollSync() {
/*  498 */     return badPollSync(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall1(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  504 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446350, paramCompletionStatus);
/*  505 */     if (paramThrowable != null) {
/*  506 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  508 */     if (this.logger.isLoggable(Level.FINE)) {
/*  509 */       Object[] arrayOfObject = null;
/*  510 */       doLog(Level.FINE, "OMG.invalidPiCall1", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  514 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall1(CompletionStatus paramCompletionStatus) {
/*  518 */     return invalidPiCall1(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall1(Throwable paramThrowable) {
/*  522 */     return invalidPiCall1(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall1() {
/*  526 */     return invalidPiCall1(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall2(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  532 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446350, paramCompletionStatus);
/*  533 */     if (paramThrowable != null) {
/*  534 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  536 */     if (this.logger.isLoggable(Level.FINE)) {
/*  537 */       Object[] arrayOfObject = null;
/*  538 */       doLog(Level.FINE, "OMG.invalidPiCall2", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  542 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall2(CompletionStatus paramCompletionStatus) {
/*  546 */     return invalidPiCall2(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall2(Throwable paramThrowable) {
/*  550 */     return invalidPiCall2(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall2() {
/*  554 */     return invalidPiCall2(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall3(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  560 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446350, paramCompletionStatus);
/*  561 */     if (paramThrowable != null) {
/*  562 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  564 */     if (this.logger.isLoggable(Level.FINE)) {
/*  565 */       Object[] arrayOfObject = null;
/*  566 */       doLog(Level.FINE, "OMG.invalidPiCall3", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  570 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall3(CompletionStatus paramCompletionStatus) {
/*  574 */     return invalidPiCall3(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall3(Throwable paramThrowable) {
/*  578 */     return invalidPiCall3(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall3() {
/*  582 */     return invalidPiCall3(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall4(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  588 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446350, paramCompletionStatus);
/*  589 */     if (paramThrowable != null) {
/*  590 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  592 */     if (this.logger.isLoggable(Level.FINE)) {
/*  593 */       Object[] arrayOfObject = null;
/*  594 */       doLog(Level.FINE, "OMG.invalidPiCall4", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  598 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall4(CompletionStatus paramCompletionStatus) {
/*  602 */     return invalidPiCall4(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall4(Throwable paramThrowable) {
/*  606 */     return invalidPiCall4(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER invalidPiCall4() {
/*  610 */     return invalidPiCall4(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER serviceContextAddFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/*  616 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446351, paramCompletionStatus);
/*  617 */     if (paramThrowable != null) {
/*  618 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  620 */     if (this.logger.isLoggable(Level.FINE)) {
/*  621 */       Object[] arrayOfObject = new Object[1];
/*  622 */       arrayOfObject[0] = paramObject;
/*  623 */       doLog(Level.FINE, "OMG.serviceContextAddFailed", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  627 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER serviceContextAddFailed(CompletionStatus paramCompletionStatus, Object paramObject) {
/*  631 */     return serviceContextAddFailed(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER serviceContextAddFailed(Throwable paramThrowable, Object paramObject) {
/*  635 */     return serviceContextAddFailed(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER serviceContextAddFailed(Object paramObject) {
/*  639 */     return serviceContextAddFailed(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER policyFactoryRegFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/*  645 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446352, paramCompletionStatus);
/*  646 */     if (paramThrowable != null) {
/*  647 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  649 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  650 */       Object[] arrayOfObject = new Object[1];
/*  651 */       arrayOfObject[0] = paramObject;
/*  652 */       doLog(Level.WARNING, "OMG.policyFactoryRegFailed", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  656 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER policyFactoryRegFailed(CompletionStatus paramCompletionStatus, Object paramObject) {
/*  660 */     return policyFactoryRegFailed(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER policyFactoryRegFailed(Throwable paramThrowable, Object paramObject) {
/*  664 */     return policyFactoryRegFailed(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER policyFactoryRegFailed(Object paramObject) {
/*  668 */     return policyFactoryRegFailed(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER createPoaDestroy(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  674 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446353, paramCompletionStatus);
/*  675 */     if (paramThrowable != null) {
/*  676 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  678 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  679 */       Object[] arrayOfObject = null;
/*  680 */       doLog(Level.WARNING, "OMG.createPoaDestroy", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  684 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER createPoaDestroy(CompletionStatus paramCompletionStatus) {
/*  688 */     return createPoaDestroy(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER createPoaDestroy(Throwable paramThrowable) {
/*  692 */     return createPoaDestroy(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER createPoaDestroy() {
/*  696 */     return createPoaDestroy(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER priorityReassign(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  702 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446354, paramCompletionStatus);
/*  703 */     if (paramThrowable != null) {
/*  704 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  706 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  707 */       Object[] arrayOfObject = null;
/*  708 */       doLog(Level.WARNING, "OMG.priorityReassign", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  712 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER priorityReassign(CompletionStatus paramCompletionStatus) {
/*  716 */     return priorityReassign(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER priorityReassign(Throwable paramThrowable) {
/*  720 */     return priorityReassign(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER priorityReassign() {
/*  724 */     return priorityReassign(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER xaStartOutsize(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  730 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446355, paramCompletionStatus);
/*  731 */     if (paramThrowable != null) {
/*  732 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  734 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  735 */       Object[] arrayOfObject = null;
/*  736 */       doLog(Level.WARNING, "OMG.xaStartOutsize", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  740 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER xaStartOutsize(CompletionStatus paramCompletionStatus) {
/*  744 */     return xaStartOutsize(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER xaStartOutsize(Throwable paramThrowable) {
/*  748 */     return xaStartOutsize(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER xaStartOutsize() {
/*  752 */     return xaStartOutsize(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER xaStartProto(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  758 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1330446356, paramCompletionStatus);
/*  759 */     if (paramThrowable != null) {
/*  760 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/*  762 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  763 */       Object[] arrayOfObject = null;
/*  764 */       doLog(Level.WARNING, "OMG.xaStartProto", arrayOfObject, OMGSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/*  768 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER xaStartProto(CompletionStatus paramCompletionStatus) {
/*  772 */     return xaStartProto(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER xaStartProto(Throwable paramThrowable) {
/*  776 */     return xaStartProto(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER xaStartProto() {
/*  780 */     return xaStartProto(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badServantManagerType(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  790 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1330446337, paramCompletionStatus);
/*  791 */     if (paramThrowable != null) {
/*  792 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  794 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  795 */       Object[] arrayOfObject = null;
/*  796 */       doLog(Level.WARNING, "OMG.badServantManagerType", arrayOfObject, OMGSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  800 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badServantManagerType(CompletionStatus paramCompletionStatus) {
/*  804 */     return badServantManagerType(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badServantManagerType(Throwable paramThrowable) {
/*  808 */     return badServantManagerType(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badServantManagerType() {
/*  812 */     return badServantManagerType(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION operationUnknownToTarget(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  818 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1330446338, paramCompletionStatus);
/*  819 */     if (paramThrowable != null) {
/*  820 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  822 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  823 */       Object[] arrayOfObject = null;
/*  824 */       doLog(Level.WARNING, "OMG.operationUnknownToTarget", arrayOfObject, OMGSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  828 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION operationUnknownToTarget(CompletionStatus paramCompletionStatus) {
/*  832 */     return operationUnknownToTarget(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION operationUnknownToTarget(Throwable paramThrowable) {
/*  836 */     return operationUnknownToTarget(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION operationUnknownToTarget() {
/*  840 */     return operationUnknownToTarget(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unableRegisterValueFactory(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  850 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446337, paramCompletionStatus);
/*  851 */     if (paramThrowable != null) {
/*  852 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/*  854 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  855 */       Object[] arrayOfObject = null;
/*  856 */       doLog(Level.WARNING, "OMG.unableRegisterValueFactory", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/*  860 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unableRegisterValueFactory(CompletionStatus paramCompletionStatus) {
/*  864 */     return unableRegisterValueFactory(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unableRegisterValueFactory(Throwable paramThrowable) {
/*  868 */     return unableRegisterValueFactory(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unableRegisterValueFactory() {
/*  872 */     return unableRegisterValueFactory(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ridAlreadyDefined(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  878 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446338, paramCompletionStatus);
/*  879 */     if (paramThrowable != null) {
/*  880 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/*  882 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  883 */       Object[] arrayOfObject = null;
/*  884 */       doLog(Level.WARNING, "OMG.ridAlreadyDefined", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/*  888 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ridAlreadyDefined(CompletionStatus paramCompletionStatus) {
/*  892 */     return ridAlreadyDefined(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ridAlreadyDefined(Throwable paramThrowable) {
/*  896 */     return ridAlreadyDefined(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ridAlreadyDefined() {
/*  900 */     return ridAlreadyDefined(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nameUsedIfr(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  906 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446339, paramCompletionStatus);
/*  907 */     if (paramThrowable != null) {
/*  908 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/*  910 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  911 */       Object[] arrayOfObject = null;
/*  912 */       doLog(Level.WARNING, "OMG.nameUsedIfr", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/*  916 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nameUsedIfr(CompletionStatus paramCompletionStatus) {
/*  920 */     return nameUsedIfr(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nameUsedIfr(Throwable paramThrowable) {
/*  924 */     return nameUsedIfr(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nameUsedIfr() {
/*  928 */     return nameUsedIfr(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM targetNotContainer(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  934 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446340, paramCompletionStatus);
/*  935 */     if (paramThrowable != null) {
/*  936 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/*  938 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  939 */       Object[] arrayOfObject = null;
/*  940 */       doLog(Level.WARNING, "OMG.targetNotContainer", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/*  944 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM targetNotContainer(CompletionStatus paramCompletionStatus) {
/*  948 */     return targetNotContainer(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM targetNotContainer(Throwable paramThrowable) {
/*  952 */     return targetNotContainer(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM targetNotContainer() {
/*  956 */     return targetNotContainer(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nameClash(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  962 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446341, paramCompletionStatus);
/*  963 */     if (paramThrowable != null) {
/*  964 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/*  966 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  967 */       Object[] arrayOfObject = null;
/*  968 */       doLog(Level.WARNING, "OMG.nameClash", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/*  972 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nameClash(CompletionStatus paramCompletionStatus) {
/*  976 */     return nameClash(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nameClash(Throwable paramThrowable) {
/*  980 */     return nameClash(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nameClash() {
/*  984 */     return nameClash(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notSerializable(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/*  990 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446342, paramCompletionStatus);
/*  991 */     if (paramThrowable != null) {
/*  992 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/*  994 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  995 */       Object[] arrayOfObject = new Object[1];
/*  996 */       arrayOfObject[0] = paramObject;
/*  997 */       doLog(Level.WARNING, "OMG.notSerializable", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1001 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notSerializable(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 1005 */     return notSerializable(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notSerializable(Throwable paramThrowable, Object paramObject) {
/* 1009 */     return notSerializable(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notSerializable(Object paramObject) {
/* 1013 */     return notSerializable(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadSchemeName(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1019 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446343, paramCompletionStatus);
/* 1020 */     if (paramThrowable != null) {
/* 1021 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1023 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1024 */       Object[] arrayOfObject = null;
/* 1025 */       doLog(Level.WARNING, "OMG.soBadSchemeName", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1029 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadSchemeName(CompletionStatus paramCompletionStatus) {
/* 1033 */     return soBadSchemeName(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadSchemeName(Throwable paramThrowable) {
/* 1037 */     return soBadSchemeName(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadSchemeName() {
/* 1041 */     return soBadSchemeName(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadAddress(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1047 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446344, paramCompletionStatus);
/* 1048 */     if (paramThrowable != null) {
/* 1049 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1051 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1052 */       Object[] arrayOfObject = null;
/* 1053 */       doLog(Level.WARNING, "OMG.soBadAddress", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1057 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadAddress(CompletionStatus paramCompletionStatus) {
/* 1061 */     return soBadAddress(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadAddress(Throwable paramThrowable) {
/* 1065 */     return soBadAddress(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadAddress() {
/* 1069 */     return soBadAddress(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadSchemaSpecific(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1075 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446345, paramCompletionStatus);
/* 1076 */     if (paramThrowable != null) {
/* 1077 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1079 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1080 */       Object[] arrayOfObject = null;
/* 1081 */       doLog(Level.WARNING, "OMG.soBadSchemaSpecific", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1085 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadSchemaSpecific(CompletionStatus paramCompletionStatus) {
/* 1089 */     return soBadSchemaSpecific(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadSchemaSpecific(Throwable paramThrowable) {
/* 1093 */     return soBadSchemaSpecific(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soBadSchemaSpecific() {
/* 1097 */     return soBadSchemaSpecific(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soNonSpecific(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1103 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446346, paramCompletionStatus);
/* 1104 */     if (paramThrowable != null) {
/* 1105 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1107 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1108 */       Object[] arrayOfObject = null;
/* 1109 */       doLog(Level.WARNING, "OMG.soNonSpecific", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1113 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soNonSpecific(CompletionStatus paramCompletionStatus) {
/* 1117 */     return soNonSpecific(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soNonSpecific(Throwable paramThrowable) {
/* 1121 */     return soNonSpecific(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM soNonSpecific() {
/* 1125 */     return soNonSpecific(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM irDeriveAbsIntBase(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1131 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446347, paramCompletionStatus);
/* 1132 */     if (paramThrowable != null) {
/* 1133 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1135 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1136 */       Object[] arrayOfObject = null;
/* 1137 */       doLog(Level.WARNING, "OMG.irDeriveAbsIntBase", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1141 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM irDeriveAbsIntBase(CompletionStatus paramCompletionStatus) {
/* 1145 */     return irDeriveAbsIntBase(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM irDeriveAbsIntBase(Throwable paramThrowable) {
/* 1149 */     return irDeriveAbsIntBase(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM irDeriveAbsIntBase() {
/* 1153 */     return irDeriveAbsIntBase(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM irValueSupport(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1159 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446348, paramCompletionStatus);
/* 1160 */     if (paramThrowable != null) {
/* 1161 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1163 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1164 */       Object[] arrayOfObject = null;
/* 1165 */       doLog(Level.WARNING, "OMG.irValueSupport", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1169 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM irValueSupport(CompletionStatus paramCompletionStatus) {
/* 1173 */     return irValueSupport(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM irValueSupport(Throwable paramThrowable) {
/* 1177 */     return irValueSupport(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM irValueSupport() {
/* 1181 */     return irValueSupport(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM incompleteTypecode(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1187 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446349, paramCompletionStatus);
/* 1188 */     if (paramThrowable != null) {
/* 1189 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1191 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1192 */       Object[] arrayOfObject = null;
/* 1193 */       doLog(Level.WARNING, "OMG.incompleteTypecode", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1197 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM incompleteTypecode(CompletionStatus paramCompletionStatus) {
/* 1201 */     return incompleteTypecode(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM incompleteTypecode(Throwable paramThrowable) {
/* 1205 */     return incompleteTypecode(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM incompleteTypecode() {
/* 1209 */     return incompleteTypecode(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidObjectId(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1215 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446350, paramCompletionStatus);
/* 1216 */     if (paramThrowable != null) {
/* 1217 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1219 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1220 */       Object[] arrayOfObject = null;
/* 1221 */       doLog(Level.WARNING, "OMG.invalidObjectId", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1225 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidObjectId(CompletionStatus paramCompletionStatus) {
/* 1229 */     return invalidObjectId(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidObjectId(Throwable paramThrowable) {
/* 1233 */     return invalidObjectId(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidObjectId() {
/* 1237 */     return invalidObjectId(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeBadName(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1243 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446351, paramCompletionStatus);
/* 1244 */     if (paramThrowable != null) {
/* 1245 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1247 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1248 */       Object[] arrayOfObject = null;
/* 1249 */       doLog(Level.WARNING, "OMG.typecodeBadName", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1253 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeBadName(CompletionStatus paramCompletionStatus) {
/* 1257 */     return typecodeBadName(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeBadName(Throwable paramThrowable) {
/* 1261 */     return typecodeBadName(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeBadName() {
/* 1265 */     return typecodeBadName(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeBadRepid(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1271 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446352, paramCompletionStatus);
/* 1272 */     if (paramThrowable != null) {
/* 1273 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1275 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1276 */       Object[] arrayOfObject = null;
/* 1277 */       doLog(Level.WARNING, "OMG.typecodeBadRepid", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1281 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeBadRepid(CompletionStatus paramCompletionStatus) {
/* 1285 */     return typecodeBadRepid(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeBadRepid(Throwable paramThrowable) {
/* 1289 */     return typecodeBadRepid(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeBadRepid() {
/* 1293 */     return typecodeBadRepid(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeInvMember(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1299 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446353, paramCompletionStatus);
/* 1300 */     if (paramThrowable != null) {
/* 1301 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1303 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1304 */       Object[] arrayOfObject = null;
/* 1305 */       doLog(Level.WARNING, "OMG.typecodeInvMember", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1309 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeInvMember(CompletionStatus paramCompletionStatus) {
/* 1313 */     return typecodeInvMember(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeInvMember(Throwable paramThrowable) {
/* 1317 */     return typecodeInvMember(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM typecodeInvMember() {
/* 1321 */     return typecodeInvMember(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionDupLabel(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1327 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446354, paramCompletionStatus);
/* 1328 */     if (paramThrowable != null) {
/* 1329 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1331 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1332 */       Object[] arrayOfObject = null;
/* 1333 */       doLog(Level.WARNING, "OMG.tcUnionDupLabel", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1337 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionDupLabel(CompletionStatus paramCompletionStatus) {
/* 1341 */     return tcUnionDupLabel(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionDupLabel(Throwable paramThrowable) {
/* 1345 */     return tcUnionDupLabel(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionDupLabel() {
/* 1349 */     return tcUnionDupLabel(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionIncompatible(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1355 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446355, paramCompletionStatus);
/* 1356 */     if (paramThrowable != null) {
/* 1357 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1359 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1360 */       Object[] arrayOfObject = null;
/* 1361 */       doLog(Level.WARNING, "OMG.tcUnionIncompatible", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1365 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionIncompatible(CompletionStatus paramCompletionStatus) {
/* 1369 */     return tcUnionIncompatible(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionIncompatible(Throwable paramThrowable) {
/* 1373 */     return tcUnionIncompatible(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionIncompatible() {
/* 1377 */     return tcUnionIncompatible(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionBadDisc(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1383 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446356, paramCompletionStatus);
/* 1384 */     if (paramThrowable != null) {
/* 1385 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1387 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1388 */       Object[] arrayOfObject = null;
/* 1389 */       doLog(Level.WARNING, "OMG.tcUnionBadDisc", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1393 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionBadDisc(CompletionStatus paramCompletionStatus) {
/* 1397 */     return tcUnionBadDisc(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionBadDisc(Throwable paramThrowable) {
/* 1401 */     return tcUnionBadDisc(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM tcUnionBadDisc() {
/* 1405 */     return tcUnionBadDisc(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM setExceptionBadAny(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1411 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446357, paramCompletionStatus);
/* 1412 */     if (paramThrowable != null) {
/* 1413 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1415 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1416 */       Object[] arrayOfObject = null;
/* 1417 */       doLog(Level.WARNING, "OMG.setExceptionBadAny", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1421 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM setExceptionBadAny(CompletionStatus paramCompletionStatus) {
/* 1425 */     return setExceptionBadAny(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM setExceptionBadAny(Throwable paramThrowable) {
/* 1429 */     return setExceptionBadAny(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM setExceptionBadAny() {
/* 1433 */     return setExceptionBadAny(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM setExceptionUnlisted(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1439 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446358, paramCompletionStatus);
/* 1440 */     if (paramThrowable != null) {
/* 1441 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1443 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1444 */       Object[] arrayOfObject = null;
/* 1445 */       doLog(Level.WARNING, "OMG.setExceptionUnlisted", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1449 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM setExceptionUnlisted(CompletionStatus paramCompletionStatus) {
/* 1453 */     return setExceptionUnlisted(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM setExceptionUnlisted(Throwable paramThrowable) {
/* 1457 */     return setExceptionUnlisted(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM setExceptionUnlisted() {
/* 1461 */     return setExceptionUnlisted(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM noClientWcharCodesetCtx(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1467 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446359, paramCompletionStatus);
/* 1468 */     if (paramThrowable != null) {
/* 1469 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1471 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1472 */       Object[] arrayOfObject = null;
/* 1473 */       doLog(Level.WARNING, "OMG.noClientWcharCodesetCtx", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1477 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM noClientWcharCodesetCtx(CompletionStatus paramCompletionStatus) {
/* 1481 */     return noClientWcharCodesetCtx(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM noClientWcharCodesetCtx(Throwable paramThrowable) {
/* 1485 */     return noClientWcharCodesetCtx(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM noClientWcharCodesetCtx() {
/* 1489 */     return noClientWcharCodesetCtx(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM illegalServiceContext(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1495 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446360, paramCompletionStatus);
/* 1496 */     if (paramThrowable != null) {
/* 1497 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1499 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1500 */       Object[] arrayOfObject = null;
/* 1501 */       doLog(Level.WARNING, "OMG.illegalServiceContext", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1505 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM illegalServiceContext(CompletionStatus paramCompletionStatus) {
/* 1509 */     return illegalServiceContext(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM illegalServiceContext(Throwable paramThrowable) {
/* 1513 */     return illegalServiceContext(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM illegalServiceContext() {
/* 1517 */     return illegalServiceContext(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM enumOutOfRange(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1523 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446361, paramCompletionStatus);
/* 1524 */     if (paramThrowable != null) {
/* 1525 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1527 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1528 */       Object[] arrayOfObject = null;
/* 1529 */       doLog(Level.WARNING, "OMG.enumOutOfRange", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1533 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM enumOutOfRange(CompletionStatus paramCompletionStatus) {
/* 1537 */     return enumOutOfRange(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM enumOutOfRange(Throwable paramThrowable) {
/* 1541 */     return enumOutOfRange(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM enumOutOfRange() {
/* 1545 */     return enumOutOfRange(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidServiceContextId(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1551 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446362, paramCompletionStatus);
/* 1552 */     if (paramThrowable != null) {
/* 1553 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1555 */     if (this.logger.isLoggable(Level.FINE)) {
/* 1556 */       Object[] arrayOfObject = null;
/* 1557 */       doLog(Level.FINE, "OMG.invalidServiceContextId", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1561 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidServiceContextId(CompletionStatus paramCompletionStatus) {
/* 1565 */     return invalidServiceContextId(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidServiceContextId(Throwable paramThrowable) {
/* 1569 */     return invalidServiceContextId(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidServiceContextId() {
/* 1573 */     return invalidServiceContextId(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM rirWithNullObject(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1579 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446363, paramCompletionStatus);
/* 1580 */     if (paramThrowable != null) {
/* 1581 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1583 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1584 */       Object[] arrayOfObject = null;
/* 1585 */       doLog(Level.WARNING, "OMG.rirWithNullObject", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1589 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM rirWithNullObject(CompletionStatus paramCompletionStatus) {
/* 1593 */     return rirWithNullObject(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM rirWithNullObject(Throwable paramThrowable) {
/* 1597 */     return rirWithNullObject(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM rirWithNullObject() {
/* 1601 */     return rirWithNullObject(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidComponentId(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 1607 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446364, paramCompletionStatus);
/* 1608 */     if (paramThrowable != null) {
/* 1609 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1611 */     if (this.logger.isLoggable(Level.FINE)) {
/* 1612 */       Object[] arrayOfObject = new Object[1];
/* 1613 */       arrayOfObject[0] = paramObject;
/* 1614 */       doLog(Level.FINE, "OMG.invalidComponentId", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1618 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidComponentId(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 1622 */     return invalidComponentId(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidComponentId(Throwable paramThrowable, Object paramObject) {
/* 1626 */     return invalidComponentId(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidComponentId(Object paramObject) {
/* 1630 */     return invalidComponentId(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidProfileId(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1636 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446365, paramCompletionStatus);
/* 1637 */     if (paramThrowable != null) {
/* 1638 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1640 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1641 */       Object[] arrayOfObject = null;
/* 1642 */       doLog(Level.WARNING, "OMG.invalidProfileId", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1646 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidProfileId(CompletionStatus paramCompletionStatus) {
/* 1650 */     return invalidProfileId(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidProfileId(Throwable paramThrowable) {
/* 1654 */     return invalidProfileId(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidProfileId() {
/* 1658 */     return invalidProfileId(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM policyTypeDuplicate(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1664 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446366, paramCompletionStatus);
/* 1665 */     if (paramThrowable != null) {
/* 1666 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1668 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1669 */       Object[] arrayOfObject = null;
/* 1670 */       doLog(Level.WARNING, "OMG.policyTypeDuplicate", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1674 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM policyTypeDuplicate(CompletionStatus paramCompletionStatus) {
/* 1678 */     return policyTypeDuplicate(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM policyTypeDuplicate(Throwable paramThrowable) {
/* 1682 */     return policyTypeDuplicate(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM policyTypeDuplicate() {
/* 1686 */     return policyTypeDuplicate(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badOnewayDefinition(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1692 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446367, paramCompletionStatus);
/* 1693 */     if (paramThrowable != null) {
/* 1694 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1696 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1697 */       Object[] arrayOfObject = null;
/* 1698 */       doLog(Level.WARNING, "OMG.badOnewayDefinition", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1702 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badOnewayDefinition(CompletionStatus paramCompletionStatus) {
/* 1706 */     return badOnewayDefinition(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badOnewayDefinition(Throwable paramThrowable) {
/* 1710 */     return badOnewayDefinition(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badOnewayDefinition() {
/* 1714 */     return badOnewayDefinition(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM diiForImplicitOperation(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1720 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446368, paramCompletionStatus);
/* 1721 */     if (paramThrowable != null) {
/* 1722 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1724 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1725 */       Object[] arrayOfObject = null;
/* 1726 */       doLog(Level.WARNING, "OMG.diiForImplicitOperation", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1730 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM diiForImplicitOperation(CompletionStatus paramCompletionStatus) {
/* 1734 */     return diiForImplicitOperation(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM diiForImplicitOperation(Throwable paramThrowable) {
/* 1738 */     return diiForImplicitOperation(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM diiForImplicitOperation() {
/* 1742 */     return diiForImplicitOperation(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM xaCallInval(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1748 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446369, paramCompletionStatus);
/* 1749 */     if (paramThrowable != null) {
/* 1750 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1752 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1753 */       Object[] arrayOfObject = null;
/* 1754 */       doLog(Level.WARNING, "OMG.xaCallInval", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1758 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM xaCallInval(CompletionStatus paramCompletionStatus) {
/* 1762 */     return xaCallInval(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM xaCallInval(Throwable paramThrowable) {
/* 1766 */     return xaCallInval(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM xaCallInval() {
/* 1770 */     return xaCallInval(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unionBadDiscriminator(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1776 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446370, paramCompletionStatus);
/* 1777 */     if (paramThrowable != null) {
/* 1778 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1780 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1781 */       Object[] arrayOfObject = null;
/* 1782 */       doLog(Level.WARNING, "OMG.unionBadDiscriminator", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1786 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unionBadDiscriminator(CompletionStatus paramCompletionStatus) {
/* 1790 */     return unionBadDiscriminator(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unionBadDiscriminator(Throwable paramThrowable) {
/* 1794 */     return unionBadDiscriminator(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unionBadDiscriminator() {
/* 1798 */     return unionBadDiscriminator(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalPropertyName(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1804 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446371, paramCompletionStatus);
/* 1805 */     if (paramThrowable != null) {
/* 1806 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1808 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1809 */       Object[] arrayOfObject = null;
/* 1810 */       doLog(Level.WARNING, "OMG.ctxIllegalPropertyName", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1814 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalPropertyName(CompletionStatus paramCompletionStatus) {
/* 1818 */     return ctxIllegalPropertyName(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalPropertyName(Throwable paramThrowable) {
/* 1822 */     return ctxIllegalPropertyName(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalPropertyName() {
/* 1826 */     return ctxIllegalPropertyName(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalSearchString(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1832 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446372, paramCompletionStatus);
/* 1833 */     if (paramThrowable != null) {
/* 1834 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1836 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1837 */       Object[] arrayOfObject = null;
/* 1838 */       doLog(Level.WARNING, "OMG.ctxIllegalSearchString", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1842 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalSearchString(CompletionStatus paramCompletionStatus) {
/* 1846 */     return ctxIllegalSearchString(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalSearchString(Throwable paramThrowable) {
/* 1850 */     return ctxIllegalSearchString(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalSearchString() {
/* 1854 */     return ctxIllegalSearchString(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalName(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1860 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446373, paramCompletionStatus);
/* 1861 */     if (paramThrowable != null) {
/* 1862 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1864 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1865 */       Object[] arrayOfObject = null;
/* 1866 */       doLog(Level.WARNING, "OMG.ctxIllegalName", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1870 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalName(CompletionStatus paramCompletionStatus) {
/* 1874 */     return ctxIllegalName(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalName(Throwable paramThrowable) {
/* 1878 */     return ctxIllegalName(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxIllegalName() {
/* 1882 */     return ctxIllegalName(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxNonEmpty(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1888 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446374, paramCompletionStatus);
/* 1889 */     if (paramThrowable != null) {
/* 1890 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1892 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1893 */       Object[] arrayOfObject = null;
/* 1894 */       doLog(Level.WARNING, "OMG.ctxNonEmpty", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1898 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxNonEmpty(CompletionStatus paramCompletionStatus) {
/* 1902 */     return ctxNonEmpty(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxNonEmpty(Throwable paramThrowable) {
/* 1906 */     return ctxNonEmpty(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM ctxNonEmpty() {
/* 1910 */     return ctxNonEmpty(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidStreamFormatVersion(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 1916 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446375, paramCompletionStatus);
/* 1917 */     if (paramThrowable != null) {
/* 1918 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1920 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1921 */       Object[] arrayOfObject = new Object[1];
/* 1922 */       arrayOfObject[0] = paramObject;
/* 1923 */       doLog(Level.WARNING, "OMG.invalidStreamFormatVersion", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1927 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidStreamFormatVersion(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 1931 */     return invalidStreamFormatVersion(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidStreamFormatVersion(Throwable paramThrowable, Object paramObject) {
/* 1935 */     return invalidStreamFormatVersion(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidStreamFormatVersion(Object paramObject) {
/* 1939 */     return invalidStreamFormatVersion(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notAValueoutputstream(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1945 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446376, paramCompletionStatus);
/* 1946 */     if (paramThrowable != null) {
/* 1947 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1949 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1950 */       Object[] arrayOfObject = null;
/* 1951 */       doLog(Level.WARNING, "OMG.notAValueoutputstream", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1955 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notAValueoutputstream(CompletionStatus paramCompletionStatus) {
/* 1959 */     return notAValueoutputstream(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notAValueoutputstream(Throwable paramThrowable) {
/* 1963 */     return notAValueoutputstream(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notAValueoutputstream() {
/* 1967 */     return notAValueoutputstream(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notAValueinputstream(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1973 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1330446377, paramCompletionStatus);
/* 1974 */     if (paramThrowable != null) {
/* 1975 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1977 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1978 */       Object[] arrayOfObject = null;
/* 1979 */       doLog(Level.WARNING, "OMG.notAValueinputstream", arrayOfObject, OMGSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1983 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notAValueinputstream(CompletionStatus paramCompletionStatus) {
/* 1987 */     return notAValueinputstream(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notAValueinputstream(Throwable paramThrowable) {
/* 1991 */     return notAValueinputstream(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM notAValueinputstream() {
/* 1995 */     return notAValueinputstream(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE marshallIncompleteTypecode(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2005 */     BAD_TYPECODE localBAD_TYPECODE = new BAD_TYPECODE(1330446337, paramCompletionStatus);
/* 2006 */     if (paramThrowable != null) {
/* 2007 */       localBAD_TYPECODE.initCause(paramThrowable);
/*      */     }
/* 2009 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2010 */       Object[] arrayOfObject = null;
/* 2011 */       doLog(Level.WARNING, "OMG.marshallIncompleteTypecode", arrayOfObject, OMGSystemException.class, localBAD_TYPECODE);
/*      */     }
/*      */ 
/* 2015 */     return localBAD_TYPECODE;
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE marshallIncompleteTypecode(CompletionStatus paramCompletionStatus) {
/* 2019 */     return marshallIncompleteTypecode(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE marshallIncompleteTypecode(Throwable paramThrowable) {
/* 2023 */     return marshallIncompleteTypecode(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE marshallIncompleteTypecode() {
/* 2027 */     return marshallIncompleteTypecode(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE badMemberTypecode(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2033 */     BAD_TYPECODE localBAD_TYPECODE = new BAD_TYPECODE(1330446338, paramCompletionStatus);
/* 2034 */     if (paramThrowable != null) {
/* 2035 */       localBAD_TYPECODE.initCause(paramThrowable);
/*      */     }
/* 2037 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2038 */       Object[] arrayOfObject = null;
/* 2039 */       doLog(Level.WARNING, "OMG.badMemberTypecode", arrayOfObject, OMGSystemException.class, localBAD_TYPECODE);
/*      */     }
/*      */ 
/* 2043 */     return localBAD_TYPECODE;
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE badMemberTypecode(CompletionStatus paramCompletionStatus) {
/* 2047 */     return badMemberTypecode(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE badMemberTypecode(Throwable paramThrowable) {
/* 2051 */     return badMemberTypecode(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE badMemberTypecode() {
/* 2055 */     return badMemberTypecode(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE illegalParameter(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2061 */     BAD_TYPECODE localBAD_TYPECODE = new BAD_TYPECODE(1330446339, paramCompletionStatus);
/* 2062 */     if (paramThrowable != null) {
/* 2063 */       localBAD_TYPECODE.initCause(paramThrowable);
/*      */     }
/* 2065 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2066 */       Object[] arrayOfObject = null;
/* 2067 */       doLog(Level.WARNING, "OMG.illegalParameter", arrayOfObject, OMGSystemException.class, localBAD_TYPECODE);
/*      */     }
/*      */ 
/* 2071 */     return localBAD_TYPECODE;
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE illegalParameter(CompletionStatus paramCompletionStatus) {
/* 2075 */     return illegalParameter(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE illegalParameter(Throwable paramThrowable) {
/* 2079 */     return illegalParameter(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE illegalParameter() {
/* 2083 */     return illegalParameter(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION charNotInCodeset(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2093 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1330446337, paramCompletionStatus);
/* 2094 */     if (paramThrowable != null) {
/* 2095 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2097 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2098 */       Object[] arrayOfObject = null;
/* 2099 */       doLog(Level.WARNING, "OMG.charNotInCodeset", arrayOfObject, OMGSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2103 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION charNotInCodeset(CompletionStatus paramCompletionStatus) {
/* 2107 */     return charNotInCodeset(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION charNotInCodeset(Throwable paramThrowable) {
/* 2111 */     return charNotInCodeset(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION charNotInCodeset() {
/* 2115 */     return charNotInCodeset(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION priorityMapFailre(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2121 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1330446338, paramCompletionStatus);
/* 2122 */     if (paramThrowable != null) {
/* 2123 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2125 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2126 */       Object[] arrayOfObject = null;
/* 2127 */       doLog(Level.WARNING, "OMG.priorityMapFailre", arrayOfObject, OMGSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2131 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION priorityMapFailre(CompletionStatus paramCompletionStatus) {
/* 2135 */     return priorityMapFailre(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION priorityMapFailre(Throwable paramThrowable) {
/* 2139 */     return priorityMapFailre(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION priorityMapFailre() {
/* 2143 */     return priorityMapFailre(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public IMP_LIMIT noUsableProfile(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2153 */     IMP_LIMIT localIMP_LIMIT = new IMP_LIMIT(1330446337, paramCompletionStatus);
/* 2154 */     if (paramThrowable != null) {
/* 2155 */       localIMP_LIMIT.initCause(paramThrowable);
/*      */     }
/* 2157 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2158 */       Object[] arrayOfObject = null;
/* 2159 */       doLog(Level.WARNING, "OMG.noUsableProfile", arrayOfObject, OMGSystemException.class, localIMP_LIMIT);
/*      */     }
/*      */ 
/* 2163 */     return localIMP_LIMIT;
/*      */   }
/*      */ 
/*      */   public IMP_LIMIT noUsableProfile(CompletionStatus paramCompletionStatus) {
/* 2167 */     return noUsableProfile(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public IMP_LIMIT noUsableProfile(Throwable paramThrowable) {
/* 2171 */     return noUsableProfile(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public IMP_LIMIT noUsableProfile() {
/* 2175 */     return noUsableProfile(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE priorityRangeRestrict(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2185 */     INITIALIZE localINITIALIZE = new INITIALIZE(1330446337, paramCompletionStatus);
/* 2186 */     if (paramThrowable != null) {
/* 2187 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 2189 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2190 */       Object[] arrayOfObject = null;
/* 2191 */       doLog(Level.WARNING, "OMG.priorityRangeRestrict", arrayOfObject, OMGSystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 2195 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE priorityRangeRestrict(CompletionStatus paramCompletionStatus) {
/* 2199 */     return priorityRangeRestrict(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE priorityRangeRestrict(Throwable paramThrowable) {
/* 2203 */     return priorityRangeRestrict(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE priorityRangeRestrict() {
/* 2207 */     return priorityRangeRestrict(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF noServerWcharCodesetCmp(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2217 */     INV_OBJREF localINV_OBJREF = new INV_OBJREF(1330446337, paramCompletionStatus);
/* 2218 */     if (paramThrowable != null) {
/* 2219 */       localINV_OBJREF.initCause(paramThrowable);
/*      */     }
/* 2221 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2222 */       Object[] arrayOfObject = null;
/* 2223 */       doLog(Level.WARNING, "OMG.noServerWcharCodesetCmp", arrayOfObject, OMGSystemException.class, localINV_OBJREF);
/*      */     }
/*      */ 
/* 2227 */     return localINV_OBJREF;
/*      */   }
/*      */ 
/*      */   public INV_OBJREF noServerWcharCodesetCmp(CompletionStatus paramCompletionStatus) {
/* 2231 */     return noServerWcharCodesetCmp(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF noServerWcharCodesetCmp(Throwable paramThrowable) {
/* 2235 */     return noServerWcharCodesetCmp(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF noServerWcharCodesetCmp() {
/* 2239 */     return noServerWcharCodesetCmp(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF codesetComponentRequired(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2245 */     INV_OBJREF localINV_OBJREF = new INV_OBJREF(1330446338, paramCompletionStatus);
/* 2246 */     if (paramThrowable != null) {
/* 2247 */       localINV_OBJREF.initCause(paramThrowable);
/*      */     }
/* 2249 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2250 */       Object[] arrayOfObject = null;
/* 2251 */       doLog(Level.WARNING, "OMG.codesetComponentRequired", arrayOfObject, OMGSystemException.class, localINV_OBJREF);
/*      */     }
/*      */ 
/* 2255 */     return localINV_OBJREF;
/*      */   }
/*      */ 
/*      */   public INV_OBJREF codesetComponentRequired(CompletionStatus paramCompletionStatus) {
/* 2259 */     return codesetComponentRequired(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF codesetComponentRequired(Throwable paramThrowable) {
/* 2263 */     return codesetComponentRequired(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF codesetComponentRequired() {
/* 2267 */     return codesetComponentRequired(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INV_POLICY iorPolicyReconcileError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2277 */     INV_POLICY localINV_POLICY = new INV_POLICY(1330446337, paramCompletionStatus);
/* 2278 */     if (paramThrowable != null) {
/* 2279 */       localINV_POLICY.initCause(paramThrowable);
/*      */     }
/* 2281 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2282 */       Object[] arrayOfObject = null;
/* 2283 */       doLog(Level.WARNING, "OMG.iorPolicyReconcileError", arrayOfObject, OMGSystemException.class, localINV_POLICY);
/*      */     }
/*      */ 
/* 2287 */     return localINV_POLICY;
/*      */   }
/*      */ 
/*      */   public INV_POLICY iorPolicyReconcileError(CompletionStatus paramCompletionStatus) {
/* 2291 */     return iorPolicyReconcileError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INV_POLICY iorPolicyReconcileError(Throwable paramThrowable) {
/* 2295 */     return iorPolicyReconcileError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INV_POLICY iorPolicyReconcileError() {
/* 2299 */     return iorPolicyReconcileError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INV_POLICY policyUnknown(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2305 */     INV_POLICY localINV_POLICY = new INV_POLICY(1330446338, paramCompletionStatus);
/* 2306 */     if (paramThrowable != null) {
/* 2307 */       localINV_POLICY.initCause(paramThrowable);
/*      */     }
/* 2309 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2310 */       Object[] arrayOfObject = null;
/* 2311 */       doLog(Level.WARNING, "OMG.policyUnknown", arrayOfObject, OMGSystemException.class, localINV_POLICY);
/*      */     }
/*      */ 
/* 2315 */     return localINV_POLICY;
/*      */   }
/*      */ 
/*      */   public INV_POLICY policyUnknown(CompletionStatus paramCompletionStatus) {
/* 2319 */     return policyUnknown(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INV_POLICY policyUnknown(Throwable paramThrowable) {
/* 2323 */     return policyUnknown(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INV_POLICY policyUnknown() {
/* 2327 */     return policyUnknown(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INV_POLICY noPolicyFactory(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2333 */     INV_POLICY localINV_POLICY = new INV_POLICY(1330446339, paramCompletionStatus);
/* 2334 */     if (paramThrowable != null) {
/* 2335 */       localINV_POLICY.initCause(paramThrowable);
/*      */     }
/* 2337 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2338 */       Object[] arrayOfObject = null;
/* 2339 */       doLog(Level.WARNING, "OMG.noPolicyFactory", arrayOfObject, OMGSystemException.class, localINV_POLICY);
/*      */     }
/*      */ 
/* 2343 */     return localINV_POLICY;
/*      */   }
/*      */ 
/*      */   public INV_POLICY noPolicyFactory(CompletionStatus paramCompletionStatus) {
/* 2347 */     return noPolicyFactory(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INV_POLICY noPolicyFactory(Throwable paramThrowable) {
/* 2351 */     return noPolicyFactory(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INV_POLICY noPolicyFactory() {
/* 2355 */     return noPolicyFactory(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL xaRmerr(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2365 */     INTERNAL localINTERNAL = new INTERNAL(1330446337, paramCompletionStatus);
/* 2366 */     if (paramThrowable != null) {
/* 2367 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 2369 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2370 */       Object[] arrayOfObject = null;
/* 2371 */       doLog(Level.WARNING, "OMG.xaRmerr", arrayOfObject, OMGSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 2375 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL xaRmerr(CompletionStatus paramCompletionStatus) {
/* 2379 */     return xaRmerr(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL xaRmerr(Throwable paramThrowable) {
/* 2383 */     return xaRmerr(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL xaRmerr() {
/* 2387 */     return xaRmerr(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL xaRmfail(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2393 */     INTERNAL localINTERNAL = new INTERNAL(1330446338, paramCompletionStatus);
/* 2394 */     if (paramThrowable != null) {
/* 2395 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 2397 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2398 */       Object[] arrayOfObject = null;
/* 2399 */       doLog(Level.WARNING, "OMG.xaRmfail", arrayOfObject, OMGSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 2403 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL xaRmfail(CompletionStatus paramCompletionStatus) {
/* 2407 */     return xaRmfail(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL xaRmfail(Throwable paramThrowable) {
/* 2411 */     return xaRmfail(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL xaRmfail() {
/* 2415 */     return xaRmfail(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTF_REPOS noIr(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2425 */     INTF_REPOS localINTF_REPOS = new INTF_REPOS(1330446337, paramCompletionStatus);
/* 2426 */     if (paramThrowable != null) {
/* 2427 */       localINTF_REPOS.initCause(paramThrowable);
/*      */     }
/* 2429 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2430 */       Object[] arrayOfObject = null;
/* 2431 */       doLog(Level.WARNING, "OMG.noIr", arrayOfObject, OMGSystemException.class, localINTF_REPOS);
/*      */     }
/*      */ 
/* 2435 */     return localINTF_REPOS;
/*      */   }
/*      */ 
/*      */   public INTF_REPOS noIr(CompletionStatus paramCompletionStatus) {
/* 2439 */     return noIr(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTF_REPOS noIr(Throwable paramThrowable) {
/* 2443 */     return noIr(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTF_REPOS noIr() {
/* 2447 */     return noIr(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTF_REPOS noInterfaceInIr(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2453 */     INTF_REPOS localINTF_REPOS = new INTF_REPOS(1330446338, paramCompletionStatus);
/* 2454 */     if (paramThrowable != null) {
/* 2455 */       localINTF_REPOS.initCause(paramThrowable);
/*      */     }
/* 2457 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2458 */       Object[] arrayOfObject = null;
/* 2459 */       doLog(Level.WARNING, "OMG.noInterfaceInIr", arrayOfObject, OMGSystemException.class, localINTF_REPOS);
/*      */     }
/*      */ 
/* 2463 */     return localINTF_REPOS;
/*      */   }
/*      */ 
/*      */   public INTF_REPOS noInterfaceInIr(CompletionStatus paramCompletionStatus) {
/* 2467 */     return noInterfaceInIr(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTF_REPOS noInterfaceInIr(Throwable paramThrowable) {
/* 2471 */     return noInterfaceInIr(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTF_REPOS noInterfaceInIr() {
/* 2475 */     return noInterfaceInIr(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unableLocateValueFactory(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2485 */     MARSHAL localMARSHAL = new MARSHAL(1330446337, paramCompletionStatus);
/* 2486 */     if (paramThrowable != null) {
/* 2487 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2489 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2490 */       Object[] arrayOfObject = null;
/* 2491 */       doLog(Level.FINE, "OMG.unableLocateValueFactory", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2495 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL unableLocateValueFactory(CompletionStatus paramCompletionStatus) {
/* 2499 */     return unableLocateValueFactory(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unableLocateValueFactory(Throwable paramThrowable) {
/* 2503 */     return unableLocateValueFactory(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL unableLocateValueFactory() {
/* 2507 */     return unableLocateValueFactory(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL setResultBeforeCtx(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2513 */     MARSHAL localMARSHAL = new MARSHAL(1330446338, paramCompletionStatus);
/* 2514 */     if (paramThrowable != null) {
/* 2515 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2517 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2518 */       Object[] arrayOfObject = null;
/* 2519 */       doLog(Level.WARNING, "OMG.setResultBeforeCtx", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2523 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL setResultBeforeCtx(CompletionStatus paramCompletionStatus) {
/* 2527 */     return setResultBeforeCtx(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL setResultBeforeCtx(Throwable paramThrowable) {
/* 2531 */     return setResultBeforeCtx(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL setResultBeforeCtx() {
/* 2535 */     return setResultBeforeCtx(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badNvlist(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2541 */     MARSHAL localMARSHAL = new MARSHAL(1330446339, paramCompletionStatus);
/* 2542 */     if (paramThrowable != null) {
/* 2543 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2545 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2546 */       Object[] arrayOfObject = null;
/* 2547 */       doLog(Level.WARNING, "OMG.badNvlist", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2551 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badNvlist(CompletionStatus paramCompletionStatus) {
/* 2555 */     return badNvlist(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badNvlist(Throwable paramThrowable) {
/* 2559 */     return badNvlist(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL badNvlist() {
/* 2563 */     return badNvlist(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL notAnObjectImpl(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2569 */     MARSHAL localMARSHAL = new MARSHAL(1330446340, paramCompletionStatus);
/* 2570 */     if (paramThrowable != null) {
/* 2571 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2573 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2574 */       Object[] arrayOfObject = null;
/* 2575 */       doLog(Level.WARNING, "OMG.notAnObjectImpl", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2579 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL notAnObjectImpl(CompletionStatus paramCompletionStatus) {
/* 2583 */     return notAnObjectImpl(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL notAnObjectImpl(Throwable paramThrowable) {
/* 2587 */     return notAnObjectImpl(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL notAnObjectImpl() {
/* 2591 */     return notAnObjectImpl(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharBadGiopVersionSent(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2597 */     MARSHAL localMARSHAL = new MARSHAL(1330446341, paramCompletionStatus);
/* 2598 */     if (paramThrowable != null) {
/* 2599 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2601 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2602 */       Object[] arrayOfObject = null;
/* 2603 */       doLog(Level.WARNING, "OMG.wcharBadGiopVersionSent", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2607 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharBadGiopVersionSent(CompletionStatus paramCompletionStatus) {
/* 2611 */     return wcharBadGiopVersionSent(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharBadGiopVersionSent(Throwable paramThrowable) {
/* 2615 */     return wcharBadGiopVersionSent(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharBadGiopVersionSent() {
/* 2619 */     return wcharBadGiopVersionSent(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharBadGiopVersionReturned(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2625 */     MARSHAL localMARSHAL = new MARSHAL(1330446342, paramCompletionStatus);
/* 2626 */     if (paramThrowable != null) {
/* 2627 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2629 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2630 */       Object[] arrayOfObject = null;
/* 2631 */       doLog(Level.WARNING, "OMG.wcharBadGiopVersionReturned", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2635 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharBadGiopVersionReturned(CompletionStatus paramCompletionStatus) {
/* 2639 */     return wcharBadGiopVersionReturned(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharBadGiopVersionReturned(Throwable paramThrowable) {
/* 2643 */     return wcharBadGiopVersionReturned(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharBadGiopVersionReturned() {
/* 2647 */     return wcharBadGiopVersionReturned(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unsupportedFormatVersion(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2653 */     MARSHAL localMARSHAL = new MARSHAL(1330446343, paramCompletionStatus);
/* 2654 */     if (paramThrowable != null) {
/* 2655 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2657 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2658 */       Object[] arrayOfObject = null;
/* 2659 */       doLog(Level.WARNING, "OMG.unsupportedFormatVersion", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2663 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL unsupportedFormatVersion(CompletionStatus paramCompletionStatus) {
/* 2667 */     return unsupportedFormatVersion(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unsupportedFormatVersion(Throwable paramThrowable) {
/* 2671 */     return unsupportedFormatVersion(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL unsupportedFormatVersion() {
/* 2675 */     return unsupportedFormatVersion(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible1(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2681 */     MARSHAL localMARSHAL = new MARSHAL(1330446344, paramCompletionStatus);
/* 2682 */     if (paramThrowable != null) {
/* 2683 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2685 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2686 */       Object[] arrayOfObject = null;
/* 2687 */       doLog(Level.WARNING, "OMG.rmiiiopOptionalDataIncompatible1", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2691 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible1(CompletionStatus paramCompletionStatus) {
/* 2695 */     return rmiiiopOptionalDataIncompatible1(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible1(Throwable paramThrowable) {
/* 2699 */     return rmiiiopOptionalDataIncompatible1(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible1() {
/* 2703 */     return rmiiiopOptionalDataIncompatible1(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible2(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2709 */     MARSHAL localMARSHAL = new MARSHAL(1330446344, paramCompletionStatus);
/* 2710 */     if (paramThrowable != null) {
/* 2711 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2713 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2714 */       Object[] arrayOfObject = null;
/* 2715 */       doLog(Level.WARNING, "OMG.rmiiiopOptionalDataIncompatible2", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2719 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible2(CompletionStatus paramCompletionStatus) {
/* 2723 */     return rmiiiopOptionalDataIncompatible2(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible2(Throwable paramThrowable) {
/* 2727 */     return rmiiiopOptionalDataIncompatible2(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible2() {
/* 2731 */     return rmiiiopOptionalDataIncompatible2(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible3(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2737 */     MARSHAL localMARSHAL = new MARSHAL(1330446344, paramCompletionStatus);
/* 2738 */     if (paramThrowable != null) {
/* 2739 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 2741 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2742 */       Object[] arrayOfObject = null;
/* 2743 */       doLog(Level.FINE, "OMG.rmiiiopOptionalDataIncompatible3", arrayOfObject, OMGSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 2747 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible3(CompletionStatus paramCompletionStatus) {
/* 2751 */     return rmiiiopOptionalDataIncompatible3(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible3(Throwable paramThrowable) {
/* 2755 */     return rmiiiopOptionalDataIncompatible3(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL rmiiiopOptionalDataIncompatible3() {
/* 2759 */     return rmiiiopOptionalDataIncompatible3(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT missingLocalValueImpl(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2769 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1330446337, paramCompletionStatus);
/* 2770 */     if (paramThrowable != null) {
/* 2771 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 2773 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2774 */       Object[] arrayOfObject = null;
/* 2775 */       doLog(Level.WARNING, "OMG.missingLocalValueImpl", arrayOfObject, OMGSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 2779 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT missingLocalValueImpl(CompletionStatus paramCompletionStatus) {
/* 2783 */     return missingLocalValueImpl(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT missingLocalValueImpl(Throwable paramThrowable) {
/* 2787 */     return missingLocalValueImpl(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT missingLocalValueImpl() {
/* 2791 */     return missingLocalValueImpl(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT incompatibleValueImpl(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2797 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1330446338, paramCompletionStatus);
/* 2798 */     if (paramThrowable != null) {
/* 2799 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 2801 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2802 */       Object[] arrayOfObject = null;
/* 2803 */       doLog(Level.WARNING, "OMG.incompatibleValueImpl", arrayOfObject, OMGSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 2807 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT incompatibleValueImpl(CompletionStatus paramCompletionStatus) {
/* 2811 */     return incompatibleValueImpl(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT incompatibleValueImpl(Throwable paramThrowable) {
/* 2815 */     return incompatibleValueImpl(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT incompatibleValueImpl() {
/* 2819 */     return incompatibleValueImpl(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT noUsableProfile2(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2825 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1330446339, paramCompletionStatus);
/* 2826 */     if (paramThrowable != null) {
/* 2827 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 2829 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2830 */       Object[] arrayOfObject = null;
/* 2831 */       doLog(Level.WARNING, "OMG.noUsableProfile2", arrayOfObject, OMGSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 2835 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT noUsableProfile2(CompletionStatus paramCompletionStatus) {
/* 2839 */     return noUsableProfile2(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT noUsableProfile2(Throwable paramThrowable) {
/* 2843 */     return noUsableProfile2(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT noUsableProfile2() {
/* 2847 */     return noUsableProfile2(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT diiLocalObject(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2853 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1330446340, paramCompletionStatus);
/* 2854 */     if (paramThrowable != null) {
/* 2855 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 2857 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2858 */       Object[] arrayOfObject = null;
/* 2859 */       doLog(Level.WARNING, "OMG.diiLocalObject", arrayOfObject, OMGSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 2863 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT diiLocalObject(CompletionStatus paramCompletionStatus) {
/* 2867 */     return diiLocalObject(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT diiLocalObject(Throwable paramThrowable) {
/* 2871 */     return diiLocalObject(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT diiLocalObject() {
/* 2875 */     return diiLocalObject(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioReset(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2881 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1330446341, paramCompletionStatus);
/* 2882 */     if (paramThrowable != null) {
/* 2883 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 2885 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2886 */       Object[] arrayOfObject = null;
/* 2887 */       doLog(Level.WARNING, "OMG.bioReset", arrayOfObject, OMGSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 2891 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioReset(CompletionStatus paramCompletionStatus) {
/* 2895 */     return bioReset(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioReset(Throwable paramThrowable) {
/* 2899 */     return bioReset(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioReset() {
/* 2903 */     return bioReset(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioMetaNotAvailable(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2909 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1330446342, paramCompletionStatus);
/* 2910 */     if (paramThrowable != null) {
/* 2911 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 2913 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2914 */       Object[] arrayOfObject = null;
/* 2915 */       doLog(Level.WARNING, "OMG.bioMetaNotAvailable", arrayOfObject, OMGSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 2919 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioMetaNotAvailable(CompletionStatus paramCompletionStatus) {
/* 2923 */     return bioMetaNotAvailable(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioMetaNotAvailable(Throwable paramThrowable) {
/* 2927 */     return bioMetaNotAvailable(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioMetaNotAvailable() {
/* 2931 */     return bioMetaNotAvailable(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioGenomicNoIterator(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2937 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1330446343, paramCompletionStatus);
/* 2938 */     if (paramThrowable != null) {
/* 2939 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 2941 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2942 */       Object[] arrayOfObject = null;
/* 2943 */       doLog(Level.WARNING, "OMG.bioGenomicNoIterator", arrayOfObject, OMGSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 2947 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioGenomicNoIterator(CompletionStatus paramCompletionStatus) {
/* 2951 */     return bioGenomicNoIterator(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioGenomicNoIterator(Throwable paramThrowable) {
/* 2955 */     return bioGenomicNoIterator(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT bioGenomicNoIterator() {
/* 2959 */     return bioGenomicNoIterator(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported1(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2969 */     NO_RESOURCES localNO_RESOURCES = new NO_RESOURCES(1330446337, paramCompletionStatus);
/* 2970 */     if (paramThrowable != null) {
/* 2971 */       localNO_RESOURCES.initCause(paramThrowable);
/*      */     }
/* 2973 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2974 */       Object[] arrayOfObject = null;
/* 2975 */       doLog(Level.FINE, "OMG.piOperationNotSupported1", arrayOfObject, OMGSystemException.class, localNO_RESOURCES);
/*      */     }
/*      */ 
/* 2979 */     return localNO_RESOURCES;
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported1(CompletionStatus paramCompletionStatus) {
/* 2983 */     return piOperationNotSupported1(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported1(Throwable paramThrowable) {
/* 2987 */     return piOperationNotSupported1(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported1() {
/* 2991 */     return piOperationNotSupported1(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported2(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2997 */     NO_RESOURCES localNO_RESOURCES = new NO_RESOURCES(1330446337, paramCompletionStatus);
/* 2998 */     if (paramThrowable != null) {
/* 2999 */       localNO_RESOURCES.initCause(paramThrowable);
/*      */     }
/* 3001 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3002 */       Object[] arrayOfObject = null;
/* 3003 */       doLog(Level.FINE, "OMG.piOperationNotSupported2", arrayOfObject, OMGSystemException.class, localNO_RESOURCES);
/*      */     }
/*      */ 
/* 3007 */     return localNO_RESOURCES;
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported2(CompletionStatus paramCompletionStatus) {
/* 3011 */     return piOperationNotSupported2(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported2(Throwable paramThrowable) {
/* 3015 */     return piOperationNotSupported2(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported2() {
/* 3019 */     return piOperationNotSupported2(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported3(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3025 */     NO_RESOURCES localNO_RESOURCES = new NO_RESOURCES(1330446337, paramCompletionStatus);
/* 3026 */     if (paramThrowable != null) {
/* 3027 */       localNO_RESOURCES.initCause(paramThrowable);
/*      */     }
/* 3029 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3030 */       Object[] arrayOfObject = null;
/* 3031 */       doLog(Level.FINE, "OMG.piOperationNotSupported3", arrayOfObject, OMGSystemException.class, localNO_RESOURCES);
/*      */     }
/*      */ 
/* 3035 */     return localNO_RESOURCES;
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported3(CompletionStatus paramCompletionStatus) {
/* 3039 */     return piOperationNotSupported3(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported3(Throwable paramThrowable) {
/* 3043 */     return piOperationNotSupported3(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported3() {
/* 3047 */     return piOperationNotSupported3(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported4(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3053 */     NO_RESOURCES localNO_RESOURCES = new NO_RESOURCES(1330446337, paramCompletionStatus);
/* 3054 */     if (paramThrowable != null) {
/* 3055 */       localNO_RESOURCES.initCause(paramThrowable);
/*      */     }
/* 3057 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3058 */       Object[] arrayOfObject = null;
/* 3059 */       doLog(Level.FINE, "OMG.piOperationNotSupported4", arrayOfObject, OMGSystemException.class, localNO_RESOURCES);
/*      */     }
/*      */ 
/* 3063 */     return localNO_RESOURCES;
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported4(CompletionStatus paramCompletionStatus) {
/* 3067 */     return piOperationNotSupported4(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported4(Throwable paramThrowable) {
/* 3071 */     return piOperationNotSupported4(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported4() {
/* 3075 */     return piOperationNotSupported4(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported5(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3081 */     NO_RESOURCES localNO_RESOURCES = new NO_RESOURCES(1330446337, paramCompletionStatus);
/* 3082 */     if (paramThrowable != null) {
/* 3083 */       localNO_RESOURCES.initCause(paramThrowable);
/*      */     }
/* 3085 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3086 */       Object[] arrayOfObject = null;
/* 3087 */       doLog(Level.FINE, "OMG.piOperationNotSupported5", arrayOfObject, OMGSystemException.class, localNO_RESOURCES);
/*      */     }
/*      */ 
/* 3091 */     return localNO_RESOURCES;
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported5(CompletionStatus paramCompletionStatus) {
/* 3095 */     return piOperationNotSupported5(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported5(Throwable paramThrowable) {
/* 3099 */     return piOperationNotSupported5(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported5() {
/* 3103 */     return piOperationNotSupported5(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported6(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3109 */     NO_RESOURCES localNO_RESOURCES = new NO_RESOURCES(1330446337, paramCompletionStatus);
/* 3110 */     if (paramThrowable != null) {
/* 3111 */       localNO_RESOURCES.initCause(paramThrowable);
/*      */     }
/* 3113 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3114 */       Object[] arrayOfObject = null;
/* 3115 */       doLog(Level.FINE, "OMG.piOperationNotSupported6", arrayOfObject, OMGSystemException.class, localNO_RESOURCES);
/*      */     }
/*      */ 
/* 3119 */     return localNO_RESOURCES;
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported6(CompletionStatus paramCompletionStatus) {
/* 3123 */     return piOperationNotSupported6(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported6(Throwable paramThrowable) {
/* 3127 */     return piOperationNotSupported6(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported6() {
/* 3131 */     return piOperationNotSupported6(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported7(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3137 */     NO_RESOURCES localNO_RESOURCES = new NO_RESOURCES(1330446337, paramCompletionStatus);
/* 3138 */     if (paramThrowable != null) {
/* 3139 */       localNO_RESOURCES.initCause(paramThrowable);
/*      */     }
/* 3141 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3142 */       Object[] arrayOfObject = null;
/* 3143 */       doLog(Level.FINE, "OMG.piOperationNotSupported7", arrayOfObject, OMGSystemException.class, localNO_RESOURCES);
/*      */     }
/*      */ 
/* 3147 */     return localNO_RESOURCES;
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported7(CompletionStatus paramCompletionStatus) {
/* 3151 */     return piOperationNotSupported7(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported7(Throwable paramThrowable) {
/* 3155 */     return piOperationNotSupported7(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported7() {
/* 3159 */     return piOperationNotSupported7(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported8(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3165 */     NO_RESOURCES localNO_RESOURCES = new NO_RESOURCES(1330446337, paramCompletionStatus);
/* 3166 */     if (paramThrowable != null) {
/* 3167 */       localNO_RESOURCES.initCause(paramThrowable);
/*      */     }
/* 3169 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3170 */       Object[] arrayOfObject = null;
/* 3171 */       doLog(Level.FINE, "OMG.piOperationNotSupported8", arrayOfObject, OMGSystemException.class, localNO_RESOURCES);
/*      */     }
/*      */ 
/* 3175 */     return localNO_RESOURCES;
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported8(CompletionStatus paramCompletionStatus) {
/* 3179 */     return piOperationNotSupported8(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported8(Throwable paramThrowable) {
/* 3183 */     return piOperationNotSupported8(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES piOperationNotSupported8() {
/* 3187 */     return piOperationNotSupported8(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES noConnectionPriority(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3193 */     NO_RESOURCES localNO_RESOURCES = new NO_RESOURCES(1330446338, paramCompletionStatus);
/* 3194 */     if (paramThrowable != null) {
/* 3195 */       localNO_RESOURCES.initCause(paramThrowable);
/*      */     }
/* 3197 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3198 */       Object[] arrayOfObject = null;
/* 3199 */       doLog(Level.WARNING, "OMG.noConnectionPriority", arrayOfObject, OMGSystemException.class, localNO_RESOURCES);
/*      */     }
/*      */ 
/* 3203 */     return localNO_RESOURCES;
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES noConnectionPriority(CompletionStatus paramCompletionStatus) {
/* 3207 */     return noConnectionPriority(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES noConnectionPriority(Throwable paramThrowable) {
/* 3211 */     return noConnectionPriority(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_RESOURCES noConnectionPriority() {
/* 3215 */     return noConnectionPriority(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaRb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3225 */     TRANSACTION_ROLLEDBACK localTRANSACTION_ROLLEDBACK = new TRANSACTION_ROLLEDBACK(1330446337, paramCompletionStatus);
/* 3226 */     if (paramThrowable != null) {
/* 3227 */       localTRANSACTION_ROLLEDBACK.initCause(paramThrowable);
/*      */     }
/* 3229 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3230 */       Object[] arrayOfObject = null;
/* 3231 */       doLog(Level.WARNING, "OMG.xaRb", arrayOfObject, OMGSystemException.class, localTRANSACTION_ROLLEDBACK);
/*      */     }
/*      */ 
/* 3235 */     return localTRANSACTION_ROLLEDBACK;
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaRb(CompletionStatus paramCompletionStatus) {
/* 3239 */     return xaRb(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaRb(Throwable paramThrowable) {
/* 3243 */     return xaRb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaRb() {
/* 3247 */     return xaRb(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaNota(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3253 */     TRANSACTION_ROLLEDBACK localTRANSACTION_ROLLEDBACK = new TRANSACTION_ROLLEDBACK(1330446338, paramCompletionStatus);
/* 3254 */     if (paramThrowable != null) {
/* 3255 */       localTRANSACTION_ROLLEDBACK.initCause(paramThrowable);
/*      */     }
/* 3257 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3258 */       Object[] arrayOfObject = null;
/* 3259 */       doLog(Level.WARNING, "OMG.xaNota", arrayOfObject, OMGSystemException.class, localTRANSACTION_ROLLEDBACK);
/*      */     }
/*      */ 
/* 3263 */     return localTRANSACTION_ROLLEDBACK;
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaNota(CompletionStatus paramCompletionStatus) {
/* 3267 */     return xaNota(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaNota(Throwable paramThrowable) {
/* 3271 */     return xaNota(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaNota() {
/* 3275 */     return xaNota(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaEndTrueRollbackDeferred(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3281 */     TRANSACTION_ROLLEDBACK localTRANSACTION_ROLLEDBACK = new TRANSACTION_ROLLEDBACK(1330446339, paramCompletionStatus);
/* 3282 */     if (paramThrowable != null) {
/* 3283 */       localTRANSACTION_ROLLEDBACK.initCause(paramThrowable);
/*      */     }
/* 3285 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3286 */       Object[] arrayOfObject = null;
/* 3287 */       doLog(Level.WARNING, "OMG.xaEndTrueRollbackDeferred", arrayOfObject, OMGSystemException.class, localTRANSACTION_ROLLEDBACK);
/*      */     }
/*      */ 
/* 3291 */     return localTRANSACTION_ROLLEDBACK;
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaEndTrueRollbackDeferred(CompletionStatus paramCompletionStatus) {
/* 3295 */     return xaEndTrueRollbackDeferred(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaEndTrueRollbackDeferred(Throwable paramThrowable) {
/* 3299 */     return xaEndTrueRollbackDeferred(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public TRANSACTION_ROLLEDBACK xaEndTrueRollbackDeferred() {
/* 3303 */     return xaEndTrueRollbackDeferred(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaRequestDiscard(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3313 */     TRANSIENT localTRANSIENT = new TRANSIENT(1330446337, paramCompletionStatus);
/* 3314 */     if (paramThrowable != null) {
/* 3315 */       localTRANSIENT.initCause(paramThrowable);
/*      */     }
/* 3317 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3318 */       Object[] arrayOfObject = null;
/* 3319 */       doLog(Level.WARNING, "OMG.poaRequestDiscard", arrayOfObject, OMGSystemException.class, localTRANSIENT);
/*      */     }
/*      */ 
/* 3323 */     return localTRANSIENT;
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaRequestDiscard(CompletionStatus paramCompletionStatus) {
/* 3327 */     return poaRequestDiscard(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaRequestDiscard(Throwable paramThrowable) {
/* 3331 */     return poaRequestDiscard(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaRequestDiscard() {
/* 3335 */     return poaRequestDiscard(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT noUsableProfile3(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3341 */     TRANSIENT localTRANSIENT = new TRANSIENT(1330446338, paramCompletionStatus);
/* 3342 */     if (paramThrowable != null) {
/* 3343 */       localTRANSIENT.initCause(paramThrowable);
/*      */     }
/* 3345 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3346 */       Object[] arrayOfObject = null;
/* 3347 */       doLog(Level.WARNING, "OMG.noUsableProfile3", arrayOfObject, OMGSystemException.class, localTRANSIENT);
/*      */     }
/*      */ 
/* 3351 */     return localTRANSIENT;
/*      */   }
/*      */ 
/*      */   public TRANSIENT noUsableProfile3(CompletionStatus paramCompletionStatus) {
/* 3355 */     return noUsableProfile3(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT noUsableProfile3(Throwable paramThrowable) {
/* 3359 */     return noUsableProfile3(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public TRANSIENT noUsableProfile3() {
/* 3363 */     return noUsableProfile3(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT requestCancelled(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3369 */     TRANSIENT localTRANSIENT = new TRANSIENT(1330446339, paramCompletionStatus);
/* 3370 */     if (paramThrowable != null) {
/* 3371 */       localTRANSIENT.initCause(paramThrowable);
/*      */     }
/* 3373 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3374 */       Object[] arrayOfObject = null;
/* 3375 */       doLog(Level.WARNING, "OMG.requestCancelled", arrayOfObject, OMGSystemException.class, localTRANSIENT);
/*      */     }
/*      */ 
/* 3379 */     return localTRANSIENT;
/*      */   }
/*      */ 
/*      */   public TRANSIENT requestCancelled(CompletionStatus paramCompletionStatus) {
/* 3383 */     return requestCancelled(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT requestCancelled(Throwable paramThrowable) {
/* 3387 */     return requestCancelled(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public TRANSIENT requestCancelled() {
/* 3391 */     return requestCancelled(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaDestroyed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3397 */     TRANSIENT localTRANSIENT = new TRANSIENT(1330446340, paramCompletionStatus);
/* 3398 */     if (paramThrowable != null) {
/* 3399 */       localTRANSIENT.initCause(paramThrowable);
/*      */     }
/* 3401 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3402 */       Object[] arrayOfObject = null;
/* 3403 */       doLog(Level.WARNING, "OMG.poaDestroyed", arrayOfObject, OMGSystemException.class, localTRANSIENT);
/*      */     }
/*      */ 
/* 3407 */     return localTRANSIENT;
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaDestroyed(CompletionStatus paramCompletionStatus) {
/* 3411 */     return poaDestroyed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaDestroyed(Throwable paramThrowable) {
/* 3415 */     return poaDestroyed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public TRANSIENT poaDestroyed() {
/* 3419 */     return poaDestroyed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST unregisteredValueAsObjref(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3429 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1330446337, paramCompletionStatus);
/* 3430 */     if (paramThrowable != null) {
/* 3431 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 3433 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3434 */       Object[] arrayOfObject = null;
/* 3435 */       doLog(Level.WARNING, "OMG.unregisteredValueAsObjref", arrayOfObject, OMGSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 3439 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST unregisteredValueAsObjref(CompletionStatus paramCompletionStatus) {
/* 3443 */     return unregisteredValueAsObjref(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST unregisteredValueAsObjref(Throwable paramThrowable) {
/* 3447 */     return unregisteredValueAsObjref(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST unregisteredValueAsObjref() {
/* 3451 */     return unregisteredValueAsObjref(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST noObjectAdaptor(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3457 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1330446338, paramCompletionStatus);
/* 3458 */     if (paramThrowable != null) {
/* 3459 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 3461 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3462 */       Object[] arrayOfObject = null;
/* 3463 */       doLog(Level.FINE, "OMG.noObjectAdaptor", arrayOfObject, OMGSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 3467 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST noObjectAdaptor(CompletionStatus paramCompletionStatus) {
/* 3471 */     return noObjectAdaptor(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST noObjectAdaptor(Throwable paramThrowable) {
/* 3475 */     return noObjectAdaptor(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST noObjectAdaptor() {
/* 3479 */     return noObjectAdaptor(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST bioNotAvailable(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3485 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1330446339, paramCompletionStatus);
/* 3486 */     if (paramThrowable != null) {
/* 3487 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 3489 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3490 */       Object[] arrayOfObject = null;
/* 3491 */       doLog(Level.WARNING, "OMG.bioNotAvailable", arrayOfObject, OMGSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 3495 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST bioNotAvailable(CompletionStatus paramCompletionStatus) {
/* 3499 */     return bioNotAvailable(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST bioNotAvailable(Throwable paramThrowable) {
/* 3503 */     return bioNotAvailable(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST bioNotAvailable() {
/* 3507 */     return bioNotAvailable(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST objectAdapterInactive(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3513 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1330446340, paramCompletionStatus);
/* 3514 */     if (paramThrowable != null) {
/* 3515 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 3517 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3518 */       Object[] arrayOfObject = null;
/* 3519 */       doLog(Level.WARNING, "OMG.objectAdapterInactive", arrayOfObject, OMGSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 3523 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST objectAdapterInactive(CompletionStatus paramCompletionStatus) {
/* 3527 */     return objectAdapterInactive(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST objectAdapterInactive(Throwable paramThrowable) {
/* 3531 */     return objectAdapterInactive(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST objectAdapterInactive() {
/* 3535 */     return objectAdapterInactive(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER adapterActivatorException(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 3545 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1330446337, paramCompletionStatus);
/* 3546 */     if (paramThrowable != null) {
/* 3547 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 3549 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3550 */       Object[] arrayOfObject = new Object[2];
/* 3551 */       arrayOfObject[0] = paramObject1;
/* 3552 */       arrayOfObject[1] = paramObject2;
/* 3553 */       doLog(Level.WARNING, "OMG.adapterActivatorException", arrayOfObject, OMGSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 3557 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER adapterActivatorException(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 3561 */     return adapterActivatorException(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER adapterActivatorException(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 3565 */     return adapterActivatorException(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER adapterActivatorException(Object paramObject1, Object paramObject2) {
/* 3569 */     return adapterActivatorException(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER badServantType(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3575 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1330446338, paramCompletionStatus);
/* 3576 */     if (paramThrowable != null) {
/* 3577 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 3579 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3580 */       Object[] arrayOfObject = null;
/* 3581 */       doLog(Level.WARNING, "OMG.badServantType", arrayOfObject, OMGSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 3585 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER badServantType(CompletionStatus paramCompletionStatus) {
/* 3589 */     return badServantType(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER badServantType(Throwable paramThrowable) {
/* 3593 */     return badServantType(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER badServantType() {
/* 3597 */     return badServantType(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noDefaultServant(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3603 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1330446339, paramCompletionStatus);
/* 3604 */     if (paramThrowable != null) {
/* 3605 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 3607 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3608 */       Object[] arrayOfObject = null;
/* 3609 */       doLog(Level.WARNING, "OMG.noDefaultServant", arrayOfObject, OMGSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 3613 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noDefaultServant(CompletionStatus paramCompletionStatus) {
/* 3617 */     return noDefaultServant(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noDefaultServant(Throwable paramThrowable) {
/* 3621 */     return noDefaultServant(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noDefaultServant() {
/* 3625 */     return noDefaultServant(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noServantManager(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3631 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1330446340, paramCompletionStatus);
/* 3632 */     if (paramThrowable != null) {
/* 3633 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 3635 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3636 */       Object[] arrayOfObject = null;
/* 3637 */       doLog(Level.WARNING, "OMG.noServantManager", arrayOfObject, OMGSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 3641 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noServantManager(CompletionStatus paramCompletionStatus) {
/* 3645 */     return noServantManager(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noServantManager(Throwable paramThrowable) {
/* 3649 */     return noServantManager(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noServantManager() {
/* 3653 */     return noServantManager(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER badPolicyIncarnate(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3659 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1330446341, paramCompletionStatus);
/* 3660 */     if (paramThrowable != null) {
/* 3661 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 3663 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3664 */       Object[] arrayOfObject = null;
/* 3665 */       doLog(Level.WARNING, "OMG.badPolicyIncarnate", arrayOfObject, OMGSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 3669 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER badPolicyIncarnate(CompletionStatus paramCompletionStatus) {
/* 3673 */     return badPolicyIncarnate(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER badPolicyIncarnate(Throwable paramThrowable) {
/* 3677 */     return badPolicyIncarnate(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER badPolicyIncarnate() {
/* 3681 */     return badPolicyIncarnate(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER piExcCompEstablished(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3687 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1330446342, paramCompletionStatus);
/* 3688 */     if (paramThrowable != null) {
/* 3689 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 3691 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3692 */       Object[] arrayOfObject = null;
/* 3693 */       doLog(Level.WARNING, "OMG.piExcCompEstablished", arrayOfObject, OMGSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 3697 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER piExcCompEstablished(CompletionStatus paramCompletionStatus) {
/* 3701 */     return piExcCompEstablished(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER piExcCompEstablished(Throwable paramThrowable) {
/* 3705 */     return piExcCompEstablished(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER piExcCompEstablished() {
/* 3709 */     return piExcCompEstablished(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER nullServantReturned(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3715 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1330446343, paramCompletionStatus);
/* 3716 */     if (paramThrowable != null) {
/* 3717 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 3719 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3720 */       Object[] arrayOfObject = null;
/* 3721 */       doLog(Level.FINE, "OMG.nullServantReturned", arrayOfObject, OMGSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 3725 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER nullServantReturned(CompletionStatus paramCompletionStatus) {
/* 3729 */     return nullServantReturned(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER nullServantReturned(Throwable paramThrowable) {
/* 3733 */     return nullServantReturned(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER nullServantReturned() {
/* 3737 */     return nullServantReturned(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownUserException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3747 */     UNKNOWN localUNKNOWN = new UNKNOWN(1330446337, paramCompletionStatus);
/* 3748 */     if (paramThrowable != null) {
/* 3749 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 3751 */     if (this.logger.isLoggable(Level.FINE)) {
/* 3752 */       Object[] arrayOfObject = null;
/* 3753 */       doLog(Level.FINE, "OMG.unknownUserException", arrayOfObject, OMGSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 3757 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownUserException(CompletionStatus paramCompletionStatus) {
/* 3761 */     return unknownUserException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownUserException(Throwable paramThrowable) {
/* 3765 */     return unknownUserException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownUserException() {
/* 3769 */     return unknownUserException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unsupportedSystemException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3775 */     UNKNOWN localUNKNOWN = new UNKNOWN(1330446338, paramCompletionStatus);
/* 3776 */     if (paramThrowable != null) {
/* 3777 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 3779 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3780 */       Object[] arrayOfObject = null;
/* 3781 */       doLog(Level.WARNING, "OMG.unsupportedSystemException", arrayOfObject, OMGSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 3785 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unsupportedSystemException(CompletionStatus paramCompletionStatus) {
/* 3789 */     return unsupportedSystemException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unsupportedSystemException(Throwable paramThrowable) {
/* 3793 */     return unsupportedSystemException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unsupportedSystemException() {
/* 3797 */     return unsupportedSystemException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN piUnknownUserException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3803 */     UNKNOWN localUNKNOWN = new UNKNOWN(1330446339, paramCompletionStatus);
/* 3804 */     if (paramThrowable != null) {
/* 3805 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 3807 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3808 */       Object[] arrayOfObject = null;
/* 3809 */       doLog(Level.WARNING, "OMG.piUnknownUserException", arrayOfObject, OMGSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 3813 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN piUnknownUserException(CompletionStatus paramCompletionStatus) {
/* 3817 */     return piUnknownUserException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN piUnknownUserException(Throwable paramThrowable) {
/* 3821 */     return piUnknownUserException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN piUnknownUserException() {
/* 3825 */     return piUnknownUserException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.logging.OMGSystemException
 * JD-Core Version:    0.6.2
 */