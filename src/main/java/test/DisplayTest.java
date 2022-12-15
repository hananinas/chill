package test;

import Model.Display;
import Model.SearchIsEmptyException;
import Model.VideoData;
import Model.VideoObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DisplayTest {
    private Display movie;
    private Display show;

    @BeforeEach
    public void setUp() {
        movie = new Display(new VideoData(), "src/main/resources/data/film.txt", "src/main/resources/images/filmplakater");
        movie.videoData();

        show = new Display(new VideoData(), "src/main/resources/data/serier.txt", "src/main/resources/images/serieforsider");
        show.videoData();
    }

    @AfterEach
    public void tearDown() {
        movie = null;
    }

    @Test
    void videoData_Movies_size() {
        // Arrange
        int expected = 100;

        // Act
        int actual = movie.getAll().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void videoData_Shows_size() {
        // Arrange
        int expected = 100;

        // Act
        int actual = show.getAll().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getAllCategories_Movies() {
        // Arrange
        int expected = 20;

        // Act
        int actual = movie.getAllCategories().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getAllCategories_Shows() {
        // Arrange
        int expected = 20;

        // Act
        int actual = show.getAllCategories().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getAllCategories_MoviesShows() {
        // Arrange
        int expected = 23;

        // Act
        Set<String> allcategories = show.getAllCategories();
        allcategories.addAll(movie.getAllCategories());
        int actual = allcategories.size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void search_Movie_goD() throws SearchIsEmptyException {
        // Arrange
        int expected = 2;

        // Act
        int actual = movie.videoSearch("goD").size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void videoSearch_SearchIsEmptyException() throws SearchIsEmptyException {
        String search = "llllllllll";
        assertThrows(SearchIsEmptyException.class, () ->{
            movie.videoSearch(search);
            if(movie.videoSearch(search).size() == 0){
                throw new SearchIsEmptyException();
            }
        });
    }

    @Test
    void getVideoByCategory_getMovieCategory_Film_Noir() {
        // Arrange
        int expected = 4;

        // Act
        int actual = movie.getVideoByCategory("Film-Noir").size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void addFavList_Add3MovieToFav() {
        // Arrange
        HashSet<VideoObject>favList = new HashSet<VideoObject>();
        int expected = 3;

        // Act
        movie.addFavList("Titanic", favList);
        movie.addFavList("Rear Window", favList);
        movie.addFavList("The Third Man", favList);
        int actual = favList.size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void removeFavList_removeMovie() {
        // Arrange
        HashSet<VideoObject>favList = new HashSet<VideoObject>();
        int expected = 2;

        // Act
        movie.addFavList("Titanic", favList);
        movie.addFavList("Rear Window", favList);
        movie.addFavList("The Third Man", favList);
        movie.removeFavList("Rear Window", favList);
        int actual = favList.size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void favSearch() {
        // Arrange
        HashSet<VideoObject>favList = new HashSet<VideoObject>();
        int expected =1;

        // Act
        movie.addFavList("Titanic", favList);
        movie.addFavList("Rear Window", favList);
        movie.addFavList("The Third Man", favList);
        int actual = movie.favSearch("The Third Man",favList).size();

        // Assert
        assertEquals(expected, actual);
    }
}