/*     */ package javax.naming;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Reference
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   protected String className;
/*  96 */   protected Vector<RefAddr> addrs = null;
/*     */ 
/* 104 */   protected String classFactory = null;
/*     */ 
/* 111 */   protected String classFactoryLocation = null;
/*     */   private static final long serialVersionUID = -1673475790065791735L;
/*     */ 
/*     */   public Reference(String paramString)
/*     */   {
/* 122 */     this.className = paramString;
/* 123 */     this.addrs = new Vector();
/*     */   }
/*     */ 
/*     */   public Reference(String paramString, RefAddr paramRefAddr)
/*     */   {
/* 136 */     this.className = paramString;
/* 137 */     this.addrs = new Vector();
/* 138 */     this.addrs.addElement(paramRefAddr);
/*     */   }
/*     */ 
/*     */   public Reference(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 155 */     this(paramString1);
/* 156 */     this.classFactory = paramString2;
/* 157 */     this.classFactoryLocation = paramString3;
/*     */   }
/*     */ 
/*     */   public Reference(String paramString1, RefAddr paramRefAddr, String paramString2, String paramString3)
/*     */   {
/* 176 */     this(paramString1, paramRefAddr);
/* 177 */     this.classFactory = paramString2;
/* 178 */     this.classFactoryLocation = paramString3;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 188 */     return this.className;
/*     */   }
/*     */ 
/*     */   public String getFactoryClassName()
/*     */   {
/* 199 */     return this.classFactory;
/*     */   }
/*     */ 
/*     */   public String getFactoryClassLocation()
/*     */   {
/* 213 */     return this.classFactoryLocation;
/*     */   }
/*     */ 
/*     */   public RefAddr get(String paramString)
/*     */   {
/* 225 */     int i = this.addrs.size();
/*     */ 
/* 227 */     for (int j = 0; j < i; j++) {
/* 228 */       RefAddr localRefAddr = (RefAddr)this.addrs.elementAt(j);
/* 229 */       if (localRefAddr.getType().compareTo(paramString) == 0)
/* 230 */         return localRefAddr;
/*     */     }
/* 232 */     return null;
/*     */   }
/*     */ 
/*     */   public RefAddr get(int paramInt)
/*     */   {
/* 244 */     return (RefAddr)this.addrs.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public Enumeration<RefAddr> getAll()
/*     */   {
/* 258 */     return this.addrs.elements();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 267 */     return this.addrs.size();
/*     */   }
/*     */ 
/*     */   public void add(RefAddr paramRefAddr)
/*     */   {
/* 276 */     this.addrs.addElement(paramRefAddr);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, RefAddr paramRefAddr)
/*     */   {
/* 290 */     this.addrs.insertElementAt(paramRefAddr, paramInt);
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt)
/*     */   {
/* 304 */     Object localObject = this.addrs.elementAt(paramInt);
/* 305 */     this.addrs.removeElementAt(paramInt);
/* 306 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 313 */     this.addrs.setSize(0);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 329 */     if ((paramObject != null) && ((paramObject instanceof Reference))) {
/* 330 */       Reference localReference = (Reference)paramObject;
/*     */ 
/* 332 */       if ((localReference.className.equals(this.className)) && (localReference.size() == size()))
/*     */       {
/* 334 */         Enumeration localEnumeration1 = getAll();
/* 335 */         Enumeration localEnumeration2 = localReference.getAll();
/* 336 */         while (localEnumeration1.hasMoreElements())
/* 337 */           if (!localEnumeration1.nextElement().equals(localEnumeration2.nextElement()))
/* 338 */             return false;
/* 339 */         return true;
/*     */       }
/*     */     }
/* 342 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 352 */     int i = this.className.hashCode();
/* 353 */     for (Enumeration localEnumeration = getAll(); localEnumeration.hasMoreElements(); )
/* 354 */       i += localEnumeration.nextElement().hashCode();
/* 355 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 367 */     StringBuffer localStringBuffer = new StringBuffer("Reference Class Name: " + this.className + "\n");
/*     */ 
/* 369 */     int i = this.addrs.size();
/* 370 */     for (int j = 0; j < i; j++) {
/* 371 */       localStringBuffer.append(get(j).toString());
/*     */     }
/* 373 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 383 */     Reference localReference = new Reference(this.className, this.classFactory, this.classFactoryLocation);
/* 384 */     Enumeration localEnumeration = getAll();
/* 385 */     localReference.addrs = new Vector();
/*     */ 
/* 387 */     while (localEnumeration.hasMoreElements())
/* 388 */       localReference.addrs.addElement(localEnumeration.nextElement());
/* 389 */     return localReference;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.Reference
 * JD-Core Version:    0.6.2
 */