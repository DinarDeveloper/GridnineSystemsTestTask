package utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;

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
            shop.append("products", new ArrayList<String>());

            if (getShop(shopName) == null) {
                MongoDBUtils.shops.insertOne(shop);
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
            shops.updateOne(eq(getShop(shopName)), new Document("$addToSet",
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
                        lookup("shop", "name", "name", "shop_list"),
                        unwind("$shop_list"),
                        group("$shop_list.name",
                                sum("count_products", 1),    // Количество товара
                                min("min_price", "$price"),  // Самый дешевый товар
                                max("max_price", "$price"),  // Самый дорогой товар
                                avg("avg_price", "$price")), // Средняя цена товара
                        match(lt("cheap_product", 100)),
                        count("cheap_product")                            // Количество товаров, дешевле 100 рублей
                )
        );
    }

    private static void printInfo(AggregateIterable<Document> documents) throws MongoException {
        try {
            for (Document document : documents) {
                String shopName = (String) document.get("_id");
                System.out.println("Магазин " + shopName);
                System.out.println("Количество товара: " + document.get("count_products"));
                System.out.println("Средняя цена товара: " + document.get("avg_price"));
                System.out.println("Самый дорогой товар:  " + document.get("max_price"));
                System.out.println("Самый дешевый товар:  " + document.get("min_price"));
                System.out.println("Количество товаров, дешевле 100 рублей: " + document.get("cheap_product"));
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