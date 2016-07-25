/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryIteratorException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.DirectoryStream.Filter;
/*     */ import java.nio.file.NotDirectoryException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class WindowsDirectoryStream
/*     */   implements DirectoryStream<Path>
/*     */ {
/*     */   private final WindowsPath dir;
/*     */   private final DirectoryStream.Filter<? super Path> filter;
/*     */   private final long handle;
/*     */   private final String firstName;
/*     */   private final NativeBuffer findDataBuffer;
/*  55 */   private final Object closeLock = new Object();
/*     */ 
/*  58 */   private boolean isOpen = true;
/*     */   private Iterator<Path> iterator;
/*     */ 
/*     */   WindowsDirectoryStream(WindowsPath paramWindowsPath, DirectoryStream.Filter<? super Path> paramFilter)
/*     */     throws IOException
/*     */   {
/*  65 */     this.dir = paramWindowsPath;
/*  66 */     this.filter = paramFilter;
/*     */     try
/*     */     {
/*  70 */       String str = paramWindowsPath.getPathForWin32Calls();
/*  71 */       int i = str.charAt(str.length() - 1);
/*  72 */       if ((i == 58) || (i == 92))
/*  73 */         str = str + "*";
/*     */       else {
/*  75 */         str = str + "\\*";
/*     */       }
/*     */ 
/*  78 */       WindowsNativeDispatcher.FirstFile localFirstFile = WindowsNativeDispatcher.FindFirstFile(str);
/*  79 */       this.handle = localFirstFile.handle();
/*  80 */       this.firstName = localFirstFile.name();
/*  81 */       this.findDataBuffer = WindowsFileAttributes.getBufferForFindData();
/*     */     } catch (WindowsException localWindowsException) {
/*  83 */       if (localWindowsException.lastError() == 267) {
/*  84 */         throw new NotDirectoryException(paramWindowsPath.getPathForExceptionMessage());
/*     */       }
/*  86 */       localWindowsException.rethrowAsIOException(paramWindowsPath);
/*     */ 
/*  89 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  97 */     synchronized (this.closeLock) {
/*  98 */       if (!this.isOpen)
/*  99 */         return;
/* 100 */       this.isOpen = false;
/*     */     }
/* 102 */     this.findDataBuffer.release();
/*     */     try {
/* 104 */       WindowsNativeDispatcher.FindClose(this.handle);
/*     */     } catch (WindowsException localWindowsException) {
/* 106 */       localWindowsException.rethrowAsIOException(this.dir);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator<Path> iterator()
/*     */   {
/* 112 */     if (!this.isOpen) {
/* 113 */       throw new IllegalStateException("Directory stream is closed");
/*     */     }
/* 115 */     synchronized (this) {
/* 116 */       if (this.iterator != null)
/* 117 */         throw new IllegalStateException("Iterator already obtained");
/* 118 */       this.iterator = new WindowsDirectoryIterator(this.firstName);
/* 119 */       return this.iterator;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class WindowsDirectoryIterator
/*     */     implements Iterator<Path>
/*     */   {
/* 130 */     private boolean atEof = false;
/*     */     private String first;
/*     */     private Path nextEntry;
/*     */     private String prefix;
/*     */ 
/*     */     WindowsDirectoryIterator(String arg2)
/*     */     {
/*     */       Object localObject;
/* 131 */       this.first = localObject;
/* 132 */       if (WindowsDirectoryStream.this.dir.needsSlashWhenResolving())
/* 133 */         this.prefix = (WindowsDirectoryStream.this.dir.toString() + "\\");
/*     */       else
/* 135 */         this.prefix = WindowsDirectoryStream.this.dir.toString();
/*     */     }
/*     */ 
/*     */     private boolean isSelfOrParent(String paramString)
/*     */     {
/* 141 */       return (paramString.equals(".")) || (paramString.equals(".."));
/*     */     }
/*     */ 
/*     */     private Path acceptEntry(String paramString, BasicFileAttributes paramBasicFileAttributes)
/*     */     {
/* 146 */       WindowsPath localWindowsPath = WindowsPath.createFromNormalizedPath(WindowsDirectoryStream.this.dir.getFileSystem(), this.prefix + paramString, paramBasicFileAttributes);
/*     */       try
/*     */       {
/* 149 */         if (WindowsDirectoryStream.this.filter.accept(localWindowsPath))
/* 150 */           return localWindowsPath;
/*     */       } catch (IOException localIOException) {
/* 152 */         throw new DirectoryIteratorException(localIOException);
/*     */       }
/* 154 */       return null;
/*     */     }
/*     */ 
/*     */     private Path readNextEntry()
/*     */     {
/* 160 */       if (this.first != null) {
/* 161 */         this.nextEntry = (isSelfOrParent(this.first) ? null : acceptEntry(this.first, null));
/* 162 */         this.first = null;
/* 163 */         if (this.nextEntry != null)
/* 164 */           return this.nextEntry;
/*     */       }
/*     */       while (true)
/*     */       {
/* 168 */         String str = null;
/*     */         WindowsFileAttributes localWindowsFileAttributes;
/* 172 */         synchronized (WindowsDirectoryStream.this.closeLock) {
/*     */           try {
/* 174 */             if (WindowsDirectoryStream.this.isOpen)
/* 175 */               str = WindowsNativeDispatcher.FindNextFile(WindowsDirectoryStream.this.handle, WindowsDirectoryStream.this.findDataBuffer.address());
/*     */           }
/*     */           catch (WindowsException localWindowsException) {
/* 178 */             IOException localIOException = localWindowsException.asIOException(WindowsDirectoryStream.this.dir);
/* 179 */             throw new DirectoryIteratorException(localIOException);
/*     */           }
/*     */ 
/* 183 */           if (str == null) {
/* 184 */             this.atEof = true;
/* 185 */             return null;
/*     */           }
/*     */ 
/* 189 */           if (isSelfOrParent(str))
/*     */           {
/*     */             continue;
/*     */           }
/*     */ 
/* 195 */           localWindowsFileAttributes = WindowsFileAttributes.fromFindData(WindowsDirectoryStream.this.findDataBuffer.address());
/*     */         }
/*     */ 
/* 200 */         ??? = acceptEntry(str, localWindowsFileAttributes);
/* 201 */         if (??? != null)
/* 202 */           return ???;
/*     */       }
/*     */     }
/*     */ 
/*     */     public synchronized boolean hasNext()
/*     */     {
/* 208 */       if ((this.nextEntry == null) && (!this.atEof))
/* 209 */         this.nextEntry = readNextEntry();
/* 210 */       return this.nextEntry != null;
/*     */     }
/*     */ 
/*     */     public synchronized Path next()
/*     */     {
/* 215 */       Path localPath = null;
/* 216 */       if ((this.nextEntry == null) && (!this.atEof)) {
/* 217 */         localPath = readNextEntry();
/*     */       } else {
/* 219 */         localPath = this.nextEntry;
/* 220 */         this.nextEntry = null;
/*     */       }
/* 222 */       if (localPath == null)
/* 223 */         throw new NoSuchElementException();
/* 224 */       return localPath;
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 229 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsDirectoryStream
 * JD-Core Version:    0.6.2
 */