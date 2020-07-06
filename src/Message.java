import java.io.*;

public class Message implements Serializable{
    private String choice;
    private double bet;
    private boolean isWin;

    Message() {
        isWin = false;
    }

    Message(String choice, double bet) {
        this.choice = choice;
        this.bet = bet;
    }

    //Сеттеры
    void setChoice(String choice) {
        this.choice = choice;
    }

    void setBet(double bet) {
        this.bet = bet;
    }

    void setWin(boolean win) {
        this.isWin = win;
    }

    //Геттеры
    String getChoice() {
        return choice;
    }

    double getBet() {
        return bet;
    }

    boolean isWin() {
        return isWin;
    }
}