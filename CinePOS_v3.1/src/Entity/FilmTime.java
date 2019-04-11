package Entity;
import java.sql.Time;
import java.sql.Timestamp;


public class FilmTime {

    public int id;
    public Integer filmScheduleId;
    public Film film;
    public String startTime;
    public String endTime;
    public boolean status;
    public Integer createdBy;
    public String createdAt;

}
