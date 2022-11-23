# La source

![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/apache/maven.svg?label=License)
```mermaid
classDiagram
    
    Association "1" -- "n" Membre : membre
    Association "1" -- "1" Membre : admin
    Membre "0..n"-- "0..n" Post : membre
    Membre "0..n" -- "0..n" Post : admin
    
    class Association{
        int id
        String nom
        List~String~ membres
        fk int idPersonneContact
    }
    
    class Membre{
        int id
        String nom
        String prenom
        fk int idAssociation
        bool contact
        MembrePropose()
        AdminPropose()
        AdminValidate()
        AdminAccept()
    }
    
    class Post{
        int id
        LocalDateTime date
        fk int idAssiciation
        List~Association~ demandeAsso
        List~Association~ isValidatedDemand
        List~Association~ isAcceptedDemand
    }
```
```mermaid
classDiagram
    Association "1" -- "1..n" Membre
    Association "1" --> "1" PersonneContact
    PersonneContact --|> Membre
    Offre --> Demande
    PersonneContact --> Demande
    Offre "0...n" --> "1" Association: propose
    class Association{
        int id
        String nom
        fk int idPersonneContact
    }
    class PersonneContact{
        fk int idMembre
        fk int idAssociation
    }
    class Membre{
        int id
        String nom
        String prenom
        fk int idAssociation
        proposerOffre()
        demander()
    }
    class Offre{
        int id
        String nom
        List~String~ Categorie
        LocalDatetime date
        fk int idAssociation
    }
    class Demande{
        int id
        LocalDatetime date
        bool validatedByContact
        fk int idOffre
        fk int idAssocation
    }
```

