/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class InputMap
/*     */   implements Serializable
/*     */ {
/*     */   private transient ArrayTable arrayTable;
/*     */   private InputMap parent;
/*     */ 
/*     */   public void setParent(InputMap paramInputMap)
/*     */   {
/*  74 */     this.parent = paramInputMap;
/*     */   }
/*     */ 
/*     */   public InputMap getParent()
/*     */   {
/*  84 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void put(KeyStroke paramKeyStroke, Object paramObject)
/*     */   {
/*  93 */     if (paramKeyStroke == null) {
/*  94 */       return;
/*     */     }
/*  96 */     if (paramObject == null) {
/*  97 */       remove(paramKeyStroke);
/*     */     }
/*     */     else {
/* 100 */       if (this.arrayTable == null) {
/* 101 */         this.arrayTable = new ArrayTable();
/*     */       }
/* 103 */       this.arrayTable.put(paramKeyStroke, paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object get(KeyStroke paramKeyStroke)
/*     */   {
/* 112 */     if (this.arrayTable == null) {
/* 113 */       localObject = getParent();
/*     */ 
/* 115 */       if (localObject != null) {
/* 116 */         return ((InputMap)localObject).get(paramKeyStroke);
/*     */       }
/* 118 */       return null;
/*     */     }
/* 120 */     Object localObject = this.arrayTable.get(paramKeyStroke);
/*     */ 
/* 122 */     if (localObject == null) {
/* 123 */       InputMap localInputMap = getParent();
/*     */ 
/* 125 */       if (localInputMap != null) {
/* 126 */         return localInputMap.get(paramKeyStroke);
/*     */       }
/*     */     }
/* 129 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void remove(KeyStroke paramKeyStroke)
/*     */   {
/* 137 */     if (this.arrayTable != null)
/* 138 */       this.arrayTable.remove(paramKeyStroke);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 146 */     if (this.arrayTable != null)
/* 147 */       this.arrayTable.clear();
/*     */   }
/*     */ 
/*     */   public KeyStroke[] keys()
/*     */   {
/* 155 */     if (this.arrayTable == null) {
/* 156 */       return null;
/*     */     }
/* 158 */     KeyStroke[] arrayOfKeyStroke = new KeyStroke[this.arrayTable.size()];
/* 159 */     this.arrayTable.getKeys(arrayOfKeyStroke);
/* 160 */     return arrayOfKeyStroke;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 167 */     if (this.arrayTable == null) {
/* 168 */       return 0;
/*     */     }
/* 170 */     return this.arrayTable.size();
/*     */   }
/*     */ 
/*     */   public KeyStroke[] allKeys()
/*     */   {
/* 179 */     int i = size();
/* 180 */     InputMap localInputMap = getParent();
/*     */ 
/* 182 */     if (i == 0) {
/* 183 */       if (localInputMap != null) {
/* 184 */         return localInputMap.allKeys();
/*     */       }
/* 186 */       return keys();
/*     */     }
/* 188 */     if (localInputMap == null) {
/* 189 */       return keys();
/*     */     }
/* 191 */     KeyStroke[] arrayOfKeyStroke1 = keys();
/* 192 */     KeyStroke[] arrayOfKeyStroke2 = localInputMap.allKeys();
/*     */ 
/* 194 */     if (arrayOfKeyStroke2 == null) {
/* 195 */       return arrayOfKeyStroke1;
/*     */     }
/* 197 */     if (arrayOfKeyStroke1 == null)
/*     */     {
/* 200 */       return arrayOfKeyStroke2;
/*     */     }
/*     */ 
/* 203 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 206 */     for (int j = arrayOfKeyStroke1.length - 1; j >= 0; j--) {
/* 207 */       localHashMap.put(arrayOfKeyStroke1[j], arrayOfKeyStroke1[j]);
/*     */     }
/* 209 */     for (j = arrayOfKeyStroke2.length - 1; j >= 0; j--) {
/* 210 */       localHashMap.put(arrayOfKeyStroke2[j], arrayOfKeyStroke2[j]);
/*     */     }
/*     */ 
/* 213 */     KeyStroke[] arrayOfKeyStroke3 = new KeyStroke[localHashMap.size()];
/*     */ 
/* 215 */     return (KeyStroke[])localHashMap.keySet().toArray(arrayOfKeyStroke3);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 219 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 221 */     ArrayTable.writeArrayTable(paramObjectOutputStream, this.arrayTable);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws ClassNotFoundException, IOException
/*     */   {
/* 226 */     paramObjectInputStream.defaultReadObject();
/* 227 */     for (int i = paramObjectInputStream.readInt() - 1; i >= 0; i--)
/* 228 */       put((KeyStroke)paramObjectInputStream.readObject(), paramObjectInputStream.readObject());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.InputMap
 * JD-Core Version:    0.6.2
 */