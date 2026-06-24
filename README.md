# 🚂 Railway Optimization Models

#FRENCH VERSION ~~ VERSION FRANCAISE

## 📌 Contexte & Objectif
Ce dépôt regroupe une série de modèles d'optimisation basés sur la Programmation par Contraintes (Constraint Programming). L'objectif est de modéliser et de résoudre des problèmes complexes liés à la logistique, à la planification et à l'exploitation des réseaux de transport ferroviaire.

## 🧮 Modèles Implémentés et Problématiques

### 1. 🔧 Affectation des Équipes de Maintenance (`AffectationMaintenance.java`)
**Problématique :** Optimiser l'allocation des équipes sur les différentes tâches d'entretien des rames. L'algorithme doit respecter les contraintes de temps, éviter la surcharge simultanée des terminaux et minimiser le temps total d'immobilisation du matériel.

### 2. 📦 Optimisation du Chargement de Fret (`OptimisationFret.java`)
**Problématique :** Maximiser la rentabilité d'un train de marchandises en sélectionnant de manière optimale les conteneurs à charger, tout en respectant strictement les contraintes de capacité et de poids maximum autorisé par convoi.

### 3. ⏱️ Ordonnancement des Manœuvres (`Ordonnancement...java`)
**Problématique :** Planifier l'ordre de passage et l'occupation des ressources (voies, quais) pour une série de trains. Le modèle gère les contraintes de précédence chronologique pour éviter les goulots d'étranglement sur le réseau.

### 4. 🗺️ Routage des Inspections Réseau (`RoutageInspections.java`)
**Problématique :** Déterminer le parcours le plus efficace pour une équipe d'inspection devant visiter plusieurs points critiques du réseau. L'objectif est de minimiser la distance totale et les coûts de déplacement.

### 5. 💰 Affectation et Réduction des Coûts (`AffectationCouts.java`)
**Problématique :** Affecter les ressources disponibles (locomotives ou conducteurs) aux différents trajets requis par le plan de transport, dans le but de réduire au minimum les coûts opérationnels globaux associés à ces affectations.

## 🛠️ Stack Technique
* **Langage :** Java
* **Solveur :** Choco-solver (Bibliothèque Open Source de Programmation par Contraintes)


#ENGLISH VERSION ~~ VERSION ANGLAISE
# 🚂 Railway Optimization Models

## 📌 Context & Objective
This repository contains a series of optimization models based on Constraint Programming. The objective is to model and solve complex problems related to the logistics, scheduling, and operations of railway transport networks.

## 🧮 Implemented Models and Business Problems

### 1. 🔧 Maintenance Team Scheduling (`AffectationMaintenance.java`)
**Business Problem:** Optimize the allocation of teams to various train maintenance tasks. The algorithm must respect time constraints, avoid simultaneous terminal overload, and minimize the total downtime of the equipment.

### 2. 📦 Freight Loading Optimization (`OptimisationFret.java`)
**Business Problem:** Maximize the profitability of a freight train by optimally selecting the containers to load, while strictly respecting the capacity and maximum weight constraints per convoy.

### 3. ⏱️ Operations Scheduling (`Ordonnancement...java`)
**Business Problem:** Schedule the passing order and resource occupation (tracks, platforms) for a series of trains. The model handles chronological precedence constraints to avoid bottlenecks on the network.

### 4. 🗺️ Network Inspection Routing (`RoutageInspectio...java`)
**Business Problem:** Determine the most efficient route for an inspection team that needs to visit several critical points on the network. The objective is to minimize the total distance and travel costs.

### 5. 💰 Cost Assignment and Reduction (`AffectationCouts.java`)
**Business Problem:** Assign available resources (locomotives or drivers) to the various routes required by the transport plan, with the goal of minimizing the overall operational costs associated with these assignments.

## 🛠️ Tech Stack
* **Language:** Java
* **Solver:** Choco-solver (Open Source Constraint Programming Library)
