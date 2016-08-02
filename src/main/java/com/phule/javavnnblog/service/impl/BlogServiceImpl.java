/**
 * 
 */
package com.phule.javavnnblog.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phule.javavnnblog.model.Authentication;
import com.phule.javavnnblog.model.Config;
import com.phule.javavnnblog.model.SSHInfo;
import com.phule.javavnnblog.repository.BlogRepository;
import com.phule.javavnnblog.service.BlogService;

/**
 * @author MrPhu
 *
 */
@Service
public class BlogServiceImpl implements BlogService{
	@Autowired
	private BlogRepository blogRepository;


	public void insertSSH(SSHInfo ssh) {
		blogRepository.insertSSH(ssh);
		
	}


	public ArrayList<Authentication> getAllAuthentication() {
		return blogRepository.getAllAuthentication();
	}


	public Config getConfig() {
		return blogRepository.getConfig();
	}
	

}
