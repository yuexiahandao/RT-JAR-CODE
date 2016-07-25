/*    */ package sun.security.smartcardio;
/*    */ 
/*    */ final class PCSCException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 4181137171979130432L;
/*    */   final int code;
/*    */ 
/*    */   PCSCException(int paramInt)
/*    */   {
/* 45 */     super(toErrorString(paramInt));
/* 46 */     this.code = paramInt;
/*    */   }
/*    */ 
/*    */   private static String toErrorString(int paramInt) {
/* 50 */     switch (paramInt) { case 0:
/* 51 */       return "SCARD_S_SUCCESS";
/*    */     case -2146435070:
/* 52 */       return "SCARD_E_CANCELLED";
/*    */     case -2146435058:
/* 53 */       return "SCARD_E_CANT_DISPOSE";
/*    */     case -2146435064:
/* 54 */       return "SCARD_E_INSUFFICIENT_BUFFER";
/*    */     case -2146435051:
/* 55 */       return "SCARD_E_INVALID_ATR";
/*    */     case -2146435069:
/* 56 */       return "SCARD_E_INVALID_HANDLE";
/*    */     case -2146435068:
/* 57 */       return "SCARD_E_INVALID_PARAMETER";
/*    */     case -2146435067:
/* 58 */       return "SCARD_E_INVALID_TARGET";
/*    */     case -2146435055:
/* 59 */       return "SCARD_E_INVALID_VALUE";
/*    */     case -2146435066:
/* 60 */       return "SCARD_E_NO_MEMORY";
/*    */     case -2146435053:
/* 61 */       return "SCARD_F_COMM_ERROR";
/*    */     case -2146435071:
/* 62 */       return "SCARD_F_INTERNAL_ERROR";
/*    */     case -2146435052:
/* 63 */       return "SCARD_F_UNKNOWN_ERROR";
/*    */     case -2146435065:
/* 64 */       return "SCARD_F_WAITED_TOO_LONG";
/*    */     case -2146435063:
/* 65 */       return "SCARD_E_UNKNOWN_READER";
/*    */     case -2146435062:
/* 66 */       return "SCARD_E_TIMEOUT";
/*    */     case -2146435061:
/* 67 */       return "SCARD_E_SHARING_VIOLATION";
/*    */     case -2146435060:
/* 68 */       return "SCARD_E_NO_SMARTCARD";
/*    */     case -2146435059:
/* 69 */       return "SCARD_E_UNKNOWN_CARD";
/*    */     case -2146435057:
/* 70 */       return "SCARD_E_PROTO_MISMATCH";
/*    */     case -2146435056:
/* 71 */       return "SCARD_E_NOT_READY";
/*    */     case -2146435054:
/* 72 */       return "SCARD_E_SYSTEM_CANCELLED";
/*    */     case -2146435050:
/* 73 */       return "SCARD_E_NOT_TRANSACTED";
/*    */     case -2146435049:
/* 74 */       return "SCARD_E_READER_UNAVAILABLE";
/*    */     case -2146434971:
/* 76 */       return "SCARD_W_UNSUPPORTED_CARD";
/*    */     case -2146434970:
/* 77 */       return "SCARD_W_UNRESPONSIVE_CARD";
/*    */     case -2146434969:
/* 78 */       return "SCARD_W_UNPOWERED_CARD";
/*    */     case -2146434968:
/* 79 */       return "SCARD_W_RESET_CARD";
/*    */     case -2146434967:
/* 80 */       return "SCARD_W_REMOVED_CARD";
/*    */     case -2146434966:
/* 81 */       return "SCARD_W_INSERTED_CARD";
/*    */     case -2146435041:
/* 83 */       return "SCARD_E_UNSUPPORTED_FEATURE";
/*    */     case -2146435047:
/* 84 */       return "SCARD_E_PCI_TOO_SMALL";
/*    */     case -2146435046:
/* 85 */       return "SCARD_E_READER_UNSUPPORTED";
/*    */     case -2146435045:
/* 86 */       return "SCARD_E_DUPLICATE_READER";
/*    */     case -2146435044:
/* 87 */       return "SCARD_E_CARD_UNSUPPORTED";
/*    */     case -2146435043:
/* 88 */       return "SCARD_E_NO_SERVICE";
/*    */     case -2146435042:
/* 89 */       return "SCARD_E_SERVICE_STOPPED";
/*    */     case -2146435026:
/* 91 */       return "SCARD_E_NO_READERS_AVAILABLE";
/*    */     case 6:
/* 92 */       return "WINDOWS_ERROR_INVALID_HANDLE";
/*    */     case 87:
/* 93 */       return "WINDOWS_ERROR_INVALID_PARAMETER";
/*    */     }
/* 95 */     return "Unknown error 0x" + Integer.toHexString(paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.smartcardio.PCSCException
 * JD-Core Version:    0.6.2
 */