/*     */ package java.util.logging;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ public class StreamHandler extends Handler
/*     */ {
/*  62 */   private LogManager manager = LogManager.getLogManager();
/*     */   private OutputStream output;
/*     */   private boolean doneHeader;
/*     */   private Writer writer;
/*     */ 
/*     */   private void configure()
/*     */   {
/*  71 */     LogManager localLogManager = LogManager.getLogManager();
/*  72 */     String str = getClass().getName();
/*     */ 
/*  74 */     setLevel(localLogManager.getLevelProperty(str + ".level", Level.INFO));
/*  75 */     setFilter(localLogManager.getFilterProperty(str + ".filter", null));
/*  76 */     setFormatter(localLogManager.getFormatterProperty(str + ".formatter", new SimpleFormatter()));
/*     */     try {
/*  78 */       setEncoding(localLogManager.getStringProperty(str + ".encoding", null));
/*     */     } catch (Exception localException1) {
/*     */       try {
/*  81 */         setEncoding(null);
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public StreamHandler()
/*     */   {
/*  93 */     this.sealed = false;
/*  94 */     configure();
/*  95 */     this.sealed = true;
/*     */   }
/*     */ 
/*     */   public StreamHandler(OutputStream paramOutputStream, Formatter paramFormatter)
/*     */   {
/* 106 */     this.sealed = false;
/* 107 */     configure();
/* 108 */     setFormatter(paramFormatter);
/* 109 */     setOutputStream(paramOutputStream);
/* 110 */     this.sealed = true;
/*     */   }
/*     */ 
/*     */   protected synchronized void setOutputStream(OutputStream paramOutputStream)
/*     */     throws SecurityException
/*     */   {
/* 125 */     if (paramOutputStream == null) {
/* 126 */       throw new NullPointerException();
/*     */     }
/* 128 */     flushAndClose();
/* 129 */     this.output = paramOutputStream;
/* 130 */     this.doneHeader = false;
/* 131 */     String str = getEncoding();
/* 132 */     if (str == null)
/* 133 */       this.writer = new OutputStreamWriter(this.output);
/*     */     else
/*     */       try {
/* 136 */         this.writer = new OutputStreamWriter(this.output, str);
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */       {
/* 140 */         throw new Error("Unexpected exception " + localUnsupportedEncodingException);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setEncoding(String paramString)
/*     */     throws SecurityException, UnsupportedEncodingException
/*     */   {
/* 160 */     super.setEncoding(paramString);
/* 161 */     if (this.output == null) {
/* 162 */       return;
/*     */     }
/*     */ 
/* 165 */     flush();
/* 166 */     if (paramString == null)
/* 167 */       this.writer = new OutputStreamWriter(this.output);
/*     */     else
/* 169 */       this.writer = new OutputStreamWriter(this.output, paramString);
/*     */   }
/*     */ 
/*     */   public synchronized void publish(LogRecord paramLogRecord)
/*     */   {
/* 191 */     if (!isLoggable(paramLogRecord))
/*     */       return;
/*     */     String str;
/*     */     try
/*     */     {
/* 196 */       str = getFormatter().format(paramLogRecord);
/*     */     }
/*     */     catch (Exception localException1)
/*     */     {
/* 200 */       reportError(null, localException1, 5);
/* 201 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 205 */       if (!this.doneHeader) {
/* 206 */         this.writer.write(getFormatter().getHead(this));
/* 207 */         this.doneHeader = true;
/*     */       }
/* 209 */       this.writer.write(str);
/*     */     }
/*     */     catch (Exception localException2)
/*     */     {
/* 213 */       reportError(null, localException2, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isLoggable(LogRecord paramLogRecord)
/*     */   {
/* 230 */     if ((this.writer == null) || (paramLogRecord == null)) {
/* 231 */       return false;
/*     */     }
/* 233 */     return super.isLoggable(paramLogRecord);
/*     */   }
/*     */ 
/*     */   public synchronized void flush()
/*     */   {
/* 240 */     if (this.writer != null)
/*     */       try {
/* 242 */         this.writer.flush();
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 246 */         reportError(null, localException, 2);
/*     */       }
/*     */   }
/*     */ 
/*     */   private synchronized void flushAndClose() throws SecurityException
/*     */   {
/* 252 */     checkPermission();
/* 253 */     if (this.writer != null) {
/*     */       try {
/* 255 */         if (!this.doneHeader) {
/* 256 */           this.writer.write(getFormatter().getHead(this));
/* 257 */           this.doneHeader = true;
/*     */         }
/* 259 */         this.writer.write(getFormatter().getTail(this));
/* 260 */         this.writer.flush();
/* 261 */         this.writer.close();
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 265 */         reportError(null, localException, 3);
/*     */       }
/* 267 */       this.writer = null;
/* 268 */       this.output = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */     throws SecurityException
/*     */   {
/* 284 */     flushAndClose();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.StreamHandler
 * JD-Core Version:    0.6.2
 */