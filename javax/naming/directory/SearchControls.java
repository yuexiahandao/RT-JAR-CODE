/*     */ package javax.naming.directory;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class SearchControls
/*     */   implements Serializable
/*     */ {
/*     */   public static final int OBJECT_SCOPE = 0;
/*     */   public static final int ONELEVEL_SCOPE = 1;
/*     */   public static final int SUBTREE_SCOPE = 2;
/*     */   private int searchScope;
/*     */   private int timeLimit;
/*     */   private boolean derefLink;
/*     */   private boolean returnObj;
/*     */   private long countLimit;
/*     */   private String[] attributesToReturn;
/*     */   private static final long serialVersionUID = -2480540967773454797L;
/*     */ 
/*     */   public SearchControls()
/*     */   {
/* 154 */     this.searchScope = 1;
/* 155 */     this.timeLimit = 0;
/* 156 */     this.countLimit = 0L;
/* 157 */     this.derefLink = false;
/* 158 */     this.returnObj = false;
/* 159 */     this.attributesToReturn = null;
/*     */   }
/*     */ 
/*     */   public SearchControls(int paramInt1, long paramLong, int paramInt2, String[] paramArrayOfString, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 183 */     this.searchScope = paramInt1;
/* 184 */     this.timeLimit = paramInt2;
/* 185 */     this.derefLink = paramBoolean2;
/* 186 */     this.returnObj = paramBoolean1;
/* 187 */     this.countLimit = paramLong;
/* 188 */     this.attributesToReturn = paramArrayOfString;
/*     */   }
/*     */ 
/*     */   public int getSearchScope()
/*     */   {
/* 200 */     return this.searchScope;
/*     */   }
/*     */ 
/*     */   public int getTimeLimit()
/*     */   {
/* 211 */     return this.timeLimit;
/*     */   }
/*     */ 
/*     */   public boolean getDerefLinkFlag()
/*     */   {
/* 221 */     return this.derefLink;
/*     */   }
/*     */ 
/*     */   public boolean getReturningObjFlag()
/*     */   {
/* 231 */     return this.returnObj;
/*     */   }
/*     */ 
/*     */   public long getCountLimit()
/*     */   {
/* 243 */     return this.countLimit;
/*     */   }
/*     */ 
/*     */   public String[] getReturningAttributes()
/*     */   {
/* 257 */     return this.attributesToReturn;
/*     */   }
/*     */ 
/*     */   public void setSearchScope(int paramInt)
/*     */   {
/* 267 */     this.searchScope = paramInt;
/*     */   }
/*     */ 
/*     */   public void setTimeLimit(int paramInt)
/*     */   {
/* 278 */     this.timeLimit = paramInt;
/*     */   }
/*     */ 
/*     */   public void setDerefLinkFlag(boolean paramBoolean)
/*     */   {
/* 288 */     this.derefLink = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setReturningObjFlag(boolean paramBoolean)
/*     */   {
/* 302 */     this.returnObj = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setCountLimit(long paramLong)
/*     */   {
/* 315 */     this.countLimit = paramLong;
/*     */   }
/*     */ 
/*     */   public void setReturningAttributes(String[] paramArrayOfString)
/*     */   {
/* 329 */     this.attributesToReturn = paramArrayOfString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.directory.SearchControls
 * JD-Core Version:    0.6.2
 */