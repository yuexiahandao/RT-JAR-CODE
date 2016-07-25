/*     */ package javax.management.loading;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class MLetParser
/*     */ {
/*     */   private int c;
/*  64 */   private static String tag = "mlet";
/*     */ 
/*     */   public void skipSpace(Reader paramReader)
/*     */     throws IOException
/*     */   {
/*  89 */     while ((this.c >= 0) && ((this.c == 32) || (this.c == 9) || (this.c == 10) || (this.c == 13)))
/*  90 */       this.c = paramReader.read();
/*     */   }
/*     */ 
/*     */   public String scanIdentifier(Reader paramReader)
/*     */     throws IOException
/*     */   {
/*  98 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 100 */     while (((this.c >= 97) && (this.c <= 122)) || ((this.c >= 65) && (this.c <= 90)) || ((this.c >= 48) && (this.c <= 57)) || (this.c == 95))
/*     */     {
/* 103 */       localStringBuilder.append((char)this.c);
/* 104 */       this.c = paramReader.read();
/*     */     }
/* 106 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public Map<String, String> scanTag(Reader paramReader)
/*     */     throws IOException
/*     */   {
/* 115 */     HashMap localHashMap = new HashMap();
/* 116 */     skipSpace(paramReader);
/* 117 */     while ((this.c >= 0) && (this.c != 62)) {
/* 118 */       if (this.c == 60)
/* 119 */         throw new IOException("Missing '>' in tag");
/* 120 */       String str1 = scanIdentifier(paramReader);
/* 121 */       String str2 = "";
/* 122 */       skipSpace(paramReader);
/* 123 */       if (this.c == 61) {
/* 124 */         int i = -1;
/* 125 */         this.c = paramReader.read();
/* 126 */         skipSpace(paramReader);
/* 127 */         if ((this.c == 39) || (this.c == 34)) {
/* 128 */           i = this.c;
/* 129 */           this.c = paramReader.read();
/*     */         }
/* 131 */         StringBuilder localStringBuilder = new StringBuilder();
/* 132 */         while ((this.c > 0) && (((i < 0) && (this.c != 32) && (this.c != 9) && (this.c != 10) && (this.c != 13) && (this.c != 62)) || ((i >= 0) && (this.c != i))))
/*     */         {
/* 136 */           localStringBuilder.append((char)this.c);
/* 137 */           this.c = paramReader.read();
/*     */         }
/* 139 */         if (this.c == i) {
/* 140 */           this.c = paramReader.read();
/*     */         }
/* 142 */         skipSpace(paramReader);
/* 143 */         str2 = localStringBuilder.toString();
/*     */       }
/* 145 */       localHashMap.put(str1.toLowerCase(), str2);
/* 146 */       skipSpace(paramReader);
/*     */     }
/* 148 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   public List<MLetContent> parse(URL paramURL)
/*     */     throws IOException
/*     */   {
/* 155 */     String str1 = "parse";
/*     */ 
/* 157 */     String str2 = "<arg type=... value=...> tag requires type parameter.";
/* 158 */     String str3 = "<arg type=... value=...> tag requires value parameter.";
/* 159 */     String str4 = "<arg> tag outside <mlet> ... </mlet>.";
/* 160 */     String str5 = "<mlet> tag requires either code or object parameter.";
/* 161 */     String str6 = "<mlet> tag requires archive parameter.";
/*     */ 
/* 165 */     URLConnection localURLConnection = paramURL.openConnection();
/* 166 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localURLConnection.getInputStream(), "UTF-8"));
/*     */ 
/* 172 */     paramURL = localURLConnection.getURL();
/*     */ 
/* 174 */     ArrayList localArrayList1 = new ArrayList();
/* 175 */     Map localMap1 = null;
/*     */ 
/* 177 */     ArrayList localArrayList2 = new ArrayList();
/* 178 */     ArrayList localArrayList3 = new ArrayList();
/*     */     while (true)
/*     */     {
/* 182 */       this.c = localBufferedReader.read();
/* 183 */       if (this.c == -1)
/*     */         break;
/* 185 */       if (this.c == 60) {
/* 186 */         this.c = localBufferedReader.read();
/*     */         String str7;
/* 187 */         if (this.c == 47) {
/* 188 */           this.c = localBufferedReader.read();
/* 189 */           str7 = scanIdentifier(localBufferedReader);
/* 190 */           if (this.c != 62)
/* 191 */             throw new IOException("Missing '>' in tag");
/* 192 */           if (str7.equalsIgnoreCase(tag)) {
/* 193 */             if (localMap1 != null) {
/* 194 */               localArrayList1.add(new MLetContent(paramURL, localMap1, localArrayList2, localArrayList3));
/*     */             }
/* 196 */             localMap1 = null;
/* 197 */             localArrayList2 = new ArrayList();
/* 198 */             localArrayList3 = new ArrayList();
/*     */           }
/*     */         } else {
/* 201 */           str7 = scanIdentifier(localBufferedReader);
/* 202 */           if (str7.equalsIgnoreCase("arg")) {
/* 203 */             Map localMap2 = scanTag(localBufferedReader);
/* 204 */             String str8 = (String)localMap2.get("type");
/* 205 */             if (str8 == null) {
/* 206 */               JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), str1, str2);
/*     */ 
/* 209 */               throw new IOException(str2);
/*     */             }
/* 211 */             if (localMap1 != null) {
/* 212 */               localArrayList2.add(str8);
/*     */             } else {
/* 214 */               JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), str1, str4);
/*     */ 
/* 217 */               throw new IOException(str4);
/*     */             }
/*     */ 
/* 220 */             String str9 = (String)localMap2.get("value");
/* 221 */             if (str9 == null) {
/* 222 */               JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), str1, str3);
/*     */ 
/* 225 */               throw new IOException(str3);
/*     */             }
/* 227 */             if (localMap1 != null) {
/* 228 */               localArrayList3.add(str9);
/*     */             } else {
/* 230 */               JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), str1, str4);
/*     */ 
/* 233 */               throw new IOException(str4);
/*     */             }
/*     */ 
/*     */           }
/* 237 */           else if (str7.equalsIgnoreCase(tag)) {
/* 238 */             localMap1 = scanTag(localBufferedReader);
/* 239 */             if ((localMap1.get("code") == null) && (localMap1.get("object") == null)) {
/* 240 */               JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), str1, str5);
/*     */ 
/* 243 */               throw new IOException(str5);
/*     */             }
/* 245 */             if (localMap1.get("archive") == null) {
/* 246 */               JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), str1, str6);
/*     */ 
/* 249 */               throw new IOException(str6);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 256 */     localBufferedReader.close();
/* 257 */     return localArrayList1;
/*     */   }
/*     */ 
/*     */   public List<MLetContent> parseURL(String paramString)
/*     */     throws IOException
/*     */   {
/*     */     URL localURL;
/* 267 */     if (paramString.indexOf(':') <= 1) {
/* 268 */       String str1 = System.getProperty("user.dir");
/*     */       String str2;
/* 270 */       if ((str1.charAt(0) == '/') || (str1.charAt(0) == File.separatorChar))
/*     */       {
/* 272 */         str2 = "file:";
/*     */       }
/* 274 */       else str2 = "file:/";
/*     */ 
/* 276 */       localURL = new URL(str2 + str1.replace(File.separatorChar, '/') + "/");
/*     */ 
/* 278 */       localURL = new URL(localURL, paramString);
/*     */     } else {
/* 280 */       localURL = new URL(paramString);
/*     */     }
/*     */ 
/* 284 */     return parse(localURL);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.loading.MLetParser
 * JD-Core Version:    0.6.2
 */