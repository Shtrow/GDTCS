
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
		cache.put(ip, user);
		return true;
	}

	public synchronized void addIp(String user, String ip) {
		cache.put(ip, user);
	}

	public synchronized String initNewToken(String user) {
		String token = buildToken(user);
		users.put(user, token);
		return token;
	}

	public synchronized void removeUser(String user) {
		Enumeration<String> cacheKeys = cache.keys();
		while (cacheKeys.hasMoreElements()) {
			String ip = cacheKeys.nextElement();
			if (cache.get(ip).equals(user)) {
				cache.remove(ip);
				return;
			}
		}
	}

	public synchronized void ereaseUser(String user) {
		removeUser(user);
		users.remove(user);
	}

	public synchronized String getToken(String user) {
		return users.get(user);
	}

	public synchronized String getUserFromIp(String ip) {
		return cache.get(ip);
	}

	public String getUserFromToken(String token) {
		Enumeration<String> userKeys = users.keys();
		while (userKeys.hasMoreElements()) {
			String user = userKeys.nextElement();
			if (users.get(user).equals(token)) {
				return user;
			}
		}
		return null;
	}

	public boolean isValidToken(String token) {
		Enumeration<String> userKeys = users.keys();
		while (userKeys.hasMoreElements()) {
			String currentUser = userKeys.nextElement();
			if (users.get(currentUser).equals(token)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidUser(String user) {
		return users.containsKey(user);
	}
}
