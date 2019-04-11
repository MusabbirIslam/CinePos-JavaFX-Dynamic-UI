package Entity;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class Screen {

    public int id;
    public String name;
    public boolean isSeatPlanComplete;
    public int noOfSeat;
    public ScreenDimension screenDimension;
    public boolean active;
    public int rowCount;
    public int columnCount;
    public String openingTime;
    public String closingTime;
    public String createdAt;

}
