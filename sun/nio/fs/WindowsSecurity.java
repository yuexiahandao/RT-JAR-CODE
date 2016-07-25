/*     */ package sun.nio.fs;
/*     */ 
/*     */ class WindowsSecurity
/*     */ {
/*  50 */   static final long processTokenWithDuplicateAccess = openProcessToken(2);
/*     */ 
/*  56 */   static final long processTokenWithQueryAccess = openProcessToken(8);
/*     */ 
/*     */   private static long openProcessToken(int paramInt)
/*     */   {
/*     */     try
/*     */     {
/*  41 */       return WindowsNativeDispatcher.OpenProcessToken(WindowsNativeDispatcher.GetCurrentProcess(), paramInt); } catch (WindowsException localWindowsException) {
/*     */     }
/*  43 */     return 0L;
/*     */   }
/*     */ 
/*     */   static Privilege enablePrivilege(String paramString)
/*     */   {
/*     */     final long l1;
/*     */     try
/*     */     {
/*  74 */       l1 = WindowsNativeDispatcher.LookupPrivilegeValue(paramString);
/*     */     }
/*     */     catch (WindowsException localWindowsException1) {
/*  77 */       throw new AssertionError(localWindowsException1);
/*     */     }
/*     */ 
/*  80 */     long l2 = 0L;
/*  81 */     boolean bool1 = false;
/*  82 */     boolean bool2 = false;
/*     */     try {
/*  84 */       l2 = WindowsNativeDispatcher.OpenThreadToken(WindowsNativeDispatcher.GetCurrentThread(), 32, false);
/*     */ 
/*  86 */       if ((l2 == 0L) && (processTokenWithDuplicateAccess != 0L)) {
/*  87 */         l2 = WindowsNativeDispatcher.DuplicateTokenEx(processTokenWithDuplicateAccess, 36);
/*     */ 
/*  89 */         WindowsNativeDispatcher.SetThreadToken(0L, l2);
/*  90 */         bool1 = true;
/*     */       }
/*     */ 
/*  93 */       if (l2 != 0L) {
/*  94 */         WindowsNativeDispatcher.AdjustTokenPrivileges(l2, l1, 2);
/*  95 */         bool2 = true;
/*     */       }
/*     */     }
/*     */     catch (WindowsException localWindowsException2)
/*     */     {
/*     */     }
/* 101 */     long l3 = l2;
/* 102 */     boolean bool3 = bool1;
/* 103 */     final boolean bool4 = bool2;
/*     */ 
/* 105 */     return new Privilege()
/*     */     {
/*     */       public void drop() {
/* 108 */         if (this.val$token != 0L)
/*     */           try {
/* 110 */             if (bool4)
/* 111 */               WindowsNativeDispatcher.SetThreadToken(0L, 0L);
/* 112 */             else if (l1)
/* 113 */               WindowsNativeDispatcher.AdjustTokenPrivileges(this.val$token, this.val$pLuid, 0);
/*     */           }
/*     */           catch (WindowsException localWindowsException) {
/* 116 */             throw new AssertionError(localWindowsException);
/*     */           } finally {
/* 118 */             WindowsNativeDispatcher.CloseHandle(this.val$token);
/*     */           }
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   static boolean checkAccessMask(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */     throws WindowsException
/*     */   {
/* 132 */     int i = 8;
/* 133 */     long l = WindowsNativeDispatcher.OpenThreadToken(WindowsNativeDispatcher.GetCurrentThread(), i, false);
/* 134 */     if ((l == 0L) && (processTokenWithDuplicateAccess != 0L)) {
/* 135 */       l = WindowsNativeDispatcher.DuplicateTokenEx(processTokenWithDuplicateAccess, i);
/*     */     }
/*     */ 
/* 138 */     boolean bool = false;
/* 139 */     if (l != 0L) {
/*     */       try {
/* 141 */         bool = WindowsNativeDispatcher.AccessCheck(l, paramLong, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */       }
/*     */       finally {
/* 144 */         WindowsNativeDispatcher.CloseHandle(l);
/*     */       }
/*     */     }
/* 147 */     return bool;
/*     */   }
/*     */ 
/*     */   static abstract interface Privilege
/*     */   {
/*     */     public abstract void drop();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsSecurity
 * JD-Core Version:    0.6.2
 */