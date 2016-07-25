/*     */ package com.sun.jmx.trace;
/*     */ 
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ @Deprecated
/*     */ public class TraceImplementation
/*     */   implements TraceDestination
/*     */ {
/*     */   private PrintWriter out;
/*     */   private int level;
/*     */ 
/*     */   static TraceImplementation newDestination(int paramInt)
/*     */   {
/*     */     try
/*     */     {
/*  45 */       TraceImplementation localTraceImplementation = new TraceImplementation();
/*  46 */       localTraceImplementation.level = paramInt;
/*  47 */       return localTraceImplementation; } catch (IOException localIOException) {
/*     */     }
/*  49 */     return null;
/*     */   }
/*     */ 
/*     */   public static void init()
/*     */     throws IOException
/*     */   {
/*  64 */     Trace.setDestination(new TraceImplementation());
/*     */   }
/*     */ 
/*     */   public static void init(int paramInt)
/*     */     throws IOException
/*     */   {
/*  79 */     TraceImplementation localTraceImplementation = new TraceImplementation();
/*  80 */     localTraceImplementation.level = paramInt;
/*  81 */     Trace.setDestination(localTraceImplementation);
/*     */   }
/*     */ 
/*     */   public TraceImplementation()
/*     */     throws IOException
/*     */   {
/*     */     String str1;
/*  93 */     if ((str1 = System.getProperty("com.sun.jmx.trace.file")) != null)
/*     */     {
/*  97 */       this.out = new PrintWriter(new FileOutputStream(str1), true);
/*     */     }
/*     */     else
/*     */     {
/* 103 */       this.out = new PrintWriter(System.err, true);
/*     */     }
/*     */     String str2;
/* 107 */     if ((str2 = System.getProperty("com.sun.jmx.trace.level")) != null)
/*     */     {
/* 111 */       if (str2.equals("DEBUG"))
/*     */       {
/* 113 */         this.level = 2;
/*     */       }
/* 115 */       else if (str2.equals("TRACE"))
/*     */       {
/* 117 */         this.level = 1;
/*     */       }
/*     */       else
/*     */       {
/* 121 */         this.level = 0;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 128 */       this.level = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isSelected(int paramInt1, int paramInt2)
/*     */   {
/* 141 */     return paramInt1 <= this.level;
/*     */   }
/*     */ 
/*     */   public boolean send(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 153 */     if (isSelected(paramInt1, paramInt2))
/*     */     {
/* 155 */       this.out.println((paramString1 != null ? "Class:  " + paramString1 : "") + (paramString2 != null ? "\nMethod: " + paramString2 : "") + "\n\tlevel:   " + getLevel(paramInt1) + "\n\ttype:    " + getType(paramInt2) + "\n\tmessage: " + paramString3);
/*     */ 
/* 161 */       return true;
/*     */     }
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean send(int paramInt1, int paramInt2, String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 175 */     boolean bool = send(paramInt1, paramInt2, paramString1, paramString2, paramThrowable.toString());
/*     */ 
/* 177 */     if (bool) {
/* 178 */       paramThrowable.printStackTrace(this.out);
/*     */     }
/* 180 */     return bool;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   private static String getType(int paramInt)
/*     */   {
/* 199 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/* 202 */       return "INFO_MBEANSERVER";
/*     */     case 256:
/* 205 */       return "INFO_ADAPTOR_SNMP";
/*     */     case 512:
/* 208 */       return "INFO_SNMP";
/*     */     case 2:
/* 211 */       return "INFO_MLET";
/*     */     case 4:
/* 214 */       return "INFO_MONITOR";
/*     */     case 8:
/* 217 */       return "INFO_TIMER";
/*     */     case 16:
/* 220 */       return "INFO_MISC";
/*     */     case 32:
/* 223 */       return "INFO_NOTIFICATION";
/*     */     case 64:
/* 226 */       return "INFO_RELATION";
/*     */     case 128:
/* 229 */       return "INFO_MODELMBEAN";
/*     */     }
/*     */ 
/* 232 */     return "UNKNOWN_TRACE_TYPE";
/*     */   }
/*     */ 
/*     */   private static String getLevel(int paramInt)
/*     */   {
/* 242 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/* 245 */       return "LEVEL_ERROR";
/*     */     case 1:
/* 248 */       return "LEVEL_TRACE";
/*     */     case 2:
/* 251 */       return "LEVEL_DEBUG";
/*     */     }
/*     */ 
/* 254 */     return "UNKNOWN_TRACE_LEVEL";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.trace.TraceImplementation
 * JD-Core Version:    0.6.2
 */