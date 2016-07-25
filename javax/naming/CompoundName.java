/*     */ package javax.naming;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class CompoundName
/*     */   implements Name
/*     */ {
/*     */   protected transient NameImpl impl;
/*     */   protected transient Properties mySyntax;
/*     */   private static final long serialVersionUID = 3513100557083972036L;
/*     */ 
/*     */   protected CompoundName(Enumeration<String> paramEnumeration, Properties paramProperties)
/*     */   {
/* 181 */     if (paramProperties == null) {
/* 182 */       throw new NullPointerException();
/*     */     }
/* 184 */     this.mySyntax = paramProperties;
/* 185 */     this.impl = new NameImpl(paramProperties, paramEnumeration);
/*     */   }
/*     */ 
/*     */   public CompoundName(String paramString, Properties paramProperties)
/*     */     throws InvalidNameException
/*     */   {
/* 200 */     if (paramProperties == null) {
/* 201 */       throw new NullPointerException();
/*     */     }
/* 203 */     this.mySyntax = paramProperties;
/* 204 */     this.impl = new NameImpl(paramProperties, paramString);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 220 */     return this.impl.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 248 */     return (paramObject != null) && ((paramObject instanceof CompoundName)) && (this.impl.equals(((CompoundName)paramObject).impl));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 265 */     return this.impl.hashCode();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 277 */     return new CompoundName(getAll(), this.mySyntax);
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 312 */     if (!(paramObject instanceof CompoundName)) {
/* 313 */       throw new ClassCastException("Not a CompoundName");
/*     */     }
/* 315 */     return this.impl.compareTo(((CompoundName)paramObject).impl);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 324 */     return this.impl.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 334 */     return this.impl.isEmpty();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getAll()
/*     */   {
/* 347 */     return this.impl.getAll();
/*     */   }
/*     */ 
/*     */   public String get(int paramInt)
/*     */   {
/* 360 */     return this.impl.get(paramInt);
/*     */   }
/*     */ 
/*     */   public Name getPrefix(int paramInt)
/*     */   {
/* 379 */     Enumeration localEnumeration = this.impl.getPrefix(paramInt);
/* 380 */     return new CompoundName(localEnumeration, this.mySyntax);
/*     */   }
/*     */ 
/*     */   public Name getSuffix(int paramInt)
/*     */   {
/* 399 */     Enumeration localEnumeration = this.impl.getSuffix(paramInt);
/* 400 */     return new CompoundName(localEnumeration, this.mySyntax);
/*     */   }
/*     */ 
/*     */   public boolean startsWith(Name paramName)
/*     */   {
/* 417 */     if ((paramName instanceof CompoundName)) {
/* 418 */       return this.impl.startsWith(paramName.size(), paramName.getAll());
/*     */     }
/* 420 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean endsWith(Name paramName)
/*     */   {
/* 438 */     if ((paramName instanceof CompoundName)) {
/* 439 */       return this.impl.endsWith(paramName.size(), paramName.getAll());
/*     */     }
/* 441 */     return false;
/*     */   }
/*     */ 
/*     */   public Name addAll(Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/* 458 */     if ((paramName instanceof CompoundName)) {
/* 459 */       this.impl.addAll(paramName.getAll());
/* 460 */       return this;
/*     */     }
/* 462 */     throw new InvalidNameException("Not a compound name: " + paramName.toString());
/*     */   }
/*     */ 
/*     */   public Name addAll(int paramInt, Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/* 488 */     if ((paramName instanceof CompoundName)) {
/* 489 */       this.impl.addAll(paramInt, paramName.getAll());
/* 490 */       return this;
/*     */     }
/* 492 */     throw new InvalidNameException("Not a compound name: " + paramName.toString());
/*     */   }
/*     */ 
/*     */   public Name add(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 506 */     this.impl.add(paramString);
/* 507 */     return this;
/*     */   }
/*     */ 
/*     */   public Name add(int paramInt, String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 527 */     this.impl.add(paramInt, paramString);
/* 528 */     return this;
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/* 547 */     return this.impl.remove(paramInt);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 558 */     paramObjectOutputStream.writeObject(this.mySyntax);
/* 559 */     paramObjectOutputStream.writeInt(size());
/* 560 */     Enumeration localEnumeration = getAll();
/* 561 */     while (localEnumeration.hasMoreElements())
/* 562 */       paramObjectOutputStream.writeObject(localEnumeration.nextElement());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 571 */     this.mySyntax = ((Properties)paramObjectInputStream.readObject());
/* 572 */     this.impl = new NameImpl(this.mySyntax);
/* 573 */     int i = paramObjectInputStream.readInt();
/*     */     try {
/*     */       while (true) { i--; if (i < 0) break;
/* 576 */         add((String)paramObjectInputStream.readObject()); }
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException) {
/* 579 */       throw new StreamCorruptedException("Invalid name");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.CompoundName
 * JD-Core Version:    0.6.2
 */