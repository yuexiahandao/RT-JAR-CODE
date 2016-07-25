/*     */ package sun.security.krb5.internal.tools;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import javax.security.auth.kerberos.KeyTab;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.KrbAsReqBuilder;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.krb5.internal.HostAddresses;
/*     */ import sun.security.krb5.internal.KDCOptions;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.ccache.Credentials;
/*     */ import sun.security.krb5.internal.ccache.CredentialsCache;
/*     */ import sun.security.util.Password;
/*     */ 
/*     */ public class Kinit
/*     */ {
/*     */   private KinitOptions options;
/*  52 */   private static final boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*     */     try
/*     */     {
/* 113 */       Kinit localKinit = new Kinit(paramArrayOfString);
/*     */     }
/*     */     catch (Exception localException) {
/* 116 */       String str = null;
/* 117 */       if ((localException instanceof KrbException)) {
/* 118 */         str = ((KrbException)localException).krbErrorMessage() + " " + ((KrbException)localException).returnCodeMessage();
/*     */       }
/*     */       else {
/* 121 */         str = localException.getMessage();
/*     */       }
/* 123 */       if (str != null)
/* 124 */         System.err.println("Exception: " + str);
/*     */       else {
/* 126 */         System.out.println("Exception: " + localException);
/*     */       }
/* 128 */       localException.printStackTrace();
/* 129 */       System.exit(-1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Kinit(String[] paramArrayOfString)
/*     */     throws IOException, RealmException, KrbException
/*     */   {
/* 144 */     if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
/* 145 */       this.options = new KinitOptions();
/*     */     else {
/* 147 */       this.options = new KinitOptions(paramArrayOfString);
/*     */     }
/* 149 */     String str1 = null;
/* 150 */     PrincipalName localPrincipalName1 = this.options.getPrincipal();
/* 151 */     if (localPrincipalName1 != null) {
/* 152 */       str1 = localPrincipalName1.toString();
/*     */     }
/*     */ 
/* 155 */     if (DEBUG) {
/* 156 */       System.out.println("Principal is " + localPrincipalName1);
/*     */     }
/* 158 */     char[] arrayOfChar = this.options.password;
/* 159 */     boolean bool = this.options.useKeytabFile();
/*     */     KrbAsReqBuilder localKrbAsReqBuilder;
/* 160 */     if (!bool) {
/* 161 */       if (str1 == null) {
/* 162 */         throw new IllegalArgumentException(" Can not obtain principal name");
/*     */       }
/*     */ 
/* 165 */       if (arrayOfChar == null) {
/* 166 */         System.out.print("Password for " + str1 + ":");
/* 167 */         System.out.flush();
/* 168 */         arrayOfChar = Password.readPassword(System.in);
/* 169 */         if (DEBUG) {
/* 170 */           System.out.println(">>> Kinit console input " + new String(arrayOfChar));
/*     */         }
/*     */       }
/*     */ 
/* 174 */       localKrbAsReqBuilder = new KrbAsReqBuilder(localPrincipalName1, arrayOfChar);
/*     */     } else {
/* 176 */       if (DEBUG) {
/* 177 */         System.out.println(">>> Kinit using keytab");
/*     */       }
/* 179 */       if (str1 == null) {
/* 180 */         throw new IllegalArgumentException("Principal name must be specified.");
/*     */       }
/*     */ 
/* 183 */       localObject = this.options.keytabFileName();
/* 184 */       if ((localObject != null) && 
/* 185 */         (DEBUG)) {
/* 186 */         System.out.println(">>> Kinit keytab file name: " + (String)localObject);
/*     */       }
/*     */ 
/* 191 */       localKrbAsReqBuilder = new KrbAsReqBuilder(localPrincipalName1, localObject == null ? KeyTab.getInstance() : KeyTab.getInstance(new File((String)localObject)));
/*     */     }
/*     */ 
/* 196 */     Object localObject = new KDCOptions();
/* 197 */     setOptions(1, this.options.forwardable, (KDCOptions)localObject);
/* 198 */     setOptions(3, this.options.proxiable, (KDCOptions)localObject);
/* 199 */     localKrbAsReqBuilder.setOptions((KDCOptions)localObject);
/* 200 */     String str2 = this.options.getKDCRealm();
/* 201 */     if (str2 == null) {
/* 202 */       str2 = Config.getInstance().getDefaultRealm();
/*     */     }
/*     */ 
/* 205 */     if (DEBUG) {
/* 206 */       System.out.println(">>> Kinit realm name is " + str2);
/*     */     }
/*     */ 
/* 209 */     PrincipalName localPrincipalName2 = new PrincipalName("krbtgt/" + str2, 2);
/*     */ 
/* 211 */     localPrincipalName2.setRealm(str2);
/* 212 */     localKrbAsReqBuilder.setTarget(localPrincipalName2);
/*     */ 
/* 214 */     if (DEBUG) {
/* 215 */       System.out.println(">>> Creating KrbAsReq");
/*     */     }
/*     */ 
/* 218 */     if (this.options.getAddressOption()) {
/* 219 */       localKrbAsReqBuilder.setAddresses(HostAddresses.getLocalAddresses());
/*     */     }
/* 221 */     localKrbAsReqBuilder.action();
/*     */ 
/* 223 */     Credentials localCredentials = localKrbAsReqBuilder.getCCreds();
/*     */ 
/* 225 */     localKrbAsReqBuilder.destroy();
/*     */ 
/* 228 */     CredentialsCache localCredentialsCache = CredentialsCache.create(localPrincipalName1, this.options.cachename);
/*     */ 
/* 230 */     if (localCredentialsCache == null) {
/* 231 */       throw new IOException("Unable to create the cache file " + this.options.cachename);
/*     */     }
/*     */ 
/* 234 */     localCredentialsCache.update(localCredentials);
/* 235 */     localCredentialsCache.save();
/*     */ 
/* 237 */     if (this.options.password == null)
/*     */     {
/* 239 */       System.out.println("New ticket is stored in cache file " + this.options.cachename);
/*     */     }
/*     */     else {
/* 242 */       Arrays.fill(this.options.password, '0');
/*     */     }
/*     */ 
/* 246 */     if (arrayOfChar != null) {
/* 247 */       Arrays.fill(arrayOfChar, '0');
/*     */     }
/* 249 */     this.options = null;
/*     */   }
/*     */ 
/*     */   private static void setOptions(int paramInt1, int paramInt2, KDCOptions paramKDCOptions) {
/* 253 */     switch (paramInt2) {
/*     */     case 0:
/* 255 */       break;
/*     */     case -1:
/* 257 */       paramKDCOptions.set(paramInt1, false);
/* 258 */       break;
/*     */     case 1:
/* 260 */       paramKDCOptions.set(paramInt1, true);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.tools.Kinit
 * JD-Core Version:    0.6.2
 */