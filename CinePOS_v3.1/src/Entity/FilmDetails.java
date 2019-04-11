package Entity;

/**
 * Application Name : CinePOS
 * Package Name     : Entity
 * Author           : Abu Bakar Siddique
 * Email            : absiddique.live@gmail.com
 * Created Date     : 2/10/17
 */
public class FilmDetails {
    public int filmTimeId;
    public String filmName;
    public String screenName;
    public String filmTrailer;
    public double rating;
    public String genere;
    public String startTime;
    public String endTime;
    public String duration;
    public String synopsis;
    public String cast;
    public String director;
    public String nextShowing;

    public FilmDetails(){
        filmTimeId=0;
        filmName="";
        screenName ="";
        rating = 0.0;
        genere= "";
        startTime="";
        endTime= "";
        duration= "";
        synopsis= "synopsis";
        cast = "cast";
        director = "director";
        nextShowing = "";

    }

}
