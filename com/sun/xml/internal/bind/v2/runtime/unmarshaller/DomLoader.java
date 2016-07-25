/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import javax.xml.bind.annotation.DomHandler;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.sax.TransformerHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class DomLoader<ResultT extends Result> extends Loader
/*     */ {
/*     */   private final DomHandler<?, ResultT> dom;
/*     */ 
/*     */   public DomLoader(DomHandler<?, ResultT> dom)
/*     */   {
/*  94 */     super(true);
/*  95 */     this.dom = dom;
/*     */   }
/*     */ 
/*     */   public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */   {
/* 100 */     UnmarshallingContext context = state.getContext();
/* 101 */     if (state.getTarget() == null) {
/* 102 */       state.setTarget(new State(context));
/*     */     }
/* 104 */     State s = (State)state.getTarget();
/*     */     try {
/* 106 */       s.declarePrefixes(context, context.getNewlyDeclaredPrefixes());
/* 107 */       s.handler.startElement(ea.uri, ea.local, ea.getQname(), ea.atts);
/*     */     } catch (SAXException e) {
/* 109 */       context.handleError(e);
/* 110 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */   {
/* 116 */     state.setLoader(this);
/* 117 */     State s = (State)state.getPrev().getTarget();
/* 118 */     s.depth += 1;
/* 119 */     state.setTarget(s);
/*     */   }
/*     */ 
/*     */   public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException
/*     */   {
/* 124 */     if (text.length() == 0)
/* 125 */       return;
/*     */     try {
/* 127 */       State s = (State)state.getTarget();
/* 128 */       s.handler.characters(text.toString().toCharArray(), 0, text.length());
/*     */     } catch (SAXException e) {
/* 130 */       state.getContext().handleError(e);
/* 131 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */   {
/* 137 */     State s = (State)state.getTarget();
/* 138 */     UnmarshallingContext context = state.getContext();
/*     */     try
/*     */     {
/* 141 */       s.handler.endElement(ea.uri, ea.local, ea.getQname());
/* 142 */       s.undeclarePrefixes(context.getNewlyDeclaredPrefixes());
/*     */     } catch (SAXException e) {
/* 144 */       context.handleError(e);
/* 145 */       throw e;
/*     */     }
/*     */ 
/* 148 */     if (--s.depth == 0)
/*     */     {
/*     */       try {
/* 151 */         s.undeclarePrefixes(context.getAllDeclaredPrefixes());
/* 152 */         s.handler.endDocument();
/*     */       } catch (SAXException e) {
/* 154 */         context.handleError(e);
/* 155 */         throw e;
/*     */       }
/*     */ 
/* 159 */       state.setTarget(s.getElement());
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class State
/*     */   {
/*  51 */     private final TransformerHandler handler = JAXBContextImpl.createTransformerHandler();
/*     */     private final ResultT result;
/*  57 */     int depth = 1;
/*     */ 
/*     */     public State(UnmarshallingContext context) throws SAXException {
/*  60 */       this.result = DomLoader.this.dom.createUnmarshaller(context);
/*     */ 
/*  62 */       this.handler.setResult(this.result);
/*     */       try
/*     */       {
/*  66 */         this.handler.setDocumentLocator(context.getLocator());
/*  67 */         this.handler.startDocument();
/*  68 */         declarePrefixes(context, context.getAllDeclaredPrefixes());
/*     */       } catch (SAXException e) {
/*  70 */         context.handleError(e);
/*  71 */         throw e;
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object getElement() {
/*  76 */       return DomLoader.this.dom.getElement(this.result);
/*     */     }
/*     */ 
/*     */     private void declarePrefixes(UnmarshallingContext context, String[] prefixes) throws SAXException {
/*  80 */       for (int i = prefixes.length - 1; i >= 0; i--) {
/*  81 */         String nsUri = context.getNamespaceURI(prefixes[i]);
/*  82 */         if (nsUri == null) throw new IllegalStateException("prefix '" + prefixes[i] + "' isn't bound");
/*  83 */         this.handler.startPrefixMapping(prefixes[i], nsUri);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void undeclarePrefixes(String[] prefixes) throws SAXException {
/*  88 */       for (int i = prefixes.length - 1; i >= 0; i--)
/*  89 */         this.handler.endPrefixMapping(prefixes[i]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.DomLoader
 * JD-Core Version:    0.6.2
 */