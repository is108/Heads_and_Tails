
class Casino  {
    protected boolean calcVictory(String choice) {
        if (choice.equalsIgnoreCase("HEADS") && randomize()) //Если пользователь выбрал HEADS и число выпало больше 0.5
            return true;                                        //сообщить о победе
        else if (choice.equalsIgnoreCase("TAILS") && !randomize()) //Если пользователь выбрал TAILS и число выпало меньше 0.5
            return true;                                              //сообщить о поражении

        return false; // Сообщить о поражении
    }

    private boolean randomize() {
        return Math.random() > 0.5;
    }

}


