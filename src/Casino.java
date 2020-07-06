
class Casino  {
    final private double balance_player = 10000.0; //Изначальный баланс игрока

    protected boolean calcVictory(String choice) {
        if (choice.equalsIgnoreCase("HEADS") && randomize()) //Если пользователь выбрал HEADS и число выпало больше 0.5
            return true;                                        //сообщить о победе
        else if (choice.equalsIgnoreCase("TAILS") && !randomize()) //Если пользователь выбрал TAILS и число выпало меньше 0.5
            return true;                                              //сообщить о поражении

        return false; // Сообщить о поражении
    }

    private boolean randomize() {
        double result =  Math.random();

        return result > 0.5 ?  true :  false;
    }

}


