use code_type_db;

insert into code_type(id, name)
SELECT * FROM (SELECT 1, 'CATEGORY') AS tmp
WHERE NOT EXISTS (
        SELECT name FROM code_type WHERE name = 'CATEGORY'
    ) LIMIT 1;


insert into code_type(id, name)
SELECT * FROM (SELECT 2, 'TRANSACTION_TYPE') AS tmp
WHERE NOT EXISTS (
        SELECT name FROM code_type WHERE name = 'TRANSACTION_TYPE'
    ) LIMIT 1;

insert into code_type(id, name)
SELECT * FROM (SELECT 3, 'STATUS') AS tmp
WHERE NOT EXISTS (
        SELECT name FROM code_type WHERE name = 'STATUS'
    ) LIMIT 1;

insert into code_value(id, name, code_type_id)
SELECT * FROM (SELECT 1, 'PAYNOW', (select id from code_type where name='CATEGORY')) AS tmp
WHERE NOT EXISTS (
        SELECT name FROM code_value WHERE name = 'PAYNOW'
    ) LIMIT 1;

insert into code_value(id, name, code_type_id)
SELECT * FROM (SELECT 2, 'ATM', (select id from code_type where name='CATEGORY')) AS tmp
WHERE NOT EXISTS (
        SELECT name FROM code_value WHERE name = 'ATM'
    ) LIMIT 1;


insert into code_value(id, name, code_type_id)
SELECT * FROM (SELECT 3, 'SUCCESS', (select id from code_type where name='STATUS')) AS tmp
WHERE NOT EXISTS (
        SELECT name FROM code_value WHERE name = 'SUCCESS'
    ) LIMIT 1;

insert into code_value(id, name, code_type_id)
SELECT * FROM (SELECT 4, 'PENDING', (select id from code_type where name='STATUS')) AS tmp
WHERE NOT EXISTS (
        SELECT name FROM code_value WHERE name = 'PENDING'
    ) LIMIT 1;

insert into code_value(id, name, code_type_id)
SELECT * FROM (SELECT 5, 'FAILURE', (select id from code_type where name='STATUS')) AS tmp
WHERE NOT EXISTS (
        SELECT name FROM code_value WHERE name = 'FAILURE'
    ) LIMIT 1;
