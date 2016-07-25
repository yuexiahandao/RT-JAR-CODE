/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class Perm
/*      */ {
/*      */   public final String CLASS;
/*      */   public final String FULL_CLASS;
/*      */   public final String[] TARGETS;
/*      */   public final String[] ACTIONS;
/*      */ 
/*      */   public Perm(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2)
/*      */   {
/* 3726 */     this.CLASS = paramString1;
/* 3727 */     this.FULL_CLASS = paramString2;
/* 3728 */     this.TARGETS = paramArrayOfString1;
/* 3729 */     this.ACTIONS = paramArrayOfString2;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.Perm
 * JD-Core Version:    0.6.2
 */