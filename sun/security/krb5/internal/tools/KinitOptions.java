/*     */ package sun.security.krb5.internal.tools;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.ccache.CCacheInputStream;
/*     */ import sun.security.krb5.internal.ccache.FileCredentialsCache;
/*     */ 
/*     */ class KinitOptions
/*     */ {
/*  52 */   public boolean validate = false;
/*     */ 
/*  57 */   public short forwardable = -1;
/*  58 */   public short proxiable = -1;
/*  59 */   public boolean renew = false;
/*     */   public KerberosTime lifetime;
/*     */   public KerberosTime renewable_lifetime;
/*     */   public String target_service;
/*     */   public String keytab_file;
/*     */   public String cachename;
/*     */   private PrincipalName principal;
/*     */   public String realm;
/*  67 */   char[] password = null;
/*     */   public boolean keytab;
/*  69 */   private boolean DEBUG = Krb5.DEBUG;
/*  70 */   private boolean includeAddresses = true;
/*  71 */   private boolean useKeytab = false;
/*     */   private String ktabName;
/*     */ 
/*     */   public KinitOptions()
/*     */     throws RuntimeException, RealmException
/*     */   {
/*  77 */     this.cachename = FileCredentialsCache.getDefaultCacheName();
/*  78 */     if (this.cachename == null) {
/*  79 */       throw new RuntimeException("default cache name error");
/*     */     }
/*  81 */     this.principal = getDefaultPrincipal();
/*     */   }
/*     */ 
/*     */   public void setKDCRealm(String paramString) throws RealmException {
/*  85 */     this.realm = paramString;
/*     */   }
/*     */ 
/*     */   public String getKDCRealm() {
/*  89 */     if ((this.realm == null) && 
/*  90 */       (this.principal != null)) {
/*  91 */       return this.principal.getRealmString();
/*     */     }
/*     */ 
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   public KinitOptions(String[] paramArrayOfString)
/*     */     throws KrbException, RuntimeException, IOException
/*     */   {
/* 100 */     String str1 = null;
/*     */ 
/* 102 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 103 */       if (paramArrayOfString[i].equals("-f")) {
/* 104 */         this.forwardable = 1;
/* 105 */       } else if (paramArrayOfString[i].equals("-p")) {
/* 106 */         this.proxiable = 1;
/* 107 */       } else if (paramArrayOfString[i].equals("-c"))
/*     */       {
/* 109 */         if (paramArrayOfString[(i + 1)].startsWith("-")) {
/* 110 */           throw new IllegalArgumentException("input format  not correct:  -c  option must be followed by the cache name");
/*     */         }
/*     */ 
/* 116 */         this.cachename = paramArrayOfString[(++i)];
/* 117 */         if ((this.cachename.length() >= 5) && (this.cachename.substring(0, 5).equalsIgnoreCase("FILE:")))
/*     */         {
/* 119 */           this.cachename = this.cachename.substring(5);
/*     */         }
/* 121 */       } else if (paramArrayOfString[i].equals("-A")) {
/* 122 */         this.includeAddresses = false;
/* 123 */       } else if (paramArrayOfString[i].equals("-k")) {
/* 124 */         this.useKeytab = true;
/* 125 */       } else if (paramArrayOfString[i].equals("-t")) {
/* 126 */         if (this.ktabName != null) {
/* 127 */           throw new IllegalArgumentException("-t option/keytab file name repeated");
/*     */         }
/* 129 */         if (i + 1 < paramArrayOfString.length)
/* 130 */           this.ktabName = paramArrayOfString[(++i)];
/*     */         else {
/* 132 */           throw new IllegalArgumentException("-t option requires keytab file name");
/*     */         }
/*     */ 
/* 136 */         this.useKeytab = true;
/* 137 */       } else if (paramArrayOfString[i].equalsIgnoreCase("-help")) {
/* 138 */         printHelp();
/* 139 */         System.exit(0);
/* 140 */       } else if (str1 == null) {
/* 141 */         str1 = paramArrayOfString[i];
/*     */         try {
/* 143 */           this.principal = new PrincipalName(str1);
/*     */         } catch (Exception localException) {
/* 145 */           throw new IllegalArgumentException("invalid Principal name: " + str1 + localException.getMessage());
/*     */         }
/*     */ 
/* 149 */         if (this.principal.getRealm() == null) {
/* 150 */           String str2 = Config.getInstance().getDefault("default_realm", "libdefaults");
/*     */ 
/* 153 */           if (str2 != null)
/* 154 */             this.principal.setRealm(str2);
/* 155 */           else throw new IllegalArgumentException("invalid Realm name");
/*     */         }
/*     */       }
/* 158 */       else if (this.password == null)
/*     */       {
/* 160 */         this.password = paramArrayOfString[i].toCharArray();
/*     */       } else {
/* 162 */         throw new IllegalArgumentException("too many parameters");
/*     */       }
/*     */     }
/*     */ 
/* 166 */     if (this.cachename == null) {
/* 167 */       this.cachename = FileCredentialsCache.getDefaultCacheName();
/* 168 */       if (this.cachename == null) {
/* 169 */         throw new RuntimeException("default cache name error");
/*     */       }
/*     */     }
/* 172 */     if (this.principal == null)
/* 173 */       this.principal = getDefaultPrincipal();
/*     */   }
/*     */ 
/*     */   PrincipalName getDefaultPrincipal()
/*     */   {
/* 179 */     String str1 = null;
/*     */     try {
/* 181 */       str1 = Config.getInstance().getDefaultRealm();
/*     */     } catch (KrbException localKrbException) {
/* 183 */       System.out.println("Can not get default realm " + localKrbException.getMessage());
/*     */ 
/* 185 */       localKrbException.printStackTrace();
/* 186 */       return null;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 193 */       CCacheInputStream localCCacheInputStream = new CCacheInputStream(new FileInputStream(this.cachename));
/*     */       int i;
/* 196 */       if ((i = localCCacheInputStream.readVersion()) == 1284)
/*     */       {
/* 198 */         localCCacheInputStream.readTag();
/*     */       }
/* 200 */       else if ((i == 1281) || (i == 1282))
/*     */       {
/* 202 */         localCCacheInputStream.setNativeByteOrder();
/*     */       }
/*     */ 
/* 205 */       PrincipalName localPrincipalName2 = localCCacheInputStream.readPrincipal(i);
/* 206 */       localCCacheInputStream.close();
/* 207 */       String str3 = localPrincipalName2.getRealmString();
/* 208 */       if (str3 == null) {
/* 209 */         localPrincipalName2.setRealm(str1);
/*     */       }
/* 211 */       if (this.DEBUG) {
/* 212 */         System.out.println(">>>KinitOptions principal name from the cache is :" + localPrincipalName2);
/*     */       }
/*     */ 
/* 215 */       return localPrincipalName2;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 219 */       if (this.DEBUG)
/* 220 */         localIOException.printStackTrace();
/*     */     }
/*     */     catch (RealmException localRealmException1) {
/* 223 */       if (this.DEBUG) {
/* 224 */         localRealmException1.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 228 */     String str2 = System.getProperty("user.name");
/* 229 */     if (this.DEBUG) {
/* 230 */       System.out.println(">>>KinitOptions default username is :" + str2);
/*     */     }
/*     */ 
/* 233 */     if (str1 != null) {
/*     */       try {
/* 235 */         PrincipalName localPrincipalName1 = new PrincipalName(str2);
/* 236 */         if (localPrincipalName1.getRealm() == null)
/* 237 */           localPrincipalName1.setRealm(str1);
/* 238 */         return localPrincipalName1;
/*     */       }
/*     */       catch (RealmException localRealmException2) {
/* 241 */         if (this.DEBUG) {
/* 242 */           System.out.println("Exception in getting principal name " + localRealmException2.getMessage());
/*     */ 
/* 244 */           localRealmException2.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 248 */     return null;
/*     */   }
/*     */ 
/*     */   void printHelp()
/*     */   {
/* 253 */     System.out.println("Usage: kinit [-A] [-f] [-p] [-c cachename] [[-k [-t keytab_file_name]] [principal] [password]");
/*     */ 
/* 257 */     System.out.println("\tavailable options to Kerberos 5 ticket request:");
/*     */ 
/* 259 */     System.out.println("\t    -A   do not include addresses");
/* 260 */     System.out.println("\t    -f   forwardable");
/* 261 */     System.out.println("\t    -p   proxiable");
/* 262 */     System.out.println("\t    -c   cache name (i.e., FILE:\\d:\\myProfiles\\mykrb5cache)");
/*     */ 
/* 264 */     System.out.println("\t    -k   use keytab");
/* 265 */     System.out.println("\t    -t   keytab file name");
/* 266 */     System.out.println("\t    principal   the principal name (i.e., qweadf@ATHENA.MIT.EDU qweadf)");
/*     */ 
/* 268 */     System.out.println("\t    password   the principal's Kerberos password");
/*     */   }
/*     */ 
/*     */   public boolean getAddressOption()
/*     */   {
/* 273 */     return this.includeAddresses;
/*     */   }
/*     */ 
/*     */   public boolean useKeytabFile() {
/* 277 */     return this.useKeytab;
/*     */   }
/*     */ 
/*     */   public String keytabFileName() {
/* 281 */     return this.ktabName;
/*     */   }
/*     */ 
/*     */   public PrincipalName getPrincipal() {
/* 285 */     return this.principal;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.tools.KinitOptions
 * JD-Core Version:    0.6.2
 */