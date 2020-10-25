package common;

public class Domaine {

	 public static DomaineType fromString(String domaine) {
			switch(domaine) {
				 case "AUTRE":
						return DomaineType.AUTRE;
				 case "LOGICIEL":
						return DomaineType.LOGICIEL;
				 default:
						throw new IllegalArgumentException("Domaine unknown");
			}
	 }

	 public enum DomaineType {
			AUTRE("AUTRE"),
			LOGICIEL("LOGICIEL");

			private String name;

			private DomaineType(String name) {
				 this.name = name;
			}

			@Override
			public String toString() {
				 return this.name;
			}
	 }
}
