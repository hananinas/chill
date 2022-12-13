package Model;

public class test {

    public static void main(String[] args) {
       DataAccess seriedata = new VideoData();
        Display serie = new Display(seriedata, "src/main/resources/data/serier.txt", "src/main/resources/images/serieforsider");
        serie.VideoData();
        System.out.println(serie.getAllCategories().size());




        VideoData filmdata = new VideoData();
        Display film = new Display(filmdata, "src/main/resources/data/film.txt", "src/main/resources/images/filmplakater");
        film.VideoData();

        System.out.println(film.getAllCategories().size());

        



        // film.addToFavList("Vertigo");
        // film.addToFavList("Psycho");
        // film.RemoveFavList("Vertigo");
        // film.addToFavList("Vertigo");
        // film.displayFavList();

        // film.getAll();

        // String search = "Rom";
        // System.out.println("Searching: '" + search +
        // "'........................................");

        // film.displayVideoSearch(search);

        // System.out.println(serie.getAllCategories());
        // film.displayCategory();

        // film.displayVideoSearch(search);

        // serie.getEpisodeOfTheSeasons();

    }
}