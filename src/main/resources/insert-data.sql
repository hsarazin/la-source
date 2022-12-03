-- ATTENTION : Bien penser aux quotes simples pour les valeurs de type chaines...
insert into ASSOCIATION (NOM) values('WWF'),('Greenpeace');

insert into MEMBER (LOGIN,PASSWORD,ISCONTACT,ASSOCIATION_ID) values('alice','alice','false',null),('bob','bob','true',1),('serge','serge','true',2);

insert into POST (CATEGORIE,NOM,ASSOCIATION_ID) values('jouet','ballon de foot', null),('jouet', 'balle de tennis ', 1),('jouet', 'ballon de rugby', 2);