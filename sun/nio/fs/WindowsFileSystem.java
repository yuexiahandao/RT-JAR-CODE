/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.attribute.GroupPrincipal;
/*     */ import java.nio.file.attribute.UserPrincipal;
/*     */ import java.nio.file.attribute.UserPrincipalLookupService;
/*     */ import java.nio.file.attribute.UserPrincipalNotFoundException;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class WindowsFileSystem extends FileSystem
/*     */ {
/*     */   private final WindowsFileSystemProvider provider;
/*     */   private final String defaultDirectory;
/*     */   private final String defaultRoot;
/*     */   private final boolean supportsLinks;
/*     */   private final boolean supportsStreamEnumeration;
/* 230 */   private static final Set<String> supportedFileAttributeViews = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "basic", "dos", "acl", "owner", "user" })));
/*     */   private static final String GLOB_SYNTAX = "glob";
/*     */   private static final String REGEX_SYNTAX = "regex";
/*     */ 
/*     */   WindowsFileSystem(WindowsFileSystemProvider paramWindowsFileSystemProvider, String paramString)
/*     */   {
/*  54 */     this.provider = paramWindowsFileSystemProvider;
/*     */ 
/*  57 */     WindowsPathParser.Result localResult = WindowsPathParser.parse(paramString);
/*     */ 
/*  59 */     if ((localResult.type() != WindowsPathType.ABSOLUTE) && (localResult.type() != WindowsPathType.UNC))
/*     */     {
/*  61 */       throw new AssertionError("Default directory is not an absolute path");
/*  62 */     }this.defaultDirectory = localResult.path();
/*  63 */     this.defaultRoot = localResult.root();
/*     */ 
/*  65 */     GetPropertyAction localGetPropertyAction = new GetPropertyAction("os.version");
/*  66 */     String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  67 */     String[] arrayOfString = Util.split(str, '.');
/*  68 */     int i = Integer.parseInt(arrayOfString[0]);
/*  69 */     int j = Integer.parseInt(arrayOfString[1]);
/*     */ 
/*  72 */     this.supportsLinks = (i >= 6);
/*     */ 
/*  75 */     this.supportsStreamEnumeration = ((i >= 6) || ((i == 5) && (j >= 2)));
/*     */   }
/*     */ 
/*     */   String defaultDirectory()
/*     */   {
/*  80 */     return this.defaultDirectory;
/*     */   }
/*     */ 
/*     */   String defaultRoot() {
/*  84 */     return this.defaultRoot;
/*     */   }
/*     */ 
/*     */   boolean supportsLinks() {
/*  88 */     return this.supportsLinks;
/*     */   }
/*     */ 
/*     */   boolean supportsStreamEnumeration() {
/*  92 */     return this.supportsStreamEnumeration;
/*     */   }
/*     */ 
/*     */   public FileSystemProvider provider()
/*     */   {
/*  97 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public String getSeparator()
/*     */   {
/* 102 */     return "\\";
/*     */   }
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/* 117 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Iterable<Path> getRootDirectories()
/*     */   {
/* 122 */     int i = 0;
/*     */     try {
/* 124 */       i = WindowsNativeDispatcher.GetLogicalDrives();
/*     */     }
/*     */     catch (WindowsException localWindowsException) {
/* 127 */       throw new AssertionError(localWindowsException.getMessage());
/*     */     }
/*     */ 
/* 131 */     ArrayList localArrayList = new ArrayList();
/* 132 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 133 */     for (int j = 0; j <= 25; j++) {
/* 134 */       if ((i & 1 << j) != 0) {
/* 135 */         StringBuilder localStringBuilder = new StringBuilder(3);
/* 136 */         localStringBuilder.append((char)(65 + j));
/* 137 */         localStringBuilder.append(":\\");
/* 138 */         String str = localStringBuilder.toString();
/* 139 */         if (localSecurityManager != null) {
/*     */           try {
/* 141 */             localSecurityManager.checkRead(str);
/*     */           } catch (SecurityException localSecurityException) {
/* 143 */             continue;
/*     */           }
/*     */         }
/* 146 */         localArrayList.add(WindowsPath.createFromNormalizedPath(this, str));
/*     */       }
/*     */     }
/* 149 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public Iterable<FileStore> getFileStores()
/*     */   {
/* 214 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 215 */     if (localSecurityManager != null) {
/*     */       try {
/* 217 */         localSecurityManager.checkPermission(new RuntimePermission("getFileStoreAttributes"));
/*     */       } catch (SecurityException localSecurityException) {
/* 219 */         return Collections.emptyList();
/*     */       }
/*     */     }
/* 222 */     return new Iterable() {
/*     */       public Iterator<FileStore> iterator() {
/* 224 */         return new WindowsFileSystem.FileStoreIterator(WindowsFileSystem.this);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public Set<String> supportedFileAttributeViews()
/*     */   {
/* 235 */     return supportedFileAttributeViews;
/*     */   }
/*     */ 
/*     */   public final Path getPath(String paramString, String[] paramArrayOfString)
/*     */   {
/*     */     String str1;
/* 241 */     if (paramArrayOfString.length == 0) {
/* 242 */       str1 = paramString;
/*     */     } else {
/* 244 */       StringBuilder localStringBuilder = new StringBuilder();
/* 245 */       localStringBuilder.append(paramString);
/* 246 */       for (String str2 : paramArrayOfString) {
/* 247 */         if (str2.length() > 0) {
/* 248 */           if (localStringBuilder.length() > 0)
/* 249 */             localStringBuilder.append('\\');
/* 250 */           localStringBuilder.append(str2);
/*     */         }
/*     */       }
/* 253 */       str1 = localStringBuilder.toString();
/*     */     }
/* 255 */     return WindowsPath.parse(this, str1);
/*     */   }
/*     */ 
/*     */   public UserPrincipalLookupService getUserPrincipalLookupService()
/*     */   {
/* 260 */     return LookupService.instance;
/*     */   }
/*     */ 
/*     */   public PathMatcher getPathMatcher(String paramString)
/*     */   {
/* 286 */     int i = paramString.indexOf(':');
/* 287 */     if ((i <= 0) || (i == paramString.length()))
/* 288 */       throw new IllegalArgumentException();
/* 289 */     String str1 = paramString.substring(0, i);
/* 290 */     String str2 = paramString.substring(i + 1);
/*     */     String str3;
/* 293 */     if (str1.equals("glob")) {
/* 294 */       str3 = Globs.toWindowsRegexPattern(str2);
/*     */     }
/* 296 */     else if (str1.equals("regex"))
/* 297 */       str3 = str2;
/*     */     else {
/* 299 */       throw new UnsupportedOperationException("Syntax '" + str1 + "' not recognized");
/*     */     }
/*     */ 
/* 305 */     final Pattern localPattern = Pattern.compile(str3, 66);
/*     */ 
/* 309 */     return new PathMatcher()
/*     */     {
/*     */       public boolean matches(Path paramAnonymousPath) {
/* 312 */         return localPattern.matcher(paramAnonymousPath.toString()).matches();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public WatchService newWatchService()
/*     */     throws IOException
/*     */   {
/* 323 */     return new WindowsWatchService(this);
/*     */   }
/*     */ 
/*     */   private class FileStoreIterator
/*     */     implements Iterator<FileStore>
/*     */   {
/*     */     private final Iterator<Path> roots;
/*     */     private FileStore next;
/*     */ 
/*     */     FileStoreIterator()
/*     */     {
/* 160 */       this.roots = WindowsFileSystem.this.getRootDirectories().iterator();
/*     */     }
/*     */ 
/*     */     private FileStore readNext() {
/* 164 */       assert (Thread.holdsLock(this));
/*     */       while (true) {
/* 166 */         if (!this.roots.hasNext())
/* 167 */           return null;
/* 168 */         WindowsPath localWindowsPath = (WindowsPath)this.roots.next();
/*     */         try
/*     */         {
/* 171 */           localWindowsPath.checkRead(); } catch (SecurityException localSecurityException) {
/*     */         }
/* 173 */         continue;
/*     */         try
/*     */         {
/* 176 */           WindowsFileStore localWindowsFileStore = WindowsFileStore.create(localWindowsPath.toString(), true);
/* 177 */           if (localWindowsFileStore != null)
/* 178 */             return localWindowsFileStore;
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public synchronized boolean hasNext() {
/* 187 */       if (this.next != null)
/* 188 */         return true;
/* 189 */       this.next = readNext();
/* 190 */       return this.next != null;
/*     */     }
/*     */ 
/*     */     public synchronized FileStore next()
/*     */     {
/* 195 */       if (this.next == null)
/* 196 */         this.next = readNext();
/* 197 */       if (this.next == null) {
/* 198 */         throw new NoSuchElementException();
/*     */       }
/* 200 */       FileStore localFileStore = this.next;
/* 201 */       this.next = null;
/* 202 */       return localFileStore;
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 208 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LookupService
/*     */   {
/* 264 */     static final UserPrincipalLookupService instance = new UserPrincipalLookupService()
/*     */     {
/*     */       public UserPrincipal lookupPrincipalByName(String paramAnonymousString)
/*     */         throws IOException
/*     */       {
/* 270 */         return WindowsUserPrincipals.lookup(paramAnonymousString);
/*     */       }
/*     */ 
/*     */       public GroupPrincipal lookupPrincipalByGroupName(String paramAnonymousString)
/*     */         throws IOException
/*     */       {
/* 276 */         UserPrincipal localUserPrincipal = WindowsUserPrincipals.lookup(paramAnonymousString);
/* 277 */         if (!(localUserPrincipal instanceof GroupPrincipal))
/* 278 */           throw new UserPrincipalNotFoundException(paramAnonymousString);
/* 279 */         return (GroupPrincipal)localUserPrincipal;
/*     */       }
/* 264 */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsFileSystem
 * JD-Core Version:    0.6.2
 */