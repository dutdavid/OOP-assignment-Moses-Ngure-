CREATE TABLE `users` (
  `sin` int(9) NOT NULL,
  `fullName` varchar(64) DEFAULT NULL,
  `email` varchar(64) UNIQUE,
  `password` varchar(256) DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `isAdmin` tinyint DEFAULT 0,
  PRIMARY KEY (`sin`, `email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `accounts` (
  `accountNum` varchar(64),
  `account` longblob,
  PRIMARY KEY (`accountNum`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
