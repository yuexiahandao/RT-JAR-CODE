/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class FileDataSource
/*     */   implements DataSource
/*     */ {
/*  62 */   private File _file = null;
/*  63 */   private FileTypeMap typeMap = null;
/*     */ 
/*     */   public FileDataSource(File file)
/*     */   {
/*  73 */     this._file = file;
/*     */   }
/*     */ 
/*     */   public FileDataSource(String name)
/*     */   {
/*  85 */     this(new File(name));
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/*  97 */     return new FileInputStream(this._file);
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 109 */     return new FileOutputStream(this._file);
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 125 */     if (this.typeMap == null) {
/* 126 */       return FileTypeMap.getDefaultFileTypeMap().getContentType(this._file);
/*     */     }
/* 128 */     return this.typeMap.getContentType(this._file);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 139 */     return this._file.getName();
/*     */   }
/*     */ 
/*     */   public File getFile()
/*     */   {
/* 147 */     return this._file;
/*     */   }
/*     */ 
/*     */   public void setFileTypeMap(FileTypeMap map)
/*     */   {
/* 156 */     this.typeMap = map;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.FileDataSource
 * JD-Core Version:    0.6.2
 */