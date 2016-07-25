/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ class ArrayTable
/*     */   implements Cloneable
/*     */ {
/*  47 */   private Object table = null;
/*     */   private static final int ARRAY_BOUNDARY = 8;
/*     */ 
/*     */   static void writeArrayTable(ObjectOutputStream paramObjectOutputStream, ArrayTable paramArrayTable)
/*     */     throws IOException
/*     */   {
/*     */     Object[] arrayOfObject1;
/*  63 */     if ((paramArrayTable == null) || ((arrayOfObject1 = paramArrayTable.getKeys(null)) == null)) {
/*  64 */       paramObjectOutputStream.writeInt(0);
/*     */     }
/*     */     else
/*     */     {
/*  70 */       int i = 0;
/*     */ 
/*  72 */       for (int j = 0; j < arrayOfObject1.length; j++) {
/*  73 */         Object localObject1 = arrayOfObject1[j];
/*     */ 
/*  76 */         if ((((localObject1 instanceof Serializable)) && ((paramArrayTable.get(localObject1) instanceof Serializable))) || (((localObject1 instanceof ClientPropertyKey)) && (((ClientPropertyKey)localObject1).getReportValueNotSerializable())))
/*     */         {
/*  83 */           i++;
/*     */         }
/*  85 */         else arrayOfObject1[j] = null;
/*     */ 
/*     */       }
/*     */ 
/*  89 */       paramObjectOutputStream.writeInt(i);
/*  90 */       if (i > 0)
/*  91 */         for (Object localObject2 : arrayOfObject1)
/*  92 */           if (localObject2 != null) {
/*  93 */             paramObjectOutputStream.writeObject(localObject2);
/*  94 */             paramObjectOutputStream.writeObject(paramArrayTable.get(localObject2));
/*  95 */             i--; if (i == 0)
/*     */               break;
/*     */           }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void put(Object paramObject1, Object paramObject2)
/*     */   {
/* 109 */     if (this.table == null) {
/* 110 */       this.table = new Object[] { paramObject1, paramObject2 };
/*     */     } else {
/* 112 */       int i = size();
/* 113 */       if (i < 8)
/*     */       {
/*     */         Object[] arrayOfObject1;
/*     */         int j;
/* 114 */         if (containsKey(paramObject1)) {
/* 115 */           arrayOfObject1 = (Object[])this.table;
/* 116 */           for (j = 0; j < arrayOfObject1.length - 1; j += 2)
/* 117 */             if (arrayOfObject1[j].equals(paramObject1)) {
/* 118 */               arrayOfObject1[(j + 1)] = paramObject2;
/* 119 */               break;
/*     */             }
/*     */         }
/*     */         else {
/* 123 */           arrayOfObject1 = (Object[])this.table;
/* 124 */           j = arrayOfObject1.length;
/* 125 */           Object[] arrayOfObject2 = new Object[j + 2];
/* 126 */           System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, j);
/*     */ 
/* 128 */           arrayOfObject2[j] = paramObject1;
/* 129 */           arrayOfObject2[(j + 1)] = paramObject2;
/* 130 */           this.table = arrayOfObject2;
/*     */         }
/*     */       } else {
/* 133 */         if ((i == 8) && (isArray())) {
/* 134 */           grow();
/*     */         }
/* 136 */         ((Hashtable)this.table).put(paramObject1, paramObject2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/* 145 */     Object localObject = null;
/* 146 */     if (this.table != null) {
/* 147 */       if (isArray()) {
/* 148 */         Object[] arrayOfObject = (Object[])this.table;
/* 149 */         for (int i = 0; i < arrayOfObject.length - 1; i += 2)
/* 150 */           if (arrayOfObject[i].equals(paramObject)) {
/* 151 */             localObject = arrayOfObject[(i + 1)];
/* 152 */             break;
/*     */           }
/*     */       }
/*     */       else {
/* 156 */         localObject = ((Hashtable)this.table).get(paramObject);
/*     */       }
/*     */     }
/* 159 */     return localObject;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 167 */     if (this.table == null)
/* 168 */       return 0;
/*     */     int i;
/* 169 */     if (isArray())
/* 170 */       i = ((Object[])this.table).length / 2;
/*     */     else {
/* 172 */       i = ((Hashtable)this.table).size();
/*     */     }
/* 174 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/* 181 */     boolean bool = false;
/* 182 */     if (this.table != null) {
/* 183 */       if (isArray()) {
/* 184 */         Object[] arrayOfObject = (Object[])this.table;
/* 185 */         for (int i = 0; i < arrayOfObject.length - 1; i += 2)
/* 186 */           if (arrayOfObject[i].equals(paramObject)) {
/* 187 */             bool = true;
/* 188 */             break;
/*     */           }
/*     */       }
/*     */       else {
/* 192 */         bool = ((Hashtable)this.table).containsKey(paramObject);
/*     */       }
/*     */     }
/* 195 */     return bool;
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 203 */     Object localObject = null;
/* 204 */     if (paramObject == null) {
/* 205 */       return null;
/*     */     }
/* 207 */     if (this.table != null) {
/* 208 */       if (isArray())
/*     */       {
/* 210 */         int i = -1;
/* 211 */         Object[] arrayOfObject1 = (Object[])this.table;
/* 212 */         for (int j = arrayOfObject1.length - 2; j >= 0; j -= 2) {
/* 213 */           if (arrayOfObject1[j].equals(paramObject)) {
/* 214 */             i = j;
/* 215 */             localObject = arrayOfObject1[(j + 1)];
/* 216 */             break;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 221 */         if (i != -1) {
/* 222 */           Object[] arrayOfObject2 = new Object[arrayOfObject1.length - 2];
/*     */ 
/* 224 */           System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, i);
/*     */ 
/* 228 */           if (i < arrayOfObject2.length) {
/* 229 */             System.arraycopy(arrayOfObject1, i + 2, arrayOfObject2, i, arrayOfObject2.length - i);
/*     */           }
/*     */ 
/* 232 */           this.table = (arrayOfObject2.length == 0 ? null : arrayOfObject2);
/*     */         }
/*     */       } else {
/* 235 */         localObject = ((Hashtable)this.table).remove(paramObject);
/*     */       }
/* 237 */       if ((size() == 7) && (!isArray())) {
/* 238 */         shrink();
/*     */       }
/*     */     }
/* 241 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 248 */     this.table = null;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 255 */     ArrayTable localArrayTable = new ArrayTable();
/*     */     Object localObject1;
/* 256 */     if (isArray()) {
/* 257 */       localObject1 = (Object[])this.table;
/* 258 */       for (int i = 0; i < localObject1.length - 1; i += 2)
/* 259 */         localArrayTable.put(localObject1[i], localObject1[(i + 1)]);
/*     */     }
/*     */     else {
/* 262 */       localObject1 = (Hashtable)this.table;
/* 263 */       Enumeration localEnumeration = ((Hashtable)localObject1).keys();
/* 264 */       while (localEnumeration.hasMoreElements()) {
/* 265 */         Object localObject2 = localEnumeration.nextElement();
/* 266 */         localArrayTable.put(localObject2, ((Hashtable)localObject1).get(localObject2));
/*     */       }
/*     */     }
/* 269 */     return localArrayTable;
/*     */   }
/*     */ 
/*     */   public Object[] getKeys(Object[] paramArrayOfObject)
/*     */   {
/* 279 */     if (this.table == null)
/* 280 */       return null;
/*     */     Object localObject;
/*     */     int i;
/*     */     int j;
/* 282 */     if (isArray()) {
/* 283 */       localObject = (Object[])this.table;
/* 284 */       if (paramArrayOfObject == null) {
/* 285 */         paramArrayOfObject = new Object[localObject.length / 2];
/*     */       }
/* 287 */       i = 0; for (j = 0; i < localObject.length - 1; )
/*     */       {
/* 289 */         paramArrayOfObject[j] = localObject[i];
/*     */ 
/* 287 */         i += 2;
/* 288 */         j++;
/*     */       }
/*     */     }
/*     */     else {
/* 292 */       localObject = (Hashtable)this.table;
/* 293 */       Enumeration localEnumeration = ((Hashtable)localObject).keys();
/* 294 */       j = ((Hashtable)localObject).size();
/* 295 */       if (paramArrayOfObject == null) {
/* 296 */         paramArrayOfObject = new Object[j];
/*     */       }
/* 298 */       while (j > 0) {
/* 299 */         paramArrayOfObject[(--j)] = localEnumeration.nextElement();
/*     */       }
/*     */     }
/* 302 */     return paramArrayOfObject;
/*     */   }
/*     */ 
/*     */   private boolean isArray()
/*     */   {
/* 310 */     return this.table instanceof Object[];
/*     */   }
/*     */ 
/*     */   private void grow()
/*     */   {
/* 317 */     Object[] arrayOfObject = (Object[])this.table;
/* 318 */     Hashtable localHashtable = new Hashtable(arrayOfObject.length / 2);
/* 319 */     for (int i = 0; i < arrayOfObject.length; i += 2) {
/* 320 */       localHashtable.put(arrayOfObject[i], arrayOfObject[(i + 1)]);
/*     */     }
/* 322 */     this.table = localHashtable;
/*     */   }
/*     */ 
/*     */   private void shrink()
/*     */   {
/* 329 */     Hashtable localHashtable = (Hashtable)this.table;
/* 330 */     Object[] arrayOfObject = new Object[localHashtable.size() * 2];
/* 331 */     Enumeration localEnumeration = localHashtable.keys();
/* 332 */     int i = 0;
/*     */ 
/* 334 */     while (localEnumeration.hasMoreElements()) {
/* 335 */       Object localObject = localEnumeration.nextElement();
/* 336 */       arrayOfObject[i] = localObject;
/* 337 */       arrayOfObject[(i + 1)] = localHashtable.get(localObject);
/* 338 */       i += 2;
/*     */     }
/* 340 */     this.table = arrayOfObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ArrayTable
 * JD-Core Version:    0.6.2
 */