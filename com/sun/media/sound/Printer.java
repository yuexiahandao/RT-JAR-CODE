/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ final class Printer
/*     */ {
/*     */   static final boolean err = false;
/*     */   static final boolean debug = false;
/*     */   static final boolean trace = false;
/*     */   static final boolean verbose = false;
/*     */   static final boolean release = false;
/*     */   static final boolean SHOW_THREADID = false;
/*     */   static final boolean SHOW_TIMESTAMP = false;
/* 107 */   private static long startTime = 0L;
/*     */ 
/*     */   public static void err(String paramString) {  } 
/*     */   public static void debug(String paramString) {  } 
/*     */   public static void trace(String paramString) {  } 
/*     */   public static void verbose(String paramString) {  } 
/*     */   public static void release(String paramString) {  } 
/* 110 */   public static void println(String paramString) { String str = "";
/*     */ 
/* 120 */     System.out.println(str + paramString); }
/*     */ 
/*     */   public static void println()
/*     */   {
/* 124 */     System.out.println();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.Printer
 * JD-Core Version:    0.6.2
 */