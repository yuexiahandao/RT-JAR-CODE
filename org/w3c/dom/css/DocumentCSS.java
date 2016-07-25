package org.w3c.dom.css;

import org.w3c.dom.Element;
import org.w3c.dom.stylesheets.DocumentStyle;

public abstract interface DocumentCSS extends DocumentStyle
{
  public abstract CSSStyleDeclaration getOverrideStyle(Element paramElement, String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.css.DocumentCSS
 * JD-Core Version:    0.6.2
 */