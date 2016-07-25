/*      */ package sun.tools.jar;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileDescriptor;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.OpenOption;
/*      */ import java.nio.file.Path;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.jar.Attributes;
/*      */ import java.util.jar.Attributes.Name;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.jar.JarOutputStream;
/*      */ import java.util.jar.Manifest;
/*      */ import java.util.zip.CRC32;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipFile;
/*      */ import java.util.zip.ZipInputStream;
/*      */ import java.util.zip.ZipOutputStream;
/*      */ import sun.misc.JarIndex;
/*      */ 
/*      */ public class Main
/*      */ {
/*      */   String program;
/*      */   PrintStream out;
/*      */   PrintStream err;
/*      */   String fname;
/*      */   String mname;
/*      */   String ename;
/*   51 */   String zname = "";
/*      */   String[] files;
/*   53 */   String rootjar = null;
/*      */ 
/*   58 */   Map<String, File> entryMap = new HashMap();
/*      */ 
/*   61 */   Set<File> entries = new LinkedHashSet();
/*      */ 
/*   64 */   Set<String> paths = new HashSet();
/*      */   boolean cflag;
/*      */   boolean uflag;
/*      */   boolean xflag;
/*      */   boolean tflag;
/*      */   boolean vflag;
/*      */   boolean flag0;
/*      */   boolean Mflag;
/*      */   boolean iflag;
/*      */   boolean pflag;
/*      */   static final String MANIFEST_DIR = "META-INF/";
/*      */   static final String VERSION = "1.0";
/*      */   private static ResourceBundle rsrc;
/*      */   private static final boolean useExtractionTime;
/*      */   private boolean ok;
/*  810 */   private byte[] copyBuf = new byte[8192];
/*      */ 
/* 1089 */   private HashSet<String> jarPaths = new HashSet();
/*      */ 
/*      */   private String getMsg(String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  105 */       return rsrc.getString(paramString); } catch (MissingResourceException localMissingResourceException) {
/*      */     }
/*  107 */     throw new Error("Error in message file");
/*      */   }
/*      */ 
/*      */   private String formatMsg(String paramString1, String paramString2)
/*      */   {
/*  112 */     String str = getMsg(paramString1);
/*  113 */     String[] arrayOfString = new String[1];
/*  114 */     arrayOfString[0] = paramString2;
/*  115 */     return MessageFormat.format(str, (Object[])arrayOfString);
/*      */   }
/*      */ 
/*      */   private String formatMsg2(String paramString1, String paramString2, String paramString3) {
/*  119 */     String str = getMsg(paramString1);
/*  120 */     String[] arrayOfString = new String[2];
/*  121 */     arrayOfString[0] = paramString2;
/*  122 */     arrayOfString[1] = paramString3;
/*  123 */     return MessageFormat.format(str, (Object[])arrayOfString);
/*      */   }
/*      */ 
/*      */   public Main(PrintStream paramPrintStream1, PrintStream paramPrintStream2, String paramString) {
/*  127 */     this.out = paramPrintStream1;
/*  128 */     this.err = paramPrintStream2;
/*  129 */     this.program = paramString;
/*      */   }
/*      */ 
/*      */   private static File createTempFileInSameDirectoryAs(File paramFile)
/*      */     throws IOException
/*      */   {
/*  138 */     File localFile = paramFile.getParentFile();
/*  139 */     if (localFile == null)
/*  140 */       localFile = new File(".");
/*  141 */     return File.createTempFile("jartmp", null, localFile);
/*      */   }
/*      */ 
/*      */   public synchronized boolean run(String[] paramArrayOfString)
/*      */   {
/*  150 */     this.ok = true;
/*  151 */     if (!parseArgs(paramArrayOfString))
/*  152 */       return false;
/*      */     try
/*      */     {
/*  155 */       if (((this.cflag) || (this.uflag)) && 
/*  156 */         (this.fname != null))
/*      */       {
/*  160 */         this.zname = this.fname.replace(File.separatorChar, '/');
/*  161 */         if (this.zname.startsWith("./"))
/*  162 */           this.zname = this.zname.substring(2);
/*      */       }
/*      */       Object localObject1;
/*      */       Object localObject2;
/*      */       Object localObject3;
/*  166 */       if (this.cflag) {
/*  167 */         localObject1 = null;
/*  168 */         localObject2 = null;
/*      */ 
/*  170 */         if (!this.Mflag) {
/*  171 */           if (this.mname != null) {
/*  172 */             localObject2 = new FileInputStream(this.mname);
/*  173 */             localObject1 = new Manifest(new BufferedInputStream((InputStream)localObject2));
/*      */           } else {
/*  175 */             localObject1 = new Manifest();
/*      */           }
/*  177 */           addVersion((Manifest)localObject1);
/*  178 */           addCreatedBy((Manifest)localObject1);
/*  179 */           if (isAmbiguousMainClass((Manifest)localObject1)) {
/*  180 */             if (localObject2 != null) {
/*  181 */               ((InputStream)localObject2).close();
/*      */             }
/*  183 */             return false;
/*      */           }
/*  185 */           if (this.ename != null) {
/*  186 */             addMainClass((Manifest)localObject1, this.ename);
/*      */           }
/*      */         }
/*  189 */         expand(null, this.files, false);
/*      */ 
/*  191 */         if (this.fname != null) {
/*  192 */           localObject3 = new FileOutputStream(this.fname);
/*      */         } else {
/*  194 */           localObject3 = new FileOutputStream(FileDescriptor.out);
/*  195 */           if (this.vflag)
/*      */           {
/*  199 */             this.vflag = false;
/*      */           }
/*      */         }
/*  202 */         create(new BufferedOutputStream((OutputStream)localObject3, 4096), (Manifest)localObject1);
/*  203 */         if (localObject2 != null) {
/*  204 */           ((InputStream)localObject2).close();
/*      */         }
/*  206 */         ((OutputStream)localObject3).close();
/*  207 */       } else if (this.uflag) {
/*  208 */         localObject1 = null; localObject2 = null;
/*      */         FileOutputStream localFileOutputStream;
/*  211 */         if (this.fname != null) {
/*  212 */           localObject1 = new File(this.fname);
/*  213 */           localObject2 = createTempFileInSameDirectoryAs((File)localObject1);
/*  214 */           localObject3 = new FileInputStream((File)localObject1);
/*  215 */           localFileOutputStream = new FileOutputStream((File)localObject2);
/*      */         } else {
/*  217 */           localObject3 = new FileInputStream(FileDescriptor.in);
/*  218 */           localFileOutputStream = new FileOutputStream(FileDescriptor.out);
/*  219 */           this.vflag = false;
/*      */         }
/*  221 */         InputStream localInputStream = (!this.Mflag) && (this.mname != null) ? new FileInputStream(this.mname) : null;
/*      */ 
/*  223 */         expand(null, this.files, true);
/*  224 */         boolean bool = update((InputStream)localObject3, new BufferedOutputStream(localFileOutputStream), localInputStream, null);
/*      */ 
/*  226 */         if (this.ok) {
/*  227 */           this.ok = bool;
/*      */         }
/*  229 */         ((FileInputStream)localObject3).close();
/*  230 */         localFileOutputStream.close();
/*  231 */         if (localInputStream != null) {
/*  232 */           localInputStream.close();
/*      */         }
/*  234 */         if (this.fname != null)
/*      */         {
/*  236 */           ((File)localObject1).delete();
/*  237 */           if (!((File)localObject2).renameTo((File)localObject1)) {
/*  238 */             ((File)localObject2).delete();
/*  239 */             throw new IOException(getMsg("error.write.file"));
/*      */           }
/*  241 */           ((File)localObject2).delete();
/*      */         }
/*  243 */       } else if (this.tflag) {
/*  244 */         replaceFSC(this.files);
/*  245 */         if (this.fname != null) {
/*  246 */           list(this.fname, this.files);
/*      */         } else {
/*  248 */           localObject1 = new FileInputStream(FileDescriptor.in);
/*      */           try {
/*  250 */             list(new BufferedInputStream((InputStream)localObject1), this.files);
/*      */           } finally {
/*  252 */             ((InputStream)localObject1).close();
/*      */           }
/*      */         }
/*  255 */       } else if (this.xflag) {
/*  256 */         replaceFSC(this.files);
/*  257 */         if ((this.fname != null) && (this.files != null)) {
/*  258 */           extract(this.fname, this.files);
/*      */         } else {
/*  260 */           localObject1 = this.fname == null ? new FileInputStream(FileDescriptor.in) : new FileInputStream(this.fname);
/*      */           try
/*      */           {
/*  264 */             extract(new BufferedInputStream((InputStream)localObject1), this.files);
/*      */           } finally {
/*  266 */             ((InputStream)localObject1).close();
/*      */           }
/*      */         }
/*  269 */       } else if (this.iflag) {
/*  270 */         genIndex(this.rootjar, this.files);
/*      */       }
/*      */     } catch (IOException localIOException) {
/*  273 */       fatalError(localIOException);
/*  274 */       this.ok = false;
/*      */     } catch (Error localError) {
/*  276 */       localError.printStackTrace();
/*  277 */       this.ok = false;
/*      */     } catch (Throwable localThrowable) {
/*  279 */       localThrowable.printStackTrace();
/*  280 */       this.ok = false;
/*      */     }
/*  282 */     this.out.flush();
/*  283 */     this.err.flush();
/*  284 */     return this.ok;
/*      */   }
/*      */ 
/*      */   boolean parseArgs(String[] paramArrayOfString)
/*      */   {
/*      */     try
/*      */     {
/*  293 */       paramArrayOfString = CommandLine.parse(paramArrayOfString);
/*      */     } catch (FileNotFoundException localFileNotFoundException) {
/*  295 */       fatalError(formatMsg("error.cant.open", localFileNotFoundException.getMessage()));
/*  296 */       return false;
/*      */     } catch (IOException localIOException) {
/*  298 */       fatalError(localIOException);
/*  299 */       return false;
/*  302 */     }
/*      */ int i = 1;
/*      */     int k;
/*      */     try { String str1 = paramArrayOfString[0];
/*  305 */       if (str1.startsWith("-")) {
/*  306 */         str1 = str1.substring(1);
/*      */       }
/*  308 */       for (k = 0; k < str1.length(); k++)
/*  309 */         switch (str1.charAt(k)) {
/*      */         case 'c':
/*  311 */           if ((this.xflag) || (this.tflag) || (this.uflag) || (this.iflag)) {
/*  312 */             usageError();
/*  313 */             return false;
/*      */           }
/*  315 */           this.cflag = true;
/*  316 */           break;
/*      */         case 'u':
/*  318 */           if ((this.cflag) || (this.xflag) || (this.tflag) || (this.iflag)) {
/*  319 */             usageError();
/*  320 */             return false;
/*      */           }
/*  322 */           this.uflag = true;
/*  323 */           break;
/*      */         case 'x':
/*  325 */           if ((this.cflag) || (this.uflag) || (this.tflag) || (this.iflag)) {
/*  326 */             usageError();
/*  327 */             return false;
/*      */           }
/*  329 */           this.xflag = true;
/*  330 */           break;
/*      */         case 't':
/*  332 */           if ((this.cflag) || (this.uflag) || (this.xflag) || (this.iflag)) {
/*  333 */             usageError();
/*  334 */             return false;
/*      */           }
/*  336 */           this.tflag = true;
/*  337 */           break;
/*      */         case 'M':
/*  339 */           this.Mflag = true;
/*  340 */           break;
/*      */         case 'v':
/*  342 */           this.vflag = true;
/*  343 */           break;
/*      */         case 'f':
/*  345 */           this.fname = paramArrayOfString[(i++)];
/*  346 */           break;
/*      */         case 'm':
/*  348 */           this.mname = paramArrayOfString[(i++)];
/*  349 */           break;
/*      */         case '0':
/*  351 */           this.flag0 = true;
/*  352 */           break;
/*      */         case 'i':
/*  354 */           if ((this.cflag) || (this.uflag) || (this.xflag) || (this.tflag)) {
/*  355 */             usageError();
/*  356 */             return false;
/*      */           }
/*      */ 
/*  359 */           this.rootjar = paramArrayOfString[(i++)];
/*  360 */           this.iflag = true;
/*  361 */           break;
/*      */         case 'e':
/*  363 */           this.ename = paramArrayOfString[(i++)];
/*  364 */           break;
/*      */         case 'P':
/*  366 */           this.pflag = true;
/*  367 */           break;
/*      */         default:
/*  369 */           error(formatMsg("error.illegal.option", String.valueOf(str1.charAt(k))));
/*      */ 
/*  371 */           usageError();
/*  372 */           return false;
/*      */         }
/*      */     } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException1)
/*      */     {
/*  376 */       usageError();
/*  377 */       return false;
/*      */     }
/*  379 */     if ((!this.cflag) && (!this.tflag) && (!this.xflag) && (!this.uflag) && (!this.iflag)) {
/*  380 */       error(getMsg("error.bad.option"));
/*  381 */       usageError();
/*  382 */       return false;
/*      */     }
/*      */ 
/*  385 */     int j = paramArrayOfString.length - i;
/*  386 */     if (j > 0) {
/*  387 */       k = 0;
/*  388 */       String[] arrayOfString = new String[j];
/*      */       try {
/*  390 */         for (int m = i; m < paramArrayOfString.length; m++)
/*  391 */           if (paramArrayOfString[m].equals("-C"))
/*      */           {
/*  393 */             String str2 = paramArrayOfString[(++m)];
/*  394 */             str2 = str2 + File.separator;
/*      */ 
/*  396 */             str2 = str2.replace(File.separatorChar, '/');
/*  397 */             while (str2.indexOf("//") > -1) {
/*  398 */               str2 = str2.replace("//", "/");
/*      */             }
/*  400 */             this.paths.add(str2.replace(File.separatorChar, '/'));
/*  401 */             arrayOfString[(k++)] = (str2 + paramArrayOfString[(++m)]);
/*      */           } else {
/*  403 */             arrayOfString[(k++)] = paramArrayOfString[m];
/*      */           }
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException2) {
/*  407 */         usageError();
/*  408 */         return false;
/*      */       }
/*  410 */       this.files = new String[k];
/*  411 */       System.arraycopy(arrayOfString, 0, this.files, 0, k); } else {
/*  412 */       if ((this.cflag) && (this.mname == null)) {
/*  413 */         error(getMsg("error.bad.cflag"));
/*  414 */         usageError();
/*  415 */         return false;
/*  416 */       }if (this.uflag) {
/*  417 */         if ((this.mname != null) || (this.ename != null))
/*      */         {
/*  419 */           return true;
/*      */         }
/*  421 */         error(getMsg("error.bad.uflag"));
/*  422 */         usageError();
/*  423 */         return false;
/*      */       }
/*      */     }
/*  426 */     return true;
/*      */   }
/*      */ 
/*      */   void expand(File paramFile, String[] paramArrayOfString, boolean paramBoolean)
/*      */   {
/*  434 */     if (paramArrayOfString == null) {
/*  435 */       return;
/*      */     }
/*  437 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*      */     {
/*      */       File localFile;
/*  439 */       if (paramFile == null)
/*  440 */         localFile = new File(paramArrayOfString[i]);
/*      */       else {
/*  442 */         localFile = new File(paramFile, paramArrayOfString[i]);
/*      */       }
/*  444 */       if (localFile.isFile()) {
/*  445 */         if ((this.entries.add(localFile)) && 
/*  446 */           (paramBoolean))
/*  447 */           this.entryMap.put(entryName(localFile.getPath()), localFile);
/*      */       }
/*  449 */       else if (localFile.isDirectory()) {
/*  450 */         if (this.entries.add(localFile)) {
/*  451 */           if (paramBoolean) {
/*  452 */             String str = localFile.getPath();
/*  453 */             str = str + File.separator;
/*      */ 
/*  455 */             this.entryMap.put(entryName(str), localFile);
/*      */           }
/*  457 */           expand(localFile, localFile.list(), paramBoolean);
/*      */         }
/*      */       } else {
/*  460 */         error(formatMsg("error.nosuch.fileordir", String.valueOf(localFile)));
/*  461 */         this.ok = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void create(OutputStream paramOutputStream, Manifest paramManifest)
/*      */     throws IOException
/*      */   {
/*  472 */     JarOutputStream localJarOutputStream = new JarOutputStream(paramOutputStream);
/*  473 */     if (this.flag0) {
/*  474 */       localJarOutputStream.setMethod(0);
/*      */     }
/*  476 */     if (paramManifest != null) {
/*  477 */       if (this.vflag) {
/*  478 */         output(getMsg("out.added.manifest"));
/*      */       }
/*  480 */       localObject = new ZipEntry("META-INF/");
/*  481 */       ((ZipEntry)localObject).setTime(System.currentTimeMillis());
/*  482 */       ((ZipEntry)localObject).setSize(0L);
/*  483 */       ((ZipEntry)localObject).setCrc(0L);
/*  484 */       localJarOutputStream.putNextEntry((ZipEntry)localObject);
/*  485 */       localObject = new ZipEntry("META-INF/MANIFEST.MF");
/*  486 */       ((ZipEntry)localObject).setTime(System.currentTimeMillis());
/*  487 */       if (this.flag0) {
/*  488 */         crc32Manifest((ZipEntry)localObject, paramManifest);
/*      */       }
/*  490 */       localJarOutputStream.putNextEntry((ZipEntry)localObject);
/*  491 */       paramManifest.write(localJarOutputStream);
/*  492 */       localJarOutputStream.closeEntry();
/*      */     }
/*  494 */     for (Object localObject = this.entries.iterator(); ((Iterator)localObject).hasNext(); ) { File localFile = (File)((Iterator)localObject).next();
/*  495 */       addFile(localJarOutputStream, localFile);
/*      */     }
/*  497 */     localJarOutputStream.close();
/*      */   }
/*      */ 
/*      */   private char toUpperCaseASCII(char paramChar) {
/*  501 */     return (paramChar < 'a') || (paramChar > 'z') ? paramChar : (char)(paramChar + 'A' - 97);
/*      */   }
/*      */ 
/*      */   private boolean equalsIgnoreCase(String paramString1, String paramString2)
/*      */   {
/*  511 */     assert (paramString2.toUpperCase(Locale.ENGLISH).equals(paramString2));
/*      */     int i;
/*  513 */     if ((i = paramString1.length()) != paramString2.length())
/*  514 */       return false;
/*  515 */     for (int j = 0; j < i; j++) {
/*  516 */       char c1 = paramString1.charAt(j);
/*  517 */       char c2 = paramString2.charAt(j);
/*  518 */       if ((c1 != c2) && (toUpperCaseASCII(c1) != c2))
/*  519 */         return false;
/*      */     }
/*  521 */     return true;
/*      */   }
/*      */ 
/*      */   boolean update(InputStream paramInputStream1, OutputStream paramOutputStream, InputStream paramInputStream2, JarIndex paramJarIndex)
/*      */     throws IOException
/*      */   {
/*  531 */     ZipInputStream localZipInputStream = new ZipInputStream(paramInputStream1);
/*  532 */     JarOutputStream localJarOutputStream = new JarOutputStream(paramOutputStream);
/*  533 */     ZipEntry localZipEntry = null;
/*  534 */     int i = 0;
/*  535 */     boolean bool1 = true;
/*      */ 
/*  537 */     if (paramJarIndex != null) {
/*  538 */       addIndex(paramJarIndex, localJarOutputStream);
/*      */     }
/*      */ 
/*  542 */     while ((localZipEntry = localZipInputStream.getNextEntry()) != null) {
/*  543 */       localObject1 = localZipEntry.getName();
/*      */ 
/*  545 */       boolean bool2 = equalsIgnoreCase((String)localObject1, "META-INF/MANIFEST.MF");
/*      */ 
/*  547 */       if (((paramJarIndex == null) || (!equalsIgnoreCase((String)localObject1, "META-INF/INDEX.LIST"))) && ((!this.Mflag) || (!bool2)))
/*      */       {
/*      */         Object localObject2;
/*  550 */         if ((bool2) && ((paramInputStream2 != null) || (this.ename != null)))
/*      */         {
/*  552 */           i = 1;
/*  553 */           if (paramInputStream2 != null)
/*      */           {
/*  557 */             localObject2 = new FileInputStream(this.mname);
/*  558 */             boolean bool3 = isAmbiguousMainClass(new Manifest((InputStream)localObject2));
/*  559 */             ((FileInputStream)localObject2).close();
/*  560 */             if (bool3) {
/*  561 */               return false;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  566 */           localObject2 = new Manifest(localZipInputStream);
/*  567 */           if (paramInputStream2 != null) {
/*  568 */             ((Manifest)localObject2).read(paramInputStream2);
/*      */           }
/*  570 */           updateManifest((Manifest)localObject2, localJarOutputStream);
/*      */         }
/*  572 */         else if (!this.entryMap.containsKey(localObject1))
/*      */         {
/*  574 */           localObject2 = new ZipEntry((String)localObject1);
/*  575 */           ((ZipEntry)localObject2).setMethod(localZipEntry.getMethod());
/*  576 */           ((ZipEntry)localObject2).setTime(localZipEntry.getTime());
/*  577 */           ((ZipEntry)localObject2).setComment(localZipEntry.getComment());
/*  578 */           ((ZipEntry)localObject2).setExtra(localZipEntry.getExtra());
/*  579 */           if (localZipEntry.getMethod() == 0) {
/*  580 */             ((ZipEntry)localObject2).setSize(localZipEntry.getSize());
/*  581 */             ((ZipEntry)localObject2).setCrc(localZipEntry.getCrc());
/*      */           }
/*  583 */           localJarOutputStream.putNextEntry((ZipEntry)localObject2);
/*  584 */           copy(localZipInputStream, localJarOutputStream);
/*      */         } else {
/*  586 */           localObject2 = (File)this.entryMap.get(localObject1);
/*  587 */           addFile(localJarOutputStream, (File)localObject2);
/*  588 */           this.entryMap.remove(localObject1);
/*  589 */           this.entries.remove(localObject2);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  595 */     for (Object localObject1 = this.entries.iterator(); ((Iterator)localObject1).hasNext(); ) { File localFile = (File)((Iterator)localObject1).next();
/*  596 */       addFile(localJarOutputStream, localFile);
/*      */     }
/*  598 */     if (i == 0) {
/*  599 */       if (paramInputStream2 != null) {
/*  600 */         localObject1 = new Manifest(paramInputStream2);
/*  601 */         bool1 = !isAmbiguousMainClass((Manifest)localObject1);
/*  602 */         if (bool1)
/*  603 */           updateManifest((Manifest)localObject1, localJarOutputStream);
/*      */       }
/*  605 */       else if (this.ename != null) {
/*  606 */         updateManifest(new Manifest(), localJarOutputStream);
/*      */       }
/*      */     }
/*  609 */     localZipInputStream.close();
/*  610 */     localJarOutputStream.close();
/*  611 */     return bool1;
/*      */   }
/*      */ 
/*      */   private void addIndex(JarIndex paramJarIndex, ZipOutputStream paramZipOutputStream)
/*      */     throws IOException
/*      */   {
/*  617 */     ZipEntry localZipEntry = new ZipEntry("META-INF/INDEX.LIST");
/*  618 */     localZipEntry.setTime(System.currentTimeMillis());
/*  619 */     if (this.flag0) {
/*  620 */       CRC32OutputStream localCRC32OutputStream = new CRC32OutputStream();
/*  621 */       paramJarIndex.write(localCRC32OutputStream);
/*  622 */       localCRC32OutputStream.updateEntry(localZipEntry);
/*      */     }
/*  624 */     paramZipOutputStream.putNextEntry(localZipEntry);
/*  625 */     paramJarIndex.write(paramZipOutputStream);
/*  626 */     paramZipOutputStream.closeEntry();
/*      */   }
/*      */ 
/*      */   private void updateManifest(Manifest paramManifest, ZipOutputStream paramZipOutputStream)
/*      */     throws IOException
/*      */   {
/*  632 */     addVersion(paramManifest);
/*  633 */     addCreatedBy(paramManifest);
/*  634 */     if (this.ename != null) {
/*  635 */       addMainClass(paramManifest, this.ename);
/*      */     }
/*  637 */     ZipEntry localZipEntry = new ZipEntry("META-INF/MANIFEST.MF");
/*  638 */     localZipEntry.setTime(System.currentTimeMillis());
/*  639 */     if (this.flag0) {
/*  640 */       crc32Manifest(localZipEntry, paramManifest);
/*      */     }
/*  642 */     paramZipOutputStream.putNextEntry(localZipEntry);
/*  643 */     paramManifest.write(paramZipOutputStream);
/*  644 */     if (this.vflag)
/*  645 */       output(getMsg("out.update.manifest"));
/*      */   }
/*      */ 
/*      */   private static final boolean isWinDriveLetter(char paramChar)
/*      */   {
/*  650 */     return ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'A') && (paramChar <= 'Z'));
/*      */   }
/*      */ 
/*      */   private String safeName(String paramString) {
/*  654 */     if (!this.pflag) {
/*  655 */       int i = paramString.length();
/*  656 */       int j = paramString.lastIndexOf("../");
/*  657 */       if (j == -1)
/*  658 */         j = 0;
/*      */       else {
/*  660 */         j += 3;
/*      */       }
/*  662 */       if (File.separatorChar == '\\')
/*      */       {
/*  665 */         while (j < i) {
/*  666 */           int k = j;
/*  667 */           if ((j + 1 < i) && (paramString.charAt(j + 1) == ':') && (isWinDriveLetter(paramString.charAt(j))))
/*      */           {
/*  670 */             j += 2;
/*      */           }
/*  672 */           while ((j < i) && (paramString.charAt(j) == '/')) {
/*  673 */             j++;
/*      */           }
/*  675 */           if (j == k) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*  680 */       while ((j < i) && (paramString.charAt(j) == '/')) {
/*  681 */         j++;
/*      */       }
/*      */ 
/*  684 */       if (j != 0) {
/*  685 */         paramString = paramString.substring(j);
/*      */       }
/*      */     }
/*  688 */     return paramString;
/*      */   }
/*      */ 
/*      */   private String entryName(String paramString) {
/*  692 */     paramString = paramString.replace(File.separatorChar, '/');
/*  693 */     Object localObject = "";
/*  694 */     for (String str : this.paths) {
/*  695 */       if ((paramString.startsWith(str)) && (str.length() > ((String)localObject).length()))
/*      */       {
/*  697 */         localObject = str;
/*      */       }
/*      */     }
/*  700 */     paramString = paramString.substring(((String)localObject).length());
/*  701 */     paramString = safeName(paramString);
/*      */ 
/*  704 */     if (paramString.startsWith("./")) {
/*  705 */       paramString = paramString.substring(2);
/*      */     }
/*  707 */     return paramString;
/*      */   }
/*      */ 
/*      */   private void addVersion(Manifest paramManifest) {
/*  711 */     Attributes localAttributes = paramManifest.getMainAttributes();
/*  712 */     if (localAttributes.getValue(Attributes.Name.MANIFEST_VERSION) == null)
/*  713 */       localAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
/*      */   }
/*      */ 
/*      */   private void addCreatedBy(Manifest paramManifest)
/*      */   {
/*  718 */     Attributes localAttributes = paramManifest.getMainAttributes();
/*  719 */     if (localAttributes.getValue(new Attributes.Name("Created-By")) == null) {
/*  720 */       String str1 = System.getProperty("java.vendor");
/*  721 */       String str2 = System.getProperty("java.version");
/*  722 */       localAttributes.put(new Attributes.Name("Created-By"), str2 + " (" + str1 + ")");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addMainClass(Manifest paramManifest, String paramString)
/*      */   {
/*  728 */     Attributes localAttributes = paramManifest.getMainAttributes();
/*      */ 
/*  731 */     localAttributes.put(Attributes.Name.MAIN_CLASS, paramString);
/*      */   }
/*      */ 
/*      */   private boolean isAmbiguousMainClass(Manifest paramManifest) {
/*  735 */     if (this.ename != null) {
/*  736 */       Attributes localAttributes = paramManifest.getMainAttributes();
/*  737 */       if (localAttributes.get(Attributes.Name.MAIN_CLASS) != null) {
/*  738 */         error(getMsg("error.bad.eflag"));
/*  739 */         usageError();
/*  740 */         return true;
/*      */       }
/*      */     }
/*  743 */     return false;
/*      */   }
/*      */ 
/*      */   void addFile(ZipOutputStream paramZipOutputStream, File paramFile)
/*      */     throws IOException
/*      */   {
/*  750 */     String str = paramFile.getPath();
/*  751 */     boolean bool = paramFile.isDirectory();
/*  752 */     if (bool) {
/*  753 */       str = str + File.separator;
/*      */     }
/*      */ 
/*  756 */     str = entryName(str);
/*      */ 
/*  758 */     if ((str.equals("")) || (str.equals(".")) || (str.equals(this.zname)))
/*  759 */       return;
/*  760 */     if (((str.equals("META-INF/")) || (str.equals("META-INF/MANIFEST.MF"))) && (!this.Mflag))
/*      */     {
/*  762 */       if (this.vflag) {
/*  763 */         output(formatMsg("out.ignore.entry", str));
/*      */       }
/*  765 */       return;
/*      */     }
/*      */ 
/*  768 */     long l1 = bool ? 0L : paramFile.length();
/*      */ 
/*  770 */     if (this.vflag) {
/*  771 */       this.out.print(formatMsg("out.adding", str));
/*      */     }
/*  773 */     ZipEntry localZipEntry = new ZipEntry(str);
/*  774 */     localZipEntry.setTime(paramFile.lastModified());
/*  775 */     if (l1 == 0L) {
/*  776 */       localZipEntry.setMethod(0);
/*  777 */       localZipEntry.setSize(0L);
/*  778 */       localZipEntry.setCrc(0L);
/*  779 */     } else if (this.flag0) {
/*  780 */       crc32File(localZipEntry, paramFile);
/*      */     }
/*  782 */     paramZipOutputStream.putNextEntry(localZipEntry);
/*  783 */     if (!bool) {
/*  784 */       copy(paramFile, paramZipOutputStream);
/*      */     }
/*  786 */     paramZipOutputStream.closeEntry();
/*      */ 
/*  788 */     if (this.vflag) {
/*  789 */       l1 = localZipEntry.getSize();
/*  790 */       long l2 = localZipEntry.getCompressedSize();
/*  791 */       this.out.print(formatMsg2("out.size", String.valueOf(l1), String.valueOf(l2)));
/*      */ 
/*  793 */       if (localZipEntry.getMethod() == 8) {
/*  794 */         long l3 = 0L;
/*  795 */         if (l1 != 0L) {
/*  796 */           l3 = (l1 - l2) * 100L / l1;
/*      */         }
/*  798 */         output(formatMsg("out.deflated", String.valueOf(l3)));
/*      */       } else {
/*  800 */         output(getMsg("out.stored"));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void copy(InputStream paramInputStream, OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/*      */     int i;
/*  822 */     while ((i = paramInputStream.read(this.copyBuf)) != -1)
/*  823 */       paramOutputStream.write(this.copyBuf, 0, i);
/*      */   }
/*      */ 
/*      */   private void copy(File paramFile, OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/*  835 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/*      */     try {
/*  837 */       copy(localFileInputStream, paramOutputStream);
/*      */     } finally {
/*  839 */       localFileInputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void copy(InputStream paramInputStream, File paramFile)
/*      */     throws IOException
/*      */   {
/*  852 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
/*      */     try {
/*  854 */       copy(paramInputStream, localFileOutputStream);
/*      */     } finally {
/*  856 */       localFileOutputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void crc32Manifest(ZipEntry paramZipEntry, Manifest paramManifest)
/*      */     throws IOException
/*      */   {
/*  865 */     CRC32OutputStream localCRC32OutputStream = new CRC32OutputStream();
/*  866 */     paramManifest.write(localCRC32OutputStream);
/*  867 */     localCRC32OutputStream.updateEntry(paramZipEntry);
/*      */   }
/*      */ 
/*      */   private void crc32File(ZipEntry paramZipEntry, File paramFile)
/*      */     throws IOException
/*      */   {
/*  875 */     CRC32OutputStream localCRC32OutputStream = new CRC32OutputStream();
/*  876 */     copy(paramFile, localCRC32OutputStream);
/*  877 */     if (localCRC32OutputStream.n != paramFile.length()) {
/*  878 */       throw new JarException(formatMsg("error.incorrect.length", paramFile.getPath()));
/*      */     }
/*      */ 
/*  881 */     localCRC32OutputStream.updateEntry(paramZipEntry);
/*      */   }
/*      */ 
/*      */   void replaceFSC(String[] paramArrayOfString) {
/*  885 */     if (paramArrayOfString != null)
/*  886 */       for (int i = 0; i < paramArrayOfString.length; i++)
/*  887 */         paramArrayOfString[i] = paramArrayOfString[i].replace(File.separatorChar, '/');
/*      */   }
/*      */ 
/*      */   Set<ZipEntry> newDirSet()
/*      */   {
/*  894 */     return new HashSet() {
/*      */       public boolean add(ZipEntry paramAnonymousZipEntry) {
/*  896 */         return (paramAnonymousZipEntry == null) || (Main.useExtractionTime) ? false : super.add(paramAnonymousZipEntry);
/*      */       } } ;
/*      */   }
/*      */ 
/*      */   void updateLastModifiedTime(Set<ZipEntry> paramSet) throws IOException {
/*  901 */     for (ZipEntry localZipEntry : paramSet) {
/*  902 */       long l = localZipEntry.getTime();
/*  903 */       if (l != -1L) {
/*  904 */         String str = safeName(localZipEntry.getName().replace(File.separatorChar, '/'));
/*  905 */         if (str.length() != 0) {
/*  906 */           File localFile = new File(str.replace('/', File.separatorChar));
/*  907 */           localFile.setLastModified(l);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void extract(InputStream paramInputStream, String[] paramArrayOfString)
/*      */     throws IOException
/*      */   {
/*  917 */     ZipInputStream localZipInputStream = new ZipInputStream(paramInputStream);
/*      */ 
/*  921 */     Set localSet = newDirSet();
/*      */     ZipEntry localZipEntry;
/*  922 */     while ((localZipEntry = localZipInputStream.getNextEntry()) != null) {
/*  923 */       if (paramArrayOfString == null) {
/*  924 */         localSet.add(extractFile(localZipInputStream, localZipEntry));
/*      */       } else {
/*  926 */         String str1 = localZipEntry.getName();
/*  927 */         for (String str2 : paramArrayOfString) {
/*  928 */           if (str1.startsWith(str2)) {
/*  929 */             localSet.add(extractFile(localZipInputStream, localZipEntry));
/*  930 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  940 */     updateLastModifiedTime(localSet);
/*      */   }
/*      */ 
/*      */   void extract(String paramString, String[] paramArrayOfString)
/*      */     throws IOException
/*      */   {
/*  947 */     ZipFile localZipFile = new ZipFile(paramString);
/*  948 */     Set localSet = newDirSet();
/*  949 */     Enumeration localEnumeration = localZipFile.entries();
/*  950 */     while (localEnumeration.hasMoreElements()) {
/*  951 */       ZipEntry localZipEntry = (ZipEntry)localEnumeration.nextElement();
/*  952 */       if (paramArrayOfString == null) {
/*  953 */         localSet.add(extractFile(localZipFile.getInputStream(localZipEntry), localZipEntry));
/*      */       } else {
/*  955 */         String str1 = localZipEntry.getName();
/*  956 */         for (String str2 : paramArrayOfString) {
/*  957 */           if (str1.startsWith(str2)) {
/*  958 */             localSet.add(extractFile(localZipFile.getInputStream(localZipEntry), localZipEntry));
/*  959 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  964 */     localZipFile.close();
/*  965 */     updateLastModifiedTime(localSet);
/*      */   }
/*      */ 
/*      */   ZipEntry extractFile(InputStream paramInputStream, ZipEntry paramZipEntry)
/*      */     throws IOException
/*      */   {
/*  974 */     ZipEntry localZipEntry = null;
/*      */ 
/*  980 */     String str = safeName(paramZipEntry.getName().replace(File.separatorChar, '/'));
/*  981 */     if (str.length() == 0) {
/*  982 */       return localZipEntry;
/*      */     }
/*  984 */     File localFile1 = new File(str.replace('/', File.separatorChar));
/*  985 */     if (paramZipEntry.isDirectory()) {
/*  986 */       if (localFile1.exists()) {
/*  987 */         if (!localFile1.isDirectory())
/*  988 */           throw new IOException(formatMsg("error.create.dir", localFile1.getPath()));
/*      */       }
/*      */       else
/*      */       {
/*  992 */         if (!localFile1.mkdirs()) {
/*  993 */           throw new IOException(formatMsg("error.create.dir", localFile1.getPath()));
/*      */         }
/*      */ 
/*  996 */         localZipEntry = paramZipEntry;
/*      */       }
/*      */ 
/* 1000 */       if (this.vflag)
/* 1001 */         output(formatMsg("out.create", str));
/*      */     }
/*      */     else {
/* 1004 */       if (localFile1.getParent() != null) {
/* 1005 */         File localFile2 = new File(localFile1.getParent());
/* 1006 */         if (((!localFile2.exists()) && (!localFile2.mkdirs())) || (!localFile2.isDirectory())) {
/* 1007 */           throw new IOException(formatMsg("error.create.dir", localFile2.getPath()));
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 1012 */         copy(paramInputStream, localFile1);
/*      */       } finally {
/* 1014 */         if ((paramInputStream instanceof ZipInputStream))
/* 1015 */           ((ZipInputStream)paramInputStream).closeEntry();
/*      */         else
/* 1017 */           paramInputStream.close();
/*      */       }
/* 1019 */       if (this.vflag) {
/* 1020 */         if (paramZipEntry.getMethod() == 8)
/* 1021 */           output(formatMsg("out.inflated", str));
/*      */         else {
/* 1023 */           output(formatMsg("out.extracted", str));
/*      */         }
/*      */       }
/*      */     }
/* 1027 */     if (!useExtractionTime) {
/* 1028 */       long l = paramZipEntry.getTime();
/* 1029 */       if (l != -1L) {
/* 1030 */         localFile1.setLastModified(l);
/*      */       }
/*      */     }
/* 1033 */     return localZipEntry;
/*      */   }
/*      */ 
/*      */   void list(InputStream paramInputStream, String[] paramArrayOfString)
/*      */     throws IOException
/*      */   {
/* 1040 */     ZipInputStream localZipInputStream = new ZipInputStream(paramInputStream);
/*      */     ZipEntry localZipEntry;
/* 1042 */     while ((localZipEntry = localZipInputStream.getNextEntry()) != null)
/*      */     {
/* 1049 */       localZipInputStream.closeEntry();
/* 1050 */       printEntry(localZipEntry, paramArrayOfString);
/*      */     }
/*      */   }
/*      */ 
/*      */   void list(String paramString, String[] paramArrayOfString)
/*      */     throws IOException
/*      */   {
/* 1058 */     ZipFile localZipFile = new ZipFile(paramString);
/* 1059 */     Enumeration localEnumeration = localZipFile.entries();
/* 1060 */     while (localEnumeration.hasMoreElements()) {
/* 1061 */       printEntry((ZipEntry)localEnumeration.nextElement(), paramArrayOfString);
/*      */     }
/* 1063 */     localZipFile.close();
/*      */   }
/*      */ 
/*      */   void dumpIndex(String paramString, JarIndex paramJarIndex)
/*      */     throws IOException
/*      */   {
/* 1071 */     File localFile = new File(paramString);
/* 1072 */     Path localPath1 = localFile.toPath();
/* 1073 */     Path localPath2 = createTempFileInSameDirectoryAs(localFile).toPath();
/*      */     try {
/* 1075 */       if (update(Files.newInputStream(localPath1, new OpenOption[0]), Files.newOutputStream(localPath2, new OpenOption[0]), null, paramJarIndex))
/*      */         try
/*      */         {
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/* 1081 */           throw new IOException(getMsg("error.write.file"), localIOException);
/*      */         }
/*      */     }
/*      */     finally {
/* 1085 */       Files.deleteIfExists(localPath2);
/*      */     }
/*      */   }
/*      */ 
/*      */   List<String> getJarPath(String paramString)
/*      */     throws IOException
/*      */   {
/* 1096 */     ArrayList localArrayList = new ArrayList();
/* 1097 */     localArrayList.add(paramString);
/* 1098 */     this.jarPaths.add(paramString);
/*      */ 
/* 1101 */     String str1 = paramString.substring(0, Math.max(0, paramString.lastIndexOf('/') + 1));
/*      */ 
/* 1106 */     JarFile localJarFile = new JarFile(paramString.replace('/', File.separatorChar));
/*      */ 
/* 1108 */     if (localJarFile != null) {
/* 1109 */       Manifest localManifest = localJarFile.getManifest();
/* 1110 */       if (localManifest != null) {
/* 1111 */         Attributes localAttributes = localManifest.getMainAttributes();
/* 1112 */         if (localAttributes != null) {
/* 1113 */           String str2 = localAttributes.getValue(Attributes.Name.CLASS_PATH);
/* 1114 */           if (str2 != null) {
/* 1115 */             StringTokenizer localStringTokenizer = new StringTokenizer(str2);
/* 1116 */             while (localStringTokenizer.hasMoreTokens()) {
/* 1117 */               String str3 = localStringTokenizer.nextToken();
/* 1118 */               if (!str3.endsWith("/")) {
/* 1119 */                 str3 = str1.concat(str3);
/*      */ 
/* 1121 */                 if (!this.jarPaths.contains(str3)) {
/* 1122 */                   localArrayList.addAll(getJarPath(str3));
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1130 */     localJarFile.close();
/* 1131 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   void genIndex(String paramString, String[] paramArrayOfString)
/*      */     throws IOException
/*      */   {
/* 1138 */     List localList = getJarPath(paramString);
/* 1139 */     int i = localList.size();
/*      */ 
/* 1142 */     if ((i == 1) && (paramArrayOfString != null))
/*      */     {
/* 1145 */       for (int j = 0; j < paramArrayOfString.length; j++) {
/* 1146 */         localList.addAll(getJarPath(paramArrayOfString[j]));
/*      */       }
/* 1148 */       i = localList.size();
/*      */     }
/* 1150 */     String[] arrayOfString = (String[])localList.toArray(new String[i]);
/* 1151 */     JarIndex localJarIndex = new JarIndex(arrayOfString);
/* 1152 */     dumpIndex(paramString, localJarIndex);
/*      */   }
/*      */ 
/*      */   void printEntry(ZipEntry paramZipEntry, String[] paramArrayOfString)
/*      */     throws IOException
/*      */   {
/* 1159 */     if (paramArrayOfString == null) {
/* 1160 */       printEntry(paramZipEntry);
/*      */     } else {
/* 1162 */       String str1 = paramZipEntry.getName();
/* 1163 */       for (String str2 : paramArrayOfString)
/* 1164 */         if (str1.startsWith(str2)) {
/* 1165 */           printEntry(paramZipEntry);
/* 1166 */           return;
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   void printEntry(ZipEntry paramZipEntry)
/*      */     throws IOException
/*      */   {
/* 1176 */     if (this.vflag) {
/* 1177 */       StringBuilder localStringBuilder = new StringBuilder();
/* 1178 */       String str = Long.toString(paramZipEntry.getSize());
/* 1179 */       for (int i = 6 - str.length(); i > 0; i--) {
/* 1180 */         localStringBuilder.append(' ');
/*      */       }
/* 1182 */       localStringBuilder.append(str).append(' ').append(new Date(paramZipEntry.getTime()).toString());
/* 1183 */       localStringBuilder.append(' ').append(paramZipEntry.getName());
/* 1184 */       output(localStringBuilder.toString());
/*      */     } else {
/* 1186 */       output(paramZipEntry.getName());
/*      */     }
/*      */   }
/*      */ 
/*      */   void usageError()
/*      */   {
/* 1194 */     error(getMsg("usage"));
/*      */   }
/*      */ 
/*      */   void fatalError(Exception paramException)
/*      */   {
/* 1201 */     paramException.printStackTrace();
/*      */   }
/*      */ 
/*      */   void fatalError(String paramString)
/*      */   {
/* 1209 */     error(this.program + ": " + paramString);
/*      */   }
/*      */ 
/*      */   protected void output(String paramString)
/*      */   {
/* 1216 */     this.out.println(paramString);
/*      */   }
/*      */ 
/*      */   protected void error(String paramString)
/*      */   {
/* 1223 */     this.err.println(paramString);
/*      */   }
/*      */ 
/*      */   public static void main(String[] paramArrayOfString)
/*      */   {
/* 1230 */     Main localMain = new Main(System.out, System.err, "jar");
/* 1231 */     System.exit(localMain.run(paramArrayOfString) ? 0 : 1);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   89 */     useExtractionTime = Boolean.getBoolean("sun.tools.jar.useExtractionTime");
/*      */     try
/*      */     {
/*   97 */       rsrc = ResourceBundle.getBundle("sun.tools.jar.resources.jar");
/*      */     } catch (MissingResourceException localMissingResourceException) {
/*   99 */       throw new Error("Fatal: Resource for jar is missing");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CRC32OutputStream extends OutputStream
/*      */   {
/* 1240 */     final CRC32 crc = new CRC32();
/* 1241 */     long n = 0L;
/*      */ 
/*      */     public void write(int paramInt)
/*      */       throws IOException
/*      */     {
/* 1246 */       this.crc.update(paramInt);
/* 1247 */       this.n += 1L;
/*      */     }
/*      */ 
/*      */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 1251 */       this.crc.update(paramArrayOfByte, paramInt1, paramInt2);
/* 1252 */       this.n += paramInt2;
/*      */     }
/*      */ 
/*      */     public void updateEntry(ZipEntry paramZipEntry)
/*      */     {
/* 1260 */       paramZipEntry.setMethod(0);
/* 1261 */       paramZipEntry.setSize(this.n);
/* 1262 */       paramZipEntry.setCrc(this.crc.getValue());
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tools.jar.Main
 * JD-Core Version:    0.6.2
 */