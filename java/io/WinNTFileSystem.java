/*    */ package java.io;
/*    */ 
/*    */ class WinNTFileSystem extends Win32FileSystem
/*    */ {
/*    */   protected native String canonicalize0(String paramString)
/*    */     throws IOException;
/*    */ 
/*    */   protected native String canonicalizeWithPrefix0(String paramString1, String paramString2)
/*    */     throws IOException;
/*    */ 
/*    */   public native int getBooleanAttributes(File paramFile);
/*    */ 
/*    */   public native boolean checkAccess(File paramFile, int paramInt);
/*    */ 
/*    */   public native long getLastModifiedTime(File paramFile);
/*    */ 
/*    */   public native long getLength(File paramFile);
/*    */ 
/*    */   public native boolean setPermission(File paramFile, int paramInt, boolean paramBoolean1, boolean paramBoolean2);
/*    */ 
/*    */   public long getSpace(File paramFile, int paramInt)
/*    */   {
/* 55 */     if (paramFile.exists()) {
/* 56 */       return getSpace0(paramFile, paramInt);
/*    */     }
/* 58 */     return 0L; } 
/*    */   private native long getSpace0(File paramFile, int paramInt);
/*    */ 
/*    */   public native boolean createFileExclusively(String paramString) throws IOException;
/*    */ 
/*    */   protected native boolean delete0(File paramFile);
/*    */ 
/*    */   public native String[] list(File paramFile);
/*    */ 
/*    */   public native boolean createDirectory(File paramFile);
/*    */ 
/*    */   protected native boolean rename0(File paramFile1, File paramFile2);
/*    */ 
/*    */   public native boolean setLastModifiedTime(File paramFile, long paramLong);
/*    */ 
/*    */   public native boolean setReadOnly(File paramFile);
/*    */ 
/*    */   protected native String getDriveDirectory(int paramInt);
/*    */ 
/*    */   private static native void initIDs();
/*    */ 
/* 77 */   static { initIDs(); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.WinNTFileSystem
 * JD-Core Version:    0.6.2
 */