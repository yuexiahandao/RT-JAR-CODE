/*    */ package sun.net.sdp;
/*    */ 
/*    */ import java.io.FileDescriptor;
/*    */ import java.io.IOException;
/*    */ import java.security.AccessController;
/*    */ import sun.misc.JavaIOFileDescriptorAccess;
/*    */ import sun.misc.SharedSecrets;
/*    */ import sun.security.action.GetPropertyAction;
/*    */ import sun.security.action.LoadLibraryAction;
/*    */ 
/*    */ public final class SdpSupport
/*    */ {
/* 42 */   private static final String os = (String)AccessController.doPrivileged(new GetPropertyAction("os.name"));
/*    */ 
/* 44 */   private static final boolean isSupported = (os.equals("SunOS")) || (os.equals("Linux"));
/* 45 */   private static final JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
/*    */ 
/*    */   public static FileDescriptor createSocket()
/*    */     throws IOException
/*    */   {
/* 54 */     if (!isSupported)
/* 55 */       throw new UnsupportedOperationException("SDP not supported on this platform");
/* 56 */     int i = create0();
/* 57 */     FileDescriptor localFileDescriptor = new FileDescriptor();
/* 58 */     fdAccess.set(localFileDescriptor, i);
/* 59 */     return localFileDescriptor;
/*    */   }
/*    */ 
/*    */   public static void convertSocket(FileDescriptor paramFileDescriptor)
/*    */     throws IOException
/*    */   {
/* 67 */     if (!isSupported)
/* 68 */       throw new UnsupportedOperationException("SDP not supported on this platform");
/* 69 */     int i = fdAccess.get(paramFileDescriptor);
/* 70 */     convert0(i);
/*    */   }
/*    */ 
/*    */   private static native int create0() throws IOException;
/*    */ 
/*    */   private static native void convert0(int paramInt) throws IOException;
/*    */ 
/*    */   static {
/* 78 */     AccessController.doPrivileged(new LoadLibraryAction("net"));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.sdp.SdpSupport
 * JD-Core Version:    0.6.2
 */