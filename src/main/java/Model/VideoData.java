package Model;
import java.util.*;
import java.io.*;

/**
 Klassen har til formål at hente data og gemme dataene i de forskellige lister
 I vores felt, benytter vi to lister, videoData og images, hvor alt data afhængig af om det er en film eller serie vi beskæftiger os med
 *
 */
public class VideoData implements  DataAccess {
    private List<String> videoData;
    private ArrayList<File> images;

    public VideoData() {
        videoData = new LinkedList<>();
        images = new ArrayList<>();
    }

    // maybe change
    
    public void saveData(String path) {
        try {
           
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
           
            while ((line = reader.readLine()) != null) {
                
                videoData.add(line);
            }
            reader.close();

        } catch (Exception e) {
            System.out.println("file not found: " + e.getMessage());
        }
    }

    public void saveImage(String title){
        File folder = new File(title);
        
        File[] filerne = folder.listFiles();
        
        for(File billede : filerne){
            
            images.add(billede);
        }
       
    
    }
    
    public List<File>getImage(){
        return images; 
    }
    
    public List<String> getData() {
        return videoData; 
    }
    
    
}
