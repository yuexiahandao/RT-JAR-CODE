/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import javax.swing.text.ChangedCharSetException;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ import javax.swing.text.html.HTML.Attribute;
/*     */ import javax.swing.text.html.HTMLEditorKit.ParserCallback;
/*     */ 
/*     */ public class DocumentParser extends Parser
/*     */ {
/*     */   private int inbody;
/*     */   private int intitle;
/*     */   private int inhead;
/*     */   private int instyle;
/*     */   private int inscript;
/*     */   private boolean seentitle;
/* 111 */   private HTMLEditorKit.ParserCallback callback = null;
/* 112 */   private boolean ignoreCharSet = false;
/*     */   private static final boolean debugFlag = false;
/*     */ 
/*     */   public DocumentParser(DTD paramDTD)
/*     */   {
/* 116 */     super(paramDTD);
/*     */   }
/*     */ 
/*     */   public void parse(Reader paramReader, HTMLEditorKit.ParserCallback paramParserCallback, boolean paramBoolean) throws IOException {
/* 120 */     this.ignoreCharSet = paramBoolean;
/* 121 */     this.callback = paramParserCallback;
/* 122 */     parse(paramReader);
/*     */ 
/* 124 */     paramParserCallback.handleEndOfLineString(getEndOfLineString());
/*     */   }
/*     */ 
/*     */   protected void handleStartTag(TagElement paramTagElement)
/*     */   {
/* 132 */     Element localElement = paramTagElement.getElement();
/* 133 */     if (localElement == this.dtd.body)
/* 134 */       this.inbody += 1;
/* 135 */     else if (localElement != this.dtd.html) {
/* 136 */       if (localElement == this.dtd.head)
/* 137 */         this.inhead += 1;
/* 138 */       else if (localElement == this.dtd.title)
/* 139 */         this.intitle += 1;
/* 140 */       else if (localElement == this.dtd.style)
/* 141 */         this.instyle += 1;
/* 142 */       else if (localElement == this.dtd.script) {
/* 143 */         this.inscript += 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 153 */     if (paramTagElement.fictional()) {
/* 154 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 155 */       localSimpleAttributeSet.addAttribute(HTMLEditorKit.ParserCallback.IMPLIED, Boolean.TRUE);
/*     */ 
/* 157 */       this.callback.handleStartTag(paramTagElement.getHTMLTag(), localSimpleAttributeSet, getBlockStartPosition());
/*     */     }
/*     */     else {
/* 160 */       this.callback.handleStartTag(paramTagElement.getHTMLTag(), getAttributes(), getBlockStartPosition());
/*     */ 
/* 162 */       flushAttributes();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleComment(char[] paramArrayOfChar)
/*     */   {
/* 172 */     this.callback.handleComment(paramArrayOfChar, getBlockStartPosition());
/*     */   }
/*     */ 
/*     */   protected void handleEmptyTag(TagElement paramTagElement)
/*     */     throws ChangedCharSetException
/*     */   {
/* 180 */     Element localElement = paramTagElement.getElement();
/*     */     SimpleAttributeSet localSimpleAttributeSet;
/* 181 */     if ((localElement == this.dtd.meta) && (!this.ignoreCharSet)) {
/* 182 */       localSimpleAttributeSet = getAttributes();
/* 183 */       if (localSimpleAttributeSet != null) {
/* 184 */         String str = (String)localSimpleAttributeSet.getAttribute(HTML.Attribute.CONTENT);
/* 185 */         if (str != null) {
/* 186 */           if ("content-type".equalsIgnoreCase((String)localSimpleAttributeSet.getAttribute(HTML.Attribute.HTTPEQUIV))) {
/* 187 */             if ((!str.equalsIgnoreCase("text/html")) && (!str.equalsIgnoreCase("text/plain")))
/*     */             {
/* 189 */               throw new ChangedCharSetException(str, false);
/*     */             }
/* 191 */           } else if ("charset".equalsIgnoreCase((String)localSimpleAttributeSet.getAttribute(HTML.Attribute.HTTPEQUIV))) {
/* 192 */             throw new ChangedCharSetException(str, true);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 197 */     if ((this.inbody != 0) || (localElement == this.dtd.meta) || (localElement == this.dtd.base) || (localElement == this.dtd.isindex) || (localElement == this.dtd.style) || (localElement == this.dtd.link))
/*     */     {
/* 206 */       if (paramTagElement.fictional()) {
/* 207 */         localSimpleAttributeSet = new SimpleAttributeSet();
/* 208 */         localSimpleAttributeSet.addAttribute(HTMLEditorKit.ParserCallback.IMPLIED, Boolean.TRUE);
/*     */ 
/* 210 */         this.callback.handleSimpleTag(paramTagElement.getHTMLTag(), localSimpleAttributeSet, getBlockStartPosition());
/*     */       }
/*     */       else {
/* 213 */         this.callback.handleSimpleTag(paramTagElement.getHTMLTag(), getAttributes(), getBlockStartPosition());
/*     */ 
/* 215 */         flushAttributes();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleEndTag(TagElement paramTagElement)
/*     */   {
/* 224 */     Element localElement = paramTagElement.getElement();
/* 225 */     if (localElement == this.dtd.body) {
/* 226 */       this.inbody -= 1;
/* 227 */     } else if (localElement == this.dtd.title) {
/* 228 */       this.intitle -= 1;
/* 229 */       this.seentitle = true;
/* 230 */     } else if (localElement == this.dtd.head) {
/* 231 */       this.inhead -= 1;
/* 232 */     } else if (localElement == this.dtd.style) {
/* 233 */       this.instyle -= 1;
/* 234 */     } else if (localElement == this.dtd.script) {
/* 235 */       this.inscript -= 1;
/*     */     }
/*     */ 
/* 240 */     this.callback.handleEndTag(paramTagElement.getHTMLTag(), getBlockStartPosition());
/*     */   }
/*     */ 
/*     */   protected void handleText(char[] paramArrayOfChar)
/*     */   {
/* 248 */     if (paramArrayOfChar != null) {
/* 249 */       if (this.inscript != 0) {
/* 250 */         this.callback.handleComment(paramArrayOfChar, getBlockStartPosition());
/* 251 */         return;
/*     */       }
/* 253 */       if ((this.inbody != 0) || (this.instyle != 0) || ((this.intitle != 0) && (!this.seentitle)))
/*     */       {
/* 258 */         this.callback.handleText(paramArrayOfChar, getBlockStartPosition());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleError(int paramInt, String paramString)
/*     */   {
/* 271 */     this.callback.handleError(paramString, getCurrentPos());
/*     */   }
/*     */ 
/*     */   private void debug(String paramString)
/*     */   {
/* 279 */     System.out.println(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.DocumentParser
 * JD-Core Version:    0.6.2
 */