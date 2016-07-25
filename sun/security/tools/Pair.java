/*      */ package sun.security.tools;
/*      */ 
/*      */ class Pair<A, B>
/*      */ {
/*      */   public final A fst;
/*      */   public final B snd;
/*      */ 
/*      */   public Pair(A paramA, B paramB)
/*      */   {
/* 4216 */     this.fst = paramA;
/* 4217 */     this.snd = paramB;
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 4221 */     return "Pair[" + this.fst + "," + this.snd + "]";
/*      */   }
/*      */ 
/*      */   private static boolean equals(Object paramObject1, Object paramObject2) {
/* 4225 */     return ((paramObject1 == null) && (paramObject2 == null)) || ((paramObject1 != null) && (paramObject1.equals(paramObject2)));
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject) {
/* 4229 */     return ((paramObject instanceof Pair)) && (equals(this.fst, ((Pair)paramObject).fst)) && (equals(this.snd, ((Pair)paramObject).snd));
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 4236 */     if (this.fst == null) return this.snd == null ? 0 : this.snd.hashCode() + 1;
/* 4237 */     if (this.snd == null) return this.fst.hashCode() + 2;
/* 4238 */     return this.fst.hashCode() * 17 + this.snd.hashCode();
/*      */   }
/*      */ 
/*      */   public static <A, B> Pair<A, B> of(A paramA, B paramB) {
/* 4242 */     return new Pair(paramA, paramB);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.Pair
 * JD-Core Version:    0.6.2
 */