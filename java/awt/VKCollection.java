/*     */ package java.awt;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ class VKCollection
/*     */ {
/*     */   Map code2name;
/*     */   Map name2code;
/*     */ 
/*     */   public VKCollection()
/*     */   {
/* 871 */     this.code2name = new HashMap();
/* 872 */     this.name2code = new HashMap();
/*     */   }
/*     */ 
/*     */   public synchronized void put(String paramString, Integer paramInteger) {
/* 876 */     assert ((paramString != null) && (paramInteger != null));
/* 877 */     assert (findName(paramInteger) == null);
/* 878 */     assert (findCode(paramString) == null);
/* 879 */     this.code2name.put(paramInteger, paramString);
/* 880 */     this.name2code.put(paramString, paramInteger);
/*     */   }
/*     */ 
/*     */   public synchronized Integer findCode(String paramString) {
/* 884 */     assert (paramString != null);
/* 885 */     return (Integer)this.name2code.get(paramString);
/*     */   }
/*     */ 
/*     */   public synchronized String findName(Integer paramInteger) {
/* 889 */     assert (paramInteger != null);
/* 890 */     return (String)this.code2name.get(paramInteger);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.VKCollection
 * JD-Core Version:    0.6.2
 */