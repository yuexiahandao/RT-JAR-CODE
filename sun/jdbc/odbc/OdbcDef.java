/*     */ package sun.jdbc.odbc;
/*     */ 
/*     */ public class OdbcDef
/*     */ {
/*     */   public static final short SQL_SUCCESS = 0;
/*     */   public static final short SQL_SUCCESS_WITH_INFO = 1;
/*     */   public static final short SQL_NO_DATA = 100;
/*     */   public static final short SQL_NO_DATA_FOUND = 100;
/*     */   public static final short SQL_ERROR = -1;
/*     */   public static final short SQL_INVALID_HANDLE = -2;
/*     */   public static final short SQL_STILL_EXECUTING = 2;
/*     */   public static final short SQL_NEED_DATA = 99;
/*     */   public static final short SQL_TRUE = 1;
/*     */   public static final short SQL_FALSE = 0;
/*     */   public static final short SQL_C_BINARY = -2;
/*     */   public static final short SQL_C_BIT = -7;
/*     */   public static final short SQL_C_BOOKMARK = -18;
/*     */   public static final short SQL_C_CHAR = 1;
/*     */   public static final short SQL_C_DATE = 91;
/*     */   public static final short SQL_C_DEFAULT = 99;
/*     */   public static final short SQL_C_DOUBLE = 8;
/*     */   public static final short SQL_C_FLOAT = 7;
/*     */   public static final short SQL_C_LONG = 4;
/*     */   public static final short SQL_C_SHORT = 5;
/*     */   public static final short SQL_C_SLONG = -16;
/*     */   public static final short SQL_C_SSHORT = -15;
/*     */   public static final short SQL_C_STINYINT = -26;
/*     */   public static final short SQL_C_TIME = 92;
/*     */   public static final short SQL_C_TIMESTAMP = 93;
/*     */   public static final short SQL_C_TINYINT = -6;
/*     */   public static final short SQL_C_ULONG = -18;
/*     */   public static final short SQL_C_USHORT = -17;
/*     */   public static final short SQL_C_UTINYINT = -28;
/*     */   public static final short SQL_C_SBIGINT = -25;
/*     */   public static final short SQL_CHAR = 1;
/*     */   public static final short SQL_DATE = 9;
/*     */   public static final short SQL_TIME = 10;
/*     */   public static final short SQL_TIMESTAMP = 11;
/*     */   public static final short SQL_TYPE_NULL = 0;
/*     */   public static final short SQL_TYPE_UNKNOWN = 9999;
/*     */   public static final int SQL_NULL_HENV = 0;
/*     */   public static final int SQL_NULL_HDBC = 0;
/*     */   public static final int SQL_NULL_HSTMT = 0;
/*     */   public static final int SQL_NULL_DATA = -1;
/*     */   public static final int SQL_DATA_AT_EXEC = -2;
/*     */   public static final int SQL_NTS = -3;
/*     */   public static final int SQL_NO_TOTAL = -4;
/*     */   public static final int SQL_DEFAULT_PARAM = -5;
/*     */   public static final int SQL_COLUMN_IGNORE = -6;
/*     */   public static final int SQL_CLOSE = 0;
/*     */   public static final int SQL_DROP = 1;
/*     */   public static final int SQL_UNBIND = 2;
/*     */   public static final int SQL_RESET_PARAMS = 3;
/*     */   public static final short SQL_ACCESS_MODE = 101;
/*     */   public static final int SQL_MODE_READ_WRITE = 0;
/*     */   public static final int SQL_MODE_READ_ONLY = 1;
/*     */   public static final short SQL_AUTOCOMMIT = 102;
/*     */   public static final int SQL_AUTOCOMMIT_OFF = 0;
/*     */   public static final int SQL_AUTOCOMMIT_ON = 1;
/*     */   public static final short SQL_LOGIN_TIMEOUT = 103;
/*     */   public static final short SQL_TXN_ISOLATION = 108;
/*     */   public static final short SQL_CURRENT_QUALIFIER = 109;
/*     */   public static final short SQL_ACTIVE_CONNECTIONS = 0;
/*     */   public static final short SQL_ACTIVE_STATEMENTS = 1;
/*     */   public static final short SQL_DRIVER_NAME = 6;
/*     */   public static final short SQL_DRIVER_VER = 7;
/*     */   public static final short SQL_ODBC_VER = 10;
/*     */   public static final short SQL_SEARCH_PATTERN_ESCAPE = 14;
/*     */   public static final short SQL_ODBC_SQL_CONFORMANCE = 15;
/*     */   public static final short SQL_OSC_MINIMUM = 0;
/*     */   public static final short SQL_OSC_CORE = 1;
/*     */   public static final short SQL_OSC_EXTENDED = 2;
/*     */   public static final short SQL_DATABASE_NAME = 16;
/*     */   public static final short SQL_DBMS_NAME = 17;
/*     */   public static final short SQL_DBMS_VER = 18;
/*     */   public static final short SQL_ACCESSIBLE_TABLES = 19;
/*     */   public static final short SQL_ACCESSIBLE_PROCEDURES = 20;
/*     */   public static final short SQL_PROCEDURES = 21;
/*     */   public static final short SQL_CONCAT_NULL_BEHAVIOR = 22;
/*     */   public static final short SQL_CB_NULL = 0;
/*     */   public static final short SQL_CURSOR_COMMIT_BEHAVIOR = 23;
/*     */   public static final short SQL_CURSOR_ROLLBACK_BEHAVIOR = 24;
/*     */   public static final short SQL_CB_DELETE = 0;
/*     */   public static final short SQL_CB_CLOSE = 1;
/*     */   public static final short SQL_CB_PRESERVE = 2;
/*     */   public static final short SQL_DATA_SOURCE_READ_ONLY = 25;
/*     */   public static final short SQL_DEFAULT_TXN_ISOLATION = 26;
/*     */   public static final short SQL_EXPRESSIONS_IN_ORDERBY = 27;
/*     */   public static final short SQL_IDENTIFIER_CASE = 28;
/*     */   public static final short SQL_IC_UPPER = 1;
/*     */   public static final short SQL_IC_LOWER = 2;
/*     */   public static final short SQL_IC_SENSITIVE = 3;
/*     */   public static final short SQL_IC_MIXED = 4;
/*     */   public static final short SQL_IDENTIFIER_QUOTE_CHAR = 29;
/*     */   public static final short SQL_MAX_COLUMN_NAME_LEN = 30;
/*     */   public static final short SQL_MAX_CURSOR_NAME_LEN = 31;
/*     */   public static final short SQL_MAX_OWNER_NAME_LEN = 32;
/*     */   public static final short SQL_MAX_PROCEDURE_NAME_LEN = 33;
/*     */   public static final short SQL_MAX_QUALIFIER_NAME_LEN = 34;
/*     */   public static final short SQL_MAX_TABLE_NAME_LEN = 35;
/*     */   public static final short SQL_MULT_RESULT_SETS = 36;
/*     */   public static final short SQL_MULTIPLE_ACTIVE_TXN = 37;
/*     */   public static final short SQL_OUTER_JOINS = 38;
/*     */   public static final short SQL_OWNER_TERM = 39;
/*     */   public static final short SQL_PROCEDURE_TERM = 40;
/*     */   public static final short SQL_QUALIFIER_NAME_SEPARATOR = 41;
/*     */   public static final short SQL_QUALIFIER_TERM = 42;
/*     */   public static final short SQL_TXN_CAPABLE = 46;
/*     */   public static final short SQL_TC_NONE = 0;
/*     */   public static final short SQL_TC_DML = 1;
/*     */   public static final short SQL_TC_ALL = 2;
/*     */   public static final short SQL_TC_DDL_COMMIT = 3;
/*     */   public static final short SQL_TC_DDL_IGNORE = 4;
/*     */   public static final short SQL_USER_NAME = 47;
/*     */   public static final short SQL_CONVERT_FUNCTIONS = 48;
/*     */   public static final int SQL_FN_CVT_CONVERT = 1;
/*     */   public static final short SQL_NUMERIC_FUNCTIONS = 49;
/*     */   public static final int SQL_FN_NUM_ABS = 1;
/*     */   public static final int SQL_FN_NUM_ACOS = 2;
/*     */   public static final int SQL_FN_NUM_ASIN = 4;
/*     */   public static final int SQL_FN_NUM_ATAN = 8;
/*     */   public static final int SQL_FN_NUM_ATAN2 = 16;
/*     */   public static final int SQL_FN_NUM_CEILING = 32;
/*     */   public static final int SQL_FN_NUM_COS = 64;
/*     */   public static final int SQL_FN_NUM_COT = 128;
/*     */   public static final int SQL_FN_NUM_EXP = 256;
/*     */   public static final int SQL_FN_NUM_FLOOR = 512;
/*     */   public static final int SQL_FN_NUM_LOG = 1024;
/*     */   public static final int SQL_FN_NUM_MOD = 2048;
/*     */   public static final int SQL_FN_NUM_SIGN = 4096;
/*     */   public static final int SQL_FN_NUM_SIN = 8192;
/*     */   public static final int SQL_FN_NUM_SQRT = 16384;
/*     */   public static final int SQL_FN_NUM_TAN = 32768;
/*     */   public static final int SQL_FN_NUM_PI = 65536;
/*     */   public static final int SQL_FN_NUM_RAND = 131072;
/*     */   public static final int SQL_FN_NUM_DEGREES = 262144;
/*     */   public static final int SQL_FN_NUM_LOG10 = 524288;
/*     */   public static final int SQL_FN_NUM_POWER = 1048576;
/*     */   public static final int SQL_FN_NUM_RADIANS = 2097152;
/*     */   public static final int SQL_FN_NUM_ROUND = 4194304;
/*     */   public static final int SQL_FN_NUM_TRUNCATE = 8388608;
/*     */   public static final short SQL_STRING_FUNCTIONS = 50;
/*     */   public static final int SQL_FN_STR_CONCAT = 1;
/*     */   public static final int SQL_FN_STR_INSERT = 2;
/*     */   public static final int SQL_FN_STR_LEFT = 4;
/*     */   public static final int SQL_FN_STR_LTRIM = 8;
/*     */   public static final int SQL_FN_STR_LENGTH = 16;
/*     */   public static final int SQL_FN_STR_LOCATE = 32;
/*     */   public static final int SQL_FN_STR_LCASE = 64;
/*     */   public static final int SQL_FN_STR_REPEAT = 128;
/*     */   public static final int SQL_FN_STR_REPLACE = 256;
/*     */   public static final int SQL_FN_STR_RIGHT = 512;
/*     */   public static final int SQL_FN_STR_RTRIM = 1024;
/*     */   public static final int SQL_FN_STR_SUBSTRING = 2048;
/*     */   public static final int SQL_FN_STR_UCASE = 4096;
/*     */   public static final int SQL_FN_STR_ASCII = 8192;
/*     */   public static final int SQL_FN_STR_CHAR = 16384;
/*     */   public static final int SQL_FN_STR_DIFFERENCE = 32768;
/*     */   public static final int SQL_FN_STR_LOCATE_2 = 65536;
/*     */   public static final int SQL_FN_STR_SOUNDEX = 131072;
/*     */   public static final int SQL_FN_STR_SPACE = 262144;
/*     */   public static final short SQL_SYSTEM_FUNCTIONS = 51;
/*     */   public static final int SQL_FN_SYS_USERNAME = 1;
/*     */   public static final int SQL_FN_SYS_DBNAME = 2;
/*     */   public static final int SQL_FN_SYS_IFNULL = 4;
/*     */   public static final short SQL_TIMEDATE_FUNCTIONS = 52;
/*     */   public static final int SQL_FN_TD_NOW = 1;
/*     */   public static final int SQL_FN_TD_CURDATE = 2;
/*     */   public static final int SQL_FN_TD_DAYOFMONTH = 4;
/*     */   public static final int SQL_FN_TD_DAYOFWEEK = 8;
/*     */   public static final int SQL_FN_TD_DAYOFYEAR = 16;
/*     */   public static final int SQL_FN_TD_MONTH = 32;
/*     */   public static final int SQL_FN_TD_QUARTER = 64;
/*     */   public static final int SQL_FN_TD_WEEK = 128;
/*     */   public static final int SQL_FN_TD_YEAR = 256;
/*     */   public static final int SQL_FN_TD_CURTIME = 512;
/*     */   public static final int SQL_FN_TD_HOUR = 1024;
/*     */   public static final int SQL_FN_TD_MINUTE = 2048;
/*     */   public static final int SQL_FN_TD_SECOND = 4096;
/*     */   public static final int SQL_FN_TD_TIMESTAMPADD = 8192;
/*     */   public static final int SQL_FN_TD_TIMESTAMPDIFF = 16384;
/*     */   public static final int SQL_FN_TD_DAYNAME = 32768;
/*     */   public static final int SQL_FN_TD_MONTHNAME = 65536;
/*     */   public static final short SQL_CONVERT_BIGINT = 53;
/*     */   public static final short SQL_CONVERT_BINARY = 54;
/*     */   public static final short SQL_CONVERT_BIT = 55;
/*     */   public static final short SQL_CONVERT_CHAR = 56;
/*     */   public static final short SQL_CONVERT_DATE = 57;
/*     */   public static final short SQL_CONVERT_DECIMAL = 58;
/*     */   public static final short SQL_CONVERT_DOUBLE = 59;
/*     */   public static final short SQL_CONVERT_FLOAT = 60;
/*     */   public static final short SQL_CONVERT_INTEGER = 61;
/*     */   public static final short SQL_CONVERT_LONGVARCHAR = 62;
/*     */   public static final short SQL_CONVERT_NUMERIC = 63;
/*     */   public static final short SQL_CONVERT_REAL = 64;
/*     */   public static final short SQL_CONVERT_SMALLINT = 65;
/*     */   public static final short SQL_CONVERT_TIME = 66;
/*     */   public static final short SQL_CONVERT_TIMESTAMP = 67;
/*     */   public static final short SQL_CONVERT_TINYINT = 68;
/*     */   public static final short SQL_CONVERT_VARBINARY = 69;
/*     */   public static final short SQL_CONVERT_VARCHAR = 70;
/*     */   public static final short SQL_CONVERT_LONGVARBINARY = 71;
/*     */   public static final int SQL_CVT_CHAR = 1;
/*     */   public static final int SQL_CVT_NUMERIC = 2;
/*     */   public static final int SQL_CVT_DECIMAL = 4;
/*     */   public static final int SQL_CVT_INTEGER = 8;
/*     */   public static final int SQL_CVT_SMALLINT = 16;
/*     */   public static final int SQL_CVT_FLOAT = 32;
/*     */   public static final int SQL_CVT_REAL = 64;
/*     */   public static final int SQL_CVT_DOUBLE = 128;
/*     */   public static final int SQL_CVT_VARCHAR = 256;
/*     */   public static final int SQL_CVT_LONGVARCHAR = 512;
/*     */   public static final int SQL_CVT_BINARY = 1024;
/*     */   public static final int SQL_CVT_VARBINARY = 2048;
/*     */   public static final int SQL_CVT_BIT = 4096;
/*     */   public static final int SQL_CVT_TINYINT = 8192;
/*     */   public static final int SQL_CVT_BIGINT = 16384;
/*     */   public static final int SQL_CVT_DATE = 32768;
/*     */   public static final int SQL_CVT_TIME = 65536;
/*     */   public static final int SQL_CVT_TIMESTAMP = 131072;
/*     */   public static final int SQL_CVT_LONGVARBINARY = 262144;
/*     */   public static final short SQL_TXN_ISOLATION_OPTION = 72;
/*     */   public static final int SQL_TXN_READ_UNCOMMITTED = 1;
/*     */   public static final int SQL_TXN_READ_COMMITTED = 2;
/*     */   public static final int SQL_TXN_REPEATABLE_READ = 4;
/*     */   public static final int SQL_TXN_SERIALIZABLE = 8;
/*     */   public static final int SQL_TXN_VERSIONING = 16;
/*     */   public static final short SQL_CORRELATION_NAME = 74;
/*     */   public static final short SQL_CN_NONE = 0;
/*     */   public static final short SQL_CN_DIFFERENT = 1;
/*     */   public static final short SQL_CN_ANY = 2;
/*     */   public static final short SQL_ODBC_SQL_OPT_IEF = 73;
/*     */   public static final short SQL_NON_NULLABLE_COLUMNS = 75;
/*     */   public static final short SQL_NNC_NULL = 0;
/*     */   public static final short SQL_NNC_NON_NULL = 1;
/*     */   public static final short SQL_POSITIONED_STATEMENTS = 80;
/*     */   public static final int SQL_PS_POSITIONED_DELETE = 1;
/*     */   public static final int SQL_PS_POSITIONED_UPDATE = 2;
/*     */   public static final int SQL_PS_SELECT_FOR_UPDATE = 4;
/*     */   public static final short SQL_FILE_USAGE = 84;
/*     */   public static final short SQL_FILE_TABLE = 1;
/*     */   public static final short SQL_FILE_QUALIFIER = 2;
/*     */   public static final short SQL_NULL_COLLATION = 85;
/*     */   public static final short SQL_NC_HIGH = 0;
/*     */   public static final short SQL_NC_LOW = 1;
/*     */   public static final short SQL_NC_START = 2;
/*     */   public static final short SQL_NC_END = 4;
/*     */   public static final short SQL_ALTER_TABLE = 86;
/*     */   public static final int SQL_AT_ADD_COLUMN = 1;
/*     */   public static final int SQL_AT_DROP_COLUMN = 2;
/*     */   public static final short SQL_COLUMN_ALIAS = 87;
/*     */   public static final short SQL_GROUP_BY = 88;
/*     */   public static final short SQL_GB_NOT_SUPPORTED = 0;
/*     */   public static final short SQL_GB_GROUP_BY_EQUALS_SELECT = 1;
/*     */   public static final short SQL_GB_GROUP_BY_CONTAINS_SELECT = 2;
/*     */   public static final short SQL_GB_NO_RELATION = 3;
/*     */   public static final short SQL_KEYWORDS = 89;
/*     */   public static final short SQL_ORDER_BY_COLUMNS_IN_SELECT = 90;
/*     */   public static final short SQL_OWNER_USAGE = 91;
/*     */   public static final int SQL_OU_DML_STATEMENTS = 1;
/*     */   public static final int SQL_OU_PROCEDURE_INVOCATION = 2;
/*     */   public static final int SQL_OU_TABLE_DEFINITION = 4;
/*     */   public static final int SQL_OU_INDEX_DEFINITION = 8;
/*     */   public static final int SQL_OU_PRIVILEGE_DEFINITION = 16;
/*     */   public static final short SQL_QUALIFIER_USAGE = 92;
/*     */   public static final int SQL_QU_DML_STATEMENTS = 1;
/*     */   public static final int SQL_QU_PROCEDURE_INVOCATION = 2;
/*     */   public static final int SQL_QU_TABLE_DEFINITION = 4;
/*     */   public static final int SQL_QU_INDEX_DEFINITION = 8;
/*     */   public static final int SQL_QU_PRIVILEGE_DEFINITION = 16;
/*     */   public static final short SQL_QUOTED_IDENTIFIER_CASE = 93;
/*     */   public static final short SQL_SPECIAL_CHARACTERS = 94;
/*     */   public static final short SQL_SUBQUERIES = 95;
/*     */   public static final int SQL_SQ_COMPARISON = 1;
/*     */   public static final int SQL_SQ_EXISTS = 2;
/*     */   public static final int SQL_SQ_IN = 4;
/*     */   public static final int SQL_SQ_QUANTIFIED = 8;
/*     */   public static final int SQL_SQ_CORRELATED_SUBQUERIES = 16;
/*     */   public static final short SQL_UNION = 96;
/*     */   public static final int SQL_U_UNION = 1;
/*     */   public static final int SQL_U_UNION_ALL = 2;
/*     */   public static final short SQL_MAX_COLUMNS_IN_GROUP_BY = 97;
/*     */   public static final short SQL_MAX_COLUMNS_IN_INDEX = 98;
/*     */   public static final short SQL_MAX_COLUMNS_IN_ORDER_BY = 99;
/*     */   public static final short SQL_MAX_COLUMNS_IN_SELECT = 100;
/*     */   public static final short SQL_MAX_COLUMNS_IN_TABLE = 101;
/*     */   public static final short SQL_MAX_INDEX_SIZE = 102;
/*     */   public static final short SQL_MAX_ROW_SIZE_INCLUDES_LONG = 103;
/*     */   public static final short SQL_MAX_ROW_SIZE = 104;
/*     */   public static final short SQL_MAX_STATEMENT_LEN = 105;
/*     */   public static final short SQL_MAX_TABLES_IN_SELECT = 106;
/*     */   public static final short SQL_MAX_USER_NAME_LEN = 107;
/*     */   public static final short SQL_MAX_CHAR_LITERAL_LEN = 108;
/*     */   public static final short SQL_MAX_BINARY_LITERAL_LEN = 112;
/*     */   public static final short SQL_LIKE_CLAUSE_ESCAPE = 113;
/*     */   public static final short SQL_QUALIFIER_LOCATION = 114;
/*     */   public static final short SQL_QL_START = 1;
/*     */   public static final short SQL_QL_END = 2;
/*     */   public static final short SQL_BATCH_ROW_COUNT = 120;
/*     */   public static final int SQL_BRC_PROCEDURES = 1;
/*     */   public static final int SQL_BRC_EXPLICIT = 2;
/*     */   public static final int SQL_BRC_ROLLED_UP = 4;
/*     */   public static final short SQL_BATCH_SUPPORT = 121;
/*     */   public static final int SQL_BS_SELECT_EXPLICIT = 1;
/*     */   public static final int SQL_BS_ROW_COUNT_EXPLICIT = 2;
/*     */   public static final int SQL_BS_SELECT_PROC = 4;
/*     */   public static final int SQL_BS_ROW_COUNT_PROC = 8;
/*     */   public static final short SQL_PARAM_ARRAY_ROW_COUNTS = 153;
/*     */   public static final int SQL_PARC_BATCH = 1;
/*     */   public static final int SQL_PARC_NO_BATCH = 2;
/*     */   public static final short SQL_PARAM_ARRAY_SELECTS = 154;
/*     */   public static final int SQL_PAS_BATCH = 1;
/*     */   public static final int SQL_PAS_NO_BATCH = 2;
/*     */   public static final int SQL_PAS_NO_SELECT = 3;
/*     */   public static final short SQL_COLUMN_NAME = 1;
/*     */   public static final short SQL_COLUMN_TYPE = 2;
/*     */   public static final short SQL_COLUMN_LENGTH = 3;
/*     */   public static final short SQL_COLUMN_PRECISION = 4;
/*     */   public static final short SQL_COLUMN_SCALE = 5;
/*     */   public static final short SQL_COLUMN_DISPLAY_SIZE = 6;
/*     */   public static final short SQL_COLUMN_NULLABLE = 7;
/*     */   public static final short SQL_NULLABLE = 1;
/*     */   public static final short SQL_COLUMN_UNSIGNED = 8;
/*     */   public static final short SQL_COLUMN_MONEY = 9;
/*     */   public static final short SQL_COLUMN_UPDATABLE = 10;
/*     */   public static final short SQL_ATTR_READONLY = 0;
/*     */   public static final short SQL_ATTR_WRITE = 1;
/*     */   public static final short SQL_ATTR_READWRITE_UNKNOWN = 2;
/*     */   public static final short SQL_COLUMN_AUTO_INCREMENT = 11;
/*     */   public static final short SQL_COLUMN_CASE_SENSITIVE = 12;
/*     */   public static final short SQL_COLUMN_SEARCHABLE = 13;
/*     */   public static final short SQL_UNSEARCHABLE = 0;
/*     */   public static final short SQL_SEARCHABLE = 3;
/*     */   public static final short SQL_COLUMN_TYPE_NAME = 14;
/*     */   public static final short SQL_COLUMN_TABLE_NAME = 15;
/*     */   public static final short SQL_COLUMN_OWNER_NAME = 16;
/*     */   public static final short SQL_COLUMN_QUALIFIER_NAME = 17;
/*     */   public static final short SQL_COLUMN_LABEL = 18;
/*     */   public static final short SQL_QUERY_TIMEOUT = 0;
/*     */   public static final short SQL_MAX_ROWS = 1;
/*     */   public static final short SQL_NOSCAN = 2;
/*     */   public static final int SQL_NOSCAN_OFF = 0;
/*     */   public static final int SQL_NOSCAN_ON = 1;
/*     */   public static final short SQL_MAX_LENGTH = 3;
/*     */   public static final short SQL_CONCURRENCY = 7;
/*     */   public static final int SQL_CONCUR_READ_ONLY = 1;
/*     */   public static final int SQL_CONCUR_LOCK = 2;
/*     */   public static final int SQL_CONCUR_ROWVER = 3;
/*     */   public static final int SQL_CONCUR_VALUES = 4;
/*     */   public static final short SQL_GET_BOOKMARK = 13;
/*     */   public static final short SQL_ROW_NUMBER = 14;
/*     */   public static final short SQL_ATTR_CURSOR_TYPE = 6;
/*     */   public static final short SQL_CURSOR_FORWARD_ONLY = 0;
/*     */   public static final short SQL_CURSOR_KEYSET_DRIVEN = 1;
/*     */   public static final short SQL_CURSOR_DYNAMIC = 2;
/*     */   public static final short SQL_CURSOR_STATIC = 3;
/*     */   public static final short SQL_SCROLL_OPTIONS = 44;
/*     */   public static final int SQL_SO_FORWARD_ONLY = 1;
/*     */   public static final int SQL_SO_KEYSET_DRIVEN = 2;
/*     */   public static final int SQL_SO_DYNAMIC = 4;
/*     */   public static final int SQL_SO_MIXED = 8;
/*     */   public static final int SQL_SO_STATIC = 16;
/*     */   public static final short SQL_FORWARD_ONLY_CURSOR_ATTRIBUTES1 = 146;
/*     */   public static final short SQL_FORWARD_ONLY_CURSOR_ATTRIBUTES2 = 147;
/*     */   public static final short SQL_STATIC_CURSOR_ATTRIBUTES1 = 167;
/*     */   public static final short SQL_STATIC_CURSOR_ATTRIBUTES2 = 168;
/*     */   public static final short SQL_KEYSET_CURSOR_ATTRIBUTES1 = 150;
/*     */   public static final short SQL_KEYSET_CURSOR_ATTRIBUTES2 = 151;
/*     */   public static final short SQL_DYNAMIC_CURSOR_ATTRIBUTES1 = 144;
/*     */   public static final short SQL_DYNAMIC_CURSOR_ATTRIBUTES2 = 145;
/*     */   public static final int SQL_CA2_SENSITIVITY_ADDITIONS = 16;
/*     */   public static final int SQL_CA2_SENSITIVITY_DELETIONS = 32;
/*     */   public static final int SQL_CA2_SENSITIVITY_UPDATES = 64;
/*     */   public static final int SQL_PARAM_BIND_BY_COLUMN = 0;
/*     */   public static final int SQL_ROW_BIND_BY_COLUMN = 0;
/*     */   public static final int SQL_ATTR_QUERY_TIMEOUT = 0;
/*     */   public static final int SQL_ATTR_MAX_ROWS = 1;
/*     */   public static final int SQL_ATTR_NOSCAN = 2;
/*     */   public static final int SQL_ATTR_MAX_LENGTH = 3;
/*     */   public static final int SQL_ATTR_ASYNC_ENABLE = 4;
/*     */   public static final int SQL_ATTR_ROW_BIND_TYPE = 5;
/*     */   public static final int SQL_ATTR_CONCURRENCY = 7;
/*     */   public static final int SQL_ATTR_KEYSET_SIZE = 8;
/*     */   public static final int SQL_ATTR_SIMULATE_CURSOR = 10;
/*     */   public static final int SQL_ATTR_RETRIEVE_DATA = 11;
/*     */   public static final int SQL_ATTR_USE_BOOKMARKS = 12;
/*     */   public static final int SQL_ATTR_ROW_NUMBER = 14;
/*     */   public static final int SQL_ATTR_ENABLE_AUTO_IPD = 15;
/*     */   public static final int SQL_ATTR_FETCH_BOOKMARK_PTR = 16;
/*     */   public static final int SQL_ATTR_PARAM_BIND_OFFSET_PTR = 17;
/*     */   public static final int SQL_ATTR_PARAM_BIND_TYPE = 18;
/*     */   public static final int SQL_ATTR_PARAM_OPERATION_PTR = 19;
/*     */   public static final int SQL_ATTR_PARAM_STATUS_PTR = 20;
/*     */   public static final int SQL_ATTR_PARAMS_PROCESSED_PTR = 21;
/*     */   public static final int SQL_ATTR_PARAMSET_SIZE = 22;
/*     */   public static final int SQL_ATTR_ROW_BIND_OFFSET_PTR = 23;
/*     */   public static final int SQL_ATTR_ROW_OPERATION_PTR = 24;
/*     */   public static final int SQL_ATTR_ROW_STATUS_PTR = 25;
/*     */   public static final int SQL_ATTR_ROWS_FETCHED_PTR = 26;
/*     */   public static final int SQL_ATTR_ROW_ARRAY_SIZE = 27;
/*     */   public static final short SQL_FETCH_NEXT = 1;
/*     */   public static final short SQL_FETCH_FIRST = 2;
/*     */   public static final short SQL_FETCH_LAST = 3;
/*     */   public static final short SQL_FETCH_PRIOR = 4;
/*     */   public static final short SQL_FETCH_ABSOLUTE = 5;
/*     */   public static final short SQL_FETCH_RELATIVE = 6;
/*     */   public static final short SQL_BEST_ROWID = 1;
/*     */   public static final short SQL_ROWVER = 2;
/*     */   public static final short SQL_ALL_TYPES = 0;
/*     */   public static final short SQL_COMMIT = 0;
/*     */   public static final short SQL_ROLLBACK = 1;
/*     */   public static final int SQL_LOCK_NO_CHANGE = 0;
/*     */   public static final int SQL_LOCK_EXCLUSIVE = 1;
/*     */   public static final int SQL_LOCK_UNLOCK = 2;
/*     */   public static final int SQL_ENTIRE_ROWSET = 0;
/*     */   public static final int SQL_POSITION = 0;
/*     */   public static final int SQL_REFRESH = 1;
/*     */   public static final int SQL_UPDATE = 2;
/*     */   public static final int SQL_DELETE = 3;
/*     */   public static final int SQL_ADD = 4;
/*     */   public static final int SQL_ROW_SUCCESS = 0;
/*     */   public static final int SQL_ROW_DELETED = 1;
/*     */   public static final int SQL_ROW_UPDATED = 2;
/*     */   public static final int SQL_ROW_NOROW = 3;
/*     */   public static final int SQL_ROW_ADDED = 4;
/*     */   public static final int SQL_ROW_ERROR = 5;
/*     */   public static final int RESULTS_NOT_SET = 1;
/*     */   public static final int HAS_MORE_RESULTS = 2;
/*     */   public static final int NO_MORE_RESULTS = 3;
/*     */   public static final short SQL_LIC_FILE_NAME = 1041;
/*     */   public static final short SQL_LIC_FILE_PASSWORD = 1042;
/*     */ 
/*     */   public static int odbcTypeToJdbc(int paramInt)
/*     */   {
/* 590 */     int i = paramInt;
/*     */ 
/* 592 */     switch (paramInt) {
/*     */     case 9:
/* 594 */       i = 91;
/* 595 */       break;
/*     */     case 10:
/* 597 */       i = 92;
/* 598 */       break;
/*     */     case 11:
/* 600 */       i = 93;
/* 601 */       break;
/*     */     default:
/* 603 */       if ((paramInt >= 0) && (paramInt <= 8))
/* 604 */         i = paramInt;
/* 605 */       else if (paramInt == 12)
/* 606 */         i = paramInt;
/* 607 */       else if ((paramInt <= -1) && (paramInt >= -10))
/* 608 */         i = paramInt;
/*     */       else
/* 610 */         i = 1111;
/*     */       break;
/*     */     }
/* 613 */     return i;
/*     */   }
/*     */ 
/*     */   public static int jdbcTypeToOdbc(int paramInt)
/*     */   {
/* 626 */     int i = paramInt;
/*     */ 
/* 628 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/*     */     case 12:
/* 633 */       break;
/*     */     case 91:
/* 635 */       i = 9;
/* 636 */       break;
/*     */     case 92:
/* 638 */       i = 10;
/* 639 */       break;
/*     */     case 93:
/* 641 */       i = 11;
/*     */     }
/*     */ 
/* 645 */     return i;
/*     */   }
/*     */ 
/*     */   public static int jdbcTypeToCType(int paramInt)
/*     */   {
/* 652 */     switch (paramInt)
/*     */     {
/*     */     case 4:
/* 655 */       return -16;
/*     */     case 5:
/* 658 */       return -16;
/*     */     case -6:
/* 661 */       return -16;
/*     */     case -7:
/* 664 */       return -16;
/*     */     case -5:
/* 667 */       return -25;
/*     */     case 7:
/* 670 */       return 7;
/*     */     case 6:
/* 673 */       return 7;
/*     */     case 8:
/* 676 */       return 8;
/*     */     case 1:
/*     */     case 12:
/* 680 */       return 1;
/*     */     case -4:
/*     */     case -3:
/*     */     case -2:
/*     */     case -1:
/*     */     case 0:
/*     */     case 2:
/*     */     case 3:
/*     */     case 9:
/*     */     case 10:
/* 683 */     case 11: } return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.OdbcDef
 * JD-Core Version:    0.6.2
 */