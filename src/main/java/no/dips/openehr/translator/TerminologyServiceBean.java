package no.dips.openehr.translator;

import java.util.List;
import java.util.Map;

import org.openehr.rm.support.terminology.CodeSetAccess;
import org.openehr.rm.support.terminology.OpenEHRCodeSetIdentifiers;
import org.openehr.rm.support.terminology.TerminologyAccess;
import org.openehr.rm.support.terminology.TerminologyService;

public class TerminologyServiceBean implements TerminologyService {
	CodeSetAccess codeSetAccess;

	public TerminologyServiceBean(CodeSetAccess codeSetAccess) {
		this.codeSetAccess = codeSetAccess;
	}

	@Override
	public TerminologyAccess terminology(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSetAccess codeSet(String name) {
		// TODO Auto-generated method stub
		return codeSetAccess;
	}

	@Override
	public CodeSetAccess codeSetForId(OpenEHRCodeSetIdentifiers id) {
		return codeSetAccess;
	}

	@Override
	public boolean hasTerminology(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCodeSet(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> terminologyIdentifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> codeSetIdentifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> openehrCodeSets() {
		// TODO Auto-generated method stub
		return null;
	}

}
