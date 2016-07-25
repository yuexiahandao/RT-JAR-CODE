/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.Credentials;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.KrbTgsReq;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.ServiceName;
/*     */ 
/*     */ public class CredentialsUtil
/*     */ {
/*  54 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public static Credentials acquireServiceCreds(String paramString, Credentials paramCredentials)
/*     */     throws KrbException, IOException
/*     */   {
/*  75 */     ServiceName localServiceName1 = new ServiceName(paramString);
/*  76 */     Object localObject1 = localServiceName1.getRealmString();
/*  77 */     Object localObject2 = paramCredentials.getClient().getRealmString();
/*  78 */     String str1 = Config.getInstance().getDefaultRealm();
/*     */ 
/*  80 */     if (localObject2 == null) {
/*  81 */       localObject3 = null;
/*  82 */       if ((localObject3 = paramCredentials.getServer()) != null)
/*  83 */         localObject2 = ((PrincipalName)localObject3).getRealmString();
/*     */     }
/*  85 */     if (localObject2 == null) {
/*  86 */       localObject2 = str1;
/*     */     }
/*  88 */     if (localObject1 == null) {
/*  89 */       localObject1 = localObject2;
/*  90 */       localServiceName1.setRealm((String)localObject1);
/*     */     }
/*     */ 
/* 111 */     if (((String)localObject2).equals(localObject1))
/*     */     {
/* 113 */       if (DEBUG)
/* 114 */         System.out.println(">>> Credentials acquireServiceCreds: same realm");
/* 115 */       return serviceCreds(localServiceName1, paramCredentials);
/*     */     }
/*     */ 
/* 119 */     Object localObject3 = Realm.getRealmsList((String)localObject2, (String)localObject1);
/* 120 */     int i = 1;
/*     */ 
/* 122 */     if ((localObject3 == null) || (localObject3.length == 0))
/*     */     {
/* 124 */       if (DEBUG)
/* 125 */         System.out.println(">>> Credentials acquireServiceCreds: no realms list");
/* 126 */       return null;
/*     */     }
/*     */ 
/* 129 */     int j = 0; int k = 0;
/* 130 */     Object localObject4 = null; Credentials localCredentials1 = null; Credentials localCredentials2 = null;
/* 131 */     ServiceName localServiceName2 = null;
/* 132 */     Object localObject5 = null; String str2 = null; String str3 = null;
/*     */ 
/* 134 */     localObject4 = paramCredentials; for (j = 0; j < localObject3.length; )
/*     */     {
/* 136 */       localServiceName2 = new ServiceName("krbtgt", (String)localObject1, localObject3[j]);
/*     */ 
/* 139 */       if (DEBUG)
/*     */       {
/* 141 */         System.out.println(">>> Credentials acquireServiceCreds: main loop: [" + j + "] tempService=" + localServiceName2);
/*     */       }
/*     */       try
/*     */       {
/* 145 */         localCredentials1 = serviceCreds(localServiceName2, (Credentials)localObject4);
/*     */       } catch (Exception localException1) {
/* 147 */         localCredentials1 = null;
/*     */       }
/*     */ 
/* 150 */       if (localCredentials1 == null)
/*     */       {
/* 152 */         if (DEBUG)
/*     */         {
/* 154 */           System.out.println(">>> Credentials acquireServiceCreds: no tgt; searching backwards");
/*     */         }
/*     */ 
/* 161 */         localCredentials1 = null; for (k = j + 1; 
/* 162 */           (localCredentials1 == null) && (k < localObject3.length); k++)
/*     */         {
/* 165 */           localServiceName2 = new ServiceName("krbtgt", localObject3[k], localObject3[j]);
/*     */ 
/* 168 */           if (DEBUG)
/*     */           {
/* 170 */             System.out.println(">>> Credentials acquireServiceCreds: inner loop: [" + k + "] tempService=" + localServiceName2);
/*     */           }
/*     */           try
/*     */           {
/* 174 */             localCredentials1 = serviceCreds(localServiceName2, (Credentials)localObject4);
/*     */           } catch (Exception localException2) {
/* 176 */             localCredentials1 = null;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 181 */       if (localCredentials1 == null)
/*     */       {
/* 183 */         if (!DEBUG)
/*     */           break;
/* 185 */         System.out.println(">>> Credentials acquireServiceCreds: no tgt; cannot get creds"); break;
/*     */       }
/*     */ 
/* 195 */       str2 = localCredentials1.getServer().getInstanceComponent();
/* 196 */       if ((i != 0) && (!localCredentials1.checkDelegate())) {
/* 197 */         if (DEBUG)
/*     */         {
/* 199 */           System.out.println(">>> Credentials acquireServiceCreds: global OK-AS-DELEGATE turned off at " + localCredentials1.getServer());
/*     */         }
/*     */ 
/* 203 */         i = 0;
/*     */       }
/*     */ 
/* 206 */       if (DEBUG)
/*     */       {
/* 208 */         System.out.println(">>> Credentials acquireServiceCreds: got tgt");
/*     */       }
/*     */ 
/* 212 */       if (str2.equals(localObject1))
/*     */       {
/* 215 */         localCredentials2 = localCredentials1;
/* 216 */         str3 = str2;
/* 217 */         break;
/*     */       }
/*     */ 
/* 226 */       for (k = j + 1; k < localObject3.length; k++)
/*     */       {
/* 228 */         if (str2.equals(localObject3[k]))
/*     */         {
/*     */           break;
/*     */         }
/*     */       }
/*     */ 
/* 234 */       if (k >= localObject3.length)
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 240 */       j = k;
/* 241 */       localObject4 = localCredentials1;
/*     */ 
/* 243 */       if (DEBUG)
/*     */       {
/* 245 */         System.out.println(">>> Credentials acquireServiceCreds: continuing with main loop counter reset to " + j);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 263 */     Credentials localCredentials3 = null;
/*     */ 
/* 265 */     if (localCredentials2 != null)
/*     */     {
/* 269 */       if (DEBUG)
/*     */       {
/* 271 */         System.out.println(">>> Credentials acquireServiceCreds: got right tgt");
/*     */ 
/* 275 */         System.out.println(">>> Credentials acquireServiceCreds: obtaining service creds for " + localServiceName1);
/*     */       }
/*     */       try
/*     */       {
/* 279 */         localCredentials3 = serviceCreds(localServiceName1, localCredentials2);
/*     */       } catch (Exception localException3) {
/* 281 */         if (DEBUG)
/* 282 */           System.out.println(localException3);
/* 283 */         localCredentials3 = null;
/*     */       }
/*     */     }
/*     */ 
/* 287 */     if (localCredentials3 != null)
/*     */     {
/* 289 */       if (DEBUG)
/*     */       {
/* 291 */         System.out.println(">>> Credentials acquireServiceCreds: returning creds:");
/* 292 */         Credentials.printDebug(localCredentials3);
/*     */       }
/* 294 */       if (i == 0) {
/* 295 */         localCredentials3.resetDelegate();
/*     */       }
/* 297 */       return localCredentials3;
/*     */     }
/* 299 */     throw new KrbApErrException(63, "No service creds");
/*     */   }
/*     */ 
/*     */   private static Credentials serviceCreds(ServiceName paramServiceName, Credentials paramCredentials)
/*     */     throws KrbException, IOException
/*     */   {
/* 309 */     return new KrbTgsReq(paramCredentials, paramServiceName).sendAndGetCreds();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.CredentialsUtil
 * JD-Core Version:    0.6.2
 */