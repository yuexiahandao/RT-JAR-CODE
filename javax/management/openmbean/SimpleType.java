/*     */ package javax.management.openmbean;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public final class SimpleType<T> extends OpenType<T>
/*     */ {
/*     */   static final long serialVersionUID = 2215577471957694503L;
/*  72 */   public static final SimpleType<Void> VOID = new SimpleType(Void.class);
/*     */ 
/*  79 */   public static final SimpleType<Boolean> BOOLEAN = new SimpleType(Boolean.class);
/*     */ 
/*  86 */   public static final SimpleType<Character> CHARACTER = new SimpleType(Character.class);
/*     */ 
/*  93 */   public static final SimpleType<Byte> BYTE = new SimpleType(Byte.class);
/*     */ 
/* 100 */   public static final SimpleType<Short> SHORT = new SimpleType(Short.class);
/*     */ 
/* 107 */   public static final SimpleType<Integer> INTEGER = new SimpleType(Integer.class);
/*     */ 
/* 114 */   public static final SimpleType<Long> LONG = new SimpleType(Long.class);
/*     */ 
/* 121 */   public static final SimpleType<Float> FLOAT = new SimpleType(Float.class);
/*     */ 
/* 128 */   public static final SimpleType<Double> DOUBLE = new SimpleType(Double.class);
/*     */ 
/* 135 */   public static final SimpleType<String> STRING = new SimpleType(String.class);
/*     */ 
/* 142 */   public static final SimpleType<BigDecimal> BIGDECIMAL = new SimpleType(BigDecimal.class);
/*     */ 
/* 149 */   public static final SimpleType<BigInteger> BIGINTEGER = new SimpleType(BigInteger.class);
/*     */ 
/* 156 */   public static final SimpleType<Date> DATE = new SimpleType(Date.class);
/*     */ 
/* 163 */   public static final SimpleType<ObjectName> OBJECTNAME = new SimpleType(ObjectName.class);
/*     */ 
/* 166 */   private static final SimpleType<?>[] typeArray = { VOID, BOOLEAN, CHARACTER, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, STRING, BIGDECIMAL, BIGINTEGER, DATE, OBJECTNAME };
/*     */ 
/* 172 */   private transient Integer myHashCode = null;
/* 173 */   private transient String myToString = null;
/*     */ 
/* 293 */   private static final Map<SimpleType<?>, SimpleType<?>> canonicalTypes = new HashMap();
/*     */ 
/*     */   private SimpleType(Class<T> paramClass)
/*     */   {
/* 179 */     super(paramClass.getName(), paramClass.getName(), paramClass.getName(), false);
/*     */   }
/*     */ 
/*     */   public boolean isValue(Object paramObject)
/*     */   {
/* 204 */     if (paramObject == null) {
/* 205 */       return false;
/*     */     }
/*     */ 
/* 210 */     return getClassName().equals(paramObject.getClass().getName());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 235 */     if (!(paramObject instanceof SimpleType)) {
/* 236 */       return false;
/*     */     }
/* 238 */     SimpleType localSimpleType = (SimpleType)paramObject;
/*     */ 
/* 242 */     return getClassName().equals(localSimpleType.getClassName());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 259 */     if (this.myHashCode == null) {
/* 260 */       this.myHashCode = Integer.valueOf(getClassName().hashCode());
/*     */     }
/*     */ 
/* 265 */     return this.myHashCode.intValue();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 284 */     if (this.myToString == null) {
/* 285 */       this.myToString = (getClass().getName() + "(name=" + getTypeName() + ")");
/*     */     }
/*     */ 
/* 290 */     return this.myToString;
/*     */   }
/*     */ 
/*     */   public Object readResolve()
/*     */     throws ObjectStreamException
/*     */   {
/* 313 */     SimpleType localSimpleType = (SimpleType)canonicalTypes.get(this);
/* 314 */     if (localSimpleType == null)
/*     */     {
/* 316 */       throw new InvalidObjectException("Invalid SimpleType: " + this);
/*     */     }
/* 318 */     return localSimpleType;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 296 */     for (int i = 0; i < typeArray.length; i++) {
/* 297 */       SimpleType localSimpleType = typeArray[i];
/* 298 */       canonicalTypes.put(localSimpleType, localSimpleType);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.SimpleType
 * JD-Core Version:    0.6.2
 */