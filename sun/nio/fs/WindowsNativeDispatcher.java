/*      */ package sun.nio.fs;
/*      */ 
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ class WindowsNativeDispatcher
/*      */ {
/* 1094 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*      */ 
/*      */   static long CreateFile(String paramString, int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4)
/*      */     throws WindowsException
/*      */   {
/*   58 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*   60 */       return CreateFile0(localNativeBuffer.address(), paramInt1, paramInt2, paramLong, paramInt3, paramInt4);
/*      */     }
/*      */     finally
/*      */     {
/*   67 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   static long CreateFile(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     throws WindowsException
/*      */   {
/*   77 */     return CreateFile(paramString, paramInt1, paramInt2, 0L, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   private static native long CreateFile0(long paramLong1, int paramInt1, int paramInt2, long paramLong2, int paramInt3, int paramInt4)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void CloseHandle(long paramLong);
/*      */ 
/*      */   static void DeleteFile(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  101 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  103 */       DeleteFile0(localNativeBuffer.address());
/*      */     } finally {
/*  105 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void DeleteFile0(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static void CreateDirectory(String paramString, long paramLong)
/*      */     throws WindowsException
/*      */   {
/*  118 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  120 */       CreateDirectory0(localNativeBuffer.address(), paramLong);
/*      */     } finally {
/*  122 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void CreateDirectory0(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static void RemoveDirectory(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  134 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  136 */       RemoveDirectory0(localNativeBuffer.address());
/*      */     } finally {
/*  138 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void RemoveDirectory0(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void DeviceIoControlSetSparse(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void DeviceIoControlGetReparsePoint(long paramLong1, long paramLong2, int paramInt)
/*      */     throws WindowsException;
/*      */ 
/*      */   static FirstFile FindFirstFile(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  171 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  173 */       FirstFile localFirstFile1 = new FirstFile(null);
/*  174 */       FindFirstFile0(localNativeBuffer.address(), localFirstFile1);
/*  175 */       return localFirstFile1;
/*      */     } finally {
/*  177 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void FindFirstFile0(long paramLong, FirstFile paramFirstFile)
/*      */     throws WindowsException;
/*      */ 
/*      */   static long FindFirstFile(String paramString, long paramLong)
/*      */     throws WindowsException
/*      */   {
/*  200 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  202 */       return FindFirstFile1(localNativeBuffer.address(), paramLong);
/*      */     } finally {
/*  204 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native long FindFirstFile1(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native String FindNextFile(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static FirstStream FindFirstStream(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  230 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  232 */       FirstStream localFirstStream1 = new FirstStream(null);
/*  233 */       FindFirstStream0(localNativeBuffer.address(), localFirstStream1);
/*      */       FirstStream localFirstStream2;
/*  234 */       if (localFirstStream1.handle() == -1L)
/*  235 */         return null;
/*  236 */       return localFirstStream1;
/*      */     } finally {
/*  238 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void FindFirstStream0(long paramLong, FirstStream paramFirstStream)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native String FindNextStream(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void FindClose(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void GetFileInformationByHandle(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static void CopyFileEx(String paramString1, String paramString2, int paramInt, long paramLong)
/*      */     throws WindowsException
/*      */   {
/*  290 */     NativeBuffer localNativeBuffer1 = asNativeBuffer(paramString1);
/*  291 */     NativeBuffer localNativeBuffer2 = asNativeBuffer(paramString2);
/*      */     try {
/*  293 */       CopyFileEx0(localNativeBuffer1.address(), localNativeBuffer2.address(), paramInt, paramLong);
/*      */     }
/*      */     finally {
/*  296 */       localNativeBuffer2.release();
/*  297 */       localNativeBuffer1.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void CopyFileEx0(long paramLong1, long paramLong2, int paramInt, long paramLong3)
/*      */     throws WindowsException;
/*      */ 
/*      */   static void MoveFileEx(String paramString1, String paramString2, int paramInt)
/*      */     throws WindowsException
/*      */   {
/*  313 */     NativeBuffer localNativeBuffer1 = asNativeBuffer(paramString1);
/*  314 */     NativeBuffer localNativeBuffer2 = asNativeBuffer(paramString2);
/*      */     try {
/*  316 */       MoveFileEx0(localNativeBuffer1.address(), localNativeBuffer2.address(), paramInt);
/*      */     } finally {
/*  318 */       localNativeBuffer2.release();
/*  319 */       localNativeBuffer1.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void MoveFileEx0(long paramLong1, long paramLong2, int paramInt)
/*      */     throws WindowsException;
/*      */ 
/*      */   static int GetFileAttributes(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  331 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  333 */       return GetFileAttributes0(localNativeBuffer.address());
/*      */     } finally {
/*  335 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native int GetFileAttributes0(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static void SetFileAttributes(String paramString, int paramInt)
/*      */     throws WindowsException
/*      */   {
/*  349 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  351 */       SetFileAttributes0(localNativeBuffer.address(), paramInt);
/*      */     } finally {
/*  353 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void SetFileAttributes0(long paramLong, int paramInt)
/*      */     throws WindowsException;
/*      */ 
/*      */   static void GetFileAttributesEx(String paramString, long paramLong)
/*      */     throws WindowsException
/*      */   {
/*  367 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  369 */       GetFileAttributesEx0(localNativeBuffer.address(), paramLong);
/*      */     } finally {
/*  371 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void GetFileAttributesEx0(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void SetFileTime(long paramLong1, long paramLong2, long paramLong3, long paramLong4)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void SetEndOfFile(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native int GetLogicalDrives()
/*      */     throws WindowsException;
/*      */ 
/*      */   static VolumeInformation GetVolumeInformation(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  417 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  419 */       VolumeInformation localVolumeInformation1 = new VolumeInformation(null);
/*  420 */       GetVolumeInformation0(localNativeBuffer.address(), localVolumeInformation1);
/*  421 */       return localVolumeInformation1;
/*      */     } finally {
/*  423 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void GetVolumeInformation0(long paramLong, VolumeInformation paramVolumeInformation)
/*      */     throws WindowsException;
/*      */ 
/*      */   static int GetDriveType(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  448 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  450 */       return GetDriveType0(localNativeBuffer.address());
/*      */     } finally {
/*  452 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native int GetDriveType0(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static DiskFreeSpace GetDiskFreeSpaceEx(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  468 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  470 */       DiskFreeSpace localDiskFreeSpace1 = new DiskFreeSpace(null);
/*  471 */       GetDiskFreeSpaceEx0(localNativeBuffer.address(), localDiskFreeSpace1);
/*  472 */       return localDiskFreeSpace1;
/*      */     } finally {
/*  474 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void GetDiskFreeSpaceEx0(long paramLong, DiskFreeSpace paramDiskFreeSpace)
/*      */     throws WindowsException;
/*      */ 
/*      */   static String GetVolumePathName(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  502 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  504 */       return GetVolumePathName0(localNativeBuffer.address());
/*      */     } finally {
/*  506 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native String GetVolumePathName0(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void InitializeSecurityDescriptor(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void InitializeAcl(long paramLong, int paramInt)
/*      */     throws WindowsException;
/*      */ 
/*      */   static int GetFileSecurity(String paramString, int paramInt1, long paramLong, int paramInt2)
/*      */     throws WindowsException
/*      */   {
/*  546 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  548 */       return GetFileSecurity0(localNativeBuffer.address(), paramInt1, paramLong, paramInt2);
/*      */     }
/*      */     finally {
/*  551 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native int GetFileSecurity0(long paramLong1, int paramInt1, long paramLong2, int paramInt2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static void SetFileSecurity(String paramString, int paramInt, long paramLong)
/*      */     throws WindowsException
/*      */   {
/*  571 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  573 */       SetFileSecurity0(localNativeBuffer.address(), paramInt, paramLong);
/*      */     }
/*      */     finally {
/*  576 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   static native void SetFileSecurity0(long paramLong1, int paramInt, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native long GetSecurityDescriptorOwner(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void SetSecurityDescriptorOwner(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native long GetSecurityDescriptorDacl(long paramLong);
/*      */ 
/*      */   static native void SetSecurityDescriptorDacl(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static AclInformation GetAclInformation(long paramLong)
/*      */   {
/*  636 */     AclInformation localAclInformation = new AclInformation(null);
/*  637 */     GetAclInformation0(paramLong, localAclInformation);
/*  638 */     return localAclInformation;
/*      */   }
/*      */ 
/*      */   private static native void GetAclInformation0(long paramLong, AclInformation paramAclInformation);
/*      */ 
/*      */   static native long GetAce(long paramLong, int paramInt);
/*      */ 
/*      */   static native void AddAccessAllowedAceEx(long paramLong1, int paramInt1, int paramInt2, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void AddAccessDeniedAceEx(long paramLong1, int paramInt1, int paramInt2, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static Account LookupAccountSid(long paramLong)
/*      */     throws WindowsException
/*      */   {
/*  694 */     Account localAccount = new Account(null);
/*  695 */     LookupAccountSid0(paramLong, localAccount);
/*  696 */     return localAccount;
/*      */   }
/*      */ 
/*      */   private static native void LookupAccountSid0(long paramLong, Account paramAccount)
/*      */     throws WindowsException;
/*      */ 
/*      */   static int LookupAccountName(String paramString, long paramLong, int paramInt)
/*      */     throws WindowsException
/*      */   {
/*  728 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  730 */       return LookupAccountName0(localNativeBuffer.address(), paramLong, paramInt);
/*      */     } finally {
/*  732 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native int LookupAccountName0(long paramLong1, long paramLong2, int paramInt)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native int GetLengthSid(long paramLong);
/*      */ 
/*      */   static native String ConvertSidToStringSid(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static long ConvertStringSidToSid(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  767 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  769 */       return ConvertStringSidToSid0(localNativeBuffer.address());
/*      */     } finally {
/*  771 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native long ConvertStringSidToSid0(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native long GetCurrentProcess();
/*      */ 
/*      */   static native long GetCurrentThread();
/*      */ 
/*      */   static native long OpenProcessToken(long paramLong, int paramInt)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native long OpenThreadToken(long paramLong, int paramInt, boolean paramBoolean)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native long DuplicateTokenEx(long paramLong, int paramInt)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void SetThreadToken(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native int GetTokenInformation(long paramLong1, int paramInt1, long paramLong2, int paramInt2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void AdjustTokenPrivileges(long paramLong1, long paramLong2, int paramInt)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native boolean AccessCheck(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     throws WindowsException;
/*      */ 
/*      */   static long LookupPrivilegeValue(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  867 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  869 */       return LookupPrivilegeValue0(localNativeBuffer.address());
/*      */     } finally {
/*  871 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native long LookupPrivilegeValue0(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static void CreateSymbolicLink(String paramString1, String paramString2, int paramInt)
/*      */     throws WindowsException
/*      */   {
/*  887 */     NativeBuffer localNativeBuffer1 = asNativeBuffer(paramString1);
/*  888 */     NativeBuffer localNativeBuffer2 = asNativeBuffer(paramString2);
/*      */     try {
/*  890 */       CreateSymbolicLink0(localNativeBuffer1.address(), localNativeBuffer2.address(), paramInt);
/*      */     }
/*      */     finally {
/*  893 */       localNativeBuffer2.release();
/*  894 */       localNativeBuffer1.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void CreateSymbolicLink0(long paramLong1, long paramLong2, int paramInt)
/*      */     throws WindowsException;
/*      */ 
/*      */   static void CreateHardLink(String paramString1, String paramString2)
/*      */     throws WindowsException
/*      */   {
/*  910 */     NativeBuffer localNativeBuffer1 = asNativeBuffer(paramString1);
/*  911 */     NativeBuffer localNativeBuffer2 = asNativeBuffer(paramString2);
/*      */     try {
/*  913 */       CreateHardLink0(localNativeBuffer1.address(), localNativeBuffer2.address());
/*      */     } finally {
/*  915 */       localNativeBuffer2.release();
/*  916 */       localNativeBuffer1.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void CreateHardLink0(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static String GetFullPathName(String paramString)
/*      */     throws WindowsException
/*      */   {
/*  931 */     NativeBuffer localNativeBuffer = asNativeBuffer(paramString);
/*      */     try {
/*  933 */       return GetFullPathName0(localNativeBuffer.address());
/*      */     } finally {
/*  935 */       localNativeBuffer.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native String GetFullPathName0(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native String GetFinalPathNameByHandle(long paramLong)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native String FormatMessage(int paramInt);
/*      */ 
/*      */   static native void LocalFree(long paramLong);
/*      */ 
/*      */   static native long CreateIoCompletionPort(long paramLong1, long paramLong2, long paramLong3)
/*      */     throws WindowsException;
/*      */ 
/*      */   static CompletionStatus GetQueuedCompletionStatus(long paramLong)
/*      */     throws WindowsException
/*      */   {
/*  995 */     CompletionStatus localCompletionStatus = new CompletionStatus(null);
/*  996 */     GetQueuedCompletionStatus0(paramLong, localCompletionStatus);
/*  997 */     return localCompletionStatus;
/*      */   }
/*      */ 
/*      */   private static native void GetQueuedCompletionStatus0(long paramLong, CompletionStatus paramCompletionStatus)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void PostQueuedCompletionStatus(long paramLong1, long paramLong2)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void ReadDirectoryChangesW(long paramLong1, long paramLong2, int paramInt1, boolean paramBoolean, int paramInt2, long paramLong3, long paramLong4)
/*      */     throws WindowsException;
/*      */ 
/*      */   static BackupResult BackupRead(long paramLong1, long paramLong2, int paramInt, boolean paramBoolean, long paramLong3)
/*      */     throws WindowsException
/*      */   {
/* 1062 */     BackupResult localBackupResult = new BackupResult(null);
/* 1063 */     BackupRead0(paramLong1, paramLong2, paramInt, paramBoolean, paramLong3, localBackupResult);
/* 1064 */     return localBackupResult;
/*      */   }
/*      */ 
/*      */   private static native void BackupRead0(long paramLong1, long paramLong2, int paramInt, boolean paramBoolean, long paramLong3, BackupResult paramBackupResult)
/*      */     throws WindowsException;
/*      */ 
/*      */   static native void BackupSeek(long paramLong1, long paramLong2, long paramLong3)
/*      */     throws WindowsException;
/*      */ 
/*      */   static NativeBuffer asNativeBuffer(String paramString)
/*      */   {
/* 1097 */     int i = paramString.length() << 1;
/* 1098 */     int j = i + 2;
/*      */ 
/* 1101 */     NativeBuffer localNativeBuffer = NativeBuffers.getNativeBufferFromCache(j);
/* 1102 */     if (localNativeBuffer == null) {
/* 1103 */       localNativeBuffer = NativeBuffers.allocNativeBuffer(j);
/*      */     }
/* 1106 */     else if (localNativeBuffer.owner() == paramString) {
/* 1107 */       return localNativeBuffer;
/*      */     }
/*      */ 
/* 1111 */     char[] arrayOfChar = paramString.toCharArray();
/* 1112 */     unsafe.copyMemory(arrayOfChar, Unsafe.ARRAY_CHAR_BASE_OFFSET, null, localNativeBuffer.address(), i);
/*      */ 
/* 1114 */     unsafe.putChar(localNativeBuffer.address() + i, '\000');
/* 1115 */     localNativeBuffer.setOwner(paramString);
/* 1116 */     return localNativeBuffer;
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   static
/*      */   {
/* 1124 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Void run() {
/* 1127 */         System.loadLibrary("net");
/* 1128 */         System.loadLibrary("nio");
/* 1129 */         return null;
/*      */       }
/*      */     });
/* 1131 */     initIDs();
/*      */   }
/*      */ 
/*      */   static class Account
/*      */   {
/*      */     private String domain;
/*      */     private String name;
/*      */     private int use;
/*      */ 
/*      */     public String domain()
/*      */     {
/*  704 */       return this.domain; } 
/*  705 */     public String name() { return this.name; } 
/*  706 */     public int use() { return this.use; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class AclInformation
/*      */   {
/*      */     private int aceCount;
/*      */ 
/*      */     public int aceCount()
/*      */     {
/*  644 */       return this.aceCount;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BackupResult
/*      */   {
/*      */     private int bytesTransferred;
/*      */     private long context;
/*      */ 
/*      */     int bytesTransferred()
/*      */     {
/* 1071 */       return this.bytesTransferred; } 
/* 1072 */     long context() { return this.context; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class CompletionStatus
/*      */   {
/*      */     private int error;
/*      */     private int bytesTransferred;
/*      */     private long completionKey;
/*      */ 
/*      */     int error()
/*      */     {
/* 1005 */       return this.error; } 
/* 1006 */     int bytesTransferred() { return this.bytesTransferred; } 
/* 1007 */     long completionKey() { return this.completionKey; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class DiskFreeSpace
/*      */   {
/*      */     private long freeBytesAvailable;
/*      */     private long totalNumberOfBytes;
/*      */     private long totalNumberOfFreeBytes;
/*      */ 
/*      */     public long freeBytesAvailable()
/*      */     {
/*  483 */       return this.freeBytesAvailable; } 
/*  484 */     public long totalNumberOfBytes() { return this.totalNumberOfBytes; } 
/*  485 */     public long totalNumberOfFreeBytes() { return this.totalNumberOfFreeBytes; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class FirstFile
/*      */   {
/*      */     private long handle;
/*      */     private String name;
/*      */     private int attributes;
/*      */ 
/*      */     public long handle()
/*      */     {
/*  186 */       return this.handle; } 
/*  187 */     public String name() { return this.name; } 
/*  188 */     public int attributes() { return this.attributes; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class FirstStream
/*      */   {
/*      */     private long handle;
/*      */     private String name;
/*      */ 
/*      */     public long handle()
/*      */     {
/*  246 */       return this.handle; } 
/*  247 */     public String name() { return this.name; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class VolumeInformation
/*      */   {
/*      */     private String fileSystemName;
/*      */     private String volumeName;
/*      */     private int volumeSerialNumber;
/*      */     private int flags;
/*      */ 
/*      */     public String fileSystemName()
/*      */     {
/*  433 */       return this.fileSystemName; } 
/*  434 */     public String volumeName() { return this.volumeName; } 
/*  435 */     public int volumeSerialNumber() { return this.volumeSerialNumber; } 
/*  436 */     public int flags() { return this.flags; }
/*      */ 
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsNativeDispatcher
 * JD-Core Version:    0.6.2
 */