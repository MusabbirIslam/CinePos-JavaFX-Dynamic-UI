package Entity;

import java.sql.Timestamp;


public class ScreenSeat {
    public int id;
    public Integer screenId;
    public SeatType seatType;
    public String name;
    public Integer createdBy;
    public String createdAt;

    public ScreenSeat(){
        id=0;
        screenId=0;
        seatType = null;
        name="";
        createdBy=0;
        createdAt="";

    }
}
