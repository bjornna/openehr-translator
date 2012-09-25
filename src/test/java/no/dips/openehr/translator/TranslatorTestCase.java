package no.dips.openehr.translator;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.rm.common.resource.ResourceDescriptionItem;

public class TranslatorTestCase {

	@Test
	public void test() {
		String filename = "adl/openEHR-EHR-EVALUATION.ipss_score.v1.adl";
		OpenEHRADLParser parser = new OpenEHRADLParser();
		Archetype archetype = parser.getArchetype(TranslatorTestCase.class.getResourceAsStream("/"
				+ filename));
		assertNotNull(archetype);
		
		
		Translator translator = new TranslatorBean(archetype);
		Archetype archetype2 = translator.translate("nb", "no");
		ResourceDescriptionItem item = getResourceDescriptionItem(archetype2, "no");
		System.out.println("Resource is now: ");
		assertFalse("Use contains * ", item.getUse().contains("*"));
		assertFalse("Misuse contains *", item.getMisuse().contains("*"));

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
