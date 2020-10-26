
package common;

public class Annonce {
	private String idAnnonce;
	private String domaine;
	private final String user;
	private String titre;
	private String descriptif;
	private String prix;

	public Annonce(String user, String domaine, String titre, String descriptif, String prix) {
		if(user == null || domaine == null || titre == null || descriptif == null || prix == null) {
			throw new IllegalArgumentException("Annonce must be full");
		}
		this.domaine = domaine;
		this.user = user;
		this.titre = titre;
		this.descriptif = descriptif;
		this.prix = prix;
		this.idAnnonce = user + this.hashCode();
	}

	public String getUser() {
		return this.user;
	}

	public String getDomaine() {
		return this.domaine;
	}

	public void setDomaine(String domaine) {
		if(domaine != null) {
			this.domaine = domaine;
		}
	}

	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		if(titre != null) {
			this.titre = titre;
		}
	}

	public String getDescriptif() {
		return this.descriptif;
	}

	public void setDescriptif(String descriptif) {
		if (descriptif != null) {
			this.descriptif = descriptif;
		}
	}

	public String getPrix() {
		return this.prix;
	}

	public void setPrix(String prix) {
		if(prix != null) {
			this.prix = prix;
		}
	}

	public String getId() {
		return this.idAnnonce;
	}

	public String[] toStringArgs() {
		String[] args = new String[5];
		args[0] = getId();
		args[1] = getDomaine();
		args[2] = getTitre();
		args[3] = getDescriptif();
		args[4] = getPrix();
		return args;
	}

	public static Annonce fromStringArgs(String user, String []args) {
		if(args.length != 4) {
			return null;
		} else {
			try {
				return new Annonce(user, args[0], args[1], args[2], args[3]);
			} catch(IllegalArgumentException e) {
				return null;
			}
		}
	}

	private String filterString(String str) {
		return (str.equals("null")) ? null : str;
	}

	public boolean updateWithArgs(String[] args) {
		if(args.length != 5) {
			return false;
		}
		setDomaine(filterString(args[1]));
		setTitre(filterString(args[2]));
		setDescriptif(filterString(args[3]));
		setPrix(filterString(args[4]));
		return true;
	}
}
