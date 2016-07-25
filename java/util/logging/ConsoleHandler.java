/*     */ package java.util.logging;
/*     */ 
/*     */ public class ConsoleHandler extends StreamHandler
/*     */ {
/*     */   private void configure()
/*     */   {
/*  63 */     LogManager localLogManager = LogManager.getLogManager();
/*  64 */     String str = getClass().getName();
/*     */ 
/*  66 */     setLevel(localLogManager.getLevelProperty(str + ".level", Level.INFO));
/*  67 */     setFilter(localLogManager.getFilterProperty(str + ".filter", null));
/*  68 */     setFormatter(localLogManager.getFormatterProperty(str + ".formatter", new SimpleFormatter()));
/*     */     try {
/*  70 */       setEncoding(localLogManager.getStringProperty(str + ".encoding", null));
/*     */     } catch (Exception localException1) {
/*     */       try {
/*  73 */         setEncoding(null);
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public ConsoleHandler()
/*     */   {
/*  89 */     this.sealed = false;
/*  90 */     configure();
/*  91 */     setOutputStream(System.err);
/*  92 */     this.sealed = true;
/*     */   }
/*     */ 
/*     */   public void publish(LogRecord paramLogRecord)
/*     */   {
/* 105 */     super.publish(paramLogRecord);
/* 106 */     flush();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 115 */     flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.ConsoleHandler
 * JD-Core Version:    0.6.2
 */