package no.dips.openehr.translator.lang;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import no.dips.openehr.translator.OpenEHRADLParser;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehr.am.archetype.Archetype;

public class LangExtractTestCase {
	private String path = "C:/Users/bna/Documents/My Clinical Models/ClinicalModels.DEMO/Archetypes/";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	//@Test
	public void test() throws FileNotFoundException {
		String file = path + "/cluster/openEHR-EHR-CLUSTER.funn.v1.adl";
		OpenEHRADLParser parser = new OpenEHRADLParser();
		Archetype archetype = parser.getArchetype(new FileInputStream(new File(
				file)));
		LanguageExtract languageExtract = new LanguageExtract(archetype);
		String result = languageExtract.load();
		System.out.println("RESULT:\n\n");
		System.out.println(result);
	}

}
