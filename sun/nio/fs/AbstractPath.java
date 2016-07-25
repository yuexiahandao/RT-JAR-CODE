/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.WatchEvent.Kind;
/*     */ import java.nio.file.WatchEvent.Modifier;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ abstract class AbstractPath
/*     */   implements Path
/*     */ {
/*     */   public final boolean startsWith(String paramString)
/*     */   {
/*  43 */     return startsWith(getFileSystem().getPath(paramString, new String[0]));
/*     */   }
/*     */ 
/*     */   public final boolean endsWith(String paramString)
/*     */   {
/*  48 */     return endsWith(getFileSystem().getPath(paramString, new String[0]));
/*     */   }
/*     */ 
/*     */   public final Path resolve(String paramString)
/*     */   {
/*  53 */     return resolve(getFileSystem().getPath(paramString, new String[0]));
/*     */   }
/*     */ 
/*     */   public final Path resolveSibling(Path paramPath)
/*     */   {
/*  58 */     if (paramPath == null)
/*  59 */       throw new NullPointerException();
/*  60 */     Path localPath = getParent();
/*  61 */     return localPath == null ? paramPath : localPath.resolve(paramPath);
/*     */   }
/*     */ 
/*     */   public final Path resolveSibling(String paramString)
/*     */   {
/*  66 */     return resolveSibling(getFileSystem().getPath(paramString, new String[0]));
/*     */   }
/*     */ 
/*     */   public final Iterator<Path> iterator()
/*     */   {
/*  71 */     return new Iterator() {
/*  72 */       private int i = 0;
/*     */ 
/*     */       public boolean hasNext() {
/*  75 */         return this.i < AbstractPath.this.getNameCount();
/*     */       }
/*     */ 
/*     */       public Path next() {
/*  79 */         if (this.i < AbstractPath.this.getNameCount()) {
/*  80 */           Path localPath = AbstractPath.this.getName(this.i);
/*  81 */           this.i += 1;
/*  82 */           return localPath;
/*     */         }
/*  84 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       public void remove()
/*     */       {
/*  89 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public final File toFile()
/*     */   {
/*  96 */     return new File(toString());
/*     */   }
/*     */ 
/*     */   public final WatchKey register(WatchService paramWatchService, WatchEvent.Kind<?>[] paramArrayOfKind)
/*     */     throws IOException
/*     */   {
/* 104 */     return register(paramWatchService, paramArrayOfKind, new WatchEvent.Modifier[0]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.AbstractPath
 * JD-Core Version:    0.6.2
 */