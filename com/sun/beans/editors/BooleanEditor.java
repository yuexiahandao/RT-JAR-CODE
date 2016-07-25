/*    */ package com.sun.beans.editors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ 
/*    */ public class BooleanEditor extends PropertyEditorSupport
/*    */ {
/*    */   public String getJavaInitializationString()
/*    */   {
/* 38 */     Object localObject = getValue();
/* 39 */     return localObject != null ? localObject.toString() : "null";
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 45 */     Object localObject = getValue();
/* 46 */     return (localObject instanceof Boolean) ? getValidName(((Boolean)localObject).booleanValue()) : null;
/*    */   }
/*    */ 
/*    */   public void setAsText(String paramString)
/*    */     throws IllegalArgumentException
/*    */   {
/* 52 */     if (paramString == null)
/* 53 */       setValue(null);
/* 54 */     else if (isValidName(true, paramString))
/* 55 */       setValue(Boolean.TRUE);
/* 56 */     else if (isValidName(false, paramString))
/* 57 */       setValue(Boolean.FALSE);
/*    */     else
/* 59 */       throw new IllegalArgumentException(paramString);
/*    */   }
/*    */ 
/*    */   public String[] getTags()
/*    */   {
/* 64 */     return new String[] { getValidName(true), getValidName(false) };
/*    */   }
/*    */ 
/*    */   private String getValidName(boolean paramBoolean)
/*    */   {
/* 70 */     return paramBoolean ? "True" : "False";
/*    */   }
/*    */ 
/*    */   private boolean isValidName(boolean paramBoolean, String paramString) {
/* 74 */     return getValidName(paramBoolean).equalsIgnoreCase(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.BooleanEditor
 * JD-Core Version:    0.6.2
 */