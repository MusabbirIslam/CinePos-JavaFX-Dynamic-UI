package Entity;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public class Film {

    public int id;
    public String name;
    public Distributor distributor;
    public List<Genre> filmGenre;
    public Set<ScreenDimension> screenDimensions;
    public List<FilmImage> filmImages;
    public List<FilmTrailer> filmTrailers;
    public List<FilmTime> filmTimes;
    public float rating;
    public double durationHour;
    public double durationMin;
    public Boolean status;
    public String startDate;
    public String endDate;
    public boolean isPriceShift;
    public Integer createdBy;
    public String createdAt;

    public Film(){
        id=0;
        name="";
        distributor=null;
        filmGenre=null;
        screenDimensions = null;
        filmImages = null;
        filmTrailers= null;
        filmTimes=null;
        rating = 0;
        durationHour=0.0;
        durationMin=0.0;
        status=false;
        startDate="";
        endDate="";
        isPriceShift=false;
        createdBy=0;
        createdAt="";

    }
}
