/*
 * Main class
 */
package client;
import common.Logs;

import java.io.IOException;

public class Client {
	public static void main(String[] args) throws IOException {
		String userName = "Maitre Simonard";
		client.GDTService GDTService =
				new GDTService("192.168.43.62",1027,5);
//			new GDTService();
		GDTService.run();

		DataProvider dataProvider = new DataProvider(GDTService);

    	Controller c = new Controller(dataProvider);

    	c.run();

		System.out.println("Running client [OK]");
	}
}
