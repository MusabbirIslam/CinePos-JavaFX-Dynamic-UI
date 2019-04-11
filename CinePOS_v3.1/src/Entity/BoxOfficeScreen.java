package Entity;

import java.lang.management.ThreadInfo;

/**
 * Application Name : CinePOS_v3.1
 * Package Name     : Entity
 * Author           : Abu Bakar Siddique
 * Email            : absiddique.live@gmail.com
 * Created Date     : 3/10/17
 */
public class BoxOfficeScreen {

    public Screen screen;
    public BoxOfficeFilm boxOffice;   // Could be null
    public FilmSchedule filmSchedule;  // Could be null

    public BoxOfficeScreen(){
        screen = null;
        boxOffice =  null;
        filmSchedule = null;
    }
}
