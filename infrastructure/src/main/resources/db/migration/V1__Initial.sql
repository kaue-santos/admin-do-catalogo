
CREATE TABLE category (
    id varchar(36) NOT NULL PRIMARY KEY,
    name varchar(255) NOT NULL,
    description varchar(4000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL
);