/*    */ package sun.security.provider;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ class NativeSeedGenerator extends SeedGenerator
/*    */ {
/*    */   NativeSeedGenerator()
/*    */     throws IOException
/*    */   {
/* 45 */     if (!nativeGenerateSeed(new byte[2]))
/* 46 */       throw new IOException("Required native CryptoAPI features not  available on this machine");
/*    */   }
/*    */ 
/*    */   private static native boolean nativeGenerateSeed(byte[] paramArrayOfByte);
/*    */ 
/*    */   void getSeedBytes(byte[] paramArrayOfByte)
/*    */   {
/* 59 */     if (!nativeGenerateSeed(paramArrayOfByte))
/*    */     {
/* 61 */       throw new InternalError("Unexpected CryptoAPI failure generating seed");
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.NativeSeedGenerator
 * JD-Core Version:    0.6.2
 */