/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Random;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.spi.NamingManager;
/*     */ 
/*     */ class KrbServiceLocator
/*     */ {
/*     */   private static final String SRV_RR = "SRV";
/*  51 */   private static final String[] SRV_RR_ATTR = { "SRV" };
/*     */   private static final String SRV_TXT = "TXT";
/*  54 */   private static final String[] SRV_TXT_ATTR = { "TXT" };
/*     */ 
/*  56 */   private static final Random random = new Random();
/*     */ 
/*     */   static String[] getKerberosService(String paramString)
/*     */   {
/*  76 */     String str = "dns:///_kerberos." + paramString;
/*  77 */     Object localObject = null;
/*     */     try
/*     */     {
/*  83 */       Context localContext = NamingManager.getURLContext("dns", new Hashtable(0));
/*  84 */       if (!(localContext instanceof DirContext)) {
/*  85 */         return null;
/*     */       }
/*  87 */       Attributes localAttributes = ((DirContext)localContext).getAttributes(str, SRV_TXT_ATTR);
/*     */       Attribute localAttribute;
/*  91 */       if ((localAttributes != null) && ((localAttribute = localAttributes.get("TXT")) != null)) {
/*  92 */         int i = localAttribute.size();
/*  93 */         int j = 0;
/*  94 */         String[] arrayOfString1 = new String[i];
/*     */ 
/*  97 */         int k = 0;
/*  98 */         int m = 0;
/*  99 */         while (k < i) {
/*     */           try {
/* 101 */             arrayOfString1[m] = ((String)localAttribute.get(k));
/* 102 */             m++;
/*     */           }
/*     */           catch (Exception localException) {
/*     */           }
/* 106 */           k++;
/*     */         }
/* 108 */         j = m;
/*     */ 
/* 111 */         if (j < i) {
/* 112 */           String[] arrayOfString2 = new String[j];
/* 113 */           System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, j);
/* 114 */           localObject = arrayOfString2;
/*     */         } else {
/* 116 */           localObject = arrayOfString1;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (NamingException localNamingException) {
/*     */     }
/* 122 */     return localObject;
/*     */   }
/*     */ 
/*     */   static String[] getKerberosService(String paramString1, String paramString2)
/*     */   {
/* 136 */     String str = "dns:///_kerberos." + paramString2 + "." + paramString1;
/* 137 */     String[] arrayOfString = null;
/*     */     try
/*     */     {
/* 144 */       Context localContext = NamingManager.getURLContext("dns", new Hashtable(0));
/* 145 */       if (!(localContext instanceof DirContext)) {
/* 146 */         return null;
/*     */       }
/* 148 */       Attributes localAttributes = ((DirContext)localContext).getAttributes(str, SRV_RR_ATTR);
/*     */       Attribute localAttribute;
/* 152 */       if ((localAttributes != null) && ((localAttribute = localAttributes.get("SRV")) != null)) {
/* 153 */         int i = localAttribute.size();
/* 154 */         int j = 0;
/* 155 */         Object localObject = new SrvRecord[i];
/*     */ 
/* 158 */         int k = 0;
/* 159 */         int m = 0;
/* 160 */         while (k < i) {
/*     */           try {
/* 162 */             localObject[m] = new SrvRecord((String)localAttribute.get(k));
/* 163 */             m++;
/*     */           }
/*     */           catch (Exception localException) {
/*     */           }
/* 167 */           k++;
/*     */         }
/* 169 */         j = m;
/*     */ 
/* 172 */         if (j < i) {
/* 173 */           SrvRecord[] arrayOfSrvRecord = new SrvRecord[j];
/* 174 */           System.arraycopy(localObject, 0, arrayOfSrvRecord, 0, j);
/* 175 */           localObject = arrayOfSrvRecord;
/*     */         }
/*     */ 
/* 181 */         if (j > 1) {
/* 182 */           Arrays.sort((Object[])localObject);
/*     */         }
/*     */ 
/* 186 */         arrayOfString = extractHostports((SrvRecord[])localObject);
/*     */       }
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/*     */     }
/* 192 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static String[] extractHostports(SrvRecord[] paramArrayOfSrvRecord)
/*     */   {
/* 200 */     String[] arrayOfString = null;
/*     */ 
/* 202 */     int i = 0;
/* 203 */     int j = 0;
/* 204 */     int k = 0;
/* 205 */     int m = 0;
/* 206 */     for (int n = 0; n < paramArrayOfSrvRecord.length; n++) {
/* 207 */       if (arrayOfString == null) {
/* 208 */         arrayOfString = new String[paramArrayOfSrvRecord.length];
/*     */       }
/*     */ 
/* 212 */       i = n;
/* 213 */       while ((n < paramArrayOfSrvRecord.length - 1) && (paramArrayOfSrvRecord[n].priority == paramArrayOfSrvRecord[(n + 1)].priority))
/*     */       {
/* 215 */         n++;
/*     */       }
/* 217 */       j = n;
/*     */ 
/* 220 */       k = j - i + 1;
/* 221 */       for (int i1 = 0; i1 < k; i1++) {
/* 222 */         arrayOfString[(m++)] = selectHostport(paramArrayOfSrvRecord, i, j);
/*     */       }
/*     */     }
/* 225 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static String selectHostport(SrvRecord[] paramArrayOfSrvRecord, int paramInt1, int paramInt2)
/*     */   {
/* 234 */     if (paramInt1 == paramInt2) {
/* 235 */       return paramArrayOfSrvRecord[paramInt1].hostport;
/*     */     }
/*     */ 
/* 239 */     int i = 0;
/* 240 */     for (int j = paramInt1; j <= paramInt2; j++) {
/* 241 */       if (paramArrayOfSrvRecord[j] != null) {
/* 242 */         i += paramArrayOfSrvRecord[j].weight;
/* 243 */         paramArrayOfSrvRecord[j].sum = i;
/*     */       }
/*     */     }
/* 246 */     String str = null;
/*     */ 
/* 250 */     int k = i == 0 ? 0 : random.nextInt(i + 1);
/* 251 */     for (int m = paramInt1; m <= paramInt2; m++) {
/* 252 */       if ((paramArrayOfSrvRecord[m] != null) && (paramArrayOfSrvRecord[m].sum >= k)) {
/* 253 */         str = paramArrayOfSrvRecord[m].hostport;
/* 254 */         paramArrayOfSrvRecord[m] = null;
/* 255 */         break;
/*     */       }
/*     */     }
/* 258 */     return str;
/*     */   }
/*     */ 
/*     */   static class SrvRecord
/*     */     implements Comparable
/*     */   {
/*     */     int priority;
/*     */     int weight;
/*     */     int sum;
/*     */     String hostport;
/*     */ 
/*     */     SrvRecord(String paramString)
/*     */       throws Exception
/*     */     {
/* 281 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ");
/*     */ 
/* 284 */       if (localStringTokenizer.countTokens() == 4) {
/* 285 */         this.priority = Integer.parseInt(localStringTokenizer.nextToken());
/* 286 */         this.weight = Integer.parseInt(localStringTokenizer.nextToken());
/* 287 */         String str = localStringTokenizer.nextToken();
/* 288 */         this.hostport = (localStringTokenizer.nextToken() + ":" + str);
/*     */       } else {
/* 290 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */ 
/*     */     public int compareTo(Object paramObject)
/*     */     {
/* 299 */       SrvRecord localSrvRecord = (SrvRecord)paramObject;
/* 300 */       if (this.priority > localSrvRecord.priority)
/* 301 */         return 1;
/* 302 */       if (this.priority < localSrvRecord.priority)
/* 303 */         return -1;
/* 304 */       if ((this.weight == 0) && (localSrvRecord.weight != 0))
/* 305 */         return -1;
/* 306 */       if ((this.weight != 0) && (localSrvRecord.weight == 0)) {
/* 307 */         return 1;
/*     */       }
/* 309 */       return 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbServiceLocator
 * JD-Core Version:    0.6.2
 */