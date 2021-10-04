CREATE TABLE `user`(
    `login` varchar(100),
    `avatar` varchar(255),
    `email` varchar(100),
    `name` varchar(100),
    `token` varchar(255),

    PRIMARY KEY (`login`)
)

CREATE TABLE `user_roles`(
    `user_login` varchar(100),
    `roles` varchar(255)
)