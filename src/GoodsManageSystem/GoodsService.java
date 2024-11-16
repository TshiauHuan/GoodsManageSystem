package GoodsManageSystem;

import java.util.List;

public interface GoodsService {
    void addGoods(Goods goods);
    void deleteGoods(String id);
    void updateGoods(Goods goods);
    Goods queryGoods(String identifier);
    List<Goods> getAllGoods();
}
