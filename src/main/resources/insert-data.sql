-- ATTENTION : Bien penser aux quotes simples pour les valeurs de type chaines...
insert into ASSOCIATION (NOM) values('wwf'),('greenpeace');

insert into MEMBER (LOGIN,PASSWORD,ISCONTACT,ASSOCIATION_ID) values('alice','alice','false',null),('bob','bob','true',1),('serge','serge','false',2);

insert into POST (CATEGORIE,NOM) values('jouet','ballon de foot'),('jouet', 'balle de tennis '),('jouet', 'ballon de rugby')