/*    */ package com.sun.corba.se.spi.logging;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.LogRecord;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public abstract class LogWrapperBase
/*    */ {
/*    */   protected Logger logger;
/*    */   protected String loggerName;
/*    */ 
/*    */   protected LogWrapperBase(Logger paramLogger)
/*    */   {
/* 39 */     this.logger = paramLogger;
/* 40 */     this.loggerName = paramLogger.getName();
/*    */   }
/*    */ 
/*    */   protected void doLog(Level paramLevel, String paramString, Object[] paramArrayOfObject, Class paramClass, Throwable paramThrowable)
/*    */   {
/* 46 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString);
/* 47 */     if (paramArrayOfObject != null)
/* 48 */       localLogRecord.setParameters(paramArrayOfObject);
/* 49 */     inferCaller(paramClass, localLogRecord);
/* 50 */     localLogRecord.setThrown(paramThrowable);
/* 51 */     localLogRecord.setLoggerName(this.loggerName);
/* 52 */     localLogRecord.setResourceBundle(this.logger.getResourceBundle());
/* 53 */     this.logger.log(localLogRecord);
/*    */   }
/*    */ 
/*    */   private void inferCaller(Class paramClass, LogRecord paramLogRecord)
/*    */   {
/* 61 */     StackTraceElement[] arrayOfStackTraceElement = new Throwable().getStackTrace();
/* 62 */     StackTraceElement localStackTraceElement = null;
/* 63 */     String str1 = paramClass.getName();
/* 64 */     String str2 = LogWrapperBase.class.getName();
/*    */ 
/* 69 */     int i = 0;
/* 70 */     while (i < arrayOfStackTraceElement.length) {
/* 71 */       localStackTraceElement = arrayOfStackTraceElement[i];
/* 72 */       String str3 = localStackTraceElement.getClassName();
/* 73 */       if ((!str3.equals(str1)) && (!str3.equals(str2)))
/*    */       {
/*    */         break;
/*    */       }
/* 77 */       i++;
/*    */     }
/*    */ 
/* 82 */     if (i < arrayOfStackTraceElement.length) {
/* 83 */       paramLogRecord.setSourceClassName(localStackTraceElement.getClassName());
/* 84 */       paramLogRecord.setSourceMethodName(localStackTraceElement.getMethodName());
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void doLog(Level paramLevel, String paramString, Class paramClass, Throwable paramThrowable)
/*    */   {
/* 90 */     doLog(paramLevel, paramString, null, paramClass, paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.logging.LogWrapperBase
 * JD-Core Version:    0.6.2
 */