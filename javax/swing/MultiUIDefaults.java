/*     */ package javax.swing;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ class MultiUIDefaults extends UIDefaults
/*     */ {
/*     */   private UIDefaults[] tables;
/*     */ 
/*     */   public MultiUIDefaults(UIDefaults[] paramArrayOfUIDefaults)
/*     */   {
/*  47 */     this.tables = paramArrayOfUIDefaults;
/*     */   }
/*     */ 
/*     */   public MultiUIDefaults()
/*     */   {
/*  52 */     this.tables = new UIDefaults[0];
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/*  58 */     Object localObject = super.get(paramObject);
/*  59 */     if (localObject != null) {
/*  60 */       return localObject;
/*     */     }
/*     */ 
/*  63 */     for (UIDefaults localUIDefaults : this.tables) {
/*  64 */       localObject = localUIDefaults != null ? localUIDefaults.get(paramObject) : null;
/*  65 */       if (localObject != null) {
/*  66 */         return localObject;
/*     */       }
/*     */     }
/*     */ 
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject, Locale paramLocale)
/*     */   {
/*  76 */     Object localObject = super.get(paramObject, paramLocale);
/*  77 */     if (localObject != null) {
/*  78 */       return localObject;
/*     */     }
/*     */ 
/*  81 */     for (UIDefaults localUIDefaults : this.tables) {
/*  82 */       localObject = localUIDefaults != null ? localUIDefaults.get(paramObject, paramLocale) : null;
/*  83 */       if (localObject != null) {
/*  84 */         return localObject;
/*     */       }
/*     */     }
/*     */ 
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  93 */     return entrySet().size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  98 */     return size() == 0;
/*     */   }
/*     */ 
/*     */   public Enumeration<Object> keys()
/*     */   {
/* 104 */     return new MultiUIDefaultsEnumerator(MultiUIDefaults.MultiUIDefaultsEnumerator.Type.KEYS, entrySet());
/*     */   }
/*     */ 
/*     */   public Enumeration<Object> elements()
/*     */   {
/* 111 */     return new MultiUIDefaultsEnumerator(MultiUIDefaults.MultiUIDefaultsEnumerator.Type.ELEMENTS, entrySet());
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<Object, Object>> entrySet()
/*     */   {
/* 117 */     HashSet localHashSet = new HashSet();
/* 118 */     for (int i = this.tables.length - 1; i >= 0; i--) {
/* 119 */       if (this.tables[i] != null) {
/* 120 */         localHashSet.addAll(this.tables[i].entrySet());
/*     */       }
/*     */     }
/* 123 */     localHashSet.addAll(super.entrySet());
/* 124 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   protected void getUIError(String paramString)
/*     */   {
/* 129 */     if (this.tables.length > 0)
/* 130 */       this.tables[0].getUIError(paramString);
/*     */     else
/* 132 */       super.getUIError(paramString);
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 163 */     Object localObject1 = null;
/* 164 */     for (int i = this.tables.length - 1; i >= 0; i--) {
/* 165 */       if (this.tables[i] != null) {
/* 166 */         Object localObject3 = this.tables[i].remove(paramObject);
/* 167 */         if (localObject3 != null) {
/* 168 */           localObject1 = localObject3;
/*     */         }
/*     */       }
/*     */     }
/* 172 */     Object localObject2 = super.remove(paramObject);
/* 173 */     if (localObject2 != null) {
/* 174 */       localObject1 = localObject2;
/*     */     }
/*     */ 
/* 177 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 182 */     super.clear();
/* 183 */     for (UIDefaults localUIDefaults : this.tables)
/* 184 */       if (localUIDefaults != null)
/* 185 */         localUIDefaults.clear();
/*     */   }
/*     */ 
/*     */   public synchronized String toString()
/*     */   {
/* 192 */     StringBuffer localStringBuffer = new StringBuffer();
/* 193 */     localStringBuffer.append("{");
/* 194 */     Enumeration localEnumeration = keys();
/* 195 */     while (localEnumeration.hasMoreElements()) {
/* 196 */       Object localObject = localEnumeration.nextElement();
/* 197 */       localStringBuffer.append(localObject + "=" + get(localObject) + ", ");
/*     */     }
/* 199 */     int i = localStringBuffer.length();
/* 200 */     if (i > 1) {
/* 201 */       localStringBuffer.delete(i - 2, i);
/*     */     }
/* 203 */     localStringBuffer.append("}");
/* 204 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static class MultiUIDefaultsEnumerator
/*     */     implements Enumeration<Object>
/*     */   {
/*     */     private Iterator<Map.Entry<Object, Object>> iterator;
/*     */     private Type type;
/*     */ 
/*     */     MultiUIDefaultsEnumerator(Type paramType, Set<Map.Entry<Object, Object>> paramSet)
/*     */     {
/* 143 */       this.type = paramType;
/* 144 */       this.iterator = paramSet.iterator();
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements() {
/* 148 */       return this.iterator.hasNext();
/*     */     }
/*     */ 
/*     */     public Object nextElement() {
/* 152 */       switch (MultiUIDefaults.1.$SwitchMap$javax$swing$MultiUIDefaults$MultiUIDefaultsEnumerator$Type[this.type.ordinal()]) { case 1:
/* 153 */         return ((Map.Entry)this.iterator.next()).getKey();
/*     */       case 2:
/* 154 */         return ((Map.Entry)this.iterator.next()).getValue(); }
/* 155 */       return null;
/*     */     }
/*     */ 
/*     */     public static enum Type
/*     */     {
/* 138 */       KEYS, ELEMENTS;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.MultiUIDefaults
 * JD-Core Version:    0.6.2
 */