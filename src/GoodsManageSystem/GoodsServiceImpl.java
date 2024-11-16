package GoodsManageSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GoodsServiceImpl implements GoodsService {
    private List<Goods> goodsList;

    public GoodsServiceImpl() {
        this.goodsList = new ArrayList<>();
    }

    @Override
    public void addGoods(Goods goods) {
        if (isValidId(goods.getId()) && isValidName(goods.getName())) {
            goodsList.add(goods);
        }
    }

    @Override
    public void deleteGoods(String id) {
        goodsList.removeIf(goods -> goods.getId().equals(id));
    }

    @Override
    public void updateGoods(Goods goods) {
        for (int i = 0; i < goodsList.size(); i++) {
            if (goodsList.get(i).getId().equals(goods.getId())) {
                goodsList.set(i, goods);
                break;
            }
        }
    }

    @Override
    public Goods queryGoods(String identifier) {
        for (Goods goods : goodsList) {
            if (goods.getId().equals(identifier) || goods.getName().equals(identifier)) {
                return goods;
            }
        }
        return null;
    }

    @Override
    public List<Goods> getAllGoods() {
        return new ArrayList<>(goodsList);
    }

    private boolean isValidId(String id) {
        return Pattern.matches("[a-zA-Z0-9]+", id);
    }

    private boolean isValidName(String name) {
        return Pattern.matches("[\\u4e00-\\u9fa5a-zA-Z0-9 ]+", name);
    }
}
