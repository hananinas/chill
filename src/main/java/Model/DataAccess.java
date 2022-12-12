package Model;

import java.util.*;
import java.io.File;

public  interface DataAccess{
    
    public void saveData(String path);
    
    public void saveImage(String title);
    
    public List<File>getImage();
    
    public List<String>getData();
    
}
