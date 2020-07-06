
public class ClientApp {

	public static void main(String[] args) {

		final int port = 1500; //статический порт для подключения
		final String server = "localhost"; //статический хост для подключения

		Player player = new Player(server, port);

		// Попытка подключения к серверу
		if(!player.start())
			return;

		//Вывод меню для пользователя
		player.printGameMenu();

		//Получение данных от пользователя
		while(player.isPlay) {
			player.move();

			//Если закончился баланс, завершить игру
			if (player.getBalance() <= 0) {
				System.out.println("You run out of money on the balance");
				break;
			}

			System.out.println("\n\n");
		}

		player.disconnect();
	}
}
