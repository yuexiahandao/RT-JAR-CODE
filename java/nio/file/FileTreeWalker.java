/*     */ package java.nio.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import sun.nio.fs.BasicFileAttributesHolder;
/*     */ 
/*     */ class FileTreeWalker
/*     */ {
/*     */   private final boolean followLinks;
/*     */   private final LinkOption[] linkOptions;
/*     */   private final FileVisitor<? super Path> visitor;
/*     */   private final int maxDepth;
/*     */ 
/*     */   FileTreeWalker(Set<FileVisitOption> paramSet, FileVisitor<? super Path> paramFileVisitor, int paramInt)
/*     */   {
/*  49 */     boolean bool = false;
/*  50 */     for (FileVisitOption localFileVisitOption : paramSet)
/*     */     {
/*  52 */       switch (1.$SwitchMap$java$nio$file$FileVisitOption[localFileVisitOption.ordinal()]) { case 1:
/*  53 */         bool = true; break;
/*     */       default:
/*  55 */         throw new AssertionError("Should not get here");
/*     */       }
/*     */     }
/*  58 */     this.followLinks = bool;
/*  59 */     this.linkOptions = new LinkOption[] { bool ? new LinkOption[0] : LinkOption.NOFOLLOW_LINKS };
/*     */ 
/*  61 */     this.visitor = paramFileVisitor;
/*  62 */     this.maxDepth = paramInt;
/*     */   }
/*     */ 
/*     */   void walk(Path paramPath)
/*     */     throws IOException
/*     */   {
/*  69 */     FileVisitResult localFileVisitResult = walk(paramPath, 0, new ArrayList());
/*     */ 
/*  72 */     Objects.requireNonNull(localFileVisitResult, "FileVisitor returned null");
/*     */   }
/*     */ 
/*     */   private FileVisitResult walk(Path paramPath, int paramInt, List<AncestorDirectory> paramList)
/*     */     throws IOException
/*     */   {
/*  89 */     Object localObject1 = null;
/*  90 */     if ((paramInt > 0) && ((paramPath instanceof BasicFileAttributesHolder)) && (System.getSecurityManager() == null))
/*     */     {
/*  94 */       localObject2 = ((BasicFileAttributesHolder)paramPath).get();
/*  95 */       if ((localObject2 != null) && ((!this.followLinks) || (!((BasicFileAttributes)localObject2).isSymbolicLink())))
/*  96 */         localObject1 = localObject2;
/*     */     }
/*  98 */     Object localObject2 = null;
/*     */ 
/* 102 */     if (localObject1 == null) {
/*     */       try {
/*     */         try {
/* 105 */           localObject1 = Files.readAttributes(paramPath, BasicFileAttributes.class, this.linkOptions);
/*     */         } catch (IOException localIOException1) {
/* 107 */           if (this.followLinks)
/*     */             try {
/* 109 */               localObject1 = Files.readAttributes(paramPath, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*     */             }
/*     */             catch (IOException localIOException2)
/*     */             {
/* 113 */               localObject2 = localIOException2;
/*     */             }
/*     */           else {
/* 116 */             localObject2 = localIOException1;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (SecurityException localSecurityException1)
/*     */       {
/* 122 */         if (paramInt == 0)
/* 123 */           throw localSecurityException1;
/* 124 */         return FileVisitResult.CONTINUE;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 129 */     if (localObject2 != null) {
/* 130 */       return this.visitor.visitFileFailed(paramPath, (IOException)localObject2);
/*     */     }
/*     */ 
/* 134 */     if ((paramInt >= this.maxDepth) || (!((BasicFileAttributes)localObject1).isDirectory()))
/* 135 */       return this.visitor.visitFile(paramPath, (BasicFileAttributes)localObject1);
/*     */     Object localObject3;
/*     */     Object localObject4;
/*     */     Object localObject6;
/* 139 */     if (this.followLinks) {
/* 140 */       localObject3 = ((BasicFileAttributes)localObject1).fileKey();
/*     */ 
/* 144 */       for (localObject4 = paramList.iterator(); ((Iterator)localObject4).hasNext(); ) { AncestorDirectory localAncestorDirectory = (AncestorDirectory)((Iterator)localObject4).next();
/* 145 */         localObject6 = localAncestorDirectory.fileKey();
/* 146 */         if ((localObject3 != null) && (localObject6 != null)) {
/* 147 */           if (localObject3.equals(localObject6))
/*     */           {
/* 149 */             return this.visitor.visitFileFailed(paramPath, new FileSystemLoopException(paramPath.toString()));
/*     */           }
/*     */         }
/*     */         else {
/* 153 */           boolean bool = false;
/*     */           try {
/* 155 */             bool = Files.isSameFile(paramPath, localAncestorDirectory.file());
/*     */           }
/*     */           catch (IOException localIOException6) {
/*     */           }
/*     */           catch (SecurityException localSecurityException3) {
/*     */           }
/* 161 */           if (bool)
/*     */           {
/* 163 */             return this.visitor.visitFileFailed(paramPath, new FileSystemLoopException(paramPath.toString()));
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 169 */       paramList.add(new AncestorDirectory(paramPath, localObject3));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 174 */       localObject3 = null;
/*     */       try
/*     */       {
/* 179 */         localObject3 = Files.newDirectoryStream(paramPath);
/*     */       } catch (IOException localIOException3) {
/* 181 */         return this.visitor.visitFileFailed(paramPath, localIOException3);
/*     */       }
/*     */       catch (SecurityException localSecurityException2) {
/* 184 */         return FileVisitResult.CONTINUE;
/*     */       }
/*     */ 
/* 188 */       Object localObject5 = null;
/*     */       try
/*     */       {
/* 192 */         localObject4 = this.visitor.preVisitDirectory(paramPath, (BasicFileAttributes)localObject1);
/* 193 */         if (localObject4 != FileVisitResult.CONTINUE) {
/* 194 */           localObject6 = localObject4;
/*     */           try
/*     */           {
/* 215 */             ((DirectoryStream)localObject3).close();
/*     */           }
/*     */           catch (IOException localIOException5) {
/* 218 */             if (localObject5 == null) {
/* 219 */               localObject5 = localIOException5;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 229 */           return localObject6;
/*     */         }
/*     */         try
/*     */         {
/* 198 */           for (localObject6 = ((DirectoryStream)localObject3).iterator(); ((Iterator)localObject6).hasNext(); 
/* 206 */             localObject4 == FileVisitResult.SKIP_SIBLINGS)
/*     */           {
/* 198 */             Path localPath = (Path)((Iterator)localObject6).next();
/* 199 */             localObject4 = walk(localPath, paramInt + 1, paramList);
/*     */ 
/* 202 */             if ((localObject4 == null) || (localObject4 == FileVisitResult.TERMINATE)) {
/* 203 */               Object localObject7 = localObject4;
/*     */               try
/*     */               {
/* 215 */                 ((DirectoryStream)localObject3).close();
/*     */               }
/*     */               catch (IOException localIOException7) {
/* 218 */                 if (localObject5 == null) {
/* 219 */                   localObject5 = localIOException7;
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/* 229 */               return localObject7;
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (DirectoryIteratorException localDirectoryIteratorException)
/*     */         {
/* 211 */           localObject5 = localDirectoryIteratorException.getCause();
/*     */         }
/*     */       } finally {
/*     */         try {
/* 215 */           ((DirectoryStream)localObject3).close();
/*     */         }
/*     */         catch (IOException localIOException8) {
/* 218 */           if (localObject5 == null) {
/* 219 */             localObject5 = localIOException8;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 224 */       return this.visitor.postVisitDirectory(paramPath, (IOException)localObject5);
/*     */     }
/*     */     finally
/*     */     {
/* 228 */       if (this.followLinks)
/* 229 */         paramList.remove(paramList.size() - 1); 
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class AncestorDirectory {
/*     */     private final Path dir;
/*     */     private final Object key;
/*     */ 
/* 238 */     AncestorDirectory(Path paramPath, Object paramObject) { this.dir = paramPath;
/* 239 */       this.key = paramObject; }
/*     */ 
/*     */     Path file() {
/* 242 */       return this.dir;
/*     */     }
/*     */     Object fileKey() {
/* 245 */       return this.key;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.FileTreeWalker
 * JD-Core Version:    0.6.2
 */