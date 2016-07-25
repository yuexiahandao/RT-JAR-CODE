/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ public class JarIndex
/*     */ {
/*     */   private HashMap indexMap;
/*     */   private HashMap jarMap;
/*     */   private String[] jarFiles;
/*     */   public static final String INDEX_NAME = "META-INF/INDEX.LIST";
/*  74 */   private static final boolean metaInfFilenames = "true".equals(System.getProperty("sun.misc.JarIndex.metaInfFilenames"));
/*     */ 
/*     */   public JarIndex()
/*     */   {
/*  81 */     this.indexMap = new HashMap();
/*  82 */     this.jarMap = new HashMap();
/*     */   }
/*     */ 
/*     */   public JarIndex(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  91 */     this();
/*  92 */     read(paramInputStream);
/*     */   }
/*     */ 
/*     */   public JarIndex(String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/* 101 */     this();
/* 102 */     this.jarFiles = paramArrayOfString;
/* 103 */     parseJars(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static JarIndex getJarIndex(JarFile paramJarFile)
/*     */     throws IOException
/*     */   {
/* 116 */     return getJarIndex(paramJarFile, null);
/*     */   }
/*     */ 
/*     */   public static JarIndex getJarIndex(JarFile paramJarFile, MetaIndex paramMetaIndex)
/*     */     throws IOException
/*     */   {
/* 126 */     JarIndex localJarIndex = null;
/*     */ 
/* 130 */     if ((paramMetaIndex != null) && (!paramMetaIndex.mayContain("META-INF/INDEX.LIST")))
/*     */     {
/* 132 */       return null;
/*     */     }
/* 134 */     JarEntry localJarEntry = paramJarFile.getJarEntry("META-INF/INDEX.LIST");
/*     */ 
/* 136 */     if (localJarEntry != null) {
/* 137 */       localJarIndex = new JarIndex(paramJarFile.getInputStream(localJarEntry));
/*     */     }
/* 139 */     return localJarIndex;
/*     */   }
/*     */ 
/*     */   public String[] getJarFiles()
/*     */   {
/* 146 */     return this.jarFiles;
/*     */   }
/*     */ 
/*     */   private void addToList(String paramString1, String paramString2, HashMap paramHashMap)
/*     */   {
/* 154 */     LinkedList localLinkedList = (LinkedList)paramHashMap.get(paramString1);
/* 155 */     if (localLinkedList == null) {
/* 156 */       localLinkedList = new LinkedList();
/* 157 */       localLinkedList.add(paramString2);
/* 158 */       paramHashMap.put(paramString1, localLinkedList);
/* 159 */     } else if (!localLinkedList.contains(paramString2)) {
/* 160 */       localLinkedList.add(paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public LinkedList get(String paramString)
/*     */   {
/* 170 */     LinkedList localLinkedList = null;
/* 171 */     if ((localLinkedList = (LinkedList)this.indexMap.get(paramString)) == null)
/*     */     {
/*     */       int i;
/* 174 */       if ((i = paramString.lastIndexOf("/")) != -1) {
/* 175 */         localLinkedList = (LinkedList)this.indexMap.get(paramString.substring(0, i));
/*     */       }
/*     */     }
/* 178 */     return localLinkedList;
/*     */   }
/*     */ 
/*     */   public void add(String paramString1, String paramString2)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 197 */     if ((i = paramString1.lastIndexOf("/")) != -1)
/* 198 */       str = paramString1.substring(0, i);
/*     */     else {
/* 200 */       str = paramString1;
/*     */     }
/*     */ 
/* 204 */     addToList(str, paramString2, this.indexMap);
/*     */ 
/* 207 */     addToList(paramString2, str, this.jarMap);
/*     */   }
/*     */ 
/*     */   private void addExplicit(String paramString1, String paramString2)
/*     */   {
/* 216 */     addToList(paramString1, paramString2, this.indexMap);
/*     */ 
/* 219 */     addToList(paramString2, paramString1, this.jarMap);
/*     */   }
/*     */ 
/*     */   private void parseJars(String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/* 227 */     if (paramArrayOfString == null) {
/* 228 */       return;
/*     */     }
/*     */ 
/* 231 */     String str1 = null;
/*     */ 
/* 233 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 234 */       str1 = paramArrayOfString[i];
/* 235 */       ZipFile localZipFile = new ZipFile(str1.replace('/', File.separatorChar));
/*     */ 
/* 238 */       Enumeration localEnumeration = localZipFile.entries();
/* 239 */       while (localEnumeration.hasMoreElements()) {
/* 240 */         ZipEntry localZipEntry = (ZipEntry)localEnumeration.nextElement();
/* 241 */         String str2 = localZipEntry.getName();
/*     */ 
/* 245 */         if ((!str2.equals("META-INF/")) && (!str2.equals("META-INF/INDEX.LIST")) && (!str2.equals("META-INF/MANIFEST.MF")))
/*     */         {
/* 250 */           if (!metaInfFilenames) {
/* 251 */             add(str2, str1);
/*     */           }
/* 253 */           else if (!str2.startsWith("META-INF/"))
/* 254 */             add(str2, str1);
/* 255 */           else if (!localZipEntry.isDirectory())
/*     */           {
/* 260 */             addExplicit(str2, str1);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 265 */       localZipFile.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 276 */     BufferedWriter localBufferedWriter = new BufferedWriter(new OutputStreamWriter(paramOutputStream, "UTF8"));
/*     */ 
/* 278 */     localBufferedWriter.write("JarIndex-Version: 1.0\n\n");
/*     */ 
/* 280 */     if (this.jarFiles != null) {
/* 281 */       for (int i = 0; i < this.jarFiles.length; i++)
/*     */       {
/* 283 */         String str = this.jarFiles[i];
/* 284 */         localBufferedWriter.write(str + "\n");
/* 285 */         LinkedList localLinkedList = (LinkedList)this.jarMap.get(str);
/* 286 */         if (localLinkedList != null) {
/* 287 */           Iterator localIterator = localLinkedList.iterator();
/* 288 */           while (localIterator.hasNext()) {
/* 289 */             localBufferedWriter.write((String)localIterator.next() + "\n");
/*     */           }
/*     */         }
/* 292 */         localBufferedWriter.write("\n");
/*     */       }
/* 294 */       localBufferedWriter.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void read(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 306 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF8"));
/*     */ 
/* 308 */     String str1 = null;
/* 309 */     String str2 = null;
/*     */ 
/* 312 */     Vector localVector = new Vector();
/*     */ 
/* 315 */     while (((str1 = localBufferedReader.readLine()) != null) && (!str1.endsWith(".jar")));
/* 317 */     for (; str1 != null; str1 = localBufferedReader.readLine()) {
/* 318 */       if (str1.length() != 0)
/*     */       {
/* 321 */         if (str1.endsWith(".jar")) {
/* 322 */           str2 = str1;
/* 323 */           localVector.add(str2);
/*     */         } else {
/* 325 */           String str3 = str1;
/* 326 */           addToList(str3, str2, this.indexMap);
/* 327 */           addToList(str2, str3, this.jarMap);
/*     */         }
/*     */       }
/*     */     }
/* 331 */     this.jarFiles = ((String[])localVector.toArray(new String[localVector.size()]));
/*     */   }
/*     */ 
/*     */   public void merge(JarIndex paramJarIndex, String paramString)
/*     */   {
/* 345 */     Iterator localIterator1 = this.indexMap.entrySet().iterator();
/* 346 */     while (localIterator1.hasNext()) {
/* 347 */       Map.Entry localEntry = (Map.Entry)localIterator1.next();
/* 348 */       String str1 = (String)localEntry.getKey();
/* 349 */       LinkedList localLinkedList = (LinkedList)localEntry.getValue();
/* 350 */       Iterator localIterator2 = localLinkedList.iterator();
/* 351 */       while (localIterator2.hasNext()) {
/* 352 */         String str2 = (String)localIterator2.next();
/* 353 */         if (paramString != null) {
/* 354 */           str2 = paramString.concat(str2);
/*     */         }
/* 356 */         paramJarIndex.add(str1, str2);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JarIndex
 * JD-Core Version:    0.6.2
 */