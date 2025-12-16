# Document de design

Ceci est le document de template pour décrire l'architecture de votre programme. Vous pouvez le modifier à votre guise, mais assurez-vous de répondre à toutes les questions posées.
***Suivant certaines architectures, certaines des questions peuvent ne pas être pertinentes. Dans ce cas, vous pouvez les ignorer.***
Vous pouvez utiliser autant de diagrammes que vous le souhaitez pour expliquer votre architecture.
Nous vous conseillons d'utiliser le logiciel PlantUML pour générer vos diagrammes.

## Schéma général

Notre programme de gestion et d'export de calendriers ICS (iCalendar) structurée selon une **architecture orientée objet** se compose de **5 composants majeurs** qui interagissent de manière découplée :

#### 1.Le Modèle de données
- **Entry** (classe abstraite) : Représente une entrée générique de calendrier avec les propriétés communes.
- **Event** : Hérite de Entry, représente un événement avec date de début/fin, lieu et description.
- **Todos** : Hérite de Entry, représente une tâche avec date d'échéance et statut.

#### 2. Le Parser
- **Parser** : Responsable de la lecture et du parsing des fichiers .ics. Il extrait les données brutes (lignes DTSTART, SUMMARY, etc.) et crée les objets `Event` et `Todos` correspondants.
- Utilise un `Calendar` pour stocker les entrées parsées.

#### 3. **Le Gestionnaire de calendrier**
- **Calendar** : Composant central qui contient une liste d'entrées (`List<Entry> items). Il fournit trois fonctionnalités principales :
  - **Ajout** : addItem(Entry)
  - **Filtrage** : `filterCalendar(List<String> options)` utilise des `Function<List<String>, DatePair>` pour les événements et des `Predicate<Todos>` pour les tâches.
  - **Tri** : sortCalendar() organise les entrées par ordre chronologique.

#### 4. **Le système d'export **
- **`Exporter`** (interface) : Définit la méthode `export(Calendar, Writer)`.
- **Implémentations concrètes** :
  - `TextExporter` : Export au format texte lisible.
  - `HtmlExporter` : Export au format HTML avec balises.
  - `IcsExporter` : Export au format ICS conforme au normes de ce type de fihcier.
- Ce pattern permet de changer dynamiquement le format de sortie sans modifier la logique métier.

#### 5. **La Factory **
- **`ExporterFactory`** :
  - Instancie les exportateurs via une `Map<String, Exporter>` qui associe les options (html, text, ics) aux objets correspondants.
  - Gére la destination de sortie (terminal avec `PrintWriter(System.out)` ou fichier avec `FileWriter`).
  - Méthode principale : `outputHandler(List<String> options, Calendar cal)`.


## Utilisation du polymorphisme

1. Polymorphisme sur les entrées de calendrier (Hiérarchie Entry)
La classe abstraite Entry sert de type générique pour toutes les entrées de calendrier. Les classes Event et Todos héritent de Entry et implémentent leur propre comportement.

2. Polymorphisme sur les stratégies d'export
L'interface Exporter définit un contrat unique : export(Calendar, Writer). Les trois implémentations (TextExporter, HtmlExporter, IcsExporter) fournissent chacune une stratégie différente pour générer la sortie.

3. Polymorphisme sur la destination de sortie
Le type java.io.Writer de la bibliothèque standard Java est utilisé pour abstraire la destination de l'écriture. Nos exportateurs acceptent un Writer sans savoir s'il écrit vers un fichier ou vers la console.


## Utilisation de la déléguation

1. Délégation du filtrage (Calendar → Lambdas fonctionnelles)
La classe Calendar ne contient pas directement la logique de filtrage pour chaque option (-today, -from, -completed, etc.). Elle délègue cette responsabilité à des objets fonctionnels stockés dans des HashMap.

2. Délégation de l'export (ExporterFactory → Exporter)
ExporterFactory a la responsabilité de choisir l'exportateur et de préparer le Writer mais elle n'écrit jamais directement de HTML ou de texte. Elle délègue cette tâche à l'objet Exporter approprié.

## Utilisation de l'héritage

1. Hiérarchie des entrées de calendrier (Entry → Event / Todos)
Relation "est un" : Un Event est une Entry, un Todos est une Entry.
Les propriétés communes sont définies une seule fois dans Entry, évitant la duplication.

2. Choix de **NE PAS** utiliser l'héritage (Exporter)
Les Exportateurs n'héritent PAS les uns des autres car :

Les trois exportateurs n'ont aucun code commun à partager.
Ils implémentent le même contrat (Exporter interface) mais avec des logiques totalement différentes.

## Utilisation de la généricité

1. Stockage typé des entrées de calendrier (`List<Entry> items`)
Type safety : Le compilateur garantit que seuls des objets de type Entry (ou ses sous-classes Event, Todos) peuvent être ajoutés.

2. Types fonctionnels génériques
a. `Function<T, R>` pour les filtres d'événements
b. `Predicate<T>` pour les filtres de todos

La généricité dans notre programme garantit :

Sécurité des types : Le compilateur détecte les erreurs avant l'exécution.

Lisibilité : Pas de casts manuels partout dans le code.

## Utilisation des exceptions

Quelques exemples d'utilisation des exceptions dans notre code:

1. Gestion des exceptions IOException

a. Propagation de l'exception dans la méthode export() de Exporter (throws):

En déclarant throws IOException, on propage l'exception pour que la classe appelante (Main) la gère. Ainsi, l'exportateur se concentre sur le formatage, pas sur la gestion des erreurs système.



b. Gestion direct avec try-catch dans la méthode parse() de Parser:

Traçabilité de l'erreur : on pourra afficher l'emplacement exact de l'erreur, facilitant le débougage.

Si le fichier est introuvable ou illisible, cela se produit dès new FileReader(this.path). À ce stade, le Parser ne peut rien faire : il n'a pas de fichier alternatif à essayer.

