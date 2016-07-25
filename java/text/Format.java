/*     */ package java.text;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class Format
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -299282585814624189L;
/*     */ 
/*     */   public final String format(Object paramObject)
/*     */   {
/* 157 */     return format(paramObject, new StringBuffer(), new FieldPosition(0)).toString();
/*     */   }
/*     */ 
/*     */   public abstract StringBuffer format(Object paramObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*     */ 
/*     */   public AttributedCharacterIterator formatToCharacterIterator(Object paramObject)
/*     */   {
/* 206 */     return createAttributedCharacterIterator(format(paramObject));
/*     */   }
/*     */ 
/*     */   public abstract Object parseObject(String paramString, ParsePosition paramParsePosition);
/*     */ 
/*     */   public Object parseObject(String paramString)
/*     */     throws ParseException
/*     */   {
/* 242 */     ParsePosition localParsePosition = new ParsePosition(0);
/* 243 */     Object localObject = parseObject(paramString, localParsePosition);
/* 244 */     if (localParsePosition.index == 0) {
/* 245 */       throw new ParseException("Format.parseObject(String) failed", localParsePosition.errorIndex);
/*     */     }
/*     */ 
/* 248 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 258 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */   AttributedCharacterIterator createAttributedCharacterIterator(String paramString)
/*     */   {
/* 278 */     AttributedString localAttributedString = new AttributedString(paramString);
/*     */ 
/* 280 */     return localAttributedString.getIterator();
/*     */   }
/*     */ 
/*     */   AttributedCharacterIterator createAttributedCharacterIterator(AttributedCharacterIterator[] paramArrayOfAttributedCharacterIterator)
/*     */   {
/* 295 */     AttributedString localAttributedString = new AttributedString(paramArrayOfAttributedCharacterIterator);
/*     */ 
/* 297 */     return localAttributedString.getIterator();
/*     */   }
/*     */ 
/*     */   AttributedCharacterIterator createAttributedCharacterIterator(String paramString, AttributedCharacterIterator.Attribute paramAttribute, Object paramObject)
/*     */   {
/* 313 */     AttributedString localAttributedString = new AttributedString(paramString);
/*     */ 
/* 315 */     localAttributedString.addAttribute(paramAttribute, paramObject);
/* 316 */     return localAttributedString.getIterator();
/*     */   }
/*     */ 
/*     */   AttributedCharacterIterator createAttributedCharacterIterator(AttributedCharacterIterator paramAttributedCharacterIterator, AttributedCharacterIterator.Attribute paramAttribute, Object paramObject)
/*     */   {
/* 332 */     AttributedString localAttributedString = new AttributedString(paramAttributedCharacterIterator);
/*     */ 
/* 334 */     localAttributedString.addAttribute(paramAttribute, paramObject);
/* 335 */     return localAttributedString.getIterator();
/*     */   }
/*     */ 
/*     */   public static class Field extends AttributedCharacterIterator.Attribute
/*     */   {
/*     */     private static final long serialVersionUID = 276966692217360283L;
/*     */ 
/*     */     protected Field(String paramString)
/*     */     {
/* 358 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract interface FieldDelegate
/*     */   {
/*     */     public abstract void formatted(Format.Field paramField, Object paramObject, int paramInt1, int paramInt2, StringBuffer paramStringBuffer);
/*     */ 
/*     */     public abstract void formatted(int paramInt1, Format.Field paramField, Object paramObject, int paramInt2, int paramInt3, StringBuffer paramStringBuffer);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.Format
 * JD-Core Version:    0.6.2
 */