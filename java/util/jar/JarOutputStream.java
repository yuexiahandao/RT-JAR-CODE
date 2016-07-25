/*     */ package java.util.jar;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ public class JarOutputStream extends ZipOutputStream
/*     */ {
/*     */   private static final int JAR_MAGIC = 51966;
/* 112 */   private boolean firstEntry = true;
/*     */ 
/*     */   public JarOutputStream(OutputStream paramOutputStream, Manifest paramManifest)
/*     */     throws IOException
/*     */   {
/*  58 */     super(paramOutputStream);
/*  59 */     if (paramManifest == null) {
/*  60 */       throw new NullPointerException("man");
/*     */     }
/*  62 */     ZipEntry localZipEntry = new ZipEntry("META-INF/MANIFEST.MF");
/*  63 */     putNextEntry(localZipEntry);
/*  64 */     paramManifest.write(new BufferedOutputStream(this));
/*  65 */     closeEntry();
/*     */   }
/*     */ 
/*     */   public JarOutputStream(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  74 */     super(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void putNextEntry(ZipEntry paramZipEntry)
/*     */     throws IOException
/*     */   {
/*  90 */     if (this.firstEntry)
/*     */     {
/*  93 */       Object localObject = paramZipEntry.getExtra();
/*  94 */       if ((localObject == null) || (!hasMagic((byte[])localObject))) {
/*  95 */         if (localObject == null) {
/*  96 */           localObject = new byte[4];
/*     */         }
/*     */         else {
/*  99 */           byte[] arrayOfByte = new byte[localObject.length + 4];
/* 100 */           System.arraycopy(localObject, 0, arrayOfByte, 4, localObject.length);
/* 101 */           localObject = arrayOfByte;
/*     */         }
/* 103 */         set16((byte[])localObject, 0, 51966);
/* 104 */         set16((byte[])localObject, 2, 0);
/* 105 */         paramZipEntry.setExtra((byte[])localObject);
/*     */       }
/* 107 */       this.firstEntry = false;
/*     */     }
/* 109 */     super.putNextEntry(paramZipEntry);
/*     */   }
/*     */ 
/*     */   private static boolean hasMagic(byte[] paramArrayOfByte)
/*     */   {
/*     */     try
/*     */     {
/* 120 */       int i = 0;
/* 121 */       while (i < paramArrayOfByte.length) {
/* 122 */         if (get16(paramArrayOfByte, i) == 51966) {
/* 123 */           return true;
/*     */         }
/* 125 */         i += get16(paramArrayOfByte, i + 2) + 4;
/*     */       }
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*     */     }
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   private static int get16(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 138 */     return paramArrayOfByte[paramInt] & 0xFF | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 8;
/*     */   }
/*     */ 
/*     */   private static void set16(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 146 */     paramArrayOfByte[(paramInt1 + 0)] = ((byte)paramInt2);
/* 147 */     paramArrayOfByte[(paramInt1 + 1)] = ((byte)(paramInt2 >> 8));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.jar.JarOutputStream
 * JD-Core Version:    0.6.2
 */