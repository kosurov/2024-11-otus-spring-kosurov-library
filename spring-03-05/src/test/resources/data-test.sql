insert into author (id, fullname) values (1, 'Агата Кристи');
insert into author (id, fullname) values (2, 'Дэн Браун');
insert into author (id, fullname) values (3, 'Михаиил Афанасьевич Булгаков');

insert into genre (id, name) values (1, 'Детектив');
insert into genre (id, name) values (2, 'Роман');

insert into book (id, title, authorid, genreid) values (1, 'Убийство в восточном экспрессе', 1, 1);
insert into book (id, title, authorid, genreid) values (2, 'Ангелы и демоны', 2, 2);
insert into book (id, title, authorid, genreid) values (3, 'Мастер и Маргарита', 3, 2);

insert into comment (id, text) values (1, 'Комментарий 1');
insert into comment (id, text) values (2, 'Комментарий 2');
insert into comment (id, text) values (3, 'Комментарий 3');
