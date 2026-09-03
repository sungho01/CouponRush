CREATE TABLE member(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    member_role VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    birth_date DATE NOT NULL,
    created_at DATETIME NOT NULL
);

CREATE TABLE coupon(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    total_quantity INT NOT NULL,
    remaining_quantity INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at DATETIME NOT NULL,

    CHECK(total_quantity > 0),
    CHECK (remaining_quantity >= 0),
    CHECK (remaining_quantity <= total_quantity)
);

CREATE TABLE member_coupon(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    status VARCHAR(255) NOT NULL,
    issued_at DATETIME NOT NULL,
    used_at DATETIME,

    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id),
    UNIQUE (member_id, coupon_id)
);