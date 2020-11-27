---
title: Good Duck Transfert - Sécurité
author: Étienne Marais - Benjamin Viau
geometry: margin=3cm
---

# Sécurité

## Intégrité et authenticité

Pour s'assurer de l'identité de l'émetteur et du récepteur nous pouvons utiliser le système de HMAC.
Cela consiste à prendre une empreinte du message que l'on va envoyer avec un token qui nous identifie.
Le token est présent de chacun des côtés de la connexion et permet de signer les messages.
Lorsque l'on reçoit le message, il suffit de calculer l'empreinte à nouveau et de s'assurer que c'est la même.
Ainsi, si elle diffère, nous savons que soit le message a été modifié, soit l'utilisateur n'est pas le bon.
Le "." dans le protocole permet de faire la distinction entre la partie HMAC et le reste.

On procède ainsi. Le destinataire calcule:

```
M =
+--------------+
| HEADER       |
| CONTENT      |  =>  HMAC(token destinataire, M) => hash de M
| .            |
+--------------+

```

Ensuite, il concatène le hash de M produit avec le message:

```
+--------------+
| HEADER       |
| CONTENT      |
| .            |
| hash de M    |
+--------------+

```

Le message est envoyé de façon sécurisée (voir partie en dessous). Le récepteur commence par séparer
le contenu du hash. Il calcule alors :

```
M' =
+--------------+
| HEADER       |
| CONTENT      |  =>  HMAC(token destinataire, M') => hash de M'
| .            |
+--------------+

```

Si le `hash de M` est différent du `hash de M'` alors on sait que soit le message a été altéré soit
la personne avec qui on communique n'a pas le bon `token.`


## Protocole Client-Serveur

Pour sécuriser le protocole Client-Serveur et assurer la confidentialité, nous pouvons utiliser les sockets sécurisées
fournies par TCP à travers le protocole TLS (anciennement SSL) cela nous assure la confidentialité, l'authenticité et
l'intégrité à l'échelle des sockets et HMAC l'intégrité et l'authenticité à l'échelle de l'application.
TLS/SLL fonctionne en utilisant le protocole RSA pour sécuriser l'échange de clef. Ensuite, il utilise un protocole de chiffrement
symétrique pour assurer de la confidentialité de l'échange. Nous sommes donc bien sécurisés.


## Protocole Client-Client

Le protocole Pair-à-Pair requiert le passage par un serveur tierce pour faire l'échange des clefs.
Lorsque l'on demande l'ip, on fait une demande au serveur, qui contacte les deux paires pour leur
indiquer les clefs qui vont être utilisées lors de l'échange. Une fois cela fait, les deux pairs
peuvent chiffrer l'échange de la clef symétrique. Cela nous donne la confidentialité et HMAC nous
donne à nouveau l'intégrité et l'authenticité.


Le protocole est le suivant:

1. Le client _A_ fait la demande au serveur pour une connexion avec _B_ en transmettant sa
clef publique et le serveur transmet la demande à _B_.
```

 Client A                                    Client B
+---------+                                +---------+
| Token A |                                | Token B |
+---------+                                +---------+
    |                                          ^
    | Demande une connexion                    | Transmet la demande avec
    | avec la clef de A                        | la clef publique de A
    |               +-------------+            |
    +-------------->| Index token |------------+
                    +-------------+
                         Server
```

2. _B_ indique au serveur s'il est d'accord ou non. Dans le cas d'une réponse positive,
il transmet aussi sa clef publique au serveur qui transmet la réponse à _A_.
```

 Client A                                    Client B
+---------+                                +---------+
| Token A |                                | Token B |
+---------+                                +---------+
    ^                                          |
    | Reçoit la clef publique                  | Envoie sa clef publique
    | de B                                     | à B
    |               +-------------+            |
    +---------------| Index token |<-----------+
                    +-------------+
                         Server
                         
                         
```

3. _A_ contacte _B_ en UDP en chiffrant le message avec la clef publique de _B_.
Le message contient un token pour discuter et la clef symétrique à utiliser
pour les futurs échanges. _B_ répond par l'affirmative en chiffrant le message
avec la clef publique de _A_. Il ajoute aussi le token qui servira à authentifier
les messages avec HMAC et la clef que _B_ lui a envoyée pour confirmer.
```

 Client A                                    Client B
+---------+  Chiffré avec clef de B          +---------+
|         | -------------------------------> |         |
| Token A |                                  | Token B |
|         | <------------------------------- |         |
+---------+  Chiffré  avec la clef de A      +---------+
```

4. À partir de là, ils se mettent à discuter en chiffrant les messages avec
la clef symétrique et en utilisant le token pour s'authentifier avec HMAC
```

 Client A                                    Client B
+---------+                                +---------+
| Token A |   Messages chiffrés avec C     | Token B |
| Clef C  |<------------------------------>| Clef C  |
+---------+                                +---------+
```

Nous sommes donc sécurisés.
