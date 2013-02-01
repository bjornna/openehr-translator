package no.dips.openehr.translator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.ontology.ArchetypeOntology;
import org.openehr.am.archetype.ontology.ArchetypeTerm;
import org.openehr.am.archetype.ontology.OntologyDefinitions;
import org.openehr.rm.common.resource.ResourceDescriptionItem;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.terminology.CodeSetAccess;
import org.openehr.rm.support.terminology.TerminologyService;

public class TranslatorBean implements Translator {

	private File file;
	private Logger log = Logger.getLogger(TranslatorBean.class);
	private Archetype archetype;

	public TranslatorBean(File file) {
		this.file = file;

		log.info("Constructor");
		init();
	}

	public TranslatorBean(Archetype archetype) {
		this.archetype = archetype;
	}

	private void init() {
		OpenEHRADLParser parser = new OpenEHRADLParser();
		try {
			archetype = parser.getArchetype(new FileInputStream(file));

		} catch (FileNotFoundException e) {
			throw new ArcheTypeException(e);
		} catch (ArcheTypeException e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public Archetype translate(String from, String to) {
		log.info("Translate from:" + from + ", to:" + to);

		try {
			translateOntology(from, to);
		} catch (RuntimeException e) {
			throw new ArcheTypeException("Could not translate ontology: " + e, e);
		}
		try {
			translatePurpose(from, to);
		} catch (RuntimeException e) {
			throw new ArcheTypeException("Could not translate purpose:" + e, e);
		}
		emit();
		return archetype;

	}

	private void translateOntology(String from, String to) {
		OntologyDefinitions fromDef = getOntologyDefinition(from);
		OntologyDefinitions toDef = getOntologyDefinition(to);
		for (ArchetypeTerm t : toDef.getDefinitions()) {
			log.debug("|---> " + t.getCode() + ":" + t.getText());
		}
		toDef.getDefinitions().clear();
		for (ArchetypeTerm term : fromDef.getDefinitions()) {
			toDef.getDefinitions().add(term);
		}
		for (ArchetypeTerm t : toDef.getDefinitions()) {
			log.debug("|---> " + t.getCode() + ":" + t.getText());
		}

	}

	private void translatePurpose(String from, String to) {
		ResourceDescriptionItem itemFrom = getResourceDescriptionItem(from);
		CodeSetAccess codeSetAccess = new CodeSetAccessBean();
		TerminologyService terminologyService = new TerminologyServiceBean(codeSetAccess);
		ResourceDescriptionItem itemTo = getResourceDescriptionItem(to);
		if(itemTo == null){
			throw new ArcheTypeException("Could not find language " + to + ", are you sure you added it in Archetype Editor");
		}
		final CodePhrase copyLanguage = itemTo.getLanguage();
		final String copyPurpose = itemFrom.getPurpose();
		final List<String> copyKeywords = itemFrom.getKeywords();
		final String copyUse = itemFrom.getUse();
		final String copyMiuse = itemFrom.getMisuse();
		final String copyRight = itemFrom.getCopyright();
		final Map<String, String> copyOrgResourceUri = itemFrom.getOriginalResourceUri();
		final Map<String, String> copyOtherDetails = itemFrom.getOtherDetails();

		ResourceDescriptionItem copy = new ResourceDescriptionItem(copyLanguage, copyPurpose, copyKeywords, copyUse,
				copyMiuse, copyRight, copyOrgResourceUri, copyOtherDetails, terminologyService);
		archetype.getDescription().getDetails().remove(itemTo);
		archetype.getDescription().getDetails().add(copy);

	}

	private ResourceDescriptionItem getResourceDescriptionItem(String lang) {
		List<ResourceDescriptionItem> details = archetype.getDescription().getDetails();
		for (ResourceDescriptionItem item : details) {

			String langCode = item.getLanguage().getCodeString();
			if (lang.equals(langCode)) {
				return item;
			}

		}
		return null;
	}

	private void emit() {
		ArchetypeOntology ontology = archetype.getOntology();
		List<OntologyDefinitions> terms = ontology.getTermDefinitionsList();
		for (OntologyDefinitions def : terms) {
			log.debug("|-->" + def.getLanguage());
			for (ArchetypeTerm term : def.getDefinitions()) {
				log.debug("|---> " + term.getCode() + " : " + term.getText() + " : " + term.getDescription());
			}
		}
	}

	private OntologyDefinitions getOntologyDefinition(String from) {
		ArchetypeOntology ontology = archetype.getOntology();
		List<OntologyDefinitions> terms = ontology.getTermDefinitionsList();
		for (OntologyDefinitions def : terms) {
			log.debug("|-->" + def.getLanguage());
			if (from.equals(def.getLanguage())) {
				log.debug("|--> Hurra - will translate it");
				return def;
			}
		}
		throw new ArcheTypeException("Did not find any ontologydefinition with language" + from);
	}

}
