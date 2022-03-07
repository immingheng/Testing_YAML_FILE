-- Delete database if it already exists
drop database if exists mycuteshop;

-- Create database called inventory
create database mycuteshop;

-- use inventory
use mycuteshop;

-- CREATE TABLE FOR USER
create table user(
    user_id int auto_increment not null,
    -- Allows duplicate name but not email.
    name varchar(64) not null, 
    email varchar(256) not null,
    shopee_shop_id int not null,
    primary key (user_id, email,shopee_shop_id)
);

-- CREATE UNIQUE INDEX TO PREVENT DUPLICATE ENTRY
create unique index shopee_shop_id on user(shopee_shop_id);

-- create table with all fields of a listing
-- TODO : THIS IS INCOMPLETE!
create table shopee(
	product_id int not null,
    shopee_shop_id int not null,
    image_thumbnail varchar(256),
    -- SHOPEE RESTRICT PRODUCT NAME TO 100 CHAR
    product_name varchar(100),
    -- SHOPEE RESTRICT DESCRIPTION TO 1000 CHAR
    product_description varchar(1000),
    quantity int not null,
    price float not null,
    primary key(product_id),
    constraint fk_shopee_id foreign key(shopee_shop_id) references user(shopee_shop_id)
);

-- CREATE UNIQUE INDEX TO PREVENT DUPLICATE ENTRY
create unique index product_name on shopee(product_name);
