/*    */ package com.sun.xml.internal.org.jvnet.mimepull;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ final class MemoryData
/*    */   implements Data
/*    */ {
/* 41 */   private static final Logger LOGGER = Logger.getLogger(MemoryData.class.getName());
/*    */   private final byte[] data;
/*    */   private final int len;
/*    */   private final MIMEConfig config;
/*    */ 
/*    */   MemoryData(ByteBuffer buf, MIMEConfig config)
/*    */   {
/* 48 */     this.data = buf.array();
/* 49 */     this.len = buf.limit();
/* 50 */     this.config = config;
/*    */   }
/*    */ 
/*    */   public int size()
/*    */   {
/* 56 */     return this.len;
/*    */   }
/*    */ 
/*    */   public byte[] read()
/*    */   {
/* 61 */     return this.data;
/*    */   }
/*    */ 
/*    */   public long writeTo(DataFile file)
/*    */   {
/* 66 */     return file.writeTo(this.data, 0, this.len);
/*    */   }
/*    */ 
/*    */   public Data createNext(DataHead dataHead, ByteBuffer buf)
/*    */   {
/* 76 */     if ((!this.config.isOnlyMemory()) && (dataHead.inMemory >= this.config.memoryThreshold)) {
/*    */       try {
/* 78 */         String prefix = this.config.getTempFilePrefix();
/* 79 */         String suffix = this.config.getTempFileSuffix();
/* 80 */         File tempFile = TempFiles.createTempFile(prefix, suffix, this.config.getTempDir());
/*    */ 
/* 82 */         tempFile.deleteOnExit();
/* 83 */         if (LOGGER.isLoggable(Level.FINE)) {
/* 84 */           LOGGER.log(Level.FINE, "Created temp file = {0}", tempFile);
/*    */         }
/* 86 */         dataHead.dataFile = new DataFile(tempFile);
/*    */       } catch (IOException ioe) {
/* 88 */         throw new MIMEParsingException(ioe);
/*    */       }
/*    */ 
/* 91 */       if (dataHead.head != null) {
/* 92 */         for (Chunk c = dataHead.head; c != null; c = c.next) {
/* 93 */           long pointer = c.data.writeTo(dataHead.dataFile);
/* 94 */           c.data = new FileData(dataHead.dataFile, pointer, this.len);
/*    */         }
/*    */       }
/* 97 */       return new FileData(dataHead.dataFile, buf);
/*    */     }
/* 99 */     return new MemoryData(buf, this.config);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.MemoryData
 * JD-Core Version:    0.6.2
 */