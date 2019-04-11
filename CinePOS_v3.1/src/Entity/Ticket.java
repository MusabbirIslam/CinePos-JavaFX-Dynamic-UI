package Entity;

/**
 * Application Name : CinePOS_v3
 * Package Name     : Entity
 * Author           : Abu Bakar Siddique
 * Email            : absiddique.live@gmail.com
 * Created Date     : 2/28/17
 */
public class Ticket {

    public int id;
    public FilmTime filmTime;
    public ScreenSeat screenSeat;
    public String description;
    public String annotation;
    public Float printedPrice;
    public boolean sellOnWeb;
    public boolean sellOnKiosk;
    public boolean sellOnPos;
    public boolean isChild;
    public boolean isAdult;
    public boolean status;
    public String currentState;
    public String startDate;
    public String endDate;
    public String createdAt;

}
