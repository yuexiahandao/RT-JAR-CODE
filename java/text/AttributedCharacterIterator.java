/*     */ package java.text;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract interface AttributedCharacterIterator extends CharacterIterator
/*     */ {
/*     */   public abstract int getRunStart();
/*     */ 
/*     */   public abstract int getRunStart(Attribute paramAttribute);
/*     */ 
/*     */   public abstract int getRunStart(Set<? extends Attribute> paramSet);
/*     */ 
/*     */   public abstract int getRunLimit();
/*     */ 
/*     */   public abstract int getRunLimit(Attribute paramAttribute);
/*     */ 
/*     */   public abstract int getRunLimit(Set<? extends Attribute> paramSet);
/*     */ 
/*     */   public abstract Map<Attribute, Object> getAttributes();
/*     */ 
/*     */   public abstract Object getAttribute(Attribute paramAttribute);
/*     */ 
/*     */   public abstract Set<Attribute> getAllAttributeKeys();
/*     */ 
/*     */   public static class Attribute
/*     */     implements Serializable
/*     */   {
/*     */     private String name;
/* 100 */     private static final Map instanceMap = new HashMap(7);
/*     */ 
/* 166 */     public static final Attribute LANGUAGE = new Attribute("language");
/*     */ 
/* 177 */     public static final Attribute READING = new Attribute("reading");
/*     */ 
/* 185 */     public static final Attribute INPUT_METHOD_SEGMENT = new Attribute("input_method_segment");
/*     */     private static final long serialVersionUID = -9142742483513960612L;
/*     */ 
/*     */     protected Attribute(String paramString)
/*     */     {
/* 106 */       this.name = paramString;
/* 107 */       if (getClass() == Attribute.class)
/* 108 */         instanceMap.put(paramString, this);
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 118 */       return super.equals(paramObject);
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 126 */       return super.hashCode();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 135 */       return getClass().getName() + "(" + this.name + ")";
/*     */     }
/*     */ 
/*     */     protected String getName()
/*     */     {
/* 142 */       return this.name;
/*     */     }
/*     */ 
/*     */     protected Object readResolve()
/*     */       throws InvalidObjectException
/*     */     {
/* 149 */       if (getClass() != Attribute.class) {
/* 150 */         throw new InvalidObjectException("subclass didn't correctly implement readResolve");
/*     */       }
/*     */ 
/* 153 */       Attribute localAttribute = (Attribute)instanceMap.get(getName());
/* 154 */       if (localAttribute != null) {
/* 155 */         return localAttribute;
/*     */       }
/* 157 */       throw new InvalidObjectException("unknown attribute name");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.AttributedCharacterIterator
 * JD-Core Version:    0.6.2
 */