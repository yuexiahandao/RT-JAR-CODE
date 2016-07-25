/*     */ package javax.naming.directory;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public class BasicAttributes
/*     */   implements Attributes
/*     */ {
/*  75 */   private boolean ignoreCase = false;
/*     */ 
/*  81 */   transient Hashtable attrs = new Hashtable(11);
/*     */   private static final long serialVersionUID = 4980164073184639448L;
/*     */ 
/*     */   public BasicAttributes()
/*     */   {
/*     */   }
/*     */ 
/*     */   public BasicAttributes(boolean paramBoolean)
/*     */   {
/* 101 */     this.ignoreCase = paramBoolean;
/*     */   }
/*     */ 
/*     */   public BasicAttributes(String paramString, Object paramObject)
/*     */   {
/* 115 */     this();
/* 116 */     put(new BasicAttribute(paramString, paramObject));
/*     */   }
/*     */ 
/*     */   public BasicAttributes(String paramString, Object paramObject, boolean paramBoolean)
/*     */   {
/* 137 */     this(paramBoolean);
/* 138 */     put(new BasicAttribute(paramString, paramObject));
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*     */     BasicAttributes localBasicAttributes;
/*     */     try {
/* 144 */       localBasicAttributes = (BasicAttributes)super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 146 */       localBasicAttributes = new BasicAttributes(this.ignoreCase);
/*     */     }
/* 148 */     localBasicAttributes.attrs = ((Hashtable)this.attrs.clone());
/* 149 */     return localBasicAttributes;
/*     */   }
/*     */ 
/*     */   public boolean isCaseIgnored() {
/* 153 */     return this.ignoreCase;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 157 */     return this.attrs.size();
/*     */   }
/*     */ 
/*     */   public Attribute get(String paramString) {
/* 161 */     Attribute localAttribute = (Attribute)this.attrs.get(this.ignoreCase ? paramString.toLowerCase() : paramString);
/*     */ 
/* 163 */     return localAttribute;
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<Attribute> getAll() {
/* 167 */     return new AttrEnumImpl();
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<String> getIDs() {
/* 171 */     return new IDEnumImpl();
/*     */   }
/*     */ 
/*     */   public Attribute put(String paramString, Object paramObject) {
/* 175 */     return put(new BasicAttribute(paramString, paramObject));
/*     */   }
/*     */ 
/*     */   public Attribute put(Attribute paramAttribute) {
/* 179 */     String str = paramAttribute.getID();
/* 180 */     if (this.ignoreCase) {
/* 181 */       str = str.toLowerCase();
/*     */     }
/* 183 */     return (Attribute)this.attrs.put(str, paramAttribute);
/*     */   }
/*     */ 
/*     */   public Attribute remove(String paramString) {
/* 187 */     String str = this.ignoreCase ? paramString.toLowerCase() : paramString;
/* 188 */     return (Attribute)this.attrs.remove(str);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 200 */     if (this.attrs.size() == 0) {
/* 201 */       return "No attributes";
/*     */     }
/* 203 */     return this.attrs.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 226 */     if ((paramObject != null) && ((paramObject instanceof Attributes))) {
/* 227 */       Attributes localAttributes = (Attributes)paramObject;
/*     */ 
/* 230 */       if (this.ignoreCase != localAttributes.isCaseIgnored()) {
/* 231 */         return false;
/*     */       }
/*     */ 
/* 234 */       if (size() == localAttributes.size())
/*     */       {
/*     */         try {
/* 237 */           NamingEnumeration localNamingEnumeration = localAttributes.getAll();
/* 238 */           while (localNamingEnumeration.hasMore()) {
/* 239 */             Attribute localAttribute1 = (Attribute)localNamingEnumeration.next();
/* 240 */             Attribute localAttribute2 = get(localAttribute1.getID());
/* 241 */             if (!localAttribute1.equals(localAttribute2))
/* 242 */               return false;
/*     */           }
/*     */         }
/*     */         catch (NamingException localNamingException) {
/* 246 */           return false;
/*     */         }
/* 248 */         return true;
/*     */       }
/*     */     }
/* 251 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 269 */     int i = this.ignoreCase ? 1 : 0;
/*     */     try {
/* 271 */       NamingEnumeration localNamingEnumeration = getAll();
/* 272 */       while (localNamingEnumeration.hasMore())
/* 273 */         i += localNamingEnumeration.next().hashCode();
/*     */     } catch (NamingException localNamingException) {
/*     */     }
/* 276 */     return i;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 287 */     paramObjectOutputStream.defaultWriteObject();
/* 288 */     paramObjectOutputStream.writeInt(this.attrs.size());
/* 289 */     Enumeration localEnumeration = this.attrs.elements();
/* 290 */     while (localEnumeration.hasMoreElements())
/* 291 */       paramObjectOutputStream.writeObject(localEnumeration.nextElement());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 300 */     paramObjectInputStream.defaultReadObject();
/* 301 */     int i = paramObjectInputStream.readInt();
/* 302 */     this.attrs = (i >= 1 ? new Hashtable(i * 2) : new Hashtable(2));
/*     */     while (true)
/*     */     {
/* 305 */       i--; if (i < 0) break;
/* 306 */       put((Attribute)paramObjectInputStream.readObject());
/*     */     }
/*     */   }
/*     */ 
/*     */   class AttrEnumImpl implements NamingEnumeration<Attribute>
/*     */   {
/*     */     Enumeration<Attribute> elements;
/*     */ 
/*     */     public AttrEnumImpl()
/*     */     {
/* 316 */       this.elements = BasicAttributes.this.attrs.elements();
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements() {
/* 320 */       return this.elements.hasMoreElements();
/*     */     }
/*     */ 
/*     */     public Attribute nextElement() {
/* 324 */       return (Attribute)this.elements.nextElement();
/*     */     }
/*     */ 
/*     */     public boolean hasMore() throws NamingException {
/* 328 */       return hasMoreElements();
/*     */     }
/*     */ 
/*     */     public Attribute next() throws NamingException {
/* 332 */       return nextElement();
/*     */     }
/*     */ 
/*     */     public void close() throws NamingException {
/* 336 */       this.elements = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   class IDEnumImpl
/*     */     implements NamingEnumeration<String>
/*     */   {
/*     */     Enumeration<Attribute> elements;
/*     */ 
/*     */     public IDEnumImpl()
/*     */     {
/* 347 */       this.elements = BasicAttributes.this.attrs.elements();
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements() {
/* 351 */       return this.elements.hasMoreElements();
/*     */     }
/*     */ 
/*     */     public String nextElement() {
/* 355 */       Attribute localAttribute = (Attribute)this.elements.nextElement();
/* 356 */       return localAttribute.getID();
/*     */     }
/*     */ 
/*     */     public boolean hasMore() throws NamingException {
/* 360 */       return hasMoreElements();
/*     */     }
/*     */ 
/*     */     public String next() throws NamingException {
/* 364 */       return nextElement();
/*     */     }
/*     */ 
/*     */     public void close() throws NamingException {
/* 368 */       this.elements = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.directory.BasicAttributes
 * JD-Core Version:    0.6.2
 */