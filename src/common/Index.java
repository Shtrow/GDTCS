
/**
 * Class to describe users
 *
 * It's a singleton class to avoid duplication.
 *
 * @author Marais-Viau
 */

package common;

import java.util.Hashtable;
import java.util.Enumeration;

public class Index {
	private static Index index = null;
	private Hashtable<String, String> users = null;
	private Hashtable<String, String> cache = null;

	private Index() {
		cache = new Hashtable<>(256);
		users = new Hashtable<>(256);
	}

	public synchronized static Index getIndex() {
		if (index != null) {
			return index;
		} else {
			index = new Index();
			return index;
		}
	}

	private synchronized String buildToken(String user) {
		String token = user + user.hashCode();
		while (users.contains(token)) {
			token = user + user.hashCode();
		}
		return token;
	}

	public synchronized boolean addUser(String user, String ip) {
		if (users.containsKey(user))
			return false;
		String token = buildToken(user);
		users.put(user, token);
		cache.put(user, ip);
		return true;
	}

	public synchronized void updateIp(String user, String ip) {
		cache.put(user, ip);
	}

	public synchronized String initNewToken(String user) {
		String token = buildToken(user);
		users.put(user, token);
		return token;
	}

	public synchronized void removeUser(String user) {
		cache.remove(user);
	}

	public synchronized void ereaseUser(String user) {
		removeUser(user);
		users.remove(user);
	}

	public synchronized String getToken(String user) {
		return users.get(user);
	}

	public synchronized String getIpFromUser(String user) {
		return cache.get(user);
	}

	public synchronized String getUserFromIp(String ip) {
		Enumeration<String> userKeys = cache.keys();
		while (userKeys.hasMoreElements()) {
			String user = userKeys.nextElement();
			if (cache.get(user).equals(ip)) {
				return user;
			}
		}
		return null;

	}

	public synchronized String getUserFromToken(String token) {
		Enumeration<String> userKeys = users.keys();
		while (userKeys.hasMoreElements()) {
			String user = userKeys.nextElement();
			if (users.get(user).equals(token)) {
				return user;
			}
		}
		return null;
	}

	public synchronized boolean isValidToken(String token) {
		Enumeration<String> userKeys = users.keys();
		while (userKeys.hasMoreElements()) {
			String currentUser = userKeys.nextElement();
			if (users.get(currentUser).equals(token)) {
				return true;
			}
		}
		return false;
	}

	public synchronized boolean isValidUser(String user) {
		return users.containsKey(user);
	}
}
