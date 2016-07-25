/*     */ package javax.naming;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class CompositeName
/*     */   implements Name
/*     */ {
/*     */   private transient NameImpl impl;
/*     */   private static final long serialVersionUID = 1667768148915813118L;
/*     */ 
/*     */   protected CompositeName(Enumeration<String> paramEnumeration)
/*     */   {
/* 218 */     this.impl = new NameImpl(null, paramEnumeration);
/*     */   }
/*     */ 
/*     */   public CompositeName(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 231 */     this.impl = new NameImpl(null, paramString);
/*     */   }
/*     */ 
/*     */   public CompositeName()
/*     */   {
/* 239 */     this.impl = new NameImpl(null);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 258 */     return this.impl.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 275 */     return (paramObject != null) && ((paramObject instanceof CompositeName)) && (this.impl.equals(((CompositeName)paramObject).impl));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 289 */     return this.impl.hashCode();
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 321 */     if (!(paramObject instanceof CompositeName)) {
/* 322 */       throw new ClassCastException("Not a CompositeName");
/*     */     }
/* 324 */     return this.impl.compareTo(((CompositeName)paramObject).impl);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 335 */     return new CompositeName(getAll());
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 344 */     return this.impl.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 354 */     return this.impl.isEmpty();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getAll()
/*     */   {
/* 368 */     return this.impl.getAll();
/*     */   }
/*     */ 
/*     */   public String get(int paramInt)
/*     */   {
/* 381 */     return this.impl.get(paramInt);
/*     */   }
/*     */ 
/*     */   public Name getPrefix(int paramInt)
/*     */   {
/* 397 */     Enumeration localEnumeration = this.impl.getPrefix(paramInt);
/* 398 */     return new CompositeName(localEnumeration);
/*     */   }
/*     */ 
/*     */   public Name getSuffix(int paramInt)
/*     */   {
/* 415 */     Enumeration localEnumeration = this.impl.getSuffix(paramInt);
/* 416 */     return new CompositeName(localEnumeration);
/*     */   }
/*     */ 
/*     */   public boolean startsWith(Name paramName)
/*     */   {
/* 430 */     if ((paramName instanceof CompositeName)) {
/* 431 */       return this.impl.startsWith(paramName.size(), paramName.getAll());
/*     */     }
/* 433 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean endsWith(Name paramName)
/*     */   {
/* 449 */     if ((paramName instanceof CompositeName)) {
/* 450 */       return this.impl.endsWith(paramName.size(), paramName.getAll());
/*     */     }
/* 452 */     return false;
/*     */   }
/*     */ 
/*     */   public Name addAll(Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/* 467 */     if ((paramName instanceof CompositeName)) {
/* 468 */       this.impl.addAll(paramName.getAll());
/* 469 */       return this;
/*     */     }
/* 471 */     throw new InvalidNameException("Not a composite name: " + paramName.toString());
/*     */   }
/*     */ 
/*     */   public Name addAll(int paramInt, Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/* 494 */     if ((paramName instanceof CompositeName)) {
/* 495 */       this.impl.addAll(paramInt, paramName.getAll());
/* 496 */       return this;
/*     */     }
/* 498 */     throw new InvalidNameException("Not a composite name: " + paramName.toString());
/*     */   }
/*     */ 
/*     */   public Name add(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 512 */     this.impl.add(paramString);
/* 513 */     return this;
/*     */   }
/*     */ 
/*     */   public Name add(int paramInt, String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 535 */     this.impl.add(paramInt, paramString);
/* 536 */     return this;
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/* 555 */     return this.impl.remove(paramInt);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 565 */     paramObjectOutputStream.writeInt(size());
/* 566 */     Enumeration localEnumeration = getAll();
/* 567 */     while (localEnumeration.hasMoreElements())
/* 568 */       paramObjectOutputStream.writeObject(localEnumeration.nextElement());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 577 */     this.impl = new NameImpl(null);
/* 578 */     int i = paramObjectInputStream.readInt();
/*     */     try {
/*     */       while (true) { i--; if (i < 0) break;
/* 581 */         add((String)paramObjectInputStream.readObject()); }
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException) {
/* 584 */       throw new StreamCorruptedException("Invalid name");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.CompositeName
 * JD-Core Version:    0.6.2
 */