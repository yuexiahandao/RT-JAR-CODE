/*     */ package java.nio.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ 
/*     */ class CopyMoveHelper
/*     */ {
/*     */   private static CopyOption[] convertMoveToCopyOptions(CopyOption[] paramArrayOfCopyOption)
/*     */     throws AtomicMoveNotSupportedException
/*     */   {
/*  81 */     int i = paramArrayOfCopyOption.length;
/*  82 */     CopyOption[] arrayOfCopyOption = new CopyOption[i + 2];
/*  83 */     for (int j = 0; j < i; j++) {
/*  84 */       CopyOption localCopyOption = paramArrayOfCopyOption[j];
/*  85 */       if (localCopyOption == StandardCopyOption.ATOMIC_MOVE) {
/*  86 */         throw new AtomicMoveNotSupportedException(null, null, "Atomic move between providers is not supported");
/*     */       }
/*     */ 
/*  89 */       arrayOfCopyOption[j] = localCopyOption;
/*     */     }
/*  91 */     arrayOfCopyOption[i] = LinkOption.NOFOLLOW_LINKS;
/*  92 */     arrayOfCopyOption[(i + 1)] = StandardCopyOption.COPY_ATTRIBUTES;
/*  93 */     return arrayOfCopyOption;
/*     */   }
/*     */ 
/*     */   static void copyToForeignTarget(Path paramPath1, Path paramPath2, CopyOption[] paramArrayOfCopyOption)
/*     */     throws IOException
/*     */   {
/* 104 */     CopyOptions localCopyOptions = CopyOptions.parse(paramArrayOfCopyOption);
/* 105 */     LinkOption[] arrayOfLinkOption = { localCopyOptions.followLinks ? new LinkOption[0] : LinkOption.NOFOLLOW_LINKS };
/*     */ 
/* 109 */     BasicFileAttributes localBasicFileAttributes = Files.readAttributes(paramPath1, BasicFileAttributes.class, arrayOfLinkOption);
/*     */ 
/* 112 */     if (localBasicFileAttributes.isSymbolicLink()) {
/* 113 */       throw new IOException("Copying of symbolic links not supported");
/*     */     }
/*     */ 
/* 116 */     if (localCopyOptions.replaceExisting)
/* 117 */       Files.deleteIfExists(paramPath2);
/* 118 */     else if (Files.exists(paramPath2, new LinkOption[0]))
/* 119 */       throw new FileAlreadyExistsException(paramPath2.toString());
/*     */     Object localObject1;
/* 122 */     if (localBasicFileAttributes.isDirectory()) {
/* 123 */       Files.createDirectory(paramPath2, new FileAttribute[0]);
/*     */     } else {
/* 125 */       localObject1 = Files.newInputStream(paramPath1, new OpenOption[0]); Object localObject2 = null;
/*     */       try { Files.copy((InputStream)localObject1, paramPath2, new CopyOption[0]); }
/*     */       catch (Throwable localThrowable3)
/*     */       {
/* 125 */         localObject2 = localThrowable3; throw localThrowable3;
/*     */       } finally {
/* 127 */         if (localObject1 != null) if (localObject2 != null) try { ((InputStream)localObject1).close(); } catch (Throwable localThrowable5) { localObject2.addSuppressed(localThrowable5); } else ((InputStream)localObject1).close();
/*     */       }
/*     */     }
/*     */ 
/* 131 */     if (localCopyOptions.copyAttributes) {
/* 132 */       localObject1 = (BasicFileAttributeView)Files.getFileAttributeView(paramPath2, BasicFileAttributeView.class, arrayOfLinkOption);
/*     */       try
/*     */       {
/* 135 */         ((BasicFileAttributeView)localObject1).setTimes(localBasicFileAttributes.lastModifiedTime(), localBasicFileAttributes.lastAccessTime(), localBasicFileAttributes.creationTime());
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/*     */         try
/*     */         {
/* 141 */           Files.delete(paramPath2);
/*     */         } catch (Throwable localThrowable4) {
/* 143 */           localThrowable1.addSuppressed(localThrowable4);
/*     */         }
/* 145 */         throw localThrowable1;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void moveToForeignTarget(Path paramPath1, Path paramPath2, CopyOption[] paramArrayOfCopyOption)
/*     */     throws IOException
/*     */   {
/* 157 */     copyToForeignTarget(paramPath1, paramPath2, convertMoveToCopyOptions(paramArrayOfCopyOption));
/* 158 */     Files.delete(paramPath1);
/*     */   }
/*     */ 
/*     */   private static class CopyOptions
/*     */   {
/*  44 */     boolean replaceExisting = false;
/*  45 */     boolean copyAttributes = false;
/*  46 */     boolean followLinks = true;
/*     */ 
/*     */     static CopyOptions parse(CopyOption[] paramArrayOfCopyOption)
/*     */     {
/*  51 */       CopyOptions localCopyOptions = new CopyOptions();
/*  52 */       for (CopyOption localCopyOption : paramArrayOfCopyOption) {
/*  53 */         if (localCopyOption == StandardCopyOption.REPLACE_EXISTING) {
/*  54 */           localCopyOptions.replaceExisting = true;
/*     */         }
/*  57 */         else if (localCopyOption == LinkOption.NOFOLLOW_LINKS) {
/*  58 */           localCopyOptions.followLinks = false;
/*     */         }
/*  61 */         else if (localCopyOption == StandardCopyOption.COPY_ATTRIBUTES) {
/*  62 */           localCopyOptions.copyAttributes = true;
/*     */         }
/*     */         else {
/*  65 */           if (localCopyOption == null)
/*  66 */             throw new NullPointerException();
/*  67 */           throw new UnsupportedOperationException("'" + localCopyOption + "' is not a recognized copy option");
/*     */         }
/*     */       }
/*  70 */       return localCopyOptions;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.CopyMoveHelper
 * JD-Core Version:    0.6.2
 */