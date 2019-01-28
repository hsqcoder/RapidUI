package pers.like.framework.sample.model.pojo.douban;


import java.util.List;
import java.util.Map;

/**
 * @author Like
 */
public class Movie {

    private Integer id;
    private Star rating;
    private List<String> genres;
    private String title;
    private String year;
    private Map<String, String> images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Star getRating() {
        return rating;
    }

    public void setRating(Star rating) {
        this.rating = rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }
}
