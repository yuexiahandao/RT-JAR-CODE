/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class JAXBElement<T>
/*     */   implements Serializable
/*     */ {
/*     */   protected final QName name;
/*     */   protected final Class<T> declaredType;
/*     */   protected final Class scope;
/*     */   protected T value;
/*  86 */   protected boolean nil = false;
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public JAXBElement(QName name, Class<T> declaredType, Class scope, T value)
/*     */   {
/* 110 */     if ((declaredType == null) || (name == null))
/* 111 */       throw new IllegalArgumentException();
/* 112 */     this.declaredType = declaredType;
/* 113 */     if (scope == null) scope = GlobalScope.class;
/* 114 */     this.scope = scope;
/* 115 */     this.name = name;
/* 116 */     setValue(value);
/*     */   }
/*     */ 
/*     */   public JAXBElement(QName name, Class<T> declaredType, T value)
/*     */   {
/* 125 */     this(name, declaredType, GlobalScope.class, value);
/*     */   }
/*     */ 
/*     */   public Class<T> getDeclaredType()
/*     */   {
/* 132 */     return this.declaredType;
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/* 139 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setValue(T t)
/*     */   {
/* 151 */     this.value = t;
/*     */   }
/*     */ 
/*     */   public T getValue()
/*     */   {
/* 161 */     return this.value;
/*     */   }
/*     */ 
/*     */   public Class getScope()
/*     */   {
/* 171 */     return this.scope;
/*     */   }
/*     */ 
/*     */   public boolean isNil()
/*     */   {
/* 184 */     return (this.value == null) || (this.nil);
/*     */   }
/*     */ 
/*     */   public void setNil(boolean value)
/*     */   {
/* 193 */     this.nil = value;
/*     */   }
/*     */ 
/*     */   public boolean isGlobalScope()
/*     */   {
/* 205 */     return this.scope == GlobalScope.class;
/*     */   }
/*     */ 
/*     */   public boolean isTypeSubstituted()
/*     */   {
/* 213 */     if (this.value == null) return false;
/* 214 */     return this.value.getClass() != this.declaredType;
/*     */   }
/*     */ 
/*     */   public static final class GlobalScope
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.JAXBElement
 * JD-Core Version:    0.6.2
 */