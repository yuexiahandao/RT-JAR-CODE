/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class TabSet
/*     */   implements Serializable
/*     */ {
/*     */   private TabStop[] tabs;
/*  55 */   private int hashCode = 2147483647;
/*     */ 
/*     */   public TabSet(TabStop[] paramArrayOfTabStop)
/*     */   {
/*  63 */     if (paramArrayOfTabStop != null) {
/*  64 */       int i = paramArrayOfTabStop.length;
/*     */ 
/*  66 */       this.tabs = new TabStop[i];
/*  67 */       System.arraycopy(paramArrayOfTabStop, 0, this.tabs, 0, i);
/*     */     }
/*     */     else {
/*  70 */       this.tabs = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getTabCount()
/*     */   {
/*  77 */     return this.tabs == null ? 0 : this.tabs.length;
/*     */   }
/*     */ 
/*     */   public TabStop getTab(int paramInt)
/*     */   {
/*  86 */     int i = getTabCount();
/*     */ 
/*  88 */     if ((paramInt < 0) || (paramInt >= i)) {
/*  89 */       throw new IllegalArgumentException(paramInt + " is outside the range of tabs");
/*     */     }
/*  91 */     return this.tabs[paramInt];
/*     */   }
/*     */ 
/*     */   public TabStop getTabAfter(float paramFloat)
/*     */   {
/*  99 */     int i = getTabIndexAfter(paramFloat);
/*     */ 
/* 101 */     return i == -1 ? null : this.tabs[i];
/*     */   }
/*     */ 
/*     */   public int getTabIndex(TabStop paramTabStop)
/*     */   {
/* 109 */     for (int i = getTabCount() - 1; i >= 0; i--)
/*     */     {
/* 111 */       if (getTab(i) == paramTabStop)
/* 112 */         return i; 
/*     */     }
/* 113 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getTabIndexAfter(float paramFloat)
/*     */   {
/* 123 */     int j = 0;
/* 124 */     int k = getTabCount();
/* 125 */     while (j != k) {
/* 126 */       int i = (k - j) / 2 + j;
/* 127 */       if (paramFloat > this.tabs[i].getPosition()) {
/* 128 */         if (j == i)
/* 129 */           j = k;
/*     */         else
/* 131 */           j = i;
/*     */       }
/*     */       else {
/* 134 */         if ((i == 0) || (paramFloat > this.tabs[(i - 1)].getPosition()))
/* 135 */           return i;
/* 136 */         k = i;
/*     */       }
/*     */     }
/*     */ 
/* 140 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 154 */     if (paramObject == this) {
/* 155 */       return true;
/*     */     }
/* 157 */     if ((paramObject instanceof TabSet)) {
/* 158 */       TabSet localTabSet = (TabSet)paramObject;
/* 159 */       int i = getTabCount();
/* 160 */       if (localTabSet.getTabCount() != i) {
/* 161 */         return false;
/*     */       }
/* 163 */       for (int j = 0; j < i; j++) {
/* 164 */         TabStop localTabStop1 = getTab(j);
/* 165 */         TabStop localTabStop2 = localTabSet.getTab(j);
/* 166 */         if (((localTabStop1 == null) && (localTabStop2 != null)) || ((localTabStop1 != null) && (!getTab(j).equals(localTabSet.getTab(j)))))
/*     */         {
/* 168 */           return false;
/*     */         }
/*     */       }
/* 171 */       return true;
/*     */     }
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 183 */     if (this.hashCode == 2147483647) {
/* 184 */       this.hashCode = 0;
/* 185 */       int i = getTabCount();
/* 186 */       for (int j = 0; j < i; j++) {
/* 187 */         TabStop localTabStop = getTab(j);
/* 188 */         this.hashCode ^= (localTabStop != null ? getTab(j).hashCode() : 0);
/*     */       }
/* 190 */       if (this.hashCode == 2147483647) {
/* 191 */         this.hashCode -= 1;
/*     */       }
/*     */     }
/* 194 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 201 */     int i = getTabCount();
/* 202 */     StringBuilder localStringBuilder = new StringBuilder("[ ");
/*     */ 
/* 204 */     for (int j = 0; j < i; j++) {
/* 205 */       if (j > 0)
/* 206 */         localStringBuilder.append(" - ");
/* 207 */       localStringBuilder.append(getTab(j).toString());
/*     */     }
/* 209 */     localStringBuilder.append(" ]");
/* 210 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.TabSet
 * JD-Core Version:    0.6.2
 */