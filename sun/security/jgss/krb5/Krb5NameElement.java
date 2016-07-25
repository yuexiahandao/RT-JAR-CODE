/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.Provider;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.ServiceName;
/*     */ 
/*     */ public class Krb5NameElement
/*     */   implements GSSNameSpi
/*     */ {
/*     */   private PrincipalName krb5PrincipalName;
/*  49 */   private String gssNameStr = null;
/*  50 */   private Oid gssNameType = null;
/*     */ 
/*  53 */   private static String CHAR_ENCODING = "UTF-8";
/*     */ 
/*     */   private Krb5NameElement(PrincipalName paramPrincipalName, String paramString, Oid paramOid)
/*     */   {
/*  58 */     this.krb5PrincipalName = paramPrincipalName;
/*  59 */     this.gssNameStr = paramString;
/*  60 */     this.gssNameType = paramOid;
/*     */   }
/*     */ 
/*     */   static Krb5NameElement getInstance(String paramString, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/*  77 */     if (paramOid == null) {
/*  78 */       paramOid = Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL;
/*     */     }
/*  80 */     else if ((!paramOid.equals(GSSName.NT_USER_NAME)) && (!paramOid.equals(GSSName.NT_HOSTBASED_SERVICE)) && (!paramOid.equals(Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL)) && (!paramOid.equals(GSSName.NT_EXPORT_NAME)))
/*     */     {
/*  84 */       throw new GSSException(4, -1, paramOid.toString() + " is an unsupported nametype");
/*     */     }
/*     */ 
/*     */     Object localObject;
/*     */     try
/*     */     {
/*  91 */       if ((paramOid.equals(GSSName.NT_EXPORT_NAME)) || (paramOid.equals(Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL)))
/*     */       {
/*  93 */         localObject = new PrincipalName(paramString, 1);
/*     */       }
/*     */       else
/*     */       {
/*  97 */         String[] arrayOfString = getComponents(paramString);
/*     */ 
/* 111 */         if (paramOid.equals(GSSName.NT_USER_NAME)) {
/* 112 */           localObject = new PrincipalName(paramString, 1);
/*     */         }
/*     */         else {
/* 115 */           String str1 = null;
/* 116 */           String str2 = arrayOfString[0];
/* 117 */           if (arrayOfString.length >= 2) {
/* 118 */             str1 = arrayOfString[1];
/*     */           }
/* 120 */           String str3 = getHostBasedInstance(str2, str1);
/* 121 */           localObject = new ServiceName(str3, 3);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (KrbException localKrbException)
/*     */     {
/* 127 */       throw new GSSException(3, -1, localKrbException.getMessage());
/*     */     }
/*     */ 
/* 130 */     return new Krb5NameElement((PrincipalName)localObject, paramString, paramOid);
/*     */   }
/*     */ 
/*     */   static Krb5NameElement getInstance(PrincipalName paramPrincipalName) {
/* 134 */     return new Krb5NameElement(paramPrincipalName, paramPrincipalName.getName(), Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL);
/*     */   }
/*     */ 
/*     */   private static String[] getComponents(String paramString)
/*     */     throws GSSException
/*     */   {
/* 148 */     int i = paramString.lastIndexOf('@', paramString.length());
/*     */ 
/* 152 */     if ((i > 0) && (paramString.charAt(i - 1) == '\\'))
/*     */     {
/* 155 */       if ((i - 2 < 0) || (paramString.charAt(i - 2) != '\\'))
/*     */       {
/* 157 */         i = -1;
/*     */       }
/*     */     }
/*     */     String[] arrayOfString;
/* 160 */     if (i > 0) {
/* 161 */       String str1 = paramString.substring(0, i);
/* 162 */       String str2 = paramString.substring(i + 1);
/* 163 */       arrayOfString = new String[] { str1, str2 };
/*     */     } else {
/* 165 */       arrayOfString = new String[] { paramString };
/*     */     }
/*     */ 
/* 168 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static String getHostBasedInstance(String paramString1, String paramString2)
/*     */     throws GSSException
/*     */   {
/* 175 */     StringBuffer localStringBuffer = new StringBuffer(paramString1);
/*     */     try
/*     */     {
/* 181 */       if (paramString2 == null)
/* 182 */         paramString2 = InetAddress.getLocalHost().getHostName();
/*     */     }
/*     */     catch (UnknownHostException localUnknownHostException)
/*     */     {
/*     */     }
/* 187 */     paramString2 = paramString2.toLowerCase();
/*     */ 
/* 189 */     localStringBuffer = localStringBuffer.append('/').append(paramString2);
/* 190 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public final PrincipalName getKrb5PrincipalName() {
/* 194 */     return this.krb5PrincipalName;
/*     */   }
/*     */ 
/*     */   public boolean equals(GSSNameSpi paramGSSNameSpi)
/*     */     throws GSSException
/*     */   {
/* 209 */     if (paramGSSNameSpi == this) {
/* 210 */       return true;
/*     */     }
/* 212 */     if ((paramGSSNameSpi instanceof Krb5NameElement)) {
/* 213 */       Krb5NameElement localKrb5NameElement = (Krb5NameElement)paramGSSNameSpi;
/* 214 */       return this.krb5PrincipalName.getName().equals(localKrb5NameElement.krb5PrincipalName.getName());
/*     */     }
/*     */ 
/* 217 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 232 */     if (this == paramObject) {
/* 233 */       return true;
/*     */     }
/*     */     try
/*     */     {
/* 237 */       if ((paramObject instanceof Krb5NameElement))
/* 238 */         return equals((Krb5NameElement)paramObject);
/*     */     }
/*     */     catch (GSSException localGSSException) {
/*     */     }
/* 242 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 251 */     return 629 + this.krb5PrincipalName.getName().hashCode();
/*     */   }
/*     */ 
/*     */   public byte[] export()
/*     */     throws GSSException
/*     */   {
/* 274 */     byte[] arrayOfByte = null;
/*     */     try {
/* 276 */       arrayOfByte = this.krb5PrincipalName.getName().getBytes(CHAR_ENCODING);
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 280 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public Oid getMechanism()
/*     */   {
/* 289 */     return Krb5MechFactory.GSS_KRB5_MECH_OID;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 301 */     return this.gssNameStr;
/*     */   }
/*     */ 
/*     */   public Oid getGSSNameType()
/*     */   {
/* 309 */     return this.gssNameType;
/*     */   }
/*     */ 
/*     */   public Oid getStringNameType()
/*     */   {
/* 320 */     return this.gssNameType;
/*     */   }
/*     */ 
/*     */   public boolean isAnonymousName()
/*     */   {
/* 327 */     return this.gssNameType.equals(GSSName.NT_ANONYMOUS);
/*     */   }
/*     */ 
/*     */   public Provider getProvider() {
/* 331 */     return Krb5MechFactory.PROVIDER;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.Krb5NameElement
 * JD-Core Version:    0.6.2
 */