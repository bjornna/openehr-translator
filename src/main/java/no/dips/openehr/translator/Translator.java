package no.dips.openehr.translator;

import org.openehr.am.archetype.Archetype;

public interface Translator {
	
	public Archetype translate(String from, String to);

}
