package no.dips.openehr.translator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;



import org.apache.log4j.Logger;
import org.openehr.am.archetype.Archetype;

public class TranslatorSaver {

	private final File file;
	private final Archetype archetype;
	private File currentDir;
	private File backUpDir;
	private Logger log = Logger.getLogger(TranslatorSaver.class);

	public TranslatorSaver(File file, Archetype archetype) {
		this.file = file.getAbsoluteFile();
		this.archetype = archetype;
		init();

	}

	private void init() {
		if (!file.exists()) {
			throw new TranslatorSaverException("File does not exist; " + file);
		} else {

			currentDir = file.getParentFile();
			backUpDir = new File(currentDir, ".dipsbackup");
		}
	}

	public void save() {
		createBackUpDirIfNotExist();
		moveOriginalToBackUp();
		OpenEHRADLSerializer serializer = new OpenEHRADLSerializer();
		serializer.write(archetype, file.getAbsoluteFile().getAbsolutePath());
	}

	private void createBackUpDirIfNotExist() {
		if (!backUpDir.exists()) {
			boolean created = backUpDir.mkdir();
			if (created) {
				log.info("Created backUpDir: " + backUpDir);
			} else {
				throw new TranslatorSaverException("Could not create backupdir:" + backUpDir);
			}

		} else if (backUpDir.exists()) {
			log.info("Backupdir exist: " + backUpDir);
		}

	}

	private void moveOriginalToBackUp() {
		File destFile = new File(backUpDir, file.getName());
		try {
			copyFileFiles(file, destFile);

		} catch (IOException e) {
			e.printStackTrace();
			throw new TranslatorSaverException("Could not copy original to destfile:" + destFile, e);
		}
	}

	private void copyFileFiles(File sourceFile, File destFile) throws IOException {
		log.info("copy: " + sourceFile + " >>> " + destFile);
		File sFile = new File(sourceFile.getAbsolutePath());
		File dFile = new File(destFile.getAbsolutePath());
		log.info("copy: " + sFile + " >>> " + dFile);
		Path path = Files.copy(sFile.toPath(), dFile.toPath(), StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES);
		log.info("Copied to: " + path);
	}

}
