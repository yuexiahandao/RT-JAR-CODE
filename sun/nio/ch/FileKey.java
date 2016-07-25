/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.io.FileDescriptor;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class FileKey
/*    */ {
/*    */   private long dwVolumeSerialNumber;
/*    */   private long nFileIndexHigh;
/*    */   private long nFileIndexLow;
/*    */ 
/*    */   public static FileKey create(FileDescriptor paramFileDescriptor)
/*    */   {
/* 43 */     FileKey localFileKey = new FileKey();
/*    */     try {
/* 45 */       localFileKey.init(paramFileDescriptor);
/*    */     } catch (IOException localIOException) {
/* 47 */       throw new Error(localIOException);
/*    */     }
/* 49 */     return localFileKey;
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 53 */     return (int)(this.dwVolumeSerialNumber ^ this.dwVolumeSerialNumber >>> 32) + (int)(this.nFileIndexHigh ^ this.nFileIndexHigh >>> 32) + (int)(this.nFileIndexLow ^ this.nFileIndexHigh >>> 32);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 59 */     if (paramObject == this)
/* 60 */       return true;
/* 61 */     if (!(paramObject instanceof FileKey))
/* 62 */       return false;
/* 63 */     FileKey localFileKey = (FileKey)paramObject;
/* 64 */     if ((this.dwVolumeSerialNumber != localFileKey.dwVolumeSerialNumber) || (this.nFileIndexHigh != localFileKey.nFileIndexHigh) || (this.nFileIndexLow != localFileKey.nFileIndexLow))
/*    */     {
/* 67 */       return false;
/*    */     }
/* 69 */     return true;
/*    */   }
/*    */   private native void init(FileDescriptor paramFileDescriptor) throws IOException;
/*    */ 
/*    */   private static native void initIDs();
/*    */ 
/*    */   static {
/* 76 */     initIDs();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.FileKey
 * JD-Core Version:    0.6.2
 */