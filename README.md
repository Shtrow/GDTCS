# Good Duck Transfert Client and Server

## Auteurs
* Etienne Marais
* Benjamin Viau

## Sommaire

 - Protocole
 - Prérequis
 - Informations sur le manuel
 - Compilation
    - Serveur
    - Client
 - Fonctionnement
    - Serveur
    - Client
 - Détails l'implémentation
  - Serveur
  - Client

## Protocole
Le répertoire suivant contient le code qui implémente le protocole GDPT pour le côté serveur et pour le côté client.
Le fichier contenant le procole se trouve ici: [docs/rfc.txt](docs/rfc.txt).

## Prérequis

Votre système doit posséder les outils suivant installés:
- Java 11 ~openjdk11
- Make

## Informations sur la lecture du manuel

Sur le manuel, nous allons distinguer le terminal de l'invite de commandes du client. Nous utiliserons les symboles suivants
pour faire la distinction:
1. Pour le shell

```sh
 $ <command>
```

2. Pour l'invite de commande du client
```
>> <command>
```

## Compilation

Pour compiler, il faut se placer à la racine du dossier, là où se trouve le **Makefile**.


### Serveur

Pour compiler et lancer le serveur sur le port **1027** :
```sh
  $ make server
```
Pour le lancer sur le un autre port:
```sh
  $ make server SERVER_PORT=<port>
```

### Client

Pour compiler et lancer le client sur le port **1027** et sur l'adresse **127.0.0.1** :
```sh
 $ make client
```
Pour lancer le client sur une autre adresse et sur un autre port, il faut lancer avec:
```sh
 $ make client GTDP_addr=<addr> GDTP_port=<port>
```

Par défaut, le client n'affiche pas les paquets reçus pour permettre une meilleure lisibilité
à l'utilisateur. Il est cependant possible de les voir grâce au paramètre suivant:
```sh
  $ make client DEBUG=yes <options>
```

### Nettoyer le répertoire

Afin d'éliminer les *\*.class* il est possible d'utiliser la commande suivante
```sh
 $ make clean
```

## Fonctionnement

### Serveur

Une fois lancé, le serveur fonctionne de façon autonome et affiche les logs
de son exécution. Il peut être arrêté grace à un **CTRL+C**.

### Client

1. Aide

Pour afficher l'aide dans l'interpréteur, il faut utiliser la commande suivante:
```
>> help
```

2. Connexion:

Le client fonctionne en ligne de commande une fois lancé. Il faut d'abord se connecter
avec un identifiant pour pouvoir effectuer sa première connexion au serveur en utilisant
la commande suivante dans l'interpréteur du client:
```
>> connect [username]
```
Pour les connexions suivantes, vous pouvez simplement effectuer la commande suivante:
```
>> connect
```
En effet, un token est créé et ajouté au répertoire *\$HOME* lors de la première connexion
dans **\$HOME/.config/gdtp/token**. Si vous voulez changer d'utilisateur, il faut refaire
la manipulation avec le connect.

3. Quitter:

Pour quitter le logiciel et vous déconnectez, vous pouvez utiliser la commande suivante:
```
>> exit
```

4. Domaines

Pour afficher les domaines disponibles sur le serveur, il faut faire la commande suivante:
```
>> domains
```

5. Annonces

Pour obtenir toutes les annonces d'un domaine, il faut utiliser la commande suivante:
```
>> ancs [DOMAINE]
```

6. Propres annonces

Pour obtenir l'ensemble des annonces que vous avez postées, il faut taper la commande suivante:
```
>> own
```

7. Création

Pour poster une nouvelle annonce, il faut lancer l'éditeur intéractif:
```
>> post
```
Le prix s'écrit avec le format suivant :`13.50` correspond à 13.50€

8. Mise à jour

Pour mettre à jour une annonce, il faut écrire:
```
>> update [ID ANNONCE]
```

9. Suppression

Pour supprimer une annonce qui vous appartient du serveur, il faut lancer la commande suivante:
 ```
 >> delete [ID ANNONCE]
 ```

10. Récupération de l'IP

Il est possible de récupérer l'ip d'un collaborateur pour le contacter directement.
La commande à utiliser est la suivante:
```
>> ip [ID ANNONCE]
```

## Détail de l'implémentation
