package com.youngo.mobile.controller.translate;

import java.util.List;

import com.youngo.mobile.model.google.Translation;

public interface TranslateService {
	
	@Deprecated
	public List<Translation> translate(String[] sources, String target);
	
	public Translation translate(String source, String tl);
	
	public Translation translate(String source, String tl, String sl);
}
