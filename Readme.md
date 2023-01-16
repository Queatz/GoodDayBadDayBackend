Build

```shell
./gradlew shadowJar
```

Output is at `build/libs/Good Day Bad Day Backend-all.jar`

Configure Arango
--------------

Install from
https://www.arangodb.com/download-major/ubuntu/

```shell
echo '{"default":"en_US.UTF-8"}' > /var/lib/arangodb3/LANGUAGE
service arangodb3 restart

arangosh --server.username root --server.password root
arangosh> const users = require('@arangodb/users')
arangosh> users.save('gooddaybadday', 'gooddaybadday')
arangosh> db._createDatabase('gooddaybadday')
arangosh> users.grantDatabase('gooddaybadday', 'gooddaybadday', 'rw')
```

Deploy
=====

```shell
apt update
apt install certbot nginx default-jre python3-certbot-nginx
certbot
```

## HTTP -> HTTPS

1. Configure Nginx

2. Replace the contents of `/etc/nginx/sites-enabled/default` with the following

```
server {
    server_name _;
    listen 80;

    location / {
        proxy_pass http://localhost:9090;
    }
}
```

3. Finally

```shell
service nginx restart
```

Run
===

```shell
#! /bin/bash
nohup java -jar *.jar > log.txt 2> errors.txt < /dev/null &
```
