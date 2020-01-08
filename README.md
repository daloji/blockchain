[![Build Status](https://travis-ci.org/daloji/blockchain.svg?branch=master)](https://travis-ci.org/daloji/blockchain)


# Implémentation du protocole Bitcoin en Java

L'objectif de ce projet est la vulgarisation de la blockchain. C'est purement a titre éducatif, cependant l'application peut se connecter au reseau bitcoin en tant que pair (peer).
Le noeud crée se connecte en mode noeud complet (Full Node) .......

## prérequis
 
## Blockchain 

## Transaction

## Protocole peer to peer
 Le réseau bitcoin a une architecture de réseau pair-à-pair (peer to peer). Les noeuds du réseau s'interconnectent en réseau maillé, les noeuds fournissent et consomment des services a la fois.
 Au demarrage le noeud doit s'annoncer au réseau.Pour cela il doit trouver la liste des noeuds bitcoins 
 * Le DNS lookUp se fait via l'adresse seed.bitcoin.sipa.be qui est une sorte de DNS des noeuds bitcoins gérée par bitcoin.org 
 
  ### protocole d'echange entre pair
  Comme une trame IP , les messages envoyés ont un format bien defini par le protocole (cf https://en.bitcoin.it). Tous les messages bitcoins doivent avoir un entête de ce type

 
| Option | Description |
| ------:| -----------:|
| data   | path to data files to supply the data that will be passed into templates. |
| engine | engine to be used for processing templates. Handlebars is the default. |
| ext    | extension to be used for dest files. |
