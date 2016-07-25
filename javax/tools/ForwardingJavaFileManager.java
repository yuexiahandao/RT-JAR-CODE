/*     */ package javax.tools;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ForwardingJavaFileManager<M extends JavaFileManager>
/*     */   implements JavaFileManager
/*     */ {
/*     */   protected final M fileManager;
/*     */ 
/*     */   protected ForwardingJavaFileManager(M paramM)
/*     */   {
/*  55 */     paramM.getClass();
/*  56 */     this.fileManager = paramM;
/*     */   }
/*     */ 
/*     */   public ClassLoader getClassLoader(JavaFileManager.Location paramLocation)
/*     */   {
/*  64 */     return this.fileManager.getClassLoader(paramLocation);
/*     */   }
/*     */ 
/*     */   public Iterable<JavaFileObject> list(JavaFileManager.Location paramLocation, String paramString, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  77 */     return this.fileManager.list(paramLocation, paramString, paramSet, paramBoolean);
/*     */   }
/*     */ 
/*     */   public String inferBinaryName(JavaFileManager.Location paramLocation, JavaFileObject paramJavaFileObject)
/*     */   {
/*  84 */     return this.fileManager.inferBinaryName(paramLocation, paramJavaFileObject);
/*     */   }
/*     */ 
/*     */   public boolean isSameFile(FileObject paramFileObject1, FileObject paramFileObject2)
/*     */   {
/*  91 */     return this.fileManager.isSameFile(paramFileObject1, paramFileObject2);
/*     */   }
/*     */ 
/*     */   public boolean handleOption(String paramString, Iterator<String> paramIterator)
/*     */   {
/*  99 */     return this.fileManager.handleOption(paramString, paramIterator);
/*     */   }
/*     */ 
/*     */   public boolean hasLocation(JavaFileManager.Location paramLocation) {
/* 103 */     return this.fileManager.hasLocation(paramLocation);
/*     */   }
/*     */ 
/*     */   public int isSupportedOption(String paramString) {
/* 107 */     return this.fileManager.isSupportedOption(paramString);
/*     */   }
/*     */ 
/*     */   public JavaFileObject getJavaFileForInput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind)
/*     */     throws IOException
/*     */   {
/* 119 */     return this.fileManager.getJavaFileForInput(paramLocation, paramString, paramKind);
/*     */   }
/*     */ 
/*     */   public JavaFileObject getJavaFileForOutput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 132 */     return this.fileManager.getJavaFileForOutput(paramLocation, paramString, paramKind, paramFileObject);
/*     */   }
/*     */ 
/*     */   public FileObject getFileForInput(JavaFileManager.Location paramLocation, String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 144 */     return this.fileManager.getFileForInput(paramLocation, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public FileObject getFileForOutput(JavaFileManager.Location paramLocation, String paramString1, String paramString2, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 157 */     return this.fileManager.getFileForOutput(paramLocation, paramString1, paramString2, paramFileObject);
/*     */   }
/*     */ 
/*     */   public void flush() throws IOException {
/* 161 */     this.fileManager.flush();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 165 */     this.fileManager.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.ForwardingJavaFileManager
 * JD-Core Version:    0.6.2
 */