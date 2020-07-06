import java.util.*;

class Player extends Client {
    boolean isPlay; //игрок играет
    ArrayList<Message> history_games;

    Player(String server, int port) {
        super(server, port);

        balance = 0;
        isPlay = true;
        history_games = new ArrayList<Message>();
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

        if (choice.equalsIgnoreCase("history")) {
            printHistory();
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
        history_games.add(request_msg);
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


    //Вывод игровой истории
    private void printHistory() {
        String result; //Результат победы или поражения
        int index; //индекс ставки

        System.out.println("Your game history:");
        for (int i = 0; i < history_games.size(); i++) {
            index = i + 1;

            Message game = history_games.get(i);

            if (game.isWin())
                result = "Win";
            else
                result = "Lose";

            System.out.println(index + ") " + result + ". Choise: " + game.getChoice() + ", Bet: " + game.getBet());
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
        System.out.println("3. Print 'history' to show history games");
    }
}


