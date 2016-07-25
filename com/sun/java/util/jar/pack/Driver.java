/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.jar.Pack200;
/*     */ import java.util.jar.Pack200.Packer;
/*     */ import java.util.jar.Pack200.Unpacker;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ 
/*     */ class Driver
/*     */ {
/*  60 */   private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("com.sun.java.util.jar.pack.DriverResource");
/*     */   private static final String PACK200_OPTION_MAP = "";
/*     */   private static final String UNPACK200_OPTION_MAP = "--deflate-hint=       *> = \n  -H +>  @--deflate-hint=       = \n--verbose                $ \n  -v +>- @--verbose             $ \n--quiet        !--verbose  \n  -q +>- !--verbose               \n--remove-pack-file       $ \n  -r +>- @--remove-pack-file    $ \n--log-file=           *> = \n  -l +>  @--log-file=           = \n--config-file=        *> . \n  -f +>  @--config-file=        . \n--           . \n-   +?    >- . \n--version                . \n  -V +>  @--version             . \n--help               . \n  -? +> @--help . \n  -h +> @--help . \n";
/* 488 */   private static final String[] PACK200_PROPERTY_TO_OPTION = { "pack.segment.limit", "--segment-limit=", "pack.keep.file.order", "--no-keep-file-order", "pack.effort", "--effort=", "pack.deflate.hint", "--deflate-hint=", "pack.modification.time", "--modification-time=", "pack.pass.file.", "--pass-file=", "pack.unknown.attribute", "--unknown-attribute=", "pack.class.attribute.", "--class-attribute=", "pack.field.attribute.", "--field-attribute=", "pack.method.attribute.", "--method-attribute=", "pack.code.attribute.", "--code-attribute=", "com.sun.java.util.jar.pack.verbose", "--verbose", "com.sun.java.util.jar.pack.strip.debug", "--strip-debug" };
/*     */ 
/* 505 */   private static final String[] UNPACK200_PROPERTY_TO_OPTION = { "unpack.deflate.hint", "--deflate-hint=", "com.sun.java.util.jar.pack.verbose", "--verbose", "com.sun.java.util.jar.pack.unpack.remove.packfile", "--remove-pack-file" };
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/*  64 */     ArrayList localArrayList = new ArrayList(Arrays.asList(paramArrayOfString));
/*     */ 
/*  66 */     boolean bool1 = true;
/*  67 */     int i = 0;
/*  68 */     int j = 0;
/*  69 */     int k = 1;
/*  70 */     Object localObject1 = null;
/*  71 */     String str1 = "com.sun.java.util.jar.pack.verbose";
/*     */ 
/*  75 */     Object localObject2 = localArrayList.isEmpty() ? "" : (String)localArrayList.get(0);
/*  76 */     Object localObject3 = localObject2; int m = -1; switch (((String)localObject3).hashCode()) { case 1333303225:
/*  76 */       if (((String)localObject3).equals("--pack")) m = 0; break;
/*     */     case 1559677394:
/*  76 */       if (((String)localObject3).equals("--unpack")) m = 1; break; } switch (m) {
/*     */     case 0:
/*  78 */       localArrayList.remove(0);
/*  79 */       break;
/*     */     case 1:
/*  81 */       localArrayList.remove(0);
/*  82 */       bool1 = false;
/*  83 */       i = 1;
/*     */     }
/*     */ 
/*  89 */     localObject2 = new HashMap();
/*  90 */     ((Map)localObject2).put(str1, System.getProperty(str1));
/*     */     String[] arrayOfString1;
/*  94 */     if (bool1) {
/*  95 */       localObject3 = "";
/*  96 */       arrayOfString1 = PACK200_PROPERTY_TO_OPTION;
/*     */     } else {
/*  98 */       localObject3 = "--deflate-hint=       *> = \n  -H +>  @--deflate-hint=       = \n--verbose                $ \n  -v +>- @--verbose             $ \n--quiet        !--verbose  \n  -q +>- !--verbose               \n--remove-pack-file       $ \n  -r +>- @--remove-pack-file    $ \n--log-file=           *> = \n  -l +>  @--log-file=           = \n--config-file=        *> . \n  -f +>  @--config-file=        . \n--           . \n-   +?    >- . \n--version                . \n  -V +>  @--version             . \n--help               . \n  -? +> @--help . \n  -h +> @--help . \n";
/*  99 */       arrayOfString1 = UNPACK200_PROPERTY_TO_OPTION;
/* 103 */     }
/*     */ HashMap localHashMap = new HashMap();
/*     */     Object localObject11;
/*     */     Object localObject12;
/*     */     try { while (true) { String str2 = parseCommandOptions(localArrayList, (String)localObject3, localHashMap);
/*     */ 
/* 109 */         localObject5 = localHashMap.keySet().iterator();
/*     */         Object localObject8;
/* 110 */         while (((Iterator)localObject5).hasNext()) {
/* 111 */           localObject6 = (String)((Iterator)localObject5).next();
/* 112 */           localObject7 = null;
/* 113 */           for (int i1 = 0; i1 < arrayOfString1.length; i1 += 2) {
/* 114 */             if (((String)localObject6).equals(arrayOfString1[(1 + i1)])) {
/* 115 */               localObject7 = arrayOfString1[(0 + i1)];
/* 116 */               break;
/*     */             }
/*     */           }
/* 119 */           if (localObject7 != null) {
/* 120 */             localObject8 = (String)localHashMap.get(localObject6);
/* 121 */             ((Iterator)localObject5).remove();
/* 122 */             if (!((String)localObject7).endsWith("."))
/*     */             {
/* 124 */               if ((!((String)localObject6).equals("--verbose")) && (!((String)localObject6).endsWith("=")))
/*     */               {
/* 127 */                 int i3 = localObject8 != null ? 1 : 0;
/* 128 */                 if (((String)localObject6).startsWith("--no-"))
/* 129 */                   i3 = i3 == 0 ? 1 : 0;
/* 130 */                 localObject8 = i3 != 0 ? "true" : "false";
/*     */               }
/* 132 */               ((Map)localObject2).put(localObject7, localObject8);
/* 133 */             } else if (((String)localObject7).contains(".attribute.")) {
/* 134 */               for (String str5 : ((String)localObject8).split("")) {
/* 135 */                 localObject11 = str5.split("=", 2);
/* 136 */                 ((Map)localObject2).put((String)localObject7 + localObject11[0], localObject11[1]);
/*     */               }
/*     */             }
/*     */             else {
/* 140 */               int i4 = 1;
/* 141 */               for (localObject11 : ((String)localObject8).split(""))
/*     */               {
/*     */                 do
/* 144 */                   localObject12 = (String)localObject7 + "cli." + i4++;
/* 145 */                 while (((Map)localObject2).containsKey(localObject12));
/* 146 */                 ((Map)localObject2).put(localObject12, localObject11);
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 153 */         if ("--config-file=".equals(str2)) {
/* 154 */           localObject5 = (String)localArrayList.remove(0);
/* 155 */           localObject6 = new Properties();
/* 156 */           localObject7 = new FileInputStream((String)localObject5); localObject8 = null;
/*     */           try { ((Properties)localObject6).load((InputStream)localObject7); }
/*     */           catch (Throwable localThrowable2)
/*     */           {
/* 156 */             localObject8 = localThrowable2; throw localThrowable2;
/*     */           } finally {
/* 158 */             if (localObject7 != null) if (localObject8 != null) try { ((InputStream)localObject7).close(); } catch (Throwable localThrowable3) { ((Throwable)localObject8).addSuppressed(localThrowable3); } else ((InputStream)localObject7).close(); 
/*     */           }
/* 159 */           if (((Map)localObject2).get(str1) != null)
/* 160 */             ((Properties)localObject6).list(System.out);
/* 161 */           for (localObject7 = ((Properties)localObject6).entrySet().iterator(); ((Iterator)localObject7).hasNext(); ) { localObject8 = (Map.Entry)((Iterator)localObject7).next();
/* 162 */             ((Map)localObject2).put((String)((Map.Entry)localObject8).getKey(), (String)((Map.Entry)localObject8).getValue()); }
/*     */         } else {
/* 164 */           if ("--version".equals(str2)) {
/* 165 */             System.out.println(MessageFormat.format(RESOURCE.getString("VERSION"), new Object[] { Driver.class.getName(), "1.31, 07/05/05" }));
/* 166 */             return;
/* 167 */           }if (!"--help".equals(str2)) break;
/* 168 */           printUsage(bool1, true, System.out);
/* 169 */           System.exit(1);
/* 170 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 176 */       System.err.println(MessageFormat.format(RESOURCE.getString("BAD_ARGUMENT"), new Object[] { localIllegalArgumentException }));
/* 177 */       printUsage(bool1, false, System.err);
/* 178 */       System.exit(2);
/* 179 */       return;
/*     */     }
/*     */ 
/* 183 */     for (Object localObject4 = localHashMap.keySet().iterator(); ((Iterator)localObject4).hasNext(); ) { localObject5 = (String)((Iterator)localObject4).next();
/* 184 */       localObject6 = (String)localHashMap.get(localObject5);
/* 185 */       localObject7 = localObject5; int i2 = -1; switch (((String)localObject7).hashCode()) { case 1465478252:
/* 185 */         if (((String)localObject7).equals("--repack")) i2 = 0; break;
/*     */       case -845245370:
/* 185 */         if (((String)localObject7).equals("--no-gzip")) i2 = 1; break;
/*     */       case 1339571416:
/* 185 */         if (((String)localObject7).equals("--log-file=")) i2 = 2; break; } switch (i2) {
/*     */       case 0:
/* 187 */         j = 1;
/* 188 */         break;
/*     */       case 1:
/* 190 */         k = localObject6 == null ? 1 : 0;
/* 191 */         break;
/*     */       case 2:
/* 193 */         localObject1 = localObject6;
/* 194 */         break;
/*     */       default:
/* 196 */         throw new InternalError(MessageFormat.format(RESOURCE.getString("BAD_OPTION"), new Object[] { localObject5, localHashMap.get(localObject5) }));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 202 */     if ((localObject1 != null) && (!localObject1.equals(""))) {
/* 203 */       if (localObject1.equals("-")) {
/* 204 */         System.setErr(System.out);
/*     */       } else {
/* 206 */         localObject4 = new FileOutputStream(localObject1);
/*     */ 
/* 208 */         System.setErr(new PrintStream((OutputStream)localObject4));
/*     */       }
/*     */     }
/*     */ 
/* 212 */     int n = ((Map)localObject2).get(str1) != null ? 1 : 0;
/*     */ 
/* 214 */     Object localObject5 = "";
/* 215 */     if (!localArrayList.isEmpty()) {
/* 216 */       localObject5 = (String)localArrayList.remove(0);
/*     */     }
/* 218 */     Object localObject6 = "";
/* 219 */     if (!localArrayList.isEmpty()) {
/* 220 */       localObject6 = (String)localArrayList.remove(0);
/*     */     }
/* 222 */     Object localObject7 = "";
/* 223 */     String str3 = "";
/* 224 */     String str4 = "";
/* 225 */     if (j != 0)
/*     */     {
/* 229 */       if ((((String)localObject5).toLowerCase().endsWith(".pack")) || (((String)localObject5).toLowerCase().endsWith(".pac")) || (((String)localObject5).toLowerCase().endsWith(".gz")))
/*     */       {
/* 232 */         System.err.println(MessageFormat.format(RESOURCE.getString("BAD_REPACK_OUTPUT"), new Object[] { localObject5 }));
/*     */ 
/* 235 */         printUsage(bool1, false, System.err);
/* 236 */         System.exit(2);
/*     */       }
/* 238 */       localObject7 = localObject5;
/*     */ 
/* 240 */       if (((String)localObject6).equals(""))
/*     */       {
/* 243 */         localObject6 = localObject7;
/*     */       }
/* 245 */       str4 = createTempFile((String)localObject7, ".pack").getPath();
/* 246 */       localObject5 = str4;
/* 247 */       k = 0;
/*     */     }
/*     */ 
/* 250 */     if ((!localArrayList.isEmpty()) || ((!((String)localObject6).toLowerCase().endsWith(".jar")) && (!((String)localObject6).toLowerCase().endsWith(".zip")) && ((!((String)localObject6).equals("-")) || (bool1))))
/*     */     {
/* 256 */       printUsage(bool1, false, System.err);
/* 257 */       System.exit(2);
/* 258 */       return;
/*     */     }
/*     */ 
/* 261 */     if (j != 0)
/* 262 */       bool1 = i = 1;
/* 263 */     else if (bool1) {
/* 264 */       i = 0;
/*     */     }
/* 266 */     ??? = Pack200.newPacker();
/* 267 */     Pack200.Unpacker localUnpacker = Pack200.newUnpacker();
/*     */ 
/* 269 */     ((Pack200.Packer)???).properties().putAll((Map)localObject2);
/* 270 */     localUnpacker.properties().putAll((Map)localObject2);
/*     */     Object localObject10;
/* 271 */     if ((j != 0) && (((String)localObject7).equals(localObject6))) {
/* 272 */       localObject10 = getZipComment((String)localObject6);
/* 273 */       if ((n != 0) && (((String)localObject10).length() > 0))
/* 274 */         System.out.println(MessageFormat.format(RESOURCE.getString("DETECTED_ZIP_COMMENT"), new Object[] { localObject10 }));
/* 275 */       if (((String)localObject10).indexOf("PACK200") >= 0) {
/* 276 */         System.out.println(MessageFormat.format(RESOURCE.getString("SKIP_FOR_REPACKED"), new Object[] { localObject6 }));
/* 277 */         bool1 = false;
/* 278 */         i = 0;
/* 279 */         j = 0;
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 285 */       if (bool1)
/*     */       {
/* 287 */         localObject10 = new JarFile(new File((String)localObject6));
/*     */ 
/* 290 */         if (((String)localObject5).equals("-")) {
/* 291 */           localObject11 = System.out;
/*     */ 
/* 293 */           System.setOut(System.err);
/* 294 */         } else if (k != 0) {
/* 295 */           if (!((String)localObject5).endsWith(".gz")) {
/* 296 */             System.err.println(MessageFormat.format(RESOURCE.getString("WRITE_PACK_FILE"), new Object[] { localObject5 }));
/* 297 */             printUsage(bool1, false, System.err);
/* 298 */             System.exit(2);
/*     */           }
/* 300 */           localObject11 = new FileOutputStream((String)localObject5);
/* 301 */           localObject11 = new BufferedOutputStream((OutputStream)localObject11);
/* 302 */           localObject11 = new GZIPOutputStream((OutputStream)localObject11);
/*     */         } else {
/* 304 */           if ((!((String)localObject5).toLowerCase().endsWith(".pack")) && (!((String)localObject5).toLowerCase().endsWith(".pac")))
/*     */           {
/* 306 */             System.err.println(MessageFormat.format(RESOURCE.getString("WIRTE_PACKGZ_FILE"), new Object[] { localObject5 }));
/* 307 */             printUsage(bool1, false, System.err);
/* 308 */             System.exit(2);
/*     */           }
/* 310 */           localObject11 = new FileOutputStream((String)localObject5);
/* 311 */           localObject11 = new BufferedOutputStream((OutputStream)localObject11);
/*     */         }
/* 313 */         ((Pack200.Packer)???).pack((JarFile)localObject10, (OutputStream)localObject11);
/*     */ 
/* 315 */         ((OutputStream)localObject11).close();
/*     */       }
/*     */ 
/* 318 */       if ((j != 0) && (((String)localObject7).equals(localObject6)))
/*     */       {
/* 322 */         localObject10 = createTempFile((String)localObject6, ".bak");
/*     */ 
/* 324 */         ((File)localObject10).delete();
/* 325 */         boolean bool2 = new File((String)localObject6).renameTo((File)localObject10);
/* 326 */         if (!bool2) {
/* 327 */           throw new Error(MessageFormat.format(RESOURCE.getString("SKIP_FOR_MOVE_FAILED"), new Object[] { str3 }));
/*     */         }
/*     */ 
/* 330 */         str3 = ((File)localObject10).getPath();
/*     */       }
/*     */ 
/* 334 */       if (i != 0)
/*     */       {
/* 337 */         if (((String)localObject5).equals("-"))
/* 338 */           localObject10 = System.in;
/*     */         else
/* 340 */           localObject10 = new FileInputStream(new File((String)localObject5));
/* 341 */         BufferedInputStream localBufferedInputStream = new BufferedInputStream((InputStream)localObject10);
/* 342 */         localObject10 = localBufferedInputStream;
/* 343 */         if (Utils.isGZIPMagic(Utils.readMagic(localBufferedInputStream))) {
/* 344 */           localObject10 = new GZIPInputStream((InputStream)localObject10);
/*     */         }
/* 346 */         localObject12 = ((String)localObject7).equals("") ? localObject6 : localObject7;
/*     */ 
/* 348 */         if (((String)localObject12).equals("-"))
/* 349 */           localObject14 = System.out;
/*     */         else
/* 351 */           localObject14 = new FileOutputStream((String)localObject12);
/* 352 */         Object localObject14 = new BufferedOutputStream((OutputStream)localObject14);
/* 353 */         JarOutputStream localJarOutputStream = new JarOutputStream((OutputStream)localObject14); Object localObject15 = null;
/*     */         try { localUnpacker.unpack((InputStream)localObject10, localJarOutputStream); }
/*     */         catch (Throwable localThrowable5)
/*     */         {
/* 353 */           localObject15 = localThrowable5; throw localThrowable5;
/*     */         }
/*     */         finally {
/* 356 */           if (localJarOutputStream != null) if (localObject15 != null) try { localJarOutputStream.close(); } catch (Throwable localThrowable6) { localObject15.addSuppressed(localThrowable6); } else localJarOutputStream.close();
/*     */         }
/*     */       }
/*     */ 
/* 360 */       if (!str3.equals(""))
/*     */       {
/* 362 */         new File(str3).delete();
/* 363 */         str3 = "";
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 368 */       if (!str3.equals("")) {
/* 369 */         File localFile = new File((String)localObject6);
/* 370 */         localFile.delete();
/* 371 */         new File(str3).renameTo(localFile);
/*     */       }
/*     */ 
/* 374 */       if (!str4.equals(""))
/* 375 */         new File(str4).delete();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static File createTempFile(String paramString1, String paramString2) throws IOException
/*     */   {
/* 381 */     File localFile1 = new File(paramString1);
/* 382 */     String str = localFile1.getName();
/* 383 */     if (str.length() < 3) str = str + "tmp";
/*     */ 
/* 385 */     File localFile2 = (localFile1.getParentFile() == null) && (paramString2.equals(".bak")) ? new File(".").getAbsoluteFile() : localFile1.getParentFile();
/*     */ 
/* 389 */     Path localPath = localFile2 == null ? Files.createTempFile(str, paramString2, new FileAttribute[0]) : Files.createTempFile(localFile2.toPath(), str, paramString2, new FileAttribute[0]);
/*     */ 
/* 393 */     return localPath.toFile();
/*     */   }
/*     */ 
/*     */   private static void printUsage(boolean paramBoolean1, boolean paramBoolean2, PrintStream paramPrintStream)
/*     */   {
/* 398 */     String str = paramBoolean1 ? "pack200" : "unpack200";
/* 399 */     String[] arrayOfString1 = (String[])RESOURCE.getObject("PACK_HELP");
/* 400 */     String[] arrayOfString2 = (String[])RESOURCE.getObject("UNPACK_HELP");
/* 401 */     String[] arrayOfString3 = paramBoolean1 ? arrayOfString1 : arrayOfString2;
/* 402 */     for (int i = 0; i < arrayOfString3.length; i++) {
/* 403 */       paramPrintStream.println(arrayOfString3[i]);
/* 404 */       if (!paramBoolean2) {
/* 405 */         paramPrintStream.println(MessageFormat.format(RESOURCE.getString("MORE_INFO"), new Object[] { str }));
/* 406 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getZipComment(String paramString) throws IOException
/*     */   {
/* 413 */     byte[] arrayOfByte = new byte[1000];
/* 414 */     long l1 = new File(paramString).length();
/* 415 */     if (l1 <= 0L) return "";
/* 416 */     long l2 = Math.max(0L, l1 - arrayOfByte.length);
/* 417 */     FileInputStream localFileInputStream = new FileInputStream(new File(paramString)); Object localObject1 = null;
/*     */     try { localFileInputStream.skip(l2);
/* 419 */       localFileInputStream.read(arrayOfByte);
/* 420 */       for (int i = arrayOfByte.length - 4; i >= 0; i--) {
/* 421 */         if ((arrayOfByte[(i + 0)] == 80) && (arrayOfByte[(i + 1)] == 75) && (arrayOfByte[(i + 2)] == 5) && (arrayOfByte[(i + 3)] == 6))
/*     */         {
/* 424 */           i += 22;
/*     */           String str2;
/* 425 */           if (i < arrayOfByte.length)
/* 426 */             return new String(arrayOfByte, i, arrayOfByte.length - i, "UTF8");
/* 427 */           return "";
/*     */         }
/*     */       }
/* 430 */       return "";
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 417 */       localObject1 = localThrowable1; throw localThrowable1;
/*     */     }
/*     */     finally
/*     */     {
/* 431 */       if (localFileInputStream != null) if (localObject1 != null) try { localFileInputStream.close(); } catch (Throwable localThrowable5) { localObject1.addSuppressed(localThrowable5); } else localFileInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String parseCommandOptions(List<String> paramList, String paramString, Map<String, String> paramMap)
/*     */   {
/* 525 */     Object localObject1 = null;
/*     */ 
/* 528 */     TreeMap localTreeMap = new TreeMap();
/*     */     Object localObject4;
/*     */     Object localObject5;
/* 530 */     for (Object localObject3 : paramString.split("\n")) {
/* 531 */       localObject4 = localObject3.split("\\p{Space}+");
/* 532 */       if (localObject4.length != 0) {
/* 533 */         localObject5 = localObject4[0];
/* 534 */         localObject4[0] = "";
/* 535 */         if ((((String)localObject5).length() == 0) && (localObject4.length >= 1)) {
/* 536 */           localObject5 = localObject4[1];
/* 537 */           localObject4[1] = "";
/*     */         }
/* 539 */         if (((String)localObject5).length() != 0) {
/* 540 */           String[] arrayOfString1 = (String[])localTreeMap.put(localObject5, localObject4);
/* 541 */           if (arrayOfString1 != null)
/* 542 */             throw new RuntimeException(MessageFormat.format(RESOURCE.getString("DUPLICATE_OPTION"), new Object[] { localObject3.trim() }));
/*     */         }
/*     */       }
/*     */     }
/* 546 */     ??? = paramList.listIterator();
/* 547 */     ListIterator localListIterator = new ArrayList().listIterator();
/*     */     String str1;
/* 553 */     if (localListIterator.hasPrevious()) {
/* 554 */       str1 = (String)localListIterator.previous();
/* 555 */       localListIterator.remove(); } else {
/* 556 */       if (!((ListIterator)???).hasNext()) break label1211;
/* 557 */       str1 = (String)((ListIterator)???).next();
/*     */     }
/*     */ 
/* 563 */     int k = str1.length();
/*     */     while (true)
/*     */     {
/* 569 */       localObject4 = str1.substring(0, k);
/*     */       int m;
/* 570 */       if (!localTreeMap.containsKey(localObject4)) {
/* 571 */         if (k == 0)
/*     */           break label1199;
/* 573 */         localObject5 = localTreeMap.headMap(localObject4);
/*     */ 
/* 575 */         m = ((SortedMap)localObject5).isEmpty() ? 0 : ((String)((SortedMap)localObject5).lastKey()).length();
/* 576 */         k = Math.min(m, k - 1);
/* 577 */         localObject4 = str1.substring(0, k);
/*     */       }
/*     */       else
/*     */       {
/* 581 */         localObject4 = ((String)localObject4).intern();
/* 582 */         assert (str1.startsWith((String)localObject4));
/* 583 */         assert (((String)localObject4).length() == k);
/* 584 */         localObject5 = str1.substring(k);
/*     */ 
/* 588 */         m = 0;
/* 589 */         int n = 0;
/*     */ 
/* 591 */         int i1 = localListIterator.nextIndex();
/* 592 */         String[] arrayOfString2 = (String[])localTreeMap.get(localObject4);
/*     */ 
/* 594 */         for (String str2 : arrayOfString2) {
/* 595 */           if (str2.length() != 0) {
/* 596 */             if (str2.startsWith("#")) break;
/* 597 */             int i4 = 0;
/* 598 */             int i5 = str2.charAt(i4++);
/*     */             int i6;
/* 602 */             switch (i5)
/*     */             {
/*     */             case 43:
/* 605 */               i6 = ((String)localObject5).length() != 0 ? 1 : 0;
/* 606 */               i5 = str2.charAt(i4++);
/* 607 */               break;
/*     */             case 42:
/* 610 */               i6 = 1;
/* 611 */               i5 = str2.charAt(i4++);
/* 612 */               break;
/*     */             default:
/* 616 */               i6 = ((String)localObject5).length() == 0 ? 1 : 0;
/*     */             }
/*     */ 
/* 619 */             if (i6 != 0)
/*     */             {
/* 621 */               String str3 = str2.substring(i4);
/* 622 */               switch (i5) {
/*     */               case 46:
/* 624 */                 localObject1 = str3.length() != 0 ? str3.intern() : localObject4;
/* 625 */                 break;
/*     */               case 63:
/* 627 */                 localObject1 = str3.length() != 0 ? str3.intern() : str1;
/* 628 */                 n = 1;
/* 629 */                 break;
/*     */               case 64:
/* 631 */                 localObject4 = str3.intern();
/* 632 */                 break;
/*     */               case 62:
/* 634 */                 localListIterator.add(str3 + (String)localObject5);
/* 635 */                 localObject5 = "";
/* 636 */                 break;
/*     */               case 33:
/* 638 */                 Object localObject6 = str3.length() != 0 ? str3.intern() : localObject4;
/* 639 */                 paramMap.remove(localObject6);
/* 640 */                 paramMap.put(localObject6, null);
/* 641 */                 m = 1;
/* 642 */                 break;
/*     */               case 36:
/*     */                 String str4;
/* 645 */                 if (str3.length() != 0)
/*     */                 {
/* 647 */                   str4 = str3;
/*     */                 } else {
/* 649 */                   String str5 = (String)paramMap.get(localObject4);
/* 650 */                   if ((str5 == null) || (str5.length() == 0)) {
/* 651 */                     str4 = "1";
/*     */                   }
/*     */                   else {
/* 654 */                     str4 = "" + (1 + Integer.parseInt(str5));
/*     */                   }
/*     */                 }
/* 657 */                 paramMap.put(localObject4, str4);
/* 658 */                 m = 1;
/* 659 */                 break;
/*     */               case 38:
/*     */               case 61:
/* 663 */                 int i7 = i5 == 38 ? 1 : 0;
/*     */                 String str6;
/* 665 */                 if (localListIterator.hasPrevious()) {
/* 666 */                   str6 = (String)localListIterator.previous();
/* 667 */                   localListIterator.remove();
/* 668 */                 } else if (((ListIterator)???).hasNext()) {
/* 669 */                   str6 = (String)((ListIterator)???).next();
/*     */                 } else {
/* 671 */                   localObject1 = str1 + " ?";
/* 672 */                   n = 1;
/* 673 */                   break label1128;
/*     */                 }
/* 675 */                 if (i7 != 0) {
/* 676 */                   String str7 = (String)paramMap.get(localObject4);
/* 677 */                   if (str7 != null)
/*     */                   {
/* 679 */                     String str8 = str3;
/* 680 */                     if (str8.length() == 0) str8 = " ";
/* 681 */                     str6 = str7 + str3 + str6;
/*     */                   }
/*     */                 }
/* 684 */                 paramMap.put(localObject4, str6);
/* 685 */                 m = 1;
/* 686 */                 break;
/*     */               default:
/* 688 */                 throw new RuntimeException(MessageFormat.format(RESOURCE.getString("BAD_SPEC"), new Object[] { localObject4, str2 }));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 693 */         label1128: if ((m != 0) && (n == 0))
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 698 */         while (localListIterator.nextIndex() > i1)
/*     */         {
/* 700 */           localListIterator.previous();
/* 701 */           localListIterator.remove();
/*     */         }
/*     */ 
/* 704 */         if (n != 0) {
/* 705 */           throw new IllegalArgumentException((String)localObject1);
/*     */         }
/*     */ 
/* 708 */         if (k == 0)
/*     */           break label1199;
/* 563 */         k--;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 716 */     label1199: localListIterator.add(str1);
/*     */ 
/* 720 */     label1211: paramList.subList(0, ((ListIterator)???).nextIndex()).clear();
/*     */ 
/* 722 */     while (localListIterator.hasPrevious()) {
/* 723 */       paramList.add(0, localListIterator.previous());
/*     */     }
/*     */ 
/* 726 */     return localObject1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.Driver
 * JD-Core Version:    0.6.2
 */