package no.dips.openehr.translator;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openehr.am.archetype.Archetype;

public class TranslatorUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1112533957216823305L;
	private JLabel fromLangLabel;
	private JLabel toLangLabel;
	private JTextField fromLangField;
	private JTextField toLangField;;
	private JButton btTranslate;
	private JButton btSelectFile;
	private File file;
	private Translator translator;
	final JFileChooser fc = new JFileChooser();
	private File currentDir;

	public TranslatorUI() {
		super("ADL Translator");
		initUI();
	}

	private void initUI() {
		currentDir = new File("C:\\Users\\bna\\openEHR\\archetypes_git");
		fromLangLabel = new JLabel("From language:");
		toLangLabel = new JLabel("To language");
		fromLangField = new JTextField();
		fromLangField.setText("nb");
		toLangField = new JTextField("no");
		btSelectFile = new JButton("Select file");

		btSelectFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				fc.setCurrentDirectory(currentDir);
				int retVal = fc.showOpenDialog(TranslatorUI.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					currentDir = file.getParentFile();
					translator = new TranslatorBean(file);
					btTranslate.setEnabled(true);

				}

			}
		});
		btTranslate = new JButton("Translate");
		btTranslate.setEnabled(false);
		btTranslate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					String from = fromLangField.getText();
					String to = toLangField.getText();
					Archetype archetypeResult = translator.translate(from, to);
					TranslatorSaver saver = new TranslatorSaver(file, archetypeResult);
					saver.save();
				} catch (ArcheTypeException e) {
					showError(e);

				} catch (TranslatorSaverException e) {
					showError(e);
				}

			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		panel.add(fromLangLabel);
		panel.add(fromLangField);
		panel.add(toLangLabel);
		panel.add(toLangField);
		panel.add(btSelectFile);
		panel.add(btTranslate);
		add(panel);
		pack();
		// setSize(800, 600);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
	}

	protected void showError(Exception e) {
		showMessageDialog(this, e);

	}

	public static void main(String[] args) {

		TranslatorUI translatorUI = new TranslatorUI();

	}

}
