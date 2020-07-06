public class ServerApp {
	public static void main(String[] args) {

		final int port = 1500; // статический порт сервера

		Server server = new Server(port);
		server.start(); // запуск сервера
	}
}
