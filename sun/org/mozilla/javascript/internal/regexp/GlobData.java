/*     */ package sun.org.mozilla.javascript.internal.regexp;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Function;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ 
/*     */ final class GlobData
/*     */ {
/*     */   int mode;
/*     */   int optarg;
/*     */   boolean global;
/*     */   String str;
/*     */   Scriptable arrayobj;
/*     */   Function lambda;
/*     */   String repstr;
/* 756 */   int dollar = -1;
/*     */   StringBuffer charBuf;
/*     */   int leftIndex;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.GlobData
 * JD-Core Version:    0.6.2
 */