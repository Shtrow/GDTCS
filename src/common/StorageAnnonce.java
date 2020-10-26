package common;

import common.Annonce;
import common.Domaine;

import java.util.TreeMap;
import java.util.Hashtable;
import java.util.Set;

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
}
