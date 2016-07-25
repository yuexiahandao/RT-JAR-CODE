/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ public class MIMEConfig
/*     */ {
/*     */   private static final int DEFAULT_CHUNK_SIZE = 8192;
/*     */   private static final long DEFAULT_MEMORY_THRESHOLD = 1048576L;
/*     */   private static final String DEFAULT_FILE_PREFIX = "MIME";
/*     */   boolean parseEagerly;
/*     */   int chunkSize;
/*     */   long memoryThreshold;
/*     */   boolean onlyMemory;
/*     */   File tempDir;
/*     */   String prefix;
/*     */   String suffix;
/*     */ 
/*     */   private MIMEConfig(boolean parseEagerly, int chunkSize, long inMemoryThreshold, String dir, String prefix, String suffix)
/*     */   {
/*  62 */     this.parseEagerly = parseEagerly;
/*  63 */     this.chunkSize = chunkSize;
/*  64 */     this.memoryThreshold = inMemoryThreshold;
/*  65 */     this.prefix = prefix;
/*  66 */     this.suffix = suffix;
/*  67 */     setDir(dir);
/*     */   }
/*     */ 
/*     */   public MIMEConfig() {
/*  71 */     this(false, 8192, 1048576L, null, "MIME", null);
/*     */   }
/*     */ 
/*     */   boolean isParseEagerly()
/*     */   {
/*  76 */     return this.parseEagerly;
/*     */   }
/*     */ 
/*     */   public void setParseEagerly(boolean parseEagerly) {
/*  80 */     this.parseEagerly = parseEagerly;
/*     */   }
/*     */ 
/*     */   int getChunkSize() {
/*  84 */     return this.chunkSize;
/*     */   }
/*     */ 
/*     */   void setChunkSize(int chunkSize) {
/*  88 */     this.chunkSize = chunkSize;
/*     */   }
/*     */ 
/*     */   long getMemoryThreshold() {
/*  92 */     return this.memoryThreshold;
/*     */   }
/*     */ 
/*     */   public void setMemoryThreshold(long memoryThreshold)
/*     */   {
/* 103 */     this.memoryThreshold = memoryThreshold;
/*     */   }
/*     */ 
/*     */   boolean isOnlyMemory() {
/* 107 */     return this.memoryThreshold == -1L;
/*     */   }
/*     */ 
/*     */   File getTempDir() {
/* 111 */     return this.tempDir;
/*     */   }
/*     */ 
/*     */   String getTempFilePrefix() {
/* 115 */     return this.prefix;
/*     */   }
/*     */ 
/*     */   String getTempFileSuffix() {
/* 119 */     return this.suffix;
/*     */   }
/*     */ 
/*     */   public void setDir(String dir)
/*     */   {
/* 126 */     if ((this.tempDir == null) && (dir != null) && (!dir.equals("")))
/* 127 */       this.tempDir = new File(dir);
/*     */   }
/*     */ 
/*     */   public void validate()
/*     */   {
/* 136 */     if (!isOnlyMemory())
/*     */       try {
/* 138 */         File tempFile = this.tempDir == null ? File.createTempFile(this.prefix, this.suffix) : File.createTempFile(this.prefix, this.suffix, this.tempDir);
/*     */ 
/* 141 */         tempFile.delete();
/*     */       } catch (Exception ioe) {
/* 143 */         this.memoryThreshold = -1L;
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.MIMEConfig
 * JD-Core Version:    0.6.2
 */