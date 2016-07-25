/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class EnumSyntax
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -2739521845085831642L;
/*     */   private int value;
/*     */ 
/*     */   protected EnumSyntax(int paramInt)
/*     */   {
/* 126 */     this.value = paramInt;
/*     */   }
/*     */ 
/*     */   public int getValue()
/*     */   {
/* 134 */     return this.value;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 151 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 159 */     String[] arrayOfString = getStringTable();
/* 160 */     int i = this.value - getOffset();
/* 161 */     return (arrayOfString != null) && (i >= 0) && (i < arrayOfString.length) ? arrayOfString[i] : Integer.toString(this.value);
/*     */   }
/*     */ 
/*     */   protected Object readResolve()
/*     */     throws ObjectStreamException
/*     */   {
/* 190 */     EnumSyntax[] arrayOfEnumSyntax = getEnumValueTable();
/*     */ 
/* 192 */     if (arrayOfEnumSyntax == null) {
/* 193 */       throw new InvalidObjectException("Null enumeration value table for class " + getClass());
/*     */     }
/*     */ 
/* 198 */     int i = getOffset();
/* 199 */     int j = this.value - i;
/*     */ 
/* 201 */     if ((0 > j) || (j >= arrayOfEnumSyntax.length)) {
/* 202 */       throw new InvalidObjectException("Integer value = " + this.value + " not in valid range " + i + ".." + (i + arrayOfEnumSyntax.length - 1) + "for class " + getClass());
/*     */     }
/*     */ 
/* 208 */     EnumSyntax localEnumSyntax = arrayOfEnumSyntax[j];
/* 209 */     if (localEnumSyntax == null) {
/* 210 */       throw new InvalidObjectException("No enumeration value for integer value = " + this.value + "for class " + getClass());
/*     */     }
/*     */ 
/* 214 */     return localEnumSyntax;
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 239 */     return null;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 265 */     return null;
/*     */   }
/*     */ 
/*     */   protected int getOffset()
/*     */   {
/* 278 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.EnumSyntax
 * JD-Core Version:    0.6.2
 */