package Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Application Name : CinePOS
 * Package Name     : Entity
 * Author           : Abu Bakar Siddique
 * Email            : absiddique.live@gmail.com
 * Created Date     : 2/3/17
 */
public class OrderJson {

    public ArrayList<CartProduct>cart;
    public int terminalId;

    public OrderJson(){
        cart = new ArrayList<>();
    }
}
