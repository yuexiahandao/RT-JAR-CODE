/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_6)
/*     */ public class SimpleAnnotationValueVisitor6<R, P> extends AbstractAnnotationValueVisitor6<R, P>
/*     */ {
/*     */   protected final R DEFAULT_VALUE;
/*     */ 
/*     */   protected SimpleAnnotationValueVisitor6()
/*     */   {
/*  93 */     this.DEFAULT_VALUE = null;
/*     */   }
/*     */ 
/*     */   protected SimpleAnnotationValueVisitor6(R paramR)
/*     */   {
/* 104 */     this.DEFAULT_VALUE = paramR;
/*     */   }
/*     */ 
/*     */   protected R defaultAction(Object paramObject, P paramP)
/*     */   {
/* 117 */     return this.DEFAULT_VALUE;
/*     */   }
/*     */ 
/*     */   public R visitBoolean(boolean paramBoolean, P paramP)
/*     */   {
/* 128 */     return defaultAction(Boolean.valueOf(paramBoolean), paramP);
/*     */   }
/*     */ 
/*     */   public R visitByte(byte paramByte, P paramP)
/*     */   {
/* 139 */     return defaultAction(Byte.valueOf(paramByte), paramP);
/*     */   }
/*     */ 
/*     */   public R visitChar(char paramChar, P paramP)
/*     */   {
/* 150 */     return defaultAction(Character.valueOf(paramChar), paramP);
/*     */   }
/*     */ 
/*     */   public R visitDouble(double paramDouble, P paramP)
/*     */   {
/* 161 */     return defaultAction(Double.valueOf(paramDouble), paramP);
/*     */   }
/*     */ 
/*     */   public R visitFloat(float paramFloat, P paramP)
/*     */   {
/* 172 */     return defaultAction(Float.valueOf(paramFloat), paramP);
/*     */   }
/*     */ 
/*     */   public R visitInt(int paramInt, P paramP)
/*     */   {
/* 183 */     return defaultAction(Integer.valueOf(paramInt), paramP);
/*     */   }
/*     */ 
/*     */   public R visitLong(long paramLong, P paramP)
/*     */   {
/* 194 */     return defaultAction(Long.valueOf(paramLong), paramP);
/*     */   }
/*     */ 
/*     */   public R visitShort(short paramShort, P paramP)
/*     */   {
/* 205 */     return defaultAction(Short.valueOf(paramShort), paramP);
/*     */   }
/*     */ 
/*     */   public R visitString(String paramString, P paramP)
/*     */   {
/* 216 */     return defaultAction(paramString, paramP);
/*     */   }
/*     */ 
/*     */   public R visitType(TypeMirror paramTypeMirror, P paramP)
/*     */   {
/* 227 */     return defaultAction(paramTypeMirror, paramP);
/*     */   }
/*     */ 
/*     */   public R visitEnumConstant(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 238 */     return defaultAction(paramVariableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitAnnotation(AnnotationMirror paramAnnotationMirror, P paramP)
/*     */   {
/* 249 */     return defaultAction(paramAnnotationMirror, paramP);
/*     */   }
/*     */ 
/*     */   public R visitArray(List<? extends AnnotationValue> paramList, P paramP)
/*     */   {
/* 260 */     return defaultAction(paramList, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.SimpleAnnotationValueVisitor6
 * JD-Core Version:    0.6.2
 */