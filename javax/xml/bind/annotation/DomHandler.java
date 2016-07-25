package javax.xml.bind.annotation;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

public abstract interface DomHandler<ElementT, ResultT extends Result>
{
  public abstract ResultT createUnmarshaller(ValidationEventHandler paramValidationEventHandler);

  public abstract ElementT getElement(ResultT paramResultT);

  public abstract Source marshal(ElementT paramElementT, ValidationEventHandler paramValidationEventHandler);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.annotation.DomHandler
 * JD-Core Version:    0.6.2
 */