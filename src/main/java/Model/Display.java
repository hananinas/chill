package Model;

import java.util.*;
import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 *The Display class is responsible for handling the display of video data, such as films and TV series. It uses a {@link DataAccess}
 *object to load and save the data, and stores the data in lists of {@link VideoObject}s for films and {@link SerieObject}s for TV series.
 *The class also includes methods for retrieving and displaying the data based on various criteria, such as categories and favorites.
 */
public class Display {

    private String path;

    private List<String> videoList;
    private List<File> images;

    private List<VideoObject> allFilms;
    private List<VideoObject> allSeries;

    private Map<String, List<String>> categories;
    private Set<String> theCategories;

    private Set<VideoObject> favoriteList;


    private SerieObject serie;
    private VideoObject film;

    public Display(DataAccess video, String path, String imagePath) {
        this.path = path;

        video.saveData(path);
        video.saveImage(imagePath);

        images = video.getImage();
        videoList = video.getData();

        allFilms = new LinkedList<>();
        allSeries = new LinkedList();

        favoriteList = new HashSet<>();

        categories = new HashMap<>();
        theCategories = new HashSet<>();
    }

    public void videoData() {

        for (String enFilm : videoList) {

            String[] item = enFilm.split(";");
            String title = item[0].replace("�","ä");

            String year = item[1];

            String ratings = item[3];
            File theimage = null;
            for (File image : images) {
                if (image.getName().contains(title)) {
                    theimage = image;
                }
            }

            film = new VideoObject(title, year, ratings, theimage);

            for (String eachgenre : item[2].split(",")) {
                film.addCategories(eachgenre.trim());
                theCategories.add(eachgenre.trim());

            }

            if (path.contains("film")) {
                allFilms.add(film);
            } else {
                serie = new SerieObject(title, year, ratings, theimage);
                for (String eachCategory : item[2].split(",")) {
                    serie.addCategories(eachCategory.trim());
                }

                String SeasonsEpisodes = item[4];
                String[] seasonsplit = SeasonsEpisodes.split(",");

                for (String eachSeason : seasonsplit) {
                    String[] temp_seasons = eachSeason.split("-");
                    String seasons = temp_seasons[0];
                    String episodes = temp_seasons[1];
                    serie.addSeasonEpisodes(seasons, episodes);
                }
                allSeries.add(serie);

            }

        }

    }

    public Set<String> getAllCategories() {
        return theCategories;
    }

    public List<VideoObject> getAll() {


        List<VideoObject> indhold;
        if (path.contains("film")) {
            indhold = allFilms;
        } else {
            indhold = allSeries;
        }

        for (VideoObject video : indhold) {
            System.out.println(video.display());
        }
        return indhold;
    }


    public Map<String, List<String>> getCategorie() {

        List<String> category = new LinkedList<>(theCategories);
        Collections.sort(category);
        List<VideoObject> indhold;

        if (path.contains("film")) {
            indhold = allFilms;
        } else {
            indhold = allSeries;
        }

        for (int i = 0; i < theCategories.size(); i++) {
            List<String> titles = new LinkedList<>();

            for (VideoObject video : indhold) {

                if (video.getCategories().contains(category.get(i))) {
                    titles.add(video.getTitle());

                }

                categories.put(category.get(i), titles);

            }
        }

        return categories;
    }

    public List<VideoObject> getVideoByCategory(String searchCategory) {
        List<VideoObject> categorySearchFilter = new ArrayList<>();

        for (VideoObject video : getAll()) {
            for (String category : video.getCategories()){
                if(category.equals(searchCategory) ){
                    categorySearchFilter.add(video);
                }
            }
        }

        return categorySearchFilter;
    }

    /*
    public void displayCategory() {

        List<String> categoryKeys = new ArrayList<>(getCategorie().keySet());
        Collections.sort(categoryKeys);
        for (String key : categoryKeys) {
            System.out.println(key + ": " + categories.get(key));
        }

    }
   */



    public List<VideoObject> videoSearch(String name) throws SearchIsEmptyException {
        List<VideoObject> videoSearchFilter = new ArrayList<>();

        for (VideoObject video : getAll()) {
            if (video.getTitle().toLowerCase().contains(name.toLowerCase())) {
                videoSearchFilter.add(video);
            }
        }

        return videoSearchFilter;
    }

    // ! Favorite List -------------------------------

    public HashSet<VideoObject>  addFavList(String title, HashSet<VideoObject> favoriteList) {
        this.favoriteList = favoriteList;
        // concatenate two lists
        List<VideoObject> allVideos = Stream.concat(allFilms.stream(), allSeries.stream())
                .collect(Collectors.toList());

        for (VideoObject video : allVideos) {
            if (video.getTitle().equals(title)) {
                video.setIsFavorite(true);
                favoriteList.add(video);
            }
        }
        return favoriteList;
    }

    public HashSet<VideoObject> removeFavList(String title, HashSet<VideoObject> favoriteList ) {
        this.favoriteList = favoriteList;
        Iterator<VideoObject> iterator = favoriteList.iterator();
        while (iterator.hasNext()) {
            VideoObject next = iterator.next();
            if (next.getTitle().equals(title)) {
                iterator.remove();
            }
        }
        return favoriteList;
    }


    public List<VideoObject> favSearch(String name , HashSet<VideoObject> favVideoSeries) {
        List<VideoObject> videoSearchFilter = new ArrayList<>();

        for (VideoObject video : favVideoSeries) {
            if (video.getTitle().toLowerCase().contains(name.toLowerCase())) {
                videoSearchFilter.add(video);
            }
        }
        return videoSearchFilter;
    }

    public List<VideoObject> combine(List<VideoObject> allSeries, List<VideoObject> allFilms) {
        List<VideoObject> allvideos = allSeries;

        for (VideoObject video: allFilms
             ) {
            allvideos.add(video);
        }

        return allvideos;
    }

    /*
    void displayFavList() {
        System.out.println("Favorite list");
        for (VideoObject fav : favoriteList) {
            System.out.println(fav.display());
        }
    }
    */


}