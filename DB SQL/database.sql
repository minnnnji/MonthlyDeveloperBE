CREATE TABLE `user`(
    `email` varchar(100),
    `password` varchar(100),

    PRIMARY KEY (`email`)
)

CREATE TABLE `user_roles`(
    `user_id` varchar(100),
    `roles` varchar(255)
)