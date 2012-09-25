package no.dips.openehr.translator;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

/**
 * Hello world!
 * 
 */
public class App {
	private static final Logger log = Logger.getLogger(App.class);

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {

			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				log.info("Create a new TranslatorUI");
				TranslatorUI translatorUI = new TranslatorUI();

			}
		});
	};
}
