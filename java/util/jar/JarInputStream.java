/*     */ package java.util.jar;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import sun.security.util.ManifestEntryVerifier;
/*     */ 
/*     */ public class JarInputStream extends ZipInputStream
/*     */ {
/*     */   private Manifest man;
/*     */   private JarEntry first;
/*     */   private JarVerifier jv;
/*     */   private ManifestEntryVerifier mev;
/*     */   private final boolean doVerify;
/*     */   private boolean tryManifest;
/*     */ 
/*     */   public JarInputStream(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  62 */     this(paramInputStream, true);
/*     */   }
/*     */ 
/*     */   public JarInputStream(InputStream paramInputStream, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  76 */     super(paramInputStream);
/*  77 */     this.doVerify = paramBoolean;
/*     */ 
/*  83 */     JarEntry localJarEntry = (JarEntry)super.getNextEntry();
/*  84 */     if ((localJarEntry != null) && (localJarEntry.getName().equalsIgnoreCase("META-INF/")))
/*  85 */       localJarEntry = (JarEntry)super.getNextEntry();
/*  86 */     this.first = checkManifest(localJarEntry);
/*     */   }
/*     */ 
/*     */   private JarEntry checkManifest(JarEntry paramJarEntry)
/*     */     throws IOException
/*     */   {
/*  92 */     if ((paramJarEntry != null) && ("META-INF/MANIFEST.MF".equalsIgnoreCase(paramJarEntry.getName()))) {
/*  93 */       this.man = new Manifest();
/*  94 */       byte[] arrayOfByte = getBytes(new BufferedInputStream(this));
/*  95 */       this.man.read(new ByteArrayInputStream(arrayOfByte));
/*  96 */       closeEntry();
/*  97 */       if (this.doVerify) {
/*  98 */         this.jv = new JarVerifier(arrayOfByte);
/*  99 */         this.mev = new ManifestEntryVerifier(this.man);
/*     */       }
/* 101 */       return (JarEntry)super.getNextEntry();
/*     */     }
/* 103 */     return paramJarEntry;
/*     */   }
/*     */ 
/*     */   private byte[] getBytes(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 109 */     byte[] arrayOfByte = new byte[8192];
/* 110 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(2048);
/*     */     int i;
/* 112 */     while ((i = paramInputStream.read(arrayOfByte, 0, arrayOfByte.length)) != -1) {
/* 113 */       localByteArrayOutputStream.write(arrayOfByte, 0, i);
/*     */     }
/* 115 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public Manifest getManifest()
/*     */   {
/* 126 */     return this.man;
/*     */   }
/*     */ 
/*     */   public ZipEntry getNextEntry()
/*     */     throws IOException
/*     */   {
/*     */     JarEntry localJarEntry;
/* 141 */     if (this.first == null) {
/* 142 */       localJarEntry = (JarEntry)super.getNextEntry();
/* 143 */       if (this.tryManifest) {
/* 144 */         localJarEntry = checkManifest(localJarEntry);
/* 145 */         this.tryManifest = false;
/*     */       }
/*     */     } else {
/* 148 */       localJarEntry = this.first;
/* 149 */       if (this.first.getName().equalsIgnoreCase("META-INF/INDEX.LIST"))
/* 150 */         this.tryManifest = true;
/* 151 */       this.first = null;
/*     */     }
/* 153 */     if ((this.jv != null) && (localJarEntry != null))
/*     */     {
/* 157 */       if (this.jv.nothingToVerify() == true) {
/* 158 */         this.jv = null;
/* 159 */         this.mev = null;
/*     */       } else {
/* 161 */         this.jv.beginEntry(localJarEntry, this.mev);
/*     */       }
/*     */     }
/* 164 */     return localJarEntry;
/*     */   }
/*     */ 
/*     */   public JarEntry getNextJarEntry()
/*     */     throws IOException
/*     */   {
/* 179 */     return (JarEntry)getNextEntry();
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*     */     int i;
/* 206 */     if (this.first == null)
/* 207 */       i = super.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */     else {
/* 209 */       i = -1;
/*     */     }
/* 211 */     if (this.jv != null) {
/* 212 */       this.jv.update(i, paramArrayOfByte, paramInt1, paramInt2, this.mev);
/*     */     }
/* 214 */     return i;
/*     */   }
/*     */ 
/*     */   protected ZipEntry createZipEntry(String paramString)
/*     */   {
/* 227 */     JarEntry localJarEntry = new JarEntry(paramString);
/* 228 */     if (this.man != null) {
/* 229 */       localJarEntry.attr = this.man.getAttributes(paramString);
/*     */     }
/* 231 */     return localJarEntry;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.jar.JarInputStream
 * JD-Core Version:    0.6.2
 */