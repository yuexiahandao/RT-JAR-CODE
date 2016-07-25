/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ 
/*     */ public class LdapName
/*     */   implements Name
/*     */ {
/*     */   private transient ArrayList rdns;
/*     */   private transient String unparsed;
/*     */   private static final long serialVersionUID = -1595520034788997356L;
/*     */ 
/*     */   public LdapName(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 124 */     this.unparsed = paramString;
/* 125 */     parse();
/*     */   }
/*     */ 
/*     */   public LdapName(List<Rdn> paramList)
/*     */   {
/* 147 */     this.rdns = new ArrayList(paramList.size());
/* 148 */     for (int i = 0; i < paramList.size(); i++) {
/* 149 */       Object localObject = paramList.get(i);
/* 150 */       if (!(localObject instanceof Rdn)) {
/* 151 */         throw new IllegalArgumentException("Entry:" + localObject + "  not a valid type;list entries must be of type Rdn");
/*     */       }
/*     */ 
/* 154 */       this.rdns.add(localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private LdapName(String paramString, ArrayList paramArrayList, int paramInt1, int paramInt2)
/*     */   {
/* 167 */     this.unparsed = paramString;
/*     */ 
/* 170 */     List localList = paramArrayList.subList(paramInt1, paramInt2);
/* 171 */     this.rdns = new ArrayList(localList);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 179 */     return this.rdns.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 188 */     return this.rdns.isEmpty();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getAll()
/*     */   {
/* 204 */     final Iterator localIterator = this.rdns.iterator();
/*     */ 
/* 206 */     return new Enumeration() {
/*     */       public boolean hasMoreElements() {
/* 208 */         return localIterator.hasNext();
/*     */       }
/*     */       public String nextElement() {
/* 211 */         return localIterator.next().toString();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public String get(int paramInt)
/*     */   {
/* 225 */     return this.rdns.get(paramInt).toString();
/*     */   }
/*     */ 
/*     */   public Rdn getRdn(int paramInt)
/*     */   {
/* 237 */     return (Rdn)this.rdns.get(paramInt);
/*     */   }
/*     */ 
/*     */   public Name getPrefix(int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 255 */       return new LdapName(null, this.rdns, 0, paramInt); } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/* 257 */     throw new IndexOutOfBoundsException("Posn: " + paramInt + ", Size: " + this.rdns.size());
/*     */   }
/*     */ 
/*     */   public Name getSuffix(int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 279 */       return new LdapName(null, this.rdns, paramInt, this.rdns.size()); } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/* 281 */     throw new IndexOutOfBoundsException("Posn: " + paramInt + ", Size: " + this.rdns.size());
/*     */   }
/*     */ 
/*     */   public boolean startsWith(Name paramName)
/*     */   {
/* 300 */     if (paramName == null) {
/* 301 */       return false;
/*     */     }
/* 303 */     int i = this.rdns.size();
/* 304 */     int j = paramName.size();
/* 305 */     return (i >= j) && (matches(0, j, paramName));
/*     */   }
/*     */ 
/*     */   public boolean startsWith(List<Rdn> paramList)
/*     */   {
/* 321 */     if (paramList == null) {
/* 322 */       return false;
/*     */     }
/* 324 */     int i = this.rdns.size();
/* 325 */     int j = paramList.size();
/* 326 */     return (i >= j) && (doesListMatch(0, j, paramList));
/*     */   }
/*     */ 
/*     */   public boolean endsWith(Name paramName)
/*     */   {
/* 343 */     if (paramName == null) {
/* 344 */       return false;
/*     */     }
/* 346 */     int i = this.rdns.size();
/* 347 */     int j = paramName.size();
/* 348 */     return (i >= j) && (matches(i - j, i, paramName));
/*     */   }
/*     */ 
/*     */   public boolean endsWith(List<Rdn> paramList)
/*     */   {
/* 364 */     if (paramList == null) {
/* 365 */       return false;
/*     */     }
/* 367 */     int i = this.rdns.size();
/* 368 */     int j = paramList.size();
/* 369 */     return (i >= j) && (doesListMatch(i - j, i, paramList));
/*     */   }
/*     */ 
/*     */   private boolean doesListMatch(int paramInt1, int paramInt2, List paramList)
/*     */   {
/* 374 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 375 */       if (!this.rdns.get(i).equals(paramList.get(i - paramInt1))) {
/* 376 */         return false;
/*     */       }
/*     */     }
/* 379 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean matches(int paramInt1, int paramInt2, Name paramName)
/*     */   {
/* 390 */     if ((paramName instanceof LdapName)) {
/* 391 */       LdapName localLdapName = (LdapName)paramName;
/* 392 */       return doesListMatch(paramInt1, paramInt2, localLdapName.rdns);
/*     */     }
/* 394 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 396 */       String str = paramName.get(i - paramInt1);
/*     */       Rdn localRdn;
/*     */       try { localRdn = new Rfc2253Parser(str).parseRdn();
/*     */       } catch (InvalidNameException localInvalidNameException) {
/* 400 */         return false;
/*     */       }
/* 402 */       if (!localRdn.equals(this.rdns.get(i))) {
/* 403 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 407 */     return true;
/*     */   }
/*     */ 
/*     */   public Name addAll(Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/* 421 */     return addAll(size(), paramName);
/*     */   }
/*     */ 
/*     */   public Name addAll(List<Rdn> paramList)
/*     */   {
/* 432 */     return addAll(size(), paramList);
/*     */   }
/*     */ 
/*     */   public Name addAll(int paramInt, Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/* 455 */     this.unparsed = null;
/*     */     Object localObject;
/* 456 */     if ((paramName instanceof LdapName)) {
/* 457 */       localObject = (LdapName)paramName;
/* 458 */       this.rdns.addAll(paramInt, ((LdapName)localObject).rdns);
/*     */     } else {
/* 460 */       localObject = paramName.getAll();
/* 461 */       while (((Enumeration)localObject).hasMoreElements()) {
/* 462 */         this.rdns.add(paramInt++, new Rfc2253Parser((String)((Enumeration)localObject).nextElement()).parseRdn());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 467 */     return this;
/*     */   }
/*     */ 
/*     */   public Name addAll(int paramInt, List<Rdn> paramList)
/*     */   {
/* 485 */     this.unparsed = null;
/* 486 */     for (int i = 0; i < paramList.size(); i++) {
/* 487 */       Object localObject = paramList.get(i);
/* 488 */       if (!(localObject instanceof Rdn)) {
/* 489 */         throw new IllegalArgumentException("Entry:" + localObject + "  not a valid type;suffix list entries must be of type Rdn");
/*     */       }
/*     */ 
/* 492 */       this.rdns.add(i + paramInt, localObject);
/*     */     }
/* 494 */     return this;
/*     */   }
/*     */ 
/*     */   public Name add(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 507 */     return add(size(), paramString);
/*     */   }
/*     */ 
/*     */   public Name add(Rdn paramRdn)
/*     */   {
/* 519 */     return add(size(), paramRdn);
/*     */   }
/*     */ 
/*     */   public Name add(int paramInt, String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 540 */     Rdn localRdn = new Rfc2253Parser(paramString).parseRdn();
/* 541 */     this.rdns.add(paramInt, localRdn);
/* 542 */     this.unparsed = null;
/* 543 */     return this;
/*     */   }
/*     */ 
/*     */   public Name add(int paramInt, Rdn paramRdn)
/*     */   {
/* 562 */     if (paramRdn == null) {
/* 563 */       throw new NullPointerException("Cannot set comp to null");
/*     */     }
/* 565 */     this.rdns.add(paramInt, paramRdn);
/* 566 */     this.unparsed = null;
/* 567 */     return this;
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/* 586 */     this.unparsed = null;
/* 587 */     return this.rdns.remove(paramInt).toString();
/*     */   }
/*     */ 
/*     */   public List<Rdn> getRdns()
/*     */   {
/* 601 */     return Collections.unmodifiableList(this.rdns);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 612 */     return new LdapName(this.unparsed, this.rdns, 0, this.rdns.size());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 624 */     if (this.unparsed != null) {
/* 625 */       return this.unparsed;
/*     */     }
/* 627 */     StringBuilder localStringBuilder = new StringBuilder();
/* 628 */     int i = this.rdns.size();
/* 629 */     if (i - 1 >= 0) {
/* 630 */       localStringBuilder.append((Rdn)this.rdns.get(i - 1));
/*     */     }
/* 632 */     for (int j = i - 2; j >= 0; j--) {
/* 633 */       localStringBuilder.append(',');
/* 634 */       localStringBuilder.append((Rdn)this.rdns.get(j));
/*     */     }
/* 636 */     this.unparsed = localStringBuilder.toString();
/* 637 */     return this.unparsed;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 658 */     if (paramObject == this) {
/* 659 */       return true;
/*     */     }
/* 661 */     if (!(paramObject instanceof LdapName)) {
/* 662 */       return false;
/*     */     }
/* 664 */     LdapName localLdapName = (LdapName)paramObject;
/* 665 */     if (this.rdns.size() != localLdapName.rdns.size()) {
/* 666 */       return false;
/*     */     }
/* 668 */     if ((this.unparsed != null) && (this.unparsed.equalsIgnoreCase(localLdapName.unparsed)))
/*     */     {
/* 670 */       return true;
/*     */     }
/*     */ 
/* 673 */     for (int i = 0; i < this.rdns.size(); i++)
/*     */     {
/* 675 */       Rdn localRdn1 = (Rdn)this.rdns.get(i);
/* 676 */       Rdn localRdn2 = (Rdn)localLdapName.rdns.get(i);
/* 677 */       if (!localRdn1.equals(localRdn2)) {
/* 678 */         return false;
/*     */       }
/*     */     }
/* 681 */     return true;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 711 */     if (!(paramObject instanceof LdapName)) {
/* 712 */       throw new ClassCastException("The obj is not a LdapName");
/*     */     }
/*     */ 
/* 716 */     if (paramObject == this) {
/* 717 */       return 0;
/*     */     }
/* 719 */     LdapName localLdapName = (LdapName)paramObject;
/*     */ 
/* 721 */     if ((this.unparsed != null) && (this.unparsed.equalsIgnoreCase(localLdapName.unparsed)))
/*     */     {
/* 723 */       return 0;
/*     */     }
/*     */ 
/* 727 */     int i = Math.min(this.rdns.size(), localLdapName.rdns.size());
/* 728 */     for (int j = 0; j < i; j++)
/*     */     {
/* 730 */       Rdn localRdn1 = (Rdn)this.rdns.get(j);
/* 731 */       Rdn localRdn2 = (Rdn)localLdapName.rdns.get(j);
/*     */ 
/* 733 */       int k = localRdn1.compareTo(localRdn2);
/* 734 */       if (k != 0) {
/* 735 */         return k;
/*     */       }
/*     */     }
/* 738 */     return this.rdns.size() - localLdapName.rdns.size();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 751 */     int i = 0;
/*     */ 
/* 754 */     for (int j = 0; j < this.rdns.size(); j++) {
/* 755 */       Rdn localRdn = (Rdn)this.rdns.get(j);
/* 756 */       i += localRdn.hashCode();
/*     */     }
/* 758 */     return i;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 769 */     paramObjectOutputStream.defaultWriteObject();
/* 770 */     paramObjectOutputStream.writeObject(toString());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 775 */     paramObjectInputStream.defaultReadObject();
/* 776 */     this.unparsed = ((String)paramObjectInputStream.readObject());
/*     */     try {
/* 778 */       parse();
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException) {
/* 781 */       throw new StreamCorruptedException("Invalid name: " + this.unparsed);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parse()
/*     */     throws InvalidNameException
/*     */   {
/* 789 */     this.rdns = ((ArrayList)new Rfc2253Parser(this.unparsed).parseDn());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.LdapName
 * JD-Core Version:    0.6.2
 */