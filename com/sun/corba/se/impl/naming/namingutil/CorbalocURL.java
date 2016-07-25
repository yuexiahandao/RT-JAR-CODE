/*     */ package com.sun.corba.se.impl.naming.namingutil;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.NamingSystemException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class CorbalocURL extends INSURLBase
/*     */ {
/*  42 */   static NamingSystemException wrapper = NamingSystemException.get("naming.read");
/*     */ 
/*     */   public CorbalocURL(String paramString)
/*     */   {
/*  51 */     String str1 = paramString;
/*     */ 
/*  53 */     if (str1 != null)
/*     */     {
/*     */       try {
/*  56 */         str1 = Utility.cleanEscapes(str1);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*  60 */         badAddress(localException);
/*     */       }
/*  62 */       int i = str1.indexOf('/');
/*  63 */       if (i == -1)
/*     */       {
/*  65 */         i = str1.length();
/*     */       }
/*     */ 
/*  68 */       if (i == 0)
/*     */       {
/*  70 */         badAddress(null);
/*     */       }
/*     */ 
/*  74 */       StringTokenizer localStringTokenizer = new StringTokenizer(str1.substring(0, i), ",");
/*     */ 
/*  81 */       while (localStringTokenizer.hasMoreTokens()) {
/*  82 */         String str2 = localStringTokenizer.nextToken();
/*  83 */         IIOPEndpointInfo localIIOPEndpointInfo = null;
/*  84 */         if (str2.startsWith("iiop:")) {
/*  85 */           localIIOPEndpointInfo = handleIIOPColon(str2);
/*  86 */         } else if (str2.startsWith("rir:")) {
/*  87 */           handleRIRColon(str2);
/*  88 */           this.rirFlag = true;
/*  89 */         } else if (str2.startsWith(":")) {
/*  90 */           localIIOPEndpointInfo = handleColon(str2);
/*     */         }
/*     */         else
/*     */         {
/*  95 */           badAddress(null);
/*     */         }
/*  97 */         if (!this.rirFlag)
/*     */         {
/* 101 */           if (this.theEndpointInfo == null) {
/* 102 */             this.theEndpointInfo = new ArrayList();
/*     */           }
/* 104 */           this.theEndpointInfo.add(localIIOPEndpointInfo);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 109 */       if (str1.length() > i + 1)
/* 110 */         this.theKeyString = str1.substring(i + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void badAddress(Throwable paramThrowable)
/*     */   {
/* 122 */     throw wrapper.insBadAddress(paramThrowable);
/*     */   }
/*     */ 
/*     */   private IIOPEndpointInfo handleIIOPColon(String paramString)
/*     */   {
/* 132 */     paramString = paramString.substring(4);
/* 133 */     return handleColon(paramString);
/*     */   }
/*     */ 
/*     */   private IIOPEndpointInfo handleColon(String paramString)
/*     */   {
/* 143 */     paramString = paramString.substring(1);
/* 144 */     String str1 = paramString;
/*     */ 
/* 146 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "@");
/* 147 */     IIOPEndpointInfo localIIOPEndpointInfo = new IIOPEndpointInfo();
/* 148 */     int i = localStringTokenizer.countTokens();
/*     */ 
/* 155 */     if ((i == 0) || (i > 2))
/*     */     {
/* 158 */       badAddress(null);
/*     */     }
/* 160 */     if (i == 2)
/*     */     {
/* 162 */       String str2 = localStringTokenizer.nextToken();
/* 163 */       int k = str2.indexOf('.');
/*     */ 
/* 166 */       if (k == -1)
/* 167 */         badAddress(null);
/*     */       try
/*     */       {
/* 170 */         localIIOPEndpointInfo.setVersion(Integer.parseInt(str2.substring(0, k)), Integer.parseInt(str2.substring(k + 1)));
/*     */ 
/* 173 */         str1 = localStringTokenizer.nextToken();
/*     */       } catch (Throwable localThrowable2) {
/* 175 */         badAddress(localThrowable2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 182 */       int j = str1.indexOf('[');
/* 183 */       if (j != -1)
/*     */       {
/* 187 */         String str3 = getIPV6Port(str1);
/* 188 */         if (str3 != null) {
/* 189 */           localIIOPEndpointInfo.setPort(Integer.parseInt(str3));
/*     */         }
/* 191 */         localIIOPEndpointInfo.setHost(getIPV6Host(str1));
/* 192 */         return localIIOPEndpointInfo;
/*     */       }
/* 194 */       localStringTokenizer = new StringTokenizer(str1, ":");
/*     */ 
/* 200 */       if (localStringTokenizer.countTokens() == 2)
/*     */       {
/* 202 */         localIIOPEndpointInfo.setHost(localStringTokenizer.nextToken());
/* 203 */         localIIOPEndpointInfo.setPort(Integer.parseInt(localStringTokenizer.nextToken()));
/*     */       }
/* 206 */       else if ((str1 != null) && (str1.length() != 0))
/*     */       {
/* 212 */         localIIOPEndpointInfo.setHost(str1);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 222 */       badAddress(localThrowable1);
/*     */     }
/* 224 */     Utility.validateGIOPVersion(localIIOPEndpointInfo);
/* 225 */     return localIIOPEndpointInfo;
/*     */   }
/*     */ 
/*     */   private void handleRIRColon(String paramString)
/*     */   {
/* 233 */     if (paramString.length() != 4)
/* 234 */       badAddress(null);
/*     */   }
/*     */ 
/*     */   private String getIPV6Port(String paramString)
/*     */   {
/* 246 */     int i = paramString.indexOf(']');
/*     */ 
/* 250 */     if (i + 1 != paramString.length()) {
/* 251 */       if (paramString.charAt(i + 1) != ':') {
/* 252 */         throw new RuntimeException("Host and Port is not separated by ':'");
/*     */       }
/*     */ 
/* 258 */       return paramString.substring(i + 2);
/*     */     }
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */   private String getIPV6Host(String paramString)
/*     */   {
/* 274 */     int i = paramString.indexOf(']');
/*     */ 
/* 276 */     String str = paramString.substring(1, i);
/* 277 */     return str;
/*     */   }
/*     */ 
/*     */   public boolean isCorbanameURL()
/*     */   {
/* 284 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.namingutil.CorbalocURL
 * JD-Core Version:    0.6.2
 */