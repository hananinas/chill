package Model;

import java.util.*;
import java.io.File;

public class Display {

    String Path;

    private List<String> videoList;
    private List<File> images;

    private List<VideoObject> allFilms;
    private List<VideoObject> allSeries;

    private Map<String, List<String>> categories;
    private Set<String> theCategories;

    private List<VideoObject> favoriteList;

    SerieObject serie;
    VideoObject film;

    public Display(DataAccess video, String path, String imagePath) {
        this.Path = path;

        video.saveData(path);
        video.saveImage(imagePath);

        images = video.getImage();
        videoList = video.getData();

        allFilms = new LinkedList<>();
        allSeries = new LinkedList();

        favoriteList = new LinkedList();

        categories = new HashMap<>();
        theCategories = new HashSet<>();
    }

    public void VideoData() {

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

            if (Path.contains("film")) {
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
        if (Path.contains("film")) {
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

        if (Path.contains("film")) {
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
    public void displayCategory() {

        List<String> categoryKeys = new ArrayList<>(getCategorie().keySet());
        Collections.sort(categoryKeys);
        for (String key : categoryKeys) {
            System.out.println(key + ": " + categories.get(key));
        }

    }



    public List<VideoObject> videoSearch(String name) {
        List<VideoObject> videoSearchFilter = new ArrayList<>();

        for (VideoObject video : getAll()) {
            if (video.getTitle().toLowerCase().contains(name.toLowerCase())) {
                videoSearchFilter.add(video);
            }
        }
        return videoSearchFilter;
    }

    // ! Favorite List -------------------------------

    public void addFavList(String title) {
        for (VideoObject film : allFilms) {
            if (film.getTitle().equals(title)) {
                favoriteList.add(film);
            }
        }
    }

    public void removeFavList(String title) {
        Iterator<VideoObject> iterator = favoriteList.iterator();
        while (iterator.hasNext()) {
            VideoObject next = iterator.next();
            if (next.getTitle().equals(title)) {
                iterator.remove();
            }
        }
    }

    public void favListControl(String title) {
        boolean isInList = false;
        for (VideoObject fav : favoriteList) {
            if (fav.getTitle().equals(title)) {
                isInList = true;
                break;
            }
        }

        if (!isInList)
            addFavList(title);
        else
            removeFavList(title);
    }

    public List<VideoObject> getMyFavList() {
        return favoriteList;
    }

    void displayFavList() {
        System.out.println("Favorite list");
        for (VideoObject fav : favoriteList) {
            System.out.println(fav.display());
        }
    }

    public List<VideoObject> favSearch(String name) {
        List<VideoObject> videoSearchFilter = new ArrayList<>();

        for (VideoObject video : getMyFavList()) {
            if (video.getTitle().toLowerCase().contains(name.toLowerCase())) {
                videoSearchFilter.add(video);
            }
        }
        return videoSearchFilter;
    }

}