/*     */ package javax.naming.directory;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Vector;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ 
/*     */ public class BasicAttribute
/*     */   implements Attribute
/*     */ {
/*     */   protected String attrID;
/*     */   protected transient Vector<Object> values;
/*  92 */   protected boolean ordered = false;
/*     */   private static final long serialVersionUID = 6743528196119291326L;
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     BasicAttribute localBasicAttribute;
/*     */     try
/*     */     {
/*  97 */       localBasicAttribute = (BasicAttribute)super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  99 */       localBasicAttribute = new BasicAttribute(this.attrID, this.ordered);
/*     */     }
/* 101 */     localBasicAttribute.values = ((Vector)this.values.clone());
/* 102 */     return localBasicAttribute;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 132 */     if ((paramObject != null) && ((paramObject instanceof Attribute))) {
/* 133 */       Attribute localAttribute = (Attribute)paramObject;
/*     */ 
/* 136 */       if (isOrdered() != localAttribute.isOrdered())
/* 137 */         return false;
/*     */       int i;
/* 140 */       if ((this.attrID.equals(localAttribute.getID())) && ((i = size()) == localAttribute.size()))
/*     */       {
/*     */         try {
/* 143 */           if (isOrdered())
/*     */           {
/* 145 */             for (int j = 0; j < i; j++) {
/* 146 */               if (!valueEquals(get(j), localAttribute.get(j)))
/* 147 */                 return false;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 152 */             NamingEnumeration localNamingEnumeration = localAttribute.getAll();
/* 153 */             while (localNamingEnumeration.hasMoreElements())
/* 154 */               if (find(localNamingEnumeration.nextElement()) < 0)
/* 155 */                 return false;
/*     */           }
/*     */         }
/*     */         catch (NamingException localNamingException) {
/* 159 */           return false;
/*     */         }
/* 161 */         return true;
/*     */       }
/*     */     }
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 182 */     int i = this.attrID.hashCode();
/* 183 */     int j = this.values.size();
/*     */ 
/* 185 */     for (int k = 0; k < j; k++) {
/* 186 */       Object localObject1 = this.values.elementAt(k);
/* 187 */       if (localObject1 != null) {
/* 188 */         if (localObject1.getClass().isArray())
/*     */         {
/* 190 */           int m = Array.getLength(localObject1);
/* 191 */           for (int n = 0; n < m; n++) {
/* 192 */             Object localObject2 = Array.get(localObject1, n);
/* 193 */             if (localObject2 != null)
/* 194 */               i += localObject2.hashCode();
/*     */           }
/*     */         }
/*     */         else {
/* 198 */           i += localObject1.hashCode();
/*     */         }
/*     */       }
/*     */     }
/* 202 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 213 */     StringBuffer localStringBuffer = new StringBuffer(this.attrID + ": ");
/*     */     int i;
/*     */     Enumeration localEnumeration;
/* 214 */     if (this.values.size() == 0) {
/* 215 */       localStringBuffer.append("No values");
/*     */     } else {
/* 217 */       i = 1;
/* 218 */       for (localEnumeration = this.values.elements(); localEnumeration.hasMoreElements(); ) {
/* 219 */         if (i == 0)
/* 220 */           localStringBuffer.append(", ");
/* 221 */         localStringBuffer.append(localEnumeration.nextElement());
/* 222 */         i = 0;
/*     */       }
/*     */     }
/* 225 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public BasicAttribute(String paramString)
/*     */   {
/* 234 */     this(paramString, false);
/*     */   }
/*     */ 
/*     */   public BasicAttribute(String paramString, Object paramObject)
/*     */   {
/* 245 */     this(paramString, paramObject, false);
/*     */   }
/*     */ 
/*     */   public BasicAttribute(String paramString, boolean paramBoolean)
/*     */   {
/* 256 */     this.attrID = paramString;
/* 257 */     this.values = new Vector();
/* 258 */     this.ordered = paramBoolean;
/*     */   }
/*     */ 
/*     */   public BasicAttribute(String paramString, Object paramObject, boolean paramBoolean)
/*     */   {
/* 272 */     this(paramString, paramBoolean);
/* 273 */     this.values.addElement(paramObject);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<?> getAll()
/*     */     throws NamingException
/*     */   {
/* 285 */     return new ValuesEnumImpl();
/*     */   }
/*     */ 
/*     */   public Object get()
/*     */     throws NamingException
/*     */   {
/* 297 */     if (this.values.size() == 0) {
/* 298 */       throw new NoSuchElementException("Attribute " + getID() + " has no value");
/*     */     }
/*     */ 
/* 301 */     return this.values.elementAt(0);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 306 */     return this.values.size();
/*     */   }
/*     */ 
/*     */   public String getID() {
/* 310 */     return this.attrID;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 324 */     return find(paramObject) >= 0;
/*     */   }
/*     */ 
/*     */   private int find(Object paramObject)
/*     */   {
/*     */     int i;
/* 331 */     if (paramObject == null) {
/* 332 */       i = this.values.size();
/* 333 */       for (int j = 0; j < i; j++)
/* 334 */         if (this.values.elementAt(j) == null)
/* 335 */           return j;
/*     */     }
/*     */     else
/*     */     {
/*     */       Class localClass;
/* 337 */       if ((localClass = paramObject.getClass()).isArray()) {
/* 338 */         i = this.values.size();
/*     */ 
/* 340 */         for (int k = 0; k < i; k++) {
/* 341 */           Object localObject = this.values.elementAt(k);
/* 342 */           if ((localObject != null) && (localClass == localObject.getClass()) && (arrayEquals(paramObject, localObject)))
/*     */           {
/* 344 */             return k;
/*     */           }
/*     */         }
/*     */       } else { return this.values.indexOf(paramObject, 0); }
/*     */     }
/* 349 */     return -1;
/*     */   }
/*     */ 
/*     */   private static boolean valueEquals(Object paramObject1, Object paramObject2)
/*     */   {
/* 357 */     if (paramObject1 == paramObject2) {
/* 358 */       return true;
/*     */     }
/* 360 */     if (paramObject1 == null) {
/* 361 */       return false;
/*     */     }
/* 363 */     if ((paramObject1.getClass().isArray()) && (paramObject2.getClass().isArray()))
/*     */     {
/* 365 */       return arrayEquals(paramObject1, paramObject2);
/*     */     }
/* 367 */     return paramObject1.equals(paramObject2);
/*     */   }
/*     */ 
/*     */   private static boolean arrayEquals(Object paramObject1, Object paramObject2)
/*     */   {
/*     */     int i;
/* 376 */     if ((i = Array.getLength(paramObject1)) != Array.getLength(paramObject2)) {
/* 377 */       return false;
/*     */     }
/* 379 */     for (int j = 0; j < i; j++) {
/* 380 */       Object localObject1 = Array.get(paramObject1, j);
/* 381 */       Object localObject2 = Array.get(paramObject2, j);
/* 382 */       if ((localObject1 == null) || (localObject2 == null)) {
/* 383 */         if (localObject1 != localObject2)
/* 384 */           return false;
/* 385 */       } else if (!localObject1.equals(localObject2)) {
/* 386 */         return false;
/*     */       }
/*     */     }
/* 389 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean add(Object paramObject)
/*     */   {
/* 402 */     if ((isOrdered()) || (find(paramObject) < 0)) {
/* 403 */       this.values.addElement(paramObject);
/* 404 */       return true;
/*     */     }
/* 406 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 423 */     int i = find(paramObject);
/* 424 */     if (i >= 0) {
/* 425 */       this.values.removeElementAt(i);
/* 426 */       return true;
/*     */     }
/* 428 */     return false;
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 432 */     this.values.setSize(0);
/*     */   }
/*     */ 
/*     */   public boolean isOrdered()
/*     */   {
/* 438 */     return this.ordered;
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt) throws NamingException {
/* 442 */     return this.values.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt) {
/* 446 */     Object localObject = this.values.elementAt(paramInt);
/* 447 */     this.values.removeElementAt(paramInt);
/* 448 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, Object paramObject) {
/* 452 */     if ((!isOrdered()) && (contains(paramObject))) {
/* 453 */       throw new IllegalStateException("Cannot add duplicate to unordered attribute");
/*     */     }
/*     */ 
/* 456 */     this.values.insertElementAt(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public Object set(int paramInt, Object paramObject) {
/* 460 */     if ((!isOrdered()) && (contains(paramObject))) {
/* 461 */       throw new IllegalStateException("Cannot add duplicate to unordered attribute");
/*     */     }
/*     */ 
/* 465 */     Object localObject = this.values.elementAt(paramInt);
/* 466 */     this.values.setElementAt(paramObject, paramInt);
/* 467 */     return localObject;
/*     */   }
/*     */ 
/*     */   public DirContext getAttributeSyntaxDefinition()
/*     */     throws NamingException
/*     */   {
/* 479 */     throw new OperationNotSupportedException("attribute syntax");
/*     */   }
/*     */ 
/*     */   public DirContext getAttributeDefinition()
/*     */     throws NamingException
/*     */   {
/* 489 */     throw new OperationNotSupportedException("attribute definition");
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 503 */     paramObjectOutputStream.defaultWriteObject();
/* 504 */     paramObjectOutputStream.writeInt(this.values.size());
/* 505 */     for (int i = 0; i < this.values.size(); i++)
/* 506 */       paramObjectOutputStream.writeObject(this.values.elementAt(i));
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 515 */     paramObjectInputStream.defaultReadObject();
/* 516 */     int i = paramObjectInputStream.readInt();
/* 517 */     this.values = new Vector(i);
/*     */     while (true) { i--; if (i < 0) break;
/* 519 */       this.values.addElement(paramObjectInputStream.readObject()); }
/*     */   }
/*     */ 
/*     */   class ValuesEnumImpl implements NamingEnumeration<Object>
/*     */   {
/*     */     Enumeration list;
/*     */ 
/*     */     ValuesEnumImpl()
/*     */     {
/* 528 */       this.list = BasicAttribute.this.values.elements();
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements() {
/* 532 */       return this.list.hasMoreElements();
/*     */     }
/*     */ 
/*     */     public Object nextElement() {
/* 536 */       return this.list.nextElement();
/*     */     }
/*     */ 
/*     */     public Object next() throws NamingException {
/* 540 */       return this.list.nextElement();
/*     */     }
/*     */ 
/*     */     public boolean hasMore() throws NamingException {
/* 544 */       return this.list.hasMoreElements();
/*     */     }
/*     */ 
/*     */     public void close() throws NamingException {
/* 548 */       this.list = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.directory.BasicAttribute
 * JD-Core Version:    0.6.2
 */