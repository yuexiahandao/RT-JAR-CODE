/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import javax.swing.JFormattedTextField;
/*      */ 
/*      */ public class MaskFormatter extends DefaultFormatter
/*      */ {
/*      */   private static final char DIGIT_KEY = '#';
/*      */   private static final char LITERAL_KEY = '\'';
/*      */   private static final char UPPERCASE_KEY = 'U';
/*      */   private static final char LOWERCASE_KEY = 'L';
/*      */   private static final char ALPHA_NUMERIC_KEY = 'A';
/*      */   private static final char CHARACTER_KEY = '?';
/*      */   private static final char ANYTHING_KEY = '*';
/*      */   private static final char HEX_KEY = 'H';
/*  163 */   private static final MaskCharacter[] EmptyMaskChars = new MaskCharacter[0];
/*      */   private String mask;
/*      */   private transient MaskCharacter[] maskChars;
/*      */   private String validCharacters;
/*      */   private String invalidCharacters;
/*      */   private String placeholderString;
/*      */   private char placeholder;
/*      */   private boolean containsLiteralChars;
/*      */ 
/*      */   public MaskFormatter()
/*      */   {
/*  191 */     setAllowsInvalid(false);
/*  192 */     this.containsLiteralChars = true;
/*  193 */     this.maskChars = EmptyMaskChars;
/*  194 */     this.placeholder = ' ';
/*      */   }
/*      */ 
/*      */   public MaskFormatter(String paramString)
/*      */     throws ParseException
/*      */   {
/*  205 */     this();
/*  206 */     setMask(paramString);
/*      */   }
/*      */ 
/*      */   public void setMask(String paramString)
/*      */     throws ParseException
/*      */   {
/*  217 */     this.mask = paramString;
/*  218 */     updateInternalMask();
/*      */   }
/*      */ 
/*      */   public String getMask()
/*      */   {
/*  227 */     return this.mask;
/*      */   }
/*      */ 
/*      */   public void setValidCharacters(String paramString)
/*      */   {
/*  241 */     this.validCharacters = paramString;
/*      */   }
/*      */ 
/*      */   public String getValidCharacters()
/*      */   {
/*  250 */     return this.validCharacters;
/*      */   }
/*      */ 
/*      */   public void setInvalidCharacters(String paramString)
/*      */   {
/*  264 */     this.invalidCharacters = paramString;
/*      */   }
/*      */ 
/*      */   public String getInvalidCharacters()
/*      */   {
/*  273 */     return this.invalidCharacters;
/*      */   }
/*      */ 
/*      */   public void setPlaceholder(String paramString)
/*      */   {
/*  284 */     this.placeholderString = paramString;
/*      */   }
/*      */ 
/*      */   public String getPlaceholder()
/*      */   {
/*  295 */     return this.placeholderString;
/*      */   }
/*      */ 
/*      */   public void setPlaceholderCharacter(char paramChar)
/*      */   {
/*  310 */     this.placeholder = paramChar;
/*      */   }
/*      */ 
/*      */   public char getPlaceholderCharacter()
/*      */   {
/*  321 */     return this.placeholder;
/*      */   }
/*      */ 
/*      */   public void setValueContainsLiteralCharacters(boolean paramBoolean)
/*      */   {
/*  341 */     this.containsLiteralChars = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getValueContainsLiteralCharacters()
/*      */   {
/*  352 */     return this.containsLiteralChars;
/*      */   }
/*      */ 
/*      */   public Object stringToValue(String paramString)
/*      */     throws ParseException
/*      */   {
/*  371 */     return stringToValue(paramString, true);
/*      */   }
/*      */ 
/*      */   public String valueToString(Object paramObject)
/*      */     throws ParseException
/*      */   {
/*  386 */     String str1 = paramObject == null ? "" : paramObject.toString();
/*  387 */     StringBuilder localStringBuilder = new StringBuilder();
/*  388 */     String str2 = getPlaceholder();
/*  389 */     int[] arrayOfInt = { 0 };
/*      */ 
/*  391 */     append(localStringBuilder, str1, arrayOfInt, str2, this.maskChars);
/*  392 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public void install(JFormattedTextField paramJFormattedTextField)
/*      */   {
/*  426 */     super.install(paramJFormattedTextField);
/*      */ 
/*  429 */     if (paramJFormattedTextField != null) {
/*  430 */       Object localObject = paramJFormattedTextField.getValue();
/*      */       try
/*      */       {
/*  433 */         stringToValue(valueToString(localObject));
/*      */       } catch (ParseException localParseException) {
/*  435 */         setEditValid(false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object stringToValue(String paramString, boolean paramBoolean)
/*      */     throws ParseException
/*      */   {
/*      */     int i;
/*  450 */     if ((i = getInvalidOffset(paramString, paramBoolean)) == -1) {
/*  451 */       if (!getValueContainsLiteralCharacters()) {
/*  452 */         paramString = stripLiteralChars(paramString);
/*      */       }
/*  454 */       return super.stringToValue(paramString);
/*      */     }
/*  456 */     throw new ParseException("stringToValue passed invalid value", i);
/*      */   }
/*      */ 
/*      */   private int getInvalidOffset(String paramString, boolean paramBoolean)
/*      */   {
/*  465 */     int i = paramString.length();
/*      */ 
/*  467 */     if (i != getMaxLength())
/*      */     {
/*  469 */       return i;
/*      */     }
/*  471 */     int j = 0; for (int k = paramString.length(); j < k; j++) {
/*  472 */       char c = paramString.charAt(j);
/*      */ 
/*  474 */       if ((!isValidCharacter(j, c)) && ((paramBoolean) || (!isPlaceholder(j, c))))
/*      */       {
/*  476 */         return j;
/*      */       }
/*      */     }
/*  479 */     return -1;
/*      */   }
/*      */ 
/*      */   private void append(StringBuilder paramStringBuilder, String paramString1, int[] paramArrayOfInt, String paramString2, MaskCharacter[] paramArrayOfMaskCharacter)
/*      */     throws ParseException
/*      */   {
/*  489 */     int i = 0; int j = paramArrayOfMaskCharacter.length;
/*  490 */     for (; i < j; i++)
/*  491 */       paramArrayOfMaskCharacter[i].append(paramStringBuilder, paramString1, paramArrayOfInt, paramString2);
/*      */   }
/*      */ 
/*      */   private void updateInternalMask()
/*      */     throws ParseException
/*      */   {
/*  499 */     String str = getMask();
/*  500 */     ArrayList localArrayList1 = new ArrayList();
/*  501 */     ArrayList localArrayList2 = localArrayList1;
/*      */ 
/*  503 */     if (str != null) {
/*  504 */       int i = 0; int j = str.length();
/*  505 */       for (; i < j; i++) {
/*  506 */         char c = str.charAt(i);
/*      */ 
/*  508 */         switch (c) {
/*      */         case '#':
/*  510 */           localArrayList2.add(new DigitMaskCharacter(null));
/*  511 */           break;
/*      */         case '\'':
/*  513 */           i++; if (i < j) {
/*  514 */             c = str.charAt(i);
/*  515 */             localArrayList2.add(new LiteralCharacter(c)); } break;
/*      */         case 'U':
/*  520 */           localArrayList2.add(new UpperCaseCharacter(null));
/*  521 */           break;
/*      */         case 'L':
/*  523 */           localArrayList2.add(new LowerCaseCharacter(null));
/*  524 */           break;
/*      */         case 'A':
/*  526 */           localArrayList2.add(new AlphaNumericCharacter(null));
/*  527 */           break;
/*      */         case '?':
/*  529 */           localArrayList2.add(new CharCharacter(null));
/*  530 */           break;
/*      */         case '*':
/*  532 */           localArrayList2.add(new MaskCharacter(null));
/*  533 */           break;
/*      */         case 'H':
/*  535 */           localArrayList2.add(new HexCharacter(null));
/*  536 */           break;
/*      */         default:
/*  538 */           localArrayList2.add(new LiteralCharacter(c));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  543 */     if (localArrayList1.size() == 0) {
/*  544 */       this.maskChars = EmptyMaskChars;
/*      */     }
/*      */     else {
/*  547 */       this.maskChars = new MaskCharacter[localArrayList1.size()];
/*  548 */       localArrayList1.toArray(this.maskChars);
/*      */     }
/*      */   }
/*      */ 
/*      */   private MaskCharacter getMaskCharacter(int paramInt)
/*      */   {
/*  556 */     if (paramInt >= this.maskChars.length) {
/*  557 */       return null;
/*      */     }
/*  559 */     return this.maskChars[paramInt];
/*      */   }
/*      */ 
/*      */   private boolean isPlaceholder(int paramInt, char paramChar)
/*      */   {
/*  566 */     return getPlaceholderCharacter() == paramChar;
/*      */   }
/*      */ 
/*      */   private boolean isValidCharacter(int paramInt, char paramChar)
/*      */   {
/*  574 */     return getMaskCharacter(paramInt).isValidCharacter(paramChar);
/*      */   }
/*      */ 
/*      */   private boolean isLiteral(int paramInt)
/*      */   {
/*  582 */     return getMaskCharacter(paramInt).isLiteral();
/*      */   }
/*      */ 
/*      */   private int getMaxLength()
/*      */   {
/*  589 */     return this.maskChars.length;
/*      */   }
/*      */ 
/*      */   private char getLiteral(int paramInt)
/*      */   {
/*  596 */     return getMaskCharacter(paramInt).getChar('\000');
/*      */   }
/*      */ 
/*      */   private char getCharacter(int paramInt, char paramChar)
/*      */   {
/*  606 */     return getMaskCharacter(paramInt).getChar(paramChar);
/*      */   }
/*      */ 
/*      */   private String stripLiteralChars(String paramString)
/*      */   {
/*  613 */     StringBuilder localStringBuilder = null;
/*  614 */     int i = 0;
/*      */ 
/*  616 */     int j = 0; for (int k = paramString.length(); j < k; j++) {
/*  617 */       if (isLiteral(j)) {
/*  618 */         if (localStringBuilder == null) {
/*  619 */           localStringBuilder = new StringBuilder();
/*  620 */           if (j > 0) {
/*  621 */             localStringBuilder.append(paramString.substring(0, j));
/*      */           }
/*  623 */           i = j + 1;
/*      */         }
/*  625 */         else if (i != j) {
/*  626 */           localStringBuilder.append(paramString.substring(i, j));
/*      */         }
/*  628 */         i = j + 1;
/*      */       }
/*      */     }
/*  631 */     if (localStringBuilder == null)
/*      */     {
/*  633 */       return paramString;
/*      */     }
/*  635 */     if (i != paramString.length()) {
/*  636 */       if (localStringBuilder == null) {
/*  637 */         return paramString.substring(i);
/*      */       }
/*  639 */       localStringBuilder.append(paramString.substring(i));
/*      */     }
/*  641 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  651 */     paramObjectInputStream.defaultReadObject();
/*      */     try {
/*  653 */       updateInternalMask();
/*      */     }
/*      */     catch (ParseException localParseException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isNavigatable(int paramInt)
/*      */   {
/*  665 */     if (!getAllowsInvalid()) {
/*  666 */       return (paramInt < getMaxLength()) && (!isLiteral(paramInt));
/*      */     }
/*  668 */     return true;
/*      */   }
/*      */ 
/*      */   boolean isValidEdit(DefaultFormatter.ReplaceHolder paramReplaceHolder)
/*      */   {
/*  679 */     if (!getAllowsInvalid()) {
/*  680 */       String str = getReplaceString(paramReplaceHolder.offset, paramReplaceHolder.length, paramReplaceHolder.text);
/*      */       try
/*      */       {
/*  683 */         paramReplaceHolder.value = stringToValue(str, false);
/*      */ 
/*  685 */         return true;
/*      */       } catch (ParseException localParseException) {
/*  687 */         return false;
/*      */       }
/*      */     }
/*  690 */     return true;
/*      */   }
/*      */ 
/*      */   boolean canReplace(DefaultFormatter.ReplaceHolder paramReplaceHolder)
/*      */   {
/*  719 */     if (!getAllowsInvalid()) {
/*  720 */       StringBuilder localStringBuilder = null;
/*  721 */       String str = paramReplaceHolder.text;
/*  722 */       int i = str != null ? str.length() : 0;
/*      */ 
/*  724 */       if ((i == 0) && (paramReplaceHolder.length == 1) && (getFormattedTextField().getSelectionStart() != paramReplaceHolder.offset))
/*      */       {
/*  727 */         while ((paramReplaceHolder.offset > 0) && (isLiteral(paramReplaceHolder.offset))) {
/*  728 */           paramReplaceHolder.offset -= 1;
/*      */         }
/*      */       }
/*  731 */       int j = Math.min(getMaxLength() - paramReplaceHolder.offset, Math.max(i, paramReplaceHolder.length));
/*      */ 
/*  733 */       int k = 0; for (int m = 0; k < j; k++) {
/*  734 */         if ((m < i) && (isValidCharacter(paramReplaceHolder.offset + k, str.charAt(m))))
/*      */         {
/*  736 */           char c = str.charAt(m);
/*  737 */           if ((c != getCharacter(paramReplaceHolder.offset + k, c)) && 
/*  738 */             (localStringBuilder == null)) {
/*  739 */             localStringBuilder = new StringBuilder();
/*  740 */             if (m > 0) {
/*  741 */               localStringBuilder.append(str.substring(0, m));
/*      */             }
/*      */           }
/*      */ 
/*  745 */           if (localStringBuilder != null) {
/*  746 */             localStringBuilder.append(getCharacter(paramReplaceHolder.offset + k, c));
/*      */           }
/*      */ 
/*  749 */           m++;
/*      */         }
/*  751 */         else if (isLiteral(paramReplaceHolder.offset + k)) {
/*  752 */           if (localStringBuilder != null) {
/*  753 */             localStringBuilder.append(getLiteral(paramReplaceHolder.offset + k));
/*  754 */             if (m < i) {
/*  755 */               j = Math.min(j + 1, getMaxLength() - paramReplaceHolder.offset);
/*      */             }
/*      */ 
/*      */           }
/*  759 */           else if (m > 0) {
/*  760 */             localStringBuilder = new StringBuilder(j);
/*  761 */             localStringBuilder.append(str.substring(0, m));
/*  762 */             localStringBuilder.append(getLiteral(paramReplaceHolder.offset + k));
/*  763 */             if (m < i)
/*      */             {
/*  765 */               j = Math.min(j + 1, getMaxLength() - paramReplaceHolder.offset);
/*      */             }
/*  768 */             else if (paramReplaceHolder.cursorPosition == -1)
/*  769 */               paramReplaceHolder.cursorPosition = (paramReplaceHolder.offset + k);
/*      */           }
/*      */           else
/*      */           {
/*  773 */             paramReplaceHolder.offset += 1;
/*  774 */             paramReplaceHolder.length -= 1;
/*  775 */             k--;
/*  776 */             j--;
/*      */           }
/*      */         }
/*  779 */         else if (m >= i)
/*      */         {
/*  781 */           if (localStringBuilder == null) {
/*  782 */             localStringBuilder = new StringBuilder();
/*  783 */             if (str != null) {
/*  784 */               localStringBuilder.append(str);
/*      */             }
/*      */           }
/*  787 */           localStringBuilder.append(getPlaceholderCharacter());
/*  788 */           if ((i > 0) && (paramReplaceHolder.cursorPosition == -1)) {
/*  789 */             paramReplaceHolder.cursorPosition = (paramReplaceHolder.offset + k);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  794 */           return false;
/*      */         }
/*      */       }
/*  797 */       if (localStringBuilder != null) {
/*  798 */         paramReplaceHolder.text = localStringBuilder.toString();
/*      */       }
/*  800 */       else if ((str != null) && (paramReplaceHolder.offset + i > getMaxLength())) {
/*  801 */         paramReplaceHolder.text = str.substring(0, getMaxLength() - paramReplaceHolder.offset);
/*      */       }
/*  803 */       if ((getOverwriteMode()) && (paramReplaceHolder.text != null)) {
/*  804 */         paramReplaceHolder.length = paramReplaceHolder.text.length();
/*      */       }
/*      */     }
/*  807 */     return super.canReplace(paramReplaceHolder);
/*      */   }
/*      */ 
/*      */   private class AlphaNumericCharacter extends MaskFormatter.MaskCharacter
/*      */   {
/*      */     private AlphaNumericCharacter()
/*      */     {
/*  969 */       super(null);
/*      */     }
/*  971 */     public boolean isValidCharacter(char paramChar) { return (Character.isLetterOrDigit(paramChar)) && (super.isValidCharacter(paramChar)); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private class CharCharacter extends MaskFormatter.MaskCharacter
/*      */   {
/*      */     private CharCharacter()
/*      */     {
/*  980 */       super(null);
/*      */     }
/*  982 */     public boolean isValidCharacter(char paramChar) { return (Character.isLetter(paramChar)) && (super.isValidCharacter(paramChar)); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private class DigitMaskCharacter extends MaskFormatter.MaskCharacter
/*      */   {
/*      */     private DigitMaskCharacter()
/*      */     {
/*  925 */       super(null);
/*      */     }
/*  927 */     public boolean isValidCharacter(char paramChar) { return (Character.isDigit(paramChar)) && (super.isValidCharacter(paramChar)); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private class HexCharacter extends MaskFormatter.MaskCharacter
/*      */   {
/*      */     private HexCharacter()
/*      */     {
/*  991 */       super(null);
/*      */     }
/*  993 */     public boolean isValidCharacter(char paramChar) { return ((paramChar == '0') || (paramChar == '1') || (paramChar == '2') || (paramChar == '3') || (paramChar == '4') || (paramChar == '5') || (paramChar == '6') || (paramChar == '7') || (paramChar == '8') || (paramChar == '9') || (paramChar == 'a') || (paramChar == 'A') || (paramChar == 'b') || (paramChar == 'B') || (paramChar == 'c') || (paramChar == 'C') || (paramChar == 'd') || (paramChar == 'D') || (paramChar == 'e') || (paramChar == 'E') || (paramChar == 'f') || (paramChar == 'F')) && (super.isValidCharacter(paramChar)); }
/*      */ 
/*      */ 
/*      */     public char getChar(char paramChar)
/*      */     {
/* 1008 */       if (Character.isDigit(paramChar)) {
/* 1009 */         return paramChar;
/*      */       }
/* 1011 */       return Character.toUpperCase(paramChar);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class LiteralCharacter extends MaskFormatter.MaskCharacter
/*      */   {
/*      */     private char fixedChar;
/*      */ 
/*      */     public LiteralCharacter(char arg2)
/*      */     {
/*  908 */       super(null);
/*      */       char c;
/*  909 */       this.fixedChar = c;
/*      */     }
/*      */ 
/*      */     public boolean isLiteral() {
/*  913 */       return true;
/*      */     }
/*      */ 
/*      */     public char getChar(char paramChar) {
/*  917 */       return this.fixedChar;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class LowerCaseCharacter extends MaskFormatter.MaskCharacter
/*      */   {
/*      */     private LowerCaseCharacter()
/*      */     {
/*  953 */       super(null);
/*      */     }
/*  955 */     public boolean isValidCharacter(char paramChar) { return (Character.isLetter(paramChar)) && (super.isValidCharacter(paramChar)); }
/*      */ 
/*      */ 
/*      */     public char getChar(char paramChar)
/*      */     {
/*  960 */       return Character.toLowerCase(paramChar);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class MaskCharacter
/*      */   {
/*      */     private MaskCharacter()
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean isLiteral()
/*      */     {
/*  821 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean isValidCharacter(char paramChar)
/*      */     {
/*  833 */       if (isLiteral()) {
/*  834 */         return getChar(paramChar) == paramChar;
/*      */       }
/*      */ 
/*  837 */       paramChar = getChar(paramChar);
/*      */ 
/*  839 */       String str = MaskFormatter.this.getValidCharacters();
/*      */ 
/*  841 */       if ((str != null) && (str.indexOf(paramChar) == -1)) {
/*  842 */         return false;
/*      */       }
/*  844 */       str = MaskFormatter.this.getInvalidCharacters();
/*  845 */       if ((str != null) && (str.indexOf(paramChar) != -1)) {
/*  846 */         return false;
/*      */       }
/*  848 */       return true;
/*      */     }
/*      */ 
/*      */     public char getChar(char paramChar)
/*      */     {
/*  858 */       return paramChar;
/*      */     }
/*      */ 
/*      */     public void append(StringBuilder paramStringBuilder, String paramString1, int[] paramArrayOfInt, String paramString2)
/*      */       throws ParseException
/*      */     {
/*  868 */       int i = paramArrayOfInt[0] < paramString1.length() ? 1 : 0;
/*  869 */       char c = i != 0 ? paramString1.charAt(paramArrayOfInt[0]) : '\000';
/*      */ 
/*  871 */       if (isLiteral()) {
/*  872 */         paramStringBuilder.append(getChar(c));
/*  873 */         if (MaskFormatter.this.getValueContainsLiteralCharacters()) {
/*  874 */           if ((i != 0) && (c != getChar(c))) {
/*  875 */             throw new ParseException("Invalid character: " + c, paramArrayOfInt[0]);
/*      */           }
/*      */ 
/*  878 */           paramArrayOfInt[0] += 1;
/*      */         }
/*      */       }
/*  881 */       else if (paramArrayOfInt[0] >= paramString1.length()) {
/*  882 */         if ((paramString2 != null) && (paramArrayOfInt[0] < paramString2.length())) {
/*  883 */           paramStringBuilder.append(paramString2.charAt(paramArrayOfInt[0]));
/*      */         }
/*      */         else {
/*  886 */           paramStringBuilder.append(MaskFormatter.this.getPlaceholderCharacter());
/*      */         }
/*  888 */         paramArrayOfInt[0] += 1;
/*      */       }
/*  890 */       else if (isValidCharacter(c)) {
/*  891 */         paramStringBuilder.append(getChar(c));
/*  892 */         paramArrayOfInt[0] += 1;
/*      */       }
/*      */       else {
/*  895 */         throw new ParseException("Invalid character: " + c, paramArrayOfInt[0]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class UpperCaseCharacter extends MaskFormatter.MaskCharacter
/*      */   {
/*      */     private UpperCaseCharacter()
/*      */     {
/*  937 */       super(null);
/*      */     }
/*  939 */     public boolean isValidCharacter(char paramChar) { return (Character.isLetter(paramChar)) && (super.isValidCharacter(paramChar)); }
/*      */ 
/*      */ 
/*      */     public char getChar(char paramChar)
/*      */     {
/*  944 */       return Character.toUpperCase(paramChar);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.MaskFormatter
 * JD-Core Version:    0.6.2
 */