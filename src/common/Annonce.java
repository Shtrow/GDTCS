
package common;

public class Annonce {
	private String idAnnonce;
	private String domaine;
	private final String user;
	private String titre;
	private String descriptif;
	private String prix;

	public Annonce(String user, String domaine, String titre, String descriptif, String prix) {
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
}
