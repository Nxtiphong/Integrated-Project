CREATE DATABASE if not exists int221_tt2;

USE int221_tt2;

CREATE TABLE brand (
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL CHECK (LENGTH(TRIM(name)) > 0) ,
 websiteUrl VARCHAR(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci CHECK (websiteUrl IS NULL OR (LENGTH(TRIM(websiteUrl)) > 0)),
  isActive BOOLEAN,
  countryOfOrigin VARCHAR(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci CHECK (countryOfOrigin IS NULL OR (LENGTH(TRIM(countryOfOrigin)) > 0)),
  createdOn DATETIME NOT NULL,
  updatedOn DATETIME NOT NULL,
  CHECK (updatedOn >= createdOn)
);

CREATE TABLE saleItem (
  id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL CHECK (
        LENGTH(TRIM(model)) > 0
    ),
	brand_id INT NOT NULL,
    description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL CHECK (
        LENGTH(TRIM(description)) > 0
    ),
    price INT NOT NULL,
    ramGb INT,
    screenSizeInch DECIMAL(3,1),
    storageGb INT,
    color TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci CHECK (
        color IS NULL OR LENGTH(TRIM(color)) > 0
    ),
    quantity INT NOT NULL DEFAULT 1,
    createdOn DATETIME NOT NULL,
    updatedOn DATETIME NOT NULL,
    CHECK (updatedOn >= createdOn),
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE CASCADE
);

INSERT INTO Brand (id, name, countryOfOrigin, websiteUrl, isActive, createdOn, updatedOn)
VALUES
(1, 'Samsung', 'South Korea', 'https://www.samsung.com', 1, NOW(), NOW()),
(2, 'Apple', 'United States', 'https://www.apple.com', 1, NOW(), NOW()),
(3, 'Xiaomi', 'China', 'https://www.mi.com', 1, NOW(), NOW()),
(4, 'Huawei', 'China', 'https://www.huawei.com', 1, NOW(), NOW()),
(5, 'OnePlus', 'China', 'https://www.oneplus.com', 1, NOW(), NOW()),
(6, 'Sony', 'Japan', 'https://www.sony.com', 1, NOW(), NOW()),
(7, 'LG', 'South Korea', 'https://www.lg.com', 1, NOW(), NOW()),
(8, 'Nokia', 'Finland', 'https://www.nokia.com', 0, NOW(), NOW()),
(9, 'Motorola', 'United States', 'https://www.motorola.com', 0, NOW(), NOW()),
(10, 'OPPO', 'China', 'https://www.oppo.com', 1, NOW(), NOW()),
(11, 'Vivo', 'China', 'https://www.vivo.com', 1, NOW(), NOW()),
(12, 'ASUS', 'Taiwan', 'https://www.asus.com', 1, NOW(), NOW()),
(13, 'Google', 'United States', 'https://store.google.com', 1, NOW(), NOW()),
(14, 'Realme', 'China', 'https://www.realme.com', 1, NOW(), NOW()),
(15, 'BlackBerry', 'Canada', 'https://www.blackberry.com', 1, NOW(), NOW()),
(16, 'HTC', 'Taiwan', 'https://www.htc.com', 1, NOW(), NOW()),
(17, 'ZTE', 'China', 'https://www.zte.com', 1, NOW(), NOW()),
(18, 'Lenovo', 'China', 'https://www.lenovo.com', 1, NOW(), NOW()),
(19, 'Honor', 'China', 'https://www.hihonor.com', 1, NOW(), NOW()),
(20, 'Nothing', 'United Kingdom', 'https://nothing.tech', 1, NOW(), NOW());

INSERT INTO SaleItem (
    id, brand_id, model, description, quantity, price,
    screenSizeInch, ramGb, storageGb, color,
    createdOn, updatedOn
)
VALUES
(1, 2, 'iPhone 14 Pro Max', 'ไอโฟนเรือธงรุ่นล่าสุด มาพร้อม Dynamic Island จอใหญ่สุดในตระกูล กล้องระดับโปร', 5, 42900, 6.7, 6, 512, 'Space Black', NOW(), NOW()),
(2, 2, 'iPhone 14', 'ไอโฟนรุ่นใหม่ล่าสุด รองรับ 5G เร็วแรง ถ่ายภาพสวยทุกสภาพแสง', 8, 29700, 6.1, 6, 256, 'Midnight', NOW(), NOW()),
(3, 2, 'iPhone 13 Pro', 'ไอโฟนรุ่นโปร จอ ProMotion 120Hz กล้องระดับมืออาชีพ', 3, 33000, 6.1, 6, 256, 'Sierra Blue', NOW(), NOW()),
(7, 2, 'iPhone SE 2022', 'Budget-friendly model', 15, 14190, 4.7, 4, 64, 'Starlight', NOW(), NOW()),
(8, 2, 'iPhone 14 Plus', 'iPhone 14 Plus 128GB สี Starlight เครื่องศูนย์ไทย โมเดล TH แบต 100% มีกล่องครบ ประกันศูนย์ถึง พ.ย. 68 ส่งฟรี', 7, 29700, 6.7, 6, 256, 'Blue', NOW(), NOW()),
(16, 1, 'Galaxy S23 Ultra', 'Samsung Galaxy S23 Ultra 512GB สีดำปีศาจ สภาพนางฟ้า 99% ไร้รอย แถมเคสแท้ แบตอึดสุดๆ รองรับปากกา S-Pen', 1, 32900, 7.6, NULL, 512, NULL, NOW(), NOW());