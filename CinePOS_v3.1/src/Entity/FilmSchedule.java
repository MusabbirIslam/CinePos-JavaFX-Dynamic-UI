package Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FilmSchedule {

    public int                 id;
    public Screen              screen;
    public List<FilmTime> filmTimes;
    public boolean             status;
    public String              date;
    public int                 weekDay;
    public int                 createdBy;
    public String              createdAt;

    public FilmSchedule() {
        id = 0;
        screen = null;
        filmTimes = null;
        status = false;

        weekDay = 0;
        createdBy =0;
        createdAt = "";
    }
}
