/*     */ package java.text;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.text.IntHashtable;
/*     */ import sun.text.UCompactIntArray;
/*     */ 
/*     */ final class RBCollationTables
/*     */ {
/*     */   static final int EXPANDCHARINDEX = 2113929216;
/*     */   static final int CONTRACTCHARINDEX = 2130706432;
/*     */   static final int UNMAPPED = -1;
/*     */   static final int PRIMARYORDERMASK = -65536;
/*     */   static final int SECONDARYORDERMASK = 65280;
/*     */   static final int TERTIARYORDERMASK = 255;
/*     */   static final int PRIMARYDIFFERENCEONLY = -65536;
/*     */   static final int SECONDARYDIFFERENCEONLY = -256;
/*     */   static final int PRIMARYORDERSHIFT = 16;
/*     */   static final int SECONDARYORDERSHIFT = 8;
/* 292 */   private String rules = null;
/* 293 */   private boolean frenchSec = false;
/* 294 */   private boolean seAsianSwapping = false;
/*     */ 
/* 296 */   private UCompactIntArray mapping = null;
/* 297 */   private Vector contractTable = null;
/* 298 */   private Vector expandTable = null;
/* 299 */   private IntHashtable contractFlags = null;
/*     */ 
/* 301 */   private short maxSecOrder = 0;
/* 302 */   private short maxTerOrder = 0;
/*     */ 
/*     */   public RBCollationTables(String paramString, int paramInt)
/*     */     throws ParseException
/*     */   {
/*  80 */     this.rules = paramString;
/*     */ 
/*  82 */     RBTableBuilder localRBTableBuilder = new RBTableBuilder(new BuildAPI(null));
/*  83 */     localRBTableBuilder.build(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public String getRules()
/*     */   {
/* 138 */     return this.rules;
/*     */   }
/*     */ 
/*     */   public boolean isFrenchSec() {
/* 142 */     return this.frenchSec;
/*     */   }
/*     */ 
/*     */   public boolean isSEAsianSwapping() {
/* 146 */     return this.seAsianSwapping;
/*     */   }
/*     */ 
/*     */   Vector getContractValues(int paramInt)
/*     */   {
/* 160 */     int i = this.mapping.elementAt(paramInt);
/* 161 */     return getContractValuesImpl(i - 2130706432);
/*     */   }
/*     */ 
/*     */   private Vector getContractValuesImpl(int paramInt)
/*     */   {
/* 167 */     if (paramInt >= 0)
/*     */     {
/* 169 */       return (Vector)this.contractTable.elementAt(paramInt);
/*     */     }
/*     */ 
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */   boolean usedInContractSeq(int paramInt)
/*     */   {
/* 182 */     return this.contractFlags.get(paramInt) == 1;
/*     */   }
/*     */ 
/*     */   int getMaxExpansion(int paramInt)
/*     */   {
/* 197 */     int i = 1;
/*     */ 
/* 199 */     if (this.expandTable != null)
/*     */     {
/* 204 */       for (int j = 0; j < this.expandTable.size(); j++) {
/* 205 */         int[] arrayOfInt = (int[])this.expandTable.elementAt(j);
/* 206 */         int k = arrayOfInt.length;
/*     */ 
/* 208 */         if ((k > i) && (arrayOfInt[(k - 1)] == paramInt)) {
/* 209 */           i = k;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 214 */     return i;
/*     */   }
/*     */ 
/*     */   final int[] getExpandValueList(int paramInt)
/*     */   {
/* 223 */     return (int[])this.expandTable.elementAt(paramInt - 2113929216);
/*     */   }
/*     */ 
/*     */   int getUnicodeOrder(int paramInt)
/*     */   {
/* 232 */     return this.mapping.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   short getMaxSecOrder() {
/* 236 */     return this.maxSecOrder;
/*     */   }
/*     */ 
/*     */   short getMaxTerOrder() {
/* 240 */     return this.maxTerOrder;
/*     */   }
/*     */ 
/*     */   static void reverse(StringBuffer paramStringBuffer, int paramInt1, int paramInt2)
/*     */   {
/* 250 */     int i = paramInt1;
/*     */ 
/* 253 */     int j = paramInt2 - 1;
/* 254 */     while (i < j) {
/* 255 */       char c = paramStringBuffer.charAt(i);
/* 256 */       paramStringBuffer.setCharAt(i, paramStringBuffer.charAt(j));
/* 257 */       paramStringBuffer.setCharAt(j, c);
/* 258 */       i++;
/* 259 */       j--;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final int getEntry(Vector paramVector, String paramString, boolean paramBoolean) {
/* 264 */     for (int i = 0; i < paramVector.size(); i++) {
/* 265 */       EntryPair localEntryPair = (EntryPair)paramVector.elementAt(i);
/* 266 */       if ((localEntryPair.fwd == paramBoolean) && (localEntryPair.entryName.equals(paramString))) {
/* 267 */         return i;
/*     */       }
/*     */     }
/* 270 */     return -1;
/*     */   }
/*     */ 
/*     */   final class BuildAPI
/*     */   {
/*     */     private BuildAPI()
/*     */     {
/*     */     }
/*     */ 
/*     */     void fillInTables(boolean paramBoolean1, boolean paramBoolean2, UCompactIntArray paramUCompactIntArray, Vector paramVector1, Vector paramVector2, IntHashtable paramIntHashtable, short paramShort1, short paramShort2)
/*     */     {
/* 120 */       RBCollationTables.this.frenchSec = paramBoolean1;
/* 121 */       RBCollationTables.this.seAsianSwapping = paramBoolean2;
/* 122 */       RBCollationTables.this.mapping = paramUCompactIntArray;
/* 123 */       RBCollationTables.this.contractTable = paramVector1;
/* 124 */       RBCollationTables.this.expandTable = paramVector2;
/* 125 */       RBCollationTables.this.contractFlags = paramIntHashtable;
/* 126 */       RBCollationTables.this.maxSecOrder = paramShort1;
/* 127 */       RBCollationTables.this.maxTerOrder = paramShort2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.RBCollationTables
 * JD-Core Version:    0.6.2
 */