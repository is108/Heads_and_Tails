import java.util.*;

class Player extends Client {
    boolean isPlay; //игрок играет

    Player(String server, int port) {
        super(server, port);
        balance = 0;
        isPlay = true;
    }

    public void move() {

        Scanner scan = new Scanner(System.in);

        System.out.println("Make your choice! HEADS or TAILS");
        String choice = scan.nextLine();

        //Проверка хочет ли пользователь покинуть игру
        if (choice.equalsIgnoreCase("e")) {
            isPlay = false;
            return;
        }

        System.out.println("Make your bet. Your balance - " + balance);
        double bet;

        if (scan.hasNextDouble())
            bet = scan.nextDouble();
        else {
            System.out.println("Not a number");
            return;
        }

        if (choice.equalsIgnoreCase("HEADS") && (bet > 0 && bet <= balance)) {
            sendMessage(new Message(choice, bet));
        }
        else if (choice.equalsIgnoreCase("TAILS") && (bet > 0 && bet <= balance)) {
            sendMessage(new Message(choice, bet));
        }
        else {
            System.out.println("Incorrect data");
            return;
        }

        //Синхронизация потоков
        try {
            Thread.sleep(100);
        }
        catch(InterruptedException Ex) {}

        //Подсчет баланса
        calculateBalance();
    }


    private void calculateBalance() {
        if (request_msg.isWin()) {
            balance += request_msg.getBet();
            System.out.println("Congrations! You win " + request_msg.getBet() + "! Your current balance - " + balance);
        }
        else {
            balance -= request_msg.getBet();
            System.out.println("You lose :c! You lose " + request_msg.getBet() + "! Your current balance - " + balance + ". Try again");
        }
    }


    public void setBalance(double balance) {
        this.balance = balance;
    }


    public double getBalance() {
        return balance;
    }


    //Вывод меню, в котором указаны инструкции для игрока
    public void printGameMenu() {
        System.out.println("Welcome to the game 'Heads and Tails'.");
        System.out.println("1. Print your choice (HEADS or TAILS) and your bet. #Example: HEADS 1000");
        System.out.println("2. Print 'e' to exit");
    }
}


