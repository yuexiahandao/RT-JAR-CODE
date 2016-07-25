/*     */ package sun.management;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import javax.management.openmbean.OpenType;
/*     */ import javax.management.openmbean.TabularType;
/*     */ 
/*     */ public abstract class LazyCompositeData
/*     */   implements CompositeData, Serializable
/*     */ {
/*     */   private CompositeData compositeData;
/*     */   private static final long serialVersionUID = -2190411934472666714L;
/*     */ 
/*     */   public boolean containsKey(String paramString)
/*     */   {
/*  52 */     return compositeData().containsKey(paramString);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject) {
/*  56 */     return compositeData().containsValue(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  60 */     return compositeData().equals(paramObject);
/*     */   }
/*     */ 
/*     */   public Object get(String paramString) {
/*  64 */     return compositeData().get(paramString);
/*     */   }
/*     */ 
/*     */   public Object[] getAll(String[] paramArrayOfString) {
/*  68 */     return compositeData().getAll(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public CompositeType getCompositeType() {
/*  72 */     return compositeData().getCompositeType();
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  76 */     return compositeData().hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  81 */     return compositeData().toString();
/*     */   }
/*     */ 
/*     */   public Collection values() {
/*  85 */     return compositeData().values();
/*     */   }
/*     */ 
/*     */   private synchronized CompositeData compositeData()
/*     */   {
/*  92 */     if (this.compositeData != null)
/*  93 */       return this.compositeData;
/*  94 */     this.compositeData = getCompositeData();
/*  95 */     return this.compositeData;
/*     */   }
/*     */ 
/*     */   protected Object writeReplace()
/*     */     throws ObjectStreamException
/*     */   {
/* 105 */     return compositeData();
/*     */   }
/*     */ 
/*     */   protected abstract CompositeData getCompositeData();
/*     */ 
/*     */   static String getString(CompositeData paramCompositeData, String paramString)
/*     */   {
/* 119 */     if (paramCompositeData == null) {
/* 120 */       throw new IllegalArgumentException("Null CompositeData");
/*     */     }
/* 122 */     return (String)paramCompositeData.get(paramString);
/*     */   }
/*     */ 
/*     */   static boolean getBoolean(CompositeData paramCompositeData, String paramString) {
/* 126 */     if (paramCompositeData == null) {
/* 127 */       throw new IllegalArgumentException("Null CompositeData");
/*     */     }
/* 129 */     return ((Boolean)paramCompositeData.get(paramString)).booleanValue();
/*     */   }
/*     */ 
/*     */   static long getLong(CompositeData paramCompositeData, String paramString) {
/* 133 */     if (paramCompositeData == null) {
/* 134 */       throw new IllegalArgumentException("Null CompositeData");
/*     */     }
/* 136 */     return ((Long)paramCompositeData.get(paramString)).longValue();
/*     */   }
/*     */ 
/*     */   static int getInt(CompositeData paramCompositeData, String paramString) {
/* 140 */     if (paramCompositeData == null) {
/* 141 */       throw new IllegalArgumentException("Null CompositeData");
/*     */     }
/* 143 */     return ((Integer)paramCompositeData.get(paramString)).intValue();
/*     */   }
/*     */ 
/*     */   protected static boolean isTypeMatched(CompositeType paramCompositeType1, CompositeType paramCompositeType2)
/*     */   {
/* 152 */     if (paramCompositeType1 == paramCompositeType2) return true;
/*     */ 
/* 156 */     Set localSet = paramCompositeType1.keySet();
/*     */ 
/* 159 */     if (!paramCompositeType2.keySet().containsAll(localSet)) {
/* 160 */       return false;
/*     */     }
/* 162 */     for (Iterator localIterator = localSet.iterator(); localIterator.hasNext(); ) {
/* 163 */       String str = (String)localIterator.next();
/* 164 */       OpenType localOpenType1 = paramCompositeType1.getType(str);
/* 165 */       OpenType localOpenType2 = paramCompositeType2.getType(str);
/* 166 */       if ((localOpenType1 instanceof CompositeType)) {
/* 167 */         if (!(localOpenType2 instanceof CompositeType))
/* 168 */           return false;
/* 169 */         if (!isTypeMatched((CompositeType)localOpenType1, (CompositeType)localOpenType2))
/* 170 */           return false;
/* 171 */       } else if ((localOpenType1 instanceof TabularType)) {
/* 172 */         if (!(localOpenType2 instanceof TabularType))
/* 173 */           return false;
/* 174 */         if (!isTypeMatched((TabularType)localOpenType1, (TabularType)localOpenType2))
/* 175 */           return false;
/* 176 */       } else if (!localOpenType1.equals(localOpenType2)) {
/* 177 */         return false;
/*     */       }
/*     */     }
/* 180 */     return true;
/*     */   }
/*     */ 
/*     */   protected static boolean isTypeMatched(TabularType paramTabularType1, TabularType paramTabularType2) {
/* 184 */     if (paramTabularType1 == paramTabularType2) return true;
/*     */ 
/* 186 */     List localList1 = paramTabularType1.getIndexNames();
/* 187 */     List localList2 = paramTabularType2.getIndexNames();
/*     */ 
/* 190 */     if (!localList1.equals(localList2)) {
/* 191 */       return false;
/*     */     }
/* 193 */     return isTypeMatched(paramTabularType1.getRowType(), paramTabularType2.getRowType());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.LazyCompositeData
 * JD-Core Version:    0.6.2
 */