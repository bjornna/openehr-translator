package no.dips.openehr.translator;

import java.util.Set;

import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.terminology.CodeSetAccess;

public class CodeSetAccessBean implements CodeSetAccess {

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<CodePhrase> allCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCode(CodePhrase code) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasLang(CodePhrase lang) {
		// TODO Auto-generated method stub
		return false;
	}

}
