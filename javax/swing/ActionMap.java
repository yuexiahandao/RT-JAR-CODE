/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ActionMap
/*     */   implements Serializable
/*     */ {
/*     */   private transient ArrayTable arrayTable;
/*     */   private ActionMap parent;
/*     */ 
/*     */   public void setParent(ActionMap paramActionMap)
/*     */   {
/*  77 */     this.parent = paramActionMap;
/*     */   }
/*     */ 
/*     */   public ActionMap getParent()
/*     */   {
/*  87 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void put(Object paramObject, Action paramAction)
/*     */   {
/*  98 */     if (paramObject == null) {
/*  99 */       return;
/*     */     }
/* 101 */     if (paramAction == null) {
/* 102 */       remove(paramObject);
/*     */     }
/*     */     else {
/* 105 */       if (this.arrayTable == null) {
/* 106 */         this.arrayTable = new ArrayTable();
/*     */       }
/* 108 */       this.arrayTable.put(paramObject, paramAction);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Action get(Object paramObject)
/*     */   {
/* 117 */     Action localAction = this.arrayTable == null ? null : (Action)this.arrayTable.get(paramObject);
/*     */ 
/* 120 */     if (localAction == null) {
/* 121 */       ActionMap localActionMap = getParent();
/*     */ 
/* 123 */       if (localActionMap != null) {
/* 124 */         return localActionMap.get(paramObject);
/*     */       }
/*     */     }
/* 127 */     return localAction;
/*     */   }
/*     */ 
/*     */   public void remove(Object paramObject)
/*     */   {
/* 134 */     if (this.arrayTable != null)
/* 135 */       this.arrayTable.remove(paramObject);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 143 */     if (this.arrayTable != null)
/* 144 */       this.arrayTable.clear();
/*     */   }
/*     */ 
/*     */   public Object[] keys()
/*     */   {
/* 152 */     if (this.arrayTable == null) {
/* 153 */       return null;
/*     */     }
/* 155 */     return this.arrayTable.getKeys(null);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 164 */     if (this.arrayTable == null) {
/* 165 */       return 0;
/*     */     }
/* 167 */     return this.arrayTable.size();
/*     */   }
/*     */ 
/*     */   public Object[] allKeys()
/*     */   {
/* 176 */     int i = size();
/* 177 */     ActionMap localActionMap = getParent();
/*     */ 
/* 179 */     if (i == 0) {
/* 180 */       if (localActionMap != null) {
/* 181 */         return localActionMap.allKeys();
/*     */       }
/* 183 */       return keys();
/*     */     }
/* 185 */     if (localActionMap == null) {
/* 186 */       return keys();
/*     */     }
/* 188 */     Object[] arrayOfObject1 = keys();
/* 189 */     Object[] arrayOfObject2 = localActionMap.allKeys();
/*     */ 
/* 191 */     if (arrayOfObject2 == null) {
/* 192 */       return arrayOfObject1;
/*     */     }
/* 194 */     if (arrayOfObject1 == null)
/*     */     {
/* 197 */       return arrayOfObject2;
/*     */     }
/*     */ 
/* 200 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 203 */     for (int j = arrayOfObject1.length - 1; j >= 0; j--) {
/* 204 */       localHashMap.put(arrayOfObject1[j], arrayOfObject1[j]);
/*     */     }
/* 206 */     for (j = arrayOfObject2.length - 1; j >= 0; j--) {
/* 207 */       localHashMap.put(arrayOfObject2[j], arrayOfObject2[j]);
/*     */     }
/* 209 */     return localHashMap.keySet().toArray();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 213 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 215 */     ArrayTable.writeArrayTable(paramObjectOutputStream, this.arrayTable);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws ClassNotFoundException, IOException
/*     */   {
/* 220 */     paramObjectInputStream.defaultReadObject();
/* 221 */     for (int i = paramObjectInputStream.readInt() - 1; i >= 0; i--)
/* 222 */       put(paramObjectInputStream.readObject(), (Action)paramObjectInputStream.readObject());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ActionMap
 * JD-Core Version:    0.6.2
 */