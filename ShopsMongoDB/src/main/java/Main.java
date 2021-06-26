import java.util.Scanner;

import static utils.MongoDBUtils.*;

public class Main {

    private static final String commandExample = "Примеры команд:"
            + "\n - ДОБАВИТЬ_МАГАЗИН Девяточка"
            + "\n - ДОБАВИТЬ_ТОВАР Вафли 54"
            + "\n - ВЫСТАВИТЬ_ТОВАР Вафли Девяточка"
            + "\n - СТАТИСТИКА_ТОВАРОВ"
            + "\n - EXIT - завершает работу программы";


    public static void main(String[] args) {
        while (true) {
            System.out.println(commandExample);
            System.out.println("Введите команду:");
            Scanner scanner = new Scanner(System.in);
            String[] command = scanner.nextLine().split(" +", 2);

            switch (command[0]) {
                case "ДОБАВИТЬ_МАГАЗИН":
                    addShop(command[1]);
                    break;
                case "ДОБАВИТЬ_ТОВАР":
                    addProducts(command[1]);
                    break;
                case "ВЫСТАВИТЬ_ТОВАР":
                    addProductsToShop(command[1]);
                    break;
                case "СТАТИСТИКА_ТОВАРОВ":
                    printStatistic();
                    break;
                case "EXIT":
                    shutdownDB();
                    return;
                default:
                    System.out.println("Неверный ввод");
            }
        }
    }
}