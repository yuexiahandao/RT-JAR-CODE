package java.nio.file.attribute;

import java.io.IOException;
import java.util.List;

public abstract interface AclFileAttributeView extends FileOwnerAttributeView
{
  public abstract String name();

  public abstract List<AclEntry> getAcl()
    throws IOException;

  public abstract void setAcl(List<AclEntry> paramList)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.AclFileAttributeView
 * JD-Core Version:    0.6.2
 */