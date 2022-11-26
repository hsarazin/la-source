-- ATTENTION : Bien penser aux quotes simples pour les valeurs de type chaines...
insert into ASSOCIATION (NOM) values('wwf'),('greenpeace');

insert into MEMBER (LOGIN,PASSWORD,ISCONTACT) values('alice','alice','false'),('bob','bob','true');

insert into POST (CATEGORIE,NOM) values('jouet','ballon de foot'),('jouet', 'balle de tennis '),('jouet', 'ballon de rugby')