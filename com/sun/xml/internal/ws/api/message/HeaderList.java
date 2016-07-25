/*     */ package com.sun.xml.internal.ws.api.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.addressing.WsaTubeHelper;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.OneWayFeature;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.message.RelatesToHeader;
/*     */ import com.sun.xml.internal.ws.message.StringHeader;
/*     */ import com.sun.xml.internal.ws.resources.AddressingMessages;
/*     */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class HeaderList extends ArrayList<Header>
/*     */ {
/*     */   private static final long serialVersionUID = -6358045781349627237L;
/*     */   private int understoodBits;
/* 133 */   private BitSet moreUnderstoodBits = null;
/* 134 */   private String to = null;
/* 135 */   private String action = null;
/* 136 */   private WSEndpointReference replyTo = null;
/* 137 */   private WSEndpointReference faultTo = null;
/*     */   private String messageId;
/*     */   private String relatesTo;
/*     */ 
/*     */   public HeaderList()
/*     */   {
/*     */   }
/*     */ 
/*     */   public HeaderList(HeaderList that)
/*     */   {
/* 151 */     super(that);
/* 152 */     this.understoodBits = that.understoodBits;
/* 153 */     if (that.moreUnderstoodBits != null) {
/* 154 */       this.moreUnderstoodBits = ((BitSet)that.moreUnderstoodBits.clone());
/*     */     }
/* 156 */     this.to = that.to;
/* 157 */     this.action = that.action;
/* 158 */     this.replyTo = that.replyTo;
/* 159 */     this.faultTo = that.faultTo;
/* 160 */     this.messageId = that.messageId;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 168 */     return super.size();
/*     */   }
/*     */ 
/*     */   public void addAll(Header[] headers)
/*     */   {
/* 175 */     for (Header header : headers)
/* 176 */       add(header);
/*     */   }
/*     */ 
/*     */   public Header get(int index)
/*     */   {
/* 190 */     return (Header)super.get(index);
/*     */   }
/*     */ 
/*     */   public void understood(int index)
/*     */   {
/* 199 */     if (index >= size()) {
/* 200 */       throw new ArrayIndexOutOfBoundsException(index);
/*     */     }
/*     */ 
/* 203 */     if (index < 32) {
/* 204 */       this.understoodBits |= 1 << index;
/*     */     } else {
/* 206 */       if (this.moreUnderstoodBits == null) {
/* 207 */         this.moreUnderstoodBits = new BitSet();
/*     */       }
/* 209 */       this.moreUnderstoodBits.set(index - 32);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isUnderstood(int index)
/*     */   {
/* 219 */     if (index >= size()) {
/* 220 */       throw new ArrayIndexOutOfBoundsException(index);
/*     */     }
/*     */ 
/* 223 */     if (index < 32) {
/* 224 */       return this.understoodBits == (this.understoodBits | 1 << index);
/*     */     }
/* 226 */     if (this.moreUnderstoodBits == null) {
/* 227 */       return false;
/*     */     }
/* 229 */     return this.moreUnderstoodBits.get(index - 32);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void understood(@NotNull Header header)
/*     */   {
/* 248 */     int sz = size();
/* 249 */     for (int i = 0; i < sz; i++) {
/* 250 */       if (get(i) == header) {
/* 251 */         understood(i);
/* 252 */         return;
/*     */       }
/*     */     }
/* 255 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public Header get(@NotNull String nsUri, @NotNull String localName, boolean markAsUnderstood)
/*     */   {
/* 269 */     int len = size();
/* 270 */     for (int i = 0; i < len; i++) {
/* 271 */       Header h = get(i);
/* 272 */       if ((h.getLocalPart().equals(localName)) && (h.getNamespaceURI().equals(nsUri))) {
/* 273 */         if (markAsUnderstood) {
/* 274 */           understood(i);
/*     */         }
/* 276 */         return h;
/*     */       }
/*     */     }
/* 279 */     return null;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Header get(String nsUri, String localName)
/*     */   {
/* 287 */     return get(nsUri, localName, true);
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public Header get(@NotNull QName name, boolean markAsUnderstood)
/*     */   {
/* 302 */     return get(name.getNamespaceURI(), name.getLocalPart(), markAsUnderstood);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   @Nullable
/*     */   public Header get(@NotNull QName name)
/*     */   {
/* 312 */     return get(name, true);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Iterator<Header> getHeaders(String nsUri, String localName)
/*     */   {
/* 320 */     return getHeaders(nsUri, localName, true);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Iterator<Header> getHeaders(@NotNull final String nsUri, @NotNull final String localName, final boolean markAsUnderstood)
/*     */   {
/* 336 */     return new Iterator() {
/* 338 */       int idx = 0;
/*     */       Header next;
/*     */ 
/*     */       public boolean hasNext() {
/* 342 */         if (this.next == null) {
/* 343 */           fetch();
/*     */         }
/* 345 */         return this.next != null;
/*     */       }
/*     */ 
/*     */       public Header next() {
/* 349 */         if (this.next == null) {
/* 350 */           fetch();
/* 351 */           if (this.next == null) {
/* 352 */             throw new NoSuchElementException();
/*     */           }
/*     */         }
/*     */ 
/* 356 */         if (markAsUnderstood) {
/* 357 */           assert (HeaderList.this.get(this.idx - 1) == this.next);
/* 358 */           HeaderList.this.understood(this.idx - 1);
/*     */         }
/*     */ 
/* 361 */         Header r = this.next;
/* 362 */         this.next = null;
/* 363 */         return r;
/*     */       }
/*     */ 
/*     */       private void fetch() {
/* 367 */         while (this.idx < HeaderList.this.size()) {
/* 368 */           Header h = HeaderList.this.get(this.idx++);
/* 369 */           if ((h.getLocalPart().equals(localName)) && (h.getNamespaceURI().equals(nsUri))) {
/* 370 */             this.next = h;
/* 371 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 377 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Iterator<Header> getHeaders(@NotNull QName headerName, boolean markAsUnderstood)
/*     */   {
/* 388 */     return getHeaders(headerName.getNamespaceURI(), headerName.getLocalPart(), markAsUnderstood);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   @NotNull
/*     */   public Iterator<Header> getHeaders(@NotNull String nsUri)
/*     */   {
/* 398 */     return getHeaders(nsUri, true);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Iterator<Header> getHeaders(@NotNull final String nsUri, final boolean markAsUnderstood)
/*     */   {
/* 415 */     return new Iterator() {
/* 417 */       int idx = 0;
/*     */       Header next;
/*     */ 
/*     */       public boolean hasNext() {
/* 421 */         if (this.next == null) {
/* 422 */           fetch();
/*     */         }
/* 424 */         return this.next != null;
/*     */       }
/*     */ 
/*     */       public Header next() {
/* 428 */         if (this.next == null) {
/* 429 */           fetch();
/* 430 */           if (this.next == null) {
/* 431 */             throw new NoSuchElementException();
/*     */           }
/*     */         }
/*     */ 
/* 435 */         if (markAsUnderstood) {
/* 436 */           assert (HeaderList.this.get(this.idx - 1) == this.next);
/* 437 */           HeaderList.this.understood(this.idx - 1);
/*     */         }
/*     */ 
/* 440 */         Header r = this.next;
/* 441 */         this.next = null;
/* 442 */         return r;
/*     */       }
/*     */ 
/*     */       private void fetch() {
/* 446 */         while (this.idx < HeaderList.this.size()) {
/* 447 */           Header h = HeaderList.this.get(this.idx++);
/* 448 */           if (h.getNamespaceURI().equals(nsUri)) {
/* 449 */             this.next = h;
/* 450 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 456 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private Header getFirstHeader(QName name, boolean markUnderstood, SOAPVersion sv)
/*     */   {
/* 473 */     if (sv == null) {
/* 474 */       throw new IllegalArgumentException(AddressingMessages.NULL_SOAP_VERSION());
/*     */     }
/*     */ 
/* 477 */     Iterator iter = getHeaders(name.getNamespaceURI(), name.getLocalPart(), markUnderstood);
/* 478 */     while (iter.hasNext()) {
/* 479 */       Header h = (Header)iter.next();
/* 480 */       if (h.getRole(sv).equals(sv.implicitRole)) {
/* 481 */         return h;
/*     */       }
/*     */     }
/*     */ 
/* 485 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTo(AddressingVersion av, SOAPVersion sv)
/*     */   {
/* 500 */     if (this.to != null) {
/* 501 */       return this.to;
/*     */     }
/* 503 */     if (av == null) {
/* 504 */       throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
/*     */     }
/*     */ 
/* 507 */     Header h = getFirstHeader(av.toTag, true, sv);
/* 508 */     if (h != null)
/* 509 */       this.to = h.getStringContent();
/*     */     else {
/* 511 */       this.to = av.anonymousUri;
/*     */     }
/*     */ 
/* 514 */     return this.to;
/*     */   }
/*     */ 
/*     */   public String getAction(@NotNull AddressingVersion av, @NotNull SOAPVersion sv)
/*     */   {
/* 529 */     if (this.action != null) {
/* 530 */       return this.action;
/*     */     }
/* 532 */     if (av == null) {
/* 533 */       throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
/*     */     }
/*     */ 
/* 536 */     Header h = getFirstHeader(av.actionTag, true, sv);
/* 537 */     if (h != null) {
/* 538 */       this.action = h.getStringContent();
/*     */     }
/*     */ 
/* 541 */     return this.action;
/*     */   }
/*     */ 
/*     */   public WSEndpointReference getReplyTo(@NotNull AddressingVersion av, @NotNull SOAPVersion sv)
/*     */   {
/* 556 */     if (this.replyTo != null) {
/* 557 */       return this.replyTo;
/*     */     }
/* 559 */     if (av == null) {
/* 560 */       throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
/*     */     }
/*     */ 
/* 563 */     Header h = getFirstHeader(av.replyToTag, true, sv);
/* 564 */     if (h != null)
/*     */       try {
/* 566 */         this.replyTo = h.readAsEPR(av);
/*     */       } catch (XMLStreamException e) {
/* 568 */         throw new WebServiceException(AddressingMessages.REPLY_TO_CANNOT_PARSE(), e);
/*     */       }
/*     */     else {
/* 571 */       this.replyTo = av.anonymousEpr;
/*     */     }
/*     */ 
/* 574 */     return this.replyTo;
/*     */   }
/*     */ 
/*     */   public WSEndpointReference getFaultTo(@NotNull AddressingVersion av, @NotNull SOAPVersion sv)
/*     */   {
/* 589 */     if (this.faultTo != null) {
/* 590 */       return this.faultTo;
/*     */     }
/*     */ 
/* 593 */     if (av == null) {
/* 594 */       throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
/*     */     }
/*     */ 
/* 597 */     Header h = getFirstHeader(av.faultToTag, true, sv);
/* 598 */     if (h != null) {
/*     */       try {
/* 600 */         this.faultTo = h.readAsEPR(av);
/*     */       } catch (XMLStreamException e) {
/* 602 */         throw new WebServiceException(AddressingMessages.FAULT_TO_CANNOT_PARSE(), e);
/*     */       }
/*     */     }
/*     */ 
/* 606 */     return this.faultTo;
/*     */   }
/*     */ 
/*     */   public String getMessageID(@NotNull AddressingVersion av, @NotNull SOAPVersion sv)
/*     */   {
/* 621 */     if (this.messageId != null) {
/* 622 */       return this.messageId;
/*     */     }
/*     */ 
/* 625 */     if (av == null) {
/* 626 */       throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
/*     */     }
/*     */ 
/* 629 */     Header h = getFirstHeader(av.messageIDTag, true, sv);
/* 630 */     if (h != null) {
/* 631 */       this.messageId = h.getStringContent();
/*     */     }
/*     */ 
/* 634 */     return this.messageId;
/*     */   }
/*     */ 
/*     */   public String getRelatesTo(@NotNull AddressingVersion av, @NotNull SOAPVersion sv)
/*     */   {
/* 649 */     if (this.relatesTo != null) {
/* 650 */       return this.relatesTo;
/*     */     }
/*     */ 
/* 653 */     if (av == null) {
/* 654 */       throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
/*     */     }
/*     */ 
/* 657 */     Header h = getFirstHeader(av.relatesToTag, true, sv);
/* 658 */     if (h != null) {
/* 659 */       this.relatesTo = h.getStringContent();
/*     */     }
/*     */ 
/* 662 */     return this.relatesTo;
/*     */   }
/*     */ 
/*     */   public void fillRequestAddressingHeaders(Packet packet, AddressingVersion av, SOAPVersion sv, boolean oneway, String action, boolean mustUnderstand)
/*     */   {
/* 683 */     fillCommonAddressingHeaders(packet, av, sv, action, mustUnderstand);
/*     */ 
/* 687 */     if (!oneway) {
/* 688 */       WSEndpointReference epr = av.anonymousEpr;
/* 689 */       add(epr.createHeader(av.replyToTag));
/*     */ 
/* 692 */       Header h = new StringHeader(av.messageIDTag, packet.getMessage().getID(av, sv));
/* 693 */       add(h);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fillRequestAddressingHeaders(Packet packet, AddressingVersion av, SOAPVersion sv, boolean oneway, String action) {
/* 698 */     fillRequestAddressingHeaders(packet, av, sv, oneway, action, false);
/*     */   }
/*     */ 
/*     */   public void fillRequestAddressingHeaders(WSDLPort wsdlPort, @NotNull WSBinding binding, Packet packet)
/*     */   {
/* 721 */     if (binding == null) {
/* 722 */       throw new IllegalArgumentException(AddressingMessages.NULL_BINDING());
/*     */     }
/*     */ 
/* 725 */     HeaderList hl = packet.getMessage().getHeaders();
/* 726 */     String action = hl.getAction(binding.getAddressingVersion(), binding.getSOAPVersion());
/* 727 */     if (action != null)
/*     */     {
/* 729 */       return;
/*     */     }
/* 731 */     AddressingVersion addressingVersion = binding.getAddressingVersion();
/*     */ 
/* 733 */     WsaTubeHelper wsaHelper = addressingVersion.getWsaHelper(wsdlPort, null, binding);
/*     */ 
/* 736 */     String effectiveInputAction = wsaHelper.getEffectiveInputAction(packet);
/* 737 */     if ((effectiveInputAction == null) || (effectiveInputAction.equals(""))) {
/* 738 */       throw new WebServiceException(ClientMessages.INVALID_SOAP_ACTION());
/*     */     }
/* 740 */     boolean oneway = !packet.expectReply.booleanValue();
/* 741 */     if (wsdlPort != null)
/*     */     {
/* 746 */       if ((!oneway) && (packet.getMessage() != null))
/*     */       {
/* 748 */         WSDLBoundOperation wbo = wsdlPort.getBinding().get(packet.getWSDLOperation());
/* 749 */         if ((wbo != null) && (wbo.getAnonymous() == WSDLBoundOperation.ANONYMOUS.prohibited)) {
/* 750 */           throw new WebServiceException(AddressingMessages.WSAW_ANONYMOUS_PROHIBITED());
/*     */         }
/*     */       }
/*     */     }
/* 754 */     if (!binding.isFeatureEnabled(OneWayFeature.class))
/*     */     {
/* 756 */       fillRequestAddressingHeaders(packet, addressingVersion, binding.getSOAPVersion(), oneway, effectiveInputAction, AddressingVersion.isRequired(binding));
/*     */     }
/*     */     else
/* 759 */       fillRequestAddressingHeaders(packet, addressingVersion, binding.getSOAPVersion(), (OneWayFeature)binding.getFeature(OneWayFeature.class), effectiveInputAction);
/*     */   }
/*     */ 
/*     */   private void fillRequestAddressingHeaders(@NotNull Packet packet, @NotNull AddressingVersion av, @NotNull SOAPVersion sv, @NotNull OneWayFeature of, @NotNull String action)
/*     */   {
/* 764 */     fillCommonAddressingHeaders(packet, av, sv, action, false);
/*     */ 
/* 767 */     if (of.getReplyTo() != null) {
/* 768 */       add(of.getReplyTo().createHeader(av.replyToTag));
/*     */ 
/* 771 */       Header h = new StringHeader(av.messageIDTag, packet.getMessage().getID(av, sv));
/* 772 */       add(h);
/*     */     }
/*     */ 
/* 776 */     if (of.getFrom() != null) {
/* 777 */       add(of.getFrom().createHeader(av.fromTag));
/*     */     }
/*     */ 
/* 781 */     if (of.getRelatesToID() != null)
/* 782 */       add(new RelatesToHeader(av.relatesToTag, of.getRelatesToID()));
/*     */   }
/*     */ 
/*     */   private void fillCommonAddressingHeaders(Packet packet, @NotNull AddressingVersion av, @NotNull SOAPVersion sv, @NotNull String action, boolean mustUnderstand)
/*     */   {
/* 796 */     if (packet == null) {
/* 797 */       throw new IllegalArgumentException(AddressingMessages.NULL_PACKET());
/*     */     }
/*     */ 
/* 800 */     if (av == null) {
/* 801 */       throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
/*     */     }
/*     */ 
/* 804 */     if (sv == null) {
/* 805 */       throw new IllegalArgumentException(AddressingMessages.NULL_SOAP_VERSION());
/*     */     }
/*     */ 
/* 808 */     if (action == null) {
/* 809 */       throw new IllegalArgumentException(AddressingMessages.NULL_ACTION());
/*     */     }
/*     */ 
/* 813 */     StringHeader h = new StringHeader(av.toTag, packet.endpointAddress.toString());
/* 814 */     add(h);
/*     */ 
/* 817 */     packet.soapAction = action;
/*     */ 
/* 820 */     h = new StringHeader(av.actionTag, action, sv, mustUnderstand);
/* 821 */     add(h);
/*     */   }
/*     */ 
/*     */   public boolean add(Header header)
/*     */   {
/* 837 */     return super.add(header);
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public Header remove(@NotNull String nsUri, @NotNull String localName)
/*     */   {
/* 851 */     int len = size();
/* 852 */     for (int i = 0; i < len; i++) {
/* 853 */       Header h = get(i);
/* 854 */       if ((h.getLocalPart().equals(localName)) && (h.getNamespaceURI().equals(nsUri))) {
/* 855 */         return remove(i);
/*     */       }
/*     */     }
/* 858 */     return null;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public Header remove(@NotNull QName name)
/*     */   {
/* 871 */     return remove(name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */ 
/*     */   public Header remove(int index)
/*     */   {
/* 883 */     removeUnderstoodBit(index);
/* 884 */     return (Header)super.remove(index);
/*     */   }
/*     */ 
/*     */   private void removeUnderstoodBit(int index)
/*     */   {
/* 894 */     assert (index < size());
/*     */ 
/* 896 */     if (index < 32)
/*     */     {
/* 915 */       int shiftedUpperBits = this.understoodBits >>> -31 + index << index;
/* 916 */       int lowerBits = this.understoodBits << -index >>> 31 - index >>> 1;
/* 917 */       this.understoodBits = (shiftedUpperBits | lowerBits);
/*     */ 
/* 919 */       if ((this.moreUnderstoodBits != null) && (this.moreUnderstoodBits.cardinality() > 0)) {
/* 920 */         if (this.moreUnderstoodBits.get(0)) {
/* 921 */           this.understoodBits |= -2147483648;
/*     */         }
/*     */ 
/* 924 */         this.moreUnderstoodBits.clear(0);
/* 925 */         for (int i = this.moreUnderstoodBits.nextSetBit(1); i > 0; i = this.moreUnderstoodBits.nextSetBit(i + 1)) {
/* 926 */           this.moreUnderstoodBits.set(i - 1);
/* 927 */           this.moreUnderstoodBits.clear(i);
/*     */         }
/*     */       }
/* 930 */     } else if ((this.moreUnderstoodBits != null) && (this.moreUnderstoodBits.cardinality() > 0)) {
/* 931 */       index -= 32;
/* 932 */       this.moreUnderstoodBits.clear(index);
/* 933 */       for (int i = this.moreUnderstoodBits.nextSetBit(index); i >= 1; i = this.moreUnderstoodBits.nextSetBit(i + 1)) {
/* 934 */         this.moreUnderstoodBits.set(i - 1);
/* 935 */         this.moreUnderstoodBits.clear(i);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 940 */     if ((size() - 1 <= 33) && (this.moreUnderstoodBits != null))
/* 941 */       this.moreUnderstoodBits = null;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object o)
/*     */   {
/* 960 */     if (o != null) {
/* 961 */       for (int index = 0; index < size(); index++) {
/* 962 */         if (o.equals(get(index))) {
/* 963 */           remove(index);
/* 964 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 968 */     return false;
/*     */   }
/*     */ 
/*     */   public static HeaderList copy(HeaderList original)
/*     */   {
/* 980 */     if (original == null) {
/* 981 */       return null;
/*     */     }
/* 983 */     return new HeaderList(original);
/*     */   }
/*     */ 
/*     */   public void readResponseAddressingHeaders(WSDLPort wsdlPort, WSBinding binding)
/*     */   {
/* 989 */     String wsaAction = getAction(binding.getAddressingVersion(), binding.getSOAPVersion());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.HeaderList
 * JD-Core Version:    0.6.2
 */