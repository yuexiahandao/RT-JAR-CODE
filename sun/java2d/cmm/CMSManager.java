/*     */ package sun.java2d.cmm;
/*     */ 
/*     */ import java.awt.color.CMMException;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.color.ICC_Profile;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ServiceLoader;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class CMSManager
/*     */ {
/*     */   public static ColorSpace GRAYspace;
/*     */   public static ColorSpace LINEAR_RGBspace;
/*  48 */   private static PCMM cmmImpl = null;
/*     */ 
/*     */   public static synchronized PCMM getModule() {
/*  51 */     if (cmmImpl != null) {
/*  52 */       return cmmImpl;
/*     */     }
/*     */ 
/*  55 */     cmmImpl = (PCMM)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/*  57 */         String str = System.getProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.CMM");
/*     */ 
/*  60 */         ServiceLoader localServiceLoader = ServiceLoader.loadInstalled(PCMM.class);
/*     */ 
/*  63 */         Object localObject = null;
/*     */ 
/*  65 */         for (PCMM localPCMM : localServiceLoader) {
/*  66 */           localObject = localPCMM;
/*  67 */           if (localPCMM.getClass().getName().equals(str)) {
/*     */             break;
/*     */           }
/*     */         }
/*  71 */         return localObject;
/*     */       }
/*     */     });
/*  75 */     if (cmmImpl == null) {
/*  76 */       throw new CMMException("Cannot initialize Color Management System.No CM module found");
/*     */     }
/*     */ 
/*  80 */     GetPropertyAction localGetPropertyAction = new GetPropertyAction("sun.java2d.cmm.trace");
/*  81 */     String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  82 */     if (str != null) {
/*  83 */       cmmImpl = new CMMTracer(cmmImpl);
/*     */     }
/*     */ 
/*  86 */     return cmmImpl;
/*     */   }
/*     */ 
/*     */   public static class CMMTracer implements PCMM
/*     */   {
/*     */     PCMM tcmm;
/*     */     String cName;
/*     */ 
/*     */     public CMMTracer(PCMM paramPCMM) {
/*  96 */       this.tcmm = paramPCMM;
/*  97 */       this.cName = paramPCMM.getClass().getName();
/*     */     }
/*     */ 
/*     */     public long loadProfile(byte[] paramArrayOfByte) {
/* 101 */       System.err.print(this.cName + ".loadProfile");
/* 102 */       long l = this.tcmm.loadProfile(paramArrayOfByte);
/* 103 */       System.err.printf("(ID=%x)\n", new Object[] { Long.valueOf(l) });
/* 104 */       return l;
/*     */     }
/*     */ 
/*     */     public void freeProfile(long paramLong) {
/* 108 */       System.err.printf(this.cName + ".freeProfile(ID=%x)\n", new Object[] { Long.valueOf(paramLong) });
/* 109 */       this.tcmm.freeProfile(paramLong);
/*     */     }
/*     */ 
/*     */     public int getProfileSize(long paramLong) {
/* 113 */       System.err.print(this.cName + ".getProfileSize(ID=" + paramLong + ")");
/* 114 */       int i = this.tcmm.getProfileSize(paramLong);
/* 115 */       System.err.println("=" + i);
/* 116 */       return i;
/*     */     }
/*     */ 
/*     */     public void getProfileData(long paramLong, byte[] paramArrayOfByte) {
/* 120 */       System.err.print(this.cName + ".getProfileData(ID=" + paramLong + ") ");
/* 121 */       System.err.println("requested " + paramArrayOfByte.length + " byte(s)");
/* 122 */       this.tcmm.getProfileData(paramLong, paramArrayOfByte);
/*     */     }
/*     */ 
/*     */     public int getTagSize(long paramLong, int paramInt) {
/* 126 */       System.err.printf(this.cName + ".getTagSize(ID=%x, TagSig=%s)", new Object[] { Long.valueOf(paramLong), signatureToString(paramInt) });
/*     */ 
/* 128 */       int i = this.tcmm.getTagSize(paramLong, paramInt);
/* 129 */       System.err.println("=" + i);
/* 130 */       return i;
/*     */     }
/*     */ 
/*     */     public void getTagData(long paramLong, int paramInt, byte[] paramArrayOfByte)
/*     */     {
/* 135 */       System.err.printf(this.cName + ".getTagData(ID=%x, TagSig=%s)", new Object[] { Long.valueOf(paramLong), signatureToString(paramInt) });
/*     */ 
/* 137 */       System.err.println(" requested " + paramArrayOfByte.length + " byte(s)");
/* 138 */       this.tcmm.getTagData(paramLong, paramInt, paramArrayOfByte);
/*     */     }
/*     */ 
/*     */     public void setTagData(long paramLong, int paramInt, byte[] paramArrayOfByte)
/*     */     {
/* 143 */       System.err.print(this.cName + ".setTagData(ID=" + paramLong + ", TagSig=" + paramInt + ")");
/*     */ 
/* 145 */       System.err.println(" sending " + paramArrayOfByte.length + " byte(s)");
/* 146 */       this.tcmm.setTagData(paramLong, paramInt, paramArrayOfByte);
/*     */     }
/*     */ 
/*     */     public ColorTransform createTransform(ICC_Profile paramICC_Profile, int paramInt1, int paramInt2)
/*     */     {
/* 153 */       System.err.println(this.cName + ".createTransform(ICC_Profile,int,int)");
/* 154 */       return this.tcmm.createTransform(paramICC_Profile, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public ColorTransform createTransform(ColorTransform[] paramArrayOfColorTransform) {
/* 158 */       System.err.println(this.cName + ".createTransform(ColorTransform[])");
/* 159 */       return this.tcmm.createTransform(paramArrayOfColorTransform);
/*     */     }
/*     */ 
/*     */     private static String signatureToString(int paramInt) {
/* 163 */       return String.format("%c%c%c%c", new Object[] { Character.valueOf((char)(0xFF & paramInt >> 24)), Character.valueOf((char)(0xFF & paramInt >> 16)), Character.valueOf((char)(0xFF & paramInt >> 8)), Character.valueOf((char)(0xFF & paramInt)) });
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.CMSManager
 * JD-Core Version:    0.6.2
 */