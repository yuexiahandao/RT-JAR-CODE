/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class MetaIndex
/*     */ {
/*     */   private static volatile Map<File, MetaIndex> jarMap;
/*     */   private String[] contents;
/*     */   private boolean isClassOnlyJar;
/*     */ 
/*     */   public static MetaIndex forJar(File paramFile)
/*     */   {
/* 148 */     return (MetaIndex)getJarMap().get(paramFile);
/*     */   }
/*     */ 
/*     */   public static synchronized void registerDirectory(File paramFile)
/*     */   {
/* 163 */     File localFile = new File(paramFile, "meta-index");
/* 164 */     if (localFile.exists())
/*     */       try {
/* 166 */         BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile));
/* 167 */         String str1 = null;
/* 168 */         String str2 = null;
/* 169 */         boolean bool = false;
/* 170 */         ArrayList localArrayList = new ArrayList();
/* 171 */         Map localMap = getJarMap();
/*     */ 
/* 174 */         paramFile = paramFile.getCanonicalFile();
/*     */ 
/* 178 */         str1 = localBufferedReader.readLine();
/* 179 */         if ((str1 == null) || (!str1.equals("% VERSION 2")))
/*     */         {
/* 181 */           localBufferedReader.close();
/* 182 */           return;
/*     */         }
/* 184 */         while ((str1 = localBufferedReader.readLine()) != null) {
/* 185 */           switch (str1.charAt(0))
/*     */           {
/*     */           case '!':
/*     */           case '#':
/*     */           case '@':
/* 190 */             if ((str2 != null) && (localArrayList.size() > 0)) {
/* 191 */               localMap.put(new File(paramFile, str2), new MetaIndex(localArrayList, bool));
/*     */ 
/* 195 */               localArrayList.clear();
/*     */             }
/*     */ 
/* 198 */             str2 = str1.substring(2);
/* 199 */             if (str1.charAt(0) == '!')
/* 200 */               bool = true;
/* 201 */             else if (bool)
/* 202 */               bool = false; break;
/*     */           case '%':
/* 208 */             break;
/*     */           default:
/* 210 */             localArrayList.add(str1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 215 */         if ((str2 != null) && (localArrayList.size() > 0)) {
/* 216 */           localMap.put(new File(paramFile, str2), new MetaIndex(localArrayList, bool));
/*     */         }
/*     */ 
/* 220 */         localBufferedReader.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean mayContain(String paramString)
/*     */   {
/* 237 */     if ((this.isClassOnlyJar) && (!paramString.endsWith(".class"))) {
/* 238 */       return false;
/*     */     }
/*     */ 
/* 241 */     String[] arrayOfString = this.contents;
/* 242 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 243 */       if (paramString.startsWith(arrayOfString[i])) {
/* 244 */         return true;
/*     */       }
/*     */     }
/* 247 */     return false;
/*     */   }
/*     */ 
/*     */   private MetaIndex(List<String> paramList, boolean paramBoolean)
/*     */     throws IllegalArgumentException
/*     */   {
/* 256 */     if (paramList == null) {
/* 257 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 260 */     this.contents = ((String[])paramList.toArray(new String[0]));
/* 261 */     this.isClassOnlyJar = paramBoolean;
/*     */   }
/*     */ 
/*     */   private static Map<File, MetaIndex> getJarMap() {
/* 265 */     if (jarMap == null) {
/* 266 */       synchronized (MetaIndex.class) {
/* 267 */         if (jarMap == null) {
/* 268 */           jarMap = new HashMap();
/*     */         }
/*     */       }
/*     */     }
/* 272 */     assert (jarMap != null);
/* 273 */     return jarMap;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.MetaIndex
 * JD-Core Version:    0.6.2
 */