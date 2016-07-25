/*     */ package sun.java2d.cmm.kcms;
/*     */ 
/*     */ import java.awt.color.CMMException;
/*     */ import java.awt.color.ICC_Profile;
/*     */ import java.awt.color.ProfileDataException;
/*     */ import java.security.AccessController;
/*     */ import sun.java2d.cmm.ColorTransform;
/*     */ import sun.java2d.cmm.PCMM;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class CMM
/*     */   implements PCMM
/*     */ {
/*  43 */   private static long ID = 0L;
/*     */   static final int cmmStatSuccess = 0;
/*     */   static final int cmmStatBadProfile = 503;
/*     */   static final int cmmStatBadTagData = 504;
/*     */   static final int cmmStatBadTagType = 505;
/*     */   static final int cmmStatBadTagId = 506;
/*     */   static final int cmmStatBadXform = 507;
/*     */   static final int cmmStatXformNotActive = 508;
/*     */   static final int cmmStatOutOfRange = 518;
/*     */   static final int cmmStatTagNotFound = 519;
/*     */ 
/*     */   static native int cmmLoadProfile(byte[] paramArrayOfByte, long[] paramArrayOfLong);
/*     */ 
/*     */   static native int cmmFreeProfile(long paramLong);
/*     */ 
/*     */   static native int cmmGetProfileSize(long paramLong, int[] paramArrayOfInt);
/*     */ 
/*     */   static native int cmmGetProfileData(long paramLong, byte[] paramArrayOfByte);
/*     */ 
/*     */   static native int cmmGetTagSize(long paramLong, int paramInt, int[] paramArrayOfInt);
/*     */ 
/*     */   static native int cmmGetTagData(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*     */ 
/*     */   static native int cmmSetTagData(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*     */ 
/*     */   static native int cmmGetTransform(ICC_Profile paramICC_Profile, int paramInt1, int paramInt2, ICC_Transform paramICC_Transform);
/*     */ 
/*     */   static native int cmmCombineTransforms(ICC_Transform[] paramArrayOfICC_Transform, ICC_Transform paramICC_Transform);
/*     */ 
/*     */   static native int cmmFreeTransform(long paramLong);
/*     */ 
/*     */   static native int cmmGetNumComponents(long paramLong, int[] paramArrayOfInt);
/*     */ 
/*     */   static native int cmmColorConvert(long paramLong, CMMImageLayout paramCMMImageLayout1, CMMImageLayout paramCMMImageLayout2);
/*     */ 
/*     */   public long loadProfile(byte[] paramArrayOfByte)
/*     */   {
/*  86 */     long[] arrayOfLong = new long[1];
/*  87 */     checkStatus(cmmLoadProfile(paramArrayOfByte, arrayOfLong));
/*  88 */     return arrayOfLong[0];
/*     */   }
/*     */ 
/*     */   public void freeProfile(long paramLong) {
/*  92 */     checkStatus(cmmFreeProfile(paramLong));
/*     */   }
/*     */ 
/*     */   public int getProfileSize(long paramLong) {
/*  96 */     int[] arrayOfInt = new int[1];
/*  97 */     checkStatus(cmmGetProfileSize(paramLong, arrayOfInt));
/*  98 */     return arrayOfInt[0];
/*     */   }
/*     */ 
/*     */   public void getProfileData(long paramLong, byte[] paramArrayOfByte) {
/* 102 */     checkStatus(cmmGetProfileData(paramLong, paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public int getTagSize(long paramLong, int paramInt) {
/* 106 */     int[] arrayOfInt = new int[1];
/* 107 */     checkStatus(cmmGetTagSize(paramLong, paramInt, arrayOfInt));
/* 108 */     return arrayOfInt[0];
/*     */   }
/*     */ 
/*     */   public void getTagData(long paramLong, int paramInt, byte[] paramArrayOfByte) {
/* 112 */     checkStatus(cmmGetTagData(paramLong, paramInt, paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public void setTagData(long paramLong, int paramInt, byte[] paramArrayOfByte) {
/* 116 */     int i = cmmSetTagData(paramLong, paramInt, paramArrayOfByte);
/*     */ 
/* 118 */     switch (i) {
/*     */     case 504:
/*     */     case 505:
/*     */     case 519:
/* 122 */       throw new IllegalArgumentException("Can not write tag data.");
/*     */     }
/* 124 */     checkStatus(i);
/*     */   }
/*     */ 
/*     */   public ColorTransform createTransform(ICC_Profile paramICC_Profile, int paramInt1, int paramInt2)
/*     */   {
/* 133 */     ICC_Transform localICC_Transform = new ICC_Transform();
/* 134 */     checkStatus(cmmGetTransform(paramICC_Profile, paramInt1, paramInt2, localICC_Transform));
/* 135 */     return localICC_Transform;
/*     */   }
/*     */ 
/*     */   public ColorTransform createTransform(ColorTransform[] paramArrayOfColorTransform)
/*     */   {
/* 142 */     ICC_Transform localICC_Transform = new ICC_Transform();
/* 143 */     ICC_Transform[] arrayOfICC_Transform = new ICC_Transform[paramArrayOfColorTransform.length];
/* 144 */     for (int i = 0; i < paramArrayOfColorTransform.length; i++) {
/* 145 */       arrayOfICC_Transform[i] = ((ICC_Transform)paramArrayOfColorTransform[i]);
/*     */     }
/* 147 */     i = cmmCombineTransforms(arrayOfICC_Transform, localICC_Transform);
/* 148 */     if ((i != 0) || (localICC_Transform.getID() == 0L)) {
/* 149 */       throw new ProfileDataException("Invalid profile sequence");
/*     */     }
/* 151 */     return localICC_Transform;
/*     */   }
/*     */ 
/*     */   static native int cmmInit();
/*     */ 
/*     */   static native int cmmTerminate();
/*     */ 
/*     */   protected void finalize()
/*     */   {
/* 174 */     checkStatus(cmmTerminate());
/*     */   }
/*     */ 
/*     */   public static void checkStatus(int paramInt)
/*     */   {
/* 179 */     if (paramInt != 0)
/* 180 */       throw new CMMException(errorString(paramInt));
/*     */   }
/*     */ 
/*     */   static String errorString(int paramInt)
/*     */   {
/* 187 */     switch (paramInt) {
/*     */     case 0:
/* 189 */       return "Success";
/*     */     case 519:
/* 192 */       return "No such tag";
/*     */     case 503:
/* 195 */       return "Invalid profile data";
/*     */     case 504:
/* 198 */       return "Invalid tag data";
/*     */     case 505:
/* 201 */       return "Invalid tag type";
/*     */     case 506:
/* 204 */       return "Invalid tag signature";
/*     */     case 507:
/* 207 */       return "Invlaid transform";
/*     */     case 508:
/* 210 */       return "Transform is not active";
/*     */     case 518:
/* 213 */       return "Invalid image format";
/*     */     }
/*     */ 
/* 216 */     return "General CMM error" + paramInt;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 163 */     AccessController.doPrivileged(new LoadLibraryAction("kcms"));
/*     */ 
/* 165 */     int i = cmmInit();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.kcms.CMM
 * JD-Core Version:    0.6.2
 */