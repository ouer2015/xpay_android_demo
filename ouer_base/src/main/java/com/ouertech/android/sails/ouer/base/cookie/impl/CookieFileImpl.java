package com.ouertech.android.sails.ouer.base.cookie.impl;


import com.ouertech.android.sails.ouer.base.cookie.naming.FileNameGenerator;

/**
 * 
 * @author bluestome
 *
 */
public class CookieFileImpl extends BaseCookieImpl {

	private String root;
	private FileNameGenerator fileNameGenerator;
	
	/**
	 * 
	 * @param root
	 * @param fileNameGenerator
	 * @deprecated this class is not complete
	 */
	@Deprecated
    private CookieFileImpl(String root,FileNameGenerator fileNameGenerator){
		this.root = root;
		this.fileNameGenerator = fileNameGenerator;
	}
	
	@Override
	public void save(String url, String cookie) {
	}

	@Override
	public String get(String url) {
		return null;
	}

}
