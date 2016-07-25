/*     */ package sun.net.httpserver;
/*     */ 
/*     */ class Code
/*     */ {
/*     */   public static final int HTTP_CONTINUE = 100;
/*     */   public static final int HTTP_OK = 200;
/*     */   public static final int HTTP_CREATED = 201;
/*     */   public static final int HTTP_ACCEPTED = 202;
/*     */   public static final int HTTP_NOT_AUTHORITATIVE = 203;
/*     */   public static final int HTTP_NO_CONTENT = 204;
/*     */   public static final int HTTP_RESET = 205;
/*     */   public static final int HTTP_PARTIAL = 206;
/*     */   public static final int HTTP_MULT_CHOICE = 300;
/*     */   public static final int HTTP_MOVED_PERM = 301;
/*     */   public static final int HTTP_MOVED_TEMP = 302;
/*     */   public static final int HTTP_SEE_OTHER = 303;
/*     */   public static final int HTTP_NOT_MODIFIED = 304;
/*     */   public static final int HTTP_USE_PROXY = 305;
/*     */   public static final int HTTP_BAD_REQUEST = 400;
/*     */   public static final int HTTP_UNAUTHORIZED = 401;
/*     */   public static final int HTTP_PAYMENT_REQUIRED = 402;
/*     */   public static final int HTTP_FORBIDDEN = 403;
/*     */   public static final int HTTP_NOT_FOUND = 404;
/*     */   public static final int HTTP_BAD_METHOD = 405;
/*     */   public static final int HTTP_NOT_ACCEPTABLE = 406;
/*     */   public static final int HTTP_PROXY_AUTH = 407;
/*     */   public static final int HTTP_CLIENT_TIMEOUT = 408;
/*     */   public static final int HTTP_CONFLICT = 409;
/*     */   public static final int HTTP_GONE = 410;
/*     */   public static final int HTTP_LENGTH_REQUIRED = 411;
/*     */   public static final int HTTP_PRECON_FAILED = 412;
/*     */   public static final int HTTP_ENTITY_TOO_LARGE = 413;
/*     */   public static final int HTTP_REQ_TOO_LONG = 414;
/*     */   public static final int HTTP_UNSUPPORTED_TYPE = 415;
/*     */   public static final int HTTP_INTERNAL_ERROR = 500;
/*     */   public static final int HTTP_NOT_IMPLEMENTED = 501;
/*     */   public static final int HTTP_BAD_GATEWAY = 502;
/*     */   public static final int HTTP_UNAVAILABLE = 503;
/*     */   public static final int HTTP_GATEWAY_TIMEOUT = 504;
/*     */   public static final int HTTP_VERSION = 505;
/*     */ 
/*     */   static String msg(int paramInt)
/*     */   {
/*  69 */     switch (paramInt) { case 200:
/*  70 */       return " OK";
/*     */     case 100:
/*  71 */       return " Continue";
/*     */     case 201:
/*  72 */       return " Created";
/*     */     case 202:
/*  73 */       return " Accepted";
/*     */     case 203:
/*  74 */       return " Non-Authoritative Information";
/*     */     case 204:
/*  75 */       return " No Content";
/*     */     case 205:
/*  76 */       return " Reset Content";
/*     */     case 206:
/*  77 */       return " Partial Content";
/*     */     case 300:
/*  78 */       return " Multiple Choices";
/*     */     case 301:
/*  79 */       return " Moved Permanently";
/*     */     case 302:
/*  80 */       return " Temporary Redirect";
/*     */     case 303:
/*  81 */       return " See Other";
/*     */     case 304:
/*  82 */       return " Not Modified";
/*     */     case 305:
/*  83 */       return " Use Proxy";
/*     */     case 400:
/*  84 */       return " Bad Request";
/*     */     case 401:
/*  85 */       return " Unauthorized";
/*     */     case 402:
/*  86 */       return " Payment Required";
/*     */     case 403:
/*  87 */       return " Forbidden";
/*     */     case 404:
/*  88 */       return " Not Found";
/*     */     case 405:
/*  89 */       return " Method Not Allowed";
/*     */     case 406:
/*  90 */       return " Not Acceptable";
/*     */     case 407:
/*  91 */       return " Proxy Authentication Required";
/*     */     case 408:
/*  92 */       return " Request Time-Out";
/*     */     case 409:
/*  93 */       return " Conflict";
/*     */     case 410:
/*  94 */       return " Gone";
/*     */     case 411:
/*  95 */       return " Length Required";
/*     */     case 412:
/*  96 */       return " Precondition Failed";
/*     */     case 413:
/*  97 */       return " Request Entity Too Large";
/*     */     case 414:
/*  98 */       return " Request-URI Too Large";
/*     */     case 415:
/*  99 */       return " Unsupported Media Type";
/*     */     case 500:
/* 100 */       return " Internal Server Error";
/*     */     case 501:
/* 101 */       return " Not Implemented";
/*     */     case 502:
/* 102 */       return " Bad Gateway";
/*     */     case 503:
/* 103 */       return " Service Unavailable";
/*     */     case 504:
/* 104 */       return " Gateway Timeout";
/*     */     case 505:
/* 105 */       return " HTTP Version Not Supported"; }
/* 106 */     return "";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.Code
 * JD-Core Version:    0.6.2
 */