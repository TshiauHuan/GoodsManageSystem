package GoodsManageSystem;

public class Product extends Goods {
    public Product(String id, String name, double price, int quantity) {
        super(id, name, price, quantity);  // 调用父类构造函数
    }
}
