package Entity;

/**
 * Application Name : CinePOS
 * Package Name     : Entity
 * Author           : Abu Bakar Siddique
 * Email            : absiddique.live@gmail.com
 * Created Date     : 2/3/17
 */
public class CartProduct {

    public int id;
    public int quantity;
    public double price;
    public String sellingType;

    public CartProduct(){
        id=0;
        quantity=0;
        price=0.0;
        sellingType="";
    }

}
