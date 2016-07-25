/*     */ package javax.tools;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.net.URI;
/*     */ 
/*     */ public class ForwardingFileObject<F extends FileObject>
/*     */   implements FileObject
/*     */ {
/*     */   protected final F fileObject;
/*     */ 
/*     */   protected ForwardingFileObject(F paramF)
/*     */   {
/*  56 */     paramF.getClass();
/*  57 */     this.fileObject = paramF;
/*     */   }
/*     */ 
/*     */   public URI toUri() {
/*  61 */     return this.fileObject.toUri();
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  65 */     return this.fileObject.getName();
/*     */   }
/*     */ 
/*     */   public InputStream openInputStream()
/*     */     throws IOException
/*     */   {
/*  74 */     return this.fileObject.openInputStream();
/*     */   }
/*     */ 
/*     */   public OutputStream openOutputStream()
/*     */     throws IOException
/*     */   {
/*  83 */     return this.fileObject.openOutputStream();
/*     */   }
/*     */ 
/*     */   public Reader openReader(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  92 */     return this.fileObject.openReader(paramBoolean);
/*     */   }
/*     */ 
/*     */   public CharSequence getCharContent(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 101 */     return this.fileObject.getCharContent(paramBoolean);
/*     */   }
/*     */ 
/*     */   public Writer openWriter()
/*     */     throws IOException
/*     */   {
/* 110 */     return this.fileObject.openWriter();
/*     */   }
/*     */ 
/*     */   public long getLastModified() {
/* 114 */     return this.fileObject.getLastModified();
/*     */   }
/*     */ 
/*     */   public boolean delete() {
/* 118 */     return this.fileObject.delete();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.ForwardingFileObject
 * JD-Core Version:    0.6.2
 */