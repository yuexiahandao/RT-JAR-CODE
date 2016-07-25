/*      */ package sun.org.mozilla.javascript.internal.regexp;
/*      */ 
/*      */ import sun.org.mozilla.javascript.internal.Context;
/*      */ 
/*      */ class CompilerState
/*      */ {
/*      */   Context cx;
/*      */   char[] cpbegin;
/*      */   int cpend;
/*      */   int cp;
/*      */   int flags;
/*      */   int parenCount;
/*      */   int parenNesting;
/*      */   int classCount;
/*      */   int progLength;
/*      */   RENode result;
/*      */ 
/*      */   CompilerState(Context paramContext, char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 2664 */     this.cx = paramContext;
/* 2665 */     this.cpbegin = paramArrayOfChar;
/* 2666 */     this.cp = 0;
/* 2667 */     this.cpend = paramInt1;
/* 2668 */     this.flags = paramInt2;
/* 2669 */     this.parenCount = 0;
/* 2670 */     this.classCount = 0;
/* 2671 */     this.progLength = 0;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.CompilerState
 * JD-Core Version:    0.6.2
 */