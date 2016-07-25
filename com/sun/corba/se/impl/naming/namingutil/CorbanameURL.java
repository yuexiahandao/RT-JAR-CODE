/*     */ package com.sun.corba.se.impl.naming.namingutil;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.NamingSystemException;
/*     */ import java.util.ArrayList;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ 
/*     */ public class CorbanameURL extends INSURLBase
/*     */ {
/*  40 */   private static NamingSystemException wrapper = NamingSystemException.get("naming");
/*     */ 
/*     */   public CorbanameURL(String paramString)
/*     */   {
/*  49 */     String str1 = paramString;
/*     */     try
/*     */     {
/*  53 */       str1 = Utility.cleanEscapes(str1);
/*     */     } catch (Exception localException1) {
/*  55 */       badAddress(localException1);
/*     */     }
/*     */ 
/*  58 */     int i = str1.indexOf('#');
/*  59 */     String str2 = null;
/*  60 */     if (i != -1)
/*     */     {
/*  63 */       str2 = "corbaloc:" + str1.substring(0, i) + "/";
/*     */     }
/*     */     else
/*     */     {
/*  68 */       str2 = "corbaloc:" + str1.substring(0, str1.length());
/*     */ 
/*  71 */       if (str2.endsWith("/") != true) {
/*  72 */         str2 = str2 + "/";
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  78 */       INSURL localINSURL = INSURLHandler.getINSURLHandler().parseURL(str2);
/*     */ 
/*  80 */       copyINSURL(localINSURL);
/*     */ 
/*  85 */       if ((i > -1) && (i < paramString.length() - 1))
/*     */       {
/*  88 */         int j = i + 1;
/*  89 */         String str3 = str1.substring(j);
/*  90 */         this.theStringifiedName = str3;
/*     */       }
/*     */     } catch (Exception localException2) {
/*  93 */       badAddress(localException2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void badAddress(Throwable paramThrowable)
/*     */     throws BAD_PARAM
/*     */   {
/* 103 */     throw wrapper.insBadAddress(paramThrowable);
/*     */   }
/*     */ 
/*     */   private void copyINSURL(INSURL paramINSURL)
/*     */   {
/* 111 */     this.rirFlag = paramINSURL.getRIRFlag();
/* 112 */     this.theEndpointInfo = ((ArrayList)paramINSURL.getEndpointInfo());
/* 113 */     this.theKeyString = paramINSURL.getKeyString();
/* 114 */     this.theStringifiedName = paramINSURL.getStringifiedName();
/*     */   }
/*     */ 
/*     */   public boolean isCorbanameURL() {
/* 118 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.namingutil.CorbanameURL
 * JD-Core Version:    0.6.2
 */