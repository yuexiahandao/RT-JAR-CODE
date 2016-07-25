/*     */ package sun.net.ftp;
/*     */ 
/*     */ public enum FtpReplyCode
/*     */ {
/*  35 */   RESTART_MARKER(110), 
/*  36 */   SERVICE_READY_IN(120), 
/*  37 */   DATA_CONNECTION_ALREADY_OPEN(125), 
/*  38 */   FILE_STATUS_OK(150), 
/*  39 */   COMMAND_OK(200), 
/*  40 */   NOT_IMPLEMENTED(202), 
/*  41 */   SYSTEM_STATUS(211), 
/*  42 */   DIRECTORY_STATUS(212), 
/*  43 */   FILE_STATUS(213), 
/*  44 */   HELP_MESSAGE(214), 
/*  45 */   NAME_SYSTEM_TYPE(215), 
/*  46 */   SERVICE_READY(220), 
/*  47 */   SERVICE_CLOSING(221), 
/*  48 */   DATA_CONNECTION_OPEN(225), 
/*  49 */   CLOSING_DATA_CONNECTION(226), 
/*  50 */   ENTERING_PASSIVE_MODE(227), 
/*  51 */   ENTERING_EXT_PASSIVE_MODE(229), 
/*  52 */   LOGGED_IN(230), 
/*  53 */   SECURELY_LOGGED_IN(232), 
/*  54 */   SECURITY_EXCHANGE_OK(234), 
/*  55 */   SECURITY_EXCHANGE_COMPLETE(235), 
/*  56 */   FILE_ACTION_OK(250), 
/*  57 */   PATHNAME_CREATED(257), 
/*  58 */   NEED_PASSWORD(331), 
/*  59 */   NEED_ACCOUNT(332), 
/*  60 */   NEED_ADAT(334), 
/*  61 */   NEED_MORE_ADAT(335), 
/*  62 */   FILE_ACTION_PENDING(350), 
/*  63 */   SERVICE_NOT_AVAILABLE(421), 
/*  64 */   CANT_OPEN_DATA_CONNECTION(425), 
/*  65 */   CONNECTION_CLOSED(426), 
/*  66 */   NEED_SECURITY_RESOURCE(431), 
/*  67 */   FILE_ACTION_NOT_TAKEN(450), 
/*  68 */   ACTION_ABORTED(451), 
/*  69 */   INSUFFICIENT_STORAGE(452), 
/*  70 */   COMMAND_UNRECOGNIZED(500), 
/*  71 */   INVALID_PARAMETER(501), 
/*  72 */   BAD_SEQUENCE(503), 
/*  73 */   NOT_IMPLEMENTED_FOR_PARAMETER(504), 
/*  74 */   NOT_LOGGED_IN(530), 
/*  75 */   NEED_ACCOUNT_FOR_STORING(532), 
/*  76 */   PROT_LEVEL_DENIED(533), 
/*  77 */   REQUEST_DENIED(534), 
/*  78 */   FAILED_SECURITY_CHECK(535), 
/*  79 */   UNSUPPORTED_PROT_LEVEL(536), 
/*  80 */   PROT_LEVEL_NOT_SUPPORTED_BY_SECURITY(537), 
/*  81 */   FILE_UNAVAILABLE(550), 
/*  82 */   PAGE_TYPE_UNKNOWN(551), 
/*  83 */   EXCEEDED_STORAGE(552), 
/*  84 */   FILE_NAME_NOT_ALLOWED(553), 
/*  85 */   PROTECTED_REPLY(631), 
/*  86 */   UNKNOWN_ERROR(999);
/*     */ 
/*     */   private final int value;
/*     */ 
/*  90 */   private FtpReplyCode(int paramInt) { this.value = paramInt; }
/*     */ 
/*     */ 
/*     */   public int getValue()
/*     */   {
/*  99 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean isPositivePreliminary()
/*     */   {
/* 110 */     return (this.value >= 100) && (this.value < 200);
/*     */   }
/*     */ 
/*     */   public boolean isPositiveCompletion()
/*     */   {
/* 121 */     return (this.value >= 200) && (this.value < 300);
/*     */   }
/*     */ 
/*     */   public boolean isPositiveIntermediate()
/*     */   {
/* 132 */     return (this.value >= 300) && (this.value < 400);
/*     */   }
/*     */ 
/*     */   public boolean isTransientNegative()
/*     */   {
/* 143 */     return (this.value >= 400) && (this.value < 500);
/*     */   }
/*     */ 
/*     */   public boolean isPermanentNegative()
/*     */   {
/* 154 */     return (this.value >= 500) && (this.value < 600);
/*     */   }
/*     */ 
/*     */   public boolean isProtectedReply()
/*     */   {
/* 165 */     return (this.value >= 600) && (this.value < 700);
/*     */   }
/*     */ 
/*     */   public boolean isSyntax()
/*     */   {
/* 176 */     return this.value / 10 - this.value / 100 * 10 == 0;
/*     */   }
/*     */ 
/*     */   public boolean isInformation()
/*     */   {
/* 187 */     return this.value / 10 - this.value / 100 * 10 == 1;
/*     */   }
/*     */ 
/*     */   public boolean isConnection()
/*     */   {
/* 198 */     return this.value / 10 - this.value / 100 * 10 == 2;
/*     */   }
/*     */ 
/*     */   public boolean isAuthentication()
/*     */   {
/* 209 */     return this.value / 10 - this.value / 100 * 10 == 3;
/*     */   }
/*     */ 
/*     */   public boolean isUnspecified()
/*     */   {
/* 220 */     return this.value / 10 - this.value / 100 * 10 == 4;
/*     */   }
/*     */ 
/*     */   public boolean isFileSystem()
/*     */   {
/* 231 */     return this.value / 10 - this.value / 100 * 10 == 5;
/*     */   }
/*     */ 
/*     */   public static FtpReplyCode find(int paramInt)
/*     */   {
/* 241 */     for (FtpReplyCode localFtpReplyCode : values()) {
/* 242 */       if (localFtpReplyCode.getValue() == paramInt) {
/* 243 */         return localFtpReplyCode;
/*     */       }
/*     */     }
/* 246 */     return UNKNOWN_ERROR;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ftp.FtpReplyCode
 * JD-Core Version:    0.6.2
 */