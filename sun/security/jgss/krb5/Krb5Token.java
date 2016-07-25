/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSToken;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ abstract class Krb5Token extends GSSToken
/*     */ {
/*     */   public static final int AP_REQ_ID = 256;
/*     */   public static final int AP_REP_ID = 512;
/*     */   public static final int ERR_ID = 768;
/*     */   public static final int MIC_ID = 257;
/*     */   public static final int WRAP_ID = 513;
/*     */   public static final int MIC_ID_v2 = 1028;
/*     */   public static final int WRAP_ID_v2 = 1284;
/*     */   public static ObjectIdentifier OID;
/*     */ 
/*     */   public static String getTokenName(int paramInt)
/*     */   {
/*  94 */     String str = null;
/*  95 */     switch (paramInt) {
/*     */     case 256:
/*     */     case 512:
/*  98 */       str = "Context Establishment Token";
/*  99 */       break;
/*     */     case 257:
/* 101 */       str = "MIC Token";
/* 102 */       break;
/*     */     case 1028:
/* 104 */       str = "MIC Token (new format)";
/* 105 */       break;
/*     */     case 513:
/* 107 */       str = "Wrap Token";
/* 108 */       break;
/*     */     case 1284:
/* 110 */       str = "Wrap Token (new format)";
/* 111 */       break;
/*     */     default:
/* 113 */       str = "Kerberos GSS-API Mechanism Token";
/*     */     }
/*     */ 
/* 116 */     return str;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  80 */       OID = new ObjectIdentifier(Krb5MechFactory.GSS_KRB5_MECH_OID.toString());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.Krb5Token
 * JD-Core Version:    0.6.2
 */