package utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class MongoDBUtils {

    private static final MongoClient mongoClient;
    private static final MongoDatabase stores;
    private static final MongoCollection<Document> shops;
    private static final MongoCollection<Document> products;

    static {
        mongoClient = new MongoClient("127.0.0.1", 27017);
        stores = mongoClient.getDatabase("stores");
        shops = stores.getCollection("shop");
        products = stores.getCollection("products");
    }


    // ДОБАВИТЬ_МАГАЗИН
    public static void addShop(String shopName) {
        if (shopName.isEmpty()) {
            throw new RuntimeException();
        }
        try {
            Document shop = new Document("name", shopName);
            if (getShop(shopName) == null) {
                shop.append("products", new ArrayList<String>());
                shops.insertOne(shop);
                System.out.println("Магазин " + shopName + " добавлен!");
            } else {
                System.out.println("Магазин уже был добавлен!");
            }
        } catch (Exception e) {
            System.out.println("Неверный ввод!");
        }
    }

    // ДОБАВИТЬ_ТОВАР
    public static void addProducts(String input) {
        String[] command = input.split(" ", 2);
        String name = command[0];
        Document product = new Document("name", name);
        product.append("price", Integer.parseInt(command[1]));
        if (getProduct(name) == null) {
            products.insertOne(product);
            System.out.println("Продукт " + name + " добавлен!");
        } else {
            System.out.println("Товар уже был добавлен");
        }
    }

    // ВЫСТАВИТЬ_ТОВАР
    public static void addProductsToShop(String input) {
        String[] command = input.split(" ", 2);
        String shopName = command[1];
        String productName = command[0];
        if (getShop(shopName) == null && getProduct(productName) == null) {
            System.out.println("Магазин или продукт не существует");
        } else {
            shops.updateOne(getShop(shopName), new Document("$addToSet",
                    new Document("products", getProduct(productName).get("name"))));
            System.out.println("Продукт " + productName + " добавлен в магазин " + shopName);
        }
    }

    // СТАТИСТИКА_ТОВАРОВ
    public static void printStatistic() {
        printInfo(getAggregate());
    }

    private static AggregateIterable<Document> getAggregate() {
        return products.aggregate(
                Arrays.asList(
                        Aggregates.lookup("shop", "name", "products", "shop_list"),
                        Aggregates.unwind("$shop_list"),
                        Aggregates.group("$shop_list.name",
                                Accumulators.sum("count_products", 1),    // Количество товара
                                Accumulators.min("min_price", "$price"),  // Самый дешевый товар
                                Accumulators.max("max_price", "$price"),  // Самый дорогой товар
                                Accumulators.avg("avg_price", "$price"))  // Средняя цена товара
                )
        );
    }

    // Количество товаров, дешевле 100 рублей
    private static long productsCheaperOneHundred(String shopName) {
        Document shop = getShop(shopName);
        ArrayList<String> products = (ArrayList<String>) shop.get("products");
        return products.stream().filter(s -> (int) getProduct(s).get("price") < 100).count();
    }

    // Вывод статистики в консоль
    private static void printInfo(AggregateIterable<Document> documents) throws MongoException {
        try {
            for (Document document : documents) {
                String shopName = (String) document.get("_id");
                System.out.println("Магазин " + shopName);
                System.out.println("Количество товара: " + document.get("count_products"));
                System.out.println("Средняя цена товара: " + document.get("avg_price"));
                System.out.println("Самый дорогой товар:  " + document.get("max_price"));
                System.out.println("Самый дешевый товар:  " + document.get("min_price"));
                System.out.println("Количество товаров, дешевле 100 рублей: " + productsCheaperOneHundred(shopName));
            }
        } catch (MongoException e) {
            System.out.println(e);
        }
    }

    // EXIT
    public static void shutdownDB() {
        stores.drop();
        mongoClient.close();
        System.out.println("Работа завершена!");
    }

    private static Document getShop(String name) {
        return shops.find(new Document("name", name)).first();
    }

    private static Document getProduct(String name) {
        return products.find(new Document("name", name)).first();
    }
}