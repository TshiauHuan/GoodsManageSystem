package GoodsManageSystem;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {
    private static GoodsService goodsService = new GoodsServiceImpl(); //goodsService：商品服务对象，用于管理商品。
    private static final String DATA_FILE = "goods.json"; //DATA_FILE：数据文件名，用于保存和加载商品数据。
    private static DefaultTableModel tableModel; //tableModel：表格模型，用于管理表格中的数据

    public static void main(String[] args) {
        loadGoodsData(); //加载商品数据

        //创建窗口frame，设置大小、关闭操作、布局和位置
        JFrame frame = new JFrame("商品管理系统");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        //创建表格和滚动面板
        String[] columnNames = {"商品编号", "商品名称", "数量", "价格"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        //创建按钮和查询面板
        JButton addButton = new JButton("添加商品");
        JButton deleteButton = new JButton("删除商品");
        JButton updateButton = new JButton("修改商品");
        JButton listButton = new JButton("显示所有商品");

        Dimension buttonSize = new Dimension(150, 25);
        addButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        updateButton.setPreferredSize(buttonSize);
        listButton.setPreferredSize(buttonSize);

        //创建查询面板
        JPanel queryPanel = new JPanel(new FlowLayout());
        JLabel queryLabel = new JLabel("输入商品编号或名称以查询：");
        JTextField queryField = new JTextField(20);
        JButton queryButton = new JButton("查询");

        queryPanel.add(queryLabel);
        queryPanel.add(queryField);
        queryPanel.add(queryButton);

        //按钮面板，采用网格布局，将各个按钮添加到面板中
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(queryButton);
        buttonPanel.add(listButton);

        //将组件添加到主窗口
        frame.add(queryPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.EAST);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProductDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String id = (String) tableModel.getValueAt(selectedRow, 0);
                    goodsService.deleteGoods(id);
                    tableModel.removeRow(selectedRow);
                    saveGoodsData();
                } else {
                    JOptionPane.showMessageDialog(frame, "请选择要删除的商品。");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String id = (String) tableModel.getValueAt(selectedRow, 0);
                    showUpdateProductDialog(id);
                } else {
                    JOptionPane.showMessageDialog(frame, "请选择要修改的商品。");
                }
            }
        });

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = queryField.getText();
                Goods goods = goodsService.queryGoods(name);
                if (goods != null) {
                    showGoodsInTable(goods);
                } else {
                    JOptionPane.showMessageDialog(frame, "未找到商品。");
                }
            }
        });

        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllGoodsInTable();
            }
        });

        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    String id = (String) tableModel.getValueAt(row, 0);
                    Goods goods = goodsService.queryGoods(id);
                    if (goods != null) {
                        switch (column) {
                            case 1:
                                goods.setName((String) tableModel.getValueAt(row, column));
                                break;
                            case 2:
                                goods.setQuantity(Integer.parseInt((String) tableModel.getValueAt(row, column)));
                                break;
                            case 3:
                                goods.setPrice(Double.parseDouble((String) tableModel.getValueAt(row, column)));
                                break;
                        }
                        saveGoodsData();
                    }
                }
            }
        });

        frame.setVisible(true);
        showAllGoodsInTable();//显示所有商品在表格中
    }

    //添加商品
    private static void showAddProductDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("添加商品");
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(5, 2));
        dialog.setLocationRelativeTo(null);

        JLabel idLabel = new JLabel("商品编号：");
        JTextField idField = new JTextField();
        JLabel nameLabel = new JLabel("商品名称：");
        JTextField nameField = new JTextField();
        JLabel priceLabel = new JLabel("价格：");
        JTextField priceField = new JTextField();
        JLabel quantityLabel = new JLabel("数量：");
        JTextField quantityField = new JTextField();

        JButton confirmButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");

        dialog.add(idLabel);
        dialog.add(idField);
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(priceLabel);
        dialog.add(priceField);
        dialog.add(quantityLabel);
        dialog.add(quantityField);
        dialog.add(confirmButton);
        dialog.add(cancelButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                Goods goods = new Product(id, name, price, quantity);
                goodsService.addGoods(goods);
                saveGoodsData();
                showAllGoodsInTable();
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    //修改商品
    private static void showUpdateProductDialog(String id) {
        Goods goods = goodsService.queryGoods(id);
        if (goods == null) {
            JOptionPane.showMessageDialog(null, "未找到商品。");
            return;
        }

        JDialog dialog = new JDialog();
        dialog.setTitle("修改商品");
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(5, 2));
        dialog.setLocationRelativeTo(null);

        JLabel idLabel = new JLabel("商品编号：");
        JTextField idField = new JTextField(goods.getId());
        idField.setEditable(false);
        JLabel nameLabel = new JLabel("商品名称：");
        JTextField nameField = new JTextField(goods.getName());
        JLabel priceLabel = new JLabel("价格：");
        JTextField priceField = new JTextField(String.valueOf(goods.getPrice()));
        JLabel quantityLabel = new JLabel("数量：");
        JTextField quantityField = new JTextField(String.valueOf(goods.getQuantity()));

        JButton confirmButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");

        dialog.add(idLabel);
        dialog.add(idField);
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(priceLabel);
        dialog.add(priceField);
        dialog.add(quantityLabel);
        dialog.add(quantityField);
        dialog.add(confirmButton);
        dialog.add(cancelButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                goods.setName(name);
                goods.setPrice(price);
                goods.setQuantity(quantity);

                goodsService.updateGoods(goods);
                saveGoodsData();
                showAllGoodsInTable();
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }


    private static void showGoodsInTable(Goods goods) {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{goods.getId(), goods.getName(), goods.getQuantity(), goods.getPrice()});
    }
    //显示所有商品
    private static void showAllGoodsInTable() {
        tableModel.setRowCount(0);
        List<Goods> goodsList = goodsService.getAllGoods();
        for (Goods goods : goodsList) {
            tableModel.addRow(new Object[]{goods.getId(), goods.getName(), goods.getQuantity(), goods.getPrice()});
        }
    }

    private static void loadGoodsData() {
        try (FileReader reader = new FileReader(DATA_FILE)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Product>>() {}.getType();
            List<Product> goodsList = gson.fromJson(reader, type);
            for (Product product : goodsList) {
                goodsService.addGoods(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveGoodsData() {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            Gson gson = new Gson();
            List<Goods> goodsList = goodsService.getAllGoods();
            gson.toJson(goodsList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
