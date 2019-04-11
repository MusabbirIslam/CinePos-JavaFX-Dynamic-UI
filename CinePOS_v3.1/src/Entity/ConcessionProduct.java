package Entity;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Sarwar on 1/13/2017.
 */

public class ConcessionProduct {

    public int id;
    public String name;
    public String description;
    public String annotation;
    public ConcessionProductCategory concessionProductCategory;
    public List<ConcessionProductImage> concessionProductImages;
    public int unit;
    public int remotePrint;
    public int isCombo;
    public int status;
    public Float sellingPrice;
    public Float buyingPrice;
    public int isPriceShift;
    public int createdBy;
    public String createdAt;

}
