/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class StylesheetPIHandler extends DefaultHandler
/*     */ {
/*     */   String m_baseID;
/*     */   String m_media;
/*     */   String m_title;
/*     */   String m_charset;
/*  58 */   Vector m_stylesheets = new Vector();
/*     */   URIResolver m_uriResolver;
/*     */ 
/*     */   public void setURIResolver(URIResolver resolver)
/*     */   {
/*  77 */     this.m_uriResolver = resolver;
/*     */   }
/*     */ 
/*     */   public URIResolver getURIResolver()
/*     */   {
/*  88 */     return this.m_uriResolver;
/*     */   }
/*     */ 
/*     */   public StylesheetPIHandler(String baseID, String media, String title, String charset)
/*     */   {
/* 105 */     this.m_baseID = baseID;
/* 106 */     this.m_media = media;
/* 107 */     this.m_title = title;
/* 108 */     this.m_charset = charset;
/*     */   }
/*     */ 
/*     */   public Source getAssociatedStylesheet()
/*     */   {
/* 120 */     int sz = this.m_stylesheets.size();
/*     */ 
/* 122 */     if (sz > 0)
/*     */     {
/* 124 */       Source source = (Source)this.m_stylesheets.elementAt(sz - 1);
/* 125 */       return source;
/*     */     }
/*     */ 
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 146 */     if (target.equals("xml-stylesheet"))
/*     */     {
/* 148 */       String href = null;
/* 149 */       String type = null;
/* 150 */       String title = null;
/* 151 */       String media = null;
/* 152 */       String charset = null;
/* 153 */       boolean alternate = false;
/* 154 */       StringTokenizer tokenizer = new StringTokenizer(data, " \t=\n", true);
/* 155 */       boolean lookedAhead = false;
/* 156 */       Source source = null;
/*     */ 
/* 158 */       String token = "";
/* 159 */       while (tokenizer.hasMoreTokens())
/*     */       {
/* 161 */         if (!lookedAhead)
/* 162 */           token = tokenizer.nextToken();
/*     */         else
/* 164 */           lookedAhead = false;
/* 165 */         if ((!tokenizer.hasMoreTokens()) || ((!token.equals(" ")) && (!token.equals("\t")) && (!token.equals("="))))
/*     */         {
/* 169 */           String name = token;
/* 170 */           if (name.equals("type"))
/*     */           {
/* 172 */             token = tokenizer.nextToken();
/* 173 */             while ((tokenizer.hasMoreTokens()) && ((token.equals(" ")) || (token.equals("\t")) || (token.equals("="))))
/*     */             {
/* 175 */               token = tokenizer.nextToken();
/* 176 */             }type = token.substring(1, token.length() - 1);
/*     */           }
/* 179 */           else if (name.equals("href"))
/*     */           {
/* 181 */             token = tokenizer.nextToken();
/* 182 */             while ((tokenizer.hasMoreTokens()) && ((token.equals(" ")) || (token.equals("\t")) || (token.equals("="))))
/*     */             {
/* 184 */               token = tokenizer.nextToken();
/* 185 */             }href = token;
/* 186 */             if (tokenizer.hasMoreTokens())
/*     */             {
/* 188 */               token = tokenizer.nextToken();
/*     */ 
/* 197 */               while ((token.equals("=")) && (tokenizer.hasMoreTokens()))
/*     */               {
/* 199 */                 href = href + token + tokenizer.nextToken();
/* 200 */                 if (!tokenizer.hasMoreTokens())
/*     */                   break;
/* 202 */                 token = tokenizer.nextToken();
/* 203 */                 lookedAhead = true;
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 211 */             href = href.substring(1, href.length() - 1);
/*     */             try
/*     */             {
/* 215 */               if (this.m_uriResolver != null)
/*     */               {
/* 217 */                 source = this.m_uriResolver.resolve(href, this.m_baseID);
/*     */               }
/*     */               else
/*     */               {
/* 221 */                 href = SystemIDResolver.getAbsoluteURI(href, this.m_baseID);
/* 222 */                 source = new SAXSource(new InputSource(href));
/*     */               }
/*     */             }
/*     */             catch (TransformerException te)
/*     */             {
/* 227 */               throw new SAXException(te);
/*     */             }
/*     */           }
/* 230 */           else if (name.equals("title"))
/*     */           {
/* 232 */             token = tokenizer.nextToken();
/* 233 */             while ((tokenizer.hasMoreTokens()) && ((token.equals(" ")) || (token.equals("\t")) || (token.equals("="))))
/*     */             {
/* 235 */               token = tokenizer.nextToken();
/* 236 */             }title = token.substring(1, token.length() - 1);
/*     */           }
/* 238 */           else if (name.equals("media"))
/*     */           {
/* 240 */             token = tokenizer.nextToken();
/* 241 */             while ((tokenizer.hasMoreTokens()) && ((token.equals(" ")) || (token.equals("\t")) || (token.equals("="))))
/*     */             {
/* 243 */               token = tokenizer.nextToken();
/* 244 */             }media = token.substring(1, token.length() - 1);
/*     */           }
/* 246 */           else if (name.equals("charset"))
/*     */           {
/* 248 */             token = tokenizer.nextToken();
/* 249 */             while ((tokenizer.hasMoreTokens()) && ((token.equals(" ")) || (token.equals("\t")) || (token.equals("="))))
/*     */             {
/* 251 */               token = tokenizer.nextToken();
/* 252 */             }charset = token.substring(1, token.length() - 1);
/*     */           }
/* 254 */           else if (name.equals("alternate"))
/*     */           {
/* 256 */             token = tokenizer.nextToken();
/* 257 */             while ((tokenizer.hasMoreTokens()) && ((token.equals(" ")) || (token.equals("\t")) || (token.equals("="))))
/*     */             {
/* 259 */               token = tokenizer.nextToken();
/* 260 */             }alternate = token.substring(1, token.length() - 1).equals("yes");
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 266 */       if ((null != type) && ((type.equals("text/xsl")) || (type.equals("text/xml")) || (type.equals("application/xml+xslt"))) && (null != href))
/*     */       {
/* 270 */         if (null != this.m_media)
/*     */         {
/* 272 */           if (null != media)
/*     */           {
/* 274 */             if (media.equals(this.m_media));
/*     */           }
/*     */           else
/*     */           {
/* 278 */             return;
/*     */           }
/*     */         }
/* 281 */         if (null != this.m_charset)
/*     */         {
/* 283 */           if (null != charset)
/*     */           {
/* 285 */             if (charset.equals(this.m_charset));
/*     */           }
/*     */           else
/*     */           {
/* 289 */             return;
/*     */           }
/*     */         }
/* 292 */         if (null != this.m_title)
/*     */         {
/* 294 */           if (null != title)
/*     */           {
/* 296 */             if (title.equals(this.m_title));
/*     */           }
/*     */           else
/*     */           {
/* 300 */             return;
/*     */           }
/*     */         }
/* 303 */         this.m_stylesheets.addElement(source);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 326 */     throw new StopParseException();
/*     */   }
/*     */ 
/*     */   public void setBaseId(String baseId)
/*     */   {
/* 335 */     this.m_baseID = baseId;
/*     */   }
/*     */ 
/*     */   public String getBaseId() {
/* 339 */     return this.m_baseID;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.StylesheetPIHandler
 * JD-Core Version:    0.6.2
 */