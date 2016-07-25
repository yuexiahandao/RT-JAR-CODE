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
/*      */ import org.omg.CORBA.BAD_TYPECODE;
/*      */ import org.omg.CORBA.COMM_FAILURE;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.DATA_CONVERSION;
/*      */ import org.omg.CORBA.INITIALIZE;
/*      */ import org.omg.CORBA.INTERNAL;
/*      */ import org.omg.CORBA.INV_OBJREF;
/*      */ import org.omg.CORBA.MARSHAL;
/*      */ import org.omg.CORBA.NO_IMPLEMENT;
/*      */ import org.omg.CORBA.OBJECT_NOT_EXIST;
/*      */ import org.omg.CORBA.OBJ_ADAPTER;
/*      */ import org.omg.CORBA.TRANSIENT;
/*      */ import org.omg.CORBA.UNKNOWN;
/*      */ 
/*      */ public class ORBUtilSystemException extends LogWrapperBase
/*      */ {
/*   45 */   private static LogWrapperFactory factory = new LogWrapperFactory()
/*      */   {
/*      */     public LogWrapperBase create(Logger paramAnonymousLogger) {
/*   48 */       return new ORBUtilSystemException(paramAnonymousLogger);
/*      */     }
/*   45 */   };
/*      */   public static final int ADAPTER_ID_NOT_AVAILABLE = 1398079689;
/*      */   public static final int SERVER_ID_NOT_AVAILABLE = 1398079690;
/*      */   public static final int ORB_ID_NOT_AVAILABLE = 1398079691;
/*      */   public static final int OBJECT_ADAPTER_ID_NOT_AVAILABLE = 1398079692;
/*      */   public static final int CONNECTING_SERVANT = 1398079693;
/*      */   public static final int EXTRACT_WRONG_TYPE = 1398079694;
/*      */   public static final int EXTRACT_WRONG_TYPE_LIST = 1398079695;
/*      */   public static final int BAD_STRING_BOUNDS = 1398079696;
/*      */   public static final int INSERT_OBJECT_INCOMPATIBLE = 1398079698;
/*      */   public static final int INSERT_OBJECT_FAILED = 1398079699;
/*      */   public static final int EXTRACT_OBJECT_INCOMPATIBLE = 1398079700;
/*      */   public static final int FIXED_NOT_MATCH = 1398079701;
/*      */   public static final int FIXED_BAD_TYPECODE = 1398079702;
/*      */   public static final int SET_EXCEPTION_CALLED_NULL_ARGS = 1398079711;
/*      */   public static final int SET_EXCEPTION_CALLED_BAD_TYPE = 1398079712;
/*      */   public static final int CONTEXT_CALLED_OUT_OF_ORDER = 1398079713;
/*      */   public static final int BAD_ORB_CONFIGURATOR = 1398079714;
/*      */   public static final int ORB_CONFIGURATOR_ERROR = 1398079715;
/*      */   public static final int ORB_DESTROYED = 1398079716;
/*      */   public static final int NEGATIVE_BOUNDS = 1398079717;
/*      */   public static final int EXTRACT_NOT_INITIALIZED = 1398079718;
/*      */   public static final int EXTRACT_OBJECT_FAILED = 1398079719;
/*      */   public static final int METHOD_NOT_FOUND_IN_TIE = 1398079720;
/*      */   public static final int CLASS_NOT_FOUND1 = 1398079721;
/*      */   public static final int CLASS_NOT_FOUND2 = 1398079722;
/*      */   public static final int CLASS_NOT_FOUND3 = 1398079723;
/*      */   public static final int GET_DELEGATE_SERVANT_NOT_ACTIVE = 1398079724;
/*      */   public static final int GET_DELEGATE_WRONG_POLICY = 1398079725;
/*      */   public static final int SET_DELEGATE_REQUIRES_STUB = 1398079726;
/*      */   public static final int GET_DELEGATE_REQUIRES_STUB = 1398079727;
/*      */   public static final int GET_TYPE_IDS_REQUIRES_STUB = 1398079728;
/*      */   public static final int GET_ORB_REQUIRES_STUB = 1398079729;
/*      */   public static final int CONNECT_REQUIRES_STUB = 1398079730;
/*      */   public static final int IS_LOCAL_REQUIRES_STUB = 1398079731;
/*      */   public static final int REQUEST_REQUIRES_STUB = 1398079732;
/*      */   public static final int BAD_ACTIVATE_TIE_CALL = 1398079733;
/*      */   public static final int IO_EXCEPTION_ON_CLOSE = 1398079734;
/*      */   public static final int NULL_PARAM = 1398079689;
/*      */   public static final int UNABLE_FIND_VALUE_FACTORY = 1398079690;
/*      */   public static final int ABSTRACT_FROM_NON_ABSTRACT = 1398079691;
/*      */   public static final int INVALID_TAGGED_PROFILE = 1398079692;
/*      */   public static final int OBJREF_FROM_FOREIGN_ORB = 1398079693;
/*      */   public static final int LOCAL_OBJECT_NOT_ALLOWED = 1398079694;
/*      */   public static final int NULL_OBJECT_REFERENCE = 1398079695;
/*      */   public static final int COULD_NOT_LOAD_CLASS = 1398079696;
/*      */   public static final int BAD_URL = 1398079697;
/*      */   public static final int FIELD_NOT_FOUND = 1398079698;
/*      */   public static final int ERROR_SETTING_FIELD = 1398079699;
/*      */   public static final int BOUNDS_ERROR_IN_DII_REQUEST = 1398079700;
/*      */   public static final int PERSISTENT_SERVER_INIT_ERROR = 1398079701;
/*      */   public static final int COULD_NOT_CREATE_ARRAY = 1398079702;
/*      */   public static final int COULD_NOT_SET_ARRAY = 1398079703;
/*      */   public static final int ILLEGAL_BOOTSTRAP_OPERATION = 1398079704;
/*      */   public static final int BOOTSTRAP_RUNTIME_EXCEPTION = 1398079705;
/*      */   public static final int BOOTSTRAP_EXCEPTION = 1398079706;
/*      */   public static final int STRING_EXPECTED = 1398079707;
/*      */   public static final int INVALID_TYPECODE_KIND = 1398079708;
/*      */   public static final int SOCKET_FACTORY_AND_CONTACT_INFO_LIST_AT_SAME_TIME = 1398079709;
/*      */   public static final int ACCEPTORS_AND_LEGACY_SOCKET_FACTORY_AT_SAME_TIME = 1398079710;
/*      */   public static final int BAD_ORB_FOR_SERVANT = 1398079711;
/*      */   public static final int INVALID_REQUEST_PARTITIONING_POLICY_VALUE = 1398079712;
/*      */   public static final int INVALID_REQUEST_PARTITIONING_COMPONENT_VALUE = 1398079713;
/*      */   public static final int INVALID_REQUEST_PARTITIONING_ID = 1398079714;
/*      */   public static final int ERROR_IN_SETTING_DYNAMIC_STUB_FACTORY_FACTORY = 1398079715;
/*      */   public static final int DSIMETHOD_NOTCALLED = 1398079689;
/*      */   public static final int ARGUMENTS_CALLED_MULTIPLE = 1398079690;
/*      */   public static final int ARGUMENTS_CALLED_AFTER_EXCEPTION = 1398079691;
/*      */   public static final int ARGUMENTS_CALLED_NULL_ARGS = 1398079692;
/*      */   public static final int ARGUMENTS_NOT_CALLED = 1398079693;
/*      */   public static final int SET_RESULT_CALLED_MULTIPLE = 1398079694;
/*      */   public static final int SET_RESULT_AFTER_EXCEPTION = 1398079695;
/*      */   public static final int SET_RESULT_CALLED_NULL_ARGS = 1398079696;
/*      */   public static final int BAD_REMOTE_TYPECODE = 1398079689;
/*      */   public static final int UNRESOLVED_RECURSIVE_TYPECODE = 1398079690;
/*      */   public static final int CONNECT_FAILURE = 1398079689;
/*      */   public static final int CONNECTION_CLOSE_REBIND = 1398079690;
/*      */   public static final int WRITE_ERROR_SEND = 1398079691;
/*      */   public static final int GET_PROPERTIES_ERROR = 1398079692;
/*      */   public static final int BOOTSTRAP_SERVER_NOT_AVAIL = 1398079693;
/*      */   public static final int INVOKE_ERROR = 1398079694;
/*      */   public static final int DEFAULT_CREATE_SERVER_SOCKET_GIVEN_NON_IIOP_CLEAR_TEXT = 1398079695;
/*      */   public static final int CONNECTION_ABORT = 1398079696;
/*      */   public static final int CONNECTION_REBIND = 1398079697;
/*      */   public static final int RECV_MSG_ERROR = 1398079698;
/*      */   public static final int IOEXCEPTION_WHEN_READING_CONNECTION = 1398079699;
/*      */   public static final int SELECTION_KEY_INVALID = 1398079700;
/*      */   public static final int EXCEPTION_IN_ACCEPT = 1398079701;
/*      */   public static final int SECURITY_EXCEPTION_IN_ACCEPT = 1398079702;
/*      */   public static final int TRANSPORT_READ_TIMEOUT_EXCEEDED = 1398079703;
/*      */   public static final int CREATE_LISTENER_FAILED = 1398079704;
/*      */   public static final int BUFFER_READ_MANAGER_TIMEOUT = 1398079705;
/*      */   public static final int BAD_STRINGIFIED_IOR_LEN = 1398079689;
/*      */   public static final int BAD_STRINGIFIED_IOR = 1398079690;
/*      */   public static final int BAD_MODIFIER = 1398079691;
/*      */   public static final int CODESET_INCOMPATIBLE = 1398079692;
/*      */   public static final int BAD_HEX_DIGIT = 1398079693;
/*      */   public static final int BAD_UNICODE_PAIR = 1398079694;
/*      */   public static final int BTC_RESULT_MORE_THAN_ONE_CHAR = 1398079695;
/*      */   public static final int BAD_CODESETS_FROM_CLIENT = 1398079696;
/*      */   public static final int INVALID_SINGLE_CHAR_CTB = 1398079697;
/*      */   public static final int BAD_GIOP_1_1_CTB = 1398079698;
/*      */   public static final int BAD_SEQUENCE_BOUNDS = 1398079700;
/*      */   public static final int ILLEGAL_SOCKET_FACTORY_TYPE = 1398079701;
/*      */   public static final int BAD_CUSTOM_SOCKET_FACTORY = 1398079702;
/*      */   public static final int FRAGMENT_SIZE_MINIMUM = 1398079703;
/*      */   public static final int FRAGMENT_SIZE_DIV = 1398079704;
/*      */   public static final int ORB_INITIALIZER_FAILURE = 1398079705;
/*      */   public static final int ORB_INITIALIZER_TYPE = 1398079706;
/*      */   public static final int ORB_INITIALREFERENCE_SYNTAX = 1398079707;
/*      */   public static final int ACCEPTOR_INSTANTIATION_FAILURE = 1398079708;
/*      */   public static final int ACCEPTOR_INSTANTIATION_TYPE_FAILURE = 1398079709;
/*      */   public static final int ILLEGAL_CONTACT_INFO_LIST_FACTORY_TYPE = 1398079710;
/*      */   public static final int BAD_CONTACT_INFO_LIST_FACTORY = 1398079711;
/*      */   public static final int ILLEGAL_IOR_TO_SOCKET_INFO_TYPE = 1398079712;
/*      */   public static final int BAD_CUSTOM_IOR_TO_SOCKET_INFO = 1398079713;
/*      */   public static final int ILLEGAL_IIOP_PRIMARY_TO_CONTACT_INFO_TYPE = 1398079714;
/*      */   public static final int BAD_CUSTOM_IIOP_PRIMARY_TO_CONTACT_INFO = 1398079715;
/*      */   public static final int BAD_CORBALOC_STRING = 1398079689;
/*      */   public static final int NO_PROFILE_PRESENT = 1398079690;
/*      */   public static final int CANNOT_CREATE_ORBID_DB = 1398079689;
/*      */   public static final int CANNOT_READ_ORBID_DB = 1398079690;
/*      */   public static final int CANNOT_WRITE_ORBID_DB = 1398079691;
/*      */   public static final int GET_SERVER_PORT_CALLED_BEFORE_ENDPOINTS_INITIALIZED = 1398079692;
/*      */   public static final int PERSISTENT_SERVERPORT_NOT_SET = 1398079693;
/*      */   public static final int PERSISTENT_SERVERID_NOT_SET = 1398079694;
/*      */   public static final int NON_EXISTENT_ORBID = 1398079689;
/*      */   public static final int NO_SERVER_SUBCONTRACT = 1398079690;
/*      */   public static final int SERVER_SC_TEMP_SIZE = 1398079691;
/*      */   public static final int NO_CLIENT_SC_CLASS = 1398079692;
/*      */   public static final int SERVER_SC_NO_IIOP_PROFILE = 1398079693;
/*      */   public static final int GET_SYSTEM_EX_RETURNED_NULL = 1398079694;
/*      */   public static final int PEEKSTRING_FAILED = 1398079695;
/*      */   public static final int GET_LOCAL_HOST_FAILED = 1398079696;
/*      */   public static final int BAD_LOCATE_REQUEST_STATUS = 1398079698;
/*      */   public static final int STRINGIFY_WRITE_ERROR = 1398079699;
/*      */   public static final int BAD_GIOP_REQUEST_TYPE = 1398079700;
/*      */   public static final int ERROR_UNMARSHALING_USEREXC = 1398079701;
/*      */   public static final int RequestDispatcherRegistry_ERROR = 1398079702;
/*      */   public static final int LOCATIONFORWARD_ERROR = 1398079703;
/*      */   public static final int WRONG_CLIENTSC = 1398079704;
/*      */   public static final int BAD_SERVANT_READ_OBJECT = 1398079705;
/*      */   public static final int MULT_IIOP_PROF_NOT_SUPPORTED = 1398079706;
/*      */   public static final int GIOP_MAGIC_ERROR = 1398079708;
/*      */   public static final int GIOP_VERSION_ERROR = 1398079709;
/*      */   public static final int ILLEGAL_REPLY_STATUS = 1398079710;
/*      */   public static final int ILLEGAL_GIOP_MSG_TYPE = 1398079711;
/*      */   public static final int FRAGMENTATION_DISALLOWED = 1398079712;
/*      */   public static final int BAD_REPLYSTATUS = 1398079713;
/*      */   public static final int CTB_CONVERTER_FAILURE = 1398079714;
/*      */   public static final int BTC_CONVERTER_FAILURE = 1398079715;
/*      */   public static final int WCHAR_ARRAY_UNSUPPORTED_ENCODING = 1398079716;
/*      */   public static final int ILLEGAL_TARGET_ADDRESS_DISPOSITION = 1398079717;
/*      */   public static final int NULL_REPLY_IN_GET_ADDR_DISPOSITION = 1398079718;
/*      */   public static final int ORB_TARGET_ADDR_PREFERENCE_IN_EXTRACT_OBJECTKEY_INVALID = 1398079719;
/*      */   public static final int INVALID_ISSTREAMED_TCKIND = 1398079720;
/*      */   public static final int INVALID_JDK1_3_1_PATCH_LEVEL = 1398079721;
/*      */   public static final int SVCCTX_UNMARSHAL_ERROR = 1398079722;
/*      */   public static final int NULL_IOR = 1398079723;
/*      */   public static final int UNSUPPORTED_GIOP_VERSION = 1398079724;
/*      */   public static final int APPLICATION_EXCEPTION_IN_SPECIAL_METHOD = 1398079725;
/*      */   public static final int STATEMENT_NOT_REACHABLE1 = 1398079726;
/*      */   public static final int STATEMENT_NOT_REACHABLE2 = 1398079727;
/*      */   public static final int STATEMENT_NOT_REACHABLE3 = 1398079728;
/*      */   public static final int STATEMENT_NOT_REACHABLE4 = 1398079729;
/*      */   public static final int STATEMENT_NOT_REACHABLE5 = 1398079730;
/*      */   public static final int STATEMENT_NOT_REACHABLE6 = 1398079731;
/*      */   public static final int UNEXPECTED_DII_EXCEPTION = 1398079732;
/*      */   public static final int METHOD_SHOULD_NOT_BE_CALLED = 1398079733;
/*      */   public static final int CANCEL_NOT_SUPPORTED = 1398079734;
/*      */   public static final int EMPTY_STACK_RUN_SERVANT_POST_INVOKE = 1398079735;
/*      */   public static final int PROBLEM_WITH_EXCEPTION_TYPECODE = 1398079736;
/*      */   public static final int ILLEGAL_SUBCONTRACT_ID = 1398079737;
/*      */   public static final int BAD_SYSTEM_EXCEPTION_IN_LOCATE_REPLY = 1398079738;
/*      */   public static final int BAD_SYSTEM_EXCEPTION_IN_REPLY = 1398079739;
/*      */   public static final int BAD_COMPLETION_STATUS_IN_LOCATE_REPLY = 1398079740;
/*      */   public static final int BAD_COMPLETION_STATUS_IN_REPLY = 1398079741;
/*      */   public static final int BADKIND_CANNOT_OCCUR = 1398079742;
/*      */   public static final int ERROR_RESOLVING_ALIAS = 1398079743;
/*      */   public static final int TK_LONG_DOUBLE_NOT_SUPPORTED = 1398079744;
/*      */   public static final int TYPECODE_NOT_SUPPORTED = 1398079745;
/*      */   public static final int BOUNDS_CANNOT_OCCUR = 1398079747;
/*      */   public static final int NUM_INVOCATIONS_ALREADY_ZERO = 1398079749;
/*      */   public static final int ERROR_INIT_BADSERVERIDHANDLER = 1398079750;
/*      */   public static final int NO_TOA = 1398079751;
/*      */   public static final int NO_POA = 1398079752;
/*      */   public static final int INVOCATION_INFO_STACK_EMPTY = 1398079753;
/*      */   public static final int BAD_CODE_SET_STRING = 1398079754;
/*      */   public static final int UNKNOWN_NATIVE_CODESET = 1398079755;
/*      */   public static final int UNKNOWN_CONVERSION_CODE_SET = 1398079756;
/*      */   public static final int INVALID_CODE_SET_NUMBER = 1398079757;
/*      */   public static final int INVALID_CODE_SET_STRING = 1398079758;
/*      */   public static final int INVALID_CTB_CONVERTER_NAME = 1398079759;
/*      */   public static final int INVALID_BTC_CONVERTER_NAME = 1398079760;
/*      */   public static final int COULD_NOT_DUPLICATE_CDR_INPUT_STREAM = 1398079761;
/*      */   public static final int BOOTSTRAP_APPLICATION_EXCEPTION = 1398079762;
/*      */   public static final int DUPLICATE_INDIRECTION_OFFSET = 1398079763;
/*      */   public static final int BAD_MESSAGE_TYPE_FOR_CANCEL = 1398079764;
/*      */   public static final int DUPLICATE_EXCEPTION_DETAIL_MESSAGE = 1398079765;
/*      */   public static final int BAD_EXCEPTION_DETAIL_MESSAGE_SERVICE_CONTEXT_TYPE = 1398079766;
/*      */   public static final int UNEXPECTED_DIRECT_BYTE_BUFFER_WITH_NON_CHANNEL_SOCKET = 1398079767;
/*      */   public static final int UNEXPECTED_NON_DIRECT_BYTE_BUFFER_WITH_CHANNEL_SOCKET = 1398079768;
/*      */   public static final int INVALID_CONTACT_INFO_LIST_ITERATOR_FAILURE_EXCEPTION = 1398079770;
/*      */   public static final int REMARSHAL_WITH_NOWHERE_TO_GO = 1398079771;
/*      */   public static final int EXCEPTION_WHEN_SENDING_CLOSE_CONNECTION = 1398079772;
/*      */   public static final int INVOCATION_ERROR_IN_REFLECTIVE_TIE = 1398079773;
/*      */   public static final int BAD_HELPER_WRITE_METHOD = 1398079774;
/*      */   public static final int BAD_HELPER_READ_METHOD = 1398079775;
/*      */   public static final int BAD_HELPER_ID_METHOD = 1398079776;
/*      */   public static final int WRITE_UNDECLARED_EXCEPTION = 1398079777;
/*      */   public static final int READ_UNDECLARED_EXCEPTION = 1398079778;
/*      */   public static final int UNABLE_TO_SET_SOCKET_FACTORY_ORB = 1398079779;
/*      */   public static final int UNEXPECTED_EXCEPTION = 1398079780;
/*      */   public static final int NO_INVOCATION_HANDLER = 1398079781;
/*      */   public static final int INVALID_BUFF_MGR_STRATEGY = 1398079782;
/*      */   public static final int JAVA_STREAM_INIT_FAILED = 1398079783;
/*      */   public static final int DUPLICATE_ORB_VERSION_SERVICE_CONTEXT = 1398079784;
/*      */   public static final int DUPLICATE_SENDING_CONTEXT_SERVICE_CONTEXT = 1398079785;
/*      */   public static final int WORK_QUEUE_THREAD_INTERRUPTED = 1398079786;
/*      */   public static final int WORKER_THREAD_CREATED = 1398079792;
/*      */   public static final int WORKER_THREAD_THROWABLE_FROM_REQUEST_WORK = 1398079797;
/*      */   public static final int WORKER_THREAD_NOT_NEEDED = 1398079798;
/*      */   public static final int WORKER_THREAD_DO_WORK_THROWABLE = 1398079799;
/*      */   public static final int WORKER_THREAD_CAUGHT_UNEXPECTED_THROWABLE = 1398079800;
/*      */   public static final int WORKER_THREAD_CREATION_FAILURE = 1398079801;
/*      */   public static final int WORKER_THREAD_SET_NAME_FAILURE = 1398079802;
/*      */   public static final int WORK_QUEUE_REQUEST_WORK_NO_WORK_FOUND = 1398079804;
/*      */   public static final int THREAD_POOL_CLOSE_ERROR = 1398079814;
/*      */   public static final int THREAD_GROUP_IS_DESTROYED = 1398079815;
/*      */   public static final int THREAD_GROUP_HAS_ACTIVE_THREADS_IN_CLOSE = 1398079816;
/*      */   public static final int THREAD_GROUP_HAS_SUB_GROUPS_IN_CLOSE = 1398079817;
/*      */   public static final int THREAD_GROUP_DESTROY_FAILED = 1398079818;
/*      */   public static final int INTERRUPTED_JOIN_CALL_WHILE_CLOSING_THREAD_POOL = 1398079819;
/*      */   public static final int CHUNK_OVERFLOW = 1398079689;
/*      */   public static final int UNEXPECTED_EOF = 1398079690;
/*      */   public static final int READ_OBJECT_EXCEPTION = 1398079691;
/*      */   public static final int CHARACTER_OUTOFRANGE = 1398079692;
/*      */   public static final int DSI_RESULT_EXCEPTION = 1398079693;
/*      */   public static final int IIOPINPUTSTREAM_GROW = 1398079694;
/*      */   public static final int END_OF_STREAM = 1398079695;
/*      */   public static final int INVALID_OBJECT_KEY = 1398079696;
/*      */   public static final int MALFORMED_URL = 1398079697;
/*      */   public static final int VALUEHANDLER_READ_ERROR = 1398079698;
/*      */   public static final int VALUEHANDLER_READ_EXCEPTION = 1398079699;
/*      */   public static final int BAD_KIND = 1398079700;
/*      */   public static final int CNFE_READ_CLASS = 1398079701;
/*      */   public static final int BAD_REP_ID_INDIRECTION = 1398079702;
/*      */   public static final int BAD_CODEBASE_INDIRECTION = 1398079703;
/*      */   public static final int UNKNOWN_CODESET = 1398079704;
/*      */   public static final int WCHAR_DATA_IN_GIOP_1_0 = 1398079705;
/*      */   public static final int NEGATIVE_STRING_LENGTH = 1398079706;
/*      */   public static final int EXPECTED_TYPE_NULL_AND_NO_REP_ID = 1398079707;
/*      */   public static final int READ_VALUE_AND_NO_REP_ID = 1398079708;
/*      */   public static final int UNEXPECTED_ENCLOSING_VALUETYPE = 1398079710;
/*      */   public static final int POSITIVE_END_TAG = 1398079711;
/*      */   public static final int NULL_OUT_CALL = 1398079712;
/*      */   public static final int WRITE_LOCAL_OBJECT = 1398079713;
/*      */   public static final int BAD_INSERTOBJ_PARAM = 1398079714;
/*      */   public static final int CUSTOM_WRAPPER_WITH_CODEBASE = 1398079715;
/*      */   public static final int CUSTOM_WRAPPER_INDIRECTION = 1398079716;
/*      */   public static final int CUSTOM_WRAPPER_NOT_SINGLE_REPID = 1398079717;
/*      */   public static final int BAD_VALUE_TAG = 1398079718;
/*      */   public static final int BAD_TYPECODE_FOR_CUSTOM_VALUE = 1398079719;
/*      */   public static final int ERROR_INVOKING_HELPER_WRITE = 1398079720;
/*      */   public static final int BAD_DIGIT_IN_FIXED = 1398079721;
/*      */   public static final int REF_TYPE_INDIR_TYPE = 1398079722;
/*      */   public static final int BAD_RESERVED_LENGTH = 1398079723;
/*      */   public static final int NULL_NOT_ALLOWED = 1398079724;
/*      */   public static final int UNION_DISCRIMINATOR_ERROR = 1398079726;
/*      */   public static final int CANNOT_MARSHAL_NATIVE = 1398079727;
/*      */   public static final int CANNOT_MARSHAL_BAD_TCKIND = 1398079728;
/*      */   public static final int INVALID_INDIRECTION = 1398079729;
/*      */   public static final int INDIRECTION_NOT_FOUND = 1398079730;
/*      */   public static final int RECURSIVE_TYPECODE_ERROR = 1398079731;
/*      */   public static final int INVALID_SIMPLE_TYPECODE = 1398079732;
/*      */   public static final int INVALID_COMPLEX_TYPECODE = 1398079733;
/*      */   public static final int INVALID_TYPECODE_KIND_MARSHAL = 1398079734;
/*      */   public static final int UNEXPECTED_UNION_DEFAULT = 1398079735;
/*      */   public static final int ILLEGAL_UNION_DISCRIMINATOR_TYPE = 1398079736;
/*      */   public static final int COULD_NOT_SKIP_BYTES = 1398079737;
/*      */   public static final int BAD_CHUNK_LENGTH = 1398079738;
/*      */   public static final int UNABLE_TO_LOCATE_REP_ID_ARRAY = 1398079739;
/*      */   public static final int BAD_FIXED = 1398079740;
/*      */   public static final int READ_OBJECT_LOAD_CLASS_FAILURE = 1398079741;
/*      */   public static final int COULD_NOT_INSTANTIATE_HELPER = 1398079742;
/*      */   public static final int BAD_TOA_OAID = 1398079743;
/*      */   public static final int COULD_NOT_INVOKE_HELPER_READ_METHOD = 1398079744;
/*      */   public static final int COULD_NOT_FIND_CLASS = 1398079745;
/*      */   public static final int BAD_ARGUMENTS_NVLIST = 1398079746;
/*      */   public static final int STUB_CREATE_ERROR = 1398079747;
/*      */   public static final int JAVA_SERIALIZATION_EXCEPTION = 1398079748;
/*      */   public static final int GENERIC_NO_IMPL = 1398079689;
/*      */   public static final int CONTEXT_NOT_IMPLEMENTED = 1398079690;
/*      */   public static final int GETINTERFACE_NOT_IMPLEMENTED = 1398079691;
/*      */   public static final int SEND_DEFERRED_NOTIMPLEMENTED = 1398079692;
/*      */   public static final int LONG_DOUBLE_NOT_IMPLEMENTED = 1398079693;
/*      */   public static final int NO_SERVER_SC_IN_DISPATCH = 1398079689;
/*      */   public static final int ORB_CONNECT_ERROR = 1398079690;
/*      */   public static final int ADAPTER_INACTIVE_IN_ACTIVATION = 1398079691;
/*      */   public static final int LOCATE_UNKNOWN_OBJECT = 1398079689;
/*      */   public static final int BAD_SERVER_ID = 1398079690;
/*      */   public static final int BAD_SKELETON = 1398079691;
/*      */   public static final int SERVANT_NOT_FOUND = 1398079692;
/*      */   public static final int NO_OBJECT_ADAPTER_FACTORY = 1398079693;
/*      */   public static final int BAD_ADAPTER_ID = 1398079694;
/*      */   public static final int DYN_ANY_DESTROYED = 1398079695;
/*      */   public static final int REQUEST_CANCELED = 1398079689;
/*      */   public static final int UNKNOWN_CORBA_EXC = 1398079689;
/*      */   public static final int RUNTIMEEXCEPTION = 1398079690;
/*      */   public static final int UNKNOWN_SERVER_ERROR = 1398079691;
/*      */   public static final int UNKNOWN_DSI_SYSEX = 1398079692;
/*      */   public static final int UNKNOWN_SYSEX = 1398079693;
/*      */   public static final int WRONG_INTERFACE_DEF = 1398079694;
/*      */   public static final int NO_INTERFACE_DEF_STUB = 1398079695;
/*      */   public static final int UNKNOWN_EXCEPTION_IN_DISPATCH = 1398079697;
/*      */ 
/*      */   public ORBUtilSystemException(Logger paramLogger)
/*      */   {
/*   42 */     super(paramLogger);
/*      */   }
/*      */ 
/*      */   public static ORBUtilSystemException get(ORB paramORB, String paramString)
/*      */   {
/*   54 */     ORBUtilSystemException localORBUtilSystemException = (ORBUtilSystemException)paramORB.getLogWrapper(paramString, "ORBUTIL", factory);
/*      */ 
/*   57 */     return localORBUtilSystemException;
/*      */   }
/*      */ 
/*      */   public static ORBUtilSystemException get(String paramString)
/*      */   {
/*   62 */     ORBUtilSystemException localORBUtilSystemException = (ORBUtilSystemException)ORB.staticGetLogWrapper(paramString, "ORBUTIL", factory);
/*      */ 
/*   65 */     return localORBUtilSystemException;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION adapterIdNotAvailable(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*   75 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079689, paramCompletionStatus);
/*   76 */     if (paramThrowable != null) {
/*   77 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*   79 */     if (this.logger.isLoggable(Level.WARNING)) {
/*   80 */       Object[] arrayOfObject = null;
/*   81 */       doLog(Level.WARNING, "ORBUTIL.adapterIdNotAvailable", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*   85 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION adapterIdNotAvailable(CompletionStatus paramCompletionStatus) {
/*   89 */     return adapterIdNotAvailable(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION adapterIdNotAvailable(Throwable paramThrowable) {
/*   93 */     return adapterIdNotAvailable(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION adapterIdNotAvailable() {
/*   97 */     return adapterIdNotAvailable(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION serverIdNotAvailable(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  103 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079690, paramCompletionStatus);
/*  104 */     if (paramThrowable != null) {
/*  105 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  107 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  108 */       Object[] arrayOfObject = null;
/*  109 */       doLog(Level.WARNING, "ORBUTIL.serverIdNotAvailable", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  113 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION serverIdNotAvailable(CompletionStatus paramCompletionStatus) {
/*  117 */     return serverIdNotAvailable(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION serverIdNotAvailable(Throwable paramThrowable) {
/*  121 */     return serverIdNotAvailable(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION serverIdNotAvailable() {
/*  125 */     return serverIdNotAvailable(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbIdNotAvailable(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  131 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079691, paramCompletionStatus);
/*  132 */     if (paramThrowable != null) {
/*  133 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  135 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  136 */       Object[] arrayOfObject = null;
/*  137 */       doLog(Level.WARNING, "ORBUTIL.orbIdNotAvailable", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  141 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbIdNotAvailable(CompletionStatus paramCompletionStatus) {
/*  145 */     return orbIdNotAvailable(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbIdNotAvailable(Throwable paramThrowable) {
/*  149 */     return orbIdNotAvailable(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbIdNotAvailable() {
/*  153 */     return orbIdNotAvailable(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION objectAdapterIdNotAvailable(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  159 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079692, paramCompletionStatus);
/*  160 */     if (paramThrowable != null) {
/*  161 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  163 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  164 */       Object[] arrayOfObject = null;
/*  165 */       doLog(Level.WARNING, "ORBUTIL.objectAdapterIdNotAvailable", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  169 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION objectAdapterIdNotAvailable(CompletionStatus paramCompletionStatus) {
/*  173 */     return objectAdapterIdNotAvailable(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION objectAdapterIdNotAvailable(Throwable paramThrowable) {
/*  177 */     return objectAdapterIdNotAvailable(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION objectAdapterIdNotAvailable() {
/*  181 */     return objectAdapterIdNotAvailable(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION connectingServant(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  187 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079693, paramCompletionStatus);
/*  188 */     if (paramThrowable != null) {
/*  189 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  191 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  192 */       Object[] arrayOfObject = null;
/*  193 */       doLog(Level.WARNING, "ORBUTIL.connectingServant", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  197 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION connectingServant(CompletionStatus paramCompletionStatus) {
/*  201 */     return connectingServant(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION connectingServant(Throwable paramThrowable) {
/*  205 */     return connectingServant(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION connectingServant() {
/*  209 */     return connectingServant(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractWrongType(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/*  215 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079694, paramCompletionStatus);
/*  216 */     if (paramThrowable != null) {
/*  217 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  219 */     if (this.logger.isLoggable(Level.FINE)) {
/*  220 */       Object[] arrayOfObject = new Object[2];
/*  221 */       arrayOfObject[0] = paramObject1;
/*  222 */       arrayOfObject[1] = paramObject2;
/*  223 */       doLog(Level.FINE, "ORBUTIL.extractWrongType", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  227 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractWrongType(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/*  231 */     return extractWrongType(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractWrongType(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/*  235 */     return extractWrongType(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractWrongType(Object paramObject1, Object paramObject2) {
/*  239 */     return extractWrongType(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractWrongTypeList(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/*  245 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079695, paramCompletionStatus);
/*  246 */     if (paramThrowable != null) {
/*  247 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  249 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  250 */       Object[] arrayOfObject = new Object[2];
/*  251 */       arrayOfObject[0] = paramObject1;
/*  252 */       arrayOfObject[1] = paramObject2;
/*  253 */       doLog(Level.WARNING, "ORBUTIL.extractWrongTypeList", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  257 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractWrongTypeList(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/*  261 */     return extractWrongTypeList(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractWrongTypeList(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/*  265 */     return extractWrongTypeList(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractWrongTypeList(Object paramObject1, Object paramObject2) {
/*  269 */     return extractWrongTypeList(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badStringBounds(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/*  275 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079696, paramCompletionStatus);
/*  276 */     if (paramThrowable != null) {
/*  277 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  279 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  280 */       Object[] arrayOfObject = new Object[2];
/*  281 */       arrayOfObject[0] = paramObject1;
/*  282 */       arrayOfObject[1] = paramObject2;
/*  283 */       doLog(Level.WARNING, "ORBUTIL.badStringBounds", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  287 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badStringBounds(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/*  291 */     return badStringBounds(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badStringBounds(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/*  295 */     return badStringBounds(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badStringBounds(Object paramObject1, Object paramObject2) {
/*  299 */     return badStringBounds(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION insertObjectIncompatible(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  305 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079698, paramCompletionStatus);
/*  306 */     if (paramThrowable != null) {
/*  307 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  309 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  310 */       Object[] arrayOfObject = null;
/*  311 */       doLog(Level.WARNING, "ORBUTIL.insertObjectIncompatible", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  315 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION insertObjectIncompatible(CompletionStatus paramCompletionStatus) {
/*  319 */     return insertObjectIncompatible(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION insertObjectIncompatible(Throwable paramThrowable) {
/*  323 */     return insertObjectIncompatible(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION insertObjectIncompatible() {
/*  327 */     return insertObjectIncompatible(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION insertObjectFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  333 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079699, paramCompletionStatus);
/*  334 */     if (paramThrowable != null) {
/*  335 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  337 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  338 */       Object[] arrayOfObject = null;
/*  339 */       doLog(Level.WARNING, "ORBUTIL.insertObjectFailed", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  343 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION insertObjectFailed(CompletionStatus paramCompletionStatus) {
/*  347 */     return insertObjectFailed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION insertObjectFailed(Throwable paramThrowable) {
/*  351 */     return insertObjectFailed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION insertObjectFailed() {
/*  355 */     return insertObjectFailed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractObjectIncompatible(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  361 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079700, paramCompletionStatus);
/*  362 */     if (paramThrowable != null) {
/*  363 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  365 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  366 */       Object[] arrayOfObject = null;
/*  367 */       doLog(Level.WARNING, "ORBUTIL.extractObjectIncompatible", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  371 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractObjectIncompatible(CompletionStatus paramCompletionStatus) {
/*  375 */     return extractObjectIncompatible(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractObjectIncompatible(Throwable paramThrowable) {
/*  379 */     return extractObjectIncompatible(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractObjectIncompatible() {
/*  383 */     return extractObjectIncompatible(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION fixedNotMatch(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  389 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079701, paramCompletionStatus);
/*  390 */     if (paramThrowable != null) {
/*  391 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  393 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  394 */       Object[] arrayOfObject = null;
/*  395 */       doLog(Level.WARNING, "ORBUTIL.fixedNotMatch", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  399 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION fixedNotMatch(CompletionStatus paramCompletionStatus) {
/*  403 */     return fixedNotMatch(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION fixedNotMatch(Throwable paramThrowable) {
/*  407 */     return fixedNotMatch(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION fixedNotMatch() {
/*  411 */     return fixedNotMatch(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION fixedBadTypecode(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  417 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079702, paramCompletionStatus);
/*  418 */     if (paramThrowable != null) {
/*  419 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  421 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  422 */       Object[] arrayOfObject = null;
/*  423 */       doLog(Level.WARNING, "ORBUTIL.fixedBadTypecode", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  427 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION fixedBadTypecode(CompletionStatus paramCompletionStatus) {
/*  431 */     return fixedBadTypecode(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION fixedBadTypecode(Throwable paramThrowable) {
/*  435 */     return fixedBadTypecode(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION fixedBadTypecode() {
/*  439 */     return fixedBadTypecode(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setExceptionCalledNullArgs(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  445 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079711, paramCompletionStatus);
/*  446 */     if (paramThrowable != null) {
/*  447 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  449 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  450 */       Object[] arrayOfObject = null;
/*  451 */       doLog(Level.WARNING, "ORBUTIL.setExceptionCalledNullArgs", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  455 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setExceptionCalledNullArgs(CompletionStatus paramCompletionStatus) {
/*  459 */     return setExceptionCalledNullArgs(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setExceptionCalledNullArgs(Throwable paramThrowable) {
/*  463 */     return setExceptionCalledNullArgs(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setExceptionCalledNullArgs() {
/*  467 */     return setExceptionCalledNullArgs(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setExceptionCalledBadType(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  473 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079712, paramCompletionStatus);
/*  474 */     if (paramThrowable != null) {
/*  475 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  477 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  478 */       Object[] arrayOfObject = null;
/*  479 */       doLog(Level.WARNING, "ORBUTIL.setExceptionCalledBadType", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  483 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setExceptionCalledBadType(CompletionStatus paramCompletionStatus) {
/*  487 */     return setExceptionCalledBadType(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setExceptionCalledBadType(Throwable paramThrowable) {
/*  491 */     return setExceptionCalledBadType(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setExceptionCalledBadType() {
/*  495 */     return setExceptionCalledBadType(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION contextCalledOutOfOrder(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  501 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079713, paramCompletionStatus);
/*  502 */     if (paramThrowable != null) {
/*  503 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  505 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  506 */       Object[] arrayOfObject = null;
/*  507 */       doLog(Level.WARNING, "ORBUTIL.contextCalledOutOfOrder", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  511 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION contextCalledOutOfOrder(CompletionStatus paramCompletionStatus) {
/*  515 */     return contextCalledOutOfOrder(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION contextCalledOutOfOrder(Throwable paramThrowable) {
/*  519 */     return contextCalledOutOfOrder(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION contextCalledOutOfOrder() {
/*  523 */     return contextCalledOutOfOrder(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badOrbConfigurator(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/*  529 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079714, paramCompletionStatus);
/*  530 */     if (paramThrowable != null) {
/*  531 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  533 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  534 */       Object[] arrayOfObject = new Object[1];
/*  535 */       arrayOfObject[0] = paramObject;
/*  536 */       doLog(Level.WARNING, "ORBUTIL.badOrbConfigurator", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  540 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badOrbConfigurator(CompletionStatus paramCompletionStatus, Object paramObject) {
/*  544 */     return badOrbConfigurator(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badOrbConfigurator(Throwable paramThrowable, Object paramObject) {
/*  548 */     return badOrbConfigurator(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badOrbConfigurator(Object paramObject) {
/*  552 */     return badOrbConfigurator(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbConfiguratorError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  558 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079715, paramCompletionStatus);
/*  559 */     if (paramThrowable != null) {
/*  560 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  562 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  563 */       Object[] arrayOfObject = null;
/*  564 */       doLog(Level.WARNING, "ORBUTIL.orbConfiguratorError", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  568 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbConfiguratorError(CompletionStatus paramCompletionStatus) {
/*  572 */     return orbConfiguratorError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbConfiguratorError(Throwable paramThrowable) {
/*  576 */     return orbConfiguratorError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbConfiguratorError() {
/*  580 */     return orbConfiguratorError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbDestroyed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  586 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079716, paramCompletionStatus);
/*  587 */     if (paramThrowable != null) {
/*  588 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  590 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  591 */       Object[] arrayOfObject = null;
/*  592 */       doLog(Level.WARNING, "ORBUTIL.orbDestroyed", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  596 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbDestroyed(CompletionStatus paramCompletionStatus) {
/*  600 */     return orbDestroyed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbDestroyed(Throwable paramThrowable) {
/*  604 */     return orbDestroyed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION orbDestroyed() {
/*  608 */     return orbDestroyed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION negativeBounds(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  614 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079717, paramCompletionStatus);
/*  615 */     if (paramThrowable != null) {
/*  616 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  618 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  619 */       Object[] arrayOfObject = null;
/*  620 */       doLog(Level.WARNING, "ORBUTIL.negativeBounds", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  624 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION negativeBounds(CompletionStatus paramCompletionStatus) {
/*  628 */     return negativeBounds(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION negativeBounds(Throwable paramThrowable) {
/*  632 */     return negativeBounds(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION negativeBounds() {
/*  636 */     return negativeBounds(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractNotInitialized(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  642 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079718, paramCompletionStatus);
/*  643 */     if (paramThrowable != null) {
/*  644 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  646 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  647 */       Object[] arrayOfObject = null;
/*  648 */       doLog(Level.WARNING, "ORBUTIL.extractNotInitialized", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  652 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractNotInitialized(CompletionStatus paramCompletionStatus) {
/*  656 */     return extractNotInitialized(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractNotInitialized(Throwable paramThrowable) {
/*  660 */     return extractNotInitialized(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractNotInitialized() {
/*  664 */     return extractNotInitialized(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractObjectFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  670 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079719, paramCompletionStatus);
/*  671 */     if (paramThrowable != null) {
/*  672 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  674 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  675 */       Object[] arrayOfObject = null;
/*  676 */       doLog(Level.WARNING, "ORBUTIL.extractObjectFailed", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  680 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractObjectFailed(CompletionStatus paramCompletionStatus) {
/*  684 */     return extractObjectFailed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractObjectFailed(Throwable paramThrowable) {
/*  688 */     return extractObjectFailed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION extractObjectFailed() {
/*  692 */     return extractObjectFailed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION methodNotFoundInTie(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/*  698 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079720, paramCompletionStatus);
/*  699 */     if (paramThrowable != null) {
/*  700 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  702 */     if (this.logger.isLoggable(Level.FINE)) {
/*  703 */       Object[] arrayOfObject = new Object[2];
/*  704 */       arrayOfObject[0] = paramObject1;
/*  705 */       arrayOfObject[1] = paramObject2;
/*  706 */       doLog(Level.FINE, "ORBUTIL.methodNotFoundInTie", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  710 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION methodNotFoundInTie(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/*  714 */     return methodNotFoundInTie(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION methodNotFoundInTie(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/*  718 */     return methodNotFoundInTie(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION methodNotFoundInTie(Object paramObject1, Object paramObject2) {
/*  722 */     return methodNotFoundInTie(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound1(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/*  728 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079721, paramCompletionStatus);
/*  729 */     if (paramThrowable != null) {
/*  730 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  732 */     if (this.logger.isLoggable(Level.FINE)) {
/*  733 */       Object[] arrayOfObject = new Object[1];
/*  734 */       arrayOfObject[0] = paramObject;
/*  735 */       doLog(Level.FINE, "ORBUTIL.classNotFound1", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  739 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound1(CompletionStatus paramCompletionStatus, Object paramObject) {
/*  743 */     return classNotFound1(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound1(Throwable paramThrowable, Object paramObject) {
/*  747 */     return classNotFound1(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound1(Object paramObject) {
/*  751 */     return classNotFound1(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound2(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/*  757 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079722, paramCompletionStatus);
/*  758 */     if (paramThrowable != null) {
/*  759 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  761 */     if (this.logger.isLoggable(Level.FINE)) {
/*  762 */       Object[] arrayOfObject = new Object[1];
/*  763 */       arrayOfObject[0] = paramObject;
/*  764 */       doLog(Level.FINE, "ORBUTIL.classNotFound2", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  768 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound2(CompletionStatus paramCompletionStatus, Object paramObject) {
/*  772 */     return classNotFound2(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound2(Throwable paramThrowable, Object paramObject) {
/*  776 */     return classNotFound2(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound2(Object paramObject) {
/*  780 */     return classNotFound2(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound3(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/*  786 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079723, paramCompletionStatus);
/*  787 */     if (paramThrowable != null) {
/*  788 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  790 */     if (this.logger.isLoggable(Level.FINE)) {
/*  791 */       Object[] arrayOfObject = new Object[1];
/*  792 */       arrayOfObject[0] = paramObject;
/*  793 */       doLog(Level.FINE, "ORBUTIL.classNotFound3", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  797 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound3(CompletionStatus paramCompletionStatus, Object paramObject) {
/*  801 */     return classNotFound3(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound3(Throwable paramThrowable, Object paramObject) {
/*  805 */     return classNotFound3(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION classNotFound3(Object paramObject) {
/*  809 */     return classNotFound3(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateServantNotActive(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  815 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079724, paramCompletionStatus);
/*  816 */     if (paramThrowable != null) {
/*  817 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  819 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  820 */       Object[] arrayOfObject = null;
/*  821 */       doLog(Level.WARNING, "ORBUTIL.getDelegateServantNotActive", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  825 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateServantNotActive(CompletionStatus paramCompletionStatus) {
/*  829 */     return getDelegateServantNotActive(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateServantNotActive(Throwable paramThrowable) {
/*  833 */     return getDelegateServantNotActive(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateServantNotActive() {
/*  837 */     return getDelegateServantNotActive(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateWrongPolicy(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  843 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079725, paramCompletionStatus);
/*  844 */     if (paramThrowable != null) {
/*  845 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  847 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  848 */       Object[] arrayOfObject = null;
/*  849 */       doLog(Level.WARNING, "ORBUTIL.getDelegateWrongPolicy", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  853 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateWrongPolicy(CompletionStatus paramCompletionStatus) {
/*  857 */     return getDelegateWrongPolicy(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateWrongPolicy(Throwable paramThrowable) {
/*  861 */     return getDelegateWrongPolicy(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateWrongPolicy() {
/*  865 */     return getDelegateWrongPolicy(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setDelegateRequiresStub(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  871 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079726, paramCompletionStatus);
/*  872 */     if (paramThrowable != null) {
/*  873 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  875 */     if (this.logger.isLoggable(Level.FINE)) {
/*  876 */       Object[] arrayOfObject = null;
/*  877 */       doLog(Level.FINE, "ORBUTIL.setDelegateRequiresStub", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  881 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setDelegateRequiresStub(CompletionStatus paramCompletionStatus) {
/*  885 */     return setDelegateRequiresStub(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setDelegateRequiresStub(Throwable paramThrowable) {
/*  889 */     return setDelegateRequiresStub(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION setDelegateRequiresStub() {
/*  893 */     return setDelegateRequiresStub(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateRequiresStub(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  899 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079727, paramCompletionStatus);
/*  900 */     if (paramThrowable != null) {
/*  901 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  903 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  904 */       Object[] arrayOfObject = null;
/*  905 */       doLog(Level.WARNING, "ORBUTIL.getDelegateRequiresStub", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  909 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateRequiresStub(CompletionStatus paramCompletionStatus) {
/*  913 */     return getDelegateRequiresStub(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateRequiresStub(Throwable paramThrowable) {
/*  917 */     return getDelegateRequiresStub(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getDelegateRequiresStub() {
/*  921 */     return getDelegateRequiresStub(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getTypeIdsRequiresStub(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  927 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079728, paramCompletionStatus);
/*  928 */     if (paramThrowable != null) {
/*  929 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  931 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  932 */       Object[] arrayOfObject = null;
/*  933 */       doLog(Level.WARNING, "ORBUTIL.getTypeIdsRequiresStub", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  937 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getTypeIdsRequiresStub(CompletionStatus paramCompletionStatus) {
/*  941 */     return getTypeIdsRequiresStub(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getTypeIdsRequiresStub(Throwable paramThrowable) {
/*  945 */     return getTypeIdsRequiresStub(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getTypeIdsRequiresStub() {
/*  949 */     return getTypeIdsRequiresStub(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getOrbRequiresStub(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  955 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079729, paramCompletionStatus);
/*  956 */     if (paramThrowable != null) {
/*  957 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  959 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  960 */       Object[] arrayOfObject = null;
/*  961 */       doLog(Level.WARNING, "ORBUTIL.getOrbRequiresStub", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  965 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getOrbRequiresStub(CompletionStatus paramCompletionStatus) {
/*  969 */     return getOrbRequiresStub(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getOrbRequiresStub(Throwable paramThrowable) {
/*  973 */     return getOrbRequiresStub(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION getOrbRequiresStub() {
/*  977 */     return getOrbRequiresStub(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION connectRequiresStub(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/*  983 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079730, paramCompletionStatus);
/*  984 */     if (paramThrowable != null) {
/*  985 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/*  987 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  988 */       Object[] arrayOfObject = null;
/*  989 */       doLog(Level.WARNING, "ORBUTIL.connectRequiresStub", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/*  993 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION connectRequiresStub(CompletionStatus paramCompletionStatus) {
/*  997 */     return connectRequiresStub(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION connectRequiresStub(Throwable paramThrowable) {
/* 1001 */     return connectRequiresStub(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION connectRequiresStub() {
/* 1005 */     return connectRequiresStub(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION isLocalRequiresStub(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1011 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079731, paramCompletionStatus);
/* 1012 */     if (paramThrowable != null) {
/* 1013 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/* 1015 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1016 */       Object[] arrayOfObject = null;
/* 1017 */       doLog(Level.WARNING, "ORBUTIL.isLocalRequiresStub", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/* 1021 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION isLocalRequiresStub(CompletionStatus paramCompletionStatus) {
/* 1025 */     return isLocalRequiresStub(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION isLocalRequiresStub(Throwable paramThrowable) {
/* 1029 */     return isLocalRequiresStub(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION isLocalRequiresStub() {
/* 1033 */     return isLocalRequiresStub(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION requestRequiresStub(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1039 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079732, paramCompletionStatus);
/* 1040 */     if (paramThrowable != null) {
/* 1041 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/* 1043 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1044 */       Object[] arrayOfObject = null;
/* 1045 */       doLog(Level.WARNING, "ORBUTIL.requestRequiresStub", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/* 1049 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION requestRequiresStub(CompletionStatus paramCompletionStatus) {
/* 1053 */     return requestRequiresStub(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION requestRequiresStub(Throwable paramThrowable) {
/* 1057 */     return requestRequiresStub(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION requestRequiresStub() {
/* 1061 */     return requestRequiresStub(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badActivateTieCall(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1067 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079733, paramCompletionStatus);
/* 1068 */     if (paramThrowable != null) {
/* 1069 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/* 1071 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1072 */       Object[] arrayOfObject = null;
/* 1073 */       doLog(Level.WARNING, "ORBUTIL.badActivateTieCall", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/* 1077 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badActivateTieCall(CompletionStatus paramCompletionStatus) {
/* 1081 */     return badActivateTieCall(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badActivateTieCall(Throwable paramThrowable) {
/* 1085 */     return badActivateTieCall(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION badActivateTieCall() {
/* 1089 */     return badActivateTieCall(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION ioExceptionOnClose(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1095 */     BAD_OPERATION localBAD_OPERATION = new BAD_OPERATION(1398079734, paramCompletionStatus);
/* 1096 */     if (paramThrowable != null) {
/* 1097 */       localBAD_OPERATION.initCause(paramThrowable);
/*      */     }
/* 1099 */     if (this.logger.isLoggable(Level.FINE)) {
/* 1100 */       Object[] arrayOfObject = null;
/* 1101 */       doLog(Level.FINE, "ORBUTIL.ioExceptionOnClose", arrayOfObject, ORBUtilSystemException.class, localBAD_OPERATION);
/*      */     }
/*      */ 
/* 1105 */     return localBAD_OPERATION;
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION ioExceptionOnClose(CompletionStatus paramCompletionStatus) {
/* 1109 */     return ioExceptionOnClose(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION ioExceptionOnClose(Throwable paramThrowable) {
/* 1113 */     return ioExceptionOnClose(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_OPERATION ioExceptionOnClose() {
/* 1117 */     return ioExceptionOnClose(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nullParam(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1127 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079689, paramCompletionStatus);
/* 1128 */     if (paramThrowable != null) {
/* 1129 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1131 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1132 */       Object[] arrayOfObject = null;
/* 1133 */       doLog(Level.WARNING, "ORBUTIL.nullParam", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1137 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nullParam(CompletionStatus paramCompletionStatus) {
/* 1141 */     return nullParam(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nullParam(Throwable paramThrowable) {
/* 1145 */     return nullParam(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nullParam() {
/* 1149 */     return nullParam(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unableFindValueFactory(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1155 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079690, paramCompletionStatus);
/* 1156 */     if (paramThrowable != null) {
/* 1157 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1159 */     if (this.logger.isLoggable(Level.FINE)) {
/* 1160 */       Object[] arrayOfObject = null;
/* 1161 */       doLog(Level.FINE, "ORBUTIL.unableFindValueFactory", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1165 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unableFindValueFactory(CompletionStatus paramCompletionStatus) {
/* 1169 */     return unableFindValueFactory(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unableFindValueFactory(Throwable paramThrowable) {
/* 1173 */     return unableFindValueFactory(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM unableFindValueFactory() {
/* 1177 */     return unableFindValueFactory(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM abstractFromNonAbstract(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1183 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079691, paramCompletionStatus);
/* 1184 */     if (paramThrowable != null) {
/* 1185 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1187 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1188 */       Object[] arrayOfObject = null;
/* 1189 */       doLog(Level.WARNING, "ORBUTIL.abstractFromNonAbstract", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1193 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM abstractFromNonAbstract(CompletionStatus paramCompletionStatus) {
/* 1197 */     return abstractFromNonAbstract(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM abstractFromNonAbstract(Throwable paramThrowable) {
/* 1201 */     return abstractFromNonAbstract(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM abstractFromNonAbstract() {
/* 1205 */     return abstractFromNonAbstract(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidTaggedProfile(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1211 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079692, paramCompletionStatus);
/* 1212 */     if (paramThrowable != null) {
/* 1213 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1215 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1216 */       Object[] arrayOfObject = null;
/* 1217 */       doLog(Level.WARNING, "ORBUTIL.invalidTaggedProfile", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1221 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidTaggedProfile(CompletionStatus paramCompletionStatus) {
/* 1225 */     return invalidTaggedProfile(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidTaggedProfile(Throwable paramThrowable) {
/* 1229 */     return invalidTaggedProfile(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidTaggedProfile() {
/* 1233 */     return invalidTaggedProfile(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM objrefFromForeignOrb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1239 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079693, paramCompletionStatus);
/* 1240 */     if (paramThrowable != null) {
/* 1241 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1243 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1244 */       Object[] arrayOfObject = null;
/* 1245 */       doLog(Level.WARNING, "ORBUTIL.objrefFromForeignOrb", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1249 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM objrefFromForeignOrb(CompletionStatus paramCompletionStatus) {
/* 1253 */     return objrefFromForeignOrb(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM objrefFromForeignOrb(Throwable paramThrowable) {
/* 1257 */     return objrefFromForeignOrb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM objrefFromForeignOrb() {
/* 1261 */     return objrefFromForeignOrb(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM localObjectNotAllowed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1267 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079694, paramCompletionStatus);
/* 1268 */     if (paramThrowable != null) {
/* 1269 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1271 */     if (this.logger.isLoggable(Level.FINE)) {
/* 1272 */       Object[] arrayOfObject = null;
/* 1273 */       doLog(Level.FINE, "ORBUTIL.localObjectNotAllowed", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1277 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM localObjectNotAllowed(CompletionStatus paramCompletionStatus) {
/* 1281 */     return localObjectNotAllowed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM localObjectNotAllowed(Throwable paramThrowable) {
/* 1285 */     return localObjectNotAllowed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM localObjectNotAllowed() {
/* 1289 */     return localObjectNotAllowed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nullObjectReference(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1295 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079695, paramCompletionStatus);
/* 1296 */     if (paramThrowable != null) {
/* 1297 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1299 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1300 */       Object[] arrayOfObject = null;
/* 1301 */       doLog(Level.WARNING, "ORBUTIL.nullObjectReference", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1305 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nullObjectReference(CompletionStatus paramCompletionStatus) {
/* 1309 */     return nullObjectReference(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nullObjectReference(Throwable paramThrowable) {
/* 1313 */     return nullObjectReference(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM nullObjectReference() {
/* 1317 */     return nullObjectReference(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotLoadClass(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 1323 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079696, paramCompletionStatus);
/* 1324 */     if (paramThrowable != null) {
/* 1325 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1327 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1328 */       Object[] arrayOfObject = new Object[1];
/* 1329 */       arrayOfObject[0] = paramObject;
/* 1330 */       doLog(Level.WARNING, "ORBUTIL.couldNotLoadClass", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1334 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotLoadClass(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 1338 */     return couldNotLoadClass(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotLoadClass(Throwable paramThrowable, Object paramObject) {
/* 1342 */     return couldNotLoadClass(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotLoadClass(Object paramObject) {
/* 1346 */     return couldNotLoadClass(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badUrl(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 1352 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079697, paramCompletionStatus);
/* 1353 */     if (paramThrowable != null) {
/* 1354 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1356 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1357 */       Object[] arrayOfObject = new Object[1];
/* 1358 */       arrayOfObject[0] = paramObject;
/* 1359 */       doLog(Level.WARNING, "ORBUTIL.badUrl", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1363 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badUrl(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 1367 */     return badUrl(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badUrl(Throwable paramThrowable, Object paramObject) {
/* 1371 */     return badUrl(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badUrl(Object paramObject) {
/* 1375 */     return badUrl(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM fieldNotFound(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 1381 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079698, paramCompletionStatus);
/* 1382 */     if (paramThrowable != null) {
/* 1383 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1385 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1386 */       Object[] arrayOfObject = new Object[1];
/* 1387 */       arrayOfObject[0] = paramObject;
/* 1388 */       doLog(Level.WARNING, "ORBUTIL.fieldNotFound", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1392 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM fieldNotFound(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 1396 */     return fieldNotFound(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM fieldNotFound(Throwable paramThrowable, Object paramObject) {
/* 1400 */     return fieldNotFound(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM fieldNotFound(Object paramObject) {
/* 1404 */     return fieldNotFound(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM errorSettingField(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 1410 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079699, paramCompletionStatus);
/* 1411 */     if (paramThrowable != null) {
/* 1412 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1414 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1415 */       Object[] arrayOfObject = new Object[2];
/* 1416 */       arrayOfObject[0] = paramObject1;
/* 1417 */       arrayOfObject[1] = paramObject2;
/* 1418 */       doLog(Level.WARNING, "ORBUTIL.errorSettingField", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1422 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM errorSettingField(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 1426 */     return errorSettingField(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM errorSettingField(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 1430 */     return errorSettingField(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM errorSettingField(Object paramObject1, Object paramObject2) {
/* 1434 */     return errorSettingField(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM boundsErrorInDiiRequest(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1440 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079700, paramCompletionStatus);
/* 1441 */     if (paramThrowable != null) {
/* 1442 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1444 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1445 */       Object[] arrayOfObject = null;
/* 1446 */       doLog(Level.WARNING, "ORBUTIL.boundsErrorInDiiRequest", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1450 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM boundsErrorInDiiRequest(CompletionStatus paramCompletionStatus) {
/* 1454 */     return boundsErrorInDiiRequest(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM boundsErrorInDiiRequest(Throwable paramThrowable) {
/* 1458 */     return boundsErrorInDiiRequest(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM boundsErrorInDiiRequest() {
/* 1462 */     return boundsErrorInDiiRequest(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM persistentServerInitError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1468 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079701, paramCompletionStatus);
/* 1469 */     if (paramThrowable != null) {
/* 1470 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1472 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1473 */       Object[] arrayOfObject = null;
/* 1474 */       doLog(Level.WARNING, "ORBUTIL.persistentServerInitError", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1478 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM persistentServerInitError(CompletionStatus paramCompletionStatus) {
/* 1482 */     return persistentServerInitError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM persistentServerInitError(Throwable paramThrowable) {
/* 1486 */     return persistentServerInitError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM persistentServerInitError() {
/* 1490 */     return persistentServerInitError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotCreateArray(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 1496 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079702, paramCompletionStatus);
/* 1497 */     if (paramThrowable != null) {
/* 1498 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1500 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1501 */       Object[] arrayOfObject = new Object[3];
/* 1502 */       arrayOfObject[0] = paramObject1;
/* 1503 */       arrayOfObject[1] = paramObject2;
/* 1504 */       arrayOfObject[2] = paramObject3;
/* 1505 */       doLog(Level.WARNING, "ORBUTIL.couldNotCreateArray", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1509 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotCreateArray(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1513 */     return couldNotCreateArray(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotCreateArray(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1517 */     return couldNotCreateArray(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotCreateArray(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1521 */     return couldNotCreateArray(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotSetArray(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5)
/*      */   {
/* 1527 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079703, paramCompletionStatus);
/* 1528 */     if (paramThrowable != null) {
/* 1529 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1531 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1532 */       Object[] arrayOfObject = new Object[5];
/* 1533 */       arrayOfObject[0] = paramObject1;
/* 1534 */       arrayOfObject[1] = paramObject2;
/* 1535 */       arrayOfObject[2] = paramObject3;
/* 1536 */       arrayOfObject[3] = paramObject4;
/* 1537 */       arrayOfObject[4] = paramObject5;
/* 1538 */       doLog(Level.WARNING, "ORBUTIL.couldNotSetArray", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1542 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotSetArray(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5) {
/* 1546 */     return couldNotSetArray(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3, paramObject4, paramObject5);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotSetArray(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5) {
/* 1550 */     return couldNotSetArray(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3, paramObject4, paramObject5);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM couldNotSetArray(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5) {
/* 1554 */     return couldNotSetArray(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3, paramObject4, paramObject5);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM illegalBootstrapOperation(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 1560 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079704, paramCompletionStatus);
/* 1561 */     if (paramThrowable != null) {
/* 1562 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1564 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1565 */       Object[] arrayOfObject = new Object[1];
/* 1566 */       arrayOfObject[0] = paramObject;
/* 1567 */       doLog(Level.WARNING, "ORBUTIL.illegalBootstrapOperation", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1571 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM illegalBootstrapOperation(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 1575 */     return illegalBootstrapOperation(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM illegalBootstrapOperation(Throwable paramThrowable, Object paramObject) {
/* 1579 */     return illegalBootstrapOperation(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM illegalBootstrapOperation(Object paramObject) {
/* 1583 */     return illegalBootstrapOperation(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM bootstrapRuntimeException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1589 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079705, paramCompletionStatus);
/* 1590 */     if (paramThrowable != null) {
/* 1591 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1593 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1594 */       Object[] arrayOfObject = null;
/* 1595 */       doLog(Level.WARNING, "ORBUTIL.bootstrapRuntimeException", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1599 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM bootstrapRuntimeException(CompletionStatus paramCompletionStatus) {
/* 1603 */     return bootstrapRuntimeException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM bootstrapRuntimeException(Throwable paramThrowable) {
/* 1607 */     return bootstrapRuntimeException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM bootstrapRuntimeException() {
/* 1611 */     return bootstrapRuntimeException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM bootstrapException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1617 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079706, paramCompletionStatus);
/* 1618 */     if (paramThrowable != null) {
/* 1619 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1621 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1622 */       Object[] arrayOfObject = null;
/* 1623 */       doLog(Level.WARNING, "ORBUTIL.bootstrapException", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1627 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM bootstrapException(CompletionStatus paramCompletionStatus) {
/* 1631 */     return bootstrapException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM bootstrapException(Throwable paramThrowable) {
/* 1635 */     return bootstrapException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM bootstrapException() {
/* 1639 */     return bootstrapException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM stringExpected(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1645 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079707, paramCompletionStatus);
/* 1646 */     if (paramThrowable != null) {
/* 1647 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1649 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1650 */       Object[] arrayOfObject = null;
/* 1651 */       doLog(Level.WARNING, "ORBUTIL.stringExpected", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1655 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM stringExpected(CompletionStatus paramCompletionStatus) {
/* 1659 */     return stringExpected(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM stringExpected(Throwable paramThrowable) {
/* 1663 */     return stringExpected(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM stringExpected() {
/* 1667 */     return stringExpected(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidTypecodeKind(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 1673 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079708, paramCompletionStatus);
/* 1674 */     if (paramThrowable != null) {
/* 1675 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1677 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1678 */       Object[] arrayOfObject = new Object[1];
/* 1679 */       arrayOfObject[0] = paramObject;
/* 1680 */       doLog(Level.WARNING, "ORBUTIL.invalidTypecodeKind", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1684 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidTypecodeKind(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 1688 */     return invalidTypecodeKind(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidTypecodeKind(Throwable paramThrowable, Object paramObject) {
/* 1692 */     return invalidTypecodeKind(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidTypecodeKind(Object paramObject) {
/* 1696 */     return invalidTypecodeKind(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM socketFactoryAndContactInfoListAtSameTime(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1702 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079709, paramCompletionStatus);
/* 1703 */     if (paramThrowable != null) {
/* 1704 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1706 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1707 */       Object[] arrayOfObject = null;
/* 1708 */       doLog(Level.WARNING, "ORBUTIL.socketFactoryAndContactInfoListAtSameTime", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1712 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM socketFactoryAndContactInfoListAtSameTime(CompletionStatus paramCompletionStatus) {
/* 1716 */     return socketFactoryAndContactInfoListAtSameTime(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM socketFactoryAndContactInfoListAtSameTime(Throwable paramThrowable) {
/* 1720 */     return socketFactoryAndContactInfoListAtSameTime(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM socketFactoryAndContactInfoListAtSameTime() {
/* 1724 */     return socketFactoryAndContactInfoListAtSameTime(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM acceptorsAndLegacySocketFactoryAtSameTime(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1730 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079710, paramCompletionStatus);
/* 1731 */     if (paramThrowable != null) {
/* 1732 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1734 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1735 */       Object[] arrayOfObject = null;
/* 1736 */       doLog(Level.WARNING, "ORBUTIL.acceptorsAndLegacySocketFactoryAtSameTime", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1740 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM acceptorsAndLegacySocketFactoryAtSameTime(CompletionStatus paramCompletionStatus) {
/* 1744 */     return acceptorsAndLegacySocketFactoryAtSameTime(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM acceptorsAndLegacySocketFactoryAtSameTime(Throwable paramThrowable) {
/* 1748 */     return acceptorsAndLegacySocketFactoryAtSameTime(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM acceptorsAndLegacySocketFactoryAtSameTime() {
/* 1752 */     return acceptorsAndLegacySocketFactoryAtSameTime(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badOrbForServant(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1758 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079711, paramCompletionStatus);
/* 1759 */     if (paramThrowable != null) {
/* 1760 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1762 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1763 */       Object[] arrayOfObject = null;
/* 1764 */       doLog(Level.WARNING, "ORBUTIL.badOrbForServant", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1768 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badOrbForServant(CompletionStatus paramCompletionStatus) {
/* 1772 */     return badOrbForServant(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badOrbForServant(Throwable paramThrowable) {
/* 1776 */     return badOrbForServant(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM badOrbForServant() {
/* 1780 */     return badOrbForServant(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningPolicyValue(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 1786 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079712, paramCompletionStatus);
/* 1787 */     if (paramThrowable != null) {
/* 1788 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1790 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1791 */       Object[] arrayOfObject = new Object[3];
/* 1792 */       arrayOfObject[0] = paramObject1;
/* 1793 */       arrayOfObject[1] = paramObject2;
/* 1794 */       arrayOfObject[2] = paramObject3;
/* 1795 */       doLog(Level.WARNING, "ORBUTIL.invalidRequestPartitioningPolicyValue", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1799 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningPolicyValue(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1803 */     return invalidRequestPartitioningPolicyValue(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningPolicyValue(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1807 */     return invalidRequestPartitioningPolicyValue(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningPolicyValue(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1811 */     return invalidRequestPartitioningPolicyValue(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningComponentValue(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 1817 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079713, paramCompletionStatus);
/* 1818 */     if (paramThrowable != null) {
/* 1819 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1821 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1822 */       Object[] arrayOfObject = new Object[3];
/* 1823 */       arrayOfObject[0] = paramObject1;
/* 1824 */       arrayOfObject[1] = paramObject2;
/* 1825 */       arrayOfObject[2] = paramObject3;
/* 1826 */       doLog(Level.WARNING, "ORBUTIL.invalidRequestPartitioningComponentValue", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1830 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningComponentValue(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1834 */     return invalidRequestPartitioningComponentValue(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningComponentValue(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1838 */     return invalidRequestPartitioningComponentValue(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningComponentValue(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1842 */     return invalidRequestPartitioningComponentValue(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningId(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 1848 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079714, paramCompletionStatus);
/* 1849 */     if (paramThrowable != null) {
/* 1850 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1852 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1853 */       Object[] arrayOfObject = new Object[3];
/* 1854 */       arrayOfObject[0] = paramObject1;
/* 1855 */       arrayOfObject[1] = paramObject2;
/* 1856 */       arrayOfObject[2] = paramObject3;
/* 1857 */       doLog(Level.WARNING, "ORBUTIL.invalidRequestPartitioningId", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1861 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningId(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1865 */     return invalidRequestPartitioningId(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningId(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1869 */     return invalidRequestPartitioningId(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM invalidRequestPartitioningId(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1873 */     return invalidRequestPartitioningId(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM errorInSettingDynamicStubFactoryFactory(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 1879 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(1398079715, paramCompletionStatus);
/* 1880 */     if (paramThrowable != null) {
/* 1881 */       localBAD_PARAM.initCause(paramThrowable);
/*      */     }
/* 1883 */     if (this.logger.isLoggable(Level.FINE)) {
/* 1884 */       Object[] arrayOfObject = new Object[1];
/* 1885 */       arrayOfObject[0] = paramObject;
/* 1886 */       doLog(Level.FINE, "ORBUTIL.errorInSettingDynamicStubFactoryFactory", arrayOfObject, ORBUtilSystemException.class, localBAD_PARAM);
/*      */     }
/*      */ 
/* 1890 */     return localBAD_PARAM;
/*      */   }
/*      */ 
/*      */   public BAD_PARAM errorInSettingDynamicStubFactoryFactory(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 1894 */     return errorInSettingDynamicStubFactoryFactory(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM errorInSettingDynamicStubFactoryFactory(Throwable paramThrowable, Object paramObject) {
/* 1898 */     return errorInSettingDynamicStubFactoryFactory(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_PARAM errorInSettingDynamicStubFactoryFactory(Object paramObject) {
/* 1902 */     return errorInSettingDynamicStubFactoryFactory(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER dsimethodNotcalled(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1912 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398079689, paramCompletionStatus);
/* 1913 */     if (paramThrowable != null) {
/* 1914 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/* 1916 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1917 */       Object[] arrayOfObject = null;
/* 1918 */       doLog(Level.WARNING, "ORBUTIL.dsimethodNotcalled", arrayOfObject, ORBUtilSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/* 1922 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER dsimethodNotcalled(CompletionStatus paramCompletionStatus) {
/* 1926 */     return dsimethodNotcalled(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER dsimethodNotcalled(Throwable paramThrowable) {
/* 1930 */     return dsimethodNotcalled(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER dsimethodNotcalled() {
/* 1934 */     return dsimethodNotcalled(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledMultiple(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1940 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398079690, paramCompletionStatus);
/* 1941 */     if (paramThrowable != null) {
/* 1942 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/* 1944 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1945 */       Object[] arrayOfObject = null;
/* 1946 */       doLog(Level.WARNING, "ORBUTIL.argumentsCalledMultiple", arrayOfObject, ORBUtilSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/* 1950 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledMultiple(CompletionStatus paramCompletionStatus) {
/* 1954 */     return argumentsCalledMultiple(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledMultiple(Throwable paramThrowable) {
/* 1958 */     return argumentsCalledMultiple(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledMultiple() {
/* 1962 */     return argumentsCalledMultiple(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledAfterException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1968 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398079691, paramCompletionStatus);
/* 1969 */     if (paramThrowable != null) {
/* 1970 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/* 1972 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 1973 */       Object[] arrayOfObject = null;
/* 1974 */       doLog(Level.WARNING, "ORBUTIL.argumentsCalledAfterException", arrayOfObject, ORBUtilSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/* 1978 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledAfterException(CompletionStatus paramCompletionStatus) {
/* 1982 */     return argumentsCalledAfterException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledAfterException(Throwable paramThrowable) {
/* 1986 */     return argumentsCalledAfterException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledAfterException() {
/* 1990 */     return argumentsCalledAfterException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledNullArgs(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 1996 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398079692, paramCompletionStatus);
/* 1997 */     if (paramThrowable != null) {
/* 1998 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/* 2000 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2001 */       Object[] arrayOfObject = null;
/* 2002 */       doLog(Level.WARNING, "ORBUTIL.argumentsCalledNullArgs", arrayOfObject, ORBUtilSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/* 2006 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledNullArgs(CompletionStatus paramCompletionStatus) {
/* 2010 */     return argumentsCalledNullArgs(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledNullArgs(Throwable paramThrowable) {
/* 2014 */     return argumentsCalledNullArgs(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsCalledNullArgs() {
/* 2018 */     return argumentsCalledNullArgs(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsNotCalled(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2024 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398079693, paramCompletionStatus);
/* 2025 */     if (paramThrowable != null) {
/* 2026 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/* 2028 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2029 */       Object[] arrayOfObject = null;
/* 2030 */       doLog(Level.FINE, "ORBUTIL.argumentsNotCalled", arrayOfObject, ORBUtilSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/* 2034 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsNotCalled(CompletionStatus paramCompletionStatus) {
/* 2038 */     return argumentsNotCalled(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsNotCalled(Throwable paramThrowable) {
/* 2042 */     return argumentsNotCalled(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER argumentsNotCalled() {
/* 2046 */     return argumentsNotCalled(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultCalledMultiple(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2052 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398079694, paramCompletionStatus);
/* 2053 */     if (paramThrowable != null) {
/* 2054 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/* 2056 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2057 */       Object[] arrayOfObject = null;
/* 2058 */       doLog(Level.WARNING, "ORBUTIL.setResultCalledMultiple", arrayOfObject, ORBUtilSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/* 2062 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultCalledMultiple(CompletionStatus paramCompletionStatus) {
/* 2066 */     return setResultCalledMultiple(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultCalledMultiple(Throwable paramThrowable) {
/* 2070 */     return setResultCalledMultiple(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultCalledMultiple() {
/* 2074 */     return setResultCalledMultiple(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultAfterException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2080 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398079695, paramCompletionStatus);
/* 2081 */     if (paramThrowable != null) {
/* 2082 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/* 2084 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2085 */       Object[] arrayOfObject = null;
/* 2086 */       doLog(Level.FINE, "ORBUTIL.setResultAfterException", arrayOfObject, ORBUtilSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/* 2090 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultAfterException(CompletionStatus paramCompletionStatus) {
/* 2094 */     return setResultAfterException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultAfterException(Throwable paramThrowable) {
/* 2098 */     return setResultAfterException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultAfterException() {
/* 2102 */     return setResultAfterException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultCalledNullArgs(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2108 */     BAD_INV_ORDER localBAD_INV_ORDER = new BAD_INV_ORDER(1398079696, paramCompletionStatus);
/* 2109 */     if (paramThrowable != null) {
/* 2110 */       localBAD_INV_ORDER.initCause(paramThrowable);
/*      */     }
/* 2112 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2113 */       Object[] arrayOfObject = null;
/* 2114 */       doLog(Level.WARNING, "ORBUTIL.setResultCalledNullArgs", arrayOfObject, ORBUtilSystemException.class, localBAD_INV_ORDER);
/*      */     }
/*      */ 
/* 2118 */     return localBAD_INV_ORDER;
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultCalledNullArgs(CompletionStatus paramCompletionStatus) {
/* 2122 */     return setResultCalledNullArgs(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultCalledNullArgs(Throwable paramThrowable) {
/* 2126 */     return setResultCalledNullArgs(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_INV_ORDER setResultCalledNullArgs() {
/* 2130 */     return setResultCalledNullArgs(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE badRemoteTypecode(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2140 */     BAD_TYPECODE localBAD_TYPECODE = new BAD_TYPECODE(1398079689, paramCompletionStatus);
/* 2141 */     if (paramThrowable != null) {
/* 2142 */       localBAD_TYPECODE.initCause(paramThrowable);
/*      */     }
/* 2144 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2145 */       Object[] arrayOfObject = null;
/* 2146 */       doLog(Level.WARNING, "ORBUTIL.badRemoteTypecode", arrayOfObject, ORBUtilSystemException.class, localBAD_TYPECODE);
/*      */     }
/*      */ 
/* 2150 */     return localBAD_TYPECODE;
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE badRemoteTypecode(CompletionStatus paramCompletionStatus) {
/* 2154 */     return badRemoteTypecode(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE badRemoteTypecode(Throwable paramThrowable) {
/* 2158 */     return badRemoteTypecode(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE badRemoteTypecode() {
/* 2162 */     return badRemoteTypecode(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE unresolvedRecursiveTypecode(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2168 */     BAD_TYPECODE localBAD_TYPECODE = new BAD_TYPECODE(1398079690, paramCompletionStatus);
/* 2169 */     if (paramThrowable != null) {
/* 2170 */       localBAD_TYPECODE.initCause(paramThrowable);
/*      */     }
/* 2172 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2173 */       Object[] arrayOfObject = null;
/* 2174 */       doLog(Level.WARNING, "ORBUTIL.unresolvedRecursiveTypecode", arrayOfObject, ORBUtilSystemException.class, localBAD_TYPECODE);
/*      */     }
/*      */ 
/* 2178 */     return localBAD_TYPECODE;
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE unresolvedRecursiveTypecode(CompletionStatus paramCompletionStatus) {
/* 2182 */     return unresolvedRecursiveTypecode(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE unresolvedRecursiveTypecode(Throwable paramThrowable) {
/* 2186 */     return unresolvedRecursiveTypecode(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public BAD_TYPECODE unresolvedRecursiveTypecode() {
/* 2190 */     return unresolvedRecursiveTypecode(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectFailure(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 2200 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079689, paramCompletionStatus);
/* 2201 */     if (paramThrowable != null) {
/* 2202 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2204 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2205 */       Object[] arrayOfObject = new Object[3];
/* 2206 */       arrayOfObject[0] = paramObject1;
/* 2207 */       arrayOfObject[1] = paramObject2;
/* 2208 */       arrayOfObject[2] = paramObject3;
/* 2209 */       doLog(Level.WARNING, "ORBUTIL.connectFailure", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2213 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectFailure(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 2217 */     return connectFailure(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectFailure(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 2221 */     return connectFailure(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectFailure(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 2225 */     return connectFailure(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionCloseRebind(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2231 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079690, paramCompletionStatus);
/* 2232 */     if (paramThrowable != null) {
/* 2233 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2235 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2236 */       Object[] arrayOfObject = null;
/* 2237 */       doLog(Level.WARNING, "ORBUTIL.connectionCloseRebind", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2241 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionCloseRebind(CompletionStatus paramCompletionStatus) {
/* 2245 */     return connectionCloseRebind(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionCloseRebind(Throwable paramThrowable) {
/* 2249 */     return connectionCloseRebind(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionCloseRebind() {
/* 2253 */     return connectionCloseRebind(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE writeErrorSend(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2259 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079691, paramCompletionStatus);
/* 2260 */     if (paramThrowable != null) {
/* 2261 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2263 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2264 */       Object[] arrayOfObject = null;
/* 2265 */       doLog(Level.FINE, "ORBUTIL.writeErrorSend", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2269 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE writeErrorSend(CompletionStatus paramCompletionStatus) {
/* 2273 */     return writeErrorSend(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE writeErrorSend(Throwable paramThrowable) {
/* 2277 */     return writeErrorSend(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE writeErrorSend() {
/* 2281 */     return writeErrorSend(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE getPropertiesError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2287 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079692, paramCompletionStatus);
/* 2288 */     if (paramThrowable != null) {
/* 2289 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2291 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2292 */       Object[] arrayOfObject = null;
/* 2293 */       doLog(Level.WARNING, "ORBUTIL.getPropertiesError", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2297 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE getPropertiesError(CompletionStatus paramCompletionStatus) {
/* 2301 */     return getPropertiesError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE getPropertiesError(Throwable paramThrowable) {
/* 2305 */     return getPropertiesError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE getPropertiesError() {
/* 2309 */     return getPropertiesError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE bootstrapServerNotAvail(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2315 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079693, paramCompletionStatus);
/* 2316 */     if (paramThrowable != null) {
/* 2317 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2319 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2320 */       Object[] arrayOfObject = null;
/* 2321 */       doLog(Level.WARNING, "ORBUTIL.bootstrapServerNotAvail", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2325 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE bootstrapServerNotAvail(CompletionStatus paramCompletionStatus) {
/* 2329 */     return bootstrapServerNotAvail(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE bootstrapServerNotAvail(Throwable paramThrowable) {
/* 2333 */     return bootstrapServerNotAvail(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE bootstrapServerNotAvail() {
/* 2337 */     return bootstrapServerNotAvail(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE invokeError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2343 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079694, paramCompletionStatus);
/* 2344 */     if (paramThrowable != null) {
/* 2345 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2347 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2348 */       Object[] arrayOfObject = null;
/* 2349 */       doLog(Level.WARNING, "ORBUTIL.invokeError", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2353 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE invokeError(CompletionStatus paramCompletionStatus) {
/* 2357 */     return invokeError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE invokeError(Throwable paramThrowable) {
/* 2361 */     return invokeError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE invokeError() {
/* 2365 */     return invokeError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE defaultCreateServerSocketGivenNonIiopClearText(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 2371 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079695, paramCompletionStatus);
/* 2372 */     if (paramThrowable != null) {
/* 2373 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2375 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2376 */       Object[] arrayOfObject = new Object[1];
/* 2377 */       arrayOfObject[0] = paramObject;
/* 2378 */       doLog(Level.WARNING, "ORBUTIL.defaultCreateServerSocketGivenNonIiopClearText", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2382 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE defaultCreateServerSocketGivenNonIiopClearText(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 2386 */     return defaultCreateServerSocketGivenNonIiopClearText(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE defaultCreateServerSocketGivenNonIiopClearText(Throwable paramThrowable, Object paramObject) {
/* 2390 */     return defaultCreateServerSocketGivenNonIiopClearText(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE defaultCreateServerSocketGivenNonIiopClearText(Object paramObject) {
/* 2394 */     return defaultCreateServerSocketGivenNonIiopClearText(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionAbort(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2400 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079696, paramCompletionStatus);
/* 2401 */     if (paramThrowable != null) {
/* 2402 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2404 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2405 */       Object[] arrayOfObject = null;
/* 2406 */       doLog(Level.FINE, "ORBUTIL.connectionAbort", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2410 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionAbort(CompletionStatus paramCompletionStatus) {
/* 2414 */     return connectionAbort(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionAbort(Throwable paramThrowable) {
/* 2418 */     return connectionAbort(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionAbort() {
/* 2422 */     return connectionAbort(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionRebind(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2428 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079697, paramCompletionStatus);
/* 2429 */     if (paramThrowable != null) {
/* 2430 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2432 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2433 */       Object[] arrayOfObject = null;
/* 2434 */       doLog(Level.FINE, "ORBUTIL.connectionRebind", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2438 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionRebind(CompletionStatus paramCompletionStatus) {
/* 2442 */     return connectionRebind(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionRebind(Throwable paramThrowable) {
/* 2446 */     return connectionRebind(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE connectionRebind() {
/* 2450 */     return connectionRebind(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE recvMsgError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2456 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079698, paramCompletionStatus);
/* 2457 */     if (paramThrowable != null) {
/* 2458 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2460 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2461 */       Object[] arrayOfObject = null;
/* 2462 */       doLog(Level.WARNING, "ORBUTIL.recvMsgError", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2466 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE recvMsgError(CompletionStatus paramCompletionStatus) {
/* 2470 */     return recvMsgError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE recvMsgError(Throwable paramThrowable) {
/* 2474 */     return recvMsgError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE recvMsgError() {
/* 2478 */     return recvMsgError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE ioexceptionWhenReadingConnection(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2484 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079699, paramCompletionStatus);
/* 2485 */     if (paramThrowable != null) {
/* 2486 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2488 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2489 */       Object[] arrayOfObject = null;
/* 2490 */       doLog(Level.FINE, "ORBUTIL.ioexceptionWhenReadingConnection", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2494 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE ioexceptionWhenReadingConnection(CompletionStatus paramCompletionStatus) {
/* 2498 */     return ioexceptionWhenReadingConnection(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE ioexceptionWhenReadingConnection(Throwable paramThrowable) {
/* 2502 */     return ioexceptionWhenReadingConnection(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE ioexceptionWhenReadingConnection() {
/* 2506 */     return ioexceptionWhenReadingConnection(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE selectionKeyInvalid(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 2512 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079700, paramCompletionStatus);
/* 2513 */     if (paramThrowable != null) {
/* 2514 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2516 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2517 */       Object[] arrayOfObject = new Object[1];
/* 2518 */       arrayOfObject[0] = paramObject;
/* 2519 */       doLog(Level.FINE, "ORBUTIL.selectionKeyInvalid", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2523 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE selectionKeyInvalid(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 2527 */     return selectionKeyInvalid(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE selectionKeyInvalid(Throwable paramThrowable, Object paramObject) {
/* 2531 */     return selectionKeyInvalid(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE selectionKeyInvalid(Object paramObject) {
/* 2535 */     return selectionKeyInvalid(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE exceptionInAccept(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 2541 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079701, paramCompletionStatus);
/* 2542 */     if (paramThrowable != null) {
/* 2543 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2545 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2546 */       Object[] arrayOfObject = new Object[1];
/* 2547 */       arrayOfObject[0] = paramObject;
/* 2548 */       doLog(Level.FINE, "ORBUTIL.exceptionInAccept", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2552 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE exceptionInAccept(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 2556 */     return exceptionInAccept(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE exceptionInAccept(Throwable paramThrowable, Object paramObject) {
/* 2560 */     return exceptionInAccept(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE exceptionInAccept(Object paramObject) {
/* 2564 */     return exceptionInAccept(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE securityExceptionInAccept(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 2570 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079702, paramCompletionStatus);
/* 2571 */     if (paramThrowable != null) {
/* 2572 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2574 */     if (this.logger.isLoggable(Level.FINE)) {
/* 2575 */       Object[] arrayOfObject = new Object[2];
/* 2576 */       arrayOfObject[0] = paramObject1;
/* 2577 */       arrayOfObject[1] = paramObject2;
/* 2578 */       doLog(Level.FINE, "ORBUTIL.securityExceptionInAccept", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2582 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE securityExceptionInAccept(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 2586 */     return securityExceptionInAccept(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE securityExceptionInAccept(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 2590 */     return securityExceptionInAccept(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE securityExceptionInAccept(Object paramObject1, Object paramObject2) {
/* 2594 */     return securityExceptionInAccept(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE transportReadTimeoutExceeded(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4)
/*      */   {
/* 2600 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079703, paramCompletionStatus);
/* 2601 */     if (paramThrowable != null) {
/* 2602 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2604 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2605 */       Object[] arrayOfObject = new Object[4];
/* 2606 */       arrayOfObject[0] = paramObject1;
/* 2607 */       arrayOfObject[1] = paramObject2;
/* 2608 */       arrayOfObject[2] = paramObject3;
/* 2609 */       arrayOfObject[3] = paramObject4;
/* 2610 */       doLog(Level.WARNING, "ORBUTIL.transportReadTimeoutExceeded", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2614 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE transportReadTimeoutExceeded(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4) {
/* 2618 */     return transportReadTimeoutExceeded(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3, paramObject4);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE transportReadTimeoutExceeded(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4) {
/* 2622 */     return transportReadTimeoutExceeded(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3, paramObject4);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE transportReadTimeoutExceeded(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4) {
/* 2626 */     return transportReadTimeoutExceeded(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3, paramObject4);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE createListenerFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 2632 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079704, paramCompletionStatus);
/* 2633 */     if (paramThrowable != null) {
/* 2634 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2636 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 2637 */       Object[] arrayOfObject = new Object[1];
/* 2638 */       arrayOfObject[0] = paramObject;
/* 2639 */       doLog(Level.SEVERE, "ORBUTIL.createListenerFailed", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2643 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE createListenerFailed(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 2647 */     return createListenerFailed(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE createListenerFailed(Throwable paramThrowable, Object paramObject) {
/* 2651 */     return createListenerFailed(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE createListenerFailed(Object paramObject) {
/* 2655 */     return createListenerFailed(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE bufferReadManagerTimeout(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2661 */     COMM_FAILURE localCOMM_FAILURE = new COMM_FAILURE(1398079705, paramCompletionStatus);
/* 2662 */     if (paramThrowable != null) {
/* 2663 */       localCOMM_FAILURE.initCause(paramThrowable);
/*      */     }
/* 2665 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2666 */       Object[] arrayOfObject = null;
/* 2667 */       doLog(Level.WARNING, "ORBUTIL.bufferReadManagerTimeout", arrayOfObject, ORBUtilSystemException.class, localCOMM_FAILURE);
/*      */     }
/*      */ 
/* 2671 */     return localCOMM_FAILURE;
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE bufferReadManagerTimeout(CompletionStatus paramCompletionStatus) {
/* 2675 */     return bufferReadManagerTimeout(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE bufferReadManagerTimeout(Throwable paramThrowable) {
/* 2679 */     return bufferReadManagerTimeout(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public COMM_FAILURE bufferReadManagerTimeout() {
/* 2683 */     return bufferReadManagerTimeout(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badStringifiedIorLen(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2693 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079689, paramCompletionStatus);
/* 2694 */     if (paramThrowable != null) {
/* 2695 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2697 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2698 */       Object[] arrayOfObject = null;
/* 2699 */       doLog(Level.WARNING, "ORBUTIL.badStringifiedIorLen", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2703 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badStringifiedIorLen(CompletionStatus paramCompletionStatus) {
/* 2707 */     return badStringifiedIorLen(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badStringifiedIorLen(Throwable paramThrowable) {
/* 2711 */     return badStringifiedIorLen(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badStringifiedIorLen() {
/* 2715 */     return badStringifiedIorLen(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badStringifiedIor(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2721 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079690, paramCompletionStatus);
/* 2722 */     if (paramThrowable != null) {
/* 2723 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2725 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2726 */       Object[] arrayOfObject = null;
/* 2727 */       doLog(Level.WARNING, "ORBUTIL.badStringifiedIor", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2731 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badStringifiedIor(CompletionStatus paramCompletionStatus) {
/* 2735 */     return badStringifiedIor(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badStringifiedIor(Throwable paramThrowable) {
/* 2739 */     return badStringifiedIor(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badStringifiedIor() {
/* 2743 */     return badStringifiedIor(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badModifier(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2749 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079691, paramCompletionStatus);
/* 2750 */     if (paramThrowable != null) {
/* 2751 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2753 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2754 */       Object[] arrayOfObject = null;
/* 2755 */       doLog(Level.WARNING, "ORBUTIL.badModifier", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2759 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badModifier(CompletionStatus paramCompletionStatus) {
/* 2763 */     return badModifier(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badModifier(Throwable paramThrowable) {
/* 2767 */     return badModifier(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badModifier() {
/* 2771 */     return badModifier(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION codesetIncompatible(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2777 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079692, paramCompletionStatus);
/* 2778 */     if (paramThrowable != null) {
/* 2779 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2781 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2782 */       Object[] arrayOfObject = null;
/* 2783 */       doLog(Level.WARNING, "ORBUTIL.codesetIncompatible", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2787 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION codesetIncompatible(CompletionStatus paramCompletionStatus) {
/* 2791 */     return codesetIncompatible(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION codesetIncompatible(Throwable paramThrowable) {
/* 2795 */     return codesetIncompatible(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION codesetIncompatible() {
/* 2799 */     return codesetIncompatible(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badHexDigit(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2805 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079693, paramCompletionStatus);
/* 2806 */     if (paramThrowable != null) {
/* 2807 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2809 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2810 */       Object[] arrayOfObject = null;
/* 2811 */       doLog(Level.WARNING, "ORBUTIL.badHexDigit", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2815 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badHexDigit(CompletionStatus paramCompletionStatus) {
/* 2819 */     return badHexDigit(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badHexDigit(Throwable paramThrowable) {
/* 2823 */     return badHexDigit(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badHexDigit() {
/* 2827 */     return badHexDigit(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badUnicodePair(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2833 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079694, paramCompletionStatus);
/* 2834 */     if (paramThrowable != null) {
/* 2835 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2837 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2838 */       Object[] arrayOfObject = null;
/* 2839 */       doLog(Level.WARNING, "ORBUTIL.badUnicodePair", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2843 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badUnicodePair(CompletionStatus paramCompletionStatus) {
/* 2847 */     return badUnicodePair(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badUnicodePair(Throwable paramThrowable) {
/* 2851 */     return badUnicodePair(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badUnicodePair() {
/* 2855 */     return badUnicodePair(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION btcResultMoreThanOneChar(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2861 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079695, paramCompletionStatus);
/* 2862 */     if (paramThrowable != null) {
/* 2863 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2865 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2866 */       Object[] arrayOfObject = null;
/* 2867 */       doLog(Level.WARNING, "ORBUTIL.btcResultMoreThanOneChar", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2871 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION btcResultMoreThanOneChar(CompletionStatus paramCompletionStatus) {
/* 2875 */     return btcResultMoreThanOneChar(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION btcResultMoreThanOneChar(Throwable paramThrowable) {
/* 2879 */     return btcResultMoreThanOneChar(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION btcResultMoreThanOneChar() {
/* 2883 */     return btcResultMoreThanOneChar(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCodesetsFromClient(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2889 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079696, paramCompletionStatus);
/* 2890 */     if (paramThrowable != null) {
/* 2891 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2893 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2894 */       Object[] arrayOfObject = null;
/* 2895 */       doLog(Level.WARNING, "ORBUTIL.badCodesetsFromClient", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2899 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCodesetsFromClient(CompletionStatus paramCompletionStatus) {
/* 2903 */     return badCodesetsFromClient(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCodesetsFromClient(Throwable paramThrowable) {
/* 2907 */     return badCodesetsFromClient(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCodesetsFromClient() {
/* 2911 */     return badCodesetsFromClient(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION invalidSingleCharCtb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2917 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079697, paramCompletionStatus);
/* 2918 */     if (paramThrowable != null) {
/* 2919 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2921 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2922 */       Object[] arrayOfObject = null;
/* 2923 */       doLog(Level.WARNING, "ORBUTIL.invalidSingleCharCtb", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2927 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION invalidSingleCharCtb(CompletionStatus paramCompletionStatus) {
/* 2931 */     return invalidSingleCharCtb(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION invalidSingleCharCtb(Throwable paramThrowable) {
/* 2935 */     return invalidSingleCharCtb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION invalidSingleCharCtb() {
/* 2939 */     return invalidSingleCharCtb(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badGiop11Ctb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 2945 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079698, paramCompletionStatus);
/* 2946 */     if (paramThrowable != null) {
/* 2947 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2949 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2950 */       Object[] arrayOfObject = null;
/* 2951 */       doLog(Level.WARNING, "ORBUTIL.badGiop11Ctb", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2955 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badGiop11Ctb(CompletionStatus paramCompletionStatus) {
/* 2959 */     return badGiop11Ctb(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badGiop11Ctb(Throwable paramThrowable) {
/* 2963 */     return badGiop11Ctb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badGiop11Ctb() {
/* 2967 */     return badGiop11Ctb(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badSequenceBounds(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 2973 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079700, paramCompletionStatus);
/* 2974 */     if (paramThrowable != null) {
/* 2975 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 2977 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 2978 */       Object[] arrayOfObject = new Object[2];
/* 2979 */       arrayOfObject[0] = paramObject1;
/* 2980 */       arrayOfObject[1] = paramObject2;
/* 2981 */       doLog(Level.WARNING, "ORBUTIL.badSequenceBounds", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 2985 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badSequenceBounds(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 2989 */     return badSequenceBounds(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badSequenceBounds(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 2993 */     return badSequenceBounds(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badSequenceBounds(Object paramObject1, Object paramObject2) {
/* 2997 */     return badSequenceBounds(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalSocketFactoryType(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3003 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079701, paramCompletionStatus);
/* 3004 */     if (paramThrowable != null) {
/* 3005 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3007 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3008 */       Object[] arrayOfObject = new Object[1];
/* 3009 */       arrayOfObject[0] = paramObject;
/* 3010 */       doLog(Level.WARNING, "ORBUTIL.illegalSocketFactoryType", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3014 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalSocketFactoryType(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3018 */     return illegalSocketFactoryType(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalSocketFactoryType(Throwable paramThrowable, Object paramObject) {
/* 3022 */     return illegalSocketFactoryType(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalSocketFactoryType(Object paramObject) {
/* 3026 */     return illegalSocketFactoryType(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomSocketFactory(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3032 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079702, paramCompletionStatus);
/* 3033 */     if (paramThrowable != null) {
/* 3034 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3036 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3037 */       Object[] arrayOfObject = new Object[1];
/* 3038 */       arrayOfObject[0] = paramObject;
/* 3039 */       doLog(Level.WARNING, "ORBUTIL.badCustomSocketFactory", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3043 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomSocketFactory(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3047 */     return badCustomSocketFactory(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomSocketFactory(Throwable paramThrowable, Object paramObject) {
/* 3051 */     return badCustomSocketFactory(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomSocketFactory(Object paramObject) {
/* 3055 */     return badCustomSocketFactory(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION fragmentSizeMinimum(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 3061 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079703, paramCompletionStatus);
/* 3062 */     if (paramThrowable != null) {
/* 3063 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3065 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3066 */       Object[] arrayOfObject = new Object[2];
/* 3067 */       arrayOfObject[0] = paramObject1;
/* 3068 */       arrayOfObject[1] = paramObject2;
/* 3069 */       doLog(Level.WARNING, "ORBUTIL.fragmentSizeMinimum", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3073 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION fragmentSizeMinimum(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 3077 */     return fragmentSizeMinimum(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION fragmentSizeMinimum(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 3081 */     return fragmentSizeMinimum(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION fragmentSizeMinimum(Object paramObject1, Object paramObject2) {
/* 3085 */     return fragmentSizeMinimum(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION fragmentSizeDiv(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 3091 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079704, paramCompletionStatus);
/* 3092 */     if (paramThrowable != null) {
/* 3093 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3095 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3096 */       Object[] arrayOfObject = new Object[2];
/* 3097 */       arrayOfObject[0] = paramObject1;
/* 3098 */       arrayOfObject[1] = paramObject2;
/* 3099 */       doLog(Level.WARNING, "ORBUTIL.fragmentSizeDiv", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3103 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION fragmentSizeDiv(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 3107 */     return fragmentSizeDiv(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION fragmentSizeDiv(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 3111 */     return fragmentSizeDiv(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION fragmentSizeDiv(Object paramObject1, Object paramObject2) {
/* 3115 */     return fragmentSizeDiv(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitializerFailure(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3121 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079705, paramCompletionStatus);
/* 3122 */     if (paramThrowable != null) {
/* 3123 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3125 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3126 */       Object[] arrayOfObject = new Object[1];
/* 3127 */       arrayOfObject[0] = paramObject;
/* 3128 */       doLog(Level.WARNING, "ORBUTIL.orbInitializerFailure", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3132 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitializerFailure(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3136 */     return orbInitializerFailure(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitializerFailure(Throwable paramThrowable, Object paramObject) {
/* 3140 */     return orbInitializerFailure(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitializerFailure(Object paramObject) {
/* 3144 */     return orbInitializerFailure(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitializerType(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3150 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079706, paramCompletionStatus);
/* 3151 */     if (paramThrowable != null) {
/* 3152 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3154 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3155 */       Object[] arrayOfObject = new Object[1];
/* 3156 */       arrayOfObject[0] = paramObject;
/* 3157 */       doLog(Level.WARNING, "ORBUTIL.orbInitializerType", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3161 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitializerType(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3165 */     return orbInitializerType(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitializerType(Throwable paramThrowable, Object paramObject) {
/* 3169 */     return orbInitializerType(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitializerType(Object paramObject) {
/* 3173 */     return orbInitializerType(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitialreferenceSyntax(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3179 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079707, paramCompletionStatus);
/* 3180 */     if (paramThrowable != null) {
/* 3181 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3183 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3184 */       Object[] arrayOfObject = null;
/* 3185 */       doLog(Level.WARNING, "ORBUTIL.orbInitialreferenceSyntax", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3189 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitialreferenceSyntax(CompletionStatus paramCompletionStatus) {
/* 3193 */     return orbInitialreferenceSyntax(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitialreferenceSyntax(Throwable paramThrowable) {
/* 3197 */     return orbInitialreferenceSyntax(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION orbInitialreferenceSyntax() {
/* 3201 */     return orbInitialreferenceSyntax(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION acceptorInstantiationFailure(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3207 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079708, paramCompletionStatus);
/* 3208 */     if (paramThrowable != null) {
/* 3209 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3211 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3212 */       Object[] arrayOfObject = new Object[1];
/* 3213 */       arrayOfObject[0] = paramObject;
/* 3214 */       doLog(Level.WARNING, "ORBUTIL.acceptorInstantiationFailure", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3218 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION acceptorInstantiationFailure(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3222 */     return acceptorInstantiationFailure(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION acceptorInstantiationFailure(Throwable paramThrowable, Object paramObject) {
/* 3226 */     return acceptorInstantiationFailure(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION acceptorInstantiationFailure(Object paramObject) {
/* 3230 */     return acceptorInstantiationFailure(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION acceptorInstantiationTypeFailure(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3236 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079709, paramCompletionStatus);
/* 3237 */     if (paramThrowable != null) {
/* 3238 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3240 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3241 */       Object[] arrayOfObject = new Object[1];
/* 3242 */       arrayOfObject[0] = paramObject;
/* 3243 */       doLog(Level.WARNING, "ORBUTIL.acceptorInstantiationTypeFailure", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3247 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION acceptorInstantiationTypeFailure(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3251 */     return acceptorInstantiationTypeFailure(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION acceptorInstantiationTypeFailure(Throwable paramThrowable, Object paramObject) {
/* 3255 */     return acceptorInstantiationTypeFailure(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION acceptorInstantiationTypeFailure(Object paramObject) {
/* 3259 */     return acceptorInstantiationTypeFailure(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalContactInfoListFactoryType(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3265 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079710, paramCompletionStatus);
/* 3266 */     if (paramThrowable != null) {
/* 3267 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3269 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3270 */       Object[] arrayOfObject = new Object[1];
/* 3271 */       arrayOfObject[0] = paramObject;
/* 3272 */       doLog(Level.WARNING, "ORBUTIL.illegalContactInfoListFactoryType", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3276 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalContactInfoListFactoryType(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3280 */     return illegalContactInfoListFactoryType(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalContactInfoListFactoryType(Throwable paramThrowable, Object paramObject) {
/* 3284 */     return illegalContactInfoListFactoryType(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalContactInfoListFactoryType(Object paramObject) {
/* 3288 */     return illegalContactInfoListFactoryType(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badContactInfoListFactory(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3294 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079711, paramCompletionStatus);
/* 3295 */     if (paramThrowable != null) {
/* 3296 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3298 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3299 */       Object[] arrayOfObject = new Object[1];
/* 3300 */       arrayOfObject[0] = paramObject;
/* 3301 */       doLog(Level.WARNING, "ORBUTIL.badContactInfoListFactory", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3305 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badContactInfoListFactory(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3309 */     return badContactInfoListFactory(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badContactInfoListFactory(Throwable paramThrowable, Object paramObject) {
/* 3313 */     return badContactInfoListFactory(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badContactInfoListFactory(Object paramObject) {
/* 3317 */     return badContactInfoListFactory(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalIorToSocketInfoType(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3323 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079712, paramCompletionStatus);
/* 3324 */     if (paramThrowable != null) {
/* 3325 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3327 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3328 */       Object[] arrayOfObject = new Object[1];
/* 3329 */       arrayOfObject[0] = paramObject;
/* 3330 */       doLog(Level.WARNING, "ORBUTIL.illegalIorToSocketInfoType", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3334 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalIorToSocketInfoType(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3338 */     return illegalIorToSocketInfoType(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalIorToSocketInfoType(Throwable paramThrowable, Object paramObject) {
/* 3342 */     return illegalIorToSocketInfoType(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalIorToSocketInfoType(Object paramObject) {
/* 3346 */     return illegalIorToSocketInfoType(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomIorToSocketInfo(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3352 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079713, paramCompletionStatus);
/* 3353 */     if (paramThrowable != null) {
/* 3354 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3356 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3357 */       Object[] arrayOfObject = new Object[1];
/* 3358 */       arrayOfObject[0] = paramObject;
/* 3359 */       doLog(Level.WARNING, "ORBUTIL.badCustomIorToSocketInfo", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3363 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomIorToSocketInfo(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3367 */     return badCustomIorToSocketInfo(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomIorToSocketInfo(Throwable paramThrowable, Object paramObject) {
/* 3371 */     return badCustomIorToSocketInfo(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomIorToSocketInfo(Object paramObject) {
/* 3375 */     return badCustomIorToSocketInfo(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalIiopPrimaryToContactInfoType(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3381 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079714, paramCompletionStatus);
/* 3382 */     if (paramThrowable != null) {
/* 3383 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3385 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3386 */       Object[] arrayOfObject = new Object[1];
/* 3387 */       arrayOfObject[0] = paramObject;
/* 3388 */       doLog(Level.WARNING, "ORBUTIL.illegalIiopPrimaryToContactInfoType", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3392 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalIiopPrimaryToContactInfoType(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3396 */     return illegalIiopPrimaryToContactInfoType(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalIiopPrimaryToContactInfoType(Throwable paramThrowable, Object paramObject) {
/* 3400 */     return illegalIiopPrimaryToContactInfoType(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION illegalIiopPrimaryToContactInfoType(Object paramObject) {
/* 3404 */     return illegalIiopPrimaryToContactInfoType(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomIiopPrimaryToContactInfo(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 3410 */     DATA_CONVERSION localDATA_CONVERSION = new DATA_CONVERSION(1398079715, paramCompletionStatus);
/* 3411 */     if (paramThrowable != null) {
/* 3412 */       localDATA_CONVERSION.initCause(paramThrowable);
/*      */     }
/* 3414 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3415 */       Object[] arrayOfObject = new Object[1];
/* 3416 */       arrayOfObject[0] = paramObject;
/* 3417 */       doLog(Level.WARNING, "ORBUTIL.badCustomIiopPrimaryToContactInfo", arrayOfObject, ORBUtilSystemException.class, localDATA_CONVERSION);
/*      */     }
/*      */ 
/* 3421 */     return localDATA_CONVERSION;
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomIiopPrimaryToContactInfo(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 3425 */     return badCustomIiopPrimaryToContactInfo(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomIiopPrimaryToContactInfo(Throwable paramThrowable, Object paramObject) {
/* 3429 */     return badCustomIiopPrimaryToContactInfo(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public DATA_CONVERSION badCustomIiopPrimaryToContactInfo(Object paramObject) {
/* 3433 */     return badCustomIiopPrimaryToContactInfo(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF badCorbalocString(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3443 */     INV_OBJREF localINV_OBJREF = new INV_OBJREF(1398079689, paramCompletionStatus);
/* 3444 */     if (paramThrowable != null) {
/* 3445 */       localINV_OBJREF.initCause(paramThrowable);
/*      */     }
/* 3447 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3448 */       Object[] arrayOfObject = null;
/* 3449 */       doLog(Level.WARNING, "ORBUTIL.badCorbalocString", arrayOfObject, ORBUtilSystemException.class, localINV_OBJREF);
/*      */     }
/*      */ 
/* 3453 */     return localINV_OBJREF;
/*      */   }
/*      */ 
/*      */   public INV_OBJREF badCorbalocString(CompletionStatus paramCompletionStatus) {
/* 3457 */     return badCorbalocString(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF badCorbalocString(Throwable paramThrowable) {
/* 3461 */     return badCorbalocString(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF badCorbalocString() {
/* 3465 */     return badCorbalocString(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF noProfilePresent(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3471 */     INV_OBJREF localINV_OBJREF = new INV_OBJREF(1398079690, paramCompletionStatus);
/* 3472 */     if (paramThrowable != null) {
/* 3473 */       localINV_OBJREF.initCause(paramThrowable);
/*      */     }
/* 3475 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3476 */       Object[] arrayOfObject = null;
/* 3477 */       doLog(Level.WARNING, "ORBUTIL.noProfilePresent", arrayOfObject, ORBUtilSystemException.class, localINV_OBJREF);
/*      */     }
/*      */ 
/* 3481 */     return localINV_OBJREF;
/*      */   }
/*      */ 
/*      */   public INV_OBJREF noProfilePresent(CompletionStatus paramCompletionStatus) {
/* 3485 */     return noProfilePresent(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF noProfilePresent(Throwable paramThrowable) {
/* 3489 */     return noProfilePresent(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INV_OBJREF noProfilePresent() {
/* 3493 */     return noProfilePresent(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotCreateOrbidDb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3503 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398079689, paramCompletionStatus);
/* 3504 */     if (paramThrowable != null) {
/* 3505 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 3507 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3508 */       Object[] arrayOfObject = null;
/* 3509 */       doLog(Level.WARNING, "ORBUTIL.cannotCreateOrbidDb", arrayOfObject, ORBUtilSystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 3513 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotCreateOrbidDb(CompletionStatus paramCompletionStatus) {
/* 3517 */     return cannotCreateOrbidDb(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotCreateOrbidDb(Throwable paramThrowable) {
/* 3521 */     return cannotCreateOrbidDb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotCreateOrbidDb() {
/* 3525 */     return cannotCreateOrbidDb(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotReadOrbidDb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3531 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398079690, paramCompletionStatus);
/* 3532 */     if (paramThrowable != null) {
/* 3533 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 3535 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3536 */       Object[] arrayOfObject = null;
/* 3537 */       doLog(Level.WARNING, "ORBUTIL.cannotReadOrbidDb", arrayOfObject, ORBUtilSystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 3541 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotReadOrbidDb(CompletionStatus paramCompletionStatus) {
/* 3545 */     return cannotReadOrbidDb(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotReadOrbidDb(Throwable paramThrowable) {
/* 3549 */     return cannotReadOrbidDb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotReadOrbidDb() {
/* 3553 */     return cannotReadOrbidDb(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotWriteOrbidDb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3559 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398079691, paramCompletionStatus);
/* 3560 */     if (paramThrowable != null) {
/* 3561 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 3563 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3564 */       Object[] arrayOfObject = null;
/* 3565 */       doLog(Level.WARNING, "ORBUTIL.cannotWriteOrbidDb", arrayOfObject, ORBUtilSystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 3569 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotWriteOrbidDb(CompletionStatus paramCompletionStatus) {
/* 3573 */     return cannotWriteOrbidDb(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotWriteOrbidDb(Throwable paramThrowable) {
/* 3577 */     return cannotWriteOrbidDb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE cannotWriteOrbidDb() {
/* 3581 */     return cannotWriteOrbidDb(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE getServerPortCalledBeforeEndpointsInitialized(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3587 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398079692, paramCompletionStatus);
/* 3588 */     if (paramThrowable != null) {
/* 3589 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 3591 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3592 */       Object[] arrayOfObject = null;
/* 3593 */       doLog(Level.WARNING, "ORBUTIL.getServerPortCalledBeforeEndpointsInitialized", arrayOfObject, ORBUtilSystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 3597 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE getServerPortCalledBeforeEndpointsInitialized(CompletionStatus paramCompletionStatus) {
/* 3601 */     return getServerPortCalledBeforeEndpointsInitialized(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE getServerPortCalledBeforeEndpointsInitialized(Throwable paramThrowable) {
/* 3605 */     return getServerPortCalledBeforeEndpointsInitialized(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE getServerPortCalledBeforeEndpointsInitialized() {
/* 3609 */     return getServerPortCalledBeforeEndpointsInitialized(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServerportNotSet(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3615 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398079693, paramCompletionStatus);
/* 3616 */     if (paramThrowable != null) {
/* 3617 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 3619 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3620 */       Object[] arrayOfObject = null;
/* 3621 */       doLog(Level.WARNING, "ORBUTIL.persistentServerportNotSet", arrayOfObject, ORBUtilSystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 3625 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServerportNotSet(CompletionStatus paramCompletionStatus) {
/* 3629 */     return persistentServerportNotSet(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServerportNotSet(Throwable paramThrowable) {
/* 3633 */     return persistentServerportNotSet(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServerportNotSet() {
/* 3637 */     return persistentServerportNotSet(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServeridNotSet(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3643 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398079694, paramCompletionStatus);
/* 3644 */     if (paramThrowable != null) {
/* 3645 */       localINITIALIZE.initCause(paramThrowable);
/*      */     }
/* 3647 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3648 */       Object[] arrayOfObject = null;
/* 3649 */       doLog(Level.WARNING, "ORBUTIL.persistentServeridNotSet", arrayOfObject, ORBUtilSystemException.class, localINITIALIZE);
/*      */     }
/*      */ 
/* 3653 */     return localINITIALIZE;
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServeridNotSet(CompletionStatus paramCompletionStatus) {
/* 3657 */     return persistentServeridNotSet(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServeridNotSet(Throwable paramThrowable) {
/* 3661 */     return persistentServeridNotSet(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INITIALIZE persistentServeridNotSet() {
/* 3665 */     return persistentServeridNotSet(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL nonExistentOrbid(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3675 */     INTERNAL localINTERNAL = new INTERNAL(1398079689, paramCompletionStatus);
/* 3676 */     if (paramThrowable != null) {
/* 3677 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3679 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3680 */       Object[] arrayOfObject = null;
/* 3681 */       doLog(Level.WARNING, "ORBUTIL.nonExistentOrbid", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3685 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL nonExistentOrbid(CompletionStatus paramCompletionStatus) {
/* 3689 */     return nonExistentOrbid(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL nonExistentOrbid(Throwable paramThrowable) {
/* 3693 */     return nonExistentOrbid(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL nonExistentOrbid() {
/* 3697 */     return nonExistentOrbid(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noServerSubcontract(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3703 */     INTERNAL localINTERNAL = new INTERNAL(1398079690, paramCompletionStatus);
/* 3704 */     if (paramThrowable != null) {
/* 3705 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3707 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3708 */       Object[] arrayOfObject = null;
/* 3709 */       doLog(Level.WARNING, "ORBUTIL.noServerSubcontract", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3713 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL noServerSubcontract(CompletionStatus paramCompletionStatus) {
/* 3717 */     return noServerSubcontract(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noServerSubcontract(Throwable paramThrowable) {
/* 3721 */     return noServerSubcontract(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL noServerSubcontract() {
/* 3725 */     return noServerSubcontract(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL serverScTempSize(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3731 */     INTERNAL localINTERNAL = new INTERNAL(1398079691, paramCompletionStatus);
/* 3732 */     if (paramThrowable != null) {
/* 3733 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3735 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3736 */       Object[] arrayOfObject = null;
/* 3737 */       doLog(Level.WARNING, "ORBUTIL.serverScTempSize", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3741 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL serverScTempSize(CompletionStatus paramCompletionStatus) {
/* 3745 */     return serverScTempSize(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL serverScTempSize(Throwable paramThrowable) {
/* 3749 */     return serverScTempSize(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL serverScTempSize() {
/* 3753 */     return serverScTempSize(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noClientScClass(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3759 */     INTERNAL localINTERNAL = new INTERNAL(1398079692, paramCompletionStatus);
/* 3760 */     if (paramThrowable != null) {
/* 3761 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3763 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3764 */       Object[] arrayOfObject = null;
/* 3765 */       doLog(Level.WARNING, "ORBUTIL.noClientScClass", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3769 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL noClientScClass(CompletionStatus paramCompletionStatus) {
/* 3773 */     return noClientScClass(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noClientScClass(Throwable paramThrowable) {
/* 3777 */     return noClientScClass(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL noClientScClass() {
/* 3781 */     return noClientScClass(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL serverScNoIiopProfile(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3787 */     INTERNAL localINTERNAL = new INTERNAL(1398079693, paramCompletionStatus);
/* 3788 */     if (paramThrowable != null) {
/* 3789 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3791 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3792 */       Object[] arrayOfObject = null;
/* 3793 */       doLog(Level.WARNING, "ORBUTIL.serverScNoIiopProfile", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3797 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL serverScNoIiopProfile(CompletionStatus paramCompletionStatus) {
/* 3801 */     return serverScNoIiopProfile(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL serverScNoIiopProfile(Throwable paramThrowable) {
/* 3805 */     return serverScNoIiopProfile(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL serverScNoIiopProfile() {
/* 3809 */     return serverScNoIiopProfile(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL getSystemExReturnedNull(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3815 */     INTERNAL localINTERNAL = new INTERNAL(1398079694, paramCompletionStatus);
/* 3816 */     if (paramThrowable != null) {
/* 3817 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3819 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3820 */       Object[] arrayOfObject = null;
/* 3821 */       doLog(Level.WARNING, "ORBUTIL.getSystemExReturnedNull", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3825 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL getSystemExReturnedNull(CompletionStatus paramCompletionStatus) {
/* 3829 */     return getSystemExReturnedNull(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL getSystemExReturnedNull(Throwable paramThrowable) {
/* 3833 */     return getSystemExReturnedNull(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL getSystemExReturnedNull() {
/* 3837 */     return getSystemExReturnedNull(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL peekstringFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3843 */     INTERNAL localINTERNAL = new INTERNAL(1398079695, paramCompletionStatus);
/* 3844 */     if (paramThrowable != null) {
/* 3845 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3847 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3848 */       Object[] arrayOfObject = null;
/* 3849 */       doLog(Level.WARNING, "ORBUTIL.peekstringFailed", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3853 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL peekstringFailed(CompletionStatus paramCompletionStatus) {
/* 3857 */     return peekstringFailed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL peekstringFailed(Throwable paramThrowable) {
/* 3861 */     return peekstringFailed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL peekstringFailed() {
/* 3865 */     return peekstringFailed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL getLocalHostFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3871 */     INTERNAL localINTERNAL = new INTERNAL(1398079696, paramCompletionStatus);
/* 3872 */     if (paramThrowable != null) {
/* 3873 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3875 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3876 */       Object[] arrayOfObject = null;
/* 3877 */       doLog(Level.WARNING, "ORBUTIL.getLocalHostFailed", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3881 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL getLocalHostFailed(CompletionStatus paramCompletionStatus) {
/* 3885 */     return getLocalHostFailed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL getLocalHostFailed(Throwable paramThrowable) {
/* 3889 */     return getLocalHostFailed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL getLocalHostFailed() {
/* 3893 */     return getLocalHostFailed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badLocateRequestStatus(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3899 */     INTERNAL localINTERNAL = new INTERNAL(1398079698, paramCompletionStatus);
/* 3900 */     if (paramThrowable != null) {
/* 3901 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3903 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3904 */       Object[] arrayOfObject = null;
/* 3905 */       doLog(Level.WARNING, "ORBUTIL.badLocateRequestStatus", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3909 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badLocateRequestStatus(CompletionStatus paramCompletionStatus) {
/* 3913 */     return badLocateRequestStatus(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badLocateRequestStatus(Throwable paramThrowable) {
/* 3917 */     return badLocateRequestStatus(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badLocateRequestStatus() {
/* 3921 */     return badLocateRequestStatus(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL stringifyWriteError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3927 */     INTERNAL localINTERNAL = new INTERNAL(1398079699, paramCompletionStatus);
/* 3928 */     if (paramThrowable != null) {
/* 3929 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3931 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3932 */       Object[] arrayOfObject = null;
/* 3933 */       doLog(Level.WARNING, "ORBUTIL.stringifyWriteError", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3937 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL stringifyWriteError(CompletionStatus paramCompletionStatus) {
/* 3941 */     return stringifyWriteError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL stringifyWriteError(Throwable paramThrowable) {
/* 3945 */     return stringifyWriteError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL stringifyWriteError() {
/* 3949 */     return stringifyWriteError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badGiopRequestType(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3955 */     INTERNAL localINTERNAL = new INTERNAL(1398079700, paramCompletionStatus);
/* 3956 */     if (paramThrowable != null) {
/* 3957 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3959 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3960 */       Object[] arrayOfObject = null;
/* 3961 */       doLog(Level.WARNING, "ORBUTIL.badGiopRequestType", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3965 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badGiopRequestType(CompletionStatus paramCompletionStatus) {
/* 3969 */     return badGiopRequestType(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badGiopRequestType(Throwable paramThrowable) {
/* 3973 */     return badGiopRequestType(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badGiopRequestType() {
/* 3977 */     return badGiopRequestType(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL errorUnmarshalingUserexc(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 3983 */     INTERNAL localINTERNAL = new INTERNAL(1398079701, paramCompletionStatus);
/* 3984 */     if (paramThrowable != null) {
/* 3985 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 3987 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 3988 */       Object[] arrayOfObject = null;
/* 3989 */       doLog(Level.WARNING, "ORBUTIL.errorUnmarshalingUserexc", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 3993 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL errorUnmarshalingUserexc(CompletionStatus paramCompletionStatus) {
/* 3997 */     return errorUnmarshalingUserexc(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL errorUnmarshalingUserexc(Throwable paramThrowable) {
/* 4001 */     return errorUnmarshalingUserexc(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL errorUnmarshalingUserexc() {
/* 4005 */     return errorUnmarshalingUserexc(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL requestdispatcherregistryError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4011 */     INTERNAL localINTERNAL = new INTERNAL(1398079702, paramCompletionStatus);
/* 4012 */     if (paramThrowable != null) {
/* 4013 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4015 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4016 */       Object[] arrayOfObject = null;
/* 4017 */       doLog(Level.WARNING, "ORBUTIL.requestdispatcherregistryError", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4021 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL requestdispatcherregistryError(CompletionStatus paramCompletionStatus) {
/* 4025 */     return requestdispatcherregistryError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL requestdispatcherregistryError(Throwable paramThrowable) {
/* 4029 */     return requestdispatcherregistryError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL requestdispatcherregistryError() {
/* 4033 */     return requestdispatcherregistryError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL locationforwardError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4039 */     INTERNAL localINTERNAL = new INTERNAL(1398079703, paramCompletionStatus);
/* 4040 */     if (paramThrowable != null) {
/* 4041 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4043 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4044 */       Object[] arrayOfObject = null;
/* 4045 */       doLog(Level.WARNING, "ORBUTIL.locationforwardError", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4049 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL locationforwardError(CompletionStatus paramCompletionStatus) {
/* 4053 */     return locationforwardError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL locationforwardError(Throwable paramThrowable) {
/* 4057 */     return locationforwardError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL locationforwardError() {
/* 4061 */     return locationforwardError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL wrongClientsc(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4067 */     INTERNAL localINTERNAL = new INTERNAL(1398079704, paramCompletionStatus);
/* 4068 */     if (paramThrowable != null) {
/* 4069 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4071 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4072 */       Object[] arrayOfObject = null;
/* 4073 */       doLog(Level.WARNING, "ORBUTIL.wrongClientsc", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4077 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL wrongClientsc(CompletionStatus paramCompletionStatus) {
/* 4081 */     return wrongClientsc(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL wrongClientsc(Throwable paramThrowable) {
/* 4085 */     return wrongClientsc(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL wrongClientsc() {
/* 4089 */     return wrongClientsc(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badServantReadObject(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4095 */     INTERNAL localINTERNAL = new INTERNAL(1398079705, paramCompletionStatus);
/* 4096 */     if (paramThrowable != null) {
/* 4097 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4099 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4100 */       Object[] arrayOfObject = null;
/* 4101 */       doLog(Level.WARNING, "ORBUTIL.badServantReadObject", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4105 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badServantReadObject(CompletionStatus paramCompletionStatus) {
/* 4109 */     return badServantReadObject(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badServantReadObject(Throwable paramThrowable) {
/* 4113 */     return badServantReadObject(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badServantReadObject() {
/* 4117 */     return badServantReadObject(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL multIiopProfNotSupported(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4123 */     INTERNAL localINTERNAL = new INTERNAL(1398079706, paramCompletionStatus);
/* 4124 */     if (paramThrowable != null) {
/* 4125 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4127 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4128 */       Object[] arrayOfObject = null;
/* 4129 */       doLog(Level.WARNING, "ORBUTIL.multIiopProfNotSupported", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4133 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL multIiopProfNotSupported(CompletionStatus paramCompletionStatus) {
/* 4137 */     return multIiopProfNotSupported(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL multIiopProfNotSupported(Throwable paramThrowable) {
/* 4141 */     return multIiopProfNotSupported(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL multIiopProfNotSupported() {
/* 4145 */     return multIiopProfNotSupported(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL giopMagicError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4151 */     INTERNAL localINTERNAL = new INTERNAL(1398079708, paramCompletionStatus);
/* 4152 */     if (paramThrowable != null) {
/* 4153 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4155 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4156 */       Object[] arrayOfObject = null;
/* 4157 */       doLog(Level.WARNING, "ORBUTIL.giopMagicError", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4161 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL giopMagicError(CompletionStatus paramCompletionStatus) {
/* 4165 */     return giopMagicError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL giopMagicError(Throwable paramThrowable) {
/* 4169 */     return giopMagicError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL giopMagicError() {
/* 4173 */     return giopMagicError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL giopVersionError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4179 */     INTERNAL localINTERNAL = new INTERNAL(1398079709, paramCompletionStatus);
/* 4180 */     if (paramThrowable != null) {
/* 4181 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4183 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4184 */       Object[] arrayOfObject = null;
/* 4185 */       doLog(Level.WARNING, "ORBUTIL.giopVersionError", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4189 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL giopVersionError(CompletionStatus paramCompletionStatus) {
/* 4193 */     return giopVersionError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL giopVersionError(Throwable paramThrowable) {
/* 4197 */     return giopVersionError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL giopVersionError() {
/* 4201 */     return giopVersionError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalReplyStatus(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4207 */     INTERNAL localINTERNAL = new INTERNAL(1398079710, paramCompletionStatus);
/* 4208 */     if (paramThrowable != null) {
/* 4209 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4211 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4212 */       Object[] arrayOfObject = null;
/* 4213 */       doLog(Level.WARNING, "ORBUTIL.illegalReplyStatus", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4217 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalReplyStatus(CompletionStatus paramCompletionStatus) {
/* 4221 */     return illegalReplyStatus(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalReplyStatus(Throwable paramThrowable) {
/* 4225 */     return illegalReplyStatus(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalReplyStatus() {
/* 4229 */     return illegalReplyStatus(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalGiopMsgType(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4235 */     INTERNAL localINTERNAL = new INTERNAL(1398079711, paramCompletionStatus);
/* 4236 */     if (paramThrowable != null) {
/* 4237 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4239 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4240 */       Object[] arrayOfObject = null;
/* 4241 */       doLog(Level.WARNING, "ORBUTIL.illegalGiopMsgType", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4245 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalGiopMsgType(CompletionStatus paramCompletionStatus) {
/* 4249 */     return illegalGiopMsgType(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalGiopMsgType(Throwable paramThrowable) {
/* 4253 */     return illegalGiopMsgType(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalGiopMsgType() {
/* 4257 */     return illegalGiopMsgType(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL fragmentationDisallowed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4263 */     INTERNAL localINTERNAL = new INTERNAL(1398079712, paramCompletionStatus);
/* 4264 */     if (paramThrowable != null) {
/* 4265 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4267 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4268 */       Object[] arrayOfObject = null;
/* 4269 */       doLog(Level.WARNING, "ORBUTIL.fragmentationDisallowed", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4273 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL fragmentationDisallowed(CompletionStatus paramCompletionStatus) {
/* 4277 */     return fragmentationDisallowed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL fragmentationDisallowed(Throwable paramThrowable) {
/* 4281 */     return fragmentationDisallowed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL fragmentationDisallowed() {
/* 4285 */     return fragmentationDisallowed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badReplystatus(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4291 */     INTERNAL localINTERNAL = new INTERNAL(1398079713, paramCompletionStatus);
/* 4292 */     if (paramThrowable != null) {
/* 4293 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4295 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4296 */       Object[] arrayOfObject = null;
/* 4297 */       doLog(Level.WARNING, "ORBUTIL.badReplystatus", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4301 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badReplystatus(CompletionStatus paramCompletionStatus) {
/* 4305 */     return badReplystatus(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badReplystatus(Throwable paramThrowable) {
/* 4309 */     return badReplystatus(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badReplystatus() {
/* 4313 */     return badReplystatus(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL ctbConverterFailure(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4319 */     INTERNAL localINTERNAL = new INTERNAL(1398079714, paramCompletionStatus);
/* 4320 */     if (paramThrowable != null) {
/* 4321 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4323 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4324 */       Object[] arrayOfObject = null;
/* 4325 */       doLog(Level.WARNING, "ORBUTIL.ctbConverterFailure", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4329 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL ctbConverterFailure(CompletionStatus paramCompletionStatus) {
/* 4333 */     return ctbConverterFailure(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL ctbConverterFailure(Throwable paramThrowable) {
/* 4337 */     return ctbConverterFailure(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL ctbConverterFailure() {
/* 4341 */     return ctbConverterFailure(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL btcConverterFailure(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4347 */     INTERNAL localINTERNAL = new INTERNAL(1398079715, paramCompletionStatus);
/* 4348 */     if (paramThrowable != null) {
/* 4349 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4351 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4352 */       Object[] arrayOfObject = null;
/* 4353 */       doLog(Level.WARNING, "ORBUTIL.btcConverterFailure", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4357 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL btcConverterFailure(CompletionStatus paramCompletionStatus) {
/* 4361 */     return btcConverterFailure(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL btcConverterFailure(Throwable paramThrowable) {
/* 4365 */     return btcConverterFailure(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL btcConverterFailure() {
/* 4369 */     return btcConverterFailure(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL wcharArrayUnsupportedEncoding(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4375 */     INTERNAL localINTERNAL = new INTERNAL(1398079716, paramCompletionStatus);
/* 4376 */     if (paramThrowable != null) {
/* 4377 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4379 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4380 */       Object[] arrayOfObject = null;
/* 4381 */       doLog(Level.WARNING, "ORBUTIL.wcharArrayUnsupportedEncoding", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4385 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL wcharArrayUnsupportedEncoding(CompletionStatus paramCompletionStatus) {
/* 4389 */     return wcharArrayUnsupportedEncoding(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL wcharArrayUnsupportedEncoding(Throwable paramThrowable) {
/* 4393 */     return wcharArrayUnsupportedEncoding(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL wcharArrayUnsupportedEncoding() {
/* 4397 */     return wcharArrayUnsupportedEncoding(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalTargetAddressDisposition(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4403 */     INTERNAL localINTERNAL = new INTERNAL(1398079717, paramCompletionStatus);
/* 4404 */     if (paramThrowable != null) {
/* 4405 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4407 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4408 */       Object[] arrayOfObject = null;
/* 4409 */       doLog(Level.WARNING, "ORBUTIL.illegalTargetAddressDisposition", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4413 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalTargetAddressDisposition(CompletionStatus paramCompletionStatus) {
/* 4417 */     return illegalTargetAddressDisposition(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalTargetAddressDisposition(Throwable paramThrowable) {
/* 4421 */     return illegalTargetAddressDisposition(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalTargetAddressDisposition() {
/* 4425 */     return illegalTargetAddressDisposition(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL nullReplyInGetAddrDisposition(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4431 */     INTERNAL localINTERNAL = new INTERNAL(1398079718, paramCompletionStatus);
/* 4432 */     if (paramThrowable != null) {
/* 4433 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4435 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4436 */       Object[] arrayOfObject = null;
/* 4437 */       doLog(Level.WARNING, "ORBUTIL.nullReplyInGetAddrDisposition", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4441 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL nullReplyInGetAddrDisposition(CompletionStatus paramCompletionStatus) {
/* 4445 */     return nullReplyInGetAddrDisposition(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL nullReplyInGetAddrDisposition(Throwable paramThrowable) {
/* 4449 */     return nullReplyInGetAddrDisposition(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL nullReplyInGetAddrDisposition() {
/* 4453 */     return nullReplyInGetAddrDisposition(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL orbTargetAddrPreferenceInExtractObjectkeyInvalid(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4459 */     INTERNAL localINTERNAL = new INTERNAL(1398079719, paramCompletionStatus);
/* 4460 */     if (paramThrowable != null) {
/* 4461 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4463 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4464 */       Object[] arrayOfObject = null;
/* 4465 */       doLog(Level.WARNING, "ORBUTIL.orbTargetAddrPreferenceInExtractObjectkeyInvalid", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4469 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL orbTargetAddrPreferenceInExtractObjectkeyInvalid(CompletionStatus paramCompletionStatus) {
/* 4473 */     return orbTargetAddrPreferenceInExtractObjectkeyInvalid(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL orbTargetAddrPreferenceInExtractObjectkeyInvalid(Throwable paramThrowable) {
/* 4477 */     return orbTargetAddrPreferenceInExtractObjectkeyInvalid(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL orbTargetAddrPreferenceInExtractObjectkeyInvalid() {
/* 4481 */     return orbTargetAddrPreferenceInExtractObjectkeyInvalid(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidIsstreamedTckind(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 4487 */     INTERNAL localINTERNAL = new INTERNAL(1398079720, paramCompletionStatus);
/* 4488 */     if (paramThrowable != null) {
/* 4489 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4491 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4492 */       Object[] arrayOfObject = new Object[1];
/* 4493 */       arrayOfObject[0] = paramObject;
/* 4494 */       doLog(Level.WARNING, "ORBUTIL.invalidIsstreamedTckind", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4498 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidIsstreamedTckind(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 4502 */     return invalidIsstreamedTckind(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidIsstreamedTckind(Throwable paramThrowable, Object paramObject) {
/* 4506 */     return invalidIsstreamedTckind(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidIsstreamedTckind(Object paramObject) {
/* 4510 */     return invalidIsstreamedTckind(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidJdk131PatchLevel(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4516 */     INTERNAL localINTERNAL = new INTERNAL(1398079721, paramCompletionStatus);
/* 4517 */     if (paramThrowable != null) {
/* 4518 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4520 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4521 */       Object[] arrayOfObject = null;
/* 4522 */       doLog(Level.WARNING, "ORBUTIL.invalidJdk131PatchLevel", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4526 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidJdk131PatchLevel(CompletionStatus paramCompletionStatus) {
/* 4530 */     return invalidJdk131PatchLevel(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidJdk131PatchLevel(Throwable paramThrowable) {
/* 4534 */     return invalidJdk131PatchLevel(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidJdk131PatchLevel() {
/* 4538 */     return invalidJdk131PatchLevel(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL svcctxUnmarshalError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4544 */     INTERNAL localINTERNAL = new INTERNAL(1398079722, paramCompletionStatus);
/* 4545 */     if (paramThrowable != null) {
/* 4546 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4548 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4549 */       Object[] arrayOfObject = null;
/* 4550 */       doLog(Level.WARNING, "ORBUTIL.svcctxUnmarshalError", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4554 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL svcctxUnmarshalError(CompletionStatus paramCompletionStatus) {
/* 4558 */     return svcctxUnmarshalError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL svcctxUnmarshalError(Throwable paramThrowable) {
/* 4562 */     return svcctxUnmarshalError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL svcctxUnmarshalError() {
/* 4566 */     return svcctxUnmarshalError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL nullIor(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4572 */     INTERNAL localINTERNAL = new INTERNAL(1398079723, paramCompletionStatus);
/* 4573 */     if (paramThrowable != null) {
/* 4574 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4576 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4577 */       Object[] arrayOfObject = null;
/* 4578 */       doLog(Level.WARNING, "ORBUTIL.nullIor", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4582 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL nullIor(CompletionStatus paramCompletionStatus) {
/* 4586 */     return nullIor(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL nullIor(Throwable paramThrowable) {
/* 4590 */     return nullIor(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL nullIor() {
/* 4594 */     return nullIor(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unsupportedGiopVersion(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 4600 */     INTERNAL localINTERNAL = new INTERNAL(1398079724, paramCompletionStatus);
/* 4601 */     if (paramThrowable != null) {
/* 4602 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4604 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4605 */       Object[] arrayOfObject = new Object[1];
/* 4606 */       arrayOfObject[0] = paramObject;
/* 4607 */       doLog(Level.WARNING, "ORBUTIL.unsupportedGiopVersion", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4611 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL unsupportedGiopVersion(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 4615 */     return unsupportedGiopVersion(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unsupportedGiopVersion(Throwable paramThrowable, Object paramObject) {
/* 4619 */     return unsupportedGiopVersion(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unsupportedGiopVersion(Object paramObject) {
/* 4623 */     return unsupportedGiopVersion(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL applicationExceptionInSpecialMethod(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4629 */     INTERNAL localINTERNAL = new INTERNAL(1398079725, paramCompletionStatus);
/* 4630 */     if (paramThrowable != null) {
/* 4631 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4633 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4634 */       Object[] arrayOfObject = null;
/* 4635 */       doLog(Level.WARNING, "ORBUTIL.applicationExceptionInSpecialMethod", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4639 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL applicationExceptionInSpecialMethod(CompletionStatus paramCompletionStatus) {
/* 4643 */     return applicationExceptionInSpecialMethod(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL applicationExceptionInSpecialMethod(Throwable paramThrowable) {
/* 4647 */     return applicationExceptionInSpecialMethod(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL applicationExceptionInSpecialMethod() {
/* 4651 */     return applicationExceptionInSpecialMethod(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable1(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4657 */     INTERNAL localINTERNAL = new INTERNAL(1398079726, paramCompletionStatus);
/* 4658 */     if (paramThrowable != null) {
/* 4659 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4661 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4662 */       Object[] arrayOfObject = null;
/* 4663 */       doLog(Level.WARNING, "ORBUTIL.statementNotReachable1", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4667 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable1(CompletionStatus paramCompletionStatus) {
/* 4671 */     return statementNotReachable1(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable1(Throwable paramThrowable) {
/* 4675 */     return statementNotReachable1(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable1() {
/* 4679 */     return statementNotReachable1(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable2(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4685 */     INTERNAL localINTERNAL = new INTERNAL(1398079727, paramCompletionStatus);
/* 4686 */     if (paramThrowable != null) {
/* 4687 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4689 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4690 */       Object[] arrayOfObject = null;
/* 4691 */       doLog(Level.WARNING, "ORBUTIL.statementNotReachable2", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4695 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable2(CompletionStatus paramCompletionStatus) {
/* 4699 */     return statementNotReachable2(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable2(Throwable paramThrowable) {
/* 4703 */     return statementNotReachable2(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable2() {
/* 4707 */     return statementNotReachable2(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable3(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4713 */     INTERNAL localINTERNAL = new INTERNAL(1398079728, paramCompletionStatus);
/* 4714 */     if (paramThrowable != null) {
/* 4715 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4717 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4718 */       Object[] arrayOfObject = null;
/* 4719 */       doLog(Level.WARNING, "ORBUTIL.statementNotReachable3", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4723 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable3(CompletionStatus paramCompletionStatus) {
/* 4727 */     return statementNotReachable3(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable3(Throwable paramThrowable) {
/* 4731 */     return statementNotReachable3(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable3() {
/* 4735 */     return statementNotReachable3(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable4(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4741 */     INTERNAL localINTERNAL = new INTERNAL(1398079729, paramCompletionStatus);
/* 4742 */     if (paramThrowable != null) {
/* 4743 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4745 */     if (this.logger.isLoggable(Level.FINE)) {
/* 4746 */       Object[] arrayOfObject = null;
/* 4747 */       doLog(Level.FINE, "ORBUTIL.statementNotReachable4", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4751 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable4(CompletionStatus paramCompletionStatus) {
/* 4755 */     return statementNotReachable4(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable4(Throwable paramThrowable) {
/* 4759 */     return statementNotReachable4(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable4() {
/* 4763 */     return statementNotReachable4(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable5(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4769 */     INTERNAL localINTERNAL = new INTERNAL(1398079730, paramCompletionStatus);
/* 4770 */     if (paramThrowable != null) {
/* 4771 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4773 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4774 */       Object[] arrayOfObject = null;
/* 4775 */       doLog(Level.WARNING, "ORBUTIL.statementNotReachable5", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4779 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable5(CompletionStatus paramCompletionStatus) {
/* 4783 */     return statementNotReachable5(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable5(Throwable paramThrowable) {
/* 4787 */     return statementNotReachable5(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable5() {
/* 4791 */     return statementNotReachable5(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable6(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4797 */     INTERNAL localINTERNAL = new INTERNAL(1398079731, paramCompletionStatus);
/* 4798 */     if (paramThrowable != null) {
/* 4799 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4801 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4802 */       Object[] arrayOfObject = null;
/* 4803 */       doLog(Level.WARNING, "ORBUTIL.statementNotReachable6", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4807 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable6(CompletionStatus paramCompletionStatus) {
/* 4811 */     return statementNotReachable6(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable6(Throwable paramThrowable) {
/* 4815 */     return statementNotReachable6(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL statementNotReachable6() {
/* 4819 */     return statementNotReachable6(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedDiiException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4825 */     INTERNAL localINTERNAL = new INTERNAL(1398079732, paramCompletionStatus);
/* 4826 */     if (paramThrowable != null) {
/* 4827 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4829 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4830 */       Object[] arrayOfObject = null;
/* 4831 */       doLog(Level.WARNING, "ORBUTIL.unexpectedDiiException", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4835 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedDiiException(CompletionStatus paramCompletionStatus) {
/* 4839 */     return unexpectedDiiException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedDiiException(Throwable paramThrowable) {
/* 4843 */     return unexpectedDiiException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedDiiException() {
/* 4847 */     return unexpectedDiiException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL methodShouldNotBeCalled(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4853 */     INTERNAL localINTERNAL = new INTERNAL(1398079733, paramCompletionStatus);
/* 4854 */     if (paramThrowable != null) {
/* 4855 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4857 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4858 */       Object[] arrayOfObject = null;
/* 4859 */       doLog(Level.WARNING, "ORBUTIL.methodShouldNotBeCalled", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4863 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL methodShouldNotBeCalled(CompletionStatus paramCompletionStatus) {
/* 4867 */     return methodShouldNotBeCalled(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL methodShouldNotBeCalled(Throwable paramThrowable) {
/* 4871 */     return methodShouldNotBeCalled(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL methodShouldNotBeCalled() {
/* 4875 */     return methodShouldNotBeCalled(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL cancelNotSupported(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4881 */     INTERNAL localINTERNAL = new INTERNAL(1398079734, paramCompletionStatus);
/* 4882 */     if (paramThrowable != null) {
/* 4883 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4885 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4886 */       Object[] arrayOfObject = null;
/* 4887 */       doLog(Level.WARNING, "ORBUTIL.cancelNotSupported", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4891 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL cancelNotSupported(CompletionStatus paramCompletionStatus) {
/* 4895 */     return cancelNotSupported(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL cancelNotSupported(Throwable paramThrowable) {
/* 4899 */     return cancelNotSupported(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL cancelNotSupported() {
/* 4903 */     return cancelNotSupported(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL emptyStackRunServantPostInvoke(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4909 */     INTERNAL localINTERNAL = new INTERNAL(1398079735, paramCompletionStatus);
/* 4910 */     if (paramThrowable != null) {
/* 4911 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4913 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4914 */       Object[] arrayOfObject = null;
/* 4915 */       doLog(Level.WARNING, "ORBUTIL.emptyStackRunServantPostInvoke", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4919 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL emptyStackRunServantPostInvoke(CompletionStatus paramCompletionStatus) {
/* 4923 */     return emptyStackRunServantPostInvoke(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL emptyStackRunServantPostInvoke(Throwable paramThrowable) {
/* 4927 */     return emptyStackRunServantPostInvoke(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL emptyStackRunServantPostInvoke() {
/* 4931 */     return emptyStackRunServantPostInvoke(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL problemWithExceptionTypecode(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4937 */     INTERNAL localINTERNAL = new INTERNAL(1398079736, paramCompletionStatus);
/* 4938 */     if (paramThrowable != null) {
/* 4939 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4941 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4942 */       Object[] arrayOfObject = null;
/* 4943 */       doLog(Level.WARNING, "ORBUTIL.problemWithExceptionTypecode", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4947 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL problemWithExceptionTypecode(CompletionStatus paramCompletionStatus) {
/* 4951 */     return problemWithExceptionTypecode(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL problemWithExceptionTypecode(Throwable paramThrowable) {
/* 4955 */     return problemWithExceptionTypecode(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL problemWithExceptionTypecode() {
/* 4959 */     return problemWithExceptionTypecode(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalSubcontractId(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 4965 */     INTERNAL localINTERNAL = new INTERNAL(1398079737, paramCompletionStatus);
/* 4966 */     if (paramThrowable != null) {
/* 4967 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4969 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4970 */       Object[] arrayOfObject = new Object[1];
/* 4971 */       arrayOfObject[0] = paramObject;
/* 4972 */       doLog(Level.WARNING, "ORBUTIL.illegalSubcontractId", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 4976 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalSubcontractId(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 4980 */     return illegalSubcontractId(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalSubcontractId(Throwable paramThrowable, Object paramObject) {
/* 4984 */     return illegalSubcontractId(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL illegalSubcontractId(Object paramObject) {
/* 4988 */     return illegalSubcontractId(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badSystemExceptionInLocateReply(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 4994 */     INTERNAL localINTERNAL = new INTERNAL(1398079738, paramCompletionStatus);
/* 4995 */     if (paramThrowable != null) {
/* 4996 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 4998 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 4999 */       Object[] arrayOfObject = null;
/* 5000 */       doLog(Level.WARNING, "ORBUTIL.badSystemExceptionInLocateReply", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5004 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badSystemExceptionInLocateReply(CompletionStatus paramCompletionStatus) {
/* 5008 */     return badSystemExceptionInLocateReply(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badSystemExceptionInLocateReply(Throwable paramThrowable) {
/* 5012 */     return badSystemExceptionInLocateReply(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badSystemExceptionInLocateReply() {
/* 5016 */     return badSystemExceptionInLocateReply(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badSystemExceptionInReply(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5022 */     INTERNAL localINTERNAL = new INTERNAL(1398079739, paramCompletionStatus);
/* 5023 */     if (paramThrowable != null) {
/* 5024 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5026 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5027 */       Object[] arrayOfObject = null;
/* 5028 */       doLog(Level.WARNING, "ORBUTIL.badSystemExceptionInReply", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5032 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badSystemExceptionInReply(CompletionStatus paramCompletionStatus) {
/* 5036 */     return badSystemExceptionInReply(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badSystemExceptionInReply(Throwable paramThrowable) {
/* 5040 */     return badSystemExceptionInReply(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badSystemExceptionInReply() {
/* 5044 */     return badSystemExceptionInReply(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badCompletionStatusInLocateReply(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5050 */     INTERNAL localINTERNAL = new INTERNAL(1398079740, paramCompletionStatus);
/* 5051 */     if (paramThrowable != null) {
/* 5052 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5054 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5055 */       Object[] arrayOfObject = new Object[1];
/* 5056 */       arrayOfObject[0] = paramObject;
/* 5057 */       doLog(Level.WARNING, "ORBUTIL.badCompletionStatusInLocateReply", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5061 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badCompletionStatusInLocateReply(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 5065 */     return badCompletionStatusInLocateReply(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badCompletionStatusInLocateReply(Throwable paramThrowable, Object paramObject) {
/* 5069 */     return badCompletionStatusInLocateReply(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badCompletionStatusInLocateReply(Object paramObject) {
/* 5073 */     return badCompletionStatusInLocateReply(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badCompletionStatusInReply(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5079 */     INTERNAL localINTERNAL = new INTERNAL(1398079741, paramCompletionStatus);
/* 5080 */     if (paramThrowable != null) {
/* 5081 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5083 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5084 */       Object[] arrayOfObject = new Object[1];
/* 5085 */       arrayOfObject[0] = paramObject;
/* 5086 */       doLog(Level.WARNING, "ORBUTIL.badCompletionStatusInReply", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5090 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badCompletionStatusInReply(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 5094 */     return badCompletionStatusInReply(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badCompletionStatusInReply(Throwable paramThrowable, Object paramObject) {
/* 5098 */     return badCompletionStatusInReply(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badCompletionStatusInReply(Object paramObject) {
/* 5102 */     return badCompletionStatusInReply(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badkindCannotOccur(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5108 */     INTERNAL localINTERNAL = new INTERNAL(1398079742, paramCompletionStatus);
/* 5109 */     if (paramThrowable != null) {
/* 5110 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5112 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5113 */       Object[] arrayOfObject = null;
/* 5114 */       doLog(Level.WARNING, "ORBUTIL.badkindCannotOccur", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5118 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badkindCannotOccur(CompletionStatus paramCompletionStatus) {
/* 5122 */     return badkindCannotOccur(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badkindCannotOccur(Throwable paramThrowable) {
/* 5126 */     return badkindCannotOccur(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badkindCannotOccur() {
/* 5130 */     return badkindCannotOccur(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL errorResolvingAlias(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5136 */     INTERNAL localINTERNAL = new INTERNAL(1398079743, paramCompletionStatus);
/* 5137 */     if (paramThrowable != null) {
/* 5138 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5140 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5141 */       Object[] arrayOfObject = null;
/* 5142 */       doLog(Level.WARNING, "ORBUTIL.errorResolvingAlias", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5146 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL errorResolvingAlias(CompletionStatus paramCompletionStatus) {
/* 5150 */     return errorResolvingAlias(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL errorResolvingAlias(Throwable paramThrowable) {
/* 5154 */     return errorResolvingAlias(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL errorResolvingAlias() {
/* 5158 */     return errorResolvingAlias(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL tkLongDoubleNotSupported(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5164 */     INTERNAL localINTERNAL = new INTERNAL(1398079744, paramCompletionStatus);
/* 5165 */     if (paramThrowable != null) {
/* 5166 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5168 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5169 */       Object[] arrayOfObject = null;
/* 5170 */       doLog(Level.WARNING, "ORBUTIL.tkLongDoubleNotSupported", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5174 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL tkLongDoubleNotSupported(CompletionStatus paramCompletionStatus) {
/* 5178 */     return tkLongDoubleNotSupported(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL tkLongDoubleNotSupported(Throwable paramThrowable) {
/* 5182 */     return tkLongDoubleNotSupported(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL tkLongDoubleNotSupported() {
/* 5186 */     return tkLongDoubleNotSupported(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL typecodeNotSupported(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5192 */     INTERNAL localINTERNAL = new INTERNAL(1398079745, paramCompletionStatus);
/* 5193 */     if (paramThrowable != null) {
/* 5194 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5196 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5197 */       Object[] arrayOfObject = null;
/* 5198 */       doLog(Level.WARNING, "ORBUTIL.typecodeNotSupported", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5202 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL typecodeNotSupported(CompletionStatus paramCompletionStatus) {
/* 5206 */     return typecodeNotSupported(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL typecodeNotSupported(Throwable paramThrowable) {
/* 5210 */     return typecodeNotSupported(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL typecodeNotSupported() {
/* 5214 */     return typecodeNotSupported(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL boundsCannotOccur(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5220 */     INTERNAL localINTERNAL = new INTERNAL(1398079747, paramCompletionStatus);
/* 5221 */     if (paramThrowable != null) {
/* 5222 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5224 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5225 */       Object[] arrayOfObject = null;
/* 5226 */       doLog(Level.WARNING, "ORBUTIL.boundsCannotOccur", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5230 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL boundsCannotOccur(CompletionStatus paramCompletionStatus) {
/* 5234 */     return boundsCannotOccur(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL boundsCannotOccur(Throwable paramThrowable) {
/* 5238 */     return boundsCannotOccur(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL boundsCannotOccur() {
/* 5242 */     return boundsCannotOccur(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL numInvocationsAlreadyZero(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5248 */     INTERNAL localINTERNAL = new INTERNAL(1398079749, paramCompletionStatus);
/* 5249 */     if (paramThrowable != null) {
/* 5250 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5252 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5253 */       Object[] arrayOfObject = null;
/* 5254 */       doLog(Level.WARNING, "ORBUTIL.numInvocationsAlreadyZero", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5258 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL numInvocationsAlreadyZero(CompletionStatus paramCompletionStatus) {
/* 5262 */     return numInvocationsAlreadyZero(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL numInvocationsAlreadyZero(Throwable paramThrowable) {
/* 5266 */     return numInvocationsAlreadyZero(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL numInvocationsAlreadyZero() {
/* 5270 */     return numInvocationsAlreadyZero(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL errorInitBadserveridhandler(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5276 */     INTERNAL localINTERNAL = new INTERNAL(1398079750, paramCompletionStatus);
/* 5277 */     if (paramThrowable != null) {
/* 5278 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5280 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5281 */       Object[] arrayOfObject = null;
/* 5282 */       doLog(Level.WARNING, "ORBUTIL.errorInitBadserveridhandler", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5286 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL errorInitBadserveridhandler(CompletionStatus paramCompletionStatus) {
/* 5290 */     return errorInitBadserveridhandler(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL errorInitBadserveridhandler(Throwable paramThrowable) {
/* 5294 */     return errorInitBadserveridhandler(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL errorInitBadserveridhandler() {
/* 5298 */     return errorInitBadserveridhandler(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noToa(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5304 */     INTERNAL localINTERNAL = new INTERNAL(1398079751, paramCompletionStatus);
/* 5305 */     if (paramThrowable != null) {
/* 5306 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5308 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5309 */       Object[] arrayOfObject = null;
/* 5310 */       doLog(Level.WARNING, "ORBUTIL.noToa", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5314 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL noToa(CompletionStatus paramCompletionStatus) {
/* 5318 */     return noToa(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noToa(Throwable paramThrowable) {
/* 5322 */     return noToa(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL noToa() {
/* 5326 */     return noToa(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noPoa(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5332 */     INTERNAL localINTERNAL = new INTERNAL(1398079752, paramCompletionStatus);
/* 5333 */     if (paramThrowable != null) {
/* 5334 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5336 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5337 */       Object[] arrayOfObject = null;
/* 5338 */       doLog(Level.WARNING, "ORBUTIL.noPoa", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5342 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL noPoa(CompletionStatus paramCompletionStatus) {
/* 5346 */     return noPoa(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noPoa(Throwable paramThrowable) {
/* 5350 */     return noPoa(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL noPoa() {
/* 5354 */     return noPoa(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invocationInfoStackEmpty(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5360 */     INTERNAL localINTERNAL = new INTERNAL(1398079753, paramCompletionStatus);
/* 5361 */     if (paramThrowable != null) {
/* 5362 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5364 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5365 */       Object[] arrayOfObject = null;
/* 5366 */       doLog(Level.WARNING, "ORBUTIL.invocationInfoStackEmpty", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5370 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invocationInfoStackEmpty(CompletionStatus paramCompletionStatus) {
/* 5374 */     return invocationInfoStackEmpty(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invocationInfoStackEmpty(Throwable paramThrowable) {
/* 5378 */     return invocationInfoStackEmpty(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL invocationInfoStackEmpty() {
/* 5382 */     return invocationInfoStackEmpty(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badCodeSetString(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5388 */     INTERNAL localINTERNAL = new INTERNAL(1398079754, paramCompletionStatus);
/* 5389 */     if (paramThrowable != null) {
/* 5390 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5392 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5393 */       Object[] arrayOfObject = null;
/* 5394 */       doLog(Level.WARNING, "ORBUTIL.badCodeSetString", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5398 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badCodeSetString(CompletionStatus paramCompletionStatus) {
/* 5402 */     return badCodeSetString(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badCodeSetString(Throwable paramThrowable) {
/* 5406 */     return badCodeSetString(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badCodeSetString() {
/* 5410 */     return badCodeSetString(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unknownNativeCodeset(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5416 */     INTERNAL localINTERNAL = new INTERNAL(1398079755, paramCompletionStatus);
/* 5417 */     if (paramThrowable != null) {
/* 5418 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5420 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5421 */       Object[] arrayOfObject = new Object[1];
/* 5422 */       arrayOfObject[0] = paramObject;
/* 5423 */       doLog(Level.WARNING, "ORBUTIL.unknownNativeCodeset", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5427 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL unknownNativeCodeset(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 5431 */     return unknownNativeCodeset(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unknownNativeCodeset(Throwable paramThrowable, Object paramObject) {
/* 5435 */     return unknownNativeCodeset(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unknownNativeCodeset(Object paramObject) {
/* 5439 */     return unknownNativeCodeset(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unknownConversionCodeSet(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5445 */     INTERNAL localINTERNAL = new INTERNAL(1398079756, paramCompletionStatus);
/* 5446 */     if (paramThrowable != null) {
/* 5447 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5449 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5450 */       Object[] arrayOfObject = new Object[1];
/* 5451 */       arrayOfObject[0] = paramObject;
/* 5452 */       doLog(Level.WARNING, "ORBUTIL.unknownConversionCodeSet", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5456 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL unknownConversionCodeSet(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 5460 */     return unknownConversionCodeSet(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unknownConversionCodeSet(Throwable paramThrowable, Object paramObject) {
/* 5464 */     return unknownConversionCodeSet(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unknownConversionCodeSet(Object paramObject) {
/* 5468 */     return unknownConversionCodeSet(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCodeSetNumber(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5474 */     INTERNAL localINTERNAL = new INTERNAL(1398079757, paramCompletionStatus);
/* 5475 */     if (paramThrowable != null) {
/* 5476 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5478 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5479 */       Object[] arrayOfObject = null;
/* 5480 */       doLog(Level.WARNING, "ORBUTIL.invalidCodeSetNumber", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5484 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCodeSetNumber(CompletionStatus paramCompletionStatus) {
/* 5488 */     return invalidCodeSetNumber(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCodeSetNumber(Throwable paramThrowable) {
/* 5492 */     return invalidCodeSetNumber(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCodeSetNumber() {
/* 5496 */     return invalidCodeSetNumber(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCodeSetString(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5502 */     INTERNAL localINTERNAL = new INTERNAL(1398079758, paramCompletionStatus);
/* 5503 */     if (paramThrowable != null) {
/* 5504 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5506 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5507 */       Object[] arrayOfObject = new Object[1];
/* 5508 */       arrayOfObject[0] = paramObject;
/* 5509 */       doLog(Level.WARNING, "ORBUTIL.invalidCodeSetString", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5513 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCodeSetString(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 5517 */     return invalidCodeSetString(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCodeSetString(Throwable paramThrowable, Object paramObject) {
/* 5521 */     return invalidCodeSetString(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCodeSetString(Object paramObject) {
/* 5525 */     return invalidCodeSetString(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCtbConverterName(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5531 */     INTERNAL localINTERNAL = new INTERNAL(1398079759, paramCompletionStatus);
/* 5532 */     if (paramThrowable != null) {
/* 5533 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5535 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5536 */       Object[] arrayOfObject = new Object[1];
/* 5537 */       arrayOfObject[0] = paramObject;
/* 5538 */       doLog(Level.WARNING, "ORBUTIL.invalidCtbConverterName", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5542 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCtbConverterName(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 5546 */     return invalidCtbConverterName(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCtbConverterName(Throwable paramThrowable, Object paramObject) {
/* 5550 */     return invalidCtbConverterName(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidCtbConverterName(Object paramObject) {
/* 5554 */     return invalidCtbConverterName(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidBtcConverterName(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5560 */     INTERNAL localINTERNAL = new INTERNAL(1398079760, paramCompletionStatus);
/* 5561 */     if (paramThrowable != null) {
/* 5562 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5564 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5565 */       Object[] arrayOfObject = new Object[1];
/* 5566 */       arrayOfObject[0] = paramObject;
/* 5567 */       doLog(Level.WARNING, "ORBUTIL.invalidBtcConverterName", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5571 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidBtcConverterName(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 5575 */     return invalidBtcConverterName(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidBtcConverterName(Throwable paramThrowable, Object paramObject) {
/* 5579 */     return invalidBtcConverterName(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidBtcConverterName(Object paramObject) {
/* 5583 */     return invalidBtcConverterName(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL couldNotDuplicateCdrInputStream(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5589 */     INTERNAL localINTERNAL = new INTERNAL(1398079761, paramCompletionStatus);
/* 5590 */     if (paramThrowable != null) {
/* 5591 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5593 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5594 */       Object[] arrayOfObject = null;
/* 5595 */       doLog(Level.WARNING, "ORBUTIL.couldNotDuplicateCdrInputStream", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5599 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL couldNotDuplicateCdrInputStream(CompletionStatus paramCompletionStatus) {
/* 5603 */     return couldNotDuplicateCdrInputStream(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL couldNotDuplicateCdrInputStream(Throwable paramThrowable) {
/* 5607 */     return couldNotDuplicateCdrInputStream(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL couldNotDuplicateCdrInputStream() {
/* 5611 */     return couldNotDuplicateCdrInputStream(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL bootstrapApplicationException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5617 */     INTERNAL localINTERNAL = new INTERNAL(1398079762, paramCompletionStatus);
/* 5618 */     if (paramThrowable != null) {
/* 5619 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5621 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5622 */       Object[] arrayOfObject = null;
/* 5623 */       doLog(Level.WARNING, "ORBUTIL.bootstrapApplicationException", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5627 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL bootstrapApplicationException(CompletionStatus paramCompletionStatus) {
/* 5631 */     return bootstrapApplicationException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL bootstrapApplicationException(Throwable paramThrowable) {
/* 5635 */     return bootstrapApplicationException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL bootstrapApplicationException() {
/* 5639 */     return bootstrapApplicationException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateIndirectionOffset(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5645 */     INTERNAL localINTERNAL = new INTERNAL(1398079763, paramCompletionStatus);
/* 5646 */     if (paramThrowable != null) {
/* 5647 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5649 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5650 */       Object[] arrayOfObject = null;
/* 5651 */       doLog(Level.WARNING, "ORBUTIL.duplicateIndirectionOffset", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5655 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateIndirectionOffset(CompletionStatus paramCompletionStatus) {
/* 5659 */     return duplicateIndirectionOffset(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateIndirectionOffset(Throwable paramThrowable) {
/* 5663 */     return duplicateIndirectionOffset(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateIndirectionOffset() {
/* 5667 */     return duplicateIndirectionOffset(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badMessageTypeForCancel(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5673 */     INTERNAL localINTERNAL = new INTERNAL(1398079764, paramCompletionStatus);
/* 5674 */     if (paramThrowable != null) {
/* 5675 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5677 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5678 */       Object[] arrayOfObject = null;
/* 5679 */       doLog(Level.WARNING, "ORBUTIL.badMessageTypeForCancel", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5683 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badMessageTypeForCancel(CompletionStatus paramCompletionStatus) {
/* 5687 */     return badMessageTypeForCancel(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badMessageTypeForCancel(Throwable paramThrowable) {
/* 5691 */     return badMessageTypeForCancel(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badMessageTypeForCancel() {
/* 5695 */     return badMessageTypeForCancel(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateExceptionDetailMessage(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5701 */     INTERNAL localINTERNAL = new INTERNAL(1398079765, paramCompletionStatus);
/* 5702 */     if (paramThrowable != null) {
/* 5703 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5705 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5706 */       Object[] arrayOfObject = null;
/* 5707 */       doLog(Level.WARNING, "ORBUTIL.duplicateExceptionDetailMessage", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5711 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateExceptionDetailMessage(CompletionStatus paramCompletionStatus) {
/* 5715 */     return duplicateExceptionDetailMessage(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateExceptionDetailMessage(Throwable paramThrowable) {
/* 5719 */     return duplicateExceptionDetailMessage(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateExceptionDetailMessage() {
/* 5723 */     return duplicateExceptionDetailMessage(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badExceptionDetailMessageServiceContextType(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5729 */     INTERNAL localINTERNAL = new INTERNAL(1398079766, paramCompletionStatus);
/* 5730 */     if (paramThrowable != null) {
/* 5731 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5733 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5734 */       Object[] arrayOfObject = null;
/* 5735 */       doLog(Level.WARNING, "ORBUTIL.badExceptionDetailMessageServiceContextType", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5739 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badExceptionDetailMessageServiceContextType(CompletionStatus paramCompletionStatus) {
/* 5743 */     return badExceptionDetailMessageServiceContextType(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL badExceptionDetailMessageServiceContextType(Throwable paramThrowable) {
/* 5747 */     return badExceptionDetailMessageServiceContextType(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL badExceptionDetailMessageServiceContextType() {
/* 5751 */     return badExceptionDetailMessageServiceContextType(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedDirectByteBufferWithNonChannelSocket(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5757 */     INTERNAL localINTERNAL = new INTERNAL(1398079767, paramCompletionStatus);
/* 5758 */     if (paramThrowable != null) {
/* 5759 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5761 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5762 */       Object[] arrayOfObject = null;
/* 5763 */       doLog(Level.WARNING, "ORBUTIL.unexpectedDirectByteBufferWithNonChannelSocket", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5767 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedDirectByteBufferWithNonChannelSocket(CompletionStatus paramCompletionStatus) {
/* 5771 */     return unexpectedDirectByteBufferWithNonChannelSocket(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedDirectByteBufferWithNonChannelSocket(Throwable paramThrowable) {
/* 5775 */     return unexpectedDirectByteBufferWithNonChannelSocket(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedDirectByteBufferWithNonChannelSocket() {
/* 5779 */     return unexpectedDirectByteBufferWithNonChannelSocket(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedNonDirectByteBufferWithChannelSocket(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5785 */     INTERNAL localINTERNAL = new INTERNAL(1398079768, paramCompletionStatus);
/* 5786 */     if (paramThrowable != null) {
/* 5787 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5789 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5790 */       Object[] arrayOfObject = null;
/* 5791 */       doLog(Level.WARNING, "ORBUTIL.unexpectedNonDirectByteBufferWithChannelSocket", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5795 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedNonDirectByteBufferWithChannelSocket(CompletionStatus paramCompletionStatus) {
/* 5799 */     return unexpectedNonDirectByteBufferWithChannelSocket(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedNonDirectByteBufferWithChannelSocket(Throwable paramThrowable) {
/* 5803 */     return unexpectedNonDirectByteBufferWithChannelSocket(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedNonDirectByteBufferWithChannelSocket() {
/* 5807 */     return unexpectedNonDirectByteBufferWithChannelSocket(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidContactInfoListIteratorFailureException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5813 */     INTERNAL localINTERNAL = new INTERNAL(1398079770, paramCompletionStatus);
/* 5814 */     if (paramThrowable != null) {
/* 5815 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5817 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5818 */       Object[] arrayOfObject = null;
/* 5819 */       doLog(Level.WARNING, "ORBUTIL.invalidContactInfoListIteratorFailureException", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5823 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidContactInfoListIteratorFailureException(CompletionStatus paramCompletionStatus) {
/* 5827 */     return invalidContactInfoListIteratorFailureException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidContactInfoListIteratorFailureException(Throwable paramThrowable) {
/* 5831 */     return invalidContactInfoListIteratorFailureException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidContactInfoListIteratorFailureException() {
/* 5835 */     return invalidContactInfoListIteratorFailureException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL remarshalWithNowhereToGo(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5841 */     INTERNAL localINTERNAL = new INTERNAL(1398079771, paramCompletionStatus);
/* 5842 */     if (paramThrowable != null) {
/* 5843 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5845 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5846 */       Object[] arrayOfObject = null;
/* 5847 */       doLog(Level.WARNING, "ORBUTIL.remarshalWithNowhereToGo", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5851 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL remarshalWithNowhereToGo(CompletionStatus paramCompletionStatus) {
/* 5855 */     return remarshalWithNowhereToGo(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL remarshalWithNowhereToGo(Throwable paramThrowable) {
/* 5859 */     return remarshalWithNowhereToGo(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL remarshalWithNowhereToGo() {
/* 5863 */     return remarshalWithNowhereToGo(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL exceptionWhenSendingCloseConnection(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 5869 */     INTERNAL localINTERNAL = new INTERNAL(1398079772, paramCompletionStatus);
/* 5870 */     if (paramThrowable != null) {
/* 5871 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5873 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5874 */       Object[] arrayOfObject = null;
/* 5875 */       doLog(Level.WARNING, "ORBUTIL.exceptionWhenSendingCloseConnection", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5879 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL exceptionWhenSendingCloseConnection(CompletionStatus paramCompletionStatus) {
/* 5883 */     return exceptionWhenSendingCloseConnection(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL exceptionWhenSendingCloseConnection(Throwable paramThrowable) {
/* 5887 */     return exceptionWhenSendingCloseConnection(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL exceptionWhenSendingCloseConnection() {
/* 5891 */     return exceptionWhenSendingCloseConnection(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL invocationErrorInReflectiveTie(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 5897 */     INTERNAL localINTERNAL = new INTERNAL(1398079773, paramCompletionStatus);
/* 5898 */     if (paramThrowable != null) {
/* 5899 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5901 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5902 */       Object[] arrayOfObject = new Object[2];
/* 5903 */       arrayOfObject[0] = paramObject1;
/* 5904 */       arrayOfObject[1] = paramObject2;
/* 5905 */       doLog(Level.WARNING, "ORBUTIL.invocationErrorInReflectiveTie", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5909 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invocationErrorInReflectiveTie(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 5913 */     return invocationErrorInReflectiveTie(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL invocationErrorInReflectiveTie(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 5917 */     return invocationErrorInReflectiveTie(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL invocationErrorInReflectiveTie(Object paramObject1, Object paramObject2) {
/* 5921 */     return invocationErrorInReflectiveTie(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperWriteMethod(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5927 */     INTERNAL localINTERNAL = new INTERNAL(1398079774, paramCompletionStatus);
/* 5928 */     if (paramThrowable != null) {
/* 5929 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5931 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5932 */       Object[] arrayOfObject = new Object[1];
/* 5933 */       arrayOfObject[0] = paramObject;
/* 5934 */       doLog(Level.WARNING, "ORBUTIL.badHelperWriteMethod", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5938 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperWriteMethod(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 5942 */     return badHelperWriteMethod(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperWriteMethod(Throwable paramThrowable, Object paramObject) {
/* 5946 */     return badHelperWriteMethod(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperWriteMethod(Object paramObject) {
/* 5950 */     return badHelperWriteMethod(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperReadMethod(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5956 */     INTERNAL localINTERNAL = new INTERNAL(1398079775, paramCompletionStatus);
/* 5957 */     if (paramThrowable != null) {
/* 5958 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5960 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5961 */       Object[] arrayOfObject = new Object[1];
/* 5962 */       arrayOfObject[0] = paramObject;
/* 5963 */       doLog(Level.WARNING, "ORBUTIL.badHelperReadMethod", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5967 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperReadMethod(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 5971 */     return badHelperReadMethod(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperReadMethod(Throwable paramThrowable, Object paramObject) {
/* 5975 */     return badHelperReadMethod(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperReadMethod(Object paramObject) {
/* 5979 */     return badHelperReadMethod(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperIdMethod(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 5985 */     INTERNAL localINTERNAL = new INTERNAL(1398079776, paramCompletionStatus);
/* 5986 */     if (paramThrowable != null) {
/* 5987 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 5989 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 5990 */       Object[] arrayOfObject = new Object[1];
/* 5991 */       arrayOfObject[0] = paramObject;
/* 5992 */       doLog(Level.WARNING, "ORBUTIL.badHelperIdMethod", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 5996 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperIdMethod(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 6000 */     return badHelperIdMethod(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperIdMethod(Throwable paramThrowable, Object paramObject) {
/* 6004 */     return badHelperIdMethod(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL badHelperIdMethod(Object paramObject) {
/* 6008 */     return badHelperIdMethod(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL writeUndeclaredException(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 6014 */     INTERNAL localINTERNAL = new INTERNAL(1398079777, paramCompletionStatus);
/* 6015 */     if (paramThrowable != null) {
/* 6016 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6018 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6019 */       Object[] arrayOfObject = new Object[1];
/* 6020 */       arrayOfObject[0] = paramObject;
/* 6021 */       doLog(Level.WARNING, "ORBUTIL.writeUndeclaredException", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6025 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL writeUndeclaredException(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 6029 */     return writeUndeclaredException(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL writeUndeclaredException(Throwable paramThrowable, Object paramObject) {
/* 6033 */     return writeUndeclaredException(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL writeUndeclaredException(Object paramObject) {
/* 6037 */     return writeUndeclaredException(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL readUndeclaredException(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 6043 */     INTERNAL localINTERNAL = new INTERNAL(1398079778, paramCompletionStatus);
/* 6044 */     if (paramThrowable != null) {
/* 6045 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6047 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6048 */       Object[] arrayOfObject = new Object[1];
/* 6049 */       arrayOfObject[0] = paramObject;
/* 6050 */       doLog(Level.WARNING, "ORBUTIL.readUndeclaredException", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6054 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL readUndeclaredException(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 6058 */     return readUndeclaredException(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL readUndeclaredException(Throwable paramThrowable, Object paramObject) {
/* 6062 */     return readUndeclaredException(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL readUndeclaredException(Object paramObject) {
/* 6066 */     return readUndeclaredException(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL unableToSetSocketFactoryOrb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6072 */     INTERNAL localINTERNAL = new INTERNAL(1398079779, paramCompletionStatus);
/* 6073 */     if (paramThrowable != null) {
/* 6074 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6076 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6077 */       Object[] arrayOfObject = null;
/* 6078 */       doLog(Level.WARNING, "ORBUTIL.unableToSetSocketFactoryOrb", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6082 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL unableToSetSocketFactoryOrb(CompletionStatus paramCompletionStatus) {
/* 6086 */     return unableToSetSocketFactoryOrb(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unableToSetSocketFactoryOrb(Throwable paramThrowable) {
/* 6090 */     return unableToSetSocketFactoryOrb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL unableToSetSocketFactoryOrb() {
/* 6094 */     return unableToSetSocketFactoryOrb(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6100 */     INTERNAL localINTERNAL = new INTERNAL(1398079780, paramCompletionStatus);
/* 6101 */     if (paramThrowable != null) {
/* 6102 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6104 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6105 */       Object[] arrayOfObject = null;
/* 6106 */       doLog(Level.WARNING, "ORBUTIL.unexpectedException", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6110 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedException(CompletionStatus paramCompletionStatus) {
/* 6114 */     return unexpectedException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedException(Throwable paramThrowable) {
/* 6118 */     return unexpectedException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL unexpectedException() {
/* 6122 */     return unexpectedException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL noInvocationHandler(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 6128 */     INTERNAL localINTERNAL = new INTERNAL(1398079781, paramCompletionStatus);
/* 6129 */     if (paramThrowable != null) {
/* 6130 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6132 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6133 */       Object[] arrayOfObject = new Object[1];
/* 6134 */       arrayOfObject[0] = paramObject;
/* 6135 */       doLog(Level.WARNING, "ORBUTIL.noInvocationHandler", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6139 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL noInvocationHandler(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 6143 */     return noInvocationHandler(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL noInvocationHandler(Throwable paramThrowable, Object paramObject) {
/* 6147 */     return noInvocationHandler(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL noInvocationHandler(Object paramObject) {
/* 6151 */     return noInvocationHandler(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidBuffMgrStrategy(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 6157 */     INTERNAL localINTERNAL = new INTERNAL(1398079782, paramCompletionStatus);
/* 6158 */     if (paramThrowable != null) {
/* 6159 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6161 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6162 */       Object[] arrayOfObject = new Object[1];
/* 6163 */       arrayOfObject[0] = paramObject;
/* 6164 */       doLog(Level.WARNING, "ORBUTIL.invalidBuffMgrStrategy", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6168 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidBuffMgrStrategy(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 6172 */     return invalidBuffMgrStrategy(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidBuffMgrStrategy(Throwable paramThrowable, Object paramObject) {
/* 6176 */     return invalidBuffMgrStrategy(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL invalidBuffMgrStrategy(Object paramObject) {
/* 6180 */     return invalidBuffMgrStrategy(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL javaStreamInitFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6186 */     INTERNAL localINTERNAL = new INTERNAL(1398079783, paramCompletionStatus);
/* 6187 */     if (paramThrowable != null) {
/* 6188 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6190 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6191 */       Object[] arrayOfObject = null;
/* 6192 */       doLog(Level.WARNING, "ORBUTIL.javaStreamInitFailed", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6196 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL javaStreamInitFailed(CompletionStatus paramCompletionStatus) {
/* 6200 */     return javaStreamInitFailed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL javaStreamInitFailed(Throwable paramThrowable) {
/* 6204 */     return javaStreamInitFailed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL javaStreamInitFailed() {
/* 6208 */     return javaStreamInitFailed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateOrbVersionServiceContext(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6214 */     INTERNAL localINTERNAL = new INTERNAL(1398079784, paramCompletionStatus);
/* 6215 */     if (paramThrowable != null) {
/* 6216 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6218 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6219 */       Object[] arrayOfObject = null;
/* 6220 */       doLog(Level.WARNING, "ORBUTIL.duplicateOrbVersionServiceContext", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6224 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateOrbVersionServiceContext(CompletionStatus paramCompletionStatus) {
/* 6228 */     return duplicateOrbVersionServiceContext(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateOrbVersionServiceContext(Throwable paramThrowable) {
/* 6232 */     return duplicateOrbVersionServiceContext(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateOrbVersionServiceContext() {
/* 6236 */     return duplicateOrbVersionServiceContext(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateSendingContextServiceContext(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6242 */     INTERNAL localINTERNAL = new INTERNAL(1398079785, paramCompletionStatus);
/* 6243 */     if (paramThrowable != null) {
/* 6244 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6246 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6247 */       Object[] arrayOfObject = null;
/* 6248 */       doLog(Level.WARNING, "ORBUTIL.duplicateSendingContextServiceContext", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6252 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateSendingContextServiceContext(CompletionStatus paramCompletionStatus) {
/* 6256 */     return duplicateSendingContextServiceContext(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateSendingContextServiceContext(Throwable paramThrowable) {
/* 6260 */     return duplicateSendingContextServiceContext(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL duplicateSendingContextServiceContext() {
/* 6264 */     return duplicateSendingContextServiceContext(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL workQueueThreadInterrupted(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 6270 */     INTERNAL localINTERNAL = new INTERNAL(1398079786, paramCompletionStatus);
/* 6271 */     if (paramThrowable != null) {
/* 6272 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6274 */     if (this.logger.isLoggable(Level.FINE)) {
/* 6275 */       Object[] arrayOfObject = new Object[2];
/* 6276 */       arrayOfObject[0] = paramObject1;
/* 6277 */       arrayOfObject[1] = paramObject2;
/* 6278 */       doLog(Level.FINE, "ORBUTIL.workQueueThreadInterrupted", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6282 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL workQueueThreadInterrupted(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 6286 */     return workQueueThreadInterrupted(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workQueueThreadInterrupted(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 6290 */     return workQueueThreadInterrupted(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workQueueThreadInterrupted(Object paramObject1, Object paramObject2) {
/* 6294 */     return workQueueThreadInterrupted(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCreated(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 6300 */     INTERNAL localINTERNAL = new INTERNAL(1398079792, paramCompletionStatus);
/* 6301 */     if (paramThrowable != null) {
/* 6302 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6304 */     if (this.logger.isLoggable(Level.FINE)) {
/* 6305 */       Object[] arrayOfObject = new Object[2];
/* 6306 */       arrayOfObject[0] = paramObject1;
/* 6307 */       arrayOfObject[1] = paramObject2;
/* 6308 */       doLog(Level.FINE, "ORBUTIL.workerThreadCreated", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6312 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCreated(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 6316 */     return workerThreadCreated(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCreated(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 6320 */     return workerThreadCreated(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCreated(Object paramObject1, Object paramObject2) {
/* 6324 */     return workerThreadCreated(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadThrowableFromRequestWork(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 6330 */     INTERNAL localINTERNAL = new INTERNAL(1398079797, paramCompletionStatus);
/* 6331 */     if (paramThrowable != null) {
/* 6332 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6334 */     if (this.logger.isLoggable(Level.FINE)) {
/* 6335 */       Object[] arrayOfObject = new Object[3];
/* 6336 */       arrayOfObject[0] = paramObject1;
/* 6337 */       arrayOfObject[1] = paramObject2;
/* 6338 */       arrayOfObject[2] = paramObject3;
/* 6339 */       doLog(Level.FINE, "ORBUTIL.workerThreadThrowableFromRequestWork", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6343 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadThrowableFromRequestWork(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 6347 */     return workerThreadThrowableFromRequestWork(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadThrowableFromRequestWork(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 6351 */     return workerThreadThrowableFromRequestWork(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadThrowableFromRequestWork(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 6355 */     return workerThreadThrowableFromRequestWork(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadNotNeeded(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 6361 */     INTERNAL localINTERNAL = new INTERNAL(1398079798, paramCompletionStatus);
/* 6362 */     if (paramThrowable != null) {
/* 6363 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6365 */     if (this.logger.isLoggable(Level.FINE)) {
/* 6366 */       Object[] arrayOfObject = new Object[3];
/* 6367 */       arrayOfObject[0] = paramObject1;
/* 6368 */       arrayOfObject[1] = paramObject2;
/* 6369 */       arrayOfObject[2] = paramObject3;
/* 6370 */       doLog(Level.FINE, "ORBUTIL.workerThreadNotNeeded", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6374 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadNotNeeded(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 6378 */     return workerThreadNotNeeded(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadNotNeeded(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 6382 */     return workerThreadNotNeeded(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadNotNeeded(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 6386 */     return workerThreadNotNeeded(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadDoWorkThrowable(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 6392 */     INTERNAL localINTERNAL = new INTERNAL(1398079799, paramCompletionStatus);
/* 6393 */     if (paramThrowable != null) {
/* 6394 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6396 */     if (this.logger.isLoggable(Level.FINE)) {
/* 6397 */       Object[] arrayOfObject = new Object[2];
/* 6398 */       arrayOfObject[0] = paramObject1;
/* 6399 */       arrayOfObject[1] = paramObject2;
/* 6400 */       doLog(Level.FINE, "ORBUTIL.workerThreadDoWorkThrowable", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6404 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadDoWorkThrowable(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 6408 */     return workerThreadDoWorkThrowable(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadDoWorkThrowable(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 6412 */     return workerThreadDoWorkThrowable(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadDoWorkThrowable(Object paramObject1, Object paramObject2) {
/* 6416 */     return workerThreadDoWorkThrowable(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCaughtUnexpectedThrowable(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 6422 */     INTERNAL localINTERNAL = new INTERNAL(1398079800, paramCompletionStatus);
/* 6423 */     if (paramThrowable != null) {
/* 6424 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6426 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6427 */       Object[] arrayOfObject = new Object[2];
/* 6428 */       arrayOfObject[0] = paramObject1;
/* 6429 */       arrayOfObject[1] = paramObject2;
/* 6430 */       doLog(Level.WARNING, "ORBUTIL.workerThreadCaughtUnexpectedThrowable", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6434 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCaughtUnexpectedThrowable(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 6438 */     return workerThreadCaughtUnexpectedThrowable(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCaughtUnexpectedThrowable(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 6442 */     return workerThreadCaughtUnexpectedThrowable(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCaughtUnexpectedThrowable(Object paramObject1, Object paramObject2) {
/* 6446 */     return workerThreadCaughtUnexpectedThrowable(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCreationFailure(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 6452 */     INTERNAL localINTERNAL = new INTERNAL(1398079801, paramCompletionStatus);
/* 6453 */     if (paramThrowable != null) {
/* 6454 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6456 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 6457 */       Object[] arrayOfObject = new Object[1];
/* 6458 */       arrayOfObject[0] = paramObject;
/* 6459 */       doLog(Level.SEVERE, "ORBUTIL.workerThreadCreationFailure", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6463 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCreationFailure(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 6467 */     return workerThreadCreationFailure(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCreationFailure(Throwable paramThrowable, Object paramObject) {
/* 6471 */     return workerThreadCreationFailure(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadCreationFailure(Object paramObject) {
/* 6475 */     return workerThreadCreationFailure(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadSetNameFailure(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 6481 */     INTERNAL localINTERNAL = new INTERNAL(1398079802, paramCompletionStatus);
/* 6482 */     if (paramThrowable != null) {
/* 6483 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6485 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6486 */       Object[] arrayOfObject = new Object[3];
/* 6487 */       arrayOfObject[0] = paramObject1;
/* 6488 */       arrayOfObject[1] = paramObject2;
/* 6489 */       arrayOfObject[2] = paramObject3;
/* 6490 */       doLog(Level.WARNING, "ORBUTIL.workerThreadSetNameFailure", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6494 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadSetNameFailure(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 6498 */     return workerThreadSetNameFailure(paramCompletionStatus, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadSetNameFailure(Throwable paramThrowable, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 6502 */     return workerThreadSetNameFailure(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public INTERNAL workerThreadSetNameFailure(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 6506 */     return workerThreadSetNameFailure(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2, paramObject3);
/*      */   }
/*      */ 
/*      */   public INTERNAL workQueueRequestWorkNoWorkFound(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 6512 */     INTERNAL localINTERNAL = new INTERNAL(1398079804, paramCompletionStatus);
/* 6513 */     if (paramThrowable != null) {
/* 6514 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6516 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6517 */       Object[] arrayOfObject = new Object[2];
/* 6518 */       arrayOfObject[0] = paramObject1;
/* 6519 */       arrayOfObject[1] = paramObject2;
/* 6520 */       doLog(Level.WARNING, "ORBUTIL.workQueueRequestWorkNoWorkFound", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6524 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL workQueueRequestWorkNoWorkFound(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 6528 */     return workQueueRequestWorkNoWorkFound(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workQueueRequestWorkNoWorkFound(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 6532 */     return workQueueRequestWorkNoWorkFound(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL workQueueRequestWorkNoWorkFound(Object paramObject1, Object paramObject2) {
/* 6536 */     return workQueueRequestWorkNoWorkFound(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadPoolCloseError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6542 */     INTERNAL localINTERNAL = new INTERNAL(1398079814, paramCompletionStatus);
/* 6543 */     if (paramThrowable != null) {
/* 6544 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6546 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6547 */       Object[] arrayOfObject = null;
/* 6548 */       doLog(Level.WARNING, "ORBUTIL.threadPoolCloseError", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6552 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL threadPoolCloseError(CompletionStatus paramCompletionStatus) {
/* 6556 */     return threadPoolCloseError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadPoolCloseError(Throwable paramThrowable) {
/* 6560 */     return threadPoolCloseError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadPoolCloseError() {
/* 6564 */     return threadPoolCloseError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupIsDestroyed(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 6570 */     INTERNAL localINTERNAL = new INTERNAL(1398079815, paramCompletionStatus);
/* 6571 */     if (paramThrowable != null) {
/* 6572 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6574 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6575 */       Object[] arrayOfObject = new Object[1];
/* 6576 */       arrayOfObject[0] = paramObject;
/* 6577 */       doLog(Level.WARNING, "ORBUTIL.threadGroupIsDestroyed", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6581 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupIsDestroyed(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 6585 */     return threadGroupIsDestroyed(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupIsDestroyed(Throwable paramThrowable, Object paramObject) {
/* 6589 */     return threadGroupIsDestroyed(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupIsDestroyed(Object paramObject) {
/* 6593 */     return threadGroupIsDestroyed(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupHasActiveThreadsInClose(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 6599 */     INTERNAL localINTERNAL = new INTERNAL(1398079816, paramCompletionStatus);
/* 6600 */     if (paramThrowable != null) {
/* 6601 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6603 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6604 */       Object[] arrayOfObject = new Object[2];
/* 6605 */       arrayOfObject[0] = paramObject1;
/* 6606 */       arrayOfObject[1] = paramObject2;
/* 6607 */       doLog(Level.WARNING, "ORBUTIL.threadGroupHasActiveThreadsInClose", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6611 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupHasActiveThreadsInClose(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 6615 */     return threadGroupHasActiveThreadsInClose(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupHasActiveThreadsInClose(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 6619 */     return threadGroupHasActiveThreadsInClose(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupHasActiveThreadsInClose(Object paramObject1, Object paramObject2) {
/* 6623 */     return threadGroupHasActiveThreadsInClose(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupHasSubGroupsInClose(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 6629 */     INTERNAL localINTERNAL = new INTERNAL(1398079817, paramCompletionStatus);
/* 6630 */     if (paramThrowable != null) {
/* 6631 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6633 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6634 */       Object[] arrayOfObject = new Object[2];
/* 6635 */       arrayOfObject[0] = paramObject1;
/* 6636 */       arrayOfObject[1] = paramObject2;
/* 6637 */       doLog(Level.WARNING, "ORBUTIL.threadGroupHasSubGroupsInClose", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6641 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupHasSubGroupsInClose(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 6645 */     return threadGroupHasSubGroupsInClose(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupHasSubGroupsInClose(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 6649 */     return threadGroupHasSubGroupsInClose(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupHasSubGroupsInClose(Object paramObject1, Object paramObject2) {
/* 6653 */     return threadGroupHasSubGroupsInClose(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupDestroyFailed(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 6659 */     INTERNAL localINTERNAL = new INTERNAL(1398079818, paramCompletionStatus);
/* 6660 */     if (paramThrowable != null) {
/* 6661 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6663 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6664 */       Object[] arrayOfObject = new Object[1];
/* 6665 */       arrayOfObject[0] = paramObject;
/* 6666 */       doLog(Level.WARNING, "ORBUTIL.threadGroupDestroyFailed", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6670 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupDestroyFailed(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 6674 */     return threadGroupDestroyFailed(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupDestroyFailed(Throwable paramThrowable, Object paramObject) {
/* 6678 */     return threadGroupDestroyFailed(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL threadGroupDestroyFailed(Object paramObject) {
/* 6682 */     return threadGroupDestroyFailed(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public INTERNAL interruptedJoinCallWhileClosingThreadPool(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 6688 */     INTERNAL localINTERNAL = new INTERNAL(1398079819, paramCompletionStatus);
/* 6689 */     if (paramThrowable != null) {
/* 6690 */       localINTERNAL.initCause(paramThrowable);
/*      */     }
/* 6692 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6693 */       Object[] arrayOfObject = new Object[2];
/* 6694 */       arrayOfObject[0] = paramObject1;
/* 6695 */       arrayOfObject[1] = paramObject2;
/* 6696 */       doLog(Level.WARNING, "ORBUTIL.interruptedJoinCallWhileClosingThreadPool", arrayOfObject, ORBUtilSystemException.class, localINTERNAL);
/*      */     }
/*      */ 
/* 6700 */     return localINTERNAL;
/*      */   }
/*      */ 
/*      */   public INTERNAL interruptedJoinCallWhileClosingThreadPool(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 6704 */     return interruptedJoinCallWhileClosingThreadPool(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL interruptedJoinCallWhileClosingThreadPool(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 6708 */     return interruptedJoinCallWhileClosingThreadPool(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public INTERNAL interruptedJoinCallWhileClosingThreadPool(Object paramObject1, Object paramObject2) {
/* 6712 */     return interruptedJoinCallWhileClosingThreadPool(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL chunkOverflow(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6722 */     MARSHAL localMARSHAL = new MARSHAL(1398079689, paramCompletionStatus);
/* 6723 */     if (paramThrowable != null) {
/* 6724 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6726 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6727 */       Object[] arrayOfObject = null;
/* 6728 */       doLog(Level.WARNING, "ORBUTIL.chunkOverflow", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6732 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL chunkOverflow(CompletionStatus paramCompletionStatus) {
/* 6736 */     return chunkOverflow(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL chunkOverflow(Throwable paramThrowable) {
/* 6740 */     return chunkOverflow(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL chunkOverflow() {
/* 6744 */     return chunkOverflow(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedEof(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6750 */     MARSHAL localMARSHAL = new MARSHAL(1398079690, paramCompletionStatus);
/* 6751 */     if (paramThrowable != null) {
/* 6752 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6754 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6755 */       Object[] arrayOfObject = null;
/* 6756 */       doLog(Level.WARNING, "ORBUTIL.unexpectedEof", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6760 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedEof(CompletionStatus paramCompletionStatus) {
/* 6764 */     return unexpectedEof(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedEof(Throwable paramThrowable) {
/* 6768 */     return unexpectedEof(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedEof() {
/* 6772 */     return unexpectedEof(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL readObjectException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6778 */     MARSHAL localMARSHAL = new MARSHAL(1398079691, paramCompletionStatus);
/* 6779 */     if (paramThrowable != null) {
/* 6780 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6782 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6783 */       Object[] arrayOfObject = null;
/* 6784 */       doLog(Level.WARNING, "ORBUTIL.readObjectException", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6788 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL readObjectException(CompletionStatus paramCompletionStatus) {
/* 6792 */     return readObjectException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL readObjectException(Throwable paramThrowable) {
/* 6796 */     return readObjectException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL readObjectException() {
/* 6800 */     return readObjectException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL characterOutofrange(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6806 */     MARSHAL localMARSHAL = new MARSHAL(1398079692, paramCompletionStatus);
/* 6807 */     if (paramThrowable != null) {
/* 6808 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6810 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6811 */       Object[] arrayOfObject = null;
/* 6812 */       doLog(Level.WARNING, "ORBUTIL.characterOutofrange", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6816 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL characterOutofrange(CompletionStatus paramCompletionStatus) {
/* 6820 */     return characterOutofrange(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL characterOutofrange(Throwable paramThrowable) {
/* 6824 */     return characterOutofrange(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL characterOutofrange() {
/* 6828 */     return characterOutofrange(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL dsiResultException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6834 */     MARSHAL localMARSHAL = new MARSHAL(1398079693, paramCompletionStatus);
/* 6835 */     if (paramThrowable != null) {
/* 6836 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6838 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6839 */       Object[] arrayOfObject = null;
/* 6840 */       doLog(Level.WARNING, "ORBUTIL.dsiResultException", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6844 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL dsiResultException(CompletionStatus paramCompletionStatus) {
/* 6848 */     return dsiResultException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL dsiResultException(Throwable paramThrowable) {
/* 6852 */     return dsiResultException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL dsiResultException() {
/* 6856 */     return dsiResultException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL iiopinputstreamGrow(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6862 */     MARSHAL localMARSHAL = new MARSHAL(1398079694, paramCompletionStatus);
/* 6863 */     if (paramThrowable != null) {
/* 6864 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6866 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6867 */       Object[] arrayOfObject = null;
/* 6868 */       doLog(Level.WARNING, "ORBUTIL.iiopinputstreamGrow", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6872 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL iiopinputstreamGrow(CompletionStatus paramCompletionStatus) {
/* 6876 */     return iiopinputstreamGrow(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL iiopinputstreamGrow(Throwable paramThrowable) {
/* 6880 */     return iiopinputstreamGrow(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL iiopinputstreamGrow() {
/* 6884 */     return iiopinputstreamGrow(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL endOfStream(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6890 */     MARSHAL localMARSHAL = new MARSHAL(1398079695, paramCompletionStatus);
/* 6891 */     if (paramThrowable != null) {
/* 6892 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6894 */     if (this.logger.isLoggable(Level.FINE)) {
/* 6895 */       Object[] arrayOfObject = null;
/* 6896 */       doLog(Level.FINE, "ORBUTIL.endOfStream", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6900 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL endOfStream(CompletionStatus paramCompletionStatus) {
/* 6904 */     return endOfStream(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL endOfStream(Throwable paramThrowable) {
/* 6908 */     return endOfStream(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL endOfStream() {
/* 6912 */     return endOfStream(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidObjectKey(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6918 */     MARSHAL localMARSHAL = new MARSHAL(1398079696, paramCompletionStatus);
/* 6919 */     if (paramThrowable != null) {
/* 6920 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6922 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6923 */       Object[] arrayOfObject = null;
/* 6924 */       doLog(Level.WARNING, "ORBUTIL.invalidObjectKey", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6928 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidObjectKey(CompletionStatus paramCompletionStatus) {
/* 6932 */     return invalidObjectKey(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidObjectKey(Throwable paramThrowable) {
/* 6936 */     return invalidObjectKey(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidObjectKey() {
/* 6940 */     return invalidObjectKey(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL malformedUrl(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 6946 */     MARSHAL localMARSHAL = new MARSHAL(1398079697, paramCompletionStatus);
/* 6947 */     if (paramThrowable != null) {
/* 6948 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6950 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6951 */       Object[] arrayOfObject = new Object[2];
/* 6952 */       arrayOfObject[0] = paramObject1;
/* 6953 */       arrayOfObject[1] = paramObject2;
/* 6954 */       doLog(Level.WARNING, "ORBUTIL.malformedUrl", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6958 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL malformedUrl(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 6962 */     return malformedUrl(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL malformedUrl(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 6966 */     return malformedUrl(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL malformedUrl(Object paramObject1, Object paramObject2) {
/* 6970 */     return malformedUrl(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL valuehandlerReadError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 6976 */     MARSHAL localMARSHAL = new MARSHAL(1398079698, paramCompletionStatus);
/* 6977 */     if (paramThrowable != null) {
/* 6978 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 6980 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 6981 */       Object[] arrayOfObject = null;
/* 6982 */       doLog(Level.WARNING, "ORBUTIL.valuehandlerReadError", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 6986 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL valuehandlerReadError(CompletionStatus paramCompletionStatus) {
/* 6990 */     return valuehandlerReadError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL valuehandlerReadError(Throwable paramThrowable) {
/* 6994 */     return valuehandlerReadError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL valuehandlerReadError() {
/* 6998 */     return valuehandlerReadError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL valuehandlerReadException(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7004 */     MARSHAL localMARSHAL = new MARSHAL(1398079699, paramCompletionStatus);
/* 7005 */     if (paramThrowable != null) {
/* 7006 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7008 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7009 */       Object[] arrayOfObject = null;
/* 7010 */       doLog(Level.WARNING, "ORBUTIL.valuehandlerReadException", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7014 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL valuehandlerReadException(CompletionStatus paramCompletionStatus) {
/* 7018 */     return valuehandlerReadException(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL valuehandlerReadException(Throwable paramThrowable) {
/* 7022 */     return valuehandlerReadException(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL valuehandlerReadException() {
/* 7026 */     return valuehandlerReadException(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badKind(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7032 */     MARSHAL localMARSHAL = new MARSHAL(1398079700, paramCompletionStatus);
/* 7033 */     if (paramThrowable != null) {
/* 7034 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7036 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7037 */       Object[] arrayOfObject = null;
/* 7038 */       doLog(Level.WARNING, "ORBUTIL.badKind", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7042 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badKind(CompletionStatus paramCompletionStatus) {
/* 7046 */     return badKind(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badKind(Throwable paramThrowable) {
/* 7050 */     return badKind(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL badKind() {
/* 7054 */     return badKind(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL cnfeReadClass(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 7060 */     MARSHAL localMARSHAL = new MARSHAL(1398079701, paramCompletionStatus);
/* 7061 */     if (paramThrowable != null) {
/* 7062 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7064 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7065 */       Object[] arrayOfObject = new Object[1];
/* 7066 */       arrayOfObject[0] = paramObject;
/* 7067 */       doLog(Level.WARNING, "ORBUTIL.cnfeReadClass", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7071 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL cnfeReadClass(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 7075 */     return cnfeReadClass(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL cnfeReadClass(Throwable paramThrowable, Object paramObject) {
/* 7079 */     return cnfeReadClass(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL cnfeReadClass(Object paramObject) {
/* 7083 */     return cnfeReadClass(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badRepIdIndirection(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 7089 */     MARSHAL localMARSHAL = new MARSHAL(1398079702, paramCompletionStatus);
/* 7090 */     if (paramThrowable != null) {
/* 7091 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7093 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7094 */       Object[] arrayOfObject = new Object[1];
/* 7095 */       arrayOfObject[0] = paramObject;
/* 7096 */       doLog(Level.WARNING, "ORBUTIL.badRepIdIndirection", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7100 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badRepIdIndirection(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 7104 */     return badRepIdIndirection(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badRepIdIndirection(Throwable paramThrowable, Object paramObject) {
/* 7108 */     return badRepIdIndirection(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badRepIdIndirection(Object paramObject) {
/* 7112 */     return badRepIdIndirection(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badCodebaseIndirection(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 7118 */     MARSHAL localMARSHAL = new MARSHAL(1398079703, paramCompletionStatus);
/* 7119 */     if (paramThrowable != null) {
/* 7120 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7122 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7123 */       Object[] arrayOfObject = new Object[1];
/* 7124 */       arrayOfObject[0] = paramObject;
/* 7125 */       doLog(Level.WARNING, "ORBUTIL.badCodebaseIndirection", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7129 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badCodebaseIndirection(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 7133 */     return badCodebaseIndirection(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badCodebaseIndirection(Throwable paramThrowable, Object paramObject) {
/* 7137 */     return badCodebaseIndirection(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badCodebaseIndirection(Object paramObject) {
/* 7141 */     return badCodebaseIndirection(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL unknownCodeset(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 7147 */     MARSHAL localMARSHAL = new MARSHAL(1398079704, paramCompletionStatus);
/* 7148 */     if (paramThrowable != null) {
/* 7149 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7151 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7152 */       Object[] arrayOfObject = new Object[1];
/* 7153 */       arrayOfObject[0] = paramObject;
/* 7154 */       doLog(Level.WARNING, "ORBUTIL.unknownCodeset", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7158 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL unknownCodeset(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 7162 */     return unknownCodeset(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL unknownCodeset(Throwable paramThrowable, Object paramObject) {
/* 7166 */     return unknownCodeset(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL unknownCodeset(Object paramObject) {
/* 7170 */     return unknownCodeset(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharDataInGiop10(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7176 */     MARSHAL localMARSHAL = new MARSHAL(1398079705, paramCompletionStatus);
/* 7177 */     if (paramThrowable != null) {
/* 7178 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7180 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7181 */       Object[] arrayOfObject = null;
/* 7182 */       doLog(Level.WARNING, "ORBUTIL.wcharDataInGiop10", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7186 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharDataInGiop10(CompletionStatus paramCompletionStatus) {
/* 7190 */     return wcharDataInGiop10(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharDataInGiop10(Throwable paramThrowable) {
/* 7194 */     return wcharDataInGiop10(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL wcharDataInGiop10() {
/* 7198 */     return wcharDataInGiop10(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL negativeStringLength(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 7204 */     MARSHAL localMARSHAL = new MARSHAL(1398079706, paramCompletionStatus);
/* 7205 */     if (paramThrowable != null) {
/* 7206 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7208 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7209 */       Object[] arrayOfObject = new Object[1];
/* 7210 */       arrayOfObject[0] = paramObject;
/* 7211 */       doLog(Level.WARNING, "ORBUTIL.negativeStringLength", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7215 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL negativeStringLength(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 7219 */     return negativeStringLength(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL negativeStringLength(Throwable paramThrowable, Object paramObject) {
/* 7223 */     return negativeStringLength(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL negativeStringLength(Object paramObject) {
/* 7227 */     return negativeStringLength(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL expectedTypeNullAndNoRepId(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7233 */     MARSHAL localMARSHAL = new MARSHAL(1398079707, paramCompletionStatus);
/* 7234 */     if (paramThrowable != null) {
/* 7235 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7237 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7238 */       Object[] arrayOfObject = null;
/* 7239 */       doLog(Level.WARNING, "ORBUTIL.expectedTypeNullAndNoRepId", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7243 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL expectedTypeNullAndNoRepId(CompletionStatus paramCompletionStatus) {
/* 7247 */     return expectedTypeNullAndNoRepId(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL expectedTypeNullAndNoRepId(Throwable paramThrowable) {
/* 7251 */     return expectedTypeNullAndNoRepId(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL expectedTypeNullAndNoRepId() {
/* 7255 */     return expectedTypeNullAndNoRepId(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL readValueAndNoRepId(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7261 */     MARSHAL localMARSHAL = new MARSHAL(1398079708, paramCompletionStatus);
/* 7262 */     if (paramThrowable != null) {
/* 7263 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7265 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7266 */       Object[] arrayOfObject = null;
/* 7267 */       doLog(Level.WARNING, "ORBUTIL.readValueAndNoRepId", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7271 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL readValueAndNoRepId(CompletionStatus paramCompletionStatus) {
/* 7275 */     return readValueAndNoRepId(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL readValueAndNoRepId(Throwable paramThrowable) {
/* 7279 */     return readValueAndNoRepId(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL readValueAndNoRepId() {
/* 7283 */     return readValueAndNoRepId(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedEnclosingValuetype(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 7289 */     MARSHAL localMARSHAL = new MARSHAL(1398079710, paramCompletionStatus);
/* 7290 */     if (paramThrowable != null) {
/* 7291 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7293 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7294 */       Object[] arrayOfObject = new Object[2];
/* 7295 */       arrayOfObject[0] = paramObject1;
/* 7296 */       arrayOfObject[1] = paramObject2;
/* 7297 */       doLog(Level.WARNING, "ORBUTIL.unexpectedEnclosingValuetype", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7301 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedEnclosingValuetype(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 7305 */     return unexpectedEnclosingValuetype(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedEnclosingValuetype(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 7309 */     return unexpectedEnclosingValuetype(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedEnclosingValuetype(Object paramObject1, Object paramObject2) {
/* 7313 */     return unexpectedEnclosingValuetype(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL positiveEndTag(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 7319 */     MARSHAL localMARSHAL = new MARSHAL(1398079711, paramCompletionStatus);
/* 7320 */     if (paramThrowable != null) {
/* 7321 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7323 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7324 */       Object[] arrayOfObject = new Object[2];
/* 7325 */       arrayOfObject[0] = paramObject1;
/* 7326 */       arrayOfObject[1] = paramObject2;
/* 7327 */       doLog(Level.WARNING, "ORBUTIL.positiveEndTag", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7331 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL positiveEndTag(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 7335 */     return positiveEndTag(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL positiveEndTag(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 7339 */     return positiveEndTag(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL positiveEndTag(Object paramObject1, Object paramObject2) {
/* 7343 */     return positiveEndTag(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL nullOutCall(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7349 */     MARSHAL localMARSHAL = new MARSHAL(1398079712, paramCompletionStatus);
/* 7350 */     if (paramThrowable != null) {
/* 7351 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7353 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7354 */       Object[] arrayOfObject = null;
/* 7355 */       doLog(Level.WARNING, "ORBUTIL.nullOutCall", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7359 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL nullOutCall(CompletionStatus paramCompletionStatus) {
/* 7363 */     return nullOutCall(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL nullOutCall(Throwable paramThrowable) {
/* 7367 */     return nullOutCall(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL nullOutCall() {
/* 7371 */     return nullOutCall(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL writeLocalObject(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7377 */     MARSHAL localMARSHAL = new MARSHAL(1398079713, paramCompletionStatus);
/* 7378 */     if (paramThrowable != null) {
/* 7379 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7381 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7382 */       Object[] arrayOfObject = null;
/* 7383 */       doLog(Level.WARNING, "ORBUTIL.writeLocalObject", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7387 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL writeLocalObject(CompletionStatus paramCompletionStatus) {
/* 7391 */     return writeLocalObject(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL writeLocalObject(Throwable paramThrowable) {
/* 7395 */     return writeLocalObject(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL writeLocalObject() {
/* 7399 */     return writeLocalObject(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badInsertobjParam(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 7405 */     MARSHAL localMARSHAL = new MARSHAL(1398079714, paramCompletionStatus);
/* 7406 */     if (paramThrowable != null) {
/* 7407 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7409 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7410 */       Object[] arrayOfObject = new Object[1];
/* 7411 */       arrayOfObject[0] = paramObject;
/* 7412 */       doLog(Level.WARNING, "ORBUTIL.badInsertobjParam", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7416 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badInsertobjParam(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 7420 */     return badInsertobjParam(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badInsertobjParam(Throwable paramThrowable, Object paramObject) {
/* 7424 */     return badInsertobjParam(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badInsertobjParam(Object paramObject) {
/* 7428 */     return badInsertobjParam(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperWithCodebase(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7434 */     MARSHAL localMARSHAL = new MARSHAL(1398079715, paramCompletionStatus);
/* 7435 */     if (paramThrowable != null) {
/* 7436 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7438 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7439 */       Object[] arrayOfObject = null;
/* 7440 */       doLog(Level.WARNING, "ORBUTIL.customWrapperWithCodebase", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7444 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperWithCodebase(CompletionStatus paramCompletionStatus) {
/* 7448 */     return customWrapperWithCodebase(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperWithCodebase(Throwable paramThrowable) {
/* 7452 */     return customWrapperWithCodebase(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperWithCodebase() {
/* 7456 */     return customWrapperWithCodebase(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperIndirection(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7462 */     MARSHAL localMARSHAL = new MARSHAL(1398079716, paramCompletionStatus);
/* 7463 */     if (paramThrowable != null) {
/* 7464 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7466 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7467 */       Object[] arrayOfObject = null;
/* 7468 */       doLog(Level.WARNING, "ORBUTIL.customWrapperIndirection", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7472 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperIndirection(CompletionStatus paramCompletionStatus) {
/* 7476 */     return customWrapperIndirection(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperIndirection(Throwable paramThrowable) {
/* 7480 */     return customWrapperIndirection(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperIndirection() {
/* 7484 */     return customWrapperIndirection(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperNotSingleRepid(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7490 */     MARSHAL localMARSHAL = new MARSHAL(1398079717, paramCompletionStatus);
/* 7491 */     if (paramThrowable != null) {
/* 7492 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7494 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7495 */       Object[] arrayOfObject = null;
/* 7496 */       doLog(Level.WARNING, "ORBUTIL.customWrapperNotSingleRepid", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7500 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperNotSingleRepid(CompletionStatus paramCompletionStatus) {
/* 7504 */     return customWrapperNotSingleRepid(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperNotSingleRepid(Throwable paramThrowable) {
/* 7508 */     return customWrapperNotSingleRepid(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL customWrapperNotSingleRepid() {
/* 7512 */     return customWrapperNotSingleRepid(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badValueTag(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 7518 */     MARSHAL localMARSHAL = new MARSHAL(1398079718, paramCompletionStatus);
/* 7519 */     if (paramThrowable != null) {
/* 7520 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7522 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7523 */       Object[] arrayOfObject = new Object[1];
/* 7524 */       arrayOfObject[0] = paramObject;
/* 7525 */       doLog(Level.WARNING, "ORBUTIL.badValueTag", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7529 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badValueTag(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 7533 */     return badValueTag(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badValueTag(Throwable paramThrowable, Object paramObject) {
/* 7537 */     return badValueTag(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badValueTag(Object paramObject) {
/* 7541 */     return badValueTag(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badTypecodeForCustomValue(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7547 */     MARSHAL localMARSHAL = new MARSHAL(1398079719, paramCompletionStatus);
/* 7548 */     if (paramThrowable != null) {
/* 7549 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7551 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7552 */       Object[] arrayOfObject = null;
/* 7553 */       doLog(Level.WARNING, "ORBUTIL.badTypecodeForCustomValue", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7557 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badTypecodeForCustomValue(CompletionStatus paramCompletionStatus) {
/* 7561 */     return badTypecodeForCustomValue(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badTypecodeForCustomValue(Throwable paramThrowable) {
/* 7565 */     return badTypecodeForCustomValue(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL badTypecodeForCustomValue() {
/* 7569 */     return badTypecodeForCustomValue(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL errorInvokingHelperWrite(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7575 */     MARSHAL localMARSHAL = new MARSHAL(1398079720, paramCompletionStatus);
/* 7576 */     if (paramThrowable != null) {
/* 7577 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7579 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7580 */       Object[] arrayOfObject = null;
/* 7581 */       doLog(Level.WARNING, "ORBUTIL.errorInvokingHelperWrite", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7585 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL errorInvokingHelperWrite(CompletionStatus paramCompletionStatus) {
/* 7589 */     return errorInvokingHelperWrite(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL errorInvokingHelperWrite(Throwable paramThrowable) {
/* 7593 */     return errorInvokingHelperWrite(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL errorInvokingHelperWrite() {
/* 7597 */     return errorInvokingHelperWrite(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badDigitInFixed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7603 */     MARSHAL localMARSHAL = new MARSHAL(1398079721, paramCompletionStatus);
/* 7604 */     if (paramThrowable != null) {
/* 7605 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7607 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7608 */       Object[] arrayOfObject = null;
/* 7609 */       doLog(Level.WARNING, "ORBUTIL.badDigitInFixed", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7613 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badDigitInFixed(CompletionStatus paramCompletionStatus) {
/* 7617 */     return badDigitInFixed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badDigitInFixed(Throwable paramThrowable) {
/* 7621 */     return badDigitInFixed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL badDigitInFixed() {
/* 7625 */     return badDigitInFixed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL refTypeIndirType(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7631 */     MARSHAL localMARSHAL = new MARSHAL(1398079722, paramCompletionStatus);
/* 7632 */     if (paramThrowable != null) {
/* 7633 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7635 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7636 */       Object[] arrayOfObject = null;
/* 7637 */       doLog(Level.WARNING, "ORBUTIL.refTypeIndirType", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7641 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL refTypeIndirType(CompletionStatus paramCompletionStatus) {
/* 7645 */     return refTypeIndirType(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL refTypeIndirType(Throwable paramThrowable) {
/* 7649 */     return refTypeIndirType(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL refTypeIndirType() {
/* 7653 */     return refTypeIndirType(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badReservedLength(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7659 */     MARSHAL localMARSHAL = new MARSHAL(1398079723, paramCompletionStatus);
/* 7660 */     if (paramThrowable != null) {
/* 7661 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7663 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7664 */       Object[] arrayOfObject = null;
/* 7665 */       doLog(Level.WARNING, "ORBUTIL.badReservedLength", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7669 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badReservedLength(CompletionStatus paramCompletionStatus) {
/* 7673 */     return badReservedLength(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badReservedLength(Throwable paramThrowable) {
/* 7677 */     return badReservedLength(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL badReservedLength() {
/* 7681 */     return badReservedLength(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL nullNotAllowed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7687 */     MARSHAL localMARSHAL = new MARSHAL(1398079724, paramCompletionStatus);
/* 7688 */     if (paramThrowable != null) {
/* 7689 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7691 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7692 */       Object[] arrayOfObject = null;
/* 7693 */       doLog(Level.WARNING, "ORBUTIL.nullNotAllowed", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7697 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL nullNotAllowed(CompletionStatus paramCompletionStatus) {
/* 7701 */     return nullNotAllowed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL nullNotAllowed(Throwable paramThrowable) {
/* 7705 */     return nullNotAllowed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL nullNotAllowed() {
/* 7709 */     return nullNotAllowed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unionDiscriminatorError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7715 */     MARSHAL localMARSHAL = new MARSHAL(1398079726, paramCompletionStatus);
/* 7716 */     if (paramThrowable != null) {
/* 7717 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7719 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7720 */       Object[] arrayOfObject = null;
/* 7721 */       doLog(Level.WARNING, "ORBUTIL.unionDiscriminatorError", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7725 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL unionDiscriminatorError(CompletionStatus paramCompletionStatus) {
/* 7729 */     return unionDiscriminatorError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unionDiscriminatorError(Throwable paramThrowable) {
/* 7733 */     return unionDiscriminatorError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL unionDiscriminatorError() {
/* 7737 */     return unionDiscriminatorError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL cannotMarshalNative(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7743 */     MARSHAL localMARSHAL = new MARSHAL(1398079727, paramCompletionStatus);
/* 7744 */     if (paramThrowable != null) {
/* 7745 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7747 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7748 */       Object[] arrayOfObject = null;
/* 7749 */       doLog(Level.WARNING, "ORBUTIL.cannotMarshalNative", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7753 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL cannotMarshalNative(CompletionStatus paramCompletionStatus) {
/* 7757 */     return cannotMarshalNative(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL cannotMarshalNative(Throwable paramThrowable) {
/* 7761 */     return cannotMarshalNative(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL cannotMarshalNative() {
/* 7765 */     return cannotMarshalNative(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL cannotMarshalBadTckind(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7771 */     MARSHAL localMARSHAL = new MARSHAL(1398079728, paramCompletionStatus);
/* 7772 */     if (paramThrowable != null) {
/* 7773 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7775 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7776 */       Object[] arrayOfObject = null;
/* 7777 */       doLog(Level.WARNING, "ORBUTIL.cannotMarshalBadTckind", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7781 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL cannotMarshalBadTckind(CompletionStatus paramCompletionStatus) {
/* 7785 */     return cannotMarshalBadTckind(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL cannotMarshalBadTckind(Throwable paramThrowable) {
/* 7789 */     return cannotMarshalBadTckind(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL cannotMarshalBadTckind() {
/* 7793 */     return cannotMarshalBadTckind(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidIndirection(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 7799 */     MARSHAL localMARSHAL = new MARSHAL(1398079729, paramCompletionStatus);
/* 7800 */     if (paramThrowable != null) {
/* 7801 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7803 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7804 */       Object[] arrayOfObject = new Object[1];
/* 7805 */       arrayOfObject[0] = paramObject;
/* 7806 */       doLog(Level.WARNING, "ORBUTIL.invalidIndirection", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7810 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidIndirection(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 7814 */     return invalidIndirection(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidIndirection(Throwable paramThrowable, Object paramObject) {
/* 7818 */     return invalidIndirection(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidIndirection(Object paramObject) {
/* 7822 */     return invalidIndirection(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL indirectionNotFound(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 7828 */     MARSHAL localMARSHAL = new MARSHAL(1398079730, paramCompletionStatus);
/* 7829 */     if (paramThrowable != null) {
/* 7830 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7832 */     if (this.logger.isLoggable(Level.FINE)) {
/* 7833 */       Object[] arrayOfObject = new Object[1];
/* 7834 */       arrayOfObject[0] = paramObject;
/* 7835 */       doLog(Level.FINE, "ORBUTIL.indirectionNotFound", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7839 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL indirectionNotFound(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 7843 */     return indirectionNotFound(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL indirectionNotFound(Throwable paramThrowable, Object paramObject) {
/* 7847 */     return indirectionNotFound(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL indirectionNotFound(Object paramObject) {
/* 7851 */     return indirectionNotFound(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL recursiveTypecodeError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7857 */     MARSHAL localMARSHAL = new MARSHAL(1398079731, paramCompletionStatus);
/* 7858 */     if (paramThrowable != null) {
/* 7859 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7861 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7862 */       Object[] arrayOfObject = null;
/* 7863 */       doLog(Level.WARNING, "ORBUTIL.recursiveTypecodeError", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7867 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL recursiveTypecodeError(CompletionStatus paramCompletionStatus) {
/* 7871 */     return recursiveTypecodeError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL recursiveTypecodeError(Throwable paramThrowable) {
/* 7875 */     return recursiveTypecodeError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL recursiveTypecodeError() {
/* 7879 */     return recursiveTypecodeError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidSimpleTypecode(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7885 */     MARSHAL localMARSHAL = new MARSHAL(1398079732, paramCompletionStatus);
/* 7886 */     if (paramThrowable != null) {
/* 7887 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7889 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7890 */       Object[] arrayOfObject = null;
/* 7891 */       doLog(Level.WARNING, "ORBUTIL.invalidSimpleTypecode", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7895 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidSimpleTypecode(CompletionStatus paramCompletionStatus) {
/* 7899 */     return invalidSimpleTypecode(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidSimpleTypecode(Throwable paramThrowable) {
/* 7903 */     return invalidSimpleTypecode(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidSimpleTypecode() {
/* 7907 */     return invalidSimpleTypecode(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidComplexTypecode(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7913 */     MARSHAL localMARSHAL = new MARSHAL(1398079733, paramCompletionStatus);
/* 7914 */     if (paramThrowable != null) {
/* 7915 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7917 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7918 */       Object[] arrayOfObject = null;
/* 7919 */       doLog(Level.WARNING, "ORBUTIL.invalidComplexTypecode", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7923 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidComplexTypecode(CompletionStatus paramCompletionStatus) {
/* 7927 */     return invalidComplexTypecode(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidComplexTypecode(Throwable paramThrowable) {
/* 7931 */     return invalidComplexTypecode(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidComplexTypecode() {
/* 7935 */     return invalidComplexTypecode(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidTypecodeKindMarshal(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7941 */     MARSHAL localMARSHAL = new MARSHAL(1398079734, paramCompletionStatus);
/* 7942 */     if (paramThrowable != null) {
/* 7943 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7945 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7946 */       Object[] arrayOfObject = null;
/* 7947 */       doLog(Level.WARNING, "ORBUTIL.invalidTypecodeKindMarshal", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7951 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidTypecodeKindMarshal(CompletionStatus paramCompletionStatus) {
/* 7955 */     return invalidTypecodeKindMarshal(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidTypecodeKindMarshal(Throwable paramThrowable) {
/* 7959 */     return invalidTypecodeKindMarshal(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL invalidTypecodeKindMarshal() {
/* 7963 */     return invalidTypecodeKindMarshal(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedUnionDefault(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7969 */     MARSHAL localMARSHAL = new MARSHAL(1398079735, paramCompletionStatus);
/* 7970 */     if (paramThrowable != null) {
/* 7971 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 7973 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 7974 */       Object[] arrayOfObject = null;
/* 7975 */       doLog(Level.WARNING, "ORBUTIL.unexpectedUnionDefault", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 7979 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedUnionDefault(CompletionStatus paramCompletionStatus) {
/* 7983 */     return unexpectedUnionDefault(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedUnionDefault(Throwable paramThrowable) {
/* 7987 */     return unexpectedUnionDefault(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL unexpectedUnionDefault() {
/* 7991 */     return unexpectedUnionDefault(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL illegalUnionDiscriminatorType(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 7997 */     MARSHAL localMARSHAL = new MARSHAL(1398079736, paramCompletionStatus);
/* 7998 */     if (paramThrowable != null) {
/* 7999 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8001 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8002 */       Object[] arrayOfObject = null;
/* 8003 */       doLog(Level.WARNING, "ORBUTIL.illegalUnionDiscriminatorType", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8007 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL illegalUnionDiscriminatorType(CompletionStatus paramCompletionStatus) {
/* 8011 */     return illegalUnionDiscriminatorType(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL illegalUnionDiscriminatorType(Throwable paramThrowable) {
/* 8015 */     return illegalUnionDiscriminatorType(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL illegalUnionDiscriminatorType() {
/* 8019 */     return illegalUnionDiscriminatorType(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotSkipBytes(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 8025 */     MARSHAL localMARSHAL = new MARSHAL(1398079737, paramCompletionStatus);
/* 8026 */     if (paramThrowable != null) {
/* 8027 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8029 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8030 */       Object[] arrayOfObject = new Object[2];
/* 8031 */       arrayOfObject[0] = paramObject1;
/* 8032 */       arrayOfObject[1] = paramObject2;
/* 8033 */       doLog(Level.WARNING, "ORBUTIL.couldNotSkipBytes", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8037 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotSkipBytes(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 8041 */     return couldNotSkipBytes(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotSkipBytes(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 8045 */     return couldNotSkipBytes(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotSkipBytes(Object paramObject1, Object paramObject2) {
/* 8049 */     return couldNotSkipBytes(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL badChunkLength(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 8055 */     MARSHAL localMARSHAL = new MARSHAL(1398079738, paramCompletionStatus);
/* 8056 */     if (paramThrowable != null) {
/* 8057 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8059 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8060 */       Object[] arrayOfObject = new Object[2];
/* 8061 */       arrayOfObject[0] = paramObject1;
/* 8062 */       arrayOfObject[1] = paramObject2;
/* 8063 */       doLog(Level.WARNING, "ORBUTIL.badChunkLength", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8067 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badChunkLength(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 8071 */     return badChunkLength(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL badChunkLength(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 8075 */     return badChunkLength(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL badChunkLength(Object paramObject1, Object paramObject2) {
/* 8079 */     return badChunkLength(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL unableToLocateRepIdArray(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 8085 */     MARSHAL localMARSHAL = new MARSHAL(1398079739, paramCompletionStatus);
/* 8086 */     if (paramThrowable != null) {
/* 8087 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8089 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8090 */       Object[] arrayOfObject = new Object[1];
/* 8091 */       arrayOfObject[0] = paramObject;
/* 8092 */       doLog(Level.WARNING, "ORBUTIL.unableToLocateRepIdArray", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8096 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL unableToLocateRepIdArray(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 8100 */     return unableToLocateRepIdArray(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL unableToLocateRepIdArray(Throwable paramThrowable, Object paramObject) {
/* 8104 */     return unableToLocateRepIdArray(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL unableToLocateRepIdArray(Object paramObject) {
/* 8108 */     return unableToLocateRepIdArray(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badFixed(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 8114 */     MARSHAL localMARSHAL = new MARSHAL(1398079740, paramCompletionStatus);
/* 8115 */     if (paramThrowable != null) {
/* 8116 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8118 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8119 */       Object[] arrayOfObject = new Object[2];
/* 8120 */       arrayOfObject[0] = paramObject1;
/* 8121 */       arrayOfObject[1] = paramObject2;
/* 8122 */       doLog(Level.WARNING, "ORBUTIL.badFixed", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8126 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badFixed(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 8130 */     return badFixed(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL badFixed(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 8134 */     return badFixed(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL badFixed(Object paramObject1, Object paramObject2) {
/* 8138 */     return badFixed(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL readObjectLoadClassFailure(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject1, Object paramObject2)
/*      */   {
/* 8144 */     MARSHAL localMARSHAL = new MARSHAL(1398079741, paramCompletionStatus);
/* 8145 */     if (paramThrowable != null) {
/* 8146 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8148 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8149 */       Object[] arrayOfObject = new Object[2];
/* 8150 */       arrayOfObject[0] = paramObject1;
/* 8151 */       arrayOfObject[1] = paramObject2;
/* 8152 */       doLog(Level.WARNING, "ORBUTIL.readObjectLoadClassFailure", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8156 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL readObjectLoadClassFailure(CompletionStatus paramCompletionStatus, Object paramObject1, Object paramObject2) {
/* 8160 */     return readObjectLoadClassFailure(paramCompletionStatus, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL readObjectLoadClassFailure(Throwable paramThrowable, Object paramObject1, Object paramObject2) {
/* 8164 */     return readObjectLoadClassFailure(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL readObjectLoadClassFailure(Object paramObject1, Object paramObject2) {
/* 8168 */     return readObjectLoadClassFailure(CompletionStatus.COMPLETED_NO, null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotInstantiateHelper(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 8174 */     MARSHAL localMARSHAL = new MARSHAL(1398079742, paramCompletionStatus);
/* 8175 */     if (paramThrowable != null) {
/* 8176 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8178 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8179 */       Object[] arrayOfObject = new Object[1];
/* 8180 */       arrayOfObject[0] = paramObject;
/* 8181 */       doLog(Level.WARNING, "ORBUTIL.couldNotInstantiateHelper", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8185 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotInstantiateHelper(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 8189 */     return couldNotInstantiateHelper(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotInstantiateHelper(Throwable paramThrowable, Object paramObject) {
/* 8193 */     return couldNotInstantiateHelper(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotInstantiateHelper(Object paramObject) {
/* 8197 */     return couldNotInstantiateHelper(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL badToaOaid(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8203 */     MARSHAL localMARSHAL = new MARSHAL(1398079743, paramCompletionStatus);
/* 8204 */     if (paramThrowable != null) {
/* 8205 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8207 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8208 */       Object[] arrayOfObject = null;
/* 8209 */       doLog(Level.WARNING, "ORBUTIL.badToaOaid", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8213 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badToaOaid(CompletionStatus paramCompletionStatus) {
/* 8217 */     return badToaOaid(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badToaOaid(Throwable paramThrowable) {
/* 8221 */     return badToaOaid(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL badToaOaid() {
/* 8225 */     return badToaOaid(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotInvokeHelperReadMethod(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 8231 */     MARSHAL localMARSHAL = new MARSHAL(1398079744, paramCompletionStatus);
/* 8232 */     if (paramThrowable != null) {
/* 8233 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8235 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8236 */       Object[] arrayOfObject = new Object[1];
/* 8237 */       arrayOfObject[0] = paramObject;
/* 8238 */       doLog(Level.WARNING, "ORBUTIL.couldNotInvokeHelperReadMethod", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8242 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotInvokeHelperReadMethod(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 8246 */     return couldNotInvokeHelperReadMethod(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotInvokeHelperReadMethod(Throwable paramThrowable, Object paramObject) {
/* 8250 */     return couldNotInvokeHelperReadMethod(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotInvokeHelperReadMethod(Object paramObject) {
/* 8254 */     return couldNotInvokeHelperReadMethod(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotFindClass(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8260 */     MARSHAL localMARSHAL = new MARSHAL(1398079745, paramCompletionStatus);
/* 8261 */     if (paramThrowable != null) {
/* 8262 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8264 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8265 */       Object[] arrayOfObject = null;
/* 8266 */       doLog(Level.WARNING, "ORBUTIL.couldNotFindClass", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8270 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotFindClass(CompletionStatus paramCompletionStatus) {
/* 8274 */     return couldNotFindClass(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotFindClass(Throwable paramThrowable) {
/* 8278 */     return couldNotFindClass(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL couldNotFindClass() {
/* 8282 */     return couldNotFindClass(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badArgumentsNvlist(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8288 */     MARSHAL localMARSHAL = new MARSHAL(1398079746, paramCompletionStatus);
/* 8289 */     if (paramThrowable != null) {
/* 8290 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8292 */     if (this.logger.isLoggable(Level.FINE)) {
/* 8293 */       Object[] arrayOfObject = null;
/* 8294 */       doLog(Level.FINE, "ORBUTIL.badArgumentsNvlist", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8298 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL badArgumentsNvlist(CompletionStatus paramCompletionStatus) {
/* 8302 */     return badArgumentsNvlist(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL badArgumentsNvlist(Throwable paramThrowable) {
/* 8306 */     return badArgumentsNvlist(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL badArgumentsNvlist() {
/* 8310 */     return badArgumentsNvlist(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL stubCreateError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8316 */     MARSHAL localMARSHAL = new MARSHAL(1398079747, paramCompletionStatus);
/* 8317 */     if (paramThrowable != null) {
/* 8318 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8320 */     if (this.logger.isLoggable(Level.FINE)) {
/* 8321 */       Object[] arrayOfObject = null;
/* 8322 */       doLog(Level.FINE, "ORBUTIL.stubCreateError", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8326 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL stubCreateError(CompletionStatus paramCompletionStatus) {
/* 8330 */     return stubCreateError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL stubCreateError(Throwable paramThrowable) {
/* 8334 */     return stubCreateError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public MARSHAL stubCreateError() {
/* 8338 */     return stubCreateError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public MARSHAL javaSerializationException(CompletionStatus paramCompletionStatus, Throwable paramThrowable, Object paramObject)
/*      */   {
/* 8344 */     MARSHAL localMARSHAL = new MARSHAL(1398079748, paramCompletionStatus);
/* 8345 */     if (paramThrowable != null) {
/* 8346 */       localMARSHAL.initCause(paramThrowable);
/*      */     }
/* 8348 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8349 */       Object[] arrayOfObject = new Object[1];
/* 8350 */       arrayOfObject[0] = paramObject;
/* 8351 */       doLog(Level.WARNING, "ORBUTIL.javaSerializationException", arrayOfObject, ORBUtilSystemException.class, localMARSHAL);
/*      */     }
/*      */ 
/* 8355 */     return localMARSHAL;
/*      */   }
/*      */ 
/*      */   public MARSHAL javaSerializationException(CompletionStatus paramCompletionStatus, Object paramObject) {
/* 8359 */     return javaSerializationException(paramCompletionStatus, null, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL javaSerializationException(Throwable paramThrowable, Object paramObject) {
/* 8363 */     return javaSerializationException(CompletionStatus.COMPLETED_NO, paramThrowable, paramObject);
/*      */   }
/*      */ 
/*      */   public MARSHAL javaSerializationException(Object paramObject) {
/* 8367 */     return javaSerializationException(CompletionStatus.COMPLETED_NO, null, paramObject);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT genericNoImpl(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8377 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1398079689, paramCompletionStatus);
/* 8378 */     if (paramThrowable != null) {
/* 8379 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 8381 */     if (this.logger.isLoggable(Level.FINE)) {
/* 8382 */       Object[] arrayOfObject = null;
/* 8383 */       doLog(Level.FINE, "ORBUTIL.genericNoImpl", arrayOfObject, ORBUtilSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 8387 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT genericNoImpl(CompletionStatus paramCompletionStatus) {
/* 8391 */     return genericNoImpl(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT genericNoImpl(Throwable paramThrowable) {
/* 8395 */     return genericNoImpl(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT genericNoImpl() {
/* 8399 */     return genericNoImpl(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT contextNotImplemented(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8405 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1398079690, paramCompletionStatus);
/* 8406 */     if (paramThrowable != null) {
/* 8407 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 8409 */     if (this.logger.isLoggable(Level.FINE)) {
/* 8410 */       Object[] arrayOfObject = null;
/* 8411 */       doLog(Level.FINE, "ORBUTIL.contextNotImplemented", arrayOfObject, ORBUtilSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 8415 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT contextNotImplemented(CompletionStatus paramCompletionStatus) {
/* 8419 */     return contextNotImplemented(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT contextNotImplemented(Throwable paramThrowable) {
/* 8423 */     return contextNotImplemented(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT contextNotImplemented() {
/* 8427 */     return contextNotImplemented(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT getinterfaceNotImplemented(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8433 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1398079691, paramCompletionStatus);
/* 8434 */     if (paramThrowable != null) {
/* 8435 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 8437 */     if (this.logger.isLoggable(Level.FINE)) {
/* 8438 */       Object[] arrayOfObject = null;
/* 8439 */       doLog(Level.FINE, "ORBUTIL.getinterfaceNotImplemented", arrayOfObject, ORBUtilSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 8443 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT getinterfaceNotImplemented(CompletionStatus paramCompletionStatus) {
/* 8447 */     return getinterfaceNotImplemented(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT getinterfaceNotImplemented(Throwable paramThrowable) {
/* 8451 */     return getinterfaceNotImplemented(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT getinterfaceNotImplemented() {
/* 8455 */     return getinterfaceNotImplemented(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT sendDeferredNotimplemented(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8461 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1398079692, paramCompletionStatus);
/* 8462 */     if (paramThrowable != null) {
/* 8463 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 8465 */     if (this.logger.isLoggable(Level.FINE)) {
/* 8466 */       Object[] arrayOfObject = null;
/* 8467 */       doLog(Level.FINE, "ORBUTIL.sendDeferredNotimplemented", arrayOfObject, ORBUtilSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 8471 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT sendDeferredNotimplemented(CompletionStatus paramCompletionStatus) {
/* 8475 */     return sendDeferredNotimplemented(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT sendDeferredNotimplemented(Throwable paramThrowable) {
/* 8479 */     return sendDeferredNotimplemented(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT sendDeferredNotimplemented() {
/* 8483 */     return sendDeferredNotimplemented(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT longDoubleNotImplemented(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8489 */     NO_IMPLEMENT localNO_IMPLEMENT = new NO_IMPLEMENT(1398079693, paramCompletionStatus);
/* 8490 */     if (paramThrowable != null) {
/* 8491 */       localNO_IMPLEMENT.initCause(paramThrowable);
/*      */     }
/* 8493 */     if (this.logger.isLoggable(Level.FINE)) {
/* 8494 */       Object[] arrayOfObject = null;
/* 8495 */       doLog(Level.FINE, "ORBUTIL.longDoubleNotImplemented", arrayOfObject, ORBUtilSystemException.class, localNO_IMPLEMENT);
/*      */     }
/*      */ 
/* 8499 */     return localNO_IMPLEMENT;
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT longDoubleNotImplemented(CompletionStatus paramCompletionStatus) {
/* 8503 */     return longDoubleNotImplemented(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT longDoubleNotImplemented(Throwable paramThrowable) {
/* 8507 */     return longDoubleNotImplemented(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public NO_IMPLEMENT longDoubleNotImplemented() {
/* 8511 */     return longDoubleNotImplemented(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noServerScInDispatch(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8521 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398079689, paramCompletionStatus);
/* 8522 */     if (paramThrowable != null) {
/* 8523 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 8525 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8526 */       Object[] arrayOfObject = null;
/* 8527 */       doLog(Level.WARNING, "ORBUTIL.noServerScInDispatch", arrayOfObject, ORBUtilSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 8531 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noServerScInDispatch(CompletionStatus paramCompletionStatus) {
/* 8535 */     return noServerScInDispatch(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noServerScInDispatch(Throwable paramThrowable) {
/* 8539 */     return noServerScInDispatch(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER noServerScInDispatch() {
/* 8543 */     return noServerScInDispatch(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER orbConnectError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8549 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398079690, paramCompletionStatus);
/* 8550 */     if (paramThrowable != null) {
/* 8551 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 8553 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8554 */       Object[] arrayOfObject = null;
/* 8555 */       doLog(Level.WARNING, "ORBUTIL.orbConnectError", arrayOfObject, ORBUtilSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 8559 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER orbConnectError(CompletionStatus paramCompletionStatus) {
/* 8563 */     return orbConnectError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER orbConnectError(Throwable paramThrowable) {
/* 8567 */     return orbConnectError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER orbConnectError() {
/* 8571 */     return orbConnectError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER adapterInactiveInActivation(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8577 */     OBJ_ADAPTER localOBJ_ADAPTER = new OBJ_ADAPTER(1398079691, paramCompletionStatus);
/* 8578 */     if (paramThrowable != null) {
/* 8579 */       localOBJ_ADAPTER.initCause(paramThrowable);
/*      */     }
/* 8581 */     if (this.logger.isLoggable(Level.FINE)) {
/* 8582 */       Object[] arrayOfObject = null;
/* 8583 */       doLog(Level.FINE, "ORBUTIL.adapterInactiveInActivation", arrayOfObject, ORBUtilSystemException.class, localOBJ_ADAPTER);
/*      */     }
/*      */ 
/* 8587 */     return localOBJ_ADAPTER;
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER adapterInactiveInActivation(CompletionStatus paramCompletionStatus) {
/* 8591 */     return adapterInactiveInActivation(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER adapterInactiveInActivation(Throwable paramThrowable) {
/* 8595 */     return adapterInactiveInActivation(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJ_ADAPTER adapterInactiveInActivation() {
/* 8599 */     return adapterInactiveInActivation(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST locateUnknownObject(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8609 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398079689, paramCompletionStatus);
/* 8610 */     if (paramThrowable != null) {
/* 8611 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 8613 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8614 */       Object[] arrayOfObject = null;
/* 8615 */       doLog(Level.WARNING, "ORBUTIL.locateUnknownObject", arrayOfObject, ORBUtilSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 8619 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST locateUnknownObject(CompletionStatus paramCompletionStatus) {
/* 8623 */     return locateUnknownObject(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST locateUnknownObject(Throwable paramThrowable) {
/* 8627 */     return locateUnknownObject(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST locateUnknownObject() {
/* 8631 */     return locateUnknownObject(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badServerId(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8637 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398079690, paramCompletionStatus);
/* 8638 */     if (paramThrowable != null) {
/* 8639 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 8641 */     if (this.logger.isLoggable(Level.FINE)) {
/* 8642 */       Object[] arrayOfObject = null;
/* 8643 */       doLog(Level.FINE, "ORBUTIL.badServerId", arrayOfObject, ORBUtilSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 8647 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badServerId(CompletionStatus paramCompletionStatus) {
/* 8651 */     return badServerId(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badServerId(Throwable paramThrowable) {
/* 8655 */     return badServerId(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badServerId() {
/* 8659 */     return badServerId(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badSkeleton(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8665 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398079691, paramCompletionStatus);
/* 8666 */     if (paramThrowable != null) {
/* 8667 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 8669 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8670 */       Object[] arrayOfObject = null;
/* 8671 */       doLog(Level.WARNING, "ORBUTIL.badSkeleton", arrayOfObject, ORBUtilSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 8675 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badSkeleton(CompletionStatus paramCompletionStatus) {
/* 8679 */     return badSkeleton(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badSkeleton(Throwable paramThrowable) {
/* 8683 */     return badSkeleton(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badSkeleton() {
/* 8687 */     return badSkeleton(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST servantNotFound(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8693 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398079692, paramCompletionStatus);
/* 8694 */     if (paramThrowable != null) {
/* 8695 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 8697 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8698 */       Object[] arrayOfObject = null;
/* 8699 */       doLog(Level.WARNING, "ORBUTIL.servantNotFound", arrayOfObject, ORBUtilSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 8703 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST servantNotFound(CompletionStatus paramCompletionStatus) {
/* 8707 */     return servantNotFound(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST servantNotFound(Throwable paramThrowable) {
/* 8711 */     return servantNotFound(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST servantNotFound() {
/* 8715 */     return servantNotFound(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST noObjectAdapterFactory(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8721 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398079693, paramCompletionStatus);
/* 8722 */     if (paramThrowable != null) {
/* 8723 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 8725 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8726 */       Object[] arrayOfObject = null;
/* 8727 */       doLog(Level.WARNING, "ORBUTIL.noObjectAdapterFactory", arrayOfObject, ORBUtilSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 8731 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST noObjectAdapterFactory(CompletionStatus paramCompletionStatus) {
/* 8735 */     return noObjectAdapterFactory(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST noObjectAdapterFactory(Throwable paramThrowable) {
/* 8739 */     return noObjectAdapterFactory(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST noObjectAdapterFactory() {
/* 8743 */     return noObjectAdapterFactory(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badAdapterId(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8749 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398079694, paramCompletionStatus);
/* 8750 */     if (paramThrowable != null) {
/* 8751 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 8753 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8754 */       Object[] arrayOfObject = null;
/* 8755 */       doLog(Level.WARNING, "ORBUTIL.badAdapterId", arrayOfObject, ORBUtilSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 8759 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badAdapterId(CompletionStatus paramCompletionStatus) {
/* 8763 */     return badAdapterId(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badAdapterId(Throwable paramThrowable) {
/* 8767 */     return badAdapterId(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST badAdapterId() {
/* 8771 */     return badAdapterId(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST dynAnyDestroyed(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8777 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398079695, paramCompletionStatus);
/* 8778 */     if (paramThrowable != null) {
/* 8779 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*      */     }
/* 8781 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8782 */       Object[] arrayOfObject = null;
/* 8783 */       doLog(Level.WARNING, "ORBUTIL.dynAnyDestroyed", arrayOfObject, ORBUtilSystemException.class, localOBJECT_NOT_EXIST);
/*      */     }
/*      */ 
/* 8787 */     return localOBJECT_NOT_EXIST;
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST dynAnyDestroyed(CompletionStatus paramCompletionStatus) {
/* 8791 */     return dynAnyDestroyed(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST dynAnyDestroyed(Throwable paramThrowable) {
/* 8795 */     return dynAnyDestroyed(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public OBJECT_NOT_EXIST dynAnyDestroyed() {
/* 8799 */     return dynAnyDestroyed(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT requestCanceled(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8809 */     TRANSIENT localTRANSIENT = new TRANSIENT(1398079689, paramCompletionStatus);
/* 8810 */     if (paramThrowable != null) {
/* 8811 */       localTRANSIENT.initCause(paramThrowable);
/*      */     }
/* 8813 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8814 */       Object[] arrayOfObject = null;
/* 8815 */       doLog(Level.WARNING, "ORBUTIL.requestCanceled", arrayOfObject, ORBUtilSystemException.class, localTRANSIENT);
/*      */     }
/*      */ 
/* 8819 */     return localTRANSIENT;
/*      */   }
/*      */ 
/*      */   public TRANSIENT requestCanceled(CompletionStatus paramCompletionStatus) {
/* 8823 */     return requestCanceled(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public TRANSIENT requestCanceled(Throwable paramThrowable) {
/* 8827 */     return requestCanceled(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public TRANSIENT requestCanceled() {
/* 8831 */     return requestCanceled(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownCorbaExc(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8841 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398079689, paramCompletionStatus);
/* 8842 */     if (paramThrowable != null) {
/* 8843 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 8845 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8846 */       Object[] arrayOfObject = null;
/* 8847 */       doLog(Level.WARNING, "ORBUTIL.unknownCorbaExc", arrayOfObject, ORBUtilSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 8851 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownCorbaExc(CompletionStatus paramCompletionStatus) {
/* 8855 */     return unknownCorbaExc(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownCorbaExc(Throwable paramThrowable) {
/* 8859 */     return unknownCorbaExc(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownCorbaExc() {
/* 8863 */     return unknownCorbaExc(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN runtimeexception(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8869 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398079690, paramCompletionStatus);
/* 8870 */     if (paramThrowable != null) {
/* 8871 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 8873 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8874 */       Object[] arrayOfObject = null;
/* 8875 */       doLog(Level.WARNING, "ORBUTIL.runtimeexception", arrayOfObject, ORBUtilSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 8879 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN runtimeexception(CompletionStatus paramCompletionStatus) {
/* 8883 */     return runtimeexception(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN runtimeexception(Throwable paramThrowable) {
/* 8887 */     return runtimeexception(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN runtimeexception() {
/* 8891 */     return runtimeexception(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerError(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8897 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398079691, paramCompletionStatus);
/* 8898 */     if (paramThrowable != null) {
/* 8899 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 8901 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8902 */       Object[] arrayOfObject = null;
/* 8903 */       doLog(Level.WARNING, "ORBUTIL.unknownServerError", arrayOfObject, ORBUtilSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 8907 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerError(CompletionStatus paramCompletionStatus) {
/* 8911 */     return unknownServerError(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerError(Throwable paramThrowable) {
/* 8915 */     return unknownServerError(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownServerError() {
/* 8919 */     return unknownServerError(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownDsiSysex(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8925 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398079692, paramCompletionStatus);
/* 8926 */     if (paramThrowable != null) {
/* 8927 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 8929 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8930 */       Object[] arrayOfObject = null;
/* 8931 */       doLog(Level.WARNING, "ORBUTIL.unknownDsiSysex", arrayOfObject, ORBUtilSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 8935 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownDsiSysex(CompletionStatus paramCompletionStatus) {
/* 8939 */     return unknownDsiSysex(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownDsiSysex(Throwable paramThrowable) {
/* 8943 */     return unknownDsiSysex(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownDsiSysex() {
/* 8947 */     return unknownDsiSysex(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownSysex(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8953 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398079693, paramCompletionStatus);
/* 8954 */     if (paramThrowable != null) {
/* 8955 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 8957 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8958 */       Object[] arrayOfObject = null;
/* 8959 */       doLog(Level.WARNING, "ORBUTIL.unknownSysex", arrayOfObject, ORBUtilSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 8963 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownSysex(CompletionStatus paramCompletionStatus) {
/* 8967 */     return unknownSysex(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownSysex(Throwable paramThrowable) {
/* 8971 */     return unknownSysex(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownSysex() {
/* 8975 */     return unknownSysex(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN wrongInterfaceDef(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 8981 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398079694, paramCompletionStatus);
/* 8982 */     if (paramThrowable != null) {
/* 8983 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 8985 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 8986 */       Object[] arrayOfObject = null;
/* 8987 */       doLog(Level.WARNING, "ORBUTIL.wrongInterfaceDef", arrayOfObject, ORBUtilSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 8991 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN wrongInterfaceDef(CompletionStatus paramCompletionStatus) {
/* 8995 */     return wrongInterfaceDef(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN wrongInterfaceDef(Throwable paramThrowable) {
/* 8999 */     return wrongInterfaceDef(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN wrongInterfaceDef() {
/* 9003 */     return wrongInterfaceDef(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN noInterfaceDefStub(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 9009 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398079695, paramCompletionStatus);
/* 9010 */     if (paramThrowable != null) {
/* 9011 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 9013 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 9014 */       Object[] arrayOfObject = null;
/* 9015 */       doLog(Level.WARNING, "ORBUTIL.noInterfaceDefStub", arrayOfObject, ORBUtilSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 9019 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN noInterfaceDefStub(CompletionStatus paramCompletionStatus) {
/* 9023 */     return noInterfaceDefStub(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN noInterfaceDefStub(Throwable paramThrowable) {
/* 9027 */     return noInterfaceDefStub(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN noInterfaceDefStub() {
/* 9031 */     return noInterfaceDefStub(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownExceptionInDispatch(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*      */   {
/* 9037 */     UNKNOWN localUNKNOWN = new UNKNOWN(1398079697, paramCompletionStatus);
/* 9038 */     if (paramThrowable != null) {
/* 9039 */       localUNKNOWN.initCause(paramThrowable);
/*      */     }
/* 9041 */     if (this.logger.isLoggable(Level.FINE)) {
/* 9042 */       Object[] arrayOfObject = null;
/* 9043 */       doLog(Level.FINE, "ORBUTIL.unknownExceptionInDispatch", arrayOfObject, ORBUtilSystemException.class, localUNKNOWN);
/*      */     }
/*      */ 
/* 9047 */     return localUNKNOWN;
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownExceptionInDispatch(CompletionStatus paramCompletionStatus) {
/* 9051 */     return unknownExceptionInDispatch(paramCompletionStatus, null);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownExceptionInDispatch(Throwable paramThrowable) {
/* 9055 */     return unknownExceptionInDispatch(CompletionStatus.COMPLETED_NO, paramThrowable);
/*      */   }
/*      */ 
/*      */   public UNKNOWN unknownExceptionInDispatch() {
/* 9059 */     return unknownExceptionInDispatch(CompletionStatus.COMPLETED_NO, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.logging.ORBUtilSystemException
 * JD-Core Version:    0.6.2
 */