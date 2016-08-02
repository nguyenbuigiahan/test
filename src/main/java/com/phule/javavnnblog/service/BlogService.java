/**
 * 
 */
package com.phule.javavnnblog.service;

import java.util.ArrayList;

import com.phule.javavnnblog.model.Authentication;
import com.phule.javavnnblog.model.Config;
import com.phule.javavnnblog.model.SSHInfo;

/**
 * @author MrPhu
 *
 */

public interface BlogService {
	void insertSSH(SSHInfo ssh);
	ArrayList<Authentication> getAllAuthentication();
	Config getConfig();
}
