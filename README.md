[![Build Status](https://travis-ci.org/daloji/blockchain.svg?branch=master)](https://travis-ci.org/daloji/blockchain) 
[![Coverage Status](https://coveralls.io/repos/github/daloji/blockchain/badge.svg)](https://coveralls.io/github/daloji/blockchain)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/65c0fe9214ff4f0296102967274ef846)](https://www.codacy.com/manual/daloji/blockchain?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=daloji/blockchain&amp;utm_campaign=Badge_Grade)


# Implémentation du protocole Bitcoin en Java

L'objectif de ce projet est la vulgarisation de la blockchain. C'est purement a titre éducatif, cependant l'application peut se connecter au reseau bitcoin en tant que pair (peer).
Le noeud crée se connecte en mode noeud complet (Full Node) .......

## prérequis
 * Java 8+ maven 3.6.2
 * Level-DB 

## Lancement
```java
# mvn clean install
java -jar target/blockchain.jar
```
## Blockchain 

## Transaction

## Protocole peer to peer
 Le réseau bitcoin a une architecture de réseau pair-à-pair (peer to peer). Les noeuds du réseau s'interconnectent en réseau maillé, les noeuds fournissent et consomment des services a la fois.
 Au demarrage le noeud doit s'annoncer au réseau. Pour cela il doit trouver la liste des noeuds bitcoins 
 * Le DNS lookUp se fait via l'adresse seed.bitcoin.sipa.be qui est une sorte de DNS des noeuds bitcoins gérée par bitcoin.org 
 
  ### protocole d'echange entre pair
  Comme une trame IP , les messages envoyés ont un format bien defini par le protocole (cf https://en.bitcoin.it). Tous les messages bitcoins doivent avoir un entête de ce type
  
     #### Trame Header
 
     | Taille(octet) |     Nom  |   Type    |                             Description                    |
     | ------------ :| --------:| --------: | ---------------------------------------------------:       | 
     | 4             |  MAGIC   |char[4]    |         Valeur Magic                                       |
     | 12            | COMMANDE |char[12]   |chaine ASCII  représentant le type de paquet                |
     | 4             | SIZE     |uint32_t   |taille en octet de la charge utile (payload)                |
     | 4             | CHECKSUM |uchar[4]   |les 4 premiers octet de la double sha256 sha256(sha256(payload))|
   
  Notons qu'il a trois types de réseaux Bitcoins qui sont  MAINNET , TESTNET , REGTEST. le protocole est le même ,seulement la donnée (Magic) de l'entête change pour chaque reseau. le bloc genesis de la blockchaine de chaque réseau n'est pas le même. TESTNET et REGTEST sont utilisés pour le developpement , les bitcoins de ses reseaux ne valent rien du tout.
   
     #### type de Valeur  MAGIC possible
     | reseau   |  Port utilisé  | valeur hexa |         
     | ------ : | --------------:| --------: | 
     | Mainnet  |  	8333         | f9beb4d9   |   
     | Testnet  |   18333        | 0b110907   |
     | regtest  |  18444         | fabfb5da   |

  
  
  Pour etablir une connexion, le noeud doit envoyer un message Version qui annonce la version du protocole bitcoin qu'il supporte ainsi que la longueur de sa blockchain en local, Dans le cas d'un premier demarrage la longueur est 0  (analogie avec tcp SYN / SYNACK)
  Dans la suite on ne va plus reproduire l'entête
  
      ####  Trame Version 
      
     | Taille(octet) |     Nom      |   Type    |     Description                    |
     | ------------ :| ------------:| --------: | ---------------------------:       | 
     | 4             | VERSION      |int32_t    |  version du protocole utilisée     |
     | 8             | SERVICE      |uint64_t   | numero du service                  |
     | 8             | TIMESTAMP    |int64_t    | epoch unix                         |
     | 26            | ADDR_RECEIVE |net_addr	  | adresse de l'envoyeur              |
     | 26            | ADDR_FROM    |net_addr	  | adresse de reception               |
     | 8             | NONCE        |uint64_t   | nombre aleatoire                   |
     | ?             | USER_AGENT   |variable   | user agent                         |
     | 4             | START_HEIGTH |int32_t    | longueur de la blockchaine connue  |
     | 1             | RELAY        |bool       | noeud en mode relais               |
