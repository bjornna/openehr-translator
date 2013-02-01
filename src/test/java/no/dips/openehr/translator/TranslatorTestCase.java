package no.dips.openehr.translator;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.rm.common.resource.ResourceDescriptionItem;
import no.dips.openehr.translator.OpenEHRADLParser;

public class TranslatorTestCase {

	@Test
	public void test() {
		String filename = "adl/openEHR-EHR-EVALUATION.ipss_score.v1.adl";
		OpenEHRADLParser parser = new OpenEHRADLParser();
		Archetype archetype = parser.getArchetype(TranslatorTestCase.class.getResourceAsStream("/"
				+ filename));
		assertNotNull(archetype);
		Archetype resultArchetype = translate(archetype);
		ResourceDescriptionItem item = getResourceDescriptionItem(resultArchetype, "no");
		assertNotNull("ResourceDescriptionItem is null", item);
		validateItem(item);

	}
	public void testIPPSScore(){
		String filename = "adl/openEHR-EHR-EVALUATION.ipss_score.v1.adl";
	}

	protected Archetype translate(Archetype archetype) {
		Translator translator = new TranslatorBean(archetype);
		return translator.translate("nb", "no");
	}

	private void validateItem(ResourceDescriptionItem item) {
		checkStar("use", item.getUse());
		checkStar("misuse", item.getMisuse());
		for (String keyword : item.getKeywords()) {
			checkStar("keyword", keyword);
		}
		checkStar("purpose", item.getPurpose());
		checkStar("copyright", item.getCopyright());

	}

	private void checkStar(String string, String misuse) {
		if (misuse == null) {
			System.out.println("Property: " + string + " is null");
		} else {
			assertFalse(string + " contains *", misuse.contains("*"));
		}
	}

	private ResourceDescriptionItem getResourceDescriptionItem(Archetype a, String string) {
		List<ResourceDescriptionItem> details = a.getDescription().getDetails();
		for (ResourceDescriptionItem item : details) {
			if (string.equals(item.getLanguage().getCodeString())) {
				return item;
			}
		}
		fail("Did not find any language like: " + string);
		return null;
	}

}
