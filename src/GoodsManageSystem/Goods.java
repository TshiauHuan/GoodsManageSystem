package GoodsManageSystem;

public abstract class Goods {
    private String id;
    private String name;
    private double price;
    private int quantity;

    public Goods(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;  // 获取商品编号
    }

    public void setId(String id) {
        this.id = id;  // 设置商品编号
    }

    public String getName() {
        return name;  // 获取商品名称
    }

    public void setName(String name) {
        this.name = name;  // 设置商品名称
    }

    public double getPrice() {
        return price;  // 获取商品价格
    }

    public void setPrice(double price) {
        this.price = price;  // 设置商品价格
    }

    public int getQuantity() {
        return quantity;  // 获取商品数量
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;  // 设置商品数量
    }
}
