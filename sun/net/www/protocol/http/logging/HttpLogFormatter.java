/*     */ package sun.net.www.protocol.http.logging;
/*     */ 
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.SimpleFormatter;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class HttpLogFormatter extends SimpleFormatter
/*     */ {
/*  39 */   private static volatile Pattern pattern = null;
/*     */ 
/*  41 */   private static volatile Pattern cpattern = null;
/*     */ 
/*     */   public HttpLogFormatter() {
/*  44 */     if (pattern == null) {
/*  45 */       pattern = Pattern.compile("\\{[^\\}]*\\}");
/*  46 */       cpattern = Pattern.compile("[^,\\] ]{2,}");
/*     */     }
/*     */   }
/*     */ 
/*     */   public String format(LogRecord paramLogRecord)
/*     */   {
/*  52 */     String str1 = paramLogRecord.getSourceClassName();
/*  53 */     if ((str1 == null) || ((!str1.startsWith("sun.net.www.protocol.http")) && (!str1.startsWith("sun.net.www.http"))))
/*     */     {
/*  56 */       return super.format(paramLogRecord);
/*     */     }
/*  58 */     String str2 = paramLogRecord.getMessage();
/*  59 */     StringBuilder localStringBuilder = new StringBuilder("HTTP: ");
/*     */     Object localObject1;
/*     */     int j;
/*     */     Object localObject2;
/*  60 */     if (str2.startsWith("sun.net.www.MessageHeader@"))
/*     */     {
/*  64 */       localObject1 = pattern.matcher(str2);
/*  65 */       while (((Matcher)localObject1).find()) {
/*  66 */         int i = ((Matcher)localObject1).start();
/*  67 */         j = ((Matcher)localObject1).end();
/*  68 */         localObject2 = str2.substring(i + 1, j - 1);
/*  69 */         if (((String)localObject2).startsWith("null: ")) {
/*  70 */           localObject2 = ((String)localObject2).substring(6);
/*     */         }
/*  72 */         if (((String)localObject2).endsWith(": null")) {
/*  73 */           localObject2 = ((String)localObject2).substring(0, ((String)localObject2).length() - 6);
/*     */         }
/*  75 */         localStringBuilder.append("\t").append((String)localObject2).append("\n");
/*     */       }
/*  77 */     } else if (str2.startsWith("Cookies retrieved: {"))
/*     */     {
/*  79 */       localObject1 = str2.substring(20);
/*  80 */       localStringBuilder.append("Cookies from handler:\n");
/*  81 */       while (((String)localObject1).length() >= 7)
/*     */       {
/*     */         String str3;
/*     */         int k;
/*     */         int m;
/*     */         String str4;
/*  82 */         if (((String)localObject1).startsWith("Cookie=[")) {
/*  83 */           str3 = ((String)localObject1).substring(8);
/*  84 */           j = str3.indexOf("Cookie2=[");
/*  85 */           if (j > 0) {
/*  86 */             str3 = str3.substring(0, j - 1);
/*  87 */             localObject1 = str3.substring(j);
/*     */           } else {
/*  89 */             localObject1 = "";
/*     */           }
/*  91 */           if (str3.length() >= 4)
/*     */           {
/*  94 */             localObject2 = cpattern.matcher(str3);
/*  95 */             while (((Matcher)localObject2).find()) {
/*  96 */               k = ((Matcher)localObject2).start();
/*  97 */               m = ((Matcher)localObject2).end();
/*  98 */               if (k >= 0) {
/*  99 */                 str4 = str3.substring(k + 1, m > 0 ? m - 1 : str3.length() - 1);
/* 100 */                 localStringBuilder.append("\t").append(str4).append("\n");
/*     */               }
/*     */             }
/*     */           }
/* 104 */         } else if (((String)localObject1).startsWith("Cookie2=[")) {
/* 105 */           str3 = ((String)localObject1).substring(9);
/* 106 */           j = str3.indexOf("Cookie=[");
/* 107 */           if (j > 0) {
/* 108 */             str3 = str3.substring(0, j - 1);
/* 109 */             localObject1 = str3.substring(j);
/*     */           } else {
/* 111 */             localObject1 = "";
/*     */           }
/* 113 */           localObject2 = cpattern.matcher(str3);
/* 114 */           while (((Matcher)localObject2).find()) {
/* 115 */             k = ((Matcher)localObject2).start();
/* 116 */             m = ((Matcher)localObject2).end();
/* 117 */             if (k >= 0) {
/* 118 */               str4 = str3.substring(k + 1, m > 0 ? m - 1 : str3.length() - 1);
/* 119 */               localStringBuilder.append("\t").append(str4).append("\n");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 126 */       localStringBuilder.append(str2).append("\n");
/*     */     }
/* 128 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.logging.HttpLogFormatter
 * JD-Core Version:    0.6.2
 */