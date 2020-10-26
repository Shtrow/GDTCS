package common;

import common.Annonce;
import common.Domaine;

import java.util.TreeMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.LinkedList;

public class StorageAnnonce {
	private TreeMap<Domaine.DomaineType, Hashtable<String, Annonce>> storage;
	private static StorageAnnonce singleton = null;

	private StorageAnnonce (){
		storage = new TreeMap<Domaine.DomaineType, Hashtable<String, Annonce>>();
		for(Domaine.DomaineType d : Domaine.DomaineType.values()) {
			storage.put(d, new Hashtable<String, Annonce>(256));
		}

	}

	public static  synchronized StorageAnnonce build() {
		if(singleton == null) {
			singleton = new StorageAnnonce();
		}
		return singleton;
	}

	public synchronized Annonce findWithDomain(String domaine, String idAnnonce) {
		Domaine.DomaineType d = Domaine.fromString(domaine);
		if(storage.containsKey(d)) {
			return storage.get(d).get(idAnnonce);
		}
		return null;
	}

	public synchronized Annonce find(String idAnnonce) {
		for(Hashtable<String, Annonce> index : storage.values()) {
			Annonce anc = index.get(idAnnonce);
			if(anc != null) {
				return anc;
			}
		}
		return null;
	}

	public synchronized boolean addAnnonce(Domaine.DomaineType d, Annonce anc) {
		if(storage.containsKey(d)) {
			storage.get(d).put(anc.getId(), anc);
			return true;
		}
		return false;
	}

	public synchronized boolean deleteAnnonce(Annonce anc) {
		if(storage.containsKey(anc.getDomaine())) {
			storage.get(anc.getDomaine()).remove(anc.getId());
		}
		return false;
	}

	public synchronized String[] getDomaines() {
		Set<Domaine.DomaineType> domaines = storage.keySet();
		String[] args = new String[domaines.size()];
		int i = 0;
		for(Domaine.DomaineType domaine : domaines) {
			args[i] = domaine.toString();
		}
		return args;
	}

	public synchronized String[] getUserAnc(String user) {
		LinkedList<Annonce> userAnc = new LinkedList<Annonce>();
		int length = 0;
		for(Hashtable<String, Annonce> domaine : storage.values()) {
			for(Annonce anc : domaine.values()) {
				if(anc.getUser().equals(user)) {
					userAnc.push(anc);
					length++;
				}
			}
		}
		String[] args = new String[length*5];
		for(int i = 0 ; i < length ; i=i+5) {
			String[] argsAnc = userAnc.pop().toStringArgs();
			args[i] = argsAnc[0];
			args[i+1] = argsAnc[1];
			args[i+2] = argsAnc[2];
			args[i+3] = argsAnc[3];
			args[i+4] = argsAnc[4];
		}
		return args;
	}
}
