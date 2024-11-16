# 商品管理系统
此项目是一个基于Java的桌面应用程序，用于管理商品信息，主要包括增删改查四个功能。通过图形用户界面与用户进行交互，并且使用JSON文件来保存和读取商品数据。

## 项目结构

项目包含以下主要部分：
1. **商品类（Goods.java）**
2. **商品实现类（Product.java）**
3. **商品服务接口（GoodsService.java）**
4. **商品服务实现类（GoodsServiceImpl.java）**
5. **主类（Main.java）**


最终效果：
1. 用户界面

![image](https://github.com/user-attachments/assets/41707a11-5735-46c8-961d-7f51ac44fc48)


2. 相关业务弹窗

![image](https://github.com/user-attachments/assets/9d42c789-d7cc-473e-b1b0-2d0a1627b60f)


3. 警告弹窗



![image](https://github.com/user-attachments/assets/128f3b63-1426-49b9-9735-ee60886cf5a6)


## 项目流程

### 项目工作流程
1. **加载数据**：启动程序时，从`goods.json`文件中加载商品数据，并初始化商品列表。
2. **显示界面**：创建一个图形用户界面，包含商品表格和操作按钮。
3. **添加商品**：点击“添加商品”按钮，弹出对话框输入商品信息，确认后将商品添加到商品列表并更新表格显示。
4. **删除商品**：选择表格中的商品，点击“删除商品”按钮，删除该商品并更新表格显示。
5. **修改商品**：选择表格中的商品，点击“修改商品”按钮，弹出对话框修改商品信息，确认后更新商品列表和表格显示。
6. **查询商品**：输入商品编号或名称，点击“查询”按钮，在表格中显示匹配的商品。
7. **显示所有商品**：点击“显示所有商品”按钮，在表格中显示所有商品。
8. **保存数据**：每次进行添加、删除、修改操作时，都会将最新的商品数据保存到`goods.json`文件中。

### 1. 商品类（`Goods.java`）
商品类是一个抽象类，包含商品的基本属性：编号、名称、价格和数量。我们将这些属性封装在这个类中，以便在具体实现类中继承和使用。
```java
public abstract class Goods {
    private String id;
    private String name;
    private double price;
    private int quantity;

    // 构造函数和getter/setter方法
}
```

### 2. 商品实现类（`Product.java`）

在这个系统中，`Product` 类是具体的商品类，它继承了抽象的 `Goods` 类，用来表示商品的具体实例。在代码中，`Product` 类用于创建具体的商品对象，并且这些对象被添加到 `GoodsServiceImpl` 的商品列表中。下面是如何在各个部分中使用 `Product` 类的具体示例。

#### 2.1在Product 类中

`Product` 类继承了 `Goods` 类，没有额外的属性或方法，仅仅是对 `Goods` 类的具体化。

```java
public class Product extends Goods {
    public Product(String id, String name, double price, int quantity) {
        super(id, name, price, quantity);  // 调用父类构造函数
    }
}
```

#### 2.2 Main 类中的使用

在 `Main` 类中，使用 `Product` 类来创建具体的商品对象，并通过 `GoodsService` 接口将它们添加到系统中。

#### 2.3 GoodsServiceImpl 类中的使用

在 `GoodsServiceImpl` 类中使用 `Product` 类来处理商品的增删改查操作。

### 3. 商品服务接口（`GoodsService.java`）

这个接口定义了商品管理的基本操作：添加商品、删除商品、修改商品、查询商品和获取所有商品列表。接口为这些操作提供了一种标准，具体实现将在实现类中完成。
```java
public interface GoodsService {
    void addGoods(Goods goods);
    void deleteGoods(String id);
    void updateGoods(Goods goods);
    Goods queryGoods(String identifier);
    List<Goods> getAllGoods();
}
```

### 4. 商品服务实现类（`GoodsServiceImpl.java`）
这个类实现了商品服务接口中的所有方法，负责处理具体的商品管理逻辑。同时，它还包含正则表达式，用于验证商品编号和名称的格式。
```java
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

    // 其他方法...

    private boolean isValidId(String id) {
        return Pattern.matches("[a-zA-Z0-9]+", id);
    }

    private boolean isValidName(String name) {
        return Pattern.matches("[\\u4e00-\\u9fa5a-zA-Z0-9 ]+", name);
    }
}
```

### 5. 主类 (`Main.java`)

主类是程序的入口，创建图形用户界面，并处理用户的各种操作（添加、删除、修改、查询商品）。还负责从文件中加载商品数据和将数据保存到文件中。
```java
public class Main {
    private static GoodsService goodsService = new GoodsServiceImpl();
    private static final String DATA_FILE = "goods.json";
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {
        // 加载数据
        loadGoodsData();

        // 创建窗口和组件
        JFrame frame = new JFrame("商品管理系统");
        // 其他界面代码...

        frame.setVisible(true);
        showAllGoodsInTable();
    }

    // 其他方法...

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
```


------



## 可能会有的问题
### 1. 如何将数据保存到JSON文件中
在这个项目中，我们使用了Gson库将数据保存到JSON文件中。具体来说，通过调用Gson的`toJson`方法将商品数据列表序列化为JSON格式，并使用FileWriter将其写入到文件中。下面是详细的步骤：

#### 1.1 引入Gson库

导入Gson的本质就是引入jar包。

**教程见: https://blog.csdn.net/2302_80067378/article/details/137665364**

Gson.jar的资源在文件夹中

#### 1.2 准备数据

首先需要把商品数据保存在一个列表中，这个列表存储了所有的商品对象。
```java
    private List<Goods> goodsList;
```

#### 1.3 加载数据

在程序启动时，我们会尝试从一个名为 `goods.json` 的文件中读取数据，并将这些数据加载到我们的商品列表中。我们使用了Gson库来实现JSON与Java对象之间的转换。
```java
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
```

#### 1.4 保存数据

当用户添加、删除或更新商品时，我们需要将最新的商品列表保存到 `goods.json` 文件中。同样，我们使用Gson库将商品列表转换成JSON格式并写入文件。
```java
    private static void saveGoodsData() {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            Gson gson = new Gson();
            List<Goods> goodsList = goodsService.getAllGoods();
            gson.toJson(goodsList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

### 2. 用户界面是如何编写的？又是如何与这些业务进行交互的？

#### 2.1 窗体的编写

在`Main.java`中，窗体的主要部分是`JFrame`和一些Swing组件，如`JButton`、`JTable`、`JPanel`等。这些组件被组合在一起形成一个图形用户界面。

##### 2.1.1 初始化窗体

使用 `JFrame` 创建主窗口。窗口的大小设定为600x400像素，布局管理器设置为 `BorderLayout`，并将窗口位置设置在屏幕中央
```java
   JFrame frame = new JFrame("商品管理系统");
   frame.setSize(600, 400);
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   frame.setLayout(new BorderLayout());
   frame.setLocationRelativeTo(null);
```

##### 2.1.2  创建表格模型和表格

使用 `JTable` 创建一个表格来显示商品信息。表格的数据由 `DefaultTableModel` 管理，表格模型包含商品的编号、名称、数量和价格。
```java
   String[] columnNames = {"商品编号", "商品名称", "数量", "价格"};
   tableModel = new DefaultTableModel(columnNames, 0);
   JTable table = new JTable(tableModel);
   JScrollPane scrollPane = new JScrollPane(table);
```

##### 2.1.3 创建按钮和按钮面板

创建添加、删除、修改和显示所有商品的按钮，并将这些按钮放在一个垂直布局的面板中。
   ```java
   JButton addButton = new JButton("添加商品");
   JButton deleteButton = new JButton("删除商品");
   JButton updateButton = new JButton("修改商品");
   JButton listButton = new JButton("显示所有商品");

   JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
   buttonPanel.add(addButton);
   buttonPanel.add(deleteButton);
   buttonPanel.add(updateButton);
   buttonPanel.add(listButton);
   ```

##### 2.1.4 创建查询面板

创建一个查询面板，包括一个标签、一个文本输入框和一个查询按钮。
   ```java
   JPanel queryPanel = new JPanel(new FlowLayout());
   JLabel queryLabel = new JLabel("输入商品编号或名称以查询：");
   JTextField queryField = new JTextField(20);
   JButton queryButton = new JButton("查询");

   queryPanel.add(queryLabel);
   queryPanel.add(queryField);
   queryPanel.add(queryButton);
   ```

##### 2.1.5 添加组件到主窗口

将表格的滚动面板添加到主窗口的中央区域，将按钮面板添加到东侧区域，将查询面板添加到北侧区域。
   ```java
   frame.add(queryPanel, BorderLayout.NORTH);
   frame.add(scrollPane, BorderLayout.CENTER);
   frame.add(buttonPanel, BorderLayout.EAST);
   ```

#### 2.2 窗体与业务逻辑的交互

窗体通过事件监听器与业务逻辑进行交互。以下是几个关键操作的交互方式：

##### 2.2.1 创建业务逻辑对象

创建 `GoodsService` 接口的实现类 `GoodsServiceImpl` 的实例，来管理所有商品的增删改查操作。
   ```java
   private static GoodsService goodsService = new GoodsServiceImpl();
   ```

##### 2.2.2 按钮的事件监听器

每个按钮都添加了 `ActionListener`，当用户点击按钮时，会执行相应的操作。例如，点击“添加商品”按钮时，会打开一个对话框让用户输入商品信息，然后调用业务逻辑的 `addGoods` 方法添加商品。
   ```java
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
   ```

##### 2.2.3 对话框与业务逻辑的交互

当用户在对话框中输入商品信息并点击确认按钮时，收集输入的数据，创建一个 `Product` 对象，并调用 `goodsService.addGoods(goods)` 方法添加商品。
```java
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
```
## 总结
该任务总体难度较高，在实现过程中借助了AI工具辅助编写代码。

最终能够正常运行，基本实现目标效果。但效果有些不尽人意，同时存在着一些BUG，后续需要改进。
