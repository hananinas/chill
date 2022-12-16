package Model;

import java.util.*;
import java.io.File;

/**
 * SerieObject exetends video and adds extra
 */
public class SerieObject extends VideoObject {

    Map<Integer, Integer> seasonsEpisodes;

    SerieObject(String title, String year, String ratings, File image) {
        super(title, year, ratings, image);

        categories = new LinkedList<>();
        seasonsEpisodes = new HashMap<>();
    }

    public void addSeasonEpisodes(String season, String episode) {
        seasonsEpisodes.put(Integer.parseInt(season.trim()), Integer.parseInt(episode.trim()));
    }

    public Map<Integer, Integer> getSeasonsEpisodes() {
        return seasonsEpisodes;
    }

    public String display() {
        return super.display() + ";" + getSeasonsEpisodes();
    }
}
