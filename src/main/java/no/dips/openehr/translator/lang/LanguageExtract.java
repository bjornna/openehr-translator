package no.dips.openehr.translator.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.ontology.ArchetypeTerm;
import org.openehr.am.archetype.ontology.OntologyBinding;
import org.openehr.am.archetype.ontology.OntologyBindingItem;
import org.openehr.am.archetype.ontology.OntologyDefinitions;
import org.openehr.rm.common.resource.TranslationDetails;

public class LanguageExtract {
	private Logger log = Logger.getLogger(LanguageExtract.class);
	private Archetype archetype;
	private Map<String, OntologyDefinitions> tranlations = new HashMap<String, OntologyDefinitions>();
	private Map<String, List<ArchetypeTerm>> codeMap = new HashMap<>();

	public LanguageExtract(Archetype archetype) {
		this.archetype = archetype;

	}

	public String load() {
		loadLanguage();
		for (Entry<String, OntologyDefinitions> e : tranlations.entrySet()) {
			OntologyDefinitions def = e.getValue();
			String lang = e.getKey();
			for (ArchetypeTerm term : def.getDefinitions()) {
				String code = term.getCode();
				String test = term.getText();
				String descc = term.getDescription();
				log.info(lang + "->" + code + test);
				add(code, term);
			}
		}
		return createExport();
	}

	private String delim = ";";
	private String newline = "\n";

	private String createExport() {
		List<String> langs = this.findLanguages();
		StringBuffer buffer = new StringBuffer();
		buffer.append("code" + delim);
		
		StringBuffer lines = new StringBuffer();
		for (Entry<String, List<ArchetypeTerm>> entry : codeMap.entrySet()) {
			String code = entry.getKey();
			buffer.append(code + delim);
			for(ArchetypeTerm term: entry.getValue()){
				String text =  term.getText();
				buffer.append(text + delim);
				
			}
			buffer.append(newline);
		}
		return buffer.toString();
	}

	private void add(String code, ArchetypeTerm term) {
		if (codeMap.get(code) != null) {
			codeMap.get(code).add(term);
		} else {
			List<ArchetypeTerm> terms = new ArrayList<>();
			terms.add(term);
			codeMap.put(code, terms);
		}
	}

	private void loadLanguage() {
		List<OntologyDefinitions> terms = archetype.getOntology()
				.getTermDefinitionsList();
		for (OntologyDefinitions binding : terms) {
			String lan = binding.getLanguage();
			tranlations.put(lan, binding);
		}
	}

	private List<String> findLanguages() {
		Map<String, TranslationDetails> t = archetype.getTranslations();
		List<String> languages = new ArrayList<>();
		for (Entry<String, TranslationDetails> entry : t.entrySet()) {
			String lang = entry.getKey();
			String codestring = entry.getValue().getLanguage().getCodeString();
			log.info("Lang:" + lang + ", codestring: " + codestring);
			languages.add(lang);
		}
		return languages;
	}
}
