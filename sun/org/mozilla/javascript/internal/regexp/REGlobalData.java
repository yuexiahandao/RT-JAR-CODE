/*      */ package sun.org.mozilla.javascript.internal.regexp;
/*      */ 
/*      */ class REGlobalData
/*      */ {
/*      */   boolean multiline;
/*      */   RECompiled regexp;
/*      */   int lastParen;
/*      */   int skipped;
/*      */   int cp;
/*      */   long[] parens;
/*      */   REProgState stateStackTop;
/*      */   REBackTrackData backTrackStackTop;
/*      */ 
/*      */   int parens_index(int paramInt)
/*      */   {
/* 2755 */     return (int)this.parens[paramInt];
/*      */   }
/*      */ 
/*      */   int parens_length(int paramInt)
/*      */   {
/* 2763 */     return (int)(this.parens[paramInt] >>> 32);
/*      */   }
/*      */ 
/*      */   void set_parens(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 2768 */     this.parens[paramInt1] = (paramInt2 & 0xFFFFFFFF | paramInt3 << 32);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.REGlobalData
 * JD-Core Version:    0.6.2
 */