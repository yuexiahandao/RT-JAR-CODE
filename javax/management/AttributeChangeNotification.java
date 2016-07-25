/*     */ package javax.management;
/*     */ 
/*     */ public class AttributeChangeNotification extends Notification
/*     */ {
/*     */   private static final long serialVersionUID = 535176054565814134L;
/*     */   public static final String ATTRIBUTE_CHANGE = "jmx.attribute.change";
/*  67 */   private String attributeName = null;
/*     */ 
/*  72 */   private String attributeType = null;
/*     */ 
/*  77 */   private Object oldValue = null;
/*     */ 
/*  82 */   private Object newValue = null;
/*     */ 
/*     */   public AttributeChangeNotification(Object paramObject1, long paramLong1, long paramLong2, String paramString1, String paramString2, String paramString3, Object paramObject2, Object paramObject3)
/*     */   {
/* 102 */     super("jmx.attribute.change", paramObject1, paramLong1, paramLong2, paramString1);
/* 103 */     this.attributeName = paramString2;
/* 104 */     this.attributeType = paramString3;
/* 105 */     this.oldValue = paramObject2;
/* 106 */     this.newValue = paramObject3;
/*     */   }
/*     */ 
/*     */   public String getAttributeName()
/*     */   {
/* 116 */     return this.attributeName;
/*     */   }
/*     */ 
/*     */   public String getAttributeType()
/*     */   {
/* 125 */     return this.attributeType;
/*     */   }
/*     */ 
/*     */   public Object getOldValue()
/*     */   {
/* 134 */     return this.oldValue;
/*     */   }
/*     */ 
/*     */   public Object getNewValue()
/*     */   {
/* 143 */     return this.newValue;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.AttributeChangeNotification
 * JD-Core Version:    0.6.2
 */