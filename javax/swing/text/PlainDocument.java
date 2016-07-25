/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class PlainDocument extends AbstractDocument
/*     */ {
/*     */   public static final String tabSizeAttribute = "tabSize";
/*     */   public static final String lineLimitAttribute = "lineLimit";
/*     */   private AbstractDocument.AbstractElement defaultRoot;
/* 318 */   private Vector<Element> added = new Vector();
/* 319 */   private Vector<Element> removed = new Vector();
/*     */   private transient Segment s;
/*     */ 
/*     */   public PlainDocument()
/*     */   {
/*  80 */     this(new GapContent());
/*     */   }
/*     */ 
/*     */   public PlainDocument(AbstractDocument.Content paramContent)
/*     */   {
/*  90 */     super(paramContent);
/*  91 */     putProperty("tabSize", Integer.valueOf(8));
/*  92 */     this.defaultRoot = createDefaultRoot();
/*     */   }
/*     */ 
/*     */   public void insertString(int paramInt, String paramString, AttributeSet paramAttributeSet)
/*     */     throws BadLocationException
/*     */   {
/* 117 */     Object localObject = getProperty("filterNewlines");
/* 118 */     if (((localObject instanceof Boolean)) && (localObject.equals(Boolean.TRUE)) && 
/* 119 */       (paramString != null) && (paramString.indexOf('\n') >= 0)) {
/* 120 */       StringBuilder localStringBuilder = new StringBuilder(paramString);
/* 121 */       int i = localStringBuilder.length();
/* 122 */       for (int j = 0; j < i; j++) {
/* 123 */         if (localStringBuilder.charAt(j) == '\n') {
/* 124 */           localStringBuilder.setCharAt(j, ' ');
/*     */         }
/*     */       }
/* 127 */       paramString = localStringBuilder.toString();
/*     */     }
/*     */ 
/* 130 */     super.insertString(paramInt, paramString, paramAttributeSet);
/*     */   }
/*     */ 
/*     */   public Element getDefaultRootElement()
/*     */   {
/* 140 */     return this.defaultRoot;
/*     */   }
/*     */ 
/*     */   protected AbstractDocument.AbstractElement createDefaultRoot()
/*     */   {
/* 150 */     AbstractDocument.BranchElement localBranchElement = (AbstractDocument.BranchElement)createBranchElement(null, null);
/* 151 */     Element localElement = createLeafElement(localBranchElement, null, 0, 1);
/* 152 */     Element[] arrayOfElement = new Element[1];
/* 153 */     arrayOfElement[0] = localElement;
/* 154 */     localBranchElement.replace(0, 0, arrayOfElement);
/* 155 */     return localBranchElement;
/*     */   }
/*     */ 
/*     */   public Element getParagraphElement(int paramInt)
/*     */   {
/* 163 */     Element localElement = getDefaultRootElement();
/* 164 */     return localElement.getElement(localElement.getElementIndex(paramInt));
/*     */   }
/*     */ 
/*     */   protected void insertUpdate(AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent, AttributeSet paramAttributeSet)
/*     */   {
/* 176 */     this.removed.removeAllElements();
/* 177 */     this.added.removeAllElements();
/* 178 */     AbstractDocument.BranchElement localBranchElement = (AbstractDocument.BranchElement)getDefaultRootElement();
/* 179 */     int i = paramDefaultDocumentEvent.getOffset();
/* 180 */     int j = paramDefaultDocumentEvent.getLength();
/* 181 */     if (i > 0) {
/* 182 */       i--;
/* 183 */       j++;
/*     */     }
/* 185 */     int k = localBranchElement.getElementIndex(i);
/* 186 */     Element localElement = localBranchElement.getElement(k);
/* 187 */     int m = localElement.getStartOffset();
/* 188 */     int n = localElement.getEndOffset();
/* 189 */     int i1 = m;
/*     */     try {
/* 191 */       if (this.s == null) {
/* 192 */         this.s = new Segment();
/*     */       }
/* 194 */       getContent().getChars(i, j, this.s);
/* 195 */       int i2 = 0;
/* 196 */       for (int i3 = 0; i3 < j; i3++) {
/* 197 */         int i4 = this.s.array[(this.s.offset + i3)];
/* 198 */         if (i4 == 10) {
/* 199 */           int i5 = i + i3 + 1;
/* 200 */           this.added.addElement(createLeafElement(localBranchElement, null, i1, i5));
/* 201 */           i1 = i5;
/* 202 */           i2 = 1;
/*     */         }
/*     */       }
/* 205 */       if (i2 != 0) {
/* 206 */         this.removed.addElement(localElement);
/* 207 */         if ((i + j == n) && (i1 != n) && (k + 1 < localBranchElement.getElementCount()))
/*     */         {
/* 209 */           localObject = localBranchElement.getElement(k + 1);
/* 210 */           this.removed.addElement(localObject);
/* 211 */           n = ((Element)localObject).getEndOffset();
/*     */         }
/* 213 */         if (i1 < n) {
/* 214 */           this.added.addElement(createLeafElement(localBranchElement, null, i1, n));
/*     */         }
/*     */ 
/* 217 */         Object localObject = new Element[this.added.size()];
/* 218 */         this.added.copyInto((Object[])localObject);
/* 219 */         Element[] arrayOfElement = new Element[this.removed.size()];
/* 220 */         this.removed.copyInto(arrayOfElement);
/* 221 */         AbstractDocument.ElementEdit localElementEdit = new AbstractDocument.ElementEdit(localBranchElement, k, arrayOfElement, (Element[])localObject);
/* 222 */         paramDefaultDocumentEvent.addEdit(localElementEdit);
/* 223 */         localBranchElement.replace(k, arrayOfElement.length, (Element[])localObject);
/*     */       }
/* 225 */       if (Utilities.isComposedTextAttributeDefined(paramAttributeSet))
/* 226 */         insertComposedTextUpdate(paramDefaultDocumentEvent, paramAttributeSet);
/*     */     }
/*     */     catch (BadLocationException localBadLocationException) {
/* 229 */       throw new Error("Internal error: " + localBadLocationException.toString());
/*     */     }
/* 231 */     super.insertUpdate(paramDefaultDocumentEvent, paramAttributeSet);
/*     */   }
/*     */ 
/*     */   protected void removeUpdate(AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent)
/*     */   {
/* 244 */     this.removed.removeAllElements();
/* 245 */     AbstractDocument.BranchElement localBranchElement = (AbstractDocument.BranchElement)getDefaultRootElement();
/* 246 */     int i = paramDefaultDocumentEvent.getOffset();
/* 247 */     int j = paramDefaultDocumentEvent.getLength();
/* 248 */     int k = localBranchElement.getElementIndex(i);
/* 249 */     int m = localBranchElement.getElementIndex(i + j);
/*     */     Element[] arrayOfElement1;
/*     */     Element[] arrayOfElement2;
/*     */     AbstractDocument.ElementEdit localElementEdit;
/* 250 */     if (k != m)
/*     */     {
/* 252 */       for (int n = k; n <= m; n++) {
/* 253 */         this.removed.addElement(localBranchElement.getElement(n));
/*     */       }
/* 255 */       n = localBranchElement.getElement(k).getStartOffset();
/* 256 */       int i1 = localBranchElement.getElement(m).getEndOffset();
/* 257 */       arrayOfElement1 = new Element[1];
/* 258 */       arrayOfElement1[0] = createLeafElement(localBranchElement, null, n, i1);
/* 259 */       arrayOfElement2 = new Element[this.removed.size()];
/* 260 */       this.removed.copyInto(arrayOfElement2);
/* 261 */       localElementEdit = new AbstractDocument.ElementEdit(localBranchElement, k, arrayOfElement2, arrayOfElement1);
/* 262 */       paramDefaultDocumentEvent.addEdit(localElementEdit);
/* 263 */       localBranchElement.replace(k, arrayOfElement2.length, arrayOfElement1);
/*     */     }
/*     */     else {
/* 266 */       Element localElement1 = localBranchElement.getElement(k);
/* 267 */       if (!localElement1.isLeaf()) {
/* 268 */         Element localElement2 = localElement1.getElement(localElement1.getElementIndex(i));
/* 269 */         if (Utilities.isComposedTextElement(localElement2)) {
/* 270 */           arrayOfElement1 = new Element[1];
/* 271 */           arrayOfElement1[0] = createLeafElement(localBranchElement, null, localElement1.getStartOffset(), localElement1.getEndOffset());
/*     */ 
/* 273 */           arrayOfElement2 = new Element[1];
/* 274 */           arrayOfElement2[0] = localElement1;
/* 275 */           localElementEdit = new AbstractDocument.ElementEdit(localBranchElement, k, arrayOfElement2, arrayOfElement1);
/* 276 */           paramDefaultDocumentEvent.addEdit(localElementEdit);
/* 277 */           localBranchElement.replace(k, 1, arrayOfElement1);
/*     */         }
/*     */       }
/*     */     }
/* 281 */     super.removeUpdate(paramDefaultDocumentEvent);
/*     */   }
/*     */ 
/*     */   private void insertComposedTextUpdate(AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent, AttributeSet paramAttributeSet)
/*     */   {
/* 291 */     this.added.removeAllElements();
/* 292 */     AbstractDocument.BranchElement localBranchElement = (AbstractDocument.BranchElement)getDefaultRootElement();
/* 293 */     int i = paramDefaultDocumentEvent.getOffset();
/* 294 */     int j = paramDefaultDocumentEvent.getLength();
/* 295 */     int k = localBranchElement.getElementIndex(i);
/* 296 */     Element localElement = localBranchElement.getElement(k);
/* 297 */     int m = localElement.getStartOffset();
/* 298 */     int n = localElement.getEndOffset();
/* 299 */     AbstractDocument.BranchElement[] arrayOfBranchElement = new AbstractDocument.BranchElement[1];
/* 300 */     arrayOfBranchElement[0] = ((AbstractDocument.BranchElement)createBranchElement(localBranchElement, null));
/* 301 */     Element[] arrayOfElement1 = new Element[1];
/* 302 */     arrayOfElement1[0] = localElement;
/* 303 */     if (m != i)
/* 304 */       this.added.addElement(createLeafElement(arrayOfBranchElement[0], null, m, i));
/* 305 */     this.added.addElement(createLeafElement(arrayOfBranchElement[0], paramAttributeSet, i, i + j));
/* 306 */     if (n != i + j)
/* 307 */       this.added.addElement(createLeafElement(arrayOfBranchElement[0], null, i + j, n));
/* 308 */     Element[] arrayOfElement2 = new Element[this.added.size()];
/* 309 */     this.added.copyInto(arrayOfElement2);
/* 310 */     AbstractDocument.ElementEdit localElementEdit = new AbstractDocument.ElementEdit(localBranchElement, k, arrayOfElement1, arrayOfBranchElement);
/* 311 */     paramDefaultDocumentEvent.addEdit(localElementEdit);
/*     */ 
/* 313 */     arrayOfBranchElement[0].replace(0, 0, arrayOfElement2);
/* 314 */     localBranchElement.replace(k, 1, arrayOfBranchElement);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.PlainDocument
 * JD-Core Version:    0.6.2
 */