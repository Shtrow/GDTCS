package common;

import common.Annonce;
import common.Domaine;

import java.util.LinkedList;
import java.util.Hashtable;

public class StorageAnnonce {
	private Hashtable<DomainType, LinkedList<Annonce>> storage; // TODO correction
	private static StorageAnnonce singleton = null;

	private StorageAnnonce (){
		storage = new Hashtable(128);
	}

	public static StorageAnnonce build() {
		if(singleton == null) {
			singleton = new StorageAnnonce();
		}
		return singleton;
	}

	public Annonce findWithDomain(String Domaine, String idAnnonce) {
		return null;
	}

	public Annonce find(String idAnnonce) {
		return null;
	}

}
