package no.dips.openehr.translator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



import org.openehr.am.archetype.Archetype;
import org.openehr.am.serialize.ADLSerializer;

public class OpenEHRADLSerializer {

	public void write(Archetype archetype, String filename) {
		ADLSerializer serializer = new ADLSerializer();
		try {
			serializer.output(archetype, new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			throw new ArcheTypeException(e);
		} catch (IOException e) {
			throw new ArcheTypeException(e);
		}
	}
}
