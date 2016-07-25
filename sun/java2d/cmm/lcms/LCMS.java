/*     */ package sun.java2d.cmm.lcms;
/*     */ 
/*     */ import java.awt.color.ICC_Profile;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.java2d.cmm.ColorTransform;
/*     */ import sun.java2d.cmm.PCMM;
/*     */ 
/*     */ public class LCMS
/*     */   implements PCMM
/*     */ {
/*     */   public native long loadProfile(byte[] paramArrayOfByte);
/*     */ 
/*     */   public native void freeProfile(long paramLong);
/*     */ 
/*     */   public synchronized native int getProfileSize(long paramLong);
/*     */ 
/*     */   public synchronized native void getProfileData(long paramLong, byte[] paramArrayOfByte);
/*     */ 
/*     */   public synchronized native int getTagSize(long paramLong, int paramInt);
/*     */ 
/*     */   public synchronized native void getTagData(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*     */ 
/*     */   public synchronized native void setTagData(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*     */ 
/*     */   public static native long getProfileID(ICC_Profile paramICC_Profile);
/*     */ 
/*     */   public static native long createNativeTransform(long[] paramArrayOfLong, int paramInt1, int paramInt2, int paramInt3, Object paramObject);
/*     */ 
/*     */   public ColorTransform createTransform(ICC_Profile paramICC_Profile, int paramInt1, int paramInt2)
/*     */   {
/*  66 */     return new LCMSTransform(paramICC_Profile, paramInt1, paramInt1);
/*     */   }
/*     */ 
/*     */   public synchronized ColorTransform createTransform(ColorTransform[] paramArrayOfColorTransform)
/*     */   {
/*  76 */     return new LCMSTransform(paramArrayOfColorTransform);
/*     */   }
/*     */ 
/*     */   public static native void colorConvert(LCMSTransform paramLCMSTransform, LCMSImageLayout paramLCMSImageLayout1, LCMSImageLayout paramLCMSImageLayout2);
/*     */ 
/*     */   public static native void freeTransform(long paramLong);
/*     */ 
/*     */   public static native void initLCMS(Class paramClass1, Class paramClass2, Class paramClass3);
/*     */ 
/*     */   static
/*     */   {
/*  89 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*  95 */         System.loadLibrary("awt");
/*  96 */         System.loadLibrary("lcms");
/*  97 */         return null;
/*     */       }
/*     */     });
/* 102 */     initLCMS(LCMSTransform.class, LCMSImageLayout.class, ICC_Profile.class);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.lcms.LCMS
 * JD-Core Version:    0.6.2
 */