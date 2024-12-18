# **Système de Gestion des Réservations d’Hôtels**

Ce projet implémente un **système de gestion des réservations d'hôtels** avec un **backend multi-technologies** (REST, SOAP, GraphQL, gRPC) basé sur **Spring Boot** et **H2** pour la base de données en mémoire. Le **frontend mobile** est développé avec **Jetpack Compose** pour Android.

---

## **Architecture du Projet**

### **1. Backend**
- **Technologies :** 
   - REST & SOAP : Spring Boot.  
   - GraphQL : Apollo Server intégré.  
   - gRPC : Spring Boot avec Java gRPC.  
- **Base de Données :** H2 (en mémoire).  
- **Fonctionnalités CRUD :**
   - Créer, consulter, modifier et supprimer une réservation.  

### **2. Frontend Mobile**
- **Technologie :** Jetpack Compose (Kotlin).  
- **Compatibilité :** Android API niveau 21+.  
- **Fonctionnalités :**
   - Formulaire pour créer une réservation.  
   - Visualisation, modification et suppression des réservations.

---

## **Prérequis**

### **Outils Nécessaires**
- **Java 17+** (pour Spring Boot et gRPC).
- **Maven** (pour la gestion des dépendances).
- **Android Studio (version 2022.1+ avec Jetpack Compose).**

---


**Interfaces :**
![Ajouter un titre](https://github.com/user-attachments/assets/1d544b6d-14ca-4116-a49d-4b82eb29387f)


---

## **Résumé de l'Étude de Cas :**

Le but de cette étude de cas est de comparer différentes technologies d'API pour la gestion d'un système de réservation d'hôtels. Le système doit permettre aux utilisateurs de créer, consulter, modifier et supprimer des réservations d'hôtels via des appels API. Pour cela, nous allons tester quatre technologies populaires : **REST**, **SOAP**, **GraphQL**, et **gRPC**. Ces technologies seront évaluées sous plusieurs angles : **performances**, **scalabilité**, **simplicité d'implémentation**, **sécurité**, et **flexibilité**. L'étude vise à déterminer quelle technologie est la plus adaptée pour gérer des millions de requêtes simultanées, tout en assurant une consommation minimale des ressources, une latence réduite et une capacité à évoluer avec des volumes de données variables. Les tests sont réalisés en utilisant des scénarios réels, avec différentes tailles de messages et charges simultanées, et les résultats obtenus seront analysés pour fournir des recommandations spécifiques selon les cas d’usage. Cette comparaison permettra de faire un choix éclairé sur la technologie la mieux adaptée aux besoins d'une plateforme de réservation d’hôtels moderne.

---

Video Demo des Test:

https://github.com/user-attachments/assets/e2c3c20a-6bfe-49b7-a04f-6f3a9d2b53ad

Video Demo des Test client kotlin volley :

https://github.com/user-attachments/assets/df7f7e64-52e5-4b22-8303-a4cf2cc52e3b



---

## **Auteurs**

### **Projet réalisé par :**

- LAHLYAL Ahmed Moubarak
- DOUIDY Sifeddine
- FIHRI Yasmine

### **Encadré par :**
Pr. Mohamed Lachgar
