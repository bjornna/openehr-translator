package no.dips.openehr.translator;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

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
	private JLabel fileToTranslateLabel;

	private File file;
	private Translator translator;
	final JFileChooser fc = new JFileChooser();
	private File currentDir;

	private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TranslatorUI.class);

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
		fileToTranslateLabel = new JLabel("No file to translate - drag file here");
		fileToTranslateLabel.setHorizontalAlignment(SwingConstants.CENTER);

		btSelectFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				fc.setCurrentDirectory(currentDir);
				int retVal = fc.showOpenDialog(TranslatorUI.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					addFileToTranslate(fc.getSelectedFile());

				}

			}
		});
		btTranslate = new JButton("Translate");
		btTranslate.setEnabled(false);
		btTranslate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					String txt = fileToTranslateLabel.getText();
					String from = fromLangField.getText();
					String to = toLangField.getText();
					fileToTranslateLabel.setText(txt + " - start translating");
					log.info("Translate from '" + from + "' to '" + to + "' file = " + file);
					Archetype archetypeResult = translator.translate(from, to);
					fileToTranslateLabel.setText(txt + " - finished translating");
					TranslatorSaver saver = new TranslatorSaver(file, archetypeResult);
					saver.save();
					fileToTranslateLabel.setText(txt + " - translated and saved");
				} catch (ArcheTypeException e) {
					showError(e);

				} catch (TranslatorSaverException e) {
					showError(e);
				} catch (RuntimeException e) {
					showError(e);
				}

				log.info("");
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		panel.add(fromLangLabel);
		panel.add(fromLangField);
		panel.add(toLangLabel);
		panel.add(toLangField);

		JPanel southPanel = new JPanel();
		southPanel.add(btSelectFile);
		southPanel.add(btTranslate);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(panel, BorderLayout.NORTH);
		mainPanel.add(fileToTranslateLabel, BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);

		add(mainPanel);
		setTransferHandler(handler);
		pack();

		setSize(600, 200);
		setVisible(true);
		// setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		log.info("Ready");
	}

	protected void addFileToTranslate(File selectedFile) {
		try {
			file = selectedFile;
			currentDir = file.getParentFile();
			translator = new TranslatorBean(file);
			btTranslate.setEnabled(true);
			log.info("File to translate: " + file.getName());
			fileToTranslateLabel.setText(file.getName());
			super.setTitle("ADL Translator: " + file.getName());
		} catch (ArcheTypeException e) {
			String message = "Feil ved lasting av arketype!\n\n " + e.getMessage() + "\n\n. Valgt fil: " + selectedFile;
			JOptionPane.showMessageDialog(this, message, e.getMessage(), JOptionPane.ERROR_MESSAGE);
		} catch (RuntimeException e) {
			String message = "Ooops! Could not read file: " + selectedFile + ", reason:" + e;
			JOptionPane.showMessageDialog(this, message, e.getMessage(), JOptionPane.ERROR_MESSAGE);
			log.error(e);
		}

	}

	protected void showError(Exception e) {
		log.warn(e.getMessage(), e);
		showMessageDialog(this, e);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				TranslatorUI translatorUI = new TranslatorUI();

			}
		});

	}

	private final TransferHandler handler = new TransferHandler() {
		public boolean canImport(TransferHandler.TransferSupport support) {

			if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				log.debug("Can NOT import: " + support);
				return false;
			}
			support.setDropAction(COPY);
			log.debug("Can import: " + support);

			return true;
		}

		public boolean importData(TransferHandler.TransferSupport support) {
			if (!canImport(support)) {
				log.debug("Cannot import");
				return false;
			}

			Transferable t = support.getTransferable();

			try {

				java.util.List<File> l = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
				if (l.size() != 1) {
					fileToTranslateLabel.setText("NB! - you can only drag one file in this version");
				} else {

					addFileToTranslate(l.get(0));

				}
			} catch (UnsupportedFlavorException e) {
				return false;
			} catch (IOException e) {
				return false;
			}

			return true;
		}
	};

}
