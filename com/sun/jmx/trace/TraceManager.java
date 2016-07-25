/*     */ package com.sun.jmx.trace;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ @Deprecated
/*     */ public class TraceManager
/*     */   implements TraceDestination
/*     */ {
/*     */   private static Level getLevel(int paramInt)
/*     */   {
/* 127 */     switch (paramInt)
/*     */     {
/*     */     case 2:
/* 130 */       return Level.FINEST;
/*     */     case 1:
/* 132 */       return Level.FINER;
/*     */     case 0:
/* 134 */       return Level.SEVERE;
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */   private static Logger getLogger(int paramInt)
/*     */   {
/* 162 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/* 165 */       return Logger.getLogger("javax.management.mbeanserver");
/*     */     case 256:
/* 167 */       return Logger.getLogger("com.sun.jmx.snmp.daemon");
/*     */     case 512:
/* 169 */       return Logger.getLogger("com.sun.jmx.snmp");
/*     */     case 2:
/* 171 */       return Logger.getLogger("javax.management.mlet");
/*     */     case 4:
/* 173 */       return Logger.getLogger("javax.management.monitor");
/*     */     case 8:
/* 175 */       return Logger.getLogger("javax.management.timer");
/*     */     case 16:
/* 177 */       return Logger.getLogger("javax.management.misc");
/*     */     case 32:
/* 179 */       return Logger.getLogger("javax.management.notification");
/*     */     case 64:
/* 181 */       return Logger.getLogger("javax.management.relation");
/*     */     case 128:
/* 183 */       return Logger.getLogger("javax.management.modelmbean");
/*     */     }
/* 185 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isSelected(int paramInt1, int paramInt2)
/*     */   {
/*     */     Logger localLogger;
/*     */     Level localLevel;
/* 196 */     if (((localLogger = getLogger(paramInt2)) != null) && ((localLevel = getLevel(paramInt1)) != null))
/*     */     {
/* 199 */       return localLogger.isLoggable(localLevel);
/*     */     }
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean send(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 216 */     if (isSelected(paramInt1, paramInt2))
/*     */     {
/* 218 */       getLogger(paramInt2).logp(getLevel(paramInt1), paramString1, paramString2, paramString3);
/* 219 */       return true;
/*     */     }
/* 221 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean send(int paramInt1, int paramInt2, String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 236 */     if (isSelected(paramInt1, paramInt2)) {
/* 237 */       getLogger(paramInt2).log(getLevel(paramInt1), paramString1 + ": Exception occurred in " + paramString2, paramThrowable);
/*     */ 
/* 240 */       return true;
/*     */     }
/* 242 */     return false;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   void warning(String paramString1, String paramString2)
/*     */   {
/* 262 */     Logger.getLogger(paramString1).warning(paramString2);
/*     */   }
/*     */ 
/*     */   void fine(String paramString1, String paramString2)
/*     */   {
/* 272 */     Logger.getLogger(paramString1).fine(paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.trace.TraceManager
 * JD-Core Version:    0.6.2
 */