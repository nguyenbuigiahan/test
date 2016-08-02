/**
 * 
 */
package com.phule.javavnnblog.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.phule.javavnnblog.model.Authentication;
import com.phule.javavnnblog.model.Config;
import com.phule.javavnnblog.model.SSHInfo;
import com.phule.javavnnblog.repository.BlogRepository;

/**
 * @author MrPhu
 *
 */
@Repository
public class BlogRepositoryImpl implements BlogRepository {
	@Autowired
	private DataSource dataSource;

	public void insertSSH(SSHInfo ssh) {
		String sql = "INSERT INTO ShellSH VALUES('"+ssh.getId()+"','"+ssh.getHost()+"','"+ssh.getUsername()+"','"+ssh.getPassword()+"'"+")";
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		
	}

	public ArrayList<Authentication> getAllAuthentication() {
		ArrayList<Authentication> authens = new ArrayList<Authentication>();
		String sql = "SELECT * FROM AUTHENTICATION";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Authentication authen = new Authentication();
				authen.setUsername(rs.getString(2));
				authen.setPassword(rs.getString(3));
				authens.add(authen);
			}
			rs.close();
			ps.close();
			return authens;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public Config getConfig() {
		ArrayList<Config> configs = new ArrayList<Config>();
		String sql = "SELECT * FROM CONFIG";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Config config = new Config();
				config.setTimeout(rs.getInt(2));
				config.setThreads(rs.getInt(3));
				config.setTimoutPort(rs.getInt(4));
				configs.add(config);
			}
			rs.close();
			ps.close();
			return configs.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

}
