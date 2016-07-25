/*     */ package java.util.jar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.zip.ZipEntry;
/*     */ 
/*     */ public class JarEntry extends ZipEntry
/*     */ {
/*     */   Attributes attr;
/*     */   Certificate[] certs;
/*     */   CodeSigner[] signers;
/*     */ 
/*     */   public JarEntry(String paramString)
/*     */   {
/*  52 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public JarEntry(ZipEntry paramZipEntry)
/*     */   {
/*  62 */     super(paramZipEntry);
/*     */   }
/*     */ 
/*     */   public JarEntry(JarEntry paramJarEntry)
/*     */   {
/*  72 */     this(paramJarEntry);
/*  73 */     this.attr = paramJarEntry.attr;
/*  74 */     this.certs = paramJarEntry.certs;
/*  75 */     this.signers = paramJarEntry.signers;
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes()
/*     */     throws IOException
/*     */   {
/*  86 */     return this.attr;
/*     */   }
/*     */ 
/*     */   public Certificate[] getCertificates()
/*     */   {
/* 107 */     return this.certs == null ? null : (Certificate[])this.certs.clone();
/*     */   }
/*     */ 
/*     */   public CodeSigner[] getCodeSigners()
/*     */   {
/* 126 */     return this.signers == null ? null : (CodeSigner[])this.signers.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.jar.JarEntry
 * JD-Core Version:    0.6.2
 */