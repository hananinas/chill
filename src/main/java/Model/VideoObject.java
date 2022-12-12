package Model;

import java.util.*;
import java.io.File;

public class VideoObject {
    String title;
    String year;
    String ratings;
    File image;
    List<String> categories;

    VideoObject(String title, String year, String ratings, File image) {

        this.title = title;
        this.year = year;
        this.ratings = ratings;
        this.image = image;

        categories = new LinkedList<>();
    }

    public void addCategories(String category) {
        categories.add(category);

    }

    public String getTitle() {
        return title;
    }

    public String getRatings() {
        return ratings;
    }

    public String getYear() {
        return year;
    }

    public String getImage() {
        return image.getPath();
    }

    public List<String> getCategories() {
        return categories;
    }

    public String display() {
        return title + ";" + year + ";" + ratings + ";" + getCategories() + ";" + image;
    }

    
}
