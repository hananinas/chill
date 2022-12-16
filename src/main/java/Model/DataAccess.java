package Model;

import java.util.*;
import java.io.File;
/**
 * The DataAccess interface defines the signature for methods related to data access.
 */
public  interface DataAccess{
    
    public void saveData(String path);
    
    public void saveImage(String title);
    
    public List<File>getImage();
    
    public List<String>getData();
    
}
