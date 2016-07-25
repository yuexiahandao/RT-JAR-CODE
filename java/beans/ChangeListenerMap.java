/*     */ package java.beans;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.EventListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ abstract class ChangeListenerMap<L extends EventListener>
/*     */ {
/*     */   private Map<String, L[]> map;
/*     */ 
/*     */   protected abstract L[] newArray(int paramInt);
/*     */ 
/*     */   protected abstract L newProxy(String paramString, L paramL);
/*     */ 
/*     */   public final synchronized void add(String paramString, L paramL)
/*     */   {
/*  78 */     if (this.map == null) {
/*  79 */       this.map = new HashMap();
/*     */     }
/*  81 */     EventListener[] arrayOfEventListener1 = (EventListener[])this.map.get(paramString);
/*  82 */     int i = arrayOfEventListener1 != null ? arrayOfEventListener1.length : 0;
/*     */ 
/*  86 */     EventListener[] arrayOfEventListener2 = newArray(i + 1);
/*  87 */     arrayOfEventListener2[i] = paramL;
/*  88 */     if (arrayOfEventListener1 != null) {
/*  89 */       System.arraycopy(arrayOfEventListener1, 0, arrayOfEventListener2, 0, i);
/*     */     }
/*  91 */     this.map.put(paramString, arrayOfEventListener2);
/*     */   }
/*     */ 
/*     */   public final synchronized void remove(String paramString, L paramL)
/*     */   {
/* 103 */     if (this.map != null) {
/* 104 */       EventListener[] arrayOfEventListener1 = (EventListener[])this.map.get(paramString);
/* 105 */       if (arrayOfEventListener1 != null)
/* 106 */         for (int i = 0; i < arrayOfEventListener1.length; i++)
/* 107 */           if (paramL.equals(arrayOfEventListener1[i])) {
/* 108 */             int j = arrayOfEventListener1.length - 1;
/* 109 */             if (j > 0) {
/* 110 */               EventListener[] arrayOfEventListener2 = newArray(j);
/* 111 */               System.arraycopy(arrayOfEventListener1, 0, arrayOfEventListener2, 0, i);
/* 112 */               System.arraycopy(arrayOfEventListener1, i + 1, arrayOfEventListener2, i, j - i);
/* 113 */               this.map.put(paramString, arrayOfEventListener2);
/* 114 */               break;
/*     */             }
/* 116 */             this.map.remove(paramString);
/* 117 */             if (!this.map.isEmpty()) break;
/* 118 */             this.map = null; break;
/*     */           }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final synchronized L[] get(String paramString)
/*     */   {
/* 135 */     return this.map != null ? (EventListener[])this.map.get(paramString) : null;
/*     */   }
/*     */ 
/*     */   public final void set(String paramString, L[] paramArrayOfL)
/*     */   {
/* 147 */     if (paramArrayOfL != null) {
/* 148 */       if (this.map == null) {
/* 149 */         this.map = new HashMap();
/*     */       }
/* 151 */       this.map.put(paramString, paramArrayOfL);
/*     */     }
/* 153 */     else if (this.map != null) {
/* 154 */       this.map.remove(paramString);
/* 155 */       if (this.map.isEmpty())
/* 156 */         this.map = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final synchronized L[] getListeners()
/*     */   {
/* 167 */     if (this.map == null) {
/* 168 */       return newArray(0);
/*     */     }
/* 170 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 172 */     EventListener[] arrayOfEventListener = (EventListener[])this.map.get(null);
/*     */     Object localObject2;
/* 173 */     if (arrayOfEventListener != null) {
/* 174 */       for (localObject2 : arrayOfEventListener) {
/* 175 */         localArrayList.add(localObject2);
/*     */       }
/*     */     }
/* 178 */     for (??? = this.map.entrySet().iterator(); ((Iterator)???).hasNext(); ) { Map.Entry localEntry = (Map.Entry)((Iterator)???).next();
/* 179 */       String str = (String)localEntry.getKey();
/* 180 */       if (str != null) {
/* 181 */         for (EventListener localEventListener : (EventListener[])localEntry.getValue()) {
/* 182 */           localArrayList.add(newProxy(str, localEventListener));
/*     */         }
/*     */       }
/*     */     }
/* 186 */     return (EventListener[])localArrayList.toArray(newArray(localArrayList.size()));
/*     */   }
/*     */ 
/*     */   public final L[] getListeners(String paramString)
/*     */   {
/* 196 */     if (paramString != null) {
/* 197 */       EventListener[] arrayOfEventListener = get(paramString);
/* 198 */       if (arrayOfEventListener != null) {
/* 199 */         return (EventListener[])arrayOfEventListener.clone();
/*     */       }
/*     */     }
/* 202 */     return newArray(0);
/*     */   }
/*     */ 
/*     */   public final synchronized boolean hasListeners(String paramString)
/*     */   {
/* 214 */     if (this.map == null) {
/* 215 */       return false;
/*     */     }
/* 217 */     EventListener[] arrayOfEventListener = (EventListener[])this.map.get(null);
/* 218 */     return (arrayOfEventListener != null) || ((paramString != null) && (null != this.map.get(paramString)));
/*     */   }
/*     */ 
/*     */   public final Set<Map.Entry<String, L[]>> getEntries()
/*     */   {
/* 229 */     return this.map != null ? this.map.entrySet() : Collections.emptySet();
/*     */   }
/*     */ 
/*     */   public abstract L extract(L paramL);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.ChangeListenerMap
 * JD-Core Version:    0.6.2
 */