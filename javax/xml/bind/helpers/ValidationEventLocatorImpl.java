/*     */ package javax.xml.bind.helpers;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.text.MessageFormat;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class ValidationEventLocatorImpl
/*     */   implements ValidationEventLocator
/*     */ {
/* 154 */   private URL url = null;
/* 155 */   private int offset = -1;
/* 156 */   private int lineNumber = -1;
/* 157 */   private int columnNumber = -1;
/* 158 */   private Object object = null;
/* 159 */   private Node node = null;
/*     */ 
/*     */   public ValidationEventLocatorImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ValidationEventLocatorImpl(Locator loc)
/*     */   {
/*  73 */     if (loc == null) {
/*  74 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "loc"));
/*     */     }
/*     */ 
/*  78 */     this.url = toURL(loc.getSystemId());
/*  79 */     this.columnNumber = loc.getColumnNumber();
/*  80 */     this.lineNumber = loc.getLineNumber();
/*     */   }
/*     */ 
/*     */   public ValidationEventLocatorImpl(SAXParseException e)
/*     */   {
/*  96 */     if (e == null) {
/*  97 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "e"));
/*     */     }
/*     */ 
/* 101 */     this.url = toURL(e.getSystemId());
/* 102 */     this.columnNumber = e.getColumnNumber();
/* 103 */     this.lineNumber = e.getLineNumber();
/*     */   }
/*     */ 
/*     */   public ValidationEventLocatorImpl(Node _node)
/*     */   {
/* 117 */     if (_node == null) {
/* 118 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "_node"));
/*     */     }
/*     */ 
/* 122 */     this.node = _node;
/*     */   }
/*     */ 
/*     */   public ValidationEventLocatorImpl(Object _object)
/*     */   {
/* 136 */     if (_object == null) {
/* 137 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "_object"));
/*     */     }
/*     */ 
/* 141 */     this.object = _object;
/*     */   }
/*     */ 
/*     */   private static URL toURL(String systemId)
/*     */   {
/*     */     try {
/* 147 */       return new URL(systemId);
/*     */     } catch (MalformedURLException e) {
/*     */     }
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */   public URL getURL()
/*     */   {
/* 166 */     return this.url;
/*     */   }
/*     */ 
/*     */   public void setURL(URL _url)
/*     */   {
/* 175 */     this.url = _url;
/*     */   }
/*     */ 
/*     */   public int getOffset()
/*     */   {
/* 182 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public void setOffset(int _offset)
/*     */   {
/* 191 */     this.offset = _offset;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 198 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */   public void setLineNumber(int _lineNumber)
/*     */   {
/* 207 */     this.lineNumber = _lineNumber;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 214 */     return this.columnNumber;
/*     */   }
/*     */ 
/*     */   public void setColumnNumber(int _columnNumber)
/*     */   {
/* 223 */     this.columnNumber = _columnNumber;
/*     */   }
/*     */ 
/*     */   public Object getObject()
/*     */   {
/* 230 */     return this.object;
/*     */   }
/*     */ 
/*     */   public void setObject(Object _object)
/*     */   {
/* 239 */     this.object = _object;
/*     */   }
/*     */ 
/*     */   public Node getNode()
/*     */   {
/* 246 */     return this.node;
/*     */   }
/*     */ 
/*     */   public void setNode(Node _node)
/*     */   {
/* 255 */     this.node = _node;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 265 */     return MessageFormat.format("[node={0},object={1},url={2},line={3},col={4},offset={5}]", new Object[] { getNode(), getObject(), getURL(), String.valueOf(getLineNumber()), String.valueOf(getColumnNumber()), String.valueOf(getOffset()) });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.helpers.ValidationEventLocatorImpl
 * JD-Core Version:    0.6.2
 */