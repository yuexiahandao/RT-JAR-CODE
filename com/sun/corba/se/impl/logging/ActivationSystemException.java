/*     */ package com.sun.corba.se.impl.logging;
/*     */ 
/*     */ import com.sun.corba.se.spi.logging.LogWrapperBase;
/*     */ import com.sun.corba.se.spi.logging.LogWrapperFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.INITIALIZE;
/*     */ import org.omg.CORBA.INTERNAL;
/*     */ import org.omg.CORBA.OBJECT_NOT_EXIST;
/*     */ 
/*     */ public class ActivationSystemException extends LogWrapperBase
/*     */ {
/*  33 */   private static LogWrapperFactory factory = new LogWrapperFactory()
/*     */   {
/*     */     public LogWrapperBase create(Logger paramAnonymousLogger) {
/*  36 */       return new ActivationSystemException(paramAnonymousLogger);
/*     */     }
/*  33 */   };
/*     */   public static final int CANNOT_READ_REPOSITORY_DB = 1398079889;
/*     */   public static final int CANNOT_ADD_INITIAL_NAMING = 1398079890;
/*     */   public static final int CANNOT_WRITE_REPOSITORY_DB = 1398079889;
/*     */   public static final int SERVER_NOT_EXPECTED_TO_REGISTER = 1398079891;
/*     */   public static final int UNABLE_TO_START_PROCESS = 1398079892;
/*     */   public static final int SERVER_NOT_RUNNING = 1398079894;
/*     */   public static final int ERROR_IN_BAD_SERVER_ID_HANDLER = 1398079889;
/*     */ 
/*     */   public ActivationSystemException(Logger paramLogger)
/*     */   {
/*  30 */     super(paramLogger);
/*     */   }
/*     */ 
/*     */   public static ActivationSystemException get(ORB paramORB, String paramString)
/*     */   {
/*  42 */     ActivationSystemException localActivationSystemException = (ActivationSystemException)paramORB.getLogWrapper(paramString, "ACTIVATION", factory);
/*     */ 
/*  45 */     return localActivationSystemException;
/*     */   }
/*     */ 
/*     */   public static ActivationSystemException get(String paramString)
/*     */   {
/*  50 */     ActivationSystemException localActivationSystemException = (ActivationSystemException)ORB.staticGetLogWrapper(paramString, "ACTIVATION", factory);
/*     */ 
/*  53 */     return localActivationSystemException;
/*     */   }
/*     */ 
/*     */   public INITIALIZE cannotReadRepositoryDb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/*  63 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398079889, paramCompletionStatus);
/*  64 */     if (paramThrowable != null) {
/*  65 */       localINITIALIZE.initCause(paramThrowable);
/*     */     }
/*  67 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  68 */       Object[] arrayOfObject = null;
/*  69 */       doLog(Level.WARNING, "ACTIVATION.cannotReadRepositoryDb", arrayOfObject, ActivationSystemException.class, localINITIALIZE);
/*     */     }
/*     */ 
/*  73 */     return localINITIALIZE;
/*     */   }
/*     */ 
/*     */   public INITIALIZE cannotReadRepositoryDb(CompletionStatus paramCompletionStatus) {
/*  77 */     return cannotReadRepositoryDb(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INITIALIZE cannotReadRepositoryDb(Throwable paramThrowable) {
/*  81 */     return cannotReadRepositoryDb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INITIALIZE cannotReadRepositoryDb() {
/*  85 */     return cannotReadRepositoryDb(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INITIALIZE cannotAddInitialNaming(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/*  91 */     INITIALIZE localINITIALIZE = new INITIALIZE(1398079890, paramCompletionStatus);
/*  92 */     if (paramThrowable != null) {
/*  93 */       localINITIALIZE.initCause(paramThrowable);
/*     */     }
/*  95 */     if (this.logger.isLoggable(Level.WARNING)) {
/*  96 */       Object[] arrayOfObject = null;
/*  97 */       doLog(Level.WARNING, "ACTIVATION.cannotAddInitialNaming", arrayOfObject, ActivationSystemException.class, localINITIALIZE);
/*     */     }
/*     */ 
/* 101 */     return localINITIALIZE;
/*     */   }
/*     */ 
/*     */   public INITIALIZE cannotAddInitialNaming(CompletionStatus paramCompletionStatus) {
/* 105 */     return cannotAddInitialNaming(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INITIALIZE cannotAddInitialNaming(Throwable paramThrowable) {
/* 109 */     return cannotAddInitialNaming(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INITIALIZE cannotAddInitialNaming() {
/* 113 */     return cannotAddInitialNaming(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL cannotWriteRepositoryDb(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 123 */     INTERNAL localINTERNAL = new INTERNAL(1398079889, paramCompletionStatus);
/* 124 */     if (paramThrowable != null) {
/* 125 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 127 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 128 */       Object[] arrayOfObject = null;
/* 129 */       doLog(Level.WARNING, "ACTIVATION.cannotWriteRepositoryDb", arrayOfObject, ActivationSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 133 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL cannotWriteRepositoryDb(CompletionStatus paramCompletionStatus) {
/* 137 */     return cannotWriteRepositoryDb(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL cannotWriteRepositoryDb(Throwable paramThrowable) {
/* 141 */     return cannotWriteRepositoryDb(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL cannotWriteRepositoryDb() {
/* 145 */     return cannotWriteRepositoryDb(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL serverNotExpectedToRegister(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 151 */     INTERNAL localINTERNAL = new INTERNAL(1398079891, paramCompletionStatus);
/* 152 */     if (paramThrowable != null) {
/* 153 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 155 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 156 */       Object[] arrayOfObject = null;
/* 157 */       doLog(Level.WARNING, "ACTIVATION.serverNotExpectedToRegister", arrayOfObject, ActivationSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 161 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL serverNotExpectedToRegister(CompletionStatus paramCompletionStatus) {
/* 165 */     return serverNotExpectedToRegister(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL serverNotExpectedToRegister(Throwable paramThrowable) {
/* 169 */     return serverNotExpectedToRegister(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL serverNotExpectedToRegister() {
/* 173 */     return serverNotExpectedToRegister(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL unableToStartProcess(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 179 */     INTERNAL localINTERNAL = new INTERNAL(1398079892, paramCompletionStatus);
/* 180 */     if (paramThrowable != null) {
/* 181 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 183 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 184 */       Object[] arrayOfObject = null;
/* 185 */       doLog(Level.WARNING, "ACTIVATION.unableToStartProcess", arrayOfObject, ActivationSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 189 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL unableToStartProcess(CompletionStatus paramCompletionStatus) {
/* 193 */     return unableToStartProcess(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL unableToStartProcess(Throwable paramThrowable) {
/* 197 */     return unableToStartProcess(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL unableToStartProcess() {
/* 201 */     return unableToStartProcess(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL serverNotRunning(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 207 */     INTERNAL localINTERNAL = new INTERNAL(1398079894, paramCompletionStatus);
/* 208 */     if (paramThrowable != null) {
/* 209 */       localINTERNAL.initCause(paramThrowable);
/*     */     }
/* 211 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 212 */       Object[] arrayOfObject = null;
/* 213 */       doLog(Level.WARNING, "ACTIVATION.serverNotRunning", arrayOfObject, ActivationSystemException.class, localINTERNAL);
/*     */     }
/*     */ 
/* 217 */     return localINTERNAL;
/*     */   }
/*     */ 
/*     */   public INTERNAL serverNotRunning(CompletionStatus paramCompletionStatus) {
/* 221 */     return serverNotRunning(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public INTERNAL serverNotRunning(Throwable paramThrowable) {
/* 225 */     return serverNotRunning(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public INTERNAL serverNotRunning() {
/* 229 */     return serverNotRunning(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ 
/*     */   public OBJECT_NOT_EXIST errorInBadServerIdHandler(CompletionStatus paramCompletionStatus, Throwable paramThrowable)
/*     */   {
/* 239 */     OBJECT_NOT_EXIST localOBJECT_NOT_EXIST = new OBJECT_NOT_EXIST(1398079889, paramCompletionStatus);
/* 240 */     if (paramThrowable != null) {
/* 241 */       localOBJECT_NOT_EXIST.initCause(paramThrowable);
/*     */     }
/* 243 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 244 */       Object[] arrayOfObject = null;
/* 245 */       doLog(Level.WARNING, "ACTIVATION.errorInBadServerIdHandler", arrayOfObject, ActivationSystemException.class, localOBJECT_NOT_EXIST);
/*     */     }
/*     */ 
/* 249 */     return localOBJECT_NOT_EXIST;
/*     */   }
/*     */ 
/*     */   public OBJECT_NOT_EXIST errorInBadServerIdHandler(CompletionStatus paramCompletionStatus) {
/* 253 */     return errorInBadServerIdHandler(paramCompletionStatus, null);
/*     */   }
/*     */ 
/*     */   public OBJECT_NOT_EXIST errorInBadServerIdHandler(Throwable paramThrowable) {
/* 257 */     return errorInBadServerIdHandler(CompletionStatus.COMPLETED_NO, paramThrowable);
/*     */   }
/*     */ 
/*     */   public OBJECT_NOT_EXIST errorInBadServerIdHandler() {
/* 261 */     return errorInBadServerIdHandler(CompletionStatus.COMPLETED_NO, null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.logging.ActivationSystemException
 * JD-Core Version:    0.6.2
 */