/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.internal.util.KerberosFlags;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KDCOptions extends KerberosFlags
/*     */ {
/* 123 */   public final int KDC_OPT_PROXIABLE = 268435456;
/* 124 */   public final int KDC_OPT_RENEWABLE_OK = 16;
/* 125 */   public final int KDC_OPT_FORWARDABLE = 1073741824;
/*     */   public static final int RESERVED = 0;
/*     */   public static final int FORWARDABLE = 1;
/*     */   public static final int FORWARDED = 2;
/*     */   public static final int PROXIABLE = 3;
/*     */   public static final int PROXY = 4;
/*     */   public static final int ALLOW_POSTDATE = 5;
/*     */   public static final int POSTDATED = 6;
/*     */   public static final int UNUSED7 = 7;
/*     */   public static final int RENEWABLE = 8;
/*     */   public static final int UNUSED9 = 9;
/*     */   public static final int UNUSED10 = 10;
/*     */   public static final int UNUSED11 = 11;
/*     */   public static final int RENEWABLE_OK = 27;
/*     */   public static final int ENC_TKT_IN_SKEY = 28;
/*     */   public static final int RENEW = 30;
/*     */   public static final int VALIDATE = 31;
/* 147 */   private boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public KDCOptions() {
/* 150 */     super(32);
/* 151 */     setDefault();
/*     */   }
/*     */ 
/*     */   public KDCOptions(int paramInt, byte[] paramArrayOfByte) throws Asn1Exception {
/* 155 */     super(paramInt, paramArrayOfByte);
/* 156 */     if ((paramInt > paramArrayOfByte.length * 8) || (paramInt > 32))
/* 157 */       throw new Asn1Exception(502);
/*     */   }
/*     */ 
/*     */   public KDCOptions(boolean[] paramArrayOfBoolean)
/*     */     throws Asn1Exception
/*     */   {
/* 169 */     super(paramArrayOfBoolean);
/* 170 */     if (paramArrayOfBoolean.length > 32)
/* 171 */       throw new Asn1Exception(502);
/*     */   }
/*     */ 
/*     */   public KDCOptions(DerValue paramDerValue) throws Asn1Exception, IOException
/*     */   {
/* 176 */     this(paramDerValue.getUnalignedBitString(true).toBooleanArray());
/*     */   }
/*     */ 
/*     */   public KDCOptions(byte[] paramArrayOfByte)
/*     */   {
/* 186 */     super(paramArrayOfByte.length * 8, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public static KDCOptions parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 205 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/* 206 */       return null;
/* 207 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 208 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 209 */       throw new Asn1Exception(906);
/*     */     }
/* 211 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 212 */     return new KDCOptions(localDerValue2);
/*     */   }
/*     */ 
/*     */   public void set(int paramInt, boolean paramBoolean)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 225 */     super.set(paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean get(int paramInt)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 238 */     return super.get(paramInt);
/*     */   }
/*     */ 
/*     */   private void setDefault()
/*     */   {
/*     */     try
/*     */     {
/* 245 */       Config localConfig = Config.getInstance();
/*     */ 
/* 252 */       int i = localConfig.getDefaultIntValue("kdc_default_options", "libdefaults");
/*     */ 
/* 255 */       if ((i & 0x1B) == 27) {
/* 256 */         set(27, true);
/*     */       }
/* 258 */       else if (localConfig.getDefaultBooleanValue("renewable", "libdefaults")) {
/* 259 */         set(27, true);
/*     */       }
/*     */ 
/* 262 */       if ((i & 0x3) == 3) {
/* 263 */         set(3, true);
/*     */       }
/* 265 */       else if (localConfig.getDefaultBooleanValue("proxiable", "libdefaults")) {
/* 266 */         set(3, true);
/*     */       }
/*     */ 
/* 270 */       if ((i & 0x1) == 1) {
/* 271 */         set(1, true);
/*     */       }
/* 273 */       else if (localConfig.getDefaultBooleanValue("forwardable", "libdefaults"))
/* 274 */         set(1, true);
/*     */     }
/*     */     catch (KrbException localKrbException)
/*     */     {
/* 278 */       if (this.DEBUG) {
/* 279 */         System.out.println("Exception in getting default values for KDC Options from the configuration ");
/*     */ 
/* 281 */         localKrbException.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KDCOptions
 * JD-Core Version:    0.6.2
 */