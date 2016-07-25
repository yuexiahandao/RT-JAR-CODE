/*     */ package sun.jdbc.odbc;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.DriverManager;
/*     */ 
/*     */ public class JdbcOdbcTracer
/*     */ {
/*     */   private PrintWriter outWriter;
/*     */ 
/*     */   public boolean isTracing()
/*     */   {
/*  29 */     if (this.outWriter != null) {
/*  30 */       return true;
/*     */     }
/*  32 */     if (DriverManager.getLogWriter() != null) {
/*  33 */       return DriverManager.getLogWriter() != null;
/*     */     }
/*     */ 
/*  36 */     return false;
/*     */   }
/*     */ 
/*     */   public void trace(String paramString)
/*     */   {
/*  46 */     if (this.outWriter != null) {
/*  47 */       this.outWriter.println(paramString);
/*  48 */       this.outWriter.flush();
/*     */     }
/*  50 */     else if (DriverManager.getLogWriter() != null) {
/*  51 */       DriverManager.getLogWriter().println(paramString);
/*  52 */       DriverManager.getLogWriter().flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void trace(String paramString, boolean paramBoolean)
/*     */   {
/*  65 */     if (paramBoolean) {
/*  66 */       trace(paramString);
/*     */     }
/*     */ 
/*  70 */     if (this.outWriter != null) {
/*  71 */       this.outWriter.println(paramString);
/*  72 */       this.outWriter.flush();
/*     */     }
/*  74 */     else if (DriverManager.getLogWriter() != null) {
/*  75 */       DriverManager.getLogWriter().println(paramString);
/*  76 */       DriverManager.getLogWriter().flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setWriter(PrintWriter paramPrintWriter)
/*     */   {
/*  87 */     if (paramPrintWriter != null) {
/*  88 */       this.outWriter = paramPrintWriter;
/*     */     }
/*     */     else
/*  91 */       this.outWriter = null;
/*     */   }
/*     */ 
/*     */   public PrintWriter getWriter()
/*     */   {
/* 102 */     return this.outWriter;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcTracer
 * JD-Core Version:    0.6.2
 */