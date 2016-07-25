/*    */ package com.sun.beans.editors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ 
/*    */ public class StringEditor extends PropertyEditorSupport
/*    */ {
/*    */   public String getJavaInitializationString()
/*    */   {
/* 34 */     Object localObject = getValue();
/* 35 */     if (localObject == null) {
/* 36 */       return "null";
/*    */     }
/* 38 */     String str1 = localObject.toString();
/* 39 */     int i = str1.length();
/* 40 */     StringBuilder localStringBuilder = new StringBuilder(i + 2);
/* 41 */     localStringBuilder.append('"');
/* 42 */     for (int j = 0; j < i; j++) {
/* 43 */       char c = str1.charAt(j);
/* 44 */       switch (c) { case '\b':
/* 45 */         localStringBuilder.append("\\b"); break;
/*    */       case '\t':
/* 46 */         localStringBuilder.append("\\t"); break;
/*    */       case '\n':
/* 47 */         localStringBuilder.append("\\n"); break;
/*    */       case '\f':
/* 48 */         localStringBuilder.append("\\f"); break;
/*    */       case '\r':
/* 49 */         localStringBuilder.append("\\r"); break;
/*    */       case '"':
/* 50 */         localStringBuilder.append("\\\""); break;
/*    */       case '\\':
/* 51 */         localStringBuilder.append("\\\\"); break;
/*    */       default:
/* 53 */         if ((c < ' ') || (c > '~')) {
/* 54 */           localStringBuilder.append("\\u");
/* 55 */           String str2 = Integer.toHexString(c);
/* 56 */           for (int k = str2.length(); k < 4; k++) {
/* 57 */             localStringBuilder.append('0');
/*    */           }
/* 59 */           localStringBuilder.append(str2);
/*    */         } else {
/* 61 */           localStringBuilder.append(c);
/*    */         }
/*    */         break;
/*    */       }
/*    */     }
/* 66 */     localStringBuilder.append('"');
/* 67 */     return localStringBuilder.toString();
/*    */   }
/*    */ 
/*    */   public void setAsText(String paramString) {
/* 71 */     setValue(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.StringEditor
 * JD-Core Version:    0.6.2
 */