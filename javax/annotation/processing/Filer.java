package javax.annotation.processing;

import java.io.IOException;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;

public abstract interface Filer
{
  public abstract JavaFileObject createSourceFile(CharSequence paramCharSequence, Element[] paramArrayOfElement)
    throws IOException;

  public abstract JavaFileObject createClassFile(CharSequence paramCharSequence, Element[] paramArrayOfElement)
    throws IOException;

  public abstract FileObject createResource(JavaFileManager.Location paramLocation, CharSequence paramCharSequence1, CharSequence paramCharSequence2, Element[] paramArrayOfElement)
    throws IOException;

  public abstract FileObject getResource(JavaFileManager.Location paramLocation, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.annotation.processing.Filer
 * JD-Core Version:    0.6.2
 */