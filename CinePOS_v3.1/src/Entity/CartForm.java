package Entity;

public class CartForm {

    public Integer id;
    public int productQuantity;
    public int ticketQuantity;
    public double price;
    public int screenId;
    public int ticketId;
    public String sellingType;

    public CartForm(int id,int productQuantity,int ticketQuantity,double price,int screenId,int ticketId, String sellingType){
        this.id =id;
        this.productQuantity=productQuantity;
        this.ticketQuantity=ticketQuantity;
        this.price=price;
        this.screenId=screenId;
        this.ticketId=ticketId;
        this.sellingType = sellingType;
    }

}
