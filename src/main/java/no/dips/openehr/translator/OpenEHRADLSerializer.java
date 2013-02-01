package no.dips.openehr.translator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.openehr.am.archetype.Archetype;
import org.openehr.am.serialize.ADLSerializer;

public class OpenEHRADLSerializer {

	public void write(Archetype archetype, String filename) {
		ADLSerializer serializer = new ADLSerializer();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
			serializer.output(archetype, fos);
		} catch (FileNotFoundException e) {
			throw new ArcheTypeException(e);
		} catch (IOException e) {
			throw new ArcheTypeException(e);
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fos = null;

			}
		}
	}
}
