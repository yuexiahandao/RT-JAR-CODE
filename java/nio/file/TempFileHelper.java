/*     */ package java.nio.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.nio.file.attribute.PosixFilePermissions;
/*     */ import java.security.AccessController;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class TempFileHelper
/*     */ {
/*  49 */   private static final Path tmpdir = Paths.get((String)AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")), new String[0]);
/*     */ 
/*  52 */   private static final boolean isPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
/*     */ 
/*  56 */   private static final SecureRandom random = new SecureRandom();
/*     */ 
/*  58 */   private static Path generatePath(String paramString1, String paramString2, Path paramPath) { long l = random.nextLong();
/*  59 */     l = l == -9223372036854775808L ? 0L : Math.abs(l);
/*  60 */     Path localPath = paramPath.getFileSystem().getPath(paramString1 + Long.toString(l) + paramString2, new String[0]);
/*     */ 
/*  62 */     if (localPath.getParent() != null)
/*  63 */       throw new IllegalArgumentException("Invalid prefix or suffix");
/*  64 */     return paramPath.resolve(localPath);
/*     */   }
/*     */ 
/*     */   private static Path create(Path paramPath, String paramString1, String paramString2, boolean paramBoolean, FileAttribute[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/*  87 */     if (paramString1 == null)
/*  88 */       paramString1 = "";
/*  89 */     if (paramString2 == null)
/*  90 */       paramString2 = paramBoolean ? "" : ".tmp";
/*  91 */     if (paramPath == null)
/*  92 */       paramPath = tmpdir;
/*     */     Object localObject;
/*  96 */     if ((isPosix) && (paramPath.getFileSystem() == FileSystems.getDefault())) {
/*  97 */       if (paramArrayOfFileAttribute.length == 0)
/*     */       {
/*  99 */         paramArrayOfFileAttribute = new FileAttribute[1];
/* 100 */         paramArrayOfFileAttribute[0] = (paramBoolean ? PosixPermissions.dirPermissions : PosixPermissions.filePermissions);
/*     */       }
/*     */       else
/*     */       {
/* 104 */         int i = 0;
/* 105 */         for (int j = 0; j < paramArrayOfFileAttribute.length; j++) {
/* 106 */           if (paramArrayOfFileAttribute[j].name().equals("posix:permissions")) {
/* 107 */             i = 1;
/* 108 */             break;
/*     */           }
/*     */         }
/* 111 */         if (i == 0) {
/* 112 */           localObject = new FileAttribute[paramArrayOfFileAttribute.length + 1];
/* 113 */           System.arraycopy(paramArrayOfFileAttribute, 0, localObject, 0, paramArrayOfFileAttribute.length);
/* 114 */           paramArrayOfFileAttribute = (FileAttribute[])localObject;
/* 115 */           paramArrayOfFileAttribute[(paramArrayOfFileAttribute.length - 1)] = (paramBoolean ? PosixPermissions.dirPermissions : PosixPermissions.filePermissions);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 123 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*     */     while (true)
/*     */     {
/*     */       try {
/* 127 */         localObject = generatePath(paramString1, paramString2, paramPath);
/*     */       }
/*     */       catch (InvalidPathException localInvalidPathException) {
/* 130 */         if (localSecurityManager != null)
/* 131 */           throw new IllegalArgumentException("Invalid prefix or suffix");
/* 132 */         throw localInvalidPathException;
/*     */       }
/*     */       try {
/* 135 */         if (paramBoolean) {
/* 136 */           return Files.createDirectory((Path)localObject, paramArrayOfFileAttribute);
/*     */         }
/* 138 */         return Files.createFile((Path)localObject, paramArrayOfFileAttribute);
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/* 142 */         if ((paramPath == tmpdir) && (localSecurityManager != null))
/* 143 */           throw new SecurityException("Unable to create temporary file or directory");
/* 144 */         throw localSecurityException;
/*     */       }
/*     */       catch (FileAlreadyExistsException localFileAlreadyExistsException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static Path createTempFile(Path paramPath, String paramString1, String paramString2, FileAttribute[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 161 */     return create(paramPath, paramString1, paramString2, false, paramArrayOfFileAttribute);
/*     */   }
/*     */ 
/*     */   static Path createTempDirectory(Path paramPath, String paramString, FileAttribute[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 173 */     return create(paramPath, paramString, null, true, paramArrayOfFileAttribute);
/*     */   }
/*     */ 
/*     */   private static class PosixPermissions
/*     */   {
/*  69 */     static final FileAttribute<Set<PosixFilePermission>> filePermissions = PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE));
/*     */ 
/*  71 */     static final FileAttribute<Set<PosixFilePermission>> dirPermissions = PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.TempFileHelper
 * JD-Core Version:    0.6.2
 */