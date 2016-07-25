/*     */ package com.sun.jmx.trace;
/*     */ 
/*     */ import sun.misc.JavaAWTAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ @Deprecated
/*     */ public final class Trace
/*     */   implements TraceTags
/*     */ {
/*     */   private static TraceDestination out;
/*     */ 
/*     */   private static TraceDestination initDestination()
/*     */   {
/*     */     try
/*     */     {
/*  45 */       Class.forName("java.util.logging.LogManager");
/*     */ 
/*  50 */       return new TraceManager();
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/*     */     }
/*     */ 
/*  56 */     return null;
/*     */   }
/*     */ 
/*     */   public static synchronized void setDestination(TraceDestination paramTraceDestination)
/*     */   {
/*  68 */     JavaAWTAccess localJavaAWTAccess = SharedSecrets.getJavaAWTAccess();
/*  69 */     if (localJavaAWTAccess == null) {
/*  70 */       out = paramTraceDestination;
/*     */     }
/*     */     else
/*  73 */       localJavaAWTAccess.put(TraceDestination.class, paramTraceDestination);
/*     */   }
/*     */ 
/*     */   public static boolean isSelected(int paramInt1, int paramInt2)
/*     */   {
/*  88 */     TraceDestination localTraceDestination = out();
/*  89 */     if (localTraceDestination != null) return localTraceDestination.isSelected(paramInt1, paramInt2);
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean send(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 112 */     TraceDestination localTraceDestination = out();
/* 113 */     if (localTraceDestination == null) return false;
/* 114 */     if (!localTraceDestination.isSelected(paramInt1, paramInt2)) return false;
/* 115 */     return localTraceDestination.send(paramInt1, paramInt2, paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   public static boolean send(int paramInt1, int paramInt2, String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 134 */     TraceDestination localTraceDestination = out();
/* 135 */     if (localTraceDestination == null) return false;
/* 136 */     if (!localTraceDestination.isSelected(paramInt1, paramInt2)) return false;
/* 137 */     return localTraceDestination.send(paramInt1, paramInt2, paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public static void warning(String paramString1, String paramString2)
/*     */   {
/* 148 */     TraceDestination localTraceDestination = out();
/* 149 */     if ((localTraceDestination instanceof TraceManager))
/*     */     {
/* 151 */       ((TraceManager)localTraceDestination).warning(paramString1, paramString2);
/* 152 */     } else if (localTraceDestination != null)
/*     */     {
/* 154 */       localTraceDestination.send(1, 16, null, null, paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void fine(String paramString1, String paramString2)
/*     */   {
/* 165 */     TraceDestination localTraceDestination = out();
/* 166 */     if ((localTraceDestination instanceof TraceManager))
/*     */     {
/* 168 */       ((TraceManager)localTraceDestination).fine(paramString1, paramString2);
/* 169 */     } else if (localTraceDestination != null)
/*     */     {
/* 171 */       localTraceDestination.send(1, 16, null, null, paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized TraceDestination out()
/*     */   {
/* 178 */     JavaAWTAccess localJavaAWTAccess = SharedSecrets.getJavaAWTAccess();
/* 179 */     if (localJavaAWTAccess == null) {
/* 180 */       if (out == null)
/*     */       {
/* 182 */         out = initDestination();
/*     */       }
/* 184 */       return out;
/*     */     }
/*     */ 
/* 187 */     TraceDestination localTraceDestination = (TraceDestination)localJavaAWTAccess.get(TraceDestination.class);
/* 188 */     if (localTraceDestination == null)
/*     */     {
/* 190 */       localTraceDestination = initDestination();
/* 191 */       localJavaAWTAccess.put(TraceDestination.class, localTraceDestination);
/*     */     }
/* 193 */     return localTraceDestination;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.trace.Trace
 * JD-Core Version:    0.6.2
 */