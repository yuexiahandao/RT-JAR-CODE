/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.ldap.LdapName;
/*     */ import javax.naming.ldap.Rdn;
/*     */ import javax.naming.spi.NamingManager;
/*     */ 
/*     */ class ServiceLocator
/*     */ {
/*     */   private static final String SRV_RR = "SRV";
/*  53 */   private static final String[] SRV_RR_ATTR = { "SRV" };
/*     */ 
/*  55 */   private static final Random random = new Random();
/*     */ 
/*     */   static String mapDnToDomainName(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/*  72 */     if (paramString == null) {
/*  73 */       return null;
/*     */     }
/*  75 */     StringBuffer localStringBuffer = new StringBuffer();
/*  76 */     LdapName localLdapName = new LdapName(paramString);
/*     */ 
/*  81 */     List localList = localLdapName.getRdns();
/*  82 */     for (int i = localList.size() - 1; i >= 0; i--)
/*     */     {
/*  84 */       Rdn localRdn = (Rdn)localList.get(i);
/*     */ 
/*  87 */       if ((localRdn.size() == 1) && ("dc".equalsIgnoreCase(localRdn.getType())))
/*     */       {
/*  89 */         Object localObject = localRdn.getValue();
/*  90 */         if ((localObject instanceof String)) {
/*  91 */           if ((localObject.equals(".")) || ((localStringBuffer.length() == 1) && (localStringBuffer.charAt(0) == '.')))
/*     */           {
/*  93 */             localStringBuffer.setLength(0);
/*     */           }
/*     */ 
/*  96 */           if (localStringBuffer.length() > 0) {
/*  97 */             localStringBuffer.append('.');
/*     */           }
/*  99 */           localStringBuffer.append(localObject);
/*     */         } else {
/* 101 */           localStringBuffer.setLength(0);
/*     */         }
/*     */       } else {
/* 104 */         localStringBuffer.setLength(0);
/*     */       }
/*     */     }
/* 107 */     return localStringBuffer.length() != 0 ? localStringBuffer.toString() : null;
/*     */   }
/*     */ 
/*     */   static String[] getLdapService(String paramString, Hashtable paramHashtable)
/*     */   {
/* 122 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 123 */       return null;
/*     */     }
/*     */ 
/* 126 */     String str = "dns:///_ldap._tcp." + paramString;
/* 127 */     String[] arrayOfString = null;
/*     */     try
/*     */     {
/* 134 */       Context localContext = NamingManager.getURLContext("dns", paramHashtable);
/* 135 */       if (!(localContext instanceof DirContext)) {
/* 136 */         return null;
/*     */       }
/* 138 */       Attributes localAttributes = ((DirContext)localContext).getAttributes(str, SRV_RR_ATTR);
/*     */       Attribute localAttribute;
/* 142 */       if ((localAttributes != null) && ((localAttribute = localAttributes.get("SRV")) != null)) {
/* 143 */         int i = localAttribute.size();
/* 144 */         int j = 0;
/* 145 */         Object localObject = new SrvRecord[i];
/*     */ 
/* 148 */         int k = 0;
/* 149 */         int m = 0;
/* 150 */         while (k < i) {
/*     */           try {
/* 152 */             localObject[m] = new SrvRecord((String)localAttribute.get(k));
/* 153 */             m++;
/*     */           }
/*     */           catch (Exception localException) {
/*     */           }
/* 157 */           k++;
/*     */         }
/* 159 */         j = m;
/*     */ 
/* 162 */         if (j < i) {
/* 163 */           SrvRecord[] arrayOfSrvRecord = new SrvRecord[j];
/* 164 */           System.arraycopy(localObject, 0, arrayOfSrvRecord, 0, j);
/* 165 */           localObject = arrayOfSrvRecord;
/*     */         }
/*     */ 
/* 171 */         if (j > 1) {
/* 172 */           Arrays.sort((Object[])localObject);
/*     */         }
/*     */ 
/* 176 */         arrayOfString = extractHostports((SrvRecord[])localObject);
/*     */       }
/*     */     }
/*     */     catch (NamingException localNamingException) {
/*     */     }
/* 181 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static String[] extractHostports(SrvRecord[] paramArrayOfSrvRecord)
/*     */   {
/* 189 */     String[] arrayOfString = null;
/*     */ 
/* 191 */     int i = 0;
/* 192 */     int j = 0;
/* 193 */     int k = 0;
/* 194 */     int m = 0;
/* 195 */     for (int n = 0; n < paramArrayOfSrvRecord.length; n++) {
/* 196 */       if (arrayOfString == null) {
/* 197 */         arrayOfString = new String[paramArrayOfSrvRecord.length];
/*     */       }
/*     */ 
/* 201 */       i = n;
/* 202 */       while ((n < paramArrayOfSrvRecord.length - 1) && (paramArrayOfSrvRecord[n].priority == paramArrayOfSrvRecord[(n + 1)].priority))
/*     */       {
/* 204 */         n++;
/*     */       }
/* 206 */       j = n;
/*     */ 
/* 209 */       k = j - i + 1;
/* 210 */       for (int i1 = 0; i1 < k; i1++) {
/* 211 */         arrayOfString[(m++)] = selectHostport(paramArrayOfSrvRecord, i, j);
/*     */       }
/*     */     }
/* 214 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static String selectHostport(SrvRecord[] paramArrayOfSrvRecord, int paramInt1, int paramInt2)
/*     */   {
/* 223 */     if (paramInt1 == paramInt2) {
/* 224 */       return paramArrayOfSrvRecord[paramInt1].hostport;
/*     */     }
/*     */ 
/* 228 */     int i = 0;
/* 229 */     for (int j = paramInt1; j <= paramInt2; j++) {
/* 230 */       if (paramArrayOfSrvRecord[j] != null) {
/* 231 */         i += paramArrayOfSrvRecord[j].weight;
/* 232 */         paramArrayOfSrvRecord[j].sum = i;
/*     */       }
/*     */     }
/* 235 */     String str = null;
/*     */ 
/* 239 */     int k = i == 0 ? 0 : random.nextInt(i + 1);
/* 240 */     for (int m = paramInt1; m <= paramInt2; m++) {
/* 241 */       if ((paramArrayOfSrvRecord[m] != null) && (paramArrayOfSrvRecord[m].sum >= k)) {
/* 242 */         str = paramArrayOfSrvRecord[m].hostport;
/* 243 */         paramArrayOfSrvRecord[m] = null;
/* 244 */         break;
/*     */       }
/*     */     }
/* 247 */     return str;
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
/* 270 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ");
/*     */ 
/* 273 */       if (localStringTokenizer.countTokens() == 4) {
/* 274 */         this.priority = Integer.parseInt(localStringTokenizer.nextToken());
/* 275 */         this.weight = Integer.parseInt(localStringTokenizer.nextToken());
/* 276 */         String str = localStringTokenizer.nextToken();
/* 277 */         this.hostport = (localStringTokenizer.nextToken() + ":" + str);
/*     */       } else {
/* 279 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */ 
/*     */     public int compareTo(Object paramObject)
/*     */     {
/* 288 */       SrvRecord localSrvRecord = (SrvRecord)paramObject;
/* 289 */       if (this.priority > localSrvRecord.priority)
/* 290 */         return 1;
/* 291 */       if (this.priority < localSrvRecord.priority)
/* 292 */         return -1;
/* 293 */       if ((this.weight == 0) && (localSrvRecord.weight != 0))
/* 294 */         return -1;
/* 295 */       if ((this.weight != 0) && (localSrvRecord.weight == 0)) {
/* 296 */         return 1;
/*     */       }
/* 298 */       return 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.ServiceLocator
 * JD-Core Version:    0.6.2
 */