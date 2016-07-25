/*      */ package java.io;
/*      */ 
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.nio.file.FileSystems;
/*      */ import java.nio.file.Path;
/*      */ import java.security.AccessController;
/*      */ import java.security.SecureRandom;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class File
/*      */   implements Serializable, Comparable<File>
/*      */ {
/*  156 */   private static FileSystem fs = FileSystem.getFileSystem();
/*      */   private String path;
/*  175 */   private transient PathStatus status = null;
/*      */   private transient int prefixLength;
/*  215 */   public static final char separatorChar = fs.getSeparator();
/*      */ 
/*  222 */   public static final String separator = "" + separatorChar;
/*      */ 
/*  234 */   public static final char pathSeparatorChar = fs.getPathSeparator();
/*      */ 
/*  241 */   public static final String pathSeparator = "" + pathSeparatorChar;
/*      */   private static final long serialVersionUID = 301077366599181567L;
/*      */   private volatile transient Path filePath;
/*      */ 
/*      */   final boolean isInvalid()
/*      */   {
/*  186 */     if (this.status == null) {
/*  187 */       this.status = (this.path.indexOf(0) < 0 ? PathStatus.CHECKED : PathStatus.INVALID);
/*      */     }
/*      */ 
/*  190 */     return this.status == PathStatus.INVALID;
/*      */   }
/*      */ 
/*      */   int getPrefixLength()
/*      */   {
/*  204 */     return this.prefixLength;
/*      */   }
/*      */ 
/*      */   private File(String paramString, int paramInt)
/*      */   {
/*  250 */     this.path = paramString;
/*  251 */     this.prefixLength = paramInt;
/*      */   }
/*      */ 
/*      */   private File(String paramString, File paramFile)
/*      */   {
/*  260 */     assert (paramFile.path != null);
/*  261 */     assert (!paramFile.path.equals(""));
/*  262 */     this.path = fs.resolve(paramFile.path, paramString);
/*  263 */     this.prefixLength = paramFile.prefixLength;
/*      */   }
/*      */ 
/*      */   public File(String paramString)
/*      */   {
/*  276 */     if (paramString == null) {
/*  277 */       throw new NullPointerException();
/*      */     }
/*  279 */     this.path = fs.normalize(paramString);
/*  280 */     this.prefixLength = fs.prefixLength(this.path);
/*      */   }
/*      */ 
/*      */   public File(String paramString1, String paramString2)
/*      */   {
/*  316 */     if (paramString2 == null) {
/*  317 */       throw new NullPointerException();
/*      */     }
/*  319 */     if (paramString1 != null) {
/*  320 */       if (paramString1.equals("")) {
/*  321 */         this.path = fs.resolve(fs.getDefaultParent(), fs.normalize(paramString2));
/*      */       }
/*      */       else {
/*  324 */         this.path = fs.resolve(fs.normalize(paramString1), fs.normalize(paramString2));
/*      */       }
/*      */     }
/*      */     else {
/*  328 */       this.path = fs.normalize(paramString2);
/*      */     }
/*  330 */     this.prefixLength = fs.prefixLength(this.path);
/*      */   }
/*      */ 
/*      */   public File(File paramFile, String paramString)
/*      */   {
/*  359 */     if (paramString == null) {
/*  360 */       throw new NullPointerException();
/*      */     }
/*  362 */     if (paramFile != null) {
/*  363 */       if (paramFile.path.equals("")) {
/*  364 */         this.path = fs.resolve(fs.getDefaultParent(), fs.normalize(paramString));
/*      */       }
/*      */       else {
/*  367 */         this.path = fs.resolve(paramFile.path, fs.normalize(paramString));
/*      */       }
/*      */     }
/*      */     else {
/*  371 */       this.path = fs.normalize(paramString);
/*      */     }
/*  373 */     this.prefixLength = fs.prefixLength(this.path);
/*      */   }
/*      */ 
/*      */   public File(URI paramURI)
/*      */   {
/*  415 */     if (!paramURI.isAbsolute())
/*  416 */       throw new IllegalArgumentException("URI is not absolute");
/*  417 */     if (paramURI.isOpaque())
/*  418 */       throw new IllegalArgumentException("URI is not hierarchical");
/*  419 */     String str1 = paramURI.getScheme();
/*  420 */     if ((str1 == null) || (!str1.equalsIgnoreCase("file")))
/*  421 */       throw new IllegalArgumentException("URI scheme is not \"file\"");
/*  422 */     if (paramURI.getAuthority() != null)
/*  423 */       throw new IllegalArgumentException("URI has an authority component");
/*  424 */     if (paramURI.getFragment() != null)
/*  425 */       throw new IllegalArgumentException("URI has a fragment component");
/*  426 */     if (paramURI.getQuery() != null)
/*  427 */       throw new IllegalArgumentException("URI has a query component");
/*  428 */     String str2 = paramURI.getPath();
/*  429 */     if (str2.equals("")) {
/*  430 */       throw new IllegalArgumentException("URI path component is empty");
/*      */     }
/*      */ 
/*  433 */     str2 = fs.fromURIPath(str2);
/*  434 */     if (separatorChar != '/')
/*  435 */       str2 = str2.replace('/', separatorChar);
/*  436 */     this.path = fs.normalize(str2);
/*  437 */     this.prefixLength = fs.prefixLength(this.path);
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  454 */     int i = this.path.lastIndexOf(separatorChar);
/*  455 */     if (i < this.prefixLength) return this.path.substring(this.prefixLength);
/*  456 */     return this.path.substring(i + 1);
/*      */   }
/*      */ 
/*      */   public String getParent()
/*      */   {
/*  473 */     int i = this.path.lastIndexOf(separatorChar);
/*  474 */     if (i < this.prefixLength) {
/*  475 */       if ((this.prefixLength > 0) && (this.path.length() > this.prefixLength))
/*  476 */         return this.path.substring(0, this.prefixLength);
/*  477 */       return null;
/*      */     }
/*  479 */     return this.path.substring(0, i);
/*      */   }
/*      */ 
/*      */   public File getParentFile()
/*      */   {
/*  499 */     String str = getParent();
/*  500 */     if (str == null) return null;
/*  501 */     return new File(str, this.prefixLength);
/*      */   }
/*      */ 
/*      */   public String getPath()
/*      */   {
/*  512 */     return this.path;
/*      */   }
/*      */ 
/*      */   public boolean isAbsolute()
/*      */   {
/*  529 */     return fs.isAbsolute(this);
/*      */   }
/*      */ 
/*      */   public String getAbsolutePath()
/*      */   {
/*  556 */     return fs.resolve(this);
/*      */   }
/*      */ 
/*      */   public File getAbsoluteFile()
/*      */   {
/*  572 */     String str = getAbsolutePath();
/*  573 */     return new File(str, fs.prefixLength(str));
/*      */   }
/*      */ 
/*      */   public String getCanonicalPath()
/*      */     throws IOException
/*      */   {
/*  615 */     if (isInvalid()) {
/*  616 */       throw new IOException("Invalid file path");
/*      */     }
/*  618 */     return fs.canonicalize(fs.resolve(this));
/*      */   }
/*      */ 
/*      */   public File getCanonicalFile()
/*      */     throws IOException
/*      */   {
/*  643 */     String str = getCanonicalPath();
/*  644 */     return new File(str, fs.prefixLength(str));
/*      */   }
/*      */ 
/*      */   private static String slashify(String paramString, boolean paramBoolean) {
/*  648 */     String str = paramString;
/*  649 */     if (separatorChar != '/')
/*  650 */       str = str.replace(separatorChar, '/');
/*  651 */     if (!str.startsWith("/"))
/*  652 */       str = "/" + str;
/*  653 */     if ((!str.endsWith("/")) && (paramBoolean))
/*  654 */       str = str + "/";
/*  655 */     return str;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public URL toURL()
/*      */     throws MalformedURLException
/*      */   {
/*  683 */     if (isInvalid()) {
/*  684 */       throw new MalformedURLException("Invalid file path");
/*      */     }
/*  686 */     return new URL("file", "", slashify(getAbsolutePath(), isDirectory()));
/*      */   }
/*      */ 
/*      */   public URI toURI()
/*      */   {
/*      */     try
/*      */     {
/*  731 */       File localFile = getAbsoluteFile();
/*  732 */       String str = slashify(localFile.getPath(), localFile.isDirectory());
/*  733 */       if (str.startsWith("//"))
/*  734 */         str = "//" + str;
/*  735 */       return new URI("file", null, str, null);
/*      */     } catch (URISyntaxException localURISyntaxException) {
/*  737 */       throw new Error(localURISyntaxException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean canRead()
/*      */   {
/*  758 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  759 */     if (localSecurityManager != null) {
/*  760 */       localSecurityManager.checkRead(this.path);
/*      */     }
/*  762 */     if (isInvalid()) {
/*  763 */       return false;
/*      */     }
/*  765 */     return fs.checkAccess(this, 4);
/*      */   }
/*      */ 
/*      */   public boolean canWrite()
/*      */   {
/*  783 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  784 */     if (localSecurityManager != null) {
/*  785 */       localSecurityManager.checkWrite(this.path);
/*      */     }
/*  787 */     if (isInvalid()) {
/*  788 */       return false;
/*      */     }
/*  790 */     return fs.checkAccess(this, 2);
/*      */   }
/*      */ 
/*      */   public boolean exists()
/*      */   {
/*  806 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  807 */     if (localSecurityManager != null) {
/*  808 */       localSecurityManager.checkRead(this.path);
/*      */     }
/*  810 */     if (isInvalid()) {
/*  811 */       return false;
/*      */     }
/*  813 */     return (fs.getBooleanAttributes(this) & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   public boolean isDirectory()
/*      */   {
/*  836 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  837 */     if (localSecurityManager != null) {
/*  838 */       localSecurityManager.checkRead(this.path);
/*      */     }
/*  840 */     if (isInvalid()) {
/*  841 */       return false;
/*      */     }
/*  843 */     return (fs.getBooleanAttributes(this) & 0x4) != 0;
/*      */   }
/*      */ 
/*      */   public boolean isFile()
/*      */   {
/*  869 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  870 */     if (localSecurityManager != null) {
/*  871 */       localSecurityManager.checkRead(this.path);
/*      */     }
/*  873 */     if (isInvalid()) {
/*  874 */       return false;
/*      */     }
/*  876 */     return (fs.getBooleanAttributes(this) & 0x2) != 0;
/*      */   }
/*      */ 
/*      */   public boolean isHidden()
/*      */   {
/*  898 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  899 */     if (localSecurityManager != null) {
/*  900 */       localSecurityManager.checkRead(this.path);
/*      */     }
/*  902 */     if (isInvalid()) {
/*  903 */       return false;
/*      */     }
/*  905 */     return (fs.getBooleanAttributes(this) & 0x8) != 0;
/*      */   }
/*      */ 
/*      */   public long lastModified()
/*      */   {
/*  930 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  931 */     if (localSecurityManager != null) {
/*  932 */       localSecurityManager.checkRead(this.path);
/*      */     }
/*  934 */     if (isInvalid()) {
/*  935 */       return 0L;
/*      */     }
/*  937 */     return fs.getLastModifiedTime(this);
/*      */   }
/*      */ 
/*      */   public long length()
/*      */   {
/*  961 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  962 */     if (localSecurityManager != null) {
/*  963 */       localSecurityManager.checkRead(this.path);
/*      */     }
/*  965 */     if (isInvalid()) {
/*  966 */       return 0L;
/*      */     }
/*  968 */     return fs.getLength(this);
/*      */   }
/*      */ 
/*      */   public boolean createNewFile()
/*      */     throws IOException
/*      */   {
/* 1001 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1002 */     if (localSecurityManager != null) localSecurityManager.checkWrite(this.path);
/* 1003 */     if (isInvalid()) {
/* 1004 */       throw new IOException("Invalid file path");
/*      */     }
/* 1006 */     return fs.createFileExclusively(this.path);
/*      */   }
/*      */ 
/*      */   public boolean delete()
/*      */   {
/* 1028 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1029 */     if (localSecurityManager != null) {
/* 1030 */       localSecurityManager.checkDelete(this.path);
/*      */     }
/* 1032 */     if (isInvalid()) {
/* 1033 */       return false;
/*      */     }
/* 1035 */     return fs.delete(this);
/*      */   }
/*      */ 
/*      */   public void deleteOnExit()
/*      */   {
/* 1066 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1067 */     if (localSecurityManager != null) {
/* 1068 */       localSecurityManager.checkDelete(this.path);
/*      */     }
/* 1070 */     if (isInvalid()) {
/* 1071 */       return;
/*      */     }
/* 1073 */     DeleteOnExitHook.add(this.path);
/*      */   }
/*      */ 
/*      */   public String[] list()
/*      */   {
/* 1109 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1110 */     if (localSecurityManager != null) {
/* 1111 */       localSecurityManager.checkRead(this.path);
/*      */     }
/* 1113 */     if (isInvalid()) {
/* 1114 */       return null;
/*      */     }
/* 1116 */     return fs.list(this);
/*      */   }
/*      */ 
/*      */   public String[] list(FilenameFilter paramFilenameFilter)
/*      */   {
/* 1149 */     String[] arrayOfString = list();
/* 1150 */     if ((arrayOfString == null) || (paramFilenameFilter == null)) {
/* 1151 */       return arrayOfString;
/*      */     }
/* 1153 */     ArrayList localArrayList = new ArrayList();
/* 1154 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 1155 */       if (paramFilenameFilter.accept(this, arrayOfString[i])) {
/* 1156 */         localArrayList.add(arrayOfString[i]);
/*      */       }
/*      */     }
/* 1159 */     return (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*      */   }
/*      */ 
/*      */   public File[] listFiles()
/*      */   {
/* 1201 */     String[] arrayOfString = list();
/* 1202 */     if (arrayOfString == null) return null;
/* 1203 */     int i = arrayOfString.length;
/* 1204 */     File[] arrayOfFile = new File[i];
/* 1205 */     for (int j = 0; j < i; j++) {
/* 1206 */       arrayOfFile[j] = new File(arrayOfString[j], this);
/*      */     }
/* 1208 */     return arrayOfFile;
/*      */   }
/*      */ 
/*      */   public File[] listFiles(FilenameFilter paramFilenameFilter)
/*      */   {
/* 1242 */     String[] arrayOfString1 = list();
/* 1243 */     if (arrayOfString1 == null) return null;
/* 1244 */     ArrayList localArrayList = new ArrayList();
/* 1245 */     for (String str : arrayOfString1)
/* 1246 */       if ((paramFilenameFilter == null) || (paramFilenameFilter.accept(this, str)))
/* 1247 */         localArrayList.add(new File(str, this));
/* 1248 */     return (File[])localArrayList.toArray(new File[localArrayList.size()]);
/*      */   }
/*      */ 
/*      */   public File[] listFiles(FileFilter paramFileFilter)
/*      */   {
/* 1280 */     String[] arrayOfString1 = list();
/* 1281 */     if (arrayOfString1 == null) return null;
/* 1282 */     ArrayList localArrayList = new ArrayList();
/* 1283 */     for (String str : arrayOfString1) {
/* 1284 */       File localFile = new File(str, this);
/* 1285 */       if ((paramFileFilter == null) || (paramFileFilter.accept(localFile)))
/* 1286 */         localArrayList.add(localFile);
/*      */     }
/* 1288 */     return (File[])localArrayList.toArray(new File[localArrayList.size()]);
/*      */   }
/*      */ 
/*      */   public boolean mkdir()
/*      */   {
/* 1303 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1304 */     if (localSecurityManager != null) {
/* 1305 */       localSecurityManager.checkWrite(this.path);
/*      */     }
/* 1307 */     if (isInvalid()) {
/* 1308 */       return false;
/*      */     }
/* 1310 */     return fs.createDirectory(this);
/*      */   }
/*      */ 
/*      */   public boolean mkdirs()
/*      */   {
/* 1334 */     if (exists()) {
/* 1335 */       return false;
/*      */     }
/* 1337 */     if (mkdir()) {
/* 1338 */       return true;
/*      */     }
/* 1340 */     File localFile1 = null;
/*      */     try {
/* 1342 */       localFile1 = getCanonicalFile();
/*      */     } catch (IOException localIOException) {
/* 1344 */       return false;
/*      */     }
/*      */ 
/* 1347 */     File localFile2 = localFile1.getParentFile();
/* 1348 */     return (localFile2 != null) && ((localFile2.mkdirs()) || (localFile2.exists())) && (localFile1.mkdir());
/*      */   }
/*      */ 
/*      */   public boolean renameTo(File paramFile)
/*      */   {
/* 1380 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1381 */     if (localSecurityManager != null) {
/* 1382 */       localSecurityManager.checkWrite(this.path);
/* 1383 */       localSecurityManager.checkWrite(paramFile.path);
/*      */     }
/* 1385 */     if (paramFile == null) {
/* 1386 */       throw new NullPointerException();
/*      */     }
/* 1388 */     if ((isInvalid()) || (paramFile.isInvalid())) {
/* 1389 */       return false;
/*      */     }
/* 1391 */     return fs.rename(this, paramFile);
/*      */   }
/*      */ 
/*      */   public boolean setLastModified(long paramLong)
/*      */   {
/* 1421 */     if (paramLong < 0L) throw new IllegalArgumentException("Negative time");
/* 1422 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1423 */     if (localSecurityManager != null) {
/* 1424 */       localSecurityManager.checkWrite(this.path);
/*      */     }
/* 1426 */     if (isInvalid()) {
/* 1427 */       return false;
/*      */     }
/* 1429 */     return fs.setLastModifiedTime(this, paramLong);
/*      */   }
/*      */ 
/*      */   public boolean setReadOnly()
/*      */   {
/* 1450 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1451 */     if (localSecurityManager != null) {
/* 1452 */       localSecurityManager.checkWrite(this.path);
/*      */     }
/* 1454 */     if (isInvalid()) {
/* 1455 */       return false;
/*      */     }
/* 1457 */     return fs.setReadOnly(this);
/*      */   }
/*      */ 
/*      */   public boolean setWritable(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1491 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1492 */     if (localSecurityManager != null) {
/* 1493 */       localSecurityManager.checkWrite(this.path);
/*      */     }
/* 1495 */     if (isInvalid()) {
/* 1496 */       return false;
/*      */     }
/* 1498 */     return fs.setPermission(this, 2, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public boolean setWritable(boolean paramBoolean)
/*      */   {
/* 1527 */     return setWritable(paramBoolean, true);
/*      */   }
/*      */ 
/*      */   public boolean setReadable(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1564 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1565 */     if (localSecurityManager != null) {
/* 1566 */       localSecurityManager.checkWrite(this.path);
/*      */     }
/* 1568 */     if (isInvalid()) {
/* 1569 */       return false;
/*      */     }
/* 1571 */     return fs.setPermission(this, 4, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public boolean setReadable(boolean paramBoolean)
/*      */   {
/* 1603 */     return setReadable(paramBoolean, true);
/*      */   }
/*      */ 
/*      */   public boolean setExecutable(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1640 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1641 */     if (localSecurityManager != null) {
/* 1642 */       localSecurityManager.checkWrite(this.path);
/*      */     }
/* 1644 */     if (isInvalid()) {
/* 1645 */       return false;
/*      */     }
/* 1647 */     return fs.setPermission(this, 1, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public boolean setExecutable(boolean paramBoolean)
/*      */   {
/* 1679 */     return setExecutable(paramBoolean, true);
/*      */   }
/*      */ 
/*      */   public boolean canExecute()
/*      */   {
/* 1697 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1698 */     if (localSecurityManager != null) {
/* 1699 */       localSecurityManager.checkExec(this.path);
/*      */     }
/* 1701 */     if (isInvalid()) {
/* 1702 */       return false;
/*      */     }
/* 1704 */     return fs.checkAccess(this, 1);
/*      */   }
/*      */ 
/*      */   public static File[] listRoots()
/*      */   {
/* 1753 */     return fs.listRoots();
/*      */   }
/*      */ 
/*      */   public long getTotalSpace()
/*      */   {
/* 1775 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1776 */     if (localSecurityManager != null) {
/* 1777 */       localSecurityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
/* 1778 */       localSecurityManager.checkRead(this.path);
/*      */     }
/* 1780 */     if (isInvalid()) {
/* 1781 */       return 0L;
/*      */     }
/* 1783 */     return fs.getSpace(this, 0);
/*      */   }
/*      */ 
/*      */   public long getFreeSpace()
/*      */   {
/* 1813 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1814 */     if (localSecurityManager != null) {
/* 1815 */       localSecurityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
/* 1816 */       localSecurityManager.checkRead(this.path);
/*      */     }
/* 1818 */     if (isInvalid()) {
/* 1819 */       return 0L;
/*      */     }
/* 1821 */     return fs.getSpace(this, 1);
/*      */   }
/*      */ 
/*      */   public long getUsableSpace()
/*      */   {
/* 1854 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1855 */     if (localSecurityManager != null) {
/* 1856 */       localSecurityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
/* 1857 */       localSecurityManager.checkRead(this.path);
/*      */     }
/* 1859 */     if (isInvalid()) {
/* 1860 */       return 0L;
/*      */     }
/* 1862 */     return fs.getSpace(this, 2);
/*      */   }
/*      */ 
/*      */   public static File createTempFile(String paramString1, String paramString2, File paramFile)
/*      */     throws IOException
/*      */   {
/* 1977 */     if (paramString1.length() < 3)
/* 1978 */       throw new IllegalArgumentException("Prefix string too short");
/* 1979 */     if (paramString2 == null) {
/* 1980 */       paramString2 = ".tmp";
/* 1982 */     }File localFile1 = paramFile != null ? paramFile : TempDirectory.location();
/*      */ 
/* 1984 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*      */     File localFile2;
/*      */     do { localFile2 = TempDirectory.generateFile(paramString1, paramString2, localFile1);
/*      */ 
/* 1989 */       if (localSecurityManager != null)
/*      */         try {
/* 1991 */           localSecurityManager.checkWrite(localFile2.getPath());
/*      */         }
/*      */         catch (SecurityException localSecurityException) {
/* 1994 */           if (paramFile == null)
/* 1995 */             throw new SecurityException("Unable to create temporary file");
/* 1996 */           throw localSecurityException;
/*      */         }
/*      */     }
/* 1999 */     while ((fs.getBooleanAttributes(localFile2) & 0x1) != 0);
/*      */ 
/* 2001 */     if (!fs.createFileExclusively(localFile2.getPath())) {
/* 2002 */       throw new IOException("Unable to create temporary file");
/*      */     }
/* 2004 */     return localFile2;
/*      */   }
/*      */ 
/*      */   public static File createTempFile(String paramString1, String paramString2)
/*      */     throws IOException
/*      */   {
/* 2047 */     return createTempFile(paramString1, paramString2, null);
/*      */   }
/*      */ 
/*      */   public int compareTo(File paramFile)
/*      */   {
/* 2070 */     return fs.compare(this, paramFile);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 2088 */     if ((paramObject != null) && ((paramObject instanceof File))) {
/* 2089 */       return compareTo((File)paramObject) == 0;
/*      */     }
/* 2091 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 2109 */     return fs.hashCode(this);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2119 */     return getPath();
/*      */   }
/*      */ 
/*      */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 2132 */     paramObjectOutputStream.defaultWriteObject();
/* 2133 */     paramObjectOutputStream.writeChar(separatorChar);
/*      */   }
/*      */ 
/*      */   private synchronized void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2145 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 2146 */     String str = (String)localGetField.get("path", null);
/* 2147 */     char c = paramObjectInputStream.readChar();
/* 2148 */     if (c != separatorChar)
/* 2149 */       str = str.replace(c, separatorChar);
/* 2150 */     this.path = fs.normalize(str);
/* 2151 */     this.prefixLength = fs.prefixLength(this.path);
/*      */   }
/*      */ 
/*      */   public Path toPath()
/*      */   {
/* 2188 */     Path localPath = this.filePath;
/* 2189 */     if (localPath == null) {
/* 2190 */       synchronized (this) {
/* 2191 */         localPath = this.filePath;
/* 2192 */         if (localPath == null) {
/* 2193 */           localPath = FileSystems.getDefault().getPath(this.path, new String[0]);
/* 2194 */           this.filePath = localPath;
/*      */         }
/*      */       }
/*      */     }
/* 2198 */     return localPath;
/*      */   }
/*      */ 
/*      */   private static enum PathStatus
/*      */   {
/*  170 */     INVALID, CHECKED;
/*      */   }
/*      */ 
/*      */   private static class TempDirectory
/*      */   {
/* 1871 */     private static final File tmpdir = new File((String)AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")));
/*      */ 
/* 1878 */     private static final SecureRandom random = new SecureRandom();
/*      */ 
/*      */     static File location()
/*      */     {
/* 1874 */       return tmpdir;
/*      */     }
/*      */ 
/*      */     static File generateFile(String paramString1, String paramString2, File paramFile)
/*      */       throws IOException
/*      */     {
/* 1882 */       long l = random.nextLong();
/* 1883 */       if (l == -9223372036854775808L)
/* 1884 */         l = 0L;
/*      */       else {
/* 1886 */         l = Math.abs(l);
/*      */       }
/*      */ 
/* 1890 */       paramString1 = new File(paramString1).getName();
/*      */ 
/* 1892 */       String str = paramString1 + Long.toString(l) + paramString2;
/* 1893 */       File localFile = new File(paramFile, str);
/* 1894 */       if ((!str.equals(localFile.getName())) || (localFile.isInvalid())) {
/* 1895 */         if (System.getSecurityManager() != null) {
/* 1896 */           throw new IOException("Unable to create temporary file");
/*      */         }
/* 1898 */         throw new IOException("Unable to create temporary file, " + localFile);
/*      */       }
/* 1900 */       return localFile;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.File
 * JD-Core Version:    0.6.2
 */