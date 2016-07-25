/*     */ package com.sun.org.apache.regexp.internal;
/*     */ 
/*     */ import java.io.StringBufferInputStream;
/*     */ import java.io.StringReader;
/*     */ 
/*     */ final class RETestCase
/*     */ {
/* 647 */   private final StringBuffer log = new StringBuffer();
/*     */   private final int number;
/*     */   private final String tag;
/*     */   private final String pattern;
/*     */   private final String toMatch;
/*     */   private final boolean badPattern;
/*     */   private final boolean shouldMatch;
/*     */   private final String[] parens;
/*     */   private final RETest test;
/*     */   private RE regexp;
/*     */ 
/*     */   public RETestCase(RETest test, String tag, String pattern, String toMatch, boolean badPattern, boolean shouldMatch, String[] parens)
/*     */   {
/* 662 */     this.number = (++test.testCount);
/* 663 */     this.test = test;
/* 664 */     this.tag = tag;
/* 665 */     this.pattern = pattern;
/* 666 */     this.toMatch = toMatch;
/* 667 */     this.badPattern = badPattern;
/* 668 */     this.shouldMatch = shouldMatch;
/* 669 */     if (parens != null) {
/* 670 */       this.parens = new String[parens.length];
/* 671 */       for (int i = 0; i < parens.length; i++)
/* 672 */         this.parens[i] = parens[i];
/*     */     }
/*     */     else {
/* 675 */       this.parens = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void runTest()
/*     */   {
/* 681 */     this.test.say(this.tag + "(" + this.number + "): " + this.pattern);
/* 682 */     if (testCreation())
/* 683 */       testMatch();
/*     */   }
/*     */ 
/*     */   boolean testCreation()
/*     */   {
/*     */     try
/*     */     {
/* 692 */       this.regexp = new RE();
/* 693 */       this.regexp.setProgram(this.test.compiler.compile(this.pattern));
/*     */ 
/* 695 */       if (this.badPattern)
/*     */       {
/* 697 */         this.test.fail(this.log, "Was expected to be an error, but wasn't.");
/* 698 */         return false;
/*     */       }
/*     */ 
/* 701 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 707 */       if (this.badPattern)
/*     */       {
/* 709 */         this.log.append("   Match: ERR\n");
/* 710 */         success("Produces an error (" + e.toString() + "), as expected.");
/* 711 */         return false;
/*     */       }
/*     */ 
/* 715 */       String message = e.getMessage() == null ? e.toString() : e.getMessage();
/* 716 */       this.test.fail(this.log, "Produces an unexpected exception \"" + message + "\"");
/* 717 */       e.printStackTrace();
/*     */     }
/*     */     catch (Error e)
/*     */     {
/* 722 */       this.test.fail(this.log, "Compiler threw fatal error \"" + e.getMessage() + "\"");
/* 723 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 726 */     return false;
/*     */   }
/*     */ 
/*     */   private void testMatch()
/*     */   {
/* 731 */     this.log.append("   Match against: '" + this.toMatch + "'\n");
/*     */     try
/*     */     {
/* 736 */       boolean result = this.regexp.match(this.toMatch);
/* 737 */       this.log.append("   Matched: " + (result ? "YES" : "NO") + "\n");
/*     */ 
/* 740 */       if ((checkResult(result)) && ((!this.shouldMatch) || (checkParens())))
/*     */       {
/* 744 */         this.log.append("   Match using StringCharacterIterator\n");
/* 745 */         if (!tryMatchUsingCI(new StringCharacterIterator(this.toMatch))) {
/* 746 */           return;
/*     */         }
/* 748 */         this.log.append("   Match using CharacterArrayCharacterIterator\n");
/* 749 */         if (!tryMatchUsingCI(new CharacterArrayCharacterIterator(this.toMatch.toCharArray(), 0, this.toMatch.length()))) {
/* 750 */           return;
/*     */         }
/* 752 */         this.log.append("   Match using StreamCharacterIterator\n");
/* 753 */         if (!tryMatchUsingCI(new StreamCharacterIterator(new StringBufferInputStream(this.toMatch)))) {
/* 754 */           return;
/*     */         }
/* 756 */         this.log.append("   Match using ReaderCharacterIterator\n");
/* 757 */         if (!tryMatchUsingCI(new ReaderCharacterIterator(new StringReader(this.toMatch)))) {
/* 758 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 764 */       this.test.fail(this.log, "Matcher threw exception: " + e.toString());
/* 765 */       e.printStackTrace();
/*     */     }
/*     */     catch (Error e)
/*     */     {
/* 770 */       this.test.fail(this.log, "Matcher threw fatal error \"" + e.getMessage() + "\"");
/* 771 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean checkResult(boolean result)
/*     */   {
/* 778 */     if (result == this.shouldMatch) {
/* 779 */       success((this.shouldMatch ? "Matched" : "Did not match") + " \"" + this.toMatch + "\", as expected:");
/*     */ 
/* 781 */       return true;
/*     */     }
/* 783 */     if (this.shouldMatch)
/* 784 */       this.test.fail(this.log, "Did not match \"" + this.toMatch + "\", when expected to.");
/*     */     else {
/* 786 */       this.test.fail(this.log, "Matched \"" + this.toMatch + "\", when not expected to.");
/*     */     }
/* 788 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean checkParens()
/*     */   {
/* 800 */     this.log.append("   Paren count: " + this.regexp.getParenCount() + "\n");
/* 801 */     if (!assertEquals(this.log, "Wrong number of parens", this.parens.length, this.regexp.getParenCount()))
/*     */     {
/* 803 */       return false;
/*     */     }
/*     */ 
/* 807 */     for (int p = 0; p < this.regexp.getParenCount(); p++)
/*     */     {
/* 809 */       this.log.append("   Paren " + p + ": " + this.regexp.getParen(p) + "\n");
/*     */ 
/* 812 */       if ((!"null".equals(this.parens[p])) || (this.regexp.getParen(p) != null))
/*     */       {
/* 817 */         if (!assertEquals(this.log, "Wrong register " + p, this.parens[p], this.regexp.getParen(p)))
/*     */         {
/* 819 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 823 */     return true;
/*     */   }
/*     */ 
/*     */   boolean tryMatchUsingCI(CharacterIterator matchAgainst)
/*     */   {
/*     */     try {
/* 829 */       boolean result = this.regexp.match(matchAgainst, 0);
/* 830 */       this.log.append("   Match: " + (result ? "YES" : "NO") + "\n");
/* 831 */       return (checkResult(result)) && ((!this.shouldMatch) || (checkParens()));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 836 */       this.test.fail(this.log, "Matcher threw exception: " + e.toString());
/* 837 */       e.printStackTrace();
/*     */     }
/*     */     catch (Error e)
/*     */     {
/* 842 */       this.test.fail(this.log, "Matcher threw fatal error \"" + e.getMessage() + "\"");
/* 843 */       e.printStackTrace();
/*     */     }
/* 845 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean assertEquals(StringBuffer log, String message, String expected, String actual)
/*     */   {
/* 850 */     if (((expected != null) && (!expected.equals(actual))) || ((actual != null) && (!actual.equals(expected))))
/*     */     {
/* 853 */       this.test.fail(log, message + " (expected \"" + expected + "\", actual \"" + actual + "\")");
/*     */ 
/* 855 */       return false;
/*     */     }
/* 857 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean assertEquals(StringBuffer log, String message, int expected, int actual)
/*     */   {
/* 862 */     if (expected != actual) {
/* 863 */       this.test.fail(log, message + " (expected \"" + expected + "\", actual \"" + actual + "\")");
/*     */ 
/* 865 */       return false;
/*     */     }
/* 867 */     return true;
/*     */   }
/*     */ 
/*     */   void success(String s)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.regexp.internal.RETestCase
 * JD-Core Version:    0.6.2
 */